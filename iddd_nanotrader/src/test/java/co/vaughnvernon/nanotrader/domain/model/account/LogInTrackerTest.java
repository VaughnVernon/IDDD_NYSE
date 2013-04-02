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

package co.vaughnvernon.nanotrader.domain.model.account;

import java.util.Calendar;

import co.vaughnvernon.tradercommon.event.DomainEventPublisher;

import junit.framework.TestCase;

public class LogInTrackerTest extends TestCase {

	public LogInTrackerTest() {
		super();
	}

	public void testTrackLogIn() throws Exception {
		LogInTracker tracker = new LogInTracker();

		assertEquals(0, tracker.logInCount());
		assertEquals(0, tracker.logOutCount());
		assertFalse(tracker.hasLoggedIn());
		assertNull(tracker.lastLogInDate());

		tracker = tracker.trackLogIn();

		assertEquals(1, tracker.logInCount());
		assertEquals(0, tracker.logOutCount());
		assertTrue(tracker.hasLoggedIn());
		assertNotNull(tracker.lastLogInDate());

		Calendar lastLogInDate = Calendar.getInstance();
		lastLogInDate.setTime(tracker.lastLogInDate());

		assertEquals(Calendar.getInstance().get(Calendar.MONTH), lastLogInDate.get(Calendar.MONTH));
		assertEquals(Calendar.getInstance().get(Calendar.DATE), lastLogInDate.get(Calendar.DATE));
		assertEquals(Calendar.getInstance().get(Calendar.YEAR), lastLogInDate.get(Calendar.YEAR));
	}

	public void testTrackLogOut() throws Exception {
		LogInTracker tracker = new LogInTracker();

		assertEquals(0, tracker.logInCount());
		assertEquals(0, tracker.logOutCount());
		assertFalse(tracker.hasLoggedIn());
		assertNull(tracker.lastLogInDate());

		tracker = tracker.trackLogIn();

		assertEquals(1, tracker.logInCount());
		assertEquals(0, tracker.logOutCount());
		assertTrue(tracker.hasLoggedIn());
		assertNotNull(tracker.lastLogInDate());

		tracker = tracker.trackLogOut();

		assertEquals(1, tracker.logInCount());
		assertEquals(1, tracker.logOutCount());
		assertTrue(tracker.hasLoggedIn());
		assertNotNull(tracker.lastLogInDate());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}
}
