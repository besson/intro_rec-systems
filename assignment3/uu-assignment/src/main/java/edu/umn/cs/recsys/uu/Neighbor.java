package edu.umn.cs.recsys.uu;


public class Neighbor implements Comparable<Neighbor>{

	private Long id;
	private Double score;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int compareTo(Neighbor arg0) {
		return (arg0.getId() > this.score) ? 1 : 0;
	}
	
}
