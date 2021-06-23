import java.util.*;
import java.io.File;
import java.time.LocalDateTime;

public class SHDS implements Runnable{
	public Config config;
	public Preferences preferences;
	private long lastModified;
	public SHDS () {
		config = new Config();
		preferences = new Preferences();
		lastModified = System.currentTimeMillis();
	}
	
	public void setTime(int sec) {
		if(sec != 0) {
			try {
				Thread.sleep((60-sec) * 1000); 
			}
			catch(Exception ex) {
				System.out.println("Unexpected exection...");
				ex.printStackTrace();
				System.exit(0);;
			}
		}
	}
	
	public void run(){
		int min = LocalDateTime.now().getMinute();
		int hour = LocalDateTime.now().getHour(); 
		int sec =  LocalDateTime.now().getSecond();
		
		setTime(sec); // setting the second to 0
		
		while (!Thread.interrupted()) {
			
			//if any of the input files are updated, update the rules file.
			File folder = new File("items");
			File[] itemFiles = folder.listFiles();
			for(File file : itemFiles) {
				//if file lastModified is more recent than local lastModified
				if(file.lastModified() > this.lastModified) {
//					System.out.println("DEBUG: a preference file has been updated.");
					this.preferences = new Preferences();
					this.lastModified = file.lastModified();
				}//end if
//				else {
//					System.out.println("DEBUG: all files are up to date.");
//				}//end else
			}//end for each file
			
			min = LocalDateTime.now().getMinute();
			hour = LocalDateTime.now().getHour(); 
			sec =  LocalDateTime.now().getSecond();
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
		//System.out.println("Smart Home Device Schedular Running...");
		//System.out.println("Type \"exit\" or \"logout\" to close program.\n");
        //Scanner in = new Scanner(System.in); 
//		while(true) {
////			String input = in.nextLine();
////			if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("logout")) {
////				shdsThread.interrupt();
////				System.out.println("System exiting...");
////				break;
////			}
//		}
//		in.close();
	} 
}
