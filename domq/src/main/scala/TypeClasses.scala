package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLFormElement
import org.scalajs.dom.HTMLSpanElement
import cats.*
import cats.syntax.all.*

object TypeClasses:
  trait ElementProvider[T]:
    def getElement(t: T): HTMLElement

  object ElementProvider:
    given ElementProvider[HTMLElement] = ele => ele

  trait TriggerProvider[T]:
    def setTriggerHandler(t: T, handler: () => Unit): Unit
    def contraInst[S](f: S => T): TriggerProvider[S] =
      val self: TriggerProvider[T] = this
      new TriggerProvider[S]:
        def setTriggerHandler(s: S, handler: () => Unit): Unit =
          self.setTriggerHandler(f(s), handler)

  object TriggerProvider:
    def apply[T](using prov: TriggerProvider[T]): TriggerProvider[T] = prov
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

    given [T]: TriggerProvider[Selection[T]] with
      def setTriggerHandler(s: Selection[T], handler: () => Unit): Unit =
        s.addSelectEventHandler(_ => handler())

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

    def contraInst[S](f: S => T): DataProvider[S, D] =
      val self: DataProvider[T, D] = this
      new DataProvider[S, D]:
        def getData(s: S): D = self.getData(f(s))

  object DataProvider:
    def apply[T, D](using prov: DataProvider[T, D]): DataProvider[T, D] = prov
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

    given [T]: DataProvider[Selection[T], Option[T]] with
      def getData(s: Selection[T]): Option[T] = s.marked

  trait DataAcceptor[T, D]:
    def setData(t: T, d: D): Unit

    def contramap[C](f: C => D): DataAcceptor[T, C] =
      val self: DataAcceptor[T, D] = this
      new DataAcceptor[T, C]:
        def setData(t: T, c: C): Unit = self.setData(t, f(c))

    def contraInst[S](f: S => T): DataAcceptor[S, D] =
      val self: DataAcceptor[T, D] = this
      new DataAcceptor[S, D]:
        def setData(s: S, d: D): Unit = self.setData(f(s), d)

  object DataAcceptor:
    def apply[T, D](using acc: DataAcceptor[T, D]): DataAcceptor[T, D] = acc
    def apply[T, D](f: (T, D) => Unit): DataAcceptor[T, D] =
      new DataAcceptor[T, D]:
        def setData(t: T, d: D): Unit = f(t, d)
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

    given [T, D]: Monoid[DataAcceptor[T, D]] with
      def empty: DataAcceptor[T, D] = DataAcceptor[T, D]((_, _) => ())
      def combine(
          a: DataAcceptor[T, D],
          b: DataAcceptor[T, D]
      ): DataAcceptor[T, D] =
        new DataAcceptor[T, D]:
          def setData(t: T, d: D): Unit =
            a.setData(t, d)
            b.setData(t, d)

    given DataAcceptor[HTMLSpanElement, String] with
      def setData(t: HTMLSpanElement, d: String): Unit = t(innerText := d)

    given DataAcceptor[HTMLAnchorElement, String] with
      def setData(t: HTMLAnchorElement, d: String): Unit =
        t(innerText := d)

    given [T]: DataAcceptor[Selection[T], Option[T]] with
      def setData(t: Selection[T], opt: Option[T]): Unit =
        opt match {
          case Some(d) => t.select(d)
          case None    => t.unmark()
        }
