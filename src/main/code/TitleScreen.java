import javax.swing.*;
import java.awt.*;

public class TitleScreen extends JPanel {

    private final static float TITLE_RATIO = 0.03f;
    private final static float AUTHOR_RATIO = 0.02f;

    public TitleScreen(Dimension contentPaneSize){
        setPreferredSize(contentPaneSize);
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("CROSSWORD MAKER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, (int)(TITLE_RATIO * contentPaneSize.getWidth())));
        titleLabel.setForeground(new Color(0x262626));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel authorLabel = new JLabel("made by Nathan Poppe");
        authorLabel.setFont(new Font("Arial", Font.PLAIN, (int)(AUTHOR_RATIO * contentPaneSize.getWidth())));
        authorLabel.setForeground(new Color(0x3a3a3a));
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        add(authorLabel, gbc);
    }
}
