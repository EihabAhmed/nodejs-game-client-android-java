package com.bbk.gamenodejsjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private SocketHandler socketHandler;
    private String userName;
    private int myScore = 0;

    final static String USERNAME = "username";

    TextView selfPlayerTextView;
    TextView selfPlayerScoreTextView;
    TextView otherPlayerTextView;
    TextView otherPlayerScoreTextView;
    Button scoreButton;
    Button score2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        selfPlayerTextView = findViewById(R.id.self_player_textView);
        selfPlayerScoreTextView = findViewById(R.id.self_player_score_textView);
        otherPlayerTextView = findViewById(R.id.other_player_textView);
        otherPlayerScoreTextView = findViewById(R.id.other_player_score_textView);
        scoreButton = findViewById(R.id.score_button);
        score2Button = findViewById(R.id.score2_button);

        userName = getIntent().getStringExtra(USERNAME);

        assert userName != null;
        if (userName.isEmpty()) {
            finish();
        } else {
            selfPlayerTextView.setText(userName);
            selfPlayerScoreTextView.setText("" + myScore);


            socketHandler = new SocketHandler();

            scoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myScore++;

                    Score score = new Score();
                    score.username = userName;
                    score.score = myScore;

                    socketHandler.emitScore(score);
                }
            });

            score2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myScore += 2;

                    Score score = new Score();
                    score.username = userName;
                    score.score = myScore;

                    socketHandler.emitScore2(score);
                }
            });

            socketHandler.onNewScore.observe(this, incomingData -> {
                        if (incomingData.username.equals(userName)) {
                            selfPlayerScoreTextView.setText("" + incomingData.score);
                        } else {
                            otherPlayerTextView.setText(incomingData.username);
                            otherPlayerScoreTextView.setText("" + incomingData.score);
                        }

                        selfPlayerTextView.setTextColor(getResources().getColor(R.color.black));
                        selfPlayerScoreTextView.setTextColor(getResources().getColor(R.color.black));
                        otherPlayerTextView.setTextColor(getResources().getColor(R.color.black));
                        otherPlayerScoreTextView.setTextColor(getResources().getColor(R.color.black));
                    }
            );

            socketHandler.onNewScore2.observe(this, incomingData -> {
                        if (incomingData.username.equals(userName)) {
                            selfPlayerScoreTextView.setText("" + incomingData.score);
                        } else {
                            otherPlayerTextView.setText(incomingData.username);
                            otherPlayerScoreTextView.setText("" + incomingData.score);
                        }

                        selfPlayerTextView.setTextColor(getResources().getColor(R.color.purple_700));
                        selfPlayerScoreTextView.setTextColor(getResources().getColor(R.color.purple_700));
                        otherPlayerTextView.setTextColor(getResources().getColor(R.color.purple_700));
                        otherPlayerScoreTextView.setTextColor(getResources().getColor(R.color.purple_700));
                    }
            );
        }
    }

    @Override
    protected void onDestroy() {
        socketHandler.disconnectSocket();
        super.onDestroy();
    }
}