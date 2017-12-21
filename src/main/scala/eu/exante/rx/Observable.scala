package eu.exante.rx

import io.reactivex.disposables.Disposable
import io.reactivex.{ObservableSource, ObservableTransformer}

import scala.concurrent.duration.TimeUnit

class Observable[T](private val u: io.reactivex.Observable[T]) extends AnyVal {

  def asJava: io.reactivex.Observable[T] = {
    u
  }

  def asScala: Observable[T] = {
    this
  }

  def compose[R](composer: ObservableTransformer[T, R]): Observable[R] = {
    u.compose[R](composer)
  }

  def distinctUntilChanged: Observable[T] = {
    u.distinctUntilChanged()
  }

  def filter(f: T => Boolean): Observable[T] = {
    u.filter(new io.reactivex.functions.Predicate[T] {
      def test(t: T): Boolean = f(t)
    })
  }

  def flatMap[R](f: T => Observable[R]): Observable[R] = {
    u.flatMap[R](new io.reactivex.functions.Function[T, ObservableSource[R]] {
      def apply(t: T): ObservableSource[R] = f(t)
    })
  }

  def map[R](f: T => R): Observable[R] = {
    u.map[R](new io.reactivex.functions.Function[T, R] {
      def apply(t: T): R = f(t)
    })
  }

  def scan[R](initialValue: R)(f: (R, T) => R): Observable[R] = {
    u.scan[R](initialValue, new io.reactivex.functions.BiFunction[R, T, R] {
      def apply(t1: R, t2: T): R = f(t1, t2)
    })
  }

  def startWith(t: T): Observable[T] = {
    u.startWith(t)
  }

  def subscribe(f: T => Unit): Disposable = {
    u.subscribe { p => f(p) }
  }

  def switchMap[R](f: T => Observable[R]): Observable[R] = {
    u.switchMap[R](new io.reactivex.functions.Function[T, io.reactivex.Observable[R]] {
      def apply(t: T): io.reactivex.Observable[R] = f(t)
    })
  }

  def takeUntil(other: Observable[_]): Observable[T] = {
    u.takeUntil(other)
  }

  def throttleFirst(delay: Long, unit: TimeUnit): Observable[T] = {
    u.throttleFirst(delay, unit)
  }

  def throttleFirstAfter(after: Long, delay: Long, unit: TimeUnit): Observable[T] = {
    val throttling = Observable.fromArray(()).delay(after, unit)
    val u1 = u.takeUntil(throttling)
    val u2 = u.skipUntil(throttling)
    u1 mergeWith u2.throttleFirst(delay, unit)
  }

  def withLatestFrom[U, R](other: Observable[U])(f: (T, U) => R): Observable[R] = {
    u.withLatestFrom[U, R](other, new io.reactivex.functions.BiFunction[T, U, R] {
      def apply(t1: T, t2: U): R = f(t1, t2)
    })
  }
}

object Observable {

  def combineLatest[T1, T2](source1: Observable[T1], source2: Observable[T2]): Observable[(T1, T2)] = {
    io.reactivex.Observable.combineLatest[T1, T2, (T1, T2)](source1, source2, (_, _))
  }

  def combineLatest[T1, T2, T3, R](source1: Observable[T1],
                                   source2: Observable[T2],
                                   source3: Observable[T3]): Observable[(T1, T2, T3)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, (T1, T2, T3)](
      source1, source2, source3, (_, _, _))
  }

  def combineLatest[T1, T2, T3, T4](source1: Observable[T1],
                                    source2: Observable[T2],
                                    source3: Observable[T3],
                                    source4: Observable[T4]): Observable[(T1, T2, T3, T4)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, (T1, T2, T3, T4)](
      source1, source2, source3, source4, (_, _, _, _))
  }

  def concat[T](source1: Observable[T], source2: Observable[T]): Observable[T] = {
    io.reactivex.Observable.concat(source1, source2)
  }

  def concatArrayEager[T](sources: Observable[T]*): Observable[T] = {
    io.reactivex.Observable.concatArrayEager(sources.map(_.asJava): _*)
  }

  def empty[T]: Observable[T] = {
    io.reactivex.Observable.empty[T]()
  }

  def fromArray[T](items: T*): Observable[T] = {
    io.reactivex.Observable.fromArray(items: _*)
  }

  def just[T](item1: T): Observable[T] = {
    io.reactivex.Observable.just[T](item1)
  }

  def interval(initial: Long, period: Long, unit: TimeUnit): Observable[Long] = {
    io.reactivex.Observable.interval(initial, period, unit).asScala
      .map[Long] { x => x }
  }

  def merge[T](source1: Observable[T], source2: Observable[T]): Observable[T] = {
    io.reactivex.Observable.merge(source1, source2)
  }

  def never[T]: Observable[T] = {
    io.reactivex.Observable.never[T]()
  }
}
