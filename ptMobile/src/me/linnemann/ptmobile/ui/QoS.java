package me.linnemann.ptmobile.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * QoS helps Activities with sending error/success messages and showing error dialogs etc.
 * 
 * @author nlinn
 *
 */
public class QoS {

	public static final int SUCCESS=0;
	public static final int ERROR=1;

	private static final String KEY_ERROR_MESSAGE = "e";
	
	public static boolean isSuccess(Message msg) {
		return (msg.what == SUCCESS);
	}
	
	public static boolean isError(Message msg) {
		return (msg.what == ERROR);
	}
	
	public static void showErrorDialog(Context ctx, Message msg) {
		Bundle bundle = msg.getData();
		String error = bundle.getString(KEY_ERROR_MESSAGE);
		
		showErrorDialog(ctx, "Updating data failed.\n\n"+error);
	}
	
	public static void showErrorDialog(Context ctx, String errormsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(errormsg);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void sendErrorMessageToHandler(Exception e, Handler handler) {
		Message msg = new Message();
		msg.what = ERROR;
		Bundle bundle = new Bundle();
		bundle.putString(KEY_ERROR_MESSAGE, e.getMessage());
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendSuccessMessageToHandler(Handler handler) {
		handler.sendEmptyMessage(QoS.SUCCESS);
	}
	
	/**
	 * creates a Handler that show error message or ok message an than calls messageHandler
	 * 
	 * @param messageHandler
	 * @param okMessage
	 * @return
	 */
	public static Handler createHandlerShowingMessage(final Context ctx, final QoSMessageHandler messageHandler, final String okMessage) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (QoS.isError(msg)) {
					QoS.showErrorDialog(ctx, msg);
					messageHandler.onERRORFromHandler();
				} else {
					Toast toast = Toast.makeText(ctx, okMessage, Toast.LENGTH_SHORT);
					toast.show();
					messageHandler.onOKFromHandler();
				}
			}
		};
		return handler;
	}
}
