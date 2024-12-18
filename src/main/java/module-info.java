module com.example.chatting_room_projectfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
     requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;

    opens com.example.chatting_room_projectfx to javafx.fxml;
    exports com.example.chatting_room_projectfx;
}