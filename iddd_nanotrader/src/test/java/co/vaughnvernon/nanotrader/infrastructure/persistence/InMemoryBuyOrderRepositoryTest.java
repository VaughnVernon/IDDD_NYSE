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
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrderRepository;
import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class InMemoryBuyOrderRepositoryTest extends TestCase {

	private BuyOrderRepository repository;

	public InMemoryBuyOrderRepositoryTest() {
		super();

		this.repository = new InMemoryBuyOrderRepository();
	}

	public void testOpenOrdersOf() throws Exception {
		this.repository.save(this.buyOrderFixture1());

		this.repository.save(this.buyOrderFixture2());

		this.repository.save(this.buyOrderFixture3());

		Collection<BuyOrder> foundOrders =
				this.repository.openOrdersOf(this.googTickerFixture());

		assertNotNull(foundOrders);

		assertFalse(foundOrders.isEmpty());

		assertEquals(2, foundOrders.size());
	}

	public void testOrderOf() throws Exception {
		BuyOrder buyOrder = this.buyOrderFixture1();

		this.repository.save(buyOrder);

		BuyOrder foundOrder = this.repository.orderOf(buyOrder.orderId());

		assertNotNull(foundOrder);

		assertEquals(buyOrder, foundOrder);
	}

	public void testRemove() throws Exception {

		BuyOrder buyOrder = this.buyOrderFixture1();

		this.repository.save(buyOrder);

		BuyOrder foundOrder = this.repository.orderOf(buyOrder.orderId());

		assertEquals(buyOrder, foundOrder);

		this.repository.remove(buyOrder);

		foundOrder = this.repository.orderOf(buyOrder.orderId());

		assertNull(foundOrder);
	}

	public void testSave() throws Exception {

		List<OrderId> ids = new ArrayList<OrderId>();

		for (int idx = 0; idx < 10; ++idx) {

			BuyOrder order = this.buyOrderFixture1();

			this.repository.save(order);

			ids.add(order.orderId());
		}

		for (int idx = 0; idx < 10; ++idx) {

			BuyOrder order = this.repository.orderOf(ids.get(idx));

			assertNotNull(order);
		}
	}

	public void testSaveWithOverwrite() throws Exception {
		BuyOrder buyOrder = this.buyOrderFixture1();

		this.repository.save(buyOrder);

		assertTrue(buyOrder.isOpen());

		assertFalse(buyOrder.isFilled());

		BuyOrder changedBuyOrder = this.repository.orderOf(buyOrder.orderId());

		changedBuyOrder.sharesToPurchase(changedBuyOrder.execution().quantityOfSharesOrdered());

		this.repository.save(changedBuyOrder);

		changedBuyOrder = this.repository.orderOf(buyOrder.orderId());

		assertNotNull(changedBuyOrder.holdingOfFilledOrder());

		assertFalse(buyOrder.isOpen());

		assertTrue(buyOrder.isFilled());
	}

	private BuyOrder buyOrderFixture1() {
		BuyOrder buyOrder = new BuyOrder(
				AccountId.unique(),
				new Quote(this.googTickerFixture(), new Money("731.30")),
				2,
				new Money("12.99"));

		return buyOrder;
	}

	private BuyOrder buyOrderFixture2() {
		BuyOrder buyOrder = new BuyOrder(
				AccountId.unique(),
				new Quote(this.googTickerFixture(), new Money("730.89")),
				5,
				new Money("12.99"));

		return buyOrder;
	}

	private BuyOrder buyOrderFixture3() {
		BuyOrder buyOrder = new BuyOrder(
				AccountId.unique(),
				new Quote(new TickerSymbol("MSFT"), new Money("27.71")),
				20,
				new Money("12.99"));

		return buyOrder;
	}

	private TickerSymbol googTickerFixture() {
		return new TickerSymbol("GOOG");
	}
}
