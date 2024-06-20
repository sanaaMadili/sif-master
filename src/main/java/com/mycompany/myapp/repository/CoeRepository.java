package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Coe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoeRepository extends JpaRepository<Coe, Long> {}
