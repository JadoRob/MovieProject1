package org.udacity.android.movieproject1;

public class UserReviews {

    private String user;
    private String review;

    UserReviews(String user, String review) {
        this.user = user;
        this.review = review;
    }

    public String getAuthor() {
        return user;
    }

    public String getReview() {
        return review;
    }

}
