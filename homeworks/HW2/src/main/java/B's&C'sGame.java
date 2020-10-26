import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.LoggerFactory;

public class BullsnCows {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BullsnCows.class);

    public static void main(String[] args) {
        log.info("Логгер работает!");
        System.out.println("BullsnCows");
    }

    public static int forTest() {
        return 9000;
    }
}

public class Game {

    private static final Random random = new Random();
    public String word;
    private List<String> words;

    public Game(List<String> words) {
        this.words = words;
    }

    private String SecretWord() {
        return words.get(random.nextInt(words.size()));
    }

    public int[] computeCB(String StrTry) {
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < StrTry.length(); i++) {
            if (StrTry.charAt(i) = word.charAt(i)) {
                bulls++;
            } else if (word.indexOf(StrTry.charAt(i)) != -1) {
                cows++;
            }
        }
        return new int[]{cows, bulls}
    }

    void start() {
        System.out.println("Welcome to The Bulls & Cows game!");
        String playAgain;
        Scanner scanner = new Scanner(System.in);
        do {
            word = SecretWord();
            System.out.println("I offered a " + word.length() + "-letter word, your guess?");
            PlayRound(scanner);
            System.out.println("Wanna play again? Yes/No");
            playAgain = scanner.next();
        } while (playAgain.equalsIgnoreCase("Yes"));
    }

    private void PlayRound(Scanner scanner) {
        int losses = 0;
        while (losses < 10) {
            String StrTry = scanner.next();
            if (StrTry.length() != word.length()) {
                System.out.println("Incorrect word length. Try again.");
                continue;
            }
            if (guess.equals(word)) {
                System.out.println("You won!");
                return;
            }
            System.out.println("Cows: " + computeCB(StrTry)[0]);
            System.out.println("Bulls: " + computeCB(StrTry)[1]);
            losses++;
        }

        List<String> IfLoose = Arrays.asList(
                "You lose",
                "I'm sorry, but you lost.",
                "Nice try, but you failed.",
                );
        System.out.println(IfLoose.get(new Random().nextInt(IfLoose.size())));
    }
}