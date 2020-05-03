package com.org.easysolution.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Externalization {


}

class Company implements Externalizable{

    int premitivVariable = 10;
    String instanceVariable = "instanceVariable";
    static String staticVariable = "staticVariable";
    transient String transientVariable = "transientVariable";
    final String finalVariable = "finalVariable";

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}