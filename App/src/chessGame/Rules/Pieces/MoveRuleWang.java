package chessGame.Rules.Pieces;

import chessGame.Rules.ChessUtil;
import chessGame.Rules.IMoveRule;

public class MoveRuleWang implements IMoveRule {
    @Override
    // Rule for 'Wang' (King): Moves one square horizontally or vertically and must
    // stay within the palace.
    public boolean isMoveValid(int startX, int startY, int targetX, int targetY, int[][] board) {
        // Check if the move is either horizontal or vertical and only one square in
        // distance.

        // Check if the move is either horizontal or vertical and only one square in distance.
        boolean isHorizontalMove = Math.abs(startX - targetX) == 1 && startY == targetY;
        boolean isVerticalMove = Math.abs(startY - targetY) == 1 && startX == targetX;

        // Check if the destination square is within the palace.
        boolean isInPalace = ChessUtil.isInPalace(targetX, targetY);

        // Additional condition for the circumstance that when two Kings face each other with no pieces in between.
        boolean kingsFacingEachOther = startX == targetX && ChessUtil.checkY(startX, 0, startY, targetY, board)
                && (board[targetX][targetY] == 4 || board[targetX][targetY] == 20);

        return (isHorizontalMove || isVerticalMove) && isInPalace || kingsFacingEachOther;
    }
}
