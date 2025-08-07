package yuping
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.stage.Modality.ApplicationModal
import scalafx.stage.Stage
import yuping.view.{AboutController, WelcomeController}

import java.net.URL

object MainApp extends JFXApp3:
  var rootPane: Option[javafx.scene.layout.BorderPane] = None
  override def start(): Unit = {
    val rootLayoutResource: URL = getClass.getResource("/yuping/view/RootLayout.fxml")
    val loader = new FXMLLoader(rootLayoutResource)
    val rootLayout = loader.load[javafx.scene.layout.BorderPane]()
    rootPane = Option(loader.getRoot[javafx.scene.layout.BorderPane]()) //initialise
    stage = new PrimaryStage():
      title = "Final Assignment"
      scene = new Scene():
        root = rootLayout
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


