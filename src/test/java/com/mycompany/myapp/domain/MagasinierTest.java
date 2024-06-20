package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagasinierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magasinier.class);
        Magasinier magasinier1 = new Magasinier();
        magasinier1.setId(1L);
        Magasinier magasinier2 = new Magasinier();
        magasinier2.setId(magasinier1.getId());
        assertThat(magasinier1).isEqualTo(magasinier2);
        magasinier2.setId(2L);
        assertThat(magasinier1).isNotEqualTo(magasinier2);
        magasinier1.setId(null);
        assertThat(magasinier1).isNotEqualTo(magasinier2);
    }
}
