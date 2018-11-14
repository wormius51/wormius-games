package wormius.games.wormiusgames.helpers;

import java.util.ArrayList;

import wormius.games.wormiusgames.database.entities.Level;

public class LevelArray {
	private ArrayList<Level> levels;
	
	public LevelArray() {
		
	}
	
	public LevelArray(ArrayList<Level> _levels) {
		levels = _levels;
	}

	public ArrayList<Level> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<Level> levels) {
		this.levels = levels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((levels == null) ? 0 : levels.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LevelArray other = (LevelArray) obj;
		if (levels == null) {
			if (other.levels != null)
				return false;
		} else if (!levels.equals(other.levels))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LevelArray [levels=" + levels + "]";
	}
}
