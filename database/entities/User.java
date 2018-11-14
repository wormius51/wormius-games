package wormius.games.wormiusgames.database.entities;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USERS")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(unique = true)
	private String name;
	private int followers;
	private int following;
	private int solves;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "FOLLOWS", joinColumns = {@JoinColumn(name = "followerId",nullable = false)},
			inverseJoinColumns = {@JoinColumn(name = "followedId",nullable = false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"followerId","followedId"})})
	private Set<User> followingList;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PlAYED_LEVELS", joinColumns = {@JoinColumn(name = "playerId", nullable = false)},
			inverseJoinColumns = {@JoinColumn(name = "levelId", nullable = false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"playerId","levelId"})})
	private Set<Level> playedLevels;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SOLVED_LEVELS", joinColumns = {@JoinColumn(name = "playerId", nullable = false)},
			inverseJoinColumns = {@JoinColumn(name = "levelId", nullable = false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"playerId","levelId"})})
	private Set<Level> solvedLevels;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "LIKED_LEVELS", joinColumns = {@JoinColumn(name = "playerId", nullable = false)},
			inverseJoinColumns = {@JoinColumn(name = "levelId", nullable = false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"playerId","levelId"})})
	private Set<Level> likedLevels;
	public User() {
	}
	
	public User(long id, String name, int followers, int following, int solves, Set<User> followingList,
			Set<Level> playedLevels, Set<Level> solvedLevels, Set<Level> likedLevels) {
		super();
		this.id = id;
		this.name = name;
		this.followers = followers;
		this.following = following;
		this.solves = solves;
		this.followingList = followingList;
		this.playedLevels = playedLevels;
		this.solvedLevels = solvedLevels;
		this.likedLevels = likedLevels;
	}
	/**
	 * don't want to load all the levels every request
	 * @return a new hash set
	 */
	/*public Set<Level> getPlayedLevels() {
		return new HashSet<>();
	}*/
	
	public Set<Level> actuallGetPlayedLevels() {
		return playedLevels;
	}

	public void setPlayedLevels(Set<Level> playedLevels) {
		this.playedLevels = playedLevels;
	}
	/**
	 * don't want to load all the levels every request
	 * @return a new hash set
	 */
	/*public Set<Level> getSolvedLevels() {
		return new HashSet<>();
	}*/
	
	public Set<Level> actuallGetSolvedLevels() {
		return solvedLevels;
	}

	public void setSolvedLevels(Set<Level> solvedLevels) {
		this.solvedLevels = solvedLevels;
	}
	/**
	 * don't want to load all the levels every request
	 * @return a new hash set
	 */
	/*public Set<Level> getLikedLevels() {
		return new HashSet<>();
	}*/
	
	public Set<Level> actuallyGetLikedLevels() {
		return likedLevels;
	}

	public void setLikedLevels(Set<Level> likedLevels) {
		this.likedLevels = likedLevels;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getFollowing() {
		return following;
	}
	public void setFollowing(int following) {
		this.following = following;
	}
	public int getSolves() {
		return solves;
	}
	public void setSolves(int solves) {
		this.solves = solves;
	}
	
	/*public Set<User> getFollowingList() {
		return new HashSet<>();
	}*/
	
	public Set<User> actuallyGetFollowingList() {
		return followingList;
	}
	
	public void setFollowingList(Set<User> followingList) {
		this.followingList = followingList;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", followers=" + followers + ", following=" + following
				+ ", solves=" + solves + ", followingList=" + followingList + ", playedLevels=" + playedLevels
				+ ", solvedLevels=" + solvedLevels + ", likedLevels=" + likedLevels + "]";
	}
	
}
