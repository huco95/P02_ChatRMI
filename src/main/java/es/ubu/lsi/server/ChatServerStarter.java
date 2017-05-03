package es.ubu.lsi.server;

import java.rmi.Naming;


public class ChatServerStarter {

	public ChatServerStarter(){
		try {
			Naming.rebind("/ChatServerImpl", new ChatServerImpl());
	        System.out.println("Servidor registrado...");
		} catch (Exception e) {
			System.err.println("Excepcion en arranque del servidor " + e.toString());
		}	
	}
}
