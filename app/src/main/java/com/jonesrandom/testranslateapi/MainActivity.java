package com.jonesrandom.testranslateapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jonesrandom.testranslateapi.Translate.ResponseTranslate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<Langs> dataLanguage;

    EditText kataTerjemahan;

    LinearLayout inputLayout;
    LinearLayout outputLayout;

    ProgressBar progressInput;
    ProgressBar progressOutput;

    Spinner spinFrom;
    Spinner spinTo;

    TextView outputDari;
    TextView outputKe;
    TextView kataDiTerjemahkan;

    CardView cardOutputLayout;

    Button btnTerjemahan;

    InputMethodManager iml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iml = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        spinFrom = (Spinner) findViewById(R.id.spinFrom);
        spinTo = (Spinner) findViewById(R.id.spinTo);

        inputLayout = (LinearLayout) findViewById(R.id.inputLayout);
        outputLayout = (LinearLayout) findViewById(R.id.outputLayout);

        progressInput = (ProgressBar) findViewById(R.id.inputProggres);
        progressOutput = (ProgressBar) findViewById(R.id.outputProggres);

        cardOutputLayout = (CardView) findViewById(R.id.cardLayoutOutput);

        outputDari = (TextView) findViewById(R.id.outputDari);
        outputKe = (TextView) findViewById(R.id.outputKe);
        kataDiTerjemahkan = (TextView) findViewById(R.id.kataDiTerjemahkan);

        kataTerjemahan = (EditText) findViewById(R.id.kataTerjemahan);

        btnTerjemahan = (Button) findViewById(R.id.btnTerjemahan);
        btnTerjemahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int posFrom = spinFrom.getSelectedItemPosition();
                int posTo = spinTo.getSelectedItemPosition();

                Langs Dari = dataLanguage.get(posFrom);
                Langs Ke = dataLanguage.get(posTo);

                outputDari.setText(Dari.getLangs());
                outputKe.setText(Ke.getLangs());

                String Text = kataTerjemahan.getText().toString();

                if (Text.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Masukan Kata Untuk Di terjemahkan", Toast.LENGTH_SHORT).show();
                    iml.hideSoftInputFromWindow(kataTerjemahan.getWindowToken(), 0);

                } else {

                    String idFrom = Dari.getId();
                    String idTo = Ke.getId();

                    String lang = idFrom + "-" + idTo;

                    getTranslate(Text, lang);
                    iml.hideSoftInputFromWindow(kataTerjemahan.getWindowToken(), 0);

                }

            }
        });

        getLangs();


    }

    public void getLangs() {

        inputLayout.setVisibility(View.GONE);
        progressInput.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> call = service.getLangs("YOUR API KEY", "id");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dataLanguage = new ArrayList<Langs>();
                List<String> listId = new ArrayList<String>();

                if (response.isSuccessful()) {

                    String text = null;
                    try {

                        text = response.body().string();

                        JSONObject objFull = new JSONObject(text);
                        JSONObject obgLangs = objFull.getJSONObject("langs");

                        Iterator<String> iterator = obgLangs.keys();

                        int Data = 1;
                        while (iterator.hasNext()) {
                            String Ids = iterator.next();
                            listId.add(Ids);
                            Log.d("Add_Id", "onResponse: " + Data + Ids);
                            Data++;
                        }

                        for (int i = 0; i < listId.size(); i++) {

                            String id = listId.get(i);
                            String langs = (String) obgLangs.get(listId.get(i));

                            Langs language = new Langs();
                            language.setId(id);
                            language.setLangs(langs);
                            dataLanguage.add(language);

                        }

                        Collections.sort(dataLanguage, new Comparator<Langs>() {
                            @Override
                            public int compare(Langs langs, Langs t1) {
                                return langs.getLangs().compareTo(t1.getLangs());
                            }
                        });

                        SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, R.layout.spinner_row, dataLanguage);
                        spinFrom.setAdapter(adapter);
                        spinTo.setAdapter(adapter);

                        spinFrom.setSelection(35);
                        spinTo.setSelection(19);

                        inputLayout.setVisibility(View.VISIBLE);
                        progressInput.setVisibility(View.GONE);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void getTranslate(String text, String lang) {

        cardOutputLayout.setVisibility(View.VISIBLE);

        outputLayout.setVisibility(View.GONE);
        progressOutput.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseTranslate> call = service.getTranslate("YOUR API KEY", text, lang);
        call.enqueue(new Callback<ResponseTranslate>() {
            @Override
            public void onResponse(Call<ResponseTranslate> call, Response<ResponseTranslate> response) {

                if (response.isSuccessful()) {

                    String Arti = response.body().getText().get(0);
                    Log.d("Arti", "onResponse: " + Arti);

                    kataDiTerjemahkan.setText(Arti);

                    progressOutput.setVisibility(View.GONE);
                    outputLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ResponseTranslate> call, Throwable t) {

            }
        });


    }

}
