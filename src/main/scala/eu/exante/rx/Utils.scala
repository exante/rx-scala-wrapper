package eu.exante.rx

import scala.reflect.ClassTag

object Utils {
  implicit class ArrayHelper[T](val a: Array[T]) extends AnyVal {
    def castTo[U: ClassTag]: Array[U] = a.map(_.asInstanceOf[U])
  }
}
