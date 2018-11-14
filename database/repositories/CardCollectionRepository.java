package wormius.games.wormiusgames.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.User;

public interface CardCollectionRepository extends CrudRepository<CardsCollection, Long> {
	
	@Query("select c from CardsCollection c where c.owner = :owner")
	public Optional<CardsCollection> getCardsCollection(@Param("owner") User owner);
}
