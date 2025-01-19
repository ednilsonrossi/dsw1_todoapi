package br.edi.ifsp.dsw1.model.dao;

class TaskContract {

	private TaskContract() { }
	
	static final String TABLENAME_TASKS = "tasks_tb";
	static final String COLUMN_ID = "id";
	static final String COLUMN_TITLE = "title";
	static final String COLUMN_DONE = "done";
	
	//CREATE TABLE tasks_tb (id INTEGER AUTOINCREMENT PRIMARY KEY, title VARCHAR(255) NOT NULL, done BOOLEAN DEFAULT FALSE);
	static final String CREATE_TABLE = "CREATE TABLE " + TABLENAME_TASKS + " (" + COLUMN_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " + COLUMN_TITLE + " VARCHAR(255) NOT NULL, " + COLUMN_DONE + " BOOLEAN DEFAULT FALSE)";
}
