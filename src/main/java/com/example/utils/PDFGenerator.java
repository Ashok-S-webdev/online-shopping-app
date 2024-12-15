package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.example.Repo.ProductDao;
import com.example.model.CartItem;
import com.example.model.Product;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {
    public static byte[] generatePDF(List<CartItem> cartItems, double total) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Cart Details"));
            document.add(new Paragraph("\n"));

            int cartItemNo = 1;

            for (CartItem item: cartItems) {
                Product product = ProductDao.getProductByProductId(item.getProductId());
                document.add(new Paragraph(cartItemNo + ". Product: " + product.getProductName() + ", Quantity: " + item.getQuantity() + ", Price: \u20B9" + product.getProductPrice()));
                cartItemNo++;
            }

            document.add(new Paragraph("\nTotal Amount: \u20B9" + total));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}