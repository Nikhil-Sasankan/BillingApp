package com.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class InventoryEdit {
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
    private TextField descriptionField;
    @FXML
    private TextField searchField;
    @FXML
    private Text sizeText;
    private InventoryService inventoryService;

    @FXML
    private void initialize() {
        inventoryService = new InventoryService();
        List<InventoryItem> inv = inventoryService.getInventory();
        tableView.setItems(FXCollections.observableArrayList(inv));
        sizeText.setText("" + inv.size());
    }

    @FXML
    private void addInventoryItem() {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());
        String description = descriptionField.getText(); // Add new items to list
        InventoryItem newItem = new InventoryItem(name, quantity, price, description);
        inventoryService.addInventoryItem(newItem); // once added refresh
        List<InventoryItem> inv = inventoryService.getInventory();
        tableView.setItems(FXCollections.observableArrayList(inv));
        sizeText.setText("" + inv.size());
    }

    @FXML
    private void deleteInventoryItem() {
        List<InventoryItem> updatedList = new ArrayList<>();
        List<InventoryItem> inv = inventoryService.getInventory();
        for (InventoryItem inventoryItem : inv) {
            if (!inventoryItem.getSelected()) {
                updatedList.add(inventoryItem);
            }
        }
        inventoryService.setInventory(updatedList);
        inv = inventoryService.getInventory();
        tableView.setItems(FXCollections.observableArrayList(inv));
        sizeText.setText("" + inv.size());
    }

    @FXML
    private void searchButton() {
        String searchval = searchField.getText();
        List<InventoryItem> updatedList = new ArrayList<>();
        List<InventoryItem> inv = inventoryService.getInventory();
        for (InventoryItem inventoryItem : inv) {
            if (inventoryItem.getName().toLowerCase().contains(searchval.trim().toLowerCase())) {
                updatedList.add(inventoryItem);
            }
        }
        tableView.setItems(FXCollections.observableArrayList(updatedList));
        sizeText.setText("" + updatedList.size());
    }

    @FXML
    private void resetButton() {
        searchField.clear();
        List<InventoryItem> inv = inventoryService.getInventory();
        tableView.setItems(FXCollections.observableArrayList(inv));
        sizeText.setText("" + inv.size());
    }
}