package eu.exante.rx

import java.util.concurrent.Callable

import eu.exante.rx.Utils._
import io.reactivex.disposables.Disposable
import io.reactivex.functions.{Consumer, Function}
import io.reactivex.observables.GroupedObservable
import io.reactivex.{ObservableSource, ObservableTransformer, Scheduler}

import scala.collection.JavaConverters._
import scala.concurrent.duration.TimeUnit
import scala.reflect.ClassTag
import scala.{Function => _}

class Observable[T](private val u: io.reactivex.Observable[T]) extends AnyVal {

  def asJava: io.reactivex.Observable[T] = {
    u
  }

  def asScala: Observable[T] = {
    this
  }

  /* We don`t use s.c.i.List because j.u.List is interface, really implementation is unknown.
   * jlist.asScala return JListWrapper
   */
  def buffer[TOpening, TClosing](openingIndicator: ObservableSource[TOpening], closingIndicator: TOpening => ObservableSource[TClosing]): Observable[Seq[T]] = {
    val rxClosingIndicator: io.reactivex.functions.Function[TOpening, ObservableSource[TClosing]] = closingIndicator(_)
    u.buffer(openingIndicator, rxClosingIndicator)
      .asScala
      .map(_.asScala)
  }

  def compose[R](composer: ObservableTransformer[T, R]): Observable[R] = {
    u.compose[R](composer)
  }

  def delay(delay: Long, timeUnit: TimeUnit): Observable[T] = {
    u.delay(delay, timeUnit)
  }

  def delaySubscription(delay: Long, timeUnit: TimeUnit): Observable[T] = {
    u.delaySubscription(delay, timeUnit)
  }

  def distinctUntilChanged: Observable[T] = {
    u.distinctUntilChanged()
  }

  def doOnError(onError: Throwable => Unit): Observable[T] = {
    u.doOnError(t => onError(t))
  }

  def doOnNext(onNext: T => Unit): Observable[T] = {
    u.doOnNext(t => onNext(t))
  }

  def doOnComplete(onComplete: => Unit): Observable[T] = {
    u.doOnComplete(() => onComplete)
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

  def groupBy[K](keySelector: T => K): Observable[GroupedObservable[K, T]] = {
    u.groupBy[K](new io.reactivex.functions.Function[T, K] {
      override def apply(t: T): K = keySelector(t)
    })
  }

  def groupBy[K](keySelector: T => K, delayError: Boolean): Observable[GroupedObservable[K, T]] = {
    u.groupBy[K](new io.reactivex.functions.Function[T, K] {
      override def apply(t: T): K = keySelector(t)
    }, delayError)
  }

  def groupBy[K, V](keySelector: T => K, valueSelector: T => V): Observable[GroupedObservable[K, V]] = {
    val kS: io.reactivex.functions.Function[T, K] = (t: T) => keySelector(t)
    val vS: io.reactivex.functions.Function[T, V] = (t: T) => valueSelector(t)
    u.groupBy[K, V](kS, vS)
  }

  def groupBy[K, V](keySelector: T => K, valueSelector: T => V, delayError: Boolean): Observable[GroupedObservable[K, V]] = {
    val kS: io.reactivex.functions.Function[T, K] = (t: T) => keySelector(t)
    val vS: io.reactivex.functions.Function[T, V] = (t: T) => valueSelector(t)
    u.groupBy[K, V](kS, vS, delayError)
  }

  def groupBy[K, V](keySelector: T => K, valueSelector: T => V, delayError: Boolean, bufferSize: Int): Observable[GroupedObservable[K, V]] = {
    val kS: io.reactivex.functions.Function[T, K] = (t: T) => keySelector(t)
    val vS: io.reactivex.functions.Function[T, V] = (t: T) => valueSelector(t)
    u.groupBy[K, V](kS, vS, delayError, bufferSize)
  }

  def map[R](f: T => R): Observable[R] = {
    u.map[R](new io.reactivex.functions.Function[T, R] {
      def apply(t: T): R = f(t)
    })
  }

  def observeOn(scheduler: Scheduler): Observable[T] = {
    u.observeOn(scheduler)
  }

  def onErrorResumeNext(f: Throwable => Observable[T]): Observable[T] = {
    u.onErrorResumeNext(new Function[Throwable, ObservableSource[T]] {
      override def apply(t: Throwable): ObservableSource[T] = f(t)
    })
  }

  def publish[R](selector: Observable[T] => ObservableSource[R]): Observable[R] = {
    val rxSelector: io.reactivex.functions.Function[io.reactivex.Observable[T], ObservableSource[R]] = selector(_)
    u.publish(rxSelector)
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
    val rxOnSuccess: io.reactivex.functions.Consumer[T] = onSuccess(_)
    val rxOnError: io.reactivex.functions.Consumer[Throwable] = onError(_)
    u.subscribe(rxOnSuccess, rxOnError)
  }

  def switchMap[R](f: T => Observable[R]): Observable[R] = {
    u.switchMap[R](new io.reactivex.functions.Function[T, io.reactivex.Observable[R]] {
      def apply(t: T): io.reactivex.Observable[R] = f(t)
    })
  }

  def takeLast(count: Int): Observable[T] = {
    u.takeLast(count)
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

  def window[U, V](openingIndicator: ObservableSource[U], closingIndicator: U => ObservableSource[V]): Observable[Observable[T]] = {
    val rxClosingIndicator: io.reactivex.functions.Function[U, ObservableSource[V]] = closingIndicator(_)
    u.window(openingIndicator, rxClosingIndicator)
      .asScala
      .map(_.asScala)
  }

  def withLatestFrom[U, R](other: Observable[U])(f: (T, U) => R): Observable[R] = {
    u.withLatestFrom[U, R](other, new io.reactivex.functions.BiFunction[T, U, R] {
      def apply(t1: T, t2: U): R = f(t1, t2)
    })
  }

  /** Note: This is Exante extension method, there is no such method in rxJava.
    *
    * Takes elements until other observable is completed.
    */
  def takeUntilCompleted(other: Observable[_]): Observable[T] = {
    u.takeUntil(other.takeLast(1).map(_ => ()).defaultIfEmpty(()))
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

  def combineLatest[T: ClassTag, R](bufferSize: Int, sources: ObservableSource[_ <: T]*)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.castTo[T])
    }, bufferSize, sources: _*)
  }

  def combineLatest[T: ClassTag, R](sources: Iterable[_ <: ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.castTo[T])
    })
  }

  def combineLatest[T: ClassTag, R](sources: Iterable[_ <: ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources.asJava, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.castTo[T])
    }, bufferSize)
  }

  def combineLatest[T: ClassTag, R](sources: Array[ObservableSource[_ <: T]])(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.castTo[T])
    })
  }

  def combineLatest[T: ClassTag, R](sources: Array[ObservableSource[_ <: T]], bufferSize: Int)(combiner: Array[T] => R): Observable[R] = {
    io.reactivex.Observable.combineLatest[T, R](sources, new io.reactivex.functions.Function[Array[Object], R] {
      def apply(t: Array[Object]): R = combiner(t.castTo[T])
    }, bufferSize)
  }

  def combineLatest[T1, T2](source1: Observable[T1], source2: Observable[T2]): Observable[(T1, T2)] = {
    val combiner: io.reactivex.functions.BiFunction[T1, T2, (T1, T2)] = Tuple2.apply
    io.reactivex.Observable.combineLatest[T1, T2, (T1, T2)](source1, source2, combiner)
  }

  def combineLatest[T1, T2, T3, R](source1: Observable[T1],
                                   source2: Observable[T2],
                                   source3: Observable[T3]): Observable[(T1, T2, T3)] = {
    val combiner: io.reactivex.functions.Function3[T1, T2, T3, (T1, T2, T3)] = Tuple3.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, (T1, T2, T3)](source1, source2, source3, combiner)
  }

  def combineLatest[T1, T2, T3, T4](source1: Observable[T1],
                                    source2: Observable[T2],
                                    source3: Observable[T3],
                                    source4: Observable[T4]): Observable[(T1, T2, T3, T4)] = {
    val combiner: io.reactivex.functions.Function4[T1, T2, T3, T4, (T1, T2, T3, T4)] = Tuple4.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, (T1, T2, T3, T4)](source1, source2, source3, source4, combiner)
  }

  def combineLatest[T1, T2, T3, T4, T5](source1: Observable[T1],
                                        source2: Observable[T2],
                                        source3: Observable[T3],
                                        source4: Observable[T4],
                                        source5: Observable[T5]): Observable[(T1, T2, T3, T4, T5)] = {
    val combiner: io.reactivex.functions.Function5[T1, T2, T3, T4, T5, (T1, T2, T3, T4, T5)] = Tuple5.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, (T1, T2, T3, T4, T5)](source1, source2, source3, source4, source5, combiner)
  }

  def combineLatest[T1, T2, T3, T4, T5, T6](source1: Observable[T1],
                                            source2: Observable[T2],
                                            source3: Observable[T3],
                                            source4: Observable[T4],
                                            source5: Observable[T5],
                                            source6: Observable[T6]): Observable[(T1, T2, T3, T4, T5, T6)] = {
    val combiner: io.reactivex.functions.Function6[T1, T2, T3, T4, T5, T6, (T1, T2, T3, T4, T5, T6)] = Tuple6.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, (T1, T2, T3, T4, T5, T6)](source1, source2, source3, source4, source5, source6, combiner)
  }

  def combineLatest[T1, T2, T3, T4, T5, T6, T7](source1: Observable[T1],
                                                source2: Observable[T2],
                                                source3: Observable[T3],
                                                source4: Observable[T4],
                                                source5: Observable[T5],
                                                source6: Observable[T6],
                                                source7: Observable[T7]): Observable[(T1, T2, T3, T4, T5, T6, T7)] = {
    val combiner: io.reactivex.functions.Function7[T1, T2, T3, T4, T5, T6, T7, (T1, T2, T3, T4, T5, T6, T7)] = Tuple7.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, (T1, T2, T3, T4, T5, T6, T7)](source1, source2, source3, source4, source5, source6, source7, combiner)
  }

  def combineLatest[T1, T2, T3, T4, T5, T6, T7, T8](source1: Observable[T1],
                                                    source2: Observable[T2],
                                                    source3: Observable[T3],
                                                    source4: Observable[T4],
                                                    source5: Observable[T5],
                                                    source6: Observable[T6],
                                                    source7: Observable[T7],
                                                    source8: Observable[T8]): Observable[(T1, T2, T3, T4, T5, T6, T7, T8)] = {
    val combiner: io.reactivex.functions.Function8[T1, T2, T3, T4, T5, T6, T7, T8, (T1, T2, T3, T4, T5, T6, T7, T8)] = Tuple8.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, T8, (T1, T2, T3, T4, T5, T6, T7, T8)](source1, source2, source3, source4, source5, source6, source7, source8, combiner)
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
    val combiner: io.reactivex.functions.Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, (T1, T2, T3, T4, T5, T6, T7, T8, T9)] = Tuple9.apply
    io.reactivex.Observable.combineLatest[T1, T2, T3, T4, T5, T6, T7, T8, T9, (T1, T2, T3, T4, T5, T6, T7, T8, T9)](source1, source2, source3, source4, source5, source6, source7, source8, source9, combiner)
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

  def fromIterable[T](items: Iterable[T]): Observable[T] = {
    io.reactivex.Observable.fromIterable(items.asJava)
  }

  def just[T](item1: T): Observable[T] = {
    io.reactivex.Observable.just[T](item1)
  }

  def interval(initial: Long, period: Long, unit: TimeUnit): Observable[Long] = {
    io.reactivex.Observable.interval(initial, period, unit).asScala
      .map[Long] { x => x }
  }

  def intervalRange(start: Long, count: Long, initialDelay: Long, period: Long, unit: TimeUnit): Observable[Long] = {
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
