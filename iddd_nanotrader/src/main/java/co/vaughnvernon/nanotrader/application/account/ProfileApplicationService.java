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

package co.vaughnvernon.nanotrader.application.account;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import co.vaughnvernon.nanotrader.domain.model.account.Profile;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileDescriptor;
import co.vaughnvernon.nanotrader.domain.model.account.ProfileRepository;

public class ProfileApplicationService {

	private ProfileRepository profileRepository;

	public ProfileApplicationService(ProfileRepository aProfileRepository) {
		super();

		this.profileRepository = aProfileRepository;
	}

	public ProfileDescriptor logIn(
			String aUserId,
			String aPlainTextPassword) {

        if (aUserId == null) {
            throw new IllegalArgumentException("UserId must not be null.");
        }
        if (aPlainTextPassword == null) {
            throw new IllegalArgumentException("Plain text password must not be null.");
        }

        Profile profile =
                this.profileRepository()
                	.profileOf(
                			aUserId,
                			this.encryptPassword(aPlainTextPassword));

        ProfileDescriptor profileDescriptor = null;

        if (profile != null) {
        	profile.trackLogIn();

        	this.profileRepository().save(profile);

            profileDescriptor = profile.profileDescriptor();
        }

        return profileDescriptor;
	}

	public void logOut(String anAuthenticationToken) {

        if (anAuthenticationToken == null) {
            throw new IllegalArgumentException("Authentication token must not be null.");
        }

        Profile profile =
                this.profileRepository()
                	.profileAuthorizedWith(anAuthenticationToken);

        if (profile != null) {
        	profile.trackLogOut();

        	this.profileRepository().save(profile);
        }
	}

	public String registerProfile(
			String aUserId,
			String aPlainTextPassword,
			String aFullName,
			String aPostalAddress,
			String anEmailAddress,
			String aCreditCard) {

		Profile profile = new Profile(
				aUserId,
				this.encryptPassword(aPlainTextPassword),
				aFullName,
				aPostalAddress,
				anEmailAddress,
				aCreditCard);

		this.profileRepository().save(profile);

		return profile.profileId().id();
	}

    private String encryptPassword(String aPlainTextPassword) {
		if (aPlainTextPassword == null) {
			throw new IllegalArgumentException("Plain text password must not be null.");
		}

		String encryptedPassword = null;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(aPlainTextPassword.getBytes("UTF-8"));
			BigInteger bigInt = new BigInteger(1, messageDigest.digest());
			encryptedPassword = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}

		return encryptedPassword;
    }

    private ProfileRepository profileRepository() {
    	return this.profileRepository;
    }
}
