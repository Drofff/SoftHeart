package com.softHeart.telegram;

import com.softHeart.db.*;
import com.softHeart.enums.TextType;
import com.softHeart.exception.IsItQuestionException;
import com.softHeart.exception.LearnRequestException;
import com.softHeart.preprocess.TextTypeClassifier;
import com.softHeart.process.AnswerGenerator;
import com.softHeart.process.QuestionHandler;
import com.softHeart.process.StatementHandler;
import com.softHeart.utils.QuestionsCache;
import com.softHeart.utils.RandomUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Properties;

public class SoftHeartBot extends TelegramLongPollingBot {

    private static final String PROPERTIES_PATH = "bot.properties";
    private static final String USERNAME_PROPERTY = "username";
    private static final String TOKEN_PROPERTY = "token";

    private final QuestionHandler questionHandler;
    private final StatementHandler statementHandler;

    private String username;
    private String token;

    public SoftHeartBot() {
        try {
            java.sql.Connection connection = Connection.getConnection();
            AnswersHolder answersHolder = new DbAnswerHolder(connection);
            QuestionsHolder questionsHolder = new DbQuestionHolder(connection);
            AnswerGenerator answerGenerator = new AnswerGenerator(questionsHolder);
            statementHandler = new StatementHandler(answerGenerator);
            questionHandler = new QuestionHandler(answersHolder);
            initBotProperties();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void initBotProperties() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH));
        username = properties.getProperty(USERNAME_PROPERTY);
        token = properties.getProperty(TOKEN_PROPERTY);
        if(Objects.isNull(username) || Objects.isNull(token)) {
            throw new FileNotFoundException("Properties are missing");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String receivedText = message.getText();
        String userId = message.getFrom().getId().toString();
        System.out.println("question: " + receivedText);

        RandomUtils.delayRandom();

        String questionFromCache = QuestionsCache.get(userId);
        if(Objects.nonNull(questionFromCache)) {
            QuestionsCache.remove(userId);
            send(questionHandler.saveAnswer(questionFromCache, receivedText), message.getChatId());
            return;
        }

        String answer = "";

        try {
            TextType receivedTextType = TextTypeClassifier.classify(receivedText);
            if(receivedTextType.equals(TextType.QUESTION)) {
                answer = questionHandler.answer(userId, receivedText);
            } else {
                answer = statementHandler.answer(userId, receivedText);
            }
        } catch (IsItQuestionException | LearnRequestException e) {
            answer = e.getMessage();
        }

        System.out.println("answer: " + answer);
        send(answer, message.getChatId());
    }

    private void send(String message, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch(TelegramApiException e) {
            e.printStackTrace();;
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
