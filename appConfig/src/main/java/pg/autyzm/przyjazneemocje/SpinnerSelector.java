package pg.autyzm.przyjazneemocje;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerSelector extends Activity implements
        AdapterView.OnItemSelectedListener {
    Spinner spinner_plec;
    Spinner spinner_emocje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner_plec = (Spinner) findViewById(R.id.spinner_sex);
        spinner_emocje = (Spinner) findViewById(R.id.spinner_emotions);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        String plec = String.valueOf(spinner_plec.getSelectedItem());
        if (plec.equals("@string/woman")) {
            /*List<String> list = new ArrayList<String>();*/
            /*list.add("Salary");
            list.add("Sales");
            list.add("Others");*/
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
                    .createFromResource(SpinnerSelector.this, R.array.emotions_array_woman,
                            android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //eee po co to
            dataAdapter.notifyDataSetChanged(); //oraz to
            spinner_emocje.setAdapter(dataAdapter);
        }
        if (plec.equals("@string/man")) {
            ArrayAdapter<CharSequence> dataAdapter2 = ArrayAdapter
                    .createFromResource(SpinnerSelector.this, R.array.emotions_array_man,
                            android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spinner_emocje.setAdapter(dataAdapter2);
        }

        if (plec.equals("@string/child")) {
            ArrayAdapter<CharSequence> dataAdapter3 = ArrayAdapter
                    .createFromResource(SpinnerSelector.this, R.array.emotions_array_child,
                            android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            spinner_emocje.setAdapter(dataAdapter3);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
