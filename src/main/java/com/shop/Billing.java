package com.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
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
    private ComboBox<String> itemComboBox;
    @FXML
    private Button addbutton;
    
    @FXML
    private TableColumn<InventoryItem, Void> deleteColumn;
    @FXML
    private Label finalTotal;

    private static InventoryService inventoryService;
    private static List<InventoryItem> billList;

    public static List<String> getitemNames(){ 
        List<InventoryItem> inv = inventoryService.getInventory(); 
        // Populate the ComboBox with items from the backend service
        List<String> itemsNames = new ArrayList<>();
        for (InventoryItem it : inv) {
            itemsNames.add(it.getName()+"    :  Rs ₹ "+it.getPrice());
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
            newItem="";
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
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());  
        double curtotal = quantity*price ;
        
        // Adding to our total items
        InventoryItem billitem = new InventoryItem(name, quantity, price,curtotal);
        billList.add(billitem); 
        itemComboBox.getEditor().clear();
        // Update the table view 
        tableView.setItems(FXCollections.observableArrayList(billList)); 
        finalTotal.setText(" TOTAL : \t  Rs "+countTotal()); 
        itemComboBox.requestFocus();
    }
 
    @FXML
    private void handleDeleteAction(InventoryItem item ) { 
        tableView.getItems().remove(item);
        billList.remove(item); 
        finalTotal.setText(" TOTAL : \t Rs "+countTotal());
    }

    public static double countTotal(){
        double finalPrioce = 0 ;
        for (InventoryItem itms : billList) {
            finalPrioce+=itms.getItemtotal();
        }
        return finalPrioce;
    }
}
