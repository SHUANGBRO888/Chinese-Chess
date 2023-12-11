package chessGame.Rules.Pieces;

import chessGame.Rules.ChessUtil;
import chessGame.Rules.IMoveRule;

public class MoveRuleShi implements IMoveRule {
    // Rule for 'Shi' (Advisor): Moves one square diagonally and must stay within
    // the palace.
    @Override
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check if the move is diagonal and only one square in distance.
        // The Shi moves diagonally, so the difference in x and y coordinates should
        // both be exactly 1.
        return ChessUtil.isDiagonalMove(startX, startY, targetX, targetY)
                // Check if the destination square is within the palace.
                // The palace is a specific area on the board where the Shi is confined.
                && ChessUtil.isInPalace(targetX, targetY);
    }
}
