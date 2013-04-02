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

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;

public class ProfileTest extends TestCase {

	private static final String NEW_PW = "some pw characters";
	private static final String PW = "E.n>c<r*y#p@t^e(d)Pw, Really!";
	private static final String USER = "walley";

	public ProfileTest() {
		super();
	}

	public void testProfileCreationWithDescriptor() throws Exception {

		Profile profile = this.profileFixture();

		assertNotNull(profile);
		assertNotNull(profile.profileId());
		assertEquals(USER, profile.userId());
		assertEquals(PW, profile.password());
		assertNull(profile.authenticationToken());
		assertFalse(profile.isAuthorizedWith("12345"));
		assertNotNull(profile.personalInformation());
		assertEquals(profile.personalInformation().emailAddress(), profile.profileDescriptor().emailAddress());
		assertEquals(profile.personalInformation().fullName(), profile.profileDescriptor().fullName());
		assertEquals(profile.authenticationToken(), profile.profileDescriptor().authenticationToken());
	}

	public void testChangePassword() throws Exception {

		Profile profile = this.profileFixture();

		assertNotNull(profile);
		assertEquals(PW, profile.password());

		profile.changePassword(NEW_PW);

		assertFalse(profile.password().equals("PW"));
		assertEquals(NEW_PW, profile.password());
	}

	public void testChangePersonalInformation() throws Exception {

		Profile profile = this.profileFixture();

		assertNotNull(profile);

		final String creditCard = "9876 5432 1987 6543";
		profile.changePersonalInformation(profile.personalInformation().withCreditCard(creditCard));
		assertEquals(creditCard, profile.personalInformation().creditCard());

		final String emailAddress = "me@jonesandjones.com";
		profile.changePersonalInformation(profile.personalInformation().withEmailAddress(emailAddress));
		assertEquals(emailAddress, profile.personalInformation().emailAddress());

		final String fullName = "Walley J. Jones";
		profile.changePersonalInformation(profile.personalInformation().withFullName(fullName));
		assertEquals(fullName, profile.personalInformation().fullName());

		final String postalAddress = "555 Mocking Bird Lane, Burnt Mattress, ID 83701";
		profile.changePersonalInformation(profile.personalInformation().withPostalAddress(postalAddress));
		assertEquals(postalAddress, profile.personalInformation().postalAddress());
	}

	public void testTrackLogIn() throws Exception {

		Profile profile = this.profileFixture();

		assertNull(profile.authenticationToken());
		assertEquals(0, profile.logInTracker().logInCount());
		assertEquals(0, profile.logInTracker().logOutCount());

		profile.trackLogIn();

		assertNotNull(profile.authenticationToken());
		assertEquals(1, profile.logInTracker().logInCount());
		assertEquals(0, profile.logInTracker().logOutCount());

		profile.trackLogOut();

		assertNull(profile.authenticationToken());
		assertEquals(1, profile.logInTracker().logInCount());
		assertEquals(1, profile.logInTracker().logOutCount());

		profile.trackLogIn();

		assertNotNull(profile.authenticationToken());
		assertEquals(2, profile.logInTracker().logInCount());
		assertEquals(1, profile.logInTracker().logOutCount());

		profile.trackLogOut();

		assertNull(profile.authenticationToken());
		assertEquals(2, profile.logInTracker().logInCount());
		assertEquals(2, profile.logInTracker().logOutCount());
	}

	public void testOpenAccount() throws Exception {

		Profile profile = this.profileFixture();

		Money money = new Money("1000.00");

		Account account = profile.openAccount(money);

		assertNotNull(account);
		assertEquals(profile.profileId(), account.profileId());
		assertEquals(money, account.openingBalance());
		assertEquals(money, account.cashBalance());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}

	private Profile profileFixture() {
		Profile profile =
				new Profile(
						USER,
						PW,
						"Walley Jones",
						"123 Main Street, Burnt Mattress, ID 83701",
						"walley@jonesnames.me",
						"1234 5678 9012 3456");

		return profile;
	}
}
