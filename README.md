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
