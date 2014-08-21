package com.tempus.weijiujiao.db;

import java.util.ArrayList;
import java.util.List;

import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private String tabName;
	private String tabname_searchkey;
	private int clumeCount = 10;
	private static DBManager manager;

	/**
	 * 单例模式
	 * 
	 * @param context
	 * @return
	 */
	public static DBManager getInstance(Context context) {
		if (manager == null) {
			manager = new DBManager(context);
		}
		return manager;
	}

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
		tabName = helper.getTableName();
		tabname_searchkey = helper.getTableNameSearchKey();
	}

	public void add(List<BasicInfo> basicInfoList) {
		db.beginTransaction(); // 开始事务
		try {
			for (BasicInfo basicInfo : basicInfoList) {
				add(basicInfo);
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void add(BasicInfo basicInfo) {
		deleteBasic(basicInfo);
		insertBasic(basicInfo);
	}

	private void insertBasic(BasicInfo basicInfo) {
		// TODO Auto-generated method stub
		db.execSQL("INSERT INTO " + tabName+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?,?)",
				new String[] { basicInfo.getId(), basicInfo.getName(),
						basicInfo.getName_en(), basicInfo.getYear(),
						basicInfo.getVolume(), basicInfo.getPrice() + "",
						basicInfo.getSeller(), basicInfo.getImageURL(),
						basicInfo.getStar() + "" });
	}

	public String[] getKeyArray() {
		String sql = "SELECT * FROM " + this.tabname_searchkey;
		Cursor c = queryTheCursor(sql);
		String[] array = new String[c.getCount()];
		int index = 0;
		while (c.moveToNext()) {
			array[index] = c.getString(c.getColumnIndex("seachKey"));
			index++;
		}
	
		return array;
	}

	public void addKey(String key) {
		checkKeyNumbers();
		db.execSQL(
				"INSERT INTO " + this.tabname_searchkey + " VALUES(null, ?)",
				new String[] { key });
	}

	private void checkKeyNumbers() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM " + this.tabname_searchkey;
		Cursor c = queryTheCursor(sql);
		int count = c.getCount();
		if (count == 6) {
			c.moveToNext();
			int id = c.getInt(c.getColumnIndex("_id"));
			db.delete(this.tabname_searchkey, "_id = ?",new String[] { id + "" });
		}
	}

//	private boolean isExsit(BasicInfo basicInfo) {
//		// TODO Auto-generated method stub
//		String sql = "SELECT * FROM " + this.tabName + " where product_id=\""+ basicInfo.getId() + "\"";
//		Cursor c = queryTheCursor(sql);
//		if (c.moveToNext()) {
//			return true;
//		}
//		return false;
//	}

	public void deleteBasic(BasicInfo basicInfo) {
		db.delete(tabName, "product_id = ?", new String[] { basicInfo.getId() });
	}

	public void clearAllBsics() {
		db.delete(tabName, null, null);
	}

	public List<BasicInfo> getBasicList(int index) {
		ArrayList<BasicInfo> basics = new ArrayList<BasicInfo>();
		Cursor c = queryTheCursor();
		int i = 0;
		while (c.moveToNext()) {
			if (index * clumeCount <= i && i < (index + 1) * clumeCount) {
				BasicInfo basic = new ProductInfo().new BasicInfo();
				basic.setId(c.getString(c.getColumnIndex("product_id")));
				basic.setName(c.getString(c.getColumnIndex("name")));
				basic.setName_en(c.getString(c.getColumnIndex("name_en")));
				basic.setYear(c.getString(c.getColumnIndex("year")));
				basic.setVolume(c.getString(c.getColumnIndex("volume")));
				basic.setPrice(Float.parseFloat(c.getString(c
						.getColumnIndex("price"))));
				basic.setSeller(c.getString(c.getColumnIndex("seller")));
				basic.setStar(Integer.parseInt(c.getString(c
						.getColumnIndex("star"))));
				basic.setImageURL(c.getString(c.getColumnIndex("image")));
				basics.add(basic);
			}
			i++;
		}
		c.close();
		return basics;
	}

	/**
	 * query all basicInfos, return list
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String[] selectionArgs) {
		Cursor c = db.rawQuery("SELECT * FROM " + tabName, selectionArgs);
		return c;
	}

	public Cursor queryTheCursor(String sql) {
		Cursor c = db.rawQuery(sql, null);
		return c;
	}

	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT " + "* FROM  " + tabName, null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
