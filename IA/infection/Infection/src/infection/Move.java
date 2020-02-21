package infection;

/**
 * Classe représentant un mouvement de pion. La position de départ, la position
 * d'arrivée sous forme de coordonnées d'entiers. Le type de mouvement est un
 * booléen.
 */
public class Move {

	/** Couple position départ */
	protected int startX;
	protected int startY;

	/** Couple position d'arrivée */
	protected int endX;
	protected int endY;

	/**
	 * Type de mouvement possible du pion :
	 * <ul>
	 * <li>True : dupliquer</li>
	 * <li>false ; déplacement</li>
	 */
	protected boolean type;

	/**
	 * Constructeur avec paramètres, coordonnées du point de départ et du point
	 * d'arrivée. Le type est spécifié également.
	 * 
	 * @param x1 Position x de départ
	 * @param y1 Position y de départ
	 * @param x2 Position x d'arrivée
	 * @param y2 Position y d'arrivée
	 * @param type Type de mouvement
	 */
	public Move(int x1, int y1, int x2, int y2, boolean type) {
		this.startX = x1;
		this.startY = y1;
		this.endX = x2;
		this.endY = y2;
		this.type = type;
	}

	@Override
	public String toString() {
		return "(" + this.startX + "," + this.startY + ");(" + this.endX + "," + this.endY + ") : " + this.type + "\n";
	}
}
