package com.interview.leah.tictactoe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.interview.leah.tictactoe.controller.AI;
import com.interview.leah.tictactoe.model.Board;
import com.interview.leah.tictactoe.model.Box;
import com.interview.leah.tictactoe.model.OnWinListener;
import com.interview.leah.tictactoe.model.Player;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class TicTacToeActivityFragment extends Fragment {

    private Board board;
    private View rootView;

    public TicTacToeActivityFragment() {
        board = new Board();
        board.setOnWinListener(new OnWinListener() {
            @Override
            public void onWin(Player player) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(player.getName() + " wins!");
                builder.setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

            @Override
            public void onDraw() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Draw!");
                builder.setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        bindViews(rootView);
        return rootView;
    }

    private void bindViews(View rootView) {
        this.rootView = rootView;
        Resources r = getResources();
        String name = getActivity().getPackageName();
        ImageView imageView;

        for (int i=0; i < Board.BOARD_SIZE; i++) {
            for (int j=0; j < Board.BOARD_SIZE; j++) {
                int id = r.getIdentifier("imageView" + i + j, "id", name);
                imageView = (ImageView) rootView.findViewById(id);
                if (imageView != null) {
                    imageView.setOnClickListener(new TicTacToeBoxListener());
                    board.addBox(imageView, i, j);
                }
            }
        }
    }

    public void updatedUiForAi(Box box, Box playedBox) {
        board.playBox(box.getRow(), box.getColumn(), board.getPlayerByName(Player.O_PLAYER));
        AI.getInstance().advanceTree(box.getRow(), box.getColumn());
        ((ImageView) box.getView()).setImageDrawable(getResources().getDrawable(R.drawable.o));
    }

    private class TicTacToeBoxListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            if (view instanceof ImageView && board.contains((ImageView) view)) {
                final Box playedBox = board.findBoxFromView((ImageView) view);
                if (playedBox.getValue().compareTo(Box.BLANK_VALUE) == 0) {
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.x));
                    board.playBox(playedBox, board.getPlayerByName(Player.X_PLAYER));
                    AI.getInstance().advanceTree(playedBox.getRow(), playedBox.getColumn());

                    if (!board.isBoardFull()) {
                        AsyncTask<Void, Void, Box> aiTask = new AsyncTask<Void, Void, Box>() {
                            private ProgressDialog dialog = null;

                            @Override
                            protected void onPreExecute() {
                                if (!AI.getInstance().hasTree()) {
                                    dialog = ProgressDialog.show(getActivity(), "Calculating", "Please wait...", true);
                                }
                                super.onPreExecute();
                            }

                            @Override
                            protected Box doInBackground(Void... params) {
                                return AI.getInstance().moveForAI(board, playedBox.getRow(), playedBox.getColumn());
                            }

                            @Override
                            protected void onPostExecute(Box box) {
                                Log.d("Leah", "Post Executing!");
                                updatedUiForAi(box, playedBox);
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        };
                        aiTask.execute();
                    }
                }
            }
        }
    }
}
