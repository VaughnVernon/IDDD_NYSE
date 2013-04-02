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

import java.util.Date;

public final class LogInTracker {

	private Date lastLogInDate;
	private int logInCount;
	private int logOutCount;

	public LogInTracker() {
		super();
	}

	public boolean hasLoggedIn() {
		return this.lastLogInDate() != null;
	}

	public Date lastLogInDate() {
		return this.lastLogInDate;
	}

	public int logInCount() {
		return this.logInCount;
	}

	public int logOutCount() {
		return this.logOutCount;
	}

	public LogInTracker trackLogIn() {
		return new LogInTracker(new Date(), this.logInCount() + 1, this.logOutCount());
	}

	public LogInTracker trackLogOut() {
		return new LogInTracker(this.lastLogInDate(), this.logInCount(), this.logOutCount() + 1);
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			LogInTracker typedObject = (LogInTracker) anObject;
			equalObjects =
					(this.lastLogInDate() == null && this.lastLogInDate() == typedObject.lastLogInDate() ||
					 this.lastLogInDate().equals(typedObject.lastLogInDate())) &&
					this.logInCount() == typedObject.logInCount() &&
					this.logOutCount() == typedObject.logOutCount();
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (17859 * 71)
			+ (this.lastLogInDate() == null ? 0 : this.lastLogInDate().hashCode())
			+ this.logInCount()
			+ this.logOutCount();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "LogInTracker [lastLogInDate=" + lastLogInDate + ", logInCount="
				+ logInCount + ", logOutCount=" + logOutCount + "]";
	}

	private LogInTracker(Date aLastLogInDate, int aLogInCount, int aLogOutCount) {
		this();

		this.setLastLogInDate(aLastLogInDate);
		this.setLogInCount(aLogInCount);
		this.setLogOutCount(aLogOutCount);
	}

	private void setLastLogInDate(Date aLastLogInDate) {
		this.lastLogInDate = aLastLogInDate;
	}

	private void setLogInCount(int logInCount) {
		this.logInCount = logInCount;
	}

	private void setLogOutCount(int logOutCount) {
		this.logOutCount = logOutCount;
	}
}
