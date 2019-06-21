
package com.a4nesia.baso.smartaccess.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.a4nesia.baso.smartaccess.models.User;

public class LoginRequest extends Request {

    @SerializedName("data")
    @Expose
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
