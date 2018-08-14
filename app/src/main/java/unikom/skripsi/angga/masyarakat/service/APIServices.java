package unikom.skripsi.angga.masyarakat.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import unikom.skripsi.angga.masyarakat.model.HistoryModel;
import unikom.skripsi.angga.masyarakat.model.RekomendasiTempatModel;
import unikom.skripsi.angga.masyarakat.model.SungaiModel;

public interface APIServices {

    @FormUrlEncoded
    @POST("saw.php")
    Call<RekomendasiTempatModel.RekomendasiTempatDataModel> getRekomentasiTempat(@Field("lat") double lat,
                                                                                 @Field("lng") double lng);

    @GET("getNotification.php")
    Call<HistoryModel.HistoryDataModel> getHistory();

    @GET("getSungai.php")
    Call<SungaiModel.SungaiDataModel> getSungai();


}
