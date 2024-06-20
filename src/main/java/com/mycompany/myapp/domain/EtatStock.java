package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EtatStock.
 */
@Entity
@Table(name = "etat_stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtatStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qte")
    private Integer qte;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JsonIgnoreProperties(value = { "etatStocks", "ligneCommandes" }, allowSetters = true)
    private Article article;

    @ManyToOne
    @JsonIgnoreProperties(value = { "magasinier", "etatStocks" }, allowSetters = true)
    private Magasin magasin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EtatStock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQte() {
        return this.qte;
    }

    public EtatStock qte(Integer qte) {
        this.setQte(qte);
        return this;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    public String getLocation() {
        return this.location;
    }

    public EtatStock location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public EtatStock article(Article article) {
        this.setArticle(article);
        return this;
    }

    public Magasin getMagasin() {
        return this.magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public EtatStock magasin(Magasin magasin) {
        this.setMagasin(magasin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtatStock)) {
            return false;
        }
        return id != null && id.equals(((EtatStock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtatStock{" +
            "id=" + getId() +
            ", qte=" + getQte() +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
