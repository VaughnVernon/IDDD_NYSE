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

import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;
import junit.framework.TestCase;

public class HoldingTest extends TestCase {

	public HoldingTest() {
		super();
	}

	public void testHoldingAfterFill() throws Exception {
		BuyOrder buyOrder = this.buyOrderFixture();

		assertTrue(buyOrder.isOpen());

		assertFalse(buyOrder.isFilled());

		buyOrder.sharesToPurchase(buyOrder.execution().quantityOfSharesOrdered());

		try {
			Holding holding = buyOrder.holdingOfFilledOrder();

			assertNotNull(holding);

			assertEquals(buyOrder.accountId(), holding.accountId());
			assertEquals(buyOrder.holdingOfFilledOrder().acquiredOn(), holding.acquiredOn());
			assertEquals(buyOrder.orderId(), holding.orderId());
			assertEquals(buyOrder.quantityOfSharesOrdered(), holding.numberOfShares());
			assertEquals(buyOrder.quote().tickerSymbol(), holding.tickerSymbol());
			assertEquals(buyOrder.quote().price().multipliedBy(buyOrder.quantityOfSharesOrdered()), holding.totalValue());

		} catch (Exception e) {
			fail("Holding should be available with filled order.");
		}
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}

	private BuyOrder buyOrderFixture() {
		BuyOrder buyOrder = new BuyOrder(
				AccountId.unique(),
				new Quote(new TickerSymbol("GOOG"), new Money("731.30")),
				2,
				new Money("12.99"));

		return buyOrder;
	}
}
