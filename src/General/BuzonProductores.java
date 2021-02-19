package General;

import java.util.ArrayList;

// ...... Este buzón refleja donde los productores van a almacenar los productos, y como el intemediario va 
// ...... haciendo su respectivo transporte. 

public class BuzonProductores 
{
	// ... Attributos
	
	private ArrayList<Producto> buff;		// .. Buffer para guardar elementos de tipo producto
	private int nMax;						// .. Capacidad máxima del buffer 
	Object lleno, vacio;					// .. Objetos con el fin de hacer una sincronización granulada 
	
	// .. Constructor
	
	public BuzonProductores(int pMax)  		// .. Recibe la capacidad máxima 
	{
		this.buff = new ArrayList<Producto>();
		this.nMax = pMax;
		this.lleno = new Object();
		this.vacio = new Object();
	}
	
	// .... Amacenar un producto que viene del productor 
	
		// .. Se pone en exclusión el acceso a la capidad del buffer con el fin de almacenar. 
		// .. Si se almacena se procede a notificar para que se pueda retirar. 
		// .. Si no fue posible almacenar queda en espera semi activa. 

	public void almacenar (Producto produc, int idThread)
	{		
		boolean continuar = true;
		while(continuar) {
			
			synchronized (this) {
				if(buff.size() < nMax) {
					buff.add(produc);
					continuar = false;
					System.out.println("Productor --> El thread con id " + idThread + " ALMACENO el producto " + produc.getTipo() + ".  ------- Ahora hay: " + buff.size() + " elementos en el 'buff' de productores.");
				}
			}
			if(continuar){
				System.out.println("Productor --> El thread con id " + idThread + " esta en espera semi-activa para almacenar. ------- Hay: " + buff.size() + " elementos en el 'buff' de productores.");
				Thread.yield();  // .. ESPERA SEMI ACTIVA
			}
		}

		synchronized (vacio) {
			try {
				vacio.notify();
			}
			catch(Exception e) {}
		}
	}
	
	// .... Retirar un producto por medio de un intermediario
	
			// .. Se pone en exclusión el acceso a la capidad del buffer con el fin de retirar. 
			// .. Si se retira la ejecusión acaba (No sé notifica ya que los que almacenan no están dormidos sino en espera semi activa). 
			// .. Si no fue posible almacenar el intermediario se duerme. 

	public Producto retirar(int idThread)
	{
		boolean continuar = true;
		Producto producto = null;

		while(continuar) {
			synchronized (this) {
				if(buff.size() > 0) {
					producto = buff.remove(0);
					continuar = false;
					System.out.println("Intermediario (PRODUCTOR) --> El thread con id " + idThread + " REMOVIO el producto " + producto.getTipo() + ". ------- Ahora hay: " + buff.size() + " elementos en el 'buff' de productores.");
				}
			}
			if(continuar) {
				synchronized (vacio) {
					try {
						System.out.println("Intermediario (PRODUCTOR) --> El thread con id " + idThread + " esta en espera pasiva para remover. ------- Hay: " + buff.size() + "  elementos en el 'buff' de productores.");
						vacio.wait();
					}
					catch(Exception e) {}
				}
			}
		}
		
//		synchronized (lleno) {
//			try {
//				lleno.notify();
//				System.out.println("Productores --> " + idThread + " Notifica lleno");
//			}
//			catch(Exception e) {}
//		}

		return producto;
	}

}
