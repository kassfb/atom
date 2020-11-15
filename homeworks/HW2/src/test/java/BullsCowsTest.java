import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class BullsCowsTest {

    @Test
    public void allBullsTest() {
        Game game = new Game(null);
        game.word = "java";
        int[] result = game.compute("java");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(4, result[1]);

        game.word = "zone";
        result = game.compute("zone");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(4, result[1]);
    }

    @Test
    public void checkBullsCow() {
        Game game = new Game(null);
        game.word = "monkey";
        int[] result = game.compute("mokney");
        Assert.assertEquals(2, result[0]);
        Assert.assertEquals(4, result[1]);

        game.word = "father";
        result = game.compute("mother");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(4, result[1]);

        game.word = "doge";
        result = game.compute("code");
        Assert.assertEquals(1, result[0]);
        Assert.assertEquals(2, result[1]);
    }

    @Test
    public void noBullsCowsTest() {
        Game game = new Game(null);
        game.word = "mother";
        int[] result = game.compute("banana");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(0, result[1]);
    }

    @Test
    public void loadDictionaryNullCheck() throws IOException {
        List<String> words = BullsCows.getDictionary("dictionary.txt");
        Game game = new Game(words);
        String rndWord = game.secretWord();
        Assert.assertNotNull(rndWord);
    }

}