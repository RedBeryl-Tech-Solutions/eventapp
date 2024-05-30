package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmailConfig;
import com.mycompany.myapp.repository.EmailConfigRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EmailConfig}.
 */
@RestController
@RequestMapping("/api/email-configs")
@Transactional
public class EmailConfigResource {

    private final Logger log = LoggerFactory.getLogger(EmailConfigResource.class);

    private static final String ENTITY_NAME = "emailConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailConfigRepository emailConfigRepository;

    public EmailConfigResource(EmailConfigRepository emailConfigRepository) {
        this.emailConfigRepository = emailConfigRepository;
    }

    /**
     * {@code POST  /email-configs} : Create a new emailConfig.
     *
     * @param emailConfig the emailConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailConfig, or with status {@code 400 (Bad Request)} if the emailConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailConfig> createEmailConfig(@RequestBody EmailConfig emailConfig) throws URISyntaxException {
        log.debug("REST request to save EmailConfig : {}", emailConfig);
        if (emailConfig.getId() != null) {
            throw new BadRequestAlertException("A new emailConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailConfig = emailConfigRepository.save(emailConfig);
        return ResponseEntity.created(new URI("/api/email-configs/" + emailConfig.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, emailConfig.getId().toString()))
            .body(emailConfig);
    }

    /**
     * {@code PUT  /email-configs/:id} : Updates an existing emailConfig.
     *
     * @param id the id of the emailConfig to save.
     * @param emailConfig the emailConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailConfig,
     * or with status {@code 400 (Bad Request)} if the emailConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailConfig> updateEmailConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailConfig emailConfig
    ) throws URISyntaxException {
        log.debug("REST request to update EmailConfig : {}, {}", id, emailConfig);
        if (emailConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailConfig = emailConfigRepository.save(emailConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emailConfig.getId().toString()))
            .body(emailConfig);
    }

    /**
     * {@code PATCH  /email-configs/:id} : Partial updates given fields of an existing emailConfig, field will ignore if it is null
     *
     * @param id the id of the emailConfig to save.
     * @param emailConfig the emailConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailConfig,
     * or with status {@code 400 (Bad Request)} if the emailConfig is not valid,
     * or with status {@code 404 (Not Found)} if the emailConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailConfig> partialUpdateEmailConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailConfig emailConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailConfig partially : {}, {}", id, emailConfig);
        if (emailConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailConfig> result = emailConfigRepository
            .findById(emailConfig.getId())
            .map(existingEmailConfig -> {
                if (emailConfig.getEmailId() != null) {
                    existingEmailConfig.setEmailId(emailConfig.getEmailId());
                }
                if (emailConfig.getTokenString() != null) {
                    existingEmailConfig.setTokenString(emailConfig.getTokenString());
                }

                return existingEmailConfig;
            })
            .map(emailConfigRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emailConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /email-configs} : get all the emailConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailConfigs in body.
     */
    @GetMapping("")
    public List<EmailConfig> getAllEmailConfigs() {
        log.debug("REST request to get all EmailConfigs");
        return emailConfigRepository.findAll();
    }

    /**
     * {@code GET  /email-configs/:id} : get the "id" emailConfig.
     *
     * @param id the id of the emailConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailConfig> getEmailConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get EmailConfig : {}", id);
        Optional<EmailConfig> emailConfig = emailConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(emailConfig);
    }

    /**
     * {@code DELETE  /email-configs/:id} : delete the "id" emailConfig.
     *
     * @param id the id of the emailConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmailConfig : {}", id);
        emailConfigRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
