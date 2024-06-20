package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Magasinier.
 */
@Entity
@Table(name = "magasinier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Magasinier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "profession")
    private String profession;

    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ExtraUser extraUser;

    @JsonIgnoreProperties(value = { "magasinier", "etatStocks" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Magasin magasin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Magasinier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfession() {
        return this.profession;
    }

    public Magasinier profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ExtraUser getExtraUser() {
        return this.extraUser;
    }

    public void setExtraUser(ExtraUser extraUser) {
        this.extraUser = extraUser;
    }

    public Magasinier extraUser(ExtraUser extraUser) {
        this.setExtraUser(extraUser);
        return this;
    }

    public Magasin getMagasin() {
        return this.magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Magasinier magasin(Magasin magasin) {
        this.setMagasin(magasin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Magasinier)) {
            return false;
        }
        return id != null && id.equals(((Magasinier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Magasinier{" +
            "id=" + getId() +
            ", profession='" + getProfession() + "'" +
            "}";
    }
}
