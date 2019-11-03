package preprocess;

import com.softHeart.preprocess.TextAnalyzer;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TextAnalyzerTest {

    @Test
    public void extractAllWordsTest() {
        String text = "Hello people, how are you?";
        Set<String> expectedWords = new HashSet<>();
        expectedWords.add("hello");
        expectedWords.add("people");
        expectedWords.add("how");
        expectedWords.add("are");
        expectedWords.add("you");
        Set<String> allWordsFromText = TextAnalyzer.extractAllWords(text);
        assertTrue(allWordsFromText.containsAll(expectedWords));
    }

}
