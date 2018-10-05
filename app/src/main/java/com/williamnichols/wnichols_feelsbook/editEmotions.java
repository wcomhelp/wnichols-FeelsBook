package com.williamnichols.wnichols_feelsbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class editEmotions extends Activity {

    private String message;
    private ArrayList<Emotion> emotionList;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emotions);

        Intent intent = getIntent();
        message = intent.getStringExtra(viewHistory.EXTRA_MESSAGE);
    }
    @Override
    protected void onResume() {
        super.onResume();

        loadFromFile();
        /* Split on the newlines into emotion, datetime, comment. */
        String[] splitMessage = message.split("\n");
        index = Integer.parseInt(splitMessage[0]);
        /* Set String emotionName to the emotion. */
        String emotionName = splitMessage[1];
        ((TextView) findViewById(R.id.emotion)).setText(emotionName);
        String[] datetimeSplit = splitMessage[2].split("T");
        /* Set String emotionDate to the date. */
        String emotionDate = datetimeSplit[0];
        ((TextView) findViewById(R.id.oldDate)).setText(emotionDate);
        /* Set String emotionTime to the time. */
        String emotionTime = datetimeSplit[1];
        ((TextView) findViewById(R.id.oldTime)).setText(emotionTime);
        if (splitMessage.length >= 4) { // Only if there is a comment there
            /* Set String emotionComment to the comment. */
            String emotionComment = splitMessage[3];
            ((EditText) findViewById(R.id.commentEditText)).setText(emotionComment);
        }

    }

    /**
     * This method loads a Json file of FILENAME and reads it into an arrayList.
     * @throws RuntimeException if fromJson throws a JsonSyntaxException or an IOException
     */
    private void loadFromFile() throws RuntimeException {
        try {
            FileInputStream fileInputStream = openFileInput(MainActivity.FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Emotion>>(){}.getType();
            emotionList = gson.fromJson(in, listType);
            if (emotionList == null) { // If the file is empty the gson library returns null
                emotionList = new ArrayList<>(); // this handles that.
            }

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            emotionList = new ArrayList<>();
        } catch (JsonSyntaxException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method saves an arrayList into a Json file of FILENAME.
     * @throws RuntimeException if the file is not found or fromJson throws an IOException
     */
    private void saveToFile() {
        try {
            FileOutputStream fileOutputStream = openFileOutput(MainActivity.FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            Gson gson = new Gson();
            gson.toJson(emotionList, out);
            out.flush();

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method handles the onClick of the Submit button.
     *
     *
     * @throws RuntimeException
     * @param view the view of the button pressed
     */
    public void handleSubmit(View view) {
        Emotion emotion = new Emotion();
        emotion.setEmotionName(((TextView) findViewById(R.id.emotion)).getText().toString());
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss");
        String time;
        String date;
        if (((EditText) findViewById(R.id.editDateText)).getText().toString().equals("")) {
            if ((((EditText) findViewById(R.id.editTimeText)).getText().toString()).equals("")) {
                time = ((TextView) findViewById(R.id.oldTime)).getText().toString();
            }
            else {
                time = ((EditText) findViewById(R.id.editTimeText)).getText().toString();
            }
            date = ((TextView) findViewById(R.id.oldDate)).getText().toString();
        }
        else {
            if ((((EditText) findViewById(R.id.editTimeText)).getText().toString()).equals("")) {
                time = ((TextView) findViewById(R.id.oldTime)).getText().toString();
            }
            else {
                time = ((EditText) findViewById(R.id.editTimeText)).getText().toString();
            }
            date = ((TextView) findViewById(R.id.editDateText)).getText().toString();
        }

        Date d;
        try {
            d = ft.parse(date + "T" + time);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        emotion.setEmotionDate(d);
        emotion.setEmotionMessage(((EditText) findViewById(R.id.commentEditText)).getText().toString());
        emotionList.set(index, emotion);
        saveToFile();
        finish();
    }
}
