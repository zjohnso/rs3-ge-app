package com.example.projectscape.Objects;

import androidx.annotation.Nullable;

public class GameItem {

    private int id;
    private String name;
    private int buy;
    private int sell;
    private boolean members;
    private int index;
    private int price;
    private int sellPrice;
    private int buyPrice;
    private String wiki;

    public GameItem(int id, String name) {
        String[] strArray = name.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        this.id = id;
        this.name = builder.toString();
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getWiki() {
        return wiki;
    }

    public void addVolumes(int buy, int sell) {
        this.buy = buy;
        this.sell = sell;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setMembersOnly(boolean b) {
        members = b;
    }

    public boolean getMembersOnly() {
        return members;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getSell() {
        return sell;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        GameItem g = (GameItem) obj;
        assert g != null;
        return g.getId() == this.getId();
    }
}
