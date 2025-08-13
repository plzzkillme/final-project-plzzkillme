package yuping.model

import scalikejdbc.*
import scalafx.beans.property.StringProperty

import scala.util.Try

class Recipient(val name: StringProperty, val contact: StringProperty) {
  def save(): Try[Unit] = Try {
    DB autoCommit { implicit session =>
      val updated = sql"""
      UPDATE Recipient SET contact = ${contact.value}
      WHERE name = ${name.value}
    """.update.apply()

      if (updated == 0) {
        sql"""
        INSERT INTO Recipient (name, contact)
        VALUES (${name.value}, ${contact.value})
      """.update.apply()
      }
    }
  }

  def delete(): Try[Unit] = Try {
    DB autoCommit { implicit session =>
      sql"DELETE FROM Recipient WHERE name = ${name.value}".update.apply()
    }
  }
}

object Recipient {
  def getAll: Seq[Recipient] = DB readOnly { implicit session =>
    sql"SELECT name, contact FROM Recipient"
      .map(rs => new Recipient(
        StringProperty(rs.string("name")),
        StringProperty(rs.string("contact"))
      )).list.apply()
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        CREATE TABLE Recipient (
          name VARCHAR(255) PRIMARY KEY,
          contact VARCHAR(255)
        )
      """.execute.apply()
    }
  }
}