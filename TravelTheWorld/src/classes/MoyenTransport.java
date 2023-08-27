package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "MoyenTransport")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MoyenTransport extends BaseEntity {

    @Column(name = "nom")
    private String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    @Override
    public String toString() {
    	return this.getClass().getName() + " " + this.getNom();
    }
}