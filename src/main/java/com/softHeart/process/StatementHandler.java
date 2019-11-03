package com.softHeart.process;

import com.softHeart.preprocess.TextAnalyzer;
import com.softHeart.utils.PreProcessUtils;
import com.softHeart.utils.RandomUtils;

import java.util.*;

public class StatementHandler implements RequestHandler {

    private final AnswerGenerator answerGenerator;

    public StatementHandler(AnswerGenerator answerGenerator) {
        this.answerGenerator = answerGenerator;
    }

    @Override
    public String answer(String userId, String text) {
        Set<String> sentences = TextAnalyzer.divideIntoSentences(text.toLowerCase());
        Optional<String> basicAnswer = sentences.stream()
                .map(this::findBasicAnswer)
                .filter(Optional::isPresent)
                .findFirst().orElse(Optional.empty());
        return basicAnswer.orElseGet(() -> answerGenerator.generateAnswer(text));
    }

    private Optional<String> findBasicAnswer(String text) {
        String preProcessedText = preProcessStatement(text);
        Map<List<String>, List<String>> basicAnswers = getBasicAnswers();
        return basicAnswers.entrySet()
                .stream()
                .filter(x -> x.getKey().contains(preProcessedText))
                .map(x -> RandomUtils.getRandom(x.getValue()))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private String preProcessStatement(String text) {
        String lowerCasedStatement = text.toLowerCase();
        return PreProcessUtils.removeSpecialSymbols(lowerCasedStatement);
    }

    private Map<List<String>, List<String>> getBasicAnswers() {
        Map<List<String>, List<String>> basicAnswers = new HashMap<>();
        basicAnswers.put(getHelloPatterns(), getDefaultHelloMessages());
        basicAnswers.put(getYesPatterns(), getDefaultYesMessages());
        basicAnswers.put(getNoPatterns(), getDefaultNoMessages());
        basicAnswers.put(getThanksPatterns(), getDefaultThanksMessages());
        basicAnswers.put(Collections.singletonList("sorry"), Collections.singletonList("Never mind"));
        basicAnswers.put(Collections.singletonList("please"), Collections.singletonList("I'll do my best"));
        basicAnswers.put(Collections.singletonList("wait"), Collections.singletonList("Okay. I am waiting"));
        basicAnswers.put(Collections.singletonList("well done"), Collections.singletonList("thanks"));
        basicAnswers.put(Collections.singletonList("maybe"), Collections.singletonList("Why maybe?"));
        basicAnswers.put(Collections.singletonList("stop"), Collections.singletonList("Okay."));
        basicAnswers.put(getDefaultPatterns(), Collections.singletonList("))"));
        return basicAnswers;
    }

    private List<String> getHelloPatterns() {
        return Arrays.asList("hello", "hey", "hi");
    }

    private List<String> getDefaultHelloMessages() {
        return Arrays.asList("Hello, friend!", "Hello. How are you?",
        "Hey!", "Hey, there! Glad to see u!", "Hi", "He-ll-o))", "HEY))",
                "hi", "hey", "hello)", "hey-hey");
    }

    private List<String> getDefaultPatterns() {
        return Arrays.asList("good", "nice",
                "just because", "wow", "woow", "hah",
                "haha", "ahah", "great", "lol");
    }

    private List<String> getYesPatterns() {
        return Arrays.asList("yes", "yeah", "si", "yep", "sure", "of course", "definitely", "ok", "okay");
    }

    private List<String> getDefaultYesMessages() {
        return Arrays.asList("Why do you think so?", "That's great!", "Nice!", "Why?",
                "Hmm.. And why have you decided so?", "Gooood");
    }

    private List<String> getNoPatterns() {
        return Arrays.asList("no", "nope", "no way", "never");
    }

    private List<String> getDefaultNoMessages() {
        return Arrays.asList("Why?", "Why not?", "Is it a problem for you?", "Do you care about it?",
                "Okay", "hmm ok");
    }

    private List<String> getThanksPatterns() {
        return Arrays.asList("thank you", "thanks", "thnx");
    }

    private List<String> getDefaultThanksMessages() {
        return Arrays.asList("Your welcome!", "I'm happy to help you", "That's okay",
                "Thank you too");
    }

}
