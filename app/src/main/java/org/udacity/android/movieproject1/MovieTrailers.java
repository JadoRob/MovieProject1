package org.udacity.android.movieproject1;

import java.util.List;

public class MovieTrailers {

    private String trailerName;
    private String youtubeKey;

    MovieTrailers(String name, String key) {
        this.trailerName = name;
        this.youtubeKey = key;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getYoutubeLink() {
        return "https://www.youtube.com/watch?v=" + this.youtubeKey;
    }

}
