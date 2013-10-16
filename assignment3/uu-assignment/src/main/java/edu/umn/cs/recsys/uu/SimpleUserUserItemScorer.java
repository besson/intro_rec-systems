package edu.umn.cs.recsys.uu;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

		for (VectorEntry e : scores.fast(VectorEntry.State.EITHER)) {
			List<Neighbor> similarNeighborhood = getSimilarNeighborhood(userVector, e.getKey(), user);

			double score1 = 0;
			double score2 = 0;

			for (Neighbor neighbor : similarNeighborhood) {
				double similarity = neighbor.getSimilarity();
				score1 += (similarity * neighbor.getVector().get(e.getKey()));
				score2 += Math.abs(similarity);
			}

			double totalScore = userVector.mean() + (score1/score2);
			scores.set(e.getKey(), totalScore);
		}
	}

	private List<Neighbor> getSimilarNeighborhood(SparseVector userVector, Long movieId, Long userId) {
		Map<Long, MutableSparseVector> userRatings = getAllUserRatingVectorsForMovie(movieId);
		userRatings.remove(userId);
		userVector = calculateMeanCenterRating(userVector);

		CosineVectorSimilarity calculator = new CosineVectorSimilarity();
		List<Neighbor> neighborhood = new ArrayList<Neighbor>();

		for (Entry<Long, MutableSparseVector> entry : userRatings.entrySet()) {
			Neighbor neighbor = new Neighbor();
			neighbor.setId(entry.getKey());

			SparseVector neighborVector = calculateMeanCenterRating(entry.getValue());
			neighbor.usetRatingVector(neighborVector);
			neighbor.setSimilarity(calculator.similarity(userVector, neighborVector));

			neighborhood.add(neighbor);
		}

		Collections.sort(neighborhood, Collections.reverseOrder());

		return (neighborhood.size() < 30) ? neighborhood : neighborhood.subList(0, 30);
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

	private Map<Long, MutableSparseVector> getAllUserRatingVectorsForMovie(Long id) {
		LongSet userSet = itemDao.getUsersForItem(id);
		Map<Long, MutableSparseVector> userRatingVectors = new HashMap<Long, MutableSparseVector>();

		for (Long userId : userSet) {
			userRatingVectors.put(userId, getUserRatingVector(userId).mutableCopy());
		}

		return userRatingVectors;
	}
}
