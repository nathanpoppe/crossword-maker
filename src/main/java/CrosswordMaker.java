import java.io.File;
import java.util.ArrayList;

public class CrosswordMaker {

    private static final String TEMPLATES_FILE_PATH = "src/main/resources/crossword-templates.txt";

    private CrosswordReader crosswordReader;
    private ArrayList<Crossword> templates;
    private PDFWriter pdfWriter;
    private WordFinder wordFinder;
    private Crossword currCrossword;
    private int currTemplateNum;

    private GUI gui;

    public void runApplication() {
        this.gui = new GUI(this); // load Window
        readInTemplates();
        resetTemplateNames();
        this.wordFinder = new WordFinder(); // read in words
    }

    public void readInTemplates(){
        crosswordReader = new CrosswordReader();
        templates = crosswordReader.readInCrosswords(TEMPLATES_FILE_PATH, true);
        if(templates.size() > 0)
            currCrossword = templates.get(0);
        currTemplateNum = 0;
    }

    public void newTemplate(int width, int height){
        currCrossword = new Crossword(width, height);
        gui.setUpNewTemplate(currCrossword);
        gui.changeMenu("template");
    }

    public void newFile(){
        currTemplateNum = 0;
        currCrossword = templates.get(currTemplateNum);
        gui.setUpChooseTemplate(currCrossword);
        gui.changeMenu("chooseTemplate");
    }

    public void nextTemplate(boolean isNext){
        if(isNext){
            currTemplateNum++;
            currTemplateNum = currTemplateNum % (templates.size());
        }
        else{
            if(currTemplateNum == 0){
                currTemplateNum = templates.size() - 1;
            }
            else{
                currTemplateNum--;
            }
        }
        currCrossword = templates.get(currTemplateNum);
        gui.setUpChooseTemplate(currCrossword);
    }

    public void openFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.getFile();
        if(file != null){
            CrosswordReader crosswordReader = new CrosswordReader();
            currCrossword = crosswordReader.readInCrosswords(file.getPath(), false).get(0);
            gui.setUpNewFile(currCrossword, wordFinder);
            gui.changeMenu("file");
        }
        else{
            //System.out.println("Could not open file!");
        }
    }

    public void saveTemplate(){
        CrosswordWriter crosswordWriter = new CrosswordWriter();
        crosswordWriter.saveTemplates(templates, currCrossword, TEMPLATES_FILE_PATH);
    }

    public void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.saveFile(currCrossword);
        CrosswordWriter crosswordWriter = new CrosswordWriter();
        crosswordWriter.saveCrossword(currCrossword);
    }

    public void saveAsPDF(){
        pdfWriter = new PDFWriter(currCrossword);
        pdfWriter.createOnePagePDF();
        pdfWriter.openPDF();
    }

    public void templateChosen(){
        gui.setUpNewFile(currCrossword, wordFinder);
        gui.changeMenu("file");
    }

    private void resetTemplateNames(){
        for(int i = 0; i < templates.size(); i++){
            templates.get(i).setTemplateName();
        }
        CrosswordWriter crosswordWriter = new CrosswordWriter();
        crosswordWriter.saveTemplates(templates, null, TEMPLATES_FILE_PATH); // null means no new template
    }

}
