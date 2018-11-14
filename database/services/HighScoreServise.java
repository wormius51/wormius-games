package wormius.games.wormiusgames.database.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.HighScore;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.HighScoreRepository;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class HighScoreServise implements IHighScoreService {

	@Autowired
	HighScoreRepository repository;
	
	@Override
	public void saveScore(User player, int score) throws WormiusGamesExeption {
		try {
			try {
				Optional<HighScore> oh = repository.getScoreByPlayerId(player.getId());
				if (oh.isPresent()) {
					HighScore h = oh.get();
					if (score <= h.getScore())
						return;
					h.setScore(score);
					repository.save(h);
				} else {
					HighScore h = new HighScore();
					h.setPlayer(player);
					h.setScore(score);
					repository.save(h);
				}
			} catch (Exception e) {
				HighScore h = new HighScore();
				h.setPlayer(player);
				h.setScore(score);
				repository.save(h);
			}
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}

	}

	@Override
	public HighScore getHighestScore() throws WormiusGamesExeption {
		try {
			Optional<Integer> opScore = repository.getHighestScore();
			if (!opScore.isPresent())
				return null;
			Optional<HighScore> h = repository.getScoreByscore(opScore.get());
			if (h.isPresent())
				return h.get();
			return null;
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

}
