package dev.fujiwara.domq

import scala.concurrent.Future

abstract class TransNode[T](state: T):
  var onGoForward: (tmpl: T => TransNode[T], newState: T) => Unit = (_, _) => ???
  var onGoReplacing: (tmpl: T => TransNode[T], newState: T) => Unit = (_, _) => ???
  var onGoBack: (newState: T) => Unit = _ => ???
  var onGoExit: (newState: T) => Unit = _ => ???
  def init(): Unit
  def goForward(tmpl: T => TransNode[T], newState: T): Unit = onGoForward(tmpl, newState)
  def goForward(tmpl: T => TransNode[T]): Unit = goForward(tmpl, state)
  def goReplacing(tmpl: T => TransNode[T], newState: T): Unit = onGoReplacing(tmpl, newState)
  def goReplacing(tmpl: T => TransNode[T]): Unit = goReplacing(tmpl, state)
  def goBack(newState: T): Unit = onGoBack(newState)
  def goBack(): Unit = goBack(state)
  def goExit(): Unit = onGoExit(state)

class TransNodeRuntime[T]:
  private var stack: List[T => TransNode[T]] = List.empty

  def run(tmpl: T => TransNode[T], state: T, onExit: T => Unit): Unit =
    forward(tmpl, state, onExit)

  private def setupAndInit(t: TransNode[T], onExit: T => Unit): Unit =
    println("enter setupAndInit")
    t.onGoForward = (tmpl, s) => forward(tmpl, s, onExit)
    t.onGoReplacing = (tmpl, s) => replacing(tmpl, s, onExit)
    t.onGoBack = (s) => backward(s, onExit)
    t.onGoExit = (s) => onExit(s)
    t.init()

  private def forward(tmpl: T => TransNode[T], state: T, onExit: T => Unit): Unit =
    stack = tmpl :: stack
    val t = tmpl(state)
    setupAndInit(t, onExit)

  private def replacing(tmpl: T => TransNode[T], state: T, onExit: T => Unit): Unit =
    stack = tmpl :: (stack.tail)
    setupAndInit(tmpl(state), onExit)

  private def backward(state: T, onExit: T => Unit): Unit =
    stack = stack.tail
    stack.headOption.fold(onExit(state))(tmpl => {
      setupAndInit(tmpl(state), onExit)
    })


