package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtatStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtatStock.class);
        EtatStock etatStock1 = new EtatStock();
        etatStock1.setId(1L);
        EtatStock etatStock2 = new EtatStock();
        etatStock2.setId(etatStock1.getId());
        assertThat(etatStock1).isEqualTo(etatStock2);
        etatStock2.setId(2L);
        assertThat(etatStock1).isNotEqualTo(etatStock2);
        etatStock1.setId(null);
        assertThat(etatStock1).isNotEqualTo(etatStock2);
    }
}
