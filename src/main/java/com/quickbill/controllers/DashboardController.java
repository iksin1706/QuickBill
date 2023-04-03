package com.quickbill.controllers;


import com.quickbill.models.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DashboardController {

    @FXML
    private Label totalIncome;

    @FXML
    private Label totalInvoicesAmount;

    @FXML
    private Label totalInvoicesTaxes;

    private MainController mainController;

    private int amount;
    private double income;
    private double taxes;
    @FXML
    private Button allTime;
    @FXML
    private Button thisMonth;
    @FXML
    private Button thisYear;
    private ObservableList<Invoice> invoicesList;
    @FXML
    AreaChart lineChart;

    //Changing statistics to all time starting from first invoice
    @FXML
    void changeToAllTime() {
        allToWhite();
        allTime.setStyle("-fx-background-color:  #0077b6;");
        allTime.setTextFill(Color.web("#ffffff"));
        invoicesList = mainController.getInvoices();
        updateStats();
        updateChartFullTime();
        System.out.println(invoicesList.toString());
    }
    //Changing statistics to this month
    @FXML
    void changeToThisMonth() {
        allToWhite();
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        thisMonth.setStyle("-fx-background-color:  #0077b6;");
        thisMonth.setTextFill(Color.web("#ffffff"));
        invoicesList = FXCollections.observableList(mainController.getInvoices().stream()
                .filter(i -> i.getDate().getMonthValue() == currentMonth && i.getDate().getYear() == currentYear)

                .collect(Collectors.toList()));

        updateStats();
        updateChartMonth();
        System.out.println(invoicesList.toString());
    }
    //changing statistics to this year
    @FXML
    void changeToThisYear() {
        allToWhite();
        thisYear.setStyle("-fx-background-color:  #0077b6;");
        thisYear.setTextFill(Color.web("#ffffff"));
        int currentYear = LocalDateTime.now().getYear();
        invoicesList = FXCollections.observableList(mainController.getInvoices().stream()
                .filter(i -> i.getDate().getYear() == currentYear)
                .collect(Collectors.toList()));
        updateStats();
        updateChartYear();
        System.out.println(invoicesList.toString());
    }
    //changing all buttons to default
    public void allToWhite() {
        allTime.setStyle("-fx-background-color:  #cccccc;");
        allTime.setTextFill(Color.web("#0077b6"));
        thisYear.setStyle("-fx-background-color:  #cccccc;");
        thisYear.setTextFill(Color.web("#0077b6"));
        thisMonth.setStyle("-fx-background-color:  #cccccc;");
        thisMonth.setTextFill(Color.web("#0077b6"));
    }
    //setup for this controller and setting maincontroller
    public void setup(MainController mainController) {
        this.mainController = mainController;
        invoicesList = mainController.getInvoices();

        int redColor = 0, greenColor = 127, blueColor = 195;
        double opacity = 0.3;
        lineChart.setStyle("CHART_COLOR_1: rgb(" + redColor + "," + greenColor + "," + blueColor + "); "
                + "CHART_COLOR_1_TRANS_20: rgba(" + redColor + "," + greenColor + "," + blueColor + "," + opacity + ");");
        changeToThisYear();
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
    }
    //updates char to every month of this year
    public void updateChartYear() {
        lineChart.getData().clear();
        // Create a map to store the total brutto for each month
        Map<String, Double> totalBruttoByMonth = new HashMap<>();
        for (Invoice invoice : invoicesList) {
            String monthName = invoice.getDate().getMonth().name();
            if (totalBruttoByMonth.containsKey(monthName)) {
                totalBruttoByMonth.put(monthName, totalBruttoByMonth.get(monthName) + invoice.getTotalBrutto());
            } else {
                totalBruttoByMonth.put(monthName, invoice.getTotalBrutto());
            }
        }

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for (int i = 1; i <= 12; i++) {
            // Use the month as the X-axis value
            String monthName = Month.of(i).name();
            double totalBrutto = totalBruttoByMonth.getOrDefault(monthName, 0.0);
            series.getData().add(new XYChart.Data<>(monthName, totalBrutto));
        }
        lineChart.getData().add(series);
        System.out.println("Wykres");
    }
    //updates chart to all days of this month
    public void updateChartMonth() {
        lineChart.getData().clear();
        int amountOfDays=LocalDate.now().getMonth().length(LocalDate.now().isLeapYear());
        List<LocalDate> daysOfMonth = IntStream.rangeClosed(1, amountOfDays)
                .mapToObj(day -> LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), day))
                .collect(Collectors.toList());

        Map<LocalDate, Double> totalBruttoByDay = new HashMap<>();
        for (Invoice invoice : invoicesList) {
            LocalDate date = invoice.getDate();
            if (totalBruttoByDay.containsKey(date)) {
                totalBruttoByDay.put(date, totalBruttoByDay.get(date) + invoice.getTotalBrutto());
            } else {
                totalBruttoByDay.put(date, invoice.getTotalBrutto());
            }
        }

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for (LocalDate day : daysOfMonth) {
            // Use the day of the month as the X-axis value
            double totalBrutto = totalBruttoByDay.getOrDefault(day, 0.0);
            series.getData().add(new XYChart.Data<>(day.getDayOfMonth() + "", totalBrutto));
        }
        lineChart.getData().add(series);
    }
    //updates chart
    public void updateChartFullTime() {
        lineChart.getData().clear();
        int oldestYear = invoicesList.stream()
                .mapToInt(invoice -> invoice.getDate().getYear())
                .min()
                .orElse(LocalDate.now().getYear());
        int newestYear = invoicesList.stream()
                .mapToInt(invoice -> invoice.getDate().getYear())
                .max()
                .orElse(LocalDate.now().getYear());

        // Create a map to store the total brutto for each year
        Map<Integer, Double> totalBruttoByYear = new HashMap<>();
        for (Invoice invoice : invoicesList) {
            int year = invoice.getDate().getYear();
            if (totalBruttoByYear.containsKey(year)) {
                totalBruttoByYear.put(year, totalBruttoByYear.get(year) + invoice.getTotalBrutto());
            } else {
                totalBruttoByYear.put(year, invoice.getTotalBrutto());
            }
        }

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for (int year = oldestYear; year <= newestYear; year++) {
            // Use the year as the X-axis value
            double totalBrutto = totalBruttoByYear.getOrDefault(year, 0.0);
            series.getData().add(new XYChart.Data<>(year + "", totalBrutto));
        }
        lineChart.getData().add(series);
    }

    //updates numeric statistics
    public void updateStats() {
        invoicesList.sort(Comparator.comparing(Invoice::getDate));
        totalInvoicesAmount.setText(Integer.toString(getAmount()));
        totalInvoicesTaxes.setText(Double.toString(getTaxes()) + " PLN");
        totalIncome.setText(Double.toString(getIncome()) + " PLN");
    }
    //returns total paid taxes
    public double getTaxes() {
        return invoicesList.stream()
                .mapToDouble(invoice -> invoice.getTotalTaxes())
                .sum();
    }
    //returns total income
    public double getIncome() {
        return invoicesList.stream()
                .mapToDouble(invoice -> invoice.getTotalBrutto())
                .sum();
    }
    //returns total amount of invoices
    public int getAmount() {
        return invoicesList.size();
    }
}
