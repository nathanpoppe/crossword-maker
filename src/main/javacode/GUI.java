import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class GUI extends JFrame implements MouseMotionListener {

    private final static double GAP_RATIO = 0.02; // multiplied by screen width to get gap between components
    private final static double BUTTON_RATIO = 0.04; // multiplied by screen width to get button dimensions

    private Dimension screenSize;

    private CrosswordMaker crosswordMaker;
    private CrosswordGraphics crosswordGraphics;
    private ControlPanel controlPanel;
    private Menu menu;

    private int containerWidth;
    private int containerHeight;
    private int gapWidth;

    public GUI(CrosswordMaker crosswordMaker){
        super("Crossword Maker");
        this.crosswordMaker = crosswordMaker;

        setUpMenu("title");
        setUpFrame();
        setUpContentPane();
    }

    private void setUpFrame(){
        Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        screenSize = new Dimension((int)(maxBounds.getWidth()), (int)(maxBounds.getHeight()));
        setPreferredSize(screenSize);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setUpContentPane(){
        this.containerWidth = getContentPane().getWidth();
        this.containerHeight = getContentPane().getHeight();
        this.gapWidth = (int)(GAP_RATIO * containerWidth);
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, gapWidth, gapWidth));
        //getContentPane().setBackground(Color.LIGHT_GRAY); // used to change application background color
        getContentPane().add(new TitleScreen(new Dimension(containerWidth, containerHeight - 4 * gapWidth)));
        pack();
    }


    private void setUpMenu(String startingMode){
        menu = new Menu(crosswordMaker);
        menu.changeMenu(startingMode);
        setJMenuBar(menu);
        pack();
    }

    public void setUpNewTemplate(Crossword crossword){
        refreshScreen();
        showCrosswordGraphics(crossword, "template");
        pack();

    }

    public void setUpChooseTemplate(Crossword crossword){
        refreshScreen();
        showIcon(false);
        showCrosswordGraphics(crossword, "chooseTemplate");
        showIcon(true);
        addMouseMotionListener(this);
        pack();
    }

    public void showIcon(boolean isNext){
        int buttonSize = (int)(BUTTON_RATIO * containerWidth);
        getContentPane().add(new NextButton(buttonSize, buttonSize, isNext, crosswordMaker));
    }

    private void showCrosswordGraphics(Crossword crossword, String mode){
        crosswordGraphics = new CrosswordGraphics(crossword, (containerWidth - 3 * gapWidth) / 2, containerHeight - 2 * gapWidth, mode, crosswordMaker, this);
        getContentPane().add(crosswordGraphics);
        crosswordGraphics.requestFocusInWindow();
    }

    public void setUpNewFile(Crossword crossword, WordFinder wordFinder){
        refreshScreen();
        controlPanel = new ControlPanel(crossword, (containerWidth - 3 * gapWidth) / 2, containerHeight - 2 * gapWidth, this);
        showCrosswordGraphics(crossword, "file");
        crosswordGraphics.selectFirstWord();
        getContentPane().add(controlPanel);
        pack(); // finalizes JPanel width/height
        controlPanel.setUpCluesPanel();
        controlPanel.setUpHelperPanel(wordFinder);
    }

    private void refreshScreen(){
        getContentPane().removeAll();
        getContentPane().repaint();
    }

    public void changeMenu(String mode){
        menu.changeMenu(mode);
    }

    public void changePanelInfo(int selectedNum, boolean isAcross){
        controlPanel.changePanelInfo(selectedNum, isAcross);
    }

    public void changeWordSelection(int selectedIndex, boolean isAcross){
        crosswordGraphics.changeWordSelection(selectedIndex, isAcross);
    }

    public void fillInWord(String suggestion){
        crosswordGraphics.fillInWord(suggestion);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        crosswordGraphics.mouseMoved(e.getX(), e.getY());
    }
}
