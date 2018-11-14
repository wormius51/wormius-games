package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.DeckRepository;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class DeckService implements IDeckService {

	@Autowired
	private DeckRepository deckRepository;
	@Autowired
	private ICardsCollectionService cardsCollectionService;
	
	@Override
	public Deck getDeck(long id) {
		Optional<Deck> deck = deckRepository.findById(id);
		return deck.isPresent() ? deck.get() : null;
	}

	@Override
	public ArrayList<Deck> getAllDecks(User user) {
		ArrayList<Deck> decks = deckRepository.getDecksByUser(user);
		return decks;
	}

	@Override
	public ArrayList<Deck> getAllDecks(User user, String searchWord) {
		return deckRepository.getDecksByUser(user, searchWord);
	}

	@Override
	public void saveDeck(Deck deck) throws WormiusGamesExeption {
		if (!checkValidDeck(deck))
			throw new WormiusGamesExeption("the contents of this deck is not in a valid format");
		
		if (checkCardsAvailable(deck)) {
			deckRepository.save(deck);
		} else {
			throw new WormiusGamesExeption("do not have the cards");
		}
	}

	@Override
	public void deleteDeck(Deck deck) {
		deckRepository.delete(deck);
	}

	@Override
	public Deck findDeckByNameAndCreator(String name, User creator) {
		Optional<Deck> op = deckRepository.findByNameAndCreator(name, creator);
		if (op.isPresent())
			return op.get();
		return null;
	}
	
	private boolean checkValidDeck(Deck deck) {
		String sdeck = deck.getContent();
		if (!sdeck.matches("((\\d+)#(\\d,){0,2}\\d ){19}(\\d+)#(\\d,){0,2}\\d ?"))
			return false;
		Matcher deckMatcher = Pattern.compile("(\\d+)#(\\d,){0,2}\\d").matcher(sdeck);
		while (deckMatcher.find()) {
			int cardNum = Integer.parseInt(deckMatcher.group(1));
			CardEnum card = CardEnum.values()[cardNum];
			String whole = deckMatcher.group();
			if (!whole.matches("(\\d+)#(\\d,){" + (card.getNumberOfArrows() - 1) + "}\\d"))
				return false;
		}
		return true;
	}
	
	private boolean checkCardsAvailable(Deck deck) throws WormiusGamesExeption {
		CardsCollection cc = cardsCollectionService.getCardsCollection(deck.getCreator());
		if (cc == null)
			return false;
		
		String sdeck = deck.getContent();
		String scollection = cc.getContent();
		
		Matcher deckMatcher = Pattern.compile("(\\d+)#(\\d,){0,2}\\d").matcher(sdeck);
		ArrayList<String> cardsChecked = new ArrayList<>();
		while (deckMatcher.find()) {
			String cardNum = deckMatcher.group(1);
			if (cardsChecked.contains(cardNum))
				continue;
			cardsChecked.add(cardNum);
			Matcher innerDeckMatcher = Pattern.compile("\\b" + cardNum + "#").matcher(sdeck);
			int timesUsed = 0;
			while (innerDeckMatcher.find()) {
				timesUsed++;
			}
			Matcher collectionMatcher = Pattern.compile(cardNum + "#" + "(\\d+)").matcher(scollection);
			if (!collectionMatcher.find()) {
				return false;
			} else {
				int cardStock = Integer.parseInt(collectionMatcher.group(1));
				if (timesUsed > cardStock)
					return false;
			}
		}
		return true;
	}
	
	public Deck createStarterDeck(User user) throws WormiusGamesExeption {
		String content = "0#0,1,2 10#0,1,2 10#0,1,2 8#1,2 9#1,2 1#0,1,2 0#0,1,2 8#0,1 0#0,1,2 1#0,1,2 10#0,1,2 10#0,1,2 1#0,1,2 9#1,2 1#0,1,2 0#0,1,2 10#0,1,2 10#0,1,2 0#0,1,2 8#0,1";
		Deck deck = new Deck();
		deck.setCreator(user);
		deck.setContent(content);
		deck.setName("starter deck");
		saveDeck(deck);
		return deck;
	}
}
