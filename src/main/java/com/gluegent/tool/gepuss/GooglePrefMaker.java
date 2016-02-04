package com.gluegent.tool.gepuss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GooglePrefMaker {

	public abstract String getDirNamePattern();
	
	protected abstract String fileName();
	
	public void make(File settingsDir, List<File> sdkDirs) throws IOException {
		File file = new File(settingsDir, fileName());
		Properties pref = new Properties();
		if (file.exists()) {
			// 異なるファイル形式の場合は更新しない。
			try(FileInputStream in = new FileInputStream(file)) {
				pref.load(in);
				if (!"1".equals(pref.getProperty("eclipse.preferences.version"))) {
					System.err.println("[SKIP] " + fileName() + "のeclipse pluginの設定ファイルバージョンが異なるため作成をスキップします。");
					return;
				}
			}
		} else {
			pref.setProperty("eclipse.preferences.version", "1");
		}
		Map<String, File> map = nameSortedFileMap(sdkDirs);
		String xml = makeXml(map);
		String encodedXml = Base64.getEncoder().encodeToString(xml.getBytes(Charset.forName("utf-8")));
		pref.setProperty("sdkSetXml", encodedXml);
		try(FileOutputStream out = new FileOutputStream(file)) {
			pref.store(out, null);
		}
	}
	
	protected Map<String, File> nameSortedFileMap(Iterable<File> sdkDirs) {
		Map<String, File> map = new TreeMap<>();
		for (File sdkDir: sdkDirs) {
			String version = pickVersion(sdkDir);
			map.put(version, sdkDir);
		}
		return map;
	}
	
	protected String makeXml(Map<String, File> map) {
		StringBuilder sb = new StringBuilder();
		sb.append(MessageFormat.format("<sdks defaultSdk=\"{0}\">\n",
				map.get(getLast(map.keySet())).getName()));
		for (Map.Entry<String, File> e: map.entrySet()) {
			File dir = e.getValue();
			sb.append(MessageFormat.format(
				"\t<sdk name=\"{0}\" location=\"{1}\" version=\"{2}\"/>\n",
				dir.getName(), dir.getAbsolutePath(), e.getKey()));
		}
		sb.append("</sdks>");
		return sb.toString();
	}
	
	protected String pickVersion(File file) {
		Pattern p = Pattern.compile(getDirNamePattern());
		Matcher m = p.matcher(file.getName());
		m.find();
		return m.group(1);
	}
	
	protected <T> T getLast(Iterable<T> collection) {
		T last = null;
		for (T version: collection) {
			last = version;
		}
		return last;
	}
}
