package edu.ptit.vhlee.minimusic.data.local;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import edu.ptit.vhlee.minimusic.Constants;
import edu.ptit.vhlee.minimusic.data.model.Track;

public class MusicStorage {
    private static final String[] EXTENSIONS = {".mp3"};

    public static List<Track> loadTracks(String path) {
        final List<Track> tracks = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) tracks.addAll(tracks.size(), loadTracks(file.getPath()));
                for (String extension : EXTENSIONS) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        if (file.length() > (Constants.MIN_SIZE * Constants.SIZE_UNIT)) return true;
                    }
                }
                return false;
            }
        });
        for (File file : files) {
            tracks.add(new Track(file.getName(), 0, file.getPath()));
        }
        return tracks;
    }
}
