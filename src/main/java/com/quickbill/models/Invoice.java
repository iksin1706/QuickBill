package com.quickbill.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Customer seller;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "is_paid")
    private boolean isPaid;
    @Column(name = "is_transfer")
    private boolean isTransferPayment;
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "totalBrutto")
    private double totalBrutto;

    @Column(name = "totalNetto")
    private double totalNetto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval=true, mappedBy = "invoice")
    private List<InvoiceItem> items;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public ObservableList<InvoiceItem> getItems() {
        return FXCollections.observableArrayList(items);
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isTransferPayment() {
        return isTransferPayment;
    }

    public void setTransferPayment(boolean transferPayment) {
        isTransferPayment = transferPayment;
    }

    public void setItems(ObservableList<InvoiceItem> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        assignTotals();
    }

    public Invoice() {

    }
    public Invoice(User user){
        this.user=user;
        this.seller=new Customer();
        this.customer=new Customer();
        this.date=LocalDate.now();
        this.invoiceNumber="FV/"+date.getYear()+"/"+date.getMonthValue()+"/";
        this.items = FXCollections.observableArrayList(new ArrayList<InvoiceItem>());
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Customer getSeller() {
        return seller;
    }

    public void setSeller(Customer seller) {
        this.seller = seller;
    }

    public Customer getCustomer() {
        return customer;
    }
    public String getCustomerName() {
        return customer.getName();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void addItem(String name,double price,double tax,int amount,Invoice invoice){
        items.add(new InvoiceItem(name,price,tax,amount,invoice));
        assignTotals();
    }
    public String toString(){
        return invoiceNumber+" "+date+" "+" "+seller.getName();
    }
    public double getTotalBrutto(){
        return  Math.round(items.stream()
                .mapToDouble(invoiceItem -> invoiceItem.getPriceBrutto())
                .sum()*100.0)/100.0;

    }
    public double getTotalNetto(){
        return   Math.round(items.stream()
                .mapToDouble(invoiceItem -> invoiceItem.getPriceNetto())
                .sum()*100.0)/100.0;
    }
    public double getTotalTaxes(){
        return   Math.round(items.stream()
                .mapToDouble(invoiceItem -> invoiceItem.getTaxesCost())
                .sum()*100.0)/100.0;
    }
    private void assignTotals(){
        totalBrutto=getTotalBrutto();
        totalNetto=getTotalNetto();
    }
}