package com.gluegent.tool.gepuss;

public class GaeSdkPrefMaker extends GooglePrefMaker {

	@Override
	public String getDirNamePattern() {
		return "appengine-java-sdk-([0-9]+[.0-9]+)";
	}
	
	@Override
	protected String fileName() {
		return "com.google.appengine.eclipse.core.prefs";
	}

}
