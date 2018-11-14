package wormius.games.wormiusgames.database.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import wormius.games.wormiusgames.helpers.CardPackType;

@Entity
@Table(name = "CARDPACKS")
public class CardPack {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;
	@NonNull
	private CardPackType type;
	public CardPack() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardPack(Long id, User owner, CardPackType type) {
		super();
		this.id = id;
		this.owner = owner;
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public CardPackType getType() {
		return type;
	}
	public void setType(CardPackType type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CardPack other = (CardPack) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CardPack [id=" + id + ", owner=" + owner + ", type=" + type + "]";
	}
	
}
