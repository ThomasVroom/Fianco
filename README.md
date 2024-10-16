# Fianco

A small Java-based [Fianco](http://www.di.fc.ul.pt/~jpn/gv/fianco.htm) implementation.

- 10 minutes per player (only important for AI).
- Experiments and plots are recommended.
- Mention failed experiments (bitboards, etc.)

- pass zobrist hash down as a parameter in negamax to skip having to recompute every call?
- clear killer moves after every turn? might reduce the number of legality checks
- check killer moves of previous depth?
- PVS
- null move (?)
 - not in root
 - not after capture
 - not in the endgame (moves available < 16)?
 - not after previous null move (does that include switching sides?)
 - not too close to the leaf nodes (need time to correct a faulty null move)
- multicut
- improve eval function
- binary board + add/remove from one board instead of deepclone?
- opening book
- optimize time alocation
- PN search?

|                        | NegaMax | NegaMaxID | NegaMaxPlus | NegaMaxQS |
|------------------------|:-------:|:---------:|:-----------:|:---------:|
| NegaMax                |    x    |     x     |      x      |     x     |
| $\alpha\beta$-Pruning  |    x    |     x     |      x      |     x     |
| Iterative Deepening    |         |     x     |      x      |     x     |
| Windows                |         |     x     |      x      |     x     |
| Smart Termination*     |         |     x     |      x      |     x     |
| Transposition Tables   |         |     x     |      x      |     x     |
| Killer Moves           |         |           |      x      |     x     |
| History Heuristic      |         |           |      x      |     x     |
| Fractional Plies       |         |           |             |     x     |
| Quiescence Search      |         |           |             |     x     |

*Smart Termination: uses a simple non-linear function to calculate the time available per move, aiming to spend not more than 10 minutes per game in total. Search is terminated early if this time limit is hit or if a move is found that guarantees a win.
