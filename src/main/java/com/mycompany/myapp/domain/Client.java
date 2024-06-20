package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

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

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Reclamation> reclamations = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reception", "client", "ligneCommandes" }, allowSetters = true)
    private Set<AppelCommande> appelCommandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfession() {
        return this.profession;
    }

    public Client profession(String profession) {
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

    public Client extraUser(ExtraUser extraUser) {
        this.setExtraUser(extraUser);
        return this;
    }

    public Set<Reclamation> getReclamations() {
        return this.reclamations;
    }

    public void setReclamations(Set<Reclamation> reclamations) {
        if (this.reclamations != null) {
            this.reclamations.forEach(i -> i.setClient(null));
        }
        if (reclamations != null) {
            reclamations.forEach(i -> i.setClient(this));
        }
        this.reclamations = reclamations;
    }

    public Client reclamations(Set<Reclamation> reclamations) {
        this.setReclamations(reclamations);
        return this;
    }

    public Client addReclamation(Reclamation reclamation) {
        this.reclamations.add(reclamation);
        reclamation.setClient(this);
        return this;
    }

    public Client removeReclamation(Reclamation reclamation) {
        this.reclamations.remove(reclamation);
        reclamation.setClient(null);
        return this;
    }

    public Set<AppelCommande> getAppelCommandes() {
        return this.appelCommandes;
    }

    public void setAppelCommandes(Set<AppelCommande> appelCommandes) {
        if (this.appelCommandes != null) {
            this.appelCommandes.forEach(i -> i.setClient(null));
        }
        if (appelCommandes != null) {
            appelCommandes.forEach(i -> i.setClient(this));
        }
        this.appelCommandes = appelCommandes;
    }

    public Client appelCommandes(Set<AppelCommande> appelCommandes) {
        this.setAppelCommandes(appelCommandes);
        return this;
    }

    public Client addAppelCommande(AppelCommande appelCommande) {
        this.appelCommandes.add(appelCommande);
        appelCommande.setClient(this);
        return this;
    }

    public Client removeAppelCommande(AppelCommande appelCommande) {
        this.appelCommandes.remove(appelCommande);
        appelCommande.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", profession='" + getProfession() + "'" +
            "}";
    }
}
