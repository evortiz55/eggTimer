package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// set global variables
    int minutes;
    int seconds;
    int time;
    int min;
    int sec;
    boolean timerRunning = false;
    SeekBar timeSeekBar;
    TextView timeTextView;
    Button startStopButton;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        assign seekbar
        timeSeekBar = (SeekBar) findViewById(R.id.timeSeekBar);

//      assign text view
        timeTextView = (TextView) findViewById(R.id.timeTextView);

//        set max seekbar to 5min
        timeSeekBar.setMax(5 * 60);

//        listen for seekbar changes
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minutes = i / 60;
                seconds = (i % 60);
//                input to text view the selected time min and sec
                if (seconds < 10) {
                    timeTextView.setText("0" + minutes +":" + "0" + seconds);
                } else {
                    timeTextView.setText("0" + minutes+":" + seconds);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                once the tracking has stopped we need to set the final time to start the timer
                time = (minutes * 60000) + (seconds * 1000);
            }
        });
    }

    public void startStop(View view) {
//        allow changing the name of the start stop button
        startStopButton = (Button) findViewById(R.id.startStopButton);
//        first we need to check if the counter is running or is stopped
        if (!timerRunning) {
//            we make sure the seekbar is enabled
            timeSeekBar.setEnabled(false);
//            change the button to say stop
            startStopButton.setText("STOP");
//            start the timer
            countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long l) {
                    timerRunning = true;
                    min = (int) (l / 60000) % 60;
                    sec = (int) ((l / 1000) % 60);
//                    make sure the text view is updated every second to the new time
                    if (sec < 10) {
                        timeTextView.setText("0" + min +":" + "0" + sec);
                    } else {
                        timeTextView.setText("0" + min+":" + sec);
                    }
                }

                @Override
                public void onFinish() {
//                    on finish
//                    change the words in the stop button
                    startStopButton.setText("START");
//                    change the bool for the timer running to has ended so false
                    timerRunning = false;
//                    enable the seek bar
                    timeSeekBar.setEnabled(true);
//                    play the sound
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                    mediaPlayer.start();
//                    toast to inform the user to move the seek bar
                    Toast.makeText(getApplicationContext(), "SET NEW TIME (MOVE THE SEEKBAR)", Toast.LENGTH_LONG).show();
                }
            }.start();
        } else {
//            else the timer is running so if we press
//            we can re start from the beginign
            startStopButton.setText("Re-START");
//            enable the seek bar
            timeSeekBar.setEnabled(true);
//            change the bool to the timer has been stopped
            timerRunning = false;
//            cancel the count down timer
            countDownTimer.cancel();
        }
    }

}