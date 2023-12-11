package chessGame.Util;

public class GameStateManager {
    // Constants representing different states of the game.
    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int ENDED = 2;

    private int gameState;

    // Constructor initializes the game to the NOT_STARTED state.
    public GameStateManager() {
        gameState = NOT_STARTED;
    }

    // Method to change the game state to IN_PROGRESS, indicating the game has
    // started.
    public void startGame() {
        gameState = IN_PROGRESS;
    }

    // Method to change the game state to ENDED, indicating the game has concluded.
    public void endGame() {
        gameState = ENDED;
    }

    // Method to reset the game state to NOT_STARTED, readying the game for a new
    // session.
    public void resetGame() {
        gameState = NOT_STARTED;
    }

    // Getter method to retrieve the current state of the game.
    public int getGameState() {
        return gameState;
    }
}
