import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.LoggerFactory;

public class BullsCows {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BullsCows.class);

    public static List<String> getDictionary(String filename) throws IOException {
        List<String> readWords = new ArrayList<>();
        try (FileReader fileReader = new FileReader(filename)) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                readWords.add(scan.nextLine());
            }
            fileReader.close();
            log.info("Dictionary loaded!");
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return readWords;
    }

    public static void main(String[] args) throws IOException {
        log.info("Logger run!");
        List<String> words = getDictionary("dictionary.txt");
        Game game = new Game(words);
        log.info("Start B's&C's Game!");
        game.start();
        log.info("Finish!");
    }
}

