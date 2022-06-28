package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Invoice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvoiceService {
    @GET("invoices/{id}")
    Call<Invoice> getInvoice(@Path("id") int serviceId, @Header("Authorization") String accessToken);

    @POST("invoices/{id}")
    Call<Invoice> saveInvoice(@Path("id") int serviceId, @Header("Authorization") String accessToken);

    @POST("invoices/pay/{id}")
    Call<Invoice> payInvoice(@Path("id") int serviceId, @Header("Authorization") String accessToken);
}