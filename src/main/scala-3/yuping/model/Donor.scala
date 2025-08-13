package yuping.model
import scalafx.beans.property.StringProperty
import scala.util.{Try, Success, Failure}
import scalikejdbc._

class Donor(val name: StringProperty, val contact: StringProperty) {

  def save(): Try[Unit] = Try {
    DB autoCommit { implicit session =>
      val updated = sql"""
      UPDATE Donor SET contact = ${contact.value}
      WHERE name = ${name.value}
    """.update.apply()

      if (updated == 0) {
        sql"""
        INSERT INTO Donor (name, contact)
        VALUES (${name.value}, ${contact.value})
      """.update.apply()
      }
    }
  }

  def delete(): Try[Unit] = Try {
    DB autoCommit { implicit session =>
      sql"DELETE FROM Donor WHERE name = ${name.value}".update.apply()
    }
  }
}

object Donor {
  def getAllDonors: Seq[Donor] = DB readOnly { implicit session =>
    sql"SELECT name, contact FROM Donor"
      .map(rs => new Donor(
        new StringProperty(rs.string("name")),
        new StringProperty(rs.string("contact"))
      ))
      .list
      .apply()
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
      CREATE TABLE Donor (
        name VARCHAR(255) PRIMARY KEY,
        contact VARCHAR(255)
      )
    """.execute.apply()
    }
  }
}

