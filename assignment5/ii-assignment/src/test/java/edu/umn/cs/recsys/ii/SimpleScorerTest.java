package edu.umn.cs.recsys.ii;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SimpleScorerTest {

	@Test(dataProvider = "testData")
	public void shouldReturnTheCorrectResultsForSimpleScorer(String input, Recommendation expRec) throws Exception {
		String[] args = new String[1];
		args[0] = input;

		IIMain.main(args);

		List<Recommendation> recommendations = IIMain.getRecommendationsFor(input.split(":")[0]);
		Recommendation recommendation = recommendations.get(0);
	
		assertEquals(recommendation.getMovieId(), expRec.getMovieId());
		assertEquals(recommendation.getMovieTitle(), expRec.getMovieTitle());
		assertEquals(recommendation.getMovieScore(), expRec.getMovieScore());
	}
	
	@DataProvider(name = "testData")
	public Object[][] populateTestData() {
		return new Object[][] {
				{"1024:77", getRecommendation(77, "Memento (2000)", "4.1968")},
				{"1024:268", getRecommendation(268, "Batman (1989)", "2.3366")},
				{"1024:462", getRecommendation(462, "Erin Brockovich (2000)", "2.9900")},
				{"1024:393", getRecommendation(393, "Kill Bill: Vol. 2 (2004)", "3.7702")},
				{"1024:36955", getRecommendation(36955, "True Lies (1994)", "2.5612")},
				{"2048:77", getRecommendation(77l, "Memento (2000)", "4.5102")},
				{"2048:36955", getRecommendation(36955l, "True Lies (1994)", "3.8545")},
				{"2048:788", getRecommendation(788l, "Mrs. Doubtfire (1993)", "4.1253")},
		};
	}

	private Recommendation getRecommendation(long id, String title, String score) {
		Recommendation recommendation = new Recommendation();
		recommendation.setMovieId(id);
		recommendation.setMovieTitle(title);
		recommendation.setMovieScore(score);
		
		return recommendation;
	}
	
}
