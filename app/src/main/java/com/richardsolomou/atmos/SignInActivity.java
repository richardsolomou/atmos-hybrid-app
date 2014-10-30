package com.richardsolomou.atmos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class SignInActivity extends BaseActivity implements View.OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		findViewById(R.id.btn_sign_in).setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.btn_sign_in && !mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

}