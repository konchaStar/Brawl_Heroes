module com.example.brawlheroes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.example.brawlheroes to javafx.fxml;
    exports com.example.brawlheroes;
}