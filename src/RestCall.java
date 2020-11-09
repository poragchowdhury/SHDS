import java.io.*;
import java.net.*;

public class RestCall {
	public static void executePostCommand(String item, String command) throws Exception {
		String url = "http://" + Config.OPENHAB_URL + ":" + Config.OPENHAB_PORT + "/rest/items/"; //"http://localhost:8080/rest/items/";
		URL obj = new URL(url+item);
		
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-Type", "text/plain");
		postConnection.setRequestProperty("Accept", "application/json");
		
		postConnection.setDoOutput(true);
		OutputStream os = postConnection.getOutputStream();
		os.write(command.getBytes());
		os.flush();
		os.close();
		
		int responseCode = postConnection.getResponseCode();
		// System.out.println("Post Response Code :" + responseCode);
		// System.out.println("Post Response Message :" + postConnection.getResponseMessage());
		
		if(responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// System.out.println(response.toString());
		}
		else if(responseCode == HttpURLConnection.HTTP_OK) {
			// success : no return message.
		}
		else {
			// System.out.println("NO POST WORKED");
		}
	}
}
