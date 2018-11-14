package wormius.games.wormiusgames.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wormius.games.wormiusgames.database.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public boolean existsByName(String name);
}
