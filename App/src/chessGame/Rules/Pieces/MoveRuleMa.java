package chessGame.Rules.Pieces;

import chessGame.Rules.IMoveRule;

public class MoveRuleMa implements IMoveRule {
    // Rule for 'Ma' (Horse): The horse moves in an L shape. It moves two squares in
    // one direction (either row or column),
    // and then one square perpendicular to the initial direction. The move is valid
    // only if there is no piece blocking its
    // path.
    @Override
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check for L-shaped move: 2 squares along a row/column and then 1 square
        // perpendicular, or vice versa.
        return ((Math.abs(startX - targetX) == 2 && Math.abs(startY - targetY) == 1)
                || (Math.abs(startX - targetX) == 1 && Math.abs(startY - targetY) == 2))
                // Check if there is no piece on the "jump-over" square.
                && hasNoPieceBetweenForHorse(startX, startY, targetX, targetY, board);
    }
    
    private boolean hasNoPieceBetweenForHorse(int startX, int startY, int targetX, int targetY, int[][] board) {
        // If the horse moves 2 squares along the x-axis (row),
        // check the square in between along the x-axis.
        if (Math.abs(startX - targetX) == 2) {
            return board[(startX + targetX) / 2][startY] < 0;
            // If the horse moves 2 squares along the y-axis (column),
            // check the square in between along the y-axis.
        } else {
            return board[startX][(startY + targetY) / 2] < 0;
        }
    }
}
