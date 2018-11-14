package wormius.games.wormiusgames.database.services;

import wormius.games.wormiusgames.database.entities.HighScore;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface IHighScoreService {
	public void saveScore(User player, int score) throws WormiusGamesExeption;
	public HighScore getHighestScore() throws WormiusGamesExeption;
}
