package com.softHeart.db;

import java.util.Optional;

public interface AnswersHolder {
    Optional<String> findByQuestion(String question);
    void save(String question, String answer);
    Optional<String> findByTheme(String theme);
}
