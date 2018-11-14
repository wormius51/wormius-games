package wormius.games.wormiusgames.database.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import wormius.games.wormiusgames.database.entities.CardPack;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.CardPackType;

public interface CardPackRepository extends CrudRepository<CardPack, Long> {
	public ArrayList<CardPack> findByOwner(User owner);
	
	public int countByOwner(User owner);
	public int countByOwnerAndType(User owner, CardPackType type);
}
