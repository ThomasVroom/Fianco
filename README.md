# Fianco

A small Java-based [Fianco](http://www.di.fc.ul.pt/~jpn/gv/fianco.htm) implementation.

- 10 minutes per player (only important for AI).
- Experiments and plots are recommended.
- Mention failed experiments (bitboards, etc.)

- history heuristic
- try add/remove from one board instead of deepclone (combine with history heuristic to prevent creating new move objects)
- tt replacement schemes
- quiescence search
- fractional plies
- null move & multicut (?)
- endgame database (at least 6 pieces would be relevant, as they don't like giving up pieces)
- improve eval function
- opening book
