package com.dassani.expensexpress;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "expenseManager";

	// Expenses table name
	private static final String TABLE_EXPENSES = "expenses";

	// Expenses Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_FOOD_EXP = "food_expense";
	private static final String KEY_FUEL_EXP = "fuel_expense";
	private static final String KEY_SHOPPING_EXP = "shopping_expense";
	private static final String KEY_MISC_EXP = "misc_expense";
	private static final String KEY_TOTAL_EXP = "total_expenses";

	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
				+ KEY_FOOD_EXP + " TEXT," + KEY_FUEL_EXP + " TEXT,"
				+ KEY_SHOPPING_EXP + " TEXT," + KEY_MISC_EXP + " TEXT,"
				+ KEY_TOTAL_EXP + " TEXT" + ")";
		db.execSQL(CREATE_EXPENSES_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

		// Create tables again
		onCreate(db);
	}

	public Cursor getAllTitles() {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query(TABLE_EXPENSES, new String[] { KEY_ID, KEY_DATE,
				KEY_FOOD_EXP, KEY_FUEL_EXP, KEY_SHOPPING_EXP, KEY_MISC_EXP,
				KEY_TOTAL_EXP }, KEY_ID + "=?", new String[] {}, null, null,
				null, null);
	}

	// Adding new expense
	public void addExpense(ExpenseGetSet expense) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, expense.getDate()); // Date of Expense
		values.put(KEY_FOOD_EXP, expense.getFoodExpense()); // Food Expense
		values.put(KEY_FUEL_EXP, expense.getFuelExpense()); // Fuel Expense
		values.put(KEY_SHOPPING_EXP, expense.getShoppingExpense()); // Shopping
																	// Expense
		values.put(KEY_MISC_EXP, expense.getMiscExpense()); // Misc Expense
		values.put(
				KEY_TOTAL_EXP,
				expense.getFoodExpense() + expense.getFuelExpense()
						+ expense.getShoppingExpense()
						+ expense.getMiscExpense()); // Total Expense

		// Inserting Row
		db.insert(TABLE_EXPENSES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single Expense
	public ExpenseGetSet getExpense(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_EXPENSES, new String[] { KEY_ID,
				KEY_DATE, KEY_FOOD_EXP, KEY_FUEL_EXP, KEY_SHOPPING_EXP,
				KEY_MISC_EXP, KEY_TOTAL_EXP }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ExpenseGetSet expense = new ExpenseGetSet(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), Integer.parseInt(cursor
				.getString(2)), Integer.parseInt(cursor.getString(3)),
				Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor
						.getString(5)), Integer.parseInt(cursor.getString(6)));
		// return contact
		return expense;
	}

	// Getting All Expenses
	public List<ExpenseGetSet> getAllExpenses() {
		ArrayList<ExpenseGetSet> expenseList = new ArrayList<ExpenseGetSet>();
		// Select All Query
		String selectQuery = "SELECT " + KEY_ID + "," + KEY_DATE + ","
				+ KEY_TOTAL_EXP + " FROM " + TABLE_EXPENSES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ExpenseGetSet expense = new ExpenseGetSet();
				expense.setID(Integer.parseInt(cursor.getString(0)));
				expense.setDate(cursor.getString(1));
				// expense.setFoodExpense(Integer.parseInt(cursor.getString(2)));
				// expense.setFuelExpense(Integer.parseInt(cursor.getString(3)));
				// expense.setShoppingExpense(Integer.parseInt(cursor.getString(4)));
				// expense.setMiscExpense(Integer.parseInt(cursor.getString(5)));
				expense.setMiscExpense(Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				expenseList.add(expense);
			} while (cursor.moveToNext());
		}

		// return contact list
		return expenseList;
	}

	// Getting expenses Count
	public int getExpensesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_EXPENSES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Updating single expense
	public int updateExpense(ExpenseGetSet contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, contact.getDate());
		values.put(KEY_FOOD_EXP, contact.getFoodExpense());
		values.put(KEY_FUEL_EXP, contact.getFuelExpense());
		values.put(KEY_SHOPPING_EXP, contact.getShoppingExpense());
		values.put(KEY_MISC_EXP, contact.getMiscExpense());

		// updating row
		return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single expense
	public void deleteExpense(ExpenseGetSet expense) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
				new String[] { String.valueOf(expense.getID()) });
		db.close();
	}

}
