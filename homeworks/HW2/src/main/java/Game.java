import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Game.class);
    private static final Random random = new Random();
    public String word;
    private final List<String> dictionary;

    public Game(List<String> words) {
        this.dictionary = words;
    }

    public String secretWord() {
        return dictionary.get(random.nextInt(dictionary.size()));
    }

    public int[] compute(String strTry) {
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < strTry.length(); i++) {
            if (strTry.charAt(i) == word.charAt(i)) {
                bulls++;
            } else if (word.indexOf(strTry.charAt(i)) != -1) {
                cows++;
            }
        }
        return new int[]{cows, bulls};
    }

    void start() {
        System.out.println("Welcome to The Bulls & Cows game!");
        log.info("Logger run!");
        String playAgain;
        Scanner scanner = new Scanner(System.in);
        do {
            word = secretWord();
            log.info("Secret word is " + word);
            System.out.println("I offered a " + word.length() + "-letter word, your guess?");
            playRound(scanner);
            System.out.println("Wanna play again? Y/N");
            playAgain = scanner.next();
        } while (playAgain.equalsIgnoreCase("Y"));
    }

    private void playRound(Scanner scanner) {
        int losses = 0;
        while (losses < 10) {
            String strTry = scanner.next();
            if (strTry.length() != word.length()) {
                System.out.println("Incorrect word length. Try again.");
                continue;
            }
            if (strTry.equals(word)) {
                System.out.println("You won!");
                log.info("Secret word guessed!");
                return;
            }
            System.out.println("Cows: " + compute(strTry)[0]);
            System.out.println("Bulls: " + compute(strTry)[1]);
            losses++;
            System.out.println("Guesses left: " + (10 - losses));
        }
        System.out.println("You lose");
        log.info("Secret word not guessed!");
    }
}
