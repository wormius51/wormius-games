package wormius.games.wormiusgames.database.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wormius.games.wormiusgames.database.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
	
	@Query("select c from Comment c order by c.timeStamp desc")
	public ArrayList<Comment> getCommentsOrderByTimeStamp();
	
	@Query(value = "select * from Comments order by time_stamp desc limit :max", nativeQuery = true)
	public ArrayList<Comment> getCommentsOrderByTimeStamp(@Param("max") int max);
}
