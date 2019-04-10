package com.shark.feifei.data;

import com.google.common.collect.*;

import java.util.Map;
import java.util.Set;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/11 0011
 */
public class MapTable {
	/**transaction data map*/
	private Map<Object, Object> transactionDataMap;
	/**transaction data table*/
	private Table<Object, Object, Object> transactionDataTable;

	public void putData(Object key,Object value){
		checkMapNull();
		transactionDataMap.put(key, value);
	}

	public void putData(Object row,Object column,Object value){
		checkTableNull();
		transactionDataTable.put(row, column, value);
	}

	public <T> T getData(Object key) {
		checkMapNull();
		return (T) transactionDataMap.get(key);
	}

	public <T> T getData(Object row, Object column) {
		checkTableNull();
		return (T) transactionDataTable.get(row, column);
	}

	public void clearData(Object key) {
		checkMapNull();
		checkTableNull();
		boolean clearFromTable=transactionDataMap.remove(key) == null;
		if (clearFromTable){
			Map<Object, Object> columnData = transactionDataTable.row(key);
			Set<Object> remove= Sets.newHashSet(columnData.keySet());
			for (Object column : remove) {
				transactionDataTable.remove(key, column);
			}
		}
	}

	public void clearData(Object row,Object column){
		transactionDataTable.remove(row, column);
	}

	public Map<Object, Object> getTransactionDataMap() {
		return transactionDataMap;
	}

	public Table<Object, Object, Object> getTransactionDataTable() {
		return transactionDataTable;
	}

	private synchronized void checkMapNull(){
		if (this.transactionDataMap==null){
			this.transactionDataMap= Maps.newConcurrentMap();
		}
	}

	private synchronized void checkTableNull(){
		if (this.transactionDataTable==null) {
			this.transactionDataTable=Tables.synchronizedTable(HashBasedTable.create());
		}
	}
}
