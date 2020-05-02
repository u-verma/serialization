package com.org.easysolution.serialization;

import java.io.Serializable;

public class SerializationWithInheritance {
}

class Employees implements Serializable {
    String companyName = "tech-solution";
}

class Engineer extends Employees{
    String department = "payments";
}