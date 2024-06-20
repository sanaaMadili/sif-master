package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reclamation.
 */
@Entity
@Table(name = "reclamation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reclamation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private String date;

    @Lob
    @Column(name = "piece_jointe")
    private byte[] pieceJointe;

    @Column(name = "piece_jointe_content_type")
    private String pieceJointeContentType;

    @Column(name = "raison")
    private String raison;

    @ManyToOne
    @JsonIgnoreProperties(value = { "extraUser", "reclamations", "appelCommandes" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reclamation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public Reclamation date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getPieceJointe() {
        return this.pieceJointe;
    }

    public Reclamation pieceJointe(byte[] pieceJointe) {
        this.setPieceJointe(pieceJointe);
        return this;
    }

    public void setPieceJointe(byte[] pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public String getPieceJointeContentType() {
        return this.pieceJointeContentType;
    }

    public Reclamation pieceJointeContentType(String pieceJointeContentType) {
        this.pieceJointeContentType = pieceJointeContentType;
        return this;
    }

    public void setPieceJointeContentType(String pieceJointeContentType) {
        this.pieceJointeContentType = pieceJointeContentType;
    }

    public String getRaison() {
        return this.raison;
    }

    public Reclamation raison(String raison) {
        this.setRaison(raison);
        return this;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Reclamation client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reclamation)) {
            return false;
        }
        return id != null && id.equals(((Reclamation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reclamation{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", pieceJointe='" + getPieceJointe() + "'" +
            ", pieceJointeContentType='" + getPieceJointeContentType() + "'" +
            ", raison='" + getRaison() + "'" +
            "}";
    }
}
