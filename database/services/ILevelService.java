package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;

import wormius.games.wormiusgames.database.entities.Level;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface ILevelService {
	public ArrayList<Level> getAllLevels() throws WormiusGamesExeption;
	public Optional<Level> getLevel(long id) throws WormiusGamesExeption;
	public ArrayList<Level> getLevelsByCreatorId(long creatorId) throws WormiusGamesExeption;
	public ArrayList<Level> getLevelsBySearchWord(String searchWord) throws WormiusGamesExeption;
	public ArrayList<Level> getLevelsByParameters(int page, int pageLength,int playesDirection,
			int likesDirection, double solvePrecentage, String searchWord) throws WormiusGamesExeption;
	public void addLevel(Level level) throws WormiusGamesExeption;
	public void removeLevel(Level level) throws WormiusGamesExeption;
}
