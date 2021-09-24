import java.util.ArrayList;

/**
 *  CLASS: Crossword
 *  PURPOSE: Each template/created crossword is an instance of the Crossword class.
 *  AUTHOR: Nathan Poppe
 */
public class Crossword {

    // the "difficulty level" of the crossword is determined by the percentage of all boxes that are not filled
    private final static float HARD_DIFF_BENCHMARK = 0.85f; // any crossword with >=85% open boxes is classified as hard
    private final static float MED_DIFF_BENCHMARK = 0.82f; // any crossword with <85% and >=82% open boxes is classified as medium

    // crossword sizes pertain to the width and height of the crossword
    private final static int DEFAULT_SIZE = 15;
    private final static int MIN_SIZE = 3;
    private final static int MAX_SIZE = 20;

    private String name;
    private int width;
    private int height;
    private int numBoxesFilled; // used to determine difficulty level
    private int[][] numbers; // -1 indicates filled box, 0 indicates no number
    private char[][] letters; // '_' indicates no letter
    private ArrayList<Clue> acrossClues; // "" indicates no clue
    private ArrayList<Clue> downClues;

    // "default" constructor (used for creating a new template)
    public Crossword(int width, int height){
        this.width = width;
        this.height = height;
        this.numBoxesFilled = 0;
        setTemplateName();
        initializeNumbers(0);
        setNumbers();
        initializeLetters('_');
        initializeClues("");
    }

    // constructor used for creating a crossword from template (no letters/clues)
    public Crossword(String name, int width, int height, int numBoxesFilled, int[][] numbers){
        this.name = name;
        this.width = width;
        this.height = height;
        this.numBoxesFilled = numBoxesFilled;
        this.numbers = numbers;
        setNumbers();
        initializeLetters('_');
        initializeClues("");
    }

    // constructor used for loading an already started/completed crossword
    public Crossword(String name, int width, int height, int numBoxesFilled, int[][] numbers, char[][] letters, ArrayList<Clue> acrossClues, ArrayList<Clue> downClues) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.numBoxesFilled = numBoxesFilled;
        this.numbers = numbers;
        this.letters = letters;
        this.acrossClues = acrossClues;
        this.downClues = downClues;
        setNumbers();
    }


    /**
     * Initialize all entries of the numbers array to x
     * @param x - the integer to initialize to
     */
    private void initializeNumbers(int x){
        numbers = new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                numbers[i][j] = x;
            }
        }
    }

    /**
     * Initialize all entries of the letters array to x
     * @param x - the char to initialize to
     */
    private void initializeLetters(char x){
        letters = new char[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                letters[i][j] = '_';
            }
        }
    }

    /**
     * Initialize across and down clues to x
     * @param x - the String to initialize each clue to
     */
    private void initializeClues(String x){
        acrossClues = new ArrayList<Clue>();
        downClues = new ArrayList<Clue>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // if current box number > 0 and (on left edge or box to the left is filled)
                if (numbers[i][j] > 0 && (j == 0 || numbers[i][j - 1] == -1)) {
                    acrossClues.add(new Clue("", numbers[i][j]));
                }
                // if current box number > 0 and (on top edge or box above is filled)
                if (numbers[i][j] > 0 && (i == 0 || numbers[i - 1][j] == -1)) {
                    downClues.add(new Clue("", numbers[i][j]));
                }
            }
        }
    }

    /**
     * Convert 0s into their proper across/down numbers (or leave them as 0 if box has no number)
     * Example: -1 0 0     -1 1 2  (-1 still means a filled box)
     *           0 0 0 -->  3 0 0  (0 means no number in the box)
     *          -1 0 0     -1 4 0  (>0 means a clue number)
     */
    public void setNumbers(){
        int count = 1;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // if box is not filled in
                if(numbers[i][j] != -1) {
                    // if box is on edge
                    if (!isInBounds(i, j - 1) || !isInBounds(i - 1, j)) {
                        numbers[i][j] = count;
                        count++;
                    } else {
                        // if box is not on edge and box to left or number above is filled
                        if (numbers[i][j - 1] == -1 || numbers[i - 1][j] == -1) {
                            numbers[i][j] = count;
                            count++;
                        }
                        else{
                            numbers[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private boolean isInBounds(int i, int j){
        return i >= 0 && i < width && j >= 0 && j < height;
    }

    /**
     * Saves name, width, height, numBoxesFilled and numbers (all template data) into 1 string
     * @return - returns the partial save string (templates)
     */
    public String getSaveTemplateString(){
        String str = name + "\n" + width + "\n" + height + "\n" + numBoxesFilled + "\n";
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(numbers[i][j] == -1)
                    str += '!';
                else
                    str += '0';
            }
        }
        str += "\n";
        return str;
    }

    /**
     * Saves all crossword data into 1 string
     * @return - returns the full save string (crosswords)
     */
    public String getSaveString(){
        String str = getSaveTemplateString();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                str += letters[i][j];
            }
        }
        for(int i = 0; i < acrossClues.size(); i++){
            str += "\n" + acrossClues.get(i).getNumber() + ". " + acrossClues.get(i).getDescription();
        }
        for(int i = 0; i < downClues.size(); i++){
            str += "\n" + downClues.get(i).getNumber() + ". " + downClues.get(i).getDescription();
        }
        return str;
    }

    public String getName(){
        return name;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getNumBoxesFilled(){
        return numBoxesFilled;
    }

    public int getNumber(int row, int col){
        return numbers[row][col];
    }

    public int[][] getNumbers(){
        return numbers;
    }

    public char[][] getLetters(){
        return letters;
    }

    public ArrayList<Clue> getAcrossClues(){
        return acrossClues;
    }

    public ArrayList<Clue> getDownClues(){
        return downClues;
    }

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public static int getMinSize() {
        return MIN_SIZE;
    }

    public static int getMaxSize() {
        return MAX_SIZE;
    }

    public void setNumber(int number, int i, int j){
        numbers[i][j] = number;
    }

    public void changeNumBoxesFilled(int amount){
        numBoxesFilled += amount;
    }

    /**
     * Given a clue, returns the index of that clue from the list
     * Eg. input: 10 across, output: 6 (if 10 is the 6-th across clue)
     * @param clueNum - the number of the clue
     * @param isAcross - if the clues is across/down
     * @return the index
     */
    public int getClueIndex(int clueNum, boolean isAcross){
        ArrayList<Clue> clues = acrossClues;
        if(!isAcross)
            clues = downClues;
        int count = 0;
        while(count < clues.size() && clueNum != clues.get(count).getNumber()){
            count++;
        }
        return count;
    }

    /**
     * Given a selected clue returns the location in the letters array that the word starts at
     * @param number - the number of the clue
     * @return
     */
    public int[] getNumberLocation(int number){
        int[] location = new int[]{-1, -1};
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(numbers[j][i] == number){
                    location[0] = i;
                    location[1] = j;
                }
            }
        }
        return location;
    }

    /**
     * Given a selected clue returns the partial String that exists in the crossword at that clue location
     * @param number - the number of the clue
     * @param isAcross - if the clue is across/down
     * @return
     */
    public String getSuggestion(int number, boolean isAcross){
        int[] location = getNumberLocation(number);
        int i = location[0];
        int j = location[1];
        String suggestion = "";
        if(isAcross){
            while(i < width && numbers[j][i] != -1){
                suggestion += letters[j][i];
                i++;
            }
        }
        else{
            while(j < height && numbers[j][i] != -1){
                suggestion += letters[j][i];
                j++;
            }
        }
        return suggestion.toLowerCase();
    }

    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets a template name based on the size and difficulty rating. Eg. 15 x 15 (Medium)
     */
    public void setTemplateName(){
        String difficulty = "Easy";
        float percentOpen = 1.0f - numBoxesFilled * 1.0f / (width * height);
        if(percentOpen >= HARD_DIFF_BENCHMARK)
            difficulty = "Hard";
        else if(percentOpen >= MED_DIFF_BENCHMARK)
            difficulty = "Medium";
        this.name = width + " x " + height + " (" + difficulty + ")";
    }

    public void fillInWord(String word, int clueNumber, boolean isAcross){
        int[] wordLocation = getNumberLocation(clueNumber);
        int i = wordLocation[0];
        int j = wordLocation[1];
        int count = 0;
        if(isAcross){
            while(count < word.length()) {
                letters[j][i + count] = word.charAt(count);
                count++;
            }
        }
        else{
            while(count < word.length()) {
                letters[j + count][i] = word.charAt(count);
                count++;
            }
        }
    }


}


