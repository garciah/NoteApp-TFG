package com.urjc.noteprototype;

import java.util.ArrayList;
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

public class BuyElemAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ElemBuyList> items;
	private BuyDB database;

	public BuyElemAdapter(Activity activity, ArrayList<ElemBuyList> items) {
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
			v = inf.inflate(R.layout.element_file_buy_list, null);
		}
		final ElemBuyList e = items.get(position);
		final int p = position;
		final CheckBox c = (CheckBox) v.findViewById(R.id.checkElem);
		c.setChecked(e.getCheck());
		TextView t = (TextView) v.findViewById(R.id.titleBuy);
		t.setText(e.getName());
		final TextView n = (TextView) v.findViewById(R.id.textViewNumElem);
		n.setText(Integer.toString(e.getAmount()));
		ImageButton ib1 = (ImageButton) v.findViewById(R.id.imageButtonEdit);
		ImageButton ib2 = (ImageButton) v.findViewById(R.id.imageButtonDel);
		ImageButton ib3 = (ImageButton) v.findViewById(R.id.ImageButtonMore);
		ImageButton ib4 = (ImageButton) v.findViewById(R.id.ImageButtonLess);
		database = new BuyDB(activity);

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
				database.updateElement(e.getId(), e.getName(), e.getAmount(),
						aux);
				database.close();
				n.setText(Integer.toString(e.getAmount()));
			}
		});

		ib1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(activity, EditElement.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), e.getName());
				i.putExtra(DatabaseHelper.getKeyRowid(), e.getId());
				i.putExtra(DatabaseHelper.getKeyAccount(), e.getAmount());
				int aux;
				if (e.getCheck())
					aux = 1;
				else
					aux = 0;
				i.putExtra(DatabaseHelper.getKeyCheck(), aux);
				i.putExtra(DatabaseHelper.getKeyTitlebuy(), e.getIdB());
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
				Intent i = new Intent(activity, BuyElemList.class);
				i.putExtra(DatabaseHelper.getKeyTitlebuy(), e.getIdB());
				activity.startActivity(i);
				activity.finish();
			}
		});

		ib3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database.open();
				int aux;
				if (e.getCheck())
					aux = 1;
				else
					aux = 0;
				if (e.getAmount() < 99) {
					e.setAmount(e.getAmount() + 1);
					database.updateElement(e.getId(), e.getName(),
							e.getAmount(), aux);
				}
				database.close();
				n.setText(Integer.toString(e.getAmount()));
			}
		});

		ib4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database.open();
				int aux;
				if (e.getCheck())
					aux = 1;
				else
					aux = 0;
				if (e.getAmount() > 1) {
					e.setAmount(e.getAmount() - 1);
					database.updateElement(e.getId(), e.getName(),
							e.getAmount(), aux);
				}
				database.close();
				n.setText(Integer.toString(e.getAmount()));
			}
		});

		return v;
	}

}
