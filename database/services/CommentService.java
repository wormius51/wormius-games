package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wormius.games.wormiusgames.database.entities.Comment;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.database.repositories.CommentRepository;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

@Service
public class CommentService implements ICommentService {

	@Autowired
	CommentRepository repository;
	
	@Override
	public void createComment(Comment comment) throws WormiusGamesExeption {
		
		if (comment.getContent() == null || comment.getContent().equals("")) {
			throw new WormiusGamesExeption("no empty comments please");
		}
		
		try {
			repository.save(comment);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not submit comment");
		}
	}

	@Override
	public void createComment(User user, String content) throws WormiusGamesExeption {
		Comment c = new Comment();
		c.setContent(content);
		c.setCreator(user);
		createComment(c);
	}

	@Override
	public void createComment(String content) throws WormiusGamesExeption {
		Comment c = new Comment();
		c.setContent(content);
		createComment(c);
	}

	@Override
	public ArrayList<Comment> getAllComments() throws WormiusGamesExeption {
		try {
			return repository.getCommentsOrderByTimeStamp();
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not submit comment");
		}
	}

	@Override
	public ArrayList<Comment> getComments(int max) throws WormiusGamesExeption {
		try {
			return repository.getCommentsOrderByTimeStamp(max);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not get the comments");
		}
	}

	@Override
	public void deleteComment(long id) throws WormiusGamesExeption {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new WormiusGamesExeption("could not delete the comment");
		}
	}
	
}
