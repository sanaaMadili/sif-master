package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reception;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reception entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {}
