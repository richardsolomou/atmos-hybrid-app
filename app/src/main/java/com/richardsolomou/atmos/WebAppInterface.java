package com.richardsolomou.atmos;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * This class creates an interface for the web application and contains functions that can be called
 * through JavaScript on the server-side.
 *
 * @author Richard Solomou <richard@richardsolomou.com>
 * @version 0.2
 * @since 0.2
 */
public class WebAppInterface {

	/**
	 * Sets the context variable.
	 */
	Context mContext;

	/**
	 * Instantiates a new web application interface.
	 *
	 * @param c the context parameter
	 */
	WebAppInterface(Context c) {
		mContext = c;
	}

	/**
	 * Shows a toast notification to the user.
	 *
	 * @param str notification string
	 */
	@JavascriptInterface
	public void showToast(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}
}
