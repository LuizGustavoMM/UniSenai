package pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class EscritorPDF {
    public void escrever(String nomeArquivo) {
        Document document = new Document();
        try {
            final PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));
            document.open();
            document.add(new Paragraph("Meu primeiro PDF"));
        } catch (DocumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        document.close();
    }
}
