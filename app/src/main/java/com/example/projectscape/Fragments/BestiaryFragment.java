package com.example.projectscape.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectscape.Objects.Monster;
import com.example.projectscape.Activities.MonsterDetailsActivity;
import com.example.projectscape.Utility.PersistentData;
import com.example.projectscape.R;

import java.util.ArrayList;
import java.util.Objects;

public class BestiaryFragment extends Fragment {

    private CustomAdapter customAdapter;
    private ListView lv;
    private EditText et;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bestiary, container, false);
        lv = v.findViewById(R.id.monsterList);
        et = v.findViewById(R.id.bestiaryEditText);
        ImageButton clearText = v.findViewById(R.id.bestiaryClearButton);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        populateList();
        return v;
    }

    private void populateList() {
        StringBuilder itemsList = new StringBuilder();
        for (int i = 0; i < PersistentData.getMarketGoodsSize(); i++) {
            itemsList.append(PersistentData.getMarketGoodByIndex(i).toString());
        }
        customAdapter = new CustomAdapter(getActivity(), PersistentData.getMonsters());
        lv.setAdapter(customAdapter);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = ((Monster) lv.getItemAtPosition(position)).getIndex();
                Intent intent = new Intent(getActivity(), MonsterDetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });
    }


    class CustomAdapter extends ArrayAdapter<Monster> implements Filterable {

        Context context;
        ArrayList<Monster> items;
        ArrayList<Monster> itemsTemp;
        CustomFilter cs;

        CustomAdapter(Context c, ArrayList<Monster> monsters) {
            super(c, R.layout.list_monster, R.id.mName, monsters);
            context = c;
            items = monsters;
            itemsTemp = monsters;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_monster, parent, false);
            TextView name = row.findViewById(R.id.mName);
            name.setText(items.get(position).getName());
            return row;
        }

        @Nullable
        @Override
        public Monster getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (cs == null) {
                cs = new CustomFilter();
            }
            return cs;
        }

        class CustomFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                System.out.println(constraint);
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    String[] strArray = constraint.toString().split(" ");
                    StringBuilder builder = new StringBuilder();
                    for (String s : strArray) {
                        s = s.toLowerCase();
                        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                        builder.append(cap).append(" ");
                    }
                    System.out.println(builder.toString());
                    ArrayList<Monster> filtered = new ArrayList<>();
                    for (int i = 0; i < itemsTemp.size(); i++) {
                        if (itemsTemp.get(i).getName().contains(builder.toString().trim())) {
                            filtered.add(itemsTemp.get(i));
                        }
                    }
                    results.count = filtered.size();
                    results.values = filtered;
                } else {
                    results.count = itemsTemp.size();
                    results.values = itemsTemp;
                }
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<Monster>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
