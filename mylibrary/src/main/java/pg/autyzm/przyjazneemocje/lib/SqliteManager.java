package pg.autyzm.przyjazneemocje.lib;

/**
 * Created by Ann on 26.09.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import pg.autyzm.przyjazneemocje.lib.entities.Level;

public class SqliteManager extends SQLiteOpenHelper {

    private static SqliteManager appContext;
    private static SqliteManager sInstance;

    private static final String DATABASE_NAME = "przyjazneemocje";


    private SQLiteDatabase db;

    private SqliteManager(final Context context) {
        super(new DatabaseContext(context), DATABASE_NAME, null, 18);
        db = getWritableDatabase();
    }

    public static SqliteManager getAppContext() {
        return appContext;
    }

    public static synchronized SqliteManager getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SqliteManager(context.getApplicationContext());
        }
        return sInstance;

    }

    public void onCreate(SQLiteDatabase db) {

        this.db = db;


        createTablesInDatabase();
        addEmotionsToDatabase();

        addLang(1, "pl", 1);
        addLang(2, "en", 0);

        Level level_easy_icons = new Level();
        level_easy_icons.setPhotosOrVideosIdList(new ArrayList<Integer>());
        level_easy_icons.setName("Wesoły/smutny IKONY");
        level_easy_icons.setLevelActive(true);
        level_easy_icons.setTimeLimit(10);
        level_easy_icons.setPraises("dobrze");
        level_easy_icons.setAmountOfAllowedTriesForEachEmotion(3);
        level_easy_icons.setSublevelsPerEachEmotion(2);
        level_easy_icons.setPhotosOrVideosShowedForOneQuestion(3);
        level_easy_icons.setForTests(true);
        level_easy_icons.setShouldQuestionBeReadAloud(false);
        level_easy_icons.setQuestionType(Level.Question.EMOTION_NAME);


        level_easy_icons.setEmotions(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
            }
        });
        level_easy_icons.setPhotosOrVideosIdList(new ArrayList<Integer>() {
            {


                add(9);
                add(10);
                add(13);
                add(14);
                add(16);
                add(17);
                add(18);
                add(19);
                add(20);
                add(21);
                add(22);
                add(23);
                add(24);
                add(25);


            }
        });

        saveLevelToDatabase(level_easy_icons);


        Level level_easy_photos = new Level();
        level_easy_photos.setPhotosOrVideosIdList(new ArrayList<Integer>());
        level_easy_photos.setName("Wesoły/smutny ZDJĘCIA");
        level_easy_photos.setLevelActive(false);
        level_easy_photos.setTimeLimit(10);
        level_easy_photos.setPraises("dobrze");
        level_easy_photos.setAmountOfAllowedTriesForEachEmotion(3);
        level_easy_photos.setSublevelsPerEachEmotion(2);
        level_easy_photos.setPhotosOrVideosShowedForOneQuestion(3);
        level_easy_photos.setForTests(true);
        level_easy_photos.setShouldQuestionBeReadAloud(true);
        level_easy_photos.setQuestionType(Level.Question.EMOTION_NAME);

        level_easy_photos.setEmotions(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
            }
        });
        level_easy_photos.setPhotosOrVideosIdList(new ArrayList<Integer>() {
            {
                add(7);
                add(6);
                add(8);
                add(9);
                add(10);
            }
        });

        saveLevelToDatabase(level_easy_photos);

        Level level_medium = new Level();
        level_medium.setPhotosOrVideosIdList(new ArrayList<Integer>());
        level_medium.setName("Wesoły/smutny/przestraszony/zły");
        level_medium.setLevelActive(false);
        level_medium.setTimeLimit(10);
        level_medium.setPraises("dobrze");
        level_medium.setPraises("wspaniale");
        level_medium.setPraises("świetnie");
        level_medium.setAmountOfAllowedTriesForEachEmotion(3);
        level_medium.setPhotosOrVideosShowedForOneQuestion(3);
        level_medium.setSublevelsPerEachEmotion(2);
        level_medium.setForTests(true);
        level_medium.setShouldQuestionBeReadAloud(true);
        level_medium.setQuestionType(Level.Question.SHOW_EMOTION_NAME);
        level_medium.setAmountOfEmotions(Integer.toString(level_medium.getEmotions().size()));

        level_medium.setEmotions(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
                add(2);
                add(3);
            }
        });
        level_medium.setPhotosOrVideosIdList(new ArrayList<Integer>() {
            {
                add(7);
                add(6);
                add(8);
                add(9);
                add(10);
                add(1);
                add(2);
                add(3);
                add(13);
            }
        });

        saveLevelToDatabase(level_medium);


        Level level_difficult = new Level();
        level_difficult.setPhotosOrVideosIdList(new ArrayList<Integer>());
        level_difficult.setName("Wesoły/smutny/przestraszony/zły/znudzony/zdziwiony");
        level_difficult.setLevelActive(false);
        level_difficult.setTimeLimit(10);
        level_difficult.setPraises("dobrze");
        level_difficult.setAmountOfAllowedTriesForEachEmotion(3);
        level_difficult.setSublevelsPerEachEmotion(2);
        level_difficult.setPhotosOrVideosShowedForOneQuestion(3);
        level_difficult.setForTests(true);
        level_difficult.setShouldQuestionBeReadAloud(true);
        level_difficult.setQuestionType(Level.Question.SHOW_WHERE_IS_EMOTION_NAME);
        //level_difficult.setAmountOfEmotions("6");
        level_difficult.setEmotions(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }
        });
        level_difficult.setPhotosOrVideosIdList(new ArrayList<Integer>() {
            {
                add(7);
                add(6);
                add(8);
                add(9);
                add(10);
                add(1);
                add(2);
                add(3);
                add(13);
                add(11);
                add(12);
                add(4);
                add(5);
            }
        });

        saveLevelToDatabase(level_difficult);

    }


    public void onOpen(SQLiteDatabase db){

        this.db = db;

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.db = db;

        deleteTablesFromDatabase();
        createTablesInDatabase();
        addEmotionsToDatabase();
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

    public void addVideo(int path, String emotion, String fileName)
    {
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        values.put("name",fileName);
        db.insertOrThrow("videos", null, values);
    }

    public void addLang(int id, String lang, Integer selected) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("language", lang);
        values.put("selected", selected);
        db.insertOrThrow("language", null, values);
    }


    public void saveLevelToDatabase(Level level)
    {
        ContentValues values = new ContentValues();
        values.put("photos_or_videos", level.getPhotosOrVideosFlag());
        values.put("name", level.getName());
        values.put("photos_or_videos_per_level", level.getPhotosOrVideosShowedForOneQuestion());
        values.put("time_limit", level.getTimeLimit());
        values.put("is_level_active", level.isLevelActive());
        values.put("correctness", level.getAmountOfAllowedTriesForEachEmotion());
        values.put("sublevels_per_each_emotion", level.getSublevelsPerEachEmotion());
        values.put("is_for_tests", level.isForTests());
        values.put("question_type", level.getQuestionType().toString());
        values.put("hint_types_as_number", level.getHintTypesAsNumber());
        values.put("praises", level.getPraises());
        values.put("shouldQuestionBeReadAloud", level.isShouldQuestionBeReadAloud());


        if(level.getId() != 0) {
            db.update("levels", values, "id=" + level.getId(), null);


            //usunac wszystkie rekordy polaczone z tym poziomem.
            delete("levels_photos", "levelid", String.valueOf(level.getId()));
            delete("levels_emotions", "levelid", String.valueOf(level.getId()));

        }
        else {
            long longId = db.insertOrThrow("levels", null, values);
            level.setId((int) longId);
        }

        // Dodaj rekordy wiele do wielu ze zdjeciami/video

        for(Integer photoOrVideo : level.getPhotosOrVideosIdList()){

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("photoid",photoOrVideo);

            db.insertOrThrow("levels_photos", null, values);
        }

        for(Integer emotion : level.getEmotions()){

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("emotionid",emotion + 1);

            db.insertOrThrow("levels_emotions", null, values);
        }


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

    public Cursor giveVideosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("videos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
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

    public Cursor giveAllVideos() //TODO: Change to "giveVideosInLevel(int levelId)
    {
        String[] columns = {"id", "emotion", "name"};
        Cursor cursor = db.query("videos", columns, null, null, null, null, null);
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
        Cursor cursor =  db.rawQuery("select * from emotions", null);
        return cursor;
    }

    public Cursor giveAllLevels()
    {
        Cursor cursor =  db.rawQuery("select * from levels", null);
        return cursor;
    }

    public Cursor giveLevel(int id)
    {
        Cursor cursor =  db.rawQuery("select * from levels where id='" + id + "'" , null);
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


    private void createTablesInDatabase(){

        System.out.println("Tworze tablee");
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, " +
                "time_limit int, is_level_active int, name text, correctness int, sublevels_per_each_emotion int, is_for_tests int, question_type text, hint_types_as_number int, praises text, shouldQuestionBeReadAloud boolean);" + "");
        db.execSQL("create table levels_photos(" + "id integer primary key autoincrement,"  + "levelid integer references levels(id)," + "photoid integer references photos(id));" + "");
        db.execSQL("create table levels_emotions(" + "id integer primary key autoincrement," + "levelid integer references levels(id),"  + "emotionid integer references emotions(id));" + "");
        db.execSQL("create table videos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table language(" + "id integer primary key autoincrement," + "language text not null unique," + "selected integer default 0);" + "");
    }

    private void deleteTablesFromDatabase(){

        db.execSQL("drop table levels_emotions");
        db.execSQL("drop table levels_photos");
        db.execSQL("drop table levels");
        db.execSQL("drop table emotions");
        db.execSQL("drop table photos");
        db.execSQL("drop table videos");
        db.execSQL("drop table language");
    }

    private void addEmotionsToDatabase(){

        addEmotion("happy");
        addEmotion("sad");
        addEmotion("surprised");
        addEmotion("angry");
        addEmotion("scared");
        addEmotion("bored");
    }

    public int getPhotoIdByName(String name){

        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"name like " + "'%" + name + "%'", null, null, null, null);
        cursor.moveToNext();


        return cursor.getInt(0);
    }

    public String getCurrentLang() {
        String[] columns = {"id", "language", "selected"};
        String where = "selected == 1";
        Cursor cursor = db.query("language", columns, where, null, null, null, null, null);
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(1);
        }
        return result;
    }


    public void updateCurrentLang(String lang) {
        ContentValues values = new ContentValues();
        values.put("selected", 1);
        String whereTrue = "language=?";
        db.update("language", values, whereTrue, new String[]{lang});
        values.put("selected", 0);
        String whereFalse = "language!=?";
        db.update("language", values, whereFalse, new String[]{lang});
    }

}
