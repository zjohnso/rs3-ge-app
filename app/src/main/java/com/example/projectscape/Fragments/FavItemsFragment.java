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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectscape.Objects.GameItem;
import com.example.projectscape.Activities.ItemDetailsActivity;
import com.example.projectscape.Utility.PersistentData;
import com.example.projectscape.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class FavItemsFragment extends Fragment {

    private CustomAdapter customAdapter;
    private ListView lv;
    private EditText et;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fav_items, container, false);
        lv = v.findViewById(R.id.favItems);
        et = v.findViewById(R.id.favEditText);
        ImageButton clearText = v.findViewById(R.id.favClearButton);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        populateList();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }

    private void populateList() {
        customAdapter = new CustomAdapter(getActivity(), PersistentData.getFavoriteGoods());
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
                int index = ((GameItem) lv.getItemAtPosition(position)).getIndex();
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends ArrayAdapter<GameItem> implements Filterable {

        Context context;
        ArrayList<GameItem> items;
        ArrayList<GameItem> itemsTemp;
        CustomFilter cs;

        CustomAdapter(Context c, ArrayList<GameItem> gameItems) {
            super(c, R.layout.list_favorite_item, R.id.name2, gameItems);
            context = c;
            items = gameItems;
            itemsTemp = gameItems;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_favorite_item, parent, false);
            ImageView icon = row.findViewById(R.id.thumbnail2);
            TextView name = row.findViewById(R.id.name2);
            GameItem currentItem = items.get(position);
            Picasso.get().load("https://www.osrsbox.com/osrsbox-db/items-icons/" + currentItem.getId() + ".png").fit().into(icon);
            name.setText(currentItem.getName());

            final TextView labels = row.findViewById(R.id.lvDataLabels);
            StringBuilder l = new StringBuilder();
            l.append("Current Buy Volume:\n");
            l.append("Current Sell Volume:\n");
            l.append("Current Average Buy Price:\n");
            l.append("Current Average Sell Price:\n");

            final TextView views = row.findViewById(R.id.lvDataViews);
            StringBuilder v = new StringBuilder();
            v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getBuy())).append("\n");
            v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getSell())).append("\n");
            v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getBuyPrice())).append("\n");
            v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getSellPrice())).append("\n");

            labels.setText(l);
            views.setText(v);

            final ImageButton button = row.findViewById(R.id.expandButton);
            button.setFocusable(false);
            button.setFocusableInTouchMode(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (labels.getVisibility() == View.GONE) {
                        button.setBackgroundResource(R.drawable.ic_collapse);
                        labels.setVisibility(View.VISIBLE);
                        views.setVisibility(View.VISIBLE);
                    } else {
                        button.setBackgroundResource(R.drawable.ic_expand);
                        labels.setVisibility(View.GONE);
                        views.setVisibility(View.GONE);
                    }
                }
            });

            return row;
        }

        @Nullable
        @Override
        public GameItem getItem(int position) {
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
                    ArrayList<GameItem> filtered = new ArrayList<>();
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
                items = (ArrayList<GameItem>) results.values;
                notifyDataSetChanged();
            }
        }
    }


}
