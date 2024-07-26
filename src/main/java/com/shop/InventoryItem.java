package com.shop;

public class InventoryItem {
    private String name;
    private double price;
    private int quantity;
    private boolean selected;
    private double itemtotal;
    private String description;

    public InventoryItem(String name, int quantity, double price, String description) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public InventoryItem(String name, int quantity, double price) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public InventoryItem(String name, int quantity, double price , double itemtotal) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.itemtotal = itemtotal;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "InventoryItem [name=" + name + ", price=" + price + ", quantity=" + quantity + ", selected=" + selected
                + ", description=" + description + "]";
    }

    public double getItemtotal() {
        return itemtotal;
    }

    public void setItemtotal(double itemtotal) {
        this.itemtotal = itemtotal;
    }
}