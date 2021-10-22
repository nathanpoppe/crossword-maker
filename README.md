# crossword-maker

## Description

This program provides an easy-to-use GUI for creating and saving crosswords. If you enjoy doing crosswords and want to take a stab at making your own you've come to the right place! :)

Features include:
* Creating and saving crossword templates for future use
* Creating, building, opening and saving crosswords
* A "clue" mode that allows the user to enter clues for filled in words
* A "helper" mode that produces all possible words that fill out a certain across/down number in the crossword
  * Example: if you need a word that starts with 'h' and is 4 letters long, every possible word matching that description is listed
* Saving as an auto-formatted 1 page PDF document for easy printing

## Running the Program (Unix)

You will need to have the Java Runtime Environment installed to run any java applications.
The download link is available here: https://www.java.com/en/download/manual.jsp

To download the CrosswordMaker files: click the green 'Code' button and then the 'Download ZIP' button.
Locate the zip file in your Downloads folder and open it.

Next, enter the following command prompts into the terminal:

1. Locate CrosswordMaker directory: 

```
cd Downloads/crossword-maker-master/src/main
```

2. Compile code: 

```
javac -cp .:java/jars/io-7.1.16.jar:java/jars/kernel-7.1.16.jar:java/jars/layout-7.1.16.jar:java/jars/log4j.jar:java/jars/slf4j.api-1.6.1.jar:java/jars/slf4j-log4j12-1.7.8.jar: -Xlint:unchecked java/*.java
```

```
javac -cp .:code/jars/io-7.1.16.jar:code/jars/kernel-7.1.16.jar:code/jars/layout-7.1.16.jar:code/jars/log4j.jar:code/jars/slf4j.api-1.6.1.jar:code/jars/slf4j-log4j12-1.7.8.jar: -Xlint:unchecked code/*.java
```
   
3. Run program: 

```
java -cp .:code/jars/io-7.1.16.jar:code/jars/kernel-7.1.16.jar:code/jars/layout-7.1.16.jar:code/jars/log4j.jar:code/jars/slf4j.api-1.6.1.jar:code/jars/slf4j-log4j12-1.7.8.jar: code/Main
```
