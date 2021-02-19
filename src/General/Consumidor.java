package General;

//.. Thread que simula la entidad de consumidor en la cadena.

public class Consumidor extends Thread
{
	// .... Atributos
	
	private static BuzonConsumidores fin;					// ... Todos los consumidores retiran sobre un mismo 'almacen'
	
	private int id;											// ... Id que identifica al productor
	private Producto produc;								// ... Producto que está almacenando
	private int MAX_PRODUCTOS;								// ... Máxima cantidad de productos que va a almacenar.
	
	// .. Constructor: Todo se define como parametro.
	
	public Consumidor(int pID, Producto pProduc, int max, BuzonConsumidores deTodos) {
		this.fin = deTodos;
		this.id = pID;
		this.produc = pProduc;
		this.MAX_PRODUCTOS = max;
	}
	
	// .. Retira de uno a uno de sus productos hasta completar la cantidad máxima que puede sacar. 
	// .. Cada consumidor sabe el tipo de producto que busca. 
	
	public void run() {
		for(int i = 0; i < this.MAX_PRODUCTOS; i++) {
			fin.retirar(this.id, this.produc);
		}
	}
}
