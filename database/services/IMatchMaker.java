package wormius.games.wormiusgames.database.services;

import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.Match;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface IMatchMaker {
	public void joinQuake(Deck deck) throws WormiusGamesExeption;
	public boolean leaveQuake(User user);
	public Match getMatch(User user) throws WormiusGamesExeption;
	public boolean endMatch(User user) throws WormiusGamesExeption;
	public void giveUp(User user) throws WormiusGamesExeption;
	public Match ping(User user) throws WormiusGamesExeption;
	public void checkInactivity();
	public void setEmote(User user, String emote) throws WormiusGamesExeption;
}
