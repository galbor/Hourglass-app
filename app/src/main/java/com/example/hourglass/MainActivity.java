package com.example.hourglass;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    int max_time_seconds = 210;
    int cur_time_seconds;
    EditText minuteText;
    EditText secondText;
    Button mainButton;
    Button pauseButton;
    Button stopButton;

    View background;

    View.OnClickListener stop;
    View.OnClickListener start;
    View.OnClickListener pause;
    View.OnClickListener unpause;
    View.OnClickListener flip;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        minuteText = (EditText) findViewById(R.id.minutes);
        secondText = (EditText) findViewById(R.id.seconds);
        mainButton = (Button) findViewById(R.id.mainbutton);
        pauseButton = (Button) findViewById(R.id.pausebutton);
        stopButton = (Button) findViewById(R.id.stopbutton);
        background = mainButton.getRootView();

//        minuteText.setHintTextColor(Color.BLACK);
//        secondText.setHintTextColor(Color.BLACK);

        stop = new View.OnClickListener() {
            @Override public void onClick(View view) {enterStoppedMode();}};
        start = new View.OnClickListener() {
            @Override public void onClick(View view) {exitStoppedMode();}};
        pause = new View.OnClickListener() {
            @Override public void onClick(View view) {enterPausedMode();}};
        unpause = new View.OnClickListener() {
            @Override public void onClick(View view) {exitPausedMode();}};
        flip = new View.OnClickListener() {
            @Override public void onClick(View view) {
                flipHourglass();}};


        pauseButton.setOnClickListener(pause);
        stopButton.setOnClickListener(stop);


        enterStoppedMode();
    }

    public void enterStoppedMode(){
        enableEditText(minuteText, true);
        enableEditText(secondText, true);

        mainButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);

        cur_time_seconds = max_time_seconds;
        setTimeDisplay();

        setMainButtonStart();
        if (timer != null) timer.cancel();
        background.setBackgroundColor(Color.WHITE);
    }

    public void exitStoppedMode(){
        if (!getMaxTime()) return;
        cur_time_seconds = max_time_seconds;

        enableEditText(minuteText, false);
        enableEditText(secondText, false);

        exitPausedMode();
    }

    public void enterPausedMode(){
        mainButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);

        setMainButtonStart();
        mainButton.setOnClickListener(unpause);

        timer.cancel();
    }

    public void exitPausedMode(){
        mainButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        setMainButtonFlip();

        startTimer();
    }


    protected void setTimeDisplay(){
        secondText.setText(String.valueOf(cur_time_seconds % 60));
        minuteText.setText(String.valueOf(cur_time_seconds / 60));
    }

    /**
     * sets max_time_seconds to the written time
     * @return true iff succesful
     */
    protected boolean getMaxTime(){
        int res = 0;
        res += Integer.parseInt(secondText.getText().toString());
        res += Integer.parseInt(minuteText.getText().toString()) * 60;
        if (res <= 0) return false;
        max_time_seconds = res;
        return true;
    }

    protected void flipHourglass(){
        cur_time_seconds = max_time_seconds - cur_time_seconds;
        setTimeDisplay();

        timer.cancel();
        startTimer();
    }


    private void setMainButtonStart(){
        mainButton.setOnClickListener(start);
        mainButton.setText(R.string.start);
        mainButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.greenish, null));
    }

    private void setMainButtonFlip(){
        mainButton.setOnClickListener(flip);
        mainButton.setText(R.string.flip);
        mainButton.setBackgroundColor(Color.BLUE);
    }

    private void startTimer(){
        timer = new CountDownTimer(1000L *cur_time_seconds, 1000) {
            public void onTick(long millisUntilFinished) {
                cur_time_seconds--;
                setTimeDisplay();
            }

            public void onFinish() {
                background.setBackgroundColor(Color.RED);
            }
        };
        timer.start();
    }

    private void enableEditText(EditText txt, boolean enable){
        txt.setEnabled(enable);
        if (enable) return;

        txt.setHintTextColor(Color.BLACK);
        txt.setTextColor(Color.BLACK);
    }
}