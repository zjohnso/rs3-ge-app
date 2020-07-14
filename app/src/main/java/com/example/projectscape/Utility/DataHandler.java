package com.example.projectscape.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.projectscape.Objects.GameItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DataHandler {

    private static final String SP = "SHARED_PREFS";

    public static void saveFavorites(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        Set<String> set = new HashSet<>(PersistentData.getFavoriteGoodsSaved());
        mEdit1.putStringSet("list", set);
        mEdit1.apply();
    }

    static void savePlayerDetails(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int[] skills = PersistentData.getPlayer().getSkills();
        ArrayList<String> skillStrings = new ArrayList<>();
        for (int i = 0; i < skills.length; i++) {
            String s = i + "," + skills[i];
            skillStrings.add(s);
        }
        Set<String> set = new HashSet<>(skillStrings);
        editor.putStringSet("skills", set);
        editor.apply();
    }

    public static boolean getPlayerDetails(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        Set<String> set = sp.getStringSet("skills", new HashSet<String>());
        ArrayList<String> skills = new ArrayList<>(set);
        if (skills.size() == 0) {
            return false;
        }
        int[] s = new int[24];
        for (int i = 0; i < skills.size(); i++) {
            String skill = skills.get(i);
            String[] str = skill.split(",");
            int index = Integer.parseInt(str[0]);
            int level = Integer.parseInt(str[1]);
            s[index] = level;
        }
        PersistentData.getPlayer().setSkills(s);
        return true;
    }

    public static void getFavorites(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        Set<String> set = sp.getStringSet("list", new HashSet<String>());
        ArrayList<String> items = new ArrayList<>(set);
        if (items.size() == 0) {
            return;
        }
        Collections.sort(items);
        ArrayList<GameItem> favItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            String itemString = items.get(i);
            String[] str = itemString.split(",");
            int id = Integer.parseInt(str[1]);
            String name = str[0];
            String wiki = str[2];
            GameItem newItem = new GameItem(id, name);
            newItem.setWiki(wiki);
            newItem.setIndex(PersistentData.getMaketGoodEqualTo(newItem).getIndex());
            favItems.add(newItem);
        }
        PersistentData.setFavoriteGoods(favItems);

    }

    public static boolean getMarketItems(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        Set<String> set = sp.getStringSet("items", new HashSet<String>());
        ArrayList<String> items = new ArrayList<>(set);
        if (items.size() == 0) {
            return false;
        }
        Collections.sort(items);
        ArrayList<GameItem> marketItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            String itemString = items.get(i);
            String[] str = itemString.split(",");
            int id = Integer.parseInt(str[1]);
            String name = str[0];
            String wiki = str[2];
            GameItem newItem = new GameItem(id, name);
            newItem.setWiki(wiki);
            newItem.setIndex(i);
            marketItems.add(newItem);
        }
        PersistentData.setMarketGoods(marketItems);
        return true;
    }

    static void saveMarketItems(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        Set<String> set = new HashSet<>(PersistentData.getMarketGoodsSaved());
        mEdit1.putStringSet("items", set);
        mEdit1.apply();
    }

}
