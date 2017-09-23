#!/bin/sh
javac \client\\AtmHandler.java
javac \client\\DepositDialog.java
javac \client\\LogedInFrame.java
javac \client\\LogInFrame.java
javac \client\\WithdrawDialog.java

javac \common\\Bank.java
javac \common\\Names.java

javac \server\\Account.java
javac \server\\BankImpl.java
javac \server\\BankServer.java

java server.BankServer &
java client.AtmHandler