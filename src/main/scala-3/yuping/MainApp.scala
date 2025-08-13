package yuping
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.{Node, Parent, Scene, control}
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.Modality.ApplicationModal
import scalafx.stage.{Modality, Stage}
import yuping.MainApp.getClass
import yuping.model.Food
import yuping.util.Database
import yuping.view.{AboutController, DistributionWindowController, FoodEditDialogController, MainWindowController, WelcomeController}

import java.net.URL

object MainApp extends JFXApp3:
  Database.setupDB()
  val foodData = new ObservableBuffer[Food]()
  var rootPane: Option[javafx.scene.layout.BorderPane] = None
  val myfood: ObservableBuffer[Food] = ObservableBuffer(Food.getAllFoods: _*)
  var cssResource = getClass.getResource("/yuping/view/style.css")
  var mainWindowController: Option[MainWindowController] = None

  foodData ++= Food.getAllFoods

  override def start(): Unit = {
    val rootLayoutResource: URL = getClass.getResource("/yuping/view/RootLayout.fxml")
    val loader = new FXMLLoader(rootLayoutResource)
    val rootLayout = loader.load[javafx.scene.layout.BorderPane]()
    rootPane = Option(loader.getRoot[javafx.scene.layout.BorderPane]()) //initialise
    stage = new PrimaryStage() {
      title = "Food Distribution App"
      alwaysOnTop = false
      icons += new Image(getClass.getResource("/images/food6.png").toExternalForm)
      scene = new Scene():
        root = rootLayout
        stylesheets = Seq(cssResource.toExternalForm)
    }
    showWelcome() //first window
  }
  //show welcome window in the root pane
  def showWelcome(): Unit =
    val welcome = getClass.getResource("/yuping/view/Welcome.fxml")
    val loader = new FXMLLoader(welcome)
    val pane = loader.load[javafx.scene.layout.AnchorPane]()
    rootPane.foreach(_.setCenter(pane))

  //show main window in the root pane
  def showMainWindow(): Unit =
    val welcome = getClass.getResource("/yuping/view/MainWindow.fxml")
    val loader = new FXMLLoader(welcome)
    val pane = loader.load[javafx.scene.layout.AnchorPane]()
    val ctrl = loader.getController[MainWindowController]
    mainWindowController = Option(ctrl)
    rootPane.foreach(_.setCenter(pane))

  def showAbout(): Boolean =
    val about = getClass.getResource("/yuping/view/About.fxml")
    val loader = new FXMLLoader(about)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val mywindow = new Stage():
      initOwner(stage)
      initModality(ApplicationModal)
      title = "About"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[AboutController]()
    ctrl.stage = Option(mywindow)
    mywindow.showAndWait() //pop up
    ctrl.okClicked

  def showFoodEditDialog(food: Food): Boolean =
    val resource = getClass.getResource("/yuping/view/FoodEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    loader.load();
    val roots2 = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val control = loader.getController[FoodEditDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene:
        root = roots2
        stylesheets = Seq(cssResource.toExternalForm)

    control.dialogStage = dialog
    control.food = food
    dialog.showAndWait()
    control.okClicked

  def showDistributionWindow(): Unit =
    try
      val loader = new FXMLLoader(getClass.getResource("/yuping/view/DistributionWindow.fxml"))
      val page = loader.load[javafx.scene.layout.AnchorPane]()
      val dialogStage = new Stage()
      dialogStage.setTitle("Distribution Manager")
      dialogStage.initOwner(stage)
      dialogStage.setScene(new Scene(page))
      dialogStage.showAndWait()
    catch
      case e: Exception => e.printStackTrace()

  def showRecipientWindow(): Unit =
    try
      val loader = new javafx.fxml.FXMLLoader(getClass.getResource("/yuping/view/RecipientWindow.fxml"))
      val page = loader.load[javafx.scene.layout.AnchorPane]()

      val dialogStage = new javafx.stage.Stage()
      dialogStage.setTitle("Recipient Manager")
      dialogStage.initOwner(stage)
      dialogStage.setScene(new javafx.scene.Scene(page))

      dialogStage.showAndWait()
    catch
      case e: Exception =>
        e.printStackTrace()








