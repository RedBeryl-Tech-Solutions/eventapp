package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EmailConfigAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmailConfig;
import com.mycompany.myapp.repository.EmailConfigRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmailConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailConfigResourceIT {

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_STRING = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_STRING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailConfigMockMvc;

    private EmailConfig emailConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailConfig createEntity(EntityManager em) {
        EmailConfig emailConfig = new EmailConfig().emailId(DEFAULT_EMAIL_ID).tokenString(DEFAULT_TOKEN_STRING);
        return emailConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailConfig createUpdatedEntity(EntityManager em) {
        EmailConfig emailConfig = new EmailConfig().emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);
        return emailConfig;
    }

    @BeforeEach
    public void initTest() {
        emailConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createEmailConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailConfig
        var returnedEmailConfig = om.readValue(
            restEmailConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailConfig)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailConfig.class
        );

        // Validate the EmailConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmailConfigUpdatableFieldsEquals(returnedEmailConfig, getPersistedEmailConfig(returnedEmailConfig));
    }

    @Test
    @Transactional
    void createEmailConfigWithExistingId() throws Exception {
        // Create the EmailConfig with an existing ID
        emailConfig.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailConfig)))
            .andExpect(status().isBadRequest());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmailConfigs() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        // Get all the emailConfigList
        restEmailConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].tokenString").value(hasItem(DEFAULT_TOKEN_STRING)));
    }

    @Test
    @Transactional
    void getEmailConfig() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        // Get the emailConfig
        restEmailConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, emailConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailConfig.getId().intValue()))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.tokenString").value(DEFAULT_TOKEN_STRING));
    }

    @Test
    @Transactional
    void getNonExistingEmailConfig() throws Exception {
        // Get the emailConfig
        restEmailConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailConfig() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig
        EmailConfig updatedEmailConfig = emailConfigRepository.findById(emailConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmailConfig are not directly saved in db
        em.detach(updatedEmailConfig);
        updatedEmailConfig.emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);

        restEmailConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmailConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmailConfig))
            )
            .andExpect(status().isOk());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailConfigToMatchAllProperties(updatedEmailConfig);
    }

    @Test
    @Transactional
    void putNonExistingEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailConfigWithPatch() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig using partial update
        EmailConfig partialUpdatedEmailConfig = new EmailConfig();
        partialUpdatedEmailConfig.setId(emailConfig.getId());

        restEmailConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailConfig))
            )
            .andExpect(status().isOk());

        // Validate the EmailConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailConfig, emailConfig),
            getPersistedEmailConfig(emailConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailConfigWithPatch() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig using partial update
        EmailConfig partialUpdatedEmailConfig = new EmailConfig();
        partialUpdatedEmailConfig.setId(emailConfig.getId());

        partialUpdatedEmailConfig.emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);

        restEmailConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailConfig))
            )
            .andExpect(status().isOk());

        // Validate the EmailConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailConfigUpdatableFieldsEquals(partialUpdatedEmailConfig, getPersistedEmailConfig(partialUpdatedEmailConfig));
    }

    @Test
    @Transactional
    void patchNonExistingEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailConfig() throws Exception {
        // Initialize the database
        emailConfigRepository.saveAndFlush(emailConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailConfig
        restEmailConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailConfigRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EmailConfig getPersistedEmailConfig(EmailConfig emailConfig) {
        return emailConfigRepository.findById(emailConfig.getId()).orElseThrow();
    }

    protected void assertPersistedEmailConfigToMatchAllProperties(EmailConfig expectedEmailConfig) {
        assertEmailConfigAllPropertiesEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
    }

    protected void assertPersistedEmailConfigToMatchUpdatableProperties(EmailConfig expectedEmailConfig) {
        assertEmailConfigAllUpdatablePropertiesEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
    }
}
