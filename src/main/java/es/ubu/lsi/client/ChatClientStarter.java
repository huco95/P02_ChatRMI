package es.ubu.lsi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServerImpl;

/**
 * Inicia el proceso de exportación del cliente remoto y de la resolución del
 * servidor de chat remoto. Recibe las entradas de texto e invoca los metodos
 * remotos del servidor.
 * 
 * @author Felix Nogal
 * @author Mario Santamaria
 *
 */
public class ChatClientStarter {

	public ChatClientStarter(String[] args) {

		String nickname = null;
		String host = "localhost";
		ChatClientImpl client = null;
		ChatServerImpl server = null;
		Scanner sc = new Scanner(System.in);
				
		if (args.length == 0) {
			System.out.print("Introduce tu nombre de usuario: ");
			nickname = sc.nextLine();
			System.out.println();			
		} else if (args.length == 1) {
			// Si solo se introduce el nombre de usuario.
			nickname = args[0];
		} else if (args.length == 2) {
			// Si se introduce el nombre de usuario y el host.
			nickname = args[0];
			host = args[1];
		} else {
			// Si se introducen 0 o mas de 2 argumentos se cierra el cliente.
			System.out.println("Debes introducir el nombre de usuario y opcionalmente el nombre del host.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
		}

		// Creamos un nuevo cliente con el nombre de usuario.
		try {
			client = new ChatClientImpl(nickname);
		} catch (RemoteException e) {
			System.out.println("Se ha producido un error al iniciar el cliente.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
		}

		// Obtenemos el objeto remoto del servidor.
		try {
			server = (ChatServerImpl) Naming.lookup("rmi://" + host + "/ChatServerImpl");
		} catch (MalformedURLException e) {
			System.out.println("Se ha producido un error al iniciar el servidor.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
		} catch (RemoteException e) {
			System.out.println("Se ha producido un error al iniciar el servidor.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
		} catch (NotBoundException e) {
			System.out.println("Se ha producido un error al iniciar el servidor.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
		}

		// Enlazamos el cliente y el servidor.
		try {
			server.checkIn(client);
		} catch (RemoteException e) {
			System.out.println("Se produjo un error al iniciar el cliente.");
			System.out.println("Saliendo del cliente.");
			System.exit(0);
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
		
		sc = new Scanner(System.in);
		String read;
		boolean stop = false;

		while (!stop) {
			
			System.out.print("> ");
			read = sc.nextLine();
			
			if (read.toLowerCase().equals("logout")) {
				try {
					stop = true;
					server.logout(client);
				} catch (RemoteException e) {
					System.out.println("Se ha producido un error al intentar desconectar el cliente.");
				}
			} else {
				try {
					server.publish(new ChatMessage(client.getId(), read));
				} catch (RemoteException e) {
					System.out.println("Se ha producido un error al intentar enviar el mensaje.");
				}
			}
		}
		
		sc.close();
		System.out.println("Cliente desconectado.");
	}
}
