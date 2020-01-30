package pg.autyzm.przyjazneemocje.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import pg.autyzm.przyjazneemocje.R;

public class CustomList extends BaseAdapter implements ListAdapter {
    private final List<LevelItem> list;
    private ILevelListCallback levelListCallback;
    private int activeLevelPosition = -1;

    public CustomList(List<LevelItem> list, ILevelListCallback levelListCallback) {
        this.list = list;
        this.levelListCallback = levelListCallback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_single, null);
        }

        final LevelItem levelItem = list.get(position);

        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(levelItem.getName());
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton editBtn = view.findViewById(R.id.edit_btn);
        RadioButton activeChck = view.findViewById(R.id.active_chck);
        activeChck.setOnCheckedChangeListener(null);
        activeChck.setChecked(levelItem.isActive());
        if (levelItem.isCanEdit()) {
            editBtn.setVisibility(View.VISIBLE);
        } else {
            editBtn.setVisibility(View.INVISIBLE);
        }
        if (levelItem.isCanRemove()) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelListCallback.removeLevel(levelItem);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelListCallback.editLevel(levelItem);
                notifyDataSetChanged();
            }
        });
        activeChck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notifyDataSetChanged();
                if (!levelItem.isActive()) {
                    levelListCallback.setLevelActive(levelItem, isChecked);
                }
            }
        });
        return view;
    }
}