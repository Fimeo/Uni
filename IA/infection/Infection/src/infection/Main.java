package infection;

import infection.IA;
import infection.Move;
import infection.State;

public class Main {

	public static final int BLANC = 1;
	public static final int NOIR = 2;

	public static void main(String[] args) {
		// Récupération des arguments du lancement de programme
		int hauteur = 0, largeur = 0, avanceBlanc = 0, profondeurIABlanc = 0, profondeurIANoir = 0;
		boolean cut = false;
		int loop = 0;
		if (args.length != 6) {
			System.err.println(
					"Pas assez de parametres : <Longueur> <Largeur> <CoupsBlancsAvance> <ProfondeurRechercheNoir> <ProfondeurRechercheBlanc> <minmax/alphabeta>"
							+ " attendus");
			System.exit(1);
		}
		try {
			hauteur = Integer.parseInt(args[0]);
			largeur = Integer.parseInt(args[1]);
			avanceBlanc = Integer.parseInt(args[2]);
			profondeurIABlanc = Integer.parseInt(args[3]);
			profondeurIANoir = Integer.parseInt(args[4]);
			if (args[5].equals("minmax")) {
				cut = false;
			} else if (args[5].equals("alphabeta")) {
				cut = true;
			} else {
				System.err.println("Type d'algorithme : minmax ou alphabeta");
				System.exit(1);
			}
			if (hauteur <= 0 || largeur <= 0) {
				System.err.println("Parametres incorrects, les dimensions de la grilles doivent etre superieures a 0");
				System.exit(1);
			}
			if (avanceBlanc < 0) {
				System.out.println("Le nombre de coups d'avance du joueur blanc doit etre positif ou nul");
				System.exit(1);
			}
			if (profondeurIABlanc <= 0 || profondeurIANoir <= 0) {
				System.out.println("Parametres incorrects, profondeur recherche doit etre strictement positive");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("Parametres incorrects, des entiers sont attendus");
			System.exit(0);
		}

		// Création du plateau de jeu et placement des joueurs
		State s = new State(hauteur, largeur);
		s.initializeBoard();

		// Affichage du platau de jeu de base
		s.printBoard();

		// Instanciation des IA pour chaque joueur
		IA ia_blanc = new IA(BLANC, cut);
		IA ia_noir = new IA(NOIR, cut);

		// Le joueur 1 (blanc) joue ses n coups d'avance
		while (avanceBlanc > 0) {
			s = s.play(ia_blanc.decide(s, profondeurIABlanc));
			avanceBlanc--;
		}

		int player = 0;
		Move coup = null;
		/*
		 * Le jeu continue tant qu'il n'est pas terminé ou que le nombre maximum de
		 * tours n'est pas atteint (cas d'une partie infinie). A chaque tour, on
		 * récupère le meilleur coup à jouer du joueur courant puis on le joue. Les IA
		 * des deux joueurs ont été instaciées avec un booléen qui spécifie
		 * l'utilisation ou non de l'élégage alphabeta.
		 */
		while (!s.isFinished()) {
			loop++;
			if (loop > 1000)
				break;
			player = s.getCurrentPlayer();
			if (player == BLANC) {
				// Récupération du meilleur coup blanc à jouer
				coup = ia_blanc.decide(s, profondeurIABlanc);
			} else {
				// Récupération du meilleur coup noir à jouer
				coup = ia_noir.decide(s, profondeurIANoir);
			}
			s = s.play(coup);
			System.out.println("Tour : " + loop);
			s.printBoard();
		}

		System.out.println("Nombre de tours de jeu :" + loop + "\n");
		System.out.println("Plateu de jeu final : \n");
		s.printBoard();

		if (s.getScore(1) > s.getScore(2)) {
			System.out.println("Victoire du joueur 1 ! Score : " + s.getScore(1) + " vs " + s.getScore(2));
		} else if (s.getScore(1) == 0.5) {
			System.out.println("Egalité");
		} else {
			System.out.println("Victoire du joueur 2 ! Score : " + s.getScore(1) + " vs " + s.getScore(2));
		}
		// Affichage du nombre d'appels des algorithmes d'optimisation.
		System.out.println("Nombre d'appels de l'algorithme d'optimisation (blanc + noir) : " + IA.cptAppels);
	}
}
