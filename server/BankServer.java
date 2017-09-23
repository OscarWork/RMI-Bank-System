package server;
/*
 * skapar konton, banker och registret, och  registrerar alla banker 
 */
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import common.Bank;
import common.Names;

public class BankServer {

	public static void main (String[] args){
		// skapa registry, skapa banker och lägg in dom i registryt
		int port = 1099; // porten som registret ska finnas på, 1099 är standard porten.
		Registry reg = null;
		try {
			//skapar ett nytt registry som alla banker senare läggs in i, detta så att klienten kommer åt bankerna.
			 reg = LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Names nameList = new Names();
		Vector<Bank> banks = new Vector<Bank>();
		Map<Integer,Account> shbAccounts = new LinkedHashMap<Integer, Account>(8);
		Map<Integer,Account> nordeaAccounts = new LinkedHashMap<Integer, Account>(8);
		Map<Integer,Account> swedbankAccounts = new LinkedHashMap<Integer, Account>(8);
		
		/*
		 * dessa konton finns hos varje bank
		 * acc1 konto 1234,pin 0000,balance 1000kr
		 * acc2 konto:9876,pin 1111,balance 200kr
		 * acc 3 konto 4567,pin 2222,balance 0kr
		 */
		char[] pin1 = {'0','0','0','0'};
		char[] pin2 = {'1','1','1','1'};
		char[] pin3 = {'2','2','2','2'};
		
		//skapa shbkonton
		Account shbacc1 = new Account(1234,pin1,1000);
		Account shbacc2 = new Account(9876,pin2,200);
		Account shbacc3 = new Account(4567,pin3,0);
		//skapa nordeakonton
		Account nordeaacc1 = new Account(1234,pin1,1000);
		Account nordeaacc2 = new Account(9876,pin2,200);
		Account nordeaacc3 = new Account(4567,pin3,0);
		// skapa swedbank
		Account swedbankacc1 = new Account(1234,pin1,1000);
		Account swedbankacc2 = new Account(9876,pin2,200);
		Account swedbankacc3 = new Account(4567,pin3,0);
		//lägg in shb konton 
		shbAccounts.put(shbacc1.account, shbacc1);
		shbAccounts.put(shbacc2.account, shbacc2);
		shbAccounts.put(shbacc3.account, shbacc3);
		//lägg in nordea konton
		nordeaAccounts.put(nordeaacc1.account,nordeaacc1);
		nordeaAccounts.put(nordeaacc2.account,nordeaacc2);
		nordeaAccounts.put(nordeaacc3.account,nordeaacc3);
		//lägg in swedbank konton
		swedbankAccounts.put(swedbankacc1.account,swedbankacc1);
		swedbankAccounts.put(swedbankacc2.account,swedbankacc2);
		swedbankAccounts.put(swedbankacc3.account,swedbankacc3);
		
		Bank shb = null;
		Bank nordea = null;
		Bank swedbank=null;
			try {
				shb = new BankImpl(nameList.shb, shbAccounts, banks);
				nordea = new BankImpl(nameList.nordea, nordeaAccounts, banks);
				swedbank = new BankImpl(nameList.swedbank,swedbankAccounts,banks);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//lägg till bankerna i vectorn så de kan komma åt varandra
			banks.add(shb);
			banks.add(nordea);
			banks.add(swedbank);
		try {
			//lägg till bankerna i registret så att clienterna kan komma åt dom
			reg.rebind(nameList.shb,shb);
			reg.rebind(nameList.nordea, nordea);
			reg.rebind(nameList.swedbank, swedbank);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
}
