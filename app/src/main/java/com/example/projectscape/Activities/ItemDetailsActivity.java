package com.example.projectscape.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectscape.Objects.DataEntry;
import com.example.projectscape.Objects.GameItem;
import com.example.projectscape.R;
import com.example.projectscape.Utility.APIHandler;
import com.example.projectscape.Utility.DataHandler;
import com.example.projectscape.Utility.PersistentData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemDetailsActivity extends AppCompatActivity {

    private GameItem currentItem;
    private ImageButton favorite;

    private List<DataEntry> chartDataDailyWeek;
    private List<DataEntry> chartDataAvgWeek;

    private List<DataEntry> chartDataDailyMonth;
    private List<DataEntry> chartDataAvgMonth;

    private List<DataEntry> chartDataDailyQuarter;
    private List<DataEntry> chartDataAvgQuarter;

    private Button weekBtn;
    private Button monthBtn;
    private Button quarterBtn;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Intent intent = getIntent();
        int itemIndex = intent.getIntExtra("index", -1);
        currentItem = PersistentData.getMarketGoodByIndex(itemIndex);
        APIHandler.getChartData(this, currentItem.getId());
        Toolbar tb = findViewById(R.id.toolBar2);
        tb.setTitle("Item Data");
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        populateItemInfo();
    }

    private void populateItemInfo() {
        TextView nameView = findViewById(R.id.itemName);
        TextView dynoLabel = findViewById(R.id.dynamicDataLabel);
        TextView dynoView = findViewById(R.id.dynamicDataView);
        ImageView membersOnly = findViewById(R.id.membersOnly);
        if (currentItem.getMembersOnly()) {
            membersOnly.setImageDrawable(getDrawable(R.drawable.ic_members_true));
        }
        nameView.setText(currentItem.getName());

        StringBuilder l = new StringBuilder();
        l.append("Current Buy Volume:\n");
        l.append("Current Sell Volume:\n");
        l.append("Current Average Buy Price:\n");
        l.append("Current Average Sell Price:\n");

        StringBuilder v = new StringBuilder();
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getBuy())).append("\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getSell())).append("\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getBuyPrice())).append("\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.getSellPrice())).append("\n");

        dynoLabel.setText(l);
        dynoView.setText(v);
        ImageView iv = findViewById(R.id.imageView);
        Picasso.get().load("https://www.osrsbox.com/osrsbox-db/items-icons/" + currentItem.getId() + ".png").fit().into(iv);

        favorite = findViewById(R.id.favoriteSelector);
        if (PersistentData.getFavoriteGoods().contains(currentItem)) {
            favorite.setBackgroundResource(R.drawable.ic_favorites);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PersistentData.getFavoriteGoods().contains(currentItem)) {
                    favorite.setBackgroundResource(R.drawable.ic_favorites);
                    currentItem.setPrice(chartDataDailyWeek.get(chartDataDailyWeek.size() - 1).getPrice());
                    PersistentData.addFoavoriteGood(currentItem);
                } else {
                    favorite.setBackgroundResource(R.drawable.ic_favorite_deselected);
                    PersistentData.removeFavoriteGoodEqualTo(currentItem);
                }
                DataHandler.saveFavorites(v.getContext());
            }
        });

        ImageButton wikiNav = findViewById(R.id.wikiButton);
        wikiNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = currentItem.getWiki();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        if (currentItem.getWiki().equals("none")) {
            wikiNav.setEnabled(false);
            wikiNav.setBackgroundResource(R.drawable.ic_refresh_disabled);
        }
    }

    public void organizeData(ArrayList<DataEntry> chartDataDaily, ArrayList<DataEntry> chartDataAvg) {
        int currentPrice = 0;
        if (chartDataDaily.size() > 0) {
            currentPrice = chartDataDaily.get(chartDataDaily.size() - 1).getPrice();
        }
        TextView priceView = findViewById(R.id.priceView);
        String s = "" + NumberFormat.getNumberInstance(Locale.getDefault()).format(currentPrice);
        priceView.setText(s);
        chartDataAvgWeek = new ArrayList<>();
        chartDataDailyWeek = new ArrayList<>();
        chartDataAvgMonth = new ArrayList<>();
        chartDataDailyMonth = new ArrayList<>();
        chartDataAvgQuarter = new ArrayList<>();
        chartDataDailyQuarter = new ArrayList<>();

        for (int i = 0; i < chartDataDaily.size(); i++) {
            if (i >= 90) {
                DataEntry entry = chartDataDaily.get(i);
                chartDataDailyQuarter.add(entry);
                if (i >= 150) {
                    chartDataDailyMonth.add(entry);
                }
                if (i >= 174) {
                    chartDataDailyWeek.add(entry);
                }
            }
        }

        for (int i = 0; i < chartDataAvg.size(); i++) {
            if (i >= 90) {
                DataEntry entry = chartDataAvg.get(i);
                chartDataAvgQuarter.add(entry);
                if (i >= 150) {
                    chartDataAvgMonth.add(entry);
                }
                if (i >= 174) {
                    chartDataAvgWeek.add(entry);
                }
            }
        }

        populateCharts(chartDataDailyMonth, chartDataAvgMonth);

        weekBtn = findViewById(R.id.weekSelect);
        monthBtn = findViewById(R.id.monthSelect);
        quarterBtn = findViewById(R.id.quarterSelect);

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthBtn.setBackgroundColor(getColor(R.color.background));
                monthBtn.setTextColor(getColor(R.color.text));
                quarterBtn.setBackgroundColor(getColor(R.color.background));
                quarterBtn.setTextColor(getColor(R.color.text));

                weekBtn.setBackgroundColor(getColor(R.color.text));
                weekBtn.setTextColor(getColor(R.color.backgroundDark));

                populateCharts(chartDataDailyWeek, chartDataAvgWeek);
            }
        });
        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekBtn.setBackgroundColor(getColor(R.color.background));
                weekBtn.setTextColor(getColor(R.color.text));
                quarterBtn.setBackgroundColor(getColor(R.color.background));
                quarterBtn.setTextColor(getColor(R.color.text));

                monthBtn.setBackgroundColor(getColor(R.color.text));
                monthBtn.setTextColor(getColor(R.color.backgroundDark));

                populateCharts(chartDataDailyMonth, chartDataAvgMonth);
            }
        });
        quarterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthBtn.setBackgroundColor(getColor(R.color.background));
                monthBtn.setTextColor(getColor(R.color.text));
                weekBtn.setBackgroundColor(getColor(R.color.background));
                weekBtn.setTextColor(getColor(R.color.text));

                quarterBtn.setBackgroundColor(getColor(R.color.text));
                quarterBtn.setTextColor(getColor(R.color.backgroundDark));

                populateCharts(chartDataDailyQuarter, chartDataAvgQuarter);
            }
        });
    }

    private void populateCharts(List<DataEntry> daily, List<DataEntry> avg) {

        int priceChange = daily.get(daily.size() - 1).getPrice() - daily.get(0).getPrice();
        int avgPriceChange = avg.get(avg.size() - 1).getPrice() - avg.get(0).getPrice();
        int priceChangePercent = (int) Math.round((((double)priceChange)/((double)daily.get(0).getPrice()))*100);
        int avgPriceChangePercent = (int) Math.round((((double)avgPriceChange)/((double)avg.get(0).getPrice()))*100);

        TextView detailLabel = findViewById(R.id.detailedDataLabel);
        TextView detailView = findViewById(R.id.detailedDataView);

        StringBuilder l = new StringBuilder();
        l.append("Actual Price Change:\n");
        l.append("Actual Price Change (%):\n");
        l.append("Average Price Change:\n");
        l.append("Average Price Change (%):\n");

        StringBuilder v = new StringBuilder();
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(priceChange)).append("\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(priceChangePercent)).append("%\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(avgPriceChange)).append("\n");
        v.append(NumberFormat.getNumberInstance(Locale.getDefault()).format(avgPriceChangePercent)).append("%\n");

        detailLabel.setText(l);
        detailView.setText(v);

        LineChart chart = findViewById(R.id.graph);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < daily.size(); i++) {
            entries.add(new Entry(daily.get(i).getIndex(), daily.get(i).getPrice()));
        }
        LineDataSet ds = new LineDataSet(entries, "Price");
        ds.setColor(getColor(R.color.chartValues));
        ds.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(ds);
        LineData data = new LineData(dataSets);
        data.setDrawValues(false);
        chart.setData(data);
        chart.getAxisLeft().setTextColor(getColor(R.color.text));
        chart.getXAxis().setTextColor(getColor(R.color.text));
        chart.getAxisRight().setTextColor(getColor(R.color.text));
        chart.getLegend().setTextColor(getColor(R.color.text));
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(true);
        chart.setBorderColor(getColor(R.color.chartGrid));
        chart.getLegend().setEnabled(false);
        chart.setGridBackgroundColor(getColor(R.color.chartGrid));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setValueFormatter(new LargeValueFormatter());
        chart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        chart.invalidate();

        LineChart chart2 = findViewById(R.id.graph2);
        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int i = 0; i < avg.size(); i++) {
            entries2.add(new Entry(avg.get(i).getIndex(), avg.get(i).getPrice()));
        }
        LineDataSet ds2 = new LineDataSet(entries2, "Average Price");
        ds2.setColor(getColor(R.color.chartValues));
        ds2.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets2 = new ArrayList<>();
        dataSets2.add(ds2);
        LineData data2 = new LineData(dataSets2);
        data2.setDrawValues(false);
        chart2.setData(data2);
        chart2.getAxisLeft().setTextColor(getColor(R.color.text));
        chart2.getXAxis().setTextColor(getColor(R.color.text));
        chart2.getAxisRight().setTextColor(getColor(R.color.text));
        chart2.getLegend().setTextColor(getColor(R.color.text));
        chart2.getDescription().setEnabled(false);
        chart2.setDrawBorders(true);
        chart2.setBorderColor(getColor(R.color.chartGrid));
        chart2.getLegend().setEnabled(false);
        chart2.setGridBackgroundColor(getColor(R.color.chartGrid));
        chart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart2.getAxisRight().setValueFormatter(new LargeValueFormatter());
        chart2.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        chart2.invalidate();
    }
}
