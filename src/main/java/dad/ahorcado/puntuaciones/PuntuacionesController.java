package dad.ahorcado.puntuaciones;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class PuntuacionesController implements Initializable {

	// model
	
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Puntuacion> puntuacionesSorted = new SimpleListProperty<>(puntuaciones.sorted());
	
	// view
	
	@FXML
	private ListView<Puntuacion> puntuacionesList;

    @FXML
    private VBox view;
	
	public PuntuacionesController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PuntuacionesView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		puntuacionesList.itemsProperty().bind(puntuacionesSorted);
		
	}
	
	public void addPuntuacion(String nombre, int puntuacion) {
		System.out.println(nombre + ":" + puntuacion);
		puntuaciones.add(new Puntuacion(nombre, puntuacion));
	}
	
	public VBox getView() {
		return view;
	}

	public final ListProperty<Puntuacion> puntuacionesProperty() {
		return this.puntuaciones;
	}
	public final ObservableList<Puntuacion> getPuntuaciones() {
		return this.puntuacionesProperty().get();
	}
	public final void setPuntuaciones(final ObservableList<Puntuacion> puntuaciones) {
		this.puntuacionesProperty().set(puntuaciones);
	}

}
