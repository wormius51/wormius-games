package wormius.games.wormiusgames.helpers.unitTesters;

import java.util.ArrayList;

public class RegexTester {

	public static void main(String[] args) {
		String regex = args[0];
		int maxLength = Integer.parseInt(args[1]);
		ArrayList<String> matches = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			String testString = getStringByIndex(i, maxLength);
			if (testString.matches(regex)) {
				matches.add(testString);
			}
		}
		for (String match : matches) {
			System.out.println("match: " + match);
		}
	}
	
	static private String getStringByIndex(int i, int maxLength) {
		ArrayList<Character> chars = new ArrayList<>();
		while (chars.size() < maxLength && i > 0) {
			if (i < maxLength) {
				char c = (char)0;
				chars.add(c);
				i--;
			} else {
				char c = (char)i;
				chars.add(c);
				i--;
			}
		}
		String s = "";
		for (char c : chars) {
			
		}
		System.out.println("string " + i + " " + s);
		return s;
	}
}
