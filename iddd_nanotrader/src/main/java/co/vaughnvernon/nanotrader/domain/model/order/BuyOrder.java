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

import co.vaughnvernon.nanotrader.domain.model.account.AccountId;
import co.vaughnvernon.nanotrader.domain.model.account.Holding;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class BuyOrder {

	private AccountId accountId;
	private PurchaseExecution execution;
	private Holding holding;
	private OrderId orderId;
	private Quote quote;

	public BuyOrder(
			AccountId anAccountId,
			Quote aQuote,
			int aQuantityOfSharesOrdered,
			Money anOrderFee) {

		super();

		this.setAccountId(anAccountId);
		this.setExecution(new PurchaseExecution(aQuantityOfSharesOrdered));
		this.setOrderId(OrderId.unique());
		this.setQuote(aQuote);

		DomainEventPublisher.instance().publish(
				new BuyOrderPlaced(
						this.accountId(),
						this.orderId(),
						this.quote(),
						this.execution().quantityOfSharesOrdered(),
						this.execution().openDate(),
						aQuote.price().multipliedBy(aQuantityOfSharesOrdered),
						anOrderFee));
	}

	public AccountId accountId() {
		return this.accountId;
	}

	public PurchaseExecution execution() {
		return this.execution;
	}

	public boolean isFilled() {
		return this.execution().isFilled();
	}

	public boolean hasTickerSymbol(TickerSymbol aTickerSymbol) {
		return this.quote().hasTickerSymbol(aTickerSymbol);
	}

	public Holding holdingOfFilledOrder() {
		if (!this.isFilled()) {
			throw new IllegalStateException("Order must be filled before Holding can be queried.");
		}

		Holding holding = this.holding;

		if (holding == null) {
			int sharesOrdered = this.execution().quantityOfSharesOrdered();

			Money totalValue = this.quote().valueOfPricedShares(sharesOrdered);

			holding = new Holding(
						this.accountId(),
						this.orderId(),
						this.quote().tickerSymbol(),
						sharesOrdered,
						totalValue,
						this.execution().filledDate());

			this.setHolding(holding);
		}

		return holding;
	}

	public boolean isOpen() {
		return this.execution().isOpen();
	}

	public OrderId orderId() {
		return this.orderId;
	}

	public int quantityOfOutstandingShares() {
		return this.execution().quantityOfSharesOutstanding();
	}

	public int quantityOfSharesOrdered() {
		return this.execution().quantityOfSharesOrdered();
	}

	public Quote quote() {
		return this.quote;
	}

	public void sharesToPurchase(int aQuantityOfSharesAvailable) {
		if (this.execution().quantityOfSharesOutstanding() > 0) {
			if (aQuantityOfSharesAvailable > 0) {
				int quantityToPurchase =
						Math.min(
								this.execution().quantityOfSharesOutstanding(),
								aQuantityOfSharesAvailable);

				if (quantityToPurchase > 0) {
					DomainEventPublisher.instance().publish(
							new BuyOrderSharePurchaseRequested(
									this.accountId(),
									this.orderId(),
									this.quote(),
									quantityToPurchase));

					this.setExecution(
							this.execution()
								.withPurchasedSharesOf(quantityToPurchase));

					if (this.isFilled()) {
						DomainEventPublisher.instance().publish(
								new BuyOrderFilled(
										this.accountId(),
										this.orderId(),
										this.quote(),
										this.execution().quantityOfSharesOrdered(),
										this.holdingOfFilledOrder()));
					}
				}
			}
		}
	}

	public Money valueOfOrderedShares() {
		return this.quote().valueOfPricedShares(this.quantityOfSharesOrdered());
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			BuyOrder typedObject = (BuyOrder) anObject;
			equalObjects =
					this.accountId().equals(typedObject.accountId()) &&
					this.orderId().equals(typedObject.orderId());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (45321 * 61)
			+ this.accountId().hashCode()
			+ this.orderId().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "BuyOrder [accountId=" + accountId + ", orderId=" + orderId
				+ ", holding=" + holding + ", quote=" + quote + ", execution="
				+ execution + "]";
	}

	private void setAccountId(AccountId anAccountId) {
		if (anAccountId == null) {
			throw new IllegalArgumentException("AccountId must be provided.");
		}
		this.accountId = anAccountId;
	}

	private void setExecution(PurchaseExecution anExecution) {
		if (anExecution == null) {
			throw new IllegalArgumentException("BuyExecution must be provided.");
		}
		this.execution = anExecution;
	}

	private void setHolding(Holding aHolding) {
		if (aHolding == null) {
			throw new IllegalArgumentException("Holding must be provided.");
		}
		this.holding = aHolding;
	}

	private void setOrderId(OrderId anOrderId) {
		if (anOrderId == null) {
			throw new IllegalArgumentException("OrderId must be provided.");
		}
		this.orderId = anOrderId;
	}

	private void setQuote(Quote aQuote) {
		if (aQuote == null) {
			throw new IllegalArgumentException("Quote must be provided.");
		}
		this.quote = aQuote;
	}
}
