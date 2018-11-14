package wormius.games.wormiusgames.helpers.unitTesters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public class EmptyCollectionTest {

	public static void main(String[] args) {
		CardsCollection cc;
		cc = new CardsCollection();
		//cc = null;
		cc.setContent("a");
		try {
			cc = getCardsCollection(cc);
			System.out.println(cc);
		} catch (WormiusGamesExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	static public CardsCollection getCardsCollection(CardsCollection cc) throws WormiusGamesExeption {
		if (cc != null) {
			if (cc.getContent().matches(".*[A-Za-z].*")) {
				cc.setContent(numberizeCollections(cc.getContent()));
			}
			if (!cc.getContent().matches("((\\d+)#(\\d+) )+(\\d+)#(\\d+) ?")) {
				cc.setContent(getStarterCollection().getContent());
			}
			return cc;
		}

		User user = new User();
		user.setName("KindoBreak");
		return createCardsCollection(user);
	}
	
	static public CardsCollection createCardsCollection(User user) throws WormiusGamesExeption {
		CardsCollection cc = getStarterCollection();
		cc.setOwner(user);
		return cc;
	}
	
	static private String numberizeCollections(String content) throws WormiusGamesExeption {
		String s = content;
		String regex = "([A-Za-z]+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);

		while (m.find()) {
			try  {
			String cs = m.group();
			CardEnum ce = CardEnum.valueOf(cs);
			String replaceRegex = "\\b" + cs + "\\b";
			s = s.replaceAll(replaceRegex, ce.getValue() + "");
			} catch (IllegalArgumentException e) {
				throw new WormiusGamesExeption();
			}
		}

		return s;
	}
	
	static public CardsCollection getStarterCollection() {
		CardEnum[] cards = new CardEnum[] { CardEnum.Soldier, CardEnum.AxeWarrior, CardEnum.ShapeShifter,
				CardEnum.BloodSoldier, CardEnum.ShieldsMan };

		int[] amounts = new int[] { 6, 6, 4, 4, 3 };

		CardsCollection cc = new CardsCollection();
		cc.setContent(enumsToString(cards, amounts));
		return cc;
	}
	
	static private String enumsToString(CardEnum[] cards, int[] amounts) {
		String s = "";
		for (int i = 0; i < cards.length; i++) {
			if (i > 0)
				s += " ";
			s += cards[i].getValue() + "#" + amounts[i];
		}

		return s;
	}
}
