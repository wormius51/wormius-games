package wormius.games.wormiusgames.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BCSTATSS")
public class BCstats {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@OneToOne
	private User owner;
	private int wins;
	private int losses;
	private int draws;
	private int rating;
	@Column(name="coins", columnDefinition="Decimal(10,0) default '0'")
	private int coins;
	public BCstats() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BCstats(long id, User owner, int wins, int losses, int draws, int rating,int coins) {
		super();
		this.id = id;
		this.owner = owner;
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
		this.rating = rating;
		this.coins = coins;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getDraws() {
		return draws;
	}
	public void setDraws(int draws) {
		this.draws = draws;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BCstats other = (BCstats) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BCstats [id=" + id + ", owner=" + owner + ", wins=" + wins + ", losses=" + losses + ", draws=" + draws
				+ ", rating=" + rating + ",coins=" + coins + "]";
	}
}
