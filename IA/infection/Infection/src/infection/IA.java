package infection;

/**
 * Classe qui représente l'intelligence artificielle associée à un joueur Le
 * raisonnement se fait avec l'algorithme minMax. Le but est de trouver le
 * meilleur coup à jouer en fonction des suivants.
 */
public class IA {

	/**
	 * Définition du joueur qui raisonne, celui pour qui on doit avoir le score
	 * maximal.
	 */
	protected int joueurRaisonne;

	/**
	 * Booléan pour utilisation de l'élégage alphabeta
	 */
	protected boolean cut;

	/**
	 * Nombre d'appels de MinMax ou de Alphabeta
	 */
	public static long cptAppels = 0;

	/**
	 * Définition de constantes pour les algorithmes qui nécessitent des bornes
	 * inatteignables.
	 */
	private static final int MINUS_INFINITE = -10;
	private static final int PLUS_INFINITE = 10;

	/**
	 * Constructeur avec paramètre, on associe un joueur à l'IA
	 * 
	 * @param joueurRaisonne Le joueur auquel on s'intérresse
	 */
	public IA(int joueurRaisonne, boolean cut) {
		this.joueurRaisonne = joueurRaisonne;
		this.cut = cut;
	}

	/**
	 * Méthode qui permet de trouver le noeud qui donne le plus de chance de gagner.
	 * On parcours tout le graphe des possibilités en appelant au niveau de
	 * profondeur 2 l'algorithme minMax. Le meilleur coup est renvoyé.
	 * 
	 * @param s          L'état du plateau courant
	 * @param profondeur La profondeur de recherche de l'IA
	 * @return Le Move qui donne le plus de chance de gagner la partie
	 */
	public Move decide(State s, int profondeur) {
		double b = MINUS_INFINITE;
		double m = 0;
		State snew = null;
		Move c = new Move(0, 0, 0, 0, false);
		for (Move mv : s.getMoves(s.getCurrentPlayer())) {
			snew = s.play(mv);
			if (this.cut) {
				m = alphabeta(snew, MINUS_INFINITE, PLUS_INFINITE, profondeur - 1);
			} else {
				m = miniMax(snew, profondeur - 1);
			}
			if (b < m) {
				b = m;
				c = mv;
			}
		}
		return c;
	}

	/**
	 * Algorithme minMax qui parcours l'ensemble des possibilités de l'arbre de
	 * recherche et récupère la valeur qui donne le plus de chance de gagner la
	 * partie pour le joueur qui raisonne.
	 * 
	 * @param s          L'état actuel du plateau
	 * @param profondeur La profondeur de recherche de l'algorithme
	 * @return La meilleur valeur de la fonction d'évaluation
	 */
	private double miniMax(State s, int profondeur) {
		IA.cptAppels++;
		State snew = null;
		double b, m;
		// Si le jeu est terminé, on ne peut pas descendre plus bas dans la profondeur
		if (profondeur == 0 || s.isFinished()) {
			// Score du joueur qui raisonne
			return s.getScore(this.joueurRaisonne);
		} else {
			if (s.getCurrentPlayer() == this.joueurRaisonne) {
				b = MINUS_INFINITE;
				for (Move mv : s.getMoves(s.getCurrentPlayer())) {
					snew = s.play(mv);
					m = miniMax(snew, profondeur - 1);
					b = b < m ? m : b;
				}
			} else {
				b = PLUS_INFINITE;
				for (Move mv : s.getMoves(s.getCurrentPlayer())) {
					snew = s.play(mv);
					m = miniMax(snew, profondeur - 1);
					b = b > m ? m : b;
				}
			}
			return b;
		}
	}

	/**
	 * Algorithme Alphabeta, version optimisée de MinMax avec élégage des branches
	 * qui ne rapportent pas de points au joueur. L'arbre des possibilités est
	 * réduit.
	 * 
	 * @param s          Le plataeu de jeu courant
	 * @param alpha      Valeur minimun pour le joueur qui raisonne
	 * @param beta       Valeur maximum pour le joueur qui raisonne
	 * @param profondeur Profondeur de recherche de l'algorithme
	 * @return La valeur du noeud qui apporte le plus au joueur
	 */
	private double alphabeta(State s, double alpha, double beta, int profondeur) {
		IA.cptAppels++;
		double m = 0;
		if (profondeur == 0 || s.isFinished()) {
			return s.getScore(this.joueurRaisonne);
		} else {
			if (s.currentPlayer == this.joueurRaisonne) {
				for (Move mv : s.getMoves(s.currentPlayer)) {
					m = this.alphabeta(s.play(mv), alpha, beta, profondeur - 1);
					alpha = alpha > m ? alpha : m;
					if (alpha >= beta) {
						return alpha;
					}
				}
				return alpha;
			} else {
				for (Move mv : s.getMoves(s.getCurrentPlayer())) {
					m = this.alphabeta(s.play(mv), alpha, beta, profondeur - 1);
					beta = beta < m ? beta : m;
					if (alpha >= beta) {
						return beta;
					}
				}
				return beta;
			}
		}
	}
}
