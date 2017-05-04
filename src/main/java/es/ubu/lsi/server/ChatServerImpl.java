package es.ubu.lsi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	 * Informacion de los clientes baneados.
	 */
	private HashMap<Integer, HashSet<String>> baneados;

	/**
	 * Constructor del servidor.
	 */
	public ChatServerImpl() throws RemoteException {
		super();
		clientes = new ArrayList<ChatClient>();
		baneados = new HashMap<Integer, HashSet<String>>();
		numClientes = 0;
	}

	/**
	 * @see ChatServer#checkIn(ChatClient)
	 */
	public int checkIn(ChatClient client) throws RemoteException {
		numClientes++;
		client.setId(numClientes);
		clientes.add(client);
		publish(new ChatMessage(numClientes, "admin", "El usuario \"" + client.getNickName() + "\" se ha conectado."));
		return numClientes;
	}

	/**
	 * @see ChatServer#logout(ChatClient)
	 */
	public void logout(ChatClient client) throws RemoteException {
		clientes.remove(client);
		publish(new ChatMessage(numClientes,"admin", "El usuario \"" + client.getNickName() + "\" se ha desconectado."));
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
		if (msg.getNickname().equals("admin")) {
			System.out.println("### " + msg.getMessage());
		} else {
			System.out.println("--- " + msg.getNickname() + " envió: " + msg.getMessage());
		}

		for (ChatClient receptor : clientes) {
			if (!(receptor.getId() == msg.getId() || (baneados.containsKey(receptor.getId())
					&& baneados.get(receptor.getId()).contains(msg.getNickname())))) {
				receptor.receive(msg);
			}
		}
	}

	/**
	 * @see ChatServer#ban(ChatMessage)
	 */
	public void ban(ChatMessage msg) throws RemoteException {
		if (baneados.containsKey(msg.getId())) {
			HashSet<String> users = new HashSet<String>();
			users = baneados.get(msg.getId());
			users.add(msg.getMessage());
			baneados.put(msg.getId(), users);
			System.out.println("### " + msg.getNickname() + " baneó a " + msg.getMessage());
			privatemsg(msg.getNickname(), new ChatMessage(0, "admin",
					"El usuario \"" + msg.getMessage() + "\" ha sido añadido a la lista de baneados."));
		} else {
			HashSet<String> users = new HashSet<String>();
			users.add(msg.getMessage());
			baneados.put(msg.getId(), users);
			System.out.println("### " + msg.getNickname() + " baneó a " + msg.getMessage());
			privatemsg(msg.getNickname(), new ChatMessage(0, "admin",
					"El usuario \"" + msg.getMessage() + "\" ha sido añadido a la lista de baneados."));
		}
	}

	/**
	 * @see ChatServer#unban(ChatMessage)
	 */
	public void unban(ChatMessage msg) throws RemoteException {
		if (baneados.containsKey(msg.getId())) {
			HashSet<String> users = new HashSet<String>();
			users = baneados.get(msg.getId());
			if (users.remove(msg.getMessage())) {
				baneados.put(msg.getId(), users);
				System.out.println(
						"### " + msg.getNickname() + " eliminó a " + msg.getMessage() + " de la lista de baneados.");
				privatemsg(msg.getNickname(), new ChatMessage(0, "admin",
						"El usuario \"" + msg.getMessage() + "\" ha sido eliminado de la lista de baneados."));
			} else {
				privatemsg(msg.getNickname(), new ChatMessage(0, "admin", "El usuario indicado no existe."));
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
