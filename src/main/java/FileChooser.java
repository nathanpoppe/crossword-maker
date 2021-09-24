import javax.swing.*;
import java.io.File;

public class FileChooser extends JFileChooser {

    private final static String FILE_PATH = "src/main/resources/crosswords";

    public FileChooser(){
        setCurrentDirectory(new File(FILE_PATH));
    }

    public File getFile(){
        File selectedFile = null;
        int result = showOpenDialog(new JFrame());

        if(result == APPROVE_OPTION){
            selectedFile = getSelectedFile();
        }
        return selectedFile;
    }

    public void saveFile(Crossword crossword){
        setSelectedFile(new File(crossword.getName() + ".txt"));

        int result = showSaveDialog(new JFrame());

        if(result == APPROVE_OPTION){
            String fileName = getSelectedFile().getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            crossword.setName(fileName);
        }

    }


}
