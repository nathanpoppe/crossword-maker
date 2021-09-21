import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CrosswordWriter {

    public void saveCrossword(Crossword crossword){
        try{
            FileWriter fileWriter = new FileWriter("resources/crosswords/" + crossword.getName() + ".txt");
            fileWriter.write(crossword.getSaveString());
            fileWriter.close();
        }
        catch(IOException e){
            System.out.println("Could not write to file.");
        }
    }

    private void insertTemplate(ArrayList<Crossword> templates, Crossword newTemplate){
        boolean templateInserted = false;
        int count = 0;
        while(count < templates.size() && !templateInserted){
            if(newTemplate.getWidth() < templates.get(count).getWidth()){
                templates.add(count, newTemplate);
                templateInserted = true;
            }
            else if(newTemplate.getWidth() == templates.get(count).getWidth()){
                if(newTemplate.getNumBoxesFilled() >= templates.get(count).getNumBoxesFilled()){
                    templates.add(count, newTemplate);
                    templateInserted = true;
                }
            }
            else if(count == templates.size() - 1) {
                templates.add(newTemplate);
                templateInserted = true;
            }
            count++;
        }
    }

    public void saveTemplates(ArrayList<Crossword> templates, Crossword newTemplate, String fileName){

        if(newTemplate != null)
            insertTemplate(templates, newTemplate);

        try{
            FileWriter fileWriter = new FileWriter(fileName, false);
            for(int i = 0; i < templates.size(); i++){
                fileWriter.write(templates.get(i).getSaveTemplateString());
            }
            fileWriter.close();
        }
        catch(IOException e){
            System.out.println("Could not write to file.");
        }
    }

}
