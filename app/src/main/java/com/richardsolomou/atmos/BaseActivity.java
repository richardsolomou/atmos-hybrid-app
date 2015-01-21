package com.richardsolomou.atmos;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * This class serves as the base extended activity of the application, using Google Play Services to
 * keep the user logged in to the application at all times.
 *
 * @author Richard Solomou <richard@richardsolomou.com>
 * @version 0.1
 * @since 0.1
 */
public abstract class BaseActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	/**
	 * The sign in flag.
	 */
	private static final int RC_SIGN_IN = 0;

	/**
	 * The Google API client object.
	 */
	protected GoogleApiClient mGoogleApiClient;

	/**
	 * The intent in progress flag.
	 */
	protected boolean mIntentInProgress;

	/**
	 * The sign in button click flag.
	 */
	protected boolean mSignInClicked;

	/**
	 * The connection result object.
	 */
	protected ConnectionResult mConnectionResult;

	/**
	 * Initialises the activity and configures the Google API client object.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.build();
	}

	/**
	 * Attempts to start a connection to Google.
	 */
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	/**
	 * Stops the connection to Google.
	 */
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Attempts to reconnect to Google if the connection failed.
	 *
	 * @param result the connection result
	 */
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}
	}

	/**
	 * Clears any intents and connects the user if the activity was correct.
	 *
	 * @param requestCode  the request code
	 * @param responseCode the response code
	 * @param intent       the activity intent
	 */
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	/**
	 * Attempts to reconnect when the connection has been suspended.
	 *
	 * @param cause cause of suspension
	 */
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}

	/**
	 * Connects the user to Google if the connection result was received.
	 */
	protected void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (IntentSender.SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	/**
	 * Forbids users from signing in for a second time.
	 *
	 * @param connectionHint the connection hint
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;
	}
}
