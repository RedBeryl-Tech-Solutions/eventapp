package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoryTestSamples.*;
import static com.mycompany.myapp.domain.SubscriptionDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void subscriptionDetailsTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        SubscriptionDetails subscriptionDetailsBack = getSubscriptionDetailsRandomSampleGenerator();

        category.addSubscriptionDetails(subscriptionDetailsBack);
        assertThat(category.getSubscriptionDetails()).containsOnly(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getCategory()).isEqualTo(category);

        category.removeSubscriptionDetails(subscriptionDetailsBack);
        assertThat(category.getSubscriptionDetails()).doesNotContain(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getCategory()).isNull();

        category.subscriptionDetails(new HashSet<>(Set.of(subscriptionDetailsBack)));
        assertThat(category.getSubscriptionDetails()).containsOnly(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getCategory()).isEqualTo(category);

        category.setSubscriptionDetails(new HashSet<>());
        assertThat(category.getSubscriptionDetails()).doesNotContain(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getCategory()).isNull();
    }
}
