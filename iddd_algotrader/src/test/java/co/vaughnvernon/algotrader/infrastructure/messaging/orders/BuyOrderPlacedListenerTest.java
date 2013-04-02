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

package co.vaughnvernon.algotrader.infrastructure.messaging.orders;

import java.util.Collection;
import java.util.UUID;

import junit.framework.TestCase;
import co.vaughnvernon.algotrader.domain.model.order.AlgoOrder;
import co.vaughnvernon.algotrader.infrastructure.persistence.InMemoryAlgoOrderRepository;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.infrastructure.messaging.slothmq.ExchangePublisher;
import co.vaughnvernon.tradercommon.infrastructure.messaging.slothmq.SlothClient;
import co.vaughnvernon.tradercommon.infrastructure.messaging.slothmq.SlothServer;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;


public class BuyOrderPlacedListenerTest extends TestCase {

	private static final String serializedNotification =
			"{\"event\":{\"accountId\":{\"id\":\"2BA72DE0-2331-43A2-9BD3-253F7B018D4B\"},"
			+ "\"cost\":{\"amount\":3602.15,\"scale\":4},\"orderFee\":{\"amount\":9.99,\"scale\":4},"
			+ "\"occurredOn\":\"1364837024649\",\"orderId\":{\"id\":\"$ORDERID$\"},"
			+ "\"placedOnDate\":\"1364837024649\",\"quantityOfSharesOrdered\":$QUANTITY$,"
			+ "\"quote\":{\"price\":{\"amount\":720.43,\"scale\":4},\"quantity\":0,\"tickerSymbol\":{\"symbol\":\"GOOG\"}}},"
			+ "\"notificationId\":4606793901686,\"occurredOn\":\"1364837024649\","
			+ "\"typeName\":\"co.vaughnvernon.nanotrader.domain.model.order.BuyOrderPlaced\",\"version\":1}";

	private static final String notificationType =
			"co.vaughnvernon.nanotrader.domain.model.order.BuyOrderPlaced";

	public BuyOrderPlacedListenerTest() {
		super();
	}

	public void testBasicNotificationOfBuyOrderPlaced() throws Exception {
		BuyOrderPlacedListener listener = new BuyOrderPlacedListener();

		listener.filteredDispatch(
				notificationType,
				this.serializedBuyOrderPlacedNotification(100));

		listener.filteredDispatch(
				notificationType,
				this.serializedBuyOrderPlacedNotification(499));

		listener.filteredDispatch(
				notificationType,
				this.serializedBuyOrderPlacedNotification(500));

		listener.filteredDispatch(
				notificationType,
				this.serializedBuyOrderPlacedNotification(1000));

		Collection<AlgoOrder> unfilledOrders =
			InMemoryAlgoOrderRepository
				.instance()
				.unfilledBuyAlgoOrdersOfSymbol(new TickerSymbol("GOOG"));

		assertFalse(unfilledOrders.isEmpty());
		assertEquals(2, unfilledOrders.size());
	}

	public void testSlothNotificationOfBuyOrderPlaced() throws Exception {
		ExchangePublisher publisher = new ExchangePublisher("NanoTrader");

		publisher.publish(
				notificationType,
				this.serializedBuyOrderPlacedNotification(100));

		publisher.publish(
				notificationType,
				this.serializedBuyOrderPlacedNotification(499));

		publisher.publish(
				notificationType,
				this.serializedBuyOrderPlacedNotification(500));

		publisher.publish(
				notificationType,
				this.serializedBuyOrderPlacedNotification(1000));

		Thread.sleep(3000L);

		Collection<AlgoOrder> unfilledOrders =
			InMemoryAlgoOrderRepository
				.instance()
				.unfilledBuyAlgoOrdersOfSymbol(new TickerSymbol("GOOG"));

		assertFalse(unfilledOrders.isEmpty());
		assertEquals(2, unfilledOrders.size());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();

		InMemoryAlgoOrderRepository.clear();

		SlothServer.executeInProcessDetachedServer();

		Thread.sleep(1000L);

		SlothClient.instance().register(new BuyOrderPlacedListener());
	}

	@Override
	protected void tearDown() throws Exception {
		SlothClient.instance().closeAll();

		InMemoryAlgoOrderRepository.clear();
	}

	private String serializedBuyOrderPlacedNotification(int aQuantityOfShares) {
		String notification =
				serializedNotification
					.replace(
							"$QUANTITY$",
							"" + aQuantityOfShares)
					.replace(
							"$ORDERID$",
							UUID.randomUUID().toString().toUpperCase());

		return notification;
	}
}
