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

package co.vaughnvernon.algotrader.domain.model.vwap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;

public class VWAPAnalytic {

	protected static final int TRADABLE_QUOTE_BARS = 20;

	private List<PriceVolume> priceVolumes;
	private String symbol;
	private Money vwap;

	public VWAPAnalytic(String aSymbol, PriceVolume aPriceVolume) {
		super();

		this.setPriceVolumes(new ArrayList<PriceVolume>());
		this.setSymbol(aSymbol);
		this.setVwap(new Money());

		this.accumulatePriceVolume(aPriceVolume);
	}

	public void accumulatePriceVolume(PriceVolume aPriceVolume) {
		this.dropObsoletePriceVolume();

		this.priceVolumes().add(aPriceVolume);

		this.calculateVwap();
	}

	public boolean qualifiesAsTradePrice(Money anOrderTargetPrice) {
		return this.vwap().isLessThanOrEqualTo(anOrderTargetPrice);
	}

	public boolean isReadyToTrade() {
		return this.priceVolumes().size() >= TRADABLE_QUOTE_BARS;
	}

	public String symbol() {
		return this.symbol;
	}

	public Money vwap() {
		return this.vwap;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			VWAPAnalytic typedObject = (VWAPAnalytic) anObject;
			equalObjects =
					this.symbol().equals(typedObject.symbol());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
				+ (7732 * 149)
				+ this.symbol().hashCode();

		return hashCodeValue;
	}

	protected List<PriceVolume> priceVolumes() {
		return this.priceVolumes;
	}

	private void calculateVwap() {
		Money cumulativePriceVolume = this.cumulativePriceVolume();
		BigDecimal cumulativeVolume = this.cumulativeVolume();
		Money vwap = cumulativePriceVolume.dividedBy(cumulativeVolume);

		this.setVwap(vwap);
	}

	private Money cumulativePriceVolume() {
		Money cumulativePriceVolume = new Money();

		for (PriceVolume priceVolume : this.priceVolumes()) {
			cumulativePriceVolume = cumulativePriceVolume.addedTo(priceVolume.calculated());
		}

		return cumulativePriceVolume;
	}

	private BigDecimal cumulativeVolume() {
		BigDecimal cumulativeVolume = BigDecimal.ZERO;

		for (PriceVolume priceVolume : this.priceVolumes()) {
			cumulativeVolume = cumulativeVolume.add(priceVolume.volume());
		}

		return cumulativeVolume;
	}

	private void dropObsoletePriceVolume() {
		if (this.isReadyToTrade()) {
			this.priceVolumes().remove(0);
		}
	}

	private void setPriceVolumes(List<PriceVolume> aPriceVolumes) {
		this.priceVolumes = aPriceVolumes;
	}

	private void setSymbol(String aSymbol) {
		if (aSymbol == null) {
			throw new IllegalArgumentException("The symbol id is required.");
		}

		this.symbol = aSymbol;
	}

	private void setVwap(Money aVwap) {
		this.vwap = aVwap;
	}
}
