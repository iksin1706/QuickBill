package com.quickbill.models;

import javax.persistence.*;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "taxesPercentage")
    private double taxesPercentage;

    @Column(name = "taxesCost")
    private double taxesCost;

    @Column(name = "amount")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "invoice_id",referencedColumnName = "invoice_id")
    private Invoice invoice;

    public double getTaxesPercentage() {
        return taxesPercentage;
    }

    public void setTaxesPercentage(double taxesPercentage) {
        this.taxesPercentage = taxesPercentage;
        updateTotals();
    }

    public double getTaxesCost() {
        return taxesCost;
    }

    public void setTaxesCost(double taxesCost) {
        this.taxesCost = taxesCost;
    }

    public double getPriceNetto() {
        return priceNetto;
    }

    public void setPriceNetto(double priceNetto) {
        this.priceNetto = priceNetto;
    }

    public double getPriceBrutto() {
        return priceBrutto;
    }

    public void setPriceBrutto(double priceBrutto) {
        this.priceBrutto = priceBrutto;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Column(name = "priceNetto")
    private double priceNetto;
    @Column(name = "priceBrutto")
    private double priceBrutto;

    public InvoiceItem() {
        amount=1;
    }

    public InvoiceItem(String name, double price, double taxes, int amount, Invoice invoice) {
        this.name = name;
        this.taxesPercentage = taxes;
        this.amount = amount;
        this.invoice = invoice;
        setPrice(price);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        updateTotals();
    }
    private void updateTotals(){
        this.priceNetto=price*amount;
        this.priceNetto=Math.round(priceNetto* 100.0) / 100.0;
        this.taxesCost=priceNetto*taxesPercentage/100;
        this.taxesCost=Math.round(taxesCost* 100.0) / 100.0;
        this.priceBrutto=priceNetto+taxesCost;
        this.priceBrutto=Math.round(priceBrutto* 100.0) / 100.0;
    }

    public double getTaxes() {
        return taxesPercentage;
    }

    public void setTaxes(double taxes) {
        this.taxesPercentage = taxes;
        updateTotals();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        updateTotals();
    }

    public Invoice getInvoice() {
        return invoice;
    }

}
