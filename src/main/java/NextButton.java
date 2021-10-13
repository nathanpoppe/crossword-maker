import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextButton extends JButton implements ActionListener {

    private static final String PREV_ICON_NAME = "src/main/resources/previous-icon.png";
    private static final String NEXT_ICON_NAME = "src/main/resources/next-icon.png";

    private ImageIcon icon;
    private boolean isNext;
    private CrosswordMaker crosswordMaker;

    public NextButton(int width, int height, boolean isNext, CrosswordMaker crosswordMaker) {
        this.crosswordMaker = crosswordMaker;
        this.isNext = isNext;
        if(isNext)
            icon = new ImageIcon(NEXT_ICON_NAME);
        else
            icon = new ImageIcon(PREV_ICON_NAME);
        setIcon(scaleIcon(width,height));
        addActionListener(this);
    }

    private ImageIcon scaleIcon(int width, int height){
        Image image = icon.getImage(); // transform it
        Image newImg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon imageIcon = new ImageIcon(newImg);
        return imageIcon;
    }

    public void actionPerformed(ActionEvent e) {
        if(isNext)
            crosswordMaker.nextTemplate(true);
        else
            crosswordMaker.nextTemplate(false);
    }

}
