#include <lpsolve/lp_lib.h>
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <math.h>
#include <string>
#include <sstream> 
#include <cstdlib>
// g++ Mycielski.cpp -llpsolve55 -lcolamd -ldl -o Mycielski
// ./Mycielski graph.dimacs

// Need lp_solve and liblpsolve55-dev to compile and execute

void vider_row(REAL row[], const int& taille);

int main(int argc, char* argv[])
{
	lprec* lp;
	int nbNoeuds = 0, nbArcs = 0;
	if(argv[1] == nullptr)
	{
		std::cout << "Fichier graphe en paramètre nécessaire." << std::endl;
		exit(EXIT_FAILURE);
	}
	std::ifstream fichier (argv[1]);
	if (!fichier.is_open())
	{
		std::cout << "Impossible d'ouvrir le fichier du graphe." << std::endl;
		exit(EXIT_FAILURE);
	}
	std::string ligne;
	while (getline(fichier, ligne))
	{
		std::string mot;
		std::istringstream iss(ligne);
		iss >> mot;
		if (mot == "p")
		{
			iss >> mot;
			iss >> mot;
			nbNoeuds = stoi(mot);
			iss >> mot;
			nbArcs = stoi(mot);
			break;
		}
	}
	lp = make_lp(0, nbNoeuds*nbNoeuds+nbNoeuds);
	REAL row[nbNoeuds*nbNoeuds+nbNoeuds + 1];
	vider_row(row, nbNoeuds*nbNoeuds+nbNoeuds+1);
	for (int i = 1; i <= (nbNoeuds*nbNoeuds+nbNoeuds); i++)
	{
		row[i] = 1.0;
		set_int(lp, i, true);
		add_constraint(lp, row, GE, 0);
		add_constraint(lp, row, LE, 1);
		row[i] = 0.0;
	}
	for (int i = 0; i < nbNoeuds; i++)
	{
		for (int j = 0; j < nbNoeuds; j++)
		{
			row[i*nbNoeuds+j+1] = 1.0;
		}
		add_constraint(lp, row, EQ, 1);
		vider_row(row, nbNoeuds*nbNoeuds+nbNoeuds+1);
	}
	while (getline(fichier, ligne))
	{
		std::string mot;
		std::istringstream iss(ligne);
		iss >> mot;
		if (mot == "e")
		{
			iss >> mot;
			int premierNode = stoi(mot);
			iss >> mot;
			int secondNode = stoi(mot);

			for (int i = 1; i <= nbNoeuds; i++)
			{
				row[premierNode*nbNoeuds-nbNoeuds+i] = 1.0;
				row[secondNode*nbNoeuds-nbNoeuds+i] = 1.0;
				row[(nbNoeuds*nbNoeuds+1)+i] = -1.0;
				add_constraint(lp, row, LE, 0);
				vider_row(row, nbNoeuds*nbNoeuds+nbNoeuds+1);
			}
		}
	}
	for (int i = 0; i < nbNoeuds; i++)
	{
		row[nbNoeuds*nbNoeuds+i+1] = 1.0;
	}
	set_obj_fn(lp, row);
	vider_row(row, nbNoeuds*nbNoeuds+nbNoeuds+1);

	if (solve(lp) != 0)
	{
		fprintf(stderr, "INFEASIBLE\n");
		return(1);
	}
	else
	{
		get_variables(lp, row);
		int cptCol = 0;
		for (int i = 0; i < nbNoeuds ; i++)
		{
			for (int j = 1; j < nbNoeuds; j++)
			{
				cptCol++;
				if(row[i*nbNoeuds+j] == 1) {break;};
			}
			std::cout << "Noeud" << (i+1) << ", couleur :" << cptCol << std::endl;
			cptCol = 0;
		}
		print_objective(lp);
	}
	return(0);
}

void vider_row(REAL row[], const int& taille)
{
	for (int i = 0; i < taille; i++)
	{
		row[i] = 0.0;
	}
}

