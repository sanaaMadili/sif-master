package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Magasin.
 */
@Entity
@Table(name = "magasin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Magasin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_magasin")
    private Integer codeMagasin;

    @Column(name = "pays")
    private String pays;

    @Column(name = "address")
    private String address;

    @JsonIgnoreProperties(value = { "extraUser", "magasin" }, allowSetters = true)
    @OneToOne(mappedBy = "magasin")
    private Magasinier magasinier;

    @OneToMany(mappedBy = "magasin")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "article", "magasin" }, allowSetters = true)
    private Set<EtatStock> etatStocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Magasin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodeMagasin() {
        return this.codeMagasin;
    }

    public Magasin codeMagasin(Integer codeMagasin) {
        this.setCodeMagasin(codeMagasin);
        return this;
    }

    public void setCodeMagasin(Integer codeMagasin) {
        this.codeMagasin = codeMagasin;
    }

    public String getPays() {
        return this.pays;
    }

    public Magasin pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getAddress() {
        return this.address;
    }

    public Magasin address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Magasinier getMagasinier() {
        return this.magasinier;
    }

    public void setMagasinier(Magasinier magasinier) {
        if (this.magasinier != null) {
            this.magasinier.setMagasin(null);
        }
        if (magasinier != null) {
            magasinier.setMagasin(this);
        }
        this.magasinier = magasinier;
    }

    public Magasin magasinier(Magasinier magasinier) {
        this.setMagasinier(magasinier);
        return this;
    }

    public Set<EtatStock> getEtatStocks() {
        return this.etatStocks;
    }

    public void setEtatStocks(Set<EtatStock> etatStocks) {
        if (this.etatStocks != null) {
            this.etatStocks.forEach(i -> i.setMagasin(null));
        }
        if (etatStocks != null) {
            etatStocks.forEach(i -> i.setMagasin(this));
        }
        this.etatStocks = etatStocks;
    }

    public Magasin etatStocks(Set<EtatStock> etatStocks) {
        this.setEtatStocks(etatStocks);
        return this;
    }

    public Magasin addEtatStock(EtatStock etatStock) {
        this.etatStocks.add(etatStock);
        etatStock.setMagasin(this);
        return this;
    }

    public Magasin removeEtatStock(EtatStock etatStock) {
        this.etatStocks.remove(etatStock);
        etatStock.setMagasin(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Magasin)) {
            return false;
        }
        return id != null && id.equals(((Magasin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Magasin{" +
            "id=" + getId() +
            ", codeMagasin=" + getCodeMagasin() +
            ", pays='" + getPays() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
