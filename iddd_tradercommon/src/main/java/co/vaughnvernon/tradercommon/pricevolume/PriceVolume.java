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

package co.vaughnvernon.tradercommon.pricevolume;

import java.math.BigDecimal;

import co.vaughnvernon.tradercommon.monetary.Money;

public class PriceVolume {

	private Money price;
	private BigDecimal volume;

	public PriceVolume(Money aPrice, BigDecimal aVolume) {
		super();

		this.setPrice(aPrice);
		this.setVolume(aVolume);
	}

	public Money calculated() {
		return this.price().multipliedBy(this.volume());
	}

	public Money price() {
		return this.price;
	}

	public BigDecimal volume() {
		return this.volume;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			PriceVolume typedObject = (PriceVolume) anObject;
			equalObjects =
					this.price().equals(typedObject.price()) &&
					this.volume().equals(typedObject.volume());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (21785 * 3)
			+ this.price().hashCode()
			+ this.volume().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "PriceVolume [price=" + price + ", volume=" + volume + "]";
	}

	private void setPrice(Money aPrice) {
		this.price = aPrice;
	}

	private void setVolume(BigDecimal aVolume) {
		this.volume = aVolume;
	}
}
