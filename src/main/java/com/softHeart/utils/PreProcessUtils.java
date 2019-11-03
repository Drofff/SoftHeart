package com.softHeart.utils;

import java.util.regex.Pattern;

public class PreProcessUtils {

    private static final String NON_WORD_CHARS_PATTERN = "[?.,!]";
    private static final String FIRST_WORD_IN_SENTENCE_PATTERN_PREFIX = "(.+)([?!.])\\s*";
    private static final String FIRST_WORD_IN_TEXT_PATTERN_PREFIX = "^\\s*";
    private static final String WORD_SEPARATOR_PATTERN = "[\\s,]";

    private PreProcessUtils() {}

    public static String removeSpecialSymbols(String text) {
        return text.replaceAll(NON_WORD_CHARS_PATTERN, "");
    }

    public static String extractWordByPosition(String text, Integer position) {
        String [] words = text.split(WORD_SEPARATOR_PATTERN);
        if(words.length <= position) {
            return null;
        }
        return words[position];
    }

    public static boolean isFirstWordInTextOrSentence(String word, String text) {
        boolean isFirstInSentence = Pattern.compile(FIRST_WORD_IN_SENTENCE_PATTERN_PREFIX + word + WORD_SEPARATOR_PATTERN)
                .matcher(text)
                .find();
        boolean isFirstInText = Pattern.compile(FIRST_WORD_IN_TEXT_PATTERN_PREFIX + word + WORD_SEPARATOR_PATTERN)
                .matcher(text)
                .find();
        return isFirstInText || isFirstInSentence;
    }

}
