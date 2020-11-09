import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
public class Preferences {
	
	public static HashMap<String, ArrayList<ArrayList<Integer>>> map;
	public Preferences () {
		map = new HashMap<String, ArrayList<ArrayList<Integer>>>();
		readPreferences();
	}
	public void readPreferences() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("items.json"));
			JSONObject jsonObject = (JSONObject)obj;
			JSONArray items = (JSONArray)jsonObject.get("items");
			Iterator<JSONObject> iterator = items.iterator();
			while (iterator.hasNext()) {
				JSONObject item = iterator.next();
				String name = (String) item.get("name");
				ArrayList<ArrayList<Integer>> lists = map.get(name);
				if(lists == null) {
					lists = new ArrayList<ArrayList<Integer>>();
					ArrayList<ArrayList<Integer>> list = new ArrayList<>();
					/* index 0 list is for ON events */
					list.add(new ArrayList<Integer>());
					/* index 1 list is for OFF events */
					list.add(new ArrayList<Integer>());
					lists.addAll(list);
				}
					
				JSONArray on_events = (JSONArray)item.get("ON");
				Iterator<JSONObject> on_events_iterator = on_events.iterator();
				while (on_events_iterator.hasNext()) {
					String onTime = "" + on_events_iterator.next();
					onTime = onTime.replace(":","");
					int on_time = Integer.parseInt(onTime); 
					lists.get(0).add(on_time);
				}
				
				JSONArray off_events = (JSONArray)item.get("OFF");
				Iterator<JSONObject> off_events_iterator = off_events.iterator();
				while (off_events_iterator.hasNext()) {
					String offTime = "" + off_events_iterator.next();
					offTime = offTime.replace(":","");
					int off_time = Integer.parseInt(offTime); 
					lists.get(1).add(off_time);
				}
				map.put(name, lists);				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Read all preferences successfully.");
	}
}
