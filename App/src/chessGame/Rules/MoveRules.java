package chessGame.Rules;

import chessGame.Rules.Pieces.*;
import chessGame.Util.SoundUtil;

//The MoveRules class is responsible for managing the movement rules of the chess game. 
// It contains the game board and provides a method to judge whether a move is valid according to the specific rules of each chess piece.

public class MoveRules {
    private int[][] point; // The game board represented as a 2D array.
    private CheckStatus lastCheckStatus = CheckStatus.NO_CHECK; // Track the last check status

    public MoveRules(int[][] point) {
        this.point = point;
    }

    // Judges if a move is valid based on the type of the chess piece
    public boolean moveCheck(int xs, int ys, int xd, int yd) {
        // Step 1: Check if the move is within the bounds of the game board
        if (xd < 0 || yd < 0 || xd >= point.length || yd >= point[xd].length) {
            return false;
        }

        // Step 2: Check if the move ends on a piece of the same family
        if (ChessUtil.areSameAllied(xs, ys, xd, yd, point)) {
            return false;
        }

        // Step 3: Retrieve the movement rule for the chess piece
        IMoveRule rule = getMoveRule(point[xs][ys]);
        if (rule != null) {
            // Step 4: Use the specific rule to validate the move
            if (!rule.isMoveValid(xs, ys, xd, yd, point)) {
                return false;
            }
        } else {
            // If no rule is found for the piece, the move is invalid
            return false;
        }

        // Check sound
        // Execute the move temporarily for check detection
        int temp = point[xd][yd];
        point[xd][yd] = point[xs][ys];
        point[xs][ys] = -1;

        // Step 5: Check for check status after the move
        CheckStatus currentCheckStatus = isCheck();
        SoundUtil musicObject = new SoundUtil();

         if (currentCheckStatus != lastCheckStatus && currentCheckStatus != CheckStatus.NO_CHECK){
            if(currentCheckStatus == CheckStatus.RED_CHECKS){
                musicObject.playMusic("Chinese-Chess/App/src/chessGame/resources/sound/check-chess-black.wav");
            } else if(currentCheckStatus == CheckStatus.BLACK_CHECKS){
                musicObject.playMusic( "Chinese-Chess/App/src/chessGame/resources/sound/check-chess-red.wav");             
            } 
        }
        // Update the last check status
        lastCheckStatus = currentCheckStatus;

        // Undo the temporary move
         point[xs][ys] = point[xd][yd];
         point[xd][yd] = temp;

        // Reserved for Future Regional Rule Implementation:
        // The commented out code below handle a rule specific to some Chinese regional
        // variants, where if
        // Kings face each other directly on the board,
        // intervening pieces are not allowed to move. This rule is currently not active
        // but may be integrated after we submit the project.

        // // Step 5: Make the move temporarily to check for King face-to-face situation
        // int temp = point[xd][yd];
        // point[xd][yd] = point[xs][ys];
        // point[xs][ys] = -1; // Assuming -1 indicates an empty square

        // // Step 6: Check if the move causes a King face-to-face situation
        // boolean isKingFaceToFace = isKingFaceToFace(point);

        // // Step 7: Undo the temporary move
        // point[xs][ys] = point[xd][yd];
        // point[xd][yd] = temp;

        // // Step 8: Return false if the move causes a King face-to-face situation
        // if (isKingFaceToFace) {
        // return false;
        // }

        // Step 9: If all checks pass, the move is valid
        return true;
    }

    // Determines the move rule based on the piece code
    private IMoveRule getMoveRule(int pieceCode) {
        switch (pieceCode) {
            // Different cases for each piece type, returning the corresponding movement
            // rule
            case 0:
            case 8:
            case 16:
            case 24:
                return new MoveRuleJu();
            case 1:
            case 7:
            case 17:
            case 23:
                return new MoveRuleMa();
            case 2:
            case 6:
            case 18:
            case 22:
                return new MoveRuleXiang();
            case 3:
            case 5:
            case 19:
            case 21:
                return new MoveRuleShi();
            case 4:
            case 20:
                return new MoveRuleWang();
            case 9:
            case 10:
            case 25:
            case 26:
                return new MoveRulePao();
            default:
                // Handle Bing (Red Soldier) or Zu (Black Soldier)
                if ((pieceCode >= 11 && pieceCode < 16) || (pieceCode >= 27 && pieceCode <= 31)) {
                    return pieceCode < 16 ? new MoveRuleZu() : new MoveRuleBing();
                }
                break;
        }
        return null; // Return null if the piece type is not identified
    }

    // New method to check if a face-to-face situation between kings occurs
    // private boolean isKingFaceToFace(int[][] board) {
    // int king1X = -1, king1Y = -1;
    // int king2X = -1, king2Y = -1;

    // // Traverse the board to find the positions of the two kings
    // for (int x = 0; x < board.length; x++) {
    // for (int y = 0; y < board[x].length; y++) {
    // if (board[x][y] == 4) { // Assuming 4 represents one side's king
    // king1X = x;
    // king1Y = y;
    // } else if (board[x][y] == 20) { // Assuming 20 represents the other side's
    // king
    // king2X = x;
    // king2Y = y;
    // }
    // }
    // }

    // // If both kings are in the same column
    // if (king1X == king2X && king1X != -1) {
    // // Calculate the number of pieces between the two kings
    // int piecesBetween = ChessUtil.countPiecesBetweenPoints(king1X, king1Y,
    // king2X, king2Y, board);
    // // If there are no other pieces between the two kings, a face-to-face
    // situation
    // // exists
    // return piecesBetween == 0;
    // }

    // return false;
    // }

    private CheckStatus isCheck() {
        int redKingCode = 4;   // Assuming the code for the Red King is 4
        int blackKingCode = 20; // Assuming the code for the Black King is 20

        // Checking if the Red King is in check (Black is making the check)
        boolean redKingInCheck = isKingInCheck(redKingCode, blackKingCode);
        // Checking if the Black King is in check (Red is making the check)
        boolean blackKingInCheck = isKingInCheck(blackKingCode, redKingCode);

        // Return the corresponding check status based on which king is in check
        if (redKingInCheck) {
            return CheckStatus.BLACK_CHECKS; // Black is checking Red
        } else if (blackKingInCheck) {
            return CheckStatus.RED_CHECKS; // Red is checking Black
        }

        return CheckStatus.NO_CHECK; // No check is currently being made

    }
    
    private boolean isKingInCheck(int kingCode, int opponentKingCode) {
        int kingX = -1, kingY = -1;
    
        // Finding the position of the king
        for (int x = 0; x < point.length; x++) {
            for (int y = 0; y < point[x].length; y++) {
                if (point[x][y] == kingCode) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
            // Break out of the outer loop if the king's position is found
            if (kingX != -1) break;
        }
    
        // Checking if any opponent's piece can move to the king's position
        for (int x = 0; x < point.length; x++) {
            for (int y = 0; y < point[x].length; y++) {
                // Skip if it's the king itself, an empty space, or the opposing king
                if (point[x][y] != kingCode && point[x][y] != -1 && point[x][y] != opponentKingCode) {
                    IMoveRule rule = getMoveRule(point[x][y]);
                    // Check if the move is valid according to the rules of the chess piece
                    if (rule != null && rule.isMoveValid(x, y, kingX, kingY, point)) {
                        return true; // Return true if a valid move can attack the king
                    }
                }
            }
        }
    
        return false; // Return false if no piece can attack the king
    }
    

}
