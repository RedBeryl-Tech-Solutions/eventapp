package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoryTestSamples.*;
import static com.mycompany.myapp.domain.SubscriptionDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDetails.class);
        SubscriptionDetails subscriptionDetails1 = getSubscriptionDetailsSample1();
        SubscriptionDetails subscriptionDetails2 = new SubscriptionDetails();
        assertThat(subscriptionDetails1).isNotEqualTo(subscriptionDetails2);

        subscriptionDetails2.setId(subscriptionDetails1.getId());
        assertThat(subscriptionDetails1).isEqualTo(subscriptionDetails2);

        subscriptionDetails2 = getSubscriptionDetailsSample2();
        assertThat(subscriptionDetails1).isNotEqualTo(subscriptionDetails2);
    }

    @Test
    void categoryTest() throws Exception {
        SubscriptionDetails subscriptionDetails = getSubscriptionDetailsRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        subscriptionDetails.setCategory(categoryBack);
        assertThat(subscriptionDetails.getCategory()).isEqualTo(categoryBack);

        subscriptionDetails.category(null);
        assertThat(subscriptionDetails.getCategory()).isNull();
    }
}
