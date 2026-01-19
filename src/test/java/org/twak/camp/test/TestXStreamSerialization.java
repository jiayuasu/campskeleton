package org.twak.camp.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.twak.camp.Corner;
import org.twak.camp.Edge;
import org.twak.camp.Machine;
import org.twak.utils.CloneSerializable;
import org.twak.utils.collections.Loop;

/**
 * Tests for XStream serialization functionality.
 * These tests verify that the XStream security configuration works correctly
 * after upgrading to XStream 1.4.20.
 */
public class TestXStreamSerialization {

	@Test
	public void testXCloneCorner() {
		Corner original = new Corner(10.5, 20.3);
		Corner cloned = (Corner) CloneSerializable.xClone(original);
		
		Assertions.assertNotNull(cloned, "Cloned corner should not be null");
		Assertions.assertNotSame(original, cloned, "Clone should be a different object");
		Assertions.assertEquals(original.x, cloned.x, 0.0001, "X coordinate should match");
		Assertions.assertEquals(original.y, cloned.y, 0.0001, "Y coordinate should match");
	}

	@Test
	public void testXCloneMachine() {
		Machine original = new Machine();
		Machine cloned = (Machine) CloneSerializable.xClone(original);
		
		Assertions.assertNotNull(cloned, "Cloned machine should not be null");
		Assertions.assertNotSame(original, cloned, "Clone should be a different object");
	}

	@Test
	public void testXCloneEdge() {
		Corner c1 = new Corner(0, 0);
		Corner c2 = new Corner(100, 50);
		Edge original = new Edge(c1, c2);
		original.machine = new Machine();
		
		Edge cloned = (Edge) CloneSerializable.xClone(original);
		
		Assertions.assertNotNull(cloned, "Cloned edge should not be null");
		Assertions.assertNotSame(original, cloned, "Clone should be a different object");
		Assertions.assertNotNull(cloned.start, "Cloned edge start should not be null");
		Assertions.assertNotNull(cloned.end, "Cloned edge end should not be null");
	}

	@Test
	public void testXCloneLoop() {
		Loop<Edge> loop = new Loop<Edge>();
		Corner c1 = new Corner(0, 0);
		Corner c2 = new Corner(100, 0);
		Corner c3 = new Corner(100, 100);
		
		Machine directionMachine = new Machine();
		
		loop.append(new Edge(c1, c2));
		loop.append(new Edge(c2, c3));
		loop.append(new Edge(c3, c1));
		for (Edge e : loop) e.machine = directionMachine;
		
		@SuppressWarnings("unchecked")
		Loop<Edge> cloned = (Loop<Edge>) CloneSerializable.xClone(loop);
		
		Assertions.assertNotNull(cloned, "Cloned loop should not be null");
		Assertions.assertNotSame(loop, cloned, "Clone should be a different object");
		
		// Count edges in both loops
		int originalCount = 0;
		int clonedCount = 0;
		for (@SuppressWarnings("unused") Edge e : loop) originalCount++;
		for (@SuppressWarnings("unused") Edge e : cloned) clonedCount++;
		
		Assertions.assertEquals(originalCount, clonedCount, "Cloned loop should have same number of edges");
	}
}
