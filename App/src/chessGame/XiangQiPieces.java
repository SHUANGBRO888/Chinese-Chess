package chessGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class XiangQiPieces {

    public static final int PIECE_SIZE = 50; // Size of the chess piece.
    private final int side; // Indicates the side of the piece (0 for Black, 1 for Red).
    private final String type; // Type of the chess piece (e.g., Soldier, Cannon).
    private int positionX; // Coordinates of the chess piece on the board.
    private int positionY;
    private final Image pieceVisual; // Visual representation of the chess piece.

    //Constructs a XiangQiPiece with specified attributes.
    public XiangQiPieces(int side, String type, int positionX, int positionY) {
        this.side = side; // Indicates the side of the piece (0 for Black, 1 for Red)
        this.type = type; // Type of the chess piece (e.g., Soldier, Cannon)
        this.positionX = positionX; // X-coordinate of the chess piece on the board
        this.positionY = positionY; // Y-coordinate of the chess piece on the board
        this.pieceVisual = createPieceImage(); // Visual representation of the chess piece
    }

    //Generates the image of the chess piece with custom styling.
    private Image createPieceImage() {
        BufferedImage image = new BufferedImage(PIECE_SIZE, PIECE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Define colors based on the piece's side.
        Color fill, border, text;
        if (side == 0) {
            fill = new Color(222, 184, 135); // Light wooden color for Black side
            border = new Color(101, 67, 33); // Darker border for contrast
            text = Color.BLACK; // Black text for one side
        } else {
            fill = new Color(222, 184, 135); // Same light wooden color for Red side
            border = new Color(101, 67, 33); // Consistent border color
            text = Color.RED; // Red text to distinguish the other side
        }

        graphics.setColor(fill); // Setting up the graphics for drawing the piece.
        graphics.fillOval(0, 0, PIECE_SIZE, PIECE_SIZE); // Filling the oval shape to represent the main body of the piece.
        graphics.setStroke(new BasicStroke(3.0f)); // Setting the stroke for the border of the piece for visual clarity.
        graphics.setColor(border); 
        graphics.drawOval(1, 1, PIECE_SIZE - 2, PIECE_SIZE - 2); // Drawing the border of the piece.

        graphics.setFont(new Font("华文细黑", Font.BOLD, 38 * PIECE_SIZE / 50)); // Setting the font and color for the text on the piece.
        graphics.setColor(text);
        graphics.drawString(type, PIECE_SIZE / 2 - 20 * PIECE_SIZE / 50, PIECE_SIZE / 2 + 14 * PIECE_SIZE / 50); // Drawing the name of the piece at the center.

        graphics.dispose();
        return image; // Returning the created image
    }

    // Gets the X-coordinate of the piece andreturn The X-coordinate.

    public int getPositionX() {
        return positionX;
    }

    // Gets the Y-coordinate of the piece and return The Y-coordinate.
    public int getPositionY() {
        return positionY;
    }

    // Retrieves the visual representation of the chess piece and return The Image of the chess piece.
     public Image getPieceVisual() {
        return pieceVisual;
    }

    // Get the name of the piece
    public String getName() {
        return type;
    }

    // Sets the position of the chess piece
    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

}
