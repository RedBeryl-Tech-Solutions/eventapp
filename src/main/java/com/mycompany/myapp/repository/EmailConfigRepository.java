package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmailConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {}
