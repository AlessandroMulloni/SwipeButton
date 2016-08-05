package com.alessandromulloni.swipebutton.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alessandromulloni.swipebutton.SwipeButton;

public class MainActivity extends AppCompatActivity {

    private SwipeButton button1;
    private SwipeButton button2;
    private TextView textView;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (SwipeButton)findViewById(R.id.button1);
        button1.setOnSwipeListener(new SwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeStarted(View view) {
                showMessage("Button 1 swipe started");
            }

            @Override
            public void onSwipeChanged(View view, float level) {
            }

            @Override
            public void onSwipeCancelled(View view) {
                showMessage("Button 1 swipe cancelled");
            }

            @Override
            public void onSwipeConfirmed(View view) {
                showMessage("Button 1 swipe confirmed");
            }
        });

        button2 = (SwipeButton)findViewById(R.id.button2);
        button2.setOnSwipeListener(new SwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeStarted(View view) {
            }

            @Override
            public void onSwipeChanged(View view, float level) {
                showMessage("Button 2 swipe level " + level);
            }

            @Override
            public void onSwipeCancelled(View view) {
            }

            @Override
            public void onSwipeConfirmed(View view) {
            }
        });

        textView = (TextView)findViewById(R.id.textView);
    }

    private void showMessage(String message) {
        textView.setText(message);
    }
}
