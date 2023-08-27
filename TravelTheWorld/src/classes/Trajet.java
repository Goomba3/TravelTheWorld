package classes;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

@Entity
@Table(name = "Trajet")
public class Trajet extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "id_station_depart")
    private Station stationDepart;

    @ManyToOne
    @JoinColumn(name = "id_station_arrivee")
    private Station stationArrivee;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_moyen_transport")
    private MoyenTransport moyenTransport;

    public Station getStartStation() {
        return stationDepart;
    }

    public void setStartStation(Station startStation) {
        this.stationDepart = startStation;
    }

    public Station getEndStation() {
        return stationArrivee;
    }

    public void setEndStation(Station endStation) {
        this.stationArrivee = endStation;
    }

    public MoyenTransport getMoyenTransport() {
        return moyenTransport;
    }

    public void setMoyenTransport(MoyenTransport moyenTransport) {
        this.moyenTransport = moyenTransport;
    }
    
    @Override
    public String toString() {
    	return stationDepart.getNom() + " -> " + stationArrivee.getNom();
    }
}