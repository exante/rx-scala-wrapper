package eu.exante.rx

import io.reactivex.annotations.{CheckReturnValue, NonNull, SchedulerSupport}
import io.reactivex.disposables.Disposable

class ConnectableObservable[T](private val u: io.reactivex.observables.ConnectableObservable[T]) {
  def asJava: io.reactivex.observables.ConnectableObservable[T] = {
    u
  }

  def asScala: ConnectableObservable[T] = {
    this
  }

  def connect(): Disposable = {
    u.connect()
  }

  def subscribe(onSuccess: T => Unit, onError: Throwable => Unit): Disposable = {
    val rxOnSuccess: io.reactivex.functions.Consumer[T] = onSuccess(_)
    val rxOnError: io.reactivex.functions.Consumer[Throwable] = onError(_)
    u.subscribe(rxOnSuccess, rxOnError)
  }

  @NonNull
  @CheckReturnValue
  @SchedulerSupport(SchedulerSupport.NONE)
  def refCount: Observable[T] = {
    u.refCount()
  }

}
