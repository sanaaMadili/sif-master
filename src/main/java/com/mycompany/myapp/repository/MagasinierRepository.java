package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Magasinier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Magasinier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagasinierRepository extends JpaRepository<Magasinier, Long> {}
