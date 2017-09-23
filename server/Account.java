package server;
/*
 * account klassen, har allt som tillhör kontot
 */
public class Account {
	int account;
	char[] pin;
	int balance;
	boolean inUse=false;
	public Account(int account,char[] pin,int balance){
		this.account = account;
		this.pin = pin;
		this.balance = balance;
	}
	public synchronized boolean getInUse(){
		return inUse;
	}
	public synchronized void setInUse(boolean use){
		inUse = use;
	}
	public synchronized boolean checkPin(char[] input){
		boolean correctPass=true;
		if (input.length!=pin.length){
			correctPass = false;
		}
		else{
			for (int i = 0;i<input.length;i++){
				if(input[i]!=pin[i]){
					correctPass = false;
				}
			}
		}
		return correctPass;
	}
	public synchronized int getBalance(){
		return balance;
	}
	public synchronized void setBalance(int newBalance){
		balance = newBalance;
	}

}
