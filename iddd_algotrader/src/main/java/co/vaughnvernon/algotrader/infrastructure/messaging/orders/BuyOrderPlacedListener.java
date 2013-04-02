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

import co.vaughnvernon.algotrader.application.ApplicationServiceRegistry;
import co.vaughnvernon.tradercommon.infrastructure.messaging.slothmq.ExchangeListener;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.notification.NotificationReader;

public class BuyOrderPlacedListener extends ExchangeListener {

	public BuyOrderPlacedListener() {
		super();
	}

	@Override
	protected String exchangeName() {
		return "NanoTrader";
	}

	@Override
	protected void filteredDispatch(String aType, String aTextMessage) {
		NotificationReader notification = new NotificationReader(aTextMessage);

		int quantity = notification.eventIntegerValue("quantityOfSharesOrdered");

		if (quantity >= 500) {
			ApplicationServiceRegistry
				.algoOrderApplicationService()
				.createAlgoBuyOrder(
						notification.eventStringValue("orderId.id"),
						notification.eventStringValue("quote.tickerSymbol.symbol"),
						new Money(notification.eventBigDecimalValue("quote.price.amount")),
						quantity);
		}
	}

	@Override
	protected String[] listensTo() {
		return new String[] { "co.vaughnvernon.nanotrader.domain.model.order.BuyOrderPlaced" };
	}

	@Override
	protected String name() {
		return this.getClass().getName();
	}
}
