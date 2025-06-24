package com.example.veyu.data.remote

import android.content.Context
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.api.AirportApi
import com.example.veyu.data.remote.api.AuthApi
import com.example.veyu.data.remote.api.BookingApi
import com.example.veyu.data.remote.api.FlightApi
import com.example.veyu.data.remote.api.PassengerApi
import com.example.veyu.data.remote.api.PaymentMethodApi
import com.example.veyu.data.remote.api.PaymentTransactionApi
import com.example.veyu.data.remote.api.SeatFlightApi
import com.example.veyu.data.remote.api.TicketApi
import com.example.veyu.data.remote.api.UserApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "http://192.168.1.37:8080/"

    // Hàm tạo Retrofit với interceptor (gửi token)
    fun provideRetrofitWithAuth(context: Context): Retrofit {
        val userPreferences = UserPreferences(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userPreferences))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Dùng để login (không cần token)
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    // Dùng cho các API khác (cần token)
    fun provideUserApi(context: Context): UserApi {
        return provideRetrofitWithAuth(context).create(UserApi::class.java)
    }

    // Lấy thông tin sân bay
    fun provideAirportApi(context: Context): AirportApi {
        return provideRetrofitWithAuth(context).create(AirportApi::class.java)
    }

    //Lấy thông tin chuến bay
    fun provideFlightApi(context: Context): FlightApi {
        return provideRetrofitWithAuth(context).create(FlightApi::class.java)
    }

    fun privideSeatFlightApi(context: Context): SeatFlightApi {
        return provideRetrofitWithAuth(context).create(SeatFlightApi::class.java)
    }

    fun providePassengerApi(context: Context): PassengerApi {
        return provideRetrofitWithAuth(context).create(PassengerApi::class.java)
    }

    fun provideBookingApi(context: Context): BookingApi {
        return provideRetrofitWithAuth(context).create(BookingApi::class.java)
    }

    fun provideTicketApi(context: Context): TicketApi {
        return provideRetrofitWithAuth(context).create(TicketApi::class.java)
    }

    fun providePaymentTransactionApi(context: Context): PaymentTransactionApi {
        return provideRetrofitWithAuth(context).create(PaymentTransactionApi::class.java)
    }

    fun providePaymentMethodApi(context: Context): PaymentMethodApi {
        return provideRetrofitWithAuth(context).create(PaymentMethodApi::class.java)
    }
}
