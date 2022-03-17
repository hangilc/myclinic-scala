package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLFormElement
import org.scalajs.dom.HTMLSpanElement

object TypeClasses:
  trait ElementProvider[T]:
    def getElement(t: T): HTMLElement

  object ElementProvider:
    given ElementProvider[HTMLElement] = ele => ele

  trait TriggerProvider[T]:
    def setTriggerHandler(t: T, handler: () => Unit): Unit

  object TriggerProvider:
    def by[T, U](f: T => U)(using
        uProvider: TriggerProvider[U]
    ): TriggerProvider[T] =
      new TriggerProvider[T]:
        def setTriggerHandler(t: T, handler: () => Unit): Unit =
          uProvider.setTriggerHandler(f(t), handler)

    def nop[T]: TriggerProvider[T] =
      new TriggerProvider[T]:
        def setTriggerHandler(t: T, handler: () => Unit): Unit =
          ()

    given TriggerProvider[HTMLButtonElement] with
      def setTriggerHandler(b: HTMLButtonElement, handler: () => Unit): Unit =
        b(onclick := handler)
    given TriggerProvider[HTMLAnchorElement] with
      def setTriggerHandler(a: HTMLAnchorElement, handler: () => Unit): Unit =
        a(onclick := handler)
    given TriggerProvider[HTMLFormElement] with
      def setTriggerHandler(f: HTMLFormElement, handler: () => Unit): Unit =
        f(onsubmit := handler)
    given selectionDataProvider[S, T]: TriggerProvider[Selection[S, T]] =
      new TriggerProvider[Selection[S, T]]:
        def setTriggerHandler(t: Selection[S, T], handler: () => Unit): Unit =
          t.addSelectEventHandler(_ => handler())

  trait GeneralTriggerProvider[T, Kind]:
    def setTriggerHandler(t: T, handler: () => Unit): Unit

  object GeneralTriggerProvider:
    def by[T, U, Kind](f: T => U)(using
        uTrigger: GeneralTriggerProvider[U, Kind]
    ): GeneralTriggerProvider[T, Kind] =
      new GeneralTriggerProvider[T, Kind]:
        def setTriggerHandler(t: T, handler: () => Unit): Unit =
          uTrigger.setTriggerHandler(f(t), handler)

    given genAnchorTrigger[Kind]
        : GeneralTriggerProvider[HTMLAnchorElement, Kind] =
      new GeneralTriggerProvider[HTMLAnchorElement, Kind]:
        def setTriggerHandler(t: HTMLAnchorElement, handler: () => Unit): Unit =
          t(onclick := (() => handler()))

  trait DataProvider[T, D]:
    def getData(t: T): D

    def map[E](f: D => E): DataProvider[T, E] =
      val self: DataProvider[T, D] = this
      new DataProvider[T, E]:
        def getData(t: T): E = f(self.getData(t))

  object DataProvider:
    def by[T, D, U](f: T => U)(using
        uProvider: DataProvider[U, D]
    ): DataProvider[T, D] =
      new DataProvider[T, D]:
        def getData(t: T): D = uProvider.getData(f(t))
    def by[T, D, U, E](f: T => U)(m: E => D)(using
        uProvider: DataProvider[U, E]
    ): DataProvider[T, D] =
      new DataProvider[T, D]:
        def getData(t: T): D =
          m(uProvider.getData(f(t)))

    given selectionDataProvider[S, T]
        : DataProvider[Selection[S, T], Option[T]] =
      new DataProvider[Selection[S, T], Option[T]]:
        def getData(t: Selection[S, T]): Option[T] = t.selected

  trait DataAcceptor[T, D]:
    def setData(t: T, d: D): Unit

    def contramap[C](f: C => D): DataAcceptor[T, C] =
      val self: DataAcceptor[T, D] = this
      new DataAcceptor[T, C]:
        def setData(t: T, c: C): Unit = self.setData(t, f(c))

  object DataAcceptor:
    def apply[T, D](): DataAcceptor[T, D] =
      new DataAcceptor[T, D]:
        def setData(t: T, d: D): Unit = ()
    def by[T, D, U](f: T => U)(using
        uAcceptor: DataAcceptor[U, D]
    ): DataAcceptor[T, D] =
      new DataAcceptor[T, D]:
        def setData(t: T, d: D): Unit = uAcceptor.setData(f(t), d)
    def by[T, D, U, E](f: T => U, m: D => E)(using
        uAcceptor: DataAcceptor[U, E]
    ): DataAcceptor[T, D] =
      new DataAcceptor[T, D]:
        def setData(t: T, d: D): Unit =
          uAcceptor.setData(f(t), m(d))
    
    import cats.*
    given [T, D]: Monoid[DataAcceptor[T, D]] with
      def empty: DataAcceptor[T, D] = DataAcceptor[T, D]()
      def combine(a: DataAcceptor[T, D], b: DataAcceptor[T, D]): DataAcceptor[T, D] =
        new DataAcceptor[T, D]:
          def setData(t: T, d: D): Unit =
            a.setData(t, d)
            b.setData(t, d)

    given DataAcceptor[HTMLSpanElement, String] with
      def setData(t: HTMLSpanElement, d: String): Unit = t(innerText := d)
    
    given DataAcceptor[HTMLAnchorElement, String] with
      def setData(t: HTMLAnchorElement, d: String): Unit = 
        t(innerText := d)

    given selectionDataAcceptor[T, D]: DataAcceptor[Selection[T, D], Option[D]]
      with
      def setData(t: Selection[T, D], opt: Option[D]): Unit =
        opt match {
          case Some(d) => t.select(d, false)
          case None => t.unselect()
        }
