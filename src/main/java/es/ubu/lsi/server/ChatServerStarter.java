package es.ubu.lsi.server;

import java.rmi.Naming;


public class ChatServerStarter {

	public ChatServerStarter(){
		try {
			ChatServerImpl server = new ChatServerImpl();
			Naming.rebind("/ChatServerImpl", server);
	        System.out.println("Servidor registrado...");
		} catch (Exception e) {
			System.err.println("Excepcion en arranque del servidor " + e.toString());
		}	
	}
}
