import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 *  CLASS: CluesPanel
 *  PURPOSE: Used to create the JPanel where the user can enter clues corresponding to their crossword.
 *  AUTHOR: Nathan Poppe
 */
public class CluesPanel implements MouseListener, ActionListener {

    private final static int GAP_SIZE = 5; // gap between all components on the panel in pixels
    private final static int LABEL_HEIGHT = 20; // height of all labels in pixels

    private ControlPanel controlPanel;
    private Crossword crossword;

    private JPanel tab; // the panel that contains all the clue panel components

    // the DefaultListModels are used to create the JLists which are graphical lists containing the clues
    private DefaultListModel<String> acrossCluesListModel;
    private DefaultListModel<String> downCluesListModel;
    private JList<String> acrossCluesList;
    private JList<String> downCluesList;

    // the labels above the lists that say "ACROSS" and "DOWN"
    private JLabel acrossLabel;
    private JLabel downLabel;

    // used to scroll through the clues
    private JScrollPane acrossScroller;
    private JScrollPane downScroller;

    // used by the user to input clues
    private JLabel enterClueLabel;
    private JTextField textField;

    private boolean acrossSelected = true; // specifies which list has an index currently selected (across/down)

    public CluesPanel(Crossword crossword, JPanel tab, ControlPanel controlPanel){
        this.crossword = crossword;
        this.controlPanel = controlPanel;
        this.tab = tab;
        this.acrossCluesListModel = getListModel(crossword.getAcrossClues());
        this.downCluesListModel = getListModel(crossword.getDownClues());
        this.acrossCluesList = getList(acrossCluesListModel);
        this.downCluesList = getList(downCluesListModel);
        addComponentsToPanel();
    }

    private void addComponentsToPanel(){
        tab.setLayout(new FlowLayout(FlowLayout.CENTER, GAP_SIZE, GAP_SIZE));
        addClueLabels();
        addScrollers();
        addInput();
        acrossCluesList.setSelectedIndex(0); // by default the first across clue is selected
    }

    private void addClueLabels(){
        acrossLabel = new JLabel("ACROSS");
        acrossLabel.setPreferredSize(new Dimension(tab.getWidth() / 2 - 3 * GAP_SIZE / 2, LABEL_HEIGHT));
        downLabel = new JLabel("DOWN");
        downLabel.setPreferredSize(new Dimension(tab.getWidth() / 2 - 3 * GAP_SIZE / 2, LABEL_HEIGHT));
        tab.add(acrossLabel);
        tab.add(downLabel);
    }

    private void addScrollers(){
        acrossScroller = new JScrollPane(acrossCluesList);
        acrossScroller.setPreferredSize(new Dimension((tab.getWidth() - 3 * GAP_SIZE) / 2, tab.getHeight() - LABEL_HEIGHT * 2 - GAP_SIZE * 4));
        downScroller = new JScrollPane(downCluesList);
        downScroller.setPreferredSize(new Dimension((tab.getWidth() - 3 * GAP_SIZE) / 2, tab.getHeight() - LABEL_HEIGHT * 2 - GAP_SIZE * 4));
        tab.add(acrossScroller);
        tab.add(downScroller);
    }

    private void addInput(){
        enterClueLabel = new JLabel("Enter clue:");
        tab.add(enterClueLabel);
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(tab.getWidth() / 2 - GAP_SIZE, LABEL_HEIGHT));
        textField.addActionListener(this);
        tab.add(textField);
    }

    private DefaultListModel<String> getListModel(ArrayList<Clue> clues){
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(int i = 0; i < clues.size(); i++){
            Clue nextClue = clues.get(i);
            listModel.addElement(nextClue.getNumber() + ". " + nextClue.getDescription());
        }
        return listModel;
    }

    private JList<String> getList(DefaultListModel<String> listModel){
        JList<String> list = new JList<String>(listModel);
        list.setLayout(new GridLayout());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(this);
        return list;
    }

    public void changeSelectedClue(int index, boolean isAcross){
        if(isAcross){
            downCluesList.clearSelection();
            acrossSelected = true;
            acrossCluesList.setSelectedIndex(index);
            acrossCluesList.ensureIndexIsVisible(index);
        }
        else{
            acrossCluesList.clearSelection();
            acrossSelected = false;
            downCluesList.setSelectedIndex(index);
            downCluesList.ensureIndexIsVisible(index);
        }
    }

    private void nextClue(){
        if(acrossSelected)
            acrossCluesList.setSelectedIndex((getSelectedIndex() + 1) % acrossCluesListModel.size());
        else
            downCluesList.setSelectedIndex((getSelectedIndex() + 1) % downCluesListModel.size());
        controlPanel.changeWordSelection(getSelectedIndex(), acrossSelected);
    }

    private int getSelectedIndex(){
        int selectedIndex = acrossCluesList.getSelectedIndex();
        if(!acrossSelected)
            selectedIndex = downCluesList.getSelectedIndex();
        return selectedIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!textField.getText().equals("")) {
            int indexSelected = getSelectedIndex();
            if (acrossSelected) {
                int clueNumber = crossword.getAcrossClues().get(indexSelected).getNumber();
                acrossCluesListModel.setElementAt(clueNumber + ". " + textField.getText(), indexSelected);
                crossword.getAcrossClues().get(indexSelected).setDescription(textField.getText());
            } else {
                int clueNumber = crossword.getDownClues().get(indexSelected).getNumber();
                downCluesListModel.setElementAt(clueNumber + ". " + textField.getText(), indexSelected);
                crossword.getDownClues().get(indexSelected).setDescription(textField.getText());
            }
            textField.setText("");
        }
        nextClue();
    }

    private void mouseSelectedIndex(){
        if(acrossSelected) {
            if(downCluesList.getSelectedIndex() != -1){
                acrossCluesList.clearSelection();
                acrossSelected = false;
            }
        }
        else{
            if(acrossCluesList.getSelectedIndex() != -1){
                downCluesList.clearSelection();
                acrossSelected = true;
            }
        }
        textField.setFocusable(true);
        textField.requestFocus();
        controlPanel.changeWordSelection(getSelectedIndex(), acrossSelected);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseSelectedIndex();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseSelectedIndex();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
