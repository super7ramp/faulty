package io.github.super7ramp.faulty;

import java.util.Iterator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskContainer;
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
		 * Add the faulty-agent to dependencies.
		 */
		final Configuration config = project.getConfigurations().create("faultyAgent");
		config.setDescription("Faulty agent configuration");
		config.setVisible(false);
		config.defaultDependencies(deps -> {
			final DependencyHandler dependencyHandler = project.getDependencies();
			// TODO pass this as parameter/read it from somewhere
			deps.add(dependencyHandler.create("io.github.super7ramp.faulty:faulty-agent:1.0-SNAPSHOT"));
			deps.add(dependencyHandler.create("io.github.super7ramp.faulty:faulty-api:1.0-SNAPSHOT"));
		});

		/*
		 * Extend every JavaExec and Test tasks with the faulty extension.
		 */
		final TaskContainer tasks = project.getTasks();
		tasks.withType(JavaExec.class).configureEach(javaExec -> configure(config, javaExec));
		tasks.withType(Test.class).configureEach(testExec -> configure(config, testExec));

	}

	/**
	 * Configure the given task to apply the faulty task extension.
	 *
	 * @param <T>         type of the task
	 * @param agentConfig the agent configuration
	 * @param javaTask    the task
	 */
	private <T extends Task & JavaForkOptions> void configure(final Configuration agentConfig, final T javaTask) {

		final FaultyTaskExtension faultyParameters = javaTask.getExtensions().create("faulty",
				FaultyTaskExtension.class);

//		if (!faultyParameters.isEnabled()) {
//			return;
//		}

		final StringBuilder javaAgentArgs = new StringBuilder("-javaagent:");
		javaAgentArgs.append(agentConfig.getAsPath());

		if (faultyParameters.hasArguments()) {
			javaAgentArgs.append("=");

			// preTransform
			final Iterator<String> classNameIterator = faultyParameters.preTransform().iterator();
			while (classNameIterator.hasNext()) {
				javaAgentArgs.append("preTransform=").append(classNameIterator.next());
				if (classNameIterator.hasNext()) {
					javaAgentArgs.append(",");
				}
			}

			// TODO static bugs
		}

		javaTask.getJvmArgs().add(javaAgentArgs.toString());
	}

}
