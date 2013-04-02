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

package co.vaughnvernon.nanotrader.infrastructure.messaging.quotes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrderRepository;
import co.vaughnvernon.nanotrader.domain.model.order.MarketFillService;
import co.vaughnvernon.nanotrader.infrastructure.persistence.InMemoryBuyOrderRepository;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;
import co.vaughnvernon.tradercommon.quotebar.QuoteBar;

public class MarketFillServiceTest extends TestCase {

	private BuyOrderRepository buyOrderRepository;

	public MarketFillServiceTest() {
		super();

		this.buyOrderRepository = new InMemoryBuyOrderRepository();
	}

	public void testFillMarketOrders() throws Exception {
		BuyOrder[] buyOrders = this.buyOrderFixture();

		for (BuyOrder buyOrder : buyOrders) {
			this.buyOrderRepository.save(buyOrder);
		}

		QuoteBar quoteBar = this.quoteBarFixture();

		TickerSymbol tickerSymbol = new TickerSymbol(quoteBar.symbol());

		int totalQuantityAvailable =
				quoteBar
					.totalQuantity()
					.toBigInteger()
					.intValue();

		Collection<BuyOrder> openOrders =
				this.buyOrderRepository.openOrdersOf(tickerSymbol);

		MarketFillService marketFillService = new MarketFillService(this.buyOrderRepository);

		marketFillService.fillMarketBuyOrders(openOrders, totalQuantityAvailable);

		int filledCount = 0;

		for (BuyOrder previouslyOpenOrder : openOrders) {
			assertTrue(previouslyOpenOrder.isFilled());
			assertFalse(previouslyOpenOrder.isOpen());
			++filledCount;
		}

		assertEquals(filledCount, openOrders.size());

		int stillOpenCount = 0;

		for (BuyOrder possiblyOpenOrder : buyOrders) {
			if (possiblyOpenOrder.isOpen()) {
				++stillOpenCount;
			}
		}

		assertEquals(buyOrders.length, filledCount + stillOpenCount);
	}

	public void testUnmatchedFillMarketOrders() throws Exception {
		BuyOrder[] buyOrders = this.buyOrderFixture();

		for (BuyOrder buyOrder : buyOrders) {
			this.buyOrderRepository.save(buyOrder);
		}

		QuoteBar quoteBar = this.quoteBarUnmatchedFixture();

		TickerSymbol tickerSymbol = new TickerSymbol(quoteBar.symbol());

		int totalQuantityAvailable =
				quoteBar
					.totalQuantity()
					.toBigInteger()
					.intValue();

		Collection<BuyOrder> openOrders =
				this.buyOrderRepository.openOrdersOf(tickerSymbol);

		MarketFillService marketFillService =
				new MarketFillService(this.buyOrderRepository);

		marketFillService.fillMarketBuyOrders(openOrders, totalQuantityAvailable);

		int stillOpenCount = 0;

		for (BuyOrder previouslyOpenOrder : buyOrders) {
			assertFalse(previouslyOpenOrder.isFilled());
			assertTrue(previouslyOpenOrder.isOpen());
			++stillOpenCount;
		}

		assertEquals(buyOrders.length, stillOpenCount);
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();

		super.setUp();
	}

	private BuyOrder[] buyOrderFixture() {
		BuyOrder[] buyOrders = new BuyOrder[3];

		buyOrders[0] =
				new BuyOrder(
					AccountId.unique(),
					new Quote(new TickerSymbol("GOOG"), new Money("720.43")),
					5,
					new Money("9.99"));

		buyOrders[1] =
				new BuyOrder(
					AccountId.unique(),
					new Quote(new TickerSymbol("GOOG"), new Money("720.43")),
					7,
					new Money("9.99"));

		buyOrders[2] =
				new BuyOrder(
					AccountId.unique(),
					new Quote(new TickerSymbol("MSFT"), new Money("27.50")),
					10,
					new Money("9.99"));

		return buyOrders;
	}

	private QuoteBar quoteBarFixture() {

		Collection<PriceVolume> priceVolumes =
				this.priceVolumeValues(new Money("720.40"), new BigDecimal("87231"));

		QuoteBar quoteBar =
				new QuoteBar("Google, Inc.", "GOOG", new Money("720.55"), new Money("720.30"), new Money("720.25"),
						new Money("720.60"), new Money("720.25"), new BigDecimal("87231"), priceVolumes,
						new BigDecimal("12003"), 22);

		return quoteBar;
	}

	private QuoteBar quoteBarUnmatchedFixture() {

		Collection<PriceVolume> priceVolumes =
				this.priceVolumeValues(new Money("35.20"), new BigDecimal("72771"));

		QuoteBar quoteBar =
				new QuoteBar("Oracle, Inc.", "ORCL", new Money("35.20"), new Money("35.25"), new Money("35.95"),
						new Money("35.15"), new Money("35.19"), new BigDecimal("72771"), priceVolumes,
						new BigDecimal("15145"), priceVolumes.size());

		return quoteBar;
	}

	private Collection<PriceVolume> priceVolumeValues(
			Money aBasePrice,
			BigDecimal aBaseVolume) {

		List<PriceVolume> priceVolumes = new ArrayList<PriceVolume>();
		Money price = aBasePrice;
		BigDecimal volume = aBaseVolume;

		for (int idx = 0; idx < 100; ++idx) {
			if ((idx % 2) == 0) {
				price = price.addedTo(new Money("0.02"));
			} else if ((idx % 3) == 0) {
				price = price.subtract(new Money("0.03"));
			} else {
				price = price.addedTo(new Money("0.01"));
			}

			volume = volume.add(aBaseVolume);

			priceVolumes.add(new PriceVolume(price, volume));
		}

		return priceVolumes;
	}
}
