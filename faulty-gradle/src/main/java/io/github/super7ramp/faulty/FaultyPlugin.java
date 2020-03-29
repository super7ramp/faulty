package io.github.super7ramp.faulty;

import java.util.Iterator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.testing.Test;
import org.gradle.process.JavaForkOptions;

/**
 * The faulty gradle plugin.
 * <p>
 * This allows to run any {@link JavaExec} task with the faulty agent, provided
 * that the task description contains a {@link FaultyTaskExtension}.
 * <p>
 * It adds the faulty-agent and faulty-api dependencies as well.
 */
public final class FaultyPlugin implements Plugin<Project> {

	/**
	 * Constructor.
	 */
	public FaultyPlugin() {
		// Empty.
	}

	@Override
	public final void apply(final Project project) {

		/*
		 * Create a dedicated configuration for faulty agent jar.
		 */
		final Configuration config = project.getConfigurations().create("faultyAgent")
				.setDescription("Faulty agent configuration").setVisible(false);

		config.defaultDependencies(dependencySet -> {
			final DependencyHandler dependencyHandler = project.getDependencies();
			// TODO pass this as parameter/read it from somewhere
			dependencySet.add(dependencyHandler.create("io.github.super7ramp:faulty-agent:1.0"));
		});

		/*
		 * Allow every JavaExec tasks to be extended with a faulty extension.
		 */
		project.getTasks().withType(JavaExec.class).configureEach(javaExec -> {
			final FaultyTaskExtension faultyParameters = javaExec.getExtensions().create("faulty",
					FaultyTaskExtension.class);
			addJvmArguments(javaExec, faultyParameters, config);
		});

		/*
		 * Same thing with test tasks.
		 */
		project.getTasks().withType(Test.class).configureEach(testExec -> {
			final FaultyTaskExtension faultyParameters = testExec.getExtensions().create("faulty",
					FaultyTaskExtension.class);
			addJvmArguments(testExec, faultyParameters, config);
		});

	}

	/**
	 * Add the right <code>-javaagent:</code> stuff to JVM arguments so that the
	 * task can be run with faulty agent.
	 *
	 * @param javaTaskOptions    the java task options
	 * @param faultyParameters  the faulty task extension parameters
	 * @param faultyAgentConfig the agent dedicated configuration, to get the path
	 *                          to the agent jar
	 */
	private void addJvmArguments(final JavaForkOptions javaTaskOptions, final FaultyTaskExtension faultyParameters,
			final Configuration faultyAgentConfig) {

		if (!faultyParameters.isEnabled()) {
			return;
		}

		final StringBuilder javaAgentArgs = new StringBuilder("-javaagent:");
		javaAgentArgs.append(faultyAgentConfig.getAsPath());
		if (faultyParameters.hasArguments()) {
			javaAgentArgs.append("=");
			final Iterator<String> classNameIterator = faultyParameters.preTransform().iterator();
			while (classNameIterator.hasNext()) {
				javaAgentArgs.append("preTransform=").append(classNameIterator.next());
				if (classNameIterator.hasNext()) {
					javaAgentArgs.append(",");
				}
			}
			// TODO add static bugs
		}

		javaTaskOptions.getJvmArgs().add(javaAgentArgs.toString());
	}

}
