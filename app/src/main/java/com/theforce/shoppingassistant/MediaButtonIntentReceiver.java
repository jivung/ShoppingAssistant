package com.theforce.shoppingassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import android.os.Vibrator;

import android.view.KeyEvent;


/**
 * Created by callebalck on 2016-04-24.
 */
public class MediaButtonIntentReceiver extends BroadcastReceiver {

    static final int check = 1111;

    public MediaButtonIntentReceiver(){
        super();
        System.out.println("MediaButtonIntentReceiver");
    }

    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        System.out.println(intentAction);
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }

        KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        System.out.println(action);
        if (action == KeyEvent.ACTION_DOWN) {

            System.out.println("VOICE!");

            /*Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter your command");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(50);
            abortBroadcast();*/
            abortBroadcast();

        }

        //abortBroadcast();
    }
}
