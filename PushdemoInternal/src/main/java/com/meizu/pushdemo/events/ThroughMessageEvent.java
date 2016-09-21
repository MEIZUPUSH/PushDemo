package com.meizu.pushdemo.events;

import java.io.Serializable;

/**
 * Created by liaojinlong on 16-5-30.
 */
public class ThroughMessageEvent implements Serializable{
    public String message;

    public ThroughMessageEvent(String message) {
        this.message = message;
    }
}
