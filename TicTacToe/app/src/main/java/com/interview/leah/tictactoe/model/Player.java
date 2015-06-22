package com.interview.leah.tictactoe.model;

/**
 * Created by leah on 6/16/15.
 */
public class Player {
    public static String X_PLAYER = "X PLAYER";
    public static String O_PLAYER = "O PLAYER";

    private String playerName;

    public int[] rowCounts = {0, 0, 0};
    public int[] colCounts = {0, 0, 0};
    public int[] diagCounts = {0, 0};

    private boolean hasWon = false;

    public Player(String playername) {
        this.playerName = playername;
    }

//    public String toString() {
//        String returnString = "Name = " + playerName + "\n";
//        returnString += "HasWon = " + hasWon + "\n";
//        returnString += "Row counts = [" + rowCounts[0] + ", " + rowCounts[1] + ", " + rowCounts[2] + "]\n";
//        returnString += "Row counts = [" + colCounts[0] + ", " + colCounts[1] + ", " + colCounts[2] + "]\n";
//        returnString += "Row counts = [" + diagCounts[0] + ", " + diagCounts[1] +  "]\n";
//        return returnString;
//    }

    public String getName() {
        return playerName;
    }

    public void updateCounts (int row, int column) {
        rowCounts[row]++;
        colCounts[column]++;
        if (rowCounts[row] >= Board.BOARD_SIZE || colCounts[column] >= Board.BOARD_SIZE) {
            hasWon = true;
        }
        if (row == column) {
            diagCounts[0]++;
            if(diagCounts[0] >= Board.BOARD_SIZE) {
                hasWon = true;
            }
        }
        if (row == 2 - column) {
            diagCounts[1]++;
            if (diagCounts[1] >= Board.BOARD_SIZE) {
                hasWon = true;
            }
        }
    }

    public Player clone() {
        Player returnPlayer = new Player(playerName);
        returnPlayer.colCounts = colCounts.clone();
        returnPlayer.rowCounts = rowCounts.clone();
        returnPlayer.diagCounts = diagCounts.clone();
        returnPlayer.hasWon = hasWon;
        return returnPlayer;
    }

    public boolean hasWon() {
        return hasWon;
    }
}
