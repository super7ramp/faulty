package io.github.super7ramp.faulty;

import java.io.IOException;
import java.io.PrintStream;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link FaultyPlugin}.
 */
public class FaultyPluginTest {

	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder();

	@Test
	public void applyPlugin() throws IOException {

		try (final PrintStream buildFile = new PrintStream(testProjectDir.newFile("build.gradle"))) {
			buildFile.println("plugins {");
			buildFile.println("    id 'io.github.super7ramp.faulty.faulty-plugin'");
			buildFile.println("    id 'java'");
			buildFile.println("}");

			buildFile.println("test {");
//			buildFile.println("task myTask(type: JavaExec) {");
			buildFile.println("    faulty {");
			buildFile.println("        enabled = true");
			buildFile.println("    }");
			buildFile.println("}");
		}

		final BuildResult result = runTask();

		checkPluginApplied(result);
		checkDependenciesAdded(result);
		checkTestTaskJvmArgumentsUpdated(result);
	}

	private BuildResult runTask() {
		return GradleRunner.create().withProjectDir(testProjectDir.getRoot()).withPluginClasspath()
				.withArguments("test").build();
//				.withArguments("myTask").build();
	}

	private void checkTestTaskJvmArgumentsUpdated(final BuildResult result) {
	}

	private void checkDependenciesAdded(final BuildResult result) {
	}

	private void checkPluginApplied(final BuildResult resykt) {
	}

}
