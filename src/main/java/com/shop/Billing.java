package com.shop;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class Billing {
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private TableView<InventoryItem> tableView;
    @FXML
    private boolean selected;
    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    @FXML
    private TextField personnameField;
    @FXML
    private TextField mobileField;

    @FXML
    private ComboBox<String> itemComboBox;
    @FXML
    private Button addbutton;

    @FXML
    private TableColumn<InventoryItem, Void> deleteColumn;
    @FXML
    private Label finalTotal;

    @FXML
    private HBox printvboxSection;

    private static InventoryService inventoryService;
    private static List<InventoryItem> billList;

    public static List<String> getitemNames() {
        List<InventoryItem> inv = inventoryService.getInventory();
        // Populate the ComboBox with items from the backend service
        List<String> itemsNames = new ArrayList<>();
        for (InventoryItem it : inv) {
            itemsNames.add(it.getName() + "    :  Rs ₹ " + it.getPrice());
        }
        return itemsNames;
    }

    @FXML
    private void initialize() {
        inventoryService = new InventoryService();
        billList = new ArrayList<>();
        ObservableList<String> items = FXCollections.observableArrayList(getitemNames());
        itemComboBox.setItems(items);

        // Add listener to update quantity and price fields when an item is selected
        itemComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null && !newItem.trim().equals("")) {
                nameField.setText(String.valueOf(newItem.split("    :  Rs ₹ ")[0]));
                quantityField.setText(String.valueOf(1)); // By default one item
                priceField.setText(String.valueOf(newItem.split("    :  Rs ₹ ")[1]));
            }
            newItem = "";
            itemComboBox.getEditor().setText("");
            addbutton.requestFocus();
        });

        // Add listener to filter items based on user input
        itemComboBox.getEditor().setOnKeyTyped(event -> {
            String newText = itemComboBox.getEditor().getText();
            if (newText != null && !newText.trim().equals("")) {
                List<String> filteredItems = getitemNames().stream()
                        .filter(item -> item.toLowerCase().contains(newText.toLowerCase().trim()))
                        .collect(Collectors.toList());
                ObservableList<String> filteredObservableItems = FXCollections.observableArrayList(filteredItems);
                itemComboBox.setItems(filteredObservableItems);
            } else {
                ObservableList<String> ogitems = FXCollections.observableArrayList(getitemNames());
                itemComboBox.setItems(ogitems); // Reset to original list when input is cleared
            }
            itemComboBox.show();
        });

        // Configure delete column cell factory
        deleteColumn.setCellFactory(new Callback<TableColumn<InventoryItem, Void>, TableCell<InventoryItem, Void>>() {
            @Override
            public TableCell<InventoryItem, Void> call(TableColumn<InventoryItem, Void> param) {
                return new TableCell<InventoryItem, Void>() {
                    private final Button deleteButton = new Button("❌");
                    {
                        deleteButton.setStyle("-fx-font-size: 8px; -fx-background-color: red; -fx-text-fill: white;");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            InventoryItem item = getTableView().getItems().get(getIndex());
                            handleDeleteAction(item);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });

        tableView.setItems(FXCollections.observableArrayList(billList));
    }

    @FXML
    private void addBillItem() {
        // get the new bill item added bhy the user
        String name = nameField.getText();
        if (name == null || name.trim().equals(""))
            return;
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (Exception e) {
            quantity = 1;
        }
        double price = 0.0;
        try {
            price = Double.parseDouble(priceField.getText());
        } catch (Exception e) {
            return;
        }
        double curtotal = quantity * price;

        // Adding to our total items
        InventoryItem billitem = new InventoryItem(name, quantity, price, curtotal);
        billList.add(billitem);
        itemComboBox.getEditor().clear();
        // Update the table view
        tableView.setItems(FXCollections.observableArrayList(billList));
        finalTotal.setText(" TOTAL : \t  Rs. " + countTotal());
        itemComboBox.requestFocus();
    }

    @FXML
    private void handleDeleteAction(InventoryItem item) {
        tableView.getItems().remove(item);
        billList.remove(item);
        finalTotal.setText(" TOTAL : \t Rs. " + countTotal());
    }

    @FXML
    private void createBillItem() {
        String personname = personnameField.getText() + "";
        String mobileno = mobileField.getText() + "";
        BillingItem blitem = new BillingItem(personname, mobileno, countTotal() + "", billList);
        CSVReadWrite csrw = new CSVReadWrite();
        try {
            csrw.writeBilltoCSV(blitem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        confirmPrint(blitem);
        billList.clear();
        tableView.setItems(FXCollections.observableArrayList(billList));
    }

    private void confirmPrint(BillingItem blitem) {
        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonData.NO);
        Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to print the bill?");
        alert.getDialogPane().setPrefHeight(50);
        alert.getDialogPane().setPrefWidth(100);

        // Optionally, set custom styles
        alert.getDialogPane().setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-color: #f4f4f4;");

        // Set the stage style
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            handlePrintSection(blitem);
        }
    }

    private void handlePrintSection(BillingItem blitem) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd-MM-yyyy");
        String formattedDateTime = now.format(formatter);
        // Create a VBox to hold all items
        printvboxSection.getChildren().clear();
        VBox billprin = new VBox();
        printvboxSection.getChildren().add(billprin);
        // Adding a header part as Vbox
        Label header = new Label(
                "           Friends Bakery" +
                        " \nName   : " + blitem.getName() +
                        " \nMobNo. : " + blitem.getMobno() +
                        " \nTime : " + formattedDateTime + "\n\n");
        header.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-font-weight: bold;");
        billprin.getChildren().add(header);

        // now create a Vbox for each 30 items and append to a Hbox
        int i = 0;
        HBox subMenParent = new HBox();
        VBox submen = new VBox();
        // Add items to the VBox
        for (InventoryItem item : billList) {
            if (i % 30 == 0) {
                subMenParent.getChildren().add(submen);
                submen = new VBox();
                submen.setStyle("-fx-border-color: #02111b; -fx-border-width: 1px;-fx-padding: 4px;-fx-margin: -10px;");
            }
            String itemName = item.getName();
            if (itemName.length() > 15) {
                itemName = itemName.substring(0, 12) + "...";
            } else {
                itemName = String.format("%-15s", itemName);
            } 
            String itemDetails = String.format("%-1s %-3s x %-6s = %-7s",
                    itemName, item.getQuantity()+"", item.getPrice()+"",item.getItemtotal()+"");
            Label itemLabel = new Label(itemDetails);
            itemLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 10px;");
            submen.getChildren().add(itemLabel);
            i++;
        }
        subMenParent.getChildren().add(submen);
        billprin.getChildren().add(subMenParent);
        // Add the total
        Label totalLabel = new Label(
                "\n       TOTAL : Rs. " + countTotal() +
                        "\n         Thank You !!! " +
                        "\n     Contact : 8989269102");
        totalLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-font-weight: bold;");
        billprin.getChildren().add(totalLabel);
        // Capture snapshot
        WritableImage snapshot = billprin.snapshot(new SnapshotParameters(), null);
        // Convert to ImageView for printing
        ImageView imageView = new ImageView(snapshot);
        // Print the ImageView
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);

        if (job != null && job.showPrintDialog(printvboxSection.getScene().getWindow())) {
            boolean success = job.printPage(imageView);
            if (success) {
                job.endJob();
            }
        }
    }

    public static double countTotal() {
        double finalPrioce = 0;
        for (InventoryItem itms : billList) {
            finalPrioce += itms.getItemtotal();
        }
        return finalPrioce;
    }
}
