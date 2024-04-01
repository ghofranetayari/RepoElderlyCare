package RestControllers;

import Services.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/generate-pdf")
@Slf4j
public class PdfController {

    private final ShopService shopService;
    private int pdfCounter = 1;

    public PdfController(ShopService shopService) {
        this.shopService = shopService;
    }
    @PostMapping
    public ResponseEntity<byte[]> generatePdf() {
        try {
            List<ShopService.ElderlyOrderInfo> elderlyOrderInfoList = shopService.getElderlyOrderInfoWithBoughtOrders();
            ByteArrayOutputStream byteArrayOutputStream = generatePdfContent(elderlyOrderInfoList);
            String fileName = "elderly_orders_" + pdfCounter + ".pdf";
            savePdfToFile(byteArrayOutputStream, fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", fileName);
            pdfCounter++;
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ByteArrayOutputStream generatePdfContent(List<ShopService.ElderlyOrderInfo> elderlyOrderInfoList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDFont font = PDType1Font.HELVETICA_BOLD;
                contentStream.setFont(font, 10);

                // Title
                String title = "Elderly Order History";
                float titleWidth = font.getStringWidth(title) / 1000 * 10;
                float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 10;
                float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
                float titleY = page.getMediaBox().getHeight() - 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(titleX, titleY + titleHeight);
                contentStream.showText(title);
                contentStream.endText();

                // Table
                float margin = 50;
                float yPosition = titleY - 20;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float columnWidth = tableWidth / 5;
                float columnHeight = 15;

                // Draw table header
                contentStream.setFont(font, 8);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Elderly Name");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText("Phone");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText("Product");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText("Quantity");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText("Total Price");
                contentStream.endText();

                // Write table content
                contentStream.setFont(font, 8);
                for (ShopService.ElderlyOrderInfo orderInfo : elderlyOrderInfoList) {
                    yPosition -= columnHeight;

                    // Write columns
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(orderInfo.getElderlyName().replaceAll("\t", ""));

                    contentStream.newLineAtOffset(columnWidth, 0);
                    contentStream.showText(orderInfo.getElderlyPhone().replaceAll("\t", ""));

                    contentStream.newLineAtOffset(columnWidth, 0);
                    contentStream.showText(orderInfo.getProductName().replaceAll("\t", ""));

                    contentStream.newLineAtOffset(columnWidth, 0);
                    contentStream.showText(String.valueOf(orderInfo.getQuantity()));

                    contentStream.newLineAtOffset(columnWidth, 0);
                    String totalPrice = String.valueOf(orderInfo.getTotalPrice());
                    contentStream.showText(totalPrice);

                    contentStream.endText();
                }
            }

            document.save(byteArrayOutputStream);
        } catch (IOException e) {
            log.error("Error generating PDF content: {}", e.getMessage(), e);
        }

        return byteArrayOutputStream;
    }

    private void savePdfToFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) {
        try {
            String filePath = "C:/xampp/htdocs/hazemimage/" + fileName;
            Files.write(Paths.get(filePath), byteArrayOutputStream.toByteArray());
            log.info("PDF saved successfully to: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving PDF to file: {}", e.getMessage(), e);
        }
    }
}