package com.softHeart.preprocess;

import com.softHeart.enums.TextType;
import com.softHeart.exception.IsItQuestionException;
import com.softHeart.utils.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class TextTypeClassifier {

    private static final String QUESTION_MARK_PATTERN = ".+\\?";

    public static TextType classify(String text) {
        if(!TextAnalyzer.extractAllQuestionKeyWords(text).isEmpty()) {
            return TextType.QUESTION;
        }
        if(hasQuestionMark(text)) {
            throw new IsItQuestionException(getRandomIsItQuestionAnswer());
        }
        return TextType.STATEMENT;
    }

    private static boolean hasQuestionMark(String text) {
        return Pattern.compile(QUESTION_MARK_PATTERN).matcher(text).find();
    }

    private static String getRandomIsItQuestionAnswer() {
        List<String> isItQuestionAnswers = Arrays.asList("Is it an question actually??",
                "Hmm.. Does question looks like this in your world??", "Sorry, I don't understand this one");
        return RandomUtils.getRandom(isItQuestionAnswers);
    }

}
