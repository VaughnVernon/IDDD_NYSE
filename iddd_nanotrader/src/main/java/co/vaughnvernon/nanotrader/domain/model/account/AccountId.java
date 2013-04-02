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

public final class AccountId {

	private String id;

	public static AccountId unique() {
		return new AccountId(UUID.randomUUID().toString().toUpperCase());
	}

	public AccountId(String anId) {
		super();

		this.setId(anId);
	}

	public String id() {
		return this.id;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			AccountId typedObject = (AccountId) anObject;
			equalObjects =
					this.id().equals(typedObject.id());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (35071 * 59)
			+ this.id().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "AccountId [id=" + id + "]";
	}

	private void setId(String anId) {
		if (anId == null || anId.length() == 0) {
			throw new IllegalArgumentException("Id must be provided.");
		}
		if (anId.length() > 36) {
			throw new IllegalArgumentException("Id must be 36 characters.");
		}
		this.id = anId;
	}
}
