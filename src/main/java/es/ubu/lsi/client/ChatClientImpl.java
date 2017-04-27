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
	 * @throws RemoteException
	 */
	public ChatClientImpl(String nickname) throws RemoteException{
		super();
		this.nickname = nickname;
	}
	
	/**
	 * @see ChatClient#getId()
	 */
	public int getId() throws RemoteException {
		return id;
	}

	/**
	 * @see ChatClient#setId(int)
	 */
	public void setId(int id) throws RemoteException {
		this.id = id;
	}

	/**
	 * @see ChatClient#receive(ChatMessage)
	 */
	public void receive(ChatMessage msg) throws RemoteException {
		System.out.println(msg.getNickname() + ">> " + msg.getMessage());
		System.out.print("> ");
	}

	/**
	 * @see ChatClient#getNickName()
	 */
	public String getNickName() throws RemoteException {
		return nickname;
	}

}
