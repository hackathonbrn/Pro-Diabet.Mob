package raitoningu.pro_diabet.Model.ApiService;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("api/data-all")
    Call<String> makeRequest();
}
