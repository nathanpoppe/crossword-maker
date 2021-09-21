/**
 *  CLASS: Clue
 *  PURPOSE: Each made crossword consists of 2 ArrayLists of clues (across/down)
 *  AUTHOR: Nathan Poppe
 */
public class Clue {

    private String description; // the "actual" clue
    private int number; // the corresponding clue number

    public Clue(String description, int number){
        this.description = description;
        this.number = number;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public int getNumber(){
        return number;
    }

}
