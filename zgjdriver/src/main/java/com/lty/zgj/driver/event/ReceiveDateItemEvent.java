package com.lty.zgj.driver.event;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ReceiveDateItemEvent {


    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public ReceiveDateItemEvent(String itemId) {
        this.itemId = itemId;
    }
}
