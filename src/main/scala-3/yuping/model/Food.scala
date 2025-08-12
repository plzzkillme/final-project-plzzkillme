package yuping.model

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.LocalDate
import yuping.util.Database
import yuping.util.DateUtil._
import scalikejdbc._
import scala.util.{Try, Success, Failure}
import scalikejdbc.{DB, DBSession}

class Food (_name: String, _price: Double, _expiryDate: LocalDate ) extends Database :
  val name : StringProperty = StringProperty(_name)
  val price : ObjectProperty[Double] = ObjectProperty[Double](_price)
  val expiryDate: ObjectProperty[LocalDate] = ObjectProperty[LocalDate](_expiryDate)

  def save(): Try[Int] =
    if (!(isExist)) then
      Try(DB autoCommit { implicit session =>
        sql"""
        insert into food (name, price, expiryDate) values
         (${name.value}, ${price.value}, ${expiryDate.value.asString})
       """.update.apply()
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
       update food
       set
       name  = ${name.value} ,
       price   = ${price.value},
       expiryDate       = ${expiryDate.value.asString}
        where name = ${name.value} and
        price = ${price.value}
       """.update.apply()
      })

  def delete(): Try[Int] =
    if (isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
       delete from food where
        name = ${name.value} and price = ${price.value}
       """.update.apply()
      })
    else
      throw new Exception("Food not Exists in Database")

  def isExist: Boolean =
    DB readOnly { implicit session: DBSession =>
      sql"""
      select 1 from food
      where name = ${name.value} and price = ${price.value}
    """
        .map(_.int(1))
        .single
        .apply()               // returns Option[Int]
    } match
      case Some(_) => true
      case None    => false


object Food extends Database:
  def apply(
             _name: String,
             _price: Double,
             _expiryDate: LocalDate
           ): Food =

    new Food(_name, _price, _expiryDate):
      name.value = _name
      price.value = _price
      expiryDate.value = _expiryDate

  def initializeTable() =
    DB autoCommit { implicit session =>
      sql"""
    create table food (
      id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
      name varchar(64),
      price varchar(64),
      expiryDate varchar(64)
    )
    """.execute.apply()
    }

  def getAllFoods: List[Food] =
    DB readOnly { implicit session =>
      sql"select * from food"
        .map(rs => Food(
          rs.string("name"),
          rs.double("price"),
          rs.localDate("expiryDate")
        ))
        .list
        .apply()
    }

  