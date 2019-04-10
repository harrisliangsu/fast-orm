package com.shark.feifei.query.query;

import com.shark.feifei.Exception.SqlException;

import java.sql.SQLException;

/**
 * Paging query,record cursor of page index,cursor start at 1,cursor can scroll cycle
 * @Author: Shark Chili
 * @Date: 2018/11/6
 */
public class PageData {
	/** page size(how man record in one page) */
	Integer pageSize;
	/** what page now */
	Integer nowIndex;
	/** total count of records */
	Integer totalCount;
	/** max page size */
	Integer maxPageSize;
	/** whether page data had initialize or not */
	boolean initialized;
	/** whether page query scroll. */
	boolean isPageScroll;

	public void init() throws SQLException {
		checkInitData();
		initMaxPageIndex();
		nowIndex=1;
	}

	/**
	 * Get max page index.
	 */
	private void initMaxPageIndex(){
		int rest=totalCount%pageSize;
		int pageCount=totalCount/pageSize;
		if (rest==0){
			this.maxPageSize=pageCount;
		}else {
			this.maxPageSize=pageCount+1;
		}
		this.initialized=true;
	}

	/**
	 * Cursor back to 1
	 * @return now index(cursor)
	 */
	int firstPage(){
		checkInitData();
		checkOutBound(1);
		this.nowIndex=1;
		return this.nowIndex;
	}

	/**
	 * Cursor forward to last index(max page size)
	 * @return now index(cursor)
	 */
	int lastPage(){
		checkInitData();
		checkOutBound(maxPageSize);
		this.nowIndex=maxPageSize;
		return this.nowIndex;
	}

	/**
	 * Cursor forward one time
	 * @return now index(cursor)
	 */
	int nextPageIndex(){
		checkInitData();
		checkOutBound(nowIndex+1);
		// 滚动到初始
		if (nowIndex.equals(maxPageSize)){
			nowIndex=0;
		}
		this.nowIndex+=1;
		return this.nowIndex;
	}

	/**
	 * Cursor back one time
	 * @return now index(cursor)
	 */
	int previousPageIndex(){
		checkInitData();
		checkOutBound(nowIndex-1);
		if (nowIndex==1){
			nowIndex=maxPageSize+1;
		}
		this.nowIndex-=1;
		return this.nowIndex;
	}

	/**
	 * Cursor forward to or back to this index
	 * @return now index(cursor)
	 */
	void page(int index){
		checkInitData();
		checkOutBound(index);
		// 大于最大页数
		if (index>maxPageSize){
			index=index%maxPageSize;
			if (index==0){
				index=maxPageSize;
			}
		}
		// 负数
		if (index<1){
			int residue=Math.abs(index)%maxPageSize;
			if (residue==0){
				index=1;
			}else {
				index=maxPageSize-residue;
			}
		}
		this.nowIndex=index;
	}

	private void checkOutBound(int index)throws SqlException {
		if (isPageScroll) return;
		if (index==nowIndex) return;
		boolean next=index>nowIndex;
		if (next&&nowIndex.equals(maxPageSize)){
			throw new SqlException("page index out of bound",nowIndex,maxPageSize);
		}
		if (!next&&nowIndex<=1){
			throw new SqlException("page index out of bound",0,nowIndex);
		}
	}

	private void checkInitData() throws  SqlException{
		if (totalCount==null||totalCount==0){
			throw new SqlException("total count had`t init");
		}
		if (pageSize == null||pageSize==0){
			throw new SqlException("page size had`t init");
		}
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getNowIndex() {
		return nowIndex;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getMaxPageSize() {
		return maxPageSize;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isPageScroll() {
		return isPageScroll;
	}

	@Override
	public String toString() {
		return "PageData{" +
				"pageSize=" + pageSize +
				", nowIndex=" + nowIndex +
				", totalCount=" + totalCount +
				", maxPageSize=" + maxPageSize +
				", initialized=" + initialized +
				", isPageScroll=" + isPageScroll +
				'}';
	}
}
