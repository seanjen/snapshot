package com.census.snapshot;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectCharacter extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private String[] values = new String[] { "Choose from Gallery",
            "Choose Yourself", "Back" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_character);
        mListView = (ListView) findViewById(R.id.selectCharacter);

        fillList();
    }

    private void fillList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                values);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        switch (pos) {
            case 0:
                openActivity(ChooseYourself.class);
                break;
            case 1:
                openActivity(ChooseYourself.class);
                break;
            case 2:
                openActivity(SnapshotMain.class);
                break;
            default:
                break;
        }
    }

    private void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomWorldHelper.sharedWorld = null;
    }

}
