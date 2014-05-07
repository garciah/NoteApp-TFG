package com.urjc.noteprototype.account;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class AccElemAdapter extends BaseAdapter {

	protected Activity activity;
	protected List<AccountElem> items;
	private AccountDB database;

	public  AccElemAdapter(Activity activity, List<AccountElem> items) {
		super();
		this.activity = activity;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.row_elem_acc, null);
		}
		final AccountElem e = items.get(position);
		final int p = position;
		TextView t = (TextView) v.findViewById(R.id.titleElemAcc);
		t.setText(e.getTag());
		TextView n = (TextView) v.findViewById(R.id.elemNumAcc);
		n.setText(Float.toString(e.getNum()));
		ImageButton ib1 = (ImageButton) v
				.findViewById(R.id.imageButtonEditAcc);
		ImageButton ib2 = (ImageButton) v.findViewById(R.id.imageButtonDelAcc);

		database = new AccountDB(activity);

		ib1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(activity, EditElementAcc.class);
				i.putExtra(DatabaseHelper.getKeyTag(), e.getTag());
				i.putExtra(DatabaseHelper.getKeyNum(), e.getNum());
				i.putExtra(DatabaseHelper.getKeyRowid(), e.getId());
				i.putExtra(DatabaseHelper.getKeyAcc(), e.getIdAcc());
				activity.startActivityForResult(i, 1);
			}
		});

		ib2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database.open();
				database.deleteElement(e.getId());
				items.remove(p);
				database.close();
				Toast toast1 = Toast.makeText(activity, R.string.msgDelElem,
						Toast.LENGTH_SHORT);
				toast1.show();
				activity.finish();
				Intent i = new Intent(activity, AccElemList.class);
				i.putExtra(DatabaseHelper.getKeyAcc(), e.getIdAcc());
				activity.startActivity(i);
				
			}
		});

		return v;
	}

}