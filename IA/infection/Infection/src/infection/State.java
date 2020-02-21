package infection;

import java.util.ArrayList;

/**
 * Classe qui représente un état du jeu avec le placement des pions sur le
 * plateau.
 *
 */
public class State {

	/** Taille de la grille */
	protected int m;
	protected int n;

	/** Entier représentant le joueur qui joue sur le coup actuel */
	protected int currentPlayer;

	/** Plateau du jeu */
	protected int[][] board;

	/**
	 * Constructeur avec paramètres de taille de la grille de jeu Par défaut, c'est
	 * au tour du joueur 1 de jouer
	 * 
	 * @param m Hauteur de la grille
	 * @param n Largeur de la grille
	 */
	public State(int m, int n) {
		this.m = m;
		this.n = n;
		this.board = new int[m][n];
		this.currentPlayer = 1;
	}

	/**
	 * Constructeur qui prend un état en paramètre Effectue la copie conforme du
	 * board de l'état donné
	 * 
	 * @param buffer L'état copié intégralement.
	 */
	public State(State buffer) {
		// Copie de l'actuel state dans un prochain state
		this(buffer.m, buffer.n);
		this.currentPlayer = buffer.currentPlayer;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				this.board[i][j] = buffer.board[i][j];
			}
		}
	}

	/**
	 * Méthode d'affichage d'un état dans la console Les joueurs sont représentés
	 * par des entiers, 0 signifie que c'est une case vide.
	 */
	public void printBoard() {
		System.out.println("State :");
		String rep = "-".repeat(n * 4 - 1);
		System.out.println(rep);
		for (int i = 0; i < this.m; i++) {
			for (int j = 0; j < this.n; j++) {
				System.out.print(this.board[i][j] + " | ");
			}
			System.out.print("\n");
			System.out.println(rep);
		}
		System.out.println();
	}

	/**
	 * Evaluation du score d'un joueur donné Le score est le nombre de pions détenus
	 * par le joueur divisé par le nombre total de pions sur le board.
	 * 
	 * @param joueur Le joueur concerné par l'évaluation de son score
	 * @return L'évaluation du score du joueur donné
	 */
	public float getScore(int joueur) {
		float nbPionsJoueur = 0;
		float nbPionsTotal = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (this.board[i][j] != 0 || this.board[i][j] == joueur) {
					if (this.board[i][j] == joueur) {
						nbPionsJoueur++;
					}
					nbPionsTotal++;
				}
			}
		}
		return nbPionsJoueur / nbPionsTotal;
	}

	/**
	 * Indique si le jeu est terminé, le jeu est terminé dans plusieurs cas :
	 * <ul>
	 * <li>Si un des joueurs n'a plus de pions</li>
	 * <li>Si la grille est complète</li>
	 * <li>Si un des joueurs n'a plus aucun mouvement possible</li>
	 * </ul>
	 * Les tests peuvent être réduis au simple tests si un des deux joueurs n'a plus
	 * de mouvements possible.
	 * 
	 * @return Vrai pour signifier que le jeu est terminé
	 */
	public boolean isFinished() {
		if (this.getMoves(1).isEmpty() || this.getMoves(2).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Méthode qui récupère la liste des mouvements possibles pour un joueur donné
	 * sur le plateu de jeu. Les mouvements possibles sont des sauts sur une ou deux
	 * cases dans les quatres positions cardinales si la case d'arrivé est libre
	 * (sans aucun joueur). Les coups possibles sont représentés par des objects
	 * Move.
	 * 
	 * @param joueur Récupération des mouvements possibles du joueur
	 * @return ArrayList<Move> contenant les déplacements possibles du joueur donné
	 *         en paramètre.ecli
	 */
	public ArrayList<Move> getMoves(int joueur) {
		// Déclaration de la liste qui contient les mouvements possible du joueur donné.
		ArrayList<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < this.m; i++) {
			for (int j = 0; j < this.n; j++) {
				if (this.board[i][j] == joueur) {

					// Gauche une case
					if (j > 0 && this.board[i][j - 1] == 0)
						moves.add(new Move(i, j, i, j - 1, true));
					// Droite une case
					if (j < this.n - 1 && this.board[i][j + 1] == 0)
						moves.add(new Move(i, j, i, j + 1, true));
					// Haut une case
					if (i > 0 && this.board[i - 1][j] == 0)
						moves.add(new Move(i, j, i - 1, j, true));
					// Bas une case
					if (i < this.m - 1 && this.board[i + 1][j] == 0)
						moves.add(new Move(i, j, i + 1, j, true));
					// Gauche deux case
					if (j > 1 && this.board[i][j - 2] == 0)
						moves.add(new Move(i, j, i, j - 2, false));
					// Droite deux case
					if (j < this.n - 2 && this.board[i][j + 2] == 0)
						moves.add(new Move(i, j, i, j + 2, false));
					// Haut deux cases
					if (i > 1 && this.board[i - 2][j] == 0)
						moves.add(new Move(i, j, i - 2, j, false));
					// Bas deux cases
					if (i < this.m - 2 && this.board[i + 2][j] == 0)
						moves.add(new Move(i, j, i + 2, j, false));
				}
			}
		}
		return moves;
	}

	/**
	 * Méthode qui joue un coup (un mouvement) sur le plateau de jeu. Une nouvelle
	 * instance du plateau est renvoyé, l'état précédent n'est pas modifié. Elle
	 * détermine le type de mouvement :
	 * <ul>
	 * <li>Déplacement : pion qui se déplace de deux cases dans les quatres
	 * directions cardinales</li>
	 * <li>Duplication : duplication du pion dans une des quatres positions cardinal
	 * et vole des pions adverses sur la position d'arrivé dans les quatres
	 * positions cardinales situées à une case.</li>
	 * </ul>
	 * 
	 * @param move Le mouvemement joué sur le plateau.
	 * @return Un nouvel état de jeu avec le coup joué.
	 */
	public State play(Move move) {
		int colorPion = this.board[move.startX][move.startY];
		int colorEnnemy = colorPion == 1 ? 2 : 1;
		State newState = new State(this);
		newState.currentPlayer = this.currentPlayer == 1 ? 2 : 1;

		// Le point d'arrivée est mis à la couleur du pion
		newState.board[move.endX][move.endY] = colorPion;

		// Duplication
		if (move.type == true) {
			// A gauche
			if (move.endY > 0 && newState.board[move.endX][move.endY - 1] == colorEnnemy) {
				newState.board[move.endX][move.endY - 1] = colorPion;
			}
			// A droite
			if (move.endY < this.n - 1 && newState.board[move.endX][move.endY + 1] == colorEnnemy) {
				newState.board[move.endX][move.endY + 1] = colorPion;
			}
			// En haut
			if (move.endX > 0 && newState.board[move.endX - 1][move.endY] == colorEnnemy) {
				newState.board[move.endX - 1][move.endY] = colorPion;
			}
			// En bas
			if (move.endX < this.m - 1 && newState.board[move.endX + 1][move.endY] == colorEnnemy) {
				newState.board[move.endX + 1][move.endY] = colorPion;
			}
		} else {
			// On enlève la couleur du point de départ si c'est un déplacement, on laisse si
			// duplication
			newState.board[move.startX][move.startY] = 0;
		}
		return newState;
	}

	/**
	 * Place les deux joueurs sur le plateau de départ dans les extrémités :
	 * <ul>
	 * <li>En haut à gauche</li>
	 * <li>En bas à droite</li>
	 * </ul>
	 */
	public void initializeBoard() {
		this.board[0][0] = 1;
		this.board[this.m - 1][this.n - 1] = 2;
	}

	/**
	 * Retourne le joueur du coup actuel
	 * 
	 * @return le joueur du coup actuel
	 */
	public int getCurrentPlayer() {
		return this.currentPlayer;
	}

}
