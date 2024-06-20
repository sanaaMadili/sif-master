package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coe.class);
        Coe coe1 = new Coe();
        coe1.setId(1L);
        Coe coe2 = new Coe();
        coe2.setId(coe1.getId());
        assertThat(coe1).isEqualTo(coe2);
        coe2.setId(2L);
        assertThat(coe1).isNotEqualTo(coe2);
        coe1.setId(null);
        assertThat(coe1).isNotEqualTo(coe2);
    }
}
