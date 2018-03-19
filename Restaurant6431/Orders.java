// Joseph Shaffer
// shaffer.567
// CSE 6431
// Programming Assignment
// Orders class for orders object


import java.util.concurrent.*;
import java.util.*;

public class Orders {
	public BlockingQueue<Order> queueOrders;
	public int orders;
	public ArrayList<Order> finished = new ArrayList<Order>();
	
	// order class for each specific order
	public class Order {
		public int id;
		public int burger;
		public int fries;
		public int soda;
		public int iceCream;
		public int time = 0;
		public Tables.Table table;
		
		
		// Initiate variables for individual order
		public Order(int id, int burger, int fries, int soda, int iceCream, int time, Tables.Table table) {
			this.id = id;
			this.burger = burger;
			this.fries = fries;
			this.soda = soda;
			this.iceCream = iceCream;
			this.time = time;
			this.table = table;
		}
			
		// check if order is ready
		public synchronized boolean orderReady() {
			if (burger == 0 && fries == 0 && soda == 0 && iceCream == 0) 
				return true;
			else return false;
		}
	}
	
	// initiate orders 
	public Orders(int orders) {
		this.orders = orders;
		queueOrders = new ArrayBlockingQueue<Order>(orders);
	}
	
	// variable to have cooks wait if no orders are available
	public synchronized Order takeOrder() throws Exception {
		while (queueOrders.isEmpty()) {
			wait();
		}
		return queueOrders.poll();
	}
	
	// have diners place order into queue of orders
	public synchronized void placeOrder(Order order) throws Exception {
		queueOrders.put(order);
		notifyAll();
	}
	
	
	// have cooks check if order is finished
	public synchronized void finishedOrder(Order order) {
		finished.add(order);
		notifyAll(); 
	}
	
	// check on status of order
	public synchronized void orderStatus(int id) throws Exception {
		while (orderNotDone(id)) {
			wait();
		}
	}
	
	// checks if order is in the finished orders array
	private boolean orderNotDone(int id) {
		for (Order order : finished) {
			if (order.id == id) {
				return false;
			}
		}
		return true;
	}

	// checks if all orders are done
	public boolean allOrdersCompleted() {
		if (finished.size() == orders) {
			return true;
		} else {
			return false;
		}
	}
	
	
}

