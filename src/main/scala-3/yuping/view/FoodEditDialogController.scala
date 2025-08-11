package yuping.view

import yuping.model.Food
import scalafx.scene.control.Alert
import scalafx.stage.Stage
import scalafx.Includes.*
import yuping.util.DateUtil.*
import javafx.fxml.FXML
import javafx.event.ActionEvent
import javafx.scene.control.TextField
import yuping.util.DateUtil

import java.time.LocalDate

@FXML
class FoodEditDialogController():
  @FXML
  private var foodNameField: TextField = null
  @FXML
  private var foodPriceField: TextField = null
  @FXML
  private var foodExpiryDateField: TextField = null

  var dialogStage: Stage = null
  private var __food: Food = null
  var okClicked: Boolean = false

  def food = __food
  def food_=(x: Food): Unit =
    __food = x
    foodNameField.text = __food.name.value
    foodPriceField.text = __food.price.value.toString
    foodExpiryDateField.text = __food.expiryDate.value.asString
    foodExpiryDateField.setPromptText("dd.mm.yyyy")

  @FXML
  def handleOk(action: ActionEvent): Unit = {
    if isInputValid() then
      __food.name <== foodNameField.text
      __food.price.value = foodPriceField.getText.toDouble
      DateUtil.parseLocalDate(foodExpiryDateField.text.value) match {
        case Some(date) => __food.expiryDate.value = date
        case None       =>
      }

      okClicked = true
      dialogStage.close()
  }

  def handleCancel(action: ActionEvent): Unit =
    dialogStage.close()

  private def nullChecking(x: String) = x == null || x.trim.isEmpty

  def isInputValid(): Boolean =
    var errorMessage = ""

    if nullChecking(foodNameField.text.value) then
      errorMessage += "No valid food name!\n"

    if nullChecking(foodPriceField.text.value) then
      errorMessage += "No valid food price!\n"
    else
      try
        foodPriceField.getText.toDouble
      catch
        case _: NumberFormatException =>
          errorMessage += "Price must be a number!\n"

    if nullChecking(foodExpiryDateField.text.value) then
      errorMessage += "No valid expiry date!\n"
    else
      if !foodExpiryDateField.text.value.isValid then
        errorMessage += "Expiry date must be in format dd.mm.yyyy!\n"

    if errorMessage.isEmpty then
      true
    else
      val alert = new Alert(Alert.AlertType.Error):
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      alert.showAndWait()
      false

