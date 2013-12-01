package edu.umn.cs.recsys.ii;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GlobalScorerForMoreItemsTest {

	@BeforeMethod
	public void runRecommender() {
		String[] args = { "--basket", "77", "680" };
		IIMain.main(args);
	}

	@Test(dataProvider = "testData")
	public void shouldReturnTheCorrectResultsForGlobalScorer(Integer id, Recommendation expRec) throws Exception {
		List<Recommendation> recommendations = IIMain.getGlobalRecommendationsFor();
		Recommendation recommendation = recommendations.get(id);

		assertEquals(recommendation.getMovieTitle(), expRec.getMovieTitle());
		assertEquals(recommendation.getMovieScore(), expRec.getMovieScore());
	}

	@DataProvider(name = "testData")
	public Object[][] populateTestData() {
		return new Object[][] { { 0, getRecommendation(550, "Fight Club (1999)", "0.6941") },
				{ 1, getRecommendation(629, "The Usual Suspects (1995)", "0.5984") },
				{ 2, getRecommendation(238, "The Godfather (1972)", "0.5361") },
				{ 3, getRecommendation(278, "The Shawshank Redemption (1994)", "0.5166") },
				{ 4, getRecommendation(274, "The Silence of the Lambs (1991)", "0.4916") } };
	}

	private Recommendation getRecommendation(long id, String title, String score) {
		Recommendation recommendation = new Recommendation();
		recommendation.setMovieId(id);
		recommendation.setMovieTitle(title);
		recommendation.setMovieScore(score);

		return recommendation;
	}

}
