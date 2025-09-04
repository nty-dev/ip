package tsayyongbot.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** GUI for TsayYongBot */
public class Main extends Application {
    private Engine engine;

    @Override
    public void start(Stage stage) {
        engine = new Engine();

        // UI controls
        TextArea dialog = new TextArea();
        dialog.setEditable(false);
        dialog.setWrapText(true);
        dialog.appendText("Hello! I'm the Tsay Yong Bot\nWhat can I do for you?\n");

        TextField input = new TextField();
        input.setPromptText("Type a command…");
        Button send = new Button("Send");

        // Layout
        HBox bar = new HBox(8, input, send);
        bar.setPadding(new Insets(8));
        BorderPane root = new BorderPane(dialog, null, null, bar, null);
        BorderPane.setMargin(dialog, new Insets(8));

        // Events
        Runnable handle = () -> {
            String text = input.getText().trim();
            if (text.isEmpty())
                return;
            String reply = engine.reply(text);
            dialog.appendText("You: " + text + "\n");
            dialog.appendText(reply + "\n");
            input.clear();
            // Exit on bye
            if (engine.isExit()) {
                Platform.exit();
            }
        };
        send.setOnAction(e -> handle.run());
        input.setOnAction(e -> handle.run());

        // Scene & Stage
        Scene scene = new Scene(root, 420, 520);
        stage.setTitle("TsayYongBot");
        stage.setMinWidth(380);
        stage.setMinHeight(360);
        stage.setScene(scene);
        stage.show();

        // auto-scroll down as text grows
        dialog.textProperty().addListener((obs, o, n) -> dialog.setScrollTop(Double.MAX_VALUE));
    }
}
