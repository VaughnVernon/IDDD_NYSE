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

public interface ProfileRepository {

	public Profile profileAuthorizedWith(String anAuthenticationToken);

	public Profile profileOf(ProfileId aProfileId);

	public Profile profileOf(String aUserId, String anEncryptedPassword);

	public void remove(Profile aProfile);

	public void save(Profile aProfile);
}
