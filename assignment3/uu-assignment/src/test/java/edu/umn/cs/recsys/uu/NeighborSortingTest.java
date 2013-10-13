package edu.umn.cs.recsys.uu;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

public class NeighborSortingTest {

	@Test
	public void shouldSortNeighborsCorrectly() { 
		Neighbor n1 = new  Neighbor();
		n1.setId(1l);
		n1.setScore(0.90);
		
		Neighbor n2 = new  Neighbor();
		n2.setId(2l);
		n2.setScore(0.901);	
		
		Neighbor n3 = new  Neighbor();
		n3.setId(3l);
		n3.setScore(0.89);		
		
		List<Neighbor> neighbors = new ArrayList<Neighbor>();
		
		neighbors.add(n1);
		neighbors.add(n2);
		
		Collections.reverse(neighbors);

		Neighbor result = neighbors.get(0);
		assertEquals(result.getId(), Long.valueOf(2));
		assertEquals(result.getScore(), Double.valueOf(0.901));
	}
}
