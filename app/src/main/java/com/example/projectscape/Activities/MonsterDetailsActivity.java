package com.example.projectscape.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectscape.Objects.GameItem;
import com.example.projectscape.Objects.Monster;
import com.example.projectscape.Objects.MonsterVarient;
import com.example.projectscape.Utility.APIHandler;
import com.example.projectscape.Utility.PersistentData;
import com.example.projectscape.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MonsterDetailsActivity extends AppCompatActivity {

    private Spinner levelSelect;
    private String wiki;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_details);
        Intent intent = getIntent();
        int monsterIndex = intent.getIntExtra("index", -1);
        Monster currentMonster = PersistentData.getMonsterByIndex(monsterIndex);
        Toolbar tb = findViewById(R.id.toolBar3);
        tb.setTitle("Monster Details");
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView name = findViewById(R.id.mdName);
        name.setText(currentMonster.getName());

        levelSelect = findViewById(R.id.mdCLevel);
        Collections.sort(currentMonster.getVarients(), new Comparator<MonsterVarient>() {
            @Override
            public int compare(MonsterVarient v1, MonsterVarient v2) {
                return v1.getCombatLevel() - v2.getCombatLevel();
            }
        });
        ArrayAdapter<MonsterVarient> a = new ArrayAdapter<>(this, R.layout.spinner, currentMonster.getVarients());
        a.setDropDownViewResource(R.layout.spinner_dropdown);
        levelSelect.setAdapter(a);
        levelSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedVarientInformation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getSelectedVarientInformation();
    }

    public void populateInfo(MonsterVarient m) {
        Collections.sort(m.getDrops(), new Comparator<GameItem>() {
            @Override
            public int compare(GameItem v1, GameItem v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        TextView labels = findViewById(R.id.mdLabels);
        TextView views = findViewById(R.id.mdViews);
        TextView labels2 = findViewById(R.id.mdLabels2);
        TextView views2 = findViewById(R.id.mdViews2);
        TextView views3 = findViewById(R.id.mdViews3);
        TextView labels3 = findViewById(R.id.mdLabels3);
        ListView lv = findViewById(R.id.dropsList);
        StringBuilder v3 = new StringBuilder();
        if (m.isSlayerMonster()) {
            ListView cats = findViewById(R.id.categoriesList);
            ListView masters = findViewById(R.id.mastersList);
            ArrayAdapter<String> a = new ArrayAdapter<>(this, R.layout.list_slayer, m.getCategories());
            ArrayAdapter<String> b = new ArrayAdapter<>(this, R.layout.list_slayer, m.getSlayerMasters());
            cats.setAdapter(a);
            masters.setAdapter(b);

            v3.append(m.getSlayerLevel()).append("\n");
            v3.append(m.getSlayerXp()).append("\n");
        } else {
            v3.append("N/A\n");
            v3.append("N/A\n");
        }
        CustomAdapter c = new CustomAdapter(this, m.getDrops());
        lv.setAdapter(c);

        StringBuilder l = new StringBuilder();
        l.append("Hit Points:\n");
        l.append("Max Hit:\n");
        l.append("Attack Speed:\n");
        l.append("\n");
        l.append("Aggressive:\n");
        l.append("Poisonous:\n");
        l.append("Poison Immune:\n");
        l.append("Venom Immune:");

        StringBuilder l2 = new StringBuilder();
        l2.append("Attack:\n");
        l2.append("Strength:\n");
        l2.append("Defence:\n");
        l2.append("Magic:\n");
        l2.append("Ranged:\n");
        l2.append("\n");
        l2.append("Melee Attack:\n");
        l2.append("Melee Strength:\n");
        l2.append("Magic Attack:\n");
        l2.append("Magic Strength:\n");
        l2.append("Ranged Attack:\n");
        l2.append("Ranged Strength:\n");
        l2.append("\n");
        l2.append("Stab Defence:\n");
        l2.append("Slash Defence:\n");
        l2.append("Crush Defence:\n");
        l2.append("Magic Defence:\n");
        l2.append("Ranged Defence:\n");

        StringBuilder v = new StringBuilder();
        v.append(m.getHp()).append("\n");
        v.append(m.getMaxHit()).append("\n");
        v.append(m.getAttackSpeed()).append("\n");
        v.append("\n");
        if (m.isAggressive()) {
            v.append("Yes").append("\n");
        } else {
            v.append("No").append("\n");
        }
        if (m.isPoisonous()) {
            v.append("Yes").append("\n");
        } else {
            v.append("No").append("\n");
        }
        if (m.isImmunePoison()) {
            v.append("Yes").append("\n");
        } else {
            v.append("No").append("\n");
        }
        if (m.isImmuneVenom()) {
            v.append("Yes").append("\n");
        } else {
            v.append("No").append("\n");
        }

        StringBuilder v2 = new StringBuilder();
        v2.append(m.getAttackLvl()).append("\n");
        v2.append(m.getStrengthLvl()).append("\n");
        v2.append(m.getDefenceLvl()).append("\n");
        v2.append(m.getMagicLvl()).append("\n");
        v2.append(m.getRangedLvl()).append("\n");
        v2.append("\n");
        v2.append(m.getAccuracy()).append("\n");
        v2.append(m.getmStrength()).append("\n");
        v2.append(m.getMagicA()).append("\n");
        v2.append(m.getmDamage()).append("\n");
        v2.append(m.getRangedA()).append("\n");
        v2.append(m.getrStrength()).append("\n");
        v2.append("\n");
        v2.append(m.getStabD()).append("\n");
        v2.append(m.getSlashD()).append("\n");
        v2.append(m.getCrushD()).append("\n");
        v2.append(m.getMagicD()).append("\n");
        v2.append(m.getRangedD()).append("\n");

        StringBuilder l3 = new StringBuilder();
        l3.append("Slayer Level:\n");
        l3.append("Slayer XP:\n");

        labels.setText(l);
        views.setText(v);
        labels2.setText(l2);
        views2.setText(v2);
        labels3.setText(l3);
        views3.setText(v3);

        ImageButton wikiNav = findViewById(R.id.wikiButton2);
        wikiNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = wiki;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        if (wiki.equals("none")) {
            wikiNav.setEnabled(false);
            wikiNav.setBackgroundResource(R.drawable.ic_refresh_disabled);
        }
    }

    class CustomAdapter extends ArrayAdapter<GameItem> implements Filterable {

        Context context;
        ArrayList<GameItem> items;

        CustomAdapter(Context c, ArrayList<GameItem> drops) {
            super(c, R.layout.list_monster_drop, R.id.mdListName, drops);
            context = c;
            items = drops;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_monster_drop, parent, false);
            ImageView icon = row.findViewById(R.id.mdListThumbnail);
            TextView name = row.findViewById(R.id.mdListName);
            Picasso.get().load("https://www.osrsbox.com/osrsbox-db/items-icons/" + items.get(position).getId() + ".png").fit().into(icon);
            name.setText(items.get(position).getName());
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
    }

    private void getSelectedVarientInformation() {
        MonsterVarient m = (MonsterVarient) levelSelect.getSelectedItem();
        wiki = m.getWiki();
        APIHandler.getMonsterDetails(this, m);
    }



}
