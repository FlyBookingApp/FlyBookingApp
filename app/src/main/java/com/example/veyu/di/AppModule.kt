package com.example.veyu.di

import android.content.Context
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.remote.api.AirportApi
import com.example.veyu.data.remote.api.AuthApi
import com.example.veyu.data.remote.api.FlightApi
import com.example.veyu.data.remote.api.SeatFlightApi
import com.example.veyu.data.repository.AirportRepository
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.data.repository.SeatFlightRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.data.remote.api.BookingApi
import com.example.veyu.data.remote.api.PassengerApi
import com.example.veyu.data.remote.api.PaymentMethodApi
import com.example.veyu.data.remote.api.PaymentTransactionApi
import com.example.veyu.data.remote.api.TicketApi
import com.example.veyu.data.repository.AuthRepository
import com.example.veyu.data.repository.BookingRepository
import com.example.veyu.data.repository.PassengerRepository
import com.example.veyu.data.repository.PaymentMethodRepository
import com.example.veyu.data.repository.PaymentTrasactionRepository
import com.example.veyu.data.repository.TicketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Auth
    @Provides
    fun provideAuthApi(): AuthApi {
        return NetworkModule.authApi
    }

    @Provides
    fun provideAuthRepository(api: AuthApi): AuthRepository = AuthRepository(api)

    // Airport
    @Provides
    fun provideAirportRepository(api: AirportApi): AirportRepository = AirportRepository(api)

    @Provides
    fun provideAirportApi(@ApplicationContext context: Context): AirportApi {
        return NetworkModule.provideAirportApi(context)
    }

    // Flight
    @Provides
    fun provideFlightRepository(api: FlightApi): FlightRepository = FlightRepository(api)

    @Provides
    fun provideFlightApi(@ApplicationContext context: Context): FlightApi {
        return NetworkModule.provideFlightApi(context)
    }

    // User Preferences
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository {
        return UserRepository(context)
    }

    // Seat Flight
    @Provides
    fun provideSeatFlightRepository(api: SeatFlightApi): SeatFlightRepository =
        SeatFlightRepository(api)

    @Provides
    fun provideSeatFlightApi(@ApplicationContext context: Context): SeatFlightApi {
        return NetworkModule.privideSeatFlightApi(context)
    }

    // Booking
    @Provides
    fun provideBookingRepository(api: BookingApi): BookingRepository = BookingRepository(api)

    @Provides
    fun provideBookingApi(@ApplicationContext context: Context): BookingApi {
        return NetworkModule.provideBookingApi(context)
    }

    // Payment Method
    @Provides
    fun providePaymentMethodRepository(api: PaymentMethodApi): PaymentMethodRepository = PaymentMethodRepository(api)

    @Provides
    fun providePaymentMethodApi(@ApplicationContext context: Context): PaymentMethodApi {
        return NetworkModule.providePaymentMethodApi(context)
    }

    // Payment
    @Provides
    fun providePaymentTrasactionRepository(api: PaymentTransactionApi): PaymentTrasactionRepository = PaymentTrasactionRepository(api)

    @Provides
    fun proviPaymentTrasactionApi(@ApplicationContext context: Context): PaymentTransactionApi {
        return NetworkModule.providePaymentTransactionApi(context)
    }

    // Ticket
    @Provides
    fun provideTicketRepository(api: TicketApi): TicketRepository = TicketRepository(api)

    @Provides
    fun provideTicketApi(@ApplicationContext context: Context): TicketApi {
        return NetworkModule.provideTicketApi(context)
    }

    // Passenger
    @Provides
    fun providePassengerRepository(api: PassengerApi): PassengerRepository = PassengerRepository(api)

    @Provides
    fun providePassengerApi(@ApplicationContext context: Context): PassengerApi {
        return NetworkModule.providePassengerApi(context)
    }
}