package es.ubu.lsi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.ubu.lsi.common.ChatMessage;

/**
 * Implementacion del ChatClient.
 * 
 * @author Felix Nogal
 * @author Mario Santamaria
 *
 */
public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
	
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Nombre de usuario.
	 */
	private String nickname;
	/**
	 * Id del usuario.
	 */
	private int id;

	/**
	 * Constructor de ChatClient.
	 * 
	 * @param nickname el nombre de usuario
	 * @throws RemoteException if remote communication has problems
	 */
	public ChatClientImpl(String nickname) throws RemoteException {
		super();
		this.nickname = nickname;
	}
	
	/**
	 * @see ChatClient#getId()
	 * @throws RemoteException if remote communication has problems
	 */
	public int getId() throws RemoteException {
		return id;
	}

	/**
	 * @see ChatClient#setId(int)
	 * @throws RemoteException if remote communication has problems
	 */
	public void setId(int id) throws RemoteException {
		this.id = id;
	}

	/**
	 * @see ChatClient#receive(ChatMessage)
	 * @throws RemoteException if remote communication has problems
	 */
	public void receive(ChatMessage msg) throws RemoteException {
		System.out.println(msg.getNickname() + ">> " + msg.getMessage());
		System.out.print("> ");
	}

	/**
	 * @see ChatClient#getNickName()
	 * @throws RemoteException if remote communication has problems
	 */
	public String getNickName() throws RemoteException {
		return nickname;
	}

}
