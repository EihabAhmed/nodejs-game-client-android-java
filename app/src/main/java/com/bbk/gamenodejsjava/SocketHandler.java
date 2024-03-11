package com.bbk.gamenodejsjava;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.net.URISyntaxException;
import com.google.gson.Gson;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    private static final String SOCKET_URL = "http://192.168.1.7:3000/";
//    private static final String SOCKET_URL = "http://localhost:3000/";

    final String NEW_MESSAGE = "new_message";
    final String BROADCAST = "broadcast";
    final String NEW_MESSAGE2 = "new_message2";
    final String BROADCAST2 = "broadcast2";

    Socket socket;

    MutableLiveData<Score> onNewScore = new MutableLiveData<>();
    MutableLiveData<Score> onNewScore2 = new MutableLiveData<>();

    SocketHandler() {
        try {
            socket = IO.socket(SOCKET_URL);
            if (socket != null) {
                socket.connect();
            }

            registerOnNewScore();
            registerOnNewScore2();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void registerOnNewScore() {
        if (socket != null) {

            socket.on(BROADCAST, args -> {
                if (args != null) {
                    if (!args[0].toString().isEmpty()) {
                        Log.d("DATADEBUG", "$data");
                        Score score = new Gson().fromJson(args[0].toString(), Score.class);
                        onNewScore.postValue(score);
                    }
                }
            });
        }
    }

    private void registerOnNewScore2() {
        if (socket != null) {

            socket.on(BROADCAST2, args -> {
                if (args != null) {
                    if (!args[0].toString().isEmpty()) {
                        Log.d("DATADEBUG2", "$data");
                        Score score = new Gson().fromJson(args[0].toString(), Score.class);
                        onNewScore2.postValue(score);
                    }
                }
            });
        }
    }

    void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    void emitScore(Score score) {
        String jsonStr = new Gson().toJson(score, Score.class);
        if (socket != null) {
            socket.emit(NEW_MESSAGE, jsonStr);
        }
    }

    void emitScore2(Score score) {
        String jsonStr = new Gson().toJson(score, Score.class);
        if (socket != null) {
            socket.emit(NEW_MESSAGE2, jsonStr);
        }
    }
}
