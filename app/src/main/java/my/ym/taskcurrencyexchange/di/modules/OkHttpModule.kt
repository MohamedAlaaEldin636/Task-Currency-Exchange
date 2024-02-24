package my.ym.taskcurrencyexchange.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import my.ym.taskcurrencyexchange.BuildConfig
import my.ym.taskcurrencyexchange.di.qualifiers.HeadersInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object OkHttpModule {

    private const val TIMEOUT_IN_SEC = 10L

    private const val HEADER_KEY_API_KEY = "apikey"

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @HeadersInterceptor headersInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)

            addInterceptor(headersInterceptor)

            addNetworkInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @HeadersInterceptor
    @Provides
    fun provideHeadersInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val url = request.url.toString()

            val builder = request.newBuilder()

            // using the if statement in case having several sources(apis) and each of which hav
            // their own api keys or to be more generic have their own headers isa.
            if (url.startsWith(BuildConfig.BASE_URL)) {
                builder.addHeader("Accept", "application/json")

                builder.addHeader(HEADER_KEY_API_KEY, BuildConfig.API_KEY)
            }

            chain.proceed(builder.build())
        }
    }

    /**
     * - Used for debugging.
     */
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.d(message)
        }.also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

}
