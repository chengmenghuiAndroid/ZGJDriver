package com.lty.zgj.driver.event;

/**
 * Created by Administrator on 2018/6/29.
 */

public class MessageEvent {

    private String itemId;

    public MessageEvent(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}

