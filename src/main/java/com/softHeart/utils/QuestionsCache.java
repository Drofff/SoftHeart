package com.softHeart.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class QuestionsCache {

    private static Cache<String, String> questionsCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.of(30, ChronoUnit.MINUTES))
            .build();

    private QuestionsCache() {}

    public static void save(String userId, String question) {
        questionsCache.put(userId, question);
    }

    public static String get(String userId) {
        return questionsCache.getIfPresent(userId);
    }

    public static void remove(String userId) {
        questionsCache.invalidate(userId);
    }

}
