import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {

    private CrosswordMaker crosswordMaker;
    private JMenu menu;
    private JMenuItem newTemplate, newFile, openFile, saveTemplate, saveFile, saveAsPDF;

    public Menu(CrosswordMaker crosswordMaker){
        this.crosswordMaker = crosswordMaker;

        menu = new JMenu("File");

        newTemplate = new JMenuItem("New Template");
        newFile = new JMenuItem("New File");
        openFile = new JMenuItem("Open File");
        saveTemplate = new JMenuItem("Save Template");
        saveFile = new JMenuItem("Save File");
        saveAsPDF = new JMenuItem("Save As PDF");

        addActionListeners();
        add(menu);
    }

    private void addActionListeners(){
        newTemplate.addActionListener(this);
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveTemplate.addActionListener(this);
        saveFile.addActionListener(this);
        saveAsPDF.addActionListener(this);
    }

    public void changeMenu(String mode){
        menu.removeAll();
        menu.add(newTemplate);
        menu.add(newFile);
        menu.add(openFile);

        if(mode.equals("template")){
            menu.add(saveTemplate);
        }
        else if(mode.equals("file")){
            menu.add(saveFile);
            menu.add(saveAsPDF);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newTemplate) {
            crosswordMaker.newTemplate(Crossword.getDefaultSize(), Crossword.getDefaultSize());
        } else if (e.getSource() == newFile) {
            crosswordMaker.newFile();
        } else if (e.getSource() == openFile) {
            crosswordMaker.openFile();
        } else if (e.getSource() == saveTemplate) {
            crosswordMaker.saveTemplate();
        } else if (e.getSource() == saveFile) {
            crosswordMaker.saveFile();
        } else if (e.getSource() == saveAsPDF) {
            crosswordMaker.saveAsPDF();
        }
    }
}
