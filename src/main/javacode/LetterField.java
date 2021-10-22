import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class LetterField extends JFormattedTextField implements FocusListener, KeyListener, DocumentListener {

    private int letterNum;
    private HelperPanel helperPanel;

    public LetterField(char letter, int letterNum, int width, int height, HelperPanel helperPanel){
        this.letterNum = letterNum;
        this.helperPanel = helperPanel;
        addMaskFormatter();
        setPreferredSize(new Dimension(width, height));
        setLetter(letter);
        setHorizontalAlignment(JTextField.CENTER);
        setVisible(true);
        addFocusListener(this);
        addKeyListener(this);
        getDocument().addDocumentListener(this);
    }

    private void setLetter(char letter){
        if(letter == '_')
            setText("");
        else{
            setText(String.valueOf(letter));
        }
    }

    private void addMaskFormatter(){
        try {
            MaskFormatter maskFormatter = new MaskFormatter("U");
            maskFormatter.install(this);
        }
        catch(ParseException e){
            System.out.println("Could not add mask formatter to LetterField!");
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        helperPanel.changeFocussedLetterField(letterNum);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char keyPressed = String.valueOf(e.getKeyChar()).toUpperCase().charAt(0);
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_LEFT){
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                setText("");
            helperPanel.changeFocussedLetterField(letterNum - 1);
        }
        else if(Character.isLetter(keyPressed) || e.getKeyCode() == KeyEvent.VK_RIGHT){
            helperPanel.changeFocussedLetterField(letterNum + 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Event called when the content of a letterField changes.
     * @param e
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        if(getText().equals("") || getText().equals(" "))
            helperPanel.updateSuggestion(letterNum, '_');
        else
            helperPanel.updateSuggestion(letterNum, getText().toLowerCase().charAt(0));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
