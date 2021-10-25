import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;

public class CrosswordGraphics extends JPanel implements MouseListener, KeyListener, MouseMotionListener {

    private final static double TITLE_FONT_RATIO = 0.04; // multiplied by panelHeight to get titleFontSize
    private final static double TITLE_GAP_RATIO = 0.02; // multiplied by panelHeight to get gap between title and grid
    private final static double NUMBER_FONT_RATIO = 0.225; // multiplied by boxSize to get numberFontSize
    private final static double NUMBER_GAP_RATIO = 0.05; // multiplied by boxSize to get gap between cell and number
    private final static double LETTER_FONT_RATIO = 0.6; // multiplied by boxSize to get letterFontSize

    private final static String FONT_NAME = "Arial";
    private final static int FONT_STYLE = Font.PLAIN;

    private final static Color LETTER_COLOR = new Color(106, 190, 212);
    private final static Color WORD_COLOR = new Color(175, 214, 224);
    private final static Color HIGHLIGHT_COLOR = new Color(215, 215, 215);

    private Crossword crossword;
    private int width;
    private int height;
    private int startX;
    private int startY;
    private int boxSize;

    private int titleFontSize;
    private int titleGapSize;
    private int numberGapSize;
    private Font titleFont;
    private Font numberFont;
    private Font letterFont;

    private CrosswordMaker crosswordMaker;
    private GUI gui;
    private String mode;

    private int selectedI = -1;
    private int selectedJ = -1;
    private boolean acrossSelected = true;
    private boolean isHighlighted = false;

    public CrosswordGraphics(Crossword crossword, int width, int height, String mode, CrosswordMaker crosswordMaker, GUI gui) {
        this.crossword = crossword;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.crosswordMaker = crosswordMaker;
        this.gui = gui;
        setGraphicsDimensions(width, height); // set startX, startY, and boxSize
        setFonts(); // set title, number, and letter fonts
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        //setBackground(Color.lightGray); // used for testing size of contentPane
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawCrossword(g2d);
    }

    private void setGraphicsDimensions(int panelWidth, int panelHeight) {
        this.titleFontSize = (int) (TITLE_FONT_RATIO * panelHeight);
        this.titleGapSize = (int) (TITLE_GAP_RATIO * panelHeight);

        int boxAvailableWidth = panelWidth / crossword.getWidth();
        int boxAvailableHeight = (panelHeight - titleFontSize - titleGapSize) / crossword.getHeight();

        if (boxAvailableHeight > boxAvailableWidth) {
            this.boxSize = boxAvailableWidth;
            this.startX = 0;
            this.startY = (panelHeight - boxAvailableWidth * crossword.getHeight()) / 2;
        } else {
            this.boxSize = boxAvailableHeight;
            this.startX = (panelWidth - boxAvailableHeight * crossword.getWidth()) / 2;
            this.startY = titleFontSize + titleGapSize;
        }
    }

    private void setFonts() {
        this.titleFont = new Font(FONT_NAME, FONT_STYLE, titleFontSize);
        this.numberFont = new Font(FONT_NAME, FONT_STYLE, (int) (NUMBER_FONT_RATIO * boxSize));
        this.letterFont = new Font(FONT_NAME, FONT_STYLE, (int) (LETTER_FONT_RATIO * boxSize));
        this.numberGapSize = (int) (NUMBER_GAP_RATIO * boxSize);
    }

    private void drawCrossword(Graphics2D g2d) {
        drawTitle(crossword.getName(), g2d);
        if (mode.equals("file")) {
            fillSelectedWord(selectedI, selectedJ, acrossSelected, g2d);
            drawLetters(crossword.getLetters(), g2d);
        }
        if (isHighlighted)
            highlightTemplate(g2d);
        drawNumbers(crossword.getNumbers(), g2d);
        drawGrid(g2d);
    }

    private void drawTitle(String title, Graphics2D g2d) {
        FontMetrics fontMetrics = getFontMetrics(titleFont);
        int x = startX + crossword.getWidth() * boxSize / 2 - fontMetrics.stringWidth(title) / 2;
        int y = startY - titleGapSize;
        g2d.setFont(titleFont);
        g2d.setColor(Color.black);
        g2d.drawString(title, x, y);
    }

    /**
     * Paints the vertical/horizontal lines to make a grid
     *
     * @param g2d - the graphics component
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.black);
        for (int i = 0; i < crossword.getWidth() + 1; i++) {
            g2d.drawLine(startX + i * boxSize, startY, startX + i * boxSize, startY + crossword.getHeight() * boxSize);
        }
        for (int i = 0; i < crossword.getHeight() + 1; i++) {
            g2d.drawLine(startX, startY + i * boxSize, startX + crossword.getWidth() * boxSize, startY + i * boxSize);
        }
    }

    private void drawBoxNumber(int num, int i, int j, int numberHeight, Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.drawString(String.valueOf(num), startX + i * boxSize + numberGapSize, startY + j * boxSize + numberHeight);
    }

    private void drawFilledBox(int i, int j, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(startX + i * boxSize, startY + j * boxSize, boxSize, boxSize);
    }

    private void drawBoxLetter(char letter, int i, int j, FontMetrics fontMetrics, Graphics2D g2d) {
        g2d.drawString(String.valueOf(letter), startX + i * boxSize + ((boxSize - fontMetrics.charWidth(letter)) / 2), startY + (j + 1) * boxSize - ((boxSize - fontMetrics.getAscent()) / 2));
    }

    private void drawLetters(char[][] letters, Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.setFont(letterFont);
        FontMetrics fontMetrics = getFontMetrics(letterFont);
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                if (letters[i][j] != '_') {
                    drawBoxLetter(letters[i][j], j, i, fontMetrics, g2d);
                }
            }
        }
    }

    private void drawNumbers(int[][] numbers, Graphics2D g2d) {
        FontMetrics fontMetrics = getFontMetrics(numberFont);
        g2d.setColor(Color.black);
        g2d.setFont(numberFont);
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                if (numbers[i][j] == -1) {
                    drawFilledBox(j, i, Color.black, g2d);
                } else if (numbers[i][j] > 0) {
                    drawBoxNumber(numbers[i][j], j, i, fontMetrics.getAscent(), g2d);
                }
            }
        }
    }

    private void highlightTemplate(Graphics2D g2d) {
        g2d.setColor(HIGHLIGHT_COLOR);
        g2d.fillRect(startX, startY, crossword.getWidth() * boxSize, crossword.getHeight() * boxSize);
    }

    private void fillSelectedWord(int i, int j, boolean acrossSelected, Graphics2D g2d) {
        if (i >= 0 && j >= 0 && crossword.getNumbers()[j][i] != -1) {
            drawFilledBox(i, j, LETTER_COLOR, g2d);
            if (acrossSelected) {
                int index = i - 1;
                while (index >= 0 && crossword.getNumbers()[j][index] != -1) {
                    drawFilledBox(index, j, WORD_COLOR, g2d);
                    index--;
                }
                index = i + 1;
                while (index < crossword.getHeight() && crossword.getNumbers()[j][index] != -1) {
                    drawFilledBox(index, j, WORD_COLOR, g2d);
                    index++;
                }
            } else {
                int index = j - 1;
                while (index >= 0 && crossword.getNumbers()[index][i] != -1) {
                    drawFilledBox(i, index, WORD_COLOR, g2d);
                    index--;
                }
                index = j + 1;
                while (index < crossword.getWidth() && crossword.getNumbers()[index][i] != -1) {
                    drawFilledBox(i, index, WORD_COLOR, g2d);
                    index++;
                }
            }
        }
    }

    public void selectFirstWord(){
        acrossSelected = true;
        int[] firstClueLocation = crossword.getNumberLocation(1);
        selectedI = firstClueLocation[0];
        selectedJ = firstClueLocation[1];
        repaint();
    }

    private void keyTemplatePressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            int newSize = Math.max(crossword.getWidth() - 1, Crossword.getMinSize());
            crosswordMaker.newTemplate(newSize, newSize);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int newSize = Math.min(crossword.getWidth() + 1, Crossword.getMaxSize());
            crosswordMaker.newTemplate(newSize, newSize);
        }
    }

    private void keyChooseTemplatePressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            crosswordMaker.nextTemplate(false);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            crosswordMaker.nextTemplate(true);
    }

    private void keyFilePressed(KeyEvent e) {
        if(selectedI != -1 && selectedJ != -1) {
            char keyPressed = String.valueOf(e.getKeyChar()).toUpperCase().charAt(0);
            if (Character.isLetter(keyPressed)) {
                crossword.getLetters()[selectedJ][selectedI] = keyPressed;
                selectNextLetter(acrossSelected, false);
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                crossword.getLetters()[selectedJ][selectedI] = '_';
                selectPrevLetter(acrossSelected, false);
            } else if (e.getKeyCode() == KeyEvent.VK_UP)
                selectPrevLetter(false, true);
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                selectNextLetter(false, true);
            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                selectPrevLetter(true, true);
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                selectNextLetter(true, true);

            gui.changePanelInfo(getSelectedClueNum(), acrossSelected);
            repaint();
        }
    }

    private void moveRight() {
        if (selectedI == crossword.getWidth() - 1) {
            selectedJ++;
            selectedJ %= crossword.getHeight();
        }
        selectedI++;
        selectedI %= crossword.getWidth();
    }

    private void moveLeft() {
        if (selectedI == 0) {
            if (selectedJ == 0)
                selectedJ = crossword.getHeight();
            selectedJ--;
            selectedI = crossword.getWidth();
        }
        selectedI--;
    }

    private void moveDown() {
        if (selectedJ == crossword.getHeight() - 1) {
            selectedI++;
            selectedI %= crossword.getWidth();
        }
        selectedJ++;
        selectedJ %= crossword.getHeight();
    }

    private void moveUp() {
        if (selectedJ == 0) {
            if (selectedI == 0)
                selectedI = crossword.getWidth();
            selectedI--;
            selectedJ = crossword.getHeight();
        }
        selectedJ--;
    }

    private void selectNextLetter(boolean isAcross, boolean canJump) {
        if (isAcross) {
            if (canJump) {
                moveRight();
                while (crossword.getNumbers()[selectedJ][selectedI] == -1) {
                    moveRight();
                }
            } else if (selectedI != crossword.getWidth() - 1 && crossword.getNumbers()[selectedJ][selectedI + 1] != -1) {
                selectedI++;
            }
        } else {
            if (canJump) {
                moveDown();
                while (crossword.getNumbers()[selectedJ][selectedI] == -1) {
                    moveDown();
                }
            } else if (selectedJ != crossword.getHeight() - 1 && crossword.getNumbers()[selectedJ + 1][selectedI] != -1) {
                selectedJ++;
            }
        }
    }

    private void selectPrevLetter(boolean isAcross, boolean canJump) {
        if (isAcross) {
            if (canJump) {
                moveLeft();
                while (crossword.getNumbers()[selectedJ][selectedI] == -1) {
                    moveLeft();
                }
            } else if (selectedI != 0 && crossword.getNumbers()[selectedJ][selectedI - 1] != -1) {
                selectedI--;
            }
        } else {
            if (canJump) {
                moveUp();
                while (crossword.getNumbers()[selectedJ][selectedI] == -1) {
                    moveUp();
                }
            } else if (selectedJ != 0 && crossword.getNumbers()[selectedJ - 1][selectedI] != -1) {
                selectedJ--;
            }
        }
    }

    public void changeWordSelection(int selectedIndex, boolean isAcross) {
        acrossSelected = isAcross;
        int selectedNum = crossword.getAcrossClues().get(selectedIndex).getNumber();
        if (!isAcross)
            selectedNum = crossword.getDownClues().get(selectedIndex).getNumber();
        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                if (crossword.getNumbers()[j][i] == selectedNum) {
                    selectedI = i;
                    selectedJ = j;
                    break;
                }
            }
        }
        repaint();
    }

    public void fillInWord(String suggestion){
        if(suggestion.length() == crossword.getSuggestion(getSelectedClueNum(), acrossSelected).length()) {
            crossword.fillInWord(suggestion, getSelectedClueNum(), acrossSelected);
            repaint();
        }
    }

    private int getSelectedClueNum() {
        int selectedClueNum = -1;
        if (selectedI != -1 && selectedJ != -1) { // if a box is selected
            int IChange = 0;
            int JChange = 1;
            if (acrossSelected) {
                IChange = 1;
                JChange = 0;
            }
            int i = selectedI;
            int j = selectedJ;
            while (i >= 0 && j >= 0 && crossword.getNumbers()[j][i] != -1) {
                i -= IChange;
                j -= JChange;
            }
            selectedClueNum = crossword.getNumber(j + JChange, i + IChange);
        }
        return selectedClueNum;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (mode.equals("template")) {
            keyTemplatePressed(e);
        } else if (mode.equals("chooseTemplate")) {
            keyChooseTemplatePressed(e);
        } else if (mode.equals("file")) {
            keyFilePressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    private void mouseTemplateClicked(int mouseX, int mouseY) {
        int mouseI = mouseX / boxSize;
        int mouseJ = mouseY / boxSize;
        if (crossword.getNumbers()[mouseJ][mouseI] == -1) {
            crossword.setNumber(0, mouseJ, mouseI);
            crossword.setNumber(0, (crossword.getWidth() - 1) - mouseJ, (crossword.getHeight() - 1) - mouseI);
            if (mouseJ == (crossword.getWidth() - 1) - mouseJ && mouseI == (crossword.getHeight() - 1) - mouseI)
                crossword.changeNumBoxesFilled(-1);
            else
                crossword.changeNumBoxesFilled(-2);
        } else {
            crossword.setNumber(-1, mouseJ, mouseI);
            crossword.setNumber(-1, (crossword.getWidth() - 1) - mouseJ, (crossword.getHeight() - 1) - mouseI);
            if (mouseJ == (crossword.getWidth() - 1) - mouseJ && mouseI == (crossword.getHeight() - 1) - mouseI)
                crossword.changeNumBoxesFilled(1);
            else
                crossword.changeNumBoxesFilled(2);
        }
        crossword.setNumbers();
        crossword.setTemplateName();
        repaint();
    }

    private void mouseFileClicked(int mouseX, int mouseY) {
        int mouseI = mouseX / boxSize;
        int mouseJ = mouseY / boxSize;
        if(isSelectable(mouseI, mouseJ)) {
            if (mouseI == selectedI && mouseJ == selectedJ) {
                acrossSelected = !acrossSelected;
            } else {
                selectedI = mouseI;
                selectedJ = mouseJ;
            }
            gui.changePanelInfo(getSelectedClueNum(), acrossSelected);
            repaint();
        }
    }

    private boolean isSelectable(int mouseI, int mouseJ){
        boolean isSelectable = true;
        if(crossword.getNumber(mouseJ, mouseI) == -1)
            isSelectable = false;
        return isSelectable;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX() - startX;
        int mouseY = e.getY() - startY;
        if (mouseX > 0 && mouseY > 0 && mouseX < crossword.getWidth() * boxSize && mouseY < crossword.getHeight() * boxSize) {
            setFocusable(true);
            requestFocusInWindow();
            if (mode.equals("template")) {
                mouseTemplateClicked(mouseX, mouseY);
            } else if (mode.equals("chooseTemplate")) {
                crosswordMaker.templateChosen();
            } else if (mode.equals("file")) {
                mouseFileClicked(mouseX, mouseY);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }


    public void mouseMoved(int mouseX, int mouseY) {
        int gridX = mouseX - getX();
        int gridY = mouseY - getY();
        if (mode.equals("chooseTemplate")) {
            Rectangle rect = new Rectangle(startX, startY, startX + crossword.getWidth() * boxSize, startY + crossword.getHeight() * boxSize);
            Area gridArea = new Area(rect);
            if (gridArea.contains(gridX, gridY)) {
                isHighlighted = true;
            } else {
                isHighlighted = false;
            }
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMoved(e.getX() + getX(), e.getY() + getY()); // offset included
    }

}
