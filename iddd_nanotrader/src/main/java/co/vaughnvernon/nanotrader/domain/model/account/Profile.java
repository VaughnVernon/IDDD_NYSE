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

import java.util.UUID;

import co.vaughnvernon.tradercommon.monetary.Money;

// Profile seems to be a misplaced or at least a confused concept.
// It probably should be managed in an I&A context, but I am leaving
// it here to keep the model as similar to the original as tolerable.
// However, the design of the object itself is significantly improved.

public class Profile {

	private String authenticationToken;
	private LogInTracker logInTracker;
	private String password;
	private PersonalInformation personalInformation;
	private ProfileId profileId;
	private String userId;

	public Profile(
			String aUserId,
			String anEncryptedPassword,
			String aFullName,
			String aPostalAddress,
			String anEmailAddress,
			String aCreditCard) {
		super();

		this.setAuthenticationToken(null);
		this.setLogInTracker(new LogInTracker());
		this.setPassword(anEncryptedPassword);
		this.setPersonalInformation(
				new PersonalInformation(
						aFullName,
						aPostalAddress,
						anEmailAddress,
						aCreditCard));
		this.setProfileId(ProfileId.unique());
		this.setUserId(aUserId);
	}

	public String authenticationToken() {
		return this.authenticationToken;
	}

	public void changePassword(String anEncryptedPassword) {
		this.setPassword(anEncryptedPassword);
	}

	public void changePersonalInformation(PersonalInformation aPersonalInformation) {
		this.setPersonalInformation(aPersonalInformation);
	}

	public boolean isAuthorizedWith(String anAuthenticationToken) {
		return this.authenticationToken() != null &&
			   this.authenticationToken().equals(anAuthenticationToken);
	}

	public LogInTracker logInTracker() {
		return this.logInTracker;
	}

	public Account openAccount(Money anOpeningBalance) {
		return new Account(this.profileId(), anOpeningBalance);
	}

	public String password() {
		return this.password;
	}

	public PersonalInformation personalInformation() {
		return this.personalInformation;
	}

	public ProfileDescriptor profileDescriptor() {
		return new ProfileDescriptor(
				this.userId(),
				this.personalInformation().fullName(),
				this.personalInformation().emailAddress(),
				this.authenticationToken());
	}

	public ProfileId profileId() {
		return this.profileId;
	}

	public void trackLogIn() {
		this.setAuthenticationToken(UUID.randomUUID().toString().toUpperCase());

		this.setLogInTracker(this.logInTracker().trackLogIn());
	}

	public void trackLogOut() {
		this.setAuthenticationToken(null);

		this.setLogInTracker(this.logInTracker().trackLogOut());
	}

	public String userId() {
		return this.userId;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Profile typedObject = (Profile) anObject;
			equalObjects =
					this.profileId().equals(typedObject.profileId());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (41351 * 67)
			+ this.profileId().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Profile [profileId=" + profileId + ", userId=" + userId
				+ ", personalInformation=" + personalInformation
				+ ", logInTracker=" + logInTracker + ", authenticationToken="
				+ authenticationToken + "]";
	}

	private void setAuthenticationToken(String anAuthenticationToken) {
		this.authenticationToken = anAuthenticationToken;
	}

	private void setLogInTracker(LogInTracker aLogInTracker) {
		this.logInTracker = aLogInTracker;
	}

	private void setPassword(String anEncryptedPassword) {
		this.password = anEncryptedPassword;
	}

	private void setPersonalInformation(PersonalInformation aPersonalInformation) {
		if (aPersonalInformation == null) {
			throw new IllegalArgumentException("Personal information id must be provided.");
		}
		this.personalInformation = aPersonalInformation;
	}

	private void setProfileId(ProfileId aProfileId) {
		if (aProfileId == null) {
			throw new IllegalArgumentException("ProfileId must be provided.");
		}
		this.profileId = aProfileId;
	}

	private void setUserId(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User id must be provided.");
		}
		this.userId = userId;
	}
}
