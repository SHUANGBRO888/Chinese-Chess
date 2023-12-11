package chessGame.Rules.Pieces;

import chessGame.Rules.ChessUtil;
import chessGame.Rules.IMoveRule;

public class MoveRulePao implements IMoveRule {
    @Override
    // Rule for 'Pao' (Cannon): Moves like the 'Ju' but captures by leaping over a
    // single piece along the move's path.
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // First, check if the move is in a straight line (horizontally or vertically).
        if (!ChessUtil.isStraightLineMove(startX, startY, targetX, targetY)) {
            return false;
        }
        // Count the number of pieces between the start and end points.
        int piecesBetween = ChessUtil.countPiecesBetweenPoints(startX, startY, targetX, targetY, board);

        // If moving to an empty square, there should be no pieces between.
        if (!ChessUtil.hasPiece(targetX, targetY, board) && piecesBetween == 0) {
            return true;
        }

        // If capturing a piece, there should be exactly one piece between.
        return ChessUtil.hasPiece(targetX, targetY, board) && piecesBetween == 1;
    }
}
