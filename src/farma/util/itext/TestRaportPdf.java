package farma.util.itext;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestRaportPdf {

    public static void main(String[] args) throws DocumentException, FileNotFoundException {

        var doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Rapoarte/ChronoUnits.pdf"));
        doc.open();

        // title
        var bold = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        var paragraph = new Paragraph("Duration of ChronoUnits");

        var table = new PdfPTable(2);
        Stream.of("Chrono Unit", "Duration").forEach(table::addCell);

        Arrays.stream(ChronoUnit.values())
                .forEach(val -> {
                    table.addCell(val.toString());
                    table.addCell(val.getDuration().toString());
                });

        paragraph.add(table);
        doc.add(paragraph);
        doc.close();
    }
}
