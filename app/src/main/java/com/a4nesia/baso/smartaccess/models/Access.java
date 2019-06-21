
package com.a4nesia.baso.smartaccess.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Access {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("employee_id")
    @Expose
    private Integer employeeId;
    @SerializedName("room_id")
    @Expose
    private Integer roomId;
    @SerializedName("success_status")
    @Expose
    private Integer successStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("room")
    @Expose
    private Room room;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(Integer successStatus) {
        this.successStatus = successStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
