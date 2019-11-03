package com.softHeart.utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static final Integer MAX_DELAY_IN_SECONDS = 10;
    private static final Integer MILLIS_IN_SECOND = 1000;

    private RandomUtils() {}

    public static <T> T getRandom(List<T> listT) {
        if(Objects.isNull(listT) || listT.isEmpty()) {
            return null;
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(listT.size());
        return listT.get(randomIndex);
    }

    public static void delayRandom() {
        try {
            Integer randomDelay = ThreadLocalRandom.current().nextInt(MAX_DELAY_IN_SECONDS + 1);
            Thread.sleep(randomDelay * MILLIS_IN_SECOND);
        } catch (InterruptedException e) {
            System.out.println("Random delay was interrupted");
            System.out.println(e.getMessage());
        }
    }

}
