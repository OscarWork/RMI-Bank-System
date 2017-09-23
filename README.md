# RMI Bank System

Detta är en skoluppgift jag skrev 2014 för kursen ID1217 - Concurrent Programming


dessa konton finns hos varje bank
	acc1 konto 1234,pin 0000,balance 1000kr
	acc2 konto:9876,pin 1111,balance 200kr
	acc3 konto 4567,pin 2222,balance 0kr

Det finns 3 banker, SHB,Swedbank och nordea.
finns även 1 bankomat per bank.
Varje "bankomat" är ett fönster, vid start skapas de på varandra så man får dra isär de

För att starta programmet så kör man RunBanker.sh, det kompilerar alla filer i de tre package mapparna, client, common och server. sedan startar det först 
BankServer och sedan AtmHandler.

En sak som är viktig är att man måste trycka enter efter att kontonummer och pin kod har matats in vid inloggning skärmen, innan man kan trycka
på login knappen
och man måste också välja en bank att logga in på.
