package com.pingao.model;

import java.util.List;


/**
 * Created by pingao on 2018/7/12.
 */
public class WebSocketMsg {
    private String command;
    private String path;
    private List<MarkDownUnit> units;

    public WebSocketMsg(String command, String path, List<MarkDownUnit> units) {
        this.command = command;
        this.path = path;
        this.units = units;
    }

    @Override
    public String toString() {
        return "WebSocketMsg[command=" + command +
            ", path=" + path +
            ", units=" + units.size();
    }
}
