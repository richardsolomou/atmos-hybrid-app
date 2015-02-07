package com.richardsolomou.atmos;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.Plus;

/**
 * This class serves as the main activity for the application. It contains methods that allow it to
 * talk to the device's hardware to listen for an RFID chip and send its UID to the web application.
 *
 * @author Richard Solomou <richard@richardsolomou.com>
 * @version 0.2
 * @since 0.1
 */
public class MainActivity extends BaseActivity {

	/**
	 * The protocol being used for the server.
	 */
	private String protocol = "http://";

	/**
	 * The hostname used for the server.
	 */
	private String hostName = "www2.richardsolomou.com";

	/**
	 * The path of the web application.
	 */
	private String homePage = "/atmos";

	/**
	 * The name of this application.
	 */
	private String objectName = "ATMOS";

	/**
	 * The web view handler.
	 */
	private WebView webView;

	/**
	 * The supported NFC technologies.
	 */
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

	/**
	 * All hexadecimal values in a character array.
	 */
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts the byte array received by the RFID chip to hexadecimal.
	 *
	 * @param bytes the byte array
	 * @return the converted string
	 */
	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	}

	/**
	 * Checks if there is a network connection available.
	 *
	 * @return true if there is a network connection, false otherwise
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Initialises the activity, configures the WebView client and directs the user to the web application.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webView = (WebView) findViewById(R.id.webview);

		webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.setWebContentsDebuggingEnabled(true);

		if (!isNetworkAvailable()) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		webView.loadUrl(protocol + hostName + homePage);
		webView.addJavascriptInterface(new WebAppInterface(this), objectName);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Uri.parse(url).getHost().equals(hostName)) {
					return false;
				}

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.clearCache(true);
			}
		});
	}

	/**
	 * Inflates the menu items for use in the action bar.
	 *
	 * @param menu the menu
	 * @return true if the operation was successful, false otherwise
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Handles presses on the action bar items.
	 *
	 * @param item the pressed item
	 * @return true if the refresh button was pressed, false otherwise
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				webView.loadUrl("javascript:window.location.reload(true)");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Sends the user back on the WebView or alternatively closes the application.
	 *
	 * @param keyCode the key code
	 * @param event   the key event
	 * @return true if the back key was pressed, false otherwise
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		WebView myWebView = (WebView) findViewById(R.id.webview);
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Sends the UID received by the RFID chip to the server-side.
	 *
	 * @param intent the intent
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			String uid = bytesToHex(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

			webView.loadUrl("javascript:requestRouter('RFIDRouter', { \"uid\": \"" + uid + "\" })");
		}
	}

	/**
	 * Sends the user data received from the SignInActivity to the server-side.
	 *
	 * @param connectionHint the connection hint
	 */
	public void onConnected(Bundle connectionHint) {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				String gender;

				if (person.getGender() == 0) {
					gender = "male";
				} else {
					gender = "female";
				}

				final String user = "{" +
						"\"email\": \"" + email + "\"," +
						"\"family_name\": \"" + person.getName().getFamilyName() + "\"," +
						"\"gender\": \"" + gender + "\"," +
						"\"given_name\": \"" + person.getName().getGivenName() + "\"," +
						"\"id\": \"" + person.getId() + "\"," +
						"\"link\": \"" + person.getUrl() + "\"," +
						"\"locale\": \"" + person.getLanguage() + "\"," +
						"\"name\": \"" + person.getDisplayName() + "\"," +
						"\"picture\": \"" + person.getImage().getUrl().substring(0, person.getImage().getUrl().length() - 6) + "\"," +
						"\"verified_email\": " + person.hasVerified() +
						"}";

				webView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
						view.clearCache(true);
						webView.loadUrl("javascript:requestRouter('SignInRouter', { \"user\": " + user + " })");
					}
				});
			} else {
				Toast.makeText(getApplicationContext(), "Person information is empty", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the intent for NFC events and enables foreground dispatch for capturing intents.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter filter = new IntentFilter();
		filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, techList);
	}

	/**
	 * Disables foreground dispatch for capturing intents.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcAdapter.disableForegroundDispatch(this);
	}
}
