package com.example.projectscape.Utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.projectscape.Activities.ItemDetailsActivity;
import com.example.projectscape.Activities.MainActivity;
import com.example.projectscape.Activities.MonsterDetailsActivity;
import com.example.projectscape.Objects.DataEntry;
import com.example.projectscape.Objects.GameItem;
import com.example.projectscape.Objects.Monster;
import com.example.projectscape.Objects.MonsterVarient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class APIHandler {

    public static void getCharDetails(Context c) {
        new getCharDetails(c).execute();
    }

    public static void getItemDetails(Context c) {
        new getItemDetails(c).execute();
    }

    public static void getItemVolumes(Context c) {
        new getItemVolumes(c).execute();
    }

    public static void getMonsterDetails(Context c, MonsterVarient m) {
        new getMonsterDetails(c, m).execute();
    }

    public static void getMonsterSimpleDetails() {
        new getMonsterSimpleDetails().execute();
    }

    public static void getChartData(Context c, int itemId) {
        new getChartData(c, itemId).execute();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static class getCharDetails extends AsyncTask<String, Void, StringBuilder> {
        private WeakReference<Context> c;

        getCharDetails(Context context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL itemDB = new URL("https://oldschool.runescape.wiki/cors/m=hiscore_oldschool/index_lite.ws?player=Shtacks");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            try {
                String json = result.toString();
                int[] skillLevels = new int[24];
                String[] strings = json.split("\n");
                for (int i = 0; i < 24; i++) {
                    String[] skill = strings[i].split(",");
                    skillLevels[i] = Integer.parseInt(skill[1]);
                }
                PersistentData.getPlayer().setSkills(skillLevels);
                DataHandler.savePlayerDetails(c.get());
            } catch (IndexOutOfBoundsException e) {
                new getCharDetails(c.get()).execute();
            }
            ((MainActivity) c.get()).hideLoading();
        }
    }

    private static class getItemDetails extends AsyncTask<String, Void, StringBuilder> {

        private WeakReference<Context> c;

        getItemDetails(Context context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL itemDB = new URL("https://www.osrsbox.com/osrsbox-db/items-complete.json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            String json = result.toString();
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.beginObject();
                int index = 0;
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.beginObject();
                    int id = -1;
                    String itemName = null;
                    boolean geTrade = false;
                    String wiki = "none";
                    boolean stacked = false;
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        switch (name) {
                            case "id":
                                id = reader.nextInt();
                                break;
                            case "name":
                                itemName = reader.nextString();
                                break;
                            case "tradeable_on_ge":
                                geTrade = reader.nextBoolean();
                                break;
                            case "stackable":
                                stacked = reader.nextBoolean();
                                break;
                            case "wiki_url":
                                try {
                                    wiki = reader.nextString();
                                } catch (IllegalStateException e) {
                                    reader.skipValue();
                                }
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                    if (geTrade && !stacked) {
                        assert itemName != null;
                        GameItem newItem = new GameItem(id, itemName);
                        newItem.setWiki(wiki);
                        newItem.setIndex(index);
                        PersistentData.addMarketGood(newItem);
                        index++;
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataHandler.saveMarketItems(c.get());
            DataHandler.getMarketItems(c.get());
            new getItemVolumes(c.get()).execute();
        }
    }

    private static class getItemVolumes extends AsyncTask<String, Void, StringBuilder> {

        private WeakReference<Context> c;

        getItemVolumes(Context context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL itemDB = new URL("https://rsbuddy.com/exchange/summary.json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            String json = result.toString();
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.beginObject();
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.beginObject();
                    int id = -1;
                    String itemName = null;
                    int buy = -1;
                    int sell = -1;
                    int buyPrice = -1;
                    int sellPrice = -1;
                    boolean members = false;
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        switch (name) {
                            case "id":
                                id = reader.nextInt();
                                break;
                            case "name":
                                itemName = reader.nextString();
                                break;
                            case "buy_quantity":
                                buy = reader.nextInt();
                                break;
                            case "sell_quantity":
                                sell = reader.nextInt();
                                break;
                            case "members":
                                members = reader.nextBoolean();
                                break;
                            case "buy_average":
                                buyPrice = reader.nextInt();
                                break;
                            case "sell_average":
                                sellPrice = reader.nextInt();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                    assert itemName != null;
                    if (PersistentData.getMarketGoods().contains(new GameItem(id, itemName))) {
                        PersistentData.getMaketGoodEqualTo(new GameItem(id, itemName)).addVolumes(buy, sell);
                        PersistentData.getMaketGoodEqualTo(new GameItem(id, itemName)).setMembersOnly(members);
                        PersistentData.getMaketGoodEqualTo(new GameItem(id, itemName)).setBuyPrice(buyPrice);
                        PersistentData.getMaketGoodEqualTo(new GameItem(id, itemName)).setSellPrice(sellPrice);
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < PersistentData.getFavoriteGoodsSize(); i++) {
                GameItem fav = PersistentData.getFavoriteGoods().get(i);
                GameItem marketGood = PersistentData.getMarketGoodByIndex(fav.getIndex());
                fav.addVolumes(marketGood.getBuy(), marketGood.getSell());
                fav.setBuyPrice(marketGood.getBuyPrice());
                fav.setSellPrice(marketGood.getSellPrice());
            }
            ((MainActivity) c.get()).startApp();
        }
    }

    private static class getMonsterDetails extends AsyncTask<String, Void, StringBuilder> {

        private WeakReference<Context> c;
        private MonsterVarient m;

        getMonsterDetails(Context context, MonsterVarient monster) {
            c = new WeakReference<>(context);
            m = monster;
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL itemDB = new URL("https://www.osrsbox.com/osrsbox-db/monsters-json/" + m.getId() + ".json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            String json = result.toString();
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    switch (name) {
                        case "hitpoints":
                            m.setHp(reader.nextInt());
                            break;
                        case "max_hit":
                            m.setMaxHit(reader.nextInt());
                            break;
                        case "attack_speed":
                            m.setAttackSpeed(reader.nextInt());
                            break;
                        case "aggressive":
                            m.setAggressive(reader.nextBoolean());
                            break;
                        case "poisonous":
                            m.setPoisonous(reader.nextBoolean());
                            break;
                        case "immune_poison":
                            m.setImmunePoison(reader.nextBoolean());
                            break;
                        case "immune_venom":
                            m.setImmuneVenom(reader.nextBoolean());
                            break;
                        case "attack_magic":
                            m.setMagicA(reader.nextInt());
                            break;
                        case "attack_ranged":
                            m.setRangedA(reader.nextInt());
                            break;
                        case "defence_stab":
                            m.setStabD(reader.nextInt());
                            break;
                        case "defence_slash":
                            m.setSlashD(reader.nextInt());
                            break;
                        case "defence_crush":
                            m.setCrushD(reader.nextInt());
                            break;
                        case "defence_magic":
                            m.setMagicD(reader.nextInt());
                            break;
                        case "defence_ranged":
                            m.setRangedD(reader.nextInt());
                            break;
                        case "attack_accuracy":
                            m.setAccuracy(reader.nextInt());
                            break;
                        case "melee_strength":
                            m.setmStrength(reader.nextInt());
                            break;
                        case "ranged_strength":
                            m.setrStrength(reader.nextInt());
                            break;
                        case "magic_damage":
                            m.setmDamage(reader.nextInt());
                            break;
                        case "category":
                            reader.beginArray();
                            ArrayList<String> c = new ArrayList<>();
                            while (reader.hasNext()) {
                                c.add(reader.nextString());
                            }
                            reader.endArray();
                            m.setCategories(c);
                            break;
                        case "slayer_monster":
                            m.setSlayerMonster(reader.nextBoolean());
                            break;
                        case "slayer_level":
                            if (m.isSlayerMonster()) {
                                m.setSlayerLevel(reader.nextInt());
                            } else {
                                reader.skipValue();
                            }
                            break;
                        case "slayer_xp":
                            if (m.isSlayerMonster()) {
                                m.setSlayerXp(reader.nextDouble());
                            } else {
                                reader.skipValue();
                            }
                            break;
                        case "slayer_masters":
                            if (m.isSlayerMonster()) {
                                reader.beginArray();
                                ArrayList<String> sm = new ArrayList<>();
                                while (reader.hasNext()) {
                                    sm.add(reader.nextString());
                                }
                                reader.endArray();
                                m.setSlayerMasters(sm);
                            } else {
                                reader.skipValue();
                            }
                            break;
                        case "attack_level":
                            m.setAttackLvl(reader.nextInt());
                            break;
                        case "strength_level":
                            m.setStrengthLvl(reader.nextInt());
                            break;
                        case "defence_level":
                            m.setDefenceLvl(reader.nextInt());
                            break;
                        case "magic_level":
                            m.setMagicLvl(reader.nextInt());
                            break;
                        case "ranged_level":
                            m.setRangedLvl(reader.nextInt());
                            break;
                        case "drops":
                            ArrayList<GameItem> d = new ArrayList<>();
                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();
                                int id = -1;
                                String itemName = "";
                                while (reader.hasNext()) {
                                    String n = reader.nextName();
                                    switch(n) {
                                        case "id":
                                            id = reader.nextInt();
                                            break;
                                        case "name":
                                            itemName = reader.nextString();
                                            break;
                                        default:
                                            reader.skipValue();
                                            break;
                                    }
                                }
                                reader.endObject();
                                GameItem drop = new GameItem(id, itemName);
                                if (!d.contains(drop)) {
                                    d.add(drop);
                                }
                            }
                            reader.endArray();
                            m.setDrops(d);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((MonsterDetailsActivity) c.get()).populateInfo(m);
        }
    }

    private static class getMonsterSimpleDetails extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL itemDB = new URL("https://www.osrsbox.com/osrsbox-db/monsters-complete.json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            String json = result.toString();
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.beginObject();
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.beginObject();
                    int id = -1;
                    String mName = "";
                    int lvl = -1;
                    boolean dupe = false;
                    String wiki = "none";
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        switch (name) {
                            case "id":
                                id = reader.nextInt();
                                break;
                            case "name":
                                mName = reader.nextString();
                                break;
                            case "combat_level":
                                lvl = reader.nextInt();
                                break;
                            case "duplicate":
                                dupe = reader.nextBoolean();
                                break;
                            case "wiki_url":
                                try {
                                    wiki = reader.nextString();
                                } catch (IllegalStateException e) {
                                    reader.skipValue();
                                }

                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                    if (!dupe) {
                        Monster newMonster = new Monster(id, mName, lvl, wiki);
                        PersistentData.addMonster(newMonster);
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class getChartData extends AsyncTask<String, Void, StringBuilder> {

        private WeakReference<Context> c;
        private int itemId;
        ArrayList<DataEntry> chartDataDaily;
        ArrayList<DataEntry> chartDataAvg;

        getChartData(Context context, int i) {
            c = new WeakReference<>(context);
            itemId = i;
            chartDataAvg = new ArrayList<>();
            chartDataDaily = new ArrayList<>();
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            try {
                URL marketInfo = new URL("http://services.runescape.com/m=itemdb_oldschool/api/graph/" + itemId + ".json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(marketInfo.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                itemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(StringBuilder result) {
            String json = result.toString();
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.beginObject();
                reader.nextName();
                reader.beginObject();
                int count = 0;
                while (reader.hasNext()) {
                    String date = reader.nextName();
                    int price = reader.nextInt();
                    chartDataDaily.add(new DataEntry(Long.parseLong(date), price, count));
                    count++;
                }
                reader.endObject();
                reader.nextName();
                reader.beginObject();
                count = 0;
                while (reader.hasNext()) {
                    String date = reader.nextName();
                    int price = reader.nextInt();
                    chartDataAvg.add(new DataEntry(Long.parseLong(date), price, count));
                    count++;
                }
                reader.endObject();
                reader.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ItemDetailsActivity) c.get()).organizeData(chartDataDaily, chartDataAvg);
        }
    }

}
