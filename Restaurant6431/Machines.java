import java.util.concurrent.locks.ReentrantLock;

// Joseph Shaffer
// shaffer.567
// CSE 6431
// Programming Assignment
// Machines class for each machine to prepare food

// machine objects
public class Machines {
	public final ReentrantLock burgerLock = new ReentrantLock();
	public final ReentrantLock friesLock = new ReentrantLock();
	public final ReentrantLock sodaLock = new ReentrantLock();
	public final ReentrantLock iceCreamLock = new ReentrantLock();
	public int burgerCook = 5;
	public int friesCook = 3;
	public int sodaMake = 2;
	public int iceCreamMake = 1;
	public int timeForBurger = 0;
	public int timeForFries = 0;
	public int timeForIceCream = 0;
	public int timeForSoda = 0;
	
	
	// returns completion time of making burger from when started
	public int makeBurger(int time)  {

		if (timeForBurger < time)
			timeForBurger = time + burgerCook;
		else timeForBurger += burgerCook;
		
		return timeForBurger;
	}
	
	// notifies thread burger machine is available
	public synchronized void burgerCooked() {
		burgerLock.unlock();
	}
	
	// returns completion time of making fries from when started
	public int makeFries(int time) {
		
		if (timeForFries < time)
			timeForFries = time + friesCook;
		else timeForFries += friesCook;
		
		
		return timeForFries;
	}
	
	// notifies threads of cooks when fry machine is available
	public synchronized void friesCooked() {
		friesLock.unlock();
	}
	
	
	// returns completion time of making ice cream from when cook started
	public int makeIceCream(int time) {
		
		if (timeForIceCream < time)
			timeForIceCream = time + iceCreamMake;
		else timeForIceCream += iceCreamMake;
		
		return timeForIceCream;
	}
	
	
	// notifies cook threads that ice cream machine is available
	public synchronized void iceCreamMade() {
		iceCreamLock.unlock();
	}
	
	
	// returns completion time of making a soda from when a cook started
	public int makeSoda(int time) {
		
		if (timeForSoda < time)
			timeForSoda = time + sodaMake;
		else timeForSoda += sodaMake;
		
		return timeForSoda;
	}
	
	
	// notifies cook threads that ice cream machine is available
	public synchronized void sodaPoured() {
		sodaLock.unlock();
	}
}
