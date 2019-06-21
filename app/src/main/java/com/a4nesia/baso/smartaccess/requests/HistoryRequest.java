
package com.a4nesia.baso.smartaccess.requests;

import java.util.ArrayList;
import com.a4nesia.baso.smartaccess.models.Access;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryRequest extends Request {


    @SerializedName("data")
    @Expose
    private ArrayList<Access> accesses = null;



    public ArrayList<Access> getData() {
        return accesses;
    }

    public void setData(ArrayList<Access> accesses) {
        this.accesses = accesses;
    }

}
