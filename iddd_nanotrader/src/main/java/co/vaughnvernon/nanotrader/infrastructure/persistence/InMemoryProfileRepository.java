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

import java.util.HashMap;
import java.util.Map;

import co.vaughnvernon.nanotrader.domain.model.account.Profile;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileId;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileRepository;

public class InMemoryProfileRepository implements ProfileRepository {

	private static ProfileRepository instance;

	private Map<String,Profile> profiles;

	public static synchronized ProfileRepository instance() {
		if (instance == null) {
			instance = new InMemoryProfileRepository();
		}

		return instance;
	}

	public InMemoryProfileRepository() {
		super();

		this.setProfiles(new HashMap<String,Profile>());
	}

	@Override
	public Profile profileAuthorizedWith(String anAuthenticationToken) {
		for (Profile profile : this.profiles().values()) {
			if (profile.isAuthorizedWith(anAuthenticationToken)) {
				return profile;
			}
		}
		return null;
	}

	@Override
	public Profile profileOf(ProfileId aProfileId) {
		return this.profiles().get(aProfileId.id());
	}

	@Override
	public Profile profileOf(String aUserId, String anEncryptedPassword) {
		for (Profile profile : this.profiles().values()) {
			if (profile.userId().equals(aUserId) &&
					profile.password().equals(anEncryptedPassword)) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public void remove(Profile aProfile) {
		this.profiles().remove(aProfile.profileId().id());
	}

	@Override
	public void save(Profile aProfile) {
		this.profiles().put(aProfile.profileId().id(), aProfile);
	}

	private Map<String,Profile> profiles() {
		return this.profiles;
	}

	private void setProfiles(Map<String,Profile> aMap) {
		this.profiles = aMap;
	}
}
