package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dropbox.core.v2.teamlog.SmartSyncOptOutType;
import com.j256.ormlite.field.types.ShortObjectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.animation.AnimationActivity;
import pg.autyzm.przyjazneemocje.lib.entities.Level;

public class RewardActivity extends Activity {

    public int chosenColor = generateRandomColor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);



        LinearLayout rewardLayout = (LinearLayout) findViewById(R.id.activity_reward);


            rewardLayout.setBackgroundColor(chosenColor);
        System.out.println("chosen color" + chosenColor);


Intent intentReward = getIntent();
        String commandType = intentReward.getStringExtra("praise");
//intentReward.getStringExtra("praises");
//String commandPraise =
String praiseString = intentReward.getStringExtra("praises");
        String[] praiseTab = new String[10];
        praiseTab = praiseString.split(";");

        List<String> praiseList = new ArrayList<String>();

        for(String element : praiseTab) {
            if(element != null && element.length() > 0) {
                praiseList.add(element);
            }
        }

        //praiseTab = praiseList.toArray(new String[praiseList.size()]);
        System.out.println("praiseList " + praiseList);
        System.out.println("praiseTab " + praiseTab);
        System.out.println("praiseString " + praiseString);



        int max = praiseList.size();
        System.out.println("max: " + max);
        int position =  (int) Math.floor(Math.random()*max);
        System.out.println("position: " + position);
        System.out.println("praiseList size: " + praiseList.size());

        String commandPraise = praiseList.get(position);
        System.out.println("commandPraise: " + commandPraise + commandType);

String photo = intentReward.getStringExtra("photo");
String fileName = intentReward.getStringExtra("fileName");
        //DZIAŁA DLA DRAWABLE:
       int photoId = intentReward.getIntExtra("photoId",1);
       int fileId= getResources().getIdentifier("pg.autyzm.graprzyjazneemocje:drawable/" + fileName, null, null);
        System.out.println("fileName:" + fileName);
String emotion = intentReward.getStringExtra("emotion");

//String photoLocation = intentReward.getStringExtra("photoPath");

        //System.out.println("Reward photoPath" + photoLocation);
       //int photoId = getResources().getIdentifier(photoLocation, null, null);
       // System.out.println("Reward photoId: " + photoId);
        TextView praise = (TextView) findViewById(R.id.commandPraise);
//String praiseString =
        praise.setText( emotion);
        ////praise.setText( commandPraise+ " " + emotion); <- to ma być potem mówione
        ImageView correctPhoto = (ImageView) findViewById(R.id.correctAnswer);
        Speaker.getInstance(RewardActivity.this).speak(commandPraise + commandType + emotion);

//Drawable drawable = (Drawable) ("R.drawable." + photo);



        //ScaleAnimation correctAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //correctPhoto.setAnimation(correctAnimation);

        //correctPhoto.animate().setDuration(3000);
        //correctPhoto.animate().translationX(1000).setDuration(500);
        //correctPhoto.animate().translationY(200).setDuration(2000);
        //correctPhoto.animate().scaleX(2).setDuration(2000);
        //correctPhoto.animate().scaleY(2).setDuration(2000);


        //correctPhoto.setImageResource(photoId);
        correctPhoto.setImageResource(photoId);
        correctPhoto.animate().setDuration(3000);
        //correctPhoto.animate().alpha(0.5f).setDuration(2000);

        //TO NATOMIAST MA BYĆ MÓWIONE    --->    commandPraise




        Button przejdzDoAnimacji = (Button) findViewById(R.id.buttonReward);
        przejdzDoAnimacji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RewardActivity.this,MainActivity.class);
                intent.putExtra("color",chosenColor);
                System.out.println("chosen color2" + chosenColor);
                System.out.println("Reward Activity - color" + chosenColor);
                setResult(1,intent);
                finish();
            }
        });



    }

    @Override
    public void finish() {
        super.finish();


    }

    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final Random mRandom = new Random(System.currentTimeMillis());

        final int baseColor = Color.WHITE;
        Random rnd = new Random();

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;



        return Color.rgb(red, green, blue);


    }

/*    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }*/
}
