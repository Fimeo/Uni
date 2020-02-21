# Game and rules

Infection set

It's a board game with two players. A field of m * n squares is empty. Each player (black and white) has a checker in the opposite corners of the board.

Each player plays in turn, one checker can move two squares in the four cardinal directions and into an empty square (even over another checker). Or a checker can duplicate itself in one of the four cardinal positions if no checker is already there. After duplicating, it changes the colour of the pieces that touch it to its own colour. The game is over if one of the two players has no more possible moves, it has run out of checkers, or the board is full.

## Resolution

The algorithm used is that of minimax and alphabeta (pruning of minimax). The search depth is given in the command line.
The score function of a player is the proportion of pieces of his color on the board compared to the total number of pieces.