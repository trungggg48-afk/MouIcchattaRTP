package me.trungggg48.rtp.model;

public enum RTPType {
    OVERWORLD("overworld"),
    NETHER("nether"),
    END("end");

    private final String path;

    RTPType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
