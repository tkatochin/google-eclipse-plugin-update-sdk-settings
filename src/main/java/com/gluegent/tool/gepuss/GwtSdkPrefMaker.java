package com.gluegent.tool.gepuss;

public class GwtSdkPrefMaker extends GooglePrefMaker {

	@Override
	public String getDirNamePattern() {
		return "gwt-([0-9]+[.0-9]+)";
	}
	
	@Override
	protected String fileName() {
		return "com.google.gwt.eclipse.core.prefs";
	}

}
