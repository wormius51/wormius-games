package wormius.games.wormiusgames.helpers;

public class Validator implements IValidator {

	@Override
	public boolean email(String s) {
		if (s == null)
			return false;
		return s.matches("[\\w,\\d]+@[\\w,\\d]+[\\.,\\w]+");
	}

	@Override
	public boolean alphaNumeric(String s) {
		if (s == null)
			return false;
		return s.matches("[\\w,\\d]+");
	}

	@Override
	public boolean alphNumericSpace(String s) {
		if (s == null)
			return false;
		return s.matches("[\\w,\\d,\\s]+");
	}

	@Override
	public boolean closedScope(String s) {
		if (s == null)
			return false;
		int open = s.length() - s.replace("{", "").length();
		int close = s.length() - s.replace("}", "").length();
		
		return open == close;
	}

}
