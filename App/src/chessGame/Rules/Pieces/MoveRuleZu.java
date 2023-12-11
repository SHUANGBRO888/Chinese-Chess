package chessGame.Rules.Pieces;

import chessGame.Rules.IMoveRule;

public class MoveRuleZu implements IMoveRule {
    // Rule for 'Zu' (The Black Soldier): Moves one square forward; after crossing
    // the river,
    // it can also move one square horizontally.
    @Override
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check for a forward move: 'zu' can move one step forward along the column.
        // After crossing the river, 'zu' can also move one step left or right.
        boolean isForwardMove = (targetY - startY == 1) && (startX == targetX);
        boolean isSideMoveAfterRiver = (startY > 4) && (Math.abs(startX - targetX) == 1) && (startY == targetY);

        return isForwardMove || isSideMoveAfterRiver;
    }
    
}
