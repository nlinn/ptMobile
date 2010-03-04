package me.linnemann.ptmobile.qos;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * QoS helps Activities with sending error/success messages and showing error dialogs etc.
 * 
 * @author nlinn
 *
 */
public class QoS implements QoSMessageHandler{

	public static final int SUCCESS=0;
	public static final int ERROR=1;

	private static final String KEY_ERROR_MESSAGE = "emsg";
	private static final String KEY_TRACE = "trace";

	public static final boolean HANDLE_EVENT = true;
	public static final boolean IGNORE_EVENT = false;

	private Handler handler;
	private String okMessage, errorMessage;

	public QoS(final Context ctx, final QoSMessageHandler messageHandler) {
		this.handler = createHandlerShowingMessage(ctx, messageHandler);
	}

	public QoS(final Context ctx) {
		this.handler = createHandlerShowingMessage(ctx, this);
	}
	
	public void setOkMessage(String message) {
		this.okMessage = message;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public void showErrorDialog(Context ctx, Message msg) {
		Bundle bundle = msg.getData();
		String error = bundle.getString(KEY_ERROR_MESSAGE);
		String trace = bundle.getString(KEY_TRACE);

		Log.i("QoS",trace);

		showErrorDialog(ctx, error,trace);
	}

	public void showErrorDialog(final Context ctx, final String errormsg, final String details) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(errorMessage+"\n\n"+errormsg);
		builder.setPositiveButton("Send Feedback", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				sendFeedbackMail(ctx, errormsg, details);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void sendErrorMessageToHandler(Exception e) {
		Log.w("QoS","Showing error: "+e.getMessage());
		e.printStackTrace();
		Message msg = new Message();
		msg.what = ERROR;
		Bundle bundle = new Bundle();
		bundle.putString(KEY_ERROR_MESSAGE, e.getMessage());
		bundle.putString(KEY_TRACE, stackTraceAsString(e));

		msg.setData(bundle);
		handler.sendMessage(msg);
	}

	public void sendSuccessMessageToHandler() {
		handler.sendEmptyMessage(QoS.SUCCESS);
	}

	/**
	 * creates a Handler that show error message or ok message an than calls messageHandler
	 * 
	 * @param messageHandler
	 * @param okMessage
	 * @return
	 */
	private Handler createHandlerShowingMessage(final Context ctx, final QoSMessageHandler messageHandler) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == QoS.ERROR) {
					boolean handleOrIgnore = messageHandler.onQoSERROR();
					if (handleOrIgnore == QoS.HANDLE_EVENT) {
						showErrorDialog(ctx, msg);
					}
				} else {
					boolean handleOrIgnore = messageHandler.onQoSOK();
					if (handleOrIgnore == QoS.HANDLE_EVENT) {
						Toast toast = Toast.makeText(ctx, okMessage, Toast.LENGTH_SHORT);
						toast.show();
					}					
				}
			}
		};
		return handler;
	}

	public static void sendFeedbackMail(Context context, String errormsg, String details) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"niels.linnemann@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "User Feedback");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getFeedbackEmailBody(context,errormsg,details));
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	private static String getFeedbackEmailBody(Context ctx, String errormsg, String details) {
		StringBuilder body = new StringBuilder("<feel free to complain here>");
		body.append("\n\nMessage:\n");
		body.append(errormsg);
		body.append("\n\nDetails:\n");
		body.append(details);
		body.append(getBuildInfo());
		
		body.append("\n\nSystem Info:\n");
		body.append(getBasicPhoneInfo(ctx));
		body.append("\n\n");
		body.append(getMoreSysInfo());
		body.append("\n\n");
		body.append(getSystemInfo());
		return body.toString();
	}

	private static String getSystemInfo() {
		StringBuffer buffer = new StringBuffer();

		renderSysPropertyToBuffer("os.name", "os.name", buffer);
		renderSysPropertyToBuffer("os.version", "os.version", buffer);

		renderSysPropertyToBuffer("java.vendor.url", "java.vendor.url", buffer);
		renderSysPropertyToBuffer("java.version", "java.version", buffer);
		renderSysPropertyToBuffer("java.class.path", "java.class.path", buffer);
		renderSysPropertyToBuffer("java.class.version", "java.class.version", buffer);
		renderSysPropertyToBuffer("java.vendor", "java.vendor", buffer);
		renderSysPropertyToBuffer("java.home", "java.home", buffer);

		renderSysPropertyToBuffer("user.name", "user.name", buffer);
		renderSysPropertyToBuffer("user.home", "user.home", buffer);
		renderSysPropertyToBuffer("user.dir", "user.dir", buffer);

		return buffer.toString();

	}

	private static void renderSysPropertyToBuffer(String desc, String property, StringBuffer buffer) {
		buffer.append(desc);
		buffer.append(" : ");
		buffer.append(System.getProperty(property));
		buffer.append("\n");
	}

	private static String getBuildInfo() {
		StringBuilder info = new StringBuilder();
		
		info.append("\n\nBuild: \n");
		
		info.append("\nBoard: ");
		info.append(Build.BOARD);
		
		info.append("\nBrand: ");
		info.append(Build.BRAND);
				
		info.append("\nDevice: ");
		info.append(Build.DEVICE);
		
		info.append("\nDisplay: ");
		info.append(Build.DISPLAY);
		
		info.append("\nFingerprint: ");
		info.append(Build.FINGERPRINT);
		
		info.append("\nModell: ");
		info.append(Build.MODEL);
		
		info.append("\nProduct: ");
		info.append(Build.PRODUCT);
		
		info.append("\nTags: ");
		info.append(Build.TAGS);
		
		return info.toString();
	}
	
	
	private static String getBasicPhoneInfo(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		StringBuilder info = new StringBuilder();
		info.append("\n\nPhone Info:\n");
		info.append("\nModell: ");
		info.append(Build.MODEL);
		info.append("\nDevice Id: ");
		info.append(tm.getDeviceId());
		info.append("\nSubscriber Id: ");
		info.append(tm.getSubscriberId());		
		info.append("\nDevice SofwareVersion: ");
		info.append(tm.getDeviceSoftwareVersion());

		return info.toString();
	}


	private static String getMoreSysInfo() {

		ProcessBuilder cmd;
		String result="";

		try{
			String[] args = {"/system/bin/cat", "/proc/version"};
			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while(in.read(re) != -1){
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return result.trim();
	}

	private static String stackTraceAsString(Exception e) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		return result.toString();
	}

	public boolean onQoSERROR() {
		return QoS.HANDLE_EVENT;
	}

	public boolean onQoSOK() {
		return QoS.HANDLE_EVENT;
	}
}
