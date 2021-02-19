package General;

//.. Thread que simula la entidad de intermediario en la cadena.

//.. Acá hay dos tipos de entidades. 
	//.. El '0' que cubre buff productor a buff intermedio.
		// .. Este retira del buff productor y almacena en el intermedio. 
	//.. El '1' que cubre buff intermedio a buff consumidor.
		// .. Este retira del intermedio y almacena en el consumidor.

public class Intermediario extends Thread
{
	// .... Atributos
	
	private static BuzonProductores inicio;										// ... Todos los intermediario tipo 0 retiran sobre un mismo 'almacen'
	private static BuzonIntermedio medioCampo = new BuzonIntermedio(1);			// ... Todos los intermediario usan un mismo 'almacen' intermedio. NOTA: Por enunciado su capacidad es 1.
	private static BuzonConsumidores fin;										// ... Todos los intermediario tipo 1 almacenan sobre un mismo 'almacen'
	
	private int action;											// ... Se le define que tipo es para que haga ciertas acciones.
	private int id;												// ... Identificador para reconocerlo. 
	
	// .. Constructor: Todo se define como parametro, menos el buffer intermedio que comparten todos y tiene capacidad de 1.
	
	public Intermediario(int pAction, int pId, BuzonProductores deTodos, BuzonConsumidores pFin) {
		this.inicio = deTodos;
		this.fin = pFin;
		this.action = pAction;
		this.id = pId;
	}
	
	// .. Acciones a tomar dependiendo de su tipo de intermediario.
	// .. 0: Retira del buff de productores y guardar en el intermedio.
	// .. 1: Retira del buff intermedio y guarda en el consumidor.
	
	public void run() {
		if (this.action == 0) {
			Producto retirado = inicio.retirar(this.id);
			medioCampo.almacenar(retirado, this.id);
		}	
		else {
			Producto transportado = medioCampo.retirar(this.id);
			fin.almacenar(transportado, this.id);
		}
			
			
	}
	
}
