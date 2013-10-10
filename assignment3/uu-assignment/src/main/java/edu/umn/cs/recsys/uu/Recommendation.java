package edu.umn.cs.recsys.uu;

public class Recommendation {

	private Long movieId;
	private Double movieScore;
	private String movieTitle;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public Double getMovieScore() {
		return movieScore;
	}

	public void setMovieScore(Double movieScore) {
		this.movieScore = movieScore;
	}
}
