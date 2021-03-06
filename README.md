# Serialization

 - Storing, saving or transmitting the `state of an object` in binary form, is known as serialization.
 - Serialization can be achieved Using `FileOutputStream` and `ObjectOutputStream` classes.
 - The class must `implements Serializable` in order to get Serialize.
 - Serializable interface present in `java.io` package. It doesn't contain any method. It is a marker interface.

# Deserialization 
 - The process of reading the `state of an object` from the `file` is deserialization. 
 Strictly speaking it is the process of converting an object from, file or network supported form to Java supported form.
 - Deserialization can be achieved Using `FileInputStream` and `ObjectInputStream` classes.
 
  ```java
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.io.Serializable;
        
        public class Person implements Serializable{
            int instanceVariable = 10;
        }
        
        class SerializeDeserializePerson{
            public static void main(String[] args) throws IOException, ClassNotFoundException {
                Person person = new Person();
                // Serialization
                FileOutputStream fos = new FileOutputStream("person.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(person);
        
                // Deserialization
                FileInputStream fis = new FileInputStream("person.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                Person personObject = (Person) ois.readObject();
                System.out.println("instanceVariable: " + personObject.instanceVariable);
            }
        }
  ```
# Transient Keyword
 - `transient` keyword is applicable only for variables not for method or classes.
 - if a variable declare as `transient` it's value will not be serialized. 
 - For a `transient` variable compiler ignore the original value and save its default value to the serialized Object.
 - `static` variable is not part of Instance hence they don't participate in the serialization. 
 During Deserialization compiler read there value from class level memory.
 - `Imp Note` If run the below code in the same JVM, the `Re-Initialized` will get printed, as during the deserialization 
 JVM will be able to read the value from static data memory which will be shared by each object.
 - If the deserialization performed in another JVM then the default value `initialized` will be printed for static variable.
 - During serialization static member should be handled carefully else it will give unexpected behavior and hard to debug.
 - If a static variable declared as a `transient` then it has no impact as static member do not participate in serialization.
 - As, `final` variable is a constant which gets replaced by its value at the compile time, they participate in serialization by its value and not as a variable.
 - As, the `final` declared variable do not participate as a variable during the serialization, declaring it as `transient` will not have any impact.
 - `Imp Note` the sensitive information should not be marked as final, if it is going to be serialized. its state will always be saved and transient can't protect it.   
 
  ```java
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

        O/P
                instanceVariable: 10
                transientVariable: null
                **if deserialized in same JVM **
                staticVariable: Re-Initialized
                **if deserialized in another JVM **
                staticVariable: initialized
                finalVariable: This is Final
  ```
# Object Graph

 - In the below example ClassA has ClassB object and ClassB has ClassC object 

    ![example-uml](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/u-verma/serialization/master/example.iuml)

 - If Class A gets serialized all the object associated with class A, also gets serialized. 
 - This chain of object which gets serialized is called Object graph.
 - `Please Note` All the Objects which are part of Object graph `must` implements the `Serializable` Interface.
 Otherwise, a runtime exception will occur.
 - In above example, if Class B doesn't want to participant in serialization then 
 it can implement the below method and can throw the exception inside it. it will in-force Class A to declare it as a `transient`.``
    
 ```java
       private void writeObject(ObjectOutputStream oos) throws Exception {
           throw new NotSerializableException("This class can't be serialized");
       }   
 ```
 - First JVM look for the above method signature, in the class, which is being serialized before performing `default serialization`.
    If present then JVM will no longer perform `default serialization` and execute above method .
 
# Custom Serialization Deserialization
 - Custom serialization required to save the sensitive information in the serialized object for security reason.
 - if a variable declared as a transient then the information get lost during serialization. 
 Custom serialization, can help to secure that information and pass it during the process.
 - JVM Look for the below method signature, in the class, which is being serialized. 
 If present then JVM will no longer be responsible for the `default serialization`.
 
  ```java
        private void writeObject(ObjectOutputStream oos) throws Exception 
  ```
 - If  class want to stop participating in `serialization or in object graph` then it can define the above method and can throw an exception. 
 This will in-force every user to mark reference of this class as transient. 
 - It is possible to call the default serialization inside `writeObject` method by calling below method.
  ```java
        oos.defaultWriteObject();
  ```
- JVM Look for the below method signature, in the class, which is being `deserialized`. 
 If present then JVM will no longer be responsible for the `default serialization`.
 
 ```java
        private void readObject(ObjectInputStream ois) throws Exception 
 ```
 - It is possible to call the default deserialization inside `readObject` method by calling below method.
 ```java
        oos.defaultReadObject();
 ```

 - See it in action below.
 
    ![customSerialization](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/u-verma/serialization/master/customSerialization.iuml)

 ```java
    
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
 ```

# Inheritance in serialization
 - If the parent class implements serializable interface, all the child class will become serializable because of inheritance.
 - That's why the Object class doesn't implement serializable.
 - With the above in mind it is clear that a child can be serializable even though the parent is not. 
 - Few key point to remember when dealing with non serializable parent.
    - While serializing the child which has inherited variable of non serialized class, store the default value of inherited variable.
    - During deserialization, JVM executes the `instance control flow` in every non serializable parent 
    and share its instance variable to the current object. 
    - `Please Note` *instance control flow* will not be executed in child as the object of child gets created via deserialization.
    - As JVM executes the `instance control flow` during deserialization, default constructor must be present for all the non serializable parent.
    - In the below example the deserialized object will print the `companyName` as `tech-solution` and not `Tech`, based on the above explanation.

  ```java
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
  ```  
 - `Imp note` Default constructor required only in case of inheritance. To be specific, a class is not a direct child of Object class.
 -  In below example the Manager class will be deserialized properly.
 
  ```java

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
          
  ```
  
# Externalization 

 - To achieve Externalization class should implement the Externalizable interface. It is the child interface of Serializable interface.
 - In Externalization developer has the full control to serialize the object and JVM doesn't execute its default behavior. 
 - In serialization JVM take the full control to serialize the object and developer/user can't control any behavior.
 - Main advantage of Externalize over serialization is that, it is possible to serialize the part of an object in it, wherein it is not possible in serialization.
 - Externalizable interface is `not a Marker interface` like Serializable. it defines below two method which should be implemented.
  ```java
         public void writeExternal(ObjectOutput out) throws IOException;
            
         public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  ```
 - As it is possible to serialize part of the object in externalization, during deserialization JVM has to create the default object. 
 So, default (`no-argument constructor`) constructor must be present in the class which implements Externalizable.
 - `transient` has no impact in externalization as we are explicitly writing the value in serialized object.
 - static and final are still going to behave same as in serialization. 
 - The most important thing is to remember that the order of writing and reading should be same otherwise it will throw the Runtime exception.
 
  ```java
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
  ```
# SerialVersionUID
 - The serialization runtime associates with each serializable class a version number, called a serialVersionUID.
 - serialVersionUID used during deserialization to verify that the sender and receiver of a serialized object 
 have loaded classes for that object that are compatible with respect to serialization. 
 - If the receiver has loaded a class for the object that has a different serialVersionUID than that of the corresponding sender's class, 
 then deserialization will result in an InvalidClassException. 
 - A serializable class can declare its own serialVersionUID explicitly by declaring a field named serialVersionUID that must be static, final, and of type long.
  ```java
        ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
  ```
 - Although any modifier will work but recommended to use `private` as it will avoid polluting the child class.
 - If a serializable class does not explicitly declare a serialVersionUID, then the serialization runtime will calculate a default serialVersionUID value for that class based on various aspects of the class, as described in the Java(TM) Object Serialization Specification.
 - The deserialization of the object will fail in different JVM as both JVM will generate different random `serialVersionUID`.
 - All serializable classes must explicitly declare serialVersionUID values, since the default serialVersionUID computation is highly sensitive to class details that may vary depending on compiler implementations, and can thus result in unexpected InvalidClassExceptions during deserialization. 
 - Even though, if the state of the object changed in between serialization/deserialization and serialVersionUID is unchanged. The saved object can be deserialize properly. 
 Extra added property will be initialized with default value. Removed property will be ignored during deserialization. 
 - Change in `serialVersionUID` will in-force all the consumer to update the version of .class file at their end else the deserialization will fail.