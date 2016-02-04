package com.gluegent.tool.gepuss;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class EnvironmentChooserTest {

	@Test
	public void testChooseExistDirs() {
		EnvironmentChooser env = new EnvironmentChooser(new String[0]);
		String[] coverDefaultDirs = env.coverDefaultDirs(new String[0]);
		assertNotNull(coverDefaultDirs);
		assertEquals(2, coverDefaultDirs.length);
		assertEquals(new File(".").getAbsoluteFile().getParent(), coverDefaultDirs[0]);
		assertEquals("~/Library/Google", coverDefaultDirs[1]);
		
		List<File> chooseExistDirs = env.chooseExistDirs(new String[0]);
		assertNotNull(chooseExistDirs);
		assertEquals(2, chooseExistDirs.size());
		assertEquals(new File(".").getAbsoluteFile().getParentFile(), chooseExistDirs.get(0));
		assertEquals(new File(System.getProperty("user.home") + "/Library/Google"), chooseExistDirs.get(1));
	}

}
