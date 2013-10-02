package org.grouplens.mooc.cbf;

import java.io.File;
import java.util.List;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.mooc.cbf.dao.CSVItemTagDAO;
import org.grouplens.mooc.cbf.dao.ItemTitleDAO;
import org.grouplens.mooc.cbf.dao.MOOCItemDAO;
import org.grouplens.mooc.cbf.dao.MOOCRatingDAO;
import org.grouplens.mooc.cbf.dao.UserItemDAO;
import org.grouplens.mooc.cbf.dao.MOOCUserDAO;
import org.grouplens.mooc.cbf.dao.RatingFile;
import org.grouplens.mooc.cbf.dao.TagFile;
import org.grouplens.mooc.cbf.dao.TitleFile;
import org.grouplens.mooc.cbf.dao.UserFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple hello-world program.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class CBFMain {
    private static final Logger logger = LoggerFactory.getLogger(CBFMain.class);
	private static ItemRecommender irec;

    public static void main(String[] args) throws RecommenderBuildException {
        LenskitConfiguration config = configureRecommender();
        
        logger.info("building recommender");
        Recommender rec = LenskitRecommender.build(config);
        
        ItemTitleDAO dao = new UserItemDAO(new File("data/movie-titles.csv"));
        UserItemDAO rDao = new UserItemDAO(new File("data/ratings.csv"));

       
        if (args.length == 0) {
            logger.error("No users specified; provide user IDs as command line arguments");
        }

        // we automatically get a useful recommender since we have a scorer
        irec = rec.getItemRecommender();
        assert irec != null;
        try {
            // Generate 5 recommendations for each user
        
            for (String user: args) {
                long uid;
                try {
                    uid = Long.parseLong(user);
                    List<String> userTitles = rDao.getUserTitles(uid);
                    System.out.println("Watched movies: \n");
                    for(String titleId : userTitles) {
                    	System.out.println(dao.getItemTitle(Long.parseLong(titleId)));
                    }
                    System.out.println("-----------------------------------\n");

                } catch (NumberFormatException e) {
                    logger.error("cannot parse user {}", user);
                    continue;
                }
                List<ScoredId> recs = irec.recommend(uid, 5);
                if (recs.isEmpty()) {
                    logger.warn("no recommendations for user {}, do they exist?", uid);
                }
                System.out.format("Recommendations: \n");
                for (ScoredId id: recs) {
                    System.out.format("title: %s => %.4f\n", dao.getItemTitle(id.getId()), id.getScore());
                }
            }
        } catch (UnsupportedOperationException e) {
            if (e.getMessage().equals("stub implementation")) {
                System.out.println("Congratulations, the stub builds and runs!");
            }
        }
    }
    
    public static List<ScoredId> getRecommendationsFor(Long userId, Integer recs) {
    	return irec.recommend(userId, recs);
    }

    /**
     * Create the LensKit recommender configuration.
     * @return The LensKit recommender configuration.
     */
    // LensKit configuration API generates some unchecked warnings, turn them off
    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        // configure the rating data source
        config.bind(EventDAO.class)
              .to(MOOCRatingDAO.class);
        config.set(RatingFile.class)
              .to(new File("data/ratings.csv"));
        
        // use custom item and user DAOs
        // specify item DAO implementation with tags
        config.bind(ItemDAO.class)
              .to(CSVItemTagDAO.class);
        // specify tag file
        config.bind(ItemTitleDAO.class)
        .to(MOOCItemDAO.class);

        config.set(TagFile.class)
              .to(new File("data/movie-tags.csv"));
        // and title file

        config.set(TitleFile.class)
              .to(new File("data/movie-titles.csv"));

        // our user DAO can look up by user name
        config.bind(UserDAO.class)
              .to(MOOCUserDAO.class);
        config.set(UserFile.class)
              .to(new File("data/users.csv"));

        // use the TF-IDF scorer you will implement to score items
        config.bind(ItemScorer.class)
              .to(TFIDFItemScorer.class);
        return config;
    }
}