package io.github.super7ramp.faulty.agent.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link ArgumentParser}.
 */
public class ArgumentParserTest {

	@Test
	public void emptyArguments() {
		final AgentConfiguration config = new ArgumentParser("").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void apiOnly() {
		final AgentConfiguration config = new ArgumentParser("api=4").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(4, config.apiVersion());
	}

	@Test
	public void classEmpy() {
		final AgentConfiguration config = new ArgumentParser("preTransform=").parse();
		assertTrue(config.classesToPreTransform().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void classOnly() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto").parse();
		assertEquals(1, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void severalClasses() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto,preTransform=tata").parse();
		assertEquals(2, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.classesToPreTransform().contains("tata"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void repeatedClasses() {
		final AgentConfiguration config = new ArgumentParser("preTransform=toto,preTransform=toto").parse();
		assertEquals(1, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void mix() {
		final AgentConfiguration config = new ArgumentParser(
				"preTransform=toto,preTransform=tata,preTransform=titi,api=6").parse();
		assertEquals(3, config.classesToPreTransform().size());
		assertTrue(config.classesToPreTransform().contains("toto"));
		assertTrue(config.classesToPreTransform().contains("tata"));
		assertTrue(config.classesToPreTransform().contains("titi"));
		assertEquals(6, config.apiVersion());
	}

}
