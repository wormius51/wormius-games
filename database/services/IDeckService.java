package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;

import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface IDeckService {
	public Deck getDeck(long id);
	public ArrayList<Deck> getAllDecks(User user);
	public ArrayList<Deck> getAllDecks(User user, String searchWord);
	public Deck findDeckByNameAndCreator(String name, User creator);
	public void saveDeck(Deck deck) throws WormiusGamesExeption;
	public void deleteDeck(Deck deck);
	public Deck createStarterDeck(User user) throws WormiusGamesExeption;
}
