package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.Bank;
import common.Names;

public class AtmHandler {
/*
 * hittar registret, skapar alla bankomater, h�mtar en stub f�r varje bank.
 */
	
	public static void main (String[] args){
		Names nameList = new Names();
		Registry reg=null;
		Bank shb=null;
		Bank nordea = null;
		Bank swedbank = null;
		try {
			// h�mta registret som ligger p� adressen "localhost",hade det fr�gat efter en adress vi start s� hade bankerna
			// kunnat finnas p� vilken dator som helst.
			reg = LocateRegistry.getRegistry("localhost");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		try {
			// h�mtar "stubs" f�r varje bank, dessa kan clienterna sen g�ra RMI calls p� som om det var ett lokalt objekt.
			shb=(Bank) reg.lookup(nameList.shb);
			nordea = (Bank) reg.lookup(nameList.nordea);
			swedbank = (Bank) reg.lookup(nameList.swedbank);
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] banks = {nameList.shb,nameList.nordea,nameList.swedbank}; // lista med bankernas namn till listan med banker p� login sidan

		//skapar och visar 3 bankomater, som �r kopplad till varsin bank
		LogInFrame shb1 = new LogInFrame(shb,banks);
		LogInFrame nordea1 = new LogInFrame(nordea,banks);
		LogInFrame swedbank1 = new LogInFrame(swedbank,banks);
		shb1.setVisible(true);
		nordea1.setVisible(true);
		swedbank1.setVisible(true);
		
	}
}
	

