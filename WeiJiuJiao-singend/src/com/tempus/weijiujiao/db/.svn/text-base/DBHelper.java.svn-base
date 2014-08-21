package com.tempus.weijiujiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cache_db.db";
	private static final String DATABASE_TABLE_NAME = "history_table";
	private static final String DATABASE_TABLE_HISTORY_SERARCH = "history_seach_table";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "// 自增id
				+ "product_id VARCHAR, "// 商品id
				+ "name VARCHAR, "// 商品名称
				+ "name_en VARCHAR, "// 商品英文名称
				+ "year VARCHAR, "// 商品年份
				+ "volume VARCHAR, "// 商品含量
				+ "price VARCHAR, "// 价格
				+ "seller VARCHAR, "// 商家
				+ "image VARCHAR, "// 图片
				+ "star VARCHAR)");// 星级
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE_HISTORY_SERARCH
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "// 自增id
				+ "seachKey VARCHAR)");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
	}

	public String getTableName() {
		return DATABASE_TABLE_NAME;
	}
	public String getTableNameSearchKey() {
		return DATABASE_TABLE_HISTORY_SERARCH;
	}
}
