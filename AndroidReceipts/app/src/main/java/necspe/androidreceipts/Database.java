package necspe.androidreceipts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by santeri on 23.12.2016.
 */
public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AndroidReceipts";
    public static final String TABLES_TABLE_NAME = "Tables";
    public static final String DATA_TABLE_NAME = "Data";
    Context c;
    GFunctions f;

    public Database(Context c){
        super(c, DATABASE_NAME, null, 1);
        this.c = c;
        f = new GFunctions(c);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "create table " + TABLES_TABLE_NAME
                + "(name text primary key);"
        );
        db.execSQL(
                "create table " + DATA_TABLE_NAME
                + "(checks text primary key, tablename text, sum text, date text, desc text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oVer, int nVer){
        db.execSQL("DROP TABLE IF EXISTS " + TABLES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
        onCreate(db);
    }

    public boolean addTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("name", tableName);
        db.insert(TABLES_TABLE_NAME, null, vals);
        return true;
    }

    public boolean removeTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE_NAME, "tablename = ?", new String[]{tableName});
        db.delete(TABLES_TABLE_NAME, "name = ?", new String[]{tableName});
        return true;
    }

    public boolean addReceipt(String tableName, String sum, String date, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("tablename", tableName);
        vals.put("sum", sum);
        vals.put("date", date);
        vals.put("desc", desc);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((tableName + sum + date + desc + System.nanoTime()).getBytes());
            vals.put("checks", String.format("%064x", new java.math.BigInteger(1, md.digest())));
        } catch (Exception e){
            System.out.println("Error trying to calculate hash");
        }
        db.insert(DATA_TABLE_NAME, null, vals);
        return true;
    }

    public boolean removeReceipt(String check){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE_NAME, "checks = ?", new String[]{check});
        return true;
    }

    public ArrayList<ArrayList<String>> getReceipts(String tableName, String start, String end){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String placeholder = "DD--MM--YYYY";

        if (start.equals(placeholder) && end.equals(placeholder)){
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName + "'", null
            );
            f.makeList(res, list);

            return list;
        } else if (start.equals(placeholder)){
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date <= '" + end + "'", null
            );
            f.makeList(res, list);
            return list;
        } else if (end.equals(placeholder)){
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date >= '" + start + "'", null
            );
            f.makeList(res, list);
            return list;
        } else {
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date >= '" + start + "' and date <= '" + end + "'", null
            );
            f.makeList(res, list);
            return list;
        }

    }

    public ArrayList<String> getAllTables(){
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(
                "select * from " + TABLES_TABLE_NAME + "", null
        );
        res.moveToFirst();
        while(!res.isAfterLast()){
            String name = res.getString(res.getColumnIndex("name"));
            list.add(name);
            res.moveToNext();
        }
        res.close();
        return list;
    }



}
