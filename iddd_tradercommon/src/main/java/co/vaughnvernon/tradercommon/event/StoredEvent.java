//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package co.vaughnvernon.tradercommon.event;

import java.io.Serializable;
import java.util.Date;

public class StoredEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainEvent event;
    private long eventId;

    public StoredEvent(DomainEvent aDomainEvent) {
        super();

        this.setEvent(aDomainEvent);
    }

    public long eventId() {
        return this.eventId;
    }

    public Date occurredOn() {
        return this.event().occurredOn();
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> T toDomainEvent() {
        return (T) this.event();
    }

    public String typeName() {
        return this.event().getClass().getName();
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;
        if (anObject != null && this.getClass() == anObject.getClass()) {
            StoredEvent typedObject = (StoredEvent) anObject;
            equalObjects =
            		this.eventId() == typedObject.eventId();
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCode =
            + (4157 * 103)
            + (int) this.eventId();

        return hashCode;
    }

	@Override
	public String toString() {
		return "StoredEvent [eventId=" + eventId + ", event=" + event + "]";
	}

	private DomainEvent event() {
		return this.event;
	}

	private void setEvent(DomainEvent anEvent) {
		this.event = anEvent;
	}

	// WRNING: FOR INTERNAL USE ONLY
    public void setEventId(long anEventId) {
        this.eventId = anEventId;
    }
}
