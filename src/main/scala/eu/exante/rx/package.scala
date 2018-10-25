package eu.exante

package object rx {

  implicit def scalaToJavaObservable[T](s: Observable[T]): io.reactivex.Observable[T] = {
    s.asJava
  }

  implicit def javaToScalaObservable[T](j: io.reactivex.Observable[T]): Observable[T] = {
    new Observable[T](j)
  }

  implicit def scalaToJavaSingle[T](s: Single[T]): io.reactivex.Single[T] = {
    s.asJava
  }

  implicit def javaToScalaObservable[T](j: io.reactivex.Single[T]): Single[T] = {
    new Single[T](j)
  }

  implicit def optionToObservable[T](opt: Option[T]): Observable[T] = {
    opt match {
      case Some(x) => Observable.just(x)
      case None => Observable.empty
    }
  }
}
