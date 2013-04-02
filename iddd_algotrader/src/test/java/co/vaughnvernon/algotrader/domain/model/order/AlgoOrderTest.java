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

package co.vaughnvernon.algotrader.domain.model.order;

import java.math.BigDecimal;
import java.util.UUID;

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.event.DomainEventSubscriber;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class AlgoOrderTest extends TestCase {

	private AlgoOrderFilled algoOrderFilled;
	private AlgoSliceOrderSharesRequested sliceOrderSharesRequestedEvent;
	private BigDecimal totalQuantity = BigDecimal.ZERO;

	public AlgoOrderTest() {
		super();
	}

	public void testCreate() throws Exception {
		AlgoOrder algoOrder =
				new AlgoOrder(
						UUID.randomUUID().toString().toUpperCase(),
						OrderType.Buy,
						new Quote(
								new TickerSymbol("GOOG"),
								new Money("725.50"),
								500));

		assertNotNull(algoOrder);
		assertFalse(algoOrder.isFilled());
		assertTrue(algoOrder.hasSharesRemaining());
		assertEquals(new BigDecimal("500"), algoOrder.sharesRemaining());

		try {
			algoOrder.fill();

			fail("Should not be filled and should throw exception.");

		} catch (IllegalStateException e) {
			// good, ignore
		}
	}

	public void testRequestSlice() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<AlgoSliceOrderSharesRequested>() {
			@Override
			public void handleEvent(AlgoSliceOrderSharesRequested aDomainEvent) {
				sliceOrderSharesRequestedEvent = aDomainEvent;
			}

			@Override
			public Class<AlgoSliceOrderSharesRequested> subscribedToEventType() {
				return AlgoSliceOrderSharesRequested.class;
			}
		});

		AlgoOrder algoOrder =
				new AlgoOrder(
						UUID.randomUUID().toString().toUpperCase(),
						OrderType.Buy,
						new Quote(
								new TickerSymbol("GOOG"),
								new Money("725.50"),
								500));

		algoOrder.requestSlice(new Money("725.10"), 100);

		assertNotNull(sliceOrderSharesRequestedEvent);
		assertEquals(new BigDecimal("100"), sliceOrderSharesRequestedEvent.quantity());
		assertFalse(algoOrder.isFilled());
		assertTrue(algoOrder.hasSharesRemaining());
		assertEquals(new BigDecimal("400"), algoOrder.sharesRemaining());

		try {
			algoOrder.fill();

			fail("Should not be filled and should throw exception.");

		} catch (IllegalStateException e) {
			// good, ignore
		}
	}

	public void testFillAlgoOrder() throws Exception {

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<AlgoSliceOrderSharesRequested>() {
			@Override
			public void handleEvent(AlgoSliceOrderSharesRequested aDomainEvent) {
				totalQuantity = totalQuantity.add(aDomainEvent.quantity());
			}

			@Override
			public Class<AlgoSliceOrderSharesRequested> subscribedToEventType() {
				return AlgoSliceOrderSharesRequested.class;
			}
		});

		DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<AlgoOrderFilled>() {
			@Override
			public void handleEvent(AlgoOrderFilled aDomainEvent) {
				algoOrderFilled = aDomainEvent;
			}

			@Override
			public Class<AlgoOrderFilled> subscribedToEventType() {
				return AlgoOrderFilled.class;
			}
		});

		AlgoOrder algoOrder =
				new AlgoOrder(
						UUID.randomUUID().toString().toUpperCase(),
						OrderType.Buy,
						new Quote(
								new TickerSymbol("GOOG"),
								new Money("725.50"),
								500));

		for (int idx = 0; idx < 5; ++idx) {
			algoOrder.requestSlice(new Money("725.10"), 100);
		}

		assertEquals(new BigDecimal("500"), this.totalQuantity);
		assertNotNull(algoOrderFilled);
		assertEquals(algoOrder.orderId(), algoOrderFilled.orderId());
		assertTrue(algoOrder.isFilled());
		assertFalse(algoOrder.hasSharesRemaining());
		assertEquals(BigDecimal.ZERO, algoOrder.sharesRemaining());
		assertNotNull(algoOrder.fill());

		assertEquals(algoOrder.quote().price(), algoOrder.fill().price());
		assertEquals(algoOrder.quote().quantity(), algoOrder.fill().quantity().intValue());
		assertNotNull(algoOrder.fill().filledOn());
	}
}
