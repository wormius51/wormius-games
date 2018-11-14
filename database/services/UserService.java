package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.Level;
import wormius.games.wormiusgames.database.entities.LoginCredencials;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.LevelRepository;
import wormius.games.wormiusgames.database.repositories.LoginCredencialsRepository;
import wormius.games.wormiusgames.database.repositories.UserRepository;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private LevelRepository levelRepository;
	@Autowired
	private LoginCredencialsRepository credencialsRepository;
	
	@Override
	public ArrayList<User> getAllUsers() {
		return (ArrayList<User>) repository.findAll();
	}

	@Override
	public Optional<User> getUserById(long id) {
		return repository.findById(id);
	}

	@Override
	public User getUserByCredencials(String email, String password) throws WormiusGamesExeption {
		try {
			Optional<LoginCredencials> optional = credencialsRepository.getByCredencials(email, password);
			if (!optional.isPresent())
				return null;
			return optional.get().getUser();
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public ArrayList<User> getFollowingList(long id) throws WormiusGamesExeption {
		ArrayList<User> followingList = new ArrayList<>();
		try {
			Set<User> folloingSet = repository.findById(id).get().actuallyGetFollowingList();
			for (User user : folloingSet) {
				followingList.add(user);
			}
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
		return followingList;
	}

	@Override
	public boolean follow(long followerId, long followedId) {
		try {
			User user = repository.findById(followerId).get();
			User followed = repository.findById(followedId).get();
			if (user.actuallyGetFollowingList().contains(followed))
				return false;
			user.actuallyGetFollowingList().add(followed);
			user.setFollowing(user.getFollowing() + 1);
			followed.setFollowers(followed.getFollowers() + 1);
			repository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean playLevel(long playerId, long levelId) {
		try {
			User player = repository.findById(playerId).get();
			Level level = levelRepository.findById(levelId).get();
			if (player.actuallGetPlayedLevels().contains(level))
				return false;
			player.actuallGetPlayedLevels().add(level);
			repository.save(player);
			level.setPlayes(level.getPlayes() + 1);
			levelRepository.save(level);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean solveLevel(long playerId, long levelId) {
		try {
			User player = repository.findById(playerId).get();
			Level level = levelRepository.findById(levelId).get();
			if (player.actuallGetSolvedLevels().contains(level))
				return false;
			player.actuallGetSolvedLevels().add(level);
			repository.save(player);
			level.setSolves(level.getSolves() + 1);
			levelRepository.save(level);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean likeLevel(long playerId, long levelId) {
		try {
			User player = repository.findById(playerId).get();
			Level level = levelRepository.findById(levelId).get();
			if (player.actuallyGetLikedLevels().contains(level))
				return false;
			player.actuallyGetLikedLevels().add(level);
			repository.save(player);
			level.setLikes(level.getLikes() + 1);
			levelRepository.save(level);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean didLike(long playerId, long levelId) {
		try {
			User player = repository.findById(playerId).get();
			Level level = levelRepository.findById(levelId).get();
			if (player.actuallyGetLikedLevels().contains(level))
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
