package wormius.games.wormiusgames.helpers;

public class WormiusGamesExeption extends Exception {
	
	private static final long serialVersionUID = 5557919431511718022L;

	public WormiusGamesExeption(String message) {
		super(message);
	}
	
	public WormiusGamesExeption() {
		super("Could not execut request.");
	}
}
