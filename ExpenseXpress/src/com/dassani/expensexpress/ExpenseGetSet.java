package com.dassani.expensexpress;

public class ExpenseGetSet {

	// private variables
	int _id;
	String _date;
	int _food_expense;
	int _fuel_expense;
	int _shopping_expense;
	int _misc_expense;
	int _total_expenses;

	// Empty constructor
	public ExpenseGetSet() {

	}

	// constructor
	public ExpenseGetSet(int id, String _date, int _food_expense,
			int _fuel_expense, int _shopping_expense, int _misc_expense,
			int _total_expenses) {
		this._id = id;
		this._date = _date;
		this._food_expense = _food_expense;
		this._fuel_expense = _fuel_expense;
		this._shopping_expense = _shopping_expense;
		this._misc_expense = _misc_expense;
		this._total_expenses = _total_expenses;
	}

	// constructor
	public ExpenseGetSet(String _date, int _food_expense, int _fuel_expense,
			int _shopping_expense, int _misc_expense, int _total_expense) {
		this._date = _date;
		this._food_expense = _food_expense;
		this._fuel_expense = _fuel_expense;
		this._shopping_expense = _shopping_expense;
		this._misc_expense = _misc_expense;
		this._total_expenses = _total_expense;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting date
	public String getDate() {
		return this._date;
	}

	// setting date
	public void setDate(String date) {
		this._date = date;
	}

	// getting food expense
	public int getFoodExpense() {
		return this._food_expense;
	}

	// setting food expense
	public void setFoodExpense(int food_expense) {
		this._food_expense = food_expense;
	}

	// getting fuel expense
	public int getFuelExpense() {
		return this._fuel_expense;
	}

	// setting fuel expense
	public void setFuelExpense(int fuel_expense) {
		this._fuel_expense = fuel_expense;
	}

	// getting shopping expense
	public int getShoppingExpense() {
		return this._shopping_expense;
	}

	// setting shopping expense
	public void setShoppingExpense(int shopping_expense) {
		this._shopping_expense = shopping_expense;
	}

	// getting misc expense
	public int getMiscExpense() {
		return this._misc_expense;
	}

	// setting misc expense
	public void setMiscExpense(int misc_expense) {
		this._misc_expense = misc_expense;
	}

	// getting total expense
	public int getTotalExpenses() {
		return this._food_expense + this._fuel_expense + this._shopping_expense
				+ this._misc_expense;
	}

	// setting total expenses
	public void setTotalExpenses(int total_expenses) {
		this._total_expenses = total_expenses;
	}
}
