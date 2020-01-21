package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import pg.autyzm.przyjazneemocje.lib.SqliteManager;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static pg.autyzm.przyjazneemocje.lib.SqliteManager.getInstance;

public class MainActivity extends AppCompatActivity {
    private static MainActivity appContext;
    public SqliteManager sqlm;
    protected Locale myLocale;
    String currentLanguage = null;
    ImageView countryEn;
    ImageView countryPl;
    ArrayList<String> list;
    ArrayList<Boolean> active_list;
    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    public static MainActivity getAppContext() {
        return appContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        File createMainDir = new File(root + "FriendlyEmotions" + File.separator);

        if (!createMainDir.exists())
            createMainDir.mkdir();

        sqlm = getInstance(this);

        currentLanguage = getIntent().getStringExtra(SplashActivity.CURRENT_LANG);

        updateLevelList();
        //generate list

        sqlm.cleanTable("photos"); //TODO not clean and add, but only update
        sqlm.cleanTable("videos");

        //nie wiem czy to potrzebne
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

        File createDir = new File(root + "FriendlyEmotions/Photos" + File.separator);
        if (!createDir.exists()) {
            createDir.mkdir();

            Field[] drawables = pg.autyzm.przyjazneemocje.R.drawable.class.getFields();
            for (Field f : drawables) {
                try {
                    if (ifConstainsEmotionName(f.getName()))
                    {
                        extractFromDrawable(f, "Photos", ".jpg", Bitmap.CompressFormat.JPEG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

       /* File createDirV = new File(root + "FriendlyEmotions/Videos" + File.separator);
        if (!createDirV.exists()) {
            createDirV.mkdir();

            Field[] raw = pg.autyzm.przyjazneemocje.R.raw.class.getFields();
            for (Field f : raw) {
                try {
                    extractFromDrawable(f, "Videos", ".mp4", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

        if(new File(root + "FriendlyEmotions/Photos").list() != null) {

            for (String emotName : new File(root + "FriendlyEmotions/Photos").list()) {

                try {
                    int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());
                    if (emotName.contains("happywoman"))
                        sqlm.addPhoto(resID, "happywoman", emotName);
                    else if (emotName.contains("happyman"))
                        sqlm.addPhoto(resID, "happyman", emotName);
                    if (emotName.contains("happychild"))
                        sqlm.addPhoto(resID, "happychild", emotName);
                    else if (emotName.contains("angrywoman"))
                        sqlm.addPhoto(resID, "angrywoman", emotName);
                    else if (emotName.contains("angryman"))
                        sqlm.addPhoto(resID, "angryman", emotName);
                    else if (emotName.contains("angrychild"))
                        sqlm.addPhoto(resID, "angrychild", emotName);
                    else if (emotName.contains("surprisedman"))
                        sqlm.addPhoto(resID, "surprisedman", emotName);
                    else if (emotName.contains("surprisedwoman"))
                        sqlm.addPhoto(resID, "surprisedwoman", emotName);
                    else if (emotName.contains("surprisedchild"))
                        sqlm.addPhoto(resID, "surprisedchild", emotName);
                    else if (emotName.contains("boredman"))
                        sqlm.addPhoto(resID, "boredman", emotName);
                    else if (emotName.contains("boredwoman"))
                        sqlm.addPhoto(resID, "boredwoman", emotName);
                    else if (emotName.contains("boredchild"))
                        sqlm.addPhoto(resID, "boredchild", emotName);
                    else if (emotName.contains("scaredwoman"))
                        sqlm.addPhoto(resID, "scaredwoman", emotName);
                    else if (emotName.contains("scaredman"))
                        sqlm.addPhoto(resID, "scaredman", emotName);
                    else if (emotName.contains("scaredchild"))
                        sqlm.addPhoto(resID, "scaredchild", emotName);
                    else if (emotName.contains("sadwoman"))
                        sqlm.addPhoto(resID, "sadwoman", emotName);
                    else if (emotName.contains("sadman"))
                        sqlm.addPhoto(resID, "sadman", emotName);
                    else if (emotName.contains("sadchild"))
                        sqlm.addPhoto(resID, "sadchild", emotName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

       /* if(new File(root + "FriendlyEmotions/Videos").list() != null) {

            for (String emotName : new File(root + "FriendlyEmotions/Videos").list()) {

                try {
                    int resID = getResources().getIdentifier(emotName, "raw", getPackageName());
                    if (emotName.contains("happy"))
                        sqlm.addVideo(resID, "happy", emotName);
                    else if (emotName.contains("angry"))
                        sqlm.addVideo(resID, "angry", emotName);
                    else if (emotName.contains("surprised"))
                        sqlm.addVideo(resID, "surprised", emotName);
                    else if (emotName.contains("bored"))
                        sqlm.addVideo(resID, "bored", emotName);
                    else if (emotName.contains("scared"))
                        sqlm.addVideo(resID, "scared", emotName);
                    else if (emotName.contains("sad"))
                        sqlm.addVideo(resID, "sad", emotName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        // }
        Button smile = (Button) findViewById(R.id.uruchomSmileButton);
        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"Hello", Toast.LENGTH_LONG).show();

          /*   Intent intent = new Intent();
                intent.setClassName("pg.smile", "GameActivity");
                startActivity(intent);*/

                //startActivity(new Intent("pg.smile.MainActivity"));
                Intent intent = new Intent(MainActivity.this, pg.smile.MainActivity.class);
                startActivity(intent);
            }
        });

        countryPl = findViewById(R.id.imageView);
        countryPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("pl");
            }
        });
        countryEn = findViewById(R.id.imageView2);
        countryEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });

        ImageButton buttonCamera = findViewById(R.id.button_take_photo);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner_emocje = findViewById(R.id.spinner_emotions);
                //Spinner spinner_sex= findViewById(R.id.spinner_sex);
                Bundle bundle2 = new Bundle();
                bundle2.putString("SpinnerValue_Emotion", spinner_emocje.getSelectedItem().toString());
                //bundle2.putString("SpinnerValue_Sex", spinner_sex.getSelectedItem().toString());
                Intent in = new Intent(MainActivity.this, CameraActivity.class);
                in.putExtras(bundle2);
                startActivityForResult(in, 1);
            }
        });
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            sqlm.updateCurrentLang(localeName);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(SplashActivity.CURRENT_LANG, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MainActivity.this, R.string.selected_language, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

//onBackPressed usunelam

    // napisuje, bo chce, by po dodaniu poziomu lista poziomow w main activity automatycznie sie odswiezala - Pawel
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        updateLevelList();
        
    }

    public boolean ifConstainsEmotionName(String inputString)
    {
        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext()) {
            String emotion = cur.getString(1);
            if(inputString.contains(emotion))
                return true;
        }
        return false;
    }


    public void sendMessage(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, LevelConfigurationActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "String";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void updateLevelList(){

        Cursor cur = sqlm.giveAllLevels();
        list = new ArrayList<String>();
        active_list = new ArrayList<Boolean>();

        while(cur.moveToNext())
        {
            String name = cur.getString(cur.getColumnIndex("name"));
            String levelId = cur.getInt(0) + " " + name;
            //String levelId = "Level " + cur.getInt(0);

            int active = cur.getInt(cur.getColumnIndex("is_level_active"));
            boolean isLevelActive = (active != 0);
            active_list.add(isLevelActive);
            list.add(levelId);

        }

        //instantiate custom adapter
        CustomList adapter = new CustomList(list, active_list, this);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adapter);

    }

    private void extractFromDrawable(Field field, String dir, String fileExt, Bitmap.CompressFormat format) throws IOException {

        String emotName = field.getName();
        int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());
        String path = root + "FriendlyEmotions/" + dir + File.separator;
        File file = new File(path, emotName + fileExt);
        FileOutputStream outStream = new FileOutputStream(file);

        if(format != null)
        {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), resID);
            bm.compress(format, 100, outStream);
            outStream.flush();
            outStream.close();

        } else {
            resID = getResources().getIdentifier(emotName, "raw", getPackageName());
            InputStream in = getResources().openRawResource(resID);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    outStream.write(buff, 0, read);
                }
            } finally {
                in.close();
                outStream.close();
            }
        }
    }
}