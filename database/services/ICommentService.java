package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;

import wormius.games.wormiusgames.database.entities.Comment;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface ICommentService {
	public void createComment(Comment comment) throws WormiusGamesExeption;
	public void createComment(User user, String content) throws WormiusGamesExeption;
	public void createComment(String content) throws WormiusGamesExeption;
	public ArrayList<Comment> getAllComments() throws WormiusGamesExeption;
	public ArrayList<Comment> getComments(int max) throws WormiusGamesExeption;
	public void deleteComment(long id) throws WormiusGamesExeption;
}
