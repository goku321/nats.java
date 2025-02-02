package io.nats.client.support;

public class NatsJetStreamClientError {
    private static final int KIND_ILLEGAL_ARGUMENT = 0;
    private static final int KIND_ILLEGAL_STATE = 1;
    private static final String SUB = "SUB";
    private static final String SO = "SO";

    public static final NatsJetStreamClientError JsSubPullCantHaveDeliverGroup = new NatsJetStreamClientError(SUB, 90001, "Pull subscriptions can't have a deliver group.");
    public static final NatsJetStreamClientError JsSubPullCantHaveDeliverSubject = new NatsJetStreamClientError(SUB, 90002, "Pull subscriptions can't have a deliver subject.");
    public static final NatsJetStreamClientError JsSubPushCantHaveMaxPullWaiting = new NatsJetStreamClientError(SUB, 90003, "Push subscriptions cannot supply max pull waiting.");
    public static final NatsJetStreamClientError JsSubQueueDeliverGroupMismatch = new NatsJetStreamClientError(SUB, 90004, "Queue / deliver group mismatch.");
    public static final NatsJetStreamClientError JsSubFcHbNotValidPull = new NatsJetStreamClientError(SUB, 90005, "Flow Control and/or heartbeat is not valid with a pull subscription.");
    public static final NatsJetStreamClientError JsSubFcHbNotValidQueue = new NatsJetStreamClientError(SUB, 90006, "Flow Control and/or heartbeat is not valid in queue mode.");
    public static final NatsJetStreamClientError JsSubNoMatchingStreamForSubject = new NatsJetStreamClientError(SUB, 90007, "No matching streams for subject.", KIND_ILLEGAL_STATE);
    public static final NatsJetStreamClientError JsSubConsumerAlreadyConfiguredAsPush = new NatsJetStreamClientError(SUB, 90008, "Consumer is already configured as a push consumer.");
    public static final NatsJetStreamClientError JsSubConsumerAlreadyConfiguredAsPull = new NatsJetStreamClientError(SUB, 90009, "Consumer is already configured as a pull consumer.");
    public static final NatsJetStreamClientError JsSubSubjectDoesNotMatchFilter = new NatsJetStreamClientError(SUB, 90011, "Subject does not match consumer configuration filter.");
    public static final NatsJetStreamClientError JsSubConsumerAlreadyBound = new NatsJetStreamClientError(SUB, 90012, "Consumer is already bound to a subscription.");
    public static final NatsJetStreamClientError JsSubExistingConsumerNotQueue = new NatsJetStreamClientError(SUB, 90013, "Existing consumer is not configured as a queue / deliver group.");
    public static final NatsJetStreamClientError JsSubExistingConsumerIsQueue = new NatsJetStreamClientError(SUB, 90014, "Existing consumer  is configured as a queue / deliver group.");
    public static final NatsJetStreamClientError JsSubExistingQueueDoesNotMatchRequestedQueue = new NatsJetStreamClientError(SUB, 90015, "Existing consumer deliver group does not match requested queue / deliver group.");
    public static final NatsJetStreamClientError JsSubExistingConsumerCannotBeModified = new NatsJetStreamClientError(SUB, 90016, "Existing consumer cannot be modified.");
    public static final NatsJetStreamClientError JsSubConsumerNotFoundRequiredInBind = new NatsJetStreamClientError(SUB, 90017, "Consumer not found, required in bind mode.");
    public static final NatsJetStreamClientError JsSubOrderedNotAllowOnQueues = new NatsJetStreamClientError(SUB, 90018, "Ordered consumer not allowed on queues.");
    public static final NatsJetStreamClientError JsSubPushCantHaveMaxBatch = new NatsJetStreamClientError(SUB, 90019, "Push subscriptions cannot supply max batch.");
    public static final NatsJetStreamClientError JsSubPushCantHaveMaxBytes = new NatsJetStreamClientError(SUB, 90020, "Push subscriptions cannot supply max bytes.");

    public static final NatsJetStreamClientError JsSoDurableMismatch = new NatsJetStreamClientError(SO, 90101, "Builder durable must match the consumer configuration durable if both are provided.");
    public static final NatsJetStreamClientError JsSoDeliverGroupMismatch = new NatsJetStreamClientError(SO, 90102, "Builder deliver group must match the consumer configuration deliver group if both are provided.");
    public static final NatsJetStreamClientError JsSoDeliverSubjectMismatch = new NatsJetStreamClientError(SO, 90103, "Builder deliver subject must match the consumer configuration deliver subject if both are provided.");
    public static final NatsJetStreamClientError JsSoOrderedNotAllowedWithBind = new NatsJetStreamClientError(SO, 90104, "Bind is not allowed with an ordered consumer.");
    public static final NatsJetStreamClientError JsSoOrderedNotAllowedWithDeliverGroup = new NatsJetStreamClientError(SO, 90105, "Deliver group is not allowed with an ordered consumer.");
    public static final NatsJetStreamClientError JsSoOrderedNotAllowedWithDurable = new NatsJetStreamClientError(SO, 90106, "Durable is not allowed with an ordered consumer.");
    public static final NatsJetStreamClientError JsSoOrderedNotAllowedWithDeliverSubject = new NatsJetStreamClientError(SO, 90107, "Deliver subject is not allowed with an ordered consumer.");
    public static final NatsJetStreamClientError JsSoOrderedRequiresAckPolicyNone = new NatsJetStreamClientError(SO, 90108, "Ordered consumer requires Ack Policy None.");
    public static final NatsJetStreamClientError JsSoOrderedRequiresMaxDeliver = new NatsJetStreamClientError(SO, 90109, "Max deliver is limited to 1 with an ordered consumer.");

    @Deprecated // Fixed spelling error
    public static final NatsJetStreamClientError JsSubFcHbHbNotValidQueue = new NatsJetStreamClientError(SUB, 90006, "Flow Control and/or heartbeat is not valid in queue mode.");

    private final String id;
    private final String message;
    private final int kind;

    public NatsJetStreamClientError(String group, int code, String description) {
        this(group, code, description, KIND_ILLEGAL_ARGUMENT);
    }

    public NatsJetStreamClientError(String group, int code, String description, int kind) {
        id = String.format("%s-%d", group, code);
        message = String.format("[%s] %s", id, description);
        this.kind = kind;
    }

    public RuntimeException instance() {
        return _instance(message);
    }

    public RuntimeException instance(String extraMessage) {
        return _instance(message + " " + extraMessage);
    }

    private RuntimeException _instance(String msg) {
        if (kind == KIND_ILLEGAL_ARGUMENT) {
            return new IllegalArgumentException(msg);
        }
        return new IllegalStateException(msg);
    }

    public String id() {
        return id;
    }

    public String message() {
        return message;
    }
}
