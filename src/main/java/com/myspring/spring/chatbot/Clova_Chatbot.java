package com.myspring.spring.chatbot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import android.util.Base64;

@RestController
@RequestMapping(value = "/api/chatbot")
public class Clova_Chatbot {

	@GetMapping("/chatbotform")
	public String chatbotForm() {
		return "chatbot";
	}

	@ResponseBody
	@PostMapping("/chatbot")
	public ResponseEntity<?> chatbot(@RequestParam("query") String query, @RequestParam("id") String id) {
		String chatbotMessage = "";

		try {
			String apiURL = "https://6otocwy58w.apigw.ntruss.com/custom/v1/6683/7f03be0b643ccd90eefe2fe6662b999a7e1bfde09c8b1f2097436044d281b123";
			String secretKey = "d2FmeWtrR0V1a3BrVERSckVsbllNZWdjY3hUb2xNSnY=";
			URL url = new URL(apiURL);

			String message = getReqMessage(query, id);
//			System.out.println("##" + message);

			String encodeBase64String = makeSignature(message, secretKey);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json;UTF-8");
			con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

			// post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(message.getBytes("UTF-8"));
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();

			BufferedReader br;
//			System.out.println(responseCode);
			if (responseCode == 200) { // Normal call
//				System.out.println("getResponseMessage:" + con.getResponseMessage());

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String decodedString;
				while ((decodedString = in.readLine()) != null) {
					chatbotMessage = decodedString;
				}
				// chatbotMessage = decodedString;
				in.close();

			} else { // Error occurred

				chatbotMessage = con.getResponseMessage();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
//		System.out.println("chatbotMessage:" + chatbotMessage);
		JSONObject jobj = new JSONObject(chatbotMessage);
//		System.out.println("jobj:" + jobj);
		JSONArray bubbles_array = jobj.getJSONArray("bubbles");
//		System.out.println("bubbles_array:" + bubbles_array);
		JSONObject bubble = (JSONObject) bubbles_array.get(0);
//		System.out.println("bubble:" + bubble);
		JSONObject data = (JSONObject) bubble.get("data");
//		System.out.println("data:" + data);
		
		Map<String, Object> resMap = new HashMap<>();
		
		try {
			resMap.put("description", data.getString("description"));
			try {
				resMap.put("url", data.getString("urlAlias"));
			} catch(Exception e) {
				
			}
		} catch (Exception e) {
			JSONObject cover = (JSONObject) data.get("cover");
//			System.out.println("cover:" + cover);
			JSONObject data2 = (JSONObject) cover.get("data");
//			System.out.println("data2:" + data2);
			resMap.put("description", data2.getString("description"));
			
			JSONArray contentTable = (JSONArray) data.get("contentTable");
//			System.out.println("contentTable:" + contentTable);
			
			List<String> buttonList = new ArrayList<String>();
			for(int i = 0; i < contentTable.length(); i++){
				JSONArray temp = (JSONArray) contentTable.get(i);
				JSONObject res = (JSONObject) temp.get(0);
				JSONObject data3 = (JSONObject) res.get("data");
				String title = (String) data3.get("title");
				buttonList.add(title);
			}

			resMap.put("buttonList", buttonList);
		}
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	public static String makeSignature(String message, String secretKey) {

		String encodeBase64String = "";

		try {
			byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");

			SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
			encodeBase64String = Base64.encodeToString(rawHmac, Base64.NO_WRAP);

			return encodeBase64String;

		} catch (Exception e) {
			System.out.println(e);
		}

		return encodeBase64String;

	}

	public static String getReqMessage(String voiceMessage, String id) {

		String requestBody = "";

		try {

			JSONObject obj = new JSONObject();

			long timestamp = new Date().getTime();

//			System.out.println("##" + timestamp);

			obj.put("version", "v2");
			obj.put("userId", id);
//=> userId is a unique code for each chat user, not a fixed value, recommend use UUID. use different id for each user could help you to split chat history for users.

			obj.put("timestamp", timestamp);

			JSONObject bubbles_obj = new JSONObject();

			bubbles_obj.put("type", "text");

			JSONObject data_obj = new JSONObject();
			data_obj.put("description", voiceMessage);

			bubbles_obj.put("type", "text");
			bubbles_obj.put("data", data_obj);

			JSONArray bubbles_array = new JSONArray();
			bubbles_array.put(bubbles_obj);

			obj.put("bubbles", bubbles_array);
			obj.put("event", "send");

			requestBody = obj.toString();

		} catch (Exception e) {
			System.out.println("## Exception : " + e);
		}

		return requestBody;

	}
}
