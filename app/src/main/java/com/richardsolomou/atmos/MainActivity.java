package com.richardsolomou.atmos;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// NFC reader dependencies
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.widget.TextView;
import android.widget.Toast;

// Database helper and models
import com.richardsolomou.atmos.helper.DatabaseHelper;
import com.richardsolomou.atmos.model.Student;


public class MainActivity extends Activity {

	DatabaseHelper db;
	TextView card_sn;

	// List of NFC technologies.
	private final String[][] techList = new String[][]{
			new String[]{
					NfcA.class.getName(),
					NfcB.class.getName(),
					NfcF.class.getName(),
					NfcV.class.getName(),
					IsoDep.class.getName(),
					MifareClassic.class.getName(),
					MifareUltralight.class.getName(),
					Ndef.class.getName()
			}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHelper(getApplicationContext());
		card_sn = ((TextView) findViewById(R.id.card_sn));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Create pending intent.
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Create intent receiver for NFC events.
		IntentFilter filter = new IntentFilter();
		filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
		// Enable foreground dispatch for getting intent from NFC event.
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, techList);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Disable foreground dispatch.
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			String cardSN = bytesToHex(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
			Student student = db.getStudent(null, cardSN);

			if (student != null) {
				card_sn.setText(cardSN);
				Toast.makeText(getApplicationContext(), "Student with ID " + student.getStudentID() + " was matched.", Toast.LENGTH_SHORT).show();
			} else {
				Intent objIntent = new Intent(getApplicationContext(), AddStudent.class);
				objIntent.putExtra("cardSN", cardSN);
				startActivity(objIntent);
			}
		}
	}

	// Set the hexadecimal values in an array.
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	// Method to convert the byte array to hexadecimal.
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

}