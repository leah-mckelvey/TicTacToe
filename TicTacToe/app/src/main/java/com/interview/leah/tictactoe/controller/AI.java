package com.interview.leah.tictactoe.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.interview.leah.tictactoe.model.Board;
import com.interview.leah.tictactoe.model.Box;
import com.interview.leah.tictactoe.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leah on 6/16/15.
 */
public class AI {

    private static AI instance;

    private MoveTreeNode decisionTree = null;
    private MoveTreeNode curNode = null;

    public static AI getInstance() {
        if (instance == null) {
            instance = new AI();
        }
        return instance;
    }

    private AI () {

    }

    public void advanceTree(int row, int column) {
        if(curNode != null) {
            for (MoveTreeNode node : curNode.children) {
                Box box = node.move.getBox();
                if (box.getRow() == row && box.getColumn() == column) {
                    curNode = node;
                }
            }
        }
    }

    public Box moveForAI(Board board, int row, int column) {
        Move move = null;
        Box moveBox = null;

        if (decisionTree == null) {
            Board cloneBoard = board.clone();
            Player oPlayer = cloneBoard.getPlayerByName(Player.O_PLAYER);
            Player xPlayer = cloneBoard.getPlayerByName(Player.X_PLAYER);
            Move initMove = new Move(null, 0);
            MoveTreeNode initParent = new MoveTreeNode(initMove);
            decisionTree = minimax(cloneBoard, null, initParent, cloneBoard.getBoxesPlayed(), xPlayer, oPlayer, true);
            curNode = decisionTree;
            move = curNode.move;

        } else if (curNode == null) {
            //Throw an exception
        } else {
            move = curNode.children.get(0).move;
        }

        if (move != null) {
            moveBox = move.getBox();
        }
//        board.playBox(moveBox.getRow(), moveBox.getColumn(), board.getPlayerByName(Player.O_PLAYER));
        return board.findBoxFromCoords(moveBox.getRow(), moveBox.getColumn());
    }

    public boolean hasTree() {
        return decisionTree != null;
    }

    private MoveTreeNode minimax(Board board, Box positedMove, MoveTreeNode parentNode, int depth, Player player1, Player player2, boolean isMaximizingPlayer) {
        Move bestMove = new Move(positedMove, 0);
        ArrayList<Integer> possibleValues = new ArrayList<Integer>();
        MoveTreeNode bestMoveNode = null;
        if(isMaximizingPlayer) {
            if (positedMove != null) {
                board.playBox(positedMove.getRow(), positedMove.getColumn(), player1);
            }
            if (player1.hasWon()) {
                bestMove = new Move(positedMove, -1);
                bestMoveNode = new MoveTreeNode(bestMove);
                parentNode.children.add(bestMoveNode);
                parentNode.move.value = -1;
                return bestMoveNode;
            } else if (board.isBoardFull()) {
                bestMove = new Move(positedMove, 0);
                bestMoveNode = new MoveTreeNode(bestMove);
                parentNode.children.add(bestMoveNode);
                parentNode.move.value = 0;
                return bestMoveNode;
            }
            int bestValue = Integer.MIN_VALUE;
            for (Box possibleMove : board.getBoxes()) {
                if (possibleMove.getValue().compareTo(Box.BLANK_VALUE) == 0) {
                    int value;
                    Board cloneBoard = board.clone();
                    Player cloneplayer1 = cloneBoard.getPlayerByName(player1.getName());
                    Player cloneplayer2 = cloneBoard.getPlayerByName(player2.getName());
                    Move accMove = new Move(possibleMove.clone(), 0);
                    MoveTreeNode accNode = new MoveTreeNode(accMove);
                    MoveTreeNode child = minimax(cloneBoard, possibleMove.clone(), accNode, depth + 1, cloneplayer1, cloneplayer2, false);
                    value = child.move.getValue();
                    possibleValues.add(value);
                    if (value > bestValue) {
                        bestValue = value;
                        parentNode.move.value = bestValue;
                        bestMoveNode = accNode;
                    }
                }
            }

            parentNode.children.add(bestMoveNode);
        } if(!isMaximizingPlayer) {
            if (positedMove != null) {
                board.playBox(positedMove.getRow(), positedMove.getColumn(), player2);
            }
            if(player2.hasWon()) {
                bestMove = new Move(positedMove, 1);
                bestMoveNode = new MoveTreeNode(bestMove);
                parentNode.children.add(bestMoveNode);
                parentNode.move.value = 1;
                return new MoveTreeNode(bestMove);
            } else if (board.isBoardFull()) {
                bestMove = new Move(positedMove, 0);
                bestMoveNode = new MoveTreeNode(bestMove);
                parentNode.children.add(bestMoveNode);
                parentNode.move.value = 0;
                return bestMoveNode;
            }

            int bestValue = Integer.MAX_VALUE;
            for (Box possibleMove : board.getBoxes()) {
                if (possibleMove.getValue().compareTo(Box.BLANK_VALUE) == 0) {
                    int value;
                    Board cloneBoard = board.clone();
                    Player cloneplayer1 = cloneBoard.getPlayerByName(player1.getName());
                    Player cloneplayer2 = cloneBoard.getPlayerByName(player2.getName());
                    Move accMove = new Move(possibleMove.clone(), 0);
                    MoveTreeNode accNode = new MoveTreeNode(accMove);
                    MoveTreeNode child = minimax(cloneBoard, possibleMove.clone(), accNode, depth + 1, cloneplayer1, cloneplayer2, true);
                    value = accNode.move.getValue();

                    possibleValues.add(value);

                    if (value < bestValue) {
                        bestValue = value;
                        parentNode.move.value = bestValue;
                        bestMoveNode = accNode;
                    }
                    parentNode.children.add(accNode);
                }
            }
        }
        return bestMoveNode;
    }

    private class MoveTreeNode {
        private Move move;
        private List<MoveTreeNode> children;

        public MoveTreeNode(Move move) {
            this.move = move;
            children = new ArrayList<MoveTreeNode>();
        }
    }

    public class Move {
        private Box box;
        private int value;
        public Move (Box box, int value) {
            this.box = box;
            this.value = value;
        }

        public Box getBox() {
            return box;
        }

        public int getValue() {
            return value;
        }
    }

}
