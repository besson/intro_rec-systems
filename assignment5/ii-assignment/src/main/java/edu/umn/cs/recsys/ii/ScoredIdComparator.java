package edu.umn.cs.recsys.ii;

import java.util.Comparator;

import org.grouplens.lenskit.scored.ScoredId;

public class ScoredIdComparator implements Comparator<ScoredId> {

	@Override
	public int compare(ScoredId arg0, ScoredId arg1) {
		if (arg0.getScore() < arg1.getScore()) {
			return 1;
		} else if (arg0.getScore() > arg1.getScore()) {
			return -1;
		}

		return 0;
	}
}
