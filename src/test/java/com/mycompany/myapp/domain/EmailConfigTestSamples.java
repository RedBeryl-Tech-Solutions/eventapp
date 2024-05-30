package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmailConfig getEmailConfigSample1() {
        return new EmailConfig().id(1L).emailId("emailId1").tokenString("tokenString1");
    }

    public static EmailConfig getEmailConfigSample2() {
        return new EmailConfig().id(2L).emailId("emailId2").tokenString("tokenString2");
    }

    public static EmailConfig getEmailConfigRandomSampleGenerator() {
        return new EmailConfig()
            .id(longCount.incrementAndGet())
            .emailId(UUID.randomUUID().toString())
            .tokenString(UUID.randomUUID().toString());
    }
}
