package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppelCommande.
 */
@Entity
@Table(name = "appel_commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppelCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_commande")
    private Integer numCommande;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    @Column(name = "date_livraison")
    private LocalDate dateLivraison;

    @Column(name = "date_expedition")
    private LocalDate dateExpedition;

    @Column(name = "status")
    private Integer status;

    @Column(name = "annomalie")
    private Integer annomalie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appelCommandes" }, allowSetters = true)
    private Reception reception;

    @ManyToOne
    @JsonIgnoreProperties(value = { "extraUser", "reclamations", "appelCommandes" }, allowSetters = true)
    private Client client;

    @OneToMany(mappedBy = "appelCommande")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "article", "appelCommande" }, allowSetters = true)
    private Set<LigneCommande> ligneCommandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppelCommande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumCommande() {
        return this.numCommande;
    }

    public AppelCommande numCommande(Integer numCommande) {
        this.setNumCommande(numCommande);
        return this;
    }

    public void setNumCommande(Integer numCommande) {
        this.numCommande = numCommande;
    }

    public LocalDate getDateCommande() {
        return this.dateCommande;
    }

    public AppelCommande dateCommande(LocalDate dateCommande) {
        this.setDateCommande(dateCommande);
        return this;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateLivraison() {
        return this.dateLivraison;
    }

    public AppelCommande dateLivraison(LocalDate dateLivraison) {
        this.setDateLivraison(dateLivraison);
        return this;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public LocalDate getDateExpedition() {
        return this.dateExpedition;
    }

    public AppelCommande dateExpedition(LocalDate dateExpedition) {
        this.setDateExpedition(dateExpedition);
        return this;
    }

    public void setDateExpedition(LocalDate dateExpedition) {
        this.dateExpedition = dateExpedition;
    }

    public Integer getStatus() {
        return this.status;
    }

    public AppelCommande status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAnnomalie() {
        return this.annomalie;
    }

    public AppelCommande annomalie(Integer annomalie) {
        this.setAnnomalie(annomalie);
        return this;
    }

    public void setAnnomalie(Integer annomalie) {
        this.annomalie = annomalie;
    }

    public Reception getReception() {
        return this.reception;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
    }

    public AppelCommande reception(Reception reception) {
        this.setReception(reception);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AppelCommande client(Client client) {
        this.setClient(client);
        return this;
    }

    public Set<LigneCommande> getLigneCommandes() {
        return this.ligneCommandes;
    }

    public void setLigneCommandes(Set<LigneCommande> ligneCommandes) {
        if (this.ligneCommandes != null) {
            this.ligneCommandes.forEach(i -> i.setAppelCommande(null));
        }
        if (ligneCommandes != null) {
            ligneCommandes.forEach(i -> i.setAppelCommande(this));
        }
        this.ligneCommandes = ligneCommandes;
    }

    public AppelCommande ligneCommandes(Set<LigneCommande> ligneCommandes) {
        this.setLigneCommandes(ligneCommandes);
        return this;
    }

    public AppelCommande addLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.add(ligneCommande);
        ligneCommande.setAppelCommande(this);
        return this;
    }

    public AppelCommande removeLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.remove(ligneCommande);
        ligneCommande.setAppelCommande(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppelCommande)) {
            return false;
        }
        return id != null && id.equals(((AppelCommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppelCommande{" +
            "id=" + getId() +
            ", numCommande=" + getNumCommande() +
            ", dateCommande='" + getDateCommande() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            ", dateExpedition='" + getDateExpedition() + "'" +
            ", status=" + getStatus() +
            ", annomalie=" + getAnnomalie() +
            "}";
    }
}
