package actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

// THIS EXAMPLE IS FOR DEFINING ACTOR MESSAGES
public class ActorExample extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /*
     * This method defines all of the messages an actor recieves and what to do with them
     */
    @Override
    public Receive createReceive() {
        /*
        return receiveBuilder()
                .match(String.class, s -> {
                    log.info("Received String message: {}", s);
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
        */
        return receiveBuilder()
                .match(String.class, s -> matchStringReaction())
                .matchAny(s -> defaultReaction())
                .build();
    }

    private void defaultReaction() {
        log.info("Received unknown message.");
    }

    private void matchStringReaction() {
        log.info("Received String message.");
    }
}


