package com.example.projectscape.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectscape.Utility.PersistentData;
import com.example.projectscape.R;

public class ProfileFragment extends Fragment {

    private TextView labels1;
    private TextView view1;
    private TextView labels2;
    private TextView view2;
    private TextView labels3;
    private TextView view3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = v.findViewById(R.id.pName);
        name.setText(PersistentData.getPlayer().getName());
        TextView cLevel = v.findViewById(R.id.pCombatLevel);
        String string = PersistentData.getPlayer().getCombatLevel() + "";
        cLevel.setText(string);
        TextView oLevel = v.findViewById(R.id.pOverallSkillLevel);
        String string2 = PersistentData.getPlayer().getSkills()[0] + "";
        oLevel.setText(string2);

        labels1 = v.findViewById(R.id.skillLabel1);
        view1 = v.findViewById(R.id.skillView1);
        labels2 = v.findViewById(R.id.skillLabel2);
        view2 = v.findViewById(R.id.skillView2);
        labels3 = v.findViewById(R.id.skillLabel3);
        view3 = v.findViewById(R.id.skillView3);

        getSkills();

        return v;
    }

    private void getSkills() {
        int[] s = PersistentData.getPlayer().getSkills();

        StringBuilder l1 = new StringBuilder();
        l1.append("Attack:\n");
        l1.append("Strength:\n");
        l1.append("Defence:\n");
        l1.append("Ranged:\n");
        l1.append("Prayer:\n");
        l1.append("Magic:\n");
        l1.append("Runecraft:\n");
        l1.append("Construction:\n");

        StringBuilder v1 = new StringBuilder();
        v1.append(s[1]).append("\n");
        v1.append(s[3]).append("\n");
        v1.append(s[2]).append("\n");
        v1.append(s[5]).append("\n");
        v1.append(s[6]).append("\n");
        v1.append(s[7]).append("\n");
        v1.append(s[21]).append("\n");
        v1.append(s[23]).append("\n");

        StringBuilder l2 = new StringBuilder();
        l2.append("Hitpoints:\n");
        l2.append("Agility:\n");
        l2.append("Herblore:\n");
        l2.append("Thieving:\n");
        l2.append("Crafting:\n");
        l2.append("Fletching:\n");
        l2.append("Slayer:\n");
        l2.append("Hunter:\n");

        StringBuilder v2 = new StringBuilder();
        v2.append(s[4]).append("\n");
        v2.append(s[17]).append("\n");
        v2.append(s[16]).append("\n");
        v2.append(s[18]).append("\n");
        v2.append(s[13]).append("\n");
        v2.append(s[10]).append("\n");
        v2.append(s[19]).append("\n");
        v2.append(s[22]).append("\n");

        StringBuilder l3 = new StringBuilder();
        l3.append("Mining:\n");
        l3.append("Smithing:\n");
        l3.append("Fishing:\n");
        l3.append("Cooking:\n");
        l3.append("Firemaking:\n");
        l3.append("Woodcutting:\n");
        l3.append("Farming:\n");

        StringBuilder v3 = new StringBuilder();
        v3.append(s[15]).append("\n");
        v3.append(s[14]).append("\n");
        v3.append(s[11]).append("\n");
        v3.append(s[8]).append("\n");
        v3.append(s[12]).append("\n");
        v3.append(s[9]).append("\n");
        v3.append(s[20]).append("\n");

        labels1.setText(l1);
        view1.setText(v1);
        labels2.setText(l2);
        view2.setText(v2);
        labels3.setText(l3);
        view3.setText(v3);
    }

}
