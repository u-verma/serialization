package com.org.easysolution.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person implements Serializable{
    static final long serialVersionUID = 167632477823l;
    int instanceVariable = 10;
    transient String transientVariable = "I am Transient";
    static String staticVariable = "initialized";
    transient final String finalVariable = "This is Final";
}

class SerializeDeserializePerson{
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person();
        // Serialization
        Person.staticVariable = "Re-Initialized";
        FileOutputStream fos = new FileOutputStream("person.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(person);

        // Deserialization
        FileInputStream fis = new FileInputStream("person.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Person personObject = (Person) ois.readObject();
        System.out.println("instanceVariable: " + personObject.instanceVariable);
        System.out.println("transientVariable: " + personObject.transientVariable);
        System.out.println("staticVariable: " + personObject.staticVariable);
        System.out.println("finalVariable: " + personObject.finalVariable);
    }
}