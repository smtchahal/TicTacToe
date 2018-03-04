package com.sumitchahal.tictactoe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sumitchahal.tictactoe.game.Game;
import com.sumitchahal.tictactoe.game.Point;

public class GameAdapter extends BaseAdapter {

    private int[][] mBoard;
    private Context mContext;

    public GameAdapter(Context context, int[][] board) {
        mBoard = board;
        mContext = context;
    }

    @Override
    public int getCount() {
        return 9; // 3x3 grid
    }

    @Override
    public Integer getItem(int i) {
        Point point = Game.posToPoint(i);
        return mBoard[point.x][point.y];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Drawable oPiece = ContextCompat.getDrawable(mContext, R.drawable.o_piece);
        Drawable xPiece = ContextCompat.getDrawable(mContext, R.drawable.x_piece);
        GridViewItem imageView = new GridViewItem(mContext);
        imageView.setBackgroundColor(Color.WHITE);

        if (getItem(i) == Game.PLAYER_X) {
            imageView.setImageDrawable(xPiece);
        } else if (getItem(i) == Game.PLAYER_O) {
            imageView.setImageDrawable(oPiece);
        }
        return imageView;
    }

    public void update(int[][] board) {
        mBoard = board;
        notifyDataSetChanged();
    }
}
