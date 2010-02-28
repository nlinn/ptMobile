package me.linnemann.ptmobile.pivotaltracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.util.Log;

/**
 * generic support for GET, POST, PUT, DELETE Http Commands
 * 
 * @author niels
 */
public class RESTSupport {

	private static final String TAG="RESTSupport";
	private int timeout_millis;

	public RESTSupport(int timeout_millis) {
		this.timeout_millis = timeout_millis;
	}

	public InputStream doGET(URL url, Map<String, String> requestProperties) throws IOException {
		return doGET(url, requestProperties, null, null);
	}

	public InputStream doGET(URL url, Map<String, String> requestProperties, String username, String password) throws IOException {
		return doRequest("GET",url, requestProperties, username, password, null);
	}

	public InputStream doPUT(URL url, Map<String, String> requestProperties, InputStream body) throws IOException {
		return doRequest("PUT",url, requestProperties, null, null, body);
	}

	public InputStream doPOST(URL url, Map<String, String> requestProperties, InputStream body) throws IOException {
		return doRequest("POST",url, requestProperties, null, null, body);
	}

	/**
	 * 
	 * @param url
	 * @param requestProperties
	 * @param username set this value for basic authentification.
	 * @param password set this value for basic authentification.
	 * @return
	 * @throws IOException
	 */
	public InputStream doRequest(String Method, URL url, Map<String, String> requestProperties, final String username, final String password, InputStream body) throws IOException {
		InputStream in = null;
		int response = -1;

		/*
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication (username, password.toCharArray());
			}
		});*/

		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))                     
			throw new RuntimeException("Not an HTTP connection");

		if (conn instanceof HttpsURLConnection) {
			Log.d(TAG,"https found");
			((HttpsURLConnection) conn)
			.setHostnameVerifier(new AllowAllHostnameVerifier());
		}

		HttpURLConnection httpConn = (HttpURLConnection) conn;

		// --- add request properties to request
		if (requestProperties != null) {
			for (String k : requestProperties.keySet()) {
				Log.d(TAG,"property "+k+": "+requestProperties.get(k));
				httpConn.setRequestProperty(k, requestProperties.get(k));
			}
		}

		// --- add basic authentification if username/password is set
		if ((username != null) && (password != null)) {
			String credentials = username + ":" + password;
			Log.d(TAG,"credentials "+credentials);
			httpConn.setRequestProperty("Authorization", "Basic " + toString(Base64.encodeBase64((credentials.getBytes()))));	
		}

		httpConn.setAllowUserInteraction(false);
		httpConn.setInstanceFollowRedirects(true);
		httpConn.setRequestMethod(Method);
		httpConn.setReadTimeout(timeout_millis); // timeout in millis

		// --- add body to request
		if (body != null) {
			writeBody(httpConn, body);
		}

		httpConn.connect(); 
		response = httpConn.getResponseCode(); 

		if (response == HttpURLConnection.HTTP_OK) {
			in = httpConn.getInputStream();
		} else {
			String msg = "";
			if (in != null) {
				in = httpConn.getErrorStream();
				msg =textFromURL(in);
				msg = msg.replaceAll("<message>", "");
				msg = msg.replaceAll("</message>", "");
				msg = msg.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
				msg = msg.replaceAll("\n", "");
			}
			if (msg.length() > 200) {
				msg = msg.substring(0, 200);
			}

			throw new RuntimeException("HTTP "+response+" ("+getNameOfHTTPCode(response)+")\n "+msg);
		}                     

		return in;     
	}



	private void writeBody(HttpURLConnection con, InputStream body) throws IOException {

		byte buffer[] = new byte[8192];
		int read = 0;

		con.setDoOutput(true);

		OutputStream output = con.getOutputStream();
		while ((read = body.read(buffer)) != -1)
		{
			output.write(buffer, 0, read);
		}
	}


	/*	

private static void request(boolean quiet, String method, URL url, String username, String password, InputStream body)
throws IOException
{
    // sigh.  openConnection() doesn't actually open the connection,
    // just gives you a URLConnection.  connect() will open the connection.
    if (!quiet)
    {
        System.out.println("[issuing request: " + method + " " + url + "]");
    }
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setRequestMethod(method);

    // write auth header
    BASE64Encoder encoder = new BASE64Encoder();
    String encodedCredential = encoder.encode( (username + ":" + password).getBytes() );
    connection.setRequestProperty("Authorization", "BASIC " + encodedCredential);

    // write body if we're doing POST or PUT
    byte buffer[] = new byte[8192];
    int read = 0;
    if (body != null)
    {
        connection.setDoOutput(true);

        OutputStream output = connection.getOutputStream();
        while ((read = body.read(buffer)) != -1)
        {
            output.write(buffer, 0, read);
        }
    }

    // do request
    long time = System.currentTimeMillis();
    connection.connect();

    InputStream responseBodyStream = connection.getInputStream();
    StringBuffer responseBody = new StringBuffer();
    while ((read = responseBodyStream.read(buffer)) != -1)
    {
        responseBody.append(new String(buffer, 0, read));
    }
    connection.disconnect();
    time = System.currentTimeMillis() - time;

    // start printing output
    if (!quiet)
        System.out.println("[read " + responseBody.length() + " chars in " + time + "ms]");

    // look at headers
    // the 0th header has a null key, and the value is the response line ("HTTP/1.1 200 OK" or whatever)
    if (!quiet)
    {
        String header = null;
        String headerValue = null;
        int index = 0;
        while ((headerValue = connection.getHeaderField(index)) != null)
        {
            header = connection.getHeaderFieldKey(index);

            if (header == null)
                System.out.println(headerValue);
            else
                System.out.println(header + ": " + headerValue);

            index++;
        }
        System.out.println("");
    }

    // dump body
    System.out.print(responseBody);
    System.out.flush();
	 */

	/**
	 * if you don't like inputstreams as result, use this method to convert it to string
	 */
	public static String textFromURL(InputStream in) throws IOException {
		int BUFFER_SIZE = 2000;

		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];          
		try {
			while ((charRead = isr.read(inputBuffer))>0)
			{                    
				//---convert the chars to a String---
				String readString = 
					String.copyValueOf(inputBuffer, 0, charRead);                    
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			Log.w(TAG,"IO: "+e.getMessage());
			return "";
		}    
		return str;        
	}

	/*
	 * byte to string conversion
	 */
	private static String toString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length);
		for( byte b : bytes ) {
			sb.append(Character.toString((char)b));
		}
		return sb.toString();
	}

	private static String getNameOfHTTPCode(final int http) {
		switch (http) {
		case 400: return "Bad Request";
		case 401: return "Unauthorized";
		case 402: return "Payment Required";
		case 403: return "Forbidden";
		case 404: return "Not Found";
		case 405: return "Method Not Allowed";
		case 406: return "Not Acceptable";
		case 407: return "Proxy Authentication Required";
		case 408: return "Request Timeout";

		case 500: return "500 Internal Server Error";
		case 501: return "Not Implemented";
		case 502: return "Bad Gateway";
		case 503: return "Service Unavailable";
		case 504: return "Gateway Timeout";
		case 505: return "HTTP Version Not Supported";

		default: return "";
		}
	}
}