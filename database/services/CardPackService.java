package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.CardPack;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.CardPackRepository;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.CardPackType;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class CardPackService implements ICardPackService {

	@Autowired
	private CardPackRepository repository;
	
	@Autowired
	private ICardsCollectionService cardsCollectionService;
	
	@Autowired
	private IBCstatsService bCstatsService;
	
	@Override
	public void createCardPack(User user, CardPackType type) throws WormiusGamesExeption {
		try {
			CardPack cp = new CardPack();
			cp.setOwner(user);
			cp.setType(type);
			repository.save(cp);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not create the pack");
		}
	}

	@Override
	public void createCardPack(User user) throws WormiusGamesExeption {
		Random rand = new Random();
		int r = rand.nextInt(CardPackType.values().length);
		CardPackType cpt = CardPackType.values()[r];
		createCardPack(user, cpt);
	}

	@Override
	public void deleteCardPack(CardPack cardPack) throws WormiusGamesExeption {
		try {
			repository.delete(cardPack);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not delete the pack");
		}

	}

	@Override
	public int[] openCardPack(User user, long cardPackId) throws WormiusGamesExeption {
		Optional<CardPack> op = repository.findById(cardPackId);
		if (!op.isPresent()) {
			throw new WormiusGamesExeption("could not find the pack");
		}
		CardPack cardPack = op.get();
		if (!user.equals(cardPack.getOwner())) {
			throw new WormiusGamesExeption("User and card pack owner don't match");
		}
		int[] cardIndexes = new int[cardPack.getType().getSize()];
		Random rand = new Random();
		for (int i = 0; i < cardIndexes.length; i++) {
			int r = rand.nextInt(CardEnum.values().length);
			cardIndexes[i] = r;
			for (CardEnum card : CardEnum.values()) {
				if (card.getValue() == r) {
					cardsCollectionService.addCard(user, card);
					break;
				}
			}
		}
		deleteCardPack(cardPack);
		return cardIndexes;
	}

	@Override
	public ArrayList<CardPack> getCardPacks(User user) throws WormiusGamesExeption {
		try {
			return repository.findByOwner(user);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not get the packs");
		}
	}

	@Override
	public void buyCardPack(User user, CardPackType type) throws WormiusGamesExeption {
		BCstats bs = bCstatsService.getBCstats(user);
		if (bs.getCoins() < type.getPrice()) {
			throw new WormiusGamesExeption("not enothe coins");
		}
		bs.setCoins(bs.getCoins() - type.getPrice());
		createCardPack(user, type);
		bCstatsService.saveBCstats(bs);
	}

	@Override
	public int[] getAmounts(User user) throws WormiusGamesExeption {
		
		CardPackType[] types = CardPackType.values();
		int[] amounts = new int[types.length];
		for (int i = 0; i < types.length; i++) {
			try {
				amounts[i] = repository.countByOwnerAndType(user, types[i]);
			} catch (Exception e) {
				throw new WormiusGamesExeption();
			}
		}
		return amounts;
	}

}
