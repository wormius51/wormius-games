package wormius.games.wormiusgames.controllers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import wormius.games.wormiusgames.database.entities.LoginCredencials;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.services.IEmailService;
import wormius.games.wormiusgames.database.services.ILoginCredencialsService;
import wormius.games.wormiusgames.database.services.IUserService;
import wormius.games.wormiusgames.helpers.IPageMaker;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@RestController
@RequestMapping("user")
public class User_controller {
	@Autowired
	private ILoginCredencialsService loginService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ServletContext context;
	@Autowired
	private IPageMaker pageMaker;
	@Autowired
	private IEmailService emailService;

	private HttpSession getSession(boolean create) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(create);
		if (create)
			context.setAttribute(session.getId(), session);
		return session;
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestParam String displayName, @RequestParam String email,
			@RequestParam String password) {
		try {
			loginService.addLoginCredencials(displayName, email, password);
		} catch (WormiusGamesExeption e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("registered", HttpStatus.OK);
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password)
			throws WormiusGamesExeption {
		try {
			Optional<LoginCredencials> log = loginService.getLoginCredencials(email, password);
			if (log.isPresent()) {
				HttpSession session = getSession(true);
				session.setAttribute("user", log.get().getUser());
				MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
				headers.add("sessionId", session.getId());
				ResponseEntity<User> r = new ResponseEntity<User>(log.get().getUser(), headers, HttpStatus.OK);
				return r;
			}

			return new ResponseEntity<String>("User with the given credential was not found.", HttpStatus.NOT_FOUND);
		} catch (WormiusGamesExeption w) {
			return new ResponseEntity<String>("User with the given credential was not found.", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "logout")
	public ResponseEntity<String> logout(@RequestHeader String sessionId) {
		try {
			HttpSession session = (HttpSession) context.getAttribute(sessionId);
			session.invalidate();
			context.removeAttribute(sessionId);
			return new ResponseEntity<String>("logout", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "getFollowingList")
	public ResponseEntity<?> getFollowingList(@RequestHeader String sessionId) {
		try {
			HttpSession session = (HttpSession) context.getAttribute(sessionId);
			User user = (User) session.getAttribute("user");
			return new ResponseEntity<ArrayList<User>>(userService.getFollowingList(user.getId()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "followById")
	public ResponseEntity<String> followById(@RequestHeader String sessionId, @RequestParam long followedId) {
		try {
			HttpSession session = (HttpSession) context.getAttribute(sessionId);
			User user = (User) session.getAttribute("user");
			if (user.getId() == followedId)
				return new ResponseEntity<String>("Can't follow yourself, sorry.", HttpStatus.BAD_REQUEST);
			boolean success = userService.follow(user.getId(), followedId);
			if (success)
				return new ResponseEntity<String>("Followed " + userService.getUserById(followedId).get().getName(),
						HttpStatus.OK);
			return new ResponseEntity<String>("allready exist", HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<String> forgotPassword(@RequestParam String email) throws WormiusGamesExeption {
		Optional<LoginCredencials> op = loginService.findByEmail(email);
		if (!op.isPresent()) {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}
		
		LoginCredencials log = op.get();
		log.setResetToken(UUID.randomUUID().toString());
		loginService.addLoginCredencials(log);
		
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
		String link = "https://wormius-games.herokuapp.com/user/resetPassword?resetToken=" + log.getResetToken();
		
		passwordResetEmail.setFrom("support@wormiusgames.com");
		passwordResetEmail.setTo(email);
		passwordResetEmail.setSubject("Password Reset Request");
		passwordResetEmail.setText("To reset your password click the link below:\n" + link);
		emailService.sendMail(passwordResetEmail);
		return new ResponseEntity<String>("Reset token sent to your email",HttpStatus.OK);
	}
	
	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@RequestParam String resetToken, @RequestParam String password) throws WormiusGamesExeption {
		System.out.println("resetPassword");
		Optional<LoginCredencials> op = loginService.findByResetToken(resetToken);
		if (!op.isPresent()) {
			System.out.println("invalid");
			return new ResponseEntity<String>("Invalid token", HttpStatus.NOT_FOUND);
		}
		
		LoginCredencials log = op.get();
		log.setPassword(password);
		log.setResetToken(null);
		loginService.addLoginCredencials(log);
		System.out.println("changed password");
		return new ResponseEntity<String>("password updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public ResponseEntity<String> displayResetPasswordPage(@RequestParam String resetToken) {
		return new ResponseEntity<String>(pageMaker.resetPasswordPage(resetToken), HttpStatus.OK);
	}
}
