package chessGame.Rules.Pieces;

import chessGame.Rules.IMoveRule;

public class MoveRuleBing implements IMoveRule {
    // Rule for 'Bing' (The Red Soldier): Similar to 'Zu'(The Black Soldier), but
    // moves in the opposite
    @Override
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check for a forward move: 'Bing' can move one step forward along the column.
        // After crossing the river, 'Bing' can also move one step left or right.

        boolean isForwardMove = (targetY - startY == -1) && (startX == targetX);
        boolean isSideMoveAfterRiver = (startY < 5) && (Math.abs(startX - targetX) == 1) && (startY == targetY);

        return isForwardMove || isSideMoveAfterRiver;
    }
}
