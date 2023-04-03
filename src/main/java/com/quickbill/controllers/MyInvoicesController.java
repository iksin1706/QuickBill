package com.quickbill.controllers;

import com.quickbill.models.Invoice;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyInvoicesController {

    @FXML
    private TableColumn<Invoice, Double> columnBrutto;
    @FXML
    private TableColumn<Invoice, String> columnCustomer;
    @FXML
    private TableColumn<Invoice, LocalDate> columnDate;
    @FXML
    private TableColumn<Invoice, Double> columnNetto;
    @FXML
    private TableColumn<Invoice, String> columnNumber;
    @FXML
    private TableColumn<Invoice, Boolean> columnPaid;
    @FXML
    private TableView<Invoice> invoicesTable;
    @FXML
    private TableColumn<Invoice, Invoice> columnDelete;
    @FXML
    private TableColumn<Invoice, Invoice> columnEdit;
    @FXML
    private TableColumn<Invoice, Invoice> columnPDF;
    private MainController mainController;
    @FXML
    private TextField customerFileter;
    @FXML
    private DatePicker dateFielter;
    @FXML
    private Button fielterButton;
    @FXML
    private TextField numberFilelter;
    @FXML
    private TextField priceFielter;
    private ObservableList<Invoice> fieltredList;

    @FXML
    void fielterInvoices(MouseEvent event) {
        fieltredList = mainController.getInvoices();
        if (numberFilelter != null && !numberFilelter.getText().isEmpty())
            fieltredList = FXCollections.observableList(fieltredList.stream()
                    .filter(i -> i.getInvoiceNumber().contains(numberFilelter.getText()))
                    .collect(Collectors.toList()));

        if (dateFielter.getValue() != null)
            fieltredList = FXCollections.observableList(fieltredList.stream()
                    .filter(i -> i.getDate().equals(dateFielter.getValue()))
                    .collect(Collectors.toList()));

        if (priceFielter != null && !priceFielter.getText().isEmpty())
            fieltredList = FXCollections.observableList(fieltredList.stream()
                    .filter(i -> Double.compare(i.getTotalNetto(), Double.parseDouble(priceFielter.getText())) == 0)
                    .collect(Collectors.toList()));
        if (customerFileter != null && !customerFileter.getText().isEmpty())
            fieltredList = FXCollections.observableList(fieltredList.stream()
                    .filter(i -> i.getCustomerName().contains(customerFileter.getText()))
                    .collect(Collectors.toList()));

        invoicesTable.setItems(fieltredList);
    }

    @FXML
    void clearFielters(MouseEvent event) {
        invoicesTable.setItems(mainController.getInvoices());
        customerFileter.setText("");
        numberFilelter.setText("");
        dateFielter.setValue(null);
        priceFielter.setText("");
    }
    //setting up javafx controllors and adding buttons to tableview
    public void initialize() {
        columnNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnPaid.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
        columnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnNetto.setCellValueFactory(new PropertyValueFactory<>("totalNetto"));
        columnBrutto.setCellValueFactory(new PropertyValueFactory<>("totalBrutto"));

        columnDelete.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        columnDelete.setCellFactory(param -> new TableCell<Invoice, Invoice>() {
            private final Button deleteButton = new Button("usuń");

            @Override
            protected void updateItem(Invoice invoice, boolean empty) {
                super.updateItem(invoice, empty);

                if (invoice == null) {
                    setGraphic(null);
                    return;
                }
                deleteButton.setStyle("-fx-background-color: #ff0000;");
                deleteButton.setTextFill(Color.web("#ffffff"));
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Usuwanie faktury");
                            alert.setHeaderText("Czy napewno chcesz usunąć fakture: " + invoice.getInvoiceNumber());

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                deleteInvoice(invoice);
                            }
                        }
                );
            }
        });
        columnEdit.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())

        );
        columnEdit.setCellFactory(param -> new TableCell<Invoice, Invoice>() {
            private final Button editButton = new Button("edytuj");

            @Override
            protected void updateItem(Invoice invoice, boolean empty) {
                super.updateItem(invoice, empty);

                if (invoice == null) {
                    setGraphic(null);
                    return;
                }
                editButton.setStyle("-fx-background-color: #023E8A;");
                editButton.setTextFill(Color.web("#ffffff"));
                setGraphic(editButton);
                editButton.setOnAction(
                        event -> mainController.editInvoice(invoice)
                );
            }
        });

        columnPDF.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        columnPDF.setCellFactory(param -> new TableCell<Invoice, Invoice>() {
            private final Button pdfButton = new Button("PDF");

            @Override
            protected void updateItem(Invoice invoice, boolean empty) {
                super.updateItem(invoice, empty);

                if (invoice == null) {
                    setGraphic(null);
                    return;
                }
                pdfButton.setStyle("-fx-background-color: #023E8A;");
                pdfButton.setTextFill(Color.web("#ffffff"));
                setGraphic(pdfButton);
                pdfButton.setOnAction(
                        event -> mainController.generatePDF(invoice)
                );
            }
        });
    }

    public void setup(MainController main) {
        this.mainController = main;
        invoicesTable.setItems(mainController.getInvoices());
    }
    //deleting invoice in maincontroller
    public void deleteInvoice(Invoice invoice) {
        mainController.deleteInvoice(invoice);
        mainController.loadInvoices();
        invoicesTable.setItems(mainController.getInvoices());
    }
}

