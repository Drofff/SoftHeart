package com.softHeart.db;

import com.softHeart.utils.RandomUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbQuestionHolder implements QuestionsHolder {

    private final Connection connection;

    private static final String QUESTIONS_TABLE = "questions";

    public DbQuestionHolder(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getRandomQuestion() {
        String query = "select * from " + QUESTIONS_TABLE;
        return findQuestionByQuery(query);
    }

    @Override
    public String getRandomQuestionWithSubjectOnly() {
        String query = "select * from " + QUESTIONS_TABLE + " where text not like '%^O^%'";
        return findQuestionByQuery(query);
    }

    private String findQuestionByQuery(String query) {
        List<String> question = new ArrayList<>();
        try {
            ResultSet resultSet = connection.prepareStatement(query)
                    .executeQuery();
            while (resultSet.next()) {
                question.add(resultSet.getString("text"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return RandomUtils.getRandom(question);
    }
}
