package wormius.games.wormiusgames.database.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LEVELS")
public class Level{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "creator_id", nullable = false)
	private User creator;
	private String name;
	private String content;
	private int playes;
	private int solves;
	private int likes;
	public Level() {
	}
	public Level(Long id, User creator, String name, String content, int playes, int solves, int likes) {
		super();
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.content = content;
		this.playes = playes;
		this.solves = solves;
		this.likes = likes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPlayes() {
		return playes;
	}
	public void setPlayes(int playes) {
		this.playes = playes;
	}
	public int getSolves() {
		return solves;
	}
	public void setSolves(int solves) {
		this.solves = solves;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
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
		Level other = (Level) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Level [id=" + id + ", creator=" + creator + ", name=" + name + ", content=" + content + ", playes="
				+ playes + ", solves=" + solves + ", likes=" + likes + "]";
	}
	
	
}
