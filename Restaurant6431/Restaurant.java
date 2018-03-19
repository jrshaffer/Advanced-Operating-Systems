// Joseph Shaffer
// shaffer.567
// CSE 6431 Advanced Operating Systems
// Spring 2017
// Multi-Threaded Programming Assignment

import java.io.*;
import java.util.*;


public class Restaurant {
	
	public static void main(String[] args) throws Exception {
		// Get data file
		File f = new File(args[0]);
		if (!f.exists()) {
			System.out.println("File does not exist");
		}
		
		// read data file
		BufferedReader b = new BufferedReader(new FileReader(f));
		
		// number of diners
		String input = b.readLine().trim();
		int dinersNum = Integer.parseInt(input);

		// Initiate number of tables
		input = b.readLine().trim();
		int tablesNum = Integer.parseInt(input);
		Tables tables = new Tables(tablesNum);
		
		// number of cooks
		input = b.readLine().trim();
		int cooksNum = Integer.parseInt(input);
		
		// set number of orders to number of diners
		Orders orders = new Orders(dinersNum);
		// initiate machines for cooking
		Machines machine = new Machines();
		// Map to link diners to when they arrive, sit, food is prepared, receive food and leave
		HashMap<Integer, String> dinerInfo= new HashMap<Integer, String>();
		
		// map to link diners to when they leave to find last diner
		HashMap<Integer, Integer> leaveTimes = new HashMap<Integer, Integer>();
		
		// initiate diners map
		for (int i = 0; i < dinersNum; i++) {
			dinerInfo.put(i + 1, "");
		}
		
		// start cook thread
		for (int i = 0; i < cooksNum; i++) {
			Cooks cook = new Cooks(i + 1, orders, machine, dinerInfo);
			cook.start();
		}
		
		// start diner thread with arrival time and orders
		Scanner scan;
		for (int i = 0; i < dinersNum; i++) {
			scan = new Scanner(b.readLine());
			int arrive = scan.nextInt();
			int burger = scan.nextInt();
			int fries = scan.nextInt();
			int soda = scan.nextInt();
			int iceCream = scan.nextInt();
			Diners diner = new Diners(i + 1, arrive, burger, fries, soda, iceCream, orders, tables, dinerInfo, leaveTimes);
			diner.start();	
		}
		
		
		// close buffer reader
		b.close();
	}

}
