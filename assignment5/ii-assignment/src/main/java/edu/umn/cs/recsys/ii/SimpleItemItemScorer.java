package edu.umn.cs.recsys.ii;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.knn.NeighborhoodSize;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleItemItemScorer extends AbstractItemScorer {
	private final SimpleItemItemModel model;
	private final UserEventDAO userEvents;
	private final int neighborhoodSize;

	@Inject
	public SimpleItemItemScorer(SimpleItemItemModel m, UserEventDAO dao, @NeighborhoodSize int nnbrs) {
		model = m;
		userEvents = dao;
		neighborhoodSize = nnbrs;
	}

	/**
	 * Score items for a user.
	 * 
	 * @param user
	 *            The user ID.
	 * @param scores
	 *            The score vector. Its key domain is the items to score, and
	 *            the scores (rating predictions) should be written back to this
	 *            vector.
	 */
	@Override
	public void score(long user, @Nonnull MutableSparseVector scores) {
		SparseVector ratings = getUserRatingVector(user);

		for (VectorEntry e : scores.fast(VectorEntry.State.EITHER)) {
			long item = e.getKey();
			List<ScoredId> neighbors = model.getNeighbors(item);

			neighbors = cleanNeighborhood(neighbors, ratings);

			if (neighbors.size() > neighborhoodSize) {
				neighbors = neighbors.subList(0, neighborhoodSize);
			}

			double score = 0;
			double score1 = 0;

			for (ScoredId neighbor : neighbors) {
				double rating = ratings.get(neighbor.getId());
				score += neighbor.getScore() * rating;
				score1 += Math.abs(neighbor.getScore());
			}

			double totalScore = (score / score1);
			scores.set(item, totalScore);
		}
	}

	private List<ScoredId> cleanNeighborhood(List<ScoredId> neighbors, SparseVector ratings) {
		Map<Long, ScoredId> cleanNeighbors = new HashMap<Long, ScoredId>();

		for (ScoredId neighbor : neighbors) {
			cleanNeighbors.put(neighbor.getId(), neighbor);
		}

		for (ScoredId neighbor : neighbors) {
			if (!ratings.containsKey(neighbor.getId())) {
				cleanNeighbors.remove(neighbor.getId());
			}
		}

		ArrayList<ScoredId> cleanList = new ArrayList<ScoredId>(cleanNeighbors.values());
		Collections.sort(cleanList, new ScoredIdComparator());
		return cleanList;
	}

	/**
	 * Get a user's ratings.
	 * 
	 * @param user
	 *            The user ID.
	 * @return The ratings to retrieve.
	 */
	private SparseVector getUserRatingVector(long user) {
		UserHistory<Rating> history = userEvents.getEventsForUser(user, Rating.class);
		if (history == null) {
			history = History.forUser(user);
		}

		return RatingVectorUserHistorySummarizer.makeRatingVector(history);
	}
}
