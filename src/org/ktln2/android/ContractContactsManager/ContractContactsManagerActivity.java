package org.ktln2.android.ContractContactsManager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.CheckedTextView;
import android.widget.SpinnerAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.FragmentActivity;

// TODO: http://thinkandroid.wordpress.com/2010/01/13/writing-your-own-contentprovider/


/**
 * To avoid deprecated APIs we use Loaders and stuffs in order to
 * request the cursor.
 * 
 * Mainly inspired by this documentation:
 *  http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/LoaderCursor.html
 *  
 * @author gipi <gp@ktln2.org>
 */
public class ContractContactsManagerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	private final String TAG = "ContractContactsManagerActivity";
	private final int ID_CONTACTS = 0;
	private final int ID_GROUPS   = 1;
	private final int ID_RAW_CONTACTS   = 2;
	private final int ID_DATA   = 3;

	private int mID;

	private ListView mListView;
	private SimpleCursorAdapter mAdapter;
	private SimpleCursorAdapter mContactsAdapter;
	private SimpleCursorAdapter mGroupsAdapter;
	private SimpleCursorAdapter mRawContactsAdapter;
	private SimpleCursorAdapter mDataContactsAdapter;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);        
        
		// Create an empty adapter we will use to display the loaded data.
		mContactsAdapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_checkable,
				null,
				new String[] {
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.CONTACT_STATUS
				},
				new int[] {
					R.id.text1,
					R.id.text2
				});

		mGroupsAdapter = new SimpleCursorAdapter(
        		this,
			R.layout.list_item_checkable,
			null,
			new String[] {
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups.ACCOUNT_NAME
			},
			new int[] {
				R.id.text1,
				R.id.text2
        		});

		mRawContactsAdapter = new SimpleCursorAdapter(
			this,
			R.layout.list_item_checkable,
			null,
			new String[] {
				ContactsContract.RawContacts.ACCOUNT_TYPE,
				ContactsContract.RawContacts.ACCOUNT_NAME
			},
			new int[] {
				R.id.text1,
				R.id.text2
			});

		mDataContactsAdapter = new SimpleCursorAdapter(
			this,
			R.layout.list_item_checkable,
			null,
			new String[] {
				ContactsContract.RawContacts.Data.MIMETYPE,
				ContactsContract.RawContacts.Data.DATA1
			},
			new int[] {
				R.id.text1,
				R.id.text2
			});

		mListView = (ListView)findViewById(android.R.id.list);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(findViewById(android.R.id.empty));

		/*
		 * Some problems in trying to make the checkboxes work
		 *
		 * http://stackoverflow.com/questions/2679948/focusable-edittext-inside-listview
		 */
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				android.util.Log.i(TAG, "position: " + position);
				//((CheckedTextView)view.findViewById(R.id.text1)).toggle();
			}
		});
		// set spinner in order to select type of provider
		Spinner spinner = (Spinner)findViewById(R.id.typeSpinner);
		ArrayAdapter aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1,
				new String[] {
					"Contacts",
			     "Groups",
			     "Raw",
			     "RawData"
				});
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View view, int position, long id) {
				android.util.Log.i("@@@@", "position: " + position + " id: " + id);
				switchToAdapter((int)id);
			}

			public void onNothingSelected(AdapterView parent) {
			}
		});
		spinner.setAdapter(aa);

		switchToAdapter(ID_GROUPS);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getSupportLoaderManager().initLoader(ID_CONTACTS, null, this);
		getSupportLoaderManager().initLoader(ID_GROUPS, null, this);
		getSupportLoaderManager().initLoader(ID_RAW_CONTACTS, null, this);
		getSupportLoaderManager().initLoader(ID_DATA, null, this);
	}
    
	/**
	 * 
	 * @param id identifies the adapter to be used to show data
	 */
	private void switchToAdapter(int id) {
		switch(id) {
			case ID_CONTACTS:
				mAdapter = mContactsAdapter;
				break;
			case ID_GROUPS:
				mAdapter = mGroupsAdapter;
				break;
			case ID_RAW_CONTACTS:
				mAdapter = mRawContactsAdapter;
				break;
			case ID_DATA:
				mAdapter = mDataContactsAdapter;
				break;
			default:
				android.util.Log.i("@@@@", "no id");
		}

			mID = id;
			// otherwise the listView doesn't recharge
			//mAdapter.notifyDataSetChanged();
			mListView.setAdapter(mAdapter);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cl = null;
		switch (id) {
			case ID_CONTACTS:
				cl = new CursorLoader(
					this,
					ContactsContract.Contacts.CONTENT_URI, // Uri
					null, // COLUMNS
					null, // WHERE
					null, // WHERE ARGS
					ContactsContract.Contacts.DISPLAY_NAME  // ORDER BY
					);
				break;
			case ID_GROUPS:
				cl = new CursorLoader(
					    this,
					    ContactsContract.Groups.CONTENT_URI, // Uri
					    null, // COLUMNS
					    null, // WHERE
					    null, // WHERE ARGS
					    ContactsContract.Groups.TITLE  // ORDER BY
					    );
				break;
			case ID_RAW_CONTACTS:
				cl = new CursorLoader(
					this,
					ContactsContract.RawContacts.CONTENT_URI, // Uri
					null, // COLUMNS
					null, // WHERE
					null, // WHERE ARGS
					null  // ORDER BY
					);
				break;
			case ID_DATA:
				cl = new CursorLoader(
					this,
					ContactsContract.Data.CONTENT_URI, // Uri
					null, // COLUMNS
					null, // WHERE
					null, // WHERE ARGS
					null  // ORDER BY
					);
				break;
		}

		return cl;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO: use an Array
		switch (loader.getId()) {
			case ID_CONTACTS:
				mContactsAdapter.swapCursor(cursor);
				break;
			case ID_GROUPS:
				mGroupsAdapter.swapCursor(cursor);
				break;
			case ID_RAW_CONTACTS:
				mRawContactsAdapter.swapCursor(cursor);
				break;
			case ID_DATA:
				mDataContactsAdapter.swapCursor(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
	}
}
