package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Clr.
 */
@Entity
@Table(name = "clr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Clr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "constructeur_automobile")
    private String constructeurAutomobile;

    @Column(name = "model_voiture")
    private String modelVoiture;

    @Column(name = "annee_voiture")
    private Integer anneeVoiture;

    @Column(name = "etat_pneu")
    private String etatPneu;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "date_production")
    private LocalDate dateProduction;

    @JsonIgnoreProperties(value = { "reception", "client", "ligneCommandes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AppelCommande appelCommande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Clr id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConstructeurAutomobile() {
        return this.constructeurAutomobile;
    }

    public Clr constructeurAutomobile(String constructeurAutomobile) {
        this.setConstructeurAutomobile(constructeurAutomobile);
        return this;
    }

    public void setConstructeurAutomobile(String constructeurAutomobile) {
        this.constructeurAutomobile = constructeurAutomobile;
    }

    public String getModelVoiture() {
        return this.modelVoiture;
    }

    public Clr modelVoiture(String modelVoiture) {
        this.setModelVoiture(modelVoiture);
        return this;
    }

    public void setModelVoiture(String modelVoiture) {
        this.modelVoiture = modelVoiture;
    }

    public Integer getAnneeVoiture() {
        return this.anneeVoiture;
    }

    public Clr anneeVoiture(Integer anneeVoiture) {
        this.setAnneeVoiture(anneeVoiture);
        return this;
    }

    public void setAnneeVoiture(Integer anneeVoiture) {
        this.anneeVoiture = anneeVoiture;
    }

    public String getEtatPneu() {
        return this.etatPneu;
    }

    public Clr etatPneu(String etatPneu) {
        this.setEtatPneu(etatPneu);
        return this;
    }

    public void setEtatPneu(String etatPneu) {
        this.etatPneu = etatPneu;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Clr image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Clr imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public LocalDate getDateProduction() {
        return this.dateProduction;
    }

    public Clr dateProduction(LocalDate dateProduction) {
        this.setDateProduction(dateProduction);
        return this;
    }

    public void setDateProduction(LocalDate dateProduction) {
        this.dateProduction = dateProduction;
    }

    public AppelCommande getAppelCommande() {
        return this.appelCommande;
    }

    public void setAppelCommande(AppelCommande appelCommande) {
        this.appelCommande = appelCommande;
    }

    public Clr appelCommande(AppelCommande appelCommande) {
        this.setAppelCommande(appelCommande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clr)) {
            return false;
        }
        return id != null && id.equals(((Clr) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clr{" +
            "id=" + getId() +
            ", constructeurAutomobile='" + getConstructeurAutomobile() + "'" +
            ", modelVoiture='" + getModelVoiture() + "'" +
            ", anneeVoiture=" + getAnneeVoiture() +
            ", etatPneu='" + getEtatPneu() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", dateProduction='" + getDateProduction() + "'" +
            "}";
    }
}
