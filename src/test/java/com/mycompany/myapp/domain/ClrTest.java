package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clr.class);
        Clr clr1 = new Clr();
        clr1.setId(1L);
        Clr clr2 = new Clr();
        clr2.setId(clr1.getId());
        assertThat(clr1).isEqualTo(clr2);
        clr2.setId(2L);
        assertThat(clr1).isNotEqualTo(clr2);
        clr1.setId(null);
        assertThat(clr1).isNotEqualTo(clr2);
    }
}
