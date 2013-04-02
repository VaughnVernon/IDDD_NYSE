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

package co.vaughnvernon.algotrader.application.order;

import java.util.UUID;

import junit.framework.TestCase;
import co.vaughnvernon.algotrader.application.ApplicationServiceRegistry;
import co.vaughnvernon.algotrader.domain.model.order.AlgoOrder;
import co.vaughnvernon.algotrader.infrastructure.persistence.InMemoryAlgoOrderRepository;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class AlgoOrderApplicationServiceTest extends TestCase {

	public AlgoOrderApplicationServiceTest() {
		super();
	}

	public void testCreateAlgoBuyOrder() throws Exception {
		String orderId = UUID.randomUUID().toString().toUpperCase();

		ApplicationServiceRegistry
			.algoOrderApplicationService()
			.createAlgoBuyOrder(
					orderId,
					"GOOG",
					new Money("725.50"),
					500);

		AlgoOrder algoOrder =
				InMemoryAlgoOrderRepository
					.instance()
					.algoOrderOfId(orderId);

		assertNotNull(algoOrder);
		assertEquals(500, algoOrder.sharesRemaining().intValue());

		assertEquals(
				1,
				InMemoryAlgoOrderRepository
					.instance()
					.unfilledBuyAlgoOrdersOfSymbol(new TickerSymbol("GOOG"))
				.size());
	}
}
