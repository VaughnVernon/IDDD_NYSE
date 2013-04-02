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

package co.vaughnvernon.nanotrader.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.account.Profile;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileId;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileRepository;

public class InMemoryProfileRepositoryTest extends TestCase {

	private ProfileRepository repository = new InMemoryProfileRepository();

	private static final String PW = "E.n>c<r*y#p@t^e(d)Pw, Really!";
	private static final String USER = "walley";

	public InMemoryProfileRepositoryTest() {
		super();
	}

	public void testProfileAuthorizedWith() throws Exception {

		Profile profile = this.profileFixture();

		assertNull(profile.authenticationToken());

		this.repository.save(profile);

		Profile changedProfile = this.repository.profileOf(profile.profileId());

		changedProfile.trackLogIn();

		assertNotNull(changedProfile.authenticationToken());

		this.repository.save(changedProfile);

		Profile foundProfile = this.repository.profileAuthorizedWith(changedProfile.authenticationToken());

		assertNotNull(foundProfile);

		assertEquals(profile, foundProfile);
	}

	public void testProfileOf() throws Exception {

		Profile profile = this.profileFixture();

		this.repository.save(profile);

		Profile foundProfile = this.repository.profileOf(USER, PW);

		assertNotNull(foundProfile);

		assertEquals(profile, foundProfile);
	}

	public void testRemove() throws Exception {

		Profile profile = this.profileFixture();

		this.repository.save(profile);

		Profile foundProfile = this.repository.profileOf(USER, PW);

		assertEquals(profile, foundProfile);

		this.repository.remove(profile);

		foundProfile = this.repository.profileOf(USER, PW);

		assertNull(foundProfile);
	}

	public void testSave() throws Exception {

		List<ProfileId> ids = new ArrayList<ProfileId>();

		for (int idx = 0; idx < 10; ++idx) {

			Profile profile = this.profileFixture();

			this.repository.save(profile);

			ids.add(profile.profileId());
		}

		for (int idx = 0; idx < 10; ++idx) {

			Profile profile = this.repository.profileOf(ids.get(idx));

			assertNotNull(profile);
		}
	}

	public void testSaveWithOverwrite() throws Exception {
		Profile profile = this.profileFixture();

		assertNull(profile.authenticationToken());

		this.repository.save(profile);

		Profile changedProfile = this.repository.profileOf(profile.profileId());

		changedProfile.trackLogIn();

		assertNotNull(changedProfile.authenticationToken());

		this.repository.save(changedProfile);

		Profile changedAgainProfile = this.repository.profileAuthorizedWith(changedProfile.authenticationToken());

		assertNotNull(changedAgainProfile);

		assertEquals(profile, changedAgainProfile);

		changedAgainProfile.trackLogOut();

		this.repository.save(changedAgainProfile);

		Profile foundAgainProfile = this.repository.profileOf(profile.profileId());

		assertNull(foundAgainProfile.authenticationToken());

		assertNotNull(foundAgainProfile);

		assertEquals(profile, foundAgainProfile);
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
