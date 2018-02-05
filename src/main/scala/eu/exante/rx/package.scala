package eu.exante

package object rx {

  implicit def scalaToJavaObservable[T](so: Observable[T]): io.reactivex.Observable[T] = {
    so.asJava
  }

  implicit def javaToScalaObservable[T](jo: io.reactivex.Observable[T]): Observable[T] = {
    new Observable[T](jo)
  }

  implicit def optionToObservable[T](opt: Option[T]): Observable[T] = {
    opt match {
      case Some(x) => Observable.just(x)
      case None => Observable.empty
    }
  }
}
