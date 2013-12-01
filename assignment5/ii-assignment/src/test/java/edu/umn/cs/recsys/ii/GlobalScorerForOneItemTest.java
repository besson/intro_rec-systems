package edu.umn.cs.recsys.ii;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GlobalScorerForOneItemTest {

	@BeforeMethod
	public void runRecommender() {
		String[] args = { "--basket", "77" };
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
		return new Object[][] { { 0, getRecommendation(550, "Fight Club (1999)", "0.3192") },
				{ 1, getRecommendation(629, "The Usual Suspects (1995)", "0.3078") },
				{ 2, getRecommendation(38, "Eternal Sunshine of the Spotless Mind (2004)", "0.2574") },
				{ 3, getRecommendation(278, "The Shawshank Redemption (1994)", "0.2399") },
				{ 4, getRecommendation(680, "Pulp Fiction (1994)", "0.2394") }, };
	}

	private Recommendation getRecommendation(long id, String title, String score) {
		Recommendation recommendation = new Recommendation();
		recommendation.setMovieId(id);
		recommendation.setMovieTitle(title);
		recommendation.setMovieScore(score);

		return recommendation;
	}

}
