package com.williamnichols.wnichols_feelsbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

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
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "feelsbook.sav";

    private ArrayList<Emotion> emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveInFile();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        loadFromFile();

    }

    private void loadFromFile() {
        try {
            FileInputStream fileInputStream = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Emotion>>(){}.getType();
            emotionList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            emotionList = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            Gson gson = new Gson();
            gson.toJson(emotionList, out);
            out.flush();

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            // TODO: Handle the Exception properly later
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void handleEmotion(View view) {
        addRecord((Button) view);
    }
    }

    public void addRecord(Button emotionButton) {
        Emotion emotion = new Emotion();
        emotion.setEmotionName((String)emotionButton.getText());
        emotion.setEmotionDate(new Date());
        emotion.setEmotionMessage("Testing");
        emotionList.add(emotion);
    }
}
