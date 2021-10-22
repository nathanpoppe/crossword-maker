import org.apache.log4j.BasicConfigurator;

public class Main {

    public static void main(String[] args) {

        BasicConfigurator.configure(); // for the PDFWriter class
        CrosswordMaker crosswordMaker = new CrosswordMaker();
        crosswordMaker.runApplication();

    }
}
