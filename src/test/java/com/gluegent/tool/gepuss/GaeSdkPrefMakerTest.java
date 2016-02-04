package com.gluegent.tool.gepuss;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GaeSdkPrefMakerTest {

	@Test
	public void testMakeXml() {
		GaeSdkPrefMaker m = new GaeSdkPrefMaker();
		List<File> dirs = new ArrayList<>();
		dirs.add(new File("/hoge/appengine-java-sdk-1.9.28"));
		dirs.add(new File("/hoge/appengine-java-sdk-1.6.1"));
		dirs.add(new File("/hoge/appengine-java-sdk-1.9.31"));
		Map<String, File> map = m.nameSortedFileMap(dirs);
		String xml = m.makeXml(map);
		assertEquals("<sdks defaultSdk=\"appengine-java-sdk-1.9.31\">\n" +
				"\t<sdk name=\"appengine-java-sdk-1.6.1\" location=\"/hoge/appengine-java-sdk-1.6.1\" version=\"1.6.1\"/>\n" +
				"\t<sdk name=\"appengine-java-sdk-1.9.28\" location=\"/hoge/appengine-java-sdk-1.9.28\" version=\"1.9.28\"/>\n" +
				"\t<sdk name=\"appengine-java-sdk-1.9.31\" location=\"/hoge/appengine-java-sdk-1.9.31\" version=\"1.9.31\"/>\n" +
				"</sdks>", xml);
	}

	@Test
	public void testPickVersion() {
		GaeSdkPrefMaker m = new GaeSdkPrefMaker();
		assertEquals("1.9.28",
				m.pickVersion(new File("/hoge/appengine-java-sdk-1.9.28")));
	}

}
