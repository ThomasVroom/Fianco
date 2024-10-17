# Fianco

A small Java-based [Fianco](http://www.di.fc.ul.pt/~jpn/gv/fianco.htm) implementation.

- 10 minutes per player (only important for AI).
- Experiments and plots are recommended.
- Mention failed experiments (bitboards, etc.)

- binary board + add/remove from one board instead of deepclone / keep zobrist hash as variable of gamestate?

|                       | NegaMax | NegaMaxID | NegaMaxPlus | NegaMaxQS |
|-----------------------|:-------:|:---------:|:-----------:|:---------:|
| NegaMax               |    x    |     x     |      x      |     x     |
| $\alpha\beta$-Pruning |    x    |     x     |      x      |     x     |
| Iterative Deepening   |         |     x     |      x      |     x     |
| Aspiration Search     |         |     x     |      x      |     x     |
| Smart Termination*    |         |     x     |      x      |     x     |
| Transposition Tables  |         |     x     |      x      |     x     |
| Killer Moves          |         |           |      x      |     x     |
| History Heuristic     |         |           |      x      |     x     |
| Fractional Plies      |         |           |             |     x     |
| Quiescence Search     |         |           |             |     x     |

*Smart Termination: uses a simple non-linear function to calculate the time available per move, aiming to spend no more than 10 minutes per game. Search is terminated early if this time limit is hit or if a move is found that guarantees a win.
