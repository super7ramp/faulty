package io.github.super7ramp.faulty;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link FaultyPlugin}.
 */
@SuppressWarnings("javadoc")
public class FaultyPluginTest {

	private static final String TEST_PROGRAM_CLASSPATH = testProgramClassPath();

	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder();

	@Before
	public void before() throws IOException {

		try (final PrintStream settingsFile = new PrintStream(testProjectDir.newFile("settings.gradle"))) {
			settingsFile.println("pluginManagement {");
			settingsFile.println("    repositories {");
			settingsFile.println("        mavenLocal()");
			settingsFile.println("        mavenCentral()");
			settingsFile.println("    }");
			settingsFile.println("    plugins {");
			settingsFile.println("        id 'io.github.super7ramp.faulty.faulty-plugin' version '1.0-SNAPSHOT'");
			settingsFile.println("    }");
			settingsFile.println("}");
		}
	}

	@Test
	@Ignore("test is hard")
	public void applyPlugin() throws IOException {

		try (final PrintStream buildFile = new PrintStream(testProjectDir.newFile("build.gradle"))) {
			buildFile.println("task myTask(type: JavaExec) {");
			buildFile.println("    faulty {");
			buildFile.println("        enabled = true");
			buildFile.println("    }");
			buildFile.println("    classpath = files('" + TEST_PROGRAM_CLASSPATH + "')");
			buildFile.println("    main = '" + ExampleMain.class.getName() + "'");
			buildFile.println("}");
		}

		final BuildResult result = runTask("myTask");
		assertEquals(TaskOutcome.SUCCESS, result.task("myTask").getOutcome());
	}

	private BuildResult runTask(final String taskName) {
		final GradleRunner runner = GradleRunner.create();
		runner.withProjectDir(testProjectDir.getRoot());
		runner.withArguments(taskName);
		runner.withPluginClasspath();
		// for debugging, add runner.withDebug(true)
		return runner.build();
	}

	private static String testProgramClassPath() {
		// It works but it sucks
		return new File(FaultyPluginTest.class.getResource("ExampleMain.class").getFile()).getParentFile()
				.getParentFile().getParentFile().getParentFile().getParent();
	}

}
