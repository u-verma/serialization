# Serialization

 - The process of writing the `state of an object` to a `file` is serialization.
 - Serialization can be achieved Using `FileOutputStream` and `ObjectOutputStream` classes.
 - The class must `implements Serializable` in order to get Serialize.
 - Serializable interface present in `java.io` package. It doesn't contain any method. It is a marker interface.

# Deserialization 
 - The process of reading the `state of an object` from the `file` is deserialization. 
 Strictly speaking it is the process of converting an object from file or network supported form to Java supported form.
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
 - `Imp Note` If run the below code in the same JVM, the `Re-Initialized` will get printed as during the deserialization 
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
 
# Custom Serialization Deserialization
 - Custom serialization required to save the sensitive information in the serialized object for security reason.
 - if a variable declared as a transient then the information get lost during serialization. 
 Custom serialization, can help to secure that information and pass it during the process.
 - JVM Look for the below method signature, in the class, which is being serialized. 
 If present then JVM will no longer be responsible for the `default serialization`.
  ```java
        private void writeObject(ObjectOutputStream oos) throws Exception 
  ```
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
    - As JVM executes the `instance control flow` during deserialization, default constructor should be present for all the non serializable parent.
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
# Externalization 