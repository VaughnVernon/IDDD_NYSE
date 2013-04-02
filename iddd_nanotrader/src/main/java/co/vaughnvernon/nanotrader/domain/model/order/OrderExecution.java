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

public abstract class OrderExecution {

	private Date filledDate;
	private Date openDate;
	private int quantityOfSharesOrdered;
	private int quantityOfSharesOutstanding;

	public OrderExecution(int aQuantityOfSharesOrdered) {
		this(new Date(), aQuantityOfSharesOrdered, aQuantityOfSharesOrdered);
	}

	public Date filledDate() {
		return this.filledDate;
	}

	public boolean isFilled() {
		return this.filledDate() != null;
	}

	public Date openDate() {
		return this.openDate;
	}

	public boolean isOpen() {
		return !this.isFilled();
	}

	public int quantityOfSharesOrdered() {
		return this.quantityOfSharesOrdered;
	}

	public int quantityOfSharesOutstanding() {
		return this.quantityOfSharesOutstanding;
	}

	public boolean wouldOverrunExecutionWith(int aQuantityOfShares) {
		int adjustedQuantity = this.quantityOfSharesOutstanding() - aQuantityOfShares;

		return adjustedQuantity < 0;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			OrderExecution typedObject = (OrderExecution) anObject;
			equalObjects =
					this.openDate().equals(typedObject.openDate()) &&
					((this.filledDate() == null && this.filledDate() == typedObject.filledDate()) ||
					 this.filledDate().equals(typedObject.filledDate()) &&
					this.quantityOfSharesOrdered() == typedObject.quantityOfSharesOrdered() &&
					this.quantityOfSharesOutstanding() == typedObject.quantityOfSharesOutstanding());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
				+ (9381 * 101)
				+ this.openDate().hashCode()
				+ (this.filledDate() == null ? null : this.filledDate().hashCode())
				+ this.quantityOfSharesOrdered()
				+ this.quantityOfSharesOutstanding();

		return hashCodeValue;
	}

	protected OrderExecution(
			Date anOpenDate,
			int aQuantityOfSharesOrdered,
			int aQuantityOfSharesOutstanding) {

		super();

		this.setFilledDate(aQuantityOfSharesOutstanding == 0 ? new Date() : null);
		this.setOpenDate(anOpenDate);
		this.setQuantityOfSharesOrdered(aQuantityOfSharesOrdered);
		this.setQuantityOfSharesOutstanding(aQuantityOfSharesOutstanding);
	}

	private void setFilledDate(Date aFilledDate) {
		this.filledDate = aFilledDate;
	}

	private void setOpenDate(Date anOpenDate) {
		if (anOpenDate == null) {
			throw new IllegalArgumentException("Open date must be provided.");
		}
		this.openDate = anOpenDate;
	}

	private void setQuantityOfSharesOrdered(int aQuantityOfSharesOrdered) {
		if (aQuantityOfSharesOrdered <= 0) {
			throw new IllegalArgumentException("Quantity of shares ordered must be greater than zero.");
		}
		this.quantityOfSharesOrdered = aQuantityOfSharesOrdered;
	}

	private void setQuantityOfSharesOutstanding(int aQuantityOfSharesOutstanding) {
		if (aQuantityOfSharesOutstanding < 0) {
			throw new IllegalArgumentException("Quantity of shares outstanding must be zero or greater.");
		}
		this.quantityOfSharesOutstanding = aQuantityOfSharesOutstanding;
	}
}
