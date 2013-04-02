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

package co.vaughnvernon.algotrader.domain.model.vwap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import co.vaughnvernon.algotrader.domain.model.order.AlgoOrder;
import co.vaughnvernon.algotrader.domain.model.order.AlgoOrderRepository;
import co.vaughnvernon.algotrader.domain.model.order.AlgoSliceOrderSharesRequested;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.event.DomainEventSubscriber;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;
import co.vaughnvernon.tradercommon.quotebar.QuoteBar;

public class VWAPTradingService {

	private AlgoOrderRepository algoOrderRepository;
	private VWAPAnalyticRepository vwapAnalyticRepository;

	public VWAPTradingService(
			VWAPAnalyticRepository aVWAPAnalyticRepository,
			AlgoOrderRepository anAlgoOrderRepository) {

		super();

		this.algoOrderRepository = anAlgoOrderRepository;
		this.vwapAnalyticRepository = aVWAPAnalyticRepository;
	}

	public void tradeUnfilledBuyOrdersUsing(QuoteBar aQuoteBar) {
		VWAPAnalytic vwapAnalytic =
				this.findAndAccumulateVWAPAnalyticUsing(aQuoteBar);

		if (vwapAnalytic.isReadyToTrade()) {
			this.tradeUnfilledBuyOrdersUsing(aQuoteBar, vwapAnalytic);
		}
	}

	private AlgoOrderRepository algoOrderRepository() {
		return this.algoOrderRepository;
	}

	private void attemptTradeFor(
			AlgoOrder anAlgoOrder,
			VWAPAnalytic aVWAPAnalytic,
			AlgoSliceOrderSharesRequestedSubscriber aSubscriber) {

		boolean done = false;
		for (int tries = 0; !done && tries < 3; ++tries) {
			try {
				aSubscriber.clear();

				anAlgoOrder.requestSlice(aVWAPAnalytic.vwap(), 100);

				this.algoOrderRepository().save(anAlgoOrder);

				done = true;

			} catch (Exception e) {
				anAlgoOrder = this.algoOrderRepository().algoOrderOfId(anAlgoOrder.orderId());
			}
		}
	}

	private VWAPAnalytic findAndAccumulateVWAPAnalyticUsing(QuoteBar aQuoteBar) {
		VWAPAnalytic vwapAnalytic =
				this.vwapAnalyticRepository()
					.vwapAnalyticOfSymbol(aQuoteBar.symbol());

		if (vwapAnalytic != null) {
			vwapAnalytic.accumulatePriceVolume(
					new PriceVolume(
							aQuoteBar.priceVolume(),
							aQuoteBar.volume()));
		} else {
			vwapAnalytic =
					new VWAPAnalytic(
							aQuoteBar.symbol(),
							new PriceVolume(
									aQuoteBar.priceVolume(),
									aQuoteBar.volume()));
		}

		this.vwapAnalyticRepository().save(vwapAnalytic);

		return vwapAnalytic;
	}

	private void tradeUnfilledBuyOrdersUsing(QuoteBar aQuoteBar, VWAPAnalytic aVWAPAnalytic) {
		Collection<AlgoOrder> algoOrders =
				this.algoOrderRepository()
					.unfilledBuyAlgoOrdersOfSymbol(
							new TickerSymbol(aQuoteBar.symbol()));

		AlgoSliceOrderSharesRequestedSubscriber subscriber =
				new AlgoSliceOrderSharesRequestedSubscriber();

		DomainEventPublisher.instance().subscribe(subscriber);

		BigDecimal totalQuantityAvailable = aQuoteBar.totalQuantity();

		Iterator<AlgoOrder> iterator = algoOrders.iterator();

		while (iterator.hasNext() && totalQuantityAvailable.compareTo(BigDecimal.ZERO) > 0) {
			AlgoOrder algoOrder = iterator.next();

			if (aVWAPAnalytic.qualifiesAsTradePrice(algoOrder.quote().price())) {
				this.attemptTradeFor(algoOrder, aVWAPAnalytic, subscriber);

				totalQuantityAvailable =
						totalQuantityAvailable
							.subtract(new BigDecimal(subscriber.orderSharesRequested()));
			}
		}
	}

	private VWAPAnalyticRepository vwapAnalyticRepository() {
		return this.vwapAnalyticRepository;
	}

	private class AlgoSliceOrderSharesRequestedSubscriber
			implements DomainEventSubscriber<AlgoSliceOrderSharesRequested> {

		private AtomicInteger orderSharesRequested;

		AlgoSliceOrderSharesRequestedSubscriber() {
			super();

			this.orderSharesRequested = new AtomicInteger(0);
		}

		@Override
		public void handleEvent(AlgoSliceOrderSharesRequested aDomainEvent) {
			this.orderSharesRequested.set(aDomainEvent.quantity().intValue());
		}

		@Override
		public Class<AlgoSliceOrderSharesRequested> subscribedToEventType() {
			return AlgoSliceOrderSharesRequested.class;
		}

		protected void clear() {
			this.orderSharesRequested.set(0);
		}

		protected int orderSharesRequested() {
			return this.orderSharesRequested.get();
		}
	}
}
