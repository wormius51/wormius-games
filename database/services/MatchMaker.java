package wormius.games.wormiusgames.database.services;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.Match;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.services.IMatchMaker;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class MatchMaker implements IMatchMaker {
	
	@Autowired
	private IBCstatsService bCstatsService;
	
	private Set<Match> matches = new HashSet<Match>();

	private Set<Deck> quake = new HashSet<Deck>();

	/**
	 * Adds the user to the quake.
	 * 
	 * @param deck
	 *            The deck that will be used in the match (deck already has a
	 *            creator attribute).
	 * @throws WormiusGamesExeption
	 *             if this user is allready in a match.
	 */
	public void joinQuake(Deck deck) throws WormiusGamesExeption {
		User user = deck.getCreator();
		Match match = null;
		try {
			match = getMatch(deck.getCreator());
		} catch (WormiusGamesExeption e) {
			
		}
		if (match != null) {
			throw new WormiusGamesExeption("this user is allready playing");
		}
		leaveQuake(user);
		quake.add(deck);
		pairPlayers();
	}

	/**
	 * Removes the user from the quake.
	 * 
	 * @param user
	 *            The user that leaves the quake.
	 * @return True if the user was on the quake to begin with.
	 */
	public boolean leaveQuake(User user) {
		return quake.removeIf(d -> user.equals(d.getCreator()));
	}

	/**
	 * Gets the match that the user participates in.
	 * 
	 * @param user
	 *            The user playing the match.
	 * @return The match.
	 * @throws WormiusGamesExeption
	 *             if this user is not playing a match.
	 */
	public Match getMatch(User user) throws WormiusGamesExeption {
		Stream<Match> stream = matches.stream();
		Optional<Match> op = stream.filter(m -> (user.equals(m.getPlayers()[0])) || (user.equals(m.getPlayers()[1])))
				.findAny();
		if (op.isPresent())
			return op.get();
		throw new WormiusGamesExeption("No match found");
	}

	/**
	 * Uses the quake to create matches and populate the matches set with them.
	 * Removes the users from the quake.
	 * 
	 * @param deck0
	 *            The first deck to use.
	 * @param deck1
	 *            The second deck to use.
	 */
	private void createMatch(Deck deck0, Deck deck1) {
		leaveQuake(deck0.getCreator());
		leaveQuake(deck1.getCreator());
		Match match = new Match();
		match.addDeck(deck0);
		match.addDeck(deck1);
		match.addPlayer(deck0.getCreator());
		match.addPlayer(deck1.getCreator());
		matches.add(match);
	}

	/**
	 * Attempts to pair two players to a match.
	 */
	@Async
	private void pairPlayers() {
		if (quake.size() < 2)
			return;
		Deck deck0 = quake.stream().findFirst().get();
		Deck deck1 = quake.stream().filter(d -> !deck0.equals(d)).min(searchComparator()).get();
		createMatch(deck0, deck1);
	}
	
	private Comparator<? super Deck> searchComparator() {
		Comparator<? super Deck> c = new Comparator<Deck>() {
			@Override
			public int compare(Deck o1, Deck o2) {
				try {
					BCstats s1 = bCstatsService.getBCstats(o1.getCreator());
					BCstats s2 = bCstatsService.getBCstats(o2.getCreator());
					
					return Math.abs(s1.getRating() - s2.getRating());
					
				} catch (WormiusGamesExeption e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return 0;
			}
		};
		
		return c;
	}

	/**
	 * The second time this method is called on the same match, the match is
	 * removed.
	 * 
	 * @param user
	 *            The user that plays the match.
	 * @throws WormiusGamesExeption
	 *             this user is not playing a match.
	 */
	public boolean endMatch(User user) throws WormiusGamesExeption {
		Match match = getMatch(user);
		int finishedCount = 0;
		int userIndex = 0;
		for (int i = 0; i < match.getPlayers().length; i++) {
			if (user.equals(match.getPlayers()[i])) {
				if (match.getPlayerFinished()[i])
					return false;
				match.setPlayerFinished(i, true);
				userIndex = i;
				finishedCount++;
			} else if (match.getPlayerFinished()[i]) {
				finishedCount++;
			}
		}
		if (finishedCount == match.getPlayerFinished().length)
			removeMatch(match);
		if (match.getWinner() != -2 && match.getWinner() != userIndex) {
			return randome(50, match.getMoves().size());
		}
		return random();
	}
	
	private boolean random() {
		return random(50);
	}
	
	private boolean randome(double chance, int numberOfMoves) {
		return random(50 * numberOfMoves / (numberOfMoves + 10));
	}
	
	private boolean random(double chance) {
		return Math.random() * 100 <= chance;
	}
	
	public void giveUp(User user) throws WormiusGamesExeption {
		Match match = getMatch(user);
		if (match.getPlayers().length == 2) {
			for (int i = 0; i < match.getPlayers().length; i++) {
				if (!user.equals(match.getPlayers()[i])) {
					match.setWinner(i);
				} else {
					match.setPlayerFinished(i, true);
				}
			}
		}
	}
	
	private void giveUp(Match match, int index) {
		for (int i = 0; i < match.getPlayers().length; i++) {
			if (i != index) {
				match.setWinner(i);
			} else {
				match.setPlayerFinished(i, true);
			}
		}
	}
	
	@Scheduled(fixedDelay = 5000)
	public void checkInactivity() {
		for (Match match : matches) {
			int endCount = 0;
			for (int i = 0; i < match.getStrikes().length; i++) {
				if (match.getPlayerFinished()[i])
					endCount++;
				if (match.getStrikes()[i] > 9) {
					giveUp(match, i);
					break;
				} else {
					match.strike(i);
				}
			}
			if (endCount == match.getPlayerFinished().length)
				removeMatch(match);
		}
	}
	
	public Match ping(User user) throws WormiusGamesExeption {
		Match match = getMatch(user);
		for (int i = 0; i < match.getStrikes().length; i++) {
			if (user.equals(match.getPlayers()[i])) {
				match.ping(i);
				break;
			}
		}
		
		return match;
	}
	
	private void removeMatch(Match match) {
		matches.remove(match);
		if (match.getWinner() >= 0) {
			int winnerIndex = match.getWinner();
			User winner = match.getPlayers()[winnerIndex];
			User loser = match.getPlayers()[(winnerIndex + 1) % 2];
			try {
				bCstatsService.winLose(winner, loser);
			} catch (WormiusGamesExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setEmote(User user, String emote) throws WormiusGamesExeption {
		Match match = getMatch(user);
		for (int i = 0; i < match.getPlayers().length; i++) {
			if (user.equals(match.getPlayers()[i])) {
				match.setEmote(i, emote);
				break;
			}
		}
	}
}
