package General;

import java.util.ArrayList;

public class BuzonIntermedio 
{
	private ArrayList<String> buff;
	private int nMax;
	Object lleno, vacio;

	public BuzonIntermedio(int pMax) 
	{
		this.buff = new ArrayList<>();
		this.nMax = pMax;
		this.lleno = new Object();
		this.vacio = new Object();
	}

	public void almacenar (String producto, int idThread)
	{
		boolean continuar = true;

		while(continuar) {
			synchronized (this) {
				if(buff.size() < nMax) {
					buff.add(producto);
					continuar = false;
					System.out.println("Medio Campo --> " + idThread + " El producto " + producto + " fue añadido. Ahora hay: " + buff.size() + " elementos.");
				}
			}
			if(continuar){
				synchronized (lleno) {
					try {
						System.out.println("Medio Campo --> " + idThread + " El producto " + producto + " esta esperando (Entrando). Hay: " + buff.size() + " elementos.");
						lleno.wait();
					}
					catch(Exception e) {}
				}
			}
		}

		synchronized (vacio) {
			try {
				vacio.notify();
				System.out.println("Medio Campo --> " + idThread + " Notifica vacio");
			}
			catch(Exception e) {}
		}
	}

	public String retirar(int idThread)
	{
		boolean continuar = true;
		String producto = "";

		while(continuar) {
			synchronized (this) {
				if(buff.size() > 0) {
					producto = buff.remove(0);
					continuar = false;
					System.out.println("Medio Campo --> " + idThread + " El producto " + producto + " fue eliminado. Ahora hay: " + buff.size() + " elementos.");
				}
			}
			if(continuar) {
				synchronized (vacio) {
					try {
						vacio.wait();
						System.out.println("Medio Campo --> " + idThread + " Esta esperando para retirar. Hay: " + buff.size() + " elementos.");
					}
					catch(Exception e) {}
				}
			}
		}
		synchronized (lleno) {
			try {
				lleno.notify();
				System.out.println("Medio Campo --> " + idThread + " Notifica lleno");
			}
			catch(Exception e) {}
		}

		return producto;
	}


}
