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
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.account.Account;
import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.account.AccountRepository;
import co.vaughnvernon.nanotrader.domain.model.account.Holding;
import co.vaughnvernon.nanotrader.domain.model.account.Profile;
import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class InMemoryAccountRepositoryTest extends TestCase {

	private static final String PW = "E.n>c<r*y#p@t^e(d)Pw, Really!";
	private static final String USER = "walley";

	private AccountRepository repository;

	public InMemoryAccountRepositoryTest() {
		super();

		this.repository = InMemoryAccountRepository.instance();
	}

	public void testAccountOf() throws Exception {

		Account account = this.accountFixture();

		this.repository.save(account);

		Account foundAccount = this.repository.accountOf(account.accountId());

		assertEquals(account, foundAccount);
	}

	public void testRemove() throws Exception {

		Account account = this.accountFixture();

		this.repository.save(account);

		Account foundAccount = this.repository.accountOf(account.accountId());

		assertEquals(account, foundAccount);

		this.repository.remove(account);

		foundAccount = this.repository.accountOf(account.accountId());

		assertNull(foundAccount);
	}

	public void testSave() throws Exception {

		List<AccountId> ids = new ArrayList<AccountId>();

		for (int idx = 0; idx < 10; ++idx) {

			Account account = this.accountFixture();

			this.repository.save(account);

			ids.add(account.accountId());
		}

		for (int idx = 0; idx < 10; ++idx) {

			Account account = this.repository.accountOf(ids.get(idx));

			assertNotNull(account);
		}
	}

	public void testSaveWithOverwrite() throws Exception {
		Account account = this.accountFixture();

		this.repository.save(account);

		assertTrue(account.holdings().isEmpty());

		Account changedAccount = this.repository.accountOf(account.accountId());

		Holding holding = new Holding(
				account.accountId(),
				OrderId.unique(),
				new TickerSymbol("GOOG"),
				2,
				new Money("1490.25"),
				new Date());

		changedAccount.reconcileWith(holding);

		this.repository.save(changedAccount);

		changedAccount = this.repository.accountOf(account.accountId());

		assertFalse(changedAccount.holdings().isEmpty());

		assertEquals(1, changedAccount.holdings().size());
	}

	private Account accountFixture() {
		Profile profile = this.profileFixture();

		Account account =
				new Account(
						profile.profileId(),
						new Money("1000.00"));

		return account;
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
