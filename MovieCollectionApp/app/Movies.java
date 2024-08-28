/**
 * Team Members - 
 * Kaur, Rajpreet, ID: 991676169
 * Kaur, Jasmeet, ID: 991713588
 * Singh, Sukhmanmeet, ID: 991714713
*/
package app;


public class Movies {
    private String title;
    private String genre;
    private String actors;
    private int release_year;
    private int duration;
    private double rating;
    private boolean watched;

    public Movies(String title, String genre, String actors, int release_year,
                  int duration, double rating, boolean watched) {
        this.title = title;
        this.genre = genre;
        this.actors = actors;
        this.release_year = release_year;
        this.duration = duration;
        this.rating = rating;
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Genre: " + genre +
               ", Actors: " + actors + ", Release Year: " + release_year +
               ", Duration: " + duration + " mins, Rating: " + rating +
               ", Watched: " + (watched ? "Yes" : "No");
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public int getRelease_year() {
        return release_year;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public boolean isWatched() {
        return watched;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}

