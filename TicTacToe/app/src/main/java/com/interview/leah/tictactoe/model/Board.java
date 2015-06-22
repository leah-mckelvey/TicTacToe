package com.interview.leah.tictactoe.model;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leah on 6/16/15.
 */
public class Board {
    public static String DRAW = "DRAW";
    public static String PLAYING = "PLAYING";
    public static int BOARD_SIZE = 3;

    private int boxesPlayed = 0;

    private List<Box> boxes;
    private List<Player> players;

    private OnWinListener listener;

    public Board() {
        boxes = new ArrayList<Box>();
        players = new ArrayList<Player>();
        players.add(new Player(Player.X_PLAYER));
        players.add(new Player(Player.O_PLAYER));
    }

    public void addBox(ImageView view, int row, int column) {
        boxes.add(new Box(view, row, column));
    }

    public void addBox(Box box) {
        boxes.add(box);
    }

    public void playBox(int row, int column, Player player) {
        for (Box box : boxes) {
            if (box.getRow() == row && box.getColumn() ==  column) {
                playBox(box, player);
            }
        }

    }

    public void setOnWinListener(OnWinListener listener) {
        this.listener = listener;
    }

    public void setPlayers (List<Player> players) {
        this.players = players;
    }

    public void playBox(Box box, Player player) {
        box.setValue(player.getName());
        player.updateCounts(box.getRow(), box.getColumn());

        boxesPlayed++;
        if (listener != null) {
            if (player.hasWon()) {
                listener.onWin(player);
            } else if (isBoardFull()) {
                listener.onDraw();
            }
        }
    }

    public int getBoxesPlayed() {
        return boxesPlayed;
    }

    public void setBoxesPlayed(int boxesPlayed) {
        this.boxesPlayed = boxesPlayed;
    }

    public boolean isBoardFull() {
        return boxesPlayed >= BOARD_SIZE * BOARD_SIZE;
    }

    public Board clone() {
        Board newBoard = new Board();
        for (Box box : boxes) {
            newBoard.addBox(box.clone());
        }
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player player : this.players) {
            players.add(player.clone());
        }
        newBoard.setPlayers(players);
        newBoard.setBoxesPlayed(boxesPlayed);

        return newBoard;
    }

    public Player getPlayerByName(String name) {
        Player returnPlayer = null;
        for (Player player : players) {
            if (player.getName().compareTo(name) == 0) {
                returnPlayer = player;
            }
        }
        return returnPlayer;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public Box findBoxFromView(ImageView view) {
        Box returnBox = null;
        for (Box box : boxes) {
            if (box.getView() == view) {
                returnBox = box;
            }
        }
        return returnBox;
    }

    public Box findBoxFromCoords(int row, int column) {
        Box returnBox = null;
        for (Box box : boxes) {
            if (box.getRow() == row && box.getColumn() == column) {
                returnBox = box;
            }
        }
        return returnBox;
    }

    public boolean contains(ImageView view) {
        return findBoxFromView(view) != null;
    }

}
