package dad.ahorcado;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.ahorcado.palabras.PalabrasController;
import dad.ahorcado.partida.PartidaController;
import dad.ahorcado.puntuaciones.Puntuacion;
import dad.ahorcado.puntuaciones.PuntuacionesController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;

public class RootController implements Initializable {
	
	// controllers
	
	private PalabrasController palabrasController = new PalabrasController();
	private PuntuacionesController puntuacionesController = new PuntuacionesController();
	private PartidaController partidaController = new PartidaController();

	// model
	
	public static String PALABRA_ELEGIDA;
	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<Image> imagen = new SimpleObjectProperty<Image>();
	
	// view
	
	@FXML
	private TabPane view;
	
	@FXML 
	private Tab partidaTab, palabrasTab, puntuacionesTab;
	
	public RootController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// tab content
		
		palabrasTab.setContent(palabrasController.getView());
		partidaTab.setContent(partidaController.getView());
		puntuacionesTab.setContent(puntuacionesController.getView());
		
		// bindings
		
		palabrasController.palabrasProperty().bind(palabras);
		puntuacionesController.puntuacionesProperty().bind(puntuaciones);
		partidaController.imagenProperty().bindBidirectional(imagen);
		
		// listeners
		
		partidaController.gameOverProperty().addListener((o, ov, nv) -> {
			if(nv == true) {
				String nombre = partidaController.getNombre();
				int puntuacion = partidaController.getPuntosGanados();
				puntuacionesController.addPuntuacion(nombre, puntuacion);
			}
		});
	}
	
	/**
	 * elige la palabra para comenzar el juego
	 */
	public void setPalabraElegida() {
		int random =(int) (Math.random() * palabras.getSize());
		RootController.PALABRA_ELEGIDA = palabras.get(random);
		partidaController.cargarDatos();
	}
	
	// palabras
	
	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}
	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}
	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}

	
	// puntuaciones
	
	public final ListProperty<Puntuacion> puntuacionesProperty() {
		return this.puntuaciones;
	}
	public final ObservableList<Puntuacion> getPuntuaciones() {
		return this.puntuacionesProperty().get();
	}
	public final void setPuntuaciones(final ObservableList<Puntuacion> puntuaciones) {
		this.puntuacionesProperty().set(puntuaciones);
	}

	// partida
	
	public final ObjectProperty<Image> imagenProperty() {
		return this.imagen;
	}
	public final Image getImagen() {
		return this.imagenProperty().get();
	}
	public final void setImagen(final Image imagen) {
		this.imagenProperty().set(imagen);
	}
	
	public TabPane getView() {
		return view;
	}

}