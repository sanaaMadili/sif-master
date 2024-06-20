package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppelCommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppelCommande.class);
        AppelCommande appelCommande1 = new AppelCommande();
        appelCommande1.setId(1L);
        AppelCommande appelCommande2 = new AppelCommande();
        appelCommande2.setId(appelCommande1.getId());
        assertThat(appelCommande1).isEqualTo(appelCommande2);
        appelCommande2.setId(2L);
        assertThat(appelCommande1).isNotEqualTo(appelCommande2);
        appelCommande1.setId(null);
        assertThat(appelCommande1).isNotEqualTo(appelCommande2);
    }
}
