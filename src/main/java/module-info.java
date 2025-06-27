module est.ucab.jacafxproyecto {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;

    opens est.ucab.jacafxproyecto to javafx.fxml;
    exports est.ucab.jacafxproyecto;
    exports est.ucab.jacafxproyecto.controllers;
    opens est.ucab.jacafxproyecto.controllers to javafx.fxml;
}