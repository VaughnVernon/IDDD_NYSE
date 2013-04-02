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

import co.vaughnvernon.tradercommon.monetary.Money;

public final class Fill {

	private Date filledOn;
	private Money price;
	private BigDecimal quantity;

	public Fill(Money aPrice, BigDecimal aQuantity, Date aFilledOnDate) {
		super();

		this.setFilledOn(aFilledOnDate);
		this.setPrice(aPrice);
		this.setQuantity(aQuantity);
	}

	public Date filledOn() {
		return this.filledOn;
	}

	public Money price() {
		return this.price;
	}

	public BigDecimal quantity() {
		return this.quantity;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Fill typedObject = (Fill) anObject;
			equalObjects =
					this.filledOn().equals(typedObject.filledOn()) &&
					this.price().equals(typedObject.price()) &&
					this.quantity().equals(typedObject.quantity());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (48351 * 11)
			+ this.filledOn().hashCode()
			+ this.price().hashCode()
			+ this.quantity().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Fill [filledOn=" + filledOn + ", price=" + price
				+ ", quantity=" + quantity + "]";
	}

	private void setFilledOn(Date aFilledOn) {
		this.filledOn = aFilledOn;
	}

	private void setPrice(Money aPrice) {
		this.price = aPrice;
	}

	private void setQuantity(BigDecimal aQuantity) {
		this.quantity = aQuantity;
	}
}
