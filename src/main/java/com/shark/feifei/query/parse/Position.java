package com.shark.feifei.query.parse;

/**
 * Describe string position in parent string
 * @Author: Shark Chili
 * @Date: 2018/10/16 0016
 */
public class Position implements Comparable<Position> {
	/**left index*/
	int leftIndex;
	/**right index*/
	int rightIndex;

	public Position(int leftIndex,int length) {
		this.leftIndex = leftIndex;
		calculateRightIndex(length);
	}

	private void calculateRightIndex(int length){
		rightIndex=leftIndex+length;
	}

	@Override
	public String toString() {
		return "Position{" +
				"leftIndex=" + leftIndex +
				", rightIndex=" + rightIndex +
				'}';
	}

	@Override
	public int compareTo(Position o) {
		return this.leftIndex-o.leftIndex;
	}
}
