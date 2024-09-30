package Fianco.AI.util;

import java.util.SplittableRandom;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class TranspositionTable {
    
    public static enum Flag {
        EXACT, LOWERBOUND, UPPERBOUND
    }

    // number of bits to use for the primary hash
    public static final byte HASH_BITS = 22;
    public static final int HASH_SIZE = 1 << HASH_BITS;

    // zobrist key generation
    public static final SplittableRandom random = new SplittableRandom();
    public final long[][] zobristKeys = new long[81][2];
    public final long zobristKeyTurn = random.nextLong();

    // table with 2^HASH_BITS entries
    public Entry[] table = new Entry[HASH_SIZE];

    public TranspositionTable() {
        // create zobrist keys
        for (byte i = 0; i < 81; i++) {
            this.zobristKeys[i][0] = random.nextLong();
            this.zobristKeys[i][1] = random.nextLong();
        }
    }

    // returns null if no entry is found
    public Entry retrieve(GameState state) {
        long hash = getHash(state);
        Entry entry = this.table[(int)((hash >>> (63 - HASH_BITS)) % HASH_SIZE)];
        if (entry != null && entry.hashKey == hash << (HASH_BITS + 1)) {
            return entry;
        }
        return null;
    }

    // replacement scheme: always replace
    public void store(GameState state, short value, Flag flag, Move bestMove, byte depth) {
        long hash = getHash(state);
        int primary = (int)((hash >>> (63 - HASH_BITS)) % HASH_SIZE);
        this.table[primary] = new Entry(value, flag, bestMove, depth, hash << (HASH_BITS + 1));
    }

    public long getHash(GameState state) {
        long hash = 0;
        for (Byte piece : state.p1Pieces) {
            hash ^= this.zobristKeys[piece][0];
        }
        for (Byte piece : state.p2Pieces) {
            hash ^= this.zobristKeys[piece][1];
        }
        if (!state.turnIsP1) {
            hash ^= this.zobristKeyTurn;
        }
        return hash;
    }

    public class Entry {

        public short value;
        public Flag flag;
        public Move bestMove;
        public byte depth;
        public long hashKey;

        public Entry(short value, Flag flag, Move bestMove, byte depth, long hashKey) {
            this.value = value;
            this.flag = flag;
            this.bestMove = bestMove;
            this.depth = depth;
            this.hashKey = hashKey;
        }
    }
}
