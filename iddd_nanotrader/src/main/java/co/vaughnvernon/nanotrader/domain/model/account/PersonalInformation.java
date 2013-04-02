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

public final class PersonalInformation {

	private String creditCard; // weak model
	private String emailAddress; // weak model
	private String fullName; // weak model
	private String postalAddress; // weak model

	public PersonalInformation(
			String aFullName,
			String aPostalAddress,
			String anEmailAddress,
			String aCreditCard) {
		super();

		this.setCreditCard(aCreditCard);
		this.setEmailAddress(anEmailAddress);
		this.setFullName(aFullName);
		this.setPostalAddress(aPostalAddress);
	}

	public String creditCard() {
		return this.creditCard;
	}

	public PersonalInformation withCreditCard(String aCreditCard) {
		return new PersonalInformation(this.fullName(), this.postalAddress(), this.emailAddress(), aCreditCard);
	}

	public String emailAddress() {
		return this.emailAddress;
	}

	public PersonalInformation withEmailAddress(String anEmailAddress) {
		return new PersonalInformation(this.fullName(), this.postalAddress(), anEmailAddress, this.creditCard());
	}

	public String fullName() {
		return this.fullName;
	}

	public PersonalInformation withFullName(String aFullName) {
		return new PersonalInformation(aFullName, this.postalAddress(), this.emailAddress(), this.creditCard());
	}

	public String postalAddress() {
		return this.postalAddress;
	}

	public PersonalInformation withPostalAddress(String aPostalAddress) {
		return new PersonalInformation(this.fullName(), aPostalAddress, this.emailAddress(), this.creditCard());
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			PersonalInformation typedObject = (PersonalInformation) anObject;
			equalObjects =
					this.creditCard().equals(typedObject.creditCard()) &&
					this.emailAddress().equals(typedObject.emailAddress()) &&
					this.fullName().equals(typedObject.fullName()) &&
					this.postalAddress().equals(typedObject.postalAddress());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (35123 * 73)
			+ this.creditCard().hashCode()
			+ this.emailAddress().hashCode()
			+ this.fullName().hashCode()
			+ this.postalAddress().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "PersonalInformation [fullName=" + fullName + ", postalAddress="
				+ postalAddress + ", emailAddress=" + emailAddress
				+ ", creditCard=" + creditCard + "]";
	}

	private void setCreditCard(String aCreditCard) {
		if (aCreditCard == null || aCreditCard.trim().isEmpty()) {
			throw new IllegalArgumentException("Credit card must be provided.");
		}
		this.creditCard = aCreditCard;
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

	private void setPostalAddress(String aPostalAddress) {
		if (aPostalAddress == null || aPostalAddress.trim().isEmpty()) {
			throw new IllegalArgumentException("Postal address must be provided.");
		}
		this.postalAddress = aPostalAddress;
	}
}
