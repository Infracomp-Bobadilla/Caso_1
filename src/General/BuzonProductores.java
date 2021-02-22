package General;

import java.util.ArrayList;

// ...... Este buz�n refleja donde los productores van a almacenar los productos, y como el intemediario va 
// ...... haciendo su respectivo transporte. 

public class BuzonProductores 
{
	// ... Atributos
	
	private ArrayList<Producto> buff;		// .. Buffer para guardar elementos de tipo producto
	private int nMax;						// .. Capacidad m�xima del buffer 
	Object lleno, vacio;					// .. Objetos con el fin de hacer una sincronizaci�n granulada 
	
	// .. Constructor
	
	public BuzonProductores(int pMax)  		// .. Recibe la capacidad m�xima 
	{
		this.buff = new ArrayList<Producto>();
		this.nMax = pMax;
		this.lleno = new Object();
		this.vacio = new Object();
	}
	
	// .... Amacenar un producto que viene del productor 
	
		// .. Se pone en exclusi�n el acceso a la capidad del buffer con el fin de almacenar. 
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
					System.out.println("Productor con id: " + idThread + " -->ALMACEN� un producto " + produc.getTipo() + ".  --> Ahora hay: " + buff.size() + " elementos en el 'buff' de productores.");
				}
			}
			if(continuar){
				System.out.println("Productor con con id: " + idThread + " -->est� en espera semi-activa para almacenar. --> Hay: " + buff.size() + " elementos en el 'buff' de productores.");
				Thread.yield();  // .. ESPERA SEMI ACTIVA
			}
		}

		//Cuando logre almacenar notifica a los intermediarios que est�n dormidos que sigan. 
		synchronized (vacio) {
			try {
				vacio.notify();
			}
			catch(Exception e) {}
		}
	}
	
	// .... Retirar un producto por medio de un intermediario
	
			// .. Se pone en exclusi�n el acceso a la capidad del buffer con el fin de retirar. 
			// .. Si se retira la ejecusi�n acaba (No s� notifica ya que los que almacenan no est�n dormidos sino en espera semi activa). 
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
					System.out.println("Intermediario PRODUCTOR con id: " + idThread + " --> REMOVI� el producto " + producto.getTipo() + ". --> Ahora hay: " + buff.size() + " elementos en el 'buff' de productores.");
				}
			}
			if(continuar) {
				synchronized (vacio) {
					try {
						System.out.println("Intermediario PRODUCTOR con id: " + idThread + " --> est� en espera pasiva para remover. --> Hay: " + buff.size() + "  elementos en el 'buff' de productores.");
						vacio.wait();
					}
					catch(Exception e) {}
				}
			}
		}

		return producto;
	}

}
