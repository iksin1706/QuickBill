package com.quickbill.controllers;

import com.quickbill.models.Address;
import com.quickbill.models.Customer;
import com.quickbill.models.Invoice;
import com.quickbill.models.User;
import com.quickbill.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {

    @FXML
    private Parent root;
    @FXML
    private StackPane frame;
    @FXML
    private Button main;
    @FXML
    private Button myInvoices;
    @FXML
    private Button newInvoice;
    @FXML
    private Label usernameLabel;
    @FXML
    private Pane topBar;
    private Invoice invoice;


    private Invoice editInvoice;
    private ObservableList<Invoice> invoices;
    private User user;
    private DbService dbService;
    private WindowController windowController;

    private double x, y;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getEditInvoice() {
        return editInvoice;
    }

    public void setEditInvoice(Invoice editInvoice) {
        this.editInvoice = editInvoice;
    }

    public void setInvoices(ObservableList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public DbService getDbService() {
        return dbService;
    }

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    //setting variables and logic of this controller
    public void setUp(DbService dbService, WindowController windowController, User user) {
        this.dbService = dbService;
        this.windowController = windowController;
        this.user = user;
        usernameLabel.setText(user.getUsername());
        invoice = new Invoice(user);
        invoices = FXCollections.observableList(new ArrayList<Invoice>());
        System.out.println(user.getUsername());
        loadInvoices();
        changeToDashboard();

        //adding move on drag
/*
        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });

        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Pane) event.getSource()).getScene().getWindow().setX(event.getScreenX() - x);
                ((Pane) event.getSource()).getScene().getWindow().setY(event.getScreenY() - y);
            }
        });

 */
    }
    //changing page of main to provided fxml
    public void changePage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
            root = loader.load();
            if (fxml.equals("newInvoice")) {
                NewInvoiceController controller = loader.getController();
                controller.setup(this, false);
            }
            if (fxml.equals("items")) {
                ItemsController controller = loader.getController();
                controller.setup(this, false);
            }
            if (fxml.equals("template")) {
                TemplateController controller = loader.getController();
                controller.setup(this, false);
            }
            if (fxml.equals("myInvoices")) {
                MyInvoicesController controller = loader.getController();
                controller.setup(this);
            }
            if (fxml.equals("dashboard")) {
                DashboardController controller = loader.getController();
                controller.setup(this);
                System.out.println("Tu");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.getChildren().clear();
        frame.getChildren().addAll(root);
    }
    //chaging to page but with editingMode enabled
    public void changeToEditingPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
            root = loader.load();
            if (fxml.equals("newInvoice")) {
                NewInvoiceController controller = loader.getController();
                controller.setup(this, true);
            }
            if (fxml.equals("items")) {
                ItemsController controller = loader.getController();
                controller.setup(this, true);
            }
            if (fxml.equals("template")) {
                TemplateController controller = loader.getController();
                controller.setup(this, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.getChildren().clear();
        frame.getChildren().addAll(root);
    }

    //changing to page with updating buttons
    @FXML
    void changeToDashboard() {
        main.setStyle("-fx-background-color: #023E8A;");
        myInvoices.setStyle("-fx-background-color: #01014A;");
        newInvoice.setStyle("-fx-background-color: #01014A;");

        changePage("dashboard");
    }

    @FXML
    void changeToMyInvoices() {
        main.setStyle("-fx-background-color: #01014A");
        newInvoice.setStyle("-fx-background-color: #01014A;");
        myInvoices.setStyle("-fx-background-color: #023E8A;");

        changePage("myInvoices");
    }

    @FXML
    void changeToNewInvoice() {
        main.setStyle("-fx-background-color: #01014A;");
        myInvoices.setStyle("-fx-background-color: #01014A;");
        newInvoice.setStyle("-fx-background-color: #023E8A;");
        this.invoice = new Invoice(user);
        changePage("newInvoice");
    }

    @FXML
    void changeToItems(MouseEvent event) {
        changePage("items");
    }

    @FXML
    void changeToTemplate(MouseEvent event) {
        changePage("template");
    }

    //closing app
    @FXML
    void exitApp(MouseEvent event) {
        System.exit(0);
    }

    //minimazing app
    @FXML
    void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    //logout current user
    @FXML
    void logout(MouseEvent event) {
        windowController.changeWindow("login");
    }

    //adding invoice to list and database
    public void addInvoice(Invoice invoice) {
        this.invoice=invoice;
        invoices.add(invoice);
        dbService.addInvoice(invoice);
    }

    public ObservableList<Invoice> getInvoices() {
        return invoices;
    }

    @FXML
    public void editInvoice(Invoice invoice) {
        editInvoice = invoice;
        changeToEditingPage("newInvoice");
    }

    //updating invoice data
    public void updateInvoice(Invoice invoice) {
        dbService.editInvoice(editInvoice);
    }

    //loading all invoices form database
    public void loadInvoices() {
        invoices = FXCollections.observableList(dbService.getInvoices(user.getId()));
    }

    //deleting invoice from database
    public void deleteInvoice(Invoice invoice) {
        dbService.deleteInvoice(invoice);
    }

    //changing to page thats generating template
    public void generatePDF(Invoice invoice) {
        this.editInvoice = invoice;
        changeToEditingPage("template");
    }
    public Customer getCustomer(long nip){
        Customer customer= dbService.getCustomer(nip);
        Address address=customer.getAddress();
        if(customer!=null) {
            return new Customer(customer.getName(), customer.getNIP(),new Address(address.getStreet(), address.getPostCode(), address.getTown()));
        } else return null;
    }
}
