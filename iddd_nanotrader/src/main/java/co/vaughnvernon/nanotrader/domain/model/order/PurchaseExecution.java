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

public final class PurchaseExecution extends OrderExecution {

	public PurchaseExecution(int aQuantityOfSharesOrdered) {
		super(new Date(), aQuantityOfSharesOrdered, aQuantityOfSharesOrdered);
	}

	public PurchaseExecution withPurchasedSharesOf(int aQuantity) {
		if (this.wouldOverrunExecutionWith(aQuantity)) {
			throw new IllegalArgumentException("Acquired too many shares.");
		}

		int quantityOfSharesOutstanding = this.quantityOfSharesOutstanding() - aQuantity;

		return new PurchaseExecution(this.openDate(), this.quantityOfSharesOrdered(), quantityOfSharesOutstanding);
	}

	@Override
	public String toString() {
		return "PurchaseExecution [openDate=" + this.openDate()
				+ ", filledDate=" + this.filledDate()
				+ ", quantityOfSharesOrdered=" + this.quantityOfSharesOrdered()
				+ ", quantityOfSharesOutstanding=" + this.quantityOfSharesOutstanding() + "]";
	}

	private PurchaseExecution(
			Date anOpenDate,
			int aQuantityOfSharesOrdered,
			int aQuantityOfSharesOutstanding) {

		super(anOpenDate, aQuantityOfSharesOrdered, aQuantityOfSharesOutstanding);
	}
}
