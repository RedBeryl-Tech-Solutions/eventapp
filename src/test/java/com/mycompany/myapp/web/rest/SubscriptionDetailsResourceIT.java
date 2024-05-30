package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SubscriptionDetailsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SubscriptionDetails;
import com.mycompany.myapp.repository.SubscriptionDetailsRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SubscriptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionDetailsResourceIT {

    private static final String DEFAULT_SUBSCRIPTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SUBSCRIPTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBSCRIPTION_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_SUBSCRIPTION_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBSCRIPTION_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SUBSCRIPTION_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBSCRIPTION_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADDITIONAL_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_COMMENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOTIFICATION_BEFORE_EXPIRY = 1;
    private static final Integer UPDATED_NOTIFICATION_BEFORE_EXPIRY = 2;

    private static final Boolean DEFAULT_NOTIFICATION_MUTE_FLAG = false;
    private static final Boolean UPDATED_NOTIFICATION_MUTE_FLAG = true;

    private static final String DEFAULT_NOTIFICATION_TO = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFICATION_CC = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_CC = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFICATION_BCC = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_BCC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscription-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionDetailsRepository subscriptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionDetailsMockMvc;

    private SubscriptionDetails subscriptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDetails createEntity(EntityManager em) {
        SubscriptionDetails subscriptionDetails = new SubscriptionDetails()
            .subscriptionName(DEFAULT_SUBSCRIPTION_NAME)
            .subscriptionAmount(DEFAULT_SUBSCRIPTION_AMOUNT)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .subscriptionStartDate(DEFAULT_SUBSCRIPTION_START_DATE)
            .subscriptionExpiryDate(DEFAULT_SUBSCRIPTION_EXPIRY_DATE)
            .additionalComments(DEFAULT_ADDITIONAL_COMMENTS)
            .notificationBeforeExpiry(DEFAULT_NOTIFICATION_BEFORE_EXPIRY)
            .notificationMuteFlag(DEFAULT_NOTIFICATION_MUTE_FLAG)
            .notificationTo(DEFAULT_NOTIFICATION_TO)
            .notificationCc(DEFAULT_NOTIFICATION_CC)
            .notificationBcc(DEFAULT_NOTIFICATION_BCC);
        return subscriptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDetails createUpdatedEntity(EntityManager em) {
        SubscriptionDetails subscriptionDetails = new SubscriptionDetails()
            .subscriptionName(UPDATED_SUBSCRIPTION_NAME)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionExpiryDate(UPDATED_SUBSCRIPTION_EXPIRY_DATE)
            .additionalComments(UPDATED_ADDITIONAL_COMMENTS)
            .notificationBeforeExpiry(UPDATED_NOTIFICATION_BEFORE_EXPIRY)
            .notificationMuteFlag(UPDATED_NOTIFICATION_MUTE_FLAG)
            .notificationTo(UPDATED_NOTIFICATION_TO)
            .notificationCc(UPDATED_NOTIFICATION_CC)
            .notificationBcc(UPDATED_NOTIFICATION_BCC);
        return subscriptionDetails;
    }

    @BeforeEach
    public void initTest() {
        subscriptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscriptionDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionDetails
        var returnedSubscriptionDetails = om.readValue(
            restSubscriptionDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionDetails.class
        );

        // Validate the SubscriptionDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            returnedSubscriptionDetails,
            getPersistedSubscriptionDetails(returnedSubscriptionDetails)
        );
    }

    @Test
    @Transactional
    void createSubscriptionDetailsWithExistingId() throws Exception {
        // Create the SubscriptionDetails with an existing ID
        subscriptionDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubscriptionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setSubscriptionName(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setSubscriptionAmount(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setTaxAmount(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setTotalAmount(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setSubscriptionStartDate(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setSubscriptionExpiryDate(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdditionalCommentsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setAdditionalComments(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationBeforeExpiryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setNotificationBeforeExpiry(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationMuteFlagIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setNotificationMuteFlag(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationToIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setNotificationTo(null);

        // Create the SubscriptionDetails, which fails.

        restSubscriptionDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionDetails() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        // Get all the subscriptionDetailsList
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionName").value(hasItem(DEFAULT_SUBSCRIPTION_NAME)))
            .andExpect(jsonPath("$.[*].subscriptionAmount").value(hasItem(sameNumber(DEFAULT_SUBSCRIPTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].subscriptionStartDate").value(hasItem(DEFAULT_SUBSCRIPTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionExpiryDate").value(hasItem(DEFAULT_SUBSCRIPTION_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalComments").value(hasItem(DEFAULT_ADDITIONAL_COMMENTS)))
            .andExpect(jsonPath("$.[*].notificationBeforeExpiry").value(hasItem(DEFAULT_NOTIFICATION_BEFORE_EXPIRY)))
            .andExpect(jsonPath("$.[*].notificationMuteFlag").value(hasItem(DEFAULT_NOTIFICATION_MUTE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].notificationTo").value(hasItem(DEFAULT_NOTIFICATION_TO)))
            .andExpect(jsonPath("$.[*].notificationCc").value(hasItem(DEFAULT_NOTIFICATION_CC)))
            .andExpect(jsonPath("$.[*].notificationBcc").value(hasItem(DEFAULT_NOTIFICATION_BCC)));
    }

    @Test
    @Transactional
    void getSubscriptionDetails() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.subscriptionName").value(DEFAULT_SUBSCRIPTION_NAME))
            .andExpect(jsonPath("$.subscriptionAmount").value(sameNumber(DEFAULT_SUBSCRIPTION_AMOUNT)))
            .andExpect(jsonPath("$.taxAmount").value(sameNumber(DEFAULT_TAX_AMOUNT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.subscriptionStartDate").value(DEFAULT_SUBSCRIPTION_START_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionExpiryDate").value(DEFAULT_SUBSCRIPTION_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.additionalComments").value(DEFAULT_ADDITIONAL_COMMENTS))
            .andExpect(jsonPath("$.notificationBeforeExpiry").value(DEFAULT_NOTIFICATION_BEFORE_EXPIRY))
            .andExpect(jsonPath("$.notificationMuteFlag").value(DEFAULT_NOTIFICATION_MUTE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.notificationTo").value(DEFAULT_NOTIFICATION_TO))
            .andExpect(jsonPath("$.notificationCc").value(DEFAULT_NOTIFICATION_CC))
            .andExpect(jsonPath("$.notificationBcc").value(DEFAULT_NOTIFICATION_BCC));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionDetails() throws Exception {
        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionDetails() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails
        SubscriptionDetails updatedSubscriptionDetails = subscriptionDetailsRepository.findById(subscriptionDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionDetails are not directly saved in db
        em.detach(updatedSubscriptionDetails);
        updatedSubscriptionDetails
            .subscriptionName(UPDATED_SUBSCRIPTION_NAME)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionExpiryDate(UPDATED_SUBSCRIPTION_EXPIRY_DATE)
            .additionalComments(UPDATED_ADDITIONAL_COMMENTS)
            .notificationBeforeExpiry(UPDATED_NOTIFICATION_BEFORE_EXPIRY)
            .notificationMuteFlag(UPDATED_NOTIFICATION_MUTE_FLAG)
            .notificationTo(UPDATED_NOTIFICATION_TO)
            .notificationCc(UPDATED_NOTIFICATION_CC)
            .notificationBcc(UPDATED_NOTIFICATION_BCC);

        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubscriptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubscriptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionDetailsToMatchAllProperties(updatedSubscriptionDetails);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetails partialUpdatedSubscriptionDetails = new SubscriptionDetails();
        partialUpdatedSubscriptionDetails.setId(subscriptionDetails.getId());

        partialUpdatedSubscriptionDetails
            .subscriptionName(UPDATED_SUBSCRIPTION_NAME)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .additionalComments(UPDATED_ADDITIONAL_COMMENTS)
            .notificationTo(UPDATED_NOTIFICATION_TO)
            .notificationCc(UPDATED_NOTIFICATION_CC)
            .notificationBcc(UPDATED_NOTIFICATION_BCC);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionDetails, subscriptionDetails),
            getPersistedSubscriptionDetails(subscriptionDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetails partialUpdatedSubscriptionDetails = new SubscriptionDetails();
        partialUpdatedSubscriptionDetails.setId(subscriptionDetails.getId());

        partialUpdatedSubscriptionDetails
            .subscriptionName(UPDATED_SUBSCRIPTION_NAME)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionExpiryDate(UPDATED_SUBSCRIPTION_EXPIRY_DATE)
            .additionalComments(UPDATED_ADDITIONAL_COMMENTS)
            .notificationBeforeExpiry(UPDATED_NOTIFICATION_BEFORE_EXPIRY)
            .notificationMuteFlag(UPDATED_NOTIFICATION_MUTE_FLAG)
            .notificationTo(UPDATED_NOTIFICATION_TO)
            .notificationCc(UPDATED_NOTIFICATION_CC)
            .notificationBcc(UPDATED_NOTIFICATION_BCC);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            partialUpdatedSubscriptionDetails,
            getPersistedSubscriptionDetails(partialUpdatedSubscriptionDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscriptionDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionDetails() throws Exception {
        // Initialize the database
        subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionDetailsRepository.count();
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

    protected SubscriptionDetails getPersistedSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
        return subscriptionDetailsRepository.findById(subscriptionDetails.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionDetailsToMatchAllProperties(SubscriptionDetails expectedSubscriptionDetails) {
        assertSubscriptionDetailsAllPropertiesEquals(
            expectedSubscriptionDetails,
            getPersistedSubscriptionDetails(expectedSubscriptionDetails)
        );
    }

    protected void assertPersistedSubscriptionDetailsToMatchUpdatableProperties(SubscriptionDetails expectedSubscriptionDetails) {
        assertSubscriptionDetailsAllUpdatablePropertiesEquals(
            expectedSubscriptionDetails,
            getPersistedSubscriptionDetails(expectedSubscriptionDetails)
        );
    }
}
