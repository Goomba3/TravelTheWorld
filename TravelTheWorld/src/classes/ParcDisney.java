package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ParcDisney")
public class ParcDisney extends BaseEntity {

    @Column(name = "nom")
    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    @Override
    public String toString() {
    	return this.getClass().getName() + " " + this.getNom();
    }
}
