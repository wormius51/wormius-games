package wormius.games.wormiusgames.controllers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

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

import wormius.games.wormiusgames.database.entities.HighScore;
import wormius.games.wormiusgames.database.entities.Level;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.services.IHighScoreService;
import wormius.games.wormiusgames.database.services.ILevelService;
import wormius.games.wormiusgames.database.services.IUserService;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Controller
@RequestMapping("puzzling-good")
public class PuzzlingGood_controller {
	@Autowired
	private IUserService userService;
	@Autowired
	private ILevelService levelService;
	@Autowired
	private IHighScoreService highScoreService;
	@Autowired
	private ServletContext context;
	private HttpSession getSession(String sessionId) {
	    return (HttpSession) context.getAttribute(sessionId);
	}
	
	@RequestMapping("online")
	public String getGameHTML() {
		return "PuzzlingGood.html";
	}
	
	@RequestMapping("getAllLevels")
	@ResponseBody
	public ResponseEntity<ArrayList<Level>> getAllLevels() throws WormiusGamesExeption {
		return new ResponseEntity<ArrayList<Level>>(levelService.getAllLevels(),HttpStatus.OK); 
	}
	@RequestMapping("getLevelByID")
	@ResponseBody
	public ResponseEntity<?> getLevelById(@RequestParam long id) throws WormiusGamesExeption {
		Optional<Level> op = levelService.getLevel(id);
		if (!op.isPresent())
			return new ResponseEntity<String> ("Level not found.",HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<Level> (op.get(),HttpStatus.OK);
	}
	@RequestMapping("getLevelsByCreatorId")
	@ResponseBody
	public ResponseEntity<ArrayList<Level>> getLevelsByCreatorId(@RequestParam long creatorId) throws WormiusGamesExeption {
		return new ResponseEntity<ArrayList<Level>> (levelService.getLevelsByCreatorId(creatorId),HttpStatus.OK);
	}
	@RequestMapping("getLevelsBySearchWord")
	@ResponseBody
	public ResponseEntity<ArrayList<Level>> getLevelsBySearchWord(@RequestParam String searchWord) throws WormiusGamesExeption {
		return new ResponseEntity<ArrayList<Level>> (levelService.getLevelsBySearchWord(searchWord),HttpStatus.OK);
	}
	@RequestMapping("getLevelsSortedByLikes")
	@ResponseBody
	public ResponseEntity<Object[]> getLevelsSortedByLikes(@RequestParam int direction) throws WormiusGamesExeption {
		ArrayList<Level> levels = levelService.getAllLevels();
		Object[] levelArr = levels.stream().sorted((l1,l2)->l1.getLikes()<l2.getLikes() ? direction : -direction).toArray();
		return new ResponseEntity<Object[]>(levelArr, HttpStatus.OK);
	}
	@RequestMapping("getLevelsSortedByPlayes")
	@ResponseBody
	public ResponseEntity<Object[]> getLevelsSortedByPlayes(@RequestParam int direction) throws WormiusGamesExeption {
		ArrayList<Level> levels = levelService.getAllLevels();
		Stream<Level> stream = levels.stream();
		Object[] levelArr = stream.sorted((l1,l2)->l1.getPlayes()<l2.getPlayes() ? direction : -direction).toArray();
		return new ResponseEntity<Object[]>(levelArr, HttpStatus.OK);
	}
	@RequestMapping("getLevelsSortedBySolvePercentage")
	@ResponseBody
	public ResponseEntity<Object[]> getLevelsSortedBySolvePercentage(@RequestParam double solvePercentage) throws WormiusGamesExeption {
		ArrayList<Level> levels = levelService.getAllLevels();
		Object[] levelArr = levels.stream().sorted((l1,l2)->{
			if (l1.getPlayes() == 0 || l2.getPlayes() == 0)
				return 0;
			double s1 = 100*l1.getSolves()/l1.getPlayes();
			double s2 = 100*l2.getSolves()/l2.getPlayes();
			return Math.abs(s1 - solvePercentage) > Math.abs(s2 - solvePercentage) ? 1 : -1;
		}).toArray();
		return new ResponseEntity<Object[]>(levelArr, HttpStatus.OK);
	}
	
	@RequestMapping(value = "submitLevel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> submitLevel(@RequestParam String levelName, @RequestParam String content,@RequestHeader String sessionId) throws WormiusGamesExeption {
		if (getSession(sessionId) == null) {
			return new ResponseEntity<String> ("You must be logged in to submit a level.",
					HttpStatus.UNAUTHORIZED);
		}
		Level level =  new Level();
		User user = (User) getSession(sessionId).getAttribute("user");
		level.setCreator(user);
		level.setName(levelName);
		level.setContent(content);
		levelService.addLevel(level);
		return new ResponseEntity<String> ("Level " + level.getName() + " by " + user.getName() + " was added",HttpStatus.OK);
	}
	@RequestMapping(value = "removeLevel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> removeLevel(@RequestHeader String sessionId,@RequestParam long levelId) {
		try {
		User user = (User) getSession(sessionId).getAttribute("user");
		Level level = levelService.getLevel(levelId).get();
		if (level.getCreator().equals(user)) {
			levelService.removeLevel(level);
			return new ResponseEntity<String> ("Deleted the level",HttpStatus.OK);
		}
		return new ResponseEntity<String> ("You can only remove your levels",HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<String> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "playLevel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> playLevel(@RequestHeader String sessionId,@RequestParam long levelId) {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			boolean success = userService.playLevel(user.getId(), levelId);
			if (success)
				return new ResponseEntity<String> ("added played level",HttpStatus.OK);
			return new ResponseEntity<String> ("allready exists",HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "solveLevel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> solveLevel(@RequestHeader String sessionId,@RequestParam long levelId) {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			boolean success = userService.solveLevel(user.getId(), levelId);
			if (success)
				return new ResponseEntity<String> ("added solved level",HttpStatus.OK);
			return new ResponseEntity<String> ("allready exists",HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "likeLevel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> likeLevel(@RequestHeader String sessionId,@RequestParam long levelId) {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			boolean success = userService.likeLevel(user.getId(), levelId);
			if (success)
				return new ResponseEntity<String> ("added liked level",HttpStatus.OK);
			return new ResponseEntity<String> ("allready exists",HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping("didILike")
	@ResponseBody
	public ResponseEntity<String> didILike(@RequestHeader String sessionId,@RequestParam long levelId) {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			if (userService.didLike(user.getId(), levelId)) {
				return new ResponseEntity<String>("yes",HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("no",HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping("getHighestScore")
	@ResponseBody
	public ResponseEntity<?> getHighestScore() throws WormiusGamesExeption {
		HighScore h = highScoreService.getHighestScore();
		if (h == null)
			return new ResponseEntity<String> ("No score submitted yet", HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<HighScore> (h,HttpStatus.OK);
	}
	
	@RequestMapping(value = "submitHighScore", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> submitHighScore(@RequestHeader String sessionId,@RequestParam int score) {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			highScoreService.saveScore(user, score);
			return new ResponseEntity<String>("Saved the score",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping("getLevelsByParameters")
	@ResponseBody
	public ResponseEntity<ArrayList<Level>> getLevelsByParameters(
			@RequestParam int page,@RequestParam int pageLength,@RequestParam int playesDirection,
			@RequestParam int likesDirection, @RequestParam double solvePrecentage, @RequestParam String searchWord) throws WormiusGamesExeption {
		return new ResponseEntity<ArrayList<Level>> (levelService.getLevelsByParameters(page, pageLength, playesDirection, likesDirection, solvePrecentage, searchWord),HttpStatus.OK);
	}
}
