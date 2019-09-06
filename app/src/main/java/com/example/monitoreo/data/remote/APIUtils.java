package com.example.monitoreo.data.remote;

public class APIUtils {

    private APIUtils (){}

    public static final String BASE_URL = "https://api-almacen-rfid.herokuapp.com/api/";

    public static APIService getAPIService(){
        return  RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
