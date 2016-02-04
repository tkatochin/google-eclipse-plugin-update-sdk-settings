package com.gluegent.tool.gepuss;

import java.io.IOException;

public class Bootstrap {

	private static final Bootstrap INSTANCE = new Bootstrap();
	
	public static void main(String[] args) throws IOException {
		INSTANCE.run(args);
	}
	
	public void run(String[] args) throws IOException {
		EnvironmentChooser env = new EnvironmentChooser(args);
		if (env.hasEnvionment()) {
			if (env.getGaeSdkDirs() != null) {
				new GaeSdkPrefMaker().make(env.getSettingsDir(), env.getGaeSdkDirs());
			}
			if (env.getGwtSdkDirs() != null) {
				new GwtSdkPrefMaker().make(env.getSettingsDir(), env.getGwtSdkDirs());
			}
		}
	}
	
}
