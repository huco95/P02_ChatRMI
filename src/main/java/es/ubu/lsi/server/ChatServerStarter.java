package es.ubu.lsi.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;

public class ChatServerStarter {

	public ChatServerStarter(){
		try {
		      // Crea e instala el gestor de seguridad
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			Properties p = System.getProperties();
			// lee el codebase
			String url = p.getProperty("java.rmi.server.codebase");
	        // Cargador de clases dinámico ...
			Class<?> serverClass = RMIClassLoader.loadClass(url,
					"es.ubu.lsi.server.ChatServerImpl");
			Naming.rebind("/ChatServerImpl", (Remote) serverClass.newInstance());
	        System.out.println("Servidor registrado...");
		} catch (Exception e) {
			System.err.println("Excepcion en arranque del servidor " + e.toString());
		}	
	}
}
