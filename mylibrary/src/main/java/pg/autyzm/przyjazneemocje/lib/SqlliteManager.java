package pg.autyzm.przyjazneemocje.lib;

/**
 * Created by Ann on 26.09.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SqlliteManager extends SQLiteOpenHelper {

    private static final String TAG = "SqlliteManager";
    private static SqlliteManager sInstance;

    private static final String DATABASE_NAME = "przyjazneemocje";


    private SQLiteDatabase db;



    public static synchronized SqlliteManager getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx

        // System.out.println("Tworze ania3");
        // System.out.println(context.getApplicationContext().getPackageResourcePath());
        if (sInstance == null) {


            sInstance = new SqlliteManager(context.getApplicationContext());
        }
        return sInstance;
    }


    private SqlliteManager (final Context context)
    {


        super(new DatabaseContext(context), DATABASE_NAME, null, 2);

        //System.out.println("Tworze ania2");
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        this.db = db;

        System.out.println("Tworze tablee");
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, " +
                "time_limit int, is_level_active boolean, name text, correctness int, sublevels int);" + "");
        db.execSQL("create table levels_photos(" + "id integer primary key autoincrement,"  + "levelid integer references levels(id)," + "photoid integer references photos(id));" + "");
        db.execSQL("create table levels_emotions(" + "id integer primary key autoincrement," + "levelid integer references levels(id),"  + "emotionid integer references emotions(id));" + "");
        //ania dodaje probujac zmienic jezyk:
        // 1) ZlecenieIT - a
        db.execSQL("create table language(" + "id integer primary key autoincrement," +  "language text not null unique," + "selected integer default 0);" + "");


        addEmotion("happy");
        addEmotion("sad");
        addEmotion("angry");
        addEmotion("scared");
        addEmotion("surprised");
        addEmotion("bored");

        // 1) ZlecenieIT - b
        addLang(1, "pl", 1);
        addLang(2, "en", 0);

        // 3) ZlecenieIT - a
        Level level  = new Level();
        level.setPhotosOrVideosList(new ArrayList<Integer>());
        level.setName("Default level");
        level.setLevelActive(true);
        level.setTimeLimit(60);
        level.setCorrectness(2);
        level.setSublevels(2);
        level.setPvPerLevel(2);
        level.setEmotions(new ArrayList<Integer>(){
            {
                add(1); add(2);
            }
        });
        level.setPhotosOrVideosList(new ArrayList<Integer>(){
            {
                add(7); add(8); add(9); add(10);
            }
        });

        addLevel(level);
    }

    public void onOpen(SQLiteDatabase db){

        this.db = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "Database updating...");
        Log.d(TAG, "Database drop table...");
        db.execSQL("DROP TABLE IF EXISTS photos");
        db.execSQL("DROP TABLE IF EXISTS emotions");
        db.execSQL("DROP TABLE IF EXISTS levels");
        db.execSQL("DROP TABLE IF EXISTS levels_photos");
        db.execSQL("DROP TABLE IF EXISTS levels_emotions");
        db.execSQL("DROP TABLE IF EXISTS language");
        Log.d(TAG, "All data is lost.");
        Log.d(TAG, "Database create table...");
        onCreate(db);
    }

    public void addEmotion(String emotion)
    {
        ContentValues values = new ContentValues();
        values.put("emotion",emotion);
        System.out.println(db.insertOrThrow("emotions", null, values) + " addEmotion");
    }

    public void addPhoto(int path, String emotion, String fileName)
    {
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        values.put("name",fileName);
        db.insertOrThrow("photos", null, values);
    }


    public void addLevel(Level level)
    {
        ContentValues values = new ContentValues();
        values.put("photos_or_videos", level.getPhotosOrVideos());
        values.put("name", level.getName());
        values.put("photos_or_videos_per_level", level.getPvPerLevel());
        values.put("time_limit", level.getTimeLimit());
        values.put("is_level_active", level.isLevelActive());
        values.put("correctness", level.getCorrectness());
        values.put("sublevels", level.getSublevels());
        //ania
        //   values.put("language",level.getLanguage());


        if(level.getId() != 0) {
            //values.put("id", level.id);
            db.update("levels", values, "id=" + level.getId(), null);

            /*
                usunac wszystkie rekordy polaczone z tym poziomem.

            */

            delete("levels_photos", "levelid", String.valueOf(level.getId()));
            delete("levels_emotions", "levelid", String.valueOf(level.getId()));

        }
        else {
            long longId = db.insertOrThrow("levels", null, values);
            level.setId((int) longId);
        }

        // Dodaj rekordy wiele do wielu ze zdjeciami/video

        for(Integer photoOrVideo : level.getPhotosOrVideosList()){


            System.out.println("Photo id " + photoOrVideo);

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("photoid",photoOrVideo);

            db.insertOrThrow("levels_photos", null, values);
        }

        for(Integer emotion : level.getEmotions()){


            System.out.println("Emotion id " + emotion);

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("emotionid",emotion);

            db.insertOrThrow("levels_emotions", null, values);
        }


    }

    // 1) ZlecenieIT - c
    public void addLang(int id, String lang, Integer selected){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("language", lang);
        values.put("selected", selected);
        db.insertOrThrow("language", null, values);
    }

    public void delete(String tableName, String columnName, String value)
    {
        String[] args = {"" + value};
        db.delete(tableName, columnName + "=?",args);
    }

    public void cleanTable(String tableName)
    {
        db.execSQL("delete from "+ tableName);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + tableName +"'");
    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotoWithPath(String path)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"path like " + "'%" + path + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotoWithId(int id)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"id like " + "'%" + id + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotosInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "photoid"};
        Cursor cursor = db.query("levels_photos", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionsInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "emotionid"};
        Cursor cursor = db.query("levels_emotions", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionId(String name){

        String[] columns = {"id", "emotion"};
        Cursor cursor = db.query("emotions", columns,"emotion like " + "'%" + name + "%'", null, null, null, null);
        return cursor;

    }

    public Cursor giveEmotionName(int id){

        String[] columns = {"id", "emotion"};
        Cursor cursor = db.query("emotions", columns,"id like " + "'%" + id + "%'", null, null, null, null);
        return cursor;

    }

    public Cursor giveAllEmotions()
    {
        String[] columns = {"id","emotion"};//"photos_or_videos", "photos_or_videos_per", "time_limit"
        Cursor cursor = db.query("emotions", columns,null, null, null, null, null);
        return cursor;
    }

    public Cursor giveAllLevels()
    {
        String[] columns = {"id", "photos_or_videos", "is_level_active", "name"};
        Cursor cursor = db.query("levels", columns,null, null, null, null, null);

        return cursor;
    }

    public Cursor giveLevel(int id)
    {
        String[] columns = {"id", "photos_or_videos"};
        Cursor cursor = db.query("levels", columns,null, null, null, null, null);


        cursor =  db.rawQuery("select * from levels where id='" + id + "'" , null);


        return cursor;
    }


    public String giveNameOfEmotionFromPhoto(String nameOfPhoto)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,null, null, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(3);
            if(name.equals(nameOfPhoto))
                return cursor.getString(2);
        }
        return "Fail";
    }

    // 1) ZlecenieIT - d
    public String getCurrentLang(){
        String[] columns = {"id", "language", "selected"};
        String where = "selected == 1";
        Cursor cursor = db.query("language", columns, where, null, null, null, null, null);
        String result = null;
        if(cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(1);
        }
        return result;
    }

    // 1) ZlecenieIT - e
    public void updateCurrentLang(String lang){
        ContentValues values = new ContentValues();
        values.put("selected", 1);
        String whereTrue = "language=?";
        db.update("language", values, whereTrue, new String[] {lang});
        values.put("selected", 0);
        String whereFalse = "language!=?";
        db.update("language", values, whereFalse, new String[] {lang});
    }
}
