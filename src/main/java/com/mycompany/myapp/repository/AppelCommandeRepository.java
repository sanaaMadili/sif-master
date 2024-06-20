package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AppelCommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppelCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppelCommandeRepository extends JpaRepository<AppelCommande, Long> {}
