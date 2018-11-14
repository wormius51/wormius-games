package wormius.games.wormiusgames.helpers;

public enum CardEnum {
	AxeWarrior(0, 3),
	BloodSoldier(1, 3),
	BoneCrasher(2, 2),
	ForestGuardian(3, 1),
	LeafWizard(4, 2),
	Meditate(5,3),
	RiverMaster(6, 1),
	Sapling(7, 3),
	ShapeShifter(8, 2),
	ShieldsMan(9, 2),
	Soldier(10, 3),
	Telepathy(11,3),
	GrailOfFury(12, 2),
	LoneWonderer(13, 2),
	SpearFighter(14, 3),
	DesertGhost(15,3),
	Boxer(16, 3),
	SmokeDemon(17, 1),
	BodySnatcher(18,3),
	HammerWarrior(19, 2),
	BlackWasp(20, 3),
	SatansFlower(21, 3),
	Custommods(22,1),
	DancingCactus(23,2),
	BambooForest(24,1),
	Oasis(25,2),
	Sourcephy(26,2),
	Gatherer(27,1);
	
	

	private int value;
	private int numberOfArrows;

	private CardEnum(int value, int numberOfArrows) {
		this.value = value;
		this.numberOfArrows = numberOfArrows;
	}

	public int getValue() {
		return value;
	}

	public int getNumberOfArrows() {
		return numberOfArrows;
	}
}
