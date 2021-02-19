package General;

import java.util.ArrayList;

//...... Este buzón refleja donde los intemediarion van a almacenar los productos, y como el consumidor va 
//...... haciendo su respectivo retiro. 

public class BuzonConsumidores 
{
	// ... Attributos
	
	private ArrayList<Producto> buff;					// .. Buffer para guardar elementos de tipo producto
	private int nMax;									// .. Capacidad máxima del buffer 
	Object lleno, vacio;								// .. Objetos con el fin de hacer una sincronización granulada 

	public BuzonConsumidores(int pMax) 					// .. Recibe la capacidad máxima
	{
		this.buff = new ArrayList<Producto>();
		this.nMax = pMax;
		this.lleno = new Object();
		this.vacio = new Object();
	}
	
	// .... Amacenar un producto que viene del 'buff' intermedio hasta este de consumidores 
	
		// .. Se pone en exclusión el acceso a la capidad del buffer con el fin de almacenar. 
		// .. Si se almacena se acaba la ejecusión. No se necesita notificar porque los consumidores no están dormidos sino en exclusión mutua.  
		// .. Si no fue posible almacenar queda dormido. 

	public void almacenar (Producto transportado, int idThread)
	{
		boolean continuar = true;

		while(continuar) {
			synchronized (this) {
				if(buff.size() < nMax) {
					buff.add(transportado);
					continuar = false;
					System.out.println("Intermediario (CONSUMIDOR) --> El thread con id " + idThread + " ALMACENO el producto " + transportado.getTipo() + ".  ------- Ahora hay: " + buff.size() + " elementos en el 'buff' de consumidores.");
				}
			}
			if(continuar){
				synchronized (lleno) {
					try {
						System.out.println("Intermediario (CONSUMIDOR) --> El thread con id " + idThread + " esta en espera pasiva para almacenar. ------- Hay: " + buff.size() + "  elementos en el 'buff' de consumidores.");
						lleno.wait();
					}
					catch(Exception e) {}
				}
			}
		}

//		synchronized (vacio) {
//			try {
//				vacio.notify();
//				System.out.println("Consumidor --> " + idThread + " Notifica vacio");
//			}
//			catch(Exception e) {}
//		}
	}
	
	// .... Retirar un producto por medio del consumidor.
	
		// .. Se pone en exclusión el acceso a la capidad del buffer con el fin de retirar. 
		// .. Si se retira y se procede a notificar que ya se puede almacenar. 
		// .. Si no fue posible almacenar el consumidor entra en espera sema-activa. 
	

	public Producto retirar(int idThread, Producto buscado)
	{
		boolean continuar = true;
		Producto producto = null;

		while(continuar) {
			synchronized (this) {
				if(buff.size() > 0) {

					// .....Modificación --> OJO
					// .. Si se va a retirar, busca el producto que le sirve y no retira uno cualquiera. 				
					int conti = 0;
					while(conti < buff.size() && continuar) 
					{
						if(buff.get(conti).getTipo().equals(buscado.getTipo())) {
							producto = buff.remove(conti);
							continuar = false;
							System.out.println("Consumidor --> El thread con id " + idThread + " REMOVIO el producto " + producto.getTipo() + ". ------- Ahora hay: " + buff.size() + " elementos en el 'buff' de consumidores.");
						}
						conti++;
					}
				}
			}
			if(continuar) {
				System.out.println("Consumidor --> El thread con id " + idThread + " esta en espera semi-activa para retirar. ------- Hay: " + buff.size() + " elementos en el 'buff' de productores.");
				Thread.yield();  		// .. ESPERA SEMI ACTIVA
			}
		}
		synchronized (lleno) {
			try {
				lleno.notify();
				//.. System.out.println("Consumidor --> " + idThread + " Notifica lleno");
			}
			catch(Exception e) {}
		}

		return producto;
	}


}
