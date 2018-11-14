package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;

import wormius.games.wormiusgames.database.entities.CardPack;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.CardPackType;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface ICardPackService {
	public void buyCardPack(User user, CardPackType type) throws WormiusGamesExeption;
	public void createCardPack(User user, CardPackType type) throws WormiusGamesExeption;
	public void createCardPack(User user) throws WormiusGamesExeption;
	public void deleteCardPack(CardPack cardPack) throws WormiusGamesExeption;
	public int[] openCardPack(User user, long cardPackId) throws WormiusGamesExeption;
	public ArrayList<CardPack> getCardPacks(User user) throws WormiusGamesExeption;
	public int[] getAmounts(User user) throws WormiusGamesExeption;
}
