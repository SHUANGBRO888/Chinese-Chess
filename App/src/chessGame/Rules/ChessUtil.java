package chessGame.Rules;

public class ChessUtil {

    public static boolean isStraightLineMove(int xs, int ys, int xd, int yd) {
        // Determines if a move is along a straight line.
        // A move is considered straight if it's either entirely horizontal (ys == yd)
        // or entirely vertical (xs == xd).
        return xs == xd || ys == yd;
    }

    public static int countPiecesBetweenPoints(int x1, int y1, int x2, int y2, int[][] board) {
        // Counts the number of pieces between two points on the board.
        // It only counts along straight lines (either horizontal or vertical).
        int count = 0;
        if (x1 == x2) {
            // When the x-coordinates are the same, count along the Y-axis.
            for (int j = Math.min(y1, y2) + 1; j < Math.max(y1, y2); j++) {
                if (hasPiece(x1, j, board)) {
                    count++;
                }
            }
        } else if (y1 == y2) {
            // When the y-coordinates are the same, count along the X-axis.
            for (int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++) {
                if (hasPiece(i, y1, board)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isDiagonalMove(int xs, int ys, int xd, int yd) {
        // Checks if a move is diagonal.
        // A move is diagonal if the absolute horizontal and vertical distances are
        // equal.
        return Math.abs(xs - xd) == Math.abs(ys - yd);
    }

    public static boolean isInPalace(int x, int y) {
        // Determines if a given coordinate is within the 'palace'.
        // The palace is a 3x3 area at the center of each player's side of the board.
        return x > 2 && x < 6 && (y > 6 || y < 3);
    }

    public static boolean hasNoPieceBetweenForHorse(int xs, int ys, int xd, int yd, int[][] board) {
        // Specific check for the 'Horse' piece to ensure there is no piece in the way.
        // The Horse moves in an 'L' shape, so this checks the square that the Horse
        // 'jumps' over.
        return Math.abs(xs - xd) == 2 ? board[(xs + xd) / 2][ys] < 0 : board[xs][(ys + yd) / 2] < 0;
    }

    public static boolean checkY(int i, int cmpValue, int ys, int yd, int[][] board) {
        // Checks the number of pieces between two points on the Y-axis.
        int cnt = 0;
        // Determine the minimum and maximum y-coordinates to iterate between them.
        int m = Math.min(ys, yd);
        int n = Math.max(ys, yd);
        for (int j = m + 1; j < n; j++) {
            if (hasPiece(i, j, board)) {
                cnt++;
            }
        }
        // Return true if the count matches the expected value (cmpValue).
        return cnt == cmpValue;
    }

    public static boolean hasPiece(int x, int y, int[][] board) {
        return board[x][y] >= 0;
    }

    // Checks if two pieces belong to the same ally.
    public static boolean areSameAllied(int xs, int ys, int xd, int yd, int[][] board) {
        // Check if the destination is empty (no piece)
        if (board[xd][yd] < 0) {
            return false;
        }

        // Check if both pieces belong to the same range of numbers (same family)
        // Assuming pieces from one family are represented by codes less than 16
        // and the other family have numbers equal to or greater than 16.
        return board[xs][ys] < 16 ? board[xd][yd] < 16 : board[xd][yd] >= 16;
    }

}
