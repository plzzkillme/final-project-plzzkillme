package yuping.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import yuping.MainApp

@FXML
class RootLayoutController():
  @FXML
  def handleClose(action: ActionEvent): Unit =
    MainApp.stage.close()

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    MainApp.showAbout()

  @FXML
  def handleDelete(action: ActionEvent): Unit =
    
