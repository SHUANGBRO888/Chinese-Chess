package chessGame.Rules;

public interface IMoveRule {
    // interface to assess specific moves
    boolean isMoveValid(int xs, int ys, int xd, int yd, int[][] board);
}
