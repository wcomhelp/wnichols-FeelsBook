package com.williamnichols.wnichols_feelsbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "feelsbook.sav";

    private ArrayList<Emotion> emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
        super.onResume();

        loadFromFile();
        updateCountFromEmotionList(emotionList);
    }

    /**
     * This method loads a Json file of FILENAME and reads it into an arrayList.
     * @throws RuntimeException if fromJson throws a JsonSyntaxException or an IOException
     */
    private void loadFromFile() throws RuntimeException {
        try {
            FileInputStream fileInputStream = openFileInput(FILENAME);
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
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
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
     * This method handles the onClick of emotion buttons.
     *
     * First addRecord is called with the view cast to type Button to add the button to
     * the list of emotions.
     * Next updateCount is called to update the emotionCount on the Main screen
     * Lastly the list of emotions is saved with saveToFile.
     *
     * @param view the view of the button pressed
     */
    public void handleEmotion(View view) {
        addRecord((Button) view);
        saveToFile();
    }

    /**
     * This method handles the onClick of the View History button.
     *
     * The intent is changed to viewHistory.
     *
     * @param view the view of the button pressed
     */
    public void handleViewHistory(View view) {
        Intent intent = new Intent(MainActivity.this, viewHistory.class);
        startActivity(intent);
    }

    /**
     * This method adds the record of the emotion to the emotionList.
     *
     * First a new instance of the emotion class is created.
     * Next the properties are added to the new new instance of emotion.
     * Next the emotion is saved in the arrayList of emotions, emotionList.
     * Lastly the count on screen is updated.
     *
     * @param emotionButton the button pressed
     */
    public void addRecord(Button emotionButton) {
        Emotion emotion = new Emotion();
        emotion.setEmotionName((String)emotionButton.getText());
        emotion.setEmotionDate(new Date());
        EditText commentText = findViewById(R.id.commentText);
        String commentMessage = commentText.getText().toString();
        commentText.clearFocus();
        commentText.setText("");
        emotion.setEmotionMessage(commentMessage);
        this.emotionList.add(emotion);
        int emotionCount = Integer.parseInt(
                ((TextView) findViewById(getEmotionCountID(emotion))).getText().toString());
        emotionCount++;
        ((TextView) findViewById(getEmotionCountID(emotion))).setText(String.valueOf(emotionCount));
        saveToFile();
    }

    /**
     * This method updates the emotionCounts on the Main screen when given a emotionList
     *
     * Checks which button has the same ID as the button pressed,
     * sets emotionCountID to the ID of the count of the associated button.
     * Next the count is increased and updated.
     *
     * @param emotionList the ArrayList<Emotion> that you would like to use to update the counts
     */
    public void updateCountFromEmotionList(ArrayList<Emotion> emotionList) {
        resetCounters();
        for (int i = 0; i < emotionList.size(); i++) {
            int emotionCountID = getEmotionCountID(emotionList.get(i));
            int emotionCount = Integer.parseInt((
                    (TextView) findViewById(emotionCountID)).getText().toString());
            emotionCount++;
            ((TextView) findViewById(emotionCountID)).setText(String.valueOf(emotionCount));
        }
    }

    /**
     * This method gets the ID of the emotion count that matches the name of the input Emotion
     *
     * First emotion.getEmotionName gets the emotion name and this is matched against each
     * buttons name. If a match occurs, the ID of the corresponding emotion count is returned.
     *
     * @param emotion the Emotion to check the name of
     * @return id of the emotion count
     * @throws RuntimeException if no emotion matches the text of the input Emotion.
     */
    public int getEmotionCountID(Emotion emotion) {
        if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion1Button)).getText().toString())) {
            return R.id.emotion1Count;
        }
        else if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion2Button)).getText().toString())) {
            return R.id.emotion2Count;
        }
        else if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion3Button)).getText().toString())) {
            return R.id.emotion3Count;
        }
        else if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion4Button)).getText().toString())) {
            return R.id.emotion4Count;
        }
        else if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion5Button)).getText().toString())) {
            return R.id.emotion5Count;
        }
        else if (emotion.getEmotionName().equals(
                ((Button) findViewById(R.id.emotion6Button)).getText().toString())) {
            return R.id.emotion6Count;
        }
        else {throw new RuntimeException();}
    }
    public void resetCounters() {
        ((TextView) findViewById(R.id.emotion1Count)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.emotion2Count)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.emotion3Count)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.emotion4Count)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.emotion5Count)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.emotion6Count)).setText(String.valueOf(0));
    }
}
