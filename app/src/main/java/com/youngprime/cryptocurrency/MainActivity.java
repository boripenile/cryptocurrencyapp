package com.youngprime.cryptocurrency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private String coinSymbol;

    private String coinName;

    private String currencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mEditor = mSharedPreferences.edit();

        checkSharedPreferences();

        CardView bitCoinCard = (CardView) findViewById(R.id.cardBitCoin);

        bitCoinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conversionIntent = new Intent(MainActivity.this, ConversionActivity.class);
                coinSymbol = "BTC";
                coinName = "Bitcoin";
                getCurrencyCodeFromPreferences();
                conversionIntent.putExtra("currency", currencyCode);
                conversionIntent.putExtra("coin", coinSymbol);
                setCoinSymbol();
                startActivity(conversionIntent);
            }
        });

        CardView ethereumCoinCard = (CardView) findViewById(R.id.cardEthereum);

        ethereumCoinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conversionIntent = new Intent(MainActivity.this, ConversionActivity.class);
                coinSymbol = "ETH";
                coinName = "Ethereum";
                getCurrencyCodeFromPreferences();
                conversionIntent.putExtra("currency", currencyCode);
                conversionIntent.putExtra("coin", coinSymbol);
                setCoinSymbol();
                startActivity(conversionIntent);
            }
        });

    }

    private void checkSharedPreferences() {
        String localCoinSysmbol = mSharedPreferences.getString(getString(R.string.coin_symbol), "");
        coinSymbol = localCoinSysmbol;

        String localCoinName = mSharedPreferences.getString(getString(R.string.coin_name), "");
        coinName = localCoinName;
    }

    private void getCurrencyCodeFromPreferences() {
        String localCurrencyCode = mSharedPreferences.getString(getString(R.string.currency_code), "NGN");
        currencyCode = localCurrencyCode;
    }

    private void setCoinSymbol() {
        mEditor.putString(getString(R.string.coin_symbol), coinSymbol);
        mEditor.commit();

        mEditor.putString(getString(R.string.coin_name), coinName);
        mEditor.commit();
    }

}
