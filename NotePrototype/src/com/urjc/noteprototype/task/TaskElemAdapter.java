package com.urjc.noteprototype.task;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class TaskElemAdapter extends BaseAdapter {

	protected Activity activity;
	protected List<TaskClass> items;
	private TaskDB database;

	public TaskElemAdapter(Activity activity, List<TaskClass> items) {
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
			v = inf.inflate(R.layout.task_row, null);
		}
		final TaskClass e = items.get(position);
		final int p = position;
		final CheckBox c = (CheckBox) v.findViewById(R.id.checkElemTask);
		c.setChecked(e.getCheck());
		TextView t = (TextView) v.findViewById(R.id.titleTask);
		t.setText(e.getName());

		ImageButton ib1 = (ImageButton) v
				.findViewById(R.id.imageButtonEditTask);
		ImageButton ib2 = (ImageButton) v.findViewById(R.id.imageButtonDelTask);

		database = new TaskDB(activity);

		c.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database.open();
				int aux;
				if (c.isChecked()) {
					aux = 1;
				} else {
					aux = 0;
				}
				e.setCheck(c.isChecked());
				database.updateTask(e.getId(), e.getName(), aux);
				database.close();

			}
		});

		ib1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(activity, EditTask.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), e.getName());
				i.putExtra(DatabaseHelper.getKeyRowid(), e.getId());
				int aux;
				if (e.getCheck())
					aux = 1;
				else
					aux = 0;
				i.putExtra(DatabaseHelper.getKeyCheck(), aux);
				activity.startActivityForResult(i, 1);
			}
		});

		ib2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database.open();
				database.deleteTask(e.getId());
				items.remove(p);
				database.close();
				Toast toast1 = Toast.makeText(activity, R.string.msgDelElem,
						Toast.LENGTH_SHORT);
				toast1.show();
				Intent i = new Intent(activity, TaskList.class);
				activity.startActivity(i);
				activity.finish();
			}
		});

		return v;
	}

}
