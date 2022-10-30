package dad.ahorcado.palabras;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class PalabrasController implements Initializable {

	// model

	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<String> selected = new SimpleObjectProperty<>();
	
	// view

	@FXML
	private BorderPane view;

	@FXML
	private Button nuevoButton;

	@FXML
	private ListView<String> palabrasList;

	@FXML
	private Button quitarButton;

	public PalabrasController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PalabrasView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings

		palabrasList.itemsProperty().bind(palabras);
		selected.bind(palabrasList.getSelectionModel().selectedItemProperty());
		quitarButton.disableProperty().bind(selected.isNull());

	}

	public BorderPane getView() {
		return view;
	}

	@FXML
	void onNuevoAction(ActionEvent event) {

		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("Añadir palabra");
		dialog.setHeaderText("Introduzca una nueva palabra");
		dialog.setContentText("Palabra:");
		
		Optional<String> palabra = dialog.showAndWait();
		if(palabra.isPresent() && !palabra.get().isBlank() && !palabras.contains(palabra.get().trim())) {
			palabras.add(palabra.get().trim().toUpperCase());
		}

	}

	@FXML
	void onQuitarAction(ActionEvent event) {
		
		palabras.remove(selected.get());
		
	}
	
	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}

	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}

	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}

}