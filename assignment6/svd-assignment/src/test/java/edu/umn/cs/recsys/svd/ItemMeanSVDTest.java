package edu.umn.cs.recsys.svd;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.umn.cs.recsys.svd.Recommendation;
import edu.umn.cs.recsys.svd.SVDMain;

public class ItemMeanSVDTest {

	@Test(dataProvider = "testData")
	public void shouldReturnTheCorrectResultsForSimpleScorer(String input, Recommendation expRec) throws Exception {
		String[] args = new String[2];
		args[0] = "--item-mean";
		args[1] = input;

		SVDMain.main(args);

		List<Recommendation> recommendations = SVDMain.getRecommendationsFor(input.split(":")[0]);
		Recommendation recommendation = recommendations.get(0);
	
		assertEquals(recommendation.getMovieId(), expRec.getMovieId());
		assertEquals(recommendation.getMovieTitle(), expRec.getMovieTitle());
		assertEquals(recommendation.getMovieScore(), expRec.getMovieScore());
	}
	
	@DataProvider(name = "testData")
	public Object[][] populateTestData() {
		return new Object[][] {
				{"1024:77", getRecommendation(77, "Memento (2000)", "4.3499")},
				{"1024:268", getRecommendation(268, "Batman (1989)", "3.0809")},
				{"1024:462", getRecommendation(462, "Erin Brockovich (2000)", "3.6878")},
				{"1024:393", getRecommendation(393, "Kill Bill: Vol. 2 (2004)", "3.4858")},
				{"1024:36955", getRecommendation(36955, "True Lies (1994)", "4.0488")},
				{"2048:77", getRecommendation(77l, "Memento (2000)", "3.8812")},
				{"2048:36955", getRecommendation(36955l, "True Lies (1994)", "4.0488")},
				{"2048:788", getRecommendation(788l, "Mrs. Doubtfire (1993)", "4.1674")},
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
