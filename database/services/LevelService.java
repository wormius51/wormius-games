package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.Level;
import wormius.games.wormiusgames.database.repositories.LevelRepository;
import wormius.games.wormiusgames.helpers.IValidator;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class LevelService implements ILevelService {
	@Autowired
	private LevelRepository repository;
	@Autowired
	private IValidator validator;
	
	public ArrayList<Level> getAllLevels() throws WormiusGamesExeption {
		try {
			return (ArrayList<Level>) repository.findAll();
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	public Optional<Level> getLevel(long id) throws WormiusGamesExeption {
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public ArrayList<Level> getLevelsByCreatorId(long creatorId) throws WormiusGamesExeption {
		try {
			return repository.getByCreatorId(creatorId);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public ArrayList<Level> getLevelsBySearchWord(String searchWord) throws WormiusGamesExeption {
		if (!validator.closedScope(searchWord))
			throw new WormiusGamesExeption("Invalid value");
		
		try {
			return repository.getBySearchWord(searchWord);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public void addLevel(Level level) throws WormiusGamesExeption {
		try {
			repository.save(level);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public void removeLevel(Level level) throws WormiusGamesExeption {
		try {
			repository.deleteById(level.getId());
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public ArrayList<Level> getLevelsByParameters(int page, int pageLength, int playesDirection, int likesDirection,
			double solvePrecentage, String searchWord) throws WormiusGamesExeption {
		try {
			if (solvePrecentage == -1) {
				if (searchWord == null || searchWord == "") {
					return repository.getLevelsByParameters(pageLength, page * pageLength, playesDirection,
							likesDirection);
				} else {
					return repository.getLevelsByParameters(pageLength, page * pageLength, playesDirection,
							likesDirection, searchWord);
				}
			} else if (searchWord == null || searchWord == "") {
				return repository.getLevelsByParameters(pageLength, page * pageLength, playesDirection, likesDirection,
						solvePrecentage);
			}
			return repository.getLevelsByParameters(pageLength, page * pageLength, playesDirection, likesDirection,
					solvePrecentage, searchWord);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new WormiusGamesExeption();
		}
	}
}
