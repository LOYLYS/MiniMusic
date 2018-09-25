package edu.ptit.vhlee.minimusic.data.model;

public class Track {
    private String mName;
    private int mResourceId;
    private String mPath;

    public Track(String name, int resourceId, String path) {
        mName = name;
        mResourceId = resourceId;
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public void setResourceId(int resourceId) {
        mResourceId = resourceId;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }
}
