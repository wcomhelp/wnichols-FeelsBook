package com.williamnichols.wnichols_feelsbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class viewHistory extends Activity {

    public static final String EXTRA_MESSAGE = "com.williamnichols.wnichols_feelsbook.MESSAGE";


    private ArrayList<Emotion> emotionList;
    private ArrayList<String> formattedEmotionList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
    }
    @Override
    protected void onResume() {
        super.onResume();

        loadFromFile();
        updateHistoryList((ListView) findViewById(R.id.historyList));
    }

    /**
     * This method loads a Json file of FILENAME and reads it into an arrayList.
     * @throws RuntimeException if fromJson throws a JsonSyntaxException or an IOException
     */
    private void loadFromFile() {
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
     * This method formats an ArrayList of emotions into the format required.
     *  A new ArrayList<String> formattedEmotionList is created.
     *  A SimpleDateFormat date is created and the ISO 8601 pattern is applied to it.
     *  Each emotion from emotionList is formated and added to the ArrayList formattedEmotionList.
     * @param emotionList the ArrayList<Emotion> to be formats
     * @return formattedEmotionList the ArrayList<String> of emotions in the format required
     */
    private ArrayList<String> formatEmotionList (ArrayList<Emotion> emotionList) {
        ArrayList<String> formattedEmotionList = new ArrayList<>();
        SimpleDateFormat date = new SimpleDateFormat();
        date.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
        for (int i = 0; i < emotionList.size(); i++) {
            formattedEmotionList.add(emotionList.get(i).getEmotionName() +
                    "\n" + date.format(emotionList.get(i).getEmotionDate()) +
                    "\n" + emotionList.get(i).getEmotionMessage());
        }
        return formattedEmotionList;
    }

    private void updateHistoryList(ListView historyList) {
        formattedEmotionList = formatEmotionList(emotionList);
        adapter = new ArrayAdapter<>(this, R.layout.list_item, formattedEmotionList);
        historyList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * This method handles the onClick of the formatted emotions.
     *
     *
     * @param view the view of the formatted emotion pressed
     */
    public void handleFormattedEmotion(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        final TextView textview = (TextView) view;
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(getString(R.string.menuDelete))) {
                    emotionList.remove(
                            findIndexOfStringInAL(formattedEmotionList,
                                    textview.getText().toString()));
                    saveToFile();
                    loadFromFile();
                    updateHistoryList((ListView) findViewById(R.id.historyList));
                }
                else if (item.getTitle().equals(getString(R.string.menuEdit))) {
                    Intent intent = new Intent(viewHistory.this, editEmotions.class);
                    intent.putExtra(EXTRA_MESSAGE, String.valueOf(findIndexOfStringInAL(formattedEmotionList,
                            textview.getText().toString())) +
                            "\n" +
                            textview.getText().toString());
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show(); //show popup menu
    }

    public int findIndexOfStringInAL(ArrayList<String> arrayList, String searchString) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(searchString)) {
                return i;
            }
            else {}
        }
        return -1;
    }
    /*
    public void handleDelete(View view) {
        ((TextView) view).getText();
        emotionList.remove();
        saveToFile();

    }*/

}
