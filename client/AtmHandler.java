package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.Bank;
import common.Names;

public class AtmHandler {
/*
 * hittar registret, skapar alla bankomater, hämtar en stub för varje bank.
 */
	
	public static void main (String[] args){
		Names nameList = new Names();
		Registry reg=null;
		Bank shb=null;
		Bank nordea = null;
		Bank swedbank = null;
		try {
			// hämta registret som ligger på adressen "localhost",hade det frågat efter en adress vi start så hade bankerna
			// kunnat finnas på vilken dator som helst.
			reg = LocateRegistry.getRegistry("localhost");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		try {
			// hämtar "stubs" för varje bank, dessa kan clienterna sen göra RMI calls på som om det var ett lokalt objekt.
			shb=(Bank) reg.lookup(nameList.shb);
			nordea = (Bank) reg.lookup(nameList.nordea);
			swedbank = (Bank) reg.lookup(nameList.swedbank);
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] banks = {nameList.shb,nameList.nordea,nameList.swedbank}; // lista med bankernas namn till listan med banker på login sidan

		//skapar och visar 3 bankomater, som är kopplad till varsin bank
		LogInFrame shb1 = new LogInFrame(shb,banks);
		LogInFrame nordea1 = new LogInFrame(nordea,banks);
		LogInFrame swedbank1 = new LogInFrame(swedbank,banks);
		shb1.setVisible(true);
		nordea1.setVisible(true);
		swedbank1.setVisible(true);
		
	}
}
	

