package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.CardCollectionRepository;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class CardsCollectionService implements ICardsCollectionService {

	@Autowired
	private CardCollectionRepository repository;
	@Autowired
	private IBCstatsService bCstatsService;

	@Override
	public CardsCollection getCardsCollection(User owner) throws WormiusGamesExeption {
		try {
			Optional<CardsCollection> o = repository.getCardsCollection(owner);
			if (o.isPresent()) {
				try {
					bCstatsService.createBCstats(owner);
				} catch (WormiusGamesExeption e) {

				}
				CardsCollection cc = o.get();
				if (cc.getContent().matches(".*[A-Za-z].*")) {
					cc.setContent(numberizeCollections(cc.getContent()));
				}
				if (!cc.getContent().matches("((\\d+)#(\\d+) )+(\\d+)#(\\d+) ?")) {
					cc.setContent(getStarterCollection().getContent());
				}
				return cc;
			}

			return createCardsCollection(owner);
		} catch (Exception e) {
			CardsCollection starter = getStarterCollection();
			starter.setOwner(owner);
			return starter;
		}
	}

	@Override
	public void createCardsCollection(CardsCollection cardsCollection) throws WormiusGamesExeption {
		Optional<CardsCollection> o = repository.getCardsCollection(cardsCollection.getOwner());
		if (o.isPresent()) {
			throw new WormiusGamesExeption("This user allready has a cards collection.");
		} else {
			try {
				repository.save(cardsCollection);
				bCstatsService.createBCstats(cardsCollection.getOwner());
			} catch (Exception e) {
				throw new WormiusGamesExeption();
			}
		}
	}

	@Override
	public void updateCardsCollection(CardsCollection cardsCollection) throws WormiusGamesExeption {
		try {
			repository.save(cardsCollection);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	private String numberizeCollections(String content) {
		String s = content;
		String regex = "([A-Za-z]+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String cs = m.group();
			CardEnum ce = CardEnum.valueOf(cs);
			String replaceRegex = "\\b" + cs + "\\b";
			s = s.replaceAll(replaceRegex, ce.getValue() + "");
		}

		return s;
	}

	@Override
	public CardsCollection addCard(User owner, CardEnum card, int amount) throws WormiusGamesExeption {
		CardsCollection cc = getCardsCollection(owner);
		if (cc == null) {
			cc = new CardsCollection();
			cc.setOwner(owner);
			cc.setContent(card.getValue() + "#" + 1);
		} else {
			addCard(cc, card, amount);
		}

		return cc;
	}

	private CardsCollection addCard(CardsCollection cc, CardEnum card, int amount) throws WormiusGamesExeption {
		String content = cc.getContent();
		if (content.matches(".*[A-Za-z].*")) {
			content = numberizeCollections(content);
		}
		String regex = card.getValue() + "#(\\d+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {
			String a = m.group(1);
			int currentAmount = Integer.parseInt(a);
			currentAmount += amount;
			content = m.replaceFirst(card.getValue() + "#" + currentAmount);
			cc.setContent(content);
		} else {
			if (content != "") {
				cc.setContent(content + " ");
			}
			cc.setContent(content + " " + card.getValue() + "#1");
		}

		try {
			repository.save(cc);
		} catch (Exception e) {
			throw new WormiusGamesExeption("faild to save the collection.");
		}

		return cc;
	}

	@Override
	public CardsCollection addCard(User owner, CardEnum card) throws WormiusGamesExeption {
		return addCard(owner, card, 1);
	}

	@Override
	public CardsCollection addCards(User owner, ArrayList<CardEnum> cards) throws WormiusGamesExeption {
		CardsCollection cc = getCardsCollection(owner);
		for (CardEnum card : cards) {
			cc = addCard(cc, card, 1);
		}
		return cc;
	}

	@Override
	public CardsCollection addRandomCard(User owner) throws WormiusGamesExeption {
		Random rand = new Random();
		int r = rand.nextInt(CardEnum.values().length);
		CardEnum card = CardEnum.values()[r];
		return addCard(owner, card);
	}

	private String enumsToString(CardEnum[] cards, int[] amounts) {
		String s = "";
		for (int i = 0; i < cards.length; i++) {
			if (i > 0)
				s += " ";
			s += cards[i].getValue() + "#" + amounts[i];
		}

		return s;
	}

	@Override
	public CardsCollection getStarterCollection() {
		CardEnum[] cards = new CardEnum[] { CardEnum.Soldier, CardEnum.AxeWarrior, CardEnum.ShapeShifter,
				CardEnum.BloodSoldier, CardEnum.ShieldsMan };

		int[] amounts = new int[] { 6, 6, 4, 4, 3 };

		CardsCollection cc = new CardsCollection();
		cc.setContent(enumsToString(cards, amounts));
		return cc;
	}

	@Override
	public CardsCollection createCardsCollection(User user) throws WormiusGamesExeption {
		CardsCollection cc = getStarterCollection();
		cc.setOwner(user);
		createCardsCollection(cc);
		return cc;
	}

}
