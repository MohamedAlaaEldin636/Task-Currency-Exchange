package my.ym.taskcurrencyexchange.helperTypes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.ym.taskcurrencyexchange.models.BaseResponse
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

open class BaseRemoteDataSource {

    protected suspend fun <R : BaseResponse> safeApiCall(
        apiCall: suspend () -> R
    ): Result<R> = withContext(Dispatchers.IO) {
        try {
            val response = apiCall()

            if (response.success == true) {
                Result.success(response)
            }else {
                Timber.w("api invalid response -> $response")

                Result.failure(ApiException(
                    ApiException.Reason.API_ERROR, response.error?.let {
                        "Code ${it.code}, Type ${it.type}, Info -> ${it.info}"
                    }
                ))
            }
        }catch (throwable: Throwable) {
            Timber.w("api exception thrown -> $throwable")

            when (throwable) {
                is HttpException -> {
                    val reason = when (throwable.code()) {
                        in 400 until 500 -> ApiException.Reason.CLIENT_ERROR
                        in 500 until 600 -> ApiException.Reason.SERVER_ERROR
                        else -> ApiException.Reason.OTHER
                    }

                    Result.failure(ApiException(
                        reason, "Http Code ${throwable.code()}, Msg ${throwable.message()}"
                    ))
                }
                is UnknownHostException, is ConnectException, is SocketException -> {
                    Result.failure(ApiException(
                        ApiException.Reason.CONNECTION_ERROR,
                        "Poor Internet Connection or Server problem, Msg ${throwable.message}"
                    ))
                }
                else -> {
                    Result.failure(ApiException(
                        ApiException.Reason.OTHER, "Other causes, Msg ${throwable.message}"
                    ))
                }
            }
        }
    }

}
