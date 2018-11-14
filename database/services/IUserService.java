package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;

import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface IUserService {
	public ArrayList<User> getAllUsers() throws WormiusGamesExeption;
	public Optional<User> getUserById(long id) throws WormiusGamesExeption;
	public User getUserByCredencials(String email, String password) throws WormiusGamesExeption;
	public ArrayList<User> getFollowingList(long id) throws WormiusGamesExeption;
	public boolean follow(long followerId,long followedId) throws WormiusGamesExeption;
	public boolean playLevel(long playerId,long levelId) throws WormiusGamesExeption;
	public boolean solveLevel(long playerId,long levelId) throws WormiusGamesExeption;
	public boolean likeLevel(long playerId,long levelId) throws WormiusGamesExeption;
	public boolean didLike(long playerId,long levelId) throws WormiusGamesExeption;
}
