package com.softHeart.process;

import com.softHeart.db.QuestionsHolder;
import com.softHeart.preprocess.TextAnalyzer;
import com.softHeart.utils.PreProcessUtils;
import com.softHeart.utils.RandomUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnswerGenerator {

    private static final Integer FIRST_WORD_POSITION = 0;
    private static final Integer SECOND_WORD_POSITION = 1;
    private static final String SUBJECT_MARKER = "^s^";
    private static final String OBJECT_MARKER = "^o^";
    private static final String VERB_ENDING = "^c^";

    private static final String WHITESPACE_PATTERN = "\\s+";

    private final QuestionsHolder questionsHolder;

    public AnswerGenerator(QuestionsHolder questionsHolder) {
        this.questionsHolder = questionsHolder;
    }

    public String generateAnswer(String text) {
        String subject = getSubject(text);
        String object = getObject(text, subject);
        if(Objects.nonNull(subject) && Objects.isNull(object)) {
            return questionsHolder.getRandomQuestionWithSubjectOnly()
                    .replace(SUBJECT_MARKER, subject)
                    .replace(VERB_ENDING, getEnding(subject));
        } else if(Objects.isNull(subject)) {
            return getRandomErrorMessage();
        }
        String question = questionsHolder.getRandomQuestion();
        return question.replace(SUBJECT_MARKER, subject)
                .replace(VERB_ENDING, getEnding(subject))
                .replace(OBJECT_MARKER, object);
    }

    private String getSubject(String text) {
        String firstWord = PreProcessUtils.extractWordByPosition(text, FIRST_WORD_POSITION);
        if(Objects.isNull(firstWord)) {
            return null;
        }
        String firstWordLowerCased = firstWord.toLowerCase();
        Map<String, String> subjectMappings = getSubjectMappings();
        if(subjectMappings.containsKey(firstWordLowerCased)) {
            return subjectMappings.get(firstWordLowerCased);
        }
        Set<String> complexSubjects = getComplexSubjects();
        if(complexSubjects.contains(firstWordLowerCased)) {
            return firstWord + PreProcessUtils.extractWordByPosition(text, SECOND_WORD_POSITION);
        }
        return firstWord;
    }

    private String getObject(String text, String subject) {
        List<String> objects = TextAnalyzer.extractAllWords(text).stream()
                .filter(x -> !TextAnalyzer.MEANINGLESS_WORDS.contains(x) && !x.equals(subject))
                .collect(Collectors.toList());
        return RandomUtils.getRandom(objects);
    }

    private boolean matches(String text, String pattern) {
        return Pattern.compile(pattern)
                .matcher(text)
                .find();
    }

    private Map<String, String> getSubjectMappings() {
        Map<String, String> subjectMappings = new HashMap<>();
        subjectMappings.put("you", "I");
        subjectMappings.put("i", "you");
        subjectMappings.put("we", "you");
        return subjectMappings;
    }

    private Set<String> getComplexSubjects() {
        return new HashSet<>(Arrays.asList("his", "her", "my",
                "your", "their", "our", "its"));
    }

    private String getEnding(String subject) {
        List<String> specialSubjects = Arrays.asList("he", "she", "it");
        if(specialSubjects.contains(subject)) {
            return "s";
        }
        return "";
    }

    private String getRandomErrorMessage() {
        List<String> errorMessages = Arrays.asList("Sorry, I don't understand you",
                "I can not understand what are you talking about", "Hmm.. Can not understand.. Sorry",
                "Which language you are using right now? It seems like it's not English ;)");
        return RandomUtils.getRandom(errorMessages);
    }

}
