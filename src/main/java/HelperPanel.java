import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HelperPanel implements ChangeListener, ListSelectionListener, ItemListener {

    // all constant integers are measured in pixels
    private final static int GAP_SIZE = 5; // gap distance between components on the panel
    private final static int NUM_LETTER_LABEL_HEIGHT = 20;
    private final static int NUM_LETTER_INPUT_HEIGHT = 20;
    private final static int SUGGESTION_MAX_HEIGHT = 60; // the max height of the panel that holds the suggestion letterFields
    private final static int SUGGESTION_GAP_SIZE = 2; // gap distance between components on the suggestion panel

    // multiplied by the size of each letterField to get the font size of the letter in the letterField
    private final static float SUGGESTION_FONT_RATIO = 0.5f;

    private Crossword crossword; // the current crossword that the helperPanel is based on
    private JPanel tab; // the JPanel that holds all the components
    private ControlPanel controlPanel; // holds the helperPanel and cluesPanel
    private WordFinder wordFinder; // used to access the dictionaries (all possible words)

    // All HelperPanel Components
    private JLabel numLetterLabel; // displays the number of letters given by the suggestion, eg. "Number of Letters: 5"
    private JSlider numLetterSlider; // a slider that allows the user to change the suggestion length
    private JPanel suggestionPanel; // a JPanel containing the letterFields that make up the suggestion
    private int suggestionPanelHeight;
    private LetterField[] letterFields; // an array containing the letterFields that make up the suggestion
    private DefaultListModel<String> wordListModel; // contains all possible words based on the suggestion
    private JList<String> wordList; // the graphical list containing all the words
    private JScrollPane wordListScroller; // adds a scroll pane to the wordList
    private JPanel filterPanel; // a JPanel containing a Checkbox and a Combobox to filter word list
    private JComboBox setDictionaryComboBox;
    private String regDictionaryName = "84000 Words";
    private String longDictionaryName = "370000 Words";
    private JCheckBox setAlternatingCheckBox;

    private boolean isAlternating = false;
    private boolean useRegWordList = true;

    private String suggestion; // the current suggestion, eg. "s_p__e"
    private int focussedLetterField = -1;

    public HelperPanel(Crossword crossword, JPanel tab, ControlPanel controlPanel, WordFinder wordFinder){
        this.crossword = crossword;
        this.tab = tab;
        this.controlPanel = controlPanel;
        this.wordFinder = wordFinder;
        this.suggestion = crossword.getSuggestion(1, true); // gets the "default" suggestion, clue 1 across
        addComponentsToPanel();
    }

    private void addComponentsToPanel(){
        tab.setLayout(new FlowLayout(FlowLayout.CENTER, GAP_SIZE, GAP_SIZE));
        addNumLetterLabel();
        addNumLetterSlider();
        addSuggestionPanel();
        updateLetterFields();
        addWordList();
        addFilterPanel();
    }

    /**
     * Creates and adds to the helperPanel a label displaying the number of letters given by the suggestion.
     */
    private void addNumLetterLabel(){
        numLetterLabel = new JLabel();
        updateNumLetterLabel(suggestion.length());
        numLetterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numLetterLabel.setPreferredSize(new Dimension(tab.getWidth(), NUM_LETTER_LABEL_HEIGHT));
        tab.add(numLetterLabel);
    }

    private void updateNumLetterLabel(int numLetters){
        numLetterLabel.setText("Number of Letters: " + numLetters);
    }

    /**
     * Creates and adds to the helperPanel a slider that allows the user to change the suggestion length.
     */
    private void addNumLetterSlider(){
        numLetterSlider = new JSlider();
        updateNumLetterSlider(1, crossword.getWidth(), suggestion.length());
        numLetterSlider.setPreferredSize(new Dimension(tab.getWidth() / 3, NUM_LETTER_INPUT_HEIGHT));
        numLetterSlider.addChangeListener(this);
        numLetterSlider.setSnapToTicks(true);
        tab.add(numLetterSlider);
    }

    /**
     * Event called when slider controlling number of suggestion letters is moved.
     * Updates all helperPanel information based on the number of letters selected by the slider.
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        int numLetters = numLetterSlider.getValue();
        if(numLetters != suggestion.length()) { // check if suggestion length is different than previous
            updateSuggestion(getEmptyClue(numLetters));
        }
    }

    private String getEmptyClue(int length){
        String emptyClue = "";
        for(int i = 0; i < length; i++)
            emptyClue += "_";
        return emptyClue;
    }

    private void updateNumLetterSlider(int min, int max, int current){
        numLetterSlider.setMinimum(min);
        numLetterSlider.setMaximum(max);
        numLetterSlider.setValue(current);
    }

    /**
     * Creates and adds to the helperPanel a JPanel that holds the letterFields that make up the suggestion
     */
    private void addSuggestionPanel(){
        suggestionPanel = new JPanel();
        int suggestionPanelWidth = tab.getWidth() - 2 * GAP_SIZE;
        suggestionPanelHeight = Math.min(SUGGESTION_MAX_HEIGHT, (suggestionPanelWidth - (crossword.getWidth() + 1) * SUGGESTION_GAP_SIZE) / crossword.getWidth() + 2 * SUGGESTION_GAP_SIZE);
        suggestionPanel.setPreferredSize(new Dimension(suggestionPanelWidth, suggestionPanelHeight));
        suggestionPanel.setSize(new Dimension(suggestionPanelWidth, suggestionPanelHeight));
        suggestionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, SUGGESTION_GAP_SIZE, SUGGESTION_GAP_SIZE));
        //suggestionPanel.setBackground(Color.black); // for testing
        tab.add(suggestionPanel);
    }

    /**
     * Removes previous letterFields (if any) any creates new ones based on the suggestion
     */
    private void updateLetterFields(){
        suggestionPanel.removeAll(); // removes all previous letterFields to make way for new ones
        letterFields = new LetterField[suggestion.length()];
        for(int i = 0; i < suggestion.length(); i++){
            int letterFieldSize = suggestionPanel.getHeight() - 2 * SUGGESTION_GAP_SIZE;
            LetterField letterField = new LetterField(suggestion.charAt(i), i, letterFieldSize, letterFieldSize, this);
            letterField.setFont(new Font("Arial", Font.PLAIN, (int)(letterFieldSize * SUGGESTION_FONT_RATIO)));
            letterFields[i] = letterField;
            suggestionPanel.add(letterFields[i]);
        }
        suggestionPanel.revalidate();
        suggestionPanel.repaint();
    }

    /**
     * Adds a scrollable JList containing all possible words.
     */
    private void addWordList(){
        wordListModel = new DefaultListModel<String>();
        updateWordListModel();

        wordList = new JList<String>(wordListModel);
        wordList.setLayout(new GridLayout());
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordList.addListSelectionListener(this);

        wordListScroller = new JScrollPane(wordList);
        wordListScroller.setPreferredSize(new Dimension((tab.getWidth() - 2 * GAP_SIZE) / 2, tab.getHeight() - NUM_LETTER_LABEL_HEIGHT - NUM_LETTER_INPUT_HEIGHT - suggestionPanelHeight - 5 * GAP_SIZE));
        tab.add(wordListScroller);
    }

    /**
     * Updates the list containing all the words based on 3 things:
     *      1. The suggestion
     *      2. Whether to include all words or only alternating words
     *      3. Whether to use the regular dictionary or the large dictionary
     */
    private void updateWordListModel(){
        wordListModel.clear(); // remove all words
        ArrayList<String> possibleWords;
        if(isAlternating) // if user has filtered to only include alternating words
            possibleWords = wordFinder.findAlternatingWords(useRegWordList, suggestion);
        else
            possibleWords = wordFinder.findPossibleWords(useRegWordList, suggestion);

        // add words from ArrayList to WordListModel
        for(int i = 0; i < possibleWords.size(); i++){
            wordListModel.addElement(possibleWords.get(i).toUpperCase());
        }
    }

    private void updateWordList(){
        updateWordListModel();
        wordList.setModel(wordListModel);
    }

    private void addFilterPanel(){
        filterPanel = new JPanel();
        filterPanel.setPreferredSize(new Dimension((tab.getWidth() - 2 * GAP_SIZE) / 4, tab.getHeight() - NUM_LETTER_LABEL_HEIGHT - NUM_LETTER_INPUT_HEIGHT - suggestionPanelHeight - 5 * GAP_SIZE));

        filterPanel.add(new JLabel("Dictionary Size: "));
        setDictionaryComboBox = new JComboBox<String>();
        setDictionaryComboBox.addItem(regDictionaryName);
        setDictionaryComboBox.addItem(longDictionaryName);
        setDictionaryComboBox.addItemListener(this);
        filterPanel.add(setDictionaryComboBox);

        setAlternatingCheckBox = new JCheckBox("Alternating");
        setAlternatingCheckBox.addItemListener(this);
        filterPanel.add(setAlternatingCheckBox);

        tab.add(filterPanel);
    }

    /**
     * Called when setAlternatingCheckBox is selected/deselected.
     * Also called when setDictionaryComboBox is changed.
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource().equals(setDictionaryComboBox)){
            if(e.getItem().equals("84000 Words"))
                useRegWordList = true;
            else
                useRegWordList = false;
        }
        else if(e.getSource().equals(setAlternatingCheckBox)){
            if(e.getStateChange() == ItemEvent.SELECTED)
                isAlternating = true;
            else
                isAlternating = false;
        }
        updateWordList();
    }

    public void updateSuggestion(String newSuggestion){
        this.suggestion = newSuggestion;
        updateComponents();
    }

    public void updateSuggestion(int letterNum, char newLetter){
        String newSuggestion = suggestion.substring(0, letterNum) + newLetter + suggestion.substring(letterNum + 1);
        if(!newSuggestion.equals(suggestion)){
            suggestion = newSuggestion;
            updateWordList();
        }
    }

    private void updateComponents(){
        updateNumLetterLabel(suggestion.length());
        updateNumLetterSlider(1, crossword.getWidth(), suggestion.length());
        updateLetterFields();
        updateWordList();
    }

    public void changeFocussedLetterField(int newFieldToFocus){
        if(newFieldToFocus >= 0 && newFieldToFocus < letterFields.length) {
            if (newFieldToFocus != focussedLetterField) {
                focussedLetterField = newFieldToFocus;
                letterFields[focussedLetterField].requestFocus();
            }
        }
    }

    /**
     * The following methods a word in the wordList is selected.
     * @param e
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        String newWord = wordList.getSelectedValue();
        if(newWord != null)
            controlPanel.fillInWord(wordList.getSelectedValue());
    }
}
