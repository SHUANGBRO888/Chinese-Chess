package chessGame.Util;

import chessGame.XiangQiPieces;

public class GamePiecesInitializer {

    // Constants for chess piece names
    private static final String[] BLACK_PIECE_NAMES = { "車", "馬", "象", "士", "將", "炮", "卒" };
    private static final String[] RED_PIECE_NAMES = { "車", "馬", "相", "仕", "帥", "砲", "兵" };

    // Initializes and returns an array of XiangQi pieces for the game.
    public static XiangQiPieces[] initializePieces() {
        XiangQiPieces[] gamePieces = new XiangQiPieces[32];

        // Initialize main pieces for both sides
        initializeMainPieces(gamePieces);

        // Initialize cannons and soldiers for both sides
        initializeCannonsAndSoldiers(gamePieces);

        return gamePieces;
    }

    // Method to initialize the main pieces (non-soldiers and non-cannons).
    private static void initializeMainPieces(XiangQiPieces[] gamePieces) {
        for (int i = 0; i < 9; i++) {
            int nameIdx = (i < 5) ? i : (8 - i);
            // Initialize black main pieces
            gamePieces[i] = new XiangQiPieces(0, BLACK_PIECE_NAMES[nameIdx], i, 0);
            // Initialize red main pieces
            gamePieces[i + 16] = new XiangQiPieces(1, RED_PIECE_NAMES[nameIdx], i, 9);
        }
    }

    // Method to initialize cannons and soldiers.
    private static void initializeCannonsAndSoldiers(XiangQiPieces[] gamePieces) {
        // Initialize cannons for both sides
        gamePieces[9] = new XiangQiPieces(0, BLACK_PIECE_NAMES[5], 1, 2);
        gamePieces[10] = new XiangQiPieces(0, BLACK_PIECE_NAMES[5], 7, 2);
        gamePieces[25] = new XiangQiPieces(1, RED_PIECE_NAMES[5], 1, 7);
        gamePieces[26] = new XiangQiPieces(1, RED_PIECE_NAMES[5], 7, 7);

        // Initialize soldiers for both sides
        for (int i = 0; i < 5; i++) {
            gamePieces[11 + i] = new XiangQiPieces(0, BLACK_PIECE_NAMES[6], 2 * i, 3);
            gamePieces[27 + i] = new XiangQiPieces(1, RED_PIECE_NAMES[6], 2 * i, 6);
        }
    }
}
