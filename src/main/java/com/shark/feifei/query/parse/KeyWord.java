package com.shark.feifei.query.parse;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Sql keyword
 * @Author: Shark Chili
 * @Date: 2018/10/16 0016
 */
public class KeyWord implements Comparable<KeyWord> {
	/**keyword*/
	String keyword;
	/**position in sql*/
	List<Position> positions;

	public KeyWord() {
		positions= Lists.newArrayList();
	}

	public KeyWord(String keyword) {
		this();
		this.keyword = keyword;
	}

	public void addPosition(int leftIndex){
		Position position=new Position(leftIndex,keyword.length());
		positions.add(position);
	}

	@Override
	public String toString() {
		return "KeyWord{" +
				"keyword='" + keyword + '\'' +
				", positions=" + positions +
				'}';
	}

	@Override
	public int compareTo(KeyWord o) {
		Position myMaxPosition= Collections.max(positions);
		Position otherMaxPosition=Collections.max(o.positions);
		return myMaxPosition.leftIndex-otherMaxPosition.leftIndex;
	}
}
