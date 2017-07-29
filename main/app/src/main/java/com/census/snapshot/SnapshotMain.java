package com.census.snapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SnapshotMain extends Activity implements OnItemClickListener {

	private ListView mListView;
	private String[] values = new String[] { "Instructions",
			"Select Character", "Play at current location", "Select new location",
			"Exit" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.main);

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
				break;
			case 1:
				openActivity(SelectCharacter.class);
				break;
			case 2:
				openActivity(CameraWithAttachedView.class);
				break;
			case 3:
				openActivity(GoogleMapActivity.class);
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