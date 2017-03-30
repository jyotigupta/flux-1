/*
 * Copyright 2012-2016, the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.flux.eventscheduler.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shyam.akirala
 */
@Entity
@Table(name = "ScheduledEvents")
@IdClass(ScheduledEvent.ScheduledEventPK.class)
public class ScheduledEvent implements Serializable {

    @Id
    private String correlationId;

    @Id
    private String eventName;

    private long scheduledTime;

    private String eventData;

    /** for Hibernate */
    public ScheduledEvent() {
    }

    public ScheduledEvent(String correlationId, String eventName, long scheduledTime, String eventData) {
        this.correlationId = correlationId;
        this.eventName = eventName;
        this.scheduledTime = scheduledTime;
        this.eventData = eventData;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getEventName() {
        return eventName;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledEvent)) return false;

        ScheduledEvent that = (ScheduledEvent) o;

        if (scheduledTime != that.scheduledTime) return false;
        if (!correlationId.equals(that.correlationId)) return false;
        if (!eventData.equals(that.eventData)) return false;
        if (!eventName.equals(that.eventName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = correlationId.hashCode();
        result = 31 * result + eventName.hashCode();
        result = 31 * result + (int) (scheduledTime ^ (scheduledTime >>> 32));
        result = 31 * result + eventData.hashCode();
        return result;
    }

    static class ScheduledEventPK implements Serializable {

        private String correlationId;

        private String eventName;

        public ScheduledEventPK() {}

        public ScheduledEventPK(String correlationId, String eventName) {
            this.correlationId = correlationId;
            this.eventName = eventName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ScheduledEventPK)) return false;

            ScheduledEventPK that = (ScheduledEventPK) o;

            if (!correlationId.equals(that.correlationId)) return false;
            return eventName.equals(that.eventName);

        }

        @Override
        public int hashCode() {
            int result = correlationId.hashCode();
            result = 31 * result + eventName.hashCode();
            return result;
        }
    }
}