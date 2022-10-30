package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.ahorcado.RootController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.NumberStringConverter;

public class PartidaController implements Initializable {

	// model
	
	private PartidaModel model = new PartidaModel();
	
	// view
	
	@FXML
    private ImageView ahorcadoImage;

    @FXML
    private TextField intentoText;

    @FXML
    private Label letrasProbadasLabel, puntosGanadosLabel, textoEscondidoLabel;
    
    @FXML
    private Button resolverButton, letraButton;

    @FXML
    private BorderPane view;

	public PartidaController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// bindings
		
		resolverButton.disableProperty().bind(model.disableButtonsProperty());
		letraButton.disableProperty().bind(model.disableButtonsProperty());
		intentoText.disableProperty().bind(model.disableButtonsProperty());
		
		ahorcadoImage.imageProperty().bind(model.imagenProperty());
		
		model.intentoProperty().bindBidirectional(intentoText.textProperty());
		
		letrasProbadasLabel.textProperty().bind(model.letrasProbadasProperty());
		textoEscondidoLabel.textProperty().bind(model.textoEscondidoProperty());
		puntosGanadosLabel.textProperty().bindBidirectional(model.puntosGanadosProperty(), new NumberStringConverter());
		
	}
	
	@FXML
    void onLetraAction(ActionEvent event) {
		String intento = model.getIntento().trim().toUpperCase();
		if(!intento.isBlank()) {
			if(model.getLetrasProbadas() == null)
				model.comprobarLetra(intento.charAt(0));
			else if(!model.getLetrasProbadas().contains(intento.charAt(0) + ""))
				model.comprobarLetra(intento.charAt(0));
		}
    }

    @FXML
    void onResolverAction(ActionEvent event) {
    	if(!model.getIntento().isBlank()) {
			if(model.getIntento().trim().toUpperCase().equals(RootController.PALABRA_ELEGIDA))
				model.win();
			else
				model.fail();
		}
    }
    
    /**
     * me preparo para meter la siguiente imagen, escondo la palabra y la meto en el modelo
     */
    public void cargarDatos() {
    	model.setNumFile(2);
		model.setTextoEscondido(model.esconderPalabra());
		model.buildPalabraElegida();
    }
    
    // image
    
    public final ObjectProperty<Image> imagenProperty() {
		return this.model.imagenProperty();
	}
	public final Image getImagen() {
		return this.model.getImagen();
	}
	public final void setImagen(final Image imagen) {
		this.model.setImagen(imagen);
	}
	
	// game over
	
	public BooleanProperty gameOverProperty() {
		return model.gameOverProperty();
	}
	public Integer getPuntosGanados() {
		return model.getPuntosGanados();
	}
	public String getNombre() {
		return model.getNombre();
	}
	
    public BorderPane getView() {
		return view;
	}

}
