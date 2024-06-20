package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reception.
 */
@Entity
@Table(name = "reception")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reception implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pays")
    private String pays;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "reception")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reception", "client", "ligneCommandes" }, allowSetters = true)
    private Set<AppelCommande> appelCommandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reception id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPays() {
        return this.pays;
    }

    public Reception pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getAddress() {
        return this.address;
    }

    public Reception address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<AppelCommande> getAppelCommandes() {
        return this.appelCommandes;
    }

    public void setAppelCommandes(Set<AppelCommande> appelCommandes) {
        if (this.appelCommandes != null) {
            this.appelCommandes.forEach(i -> i.setReception(null));
        }
        if (appelCommandes != null) {
            appelCommandes.forEach(i -> i.setReception(this));
        }
        this.appelCommandes = appelCommandes;
    }

    public Reception appelCommandes(Set<AppelCommande> appelCommandes) {
        this.setAppelCommandes(appelCommandes);
        return this;
    }

    public Reception addAppelCommande(AppelCommande appelCommande) {
        this.appelCommandes.add(appelCommande);
        appelCommande.setReception(this);
        return this;
    }

    public Reception removeAppelCommande(AppelCommande appelCommande) {
        this.appelCommandes.remove(appelCommande);
        appelCommande.setReception(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reception)) {
            return false;
        }
        return id != null && id.equals(((Reception) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reception{" +
            "id=" + getId() +
            ", pays='" + getPays() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
