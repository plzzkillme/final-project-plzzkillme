package yuping.model

import scalafx.beans.property.{ObjectProperty, StringProperty}
import java.time.LocalDate

class Food (_name: String, _price: Double, _expiryDate: LocalDate ):
  val name : StringProperty = StringProperty(_name)
  val price : ObjectProperty[Double] = ObjectProperty[Double](_price)
  val expiryDate: ObjectProperty[LocalDate] = ObjectProperty[LocalDate](_expiryDate)
  