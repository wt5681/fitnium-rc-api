package com.mincom.fitnium;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.magneticreason.fitnium.FitniumFixture;
import com.mincom.ellipse.rc.api.MFUI;
import com.mincom.ellipse.rc.apiv2.MFUIV2;
import com.mincom.ellipse.rc.selenium.MFUISelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;

public class MincomFitniumFixture extends FitniumFixture {
	
	public static final String INDEX = "ui.jsp";
	
	/* Default properties */
	public static final String DEFAULT_BROWSER = "*firefox";
	public static final String DEFAULT_PORT = "4444";
	public static final String DEFAULT_PASSWORD = "";
	
	/* JVM Custom properties */
	public static final String TEST_SERVER_URL = "test.server.url";
	public static final String TEST_SERVER_SSO = "test.server.sso";
	public static final String TEST_BROWSER = "test.browser";
	public static final String TEST_SELENIUM_PORT = "test.selenium.port";
	public static final String TEST_LOCALE = "test.locale";
	public static final String TEST_USERNAME = "test.username";
	public static final String TEST_PASSWORD = "test.password";
	public static final String TEST_DISTRICT = "test.district";
	public static final String TEST_POSITION = "test.position";
	
	private boolean sso;
	
	private HttpCommandProcessor commandProcessor;
	
	private MFUI mfui;
	
	private MFUIV2 mfuiv2;
	
	private Properties properties = new Properties();
	
	private String testLocale = null;
	
	public MincomFitniumFixture() {
		super();
	}
	
	public void ellipseUser(String user) {
		properties.put(TEST_USERNAME, user);
	}
	
	public void ellipseDistrict(String district) {
		properties.put(TEST_DISTRICT, district);
	}
	
	public void ellipsePosition(String position) {
		properties.put(TEST_POSITION, position);
	}
	
	// TODO change from void
	public void initialiseEllipse(String url) {
//		, String user, String district,
//			String position) {
//		properties.putAll(System.getProperties());
		
		// TODO a way to overwrite defaults
		properties.put(TEST_BROWSER, DEFAULT_BROWSER);
		properties.put(TEST_SELENIUM_PORT, DEFAULT_PORT);
		properties.put(TEST_PASSWORD, DEFAULT_PASSWORD);
		
		// TODO bad
		properties.put(TEST_SERVER_URL, "http://"+url);
//		properties.put(TEST_USERNAME, user);
//		properties.put(TEST_DISTRICT, district);
//		properties.put(TEST_POSITION, position);
		
		sso = "true".equals(getProperty(TEST_SERVER_SSO));
		String browser = getProperty(TEST_BROWSER);
		if(browser == null || browser.startsWith("${"))
			browser = "*firefox";
		String[] credentials = new String[] { getProperty("test.username"),
					getProperty("test.password"),
					getProperty("test.district"),
					getProperty("test.position"), };
		int seleniumPort = Integer.parseInt(getProperty(TEST_SELENIUM_PORT));
		testLocale = getProperty(TEST_LOCALE);
		if(testLocale != null && (testLocale.startsWith("${") || testLocale.length() == 0))
			testLocale = null;

		maybeSetTimeoutFactor();
		
		this.commandProcessor = new HttpCommandProcessor("localhost", seleniumPort, browser, getURL(null));

		mfui = new MFUISelenium(commandProcessor, credentials);
		mfuiv2 = new com.mincom.ellipse.rc.selenium.apiv2.MFUISelenium(commandProcessor, credentials);
		
		try {
			commandProcessor.start();
		} catch (RuntimeException e) {
			if (e.getMessage().indexOf("Connection refused: connect") != -1) {
				throw new RuntimeException(
						"Could not contact Selenium Server; have you started it?\n"
								+ e.getMessage());
			} else {
				throw e;
			}
		}
		
		open(!false, getURL(null));
	}
	
	// TODO change void
	public void loadApplication(String app) {
		mfuiv2.loadApp(app);
	}
	
	
	// Private helper methods
	private String getProperty(String key) {
		return (String) properties.get(key);
	}
	
	private void maybeSetTimeoutFactor() {
		String tf = getProperty("test.timeout.factor");
		try {
			if (tf != null) {
				// TODO this is part of JI 3.6+
				// Use explicit, fully qualified name, so its 'obvious' what's going on.
//				com.mincom.ellipse.rc.selenium.Wait.setTimeoutFactor(Float.parseFloat(tf));
			}
		} catch (Exception e) {
			throw new RuntimeException("test.timeout.factor has illegal value.");
		}
	}
	
	private String getURL(String urlParameters) {
		String serverURL = getServerURL();
		
		/*
		 * Allow server URL to contain parameters but maintain backwards compatibility 
		 */
		if (serverURL.endsWith("/")) {
			serverURL = serverURL + INDEX;
		}
		
		if ( StringUtils.isNotBlank(urlParameters)) {
			if (serverURL.contains("?")) {
				serverURL = serverURL + "&" + urlParameters;
			} else {
				serverURL = serverURL + "?" + urlParameters;
			}
		}
		return serverURL;
	}
	
	private String getServerURL() {
		return getProperty(TEST_SERVER_URL);
	}
	
	private void open(boolean login, String url) {
//		mfuiv2.saveAcceptLanguage();
//		mfuiv2.setTestLocale(testLocale);
		mfui.open(url);
		if ( login ) {
			login();
		} else {
			mfui.waitForPageToLoad("login");
		}
		mfui.getApplication().setEnableTracking(false);
		mfui.getApplication().bypassSecurity(true);
		mfui.enableErrorCapture();
		mfuiv2.setAutoForward(false);
	}
	
	private void login() {
		login(true);
	}
	
	private void login(boolean wait) {
		if(!sso) {
			mfui.waitForPageToLoad("login");
			mfui.login();
			if (wait) mfui.waitForApplication("home", false);
		}
		else {
			mfui.ssoLogin();
			if (wait) mfui.waitForPageToLoad("home");
		}
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

}
