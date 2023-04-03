package com.quickbill.controllers;

import com.itextpdf.html2pdf.HtmlConverter;
import com.quickbill.models.Invoice;
import com.quickbill.models.InvoiceItem;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TemplateController {

    @FXML
    WebView webView;
    private MainController mainController;
    private Invoice invoice;
    private String html;
    private boolean editingMode;
    byte[] pdf;

    public boolean isEditingMode() {
        return editingMode;
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }

    @FXML
    void addInvoice(MouseEvent event) {
        generatePDF();
    }

    //setting html templete with invoice data
    public void setup(MainController mainController, boolean editingMode) {
        this.mainController = mainController;
        if (editingMode) invoice = mainController.getEditInvoice();
        else invoice = mainController.getInvoice();
        html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "\n" +
                "\n" +
                "    <!-- Favicon -->\n" +
                "    <link rel=\"icon\" href=\"./images/favicon.png\" type=\"image/x-icon\" />\n" +
                "\n" +
                "    <!-- Invoice styling -->\n" +
                "    <style>\n" +
                "        @page {\n" +
                "            size: A4;margin:0;top:0;left:0;transform:translateX(-50%)\n" +
                "        }\n" +
                "        @media print {\n" +
                "            #my-element {\n" +
                "                width: 18cm;\n" +
                "                min-height: 25cm;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        #invoice-box {\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "            text-align: center;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "\n" +
                "        body h1 {\n" +
                "            font-weight: 300;\n" +
                "            margin-bottom: 0px;\n" +
                "            padding-bottom: 0px;\n" +
                "            color: #000;\n" +
                "        }\n" +
                "\n" +
                ".items td{border:1px solid #aaa}" +
                "        body h3 {\n" +
                "            font-weight: 300;\n" +
                "            margin-top: 10px;\n" +
                "            margin-bottom: 20px;\n" +
                "            font-style: italic;\n" +
                "            color: #555;\n" +
                "        }\n" +
                "\n" +
                "        body a {\n" +
                "            color: #06f;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box {\n" +
                "            margin: auto;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);\n" +
                "            font-size: 14px;\n" +
                "            line-height: 24px;\n" +
                "            font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "            color: #555;\n" +
                "            width: 18cm;\n" +
                "            min-height: 25cm;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table {\n" +
                "            width: 100%;\n" +
                "            line-height: inherit;\n" +
                "            text-align: left;\n" +
                "            border-collapse: collapse;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table td {\n" +
                "            padding: 5px;\n" +
                "            vertical-align: top;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr td:nth-child(2) {\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.top table td {\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.top table td.title {\n" +
                "            font-size: 20px;\n" +
                "            line-height: 45px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.information table td {\n" +
                "            padding-bottom: 40px;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.heading td {\n" +
                "            background: #eee;\n" +
                "            border-bottom: 1px solid #ddd;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.details td {\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.item td {\n" +
                "            border-bottom: 1px solid #eee;\n" +
                "        }\n" +
                "\n" +
                "        .invoice-box table tr.item.last td {\n" +
                "            border-bottom: none;\n" +
                "        }\n" +
                "\n" +
                "        table tr.total td{\n" +
                "            border: none;\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (max-width: 600px) {\n" +
                "            .invoice-box table tr.top table td {\n" +
                "                width: 100%;\n" +
                "                display: block;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "\n" +
                "            .invoice-box table tr.information table td {\n" +
                "                width: 100%;\n" +
                "                display: block;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div class=\"invoice-box\">\n" +
                "        <table>\n" +
                "            <tr class=\"top\">\n" +
                "                <td colspan=\"2\">\n" +
                "                    <table>\n" +
                "                        <tr>\n" +
                "                            <td class=\"title\">\n" +
                "                                <h1>Faktura Vat</h1>\n" +
                "                            </td>\n" +
                "\n" +
                "                            <td>\n" +
                "                                Numer faktury:" + invoice.getInvoiceNumber() + "<br />\n" +
                "                                Wystawiona:" + invoice.getDate() + "<br />\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr class=\"information\">\n" +
                "                <td colspan=\"2\">\n" +
                "                    <table >\n" +
                "                        <tr>\n" +
                "                            <td>\n" +
                "                                Sprzedawca<br />\n" +
                "                                " + invoice.getSeller().getName() + "<br />\n" +
                "                                NIP:" + invoice.getSeller().getNIP() + " <br>\n" +
                "                                " + invoice.getSeller().getAddress().getStreet() + "<br>\n" +
                "                                " + invoice.getSeller().getAddress().getPostCode() + "<br>\n" +
                "                                " + invoice.getSeller().getAddress().getTown() + "\n" +
                "                            </td>\n" +
                "\n" +
                "                            <td>\n" +
                "                                Nabywca<br />\n" +
                "                                " + invoice.getCustomer().getName() + "<br />\n" +
                "                                NIP:" + invoice.getCustomer().getNIP() + " <br>\n" +
                "                                " + invoice.getCustomer().getAddress().getStreet() + "<br>\n" +
                "                                " + invoice.getCustomer().getAddress().getPostCode() + "<br>\n" +
                "                                " + invoice.getCustomer().getAddress().getTown() + "\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr class=\"heading\">\n" +
                "                <td>Metoda płatności</td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr class=\"details\">\n" +
                "                <td>" + ((invoice.isTransferPayment()) ? "Przelew" : "Gotówka") + "</td>\n" +
                "\n" +
                "                <td>" + ((invoice.getIsPaid()) ? "Zapłacono" : "Nie zapłacono") + "</td>\n" +
                "            </tr>\n" +
                "\n" + "        <table class=\"items\">\n" +
                "            <tr class=\"heading\">\n" +
                "                <td>Towar/usługa</td>\n" +
                "                <td>Cena netto</td>\n" +
                "                <td>Ilość[szt]</td>\n" +
                "                <td>Wartość netto</td>\n" +
                "                <td>VAT</td>\n" +
                "                <td>Kwota VAT</td>\n" +
                "                <td>Wartość brutto</td>\n" +
                "            </tr>\n";

        for (InvoiceItem item : invoice.getItems()) {
            html +=
                    "\n" +
                            "            <tr class=\"item\">\n" +
                            "                <td>" + item.getName() + "</td>\n" +
                            "                <td>" + item.getPrice() + "</td>\n" +
                            "                <td>" + item.getAmount() + "</td>\n" +
                            "                <td>" + item.getPriceNetto() + "</td>\n" +
                            "                <td>" + item.getTaxesPercentage() + "</td>\n" +
                            "                <td>" + item.getTaxesCost() + "</td>\n" +
                            "                <td>" + item.getPriceBrutto() + "</td>\n" +
                            "\n" +
                            "            </tr>\n";
        }
        html +=
                "            <tr class=\"total\">\n" +
                        "                <td> </td><td> </td><td> </td><td> </td><td> </td><td>W sumie</td><td>" + invoice.getTotalBrutto() + " PLN</td>\n" +
                        "\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>";

        webView.getEngine().loadContent(html);
        webView.setZoom(0.51);
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    //saving pdf
    public void generatePDF() {
        try {
            String documentsFolder = System.getProperty("user.home") + "\\Documents";

            String pdfFile = choosePath();

            HtmlConverter.convertToPdf(html, new FileOutputStream(pdfFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //choosing path for pdf to save
    public String choosePath() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName(invoice.getInvoiceNumber().replaceAll("/", ""));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        // Show the file chooser dialog
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            // return the path of the selected file
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
    //choosing printer
    public static PrintService choosePrinter() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if (printJob.printDialog()) {
            return printJob.getPrintService();
        } else {
            return null;
        }
    }
    //printing invoice with choosed printer
    @FXML
    void printInvoice(MouseEvent event) throws PrinterException, IOException {
        try {
            convertHTMLtoPDF();
            PDDocument document = PDDocument.load(pdf);
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService printer = choosePrinter();
            job.setPrintService(printer);
            job.setPageable(new PDFPageable(document));
            job.print();
        } catch (Exception e) {
        };
    }

    //converting html to pdf using itext library
    public void convertHTMLtoPDF() {
        ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, pdfOutput);
        pdf = pdfOutput.toByteArray();
    }
}