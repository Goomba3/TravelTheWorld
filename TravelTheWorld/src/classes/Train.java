package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Train")
public class Train extends MoyenTransport {

    @Column(name = "nb_wagons")
    private int nbWagons;

    public int getNbWagons() {
        return nbWagons;
    }

    public void setNbWagons(int nbWagons) {
        this.nbWagons = nbWagons;
    }
}