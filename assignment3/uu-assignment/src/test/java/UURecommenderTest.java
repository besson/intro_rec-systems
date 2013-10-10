import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import edu.umn.cs.recsys.uu.Recommendation;
import edu.umn.cs.recsys.uu.UUMain;

public class UURecommenderTest {

	@Test(dataProvider = "testData")
	public void shouldReturnTheCorrectResultForFirstExample(String input, Recommendation expRec) throws Exception {
		String[] args = new String[1];
		args[0] = input;

		UUMain.main(args);

		List<Recommendation> recommendations = UUMain.getRecommendationsFor(2048l);
		Recommendation recommendation = recommendations.get(0);
	
		assertEquals(recommendation.getMovieId(), expRec.getMovieId());
		assertEquals(recommendation.getMovieTitle(), expRec.getMovieTitle());
		assertEquals(recommendation.getMovieScore(), expRec.getMovieScore());
	}
	
	@DataProvider(name = "testData")
	public Object[][] populateTestData() {
		return new Object[][] {
				{"2048:77", getRecommendation(77l, "Mrs. Doubtfire (1993)", 3.8509f)},
				{"2048:36955", getRecommendation(36955l, "True Lies (1994)", 3.9698f)},
				{"2048:788", getRecommendation(788l, "Mrs. Doubtfire (1993)", 4.2155f)},
				{"1024:462", getRecommendation(462, "Erin Brockovich (2000)", 3.1082)},
				{"1024:393", getRecommendation(393, "Kill Bill: Vol. 2 (2004)", 3.8722)},
				{"1024:36955", getRecommendation(36955, "True Lies (1994)", 2.3524)},
				{"1024:77", getRecommendation(77, "Memento (2000)", 4.3848)},
				{"1024:268", getRecommendation(268, "Batman (1989)", 2.8646)}
		};
	}

	private Recommendation getRecommendation(long id, String title, double score) {
		Recommendation recommendation = new Recommendation();
		recommendation.setMovieId(id);
		recommendation.setMovieTitle(title);
		recommendation.setMovieScore(score);
		
		return recommendation;
	}
	
}
