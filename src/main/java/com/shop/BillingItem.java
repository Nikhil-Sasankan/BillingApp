package com.shop;
 
import java.util.List;

public class BillingItem {
    private String name ;
    private String mobno ;
    private String total ; 
    private List<InventoryItem> itemsbought; 
    
    public BillingItem(String name, String mobno, String total, List<InventoryItem> itemsbought) {
        this.name = name;
        this.mobno = mobno;
        this.total = total; 
        this.itemsbought = itemsbought;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobno() {
        return mobno;
    }
    public void setMobno(String mobno) {
        this.mobno = mobno;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    } 
    public List<InventoryItem> getItemsbought() {
        return itemsbought;
    }
    public void setItemsbought(List<InventoryItem> itemsbought) {
        this.itemsbought = itemsbought;
    }
 
}
