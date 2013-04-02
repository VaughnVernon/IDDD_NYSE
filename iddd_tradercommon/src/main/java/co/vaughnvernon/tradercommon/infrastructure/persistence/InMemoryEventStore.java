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

package co.vaughnvernon.tradercommon.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import co.vaughnvernon.tradercommon.event.DomainEvent;
import co.vaughnvernon.tradercommon.event.EventStore;
import co.vaughnvernon.tradercommon.event.StoredEvent;

public class InMemoryEventStore implements EventStore {

	private List<StoredEvent> events;
	private int nextEventId;

	public InMemoryEventStore() {
		super();

		this.setEvents(new ArrayList<StoredEvent>());
		this.setNextEventId(1);
	}

	@Override
	public List<StoredEvent> allStoredEventsBetween(
			long aLowStoredEventId,
			long aHighStoredEventId) {

		List<StoredEvent> results = new ArrayList<StoredEvent>();

		synchronized (this.events()) {
			for (StoredEvent storedEvent : this.events()) {
				if (storedEvent.eventId() >= aLowStoredEventId && storedEvent.eventId() <= aHighStoredEventId) {
					results.add(storedEvent);
				}
			}
		}

		return results;
	}

	@Override
	public List<StoredEvent> allStoredEventsSince(long aStoredEventId) {
		List<StoredEvent> results = new ArrayList<StoredEvent>();

		synchronized (this.events()) {
			for (StoredEvent storedEvent : this.events()) {
				if (storedEvent.eventId() > aStoredEventId) {
					results.add(storedEvent);
				}
			}
		}

		return results;
	}

	@Override
	public StoredEvent append(DomainEvent aDomainEvent) {
		StoredEvent storedEvent = new StoredEvent(aDomainEvent);

		synchronized (this.events()) {
			int nextEventId = this.nextEventId();

			storedEvent.setEventId(nextEventId);

			this.setNextEventId(nextEventId + 1);

			this.events().add(storedEvent);
		}

		return storedEvent;
	}

	@Override
	public long countStoredEvents() {
		int count;

		synchronized (this.events()) {
			count = this.events().size();
		}

		return count;
	}

	private List<StoredEvent> events() {
		return this.events;
	}

	private void setEvents(List<StoredEvent> anEvents) {
		this.events = anEvents;
	}

	private int nextEventId() {
		return this.nextEventId;
	}

	private void setNextEventId(int aNextEventId) {
		this.nextEventId = aNextEventId;
	}
}
