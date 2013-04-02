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
import co.vaughnvernon.tradercommon.event.DomainEvent;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;

public class BuyOrderPlaced implements DomainEvent {

	private AccountId accountId;
	private Money cost;
	private Money orderFee;
	private Date occurredOn;
	private OrderId orderId;
	private Date placedOnDate;
	private int quantityOfSharesOrdered;
	private Quote quote;

	public BuyOrderPlaced(
			AccountId anAccountId,
			OrderId anOrderId,
			Quote aQuote,
			int aQuantityOfSharesOrdered,
			Date aPlacedOnDate,
			Money aCost,
			Money anOrderFee) {
		super();

		this.setAccountId(anAccountId);
		this.setCost(aCost);
		this.setOccurredOn(new Date());
		this.setOrderFee(anOrderFee);
		this.setOrderId(anOrderId);
		this.setPlacedOnDate(aPlacedOnDate);
		this.setQuantityOfSharesOrdered(aQuantityOfSharesOrdered);
		this.setQuote(aQuote);
	}

	public AccountId accountId() {
		return this.accountId;
	}

	public Money cost() {
		return this.cost;
	}

	@Override
	public int eventVersion() {
		return 1;
	}

	@Override
	public Date occurredOn() {
		return this.occurredOn;
	}

	public Money orderFee() {
		return this.orderFee;
	}

	public OrderId orderId() {
		return this.orderId;
	}

	public Date placedOnDate() {
		return this.placedOnDate;
	}

	public int quantityOfSharesOrdered() {
		return this.quantityOfSharesOrdered;
	}

	public Quote quote() {
		return this.quote;
	}

	private void setAccountId(AccountId anAccountId) {
		this.accountId = anAccountId;
	}

	private void setCost(Money aCost) {
		this.cost = aCost;
	}

	private void setOccurredOn(Date anOccurredOn) {
		this.occurredOn = anOccurredOn;
	}

	private void setOrderFee(Money anOrderFee) {
		this.orderFee = anOrderFee;
	}

	private void setOrderId(OrderId anOrderId) {
		this.orderId = anOrderId;
	}

	private void setPlacedOnDate(Date aPlacedOnDate) {
		this.placedOnDate = aPlacedOnDate;
	}

	private void setQuantityOfSharesOrdered(int aQuantityOfSharesOrdered) {
		this.quantityOfSharesOrdered = aQuantityOfSharesOrdered;
	}

	private void setQuote(Quote aQuote) {
		this.quote = aQuote;
	}
}
