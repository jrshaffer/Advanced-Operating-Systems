// Joseph Shaffer
// shaffer.567
// CSE 6431
// Programming Assignment
// Tables class, for tables in restaurant object
// Table class for each table object

import java.util.concurrent.*;

public class Tables {
	public BlockingQueue<Table> queueTables;
	
	// Table class for each individual tables
	public class Table {
		public int id;
		public int time;
		
		public Table(int id, int time) {
			this.id = id;
			this.time = time;
		}
		
		public void setTime(int time) {
			this.time = time;
		}
		

	}
	
	// Initiates array of table objects
	public Tables(int num) throws Exception {
		queueTables = new ArrayBlockingQueue<Table>(num);
		for (int i = 0; i < num; i++) {
			Table table = new Table(i + 1, 0);
			queueTables.put(table);
		}
	}
	
	// checks if a person can sit at a table
	public synchronized Table sit() throws Exception {
		while(queueTables.isEmpty()) {
			wait();
		}
		return queueTables.poll();
	}
	
	
	// has diner leave table they you were using
	public synchronized void leave(Table table) throws Exception {
		queueTables.put(table);
		notifyAll();
	}
	
}
