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

    private static final String FILENAME = "feelsbook.sav";

    private ArrayList<Emotion> emotionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadFromFile();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveToFile();

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
        updateCount((Button) view);
        saveToFile();
    }
    }

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
    }
    /* Updates the emotionCount on the Main screen */
    /**
     * This method updates the emotionCount on the Main screen
     *
     * Checks which button has the same ID as the button pressed,
     * sets emotionCountID to the ID of the count of the associated button.
     * Next the count is increased and updated.
     *
     * @param emotionButton the button pressed
     */
    public void updateCount(Button emotionButton) {
        TextView emotionCountID;
        /* Check which button has the same ID as the button pressed if so,
         * set emotionCountID to the id of the count associated with
         * the button. */
        if (emotionButton.getId() == R.id.emotion1Button) {
            emotionCountID = findViewById(R.id.emotion1Count);
        }
        else if (emotionButton.getId() == R.id.emotion2Button) {
            emotionCountID = findViewById(R.id.emotion2Count);
        }
        else if (emotionButton.getId() == R.id.emotion3Button) {
            emotionCountID = findViewById(R.id.emotion3Count);
        }
        else if (emotionButton.getId() == R.id.emotion4Button) {
            emotionCountID = findViewById(R.id.emotion4Count);
        }
        else if (emotionButton.getId() == R.id.emotion5Button) {
            emotionCountID = findViewById(R.id.emotion5Count);
        }
        else if (emotionButton.getId() == R.id.emotion6Button) {
            emotionCountID = findViewById(R.id.emotion6Count);
        }
        else {throw new RuntimeException();}

        int emotionCount = Integer.parseInt(emotionCountID.getText().toString());
        emotionCount++;
        emotionCountID.setText(String.valueOf(emotionCount));
    }
}
