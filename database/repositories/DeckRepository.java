package wormius.games.wormiusgames.database.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.User;

public interface DeckRepository extends CrudRepository<Deck, Long> {
	
	@Query("select d from Deck d where d.creator = :user")
	public ArrayList<Deck> getDecksByUser(@Param("user") User user);
	
	@Query("select d from Deck d where d.creator = :user and d.name like :searchWord")
	public ArrayList<Deck> getDecksByUser(@Param("user") User user, @Param("searchWord") String searchWord);
	@Query("select d from Deck d where d.creator = :creator and d.name = :name")
	public Optional<Deck> findByNameAndCreator(@Param("name") String name, @Param("creator") User creator);
}
