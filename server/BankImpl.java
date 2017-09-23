package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Vector;

import common.Bank;
/*
 * själva bank implementationen, tar in sitt egen namn, en LinkedHashMap med sina accounts, en Vector med alla banker i.
 * för att "skicka vidare" requests, t.ex logga in på en annan bank, så kommer den första banken kolla vilken bank det är klienten vill logga in på
 * och sedan köra login metoden på rätt element(objekt) i banks vectorn.
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
		//kollar om det är på den här banken, isf kolla om kontot finns och att det inte är inloggat här eller ngn annan stans.
		if (bank.equals(bankName)) {
			if (accountExists(account) && accounts.get(account).checkPin(pin)
					&& !(accounts.get(account).getInUse())) {
				accounts.get(account).setInUse(true);
				return true;
			}
			else{
				System.out.println("Fel inlogg eller så finns inte kontot");
				return false;
			}
		}
		// annars kolla vilken bank det är i, sen skicka starta inloggsmetoden på den banken, med samma variabler, sedan returna till klient.
		else{
			int index=whichBank(bank);
			if (index == -1){
				System.out.println("Det finns ingen bank som heter: "+bank);
				return false;
			}
			return banks.elementAt(index).logIn(bank, account, pin);
			}
	}

	@Override		// kollar likadant som login vilken bank det gäller, är det denna bank så sätt "inUse" till false så ngn annan kan logga in på den
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
	// om det är på denna bank, hämta current balance, räkna ut ny balance, och sålänge den inte är mindre än noll, uppdatera balance till det
	// värdet, är det inte rätt bank så skicka vidare till den bank det gäller, en hjälp av whichBank(String bank)
	//kollar i klienten så inte amount är negativt 
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
	//kolla vilken bank, om rätt bank, lägg till amount till balance och uppdatera, om inte rätt bank, skicka vidare
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
	// kollar bara vad balancen är, och returnar det. om inte rätt bank så letar den rätt på den först
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
	// får in det namn som klienten skickat med som banknammn, och letar i vectorn efter en bank med  samma namn, och returnar det indexet.
	// finns inte det namnet så returnerar den -1
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


