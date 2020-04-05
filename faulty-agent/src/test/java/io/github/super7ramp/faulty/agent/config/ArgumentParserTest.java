package io.github.super7ramp.faulty.agent.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.github.super7ramp.faulty.agent.config.StaticBug.Kind;

/**
 * Tests for {@link ArgumentParser}.
 */
@SuppressWarnings("javadoc")
public class ArgumentParserTest {

	@Test
	public void emptyArguments() {
		final AgentConfiguration config = new ArgumentParser("").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void apiOnly() {
		final AgentConfiguration config = new ArgumentParser("api=4").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(4, config.apiVersion());
	}

	@Test
	public void classEmpy() {
		final AgentConfiguration config = new ArgumentParser("preTransform=").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void classOnly() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto").parse();
		assertEquals(1, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void severalClasses() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto,preTransform=tata").parse();
		assertEquals(2, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.classesToPreTransform().contains("tata"));
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void repeatedClasses() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto,preTransform=toto").parse();
		assertEquals(1, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void staticBugEmpty() {
		final AgentConfiguration config = new ArgumentParser("infiniteLoop=,runtimeException=").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void staticBugsInvalidName() {
		final AgentConfiguration config = new ArgumentParser("infiniteLoop=com.").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertTrue(config.staticBugs().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void staticBugValidShortName() {
		final AgentConfiguration config = new ArgumentParser("infiniteLoop=class.method").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(1, config.staticBugs().size());
		assertTrue(config.staticBugs().contains(new StaticBugImpl("class", "method", Kind.INFINITE_LOOP)));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void staticBugValidRealisticName() {
		final AgentConfiguration config = new ArgumentParser("infiniteLoop=com.example.proj.app.Class.method").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(1, config.staticBugs().size());
		assertTrue(config.staticBugs()
				.contains(new StaticBugImpl("com.example.proj.app.Class", "method", Kind.INFINITE_LOOP)));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void repeatedBug() {
		final AgentConfiguration config = new ArgumentParser("infiniteLoop=class.method,infiniteLoop=class.method")
				.parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(1, config.staticBugs().size());
		assertTrue(config.staticBugs().contains(new StaticBugImpl("class", "method", Kind.INFINITE_LOOP)));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void mix() {
		final StringBuilder argumentBuilder = new StringBuilder();
		argumentBuilder.append("preTransform=toto,");
		argumentBuilder.append("preTransform=tata,");
		argumentBuilder.append("preTransform=titi,");
		argumentBuilder.append("infiniteLoop=toto.do,");
		argumentBuilder.append("infiniteInterruptibleLoop=tata.da,");
		argumentBuilder.append("runtimeException=titi.di,");
		argumentBuilder.append("api=6");

		final AgentConfiguration config = new ArgumentParser(argumentBuilder.toString()).parse();

		assertEquals(3, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.classesToPreTransform().contains("tata"));
		assertTrue(config.classesToPreTransform().contains("titi"));
		assertEquals(3, config.staticBugs().size());
		assertTrue(config.staticBugs().contains(new StaticBugImpl("toto", "do", Kind.INFINITE_LOOP)));
		assertTrue(config.staticBugs().contains(new StaticBugImpl("tata", "da", Kind.INFINITE_INTERRUPTIBLE_LOOP)));
		assertTrue(config.staticBugs().contains(new StaticBugImpl("titi", "di", Kind.RUNTIME_EXCEPTION)));
		assertEquals(6, config.apiVersion());
	}

}
