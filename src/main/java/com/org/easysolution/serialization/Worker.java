package com.org.easysolution.serialization;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Worker extends Person{

}

class SerializeWorker{
    public static void main(String[] args) throws Exception {
        Worker worker = new Worker();
        // Serialization
        FileOutputStream fos = new FileOutputStream("worker.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(worker);

        // Deserialization
        FileInputStream fis = new FileInputStream("worker.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Worker worker1 = (Worker) ois.readObject();
    }
}