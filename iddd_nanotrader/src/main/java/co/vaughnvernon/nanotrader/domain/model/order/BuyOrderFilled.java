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

import java.util.Date;

import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.account.Holding;
import co.vaughnvernon.tradercommon.event.DomainEvent;
import co.vaughnvernon.tradercommon.quote.Quote;

public class BuyOrderFilled implements DomainEvent {

	private AccountId accountId;
	private int eventVersion;
	private Holding holding;
	private Date occurredOn;
	private OrderId orderId;
	private int quantityOfSharesFilled;
	private Quote quote;

	public BuyOrderFilled(
			AccountId anAccountId,
			OrderId anOrderId,
			Quote aQuote,
			int aQuantityOfSharesFilled,
			Holding aHolding) {
		super();

		this.setAccountId(anAccountId);
		this.setOrderId(anOrderId);
		this.setEventVersion(1);
		this.setHolding(aHolding);
		this.setOccurredOn(new Date());
		this.setQuantityOfSharesFilled(aQuantityOfSharesFilled);
		this.setQuote(aQuote);
	}

	public AccountId accountId() {
		return this.accountId;
	}

	@Override
	public int eventVersion() {
		return this.eventVersion;
	}

	public Holding holding() {
		return this.holding;
	}

	@Override
	public Date occurredOn() {
		return this.occurredOn;
	}

	public OrderId orderId() {
		return this.orderId;
	}

	public int quantityOfSharesFilled() {
		return this.quantityOfSharesFilled;
	}

	public Quote quote() {
		return this.quote;
	}

	private void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	private void setEventVersion(int anEventVersion) {
		this.eventVersion = anEventVersion;
	}

	private void setHolding(Holding aHolding) {
		this.holding = aHolding;
	}

	private void setOccurredOn(Date anOccurredOn) {
		this.occurredOn = anOccurredOn;
	}

	private void setOrderId(OrderId anOrderId) {
		this.orderId = anOrderId;
	}

	private void setQuantityOfSharesFilled(int quantityOfSharesFilled) {
		this.quantityOfSharesFilled = quantityOfSharesFilled;
	}

	private void setQuote(Quote quote) {
		this.quote = quote;
	}
}
