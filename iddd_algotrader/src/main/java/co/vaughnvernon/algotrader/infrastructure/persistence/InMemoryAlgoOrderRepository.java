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

package co.vaughnvernon.algotrader.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vaughnvernon.algotrader.domain.model.order.AlgoOrder;
import co.vaughnvernon.algotrader.domain.model.order.AlgoOrderRepository;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class InMemoryAlgoOrderRepository implements AlgoOrderRepository {

	private static AlgoOrderRepository instance;

	private Map<String,AlgoOrder> algoOrders;

	public static void clear() {
		((InMemoryAlgoOrderRepository) instance).algoOrders.clear();
	}

	public static synchronized AlgoOrderRepository instance() {
		if (instance == null) {
			instance = new InMemoryAlgoOrderRepository();
		}

		return instance;
	}

	@Override
	public AlgoOrder algoOrderOfId(String anOrderId) {
		for (AlgoOrder algoOrder : this.algoOrders().values()) {
			if (algoOrder.orderId().equals(anOrderId)) {
				return algoOrder;
			}
		}

		return null;
	}

	@Override
	public void save(AlgoOrder anAlgoOrder) {
		this.algoOrders().put(anAlgoOrder.orderId(), anAlgoOrder);
	}

	@Override
	public Collection<AlgoOrder> unfilledBuyAlgoOrdersOfSymbol(TickerSymbol aTickerSymbol) {
		List<AlgoOrder> algoOrders = new ArrayList<AlgoOrder>();

		for (AlgoOrder algoOrder : this.algoOrders().values()) {
			if (algoOrder.quote().hasTickerSymbol(aTickerSymbol)) {
				if (algoOrder.hasSharesRemaining()) {
					algoOrders.add(algoOrder);
				}
			}
		}

		return algoOrders;
	}

	private InMemoryAlgoOrderRepository() {
		super();

		this.algoOrders = new HashMap<String,AlgoOrder>();
	}

	private Map<String,AlgoOrder> algoOrders() {
		return this.algoOrders;
	}
}
