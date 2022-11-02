package dad.ahorcado;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import dad.ahorcado.puntuaciones.Puntuacion;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {
	
	private static final File PALABRAS_FILE = new File("data_files/palabras.txt");
	private static final File PUNTUACIONES_FILE = new File("data_files/puntuaciones.csv");

	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());
	
	public static Stage primaryStage;
	
	private RootController rootController;
	
	@Override
	public void init() throws Exception {
		System.out.println("inicializando...");

		// cargar las palabras desde fichero
		if (PALABRAS_FILE.exists()) {
			palabras.addAll(
				Files.readAllLines(
					PALABRAS_FILE.toPath(), 
					StandardCharsets.UTF_8
				)
			);
		}
		
		// cargar puntuaciones desde fichero
		if (PUNTUACIONES_FILE.exists()) {
			puntuaciones.addAll(
				Files.readAllLines(
					PUNTUACIONES_FILE.toPath(), 
					StandardCharsets.UTF_8
				)
				.stream()
				.map(total -> total.split(";"))
				.map(parts -> {
					String nombre = parts[0];
					int puntuacion = Integer.parseInt(parts[1]);
					return new Puntuacion(nombre, puntuacion);
				})
				.collect(Collectors.toList())
			);
		}
		
		rootController = new RootController();
		
		rootController.palabrasProperty().bind(palabras);
		rootController.puntuacionesProperty().bind(puntuaciones);
		
		rootController.cargarDatos();
		// una vez cargadas las palabras eligo la palabra para el juego
//		rootController.setPalabraElegida();
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
		System.out.println("terminando...");
		// guardar las palabras en un fichero
		final StringBuffer contenidoPalabras = new StringBuffer();
		rootController.getPalabras().forEach(palabra -> contenidoPalabras.append(palabra + "\n"));
		Files.writeString(
				PALABRAS_FILE.toPath(), 
				contenidoPalabras.toString().trim(), 
				StandardCharsets.UTF_8, 
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING
		);
		
		// guardar puntuaciones en un fichero
		String contenidoPuntuaciones = "";
		for(Puntuacion puntuacion:rootController.getPuntuaciones()) {
			contenidoPuntuaciones += puntuacion.toCsvString();
		}
		Files.writeString(
			PUNTUACIONES_FILE.toPath(),
			contenidoPuntuaciones.trim(),
			StandardCharsets.UTF_8,
			StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING
		);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}