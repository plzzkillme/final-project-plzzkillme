package yuping.view

import javafx.fxml.FXML
import yuping.MainApp
import yuping.model.Food
import javafx.event.ActionEvent
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scala.util.{Success, Failure}

import java.time.LocalDate
import yuping.util.DateUtil.*

@FXML
class MainWindowController:
  @FXML
  private var foodTableView: javafx.scene.control.TableView[Food] = null
  @FXML
  private var nameTableColumn: javafx.scene.control.TableColumn[Food, String] = null
  @FXML
  private var priceTableColumn: javafx.scene.control.TableColumn[Food, Double] = null
  @FXML
  private var expiryDateTableColumn: javafx.scene.control.TableColumn[Food, LocalDate] = null
  @FXML
  private var foodNameLabel: javafx.scene.control.Label = null
  @FXML
  private var foodPriceLabel: javafx.scene.control.Label = null
  @FXML
  private var foodExpiryDateLabel: javafx.scene.control.Label = null

  @FXML
  private var nametext: javafx.scene.control.TextField = null
  @FXML
  private var pricetext: javafx.scene.control.TextField = null
  @FXML
  private var expirydatetext: javafx.scene.control.TextField = null

  import java.time.format.DateTimeFormatter

  private val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

  def initialize(): Unit =
    foodTableView.items = MainApp.myfood
    nameTableColumn.cellValueFactory = _.value.name
    priceTableColumn.cellValueFactory = _.value.price
    expiryDateTableColumn.cellValueFactory = _.value.expiryDate

    foodNameLabel.setText("")
    foodPriceLabel.setText("")
    foodExpiryDateLabel.setText("")

    foodTableView.getSelectionModel.selectedItemProperty.addListener { (_, _, newValue) =>
      Option(newValue) match
        case Some(food) =>
          foodNameLabel.textProperty().unbind()
          foodPriceLabel.textProperty().unbind()
          foodExpiryDateLabel.textProperty().unbind()
          foodNameLabel.textProperty() <== food.name
          foodPriceLabel.textProperty() <== food.price.asString("%.2f")
          foodExpiryDateLabel.textProperty() <==
            Bindings.createStringBinding(
              () => food.expiryDate.value.format(dateFormatter),
              food.expiryDate
            )
        case None =>
          foodNameLabel.textProperty().unbind()
          foodPriceLabel.textProperty().unbind()
          foodExpiryDateLabel.textProperty().unbind()
          foodNameLabel.setText("")
          foodPriceLabel.setText("")
          foodExpiryDateLabel.setText("")
    }

  @FXML
  def handleNew(action: ActionEvent): Unit =
    val nameInput = nametext.getText.trim
    val priceInput = pricetext.getText.trim
    val expiryInput = expirydatetext.getText.trim

    var errorMessage = ""

    if nameInput.isEmpty then
      errorMessage += "Name cannot be empty.\n"
    else if nameInput.forall(_.isDigit) then
      errorMessage += "Name must be a string.\n"

    try
      priceInput.toDouble
    catch
      case _: NumberFormatException =>
        errorMessage += "Price must be a valid number.\n"

    expiryInput.parseLocalDate match
      case None =>
        errorMessage += s"Expiry date must be in format $DATE_PATTERN.\n"
      case Some(_) => // OK

    if errorMessage.nonEmpty then
      showError(errorMessage)
      return

    // If no errors, create the new food object
    val priceValue = priceInput.toDouble
    val expiryDateValue = expiryInput.parseLocalDate.get
    val food = new Food(nameInput, priceValue, expiryDateValue)

    // Save to database first
    food.save() match
      case Success(_) =>
        MainApp.myfood += food
        nametext.clear()
        pricetext.clear()
        expirydatetext.clear()
      case Failure(e) =>
        val alert = new Alert(AlertType.Warning):
          initOwner(MainApp.stage)
          title = "Failed to Save"
          headerText = "Database Error"
          contentText = s"Database problem: ${e.getMessage}"
        .showAndWait()

  @FXML
  def handleEdit(action: ActionEvent): Unit =
    val selectedFood = foodTableView.selectionModel().selectedItem.value

    if (selectedFood != null) then
      val okClicked = MainApp.showFoodEditDialog(selectedFood)

      if okClicked then
        selectedFood.save() match
          case Success(_) =>
            // Refresh UI so the table shows updated values
            foodTableView.refresh()
          case Failure(e) =>
            val alert = new Alert(AlertType.Warning):
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = s"Database problem: ${e.getMessage}"
            .showAndWait()
    else
      val alert = new Alert(AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Food Selected"
        contentText = "Please select a food in the table."
      .showAndWait()

  @FXML
  def handleDelete(action: ActionEvent): Unit = {
    val index = foodTableView.selectionModel().selectedIndex.value
    val selectedFood = foodTableView.selectionModel().selectedItem.value
    if (index >= 0) then
      selectedFood.save() match
        case Success(x) =>
          foodTableView.items().remove(index)
        case Failure(e) =>
          val alert = new Alert(AlertType.Error):
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          .showAndWait()
    else
      val alert = new Alert(AlertType.Error):
        initOwner(MainApp.stage)
        title = "No selection"
        headerText = "No food selected"
        contentText = "Please select a food in the table."
      .showAndWait()
  }

  @FXML
  def handleBack(action: ActionEvent): Unit =
    MainApp.showWelcome()

  private def showError(message: String): Unit =
    val alert = new Alert(AlertType.Error):
      initOwner(MainApp.stage)
      title = "Invalid input"
      headerText = "Error"
      contentText = message
    .showAndWait()

  @FXML
  def handleNewFood(action: ActionEvent) =
    val food = new Food(
      _name = "",
      _price = 0.0,
      _expiryDate = LocalDate.now()
    )

    val okClicked = MainApp.showFoodEditDialog(food)

    if okClicked then
      MainApp.myfood += food


