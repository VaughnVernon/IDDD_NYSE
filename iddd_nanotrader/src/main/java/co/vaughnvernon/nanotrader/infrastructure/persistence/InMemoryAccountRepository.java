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

import co.vaughnvernon.nanotrader.domain.model.account.Account;
import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.account.AccountRepository;

public class InMemoryAccountRepository implements AccountRepository {

	private static AccountRepository instance;

	private Map<String,Account> accounts;

	public static synchronized AccountRepository instance() {
		if (instance == null) {
			instance = new InMemoryAccountRepository();
		}

		return instance;
	}

	@Override
	public Account accountOf(AccountId anAccountId) {
		return this.accounts().get(anAccountId.id());
	}

	@Override
	public void remove(Account anAccount) {
		this.accounts().remove(anAccount.accountId().id());
	}

	@Override
	public void save(Account anAccount) {
		this.accounts().put(anAccount.accountId().id(), anAccount);
	}

	private Map<String,Account> accounts() {
		return this.accounts;
	}

	private void setAccounts(Map<String,Account> aMap) {
		this.accounts = aMap;
	}

	private InMemoryAccountRepository() {
		super();

		this.setAccounts(new HashMap<String,Account>());
	}
}
