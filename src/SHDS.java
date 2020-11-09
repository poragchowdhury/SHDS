import java.util.*;
import java.time.LocalDateTime;

public class SHDS implements Runnable{
	public Config config;
	public Preferences preferences;
	public SHDS () {
		config = new Config();
		preferences = new Preferences();
	}
	
	public void run() {
		while (!Thread.interrupted()) {
			int min = LocalDateTime.now().getMinute();
			int hour = LocalDateTime.now().getHour(); 
			int sec =  LocalDateTime.now().getSecond();
			// System.out.println(Thread.currentThread().getName() + " hour " + hour + " min " + min + " sec " + sec);
			int currentTime = hour*100+min;
			try {
				for(String item : Preferences.map.keySet()) {
					ArrayList<ArrayList<Integer>> preferences = Preferences.map.get(item);
					for(Integer on_time : preferences.get(0)) {
						if(currentTime == on_time)
							RestCall.executePostCommand(item, "ON");
					}
					
					for(Integer off_time : preferences.get(1)) {
						if(currentTime == off_time)
							RestCall.executePostCommand(item, "OFF");
					}
				}
				// thread to sleep for CHECK_TIME * 1000 milliseconds
				Thread.sleep(Config.CHECK_TIME * 1000);
			} 
			catch (InterruptedException iex) {
				System.out.println("Exiting Thread...");
				break;
			}
			catch (Exception expection) {
				System.out.println("Unexpected exection...");
				expection.printStackTrace();
				break;
			}
		}
		System.out.println("SHDS Thread Stopped.");
	}
	

	public static void main(String [] args) {
		Thread shdsThread = new Thread(new SHDS());
		shdsThread.setName("Smart Home Device Schedular");
		shdsThread.start();
		System.out.println("Smart Home Device Schedular Running...");
		System.out.println("Type \"exit\" or \"logout\" to close program.\n");
        Scanner in = new Scanner(System.in); 
		while(true) {
			String input = in.nextLine();
			if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("logout")) {
				shdsThread.interrupt();
				System.out.println("System exiting...");
				break;
			}
		}
		in.close();
	} 
}
