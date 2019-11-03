package com.softHeart.db;

import com.softHeart.utils.RandomUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DbAnswerHolder implements AnswersHolder {

    private static final String BASE_ANSWERS_TABLE = "base_answers";
    private static final String LEARNED_ANSWERS_TABLE = "learned_answers";
    private static final String THEME_BASED_ANSWERS_TABLE = "theme_based_answers";

    private final java.sql.Connection connection;

    public DbAnswerHolder(java.sql.Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<String> findByQuestion(String question) {
        String baseAnswer = getAnswerByQuestion(question, BASE_ANSWERS_TABLE);
        if(Objects.nonNull(baseAnswer)) {
            return Optional.of(baseAnswer);
        }
        String learnedAnswer = getAnswerByQuestion(question, LEARNED_ANSWERS_TABLE);
        return Optional.ofNullable(learnedAnswer);
    }

    private String getAnswerByQuestion(String question, String table) {
        String query = "select t.answer from " + table + " as t where t.question = ?";
        List<String> answers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, question);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                answers.add(resultSet.getString("answer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return RandomUtils.getRandom(answers);
    }

    @Override
    public void save(String question, String answer) {
        String query = "insert into " + LEARNED_ANSWERS_TABLE + " (question, answer) values (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<String> findByTheme(String theme) {
        String query = "select t.answer from " + THEME_BASED_ANSWERS_TABLE + " t where t.theme_name = ?";
        List<String> answers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, theme);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                answers.add(resultSet.getString("answer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String randomAnswer = RandomUtils.getRandom(answers);
        return Optional.ofNullable(randomAnswer);
    }

}
