package yuping.model

import scalafx.beans.property.{StringProperty, ObjectProperty}
import java.time.LocalDate
import yuping.util.Database
import yuping.util.DateUtil._
import scalikejdbc._
import scala.util.Try

class DistributionRecord(_donor: String, _recipient: String, _food: String, _date: LocalDate) {
  val donor = StringProperty(_donor)
  val recipient = StringProperty(_recipient)
  val food = StringProperty(_food)
  val date = ObjectProperty[LocalDate](_date)

  def save(): Try[Int] =
    Try(DB autoCommit { implicit session =>
      sql"""
      INSERT INTO DISTRIBUTIONRECORD (donor, recipient, food, date)
      VALUES (${donor.value}, ${recipient.value}, ${food.value}, ${date.value.asString})
    """.update.apply()
    }).recover { case e =>
      println(s"Failed to insert distribution: ${e.getMessage}")
      throw e
    }
}

object DistributionRecord {
  def initializeTable(): Unit =
    DB autoCommit { implicit session =>
      if DB.getTable("DISTRIBUTIONRECORD").isEmpty then
        sql"""
        CREATE TABLE DISTRIBUTIONRECORD (
          id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          donor VARCHAR(64),
          recipient VARCHAR(64),
          food VARCHAR(64),
          date VARCHAR(64)
        )
      """.execute.apply()
    }

  def getAll: List[DistributionRecord] =
    DB readOnly { implicit session =>
      sql"SELECT * FROM DISTRIBUTIONRECORD"
        .map(rs => new DistributionRecord(
          rs.string("donor"),
          rs.string("recipient"),
          rs.string("food"),
          rs.localDate("date")
        ))
        .list
        .apply()
    }
}