package wormius.games.wormiusgames.controllers;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wormius.games.wormiusgames.database.entities.Comment;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.services.ICommentService;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;


@RestController
@RequestMapping("chat")
public class Chat_controller {

	@Autowired
	private ServletContext context;
	@Autowired
	private ICommentService commentService;
	
	private HttpSession getSession(String sessionId) {
		return (HttpSession) context.getAttribute(sessionId);
	}

	private User getUser(String sessionId) throws WormiusGamesExeption {
		try {
			User user = (User) getSession(sessionId).getAttribute("user");
			return user;
		} catch (Exception e) {
			throw new WormiusGamesExeption("session expierd");
		}
	}

	@RequestMapping("getAllComments")
	public ResponseEntity<?> getAllComments() {
		try {
			ArrayList<Comment> comments = commentService.getAllComments();
			return new ResponseEntity<ArrayList<Comment>>(comments, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@RequestMapping("getComments")
	public ResponseEntity<?> getComments(@RequestParam String max) {
		int imax = 0;
		try {
			imax = Integer.parseInt(max);
		} catch (NumberFormatException e) {
			return new ResponseEntity<String>("the variable max should be an integer", HttpStatus.OK);
		}

		try {
			ArrayList<Comment> comments = commentService.getComments(imax);
			return new ResponseEntity<ArrayList<Comment>>(comments, HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "createComment", method = RequestMethod.POST)
	public ResponseEntity<String> createComment(@RequestHeader String sessionId, @RequestParam String content) {
		try {
			User user = getUser(sessionId);
			commentService.createComment(user, content);
			return new ResponseEntity<String>("comment created", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			try {
				commentService.createComment(content);
				return new ResponseEntity<String>("comment created", HttpStatus.OK);
			} catch (WormiusGamesExeption e1) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
			}
		}
	}
	
	@RequestMapping(value = "deleteComment", method = RequestMethod.POST)
	public ResponseEntity<String> deleteComment(@RequestParam String id) {
		long commentId = 0;
		try {
			commentId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return new ResponseEntity<String>("id needs to be a number", HttpStatus.OK);
		}
		
		try {
			commentService.deleteComment(commentId);
			return new ResponseEntity<String>("comment deleted", HttpStatus.OK);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
}
