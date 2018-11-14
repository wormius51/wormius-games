package wormius.games.wormiusgames.controllers;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wormius.games.wormiusgames.database.entities.BCstats;
import wormius.games.wormiusgames.database.entities.CardPack;
import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.Deck;
import wormius.games.wormiusgames.database.entities.Match;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.services.IBCstatsService;
import wormius.games.wormiusgames.database.services.ICardPackService;
import wormius.games.wormiusgames.database.services.ICardsCollectionService;
import wormius.games.wormiusgames.database.services.IDeckService;
import wormius.games.wormiusgames.database.services.IMatchMaker;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.CardPackType;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Controller
@RequestMapping("bonded-cardice")
public class BondedCardice_controller {

	@Autowired
	private IDeckService deckService;
	@Autowired
	private ICardsCollectionService cardsCollectionService;
	@Autowired
	private ServletContext context;
	@Autowired
	private IMatchMaker matchMaker;
	@Autowired
	private IBCstatsService bCstatsService;
	@Autowired
	private ICardPackService cardPackService;

	private HttpSession getSession(String sessionId) {
		return (HttpSession) context.getAttribute(sessionId);
	}

	private User getUser(String sessionId) throws WormiusGamesExeption {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			return user;
		} catch (Exception e) {
			throw new WormiusGamesExeption("session expierd");
		}
	}
	
	@RequestMapping("online")
	public String getGameHTML() {
		return "bondedCardice.html";
	}

	@RequestMapping("getAllDecks")
	@ResponseBody
	public ResponseEntity<?> getAllDecks(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			ArrayList<Deck> decks = deckService.getAllDecks(user);
			if (decks.isEmpty()) {
				Deck starterDeck = deckService.createStarterDeck(user);
				decks.add(starterDeck);
			}
			return new ResponseEntity<ArrayList<Deck>>(decks, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@RequestMapping("getDecksBySearchWord")
	@ResponseBody
	public ResponseEntity<?> getAllDecks(@RequestHeader String sessionId, @RequestParam String searchWord) {
		try {
			ArrayList<Deck> decks = deckService.getAllDecks(getUser(sessionId), searchWord);
			return new ResponseEntity<ArrayList<Deck>>(decks, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@RequestMapping("getCardsCollection")
	@ResponseBody
	public ResponseEntity<?> getCardsCollection(@RequestHeader String sessionId) {
		try {
			CardsCollection c = cardsCollectionService.getCardsCollection(getUser(sessionId));
			if (c != null) {
				//getSession(sessionId).setAttribute("cardsCollection", c);
				return new ResponseEntity<CardsCollection>(c, HttpStatus.OK);
			}
			return new ResponseEntity<String>("No cards collection", HttpStatus.NOT_FOUND);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "createCardsCollection", method = RequestMethod.POST)
	public ResponseEntity<String> createCardsCollection(@RequestHeader String sessionId) {
		try {
			cardsCollectionService.createCardsCollection(getUser(sessionId));
			return new ResponseEntity<String>("created cards collection", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "saveDeck", method = RequestMethod.POST)
	public ResponseEntity<String> saveDeck(@RequestHeader String sessionId, @RequestParam String name,
			@RequestParam String content) {
		try {
			Deck deck = deckService.findDeckByNameAndCreator(name, getUser(sessionId));
			if (deck == null) {
				deck = new Deck();
				deck.setContent(content);
				deck.setCreator(getUser(sessionId));
				deck.setName(name);
			} else {
				deck.setContent(content);
			}

			deckService.saveDeck(deck);
			return new ResponseEntity<String>("Deck " + name + " saved", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "seekMatch", method = RequestMethod.POST)
	public ResponseEntity<String> seekMatch(@RequestHeader String sessionId, @RequestParam String deckName) {
		try {
			User user = getUser(sessionId);
			Deck deck = deckService.findDeckByNameAndCreator(deckName, user);
			matchMaker.joinQuake(deck);
			return new ResponseEntity<String>("seeking match...", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "stopSeek", method = RequestMethod.POST)
	public ResponseEntity<String> stopSeek(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			matchMaker.leaveQuake(user);
			return new ResponseEntity<String>("left the quake", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getMatch", method = RequestMethod.POST)
	public ResponseEntity<?> checkMatchFound(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			Match match = matchMaker.getMatch(user);
			return new ResponseEntity<Match>(match, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "addMove", method = RequestMethod.POST)
	public ResponseEntity<String> addMove(@RequestHeader String sessionId, @RequestParam String move) {
		try {
			User user = getUser(sessionId);
			Match match = matchMaker.getMatch(user);
			try {
				match.addMove(Integer.parseInt(move));
			} catch (NumberFormatException e) {
				throw new WormiusGamesExeption("invalid move");
			}
			return new ResponseEntity<String>("move added", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "roll", method = RequestMethod.POST)
	public ResponseEntity<?> rollRequest(@RequestHeader String sessionId, @RequestParam int player) {
		try {
			User user = getUser(sessionId);
			Match match = matchMaker.getMatch(user);
			match.roll(player);
			return new ResponseEntity<Match>(match, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@ResponseBody
	@RequestMapping(value = "endMatch", method = RequestMethod.POST)
	public ResponseEntity<String> endMatch(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			boolean cardFound = matchMaker.endMatch(user);
			if (cardFound) {
				Random rand = new Random();
				int r = rand.nextInt(CardEnum.values().length);
				CardEnum card = CardEnum.values()[r];
				cardsCollectionService.addCard(user, card);
				return new ResponseEntity<String>("found " + card, HttpStatus.OK);
			}
			return new ResponseEntity<String>("the match ended", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "giveUp", method = RequestMethod.POST)
	public ResponseEntity<String> giveUp(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			matchMaker.giveUp(user);
			return new ResponseEntity<String>("giving up so fast?", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "ping", method = RequestMethod.POST)
	public ResponseEntity<?> ping(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			Match match = matchMaker.ping(user);
			return new ResponseEntity<Match>(match, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getStats", method = RequestMethod.POST)
	public ResponseEntity<?> getBCstats(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			BCstats b = bCstatsService.getBCstats(user);
			return new ResponseEntity<BCstats>(b, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "setEmote", method = RequestMethod.POST)
	public ResponseEntity<String> setEmote(@RequestHeader String sessionId, @RequestParam String emote) {
		try {
			User user = getUser(sessionId);
			matchMaker.setEmote(user, emote);
			return new ResponseEntity<String>("emote set", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "buyCardPack", method = RequestMethod.POST)
	public ResponseEntity<String> buyCardPack(@RequestHeader String sessionId, @RequestParam String cardPackType) {
		CardPackType type = null;
		try {
			type = CardPackType.valueOf(cardPackType);
		} catch (Exception e) {
			return new ResponseEntity<String>("not a valid card pack type", HttpStatus.OK);
		}
		try {
			User user = getUser(sessionId);
			cardPackService.buyCardPack(user, type);
			return new ResponseEntity<String>("got the pack", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getCardPacks")
	public ResponseEntity<?> getCardPacks(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			ArrayList<CardPack> packs = cardPackService.getCardPacks(user);
			return new ResponseEntity<ArrayList<CardPack>>(packs,HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getCardPacksAmounts")
	public ResponseEntity<?> getCardPacksAmounts(@RequestHeader String sessionId) {
		try {
			User user = getUser(sessionId);
			int[] amounts = cardPackService.getAmounts(user);
			return new ResponseEntity<int []>(amounts,HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "openCardPack", method = RequestMethod.POST)
	public ResponseEntity<?> openCardPack(@RequestHeader String sessionId, @RequestParam String cardPackId) {
		long packId;
		try {
			packId = Long.parseLong(cardPackId);
		} catch (NumberFormatException e) {
			return new ResponseEntity<String>("cardPackId must be a number", HttpStatus.OK);
		}
		try {
			User user = getUser(sessionId);
			int[] cardIndexes = cardPackService.openCardPack(user, packId);
			return new ResponseEntity<int[]>(cardIndexes, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	
}
