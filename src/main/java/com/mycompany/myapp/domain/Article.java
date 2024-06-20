package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cai")
    private Integer cai;

    @Column(name = "ref_pneu")
    private Double refPneu;

    @Column(name = "type_pneu")
    private String typePneu;

    @Column(name = "valeur")
    private Double valeur;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "devise")
    private String devise;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "article", "magasin" }, allowSetters = true)
    private Set<EtatStock> etatStocks = new HashSet<>();

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "article", "appelCommande" }, allowSetters = true)
    private Set<LigneCommande> ligneCommandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCai() {
        return this.cai;
    }

    public Article cai(Integer cai) {
        this.setCai(cai);
        return this;
    }

    public void setCai(Integer cai) {
        this.cai = cai;
    }

    public Double getRefPneu() {
        return this.refPneu;
    }

    public Article refPneu(Double refPneu) {
        this.setRefPneu(refPneu);
        return this;
    }

    public void setRefPneu(Double refPneu) {
        this.refPneu = refPneu;
    }

    public String getTypePneu() {
        return this.typePneu;
    }

    public Article typePneu(String typePneu) {
        this.setTypePneu(typePneu);
        return this;
    }

    public void setTypePneu(String typePneu) {
        this.typePneu = typePneu;
    }

    public Double getValeur() {
        return this.valeur;
    }

    public Article valeur(Double valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Article image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Article imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDevise() {
        return this.devise;
    }

    public Article devise(String devise) {
        this.setDevise(devise);
        return this;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Set<EtatStock> getEtatStocks() {
        return this.etatStocks;
    }

    public void setEtatStocks(Set<EtatStock> etatStocks) {
        if (this.etatStocks != null) {
            this.etatStocks.forEach(i -> i.setArticle(null));
        }
        if (etatStocks != null) {
            etatStocks.forEach(i -> i.setArticle(this));
        }
        this.etatStocks = etatStocks;
    }

    public Article etatStocks(Set<EtatStock> etatStocks) {
        this.setEtatStocks(etatStocks);
        return this;
    }

    public Article addEtatStock(EtatStock etatStock) {
        this.etatStocks.add(etatStock);
        etatStock.setArticle(this);
        return this;
    }

    public Article removeEtatStock(EtatStock etatStock) {
        this.etatStocks.remove(etatStock);
        etatStock.setArticle(null);
        return this;
    }

    public Set<LigneCommande> getLigneCommandes() {
        return this.ligneCommandes;
    }

    public void setLigneCommandes(Set<LigneCommande> ligneCommandes) {
        if (this.ligneCommandes != null) {
            this.ligneCommandes.forEach(i -> i.setArticle(null));
        }
        if (ligneCommandes != null) {
            ligneCommandes.forEach(i -> i.setArticle(this));
        }
        this.ligneCommandes = ligneCommandes;
    }

    public Article ligneCommandes(Set<LigneCommande> ligneCommandes) {
        this.setLigneCommandes(ligneCommandes);
        return this;
    }

    public Article addLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.add(ligneCommande);
        ligneCommande.setArticle(this);
        return this;
    }

    public Article removeLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.remove(ligneCommande);
        ligneCommande.setArticle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", cai=" + getCai() +
            ", refPneu=" + getRefPneu() +
            ", typePneu='" + getTypePneu() + "'" +
            ", valeur=" + getValeur() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", devise='" + getDevise() + "'" +
            "}";
    }
}
