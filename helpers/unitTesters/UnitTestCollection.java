package wormius.games.wormiusgames.helpers.unitTesters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wormius.games.wormiusgames.helpers.CardEnum;

public class UnitTestCollection {

	public static void main(String[] args) {
		String input = "Soldier#1 Meditate#5 BloodSoldier#4";
		
		String output = numberizeCollections(input);
		System.out.println(output);
	}

	
	private static String numberizeCollections(String content) {
		String s = content;
		String regex = "([A-Za-z]+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		
		while (m.find()) {
			String cs = m.group();
			CardEnum ce = CardEnum.valueOf(cs);
			String replaceRegex = "\\b" + cs + "\\b";
			System.out.println(replaceRegex);
			s = s.replaceAll(replaceRegex, ce.getValue() + "");
		}
		
		return s;
	}
}
