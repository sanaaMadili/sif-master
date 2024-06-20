package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Clr;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Clr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClrRepository extends JpaRepository<Clr, Long> {}
