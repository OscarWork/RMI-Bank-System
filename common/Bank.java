package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * interface för bankerna, listar alla metoder som måste finnas i BankImpl
 */
public interface Bank extends Remote {
	public String getName() throws RemoteException;
	
	public boolean logIn(String bank,int account,char[] pin) throws RemoteException;
	
	public boolean logOut(String bank,int account)throws RemoteException;
	
	public boolean withdraw (String bank, int account, int amount)throws RemoteException;
	
	public boolean deposit (String bank, int account,int amount)throws RemoteException;
	
	public int balance (String bank, int account)throws RemoteException;
	
	public boolean accountExists(int account)throws RemoteException;
	
	
}
