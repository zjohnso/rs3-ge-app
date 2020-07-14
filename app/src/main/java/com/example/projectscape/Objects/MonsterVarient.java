package com.example.projectscape.Objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MonsterVarient {

    private int combatLevel;
    private int id;
    private String wiki;

    private int hp;
    private int maxHit;
    private int attackSpeed;
    private boolean aggressive;
    private boolean poisonous;
    private boolean immunePoison;
    private boolean immuneVenom;
    private ArrayList<String> categories;
    private boolean slayerMonster;
    private int slayerLevel;
    private double slayerXp;
    private ArrayList<String> slayerMasters;
    private int attackLvl;
    private int strengthLvl;
    private int defenceLvl;
    private int magicLvl;
    private int rangedLvl;
    private int magicA;
    private int rangedA;
    private int stabD;
    private int slashD;
    private int crushD;
    private int magicD;
    private int rangedD;
    private int accuracy;
    private int mStrength;
    private int rStrength;
    private int mDamage;
    private ArrayList<GameItem> drops;

    public MonsterVarient(int combatLevel, int id, String wiki) {
        this.combatLevel = combatLevel;
        this.id = id;
        this.wiki = wiki;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public boolean isPoisonous() {
        return poisonous;
    }

    public void setPoisonous(boolean poisonous) {
        this.poisonous = poisonous;
    }

    public boolean isImmunePoison() {
        return immunePoison;
    }

    public void setImmunePoison(boolean immunePoison) {
        this.immunePoison = immunePoison;
    }

    public boolean isImmuneVenom() {
        return immuneVenom;
    }

    public void setImmuneVenom(boolean immuneVenom) {
        this.immuneVenom = immuneVenom;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public boolean isSlayerMonster() {
        return slayerMonster;
    }

    public void setSlayerMonster(boolean slayerMonster) {
        this.slayerMonster = slayerMonster;
    }

    public int getSlayerLevel() {
        return slayerLevel;
    }

    public void setSlayerLevel(int slayerLevel) {
        this.slayerLevel = slayerLevel;
    }

    public double getSlayerXp() {
        return slayerXp;
    }

    public void setSlayerXp(double slayerXp) {
        this.slayerXp = slayerXp;
    }

    public ArrayList<String> getSlayerMasters() {
        return slayerMasters;
    }

    public void setSlayerMasters(ArrayList<String> slayerMasters) {
        this.slayerMasters = slayerMasters;
    }

    public int getAttackLvl() {
        return attackLvl;
    }

    public void setAttackLvl(int attackLvl) {
        this.attackLvl = attackLvl;
    }

    public int getStrengthLvl() {
        return strengthLvl;
    }

    public void setStrengthLvl(int strengthLvl) {
        this.strengthLvl = strengthLvl;
    }

    public int getDefenceLvl() {
        return defenceLvl;
    }

    public void setDefenceLvl(int defenceLvl) {
        this.defenceLvl = defenceLvl;
    }

    public int getMagicLvl() {
        return magicLvl;
    }

    public void setMagicLvl(int magicLvl) {
        this.magicLvl = magicLvl;
    }

    public int getRangedLvl() {
        return rangedLvl;
    }

    public void setRangedLvl(int rangedLvl) {
        this.rangedLvl = rangedLvl;
    }

    public int getMagicA() {
        return magicA;
    }

    public void setMagicA(int magicA) {
        this.magicA = magicA;
    }

    public int getRangedA() {
        return rangedA;
    }

    public void setRangedA(int rangedA) {
        this.rangedA = rangedA;
    }

    public int getStabD() {
        return stabD;
    }

    public void setStabD(int stabD) {
        this.stabD = stabD;
    }

    public int getSlashD() {
        return slashD;
    }

    public void setSlashD(int slashD) {
        this.slashD = slashD;
    }

    public int getCrushD() {
        return crushD;
    }

    public void setCrushD(int crushD) {
        this.crushD = crushD;
    }

    public int getMagicD() {
        return magicD;
    }

    public void setMagicD(int magicD) {
        this.magicD = magicD;
    }

    public int getRangedD() {
        return rangedD;
    }

    public void setRangedD(int rangedD) {
        this.rangedD = rangedD;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getmStrength() {
        return mStrength;
    }

    public void setmStrength(int mStrength) {
        this.mStrength = mStrength;
    }

    public int getrStrength() {
        return rStrength;
    }

    public void setrStrength(int rStrength) {
        this.rStrength = rStrength;
    }

    public int getmDamage() {
        return mDamage;
    }

    public void setmDamage(int mDamage) {
        this.mDamage = mDamage;
    }

    public ArrayList<GameItem> getDrops() {
        return drops;
    }

    public void setDrops(ArrayList<GameItem> drops) {
        this.drops = drops;
    }

    public String getWiki() {
        return wiki;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + combatLevel;
    }
}
