package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Magasin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Magasin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagasinRepository extends JpaRepository<Magasin, Long> {}
