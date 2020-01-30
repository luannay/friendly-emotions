package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivityBackground extends Activity {
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.YELLOW);
    }
}
