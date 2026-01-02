import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Application class that starts the electorial college simulation
 * <p>
 *     A class that extends Application.  It starts up the FXML and JavaFX style
 *     application, with the help of SceneBuilder.  It mainly loads it and
 *     sets up the scene to be visible.
 * </p>
 *
 * @author augward
 */
public class ElectoralCollegeApplication extends Application {
    /**
     * Starting the application with scene
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception In case application errors are found
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ElectoralCollegeView.fxml")));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
