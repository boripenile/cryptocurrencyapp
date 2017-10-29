package com.youngprime.cryptocurrency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.youngprime.cryptocurrency.model.CountryCurrency;

import java.util.ArrayList;

/**
 * Created by bd_dev_murtala on 20/10/2017.
 */

public class CurrencyListActivtity extends AppCompatActivity {

    private static final String TAG = "CurrencyListActivtity";

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private CurrencyListAdapter adapter;

    private String coinSymbol;

    private String currencyCode;

    private String currencySymbol;

    private String currencyName;

    private ArrayList<CountryCurrency> countryCurrenciesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_list);

        Log.d(TAG, "onCreate: Started");

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mEditor = mSharedPreferences.edit();

        checkSharedPreferences();

        ListView currencyListView = (ListView) findViewById(R.id.currencyListView);

        populateCountryCurrenciesList(currencyListView);

        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currencyCode = countryCurrenciesList.get(position).getCode();
                currencySymbol = countryCurrenciesList.get(position).getSymbol();
                currencyName = countryCurrenciesList.get(position).getCurrencyName();
                setCurrencySymbol();
                Intent conversionIntent = new Intent(CurrencyListActivtity.this, ConversionActivity.class);
                conversionIntent.putExtra("currency", currencyCode);
                conversionIntent.putExtra("coin", mSharedPreferences.getString(getString(R.string.coin_symbol), ""));
                startActivity(conversionIntent);
            }
        });

        ImageButton backImage = (ImageButton) findViewById(R.id.imageBack);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyListActivtity.this.finish();
            }
        });
    }

    private void checkSharedPreferences() {
        String localCurrencySymbol = mSharedPreferences.getString(getString(R.string.currency_symbol), "");
        currencySymbol = localCurrencySymbol;
        String localCurrencyCode = mSharedPreferences.getString(getString(R.string.currency_code), "");
        currencyCode = localCurrencyCode;
        String localCurrencyName = mSharedPreferences.getString(getString(R.string.currency_name), "");
        currencyName = localCurrencyName;
    }

    private void setCurrencySymbol() {
        mEditor.putString(getString(R.string.currency_symbol), currencySymbol);
        mEditor.commit();

        mEditor.putString(getString(R.string.currency_code), currencyCode);
        mEditor.commit();

        mEditor.putString(getString(R.string.currency_name), currencyName);
        mEditor.commit();
    }

    private void populateCountryCurrenciesList(ListView listView) {
        if (countryCurrenciesList == null) {
            countryCurrenciesList = new ArrayList<CountryCurrency>();
        }
        if (countryCurrenciesList.size() > 0) return;

        Log.d(TAG, "populateCountryCurrenciesList: Started adding currencies...");
        CountryCurrency nigeria = new CountryCurrency("https://flagpedia.net/data/flags/normal/ng.png", "Nigerian Naira", "₦", "NGN");
        CountryCurrency kuwait = new CountryCurrency("https://flagpedia.net/data/flags/normal/kw.png", "Kuwaiti Dinar", "KD", "KWD");
        CountryCurrency bahraini = new CountryCurrency("https://flagpedia.net/data/flags/normal/bh.png", "Bahraini Dinar", "BD", "BHD");
        CountryCurrency omani = new CountryCurrency("https://flagpedia.net/data/flags/normal/om.png", "Omani Rial", "OMR", "OMR");
        CountryCurrency british = new CountryCurrency("https://flagpedia.net/data/flags/normal/gb.png", "British Pound", "£", "GBP");
        CountryCurrency euro = new CountryCurrency("https://flagpedia.net/data/flags/normal/es.png", "Euro", "€", "EUR");
        CountryCurrency usa = new CountryCurrency("https://flagpedia.net/data/flags/normal/us.png", "United States Dollar", "$", "USD");
        CountryCurrency swiss = new CountryCurrency("https://flagpedia.net/data/flags/normal/ch.png", "Swiss Franc", "CHF", "CHF");
        CountryCurrency libya = new CountryCurrency("https://flagpedia.net/data/flags/normal/ly.png", "Libyan Dinar", "LD", "LYD");
        CountryCurrency brunei = new CountryCurrency("https://flagpedia.net/data/flags/normal/bn.png", "Brunei Dollar", "B$", "BND");

        CountryCurrency singapore = new CountryCurrency("https://flagpedia.net/data/flags/normal/sg.png", "Singapore Dollar", "S$", "SGD");
        CountryCurrency india = new CountryCurrency("https://flagpedia.net/data/flags/normal/in.png", "India Rupees", "Rs", "INR");
        CountryCurrency japanese = new CountryCurrency("https://flagpedia.net/data/flags/normal/jp.png", "Japanese Yen", "¥", "JPY");
        CountryCurrency chinese = new CountryCurrency("https://flagpedia.net/data/flags/normal/cn.png", "Chinese Yuan", "CN¥", "CNY");
        CountryCurrency taiwan = new CountryCurrency("https://flagpedia.net/data/flags/normal/tw.png", "Taiwan Dollar", "NT$", "TWD");
        CountryCurrency australia = new CountryCurrency("https://flagpedia.net/data/flags/normal/au.png", "Australian Dollar", "AU$", "AUD");
        CountryCurrency russia = new CountryCurrency("https://flagpedia.net/data/flags/normal/ru.png", "Russian Rouble", "RUB", "RUB");
        CountryCurrency southafrica = new CountryCurrency("https://flagpedia.net/data/flags/normal/za.png", "South Africa Rand", "R", "ZAR");
        CountryCurrency mexico = new CountryCurrency("https://flagpedia.net/data/flags/normal/mx.png", "Mexica Peso", "MX$", "MXN");
        CountryCurrency turkish = new CountryCurrency("https://flagpedia.net/data/flags/normal/tr.png", "Turkish Lira", "TL", "TRY");

        countryCurrenciesList.add(nigeria);
        countryCurrenciesList.add(kuwait);
        countryCurrenciesList.add(bahraini);
        countryCurrenciesList.add(omani);
        countryCurrenciesList.add(euro);
        countryCurrenciesList.add(usa);
        countryCurrenciesList.add(swiss);
        countryCurrenciesList.add(libya);
        countryCurrenciesList.add(brunei);
        countryCurrenciesList.add(british);

        countryCurrenciesList.add(singapore);
        countryCurrenciesList.add(india);
        countryCurrenciesList.add(japanese);
        countryCurrenciesList.add(chinese);
        countryCurrenciesList.add(taiwan);
        countryCurrenciesList.add(australia);
        countryCurrenciesList.add(russia);
        countryCurrenciesList.add(southafrica);
        countryCurrenciesList.add(mexico);
        countryCurrenciesList.add(turkish);

        adapter = new CurrencyListAdapter(CurrencyListActivtity.this, R.layout.currency_item_layout,
                countryCurrenciesList);
        listView.setAdapter(adapter);
    }

}
