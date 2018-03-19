import java.util.HashMap;
import java.util.concurrent.TimeUnit;


// Joseph Shaffer
// shaffer.567
// CSE 6431
// Programming Assignment
// Cooks class with thread for cooks object


// Cooks class object
public class Cooks extends Thread {
	public int id = 0;
	public Orders orders;
	public Machines machine;
	public Orders.Order order;
	public Tables tables;
	public HashMap<Integer, String> dinerInfo;
	public int time = 0;
	
	
	// Initiate variables for cooks
	public Cooks(int id, Orders orders, Machines machine, HashMap<Integer, String> dinerInfo) {
		this.id = id;
		this.orders = orders;
		this.machine = machine;
		this.dinerInfo = dinerInfo;
	}
	
	public void run() {
		while (true) {
			try {
				
				// have cook take order if available or wait
				order = orders.takeOrder();
				if (time < order.time) {
					time = order.time;
				} 
				// place time for cook starting on order in diner info
				String input = dinerInfo.get(order.id);
				input += "Cook " + id + " starts on order for Diner " + order.id + " at time " + time + " minutes \n";
				dinerInfo.put(order.id, input);
				int startCook = 0;

				
				// while order is not ready have cook prepare order
				while (!order.orderReady()) {
					// cook burger if possible 
					if (order.burger > 0 && machine.burgerLock.tryLock(0, TimeUnit.MICROSECONDS)) { 
						// acquire burger lock
						//machine.acquireBurgerLock();
						order.time = machine.makeBurger(order.time);
							
						input = dinerInfo.get(order.id);
						startCook = order.time - machine.burgerCook;
					
						// add burger info to diner
						input += "Cook " + id + " starts using burger machine for burger " + order.burger + " for Diner " + order.id + " at time " + startCook + " minutes \n";
						dinerInfo.put(order.id, input);
							
						// update burger time
						order.burger--;
						
						// release burger lock
						machine.burgerCooked();
						// update time
						if (time < order.time) {
							time = order.time;
						} 
						
						// cook fries if possible
					}  else if (order.fries > 0 && machine.friesLock.tryLock(0, TimeUnit.MICROSECONDS)) {
						
						order.time = machine.makeFries(order.time);
							
						input = dinerInfo.get(order.id);
						startCook = order.time - machine.friesCook;
						
						// add fries info to diner
						input += "Cook " + id + " starts using fries machine for fries " + order.fries + " for Diner " + order.id + " at time " + startCook + " minutes \n";
						dinerInfo.put(order.id, input);
							
						// update fries count
						order.fries--;
						
						//release fries lock
						machine.friesCooked();
						// update time
						if (time < order.time) {
							time = order.time;
						} 
						
						
						// make soda if possible
					} else if (order.soda > 0 && machine.sodaLock.tryLock(0, TimeUnit.MICROSECONDS)) { 
						
						order.time = machine.makeSoda(order.time);
							
						input = dinerInfo.get(order.id);
						startCook = order.time - machine.sodaMake;
						
						// add soda info to diner
						input += "Cook " + id + " starts using soda machine for soda " + order.soda + " for Diner " + order.id + " at time " + startCook + " minutes \n";
						dinerInfo.put(order.id, input);
							
						// update soda count
						order.soda--;
						// release soda lock
						machine.sodaPoured();
						// update time
						if (time < order.time) {
							time = order.time;
						} 
						
						
						// make ice cream if possible 
					} else if (order.iceCream > 0 && machine.iceCreamLock.tryLock(0, TimeUnit.MICROSECONDS)) { 
						
						order.time = machine.makeIceCream(order.time);
							
						input = dinerInfo.get(order.id);
						
						startCook = order.time - machine.iceCreamMake;
					
						// add ice cream info to diner
						input += "Cook " + id + " starts using ice cream machine for ice cream " + order.iceCream + " for Diner " + order.id + " at time " + startCook + " minutes \n";
						dinerInfo.put(order.id, input);
						
						//update ice cream count
						order.iceCream--;
						
						// release ice cream lock
						machine.iceCreamMade();
						// update time
						if (time < order.time) {
							time = order.time;
						} 
					}
				} 
				
				// update diner info for receiving food
				input = dinerInfo.get(order.id);
				input += "Diner " + order.id + "'s order is completed and receives order at time " + time + " minutes by Cook " + id + "\n";
				dinerInfo.put(order.id, input);
				
				// add order to queue of finished order
				orders.finishedOrder(order);
				
			} catch (Exception e) {
				e.printStackTrace();;
			}
		}
	}
}
