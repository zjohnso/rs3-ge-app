package com.example.projectscape.Utility;

import com.example.projectscape.Objects.GameItem;
import com.example.projectscape.Objects.Monster;
import com.example.projectscape.Objects.MonsterVarient;
import com.example.projectscape.Objects.Player;

import java.util.ArrayList;

public class PersistentData {

    private static Player player = new Player("shtaks");

    private static ArrayList<GameItem> marketGoods = new ArrayList<>();
    private static ArrayList<GameItem> favoriteGoods = new ArrayList<>();
    private static ArrayList<Monster> monsters = new ArrayList<>();

    public static Player getPlayer() {
        return player;
    }

    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }

    static void addMonster(Monster m) {
        for (int i = 0; i < monsters.size(); i++) {
            Monster c = monsters.get(i);
            if (c.getName().equals(m.getName())) {
                MonsterVarient v = m.getVarients().get(0);
               monsters.get(i).addVarient(new MonsterVarient(v.getCombatLevel(), v.getId(), v.getWiki()));
               return;
            }
        }
        m.setIndex(monsters.size());
        monsters.add(m);
    }

    public static Monster getMonsterByIndex(int i) {
        return monsters.get(i);
    }

    static ArrayList<String> getMarketGoodsSaved() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < marketGoods.size(); i++) {
            list.add(marketGoods.get(i).getName() + "," + marketGoods.get(i).getId() + "," + marketGoods.get(i).getWiki());
        }
        return list;
    }

    static ArrayList<String> getFavoriteGoodsSaved() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < favoriteGoods.size(); i++) {
            list.add(favoriteGoods.get(i).getName() + "," + favoriteGoods.get(i).getId() + "," + favoriteGoods.get(i).getWiki());
        }
        return list;
    }

    static void setMarketGoods(ArrayList<GameItem> items) {
        PersistentData.marketGoods = items;
    }

    public static ArrayList<GameItem> getMarketGoods() {
        return marketGoods;
    }

    public static GameItem getMarketGoodByIndex(int index) {
        return marketGoods.get(index);
    }

    static GameItem getFavoriteGoodByIndex(int index) {
        return favoriteGoods.get(index);
    }

    static void addMarketGood(GameItem good) {
        for (int i = 0; i < marketGoods.size(); i++) {
            if (marketGoods.get(i).getName().equals(good.getName())) {
                return;
            }
        }
        marketGoods.add(good);
    }

    public static GameItem getMaketGoodEqualTo(GameItem g) {
        return marketGoods.get(marketGoods.indexOf(g));
    }

    public static void removeFavoriteGoodEqualTo(GameItem g) {
        favoriteGoods.remove(g);
    }

    public static ArrayList<GameItem> getFavoriteGoods() {
        return favoriteGoods;
    }

    public static void addFoavoriteGood(GameItem good) {
        favoriteGoods.add(good);
    }

    public static void setFavoriteGoods(ArrayList<GameItem> favoriteGoods) {
        PersistentData.favoriteGoods = favoriteGoods;
    }

    public static int getMarketGoodsSize() {
        return marketGoods.size();
    }

    static int getFavoriteGoodsSize() {
        return favoriteGoods.size();
    }
}
