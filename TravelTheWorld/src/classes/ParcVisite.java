package classes;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ParcVisite")
public class ParcVisite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_parc")
    private ParcDisney parc;

    @Column(name = "date_visite")
    private Date dateVisite;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParcDisney getParc() {
        return parc;
    }

    public void setParc(ParcDisney parc) {
        this.parc = parc;
    }

    public Date getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(Date dateVisite) {
        this.dateVisite = dateVisite;
    }
    
    @Override
    public String toString() {
    	return this.getClass().getName() + " du " + this.getDateVisite();
    }
}