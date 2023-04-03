package com.quickbill.controllers;

import com.quickbill.models.Invoice;
import com.quickbill.models.InvoiceItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;

public class ItemsController {

    @FXML
    private TableColumn<InvoiceItem, Integer> columnAmount;
    @FXML
    private TableColumn<InvoiceItem, String> columnName;
    @FXML
    private TableColumn<InvoiceItem, Double> columnPrice;
    @FXML
    private TableColumn<InvoiceItem, Double> columnPriceBrutto;
    @FXML
    private TableColumn<InvoiceItem, Double> columnPriceCost;
    @FXML
    private TableColumn<InvoiceItem, Double> columnPriceNetto;
    @FXML
    private TableColumn<InvoiceItem, Double> columnTaxPercentage;
    @FXML
    private TextField itemAmout;
    @FXML
    private TextField itemName;
    @FXML
    private TextField itemPrice;

    @FXML
    private TableView<InvoiceItem> itemsTable;
    @FXML
    private TextField taxPercentage;
    @FXML
    private Button addInvoiceButton;
    @FXML
    private Button generatePdfButton;
    @FXML
    private Label header;
    @FXML
    private TableColumn<InvoiceItem, InvoiceItem> columnDelete;
    @FXML
    private Label infoLabel;
    private MainController mainController;
    private ObservableList<InvoiceItem> items;
    private Invoice invoice;
    private boolean editingMode;

    //setting up javafxcontrols
    public void initialize() {
        items = FXCollections.observableArrayList(new ArrayList<InvoiceItem>());
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnTaxPercentage.setCellValueFactory(new PropertyValueFactory<>("taxesPercentage"));
        columnPriceCost.setCellValueFactory(new PropertyValueFactory<>("taxesCost"));
        columnPriceNetto.setCellValueFactory(new PropertyValueFactory<>("priceNetto"));
        columnPriceBrutto.setCellValueFactory(new PropertyValueFactory<>("priceBrutto"));
        columnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())

        );

        columnName.setCellFactory(TextFieldTableCell.<InvoiceItem>forTableColumn());
        columnName.setOnEditCommit((TableColumn.CellEditEvent<InvoiceItem, String> t) -> {
            try {
            String newValue = t.getNewValue();
            ((InvoiceItem) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(newValue);
            itemsTable.refresh();
        }catch (Exception e){}
        });

        columnAmount.setCellFactory(TextFieldTableCell.<InvoiceItem, Integer>forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                try {
                    return super.fromString(value);
                } catch(NumberFormatException e) {
                    return null;
                }
            }
        }));
        columnAmount.setOnEditCommit((TableColumn.CellEditEvent<InvoiceItem, Integer> t) -> {
            if(t.getNewValue()!=null&& isInteger(t.getNewValue().toString())) {
                int newValue = t.getNewValue();
                ((InvoiceItem) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAmount(newValue);
            }
            itemsTable.refresh();
        });

        columnPrice.setCellFactory(TextFieldTableCell.<InvoiceItem, Double>forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String value) {
                try {
                    return super.fromString(value);
                } catch(NumberFormatException e) {
                    return Double.NaN;
                }
            }
        }));
        columnPrice.setOnEditCommit((TableColumn.CellEditEvent<InvoiceItem, Double> t) -> {
            if(!t.getNewValue().equals(Double.NaN)&&isDouble(t.getNewValue().toString())) {
                double newValue = Math.round(t.getNewValue() * 100.0) / 100.0;
                ((InvoiceItem) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice(newValue);
            }
            itemsTable.refresh();
        });

        columnTaxPercentage.setCellFactory(TextFieldTableCell.<InvoiceItem, Double>forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String value) {
                try {
                    return super.fromString(value);
                } catch(NumberFormatException e) {
                    return Double.NaN;
                }
            }
        }));
        columnTaxPercentage.setOnEditCommit((TableColumn.CellEditEvent<InvoiceItem, Double> t) -> {
            if(!t.getNewValue().equals(Double.NaN)&&isDouble(t.getNewValue().toString())) {
                double newValue = Math.round(t.getNewValue() * 100.0) / 100.0;
                ((InvoiceItem) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTaxesPercentage(newValue);
            }
            itemsTable.refresh();
        });

        columnDelete.setCellFactory(param -> new TableCell<InvoiceItem, InvoiceItem>() {
            private final Button deleteButton = new Button("usuń");

            @Override
            protected void updateItem(InvoiceItem invoiceItem, boolean empty) {
                super.updateItem(invoiceItem, empty);

                if (invoiceItem == null) {
                    setGraphic(null);
                    return;
                }
                deleteButton.setStyle("-fx-background-color: #ff0000;");
                deleteButton.setTextFill(Color.web("#ffffff"));
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> getTableView().getItems().remove(invoiceItem));
            }
        });
    }

    //adding item to list
    @FXML
    public void addItem(MouseEvent event) {
        if(items.size()<100) {
            infoLabel.setVisible(false);
            itemPrice.setStyle("-fx-border-color: none");
            taxPercentage.setStyle("-fx-border-color: none");
            itemAmout.setStyle("-fx-border-color: none");
            if (!isValid()) return;
            String name = itemName.getText();
            double price = Double.parseDouble(itemPrice.getText());
            price = Math.round(price * 100.0) / 100.0;
            double tax = Double.parseDouble(taxPercentage.getText());
            tax = Math.round(tax * 100.0) / 100.0;
            int amount = Integer.parseInt(itemAmout.getText());
            InvoiceItem item = new InvoiceItem(name, price, tax, amount, invoice);
            this.items.add(item);
            System.out.println(invoice.getInvoiceId());
        }else{
            infoLabel.setVisible(true);
        }

    }

    //logic setup and setting maincontroler
    public void setup(MainController main, boolean editingMode) {
        this.mainController = main;
        this.editingMode = editingMode;
        invoice = editingMode ? mainController.getEditInvoice() : mainController.getInvoice();

        items = invoice.getItems();
        itemsTable.setItems(items);
        if (editingMode) {
            header.setText("Edytuj towary/usługi");
        } else {
            header.setText("Dodaj towary/usługi");
        }


    }
    //adding invoice to maincontroller
    @FXML
    public void addInvoice(MouseEvent event) {
        System.out.println(items);
        if (itemsTable.getItems().size() > 0) {
            if (editingMode) {
                invoice.setItems(items);
                mainController.updateInvoice(invoice);
                mainController.changeToMyInvoices();
            } else {
                invoice.setItems(items);
                mainController.addInvoice(invoice);
                mainController.changeToMyInvoices();
            }
        } else {
            itemPrice.setStyle("-fx-border-color: #ff0000");
            taxPercentage.setStyle("-fx-border-color: #ff0000");
            itemAmout.setStyle("-fx-border-color: #ff0000");
        }
    }
    //changing page to template where you can generate pdf
    @FXML
    public void generatePdf(MouseEvent event) {
        if(itemsTable.getItems().size() > 0) {
            if (editingMode){
                invoice.setItems(items);
                mainController.updateInvoice(invoice);
                this.mainController.changeToEditingPage("template");
            }
            else{
                invoice.setItems(items);
                mainController.addInvoice(invoice);
                this.mainController.changeToTemplate(event);
            }
        }
    }
    //checking is string double
    public boolean isDouble(String number) {
        boolean valid = true;
        if (number == null) valid = false;
        try {
            double l =  Double.parseDouble(number);
            if(l<0)valid=false;
        } catch (Exception e) {
            valid = false;
        }


        return valid;
    }
    //checking is string long
    public boolean isInteger(String number) {
        boolean valid = true;
        if (number == null) valid = false;
        try {
            double l = Integer.parseInt(number);
            if(l<0)valid=false;
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }
    //checking is provided data valid
    public boolean isValid() {
        boolean valid = true;
        if (itemName.getText() == null || itemName.getText().isEmpty()) {
            itemName.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (itemPrice.getText() == null || !isDouble(itemPrice.getText())) {
            itemPrice.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (taxPercentage.getText() == null || !isDouble(taxPercentage.getText())) {
            taxPercentage.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (itemAmout.getText() == null || !isInteger(itemAmout.getText())) {
            itemAmout.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        return valid;
    }
}
