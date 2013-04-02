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

package co.vaughnvernon.tradercommon.quote;

import java.io.Serializable;

import co.vaughnvernon.tradercommon.monetary.Money;

public final class Quote implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money price;
	private int quantity;
	private TickerSymbol tickerSymbol;

	public Quote(TickerSymbol aTickerSymbol, Money aPrice) {
		super();

		this.setPrice(aPrice);
		this.setTickerSymbol(aTickerSymbol);
	}

	public Quote(TickerSymbol aTickerSymbol, Money aPrice, int aQuantity) {
		super();

		this.setPrice(aPrice);
		this.setQuantity(aQuantity);
		this.setTickerSymbol(aTickerSymbol);
	}

	public Quote(Quote aQuote) {
		this(aQuote.tickerSymbol(), aQuote.price());
	}

	public boolean hasTickerSymbol(TickerSymbol aTickerSymbol) {
		return this.tickerSymbol().equals(aTickerSymbol);
	}

	public Money price() {
		return this.price;
	}

	public int quantity() {
		return this.quantity;
	}

	public TickerSymbol tickerSymbol() {
		return this.tickerSymbol;
	}

	public Money valueOfPricedShares(int aQuantity) {
		return this.price().multipliedBy(aQuantity);
	}

	public Quote withUpdatedPrice(Money aPrice) {
		return new Quote(this.tickerSymbol(), aPrice);
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Quote typedObject = (Quote) anObject;
			equalObjects =
					this.tickerSymbol().equals(typedObject.tickerSymbol()) &&
					this.price().equals(typedObject.price()) &&
					this.quantity() == typedObject.quantity();
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
				+ (75931 * 41)
				+ this.tickerSymbol().hashCode()
				+ this.price().hashCode()
				+ this.quantity();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Quote [tickerSymbol=" + this.tickerSymbol() + ", price=" + this.price() + "]";
	}

	private void setPrice(Money aPrice) {
		if (aPrice == null) {
			throw new IllegalStateException("Price must be provided.");
		}
		this.price = aPrice;
	}

	private void setQuantity(int aQuantity) {
		this.quantity = aQuantity;
	}

	private void setTickerSymbol(TickerSymbol aTickerSymbol) {
		if (aTickerSymbol == null) {
			throw new IllegalStateException("TickerSymbol must be provided.");
		}
		this.tickerSymbol = aTickerSymbol;
	}
}
