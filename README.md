# Fianco

A small Java-based [Fianco](http://www.di.fc.ul.pt/~jpn/gv/fianco.htm) implementation.

- 10 minutes per player (only important for AI).
- Experiments and plots are recommended.
- Mention failed experiments (bitboards, etc.)

- Contempt factor (chess wiki)
- quiescence search, modify to account for capturing sequences
    // TODO: try add/remove from one board instead of deepclone
    // - lazy eval for move generation 
    // (i.e. if you first try tt move and killer moves, then you don't need to generate all legal moves yet)
