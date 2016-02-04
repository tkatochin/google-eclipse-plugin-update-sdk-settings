package com.gluegent.tool.gepuss;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Properties;

import org.junit.Test;

public class BootstrapTest {

	@Test
	public void test() throws IOException {
		// dummy dirs/files create
		File testBase = new File("target/test-dummy-env");
		testBase.mkdirs();
		File sdksDir = new File(testBase, "sdks");
		for (String version: new String[] { "1.9.31", "1.8.20", "1.9.12"}) {
			File dummyGaeSdkDir = new File(sdksDir, "appengine-java-sdk-" + version);
			dummyGaeSdkDir.mkdirs();
			File dummyGaeSdkBinDir = new File(dummyGaeSdkDir, "bin");
			dummyGaeSdkBinDir.mkdir();
			File dummyGaeSdkShell = new File(dummyGaeSdkBinDir, "appcfg.sh");
			dummyGaeSdkShell.createNewFile();
		}
		for (String version: new String[] { "2.6.0", "2.7.0", "2.6.1"}) {
			File dummyGwtSdkDir = new File(sdksDir, "gwt-" + version);
			dummyGwtSdkDir.mkdirs();
			File dummyGwtSdkLib = new File(dummyGwtSdkDir, "gwt-user.jar");
			dummyGwtSdkLib.createNewFile();
		}
		File prefsDir = new File(testBase, ".metadata/.plugins/org.eclipse.core.runtime/.settings");
		prefsDir.mkdirs();
		
		
		Bootstrap.main(new String[] {
			testBase.getAbsolutePath(), sdksDir.getAbsolutePath()
		});
		
		File gaePref = new File(prefsDir, "com.google.appengine.eclipse.core.prefs");
		assertTrue(gaePref.exists());
		assertTrue(gaePref.isFile());
		Properties props = new Properties();
		try(FileInputStream in = new FileInputStream(gaePref)) {
			props.load(in);
		}
		assertEquals("1", props.getProperty("eclipse.preferences.version"));
		String encodedXml = props.getProperty("sdkSetXml");
		assertNotNull(encodedXml);
		String xml = new String(Base64.getDecoder().decode(encodedXml), Charset.forName("utf-8"));
		System.out.println(xml);
		assertEquals(
			"<sdks defaultSdk=\"appengine-java-sdk-1.9.31\">\n" +
			"	<sdk name=\"appengine-java-sdk-1.8.20\" location=\"" + sdksDir.getAbsolutePath() + "/appengine-java-sdk-1.8.20\" version=\"1.8.20\"/>\n" +
			"	<sdk name=\"appengine-java-sdk-1.9.12\" location=\"" + sdksDir.getAbsolutePath() + "/appengine-java-sdk-1.9.12\" version=\"1.9.12\"/>\n" +
			"	<sdk name=\"appengine-java-sdk-1.9.31\" location=\"" + sdksDir.getAbsolutePath() + "/appengine-java-sdk-1.9.31\" version=\"1.9.31\"/>\n" +
			"</sdks>", xml
		);
		
		File gwtPref = new File(prefsDir, "com.google.gwt.eclipse.core.prefs");
		assertTrue(gwtPref.exists());
		assertTrue(gwtPref.isFile());
		props = new Properties();
		try(FileInputStream in = new FileInputStream(gwtPref)) {
			props.load(in);
		}
		assertEquals("1", props.getProperty("eclipse.preferences.version"));
		encodedXml = props.getProperty("sdkSetXml");
		assertNotNull(encodedXml);
		xml = new String(Base64.getDecoder().decode(encodedXml), Charset.forName("utf-8"));
		System.out.println(xml);
		assertEquals(
			"<sdks defaultSdk=\"gwt-2.7.0\">\n" +
			"	<sdk name=\"gwt-2.6.0\" location=\"" + sdksDir.getAbsolutePath() + "/gwt-2.6.0\" version=\"2.6.0\"/>\n" +
			"	<sdk name=\"gwt-2.6.1\" location=\"" + sdksDir.getAbsolutePath() + "/gwt-2.6.1\" version=\"2.6.1\"/>\n" +
			"	<sdk name=\"gwt-2.7.0\" location=\"" + sdksDir.getAbsolutePath() + "/gwt-2.7.0\" version=\"2.7.0\"/>\n" +
			"</sdks>", xml
		);
	}

}
