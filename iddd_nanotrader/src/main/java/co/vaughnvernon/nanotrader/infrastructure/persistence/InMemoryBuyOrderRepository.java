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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.nanotrader.domain.model.order.BuyOrderRepository;
import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class InMemoryBuyOrderRepository implements BuyOrderRepository {

	private static BuyOrderRepository instance;

	private Map<String,BuyOrder> orders;

	public static synchronized BuyOrderRepository instance() {
		if (instance == null) {
			instance = new InMemoryBuyOrderRepository();
		}

		return instance;
	}

	public InMemoryBuyOrderRepository() {
		super();

		this.setOrders(new HashMap<String,BuyOrder>());
	}

	@Override
	public Collection<BuyOrder> openOrdersOf(TickerSymbol aTickerSymbol) {
		List<BuyOrder> openOrders = new ArrayList<BuyOrder>();

		for (BuyOrder order : this.orders().values()) {
			if (order.hasTickerSymbol(aTickerSymbol)) {
				if (order.isOpen()) {
					openOrders.add(order);
				}
			}
		}

		return openOrders;
	}

	@Override
	public BuyOrder orderOf(OrderId anOrderId) {
		return this.orders().get(anOrderId.id());
	}

	@Override
	public void remove(BuyOrder aOrder) {
		this.orders().remove(aOrder.orderId().id());
	}

	@Override
	public void save(BuyOrder aOrder) {
		this.orders().put(aOrder.orderId().id(), aOrder);
	}

	private Map<String,BuyOrder> orders() {
		return this.orders;
	}

	private void setOrders(Map<String,BuyOrder> aMap) {
		this.orders = aMap;
	}
}
