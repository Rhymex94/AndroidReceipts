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
 * Database class represents the SQLite database used to store all data within the application.
 * It contains methods for managing the database in various ways, for example adding and deleting
 * receipts. An instance of this class is created and used in pretty much every Activity across
 * the app.
 */
public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AndroidReceipts";
    public static final String TABLES_TABLE_NAME = "Tables";
    public static final String DATA_TABLE_NAME = "Data";
    Context c;
    GFunctions f;

    /**
     * The initializing function: used whenever an instance of Database is required.
     * @param c the Context from which the function was called.
     */
    public Database(Context c){
        super(c, DATABASE_NAME, null, 1);
        this.c = c;
        // GFunctions is used to access general, often used set of functions.
        f = new GFunctions(c);
    }

    /**
     * onCreate function is called automatically whenever an instance of the Database is created.
     * It creates, if they don't already exist, the tables for Tables and Data, latter of which
     * represents the receipts.
     * @param db the SQLite database used by the app.
     */
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

    /**
     * onUpgrade function is used when the Database is upgraded, i.e. it's tables are modified or
     * more are added or removed. It creates a new Database including the changes which were
     * made for it. All data is dropped in the process.
     * @param db the SQLite database used by the application.
     * @param oVer the version number of the old version.
     * @param nVer the version number of the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oVer, int nVer){
        db.execSQL("DROP TABLE IF EXISTS " + TABLES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
        onCreate(db);
    }

    /**
     * addTable function creates a new entry into the TABLES -table, using a name provided as a
     * parameter.
     * @param tableName the desired name for the new table entry.
     * @return True.
     */
    public boolean addTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("name", tableName);
        db.insert(TABLES_TABLE_NAME, null, vals);
        return true;
    }

    /**
     * removeTable function is quite self explanatory: it removes the contents of a given table,
     * and finally removes the table entry itself.
     * @param tableName the name of the table which the user wishes to delete.
     * @return True.
     */
    public boolean removeTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        // Remove the receipts contained by the table entry to be removed.
        db.delete(DATA_TABLE_NAME, "tablename = ?", new String[]{tableName});
        // and finally delete the table itself.
        db.delete(TABLES_TABLE_NAME, "name = ?", new String[]{tableName});
        return true;
    }

    /**
     * addReceipt function creates a new entry into the Data -table from the parameters, which
     * is to represent a new receipt.
     * @param tableName name of the table into which the receipt is to be added.
     * @param sum the total sum of the costs of the receipt.
     * @param date the date when the receipt was issued.
     * @param desc a short description of what the receipt is about.
     * @return True.
     */
    public boolean addReceipt(String tableName, String sum, String date, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("tablename", tableName);
        vals.put("sum", sum);
        vals.put("date", date);
        vals.put("desc", desc);
        /**
         * In order to identify individual receipts, a checksum is calculated and added to the
         * entry. It is used, for example, removing receipts.
         */
        try {
            // We'll use SHA-256 algorithm for calculating the checksum.
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Checksum is calculated from the tableName, sum, date, desc and current system time.
            md.update((tableName + sum + date + desc + System.nanoTime()).getBytes());
            vals.put("checks", String.format("%064x", new java.math.BigInteger(1, md.digest())));
        } catch (Exception e){
            // If there were problems calculating the hash, we'll print an error.
            System.out.println("Error trying to calculate hash");
        }
        // Finally, we'll add receipt to the database.
        db.insert(DATA_TABLE_NAME, null, vals);
        return true;
    }

    /**
     * removeReceipt removes the receipt whose checksum is given as a parameter.
     * @param check the checksum of the receipt which the user wishes to remove.
     * @return
     */
    public boolean removeReceipt(String check){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE_NAME, "checks = ?", new String[]{check});
        return true;
    }

    /**
     * getReceipts doesn't change the database, it only queries it. It returns a list of receipts
     * which result in the query made with the given parameters.
     * @param tableName the name of the table from which the query is made.
     * @param start the start date constraint in text format.
     * @param end the end date constraint in text format.
     * @return the resulting list of the query.
     */
    public ArrayList<ArrayList<String>> getReceipts(String tableName, String start, String end){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // A placeholder string used to compare whether or not a date constraint is set.
        String placeholder = "DD--MM--YYYY";

        // If no dates are set...
        if (start.equals(placeholder) && end.equals(placeholder)){
            // return ALL receipts in that table.
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName + "' order by date desc", null
            );
            // makeList creates an ArrayList from Cursor object. See GFunctions for details.
            f.makeList(res, list);
            return list;

            // If only start date is set...
        } else if (start.equals(placeholder)){
            // return the receipts that has a date later than the parameter.
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date <= '" + f.dateConvert(end) + "' order by date desc", null
            );
            f.makeList(res, list);
            return list;

            // If only end date is set...
        } else if (end.equals(placeholder)){
            // return the receipts that has a date earlier than the parameter.
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date >= '" + f.dateConvert(start) + "' order by date desc", null
            );
            f.makeList(res, list);
            return list;

            // And if both are set...
        } else {
            // return the receipts whose date fall between the date constraints.
            Cursor res = db.rawQuery(
                    "select * from " + DATA_TABLE_NAME + " where tablename = '" + tableName
                    + "' and date >= '" + f.dateConvert(start) + "' and date <= '"
                    + f.dateConvert(end) + "' order by date desc", null
            );
            f.makeList(res, list);
            return list;
        }

    }

    /**
     * getAllTables queries the Tables table for all table names within the Database and returns
     * them.
     * @return a list of all table names within the Database.
     */
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
