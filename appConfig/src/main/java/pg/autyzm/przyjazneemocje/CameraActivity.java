package pg.autyzm.przyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.ArrayMap;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import pg.autyzm.przyjazneemocje.lib.SqliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqliteManager.getInstance;

/**
 * Created by Ann on 13.11.2016.
 */
public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String emotion = extras.getString("SpinnerValue_Emotion");
        fileName = getFileName(emotion);
        takePhoto();

    }

    private String fileName;
    private static final int TAKE_PHOTO_CODE = 1;

    public void takePhoto() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    private File getTempFile() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        final File path = new File(root + "FriendlyEmotions/Photos" + File.separator);
        if (!path.exists()) {
            path.mkdir();
        }

        return new File(path, fileName + "tmp.jpg");
    }

    private String getFileName(String emotionLang)
    {
        SqliteManager sqlm = getInstance(this);
        Map<String, String> mapEmo = new ArrayMap<>();
        mapEmo.put(getResources().getString(R.string.emotion_happyman), "happyman");
        mapEmo.put(getResources().getString(R.string.emotion_sadman), "sadman");
        mapEmo.put(getResources().getString(R.string.emotion_angryman), "angryman");
        mapEmo.put(getResources().getString(R.string.emotion_scaredman), "scaredman");
        mapEmo.put(getResources().getString(R.string.emotion_surprisedman), "surprisedman");
        mapEmo.put(getResources().getString(R.string.emotion_boredman), "boredman");
        mapEmo.put(getResources().getString(R.string.emotion_happywoman), "happywoman");
        mapEmo.put(getResources().getString(R.string.emotion_sadwoman), "sadwoman");
        mapEmo.put(getResources().getString(R.string.emotion_angrywoman), "angrywoman");
        mapEmo.put(getResources().getString(R.string.emotion_scaredwoman), "scaredwoman");
        mapEmo.put(getResources().getString(R.string.emotion_surprisedwoman), "surprisedwoman");
        mapEmo.put(getResources().getString(R.string.emotion_boredwoman), "boredwoman");
        mapEmo.put(getResources().getString(R.string.emotion_happychild), "happychild");
        mapEmo.put(getResources().getString(R.string.emotion_sadchild), "sadchild");
        mapEmo.put(getResources().getString(R.string.emotion_angrychild), "angrychild");
        mapEmo.put(getResources().getString(R.string.emotion_scaredchild), "scaredchild");
        mapEmo.put(getResources().getString(R.string.emotion_surprisedchild), "surprisedchild");
        mapEmo.put(getResources().getString(R.string.emotion_boredchild), "boredchild");
        String emotion = mapEmo.get(emotionLang);
        Cursor cur = sqlm.givePhotosWithEmotion(emotion);

        int maxNumber = 1;
        while(cur.moveToNext())
        {
            String name = cur.getString(3);
            name = name.replace(".jpg","").replaceAll("[^0-9]","");
            if(maxNumber < Integer.parseInt(name))
                maxNumber = Integer.parseInt(name);

        }
        return emotion + ++maxNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                    final File path = new File(root + "FriendlyEmotions/Photos" + File.separator);
                    File largeFile = new File(path, fileName + "tmp.jpg");
                    Bitmap largeBitmap = BitmapFactory.decodeFile(largeFile.getAbsolutePath());

                    /*final int maxSize = 400;
                    int outWidth;
                    int outHeight;
                    int inWidth = largeBitmap.getWidth();
                    int inHeight = largeBitmap.getHeight();
                    if(inWidth > inHeight){
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }*/

                    Bitmap smallBitmap = Bitmap.createScaledBitmap(largeBitmap,largeBitmap.getWidth() * 1/4,largeBitmap.getHeight()*1/4,false);

                    File smallFile = new File(path, fileName + ".jpg");
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(smallFile);
                        smallBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        largeBitmap.recycle();
                        smallBitmap.recycle();
                        largeFile.delete();
                    } catch (Exception e) {}

                    finish();
                    break;
            }
        }
    }
}
