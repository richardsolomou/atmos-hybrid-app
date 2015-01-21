package com.richardsolomou.atmos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * This class employs Google Play Services to sign in the user with their Google account.
 *
 * @author Richard Solomou <richard@richardsolomou.com>
 * @version 0.1
 * @since 0.1
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener {

	/**
	 * Initialises the activity and sets a click listener on the sign in button.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		findViewById(R.id.btnSignIn).setOnClickListener(this);
	}

	/**
	 * Attempts to sign in the user with Google Play Services.
	 *
	 * @param view the view
	 */
	public void onClick(View view) {
		if (view.getId() == R.id.btnSignIn && !mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Starts the main activity and ends the current one.
	 *
	 * @param connectionHint the connection hint
	 */
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();
	}

}