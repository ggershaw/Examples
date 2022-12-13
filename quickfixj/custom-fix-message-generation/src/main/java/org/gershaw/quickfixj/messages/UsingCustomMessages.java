package org.gershaw.quickfixj.messages;

import org.gershaw.quickfixj.fixt11.GeoffsHeartbeat;
import org.gershaw.quickfixj.fixt11.field.MsgType;
import quickfix.FieldNotFound;

/** WARNING: You must run mvn package or later in the maven lifecycle on this submodule to generate
 * the classes before running this class. Otherwise, the compiler won't find the generated classes.
 * You must also mark quickfixj/custom-fix-message-generation/target/generated-sources as a
 * generated source folder
 * NOTE: the maven plugin generates the source of 1803 classes. It takes time for this submodule to
 * build and this class to start running for the first time in your IDE. Please don't freak out * */
public class UsingCustomMessages {

  /**In this artificial example, I changed the name of the heartbeat message in FIXT11-geoff.xml
   to GeoffsHeartbeat. After the mvn plugin runs, the GeoffsHeartbeat class is generated,
   along with 1802 others :).

   Note the use of FIX50SP2.modified.xml in the mvn plugin config. Diff it with FIX50SP2.xml to
   see the changes made, which allow the generated message classes to compile.

   As an exercise, change the file used in the mvn plugin's config section to FIX50SP2.xml. Run
   mvn clean install and you will see there are 100 compilation errors. An example of 1 of the
   compilation errors is

   "method getSecurityXML() is already defined in class
   org.gershaw.quickfixj.fix50sp2.component.Instrument"

   Open the generated source of org.gershaw.quickfixj.fix50sp2.component.Instrument, found here
   target/generated-sources/org/gershaw/quickfixj/fix50sp2/component/Instrument.java. Search for
   getSecurityXML(). You will see that the method is found twice in the class with different return
   types. This is not allowed in java. This makes a fun interview question :). Please refer to the
   question as "the Gershaw question" and don't tell anyone about it :))))).

   Another exercise, generate a BlahHeartbeat class, where blah is your first name. If you
   get stuck, search this submodule for GeoffsHeartbeat. If you still can't figure it out, start
   a new discussion <a href="https://github.com/ggershaw/ggershaw.github.io/discussions">Here</a> and let's
   help each other :)

   How is the source code being generated? **/

  public static void main(String[] args) throws FieldNotFound {

    GeoffsHeartbeat heartbeat = new GeoffsHeartbeat();
    String msgType = heartbeat.getHeader().getField( new MsgType()).getValue();
    System.out.println(String.join("=","I've created a custom class for MsgType",
        msgType));
  }

}
