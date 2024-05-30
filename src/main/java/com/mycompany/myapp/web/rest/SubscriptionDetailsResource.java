package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SubscriptionDetails;
import com.mycompany.myapp.repository.SubscriptionDetailsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SubscriptionDetails}.
 */
@RestController
@RequestMapping("/api/subscription-details")
@Transactional
public class SubscriptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionDetailsResource.class);

    private static final String ENTITY_NAME = "subscriptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionDetailsRepository subscriptionDetailsRepository;

    public SubscriptionDetailsResource(SubscriptionDetailsRepository subscriptionDetailsRepository) {
        this.subscriptionDetailsRepository = subscriptionDetailsRepository;
    }

    /**
     * {@code POST  /subscription-details} : Create a new subscriptionDetails.
     *
     * @param subscriptionDetails the subscriptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionDetails, or with status {@code 400 (Bad Request)} if the subscriptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionDetails> createSubscriptionDetails(@Valid @RequestBody SubscriptionDetails subscriptionDetails)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionDetails : {}", subscriptionDetails);
        if (subscriptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionDetails = subscriptionDetailsRepository.save(subscriptionDetails);
        return ResponseEntity.created(new URI("/api/subscription-details/" + subscriptionDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subscriptionDetails.getId().toString()))
            .body(subscriptionDetails);
    }

    /**
     * {@code PUT  /subscription-details/:id} : Updates an existing subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to save.
     * @param subscriptionDetails the subscriptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetails,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDetails> updateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionDetails subscriptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionDetails : {}, {}", id, subscriptionDetails);
        if (subscriptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionDetails = subscriptionDetailsRepository.save(subscriptionDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionDetails.getId().toString()))
            .body(subscriptionDetails);
    }

    /**
     * {@code PATCH  /subscription-details/:id} : Partial updates given fields of an existing subscriptionDetails, field will ignore if it is null
     *
     * @param id the id of the subscriptionDetails to save.
     * @param subscriptionDetails the subscriptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetails,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionDetails> partialUpdateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionDetails subscriptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionDetails partially : {}, {}", id, subscriptionDetails);
        if (subscriptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionDetails> result = subscriptionDetailsRepository
            .findById(subscriptionDetails.getId())
            .map(existingSubscriptionDetails -> {
                if (subscriptionDetails.getSubscriptionName() != null) {
                    existingSubscriptionDetails.setSubscriptionName(subscriptionDetails.getSubscriptionName());
                }
                if (subscriptionDetails.getSubscriptionAmount() != null) {
                    existingSubscriptionDetails.setSubscriptionAmount(subscriptionDetails.getSubscriptionAmount());
                }
                if (subscriptionDetails.getTaxAmount() != null) {
                    existingSubscriptionDetails.setTaxAmount(subscriptionDetails.getTaxAmount());
                }
                if (subscriptionDetails.getTotalAmount() != null) {
                    existingSubscriptionDetails.setTotalAmount(subscriptionDetails.getTotalAmount());
                }
                if (subscriptionDetails.getSubscriptionStartDate() != null) {
                    existingSubscriptionDetails.setSubscriptionStartDate(subscriptionDetails.getSubscriptionStartDate());
                }
                if (subscriptionDetails.getSubscriptionExpiryDate() != null) {
                    existingSubscriptionDetails.setSubscriptionExpiryDate(subscriptionDetails.getSubscriptionExpiryDate());
                }
                if (subscriptionDetails.getAdditionalComments() != null) {
                    existingSubscriptionDetails.setAdditionalComments(subscriptionDetails.getAdditionalComments());
                }
                if (subscriptionDetails.getNotificationBeforeExpiry() != null) {
                    existingSubscriptionDetails.setNotificationBeforeExpiry(subscriptionDetails.getNotificationBeforeExpiry());
                }
                if (subscriptionDetails.getNotificationMuteFlag() != null) {
                    existingSubscriptionDetails.setNotificationMuteFlag(subscriptionDetails.getNotificationMuteFlag());
                }
                if (subscriptionDetails.getNotificationTo() != null) {
                    existingSubscriptionDetails.setNotificationTo(subscriptionDetails.getNotificationTo());
                }
                if (subscriptionDetails.getNotificationCc() != null) {
                    existingSubscriptionDetails.setNotificationCc(subscriptionDetails.getNotificationCc());
                }
                if (subscriptionDetails.getNotificationBcc() != null) {
                    existingSubscriptionDetails.setNotificationBcc(subscriptionDetails.getNotificationBcc());
                }

                return existingSubscriptionDetails;
            })
            .map(subscriptionDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-details} : get all the subscriptionDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionDetails in body.
     */
    @GetMapping("")
    public List<SubscriptionDetails> getAllSubscriptionDetails() {
        log.debug("REST request to get all SubscriptionDetails");
        System.out.println(subscriptionDetailsRepository.findAll1());
        return subscriptionDetailsRepository.findAll1();
    }

    /**
     * {@code GET  /subscription-details/:id} : get the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDetails> getSubscriptionDetails(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscriptionDetails : {}", id);
        Optional<SubscriptionDetails> subscriptionDetails = subscriptionDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subscriptionDetails);
    }

    /**
     * {@code DELETE  /subscription-details/:id} : delete the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionDetails(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscriptionDetails : {}", id);
        subscriptionDetailsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
