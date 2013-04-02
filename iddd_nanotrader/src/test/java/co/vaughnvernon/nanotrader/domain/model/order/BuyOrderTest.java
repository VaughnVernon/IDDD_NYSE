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

package co.vaughnvernon.nanotrader.domain.model.order;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.account.Holding;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.event.DomainEventSubscriber;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class BuyOrderTest extends TestCase {

	private static final int NUMBER_OF_SHARES = 5;
	private static final Money PRICE = new Money("720.43");
	private static final String TICKER = "GOOG";

	private BuyOrderFilled buyOrderFilled;
	private BuyOrderPlaced buyOrderPlaced;
	private int quantityOfSharesToPurchase;
	private BuyOrderSharePurchaseRequested buyOrderSharesToPurchase;

	public BuyOrderTest() {
		super();
	}

	public void testBuyOrderCreation() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderPlaced>() {
			@Override
			public void handleEvent(BuyOrderPlaced aDomainEvent) {
				buyOrderPlaced = aDomainEvent;
			}

			@Override
			public Class<BuyOrderPlaced> subscribedToEventType() {
				return BuyOrderPlaced.class;
			}
		});

		BuyOrder buyOrder = this.buyOrderFixture();

		assertNotNull(buyOrderPlaced);
		assertNotNull(buyOrder.accountId());
		assertNotNull(buyOrder.execution());
		assertFalse(buyOrder.isFilled());
		assertTrue(buyOrder.isOpen());
		assertEquals(NUMBER_OF_SHARES, buyOrder.quantityOfSharesOrdered());
		assertNotNull(buyOrder.quote());
		assertEquals(TICKER, buyOrder.quote().tickerSymbol().symbol());
		assertEquals(PRICE, buyOrder.quote().price());
		assertEquals(PRICE.multipliedBy(NUMBER_OF_SHARES), buyOrder.valueOfOrderedShares());

		try {
			buyOrder.holdingOfFilledOrder();

			fail("Holding must not have been created yet.");

		} catch (Exception e) {
			// success
		}
	}

	public void testFillBuyOrder() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderFilled>() {
			@Override
			public void handleEvent(BuyOrderFilled aDomainEvent) {
				buyOrderFilled = aDomainEvent;
			}

			@Override
			public Class<BuyOrderFilled> subscribedToEventType() {
				return BuyOrderFilled.class;
			}
		});

		BuyOrder buyOrder = this.buyOrderFixture(); // event published here

		buyOrder.sharesToPurchase(NUMBER_OF_SHARES);

		assertNotNull(buyOrderFilled);
		assertTrue(buyOrder.isFilled());
		assertFalse(buyOrder.isOpen());

		Holding holding = null;

		try {
			holding = buyOrder.holdingOfFilledOrder();

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

	public void testPurchaseForBuyOrder() throws Exception {

		final BuyOrder buyOrder = this.buyOrderFixture();

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderSharePurchaseRequested>() {
			@Override
			public void handleEvent(BuyOrderSharePurchaseRequested aDomainEvent) {
				buyOrderSharesToPurchase = aDomainEvent;
				quantityOfSharesToPurchase += aDomainEvent.quantityOfShares();
			}

			@Override
			public Class<BuyOrderSharePurchaseRequested> subscribedToEventType() {
				return BuyOrderSharePurchaseRequested.class;
			}
		});

		buyOrder.sharesToPurchase(NUMBER_OF_SHARES / 2);
		buyOrder.sharesToPurchase(0);
		buyOrder.sharesToPurchase(NUMBER_OF_SHARES / NUMBER_OF_SHARES);
		buyOrder.sharesToPurchase(NUMBER_OF_SHARES * 10);

		assertNotNull(buyOrderSharesToPurchase);
		assertEquals(NUMBER_OF_SHARES, quantityOfSharesToPurchase);
		assertEquals(0, buyOrder.quantityOfOutstandingShares());

		assertFalse(buyOrder.isOpen());
		assertTrue(buyOrder.isFilled());
	}

	public void testPlaceBuyOrder() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<BuyOrderPlaced>() {
			@Override
			public void handleEvent(BuyOrderPlaced aDomainEvent) {
				buyOrderPlaced = aDomainEvent;
			}

			@Override
			public Class<BuyOrderPlaced> subscribedToEventType() {
				return BuyOrderPlaced.class;
			}
		});

		BuyOrder buyOrder = this.buyOrderFixture(); // event published here

		assertNotNull(buyOrderPlaced);
		assertEquals(new Money("9.99"), buyOrderPlaced.orderFee());
		assertEquals(NUMBER_OF_SHARES, buyOrderPlaced.quantityOfSharesOrdered());
		assertEquals(PRICE, buyOrderPlaced.quote().price());
		assertEquals(TICKER, buyOrderPlaced.quote().tickerSymbol().symbol());
		assertEquals(PRICE.multipliedBy(NUMBER_OF_SHARES), buyOrder.valueOfOrderedShares());
		assertEquals(buyOrder.accountId(), buyOrderPlaced.accountId());
		assertEquals(buyOrder.orderId(), buyOrderPlaced.orderId());
		assertEquals(buyOrder.quantityOfSharesOrdered(), buyOrderPlaced.quantityOfSharesOrdered());
		assertEquals(buyOrder.quote(), buyOrderPlaced.quote());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();

		super.setUp();
	}

	private BuyOrder buyOrderFixture() {
		return new BuyOrder(
				AccountId.unique(),
				new Quote(new TickerSymbol(TICKER), PRICE),
				NUMBER_OF_SHARES,
				new Money("9.99"));
	}
}
