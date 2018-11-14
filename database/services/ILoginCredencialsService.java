package wormius.games.wormiusgames.database.services;

import java.util.Optional;

import wormius.games.wormiusgames.database.entities.LoginCredencials;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface ILoginCredencialsService {
	public void addLoginCredencials(LoginCredencials loginCredencials) throws WormiusGamesExeption;
	public void addLoginCredencials(String displayName,String email, String password) throws WormiusGamesExeption;
	public Optional<LoginCredencials> getLoginCredencials(String email, String password) throws WormiusGamesExeption;
	public Optional<LoginCredencials> findByResetToken(String resetToken);
	public Optional<LoginCredencials> findByEmail(String email);
}
