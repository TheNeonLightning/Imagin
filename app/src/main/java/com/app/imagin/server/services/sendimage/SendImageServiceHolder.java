package com.app.imagin.server.services.sendimage;

import androidx.annotation.Nullable;

// Using service holder to break the cycle of dependency:
// TokenAuthenticator -> OkHttpClient instance -> service -> TokenAuthenticator
public class SendImageServiceHolder {
    private SendImageServiceBase64 service = null;

    @Nullable
    public SendImageServiceBase64 get() {
        return service;
    }

    public void set(SendImageServiceBase64 service) {
        this.service = service;
    }
}
