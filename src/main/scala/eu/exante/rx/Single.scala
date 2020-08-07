package eu.exante.rx

class Single[T](private val u: io.reactivex.Single[T]) extends AnyVal {

  def asJava: io.reactivex.Single[T] = {
    u
  }

  def asScala: Single[T] = {
    this
  }

  def map[R](f: T => R): Single[R] = {
    u.map[R](new io.reactivex.functions.Function[T, R] {
      def apply(t: T): R = f(t)
    })
  }

  def toObservable: Observable[T] = {
    u.toObservable
  }
}
