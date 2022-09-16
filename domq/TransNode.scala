package dev.fujiwara.domq

abstract class TramsNode[T](state: T):
  var onGoForward: (tmpl: T => TransNode[T], newState: T) => Unit = ???
  var onGoReplacing: (tmpl: T => TransNode[T], newState: T) => Unit = ???
  var onGoBack: (newState: T) => Unit = ???
  var onGoExit: () => Unit = ???
  def init(): Unit
  def goForward(tmpl: T => TransNode[T], newState: T): Unit = onGoForward(tmpl, newState)
  def goReplacing(tmpl: T => TransNode[T], newState: T): Unit = onGoReplacing(tmpl, newState)
  def goBack(newState: T): Unit = onGoBack(newState)
  def goExit(): Unit = onGoExit()

class TrasnNodeRuntime[T]:
  private var stack = List[T => TransNode[T]] = List.empty

  def run(tmpl: T => TransNode[T], state: T, onExit: T => Unit): Unit =
    start(tmpl(state), onExit)
  
  private def start(t: TransNode[T], onExit: T => Unit): Unit =
    t.onGoForward = (tmpl, s) =>
      stack = tmpl :: stack
      start(tmpl(s))
    t.onGoReplacing = (tmpl, s) =>
      stack = tmpl :: (statck.tail)
      start(tmpl(s))
    t.onGoBack = (s) =>




