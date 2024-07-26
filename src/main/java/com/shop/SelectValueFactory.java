package com.shop;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SelectValueFactory
        implements Callback<TableColumn.CellDataFeatures<InventoryItem, CheckBox>, ObservableValue<CheckBox>> {
    @SuppressWarnings("exports")
    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<InventoryItem, CheckBox> param) {
        InventoryItem item = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(item.getSelected());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            item.setSelected(new_val);
        });
        return new SimpleObjectProperty<>(checkBox);
    }
}