import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.geom.Rectangle;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PDFWriter {

    private final static String FONT_LOCATION = "src/main/resources/arial.ttf";

    private Crossword crossword;
    private String fileName;

    private PdfWriter writer;
    private PdfDocument pdf;
    private Document doc;

    private final static String FILE_LOCATION = "Printable-Crosswords/";

    private final static float SPACING_RATIO = 0.75f; // multiplied by current font size to get spacing amount
    private final static float NUMBER_FONT_RATIO = 0.25f; // multiplied by cellSize to get number font size

    private final static int DOC_WIDTH = 612;
    private final static int DOC_HEIGHT = 792;
    private final static int TITLE_FONT_SIZE = 24;
    private final static int MAX_CLUES_FONT_SIZE = 14;

    private final static int MARGIN_SIZE = 36;
    private final static int NUM_COLUMNS = 3;
    private final static int COLUMN_WIDTH = (DOC_WIDTH - (NUM_COLUMNS + 1) * MARGIN_SIZE) / NUM_COLUMNS;
    private final static int COLUMN_HEIGHT = (int)(DOC_HEIGHT - MARGIN_SIZE * 2 - TITLE_FONT_SIZE - SPACING_RATIO * TITLE_FONT_SIZE);

    private int cluesFontSize = MAX_CLUES_FONT_SIZE;
    private int titleBottomGap = (int)(TITLE_FONT_SIZE * SPACING_RATIO + MAX_CLUES_FONT_SIZE * SPACING_RATIO);

    private int cellSize;

    public PDFWriter(Crossword crossword) {
        this.crossword = crossword;
        this.fileName = FILE_LOCATION + crossword.getName() + ".pdf";

        try {
            writer = new PdfWriter(fileName);
        }
        catch(FileNotFoundException e){
            System.out.println("Could not find/create file: " + fileName);
        }
    }

    public void createOnePagePDF(){
        int numPages = 0;
        cluesFontSize = MAX_CLUES_FONT_SIZE;
        while(numPages != 1){
            createPDF();
            numPages = pdf.getNumberOfPages();
            cluesFontSize--;
            titleBottomGap = (int)(TITLE_FONT_SIZE * SPACING_RATIO + cluesFontSize * SPACING_RATIO);
        }
        doc.close();
    }

    private void createPDF() {
        createDocument();
        setFormatting();
        setFont();
        createTitle();
        createGrid();
        setColumns();
        createClues("ACROSS", crossword.getAcrossClues());
        createClues("DOWN", crossword.getDownClues());
    }

    private void createDocument(){
        pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(new PageSize(DOC_WIDTH, DOC_HEIGHT));
        doc = new Document(pdf);
    }

    private void createGrid() {
        Table grid = new Table(UnitValue.createPercentArray(crossword.getWidth()));
        grid.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        grid.setMarginTop(titleBottomGap);

        cellSize = (int)((COLUMN_WIDTH * 2 + 1.5 * MARGIN_SIZE - crossword.getWidth()) / crossword.getWidth());
        doc.setFontSize(NUMBER_FONT_RATIO * cellSize);

        for(int i = 0; i < crossword.getWidth(); i++){
            for(int j = 0; j < crossword.getHeight(); j++){
                int number = crossword.getNumber(i, j);
                Cell cell = new Cell();
                cell.setWidth(cellSize);
                cell.setHeight(cellSize);
                cell.setPadding(0);
                if(number == -1){  // -1 indicates filled box
                    cell.setBackgroundColor(DeviceGray.BLACK);
                    grid.addCell(cell);
                }
                else if(number == 0){ // 0 indicates box with no number
                    grid.addCell(cell);
                }
                else{ // 1+ indicates box with number
                    grid.addCell(String.valueOf(crossword.getNumber(i, j)));
                }
            }
        }
        doc.add(grid);
    }


    private void setFont() {
        try {
            FontProgram fontProgram = FontProgramFactory.createFont(FONT_LOCATION);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI);
            doc.setFont(font);
        }
        catch(IOException e){
            System.out.println("Could not create PDF font.");
        }
    }

    private void setFormatting(){
        doc.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE);
        doc.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, SPACING_RATIO));
    }

    private void setColumns(){
        Rectangle[] columns = {
                new Rectangle(MARGIN_SIZE, MARGIN_SIZE, COLUMN_WIDTH, COLUMN_HEIGHT),
                new Rectangle(MARGIN_SIZE * 2 + COLUMN_WIDTH, MARGIN_SIZE, COLUMN_WIDTH, COLUMN_HEIGHT - cellSize * crossword.getHeight() - titleBottomGap),
                new Rectangle(MARGIN_SIZE * 3 + COLUMN_WIDTH * 2, MARGIN_SIZE, COLUMN_WIDTH, COLUMN_HEIGHT - cellSize * crossword.getHeight() - titleBottomGap)};

        doc.setRenderer(new ColumnDocumentRenderer(doc, columns));
    }

    private void createClues(String clueHeader, ArrayList<Clue> clues){
        doc.setFontSize(cluesFontSize);
        doc.add(new Paragraph(clueHeader));
        List list = new List();
        for(int i = 0; i < clues.size(); i++){
            Clue nextClue = clues.get(i);
            ListItem listItem = new ListItem();
            listItem.setListSymbol(nextClue.getNumber() + ". ");
            if(nextClue.getDescription().equals(""))
                listItem.add(new Paragraph(" "));
            else
                listItem.add(new Paragraph(nextClue.getDescription()));
            list.add(listItem);
        }
        doc.add(list);
    }

    private void createTitle(){
        doc.setFontSize(TITLE_FONT_SIZE);
        doc.add(new Paragraph(crossword.getName()));
    }

    public void openPDF(){
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(fileName);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                System.out.println("Can't auto open PDF file");
            }
        }
    }
}
