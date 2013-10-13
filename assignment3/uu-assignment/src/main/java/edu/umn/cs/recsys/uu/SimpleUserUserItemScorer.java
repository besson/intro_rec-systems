package edu.umn.cs.recsys.uu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;

/**
 * User-user item scorer.
 * 
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleUserUserItemScorer extends AbstractItemScorer {
	private final UserEventDAO userDao;
	private final ItemEventDAO itemDao;

	@Inject
	public SimpleUserUserItemScorer(UserEventDAO udao, ItemEventDAO idao) {
		userDao = udao;
		itemDao = idao;
	}

	@Override
	public void score(long user, @Nonnull MutableSparseVector scores) {
		SparseVector userVector = getUserRatingVector(user);

		// TODO Score items for this user using user-user collaborative
		// filtering

		// This is the loop structure to iterate over items to score
		for (VectorEntry e : scores.fast(VectorEntry.State.EITHER)) {
			List<Neighbor> similarNeighborhood = getSimilarNeighborhood(userVector, e.getKey());
		}
	}

	private List<Neighbor> getSimilarNeighborhood(SparseVector userVector, Long movieId) {
		MutableSparseVector userRatings = getAllUserRatingVectorsForMovie(movieId);

		userVector = calculateMeanCenterRating(userVector);

		CosineVectorSimilarity calculator = new CosineVectorSimilarity();
		List<Neighbor> neighborhood = new ArrayList<Neighbor>();

		for (VectorEntry entry : userRatings.fast()) { 
			Neighbor neighbor = new Neighbor();
			neighbor.setId(entry.getKey());
			neighbor.setScore(calculator.similarity(userVector, calculateMeanCenterRating(entry.getVector())));

			neighborhood.add(neighbor);
		}
		
		Collections.reverse(neighborhood);

		return neighborhood.subList(0, 29);
	}

	/**
	 * Get a user's rating vector.
	 * 
	 * @param user
	 *            The user ID.
	 * @return The rating vector.
	 */
	private SparseVector getUserRatingVector(long user) {
		UserHistory<Rating> history = userDao.getEventsForUser(user, Rating.class);
		if (history == null) {
			history = History.forUser(user);
		}
		return RatingVectorUserHistorySummarizer.makeRatingVector(history);
	}

	private SparseVector calculateMeanCenterRating(SparseVector vector) {
		double mean = vector.mean();
		MutableSparseVector mVector = vector.mutableCopy();

		for (VectorEntry entry : mVector.fast()) {
			double value = entry.getValue();
			mVector.set(entry, value - mean);
		}

		return mVector;
	}

	private MutableSparseVector getAllUserRatingVectorsForMovie(Long id) {
		MutableSparseVector users = MutableSparseVector.create(itemDao.getUsersForItem(id));
		
		for (VectorEntry entry : users.fast() ) {
			SparseVector userRatingVector = getUserRatingVector(entry.getKey());
			users.set(userRatingVector);
		}
		
		return users;
	}
}
