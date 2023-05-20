module com.example.brawlheroes {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.brawlheroes to javafx.fxml;
    exports com.example.brawlheroes;
}