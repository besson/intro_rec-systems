package edu.umn.cs.recsys.uu;

import org.grouplens.lenskit.vectors.SparseVector;


public class Neighbor implements Comparable<Neighbor>{

	private Long id;
	private Double similarity;
	private SparseVector vector;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public void usetRatingVector(SparseVector vector) {
		this.vector = vector;
	}

	public SparseVector getVector() {
		return vector;
	}

	public void setVector(SparseVector vector) {
		this.vector = vector;
	}

	@Override
	public int compareTo(Neighbor o) {
		return this.getSimilarity().compareTo(o.getSimilarity());
	}

}
