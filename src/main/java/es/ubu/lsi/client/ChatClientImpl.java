package es.ubu.lsi.client;

import java.rmi.RemoteException;

import es.ubu.lsi.common.ChatMessage;

public class ChatClientImpl implements ChatClient {
	
	private String nickname;
	private int id;

	public ChatClientImpl(String nickname){
		this.nickname = nickname;
	}
	
	public int getId() throws RemoteException {
		return id;
	}

	public void setId(int id) throws RemoteException {
		this.id = id;
	}

	public void receive(ChatMessage msg) throws RemoteException {
		System.out.println(msg.getNickname() + ">> " + msg.getMessage());
		System.out.print("> ");
	}

	public String getNickName() throws RemoteException {
		return nickname;
	}

}
