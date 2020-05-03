package com.org.easysolution.serialization;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Externalization {
    public static void main(String[] args) throws Exception {

        Company company = new Company();
        // Serialization
        Company.staticVariable = "New -StaticValue";
        FileOutputStream fos = new FileOutputStream("company.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(company);

        // Deserialization
        FileInputStream fis = new FileInputStream("company.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Company company01 = (Company) ois.readObject();
        System.out.println("primitiveVariable: " + company01.primitiveVariable);
        System.out.println("instanceVariable: " + company01.instanceVariable);
        System.out.println("staticVariable: " + Company.staticVariable);
        System.out.println("transientVariable: " + company01.transientVariable);
        System.out.println("finalVariable: " + company01.finalVariable);
        System.out.println("ignoreThis: " + company01.ignoreThis);
    }

}

class Company implements Externalizable{
    static final long serialVersionUID = 167632477825l;

    int primitiveVariable = 10;
    String instanceVariable = "instanceVariable";
    static String staticVariable = "staticVariable";
    transient String transientVariable = "transientVariable";
    final String finalVariable = "finalVariable";
    String ignoreThis;

    public Company() {
        //default constructor
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(primitiveVariable);
        out.writeObject(instanceVariable);
        out.writeObject(staticVariable);
        out.writeObject(transientVariable);
        out.writeObject(finalVariable);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readInt();
        in.readObject();
        in.readObject();
        in.readObject();
        in.readObject();
    }
}