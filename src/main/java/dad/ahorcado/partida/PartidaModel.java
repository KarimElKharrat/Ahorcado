package dad.ahorcado.partida;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PartidaModel {

	private StringProperty intento = new SimpleStringProperty();
	private StringProperty letrasProbadas = new SimpleStringProperty();
	private StringProperty textoEscondido = new SimpleStringProperty();
	private IntegerProperty puntosGanados = new SimpleIntegerProperty();
	private IntegerProperty puntosPerdidos = new SimpleIntegerProperty();
	private BooleanProperty disableButtons = new SimpleBooleanProperty(false);
	private int numFile;
	
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
	
	public final IntegerProperty puntosPerdidosProperty() {
		return this.puntosPerdidos;
	}
	public final int getPuntosPerdidos() {
		return this.puntosPerdidosProperty().get();
	}
	public final void setPuntosPerdidos(final int puntosPerdidos) {
		this.puntosPerdidosProperty().set(puntosPerdidos);
	}
	public final void incrementPuntosPerdidos() {
		setPuntosPerdidos(getPuntosPerdidos() - 1);
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
	
	public int getNumFile() {
		return numFile;
	}
	public void setNumFile(int numFile) {
		this.numFile = numFile;
	}
	public void incrementNumFile() {
		numFile++;
	}
	
}
