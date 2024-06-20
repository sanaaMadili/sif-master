package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LigneCommande.
 */
@Entity
@Table(name = "ligne_commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qte")
    private Integer qte;

    @ManyToOne
    @JsonIgnoreProperties(value = { "etatStocks", "ligneCommandes" }, allowSetters = true)
    private Article article;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reception", "client", "ligneCommandes" }, allowSetters = true)
    private AppelCommande appelCommande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LigneCommande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQte() {
        return this.qte;
    }

    public LigneCommande qte(Integer qte) {
        this.setQte(qte);
        return this;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public LigneCommande article(Article article) {
        this.setArticle(article);
        return this;
    }

    public AppelCommande getAppelCommande() {
        return this.appelCommande;
    }

    public void setAppelCommande(AppelCommande appelCommande) {
        this.appelCommande = appelCommande;
    }

    public LigneCommande appelCommande(AppelCommande appelCommande) {
        this.setAppelCommande(appelCommande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommande)) {
            return false;
        }
        return id != null && id.equals(((LigneCommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommande{" +
            "id=" + getId() +
            ", qte=" + getQte() +
            "}";
    }
}
