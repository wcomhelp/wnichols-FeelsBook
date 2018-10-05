package com.williamnichols.wnichols_feelsbook;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;

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
import java.util.ArrayList;

public class viewHistory extends Activity {

    private static final String FILENAME = "feelsbook.sav";

    private ArrayList<Emotion> emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        loadFromFile();
        updateHistoryList();
    }

    /**
     * This method loads a Json file of FILENAME and reads it into an arrayList.
     * @throws RuntimeException if fromJson throws a JsonSyntaxException or an IOException
     */
    private void loadFromFile() {
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

    private void updateHistoryList() {

    }
}
