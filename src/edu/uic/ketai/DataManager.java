package edu.uic.ketai;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManager {

   private static final String DATABASE_NAME = "edu.uic.ketai.db";
   private static final int DATABASE_VERSION = 1;
  // private static final String TABLE_NAME = "table1";
   private static final String SENSOR_DATA_TABLE_NAME = "sensor_data";
   private static final String SENSOR_RAW_DATA = "sensor_raw_data";
   private static final String IMAGE_DATA_TABLE = "image_data";
   private static final String RFID_TABLE_NAME = "rfid_data";
   private static final String QRCODE_TABLE_NAME = "qrcode_data";

   private Context context;
   private SQLiteDatabase db;

   private SQLiteStatement insertStmt, sqlStatement;
   private static final String INSERT_SENSOR_DATA = "insert into " + SENSOR_DATA_TABLE_NAME + "(timestamp, sensor_type) values (?, ?)";
   private static final String INSERT_SENSOR_RAW_DATA = "insert into " + SENSOR_RAW_DATA + "(sensorIndex, value, parent) values (?, ?, ?)";
   private static final String INSERT_IMAGE_DATA = "insert into " + IMAGE_DATA_TABLE + "(data, timestamp) values (?, ?)";
   private static final String INSERT_RFID_DATA = "insert into " + RFID_TABLE_NAME + "(data, timestamp) values (?, ?)";
   private static final String INSERT_QRCODE = "insert into " + QRCODE_TABLE_NAME + "(data, timestamp) values (?, ?)";
   
   public DataManager(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
   }
   
   public SQLiteDatabase getDb() {
      return this.db;
   }

   public long insertQRCode(String name, Long timestamp) {
	  this.insertStmt = this.db.compileStatement(INSERT_QRCODE);
      this.insertStmt.bindString(1, name);
      this.insertStmt.bindLong(2, (new Date()).getTime());
      return this.insertStmt.executeInsert();
   }
   
   public void insertSensorData(Long timestamp, int type, float vals[])
   {
	   this.insertStmt = this.db.compileStatement(INSERT_SENSOR_DATA);
	   this.insertStmt.bindLong(1,timestamp);
	   this.insertStmt.bindLong(2, type);
	   long parent = this.insertStmt.executeInsert();
	   
	   for (int i = 0; i < vals.length; i ++)
	   {
		   this.insertStmt = this.db.compileStatement(INSERT_SENSOR_RAW_DATA);
		   this.insertStmt.bindLong(1, i);
		   this.insertStmt.bindDouble(2, (double)vals[i]);
		   this.insertStmt.bindLong(3, parent);
		   this.insertStmt.executeInsert();		   
	   }
   }
   
   public void deleteAll() {
      this.db.delete(SENSOR_DATA_TABLE_NAME, null, null);
      this.db.delete(SENSOR_RAW_DATA, null, null);
      this.db.delete(IMAGE_DATA_TABLE, null, null);
      this.db.delete(RFID_TABLE_NAME, null, null);
      this.db.delete(QRCODE_TABLE_NAME, null, null);
   }

   public List<String> selectAll() {
      List<String> list = new ArrayList<String>();
      Cursor cursor = this.db.query(QRCODE_TABLE_NAME, new String[] { "data", "timestamp" }, null, null, null, null, "timestamp desc");
      if (cursor.moveToFirst()) {
         do {
            list.add(cursor.getString(0) + ":" + cursor.getString(1)); 
         } while (cursor.moveToNext());
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      return list;
   }
   
   public long getRawSensorDataCount()
   {
	   try
	   {
		   this.sqlStatement = this.db.compileStatement("SELECT COUNT(*) FROM sensor_raw_data");
		   return   this.sqlStatement.simpleQueryForLong();
	   }
	   catch (SQLiteDoneException x) { return 0; }
   }

   private static class OpenHelper extends SQLiteOpenHelper {

      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + SENSOR_DATA_TABLE_NAME + " (id INTEGER PRIMARY KEY, timestamp BIGINT, sensor_type INTEGER)");
         db.execSQL("CREATE TABLE " + SENSOR_RAW_DATA + " (id INTEGER PRIMARY KEY, value FLOAT, sensorIndex INTEGER, parent INTEGER, FOREIGN KEY (parent) REFERENCES " + SENSOR_DATA_TABLE_NAME +  ")");
         db.execSQL("CREATE TABLE " + IMAGE_DATA_TABLE + " (id INTEGER PRIMARY KEY, timestamp BIGINT, name TEXT)");
         db.execSQL("CREATE TABLE " + RFID_TABLE_NAME + " (id INTEGER PRIMARY KEY, timestamp BIGINT, value INTEGER, intensity INTEGER)");
         db.execSQL("CREATE TABLE " + QRCODE_TABLE_NAME + " (id INTEGER PRIMARY KEY, timestamp BIGINT, data TEXT)");

      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + SENSOR_DATA_TABLE_NAME);
         db.execSQL("DROP TABLE IF EXISTS " + SENSOR_RAW_DATA);
         db.execSQL("DROP TABLE IF EXISTS " + IMAGE_DATA_TABLE);
         db.execSQL("DROP TABLE IF EXISTS " + RFID_TABLE_NAME);
         db.execSQL("DROP TABLE IF EXISTS " + QRCODE_TABLE_NAME);
                   
         onCreate(db);
      }
   }
}