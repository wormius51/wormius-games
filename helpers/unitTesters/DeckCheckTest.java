package wormius.games.wormiusgames.helpers.unitTesters;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wormius.games.wormiusgames.helpers.CardEnum;

public class DeckCheckTest {

	public static void main(String[] args) {

		String scollection = "10#6 0#6 8#4 1#4 9#3";
		
		String sdeck = "10#0,1,2 9#1,2 10#0,1,2 1#0,1,2 9#1,2 9#1,2 0#0,1,2 1#0,1,2 10#0,1,2 0#0,1,2 8#0,1 10#0,1,2 0#0,1,2 0#0,1,2 1#0,1,2 0#0,1,2 10#0,1,2 0#0,1,2 1#0,1,2 10#0,1,2";

		System.out.println(checkValidDeck(sdeck));
		
		System.out.println(checkCardsAvailable(sdeck, scollection));
		
	}
	
	private static boolean checkValidDeck(String sdeck) {
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
	
	private static boolean checkCardsAvailable(String sdeck, String scollection) {
		
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

}
