package me.linnemann.ptmobile.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleErrorDialog {

	private String message;
	private Context ctx;

	public SimpleErrorDialog(String message, Context ctx) {
		this.message = message;
		this.ctx = ctx;
	}

	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
