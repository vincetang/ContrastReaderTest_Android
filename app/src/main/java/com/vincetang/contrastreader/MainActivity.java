package com.vincetang.contrastreader;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Switch modeSwitch;
    private Button btnText1, btnText2, btnText3, btnText4;
    private int level;
    private boolean contrastOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CSC428 Reading Experiment");

        modeSwitch = (Switch) findViewById(R.id.modeSwitch);

        contrastOn = false;

        // Buttons for text passages
        btnText1 = (Button) findViewById(R.id.btnText1);
        btnText2 = (Button) findViewById(R.id.btnText2);
        btnText3 = (Button) findViewById(R.id.btnText3);
        btnText4 = (Button) findViewById(R.id.btnText4);

        modeSwitch.setOnClickListener(this);
        btnText1.setOnClickListener(this);
        btnText2.setOnClickListener(this);
        btnText3.setOnClickListener(this);
        btnText4.setOnClickListener(this);

        level = 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_results:
                Log.d("ACTIONBAR", "action_results pressed");
                showResults();

                break;
            case R.id.action_clear_results:
                Log.d("ACTIONBAR", "action_clear_results pressed");
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Clear Results")
                        .setMessage("Are you sure you want to delete all stored results?")
                        .setPositiveButton("Clear Results", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eraseResults();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showResults() {
        String FILENAME = "results_file";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_data");
        File file = new File(myDir, FILENAME);
        StringBuilder results = new StringBuilder();
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    results.append(line);
                }
                br.close();
                
            } catch (IOException e) {
                Log.e("FILEREADER", "Failed to read in results file." + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT);
        }
    }

    private void eraseResults() {
        String FILENAME = "results_file";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_data");
        File file = new File(myDir, FILENAME);
        file.delete();

        Toast.makeText(this,"Results deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.modeSwitch:
                if (modeSwitch.isChecked()) {
                    modeSwitch.setText(R.string.contrast_on);
                    contrastOn = true;
                } else {
                    modeSwitch.setText(R.string.contrast_off);
                    contrastOn = false;
                }
                break;
            case R.id.btnText1:
                loadTextActivity((String) btnText1.getText());
                break;
            case R.id.btnText2:
                loadTextActivity((String) btnText2.getText());
                break;
            case R.id.btnText3:
                loadTextActivity((String) btnText3.getText());
                break;
            case R.id.btnText4:
                loadTextActivity((String) btnText4.getText());
                break;
            default:
                break;
        }
    }

    private void loadTextActivity(String title) {
        Intent intent = new Intent(MainActivity.this, TextActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("level", level);
        intent.putExtra("contrastOn", contrastOn);
        startActivity(intent);
    }

}

