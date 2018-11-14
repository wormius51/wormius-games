package wormius.games.wormiusgames.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wormius.games.wormiusgames.database.entities.LoginCredencials;
@Repository
public interface LoginCredencialsRepository extends CrudRepository<LoginCredencials, Long> {
	@Query(value = "select * from LOGIN_CREDENCIALS where email = :email and password = :password", nativeQuery = true)
	public Optional<LoginCredencials> getByCredencials(@Param("email") String email,@Param("password") String password);
	@Query(value = "select * from LOGIN_CREDENCIALS where email = :email", nativeQuery = true)
	public Optional<LoginCredencials> getByCredencials(@Param("email") String email);
	
	public Optional<LoginCredencials> findByResetToken(String resetToken);
	
	public boolean existsByEmail(String email);
}
