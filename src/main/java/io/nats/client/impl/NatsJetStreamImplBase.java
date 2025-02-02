// Copyright 2021 The NATS Authors
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.nats.client.impl;

import io.nats.client.JetStreamApiException;
import io.nats.client.JetStreamOptions;
import io.nats.client.Message;
import io.nats.client.api.*;
import io.nats.client.support.JsonUtils;
import io.nats.client.support.NatsJetStreamConstants;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static io.nats.client.support.ApiConstants.SUBJECT;

class NatsJetStreamImplBase implements NatsJetStreamConstants {

    final NatsConnection conn;
    final JetStreamOptions jso;

    // ----------------------------------------------------------------------------------------------------
    // Create / Init
    // ----------------------------------------------------------------------------------------------------
    NatsJetStreamImplBase(NatsConnection connection, JetStreamOptions jsOptions) throws IOException {
        conn = connection;
        jso = JetStreamOptions.builder(jsOptions).build(); // builder handles null
    }

    // ----------------------------------------------------------------------------------------------------
    // Management that is also needed by regular context
    // ----------------------------------------------------------------------------------------------------
    ConsumerInfo _getConsumerInfo(String streamName, String consumer) throws IOException, JetStreamApiException {
        String subj = String.format(JSAPI_CONSUMER_INFO, streamName, consumer);
        Message resp = makeRequestResponseRequired(subj, null, jso.getRequestTimeout());
        return new ConsumerInfo(resp).throwOnHasError();
    }

    ConsumerInfo _createConsumer(String streamName, ConsumerConfiguration config) throws IOException, JetStreamApiException {
        String durable = config.getDurable();
        String requestJSON = new ConsumerCreateRequest(streamName, config).toJson();

        String subj;
        if (durable == null) {
            subj = String.format(JSAPI_CONSUMER_CREATE, streamName);
        } else {
            subj = String.format(JSAPI_DURABLE_CREATE, streamName, durable);
        }
        Message resp = makeRequestResponseRequired(subj, requestJSON.getBytes(), conn.getOptions().getConnectionTimeout());
        return new ConsumerInfo(resp).throwOnHasError();
    }

    void _createConsumerUnsubscribeOnException(String stream, ConsumerConfiguration cc, NatsJetStreamSubscription sub) throws IOException, JetStreamApiException {
        try {
            ConsumerInfo ci = _createConsumer(stream, cc);
            sub.setConsumerName(ci.getName());
        }
        catch (IOException | JetStreamApiException e) {
            // create consumer can fail, unsubscribe and then throw the exception to the user
            if (sub.getDispatcher() == null) {
                sub.unsubscribe();
            }
            else {
                sub.getDispatcher().unsubscribe(sub);
            }
            throw e;
        }
    }

    StreamInfo _getStreamInfo(String streamName, StreamInfoOptions options) throws IOException, JetStreamApiException {
        String subj = String.format(JSAPI_STREAM_INFO, streamName);
        byte[] payload = options == null ? null : options.serialize();
        Message resp = makeRequestResponseRequired(subj, payload, jso.getRequestTimeout());
        return new StreamInfo(resp).throwOnHasError();
    }

    List<String> _getStreamNamesBySubjectFilter(String subjectFilter) throws IOException, JetStreamApiException {
        byte[] body = JsonUtils.simpleMessageBody(SUBJECT, subjectFilter);
        StreamNamesReader snr = new StreamNamesReader();
        Message resp = makeRequestResponseRequired(JSAPI_STREAM_NAMES, body, jso.getRequestTimeout());
        snr.process(resp);
        return snr.getStrings();
    }

    // ----------------------------------------------------------------------------------------------------
    // Request Utils
    // ----------------------------------------------------------------------------------------------------
    Message makeRequestResponseRequired(String subject, byte[] bytes, Duration timeout) throws IOException {
        try {
            return responseRequired(conn.request(prependPrefix(subject), bytes, timeout));
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    Message makeInternalRequestResponseRequired(String subject, Headers headers, byte[] data, boolean utf8mode, Duration timeout, boolean cancelOn503) throws IOException {
        try {
            return responseRequired(conn.requestInternal(subject, headers, data, utf8mode, timeout, cancelOn503));
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    Message responseRequired(Message respMessage) throws IOException {
        if (respMessage == null) {
            throw new IOException("Timeout or no response waiting for NATS JetStream server");
        }
        return respMessage;
    }

    String prependPrefix(String subject) {
        return jso.getPrefix() + subject;
    }
}
