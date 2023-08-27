package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Aeroport")
public class Aeroport extends Station {

	@Column(name="max_avions")
	private int maxAvions;


	public int getMaxAvions() {
		return maxAvions;
	}

	public void setMaxAvions(int max) {
		this.maxAvions = max;
	}
}
