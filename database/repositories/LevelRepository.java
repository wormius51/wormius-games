package wormius.games.wormiusgames.database.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wormius.games.wormiusgames.database.entities.Level;

@Repository
public interface LevelRepository extends CrudRepository<Level, Long> {
	@Query(value = "select * from LEVELS where creator_id = :id", nativeQuery = true)
	public ArrayList<Level> getByCreatorId(@Param("id") long id);
	@Query(value = "select l from Level l where lower(l.name) like concat('%', :searchWord,'%')")
	public ArrayList<Level> getBySearchWord(@Param("searchWord") String searchWord);
	
	@Query(nativeQuery = true, value =
			"select * from LEVELS "
			+ "limit :pageLength offset :pageOffset")
	public ArrayList<Level> getLevelsByParameters(
			@Param("pageLength") int pageLength,
			@Param("pageOffset") int pageOffset);
	
	@Query(nativeQuery = true, value =
			"select * from LEVELS "
			+ "where lower(name) like lower(concat('%', :searchWord,'%')) "
			+ "order by (playes * :playesDirection) "
			+ "+ (likes * :likesDirection) "
			+ "+ (-1 * abs(solves / (playes + 1) - :solvePrecentage)) desc "
			+ "limit :pageLength offset :pageOffset")
	public ArrayList<Level> getLevelsByParameters(
			@Param("pageLength") int pageLength,
			@Param("pageOffset") int pageOffset,
			@Param("playesDirection") int playesDirection,
			@Param("likesDirection") int likesDirection,
			@Param("solvePrecentage") double solvePrecentage,
			@Param("searchWord") String searchWord);
	
	@Query(nativeQuery = true, value =
			"select * from LEVELS "
			+ "order by (playes * :playesDirection) "
			+ "+ (likes * :likesDirection) "
			+ "+ (-1 * abs(solves / (playes + 1) - :solvePrecentage)) desc "
			+ "limit :pageLength offset :pageOffset")
	public ArrayList<Level> getLevelsByParameters(
			@Param("pageLength") int pageLength,
			@Param("pageOffset") int pageOffset,
			@Param("playesDirection") int playesDirection,
			@Param("likesDirection") int likesDirection,
			@Param("solvePrecentage") double solvePrecentage);
	
	@Query(nativeQuery = true, value =
			"select * from LEVELS "
			+ "order by (playes * :playesDirection) "
			+ "+ (likes * :likesDirection) desc "
			+ "limit :pageLength offset :pageOffset")
	public ArrayList<Level> getLevelsByParameters(
			@Param("pageLength") int pageLength,
			@Param("pageOffset") int pageOffset,
			@Param("playesDirection") int playesDirection,
			@Param("likesDirection") int likesDirection);
	
	@Query(nativeQuery = true, value =
			"select * from LEVELS "
			+ "where lower(name) like lower(concat('%', :searchWord,'%')) "
			+ "order by (playes * :playesDirection) "
			+ "+ (likes * :likesDirection) desc "
			+ "limit :pageLength offset :pageOffset")
	public ArrayList<Level> getLevelsByParameters(
			@Param("pageLength") int pageLength,
			@Param("pageOffset") int pageOffset,
			@Param("playesDirection") int playesDirection,
			@Param("likesDirection") int likesDirection,
			@Param("searchWord") String searchWord);
}
