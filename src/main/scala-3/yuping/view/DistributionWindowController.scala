package yuping.view

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.event.ActionEvent
import javafx.collections.{FXCollections, ObservableList}
import yuping.model.{DistributionRecord, Donor, Food, Recipient}
import yuping.MainApp

import scala.util.{Failure, Success, Try}
import java.time.LocalDate
import yuping.util.DateUtil.*
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import scalafx.beans.property.StringProperty

class DistributionWindowController {

  // ------------------- Donors -------------------
  @FXML private var donorTable: TableView[Donor] = _
  @FXML private var donorNameColumn: TableColumn[Donor, String] = _
  @FXML private var donorContactColumn: TableColumn[Donor, String] = _
  @FXML private var donorNameField: TextField = _
  @FXML private var donorContactField: TextField = _

  private val donorData: ObservableList[Donor] = FXCollections.observableArrayList(Donor.getAllDonors: _*)

  // ------------------- Recipients -------------------
  @FXML private var recipientTable: TableView[Recipient] = _
  @FXML private var recipientNameColumn: TableColumn[Recipient, String] = _
  @FXML private var recipientContactColumn: TableColumn[Recipient, String] = _
  @FXML private var recipientNameField: TextField = _
  @FXML private var recipientContactField: TextField = _

  private val recipientData: ObservableList[Recipient] = FXCollections.observableArrayList(Recipient.getAll: _*)

  // ------------------- Food -------------------
  @FXML private var foodTable: TableView[Food] = _
  @FXML private var foodNameColumn: TableColumn[Food, String] = _
  @FXML private var foodPriceColumn: TableColumn[Food, java.lang.Double] = _
  @FXML private var foodExpiryColumn: TableColumn[Food, LocalDate] = _
  @FXML private var foodNameField: TextField = _
  @FXML private var foodPriceField: TextField = _
  @FXML private var foodExpiryField: TextField = _

  private val foodData: ObservableList[Food] = FXCollections.observableArrayList(Food.getAllFoods: _*)

  // ------------------- Distribution Records -------------------
  @FXML private var distributionTable: TableView[DistributionRecord] = _
  @FXML private var distDonorField: TextField = _
  @FXML private var distRecipientField: TextField = _
  @FXML private var distFoodField: TextField = _
  @FXML private var distDateField: TextField = _
  @FXML
  private val distributionData: ObservableList[DistributionRecord] =
    FXCollections.observableArrayList(DistributionRecord.getAll: _*)
  @FXML private var distributionDonorColumn: TableColumn[DistributionRecord, String] = _
  @FXML private var distributionRecipientColumn: TableColumn[DistributionRecord, String] = _
  @FXML private var distributionFoodColumn: TableColumn[DistributionRecord, String] = _
  @FXML private var distributionDateColumn: TableColumn[DistributionRecord, LocalDate] = _

  // ------------------- Initialization -------------------
  def initialize(): Unit = {
    // Donor Table
    donorNameColumn.setCellValueFactory(_.getValue.name)
    donorContactColumn.setCellValueFactory(_.getValue.contact)
    donorTable.setItems(donorData)
    donorTable.getSelectionModel.selectedItemProperty.addListener { (_, _, donor) =>
      if donor != null then
        donorNameField.setText(donor.name.value)
        donorContactField.setText(donor.contact.value)
    }

    // Recipient Table
    recipientNameColumn.setCellValueFactory(_.getValue.name)
    recipientContactColumn.setCellValueFactory(_.getValue.contact)
    recipientTable.setItems(recipientData)
    recipientTable.getSelectionModel.selectedItemProperty.addListener { (_, _, recipient) =>
      if recipient != null then
        recipientNameField.setText(recipient.name.value)
        recipientContactField.setText(recipient.contact.value)
    }

    // Food Table
    foodNameColumn.setCellValueFactory(_.getValue.name)
    foodPriceColumn.setCellValueFactory(cd =>
      new SimpleObjectProperty[java.lang.Double](cd.getValue.price.value)
    )
    foodExpiryColumn.setCellValueFactory(_.getValue.expiryDate)
    foodTable.setItems(foodData)
    foodTable.getSelectionModel.selectedItemProperty.addListener { (_, _, food) =>
      if food != null then
        foodNameField.setText(food.name.value)
        foodPriceField.setText(food.price.value.toString)
        foodExpiryField.setText(food.expiryDate.value.toString)
    }

    // Distribution Table
    distributionDonorColumn.setCellValueFactory(cd =>
      cd.getValue.donor.delegate.asInstanceOf[javafx.beans.value.ObservableValue[String]]
    )
    distributionRecipientColumn.setCellValueFactory(cd =>
      cd.getValue.recipient.delegate.asInstanceOf[javafx.beans.value.ObservableValue[String]]
    )
    distributionFoodColumn.setCellValueFactory(cd =>
      cd.getValue.food.delegate.asInstanceOf[javafx.beans.value.ObservableValue[String]]
    )
    distributionDateColumn.setCellValueFactory(cd =>
      new SimpleObjectProperty[LocalDate](cd.getValue.date.value)
    )
    distributionTable.setItems(distributionData)
  }

  // ------------------- Donor Handlers -------------------
  @FXML def handleAddDonor(): Unit =
    val donor = new Donor(
      scalafx.beans.property.StringProperty(donorNameField.getText.trim),
      scalafx.beans.property.StringProperty(donorContactField.getText.trim)
    )
    donor.save() match
      case Success(_) => donorData.add(donor); clearDonorFields()
      case Failure(ex) =>
        ex.printStackTrace()
        showError(s"Failed to save donor: ${ex.getMessage}")

  @FXML def handleEditDonor(): Unit =
    Option(donorTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.name.value = donorNameField.getText.trim
        selected.contact.value = donorContactField.getText.trim
        selected.save() match
          case Success(_) => donorTable.refresh(); clearDonorFields()
          case Failure(_) => showError("Failed to update donor.")
      case None => showError("No donor selected.")

  @FXML def handleDeleteDonor(): Unit =
    Option(donorTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.delete() match
          case Success(_) => donorData.remove(selected); clearDonorFields()
          case Failure(_) => showError("Failed to delete donor.")
      case None => showError("No donor selected.")

  private def clearDonorFields(): Unit =
    donorNameField.clear();
    donorContactField.clear()

  // ------------------- Recipient Handlers -------------------
  @FXML def handleAddRecipient(): Unit =
    val recipient = new Recipient(
      StringProperty(recipientNameField.getText.trim),
      StringProperty(recipientContactField.getText.trim)
    )
    recipient.save() match
      case Success(_) => recipientData.add(recipient); clearRecipientFields()
      case Failure(_) => showError("Failed to save recipient.")

  @FXML def handleEditRecipient(): Unit =
    Option(recipientTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.name.value = recipientNameField.getText.trim
        selected.contact.value = recipientContactField.getText.trim
        selected.save() match
          case Success(_) => recipientTable.refresh(); clearRecipientFields()
          case Failure(_) => showError("Failed to update recipient.")
      case None => showError("No recipient selected.")

  @FXML def handleDeleteRecipient(): Unit =
    Option(recipientTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.delete() match
          case Success(_) => recipientData.remove(selected); clearRecipientFields()
          case Failure(_) => showError("Failed to delete recipient.")
      case None => showError("No recipient selected.")

  private def clearRecipientFields(): Unit =
    recipientNameField.clear();
    recipientContactField.clear()

  // ------------------- Food Handlers -------------------
  @FXML def handleAddFood(): Unit =
    val name = foodNameField.getText.trim
    val price = foodPriceField.getText.trim.toDoubleOption.getOrElse(0.0)
    val dateText = foodExpiryField.getText.trim
    val expiry = Try(LocalDate.parse(dateText)).getOrElse {
      showError("Invalid date. Please use YYYY-MM-DD format.")
      return
  }
    val food = new Food(name, price, expiry)
    food.save() match
      case Success(_) => foodData.add(food); clearFoodFields()
      case Failure(_) => showError("Failed to save food.")

  @FXML def handleEditFood(): Unit =
    Option(foodTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.name.value = foodNameField.getText.trim
        selected.price.value = foodPriceField.getText.trim.toDoubleOption.getOrElse(selected.price.value)
        selected.expiryDate.value = foodExpiryField.getText.trim.parseLocalDate.getOrElse(selected.expiryDate.value)
        selected.save() match
          case Success(_) => foodTable.refresh(); clearFoodFields()
          case Failure(_) => showError("Failed to update food.")
      case None => showError("No food selected.")

  @FXML def handleDeleteFood(): Unit =
    Option(foodTable.getSelectionModel.getSelectedItem) match
      case Some(selected) =>
        selected.delete() match
          case Success(_) => foodData.remove(selected); clearFoodFields()
          case Failure(_) => showError("Failed to delete food.")
      case None => showError("No food selected.")

  private def clearFoodFields(): Unit =
    foodNameField.clear();
    foodPriceField.clear();
    foodExpiryField.clear()

  // ------------------- Distribution Records -------------------
  @FXML def handleAddDistribution(): Unit = {
    val donorName = distDonorField.getText.trim
    val recipientName = distRecipientField.getText.trim
    val foodName = distFoodField.getText.trim
    val dateText = distDateField.getText.trim
    val dateValue = Try(LocalDate.parse(dateText)).getOrElse {
      showError("Invalid date. Please use YYYY-MM-DD format.")
      return
    }

    val record = new DistributionRecord(donorName, recipientName, foodName, dateValue)
    record.save() match {
      case Success(_) =>
        distributionData.add(record)
        distDonorField.clear()
        distRecipientField.clear()
        distFoodField.clear()
        distDateField.clear()
      case Failure(e) =>
        showError(s"Failed to add distribution record: ${e.getMessage}")
    }
  }

  private def clearDistributionFields(): Unit =
    distDonorField.clear()
    distRecipientField.clear()
    distFoodField.clear()
    distDateField.clear()

  // ------------------- Helpers -------------------
  private def showError(msg: String): Unit =
    val alert = new Alert(Alert.AlertType.ERROR)
    alert.initOwner(MainApp.stage)
    alert.setTitle("Error")
    alert.setHeaderText("")
    alert.setContentText(msg)
    alert.showAndWait()

  @FXML def handleClose(event: ActionEvent): Unit =
    val stage = event.getSource.asInstanceOf[javafx.scene.Node].getScene.getWindow
    stage.hide()
}