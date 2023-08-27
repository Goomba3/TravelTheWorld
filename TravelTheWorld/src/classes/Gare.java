package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Gare")
public class Gare extends Station {

	@Column(name="max_trains")
	private int maxTrains;


	public int getMaxTrains() {
		return maxTrains;
	}

	public void setMaxTrains(int max) {
		this.maxTrains = max;
	}

}
