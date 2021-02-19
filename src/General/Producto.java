package General;

// ... Elemento general que se va a transportar en la cadena Productor-Consumidor.

public class Producto {
	
	// .. Atributo: Tipo del producto. Puede ser A o B
	
	private String tipo;
	
	// .. Constructor, recibe el tipo por parametro
	
	public Producto (String pTipo) {
		this.tipo = pTipo;
	}
	
	// .. Get: Devuelve el tipo de ese producto.
	
	public String getTipo() {
		return this.tipo;
	}
	
}
