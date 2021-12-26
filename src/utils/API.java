package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;


import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * Class cung cap phuong thuc giup gui request len server va nhan du lieu tra ve
 * Date: 12/12/2021
 * @author Admin
 * @version 1.0
 */
public class API {
	/**
	 * Thuoc tinh giup format ngay thang theo dinh dang
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/**
	 * Thong tin Log ra console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());
	
	/**
	 * Phuong thuc giup goi cac API dang get
	 * @param url: Duong dan toi server can request 
	 * @param token: Doan ma cung cap de xac thuc nguoi dung
	 * @return respone: phan hoi ve phia server
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception
	{
		HttpURLConnection conn = setupConnection(url, "GET", token);
		
		String respone = readRespone(conn);
		return respone;
	}

	/**
	 * @param url: Duong dan toi server can connect
	 * @param method: Khoi tao gia tri conn de ket noi voi server
	 * @param token: Ma xac thuc cua nguoi dung
	 * @return: HttpURLConnection doi tuong se connect voi server
	 * @throws IOException
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token) throws IOException
	{
		//setup
		LOGGER.info("Request URL:" + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authoriztion", "Bearer" + token);
		return conn;
	}
	
	/**
	 * @param conn: Doi tuong connect voi server
	 * @return respone: Phan hoi ve phia cua server
	 * @throws IOException
	 */
	private static String readRespone(HttpURLConnection conn) throws IOException
	{
		//Doc du lieu tra ve cua server
		BufferedReader in;
		String inputLine;
		
		if(conn.getResponseCode() / 100 == 2)
		{
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}
		else
		{
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder respone = new StringBuilder();
		while((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		respone.append(inputLine + "\n");
		in.close();
		LOGGER.info("Respone Info: " + respone.substring(0, respone.length()-1).toString());
		return respone.substring(0, respone.length()-1).toString();
	}

	int var;

	/**
	 * Phuong thuc giup goi cac API dang POST(Thanh toan,...)
	 * @param url: Duong dan toi server can request
	 * @param data: Du lieu can dua len server de xu ly (Dung Json)
	 * @param token: Ma xac thuc cua nguoi dung
	 * @return response: Phan hoi tu server(Dang string)
	 * @throws IOException
	 */
	public static String post(String url, String data, String token) throws IOException {
		allowMethods("PATCH");

		HttpURLConnection conn = setupConnection(url, "GET", token);
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();
		
		String response = readRespone(conn);
		
		return response;
	}

	/**
	 * Phuong thuc cho phep goi cac loai giao thuc API khac nhau nhu PATCH, PUT, ...(Chi hoat dong voi java 11)
	 * @deprecated Chi hoat dong voi Java <=11
	 * @param methods: Giao thuc can cho phep (PATCH, PUT...)
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
