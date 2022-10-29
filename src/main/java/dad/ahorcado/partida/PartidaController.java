package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
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
import javafx.scene.control.TextInputDialog;
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
    private Label letrasProbadasLabel, puntosGanadosLabel, puntosPerdidosLabel, textoEscondidoLabel;
    
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
		puntosPerdidosLabel.textProperty().bindBidirectional(model.puntosPerdidosProperty(), new NumberStringConverter());
		
		// load data
		
	}
	
	@FXML
    void onLetraAction(ActionEvent event) {
		String intento = model.getIntento().trim().toUpperCase();
		if(!intento.isBlank()) {
			if(model.getLetrasProbadas() == null)
				model.comprobarLetra(intento.charAt(0), this);
			else if(!model.getLetrasProbadas().contains(intento.charAt(0) + ""))
				model.comprobarLetra(intento.charAt(0), this);
		}
    }

    @FXML
    void onResolverAction(ActionEvent event) {
    	if(!model.getIntento().isBlank()) {
			if(model.getIntento().trim().toUpperCase().equals(RootController.PALABRA_ELEGIDA))
				win();
			else
				fail();
		}
    }
    
    public void cargarDatos() {
    	model.setNumFile(2);
		model.setTextoEscondido(model.esconderPalabra());
		model.buildPalabraElegida();
    }
//    ------------------------- logica de negocio -------------------------------------
    
//    private void comprobarLetra(char intento, PartidaController pc) {
//    	if(model.getPalabraElegida().contains(intento + "")) {
//			String aux = "";
//			for(int i = 0; i < model.getPalabraElegida().length(); i++) {
//				if(model.getPalabraElegida().charAt(i) == intento) {
//					aux += model.getPalabraElegida().charAt(i);
//					model.incrementPuntosGanados(1);
//				} else {
//					aux += model.getTextoEscondido().charAt(i);
//				}
//			}
//			model.setTextoEscondido(aux);
//			if(model.getPalabraElegida().equals(model.getTextoEscondido()))
//				win();
//		}
//		else
//			fail();
//		model.addLetrasProbadas(Character.toUpperCase(intento) + " ");
//		model.setIntento("");
//    }
    
    // ------------------------- logica de negocio -------------------------------
    
    private void fail() {
    	if(getClass().getResource("/images/" + model.getNumFile() + ".png") != null)
    		model.setImagen(new Image(getClass().getResource("/images/" + model.getNumFile() + ".png").toString()));
    	
    	model.incrementNumFile();
    	if(getClass().getResource("/images/" + model.getNumFile() + ".png") == null) {
    		loose();
    	}
    	
    	model.incrementPuntosPerdidos();
    }
    
    private void loose() {
    	
    	TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("DERROTA");
		dialog.setHeaderText("Has perdido, danos tu nombre: ");
		dialog.setContentText("Nombre:");
    	System.out.println("has perdido y tal");
    	
    	Optional<String> nombre = dialog.showAndWait();
    	endGame(nombre);
    }
    
    private void win() {
    	
    	model.setPuntosGanados(model.puntosPosibles());
    	model.setTextoEscondido(model.getPalabraElegida());
    	
    	TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("VICTORIA");
		dialog.setHeaderText("Has gando, danos tu nombre: ");
		dialog.setContentText("Nombre:");
		
		Optional<String> nombre = dialog.showAndWait();
    	
    	System.out.println("has ganado y tal");
    	endGame(nombre);
    }
    
    private void endGame(Optional<String> nombre) {
    	if(nombre.isPresent() && !nombre.get().isBlank()) {
    		model.setNombre(nombre.get().trim());
	    	model.setGameOver(true);
    	}
    	model.setDisableButtons(true);
    }
    
    protected void modelWin() {
    	win();
    }
    
    protected void modelFail() {
    	fail();
    }
    
    public final ObjectProperty<Image> imagenProperty() {
		return this.model.imagenProperty();
	}
	public final Image getImagen() {
		return this.model.getImagen();
	}
	public final void setImagen(final Image imagen) {
		this.model.setImagen(imagen);
	}
	
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
