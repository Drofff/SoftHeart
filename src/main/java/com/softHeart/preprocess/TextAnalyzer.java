package com.softHeart.preprocess;

import com.softHeart.utils.PreProcessUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

public class TextAnalyzer {

    private static final String WHITESPACE_SEPARATOR_PATTERN = "\\s+";
    private static final String SENTENCE_SEPARATOR_PATTERN = "[?!.]";

    private static Set<String> questionKeyWords;
    public static final CharArraySet MEANINGLESS_WORDS;

    static {
        MEANINGLESS_WORDS = new StandardAnalyzer().getStopwordSet();
    }

    private TextAnalyzer() {}

    public static Set<String> extractAllWords(String statement) {
        if(Objects.isNull(statement) || statement.isEmpty()) {
            return Collections.emptySet();
        }
        String [] partsOfSentence = statement.split(WHITESPACE_SEPARATOR_PATTERN);
        return Arrays.stream(partsOfSentence)
                .map(TextAnalyzer::wordPreProcess)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toSet());
    }

    private static String wordPreProcess(String word) {
        String preprocessedWord =  word.trim().toLowerCase();
        return PreProcessUtils.removeSpecialSymbols(preprocessedWord);
    }

    public static Set<String> extractAllQuestionKeyWords(String text) {
        String lowerCaseText = text.toLowerCase();
        Set<String> allQuestionKeyWords = new HashSet<>();
        getQuestionKeys().stream()
                .filter(x -> PreProcessUtils.isFirstWordInTextOrSentence(x, lowerCaseText))
                .forEach(allQuestionKeyWords::add);
        return allQuestionKeyWords;
    }

    public static Set<String> divideIntoSentences(String text) {
        String [] sentences = text.split(SENTENCE_SEPARATOR_PATTERN);
        return new HashSet<>(Arrays.asList(sentences));
    }

    private static Set<String> getQuestionKeys() {
        if(Objects.isNull(questionKeyWords)) {
            questionKeyWords = new HashSet<>(
                    Arrays.asList("what", "when", "where", "which", "who", "whom",
                            "whose", "why", "how", "did", "didn't", "does", "doesn't",
                            "do", "don't", "am", "can", "can't", "is", "isn't",
                            "have", "haven't", "has", "hasn't", "may", "should", "shouldn't",
                            "are", "aren't", "were", "weren't", "was", "wasn't", "will")
            );
        }
        return questionKeyWords;
    }

}
