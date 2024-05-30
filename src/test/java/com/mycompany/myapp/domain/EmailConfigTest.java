package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmailConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailConfig.class);
        EmailConfig emailConfig1 = getEmailConfigSample1();
        EmailConfig emailConfig2 = new EmailConfig();
        assertThat(emailConfig1).isNotEqualTo(emailConfig2);

        emailConfig2.setId(emailConfig1.getId());
        assertThat(emailConfig1).isEqualTo(emailConfig2);

        emailConfig2 = getEmailConfigSample2();
        assertThat(emailConfig1).isNotEqualTo(emailConfig2);
    }
}
