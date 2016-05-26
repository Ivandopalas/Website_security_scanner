package com.bsu.famcs.wscan.zap.model;

import org.zaproxy.clientapi.core.ApiResponseSet;

public class ScanInfo {
    private int progress;
    private int id;
    private State state;

    public enum State {
        FINISHED,
        PAUSED,
        RUNNING;

        public static State parse(String s) {
            if ("FINISHED".equalsIgnoreCase(s)) return FINISHED;
            if ("PAUSED".equalsIgnoreCase(s)) return PAUSED;
            if ("RUNNING".equalsIgnoreCase(s)) return RUNNING;
            throw new RuntimeException("Unknown state: "+s);
        }
    }

    public ScanInfo(ApiResponseSet response) {
        id = Integer.parseInt(response.getAttribute("id"));
        progress = Integer.parseInt(response.getAttribute("progress"));
        state = State.parse(response.getAttribute("state"));
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
