package com.softHeart.process;

import com.softHeart.db.AnswersHolder;
import com.softHeart.exception.AnswerSaveException;
import com.softHeart.exception.LearnRequestException;
import com.softHeart.preprocess.TextAnalyzer;
import com.softHeart.utils.PreProcessUtils;
import com.softHeart.utils.QuestionsCache;
import com.softHeart.utils.RandomUtils;

import java.util.*;

public class QuestionHandler implements RequestHandler {

    private AnswersHolder answersHolder;

    public QuestionHandler(AnswersHolder answersHolder) {
        this.answersHolder = answersHolder;
    }

    @Override
    public String answer(String userId, String question) {
        System.out.println("user: " + userId);
        System.out.println("question: " + question);
        String preProcessedQuestion = preProcessQuestion(question);
        System.out.println("preprocessed question: " + preProcessedQuestion);
        Optional<String> providedAnswerOptional = answersHolder.findByQuestion(preProcessedQuestion);
        if(providedAnswerOptional.isPresent()) {
            return providedAnswerOptional.get();
        }
        Optional<String> themeByAnswer = answerByTheme(preProcessedQuestion);
        if(themeByAnswer.isPresent()) {
            return themeByAnswer.get();
        }
        QuestionsCache.save(userId, preProcessedQuestion);
        throw new LearnRequestException(getRandomSaveRequestMessage());
    }

    public String saveAnswer(String question, String answer) {
        if(Objects.isNull(answer) || answer.isEmpty()) {
            throw new AnswerSaveException("Provided answer is empty");
        }
        answersHolder.save(question, answer);
        return getRandomSaveMessage();
    }

    private Optional<String> answerByTheme(String question) {
        System.out.println("answerByTheme(question=" + question + ")");
        Set<String> allWordsFromQuestion = TextAnalyzer.extractAllWords(question);
        Set<String> allQuestionKeyWords = TextAnalyzer.extractAllQuestionKeyWords(question);
        allQuestionKeyWords.forEach(allWordsFromQuestion::remove);
        return allWordsFromQuestion.stream()
                .map(x -> answersHolder.findByTheme(x))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    private String preProcessQuestion(String question) {
        String lowerCaseQuestion = question.toLowerCase();
        return PreProcessUtils.removeSpecialSymbols(lowerCaseQuestion);
    }

    private String getRandomSaveRequestMessage() {
        List<String> saveRequestMessages = Arrays.asList("Ohh.. How would you answer for this question?",
                "I don't know what to say. Can you, please, tell me an right answer?",
                "Something new! Learn me how to answer on it!");
        return RandomUtils.getRandom(saveRequestMessages);
    }

    private String getRandomSaveMessage() {
        List<String> saveMessages = Arrays.asList("I'll remember it! Thanks!",
                "Woow! With you I can learn a lot!", "Thanks! Now I know more!");
        return RandomUtils.getRandom(saveMessages);
    }

}
