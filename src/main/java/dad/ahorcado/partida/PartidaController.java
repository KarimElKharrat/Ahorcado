package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
		textoEscondidoLabel.textProperty().bind(model.palabraElegidaEscondidaProperty());
		puntosGanadosLabel.textProperty().bindBidirectional(model.puntosGanadosProperty(), new NumberStringConverter());
//		
//		model.setPalabraElegidaBasica();
//		model.cargarDatos();
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
		model.setIntento("");
    }

    @FXML
    void onResolverAction(ActionEvent event) {
    	if(!model.getIntento().isBlank()) {
			if(model.getIntento().trim().toUpperCase().equals(model.getPalabraElegidaBasica()))
				model.win();
			else
				model.fail();
		}
    	model.setIntento("");
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
	
	// cargar datos
	
	public void cargarDatos() {
		model.cargarDatos();
		model.setNumFile(2);
	}
	
	// palabras list
	
	public final ListProperty<String> palabrasProperty() {
		return model.palabrasProperty();
	}
	
	// view
	
    public BorderPane getView() {
		return view;
	}
    
}
