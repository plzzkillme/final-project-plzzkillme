import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.collections.ObservableBuffer
import java.util.Date
class Inventory(
               val id: String,
               val name: String,
               val category: String,
               var quantity: Int,
               val unit: String,
               val expiryDate: String,
               val supplier: String,
               val location: String
               ):
  def getQuantity(): Int = quantity
  def addStock(amount: Int): Unit =
    if amount > 0 then
      quantity += amount
  def removeStock(amount: Int): Boolean =
    if amount > 0 && amount <= quantity then
      quantity -= amount
      true
    else
      false
  def isLowStock(threshold: Int = 10): Boolean =
    quantity <= threshold
  def isExpired(currentDate: String): Boolean =
    expiryDate < currentDate
  override def toString: String =
    s"Item($id, $name, Quantity: $quantity, Exp: $expiryDate)"
class Alert(
           val alertId: String,
           val message: String,
           val itemId: String,
           val alertType: String,
           val timestamp: String
           ):
  def showAlert(): Unit =
    print(s"[ALERT] $alertType for item $itemId: $message at $timestamp")
  override def toString: String =
    s"Alert($alertId, $alertType, Item: $itemId, Time: $timestamp, Message: $message)"
object MyApp extends JFXApp3:

  override def start(): Unit = {
    stage = new PrimaryStage():
      title = "Food Distribution Management App"
      scene = new Scene()
  }

end MyApp
