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

import java.util.Date;

import co.vaughnvernon.tradercommon.event.DomainEvent;

public class TestableDomainEvent implements DomainEvent {

	private int id;

	private Date occurredOn;

	public TestableDomainEvent(int anId) {
		super();

		this.setId(anId);
		this.setOccurredOn(new Date());
	}

	@Override
	public int eventVersion() {
		return 1;
	}

	public int id() {
		return this.id;
	}

	@Override
	public Date occurredOn() {
		return this.occurredOn;
	}

	@Override
	public String toString() {
		return "TestableDomainEvent [id=" + id + ", occurredOn=" + occurredOn + "]";
	}

	private void setId(int anId) {
		this.id = anId;
	}

	private void setOccurredOn(Date anOccurredOn) {
		this.occurredOn = anOccurredOn;
	}
}
