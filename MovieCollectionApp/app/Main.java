/**
 * Team Members - 
 * Kaur, Rajpreet, ID: 991676169
 * Kaur, Jasmeet, ID: 991713588
 * Singh, Sukhmanmeet, ID: 991714713
*/
package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class for the Movie Flix application.
 */
public class Main extends Application {

    private ArrayList<Movies> movieList = new ArrayList<Movies>();
    private String dataFile = "./movies.txt";
    private TextField titleField;
    private TextField genreField;
    private TextField actorsField;
    private TextField releaseYearField;
    private TextField durationField;
    private TextField ratingField;
    private TextField watchedField;

    private ComboBox<String> movieComboBox; 

    Button view;

    public void start(Stage primaryStage) {

        // main border pane
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(15, 15, 15, 15));

        ImageView logoImg = new ImageView(new Image("image/movie.png"));
        logoImg.setFitHeight(130);
        logoImg.setFitWidth(160);

        // Elements
        Label heading = new Label("Movie Flix");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label titleLbl = new Label("Title:");
        titleField = new TextField();

        Label genreLbl = new Label("Genre:");
        genreField = new TextField();

        Label actorsLbl = new Label("Actors:");
        actorsField = new TextField();

        Label releaseYearLbl = new Label("Release Year:");
        releaseYearField = new TextField();

        Label durationLbl = new Label("Duration (mins):");
        durationField = new TextField();

        Label ratingLbl = new Label("Rating:");
        ratingField = new TextField();

        Label watchedLbl = new Label("Watched (Yes/No):");
        watchedField = new TextField();

        Button add = new Button("Add");
        add.setOnAction(e -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String actors = actorsField.getText().trim();
            String releaseYearText = releaseYearField.getText().trim();
            String durationText = durationField.getText().trim();
            String ratingText = ratingField.getText().trim();
            String watchedText = watchedField.getText().trim();

            if (title.isEmpty() || genre.isEmpty() || actors.isEmpty()) {
                showErrorAlert("Error", "Title, genre, and actors fields cannot be empty.");
                return;
            }

            int releaseYear = 0;
            int duration = 0;
            double rating = 0.0;

            try {
                releaseYear = Integer.parseInt(releaseYearText);
                duration = Integer.parseInt(durationText);
                rating = Double.parseDouble(ratingText);
            } catch (NumberFormatException ex) {
                showErrorAlert("Error", "Invalid numeric value for release year, duration, or rating.");
                return;
            }

            if (releaseYear < 1800 || releaseYear > 2100) {
                showErrorAlert("Error", "Invalid release year. It should be between 1800 and 2100.");
                return;
            }

            if (duration <= 0) {
                showErrorAlert("Error", "Invalid duration. It should be a positive value.");
                return;
            }

            if (rating < 0 || rating > 10) {
                showErrorAlert("Error", "Invalid rating. It should be between 0 and 10.");
                return;
            }

            if (!watchedText.equalsIgnoreCase("Yes") && !watchedText.equalsIgnoreCase("No")) {
                showErrorAlert("Error", "Watched field should be either 'Yes' or 'No'.");
                return;
            }

            boolean watched = watchedText.equalsIgnoreCase("Yes");

            movieList.add(new Movies(title, genre, actors, releaseYear, duration, rating, watched));
            saveMoviesToFile();
            clearFields();
            showInfoAlert("Adding Data", "The movie has been added.");
        });

        view = new Button("View");
        view.setOnAction(e -> {
            viewMovies();
        });

        Button clear = new Button("Clear");
        clear.setOnAction(e -> clearFields());

        // declaring rest of panes
        GridPane pane1 = new GridPane();
        GridPane pane2 = new GridPane();
        GridPane pane3 = new GridPane();

        // top pane
        pane.setTop(pane1);
        pane1.setHgap(40);
        pane1.setVgap(40);
        pane1.setStyle("-fx-border-color: blue");
        pane1.add(logoImg, 0, 0);
        pane1.add(heading, 1, 0);

        // centre pane
        pane.setCenter(pane2);
        pane2.setHgap(20);
        pane2.setVgap(20);
        pane2.setStyle("-fx-border-color: blue");
        pane2.add(titleLbl, 1, 1);
        pane2.add(titleField, 2, 1);
        pane2.add(genreLbl, 1, 2);
        pane2.add(genreField, 2, 2);
        pane2.add(actorsLbl, 1, 3);
        pane2.add(actorsField, 2, 3);
        pane2.add(releaseYearLbl, 1, 4);
        pane2.add(releaseYearField, 2, 4);
        pane2.add(durationLbl, 1, 5);
        pane2.add(durationField, 2, 5);
        pane2.add(ratingLbl, 1, 6);
        pane2.add(ratingField, 2, 6);
        pane2.add(watchedLbl, 1, 7);
        pane2.add(watchedField, 2, 7);

        // bottom pane
        pane.setBottom(pane3);
        pane3.setHgap(20);
        pane3.setVgap(20);
        pane3.setStyle("-fx-border-color: blue");
        pane3.add(add, 1, 0);
        pane3.add(view, 2, 0);
        pane3.add(clear, 3, 0);
        pane3.setPadding(new Insets(15, 15, 15, 15));

        Scene scene = new Scene(pane, 550, 600);
        primaryStage.setTitle("Movie Flix");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveMoviesToFile() {
        File f = new File(dataFile);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter clearWriter = new PrintWriter(f);
            clearWriter.close();
    
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
            for (Movies movie : movieList) {
                String formattedString = String.format("%s:%s:%s:%d:%d:%.1f:%b\n", movie.getTitle(),
                        movie.getGenre(), movie.getActors(), movie.getRelease_year(),
                        movie.getDuration(), movie.getRating(), movie.isWatched());
                pw.write(formattedString);
            }
            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    
    private void clearFields() {
        titleField.setText("");
        genreField.setText("");
        actorsField.setText("");
        releaseYearField.setText("");
        durationField.setText("");
        ratingField.setText("");
        watchedField.setText("");
    }

    private void showInfoAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void viewMovies() {
        Stage stage2 = new Stage();
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setAlignment(Pos.CENTER);

        HBox searchChoice = new HBox();
        searchChoice.setSpacing(10);
        searchChoice.setPadding(new Insets(10, 10, 10, 10));
        searchChoice.setAlignment(Pos.CENTER);

        Label choiceLbl = new Label("Search for: ");
        ComboBox<String> choiceCb = new ComboBox<String>();
        choiceCb.getItems().add("Title");
        choiceCb.getItems().add("Genre");
        choiceCb.getItems().add("Actors");
        choiceCb.getItems().add("Release Year");

        choiceCb.getSelectionModel().selectFirst();

        searchChoice.getChildren().addAll(choiceLbl, choiceCb);

        HBox searchBox = new HBox();
        searchBox.setSpacing(30);
        searchBox.setPadding(new Insets(20, 20, 20, 20));
        searchBox.setAlignment(Pos.CENTER);
        Label l1 = new Label("Enter Value");
        searchBox.getChildren().add(l1);

        TextField searchTxt = new TextField();
        Button searchBtn = new Button("Search");

        searchBox.getChildren().addAll(searchTxt, searchBtn);

        movieComboBox = new ComboBox<String>();
        movieComboBox.getItems().add("Select a Movie");
        try {
            Scanner sc = new Scanner(new FileReader(dataFile));
            while (sc.hasNext()) {
                String[] tokens = sc.nextLine().split(":");
                movieComboBox.getItems().add(tokens[0]);
                movieList.add(new Movies(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]),
                        Integer.parseInt(tokens[4]), Double.parseDouble(tokens[5]), Boolean.parseBoolean(tokens[6])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Button to view details on a new screen
        Button viewDetailsBtn = new Button("View Details");
        viewDetailsBtn.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null && !selectedMovie.equals("Select a Movie")) {
                viewMovieDetails(selectedMovie);
            } else {
                showErrorAlert("Error", "Please select a movie to view details.");
            }
        });

        searchBtn.setOnAction(e -> {
            movieComboBox.getItems().clear();
            String searchInput = searchTxt.getText().trim().toLowerCase();
            boolean found = false;
            try {
                Scanner sc = new Scanner(new FileReader(dataFile));
                while (sc.hasNext()) {
                    String[] tokens = sc.nextLine().split(":");
                    String title = tokens[0].toLowerCase();
                    String genre = tokens[1].toLowerCase();
                    String actors = tokens[2].toLowerCase();
                    String releaseYear = tokens[3];

                    if (searchInput.isEmpty() ||
                            title.contains(searchInput) ||
                            genre.contains(searchInput) ||
                            actors.contains(searchInput) ||
                            releaseYear.equals(searchInput)) {
                        found = true;
                        movieComboBox.getItems().add(tokens[0]);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (!found) {
                movieComboBox.getItems().add("Sorry! No result found.");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            stage2.close();
        });

        // New: Buttons for editing and deleting movies
        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(20);
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        buttonsBox.getChildren().addAll(editBtn, deleteBtn);

        editBtn.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null && !selectedMovie.equals("Select a Movie")) {
                editMovie(selectedMovie);
            }
        });

        deleteBtn.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null && !selectedMovie.equals("Select a Movie")) {
                deleteMovie(selectedMovie);
            }
        });

        vbox.getChildren().addAll(searchChoice, searchBox, movieComboBox, buttonsBox, viewDetailsBtn, backBtn);

        Scene scene = new Scene(vbox, 750, 600);
        stage2.setTitle("Movie Flix - View Movies");
        stage2.setScene(scene);
        stage2.show();
    }

    private void viewMovieDetails(String selectedMovie) {
        for (Movies movie : movieList) {
            if (movie.getTitle().equalsIgnoreCase(selectedMovie)) {
                Stage stage = new Stage();
                stage.setTitle("Movie Details - " + movie.getTitle());

                VBox vbox = new VBox(10);
                vbox.setPadding(new Insets(20));
                vbox.setAlignment(Pos.CENTER);

                Label titleLabel = new Label("Title: " + movie.getTitle());
                Label genreLabel = new Label("Genre: " + movie.getGenre());
                Label actorsLabel = new Label("Actors: " + movie.getActors());
                Label releaseYearLabel = new Label("Release Year: " + movie.getRelease_year());
                Label durationLabel = new Label("Duration (mins): " + movie.getDuration());
                Label ratingLabel = new Label("Rating: " + movie.getRating());
                Label watchedLabel = new Label("Watched: " + (movie.isWatched() ? "Yes" : "No"));

                vbox.getChildren().addAll(titleLabel, genreLabel, actorsLabel, releaseYearLabel,
                        durationLabel, ratingLabel, watchedLabel);

                Scene scene = new Scene(vbox, 300, 200);
                stage.setScene(scene);
                stage.show();
                return;
            }
        }
    }

    private void editMovie(String movieTitle) {
        for (Movies movie : movieList) {
            if (movie.getTitle().equalsIgnoreCase(movieTitle)) {
                Stage editStage = new Stage();
                editStage.setTitle("Edit Movie: " + movieTitle);
                GridPane editGrid = new GridPane();
                editGrid.setAlignment(Pos.CENTER);
                editGrid.setHgap(10);
                editGrid.setVgap(10);
                editGrid.setPadding(new Insets(20));
    
                TextField editTitleField = new TextField(movie.getTitle());
                TextField editGenreField = new TextField(movie.getGenre());
                TextField editActorsField = new TextField(movie.getActors());
                TextField editReleaseYearField = new TextField(String.valueOf(movie.getRelease_year()));
                TextField editDurationField = new TextField(String.valueOf(movie.getDuration()));
                TextField editRatingField = new TextField(String.valueOf(movie.getRating()));
                TextField editWatchedField = new TextField(movie.isWatched() ? "Yes" : "No");
    
                Button saveBtn = new Button("Save");
                saveBtn.setOnAction(e -> {
                    String title = editTitleField.getText().trim();
                    String genre = editGenreField.getText().trim();
                    String actors = editActorsField.getText().trim();
                    String releaseYearText = editReleaseYearField.getText().trim();
                    String durationText = editDurationField.getText().trim();
                    String ratingText = editRatingField.getText().trim();
                    String watchedText = editWatchedField.getText().trim();
    
                    if (title.isEmpty() || genre.isEmpty() || actors.isEmpty()) {
                        showErrorAlert("Error", "Title, genre, and actors fields cannot be empty.");
                        return;
                    }
    
                    int releaseYear = 0;
                    int duration = 0;
                    double rating = 0.0;
    
                    try {
                        releaseYear = Integer.parseInt(releaseYearText);
                        duration = Integer.parseInt(durationText);
                        rating = Double.parseDouble(ratingText);
                    } catch (NumberFormatException ex) {
                        showErrorAlert("Error", "Invalid numeric value for release year, duration, or rating.");
                        return;
                    }
    
                    if (releaseYear < 1800 || releaseYear > 2100) {
                        showErrorAlert("Error", "Invalid release year. It should be between 1800 and 2100.");
                        return;
                    }
    
                    if (duration <= 0) {
                        showErrorAlert("Error", "Invalid duration. It should be a positive value.");
                        return;
                    }
    
                    if (rating < 0 || rating > 10) {
                        showErrorAlert("Error", "Invalid rating. It should be between 0 and 10.");
                        return;
                    }
    
                    if (!watchedText.equalsIgnoreCase("Yes") && !watchedText.equalsIgnoreCase("No")) {
                        showErrorAlert("Error", "Watched field should be either 'Yes' or 'No'.");
                        return;
                    }
    
                    boolean watched = watchedText.equalsIgnoreCase("Yes");
    
                    movie.setTitle(title);
                    movie.setGenre(genre);
                    movie.setActors(actors);
                    movie.setRelease_year(releaseYear);
                    movie.setDuration(duration);
                    movie.setRating(rating);
                    movie.setWatched(watched);
    
                    saveMoviesToFile();
                    showInfoAlert("Success", "The movie has been edited.");
                    editStage.close();
                    viewMovies();
                });
    
                Button cancelBtn = new Button("Cancel");
                cancelBtn.setOnAction(e -> {editStage.close(); viewMovies();});
    
                editGrid.add(new Label("Title:"), 0, 0);
                editGrid.add(editTitleField, 1, 0);
                editGrid.add(new Label("Genre:"), 0, 1);
                editGrid.add(editGenreField, 1, 1);
                editGrid.add(new Label("Actors:"), 0, 2);
                editGrid.add(editActorsField, 1, 2);
                editGrid.add(new Label("Release Year:"), 0, 3);
                editGrid.add(editReleaseYearField, 1, 3);
                editGrid.add(new Label("Duration (mins):"), 0, 4);
                editGrid.add(editDurationField, 1, 4);
                editGrid.add(new Label("Rating:"), 0, 5);
                editGrid.add(editRatingField, 1, 5);
                editGrid.add(new Label("Watched (Yes/No):"), 0, 6);
                editGrid.add(editWatchedField, 1, 6);
                editGrid.add(saveBtn, 0, 7);
                editGrid.add(cancelBtn, 1, 7);
    
                Scene editScene = new Scene(editGrid, 400, 300);
                editStage.setScene(editScene);
                editStage.show();
                return;
            }
        }
    }
    

    private void deleteMovie(String movieTitle) {
        for (Movies movie : movieList) {
            if (movie.getTitle().equalsIgnoreCase(movieTitle)) {
                movieList.remove(movie);
                saveMoviesToFile();
                showInfoAlert("Success", "The movie has been deleted.");
                viewMovies();
                return; 
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
