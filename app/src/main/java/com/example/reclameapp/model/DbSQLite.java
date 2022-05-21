package com.example.reclameapp.model;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbSQLite extends SQLiteOpenHelper {

    public DbSQLite(Context context){
        // contexto,nomedobanco,cursor, versão
        super(context, "banco.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            String sql;
            sql = "CREATE TABLE issue(id integer primary key autoincrement, user text, description text, latitude real, longitude real, day default CURRENT_TIMESTAMP, sinc text);";
            db.execSQL(sql);

            sql = "CREATE TABLE user(id integer primary key autoincrement, doc text, name text,password text);";
            db.execSQL(sql);

            sql = "CREATE TABLE image(id integer primary key autoincrement, issueId int, photo blob);";
            db.execSQL(sql);
        } catch (Exception ex) {
            Log.e("Error on create table: ", ex.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS issue");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}