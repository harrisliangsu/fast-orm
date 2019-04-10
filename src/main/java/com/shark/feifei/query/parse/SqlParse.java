package com.shark.feifei.query.parse;

import com.google.common.collect.Lists;
import com.shark.feifei.Exception.SqlParseException;
import com.shark.feifei.query.consts.Sql;
import com.shark.util.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parse sql string,get {@link KeyWord}
 * @Author: SuLiang
 * @Date: 2018/10/16 0016
 */
public class SqlParse {
	/**origin sql*/
	private String sql;
	/**keywords*/
	List<KeyWord> keywords;
	/**statement=update or delete or insert*/
	private String statement;

	public SqlParse() {
		keywords= Lists.newArrayList();
	}

	public SqlParse(String sql) {
		this();
		this.sql = sql;
		checkLegal();
	}

	/**
	 * Parse this sql.
	 */
	public void parse(){
		for (String keywordStr : Sql.KEYWORDS) {
			Map<Integer,Integer> indexs= StringUtil.indexOf(sql,keywordStr);
			if (indexs!=null&&!indexs.isEmpty()){
				KeyWord keyWord=new KeyWord(keywordStr);
				keywords.add(keyWord);
				for (Integer index : indexs.values()) {
					keyWord.addPosition(index);
				}
			}
		}
	}

	/**
	 * Replace string between keyword1 occurrence time and keyword2 occurrence with replace string
	 * @param keyword1Str keyword 1 string
	 * @param occurrenceTime1 how many time keyword 1 occurrence
	 * @param keyword2Str keyword 1 string
	 * @param occurrenceTime2 how many time keyword 1 occurrence
	 * @param replaceStr to replace string use this
	 * @return string that had replaced.
	 */
	public String replace(String keyword1Str, int occurrenceTime1, String keyword2Str, int occurrenceTime2, String replaceStr){
		KeyWord keyWord1=findKeyword(keyword1Str);
		KeyWord keyWord2=findKeyword(keyword2Str);
		int startIndex=keyWord1.positions.get(occurrenceTime1-1).rightIndex;
		int endIndex=keyWord2.positions.get(occurrenceTime2-1).leftIndex;
		return this.sql.substring(0,startIndex+1)+" "+replaceStr+" "+ this.sql.substring(endIndex);
	}

	/**
	 * Replace between this keyword that occurrence on this time and next keyword.
	 * @param keywordStr keyword string
	 * @param occurrenceTime how many time keyword occurrence
	 * @param replaceStr to replace string use this
	 * @return string that had replaced.
	 */
	public String replaceToNextKeyword(String keywordStr,int occurrenceTime,String replaceStr){
		KeyWord keyWordStart=findKeyword(keywordStr);
		// 排序一次
		keywords.sort(null);
		int nextKeyWordIndex=keywords.indexOf(keyWordStart);
		KeyWord nextKeyWord=keywords.get(nextKeyWordIndex+1);
		return replace(keywordStr,occurrenceTime,nextKeyWord.keyword,1,replaceStr);
	}

	/**
	 * Find string after keyword and index is afterIndex
	 * @param keywordStr keyword string
	 * @param afterIndex start at 1.
	 * @return string after keywordStr
	 */
	public String findStrAfterKeyword(String keywordStr,int afterIndex){
		KeyWord keyWord=findKeyword(keywordStr);
		String resetSql=sql.substring(keyWord.positions.get(0).rightIndex+1);
		String[] splits=resetSql.split(" ");
		List<String> resetStrs=Lists.newArrayList();
		for (String split : splits) {
			if (!StringUtil.isEmpty(split)){
				resetStrs.add(split.trim());
			}
		}
		if (afterIndex<=resetStrs.size()+1){
			return resetStrs.get(afterIndex-1);
		}else {
			throw new SqlParseException("after index {},{} is out of bound when find str after keyword",afterIndex,resetStrs.size()+1);
		}
	}

	/**
	 * Find the keyword according to keyword string
	 * @param keywordStr keyword string
	 * @return {@link KeyWord} represent by the keywordStr
	 */
	private KeyWord findKeyword(String keywordStr){
		// 统一转成大写比较
		List<KeyWord> listKeyword=this.keywords.stream()
				.filter(k->k.keyword.trim().equals(keywordStr.toUpperCase().trim()))
				.collect(Collectors.toList());
		if (listKeyword.isEmpty()){
			throw new SqlParseException("keyword %s is not existed",keywordStr);
		}
		if (listKeyword.size()>1){
			throw new SqlParseException("keyword %s parse error %s",keywordStr,listKeyword);
		}
		return listKeyword.get(0);
	}



	/**
	 * Check whether exist multi statement or not
	 */
	private void checkLegal() {
		List<String> containKeyword=Lists.newArrayList();
		//1.检测是否合法
		for (int i = 0; i < sql.length(); i++) {
			for (String k : Sql.STATEMENT) {
				if (i+k.length()>sql.length()) break;
				String subStr=sql.substring(i,i+k.length());
				// 转成大写,去掉空格比较
				subStr=subStr.toUpperCase().trim();
				if (k.equals(subStr)){
					containKeyword.add(k);
				}
			}
		}
		if (containKeyword.isEmpty()){
			throw new SqlParseException("sql is illegal,it is short of keyword(select or update or insert or delete) ,{}",sql);
		}
		this.statement =containKeyword.stream().findFirst().get();
	}

	public String getStatement() {
		return statement;
	}

	public SqlParse setSql(String sql) {
		this.sql = sql;
		return this;
	}

	@Override
	public String toString() {
		return "SqlParse{" +
				"sql='" + sql + '\'' +
				", keywords=" + keywords +
				", statement='" + statement + '\'' +
				'}';
	}
}
