package General;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("-------------------------------------------------------");
		System.out.println("..... CASO 1 - PRODCUTOR & CONSUMIDOR .....");
		System.out.println(".. Juan José Beltrán - Santiago Bobadilla ..");
		System.out.println("-------------------------------------------------------");
		
		// .. Atributos desde lectura
		Properties datos=new Properties();
		
		try {
			datos = cargar(new File("./src/General/PropertiesFile.PROPERTIES"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int numProduCons = Integer.parseInt(datos.getProperty("concurrencia.numProdCons"));
		int numProductos = Integer.parseInt(datos.getProperty("concurrencia.numProductos"));
		
		int capacidadBuffProduc =  Integer.parseInt(datos.getProperty("concurrencia.buzonesProd"));
		int capacidadBuffConsum = Integer.parseInt(datos.getProperty("concurrencia.buzonesCons"));
		
		System.out.println("..... La asignación por parametro fue:");
		System.out.println(".. La cantidad de productores y consumidores es: " + numProduCons);
		System.out.println(".. Cada uno procesa un total de " + numProductos + " productos.");
		System.out.println(".. La capacidad del 'buffer' de los productores es: " + capacidadBuffProduc);
		System.out.println(".. La capacidad del 'buffer' de los consumidores es: " + capacidadBuffConsum);
		System.out.println("-------------------------------------------------------");
		
		// .. Calculo de intermediarios necesarios. 
		
		int intermedio = numProduCons * numProductos * 2;
		
		System.out.println("..... La cantidad de productos que movilizan los intermediarios es: " + intermedio);
		System.out.println("-------------------------------------------------------");
		
		// .. Crear buzones extremos
		
		BuzonProductores inicioTodos = new BuzonProductores(capacidadBuffProduc);
		BuzonConsumidores finTodos = new BuzonConsumidores(capacidadBuffConsum);
		
		System.out.println("..... La convensión usada en los 'ID' es:");
		System.out.println(".. Los productores tienen un id cercano al 100.");
		System.out.println(".. Los intermediarios tienen un id cercano al 0.");
		System.out.println(".. Los consumidores tienen un id cercano al 500.");
		System.out.println("-------------------------------------------------------");
		System.out.println("..... SIMULACIÓN .... ");
		System.out.println("-------------------------------------------------------");
		
		// ................................................ FORMA 1 DE INICIALIZACIÓN 
		
		// ... Creamos productores
		
		for(int i = 0; i < numProduCons; i++) 
			if(i%2 == 0) {															// .. La mitad es A y la otra mitad es B
				Producto A = new Producto("A");
				new Productor(i+100, A, numProductos, inicioTodos).start();
			}
			else {
				Producto B = new Producto("B");
				new Productor(i+100, B, numProductos, inicioTodos).start();
			}
		
		// ... Creamos intermediarios
		
		for(int i = 0; i < intermedio; i++) 
			if(i%2==0)
				new Intermediario(0, i+1, inicioTodos, finTodos).start();
			else
				new Intermediario(1, i+1, inicioTodos, finTodos).start();
		
		// ... Creamos consumidores
		
		for(int i = 0; i < numProduCons; i++)
			if(i%2 == 0) {								// .. La mitad es A y la otra mitad es B
				Producto A = new Producto("A");
				new Consumidor(i+500, A, numProductos, finTodos).start();
			}
			else {
				Producto B = new Producto("B");
				new Consumidor(i+500, B, numProductos, finTodos).start();
			}
		
//		// ................................................ FORMA 2 DE INICIALIZACIÓN 
//		// .. Todos intercalados
//		
//		for(int i = 0; i < intermedio; i++) {
//			
//			// .. Creo un consumidor
//			if(i < numProduCons) {
//				if(i%2 == 0) {														// .. La mitad es A y la otra mitad es B
//					Producto A = new Producto("A");
//					new Productor(i+100, A, numProductos, inicioTodos).start();
//				}
//				else {
//					Producto B = new Producto("B");
//					new Productor(i+100, B, numProductos, inicioTodos).start();
//				}
//			}
//			
//			// .. Creo intermediario
//			if(i%2==0)
//				new Intermediario(0, i+1, inicioTodos, finTodos).start();
//			else
//				new Intermediario(1, i+1, inicioTodos, finTodos).start();
//			
//			// .. Creo consumidor
//			if( i < numProduCons) {
//				if(i%2 == 0) {															// .. La mitad es A y la otra mitad es B
//					Producto A = new Producto("A");
//					new Consumidor(i+500, A, numProductos, finTodos).start();
//				}
//				else {
//					Producto B = new Producto("B");
//					new Consumidor(i+500, B, numProductos, finTodos).start();
//				}
//			}
//			
//		}
		
	}
	
	
	public static Properties cargar( File pRuta ) throws Exception
	{
		Properties datos = new Properties( );
		FileInputStream in = new FileInputStream( pRuta );
		try
		{
			datos.load( in );
			in.close( );

		}
		catch( Exception e )
		{
			throw new Exception( "Error al cargar el archivo: archivo no válido." );
		}
		
		return datos;
	}
	
	
	
	
	

}
