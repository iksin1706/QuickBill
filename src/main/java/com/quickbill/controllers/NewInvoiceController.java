package com.quickbill.controllers;

import com.quickbill.helpers.NipValidator;
import com.quickbill.helpers.PostCodeValidator;
import com.quickbill.models.Customer;
import com.quickbill.models.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;

public class NewInvoiceController {

    private MainController mainController;
    private Invoice invoice;
    @FXML
    private TextField cAddress;
    @FXML
    private TextField cCity;
    @FXML
    private TextField cNIP;
    @FXML
    private TextField cName;
    @FXML
    private TextField cPostCode;
    @FXML
    private DatePicker invoiceDate;
    @FXML
    private TextField invoiceNumber;
    @FXML
    private TextField sAddress;
    @FXML
    private TextField sCity;
    @FXML
    private TextField sNIP;
    @FXML
    private TextField sName;
    @FXML
    private TextField sPostCode;
    @FXML
    private CheckBox isPaid;
    @FXML
    private CheckBox isTransfer;
    @FXML
    private Label header;
    @FXML
    private Label infoLabel;
    private boolean editingMode;

    public void initialize() {
        invoiceDate.setChronology(IsoChronology.INSTANCE);
        invoiceDate.setShowWeekNumbers(false);
    }

    //changing page to items page
    @FXML
    public void changeToItems(MouseEvent event) {
        if (!isValid()) return;
        invoice.setDate(invoiceDate.getValue());
        invoice.setInvoiceNumber(invoiceNumber.getText().trim());
        invoice.setTransferPayment(isTransfer.isSelected());
        invoice.setPaid(isPaid.isSelected());
        invoice.getCustomer().getAddress().setStreet(cAddress.getText().trim());
        invoice.getCustomer().setNIP(Long.parseLong(cNIP.getText().trim()));
        invoice.getCustomer().getAddress().setPostCode(cPostCode.getText().trim());
        invoice.getCustomer().getAddress().setTown(cCity.getText().trim());
        invoice.getSeller().setNIP(Long.parseLong(sNIP.getText().trim()));
        invoice.getSeller().getAddress().setPostCode(sPostCode.getText().trim());
        invoice.getSeller().getAddress().setTown(sCity.getText().trim());
        invoice.getCustomer().setName(cName.getText().trim());
        invoice.getSeller().getAddress().setStreet(sAddress.getText().trim());
        invoice.getSeller().setName(sName.getText().trim());
        if (editingMode) mainController.changeToEditingPage("items");
        else
            mainController.changeToItems(event);
    }
    //checking is all provided data is valid and not empty
    public boolean isValid() {

        boolean valid = true;
        resetValidatorBorders();
        if (invoiceDate.getValue() == null) {
            invoiceDate.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (invoiceNumber.getText() == null||invoiceNumber.getText().trim().isEmpty()) {
            invoiceNumber.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (cAddress.getText() == null||cAddress.getText().trim().isEmpty()) {
            cAddress.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (!NipValidator.isValid(cNIP.getText())) {
            cNIP.setStyle("-fx-border-color: #ff0000");
            infoLabel.setVisible(true);
            infoLabel.setText("Numer NIP musi składać się z 10 cyfr");
            valid = false;
        }
        if (cCity.getText() == null||cCity.getText().trim().isEmpty()) {
            cCity.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (cName.getText() == null||cName.getText().trim().isEmpty()) {
            cName.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (!PostCodeValidator.isValid(cPostCode.getText())) {
            cPostCode.setStyle("-fx-border-color: #ff0000");
            infoLabel.setVisible(true);
            infoLabel.setText("Nieprawidłowy kod pocztowy.Oto przykład jak powinien wyglądać: 12-123");
            valid = false;
        }
        if (sAddress.getText() == null||sAddress.getText().trim().isEmpty()) {
            sAddress.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (!NipValidator.isValid(sNIP.getText())) {
            sNIP.setStyle("-fx-border-color: #ff0000");
            infoLabel.setVisible(true);
            infoLabel.setText("Numer NIP musi składać się z 10 cyfr");
            valid = false;
        }
        if (sCity.getText() == null||sCity.getText().trim().isEmpty()) {
            sCity.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (sName.getText() == null||sName.getText().trim().isEmpty()) {
            sName.setStyle("-fx-border-color: #ff0000");
            valid = false;
        }
        if (!PostCodeValidator.isValid(sPostCode.getText())) {
            sPostCode.setStyle("-fx-border-color: #ff0000");
            infoLabel.setVisible(true);
            infoLabel.setText("Nieprawidłowy kod pocztowy.Oto przykład jak powinien wyglądać: 12-123");
            valid = false;
        }
        return valid;
    }

    public void resetValidatorBorders(){
        infoLabel.setVisible(false);
        invoiceDate.setStyle("-fx-border-color: none");
        invoiceNumber.setStyle("-fx-border-color: none");
        cAddress.setStyle("-fx-border-color: none");
        cNIP.setStyle("-fx-border-color: none");
        cCity.setStyle("-fx-border-color: none");
        cName.setStyle("-fx-border-color: none");
        cPostCode.setStyle("-fx-border-color: none");
        sAddress.setStyle("-fx-border-color: none");
        sNIP.setStyle("-fx-border-color: none");
        sCity.setStyle("-fx-border-color: none");
        sName.setStyle("-fx-border-color: none");
        sPostCode.setStyle("-fx-border-color: none");
    }
    @FXML
    void resetBorder(KeyEvent event) {
        ((TextField)event.getSource()).setStyle("-fx-border-color: none");
    }


    //setting everything
    public void setup(MainController main, boolean editingMode) {
        this.mainController = main;
        this.editingMode = editingMode;
        if (!editingMode) {
            invoice = mainController.getInvoice();
            header.setText("Nowa faktura");
        } else {
            invoice = mainController.getEditInvoice();
            header.setText("Edytuj fakture " + invoice.getInvoiceNumber());
        }
        updateData();

        cNIP.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                invoice.setCustomer(mainController.getCustomer(Long.parseLong(cNIP.getText())));
                updateData();
            }
        } );
        sNIP.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                invoice.setSeller(mainController.getCustomer(Long.parseLong(sNIP.getText())));
                updateData();
            }

        } );
    }
    public void updateData(){
        LocalDate date = LocalDate.now();
        invoiceNumber.setText(invoice.getInvoiceNumber());
        invoiceDate.setValue(invoice.getDate());
        isPaid.setSelected(invoice.getIsPaid());
        isTransfer.setSelected(invoice.getIsPaid());
        cName.setText(invoice.getCustomer().getName());
        sName.setText(invoice.getSeller().getName());
        cAddress.setText(invoice.getCustomer().getAddress().getStreet());
        sAddress.setText(invoice.getSeller().getAddress().getStreet());
        cNIP.setText(invoice.getCustomer().getNIP() != 0 ? Long.toString(invoice.getCustomer().getNIP()) : "");
        sNIP.setText(invoice.getSeller().getNIP() != 0 ? Long.toString(invoice.getSeller().getNIP()) : "");
        cPostCode.setText(invoice.getCustomer().getAddress().getPostCode());
        sPostCode.setText(invoice.getSeller().getAddress().getPostCode());
        cCity.setText(invoice.getCustomer().getAddress().getTown());
        sCity.setText(invoice.getSeller().getAddress().getTown());
    }

    @FXML
    void autoFillCustomer(MouseEvent event) {
        try {
            Customer customer = mainController.getCustomer(Long.parseLong(cNIP.getText()));
            if (customer != null) {
                invoice.setCustomer(customer);
                updateData();
            }
        } catch (Exception e){

        }
    }

    @FXML
    void autoFillSeller(MouseEvent event) {
        try {
            Customer seller = mainController.getCustomer(Long.parseLong(sNIP.getText()));
            if (seller != null) {
                invoice.setSeller(seller);
                updateData();
            }
        } catch (Exception e){

        }
    }


}
