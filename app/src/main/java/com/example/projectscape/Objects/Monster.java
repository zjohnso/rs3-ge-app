package com.example.projectscape.Objects;

import java.util.ArrayList;

public class Monster {

    private String name;
    private int index;
    private ArrayList<MonsterVarient> varients;

    public Monster(int id, String name, int combatLevel, String wiki) {
        String[] strArray = name.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        this.name = builder.toString();
        varients = new ArrayList<>();
        varients.add(new MonsterVarient(combatLevel, id, wiki));
    }

    public ArrayList<MonsterVarient> getVarients() {
        return varients;
    }

    public void addVarient(MonsterVarient v) {
        for (int i = 0; i < varients.size(); i++) {
            if (v.getCombatLevel() == varients.get(i).getCombatLevel()) {
                return;
            }
        }
        varients.add(v);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
