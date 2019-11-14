package pg.autyzm.graprzyjazneemocje;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.SweepGradient;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pg.autyzm.przyjazneemocje.lib.Level;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;


public class MainActivity extends Activity implements View.OnClickListener {

    int sublevelsLeft;
    List<Integer> sublevelsList;

    List<String> photosWithEmotionSelected;
    List<String> photosWithRestOfEmotions;
    List<String> photosToUseInSublevel;
    String goodAnswer;
    Cursor cur0;
    SqlliteManager sqlm;
    String myLocale;
    int wrongAnswers;
    int rightAnswers;
    int wrongAnswersSublevel;
    int rightAnswersSublevel;
    int timeout;
    int timeoutSubLevel;
    String commandText;
    //to na 99 proent zle String language;
    boolean animationEnds = true;
    Level l;
    CountDownTimer timer;
    public Speaker speaker;
    String language;
    //ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// https://stackoverflow.com/questions/2900023/change-app-language-programmatically-in-android
        String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
       // this.setContentView(R.layout.main);   //czy activitymain? nie wiadomo



        // jak to ugryzc AAAAAAAAAAAAAAAAA setLocale(language);

        //actionBar.setTitle(getResources().getString(R.string.app_name));

//stLocale(myLocale);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        sqlm = getInstance(this);


        sqlm.getReadableDatabase();

        // Birgiel

        cur0 = sqlm.giveAllLevels();

        findNextActiveLevel();

        generateView(photosToUseInSublevel);

        //JG

        speaker = Speaker.getInstance(MainActivity.this);

        final ImageButton speakerButton = (ImageButton) findViewById(R.id.matchEmotionsSpeakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                speaker.speak(commandText);
            }
        });



    }
/**
    private void setLocale(Locale currentLocale) {
        currentLocale.equals(myLocale);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = currentLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.putExtra(myLocale, currentLocale);
        startActivity(refresh);



    }
**/
/*
private void setLocale(String lang) {
    Locale locale = new Locale(lang);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
    SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
    editor.putString("My_Lang",lang);
    editor.apply();
}


public void loadLocale() {

    SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
    String language = prefs.getString("My_Lang", "");
    setLocale(language);
}
*/

    boolean findNextActiveLevel(){

            if(sublevelsLeft != 0){
                generateSublevel(sublevelsList.get(sublevelsLeft - 1));
                return true;
            }

            // zaraz zostanie zaladowany nowy poziom (skonczyly sie podpoziomy. trzeba ustalic, czy dziecko odpowiedzialo wystarczajaco dobrze, by przejsc dalej

            while(cur0.moveToNext()){

                if(! loadLevel()){
                    continue;
                }
                else{
                    return true;
                }
            }

            return false;

        }

        boolean loadLevel(){


            wrongAnswersSublevel = 0;
            rightAnswersSublevel = 0;
            timeoutSubLevel = 0;




            int levelId = 0;
            int photosPerLvL = 0;
            l = null;

            System.out.println(cur0.getCount());




            levelId = cur0.getInt(cur0.getColumnIndex("id"));

            Cursor cur2 = sqlm.giveLevel(levelId);
            Cursor cur3 = sqlm.givePhotosInLevel(levelId);
            Cursor cur4 = sqlm.giveEmotionsInLevel(levelId);

            l = new Level(cur2, cur3, cur4);

            photosPerLvL = l.getPvPerLevel();


            if(!l.isLevelActive()) return false;


            // tworzymy tablice do permutowania

            sublevelsLeft = l.getEmotions().size() * l.getSublevels();

            sublevelsList = new ArrayList<Integer>();

            for(int i = 0; i < l.getEmotions().size(); i++){

                for(int j = 0; j < l.getSublevels(); j++){

                    sublevelsList.add(l.getEmotions().get(i));

                }

            }

            java.util.Collections.shuffle(sublevelsList);



             generateSublevel(sublevelsList.get(sublevelsLeft - 1));




            // wylosuj emocje z wybranych emocji, odczytaj jej imie (bo mamy liste id)
            //int emotionIndexInList = selectEmotionToChoose(l);



            return true;

    }

    void generateSublevel(int emotionIndexInList){


        Cursor emotionCur = sqlm.giveEmotionName(emotionIndexInList);

        emotionCur.moveToFirst();
        String selectedEmotionName = emotionCur.getString(emotionCur.getColumnIndex("emotion"));
        // po kolei czytaj nazwy emocji wybranych zdjec, jesli ich emocja = wybranej emocji, idzie do listy a, jesli nie, lista b

        photosWithEmotionSelected = new ArrayList<String>();
        photosWithRestOfEmotions = new ArrayList<String>();
        photosToUseInSublevel = new ArrayList<String>();



        for(int e : l.getPhotosOrVideosList()){

            //System.out.println("Id zdjecia: " + e);
            Cursor curEmotion = sqlm.givePhotoWithId(e);



            curEmotion.moveToFirst();
            String photoEmotionName = curEmotion.getString(curEmotion.getColumnIndex("emotion"));
            String photoName = curEmotion.getString(curEmotion.getColumnIndex("name"));



            if(photoEmotionName.equals(selectedEmotionName)){
                photosWithEmotionSelected.add(photoName);
            }
            else{
                photosWithRestOfEmotions.add(photoName);

            }

        }

        // z listy a wybieramy jedno zdjecie, ktore bedzie prawidlowa odpowiedzia

        goodAnswer = selectPhotoWithSelectedEmotion();

        // z listy b wybieramy zdjecia nieprawidlowe

        selectPhotoWithNotSelectedEmotions(l.getPvPerLevel());

        // laczymy dobra odpowiedz z reszta wybranych zdjec i przekazujemy to dalej

        photosToUseInSublevel.add(goodAnswer);

        java.util.Collections.shuffle(photosToUseInSublevel);


        // z tego co rozumiem w photosList powinny byc name wszystkich zdjec, jakie maja sie pojawic w lvl (czyli - 3 pozycje)



        StartTimer(l);
           /* final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinearLayout imagesLinear = (LinearLayout)findViewById(R.id.imageGallery);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation((float)0.1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    final int childcount = imagesLinear.getChildCount();
                    for (int i = 0; i < childcount; i++)
                    {
                        ImageView image = (ImageView) imagesLinear.getChildAt(i);
                        if(image.getId() != 1)
                        {
                            image.setColorFilter(filter);
                        }

                    }
                    timeout ++;
                }
            }, l.timeLimit * 1000);*/





        // /birgiel

    }

   @SuppressLint("ResourceType")
    void generateView(List<String> photosList){


        TextView txt = (TextView) findViewById(R.id.rightEmotion);
       // txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        String rightEm = goodAnswer.replace(".jpg","").replaceAll("[0-9.]", "");
        String rightEmotionLang = getResources().getString(getResources().getIdentifier("emotion_" + rightEm, "string", getPackageName()));
        commandText = getResources().getString(R.string.label_show_emotion) + " " + rightEmotionLang;
        txt.setText(commandText);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);

        linearLayout1.removeAllViews();
        int listSize=photosList.size();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 790/listSize, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50-(150/listSize), getResources().getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(height, height);
        lp.setMargins(45/listSize, 10, 45/listSize, margin);
        lp.gravity = Gravity.CENTER;
        for(String photoName:photosList)
        {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
            File fileOut = new File(root + "Emotions" + File.separator + photoName);
            try {

                ImageView image = new ImageView(MainActivity.this);
                image.setLayoutParams(lp);

                if(photoName.equals(goodAnswer)){
                    image.setId(1);
                }
                else{
                    image.setId(0);
                }


                image.setOnClickListener(this);
                Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(fileOut));
                image.setImageBitmap(captureBmp);
                linearLayout1.addView(image);
            }
            catch(IOException e) {
                System.out.println("IO Exception " + photoName);
            }
        }



    }


   @SuppressLint("ResourceType")
    public void onClick(View v) {


        if(v.getId() == 1) {
            animationEnds = false;
            sublevelsLeft--;
            rightAnswers++;
            rightAnswersSublevel++;
            timer.cancel();

            boolean correctness = true;

            if(sublevelsLeft == 0) {
                correctness = checkCorrectness();
            }




            if(correctness) {
                Intent i = new Intent(this, AnimationActivity.class);
                startActivityForResult(i, 1);

            }
            else{
                startEndActivity(false);

//                Intent i = new Intent(this, LevelFailedActivity.class);
//                startActivityForResult(i, 2);

            }
            //startActivity(i);







        }
        else //jesli nie wybrano wlasciwej
        {
            wrongAnswers++;
            wrongAnswersSublevel++;
        }




    }

    boolean checkCorrectness(){



        if(wrongAnswersSublevel > l.getCorrectness()){

            return false;

        }


        return true;

    }



/*
    int selectEmotionToChoose(Level l){

        Random rand = new Random();

        int emotionIndexInList = rand.nextInt(l.emotions.size());

        return emotionIndexInList;
    }
*/
    String selectPhotoWithSelectedEmotion(){

        Random rand = new Random();

        int photoWithSelectedEmotionIndex = rand.nextInt(photosWithEmotionSelected.size());

        String name = photosWithEmotionSelected.get(photoWithSelectedEmotionIndex);

        return name;
    }

    void selectPhotoWithNotSelectedEmotions(int howMany){

        Random rand = new Random();

        for(int i = 0; i < howMany - 1; i++) {

            if(photosWithRestOfEmotions.size() > 0){
                int photoWithSelectedEmotionIndex = rand.nextInt(photosWithRestOfEmotions.size());
                String name = photosWithRestOfEmotions.get(photoWithSelectedEmotionIndex);

                photosToUseInSublevel.add(name);
                photosWithRestOfEmotions.remove(name);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:
                animationEnds = true;

                if(! findNextActiveLevel()){

                    startEndActivity(true);
                }

                generateView(photosToUseInSublevel);
                System.out.println("Wygenerowano view");

                break;
            case 2:


                java.util.Collections.shuffle(sublevelsList);
                sublevelsLeft = l.getEmotions().size() * l.getSublevels();

                wrongAnswersSublevel = 0;
                rightAnswersSublevel = 0;
                timeoutSubLevel = 0;

                generateSublevel(sublevelsList.get(sublevelsLeft - 1));

                break;
        }
    }
    private void startEndActivity(boolean pass){
        Intent in = new Intent(this, EndActivity.class);
        in.putExtra("PASS", pass);
        in.putExtra("WRONG", wrongAnswers);
        in.putExtra("RIGHT", rightAnswers);
        in.putExtra("TIMEOUT", timeout);


        startActivityForResult(in, 2);

        //startActivity(in);
    }

    private void StartTimer(Level l)
    {
        //timer! seconds * 1000
        if(l.getTimeLimit() != 0)
        {
            timer = new CountDownTimer(l.getTimeLimit() * 1000, 1000) {

                public void onTick(long millisUntilFinished) {


                }
               @SuppressLint("ResourceType")
                public void onFinish() {
                    LinearLayout imagesLinear = (LinearLayout)findViewById(R.id.imageGallery);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation((float)0.1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    final int childcount = imagesLinear.getChildCount();
                    for (int i = 0; i < childcount; i++)
                    {
                        ImageView image = (ImageView) imagesLinear.getChildAt(i);
                        if(image.getId() != 1)
                        {
                            image.setColorFilter(filter);
                        }
                        else
                        {
                            image.setPadding(40,40,40,40);
                            image.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            /*
                            image.buildDrawingCache();
                            Bitmap bmap = image.getDrawingCache();
                            Bitmap paddedBitmap = Bitmap.createBitmap(
                                    bmap.getWidth() + 40,
                                    bmap.getHeight() + 36,
                                    Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(paddedBitmap);
                            canvas.drawARGB(0xFF, 0xFF, 0xFF, 0xFF); // this represents white color
                            canvas.drawBitmap(bmap,20,20,new Paint(Paint.FILTER_BITMAP_FLAG));
                            image.setImageBitmap(paddedBitmap);
                            */
                        }

                    }
                    timeout ++;
                    timeoutSubLevel++;
                }
            }.start();

        }
    }
}
