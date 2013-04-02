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

import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.event.DomainEvent;
import co.vaughnvernon.tradercommon.event.EventStore;
import co.vaughnvernon.tradercommon.event.StoredEvent;

public class InMemoryEventStoreTest extends TestCase {

	private EventStore eventStore = new InMemoryEventStore();

	public InMemoryEventStoreTest() {
		super();
	}

	public void testAllStoredEventsBetween() throws Exception {
		for (int id = 1; id <= 20; ++id) {
			DomainEvent event = new TestableDomainEvent(id);

			this.eventStore.append(event);
		}

		List<StoredEvent> events = this.eventStore.allStoredEventsBetween(10, 15);

		assertEquals(6, events.size());

		int id = 10;

		for (StoredEvent storedEvent : events) {
			assertEquals(id, storedEvent.eventId());

			TestableDomainEvent event = storedEvent.toDomainEvent();

			assertEquals(id, event.id());

			++id;
		}
	}

	public void testAllStoredEventsSince() throws Exception {
		for (int id = 1; id <= 20; ++id) {
			DomainEvent event = new TestableDomainEvent(id);

			this.eventStore.append(event);
		}

		List<StoredEvent> events = this.eventStore.allStoredEventsSince(11);

		assertEquals(9, events.size());

		int id = 12;

		for (StoredEvent storedEvent : events) {
			assertEquals(id, storedEvent.eventId());

			TestableDomainEvent event = storedEvent.toDomainEvent();

			assertEquals(id, event.id());

			++id;
		}
	}

	public void testAppend() throws Exception {
		for (int id = 1; id <= 20; ++id) {
			TestableDomainEvent event = new TestableDomainEvent(id);

			StoredEvent storedEvent = this.eventStore.append(event);

			assertEquals(event.id(), storedEvent.eventId());
		}

		assertEquals(20, this.eventStore.countStoredEvents());
	}

	public void testCountStoredEvents() throws Exception {
		for (int id = 1; id <= 20; ++id) {
			DomainEvent event = new TestableDomainEvent(id);

			this.eventStore.append(event);
		}

		assertEquals(20, this.eventStore.countStoredEvents());
	}
}
