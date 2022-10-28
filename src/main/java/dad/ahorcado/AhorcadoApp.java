package dad.ahorcado;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {
	
	private static final File PALABRAS_FILE = new File("palabras.txt");

	public static Stage primaryStage;
	
	private RootController rootController = new RootController();
	
	@Override
	public void init() throws Exception {
		System.out.println("inicializando!!");
		// cargar las palabras desde fichero
		if (PALABRAS_FILE.exists()) {
			rootController.getPalabras().addAll(
					Files.readAllLines(
							PALABRAS_FILE.toPath(), 
							StandardCharsets.UTF_8
					)
			);
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		AhorcadoApp.primaryStage = primaryStage;
		
		primaryStage.setTitle("Ahorcado");
		primaryStage.setScene(new Scene(rootController.getView()));
		primaryStage.show();
		
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("terminando!!");
		// guardar las palabras en un fichero
		final StringBuffer contenido = new StringBuffer();
		rootController.getPalabras().forEach(palabra -> contenido.append(palabra + "\n"));
		Files.writeString(
				PALABRAS_FILE.toPath(), 
				contenido.toString().trim(), 
				StandardCharsets.UTF_8, 
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING
		);
	}

	public static void main(String[] args) {
		launch(args);
	}

}