package yuping.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.stage.Stage

@FXML
class AboutController():
  //MODEL PROPERTY
  //STAGE PROPERTY
  var stage: Option[Stage] = None
  //RETURN PROPERTY
  var okClicked = false
  @FXML
  def handleClose(action: ActionEvent): Unit = {
    okClicked = true
    stage.foreach(x => x.close())
  }
