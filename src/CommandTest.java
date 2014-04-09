import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sourceforge.jaad.Play;

import utils.Sound;
import utils.SystemInfo;

public class CommandTest{
	private static class Authenticate{
		public String username = "android";
		public String password = "AC7IBG09A3DTSYM4R41UJWL07VLN8JI7";
		public String deviceModel = "android-generic";
		public SubAuthenticate sub = new SubAuthenticate();
	}
	private static class SubAuthenticate{
		public String bogus = "bogus";
		public String someKey = "salt";
	}
	public static void main(String[] args){
//		String to_encrypt = "encrypt";
//		String strkey = "salt";
//		try {
//			SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
//			Cipher cipher = Cipher.getInstance("Blowfish");
//			cipher.init(Cipher.ENCRYPT_MODE, key);
//			System.out.print(to_encrypt + ": ");
//			System.out.println(cipher.doFinal(to_encrypt.getBytes()));
//		} catch (Exception e) {}
//		  
//		Sound sound = new Sound();
//		//sound.play("http://audio-dc6-t3-1.pandora.com/samples/0/2/6/6/730099486620-1-16-30sec-64kbps.mp3");
//		  
//		Play play = new Play();
//		String song = "C:\\Users\\Robby\\Downloads\\background.mp4";
//		try{
//		sound.play(song);
//		}catch(Exception e){e.printStackTrace();}
//		String[] songs = new String[1];
//		songs[0] = song;
//		//play.main(songs);
		
		Authenticate auth = new Authenticate();
		Gson gson = new Gson();
		String json = gson.toJson(auth);
//		System.out.println(json);
		
		String url = "http://tuner.pandora.com/services/json/?method=auth.partnerLogin";
		String response = "";
		
//		try {
//			URLConnection conn = new URL(url).openConnection();
//			conn.setDoOutput(true);
//			
//			OutputStreamWriter output = new OutputStreamWriter(conn.getOutputStream());
//			output.write(json);
//			output.close();
//			
//			BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String responseLine = "";
//			while((responseLine = input.readLine()) != null){
//				response += responseLine;
//			}
//			input.close();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		if(!response.equals("")){
//			gson.
//		}
		
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonObject subObj = obj.get("sub").getAsJsonObject();
		String someKey = subObj.get("someKey").getAsString();
//		System.out.println(someKey);
		
		
		long curTime = System.currentTimeMillis();
		
//		for(int i=0; i<10; i++){
//			curTime = System.currentTimeMillis();
//			SystemInfo.getTasks();
//			System.out.println(System.currentTimeMillis() - curTime);
//		}
		
//		curTime = System.currentTimeMillis();
//		SystemInfo.getServices();
//		System.out.println(System.currentTimeMillis() - curTime);
		
		SystemInfo.getTasks();
		
	}
}

//Username: windowsgadget
//Password: EVCCIBGS9AOJTSYMNNFUML07VLH8JYP0
//deviceId: WG01
//Decrypt password: E#IO$MYZOAB%FVR2
//Encrypt password: %22CML*ZU$8YXP[1