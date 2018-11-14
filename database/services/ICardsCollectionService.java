package wormius.games.wormiusgames.database.services;

import java.util.ArrayList;

import wormius.games.wormiusgames.database.entities.CardsCollection;
import wormius.games.wormiusgames.database.entities.User;
import wormius.games.wormiusgames.helpers.CardEnum;
import wormius.games.wormiusgames.helpers.WormiusGamesExeption;

public interface ICardsCollectionService {
	public CardsCollection getCardsCollection(User owner) throws WormiusGamesExeption;
	public void createCardsCollection(CardsCollection cardsCollection) throws WormiusGamesExeption;
	public CardsCollection createCardsCollection(User user) throws WormiusGamesExeption;
	public void updateCardsCollection(CardsCollection cardsCollection) throws WormiusGamesExeption;
	public CardsCollection addCard(User owner, CardEnum card, int amount)  throws WormiusGamesExeption;
	public CardsCollection addCard(User owner, CardEnum card)  throws WormiusGamesExeption;
	public CardsCollection addCards(User owner, ArrayList<CardEnum> cards) throws WormiusGamesExeption;
	public CardsCollection addRandomCard(User owner)  throws WormiusGamesExeption;
	public CardsCollection getStarterCollection();
}
