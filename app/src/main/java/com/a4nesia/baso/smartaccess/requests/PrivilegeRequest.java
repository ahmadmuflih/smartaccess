
package com.a4nesia.baso.smartaccess.requests;

import java.util.ArrayList;
import java.util.List;

import com.a4nesia.baso.smartaccess.models.Privilege;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivilegeRequest extends Request{


    @SerializedName("data")
    @Expose
    private ArrayList<Privilege> privileges = null;



    public ArrayList<Privilege> getPrivilege() {
        return privileges;
    }

    public void setPrivilege(ArrayList<Privilege> privileges) {
        this.privileges = privileges;
    }


}
