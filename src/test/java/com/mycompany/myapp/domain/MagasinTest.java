package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagasinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magasin.class);
        Magasin magasin1 = new Magasin();
        magasin1.setId(1L);
        Magasin magasin2 = new Magasin();
        magasin2.setId(magasin1.getId());
        assertThat(magasin1).isEqualTo(magasin2);
        magasin2.setId(2L);
        assertThat(magasin1).isNotEqualTo(magasin2);
        magasin1.setId(null);
        assertThat(magasin1).isNotEqualTo(magasin2);
    }
}
