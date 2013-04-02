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

import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;

public class AlgoOrder {

	private Fill fill;
	private String orderId;
    private Quote quote;
    private BigDecimal sharesRemaining;
	private OrderType type;

	public AlgoOrder(
			String anOrderId,
			OrderType aType,
			Quote aQuote) {
		super();

		if (aQuote.quantity() < 500) {
			throw new IllegalArgumentException("Cannot be less than 500 shares.");
		}

		this.setOrderId(anOrderId);
		this.setType(aType);
		this.setQuote(aQuote);
		this.setSharesRemaining(new BigDecimal(aQuote.quantity()));
	}

	public Fill fill() {
		if (this.fill == null) {
			throw new IllegalStateException("Algo order has not yet been filled; first use isFilled().");
		}

		return this.fill;
	}

	public boolean isFilled() {
		return this.fill != null;
	}

	public String orderId() {
		return this.orderId;
	}

	public Quote quote() {
		return this.quote;
	}

	public void requestSlice(Money aPrice, BigDecimal aSliceQuantity) {
		if (this.hasSharesRemaining()) {
			BigDecimal sharesToAcquire =
					this.min(this.sharesRemaining(), aSliceQuantity);

			this.setSharesRemaining(this.sharesRemaining().subtract(sharesToAcquire));

			DomainEventPublisher
				.instance()
				.publish(new AlgoSliceOrderSharesRequested(
						this.orderId(),
						this.quote().tickerSymbol(),
						aPrice,
						aSliceQuantity));

			if (!this.hasSharesRemaining()) {
				this.filled();
			}
		}
	}

	public void requestSlice(Money aPrice, int aSliceQuantity) {
		this.requestSlice(aPrice, new BigDecimal(aSliceQuantity));
	}

	public boolean hasSharesRemaining() {
		return this.sharesRemaining().compareTo(BigDecimal.ZERO) != 0;
	}

	public BigDecimal sharesRemaining() {
		return this.sharesRemaining;
	}

	public OrderType type() {
		return this.type;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			AlgoOrder typedObject = (AlgoOrder) anObject;
			equalObjects =
					this.orderId().equals(typedObject.orderId());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (77329 * 7)
			+ this.orderId().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "AlgoOrder [orderId=" + orderId + ", quote=" + quote
				+ ", sharesRemaining=" + sharesRemaining + ", type=" + type
				+ "]";
	}

	private void setFill(Fill aFill) {
		this.fill = aFill;
	}

	private void filled() {
		if (this.isFilled()) {
			throw new IllegalStateException("Algo order is already filled.");
		}

		if (this.hasSharesRemaining()) {
			throw new IllegalStateException("Algo order is not yet filled.");
		}

		this.setFill(
				new Fill(
						this.quote().price(),
						new BigDecimal(this.quote().quantity()),
						new Date()));

		DomainEventPublisher
			.instance()
			.publish(new AlgoOrderFilled(
					this.orderId(),
					this.type().name(),
					this.quote()));
	}

	private BigDecimal min(BigDecimal aQuantity1, BigDecimal aQuantity2) {
		return aQuantity1.compareTo(aQuantity2) <= 0 ? aQuantity1 : aQuantity2;
	}

	private void setOrderId(String anOrderId) {
		this.orderId = anOrderId;
	}

	private void setQuote(Quote aQuote) {
		this.quote = aQuote;
	}

	private void setSharesRemaining(BigDecimal aSharesRemaining) {
		if (aSharesRemaining.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Cannot be less than zero.");
		}
		this.sharesRemaining = aSharesRemaining;
	}

	private void setType(OrderType aType) {
		this.type = aType;
	}
}
