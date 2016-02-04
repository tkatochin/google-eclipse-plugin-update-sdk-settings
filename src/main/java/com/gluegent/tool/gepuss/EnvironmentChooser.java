package com.gluegent.tool.gepuss;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironmentChooser {

	public EnvironmentChooser(String[] pathes) {
		gaeSdkDirs = new ArrayList<>();
		gwtSdkDirs = new ArrayList<>();
		run(pathes);
	}
	
	private File settingsDir;
	private List<File> gaeSdkDirs;
	private List<File> gwtSdkDirs;
	
	public File getSettingsDir() {
		return settingsDir;
	}
	
	public List<File> getGaeSdkDirs() {
		return gaeSdkDirs;
	}
	
	public List<File> getGwtSdkDirs() {
		return gwtSdkDirs;
	}
	
	private void run(String[] pathes) {
		List<File> dirs = chooseExistDirs(pathes);
		
		findSettingsDir(dirs);
		findGaeSdkDirs(dirs);
		findGwtSdkDirs(dirs);
	}
	
	public boolean hasEnvionment() {
		if (settingsDir == null) {
			System.err.println("[ABORT] eclipseのワークスペースの指定がありません。");
			return false;
		}
		if (gaeSdkDirs.size() == 0 && gwtSdkDirs.size() == 0) {
			System.err.println("[ABORT] appengine-java-sdk-*, gwt-*のいづれの場所も見つかりません。");
			return false;
		}
		return true;
	}
	
	protected List<File> chooseExistDirs(String[] pathes) {
		pathes = coverDefaultDirs(pathes);
		List<File> results = new ArrayList<>();
		for (String path: pathes) {
			File dir = new File(path.replaceFirst("^~", System.getProperty("user.home")));
			if (dir.exists() && dir.isDirectory()) {
				results.add(dir);
			}
		}
		return results;
	}
	
	protected String[] coverDefaultDirs(String[] pathes) {
		if (pathes.length == 0) {
			// default
			pathes = new String[] {
				new File(".").getAbsoluteFile().getParent(),
				"~/Library/Google"
			};
		}
		return pathes;
	}
	
	private void findSettingsDir(List<File> dirs) {
		for (File dir: dirs) {
			File result = new File(dir,
					".metadata/.plugins/org.eclipse.core.runtime/.settings");
			if (result.exists() && result.isDirectory()) {
				settingsDir = result;
				return;
			}
		}
	}

	private void findGaeSdkDirs(List<File> dirs) {
		for (File dir: dirs) {
			gaeSdkDirs.addAll(Arrays.asList(dir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if (pathname.getName().matches(new GaeSdkPrefMaker().getDirNamePattern()) && pathname.isDirectory()) {
							File appcfg = new File(pathname, "bin/appcfg.sh");
							if (appcfg.exists() && appcfg.isFile()) {
								return true;
							}
						}
						return false;
					}
				})));
		}
		if (gaeSdkDirs.size() == 0) {
			System.err.println("[WARING] appengine-java-sdk-*を配置している場所が見つかりません。");
		}
	}

	private void findGwtSdkDirs(List<File> dirs) {
		for (File dir: dirs) {
			gwtSdkDirs.addAll(Arrays.asList(dir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if (pathname.getName().matches(new GwtSdkPrefMaker().getDirNamePattern()) && pathname.isDirectory()) {
							File gwtUser = new File(pathname, "gwt-user.jar");
							if (gwtUser.exists() && gwtUser.isFile()) {
								return true;
							}
						}
						return false;
					}
				})));
		}
		if (gwtSdkDirs.size() == 0) {
			System.err.println("[WARING] gwt-*を配置している場所が見つかりません。");
		}
	}
}
