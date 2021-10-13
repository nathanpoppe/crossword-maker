# crossword-maker

## Description

This program provides an easy-to-use GUI for creating and saving crosswords. 

Features include:
* Creating and saving crossword templates for future use
* Creating, building, opening and saving crosswords
* A "clue" mode that allows the user to enter clues for filled in words
* A "helper" mode that produces all possible words that fill out a certain across/down number in the crossword
  * Example: if you need a word that starts with 'h' and is 4 letters long, every possible word matching that description is listed
* Saving as an auto-formatted 1 page PDF document for easy printing

## Running the Program (Unix)
Enter the following command prompts into the terminal:

1. Locate CrosswordMaker directory: 
   
cd Downloads/crossword-maker-master/src/main
   
2. Compile code: 

javac -cp .:java/jars/io-7.1.16.jar:java/jars/kernel-7.1.16.jar:java/jars/layout-7.1.16.jar:java/jars/log4j.jar:java/jars/slf4j.api-1.6.1.jar:java/jars/slf4j-log4j12-1.7.8.jar: -Xlint:unchecked java/*.java
   
3. Run program: 

java -cp .:java/jars/io-7.1.16.jar:java/jars/kernel-7.1.16.jar:java/jars/layout-7.1.16.jar:java/jars/log4j.jar:java/jars/slf4j.api-1.6.1.jar:java/jars/slf4j-log4j12-1.7.8.jar: Main
