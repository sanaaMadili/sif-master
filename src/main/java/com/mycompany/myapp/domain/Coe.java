package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coe.
 */
@Entity
@Table(name = "coe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_voiture")
    private String typeVoiture;

    @Column(name = "poids_voiture")
    private Double poidsVoiture;

    @Column(name = "vitesse_voiture")
    private Double vitesseVoiture;

    @JsonIgnoreProperties(value = { "reception", "client", "ligneCommandes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AppelCommande appelCommande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeVoiture() {
        return this.typeVoiture;
    }

    public Coe typeVoiture(String typeVoiture) {
        this.setTypeVoiture(typeVoiture);
        return this;
    }

    public void setTypeVoiture(String typeVoiture) {
        this.typeVoiture = typeVoiture;
    }

    public Double getPoidsVoiture() {
        return this.poidsVoiture;
    }

    public Coe poidsVoiture(Double poidsVoiture) {
        this.setPoidsVoiture(poidsVoiture);
        return this;
    }

    public void setPoidsVoiture(Double poidsVoiture) {
        this.poidsVoiture = poidsVoiture;
    }

    public Double getVitesseVoiture() {
        return this.vitesseVoiture;
    }

    public Coe vitesseVoiture(Double vitesseVoiture) {
        this.setVitesseVoiture(vitesseVoiture);
        return this;
    }

    public void setVitesseVoiture(Double vitesseVoiture) {
        this.vitesseVoiture = vitesseVoiture;
    }

    public AppelCommande getAppelCommande() {
        return this.appelCommande;
    }

    public void setAppelCommande(AppelCommande appelCommande) {
        this.appelCommande = appelCommande;
    }

    public Coe appelCommande(AppelCommande appelCommande) {
        this.setAppelCommande(appelCommande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coe)) {
            return false;
        }
        return id != null && id.equals(((Coe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coe{" +
            "id=" + getId() +
            ", typeVoiture='" + getTypeVoiture() + "'" +
            ", poidsVoiture=" + getPoidsVoiture() +
            ", vitesseVoiture=" + getVitesseVoiture() +
            "}";
    }
}
