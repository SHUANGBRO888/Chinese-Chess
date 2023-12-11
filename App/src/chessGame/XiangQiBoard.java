package chessGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.*;

import chessGame.Rules.*;
import chessGame.Util.*;

public class XiangQiBoard extends JPanel {

    // Class member variables
    private XiangQiPieces[] gamePieces; // Array to hold the chess pieces
    private MoveRules gameRules; // The rules of the chess game
    private BoardMouseListener mouseListener; // Listener for mouse actions on the board

    // Game state and turn indicators
    private GameStateManager gameStateManager;
    private boolean isTurnRed; // true for Red's turn, false for Black's turn
    private int[][] boardPoints; // Represents positions on the chessboard

    // Graphics related variables for drawing the board and pieces
    private Image boardImage; // Chessboard image
    private Image bufferImage; // Buffer for double buffering
    private Image backgroundImage; // Background for the GUI.
    private Graphics bufferGraphics; // Graphics context for buffer
    private JButton buttonStartGame; // Button to start or end the game

    // Timer utility to manage game timing
    private TimerUtil gameTimerUtil;

    // Constants for layout and sizing
    private static final int CELL_WIDTH = 60; // Cell width on chessboard
    private static final int BOARD_OFFSET_X = 40, BOARD_OFFSET_Y = 40; // Top-left corner offset
    private static final int PIECE_RADIUS = XiangQiPieces.PIECE_SIZE / 2; // Chess piece radius
    private static final int BOARD_WIDTH = BOARD_OFFSET_X + CELL_WIDTH * 8 + 500; // Total canvas width
    private static final int BOARD_HEIGHT = BOARD_OFFSET_Y + CELL_WIDTH * 9 + 80; // Total canvas height

    public XiangQiBoard() {

        // BGM
        String filepath = "Chinese-Chess/App/src/chessGame/resources/sound/bgm.wav";
        SoundUtil musicObject = new SoundUtil();

        try {
            musicObject.playSound(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setting up the main game window
        JFrame gameWindow = new JFrame("Chinese Chess");
        gameWindow.setBounds(500, 500, BOARD_WIDTH, BOARD_HEIGHT); // Set the size and position of the frame

        setVariable(); // Set variables
        setGameButton(); // Set the buttons
        addMouseListener(mouseListener); // Add mouse listener for board interaction
        addMouseMotionListener(mouseListener); // Add mouse motion listener for drag and drop

        // Set up the timer
        gameTimerUtil = new TimerUtil(45, this::refreshDisplay, this::endGame);
        // Using relative path for the background image.
        backgroundImage = new ImageIcon(getClass().getResource("/chessGame/resources/images/backgroundImage.jpg"))
                .getImage();
        gameStateManager = new GameStateManager();

        setLayout(null); // Use absolute layout (no layout manager)
        setBackground(new Color(255, 255, 255)); // Set the background color of the panel
        gameWindow.getContentPane().add(this); // Add this panel to the frame's content pane
        gameWindow.setVisible(true); // Make the frame visible
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation

    }

    private void setVariable() {
        gamePieces = new XiangQiPieces[32]; // Initialize array for chess pieces
        boardPoints = new int[9][10]; // Initialize board points (9x10 grid)
        gameRules = new MoveRules(boardPoints); // Initialize game rules
        boardImage = drawChessBoard(); // Create chess board image
        mouseListener = new BoardMouseListener(); // Initialize mouse listener
        gamePieces = GamePiecesInitializer.initializePieces(); // Initialize chess pieces
        setBoardPoints(); // Initialize board points
    }

    // Initializes and sets up the game start/end button
    private void setGameButton() {
        buttonStartGame = new JButton("START A GAME");
        buttonStartGame.setFont(new Font("", Font.BOLD, 18));
        buttonStartGame.setBounds(BOARD_WIDTH - 250, 240, 200, 50);

        // Assign action for the button using Lambda expression
        buttonStartGame.addActionListener(e -> toggleGameState());
        add(buttonStartGame); // Add the button to the board
    }

    // Toggles the game state between started and ended
    private void toggleGameState() {
        if (gameStateManager.getGameState() != GameStateManager.IN_PROGRESS) {
            startNewGame();
        } else {
            endCurrentGame();
        }
        refreshDisplay(); // Refresh the board display
    }

    // Starts a new game
    private void startNewGame() {
        gameStateManager.startGame(); // Set game state to start
        isTurnRed = true; // Red's turn to start
        gameTimerUtil.resetTimer(45); // Reset the timer
        setBoardPoints(); // Initialize board points
        buttonStartGame.setText("END GAME"); // Update button text
    }

    // Ends the current game
    private void endCurrentGame() {
        gameStateManager.endGame(); // Set game state to ended
        gameTimerUtil.cancelTimer(); // Stop the timer
        setBoardPoints(); // Reset board points
        buttonStartGame.setText("START A GAME"); // Update button text
    }

    // Initializes the board points to represent the current state of the game
    private void setBoardPoints() {
        // Reset all board positions to indicate absence of chess pieces
        resetBoardPoints();

        // Assign each chess piece to its starting position on the board
        for (XiangQiPieces piece : gamePieces) {
            int x = piece.getPositionX();
            int y = piece.getPositionY();
            boardPoints[x][y] = getPieceIndex(piece);
        }
    }

    // Resets all points on the board to a default state
    private void resetBoardPoints() {
        for (int i = 0; i < boardPoints.length; i++) {
            Arrays.fill(boardPoints[i], -1);
        }
    }

    // Retrieves the index of a chess piece in the gamePieces array
    private int getPieceIndex(XiangQiPieces piece) {
        return Arrays.asList(gamePieces).indexOf(piece);
    }

    private Image drawChessBoard() {
        BufferedImage chessBoardImage = new BufferedImage(CELL_WIDTH * 8, CELL_WIDTH * 9, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) chessBoardImage.getGraphics();

        drawGridLines(g2d); // Draw the grid lines
        drawBordersAndPalaces(g2d); // Draw borders and palaces
        drawTextOnBoard(g2d); // Add text to the board

        return chessBoardImage;
    }

    // Method to draw grid lines on the chessboard
    private void drawGridLines(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        // Draw horizontal lines
        for (int j = 1; j < 9; j++) {
            g2d.drawLine(0, CELL_WIDTH * j, CELL_WIDTH * 8, CELL_WIDTH * j);
        }
        // Draw vertical lines
        for (int i = 1; i < 8; i++) {
            g2d.drawLine(CELL_WIDTH * i, 0, CELL_WIDTH * i, CELL_WIDTH * 4);
            g2d.drawLine(CELL_WIDTH * i, CELL_WIDTH * 5, CELL_WIDTH * i, CELL_WIDTH * 9);
        }
    }

    // Method to draw the borders and palaces
    private void drawBordersAndPalaces(Graphics2D g2d) {
        g2d.drawRect(1, 1, CELL_WIDTH * 8 - 2, CELL_WIDTH * 9 - 2);
        // Palace diagonal lines
        g2d.drawLine(CELL_WIDTH * 3, 0, CELL_WIDTH * 5, CELL_WIDTH * 2);
        g2d.drawLine(CELL_WIDTH * 3, CELL_WIDTH * 2, CELL_WIDTH * 5, 0);
        g2d.drawLine(CELL_WIDTH * 3, CELL_WIDTH * 7, CELL_WIDTH * 5, CELL_WIDTH * 9);
        g2d.drawLine(CELL_WIDTH * 3, CELL_WIDTH * 9, CELL_WIDTH * 5, CELL_WIDTH * 7);
    }

    // Method to add text to the chessboard
    private void drawTextOnBoard(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("华文细黑", Font.BOLD, (int) (45.0 * CELL_WIDTH / 60)));
        // Draw "Chu River" and "Han Border" labels
        g2d.drawString("楚河", (int) (CELL_WIDTH * 4 - 180.0 * CELL_WIDTH / 60),
                (int) (CELL_WIDTH * 4.5 + 15.0 * CELL_WIDTH / 60));
        g2d.drawString("漢界", (int) (CELL_WIDTH * 4 + 60.0 * CELL_WIDTH / 60),
                (int) (CELL_WIDTH * 4.5 + 15.0 * CELL_WIDTH / 60));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the chessboard and its border
        drawChessboardWithBorder(g2d);

        // Draw all the chess pieces on the board
        drawChessPieces(g2d);

        // Handling the selected chess piece, if any
        drawSelectedChessPiece(g2d);

        // Paint additional tips on the board
        drawGameHints(g2d);
    }

    // Draws the chessboard and its border
    private void drawChessboardWithBorder(Graphics2D g2d) {
        // Draw the chessboard image
        g2d.drawImage(boardImage, BOARD_OFFSET_X, BOARD_OFFSET_Y, null);

        // Draw a border around the chessboard
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawRect(BOARD_OFFSET_X - 5, BOARD_OFFSET_Y - 5,
                boardImage.getWidth(this) + 10, boardImage.getHeight(this) + 10);
    }

    // Draws all chess pieces on the board, except the selected one
    private void drawChessPieces(Graphics2D g2d) {
        int selIdx = mouseListener.getActiveChessPiece();

        // Loop through the board and draw each piece
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                int idx = boardPoints[i][j];
                if (idx >= 0 && idx != selIdx) {
                    int xpos = BOARD_OFFSET_X + CELL_WIDTH * i - PIECE_RADIUS;
                    int ypos = BOARD_OFFSET_Y + CELL_WIDTH * j - PIECE_RADIUS;
                    g2d.drawImage(gamePieces[idx].getPieceVisual(), xpos, ypos, null);
                }
            }
        }
    }

    // Handles the drawing of the selected chess piece
    private void drawSelectedChessPiece(Graphics2D g2d) {
        int selIdx = mouseListener.getActiveChessPiece();

        // Draw the selected chess piece if any
        if (selIdx >= 0) {
            paintComponent(g2d, mouseListener.getActiveX(), mouseListener.getActiveY());
            g2d.drawImage(gamePieces[selIdx].getPieceVisual(),
                    mouseListener.getChessPieceMouseX(),
                    mouseListener.getChessPieceMouseY(), null);
        }
    }

    // Draws game hints on the board based on the current game state
    private void drawGameHints(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 25));

        switch (gameStateManager.getGameState()) {
            case GameStateManager.NOT_STARTED:
                // Draw hint for game start
                g2d.setColor(Color.BLACK);
                g2d.drawString("CLICK TO START", BOARD_WIDTH - 255, 160);
                break;

            case GameStateManager.IN_PROGRESS:
                // Draw hints for whose turn it is and the countdown
                String turnText = isTurnRed ? "Red's turn" : "Black's turn";
                g2d.setColor(isTurnRed ? Color.RED : Color.BLACK);
                g2d.drawString(turnText, BOARD_WIDTH - 207, 130);
                g2d.drawString("Countdown: " + gameTimerUtil.getCurrentCountdown() + " s", BOARD_WIDTH - 250, 170);
                break;

            case GameStateManager.ENDED:
                // Draw game over message and announce the winner
                String winnerText = isTurnRed ? "Red wins!" : "Black wins!";
                g2d.setColor(isTurnRed ? Color.RED : Color.BLACK);
                g2d.drawString("GAME OVER", BOARD_WIDTH - 222, 140);
                g2d.drawString(winnerText, BOARD_WIDTH - 214, 180);
                break;
        }
    }

    // Method to highlight the selected chess piece
    public void paintComponent(Graphics2D g2d, int i, int j) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Color.red);
        int x = BOARD_OFFSET_X + CELL_WIDTH * i, y = BOARD_OFFSET_Y + CELL_WIDTH * j;
        final int d1 = 15, d2 = 5;
        // Draw red corners around the selected piece
        g2d.drawLine(x - d1, y - d2, x - d2, y - d2);
        g2d.drawLine(x - d1, y + d2, x - d2, y + d2);
        g2d.drawLine(x + d1, y - d2, x + d2, y - d2);
        g2d.drawLine(x + d1, y + d2, x + d2, y + d2);
        g2d.drawLine(x - d2, y - d2, x - d2, y - d1);
        g2d.drawLine(x + d2, y - d2, x + d2, y - d1);
        g2d.drawLine(x - d2, y + d2, x - d2, y + d1);
        g2d.drawLine(x + d2, y + d2, x + d2, y + d1);
    }

    // Main rendering method, responsible for updating the game's display
    public void refreshDisplay() {
        prepareBuffer();
        drawGame();
        drawBufferToScreen();
        checkIfGameEnds();
    }

    // Prepares the double buffer image and graphics context
    private void prepareBuffer() {
        // Reinitialize buffer if the size has changed or it's not created yet
        if (bufferImage == null || bufferImage.getWidth(this) != getWidth()
                || bufferImage.getHeight(this) != getHeight()) {
            bufferImage = createImage(getWidth(), getHeight());
            if (bufferImage != null) {
                bufferGraphics = bufferImage.getGraphics();
            }
        }
    }

    // Draws the current state of the game onto the buffer
    private void drawGame() {
        // Clear buffer and draw components
        if (bufferGraphics != null) {
            bufferGraphics.setColor(getBackground());
            bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
            paint(bufferGraphics);
        }
    }

    // Draws the buffered image to the screen
    private void drawBufferToScreen() {
        // Draw buffer to actual screen
        Graphics g = getGraphics();
        if (g != null && bufferImage != null) {
            g.drawImage(bufferImage, 0, 0, null);
            g.dispose();
        }
    }

    // Checks if the game ends.
    private void checkIfGameEnds() {
        // Handle actions at game end
        if (gameStateManager.getGameState() == GameStateManager.ENDED) {
            handleGameEnd();
        }
    }

    // Handles the logic when the game ends
    private void handleGameEnd() {
        // Stop the timer and update UI
        gameTimerUtil.cancelTimer();
        updateStartButtonForGameEnd();
        showGameEndDialog();
    }

    // Updates the text of the start game button at the end of the game
    private void updateStartButtonForGameEnd() {
        // Change button text to indicate game restart
        buttonStartGame.setText("HIT TO RESTART");
    }

    // Shows a dialog box announcing the end of the game
    private void showGameEndDialog() {
        // Display winner information in a dialog box
        String winnerMessage = isTurnRed ? "Congratulations! The Winner is Red!"
                : "Congratulations! The Winner is Black!";
        JOptionPane.showMessageDialog(null, winnerMessage, "GAME OVER", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // we call the superclass method to handle the default painting.
        // Having the background image fit exatly to current component's width and
        // height.
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    // End of the game
    private void endGame() {
        gameStateManager.endGame();
        isTurnRed = !isTurnRed;
        refreshDisplay();
    }

    // Mouse listener for the chessboard
    private class BoardMouseListener implements MouseListener, MouseMotionListener {

        private int activeChessPiece = -1; // Index of the active chess piece, -1 means none selected
        private int activeX, activeY; // Coordinates of the active chess piece on the board
        private int currentMouseX, currentMouseY; // Current mouse coordinates

        @Override
        public void mousePressed(MouseEvent me) {
            activeChessPiece = -1; // Reset selected piece index
            processMousePress(me);
            selectActiveChessPiece();
        }

        private void processMousePress(MouseEvent me) {
            // Convert mouse coordinates to board indices
            activeX = calculateBoardIndex(me.getX(), BOARD_OFFSET_X - PIECE_RADIUS,
                    boardImage.getWidth(null) + 2 * PIECE_RADIUS);
            activeY = calculateBoardIndex(me.getY(), BOARD_OFFSET_Y - PIECE_RADIUS,
                    boardImage.getHeight(null) + 2 * PIECE_RADIUS);
        }

        private void selectActiveChessPiece() {
            // Check for invalid selection coordinates
            if (activeX == -1 || activeY == -1) {
                return; // Do nothing if the selection is invalid
            }
            // Get the piece index at the selected coordinates
            int selectedPieceIndex = boardPoints[activeX][activeY]; 

            // Conditions to ignore the selection
            if (selectedPieceIndex < 0 || gameStateManager.getGameState() != GameStateManager.IN_PROGRESS) {
                // No piece selected or not in game state
                return; 
            }

            // Check if the selected piece belongs to the current player
            if (isTurnRed ? selectedPieceIndex < 16 : selectedPieceIndex >= 16) {
                // Ignore if the piece belongs to the opponent
                return; 
            }

            activeChessPiece = selectedPieceIndex; // Set the selected piece index
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (activeChessPiece < 0) {
                return; // Do nothing if no piece is selected
            }
            currentMouseX = me.getX(); // Update the x-coordinate of the mouse
            currentMouseY = me.getY(); // Update the y-coordinate of the mouse
            refreshDisplay(); // Refresh the board to display the piece being dragged
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            if (activeChessPiece < 0) {
                return; // Do nothing if no chess piece is currently selected
            }

            // Calculate the destination indices based on the mouse release position
            int destinationX = calculateBoardIndex(me.getX(), BOARD_OFFSET_X - PIECE_RADIUS,
                    boardImage.getWidth(null) + 2 * PIECE_RADIUS);
            int destinationY = calculateBoardIndex(me.getY(), BOARD_OFFSET_Y - PIECE_RADIUS,
                    boardImage.getHeight(null) + 2 * PIECE_RADIUS);

            // Execute the piece move if it's valid according to the game rules
            if (gameRules.moveCheck(activeX, activeY, destinationX, destinationY)) {
                checkAndExecuteMove(destinationX, destinationY);
            }

            // Reset the active piece and refresh the board display
            activeChessPiece = -1;
            refreshDisplay();
        }

        private void checkAndExecuteMove(int destinationX, int destinationY) {
            int pieceAtDestination = boardPoints[destinationX][destinationY];
            // Move the selected chess piece to the new position
            movePiece(destinationX, destinationY);
        
            // Check if the game ends (if a king is captured)
            if (pieceAtDestination == 4 || pieceAtDestination == 20) {
                gameStateManager.endGame();
            } else {
                // If not, switch turn.
                switchTurns();
            }
        }
        
        private void movePiece(int destinationX, int destinationY) {
            boardPoints[destinationX][destinationY] = boardPoints[activeX][activeY];
            boardPoints[activeX][activeY] = -1; // Clear the original position
        }

        private void switchTurns() {
            // Toggle the turn between red and black players
            isTurnRed = !isTurnRed;
            gameTimerUtil.resetTimer(45); // Reset the turn timer
        }

        private int calculateBoardIndex(int mouseCoordinate, int boardStartOffset, int boardSizeWithMargin) {
            if (mouseCoordinate < boardStartOffset || mouseCoordinate > boardStartOffset + boardSizeWithMargin) {
                return -1; // Coordinate is outside the board area
            }
            int boardIndex = (mouseCoordinate - boardStartOffset) / CELL_WIDTH;
            int positionWithinCell = (mouseCoordinate - boardStartOffset) % CELL_WIDTH;
            return positionWithinCell < 2 * PIECE_RADIUS ? boardIndex : -1; // Check if within piece's grabbing radius
        }

        // Getter methods for mouse and piece positions
        public int getActiveX() {
            return activeX;
        }

        public int getActiveY() {
            return activeY;
        }

        public int getActiveChessPiece() {
            return activeChessPiece;
        }

        public int getChessPieceMouseX() {
            return currentMouseX - PIECE_RADIUS;
        }

        public int getChessPieceMouseY() {
            return currentMouseY - PIECE_RADIUS;
        }

        // Empty implementations for other MouseListener methods
        @Override
        public void mouseClicked(MouseEvent me) {

        }

        @Override
        public void mouseEntered(MouseEvent me) {

        }

        @Override
        public void mouseExited(MouseEvent me) {

        }

        @Override
        public void mouseMoved(MouseEvent me) {

        }
    }
}
