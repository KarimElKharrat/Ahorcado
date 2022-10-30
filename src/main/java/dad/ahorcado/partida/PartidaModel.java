package dad.ahorcado.partida;

import java.util.Optional;

import dad.ahorcado.AhorcadoApp;
import dad.ahorcado.RootController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;

public class PartidaModel {

	private StringProperty intento = new SimpleStringProperty();
	private StringProperty letrasProbadas = new SimpleStringProperty();
	private StringProperty textoEscondido = new SimpleStringProperty();
	
	private IntegerProperty puntosGanados = new SimpleIntegerProperty();
	
	private BooleanProperty disableButtons = new SimpleBooleanProperty(false);
	private BooleanProperty gameOver = new SimpleBooleanProperty();
	
	private ObjectProperty<Image> imagen = new SimpleObjectProperty<Image>();
	
	private String nombre;
	private String palabraElegida = "";
	
	private int numFile;
	
	/**
	 * meto la palabra elegida en el modelo
	 */
	public void buildPalabraElegida() {
    	int i;
		for(i = 0; i < RootController.PALABRA_ELEGIDA.length()-1; i++) {
			addPalabraElegida(RootController.PALABRA_ELEGIDA.charAt(i) + " ");
		}
		addPalabraElegida(RootController.PALABRA_ELEGIDA.charAt(i) + "");
    }
	
	/**
	 * escondo la palabra y la guardo en el modelo
	 * @return palabra escondida
	 */
	protected String esconderPalabra() {
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
	
	/**
	 * @return cantidad de puntos máximos posibles
	 */
	private int puntosPosibles() {
    	int result = 0;
    	for(int i = 0; i < RootController.PALABRA_ELEGIDA.length(); i++) {
    		if(Character.isLetter(RootController.PALABRA_ELEGIDA.toUpperCase().charAt(i))) {
    			result++;
    		}
    	}
    	return result;
    }
	
	/**
	 * compruebo si la letra está en la palabra elegida o no
	 * @param intento
	 * @param pc (PartidaController)
	 */
	protected void comprobarLetra(char intento) {
		if(getPalabraElegida().contains(intento + "")) {
			String aux = "";
			for(int i = 0; i < getPalabraElegida().length(); i++) {
				if(getPalabraElegida().charAt(i) == intento) {
					aux += getPalabraElegida().charAt(i);
					incrementPuntosGanados(1);
				} else {
					aux += getTextoEscondido().charAt(i);
				}
			}
			setTextoEscondido(aux);
			if(getPalabraElegida().equals(getTextoEscondido()))
				win();
		}
		else
			fail();
		addLetrasProbadas(Character.toUpperCase(intento) + " ");
		setIntento("");
	}
	
	/**
	 * cuando falla modifica la imagen e incrementa los puntos perdidos
	 */
    protected void fail() {
    	if(getClass().getResource("/images/" + getNumFile() + ".png") != null)
    		setImagen(new Image(getClass().getResource("/images/" + getNumFile() + ".png").toString()));
    	
    	incrementNumFile();
    	if(getClass().getResource("/images/" + getNumFile() + ".png") == null) {
    		loose();
    	}
    	
    }
    
    /**
     * cuando pierde establece los puntos, muestra la palabra, salta una pantalla y te dirige a endGame() 
     */
    private void loose() {
    	
    	TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("DERROTA");
		dialog.setHeaderText("Has perdido, danos tu nombre: ");
		dialog.setContentText("Nombre:");
    	
    	Optional<String> nombre = dialog.showAndWait();
    	endGame(nombre);
    }
    
    /**
     * cuando gana establece los puntos, muestra la palabra, salta una pantalla y te dirige a endGame() 
     */
    protected void win() {
    	
    	setPuntosGanados(puntosPosibles());
    	setTextoEscondido(getPalabraElegida());
    	
    	TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("VICTORIA");
		dialog.setHeaderText("Has gando, danos tu nombre: ");
		dialog.setContentText("Nombre:");
		
		Optional<String> nombre = dialog.showAndWait();
    	endGame(nombre);
    }
    
    /**
     * cuando acaba la partida pregunto por el nombre del jugador (para meterlo en la pestaña puntuaciones)
     * @param nombre
     */
    private void endGame(Optional<String> nombre) {
    	if(nombre.isPresent() && !nombre.get().isBlank()) {
    		setNombre(nombre.get().trim());
	    	setGameOver(true);
    	}
    	setDisableButtons(true);
    }
	
	public final StringProperty intentoProperty() {
		return this.intento;
	}
	public final String getIntento() {
		return this.intentoProperty().get();
	}
	public final void setIntento(final String intento) {
		this.intentoProperty().set(intento);
	}
	
	public final StringProperty letrasProbadasProperty() {
		return this.letrasProbadas;
	}
	public final String getLetrasProbadas() {
		return this.letrasProbadasProperty().get();
	}
	public final void setLetrasProbadas(final String letrasProbadas) {
		this.letrasProbadasProperty().set(letrasProbadas);
	}
	public final void addLetrasProbadas(final String letrasProbadas) {
		letrasProbadasProperty().set(getLetrasProbadas() != null ? getLetrasProbadas() + letrasProbadas:letrasProbadas);
	}
	
	public final StringProperty textoEscondidoProperty() {
		return this.textoEscondido;
	}
	public final String getTextoEscondido() {
		return this.textoEscondidoProperty().get();
	}
	public final void setTextoEscondido(final String textoEscondido) {
		this.textoEscondidoProperty().set(textoEscondido);
	}
	
	public final IntegerProperty puntosGanadosProperty() {
		return this.puntosGanados;
	}
	public final int getPuntosGanados() {
		return this.puntosGanadosProperty().get();
	}
	public final void setPuntosGanados(final int puntosGanados) {
		this.puntosGanadosProperty().set(puntosGanados);
	}
	public final void incrementPuntosGanados(int puntos) {
		setPuntosGanados(getPuntosGanados() + puntos);
	}
	
	public final BooleanProperty disableButtonsProperty() {
		return this.disableButtons;
	}
	public final boolean isDisableButtons() {
		return this.disableButtonsProperty().get();
	}
	public final void setDisableButtons(final boolean disableButtons) {
		this.disableButtonsProperty().set(disableButtons);
	}
	
	public final BooleanProperty gameOverProperty() {
		return this.gameOver;
	}
	public final boolean isGameOver() {
		return this.gameOverProperty().get();
	}
	public final void setGameOver(final boolean ganado) {
		this.gameOverProperty().set(ganado);
	}
	
	public final ObjectProperty<Image> imagenProperty() {
		return this.imagen;
	}
	public final Image getImagen() {
		return this.imagenProperty().get();
	}
	public final void setImagen(final Image imagen) {
		this.imagenProperty().set(imagen);
	}
	
	public int getNumFile() {
		return numFile;
	}
	public void setNumFile(int numFile) {
		this.numFile = numFile;
	}
	public void incrementNumFile() {
		numFile++;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPalabraElegida() {
		return palabraElegida;
	}
	public void setPalabraElegida(String palabraElegida) {
		this.palabraElegida = palabraElegida;
	}
	public void addPalabraElegida(String s) {
		this.palabraElegida += s;
	}
		
}
