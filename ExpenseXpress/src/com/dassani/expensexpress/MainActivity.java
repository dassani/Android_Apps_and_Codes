package com.dassani.expensexpress;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Boolean blnIsRecordsShown = false;
	private Context dContext;
	private String[] straExpensesType; // = { "Food", "Fuel", "Shopping", "Misc"
										// };
	private String[] straRecords;
	private int intSelectedExpensesType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dContext = this;
		loadMainPage();
	}

	void loadMainPage() {

		final DatabaseManager db = new DatabaseManager(this);
		/**
		 * CRUD Operations
		 * */
		final EditText etDate = (EditText) findViewById(R.id.etDate);
		final EditText etFoodExp = (EditText) findViewById(R.id.etFoodExp);
		final EditText etFuelExp = (EditText) findViewById(R.id.etFuelExp);
		final EditText etShoppingExp = (EditText) findViewById(R.id.etShoppingExp);
		final EditText etMiscExp = (EditText) findViewById(R.id.etMiscExp);
		final TextView tvExpenseType = (TextView) findViewById(R.id.tvExpenseType);

		etDate.setText(returnDate());

		Cursor c = db.getAllTitles();
		// c.getColumnCount(); //this line will give the number of columns in
		// table.
		// c = db.rawQuery("PRAGMA table_info(expenses)", null);
		// if ( c.moveToFirst() ) {
		// do {
		// System.out.println("col: " + c.getString(1));
		// } while (c.moveToNext());
		// }
		//Cursor c1 = new SQLiteDatabase(rawQuery("PRAGMA table_info(expenses)", null));

		straExpensesType = new String[c.getColumnCount()];

		tvExpenseType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Builder builder = new AlertDialog.Builder(dContext);
				builder.setTitle("Expense");
				ListAdapter ladptSearchOptions = new ArrayAdapter<String>(
						dContext, R.layout.list_item_menu, straExpensesType);
				builder.setSingleChoiceItems(ladptSearchOptions,
						intSelectedExpensesType,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int which) {
								MainActivity.this.runOnUiThread(new Runnable() {
									public void run() {
										intSelectedExpensesType = which;
										tvExpenseType
												.setText(straExpensesType[which]);
										dialog.dismiss();
									}
								});
							}
						});
				builder.setOnCancelListener(new OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						dialog.dismiss();
					}
				});
				builder.show();

			}
		});
		Button btnAddRecord = (Button) findViewById(R.id.btnAddRecord);
		final Button btnShowRecords = (Button) findViewById(R.id.btnShowRecords);
		final ListView lvRecords = (ListView) findViewById(R.id.lvRecords);

		btnAddRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etFoodExp.getText().toString().trim().equals("")) {
					Toast.makeText(dContext, "Please fill Food Expense",
							Toast.LENGTH_SHORT).show();
					etFoodExp.requestFocus();
				} else if (etFuelExp.getText().toString().trim().equals("")) {
					Toast.makeText(dContext, "Please fill Fuel Expense",
							Toast.LENGTH_SHORT).show();
					etFuelExp.requestFocus();
				} else if (etShoppingExp.getText().toString().trim().equals("")) {
					Toast.makeText(dContext, "Please fill Shopping Expense",
							Toast.LENGTH_SHORT).show();
					etShoppingExp.requestFocus();
				} else if (etMiscExp.getText().toString().trim().equals("")) {
					Toast.makeText(dContext, "Please fill Misc Expense",
							Toast.LENGTH_SHORT).show();
					etMiscExp.requestFocus();
				} else if (etDate.getText().toString().trim().equals("")) {
					Toast.makeText(dContext, "Please fill Date DD-MM-YYYY",
							Toast.LENGTH_SHORT).show();
					etDate.requestFocus();
				} else {
					int tempTotal = Integer.parseInt(etFoodExp.getText()
							.toString().trim())
							+ Integer.parseInt(etFuelExp.getText().toString()
									.trim())
							+ Integer.parseInt(etShoppingExp.getText()
									.toString().trim())
							+ Integer.parseInt(etMiscExp.getText().toString()
									.trim());
					db.addExpense(new ExpenseGetSet(etDate.getText().toString()
							.trim(), Integer.parseInt(etFoodExp.getText()
							.toString().trim()), Integer.parseInt(etFuelExp
							.getText().toString().trim()),
							Integer.parseInt(etShoppingExp.getText().toString()
									.trim()), Integer.parseInt(etMiscExp
									.getText().toString().trim()), tempTotal));
					clearFields();
					Toast.makeText(dContext, "Data added successfully",
							Toast.LENGTH_SHORT).show();
					((TextView) findViewById(R.id.tvShowRecords))
							.setVisibility(TextView.GONE);
					((TextView) findViewById(R.id.tvShowRecords)).setText("");
					btnShowRecords.setText(R.string.strShow);
					etDate.setText(returnDate());
					etDate.requestFocus();
				}

			}
		});

		btnShowRecords.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!blnIsRecordsShown) {
					List<ExpenseGetSet> expenses = db.getAllExpenses();
					if (expenses.isEmpty()) {
						Toast.makeText(dContext, "No Records Found",
								Toast.LENGTH_SHORT).show();
					} else {
						// String tempString = "";
						int i = 0;
						straRecords = new String[expenses.size()];
						for (ExpenseGetSet exp : expenses) {
							straRecords[i] = exp.getID() + ", Date:"
									+ exp.getDate() + ", Total:"
									+ exp.getTotalExpenses();
							i++;
							// tempString = tempString.concat("Id: " +
							// exp.getID()
							// + ", Date:" + exp.getDate() + ", Total:"
							// + exp.getTotalExpenses() + "\n");
						}
						lvRecords.setAdapter(new ArrayAdapter<String>(dContext,
								android.R.layout.simple_list_item_1,
								android.R.id.text1, straRecords));
						lvRecords.setVisibility(ListView.VISIBLE);
						// ((TextView) findViewById(R.id.tvShowRecords))
						// .setVisibility(TextView.VISIBLE);
						// ((TextView) findViewById(R.id.tvShowRecords))
						// .setText(tempString);
						// ((TextView) findViewById(R.id.tvShowRecords))
						// .requestFocus();
						btnShowRecords.setText(R.string.strHide);
					}
					blnIsRecordsShown = true;

				} else {
					// ((TextView) findViewById(R.id.tvShowRecords))
					// .setVisibility(TextView.GONE);
					// ((TextView)
					// findViewById(R.id.tvShowRecords)).setText("");
					btnShowRecords.setText(R.string.strShow);
					lvRecords.setVisibility(ListView.GONE);
					blnIsRecordsShown = false;
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void clearFields() {
		((EditText) findViewById(R.id.etDate)).setText("");
		((EditText) findViewById(R.id.etFoodExp)).setText("");
		((EditText) findViewById(R.id.etFuelExp)).setText("");
		((EditText) findViewById(R.id.etShoppingExp)).setText("");
		((EditText) findViewById(R.id.etMiscExp)).setText("");
	}

	private String returnDate() {
		Calendar c = Calendar.getInstance();
		int Day = c.get(Calendar.DATE);
		int Month = c.get(Calendar.MONTH);
		Month += 1;
		int Year = c.get(Calendar.YEAR);
		return (Day + "-" + Month + "-" + Year);
	}

}
