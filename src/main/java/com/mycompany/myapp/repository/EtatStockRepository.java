package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EtatStock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EtatStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtatStockRepository extends JpaRepository<EtatStock, Long> {}
