package chessGame.Rules.Pieces;

import chessGame.Rules.ChessUtil;
import chessGame.Rules.IMoveRule;

public class MoveRuleXiang implements IMoveRule {
    @Override
    // Rule for 'Xiang' (Minister): Moves two squares diagonally, but cannot cross
    // the river and cannot leap over pieces.
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // The move must be diagonal and exactly two squares in length.
        boolean isDiagonalMove = ChessUtil.isDiagonalMove(startX, startY, targetX, targetY);
        boolean isTwoSquares = Math.abs(startX - targetX) == 2;

        if (!isDiagonalMove || !isTwoSquares) {
            return false;
        }

        // Check if the move crosses the river
        boolean crossingRiver = (startY < 5 && targetY >= 5) || (startY >= 5 && targetY < 5);
        if (crossingRiver) {
            return false;
        }

        // Check if there is no piece in the way
        int middleX = (startX + targetX) / 2;
        int middleY = (startY + targetY) / 2;
        return board[middleX][middleY] < 0;
    }
}
