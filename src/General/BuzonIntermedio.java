package General;

import java.util.ArrayList;

// ...... Este buz�n refleja donde los intemediarios van a almacenar los productos, y como el intemediario va 
// ...... haciendo su respectivo transporte luego hasta los consumidores. 

public class BuzonIntermedio 
{
	// ... Attributos
	
	private ArrayList<Producto> buff;		// .. Buffer para guardar elementos de tipo producto
	private int nMax;						// .. Capacidad m�xima del buffer 
	Object lleno, vacio;					// .. Objetos con el fin de hacer una sincronizaci�n granulada 

	public BuzonIntermedio(int pMax) 		// .. Recibe la capacidad m�xima 
	{
		this.buff = new ArrayList<Producto>();
		this.nMax = pMax;
		this.lleno = new Object();
		this.vacio = new Object();
	}
	
	// .... Amacenar un producto que viene del intemediario que lo saco del buff consumidor 
	
		// .. Se pone en exclusi�n el acceso a la capidad del buffer con el fin de almacenar. 
		// .. Si se almacena se procede a notificar para que se pueda retirar. 
		// .. Si no fue posible almacenar se duerme.

	public void almacenar (Producto retirado, int idThread)
	{
		boolean continuar = true;

		while(continuar) {
			synchronized (this) {
				if(buff.size() < nMax) {
					buff.add(retirado);
					continuar = false;
					System.out.println("Intermediario MEDIO CAMPO con id: " + idThread + "--> ALMACEN� el producto " + retirado.getTipo() + ".  --> Ahora hay: " + buff.size() + " elementos en el 'buff' intermedio.");
				}
			}
			if(continuar){
				synchronized (lleno) {
					try {
						System.out.println("Intermediario MEDIO CAMPO con id: " + idThread + "--> est� en espera pasiva para almacenar. ------- Hay: " + buff.size() + "  elementos en el 'buff' intermedio.");
						lleno.wait();
					}
					catch(Exception e) {}
				}
			}
		}

		synchronized (vacio) {
			try {
				vacio.notify();
			}
			catch(Exception e) {}
		}
	}
	
	// .... Retirar un producto por medio de un intermediario para llevarlo al buff de consumidores
	
		// .. Se pone en exclusi�n el acceso a la capidad del buffer con el fin de retirar. 
		// .. Si se retira se procede a notificar para que se pueda almacenar. 
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
					System.out.println("Intermediario MEDIO CAMPO con id: " + idThread + " --> REMOVI� el producto " + producto.getTipo() + ". --> Ahora hay: " + buff.size() + " elementos en el 'buff' intermedio.");
				}
			}
			if(continuar) {
				synchronized (vacio) {
					try {
						System.out.println("Intermediario MEDIO CAMPO con id: " + idThread + " --> est� en espera pasiva para remover. --> Hay: " + buff.size() + "  elementos en el 'buff' intermedio.");
						vacio.wait();
					}
					catch(Exception e) {}
				}
			}
		}
		synchronized (lleno) {
			try {
				lleno.notify();
			}
			catch(Exception e) {}
		}

		return producto;
	}

}
