package com.interview.leah.tictactoe.model;

import android.widget.ImageView;

/**
 * Created by leah on 6/16/15.
 */
public class Box {
    public static String BLANK_VALUE = "BLANK";

    private String value;
    private ImageView view;
    private int row;
    private int column;
    public Box(ImageView view, int row, int column) {
        this.row = row;
        this.column = column;
        this.view = view;
        value = BLANK_VALUE;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public ImageView getView() {
        return this.view;
    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.row;
    }

    public Box clone() {
        // Kind of hacky--I don't want the AI to update the visible game board.
        Box returnBox = new Box(null, row, column);
        returnBox.setValue(value);
        return returnBox;
    }
}
