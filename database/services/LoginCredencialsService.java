package wormius.games.wormiusgames.database.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.LoginCredencials;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.LoginCredencialsRepository;
import wormius.games.wormiusgames.database.repositories.UserRepository;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class LoginCredencialsService implements ILoginCredencialsService {
	@Autowired
	private LoginCredencialsRepository repository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void addLoginCredencials(LoginCredencials loginCredencials) throws WormiusGamesExeption {
		try {
			loginCredencials.setPassword(bCryptPasswordEncoder.encode(loginCredencials.getPassword()));
			repository.save(loginCredencials);
		}catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public void addLoginCredencials(String displayName, String email, String password) throws WormiusGamesExeption {
		
		if (userRepository.existsByName(displayName)) {
			throw new WormiusGamesExeption("An account with this name already exists.");
		}
		if (repository.existsByEmail(email)) {
			throw new WormiusGamesExeption("An account with this email already exists.");
		}
		
		try {
			User user = new User();
			user.setName(displayName);
			userRepository.save(user);
			LoginCredencials log = new LoginCredencials();
			log.setEmail(email);
			log.setPassword(bCryptPasswordEncoder.encode(password));
			log.setUser(user);
			repository.save(log);
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public Optional<LoginCredencials> getLoginCredencials(String email, String password) throws WormiusGamesExeption {
		try {
			Optional<LoginCredencials> l = repository.getByCredencials(email);
			if (!l.isPresent())
				return l;
			if (bCryptPasswordEncoder.matches(password, l.get().getPassword())) {
				return l;
			}
			return Optional.empty();
		} catch (Exception e) {
			throw new WormiusGamesExeption();
		}
	}

	@Override
	public Optional<LoginCredencials> findByResetToken(String resetToken) {
		return repository.findByResetToken(resetToken);
	}

	@Override
	public Optional<LoginCredencials> findByEmail(String email) {
		return repository.getByCredencials(email);
	}
}
