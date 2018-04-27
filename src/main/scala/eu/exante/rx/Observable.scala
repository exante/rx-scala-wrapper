package eu.exante.rx

import java.util.concurrent.Callable

import io.reactivex.disposables.Disposable
import io.reactivex.functions.{Consumer, Function}
import io.reactivex.{ObservableSource, ObservableTransformer, Scheduler}

import scala.collection.JavaConverters._
import scala.concurrent.duration.TimeUnit
import scala.{Function => _}

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

  def doOnError(onError: Throwable => Unit): Observable[T] = {
    u.doOnError(t => onError(t))
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

  def observeOn(scheduler: Scheduler): Observable[T] = {
    u.observeOn(scheduler)
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

  def subscribe(onSuccess: T => Unit, onError: Throwable => Unit): Disposable = {
    u.subscribe(p => onSuccess(p), t => onError(t))
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

  def amb[T](sources: Iterable[_ <: ObservableSource[_ <: T]]): Observable[T] = {
    io.reactivex.Observable.amb[T](sources.asJava)
  }

  def ambArray[T](sources: ObservableSource[_ <: T]*): Observable[T] = {
    io.reactivex.Observable.ambArray[T](sources: _*)
  }

  def bufferSize: Int = {
    io.reactivex.Observable.bufferSize()
  }

  def combineLatest[T, R](bufferSize: Int, sources: ObservableSource[_ <: T]*)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    }, bufferSize, sources: _*)
  }

  def combineLatest[T, R](sources: Iterable[_ <: ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    })
  }

  def combineLatest[T, R](sources: Iterable[_ <: ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(sources.asInstanceOf[Array[T]])
    }, bufferSize)
  }

  def combineLatest[T, R](sources: Array[ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(sources.asInstanceOf[Array[T]])
    })
  }

  def combineLatest[T, R](sources: Array[ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(sources.asInstanceOf[Array[T]])
    }, bufferSize)
  }

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

  def combineLatest[T1, T2, T3, T4, T5](source1: Observable[T1],
                                        source2: Observable[T2],
                                        source3: Observable[T3],
                                        source4: Observable[T4],
                                        source5: Observable[T5]): Observable[(T1, T2, T3, T4, T5)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, (T1, T2, T3, T4, T5)](
      source1, source2, source3, source4, source5, (_, _, _, _, _))
  }

  def combineLatest[T1, T2, T3, T4, T5, T6](source1: Observable[T1],
                                            source2: Observable[T2],
                                            source3: Observable[T3],
                                            source4: Observable[T4],
                                            source5: Observable[T5],
                                            source6: Observable[T6]): Observable[(T1, T2, T3, T4, T5, T6)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, (T1, T2, T3, T4, T5, T6)](
      source1, source2, source3, source4, source5, source6, (_, _, _, _, _, _))
  }

  def combineLatest[T1, T2, T3, T4, T5, T6, T7](source1: Observable[T1],
                                                source2: Observable[T2],
                                                source3: Observable[T3],
                                                source4: Observable[T4],
                                                source5: Observable[T5],
                                                source6: Observable[T6],
                                                source7: Observable[T7]): Observable[(T1, T2, T3, T4, T5, T6, T7)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, (T1, T2, T3, T4, T5, T6, T7)](
      source1, source2, source3, source4, source5, source6, source7, (_, _, _, _, _, _, _))
  }

  def combineLatest[T1, T2, T3, T4, T5, T6, T7, T8](source1: Observable[T1],
                                                    source2: Observable[T2],
                                                    source3: Observable[T3],
                                                    source4: Observable[T4],
                                                    source5: Observable[T5],
                                                    source6: Observable[T6],
                                                    source7: Observable[T7],
                                                    source8: Observable[T8]): Observable[(T1, T2, T3, T4, T5, T6, T7, T8)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, T8, (T1, T2, T3, T4, T5, T6, T7, T8)](
      source1, source2, source3, source4, source5, source6, source7, source8, (_, _, _, _, _, _, _, _))
  }

  def combineLatest[T1, T2, T3, T4, T5, T6, T7, T8, T9](source1: Observable[T1],
                                                        source2: Observable[T2],
                                                        source3: Observable[T3],
                                                        source4: Observable[T4],
                                                        source5: Observable[T5],
                                                        source6: Observable[T6],
                                                        source7: Observable[T7],
                                                        source8: Observable[T8],
                                                        source9: Observable[T9]): Observable[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] = {
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, T8, T9, (T1, T2, T3, T4, T5, T6, T7, T8, T9)](
      source1, source2, source3, source4, source5, source6, source7, source8, source9, (_, _, _, _, _, _, _, _, _))
  }

  def combineLatestDelayError[T, R](sources: Array[ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatestDelayError[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    })
  }

  def combineLatestDelayError[T, R](bufferSize: Int, sources: ObservableSource[_ <: T]*)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatestDelayError[T, R](new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    }, bufferSize, sources: _*)
  }

  def combineLatestDelayError[T, R](sources: Array[ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatestDelayError[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    }, bufferSize)
  }

  def combineLatestDelayError[T, R](sources: Iterable[ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatestDelayError[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    })
  }

  def combineLatestDelayError[T, R](sources: Iterable[ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatestDelayError[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.asInstanceOf[Array[T]])
    }, bufferSize)
  }

  def concat[T](sources: Iterable[_ <: ObservableSource[_ <: T]]): Observable[T] = {
    io.reactivex.Observable.concat[T](sources.asJava)
  }

  def concat[T](sources: ObservableSource[_ <: ObservableSource[_ <: T]]): Observable[T] = {
    io.reactivex.Observable.concat[T](sources)
  }

  def concat[T](sources: ObservableSource[_ <: ObservableSource[_ <: T]], prefetch: Int): Observable[T] = {
    io.reactivex.Observable.concat[T](sources, prefetch)
  }

  def concat[T](source1: Observable[_ <: T], source2: Observable[_ <: T]): Observable[T] = {
    io.reactivex.Observable.concat(source1, source2)
  }

  def concat[T](source1: Observable[_ <: T],
                source2: Observable[_ <: T],
                source3: Observable[_ <: T]): Observable[T] = {
    io.reactivex.Observable.concat(source1, source2, source3)
  }

  def concat[T](source1: Observable[_ <: T],
                source2: Observable[_ <: T],
                source3: Observable[_ <: T],
                source4: Observable[_ <: T]): Observable[T] = {
    io.reactivex.Observable.concat(source1, source2, source3, source4)
  }

  def concatArray[T](sources: ObservableSource[_ <: T]*): Observable[T] = {
    io.reactivex.Observable.concatArray[T](sources: _*)
  }

  def concatArrayDelayError[T](sources: ObservableSource[_ <: T]*): Observable[T] = {
    io.reactivex.Observable.concatArrayDelayError[T](sources: _*)
  }

  def concatArrayEager[T](sources: ObservableSource[_ <: T]*): Observable[T] = {
    io.reactivex.Observable.concatArrayEager[T](sources: _*)
  }

  def concatArrayEager[T](maxConcurrency: Int, prefetch: Int, sources: ObservableSource[_ <: T]*): Observable[T] = {
    io.reactivex.Observable.concatArrayEager[T](maxConcurrency, prefetch, sources: _*)
  }

  def concatDelayError[T](sources: Iterable[_ <: ObservableSource[_ <: T]]): Observable[T] = {
    io.reactivex.Observable.concatDelayError[T](sources.asJava)
  }

  def concatDelayError[T](sources: ObservableSource[_ <: ObservableSource[_ <: T]]) = {
    io.reactivex.Observable.concatDelayError[T](sources)
  }

  def concatDelayError[T](sources: ObservableSource[_ <: ObservableSource[_ <: T]], prefetch: Int, tillTheEnd: Boolean): Observable[T] = {
    io.reactivex.Observable.concatDelayError[T](sources, prefetch, tillTheEnd)
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

  def intervalRange(start: Long, count: Long, initialDelay: Long, period: Long, unit: TimeUnit ): Observable[Long] = {
    io.reactivex.Observable.intervalRange(start, count, initialDelay, period, unit).map[Long] { x => x }
  }

  def merge[T](source1: Observable[T], source2: Observable[T]): Observable[T] = {
    io.reactivex.Observable.merge(source1, source2)
  }

  def never[T]: Observable[T] = {
    io.reactivex.Observable.never[T]()
  }

  def using[T, R](resourceFactory: => R, observableFactory: R => ObservableSource[T], disposeResource: R => Unit): Observable[T] = {
    val r: Callable[R] = () => resourceFactory
    val o: Function[R, ObservableSource[T]] = (t: R) => observableFactory(t)
    val d: Consumer[R] = (t: R) => disposeResource(t)

    io.reactivex.Observable.using(r, o, d)
  }
}
