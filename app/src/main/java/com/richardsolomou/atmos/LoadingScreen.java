package com.richardsolomou.atmos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.richardsolomou.atmos.helper.DatabaseHelper;


public class LoadingScreen extends Activity {

	DatabaseHelper db;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new LoadViewTask().execute();
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(LoadingScreen.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Loading...");
			progressDialog.setMessage("Loading application view, please wait...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			db = new DatabaseHelper(getApplicationContext());
			db.deleteAllStudents();
			db.resetDB();

			try {
				synchronized (this) {
					for (int i = 0; i <= 4; i++) {
						this.wait(50);
						publishProgress(i * 25);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			Intent objIntent = new Intent(getApplicationContext(), SignInActivity.class);
			startActivity(objIntent);
		}

	}

}