import java.util.HashMap;

// Joseph Shaffer
// shaffer.567
// CSE 6431
// Programming Assignment
// customers/diners class for restaurant

public class Diners extends Thread {
	public int arrive = 0;
	public int burger = 0;
	public int fries = 0;
	public int soda = 0;
	public int iceCream = 0;
	public int id = 0;
	public int time = 0;
	public Tables tables;
	public Tables.Table table;
	public Orders orders;
	public Orders.Order order;
	public int lastDinerID = 0;
	public int lastDinerTime = 0;
	public HashMap<Integer, String> dinerInfo;
	public HashMap<Integer, Integer> leaveTimes;
	
	// initiate variables for each diner
	public Diners(int id, int arrive, int burger, int fries, int soda, int iceCream, Orders orders, Tables tables, HashMap<Integer, String> dinerInfo, HashMap<Integer, Integer> leaveTimes) {
		this.id = id;
		this.arrive = arrive;
		this.burger = burger;
		this.fries = fries;
		this.soda = soda;
		this.iceCream = iceCream;
		this.orders = orders;
		this.tables = tables;
		this.dinerInfo = dinerInfo;
		this.leaveTimes = leaveTimes;
	}

	public void run() {
		try {
			// add arrival time to output for each diner
			String info = dinerInfo.get(id);
			info += "Diner " + id + " arrives at time " + arrive + " minutes \n";
			// have person sit at first available table
			table = tables.sit();
			
			// adjust when they sit at table by table availability
			if (arrive > table.time) {
				time = arrive;
			} else {
				time = table.time;
			}
			
			
			// add when they are seated to each output
			info += "Diner " + id + " is sat at table " + table.id + " at time " + time + " minutes \n";
			dinerInfo.put(id, info);
			
			// create and place order for each diner
			order = orders.new Order(id, burger, fries, soda, iceCream, time, table);
			orders.placeOrder(order);
			
			// check if order is done or wait
			orders.orderStatus(order.id);
			time = order.time;
			
			// adjust time for eating
			time += 30;
			
			// add info for diner leaving
			info = dinerInfo.get(id);
			info += "Diner " + id + " leaves Table " + table.id + " at time " + time + " minutes \n";
			dinerInfo.put(id, info);
			
			// set table to be available at new time
			order.table.setTime(time);
			tables.leave(table);
			
			// add leaving time to map for each diner
			leaveTimes.put(id, time);
			
			// check if all diners are done, if so print info and last diner leaving time
			if (orders.allOrdersCompleted()) {
				for (int i = 0; i < dinerInfo.keySet().size(); i++) {
					System.out.println(dinerInfo.get(i + 1));
					if (leaveTimes.get(i+1) > lastDinerTime) {
						lastDinerTime = leaveTimes.get(i+1);
						lastDinerID = i+1;
					}
				}
				
				System.out.format("The final diner is %d and leaves at time %d minutes \n", lastDinerID, lastDinerTime);
				
				// exit system
				System.exit(0);
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
