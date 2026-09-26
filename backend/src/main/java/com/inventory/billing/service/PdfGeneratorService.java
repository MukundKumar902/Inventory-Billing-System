package com.inventory.billing.service;

import com.inventory.billing.entity.Invoice;
import com.inventory.billing.entity.InvoiceItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font Styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

            // Store Header
            Paragraph title = new Paragraph("RETAIL INVENTORY & BILLING STORE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Tax Invoice / Cash Memo", subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            // Invoice & Customer Info Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            infoTable.addCell(getNoBorderCell("Invoice No: " + invoice.getInvoiceNumber(), boldFont));
            infoTable.addCell(getNoBorderCell("Date: " + invoice.getInvoiceDate().format(formatter), regularFont));
            
            String customerDetails = (invoice.getCustomer() != null) 
                    ? invoice.getCustomer().getName() + " (" + invoice.getCustomer().getPhone() + ")"
                    : "Walk-in Customer";
            
            infoTable.addCell(getNoBorderCell("Customer: " + customerDetails, regularFont));
            infoTable.addCell(getNoBorderCell("Payment Mode: " + invoice.getPaymentMode(), regularFont));

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // Items Table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 4, 2, 2, 2, 2});

            // Table Headers
            String[] headers = {"#", "Item Description", "Qty", "Price (₹)", "GST %", "Total (₹)"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Table Items
            int count = 1;
            for (InvoiceItem item : invoice.getItems()) {
                table.addCell(getCenteredCell(String.valueOf(count++), regularFont));
                table.addCell(getCell(item.getProduct().getName(), regularFont));
                table.addCell(getCenteredCell(String.valueOf(item.getQuantity()), regularFont));
                table.addCell(getRightCell(String.format("%.2f", item.getUnitPrice()), regularFont));
                table.addCell(getCenteredCell(String.format("%.1f%%", item.getGstPercent()), regularFont));
                table.addCell(getRightCell(String.format("%.2f", item.getTotalPrice()), regularFont));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Summary Table (Subtotal, Tax, Grand Total)
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(getCell("Sub Total:", boldFont));
            summaryTable.addCell(getRightCell(String.format("₹ %.2f", invoice.getSubTotal()), regularFont));

            summaryTable.addCell(getCell("GST Tax Amount:", boldFont));
            summaryTable.addCell(getRightCell(String.format("₹ %.2f", invoice.getTotalTax()), regularFont));

            summaryTable.addCell(getCell("Discount:", boldFont));
            summaryTable.addCell(getRightCell(String.format("₹ %.2f", invoice.getTotalDiscount()), regularFont));

            PdfPCell grandTotalLabel = getCell("Grand Total:", titleFont);
            grandTotalLabel.setBackgroundColor(Color.YELLOW);
            summaryTable.addCell(grandTotalLabel);

            PdfPCell grandTotalVal = getRightCell(String.format("₹ %.2f", invoice.getGrandTotal()), titleFont);
            grandTotalVal.setBackgroundColor(Color.YELLOW);
            summaryTable.addCell(grandTotalVal);

            document.add(summaryTable);
            document.add(Chunk.NEWLINE);

            // Footer
            Paragraph footer = new Paragraph("Thank you for your business! Visit Again.", subTitleFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF invoice", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell getNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell getCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell getCenteredCell(String text, Font font) {
        PdfPCell cell = getCell(text, font);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell getRightCell(String text, Font font) {
        PdfPCell cell = getCell(text, font);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }
}
