package wormius.games.wormiusgames.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wormius.games.wormiusgames.database.entities.HighScore;

public interface HighScoreRepository extends CrudRepository<HighScore, Long> {
	@Query(value = "select max(score) from HIGHSCORES", nativeQuery = true)
	public Optional<Integer> getHighestScore();
	@Query(value = "select * from HIGHSCORES where player_id = :playerId", nativeQuery = true)
	public Optional<HighScore> getScoreByPlayerId(@Param("playerId") long playerId);
	@Query(value = "select * from HIGHSCORES where score = :score", nativeQuery = true)
	public Optional<HighScore> getScoreByscore(@Param("score") int score);
}
