package wormius.games.wormiusgames.database.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.User;

public interface BCstatsRepository extends CrudRepository<BCstats, Long> {

	public Optional<BCstats> findByOwner(User owner);
}
