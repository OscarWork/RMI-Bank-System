package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Vector;

import common.Bank;
/*
 * sj�lva bank implementationen, tar in sitt egen namn, en LinkedHashMap med sina accounts, en Vector med alla banker i.
 * f�r att "skicka vidare" requests, t.ex logga in p� en annan bank, s� kommer den f�rsta banken kolla vilken bank det �r klienten vill logga in p�
 * och sedan k�ra login metoden p� r�tt element(objekt) i banks vectorn.
 */
public class BankImpl extends UnicastRemoteObject implements Bank {

	String bankName;
	Map<Integer,Account> accounts;
	Vector<Bank> banks;
	public BankImpl(String bankName,Map<Integer,Account> accounts,Vector<Bank> banks )throws RemoteException {
		this.bankName = bankName;
		this.accounts = accounts;
		this.banks = banks;
	}
	@Override
	public boolean accountExists(int account) {
		
		return accounts.containsKey(account);
	}



	@Override
	public boolean logIn(String bank, int account, char[] pin) throws RemoteException {
		//kollar om det �r p� den h�r banken, isf kolla om kontot finns och att det inte �r inloggat h�r eller ngn annan stans.
		if (bank.equals(bankName)) {
			if (accountExists(account) && accounts.get(account).checkPin(pin)
					&& !(accounts.get(account).getInUse())) {
				accounts.get(account).setInUse(true);
				return true;
			}
			else{
				System.out.println("Fel inlogg eller s� finns inte kontot");
				return false;
			}
		}
		// annars kolla vilken bank det �r i, sen skicka starta inloggsmetoden p� den banken, med samma variabler, sedan returna till klient.
		else{
			int index=whichBank(bank);
			if (index == -1){
				System.out.println("Det finns ingen bank som heter: "+bank);
				return false;
			}
			return banks.elementAt(index).logIn(bank, account, pin);
			}
	}

	@Override		// kollar likadant som login vilken bank det g�ller, �r det denna bank s� s�tt "inUse" till false s� ngn annan kan logga in p� den
	public boolean logOut(String bank,int account) throws RemoteException{
		if (bank.equals(bankName)){
			if(accountExists(account) && (accounts.get(account).getInUse()))
			accounts.get(account).setInUse(false);
				return true;
		}
		else{
			int index=whichBank(bank);
			if (index == -1){
				return false;
			}
			return banks.elementAt(index).logOut(bank, account);
			}
	}
	@Override
	// om det �r p� denna bank, h�mta current balance, r�kna ut ny balance, och s�l�nge den inte �r mindre �n noll, uppdatera balance till det
	// v�rdet, �r det inte r�tt bank s� skicka vidare till den bank det g�ller, en hj�lp av whichBank(String bank)
	//kollar i klienten s� inte amount �r negativt 
	public boolean withdraw(String bank, int account, int amount)
			throws RemoteException {
		if (bank.equals(bankName)) {
			int balance = accounts.get(account).getBalance();
			int newBalance = balance -= amount;
			if (newBalance > 0) {
				accounts.get(account).setBalance(newBalance);
				return true;
			}
			else{
				System.out.print("Server error while withdrawing from: " + account+"Not enough money");
				return false;
			}
		}
		else{
			int index=whichBank(bank);
			if (index == -1){
				System.out.println("Det finns ingen bank som heter: "+bank);
				return false;
			}
				
			return banks.elementAt(index).withdraw(bank, account,amount);
			}
	}

	@Override
	//kolla vilken bank, om r�tt bank, l�gg till amount till balance och uppdatera, om inte r�tt bank, skicka vidare
	public boolean deposit(String bank,int account,int amount)throws RemoteException {
		if (bank.equals(bankName)){
			int balance = accounts.get(account).getBalance();
			int newBalance =balance += amount;
			accounts.get(account).setBalance(newBalance);
		return true;
	}
		else{
			int index=whichBank(bank);
			if (index == -1){
				System.out.println("Det finns ingen bank som heter: "+bank);
				return false;
			}
			return banks.elementAt(index).deposit(bank, account,amount);
			}
	}

	@Override
	// kollar bara vad balancen �r, och returnar det. om inte r�tt bank s� letar den r�tt p� den f�rst
	public int balance(String bank,int account) throws RemoteException{
		if (bank.equals(bankName)){
			return accounts.get(account).getBalance();
		}
		else{
			int index=whichBank(bank);
			if (index == -1){
				System.out.println("Det finns ingen bank som heter: "+bank);
				return -1;
			}
			return banks.elementAt(index).balance(bank, account);
		}
	}
	@Override
	public String getName() throws RemoteException {
		return bankName;
	}
	// f�r in det namn som klienten skickat med som banknammn, och letar i vectorn efter en bank med  samma namn, och returnar det indexet.
	// finns inte det namnet s� returnerar den -1
	private int whichBank(String bank) throws RemoteException{
		int index=0;
		for (int i=0; i < banks.size();i++){
			if(banks.elementAt(i).getName().equals(bank)){
				index = i;
			return index;
		}
	}
	System.out.print("The stated bank does not exist");
	return -1;
	}
}


