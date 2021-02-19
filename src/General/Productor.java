package General;

// .. Thread que simula la entidad de productores en la cadena.

public class Productor extends Thread
{
	// .... Atributos
	
	private static BuzonProductores inicio;					// ... Todos los productores trabajan sobre un mismo 'almacen'
	
	private int id;											// ... Id que identifica al productor
	private Producto produc;								// ... Producto que está almacenando
	private int MAX_PRODUCTOS;								// ... Máxima cantidad de productos que va a almacenar.
	
	// .. Constructor: Todo se define como parametro.
	
	public Productor(int pID, Producto pProduc, int max, BuzonProductores deTodos) 
	{
		this.inicio = deTodos;
		this.id = pID;
		this.produc = pProduc;
		this.MAX_PRODUCTOS = max;
	}
	
	// .. Almacena de uno a uno de sus productos hasta completar la cantidad máxima que puede sacar. 
	// .. Cada productor sabe que tipo de producto esta almacenando. 
	
	public void run() {
		for(int i = 0; i < this.MAX_PRODUCTOS; i++) {
			inicio.almacenar(this.produc, this.id);
		}
	}
	
}
