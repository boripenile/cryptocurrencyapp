package com.youngprime.cryptocurrency;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youngprime.cryptocurrency.service.CrytoCurrencyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bd_dev_murtala on 20/10/2017.
 */

public class ConversionActivity extends AppCompatActivity
        implements CoinDialogFragment.CoinDialogListener, CurrencyDialogFragment.CurrencyDialogListener{

    private static final String TAG = "ConversionActivity";

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private Button btnCoinCurrency;

    private Button btnLoadCurrencies;

    private Button editCoinValue;

    private Button editCurrencyValue;

    private TextView txtConversionSummary;

    private String currencyCode;

    private String currencyName;

    private String currencySymbol;

    private String coinSymbol;

    private Double coinValue;

    private String coinName;

    private Double currencyValue;

    private SwipeRefreshLayout refreshLayout;

    private RelativeLayout parentLayout;

    private final static String BASE_URL = "https://min-api.cryptocompare.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_conversion_layout);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        parentLayout = (RelativeLayout) findViewById(R.id.conversionLayoutParent);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mEditor = mSharedPreferences.edit();

        Log.d(TAG, "onCreate: Started");

        btnLoadCurrencies = (Button) findViewById(R.id.btnCurrency);

        btnLoadCurrencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent currenciesIntent = new Intent(ConversionActivity.this, CurrencyListActivtity.class);
                startActivity(currenciesIntent);
            }
        });

        btnCoinCurrency = (Button) findViewById(R.id.btnCoinCurrency);

        btnCoinCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent coinsIntent = new Intent(ConversionActivity.this, MainActivity.class);
                startActivity(coinsIntent);
            }
        });

        editCoinValue = (Button) findViewById(R.id.editValueCoin);
        editCurrencyValue = (Button) findViewById(R.id.editValueCurrency);

        editCoinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Coin Value button clicked.");
                showCoinDialog();
            }
        });

        editCurrencyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Currency Value button clicked.");
                showCurrencyDialog();
            }
        });

        txtConversionSummary = (TextView) findViewById(R.id.txtConversionSummary);
        Intent incomingIntent = getIntent();
        if (incomingIntent.getStringExtra("coin") != null &&
                incomingIntent.getStringExtra("currency") != null) {
            coinSymbol = incomingIntent.getStringExtra("coin");
            currencyCode = incomingIntent.getStringExtra("currency");
            convertFromCoinToCurrency(String.valueOf(coinValue));
        }

        ImageButton backImage = (ImageButton) findViewById(R.id.imageBack);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent coinsIntent = new Intent(ConversionActivity.this, MainActivity.class);
                startActivity(coinsIntent);
            }
        });
    }

    private void getConversion() {
        toggleLayout();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clearConversionSummary();
        CrytoCurrencyService currencyService = retrofit.create(CrytoCurrencyService.class);
        Call<Object> resp = currencyService.getConversion(coinSymbol, currencyCode);
        Log.d(TAG, "getConversion:");
        processResponse(resp);
    }

    private void getConversionFromCurrency() {
        toggleLayout();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clearConversionSummary();
        CrytoCurrencyService currencyService = retrofit.create(CrytoCurrencyService.class);
        Call<Object> resp = currencyService.getConversion(currencyCode, coinSymbol);
        Log.d(TAG, "getConversionFromCurrency:");
        processResponseFromCurrency(resp);
    }

    private void processResponse(Call<Object> resp) {
        resp.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                toggleLayout();
                Object converted = response.body();
                populateConversion(converted);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                toggleLayout();
                Log.e(TAG, "onFailure: Something went wrong " + t.getMessage());
                setCurrencyValue(currencyValue);
            }
        });
    }

    private void processResponseFromCurrency(Call<Object> resp) {
        resp.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                toggleLayout();
                Object converted = response.body();
                populateConversionFromCurrency(converted);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                toggleLayout();
                Log.e(TAG, "onFailure: Something went wrong " + t.getMessage());
                setCoinValue(currencyValue);
            }
        });
    }

    private void showCoinDialog() {
        DialogFragment coinDialog = new CoinDialogFragment();
        coinDialog.show(getFragmentManager(), "CoinDialogFragment");
    }

    private void showCurrencyDialog() {
        DialogFragment currencyDialog = new CurrencyDialogFragment();
        currencyDialog.show(getFragmentManager(), "CurrencyDialogFragment");
    }

    @Override
    public void onCoinDialogPositiveClick(DialogFragment dialog) {
        EditText coin = (EditText) dialog.getDialog().findViewById(R.id.editCoinValueToConvert);
        convertFromCoinToCurrency(coin.getText().toString());
    }

    @Override
    public void onCoinDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onCurrencyDialogPositiveClick(DialogFragment dialog) {
        EditText currency = (EditText) dialog.getDialog().findViewById(R.id.editCurrencyValueToConvert);
        convertFromCurrencyToCoin(currency.getText().toString());
    }

    @Override
    public void onCurrencyDialogNegativeClick(DialogFragment dialog) {

    }

    private void setCoinValue() {
        mEditor.putString(getString(R.string.coin_value), String.valueOf(coinValue));
        mEditor.commit();
    }

    private void setCurrencyValue() {
        mEditor.putString(getString(R.string.currency_value), String.valueOf(currencyValue));
        mEditor.commit();
    }

    private void populateConversion(Object conversion) {
        if (conversion != null) {
            String result = conversion.toString();
            String value = result.replace("{", "");
            String convertedAmount = value.substring(4, value.length() - 1);
            Log.d(TAG, "populateConversion: " + convertedAmount);
            Double amountToDouble = Double.parseDouble(convertedAmount);
            Double convertedResult = Double.parseDouble(editCoinValue.getText().toString()) * amountToDouble;
            String strConvertedResult = String.format("%.2f", convertedResult.doubleValue());
            editCurrencyValue.setText(strConvertedResult);

            setCurrencyValue(Double.parseDouble(strConvertedResult));
        }
    }

    private void populateConversionFromCurrency(Object conversion) {
        if (conversion != null) {
            String result = conversion.toString();
            String value = result.replace("{", "");
            String convertedAmount = value.substring(4, value.length() - 1);
            Log.d(TAG, "populateConversionFromCurrency: " + convertedAmount);
            Double amountToDouble = Double.parseDouble(convertedAmount);
            Double convertedResult = amountToDouble * Double.parseDouble(editCurrencyValue.getText().toString());
            String strConvertedResult = String.valueOf(convertedResult.doubleValue());
            editCoinValue.setText(strConvertedResult);

            setCoinValue(Double.parseDouble(strConvertedResult));
        }
    }
    private void setCurrencyValue(Double value) {
        mEditor.putString(getString(R.string.currency_value), String.valueOf(value));
        mEditor.commit();

        currencyValue = value;
        setConversionSummary();
    }

    private void setCoinValue(Double value) {
        mEditor.putString(getString(R.string.coin_value), String.valueOf(value));
        mEditor.commit();

        coinValue = value;
        setConversionSummaryFromCurrency();
    }

    private void convertFromCoinToCurrency(String coinVal) {
        try {
            coinValue = Double.parseDouble(coinVal);
            editCoinValue.setText(String.valueOf(coinValue));
        } catch (NumberFormatException e) {
            coinValue = 1.0;
            editCoinValue.setText(String.valueOf(coinValue));
        }
        setCoinValue();
        getConversion();
    }

    private void convertFromCurrencyToCoin(String currencyVal) {
        try {
            currencyValue = Double.parseDouble(currencyVal);
            editCurrencyValue.setText(String.valueOf(currencyValue));
        } catch (NumberFormatException e) {
            currencyValue = 1.0;
            editCurrencyValue.setText(String.valueOf(currencyValue));
        }
        setCurrencyValue();
        getConversionFromCurrency();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currencyCode = mSharedPreferences.getString(getString(R.string.currency_code), "NGN");
        currencyName = mSharedPreferences.getString(getString(R.string.currency_name), "Nigerian Naira");
        coinName = mSharedPreferences.getString(getString(R.string.coin_name), "Bitcoin");

        currencySymbol = mSharedPreferences.getString(getString(R.string.currency_symbol), "₦");
        btnLoadCurrencies.setText(currencySymbol);

        coinSymbol = mSharedPreferences.getString(getString(R.string.coin_symbol), "BTC");
        btnCoinCurrency.setText(coinSymbol);

        coinValue = Double.parseDouble(mSharedPreferences.getString(getString(R.string.coin_value), "1.0"));
        editCoinValue.setText(String.valueOf(coinValue));

        currencyValue = Double.parseDouble(mSharedPreferences.getString(getString(R.string.currency_value), "2131799.88"));
        editCurrencyValue.setText(String.valueOf(currencyValue));
    }

    private void setConversionSummary() {
        StringBuilder content = new StringBuilder();
        content.append(coinValue);
        content.append(" ");
        content.append(coinName);
        content.append(" equal to ");
        content.append(String.format("%.2f", currencyValue));
        content.append(" ");
        content.append(currencyName);

        txtConversionSummary.setText(content.toString());
    }

    private void setConversionSummaryFromCurrency() {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%.2f", currencyValue));
        content.append(" ");
        content.append(currencyName);
        content.append(" equal to ");
        content.append(coinValue);
        content.append(" ");
        content.append(coinName);

        txtConversionSummary.setText(content.toString());
    }

    private void clearConversionSummary() {
        txtConversionSummary.setText("");
    }

    private void toggleLayout() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            parentLayout.setAlpha(1.0f);
            btnCoinCurrency.setClickable(true);
            btnLoadCurrencies.setClickable(true);
            editCoinValue.setClickable(true);
            editCurrencyValue.setClickable(true);
        } else {
            refreshLayout.setRefreshing(true);
            parentLayout.setAlpha(0.5f);
            btnCoinCurrency.setClickable(false);
            btnLoadCurrencies.setClickable(false);
            editCoinValue.setClickable(false);
            editCurrencyValue.setClickable(false);
        }
    }

}
