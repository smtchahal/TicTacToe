package com.sumitchahal.tictactoe.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.sumitchahal.tictactoe.GameAdapter;
import com.sumitchahal.tictactoe.R;
import com.sumitchahal.tictactoe.game.Game;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String PREF_GAME_PC_SCORE = "pref_game_pc_score";
    private static final String PREF_GAME_TIE_SCORE = "pref_game_tie_score";
    private static final String PREF_GAME_OBJECT = "pref_game_object";

    //private static final String LOG_TAG = "MainActivity";

    private Game mGame;
    private GameAdapter mAdapter;
    private TextView mResultView;
    private TextView mPcScoreView;
    private TextView mTiedScoreView;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI references
        mResultView = (TextView) findViewById(R.id.text_result);
        mPcScoreView = (TextView) findViewById(R.id.text_pc_score);
        mTiedScoreView = (TextView) findViewById(R.id.text_tied_score);

        mPrefs = getPreferences(MODE_PRIVATE);

        mAdapter = new GameAdapter(this, new int[3][3]);

        GridView gridView = (GridView) findViewById(R.id.grid_main);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);

        Button youFirstButton = (Button) findViewById(R.id.button_you);
        Button pcFirstButton = (Button) findViewById(R.id.button_pc);
        youFirstButton.setOnClickListener(this);
        pcFirstButton.setOnClickListener(this);

        updateScoreViews();
        restoreGameObject();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGameObject();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_pc) {
            mGame = new Game(Game.PLAYER_X);
        } else if (view.getId() == R.id.button_you) {
            mGame = new Game(Game.PLAYER_O);
        }
        mAdapter.update(mGame.getBoard());
        mResultView.setText(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        if (mGame != null && !mGame.isOver() && mGame.getBoardItem(Game.posToPoint(pos)) == 0) {
            mGame.play(Game.posToPoint(pos));
            mAdapter.update(mGame.getBoard());
            if (mGame.isOver()) {
                SharedPreferences.Editor editor = mPrefs.edit();
                if (mGame.hasXWon()) {
                    mResultView.setText(R.string.result_you_lose);
                    int pcScore = Integer.parseInt(mPcScoreView.getText().toString());
                    ++pcScore;
                    editor.putInt(PREF_GAME_PC_SCORE, pcScore);
                } else {
                    mResultView.setText(R.string.result_game_tied);
                    int tiedScore = Integer.parseInt(mTiedScoreView.getText().toString());
                    ++tiedScore;
                    editor.putInt(PREF_GAME_TIE_SCORE, tiedScore);
                }
                editor.apply();
                updateScoreViews();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_scores) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.remove(PREF_GAME_TIE_SCORE);
            editor.remove(PREF_GAME_PC_SCORE);
            editor.apply();
            updateScoreViews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateScoreViews() {
        mPcScoreView.setText(String.valueOf(mPrefs.getInt(PREF_GAME_PC_SCORE, 0)));
        mTiedScoreView.setText(String.valueOf(mPrefs.getInt(PREF_GAME_TIE_SCORE, 0)));
    }

    private void saveGameObject() {
        SharedPreferences.Editor editor = mPrefs.edit();
        if (mGame.isOver()) {
            editor.remove(PREF_GAME_OBJECT);
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(mGame);
            editor.putString(PREF_GAME_OBJECT, json);
        }
        editor.apply();
    }

    private void restoreGameObject() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_GAME_OBJECT, null);
        if (json != null) {
            mGame = gson.fromJson(json, Game.class);
            mAdapter.update(mGame.getBoard());
        } else {
            mGame = new Game(Game.PLAYER_O);
        }
    }
}
