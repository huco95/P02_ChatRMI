package es.ubu.lsi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import es.ubu.lsi.client.ChatClient;
import es.ubu.lsi.common.ChatMessage;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listado de clientes conectados.
	 */
	private List<ChatClient> clientes;

	/**
	 * Numero de clientes.
	 */
	private int numClientes;

	/**
	 * Constructor del servidor.
	 */
	public ChatServerImpl() throws RemoteException {
		clientes = new ArrayList<ChatClient>();
		numClientes = 0;
	}

	/**
	 * @see ChatServer#checkIn(ChatClient)
	 */
	public int checkIn(ChatClient client) throws RemoteException {
		numClientes++;
		client.setId(numClientes);
		clientes.add(client);
		System.out.println("\nSe ha conectado un nuevo cliente:");
		System.out.println("\t- USERNAME: " + client.getNickName() + "\n");
		publish(new ChatMessage(numClientes, "El usuario " + client.getNickName() + " se ha conectado."));
		return numClientes;
	}

	/**
	 * @see ChatServer#logout(ChatClient)
	 */
	public void logout(ChatClient client) throws RemoteException {
		clientes.remove(client);
		publish(new ChatMessage(numClientes, "El usuario " + client.getNickName() + " se ha desconectado."));
	}

	/**
	 * @see ChatServer#privatemsg(String, ChatMessage)
	 */
	public void privatemsg(String tonickname, ChatMessage msg) throws RemoteException {
		for (ChatClient receptor : clientes) {
			if (receptor.getNickName().equals(tonickname)) {
				receptor.receive(msg);
				break;
			}
		}
	}

	/**
	 * @see ChatServer#publish(ChatMessage)
	 */
	public void publish(ChatMessage msg) throws RemoteException {
		for (ChatClient receptor : clientes) {
			if(receptor.getId() != msg.getId()){
				receptor.receive(msg);
			}
		}
	}

	/**
	 * @see ChatServer#shutdown(ChatClient)
	 */
	public void shutdown(ChatClient client) throws RemoteException {
		publish(new ChatMessage(numClientes, "Se ha ordenado el cierre del servidor.Cerrando..."));
		System.exit(0);
	}

}
