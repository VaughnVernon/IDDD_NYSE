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

package co.vaughnvernon.nanotrader.domain.model.account;

import java.util.Date;

import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class Holding {

	private AccountId accountId;
	private Date acquiredOn;
	private int numberOfShares;
	private OrderId orderId;
	private TickerSymbol tickerSymbol;
	private Money totalValue;

	public Holding(
			AccountId anAccountId,
			OrderId anOrderId,
			TickerSymbol aTickerSymbol,
			int aNumberOfShares,
			Money aTotalValue,
			Date anAcquiredOn) {
		super();

		this.setAccountId(anAccountId);
		this.setAcquiredOn(anAcquiredOn);
		this.setNumberOfShares(aNumberOfShares);
		this.setOrderId(anOrderId);
		this.setTickerSymbol(aTickerSymbol);
		this.setTotalValue(aTotalValue);
	}

	public Holding(Holding aHolding) {
		this(
				aHolding.accountId(),
				aHolding.orderId(),
				aHolding.tickerSymbol(),
				aHolding.numberOfShares(),
				aHolding.totalValue(),
				aHolding.acquiredOn());
	}

	public AccountId accountId() {
		return this.accountId;
	}

	public Date acquiredOn() {
		return this.acquiredOn;
	}

	public int numberOfShares() {
		return this.numberOfShares;
	}

	public OrderId orderId() {
		return this.orderId;
	}

	public TickerSymbol tickerSymbol() {
		return this.tickerSymbol;
	}

	public Money totalValue() {
		return this.totalValue;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Holding typedObject = (Holding) anObject;
			equalObjects =
					this.accountId().equals(typedObject.accountId()) &&
					this.acquiredOn().equals(typedObject.acquiredOn()) &&
					this.numberOfShares() == typedObject.numberOfShares() &&
					this.orderId().equals(typedObject.orderId()) &&
					this.tickerSymbol().equals(typedObject.tickerSymbol()) &&
					this.totalValue().equals(typedObject.totalValue());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (72721 * 97)
			+ this.accountId().hashCode()
			+ this.acquiredOn().hashCode()
			+ this.numberOfShares()
			+ this.orderId().hashCode()
			+ this.tickerSymbol().hashCode()
			+ this.totalValue().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Holding [accountId=" + accountId + ", acquiredOn=" + acquiredOn
				+ ", numberOfShares=" + numberOfShares + ", orderId=" + orderId
				+ ", tickerSymbol=" + tickerSymbol + ", totalValue="
				+ totalValue + "]";
	}

	private void setAccountId(AccountId anAccountId) {
		if (anAccountId == null) {
			throw new IllegalArgumentException("AccountId must be provided.");
		}
		this.accountId = anAccountId;
	}

	private void setAcquiredOn(Date anAcquiredOn) {
		if (anAcquiredOn == null) {
			throw new IllegalArgumentException("Acquired-on date must be provided.");
		}
		this.acquiredOn = anAcquiredOn;
	}

	private void setNumberOfShares(int aNumberOfShares) {
		if (aNumberOfShares <= 0) {
			throw new IllegalArgumentException("Number of shares must be greater than zero.");
		}
		this.numberOfShares = aNumberOfShares;
	}

	private void setOrderId(OrderId anOrderId) {
		if (anOrderId == null) {
			throw new IllegalArgumentException("OrderId must be provided.");
		}
		this.orderId = anOrderId;
	}

	private void setTickerSymbol(TickerSymbol aTickerSymbol) {
		if (aTickerSymbol == null) {
			throw new IllegalArgumentException("Ticker symbol date must be provided.");
		}
		this.tickerSymbol = aTickerSymbol;
	}

	private void setTotalValue(Money aTotalValue) {
		if (aTotalValue == null) {
			throw new IllegalArgumentException("Total value must be provided.");
		}
		if (aTotalValue.amount().doubleValue() <= 0) {
			throw new IllegalArgumentException("Total value must be greater than 0.");
		}
		this.totalValue = aTotalValue;
	}
}
