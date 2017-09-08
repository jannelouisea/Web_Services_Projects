package actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import akka.actor.Props;

// THIS EXAMPLE IS FOR DEFINING THE ACTOR'S PROPS METHOD
public class ActorPropsExample extends AbstractActor {
    /**
     * Create Props for an actor of this type.
     * @param magicNumber The magic number to be passed to this actor’s constructor.
     * @return a Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    static Props props(Integer magicNumber) {
        // You need to specify the actual type of the returned actor
        // since Java 8 lambdas have some runtime type information erased
        return Props.create(ActorPropsExample.class, () -> new ActorPropsExample(magicNumber));
    }

    // Parameters
    private final Integer magicNumber;

    // Constructor function
    public ActorPropsExample(Integer magicNumber) {
        this.magicNumber = magicNumber;
    }

    /*
     * This method defines all of the messages an actor receives and what to do with them
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(s -> {})
                .build();
    }
}
