import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  CLASS: CrosswordReader
 *  PURPOSE: Reads in templates/crosswords for the purpose of displaying/manipulating them
 *  AUTHOR: Nathan Poppe
 *
 *  TEXT FILE LAYOUT EXAMPLE: (converted to Crossword class)
 *  crossword-example.txt:
 *  Nate's Crossword  // the name of the crossword, template X: if a template where X is the template number
 *  15                // the width of the crossword
 *  15                // the height of the crossword
 *  18                // the number of boxes filled in the crossword (a greater boxes filled value indicates an easier to make crossword)
 *  0000!0000!0000... // the number code: each 0 corresponds to an unfilled box, each ! corresponds to a filled box (the length of this code should be width*height)
 *  HOPE_CHAT_SWOR... // the letter code: each char corresponds to a letter written in ('_' if no letter added yet or box is filled) (length should be width*height)
 *  1. desire         // the 1 across clue
 *  5. chat           // the 5 across clue
 *  ...
 *  1. opposite of..  // the 1 down clue (if the clue number decreases that means we are on the down clues)
 *  ...
 *
 *  Note: the numbers and letters are read in left to right:
 *  eg. char 0: corresponds to number[0][0]
 *      char 14: corresponds to number[0][14]
 *      char 15: corresponds to number[1][0]
 */

public class CrosswordReader {

    /**
     * Reads in templates/crosswords from a filePath (see above for layout)
     * @param filePath - the file path to the saved template/crossword
     * @param isTemplate - true if reading templates / false if reading crosswords
     * @return - a list of Crossword instances (list length is 1 if reading a crossword)
     */
    public ArrayList<Crossword> readInCrosswords(String filePath, boolean isTemplate){
        ArrayList<Crossword> crosswordList = new ArrayList<Crossword>();

        try{
            Scanner scanner = new Scanner(new File(filePath));
            while(scanner.hasNextLine()){
                String name = scanner.nextLine();
                int width = Integer.parseInt(scanner.nextLine());
                int height = Integer.parseInt(scanner.nextLine());
                int numBoxesFilled = Integer.parseInt(scanner.nextLine());
                int[][] numbers = setNumbers(width, height, scanner.nextLine());

                if(!isTemplate){ // only read in letters and clues if an actual crossword
                    char[][] letters = setLetters(width, height, scanner.nextLine());

                    ArrayList<Clue> acrossClues = new ArrayList<Clue>();
                    ArrayList<Clue> downClues = new ArrayList<Clue>();
                    ArrayList<Clue> currentClues = acrossClues;

                    int currentNum = -1;
                    while(scanner.hasNextLine()){
                        Clue nextClue = getClue(scanner.nextLine());

                        // if clue number is less than previous, switch to down clues
                        if(nextClue.getNumber() < currentNum)
                            currentClues = downClues;

                        currentNum = nextClue.getNumber();
                        currentClues.add(nextClue);
                    }

                    crosswordList.add(new Crossword(name, width, height, numBoxesFilled, numbers, letters, acrossClues, downClues));
                }

                else{ // if a template
                    crosswordList.add(new Crossword(name, width, height, numBoxesFilled, numbers));
                }
            }
            scanner.close();
        }
        catch(Exception e){
            System.out.println("File: " + filePath + " not found.");
        }
        return crosswordList;
    }

    /**
     * Converts a string of "numbers" into the properly formatted array of across/down numbers
     * @param width - width of the crossword
     * @param height - height of the crossword
     * @param numberCode - string of all "numbers", 0's (empty boxes) !'s  (filled boxes)
     * @return - the multidimensional array of numbers (across/down)
     */
    private int[][] setNumbers(int width, int height, String numberCode){
        int[][] numbers = new int[width][height];
        for(int i = 0; i < numberCode.length(); i++){
            if(numberCode.charAt(i) == '!'){
                numbers[i / width][i % height] = -1;
            }
            else{
                numbers[i / width][i % height] = 0;
            }
        }
        return numbers;
    }

    /**
     * Converts a string of character into the properly formatted array of letters ('_' for unfilled letters)
     * @param width - width of the crossword
     * @param height - height of the crossword
     * @param letterCode - the string of all letters
     * @return - the multidimensional array of crossword letters
     */
    private char[][] setLetters(int width, int height, String letterCode){
        char[][] letters = new char[width][height];
        for(int i = 0; i < letterCode.length(); i++){
            letters[i / width][i % height] = letterCode.charAt(i);
        }
        return letters;
    }

    /**
     * Converts a clue save string into an instance of the Clue class
     * @param nextLine - the clue save string
     * @return - the actual "Clue" to be added to the crossword
     */
    private Clue getClue(String nextLine){
        return new Clue(getClueDescription(nextLine), getClueNumber(nextLine));
    }

    /**
     * Gets the clue number from the clue save string
     * @param nextLine - the clue save string
     * @return - the clue number
     */
    private int getClueNumber(String nextLine){
        String[] tokens = nextLine.split("\\."); // split string using the '.' character
        return Integer.parseInt(tokens[0]);
    }

    /**
     * Gets the clue description from the clue save string
     * @param nextLine - the clue save string
     * @return - the clue description ("" if no description)
     */
    private String getClueDescription(String nextLine){
        String description = "";
        int count = 0;
        while(nextLine.charAt(count) != '.') {
            count++;
        }
        count++;
        for(int i = count; i < nextLine.length(); i++){
            description += nextLine.charAt(i);
        }
        return description.trim();
    }


}