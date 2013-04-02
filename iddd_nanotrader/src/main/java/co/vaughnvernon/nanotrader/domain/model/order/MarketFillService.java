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

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.event.DomainEventSubscriber;

public class MarketFillService {

	private BuyOrderRepository buyOrderRepository;

	public MarketFillService(BuyOrderRepository aBuyOrderRepository) {
		super();

		this.buyOrderRepository = aBuyOrderRepository;
	}

	public void fillMarketBuyOrders(
			Collection<BuyOrder> anOpenBuyOrders,
			int aTotalQuantityAvailable) {

		BuyOrderSharePurchaseRequestedSubscriber subscriber =
				new BuyOrderSharePurchaseRequestedSubscriber();

		DomainEventPublisher.instance().subscribe(subscriber);

		Iterator<BuyOrder> iterator = anOpenBuyOrders.iterator();

		while (iterator.hasNext() && aTotalQuantityAvailable > 0) {

			BuyOrder buyOrder = iterator.next();

			boolean done = false;
			for (int tries = 0; !done && tries < 3; ++tries) {
				subscriber.clear();

				if (this.trySharesToPurchase(buyOrder, aTotalQuantityAvailable)) {
					aTotalQuantityAvailable -= subscriber.orderSharesRequested();
					done = true;
				} else {
					buyOrder = this.buyOrderRepository().orderOf(buyOrder.orderId());
				}
			}
		}
	}

	private BuyOrderRepository buyOrderRepository() {
		return this.buyOrderRepository;
	}

	private boolean trySharesToPurchase(
			BuyOrder aBuyOrder,
			int aTotalQuantityAvailable) {

		try {
			aBuyOrder.sharesToPurchase(aTotalQuantityAvailable);

			this.buyOrderRepository().save(aBuyOrder);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private class BuyOrderSharePurchaseRequestedSubscriber
			implements DomainEventSubscriber<BuyOrderSharePurchaseRequested> {
		private AtomicInteger orderSharesRequested;

		BuyOrderSharePurchaseRequestedSubscriber() {
			super();

			this.orderSharesRequested = new AtomicInteger(0);
		}

		@Override
		public void handleEvent(BuyOrderSharePurchaseRequested aDomainEvent) {
			this.orderSharesRequested.set(aDomainEvent.quantityOfShares());
		}

		@Override
		public Class<BuyOrderSharePurchaseRequested> subscribedToEventType() {
			return BuyOrderSharePurchaseRequested.class;
		}

		protected void clear() {
			this.orderSharesRequested.set(0);
		}

		protected int orderSharesRequested() {
			return this.orderSharesRequested.get();
		}
	}
}
