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

public final class ProfileDescriptor {

	private String authenticationToken;
	private String emailAddress;
	private String fullName;
	private String userId;

	public ProfileDescriptor(
			String aUserId,
			String aFullname,
			String anEmailAddress,
			String anAuthenticationToken) {
		super();

		this.setAuthenticationToken(anAuthenticationToken);
		this.setEmailAddress(anEmailAddress);
		this.setFullName(aFullname);
		this.setUserId(aUserId);
	}

	public String authenticationToken() {
		return this.authenticationToken;
	}

	public String emailAddress() {
		return this.emailAddress;
	}

	public String fullName() {
		return this.fullName;
	}

	public String userId() {
		return this.userId;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			ProfileDescriptor typedObject = (ProfileDescriptor) anObject;
			equalObjects =
					this.authenticationToken().equals(typedObject.authenticationToken()) &&
					this.emailAddress().equals(typedObject.emailAddress()) &&
					this.fullName().equals(typedObject.fullName()) &&
					this.userId().equals(typedObject.userId());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (58591 * 79)
			+ this.authenticationToken().hashCode()
			+ this.emailAddress().hashCode()
			+ this.fullName().hashCode()
			+ this.userId().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "ProfileDescriptor [userId=" + userId + ", fullName=" + fullName
				+ ", emailAddress=" + emailAddress + ", authenticationToken="
				+ authenticationToken + "]";
	}

	private void setAuthenticationToken(String anAuthenticationToken) {
		this.authenticationToken = anAuthenticationToken;
	}

	private void setEmailAddress(String anEmailAddress) {
		if (anEmailAddress == null || anEmailAddress.trim().isEmpty()) {
			throw new IllegalArgumentException("Email address must be provided.");
		}
		this.emailAddress = anEmailAddress;
	}

	private void setFullName(String aFullName) {
		if (aFullName == null || aFullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Full name must be provided.");
		}
		this.fullName = aFullName;
	}

	private void setUserId(String aUserId) {
		if (aUserId == null || aUserId.trim().isEmpty()) {
			throw new IllegalArgumentException("User id must be provided.");
		}
		this.userId = aUserId;
	}
}
