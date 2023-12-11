package chessGame.Rules.Pieces;

import chessGame.Rules.ChessUtil;
import chessGame.Rules.IMoveRule;

public class MoveRuleJu implements IMoveRule {
    @Override
    // Rule for 'Ju' (Chariot): It can move any number of squares along rows or
    // columns, but cannot leap over other pieces.
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check if the move is in a straight line (either horizontally or vertically).
        return ChessUtil.isStraightLineMove(startX, startY, targetX, targetY)
                // Ensure that there are no pieces between the start and destination points.
                && ChessUtil.countPiecesBetweenPoints(startX, startY, targetX, targetY, board) == 0;
    }
}
