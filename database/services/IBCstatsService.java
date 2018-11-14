package wormius.games.wormiusgames.database.services;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;;

public interface IBCstatsService {
	public BCstats getBCstats(User user) throws WormiusGamesExeption;
	public void saveBCstats(BCstats stats) throws WormiusGamesExeption;
	public void createBCstats(User user) throws WormiusGamesExeption;
	public void deleteBCstats(BCstats stats) throws WormiusGamesExeption;
	public void winLose(User winner, User loser) throws WormiusGamesExeption;
}
