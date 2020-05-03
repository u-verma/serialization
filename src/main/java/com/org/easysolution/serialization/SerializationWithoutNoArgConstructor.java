package com.org.easysolution.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationWithoutNoArgConstructor {

    public static void main(String[] args) throws Exception {

        Manager manager = new Manager("TestName", "Engineering");
        // Serialization

        FileOutputStream fos = new FileOutputStream("manager.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(manager);

        // Deserialization
        FileInputStream fis = new FileInputStream("manager.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Manager manager01 = (Manager) ois.readObject();
        System.out.println("name: " + manager01.name);
        System.out.println("department: " + manager01.department);
    }
}

class Manager implements Serializable {
    String name;
    String department;

    public Manager(String name, String department) {
        this.name = name;
        this.department = department;
    }
}

