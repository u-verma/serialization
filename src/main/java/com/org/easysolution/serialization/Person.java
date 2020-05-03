package com.org.easysolution.serialization;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 167632477823L;
    int instanceVariable = 10;
    transient String transientVariable = "I am Transient";
    static String staticVariable = "initialized";
    transient final String finalVariable = "This is Final";
}

class Employee implements Serializable {
    String instanceVariable = "instanceVariable";
}

class SerializeDeserializePerson {
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
        System.out.println("staticVariable: " + Person.staticVariable);
        System.out.println("finalVariable: " + personObject.finalVariable);
    }
}

class MultipleSerializeDeserialization {
    public static void main(String[] args) throws Exception {
        Person person = new Person();
        Employee employee = new Employee();
        FileOutputStream fos = new FileOutputStream("person.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(person);
        oos.writeObject(employee);

        // Deserialization
        FileInputStream fis = new FileInputStream("person.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);

        while (true) {
            Object object;
            try {
                object = ois.readObject();
            } catch (EOFException ex) {
                break;
            }
            if (object instanceof Person) {
                Person person1 = (Person) object;
                System.out.println(person1.instanceVariable);
            } else if (object instanceof Employee) {
                Employee employee1 = (Employee) object;
                System.out.println(employee1.instanceVariable);
            }
        }
    }
}