package com.org.easysolution.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationWithInheritance {
    public static void main(String[] args) throws Exception{
        Engineer engineer = new Engineer();
        engineer.companyName = "Tech";
        engineer.department="cards";
        // Serialization
        FileOutputStream fos = new FileOutputStream("engineer.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(engineer);

        // Deserialization
        FileInputStream fis = new FileInputStream("engineer.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Engineer engineer01 = (Engineer) ois.readObject();
        System.out.println("The inherited variable value: " + engineer01.companyName);
    }
}

class Employees  {
    String companyName = "tech-solution";
}

class Engineer extends Employees implements Serializable{
    String department = "payments";
}