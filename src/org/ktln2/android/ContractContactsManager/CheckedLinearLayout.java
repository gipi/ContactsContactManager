package org.ktln2.android.ContractContactsManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Checkable;
import android.widget.CheckedTextView;


/**
 * This layout permits to make the CheckedTextView contained in a ListView item
 * to be checked correctly.
 *
 * TODO: find the CheckedTextView during onLayout() or others "drawing" operations
 *       so to avoid expensive requery findViewById().
 *
 * Inspired from a discussion on google groups
 *  http://groups.google.com/group/android-beginners/msg/7cbb4219544b6360
 */
public class CheckedLinearLayout extends LinearLayout implements Checkable {
	private static final String TAG = "CheckedLinearLayout";
	private CheckedTextView mCheckedTextView;

	public CheckedLinearLayout(Context context) {
		super(context);

		mCheckedTextView = (CheckedTextView)findViewById(R.id.text1);
	}

	public CheckedLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCheckedTextView = (CheckedTextView)findViewById(R.id.text1);
	}

	/*
	public CheckedLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}*/

	@Override
	public boolean isChecked() {
		mCheckedTextView = (CheckedTextView)findViewById(R.id.text1);
		return mCheckedTextView.isChecked();
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckedTextView = (CheckedTextView)findViewById(R.id.text1);
		mCheckedTextView.setChecked(checked);
	}

	@Override
	public void toggle() {
		mCheckedTextView.toggle();
	}
}
