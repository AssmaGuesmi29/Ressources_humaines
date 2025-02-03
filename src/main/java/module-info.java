module com.example.ressource_humaines {
    requires javafx

    .controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires layout;
    requires kernel;
    requires org.apache.pdfbox;
    opens com.example.ressource_humaines to javafx.fxml;  // This is fine if your main app needs reflection
    opens controller to javafx.fxml;  // THIS is the missing line you need
    opens Model to javafx.base;
    exports Model;
    exports com.example.ressource_humaines;
    exports controller;
}
