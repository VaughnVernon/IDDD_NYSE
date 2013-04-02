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

package co.vaughnvernon.tradercommon.quotebar;

import java.math.BigDecimal;
import java.util.Collection;

import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;

public final class QuoteBar {

	private Money close;
	private String companyName;
	private Money high;
	private Money low;
	private Money open;
	private Money price;
	private Money priceVolume;
	private String symbol;
	private BigDecimal totalQuantity;
	private int totalQuotesInBar;
	private BigDecimal volume;

	public QuoteBar(
			String aCompanyName,
			String aSymbol,
			Money aPrice,
			Money anOpen,
			Money aClose,
			Money aHigh,
			Money aLow,
			BigDecimal aVolume,
			Collection<PriceVolume> aPriceVolumes,
			BigDecimal aTotalQuantity,
			int aTotalQuotesInBar) {

		super();

		this.setClose(aClose);
		this.setCompanyName(aCompanyName);
		this.setHigh(aHigh);
		this.setLow(aLow);
		this.setOpen(anOpen);
		this.setPrice(aPrice);
		this.setSymbol(aSymbol);
		this.setTotalQuantity(aTotalQuantity);
		this.setTotalQuotesInBar(aTotalQuotesInBar);
		this.setVolume(aVolume);

		this.calculatePriceVolume(aPriceVolumes);
	}

	public Money change() {
		return this.close().subtract(this.open());
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

	public Money priceVolume() {
		return this.priceVolume;
	}

	public String symbol() {
		return this.symbol;
	}

	public BigDecimal totalQuantity() {
		return this.totalQuantity;
	}

	public int totalQuotesInBar() {
		return this.totalQuotesInBar;
	}

	public BigDecimal volume() {
		return this.volume;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			QuoteBar typedObject = (QuoteBar) anObject;
			equalObjects =
					this.close().equals(typedObject.close()) &&
					this.companyName().equals(typedObject.companyName()) &&
					this.high().equals(typedObject.high()) &&
					this.low().equals(typedObject.low()) &&
					this.open().equals(typedObject.open()) &&
					this.price().equals(typedObject.price()) &&
					this.priceVolume().equals(typedObject.priceVolume()) &&
					this.symbol().equals(typedObject.symbol()) &&
					this.volume().equals(typedObject.volume());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (75257 * 2)
			+ this.close().hashCode()
			+ this.companyName().hashCode()
			+ this.high().hashCode()
			+ this.low().hashCode()
			+ this.open().hashCode()
			+ this.price().hashCode()
			+ this.priceVolume().hashCode()
			+ this.symbol().hashCode()
			+ this.volume().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "QuoteBar [companyName=" + this.companyName + ", symbol="
				+ this.symbol + ", price=" + this.price + ", open=" + this.open
				+ ", close=" + this.close + ", change=" + this.change()
				+ ", high=" + this.high + ", low=" + this.low
				+ ", priceVolume=" + this.priceVolume + ", volume=" + this.volume
				+ ", totalQuotesInBar=" + this.totalQuotesInBar + "]";
	}

	private void calculatePriceVolume(Collection<PriceVolume> aPriceVolumes) {
		Money totalPriceVolume = new Money();
		BigDecimal totalVolume = BigDecimal.ZERO;

		for (PriceVolume singlePriceVolume : aPriceVolumes) {
			totalPriceVolume = totalPriceVolume.addedTo(singlePriceVolume.calculated());
			totalVolume = totalVolume.add(singlePriceVolume.volume());
		}

		this.setPriceVolume(totalPriceVolume.dividedBy(totalVolume));
	}

	private void setClose(Money aClose) {
		if (aClose == null) {
			throw new IllegalArgumentException("Close must be provided.");
		}
		this.close = aClose;
	}

	private void setCompanyName(String aCompanyName) {
		if (aCompanyName == null || aCompanyName.trim().isEmpty()) {
			throw new IllegalArgumentException("Company name must be provided.");
		}
		this.companyName = aCompanyName;
	}

	private void setHigh(Money aHigh) {
		if (aHigh == null) {
			throw new IllegalArgumentException("High must be provided.");
		}
		this.high = aHigh;
	}

	private void setLow(Money aLow) {
		if (aLow == null) {
			throw new IllegalArgumentException("Low must be provided.");
		}
		this.low = aLow;
	}

	private void setOpen(Money anOpen) {
		if (anOpen == null) {
			throw new IllegalArgumentException("Open must be provided.");
		}
		this.open = anOpen;
	}

	private void setPrice(Money aPrice) {
		if (aPrice == null) {
			throw new IllegalArgumentException("Price must be provided.");
		}
		this.price = aPrice;
	}

	private void setPriceVolume(Money aPriceVolume) {
		if (aPriceVolume == null) {
			throw new IllegalArgumentException("Price volumne must be provided.");
		}
		this.priceVolume = aPriceVolume;
	}

	private void setSymbol(String aSymbol) {
		if (aSymbol == null || aSymbol.trim().isEmpty()) {
			throw new IllegalArgumentException("Symbol must be provided.");
		}
		this.symbol = aSymbol;
	}

	private void setTotalQuantity(BigDecimal aTotalQuantity) {
		if (aTotalQuantity == null) {
			throw new IllegalArgumentException("Total quantity must be provided.");
		}
		this.totalQuantity = aTotalQuantity;
	}

	private void setTotalQuotesInBar(int aTotalQuotesInBar) {
		this.totalQuotesInBar = aTotalQuotesInBar;
	}

	private void setVolume(BigDecimal aVolume) {
		if (aVolume == null) {
			throw new IllegalArgumentException("Volume must be provided.");
		}
		this.volume = aVolume;
	}
}
