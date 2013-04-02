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

package co.vaughnvernon.tradercommon.quotefeed;

import java.math.BigDecimal;

import co.vaughnvernon.tradercommon.monetary.Money;

public final class FeedQuote {

	private Money close;
	private String companyName;
	private Money high;
	private Money low;
	private Money open;
	private Money price;
	private BigDecimal quantity;
	private String symbol;
	private BigDecimal volume;

	public FeedQuote(String aCompanyName, Money aHigh, Money aLow, Money anOpen, Money aClose,
			Money aPrice, BigDecimal aQuantity, String aSymbol, BigDecimal aVolume) {

		super();

		this.setClose(aClose);
		this.setCompanyName(aCompanyName);
		this.setHigh(aHigh);
		this.setLow(aLow);
		this.setOpen(anOpen);
		this.setPrice(aPrice);
		this.setQuantity(aQuantity);
		this.setSymbol(aSymbol);
		this.setVolume(aVolume);
	}

	public Money close() {
		return this.close;
	}

	public String companyName() {
		return this.companyName;
	}

	public Money high() {
		return this.high;
	}

	public Money low() {
		return this.low;
	}

	public Money open() {
		return this.open;
	}

	public Money price() {
		return this.price;
	}

	public BigDecimal quantity() {
		return this.quantity;
	}

	public String symbol() {
		return this.symbol;
	}

	public BigDecimal volume() {
		return this.volume;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			FeedQuote typedObject = (FeedQuote) anObject;
			equalObjects =
					this.companyName().equals(typedObject.companyName()) &&
					this.high().equals(typedObject.high()) &&
					this.low().equals(typedObject.low()) &&
					this.open().equals(typedObject.open()) &&
					this.price().equals(typedObject.price()) &&
					this.quantity().equals(typedObject.quantity()) &&
					this.symbol().equals(typedObject.symbol()) &&
					this.volume().equals(typedObject.volume());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (84327 * 31)
			+ this.companyName().hashCode()
			+ this.high().hashCode()
			+ this.low().hashCode()
			+ this.open().hashCode()
			+ this.price().hashCode()
			+ this.quantity().hashCode()
			+ this.symbol().hashCode()
			+ this.volume().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "FeedQuote [companyName=" + this.companyName
				+ ", high=" + this.high + ", low=" + this.low
				+ ", open=" + this.open + ", close=" + this.close
				+ ", price=" + this.price + ", quantity=" + this.quantity
				+ ", symbol=" + this.symbol + ", volume=" + this.volume + "]";
	}

	private void setClose(Money close) {
		this.close = close;
	}

	private void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	private void setHigh(Money high) {
		this.high = high;
	}

	private void setLow(Money low) {
		this.low = low;
	}

	private void setOpen(Money open) {
		this.open = open;
	}

	private void setPrice(Money price) {
		this.price = price;
	}

	private void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	private void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	private void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
}
