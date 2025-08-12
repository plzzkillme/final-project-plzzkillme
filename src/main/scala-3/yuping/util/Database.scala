package yuping.util
import scalikejdbc.*
import yuping.model.Food

trait Database :
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")
  // ad-hoc session provider on the REPL
  given AutoSession = AutoSession

object Database extends Database :
  def setupDB() =
    if (!hasDBInitialize)
      Food.initializeTable()
  def hasDBInitialize : Boolean =
    DB getTable "Food" match
      case Some(x) => true
      case None => false
