package com.shop;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List; 

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter; 

public class CSVReadWrite {
    static String[] HEADERS = { "name", "quantity", "price", "description" };
    public static String csvtiemspath = "/items.csv";

    public List<InventoryItem> getItemDetails() {
        List<InventoryItem> itemlists = new ArrayList<>();
        // CSVParser parser;
        try (CSVReader reader = new CSVReader(new FileReader(csvtiemspath))) {
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvtiemspath))) {
            writer.writeNext(HEADERS);
            for (InventoryItem inv : ls) {
                String[] data = { inv.getName(),inv.getQuantity()+"" , inv.getPrice()+"", inv.getDescription() };
                writer.writeNext(data);   
            }  
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}