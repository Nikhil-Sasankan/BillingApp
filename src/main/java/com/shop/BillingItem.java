package com.shop;

import java.util.Date;
import java.util.List;

public class BillingItem {
    private String name ;
    private String mobno ;
    private String total ;
    private Date shoppingtime ;
    private List<InventoryItem> itemsbought; 
    
    public BillingItem(String name, String mobno, String total, Date shoppingtime, List<InventoryItem> itemsbought) {
        this.name = name;
        this.mobno = mobno;
        this.total = total;
        this.shoppingtime = shoppingtime;
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
    public Date getShoppingtime() {
        return shoppingtime;
    }
    public void setShoppingtime(Date shoppingtime) {
        this.shoppingtime = shoppingtime;
    }
    public List<InventoryItem> getItemsbought() {
        return itemsbought;
    }
    public void setItemsbought(List<InventoryItem> itemsbought) {
        this.itemsbought = itemsbought;
    }
 
}
