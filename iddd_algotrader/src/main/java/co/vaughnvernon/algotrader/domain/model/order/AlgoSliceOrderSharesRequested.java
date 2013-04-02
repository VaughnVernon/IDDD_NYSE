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
import java.util.Date;

import co.vaughnvernon.tradercommon.event.DomainEvent;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class AlgoSliceOrderSharesRequested implements DomainEvent {

	private int eventVersion;
	private Date occurredOn;
	private String orderId;
	private Money price;
	private BigDecimal quantity;
	private TickerSymbol tickerSymbol;

	public AlgoSliceOrderSharesRequested(
			String anOrderId,
			TickerSymbol aTickerSymbol,
			Money aPrice,
			BigDecimal aQuantity) {

		super();

		this.eventVersion = 1;
		this.occurredOn = new Date();
		this.orderId = anOrderId;
		this.price = aPrice;
		this.quantity = aQuantity;
		this.tickerSymbol = aTickerSymbol;
	}

	@Override
	public int eventVersion() {
		return this.eventVersion;
	}

	@Override
	public Date occurredOn() {
		return this.occurredOn;
	}

	public String orderId() {
		return this.orderId;
	}

	public Money price() {
		return this.price;
	}

	public BigDecimal quantity() {
		return this.quantity;
	}

	public TickerSymbol tickerSymbol() {
		return this.tickerSymbol;
	}
}
