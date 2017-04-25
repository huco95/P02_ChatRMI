package es.ubu.lsi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServerImpl;

public class ChatClientStarter {

	public ChatClientStarter(String[] args) {
		String nickname = null;
		String host = "localhost";
		ChatClientImpl client;
		ChatServerImpl server = null;

		if (args.length == 1) {
			nickname = args[0];
		} else if (args.length == 2) {
			nickname = args[0];
			host = args[1];
		} else {
			System.out.println("Introduce el nombre de usuario.");
			System.exit(0);
		}

		client = new ChatClientImpl(nickname);
		try {
			server = (ChatServerImpl) Naming.lookup("rmi://" + host + "/ChatServerImpl");
		} catch (MalformedURLException e) {
			System.err.println("Error al obtener la referencia remota del Servidor");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("Error al obtener la referencia remota del Servidor");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("Error al obtener la referencia remota del Servidor");
			e.printStackTrace();
		}
		
		try {
			server.checkIn(client);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("--------------------------------------------");
		System.out.println("               BIENVENIDO");
		System.out.println("--------------------------------------------");
		System.out.println("Logeado como \"" + nickname + "\"");
		System.out.println("Comandos para banear, unbanear y salir:");

		System.out.println("\t- BAN + \"username\"");
		System.out.println("\t- UNBAN + \"username\"");
		System.out.println("\t- LOGOUT");
		System.out.println("--------------------------------------------");

		boolean stop = false;
		String read;
		Scanner sc = new Scanner(System.in);
		while(!stop){
			System.out.print("> ");
			read = sc.nextLine();
			if (read.equals("logout")){
				try {
					server.logout(client);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sc.close();
				stop = true;
			}else{
				try {
					server.publish(new ChatMessage(client.getId(), read));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

	}

}
