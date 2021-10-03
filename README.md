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

Compile: 
javac -cp .:jars/io-7.1.16.jar:jars/kernel-7.1.16.jar:jars/layout-7.1.16.jar:jars/log4j.jar:jars/slf4j.api-1.6.1.jar:jars/slf4j-log4j12-1.7.8.jar: -Xlint:unchecked Clue.java CluesPanel.java ControlPanel.java Crossword.java CrosswordGraphics.java CrosswordMaker.java CrosswordReader.java CrosswordWriter.java FileChooser.java GUI.java HelperPanel.java LetterField.java Main.java Menu.java NextButton.java PDFWriter.java TitleScreen.java WordFinder.java
javac -cp .:java/jars/io-7.1.16.jar:java/jars/kernel-7.1.16.jar:java/jars/layout-7.1.16.jar:java/jars/log4j.jar:java/jars/slf4j.api-1.6.1.jar:java/jars/slf4j-log4j12-1.7.8.jar: -Xlint:unchecked java/Clue.java java/CluesPanel.java java/ControlPanel.java java/Crossword.java java/CrosswordGraphics.java java/CrosswordMaker.java java/CrosswordReader.java java/CrosswordWriter.java java/FileChooser.java java/GUI.java java/HelperPanel.java java/LetterField.java java/Main.java java/Menu.java java/NextButton.java java/PDFWriter.java java/TitleScreen.java java/WordFinder.java

Run: 
java -cp .:jars/io-7.1.16.jar:jars/kernel-7.1.16.jar:jars/layout-7.1.16.jar:jars/log4j.jar:jars/slf4j.api-1.6.1.jar:jars/slf4j-log4j12-1.7.8.jar: Main
java -cp .:java/jars/io-7.1.16.jar:java/jars/kernel-7.1.16.jar:java/jars/layout-7.1.16.jar:java/jars/log4j.jar:java/jars/slf4j.api-1.6.1.jar:java/jars/slf4j-log4j12-1.7.8.jar: Main


