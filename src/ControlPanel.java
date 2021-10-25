import javax.swing.*;
import java.awt.*;

/**
 *  CLASS: ControlPanel
 *  PURPOSE: Used when making a crossword. Has 2 modes: clues (write in clues), and helper (gives word hints)
 *  AUTHOR: Nathan Poppe
 */
public class ControlPanel extends JPanel {

    private JPanel helperTab, cluesTab;
    private JTabbedPane tabbedPane;
    private CluesPanel cluesPanel;
    private HelperPanel helperPanel;
    private Crossword crossword;

    private GUI gui;

    public ControlPanel(Crossword crossword, int width, int height, GUI gui) {

        this.crossword = crossword;
        this.gui = gui;
        helperTab = new JPanel();
        cluesTab = new JPanel();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Helper", helperTab);
        tabbedPane.addTab("Clues", cluesTab);
        tabbedPane.setPreferredSize(new Dimension(width, height));
        tabbedPane.setSize(new Dimension(width, height));
        setLayout(new FlowLayout());
        add(tabbedPane);
    }

    public void setUpCluesPanel(){
        cluesPanel = new CluesPanel(crossword, cluesTab, this);
    }

    public void setUpHelperPanel(WordFinder wordFinder){
        helperPanel = new HelperPanel(crossword, helperTab, this, wordFinder);
    }

    public void changePanelInfo(int selectedNum, boolean isAcross){
        cluesPanel.changeSelectedClue(crossword.getClueIndex(selectedNum, isAcross), isAcross);
        helperPanel.updateSuggestion(crossword.getSuggestion(selectedNum, isAcross));
    }

    public void changeWordSelection(int selectedIndex, boolean isAcross){
        gui.changeWordSelection(selectedIndex, isAcross);
    }
    public void fillInWord(String suggestion){
        gui.fillInWord(suggestion);
    }
}
