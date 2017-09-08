package actors;

public class ActorsSendingAndReciveingMessagesExample {

    // TELLING A MESSAGE
    // target.tell(message, <who is the sender>);

    // Within an actor trying to talk to another actor
    // target.tell(message, getSelf());

    // Outside of an actor
    // target.tell(message, null);

    // Responding to a message
    // getSender().tell(s, getSelf());


}
