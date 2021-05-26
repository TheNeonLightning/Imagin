package com.app.imagin.server.services.sendimage;

import android.content.Context;
import com.app.imagin.server.SessionManager;
import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

// If some request fails with 401 Unauthorized this Authenticator will attempt to refresh token.
// In case token refreshing was successful method authenticate(...) will return new built request
// with the fresh token added in headers.
public class TokenAuthenticator implements Authenticator {

    // Using service holder to break the cycle of dependency:
    // TokenAuthenticator -> OkHttpClient instance -> service -> TokenAuthenticator
    private SendImageServiceHolder serviceHolder;
    private SessionManager sessionManager;

    public TokenAuthenticator(Context context, SendImageServiceHolder serviceHolder) {
        this.serviceHolder = serviceHolder;
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (serviceHolder == null) {
            return null;
        }

        String oldToken = sessionManager.fetchAuthToken();
        retrofit2.Response refreshResponse = serviceHolder.get().refreshToken(oldToken).execute();

        if (refreshResponse != null) {
            String newToken = (String) refreshResponse.body();
            sessionManager.setAuthToken(newToken);
            return response.request().newBuilder()
                    .header("Authorization", newToken)
                    .build();
        }
        return null;
    }
}