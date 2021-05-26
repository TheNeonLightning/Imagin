package com.app.imagin;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.imagin.server.SessionManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionManagerTest {

    Context mockContext;
    SharedPreferences mockPreferences;
    SharedPreferences.Editor mockEditor;

    @Before
    public void setUpContext() {
        mockContext = mock(Context.class);
        mockPreferences = mock(SharedPreferences.class);
        mockEditor = mock(SharedPreferences.Editor.class);
        when(mockContext.getSharedPreferences(mockContext.getString(R.string.app_name),
                Context.MODE_PRIVATE)).thenReturn(mockPreferences);
        when(mockPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString("user_token", "new_token")).thenReturn(mockEditor);
        when(mockPreferences.getString("user_token", null)).thenReturn("new_token");
    }

    @Test
    public void putStringTest() {
        SessionManager sessionManager = new SessionManager(mockContext);
        sessionManager.setAuthToken("new_token");
        verify(mockEditor).putString("user_token", "new_token");
        verify(mockEditor).apply();
    }

    @Test
    public void fetchStringTest() {
        SessionManager sessionManager = new SessionManager(mockContext);
        sessionManager.fetchAuthToken();
        verify(mockPreferences).getString("user_token", null);
    }
}
