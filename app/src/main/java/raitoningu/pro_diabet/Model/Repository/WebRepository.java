package raitoningu.pro_diabet.Model.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import raitoningu.pro_diabet.Model.ApiService.APIService;
import raitoningu.pro_diabet.Model.ApiService.APIUrl;
import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebRepository {
    Application application;
    private static OkHttpClient providesOkHttpClientBuilder(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.readTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS).build();
    }

    List<NoteEntity> webserviceResponseList = new ArrayList<>();
    public LiveData<List<NoteEntity>> providesWebService() {
        final MutableLiveData<List<NoteEntity>> data = new MutableLiveData<>();
            String response = "";
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIUrl.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(providesOkHttpClientBuilder())
                        .build();
                APIService service = retrofit.create(APIService.class);
                response = service.makeRequest().execute().body();
                service.makeRequest().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("Repository","Response::::"+response.body());
                        webserviceResponseList = parseJson(response.body());
                        data.setValue(webserviceResponseList);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
            return data;
    }
    private List<NoteEntity> parseJson(String response) {
        List<NoteEntity> apiResults = new ArrayList<>();
        JSONObject object;
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(response);
            for (int i=0; i< jsonArray.length(); i++)
            {
                object = jsonArray.getJSONObject(i);
                NoteEntity mNoteModel = new NoteEntity(
                        Long.parseLong(object.getString("date")),
                        Float.parseFloat(object.getString("sugar")),
                        Float.parseFloat(object.getString("bread")),
                        Float.parseFloat(object.getString("short_insulin")),
                        Float.parseFloat(object.getString("long_insulin")),
                        object.getString("comment"));
                apiResults.add(mNoteModel);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return apiResults;
    }
}


