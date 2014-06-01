package org.jivesoftware.openfire.plugin.xmppapns;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushAndroidMessage extends Thread {
	private static final Logger Log = LoggerFactory.getLogger(xmpp2apns.class);
	private String device_token;
	private String data;

	public PushAndroidMessage(String device_token, String data) {
		this.device_token = device_token;
		this.data = data;
	}

	@Override
	public void run() {
		super.run();
		try {

			// 1. URL
			URL url = new URL("https://android.googleapis.com/gcm/send");
			//Log.info("Push =" + url);
			// 2. Open connection
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			// 3. Specify POST method
			conn.setRequestMethod("POST");

			// 4. Set the headers
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Authorization", "key=AIzaSyDYoFjU421-S9tMFrcMhiNAtDIgBj7_fo8");
			conn.setRequestProperty("Sender", "id=521359046762");
			conn.setDoOutput(true);
			String postData = "data.message=" + data + "&registration_id=" + device_token + "";

			byte[] content = postData.getBytes();
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(content);
			outputStream.flush();
			outputStream.close();

			// 6. Get the response
			int responseCode = conn.getResponseCode();
			//Log.info("Push rescode=" + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// 7. Print result
			//Log.info("Android push response =" + response.toString());

		} catch (Exception e) {
			// TODO: handle exception
			Log.error(e.getMessage() + e);
		}

	}

}
