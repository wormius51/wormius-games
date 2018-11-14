package wormius.games.wormiusgames.helpers;

public enum CardPackType {
	Common(0,3,30),
	Rare(1,7,60),
	Epic(2,20,120),
	legendary(3,50,240);
	
	private int value;
	private int size;
	private int price;
	private CardPackType(int value ,int size, int price) {
		this.value = value;
		this.size = size;
		this.price = price;
	}
	public int getSize() {
		return size;
	}
	public int getValue() {
		return value;
	}
	public int getPrice() {
		return price;
	}
}
