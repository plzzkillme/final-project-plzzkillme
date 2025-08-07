package yuping.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import yuping.MainApp

@FXML
class RootLayoutController():
  @FXML
  def handleClose(action: ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    MainApp.showAbout()