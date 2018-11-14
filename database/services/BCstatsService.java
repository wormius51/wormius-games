package wormius.games.wormiusgames.database.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.BCstatsRepository;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class BCstatsService implements IBCstatsService {

	@Autowired
	private BCstatsRepository repository;
	
	@Override
	public BCstats getBCstats(User user) throws WormiusGamesExeption {
		Optional<BCstats> op = repository.findByOwner(user);
		if (!op.isPresent())
			throw new WormiusGamesExeption("could not find the stats of this user");
		return op.get();
	}

	@Override
	public void saveBCstats(BCstats stats) throws WormiusGamesExeption {
		try {
			repository.save(stats);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}

	}

	@Override
	public void deleteBCstats(BCstats stats) throws WormiusGamesExeption {
		try {
			repository.delete(stats);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public void createBCstats(User user) throws WormiusGamesExeption {
		Optional<BCstats> op = repository.findByOwner(user);
		if (op.isPresent())
			throw new WormiusGamesExeption("this user allready has stats.");
		BCstats b = new BCstats();
		b.setOwner(user);
		saveBCstats(b);
	}

	@Override
	@Async
	public void winLose(User winner, User loser) throws WormiusGamesExeption {
		BCstats ws = getBCstats(winner);
		BCstats ls = getBCstats(loser);
		ws.setWins(ws.getWins() + 1);
		ls.setLosses(ls.getLosses() + 1);
		ws.setCoins(ws.getCoins() + 10);
		ls.setCoins(ls.getCoins() + 5);
		int ratingChange = ratingChange(ws.getRating(), ls.getRating());
		ws.setRating(ws.getRating() + ratingChange);
		ls.setRating(ls.getRating() - ratingChange);
		saveBCstats(ws);
		saveBCstats(ls);
	}
	
	private int ratingChange(int winnerRating, int loserRating) {
		int k = 30;
		double expectedScore = 1 / (1 + Math.pow(10, (loserRating - winnerRating) / 400));
		int change =  (int) ((1 - expectedScore) * k);
		return change;
	}

}
