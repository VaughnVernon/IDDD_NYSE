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

import java.util.Calendar;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrderFilled;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrderPlaced;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.event.DomainEventSubscriber;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class AccountTest extends TestCase {

	private Account reconcilableAccount;

	public AccountTest() {
		super();
	}

	public void testOpenAccount() throws Exception {

		Profile profile = this.profileFixture();

		Money money = new Money("1000.00");

		Account account = profile.openAccount(money);

		assertNotNull(account);
		assertNotNull(account.accountId());
		assertEquals(profile.profileId(), account.profileId());
		assertEquals(money, account.openingBalance());
		assertEquals(money, account.cashBalance());
		assertEquals(0, account.holdings().size());

		Calendar establishedOn = Calendar.getInstance();
		establishedOn.setTime(account.establishedOn());
		Calendar now = Calendar.getInstance();

		assertEquals(now.get(Calendar.MONTH), establishedOn.get(Calendar.MONTH));
		assertEquals(now.get(Calendar.DATE), establishedOn.get(Calendar.DATE));
		assertEquals(now.get(Calendar.YEAR), establishedOn.get(Calendar.YEAR));
	}

	public void testPlaceBuyOrder() throws Exception {

		Money money = new Money("10000.00");

		Account account = this.profileFixture().openAccount(money);

		Money orderFee = new Money("9.99");
		Money price = new Money("723.25");
		int shares = 10;
		TickerSymbol tickerSymbol = new TickerSymbol("GOOG");

		BuyOrder buyOrder = account.placeBuyOrder(tickerSymbol, price, shares, orderFee);

		assertEquals(account.accountId(), buyOrder.accountId());
		assertEquals(tickerSymbol, buyOrder.quote().tickerSymbol());
		assertEquals(price, buyOrder.quote().price());
		assertEquals(shares, buyOrder.quantityOfSharesOrdered());
		assertEquals(shares, buyOrder.execution().quantityOfSharesOrdered());
		assertEquals(shares, buyOrder.execution().quantityOfSharesOutstanding());
	}

	public void testReconcileWith() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderPlaced>() {
			@Override
			public void handleEvent(BuyOrderPlaced aDomainEvent) {
				reconcilableAccount.reconcileWith(
						new Payment(
								aDomainEvent.accountId(),
								aDomainEvent.orderId(),
								"BUY: "
										+ aDomainEvent.quantityOfSharesOrdered()
										+ " of "
										+ aDomainEvent.quote().tickerSymbol()
										+ " at "
										+ aDomainEvent.quote().price(),
								aDomainEvent.cost(),
								aDomainEvent.orderFee(),
								aDomainEvent.placedOnDate()));
			}

			@Override
			public Class<BuyOrderPlaced> subscribedToEventType() {
				return BuyOrderPlaced.class;
			}
		});

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderFilled>() {
			@Override
			public void handleEvent(BuyOrderFilled aDomainEvent) {
				reconcilableAccount.reconcileWith(aDomainEvent.holding());
			}

			@Override
			public Class<BuyOrderFilled> subscribedToEventType() {
				return BuyOrderFilled.class;
			}
		});

		Money openingBalance = new Money("10000.00");

		reconcilableAccount = this.profileFixture().openAccount(openingBalance);

		assertTrue(reconcilableAccount.holdings().isEmpty());

		Money orderFee = new Money("9.99");
		Money price = new Money("723.25");
		int shares = 10;
		TickerSymbol tickerSymbol = new TickerSymbol("GOOG");

		BuyOrder buyOrder = reconcilableAccount.placeBuyOrder(tickerSymbol, price, shares, orderFee);

		buyOrder.sharesToPurchase(shares);

		assertFalse(reconcilableAccount.holdings().isEmpty());
		assertEquals(1, reconcilableAccount.holdings().size());

		// the current balance should be openinBalance minus orderFee
		// since the debit of the cost and the credit of the new holding
		// is the same
		assertEquals(openingBalance.subtract(orderFee), reconcilableAccount.currentBalance());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}

	private Profile profileFixture() {
		Profile profile =
				new Profile(
						"blah",
						"blahblah",
						"Walley Jones",
						"123 Main Street, Burnt Mattress, ID 83701",
						"walley@jonesnames.me",
						"1234 5678 9012 3456");

		return profile;
	}
}
