package wormius.games.wormiusgames.database.entities;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "MATCHES")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private User[] players;
	private Deck[] decks;
	private ArrayList<Integer> moves;
	private int[] positions;
	private boolean finished;
	
	private ArrayList<Integer> p0Rolls;
	private ArrayList<Integer> p1Rolls;
	
	private boolean[] playerFinished;
	
	private int winner;
	private int[] strikes;
	
	private String[] emotes;
	
	public Match() {
		positions = new int[2];
		positions[0] = (int)(Math.random() * 20);
		positions[1] = (int)(Math.random() * 20);
		moves = new ArrayList<>();
		finished = false;
		p0Rolls = new ArrayList<>();
		p1Rolls = new ArrayList<>();
		playerFinished = new boolean[2];
		for (int i = 0; i < playerFinished.length; i++) {
			setPlayerFinished(i, false);
		}
		winner = -2; // not finished
		
		strikes = new int[2];
		strikes[0] = 0;
		strikes[1] = 1;
		
		emotes = new String[2];
		emotes[0] = "";
		emotes[1] = "";
	}

	public Match(long id, User[] players, Deck[] decks, ArrayList<Integer> moves, int[] positions, boolean finished,
			ArrayList<Integer> p0Rolls, ArrayList<Integer> p1Rolls, boolean[] playerFinished, int winner,
			int[] strikes) {
		super();
		this.id = id;
		this.players = players;
		this.decks = decks;
		this.moves = moves;
		this.positions = positions;
		this.finished = finished;
		this.p0Rolls = p0Rolls;
		this.p1Rolls = p1Rolls;
		this.playerFinished = playerFinished;
		this.winner = winner;
		this.strikes = strikes;
	}



	public void addPlayer(User player) {
		if (players == null) {
			players = new User[2];
			players[0] = player;
		} else {
			players[1] = player;
		}
	}
	
	public void addDeck(Deck deck) {
		if (decks == null) {
			decks = new Deck[2];
			decks[0] = deck;
		} else {
			decks[1] = deck;
		}
	}

	public void addMove(int move) {
		if (moves == null) {
			moves = new ArrayList<>();
		}
		moves.add(move);
	}
	
	public void roll (int player) {
		if (player < players.length) {
			ArrayList<Integer> rolls;
			if (player == 0) {
				rolls = p0Rolls;
			} else {
				rolls = p1Rolls;
			}
			rolls.add((int) (Math.random() * 20));
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User[] getPlayers() {
		return players;
	}

	public void setPlayers(User[] players) {
		this.players = players;
	}

	public Deck[] getDecks() {
		return decks;
	}

	public void setDecks(Deck[] decks) {
		this.decks = decks;
	}

	public ArrayList<Integer> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<Integer> moves) {
		this.moves = moves;
	}

	public int[] getPositions() {
		return positions;
	}

	public void setPositions(int[] positions) {
		this.positions = positions;
	}
	
	public void setPosition (int index, int value) {
		positions[index] = value;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public ArrayList<Integer> getP0Rolls() {
		return p0Rolls;
	}

	public void setP0Rolls(ArrayList<Integer> p0Rolls) {
		this.p0Rolls = p0Rolls;
	}

	public ArrayList<Integer> getP1Rolls() {
		return p1Rolls;
	}

	public void setP1Rolls(ArrayList<Integer> p1Rolls) {
		this.p1Rolls = p1Rolls;
	}
	
	public boolean[] getPlayerFinished() {
		return playerFinished;
	}

	public void setPlayerFinished(boolean[] playerFinished) {
		this.playerFinished = playerFinished;
	}
	
	public void setPlayerFinished(int index, boolean isFinished) {
		playerFinished[index] = isFinished;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int[] getStrikes() {
		return strikes;
	}

	public void setStrikes(int[] strikes) {
		this.strikes = strikes;
	}
	
	public void strike(int i) {
		strikes[i]++;
	}
	
	public void ping(int i) {
		strikes[i] = 0;
	}

	public String[] getEmotes() {
		return emotes;
	}

	public void setEmotes(String[] emotes) {
		this.emotes = emotes;
	}
	
	public void setEmote(int player, String emote) {
		emotes[player] = emote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(players);
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
		Match other = (Match) obj;
		if ((other.getPlayers()[0] != null && other.getPlayers()[0].equals(players[0])) || 
				(other.getPlayers()[0] != null && other.getPlayers()[0].equals(players[1])) || 
				(other.getPlayers()[1] != null && other.getPlayers()[1].equals(players[0])) ||
				(other.getPlayers()[1] != null && other.getPlayers()[1].equals(players[1])))
			return true;
		if (!Arrays.equals(players, other.players))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + ", players=" + Arrays.toString(players) + ", decks=" + Arrays.toString(decks)
				+ ", moves=" + moves + ", positions=" + Arrays.toString(positions) + ", finished=" + finished
				+ ", p0Rolls=" + p0Rolls + ", p1Rolls=" + p1Rolls + ", playerFinished="
				+ Arrays.toString(playerFinished) + ", winner=" + winner + ", strikes=" + Arrays.toString(strikes)
				+ "]";
	}
}
