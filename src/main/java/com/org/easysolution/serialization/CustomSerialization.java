package com.org.easysolution.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class CustomSerialization {
    public static void main(String[] args) throws Exception {
        Account account = new Account();
        FileOutputStream fos = new FileOutputStream("account.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(account);

        FileInputStream fis = new FileInputStream("account.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Account account1 = (Account) ois.readObject();
        System.out.println("password: " + account1.password);
    }
}

class Account implements Serializable{
    String userName = "TestAccount";
    transient String password = "password";
    // Before executing default serialization JVM look for the this method signature and call it.
    private void writeObject(ObjectOutputStream oos) throws Exception{
        // This line tell JVM to Perform the default Serialization
        oos.defaultWriteObject();
        // Encrypting the original password.
        byte[] encPassword = Base64.getEncoder().encode(this.password.getBytes());
        // write the encrypted password in serialized object
        oos.writeObject(encPassword);
    }

    // Before executing default Deserialization JVM look for the this method signature and call it.
    private void readObject(ObjectInputStream ois) throws Exception{
        // This line tell JVM to Perform the default deserialization
        ois.defaultReadObject();
        // write the encrypted password in serialized object
        byte[] encPassword = (byte[]) ois.readObject();
        // Decrypting the original password.
        password = new String(Base64.getDecoder().decode(encPassword));
    }
}