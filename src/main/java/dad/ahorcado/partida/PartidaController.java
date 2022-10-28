package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.ahorcado.RootController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
	private ObjectProperty<Image> imagen = new SimpleObjectProperty<Image>();
	private String palabraElegida = "";

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
		
		ahorcadoImage.imageProperty().bind(imagen);
		
		model.intentoProperty().bindBidirectional(intentoText.textProperty());
		
		letrasProbadasLabel.textProperty().bind(model.letrasProbadasProperty());
		textoEscondidoLabel.textProperty().bind(model.textoEscondidoProperty());
		puntosGanadosLabel.textProperty().bindBidirectional(model.puntosGanadosProperty(), new NumberStringConverter());
		puntosPerdidosLabel.textProperty().bindBidirectional(model.puntosPerdidosProperty(), new NumberStringConverter());
		
		// load data
		
		model.setNumFile(2);
		model.setTextoEscondido(esconderPalabra());
		buildPalabraElegida();
		
	}
	
	@FXML
    void onLetraAction(ActionEvent event) {
		String intento = model.getIntento().trim().toUpperCase();
		if(!intento.isBlank()) {
			if(model.getLetrasProbadas() == null)
				comprobarLetra(intento.charAt(0));
			else if(!model.getLetrasProbadas().contains(intento.charAt(0) + ""))
				comprobarLetra(intento.charAt(0));
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
    
//    ------------------------- logica de negocio -------------------------------------
    private void buildPalabraElegida() {
    	int i;
		for(i = 0; i < RootController.PALABRA_ELEGIDA.length()-1; i++) {
			palabraElegida += RootController.PALABRA_ELEGIDA.charAt(i) + " ";
		}
		palabraElegida += RootController.PALABRA_ELEGIDA.charAt(i);
    }
    
    private String esconderPalabra() {
    	String result = "";
    	for(int i = 0; i < RootController.PALABRA_ELEGIDA.length()-1; i++) {
    		if(Character.isLetter(RootController.PALABRA_ELEGIDA.charAt(i))) {
    			result += "_ ";
    		} else if(Character.isSpaceChar(RootController.PALABRA_ELEGIDA.charAt(i))) {
    			result += "  ";
    		}
    	}
    	result += "_ ";
    	return result;
    }
    
    private void comprobarLetra(char intento) {
    	if(palabraElegida.contains(intento + "")) {
			String aux = "";
			for(int i = 0; i < palabraElegida.length(); i++) {
				if(palabraElegida.charAt(i) == intento) {
					aux += palabraElegida.charAt(i);
					model.incrementPuntosGanados(1);
				} else {
					aux += model.getTextoEscondido().charAt(i);
				}
			}
			model.setTextoEscondido(aux);
			if(palabraElegida.equals(model.getTextoEscondido()))
				win();
		}
		else
			fail();
		model.addLetrasProbadas(Character.toUpperCase(intento) + " ");
		model.setIntento("");
    }
    
    private void fail() {
    	if(getClass().getResource("/images/" + model.getNumFile() + ".png") != null)
    		imagen.set(new Image(getClass().getResource("/images/" + model.getNumFile() + ".png").toString()));
    	
    	model.incrementNumFile();
    	if(getClass().getResource("/images/" + model.getNumFile() + ".png") == null) {
    		loose();
    	}
    	
    	model.incrementPuntosPerdidos();
    }
    
    private void loose() {
    	endGame();
		// TODO popup que diga que has perdido pida tu nombre(ya le pongo yo la puntuacion)
    	System.out.println("has perdido y tal");
    }
    
    private void win() {
    	endGame();
    	model.setPuntosGanados(puntosPosibles());
    	model.setTextoEscondido(palabraElegida);
    	// TODO popup has ganado inserte su nombre(ya le pongo yo la puntuacion)
    	System.out.println("has ganado y tal");
    }
    
    private void endGame() {
    	model.setDisableButtons(true);
    }
    
    private int puntosPosibles() {
    	int result = 0;
    	for(int i = 0; i < RootController.PALABRA_ELEGIDA.length(); i++) {
    		if(Character.isLetter(RootController.PALABRA_ELEGIDA.toUpperCase().charAt(i))) {
    			result++;
    		}
    	}
    	return result;
    }
    
    // ------------------------- logica de negocio -------------------------------
    
    public final ObjectProperty<Image> imagenProperty() {
		return this.imagen;
	}
	public final Image getImagen() {
		return this.imagenProperty().get();
	}
	public final void setImagen(final Image imagen) {
		this.imagenProperty().set(imagen);
	}
	
    public BorderPane getView() {
		return view;
	}

}
