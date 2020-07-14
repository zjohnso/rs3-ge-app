package com.example.projectscape.Objects;

public class Player {

    private int[] skills;
    private String name;
    private int combatLevel;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int[] getSkills() {
        return skills;
    }

    public void setSkills(int[] skills) {
        this.skills = skills;
        calculateCombatLevel();
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    private void calculateCombatLevel() {
        int attack = skills[1];
        int strength = skills[3];
        int defence = skills[2];
        int ranged = skills[5];
        int prayer = skills[6];
        int magic = skills[7];
        int hitpoints = skills[4];

        double base = .25*(defence + hitpoints + Math.floor(prayer/2));
        double melee = .325*(attack + strength);
        double range = .325*(Math.floor(3*ranged/2));
        double mage = .325*(Math.floor(3*magic/2));
        combatLevel = (int) Math.floor(base + greatest(melee, range, mage));
    }

    private double greatest(double a, double b, double c) {
        if( a >= b && a >= c) {
            return a;
        } else if (b >= a && b >= c) {
            return b;
        } else {
            return c;
        }
    }
}
