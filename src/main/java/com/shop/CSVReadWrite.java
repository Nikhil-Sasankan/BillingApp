package com.shop;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter; 

public class CSVReadWrite {
    static String[] HEADERS = { "name", "quantity", "price", "description" }; 
    static String[] HEADERS2 = { "","name", "quantity", "price", "total price"};
    public static final String csvtiemspath = "C:/BILLERDATA/";
    public static String itemsname = csvtiemspath+"items.csv"; 

    public List<InventoryItem> getItemDetails() {
        List<InventoryItem> itemlists = new ArrayList<>();
        // CSVParser parser;
        try (CSVReader reader = new CSVReader(new FileReader(itemsname))) {
            String[] nextLine;
            boolean skipfirst = true;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                if (skipfirst) { 
                    skipfirst = false;
                    continue;
                }
                itemlists.add(new InventoryItem(nextLine[0] , Integer.parseInt(nextLine[1] ),
                          Double.parseDouble(nextLine[2] ), nextLine[3] )); 
            } 
        } catch (Exception e) {
            e.getCause();
        }
        return itemlists;
    }

    public void writetoCSV(List<InventoryItem> ls) throws IOException { 
        try (CSVWriter writer = new CSVWriter(new FileWriter(itemsname))) {
            writer.writeNext(HEADERS);
            for (InventoryItem inv : ls) {
                String[] data = { inv.getName(),inv.getQuantity()+"" , inv.getPrice()+"", inv.getDescription() };
                writer.writeNext(data);   
            }  
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void writeBilltoCSV(BillingItem bl) throws IOException { 
        // Create a new file based on Date
        LocalDateTime now = LocalDateTime.now();
        // Define a formatter for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a dd-MM-yyyy");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("HH_mm_dd-MM-yyyy");
        // Format the current date and time as a string
        String formattedDateTime = now.format(formatter);
        String formattedDateTimeDate = now.format(formatterDate);
        String formattedDateTimeDateTime = now.format(formatterDateTime);


        File directory = new File(csvtiemspath+formattedDateTimeDate);
        if(!directory.exists()){
            directory.mkdir();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(directory+"/"+bl.getName()+"_"+formattedDateTimeDateTime+".csv"))) {
            writer.writeNext(new String[] {"FRIENDS BAKERY"});
            writer.writeNext(new String[] {"",""});
            writer.writeNext(new String[] {"Name : ",bl.getName()});
            writer.writeNext(new String[] {"Mob  : ",bl.getMobno()});
            writer.writeNext(new String[] {"Time : ",formattedDateTime});
            writer.writeNext(new String[] {"",""});
            writer.writeNext(new String[] {"ITEMS :"});
            writer.writeNext(HEADERS2);
            for (InventoryItem inv : bl.getItemsbought()) {
                String[] data = {"", inv.getName(),inv.getQuantity()+"" , inv.getPrice()+"", inv.getItemtotal()+"" };
                writer.writeNext(data);   
            }  
            writer.writeNext(new String[] {"",""});
            writer.writeNext(new String[] {"","","TOTAL PRICE","",bl.getTotal()+""});
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}