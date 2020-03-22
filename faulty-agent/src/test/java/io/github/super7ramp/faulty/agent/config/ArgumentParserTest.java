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
		assertTrue(config.transformableClassPrefix().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void apiOnly() {
		final AgentConfiguration config = new ArgumentParser("api=4").parse();
		assertTrue(config.transformableClassPrefix().isEmpty());
		assertEquals(4, config.apiVersion());
	}

	@Test
	public void classEmpy() {
		final AgentConfiguration config = new ArgumentParser("include=").parse();
		assertTrue(config.transformableClassPrefix().isEmpty());
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void classOnly() {
		final AgentConfiguration config = new ArgumentParser("include=toto").parse();
		assertEquals(1, config.transformableClassPrefix().size());
		assertTrue(config.transformableClassPrefix().contains("toto"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void severalClasses() {
		final AgentConfiguration config = new ArgumentParser("include=toto,include=tata").parse();
		assertEquals(2, config.transformableClassPrefix().size());
		assertTrue(config.transformableClassPrefix().contains("toto"));
		assertTrue(config.transformableClassPrefix().contains("tata"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void repeatedClasses() {
		final AgentConfiguration config = new ArgumentParser("include=toto,include=toto").parse();
		assertEquals(1, config.transformableClassPrefix().size());
		assertTrue(config.transformableClassPrefix().contains("toto"));
		assertEquals(7, config.apiVersion());
	}

	@Test
	public void mix() {
		final AgentConfiguration config = new ArgumentParser("include=toto,include=tata,include=titi,api=6").parse();
		assertEquals(3, config.transformableClassPrefix().size());
		assertTrue(config.transformableClassPrefix().contains("toto"));
		assertTrue(config.transformableClassPrefix().contains("tata"));
		assertTrue(config.transformableClassPrefix().contains("titi"));
		assertEquals(6, config.apiVersion());
	}

}
