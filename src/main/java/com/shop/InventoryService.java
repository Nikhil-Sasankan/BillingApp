package com.shop;

import java.io.IOException;
import java.util.List;

public class InventoryService {
    private List<InventoryItem> inventory;

    public InventoryService() {
        this.inventory = fetchItemsFromCSV();
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<InventoryItem> inventory) {
        this.inventory = inventory;
        updateItemsToCSV(inventory);
    }

    public void addInventoryItem(InventoryItem item) {
        inventory.add(item);
        updateItemsToCSV(inventory);
    }

    public static CSVReadWrite csvrw = new CSVReadWrite();

    public static List<InventoryItem> fetchItemsFromCSV() {
        return csvrw.getItemDetails();
    }

    public static void updateItemsToCSV(List<InventoryItem> itemlist) {
        try {
            csvrw.writetoCSV(itemlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}