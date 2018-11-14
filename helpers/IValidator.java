package wormius.games.wormiusgames.helpers;

public interface IValidator {
	public boolean email(String s);
	public boolean alphaNumeric(String s);
	public boolean alphNumericSpace(String s);
	public boolean closedScope(String s);
}
