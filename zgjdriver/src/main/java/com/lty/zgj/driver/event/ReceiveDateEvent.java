package com.lty.zgj.driver.event;

/**
 * Created by Administrator on 2018/6/29.
 */

public class ReceiveDateEvent {

    private String itemId;
    private int tag;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public ReceiveDateEvent(String itemId, int tag) {
        this.itemId = itemId;
        this.tag = tag;
    }


}
