package dev.fujiwara.domq.transition

type TransitionNode[S] = (S, Transition[S] => Unit) => Unit

sealed trait Transition[S]
case class GoForward[S](next: TransitionNode[S], state: S) extends Transition[S]
case class GoBack[S](state: S) extends Transition[S]
case class GoTo[S](
      next: TransitionNode[S],
      state: S,
      stackFun: List[TransitionNode[S]] => List[TransitionNode[S]] = (ss: List[TransitionNode[S]]) => ss
  ) extends Transition[S]
case class Exit[S]() extends Transition[S]

object Transition:
  def run[S](f: TransitionNode[S], state: S, stack: List[TransitionNode[S]],
    onExit: () => Unit): Unit =
    f(
      state,
      trans =>
        trans match {
          case GoForward(next, state) => run[S](next, state, f :: stack, onExit)
          case GoTo(next, state, sfun) => run(next, state, sfun(stack), onExit)
          case GoBack(state) =>
            if stack.isEmpty then onExit()
            else run(stack.head, state, stack.tail, onExit)
          case Exit() => onExit()
        }
    )

  
