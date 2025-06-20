module est.ucab.jacafxproyecto {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens est.ucab.jacafxproyecto to javafx.fxml;
    exports est.ucab.jacafxproyecto;
}