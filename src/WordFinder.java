import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class WordFinder {

    private final static String REG_FILE_NAME = "resources/84000-words.txt";
    private final static String LONG_FILE_NAME = "resources/370000-words.txt";

    private ArrayList<String> regWordList;
    private ArrayList<String> longWordList;

    public WordFinder(){
        regWordList = readInWords(REG_FILE_NAME);
        longWordList = readInWords(LONG_FILE_NAME);
    }

    private ArrayList<String> readInWords(String fileName){
        ArrayList<String> wordList = new ArrayList<String>();
        try{
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()){
                wordList.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Could not read in words!");
        }
        return wordList;
    }

    private ArrayList<String> setWordList(boolean useRegWordList){
        if(useRegWordList)
            return regWordList;
        else
            return longWordList;
    }

    public ArrayList<String> findPossibleWords(boolean useRegWordList, String clue){
        ArrayList<String> wordList = setWordList(useRegWordList);
        ArrayList<String> possibleWords = new ArrayList<String>();
        for(int i = 0; i < wordList.size(); i++){
            String word = wordList.get(i);
            clue = clue.toLowerCase().trim();
            if(word.length() == clue.length()){
                boolean possibleWord = true;
                int letterCount = 0;
                while(possibleWord && letterCount < word.length()){
                    if(clue.charAt(letterCount) == word.charAt(letterCount) || clue.charAt(letterCount) == '_' || clue.charAt(letterCount) == '@' && isVowel(word.charAt(letterCount)) || clue.charAt(letterCount) == '#' && !isVowel(word.charAt(letterCount))){
                        if(letterCount == word.length() - 1){
                            possibleWords.add(word);
                        }
                        letterCount++;
                    }
                    else{
                        possibleWord = false;
                    }
                }
            }
        }
        return possibleWords;
    }


    public ArrayList<String> findAlternatingWords(boolean useRegWordList, String clue){
        ArrayList<String> checkWords = findPossibleWords(useRegWordList, clue);
        ArrayList<String> possibleWords = new ArrayList<String>();
        for(int i = 0; i < checkWords.size(); i++){
            String word = checkWords.get(i);
            boolean possibleWord = true;
            int letterCount = 1;
            while(possibleWord && letterCount < word.length()){
                if(isVowel(word.charAt(letterCount - 1)) == isVowel(word.charAt(letterCount))){
                    possibleWord = false;
                }
                else{
                    if(letterCount == word.length() - 1){
                        possibleWords.add(word);
                    }
                }
                letterCount++;
            }
        }
        return possibleWords;
    }

    private boolean isVowel(char c){
        return "AEIOUaeiou".indexOf(c) != -1;
    }

}

