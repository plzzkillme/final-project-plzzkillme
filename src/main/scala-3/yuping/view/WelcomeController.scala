package yuping.view

import javafx.fxml.FXML
import yuping.MainApp

@FXML
class WelcomeController():

  @FXML
  def handleStart(): Unit =
    //call the main window
    MainApp.showMainWindow()

  @FXML
  def handleOpenDistributionWindow(): Unit =
    MainApp.showDistributionWindow() 