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

package co.vaughnvernon.nanotrader.domain.model.account;

import java.util.Date;

import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.monetary.Money;

public class Payment {

	private AccountId accountId;
	private Money cost;
	private String description;
	private Money fee;
	private OrderId orderId;
	private Date paidOn;

	public Payment(
			AccountId anAccountId,
			OrderId anOrderId,
			String aDescription,
			Money aCost,
			Money aFee,
			Date aPaidOnDate) {
		super();

		this.setAccountId(anAccountId);
		this.setCost(aCost);
		this.setDescription(aDescription);
		this.setFee(aFee);
		this.setOrderId(anOrderId);
		this.setPaidOn(aPaidOnDate);
	}

	public AccountId accountId() {
		return this.accountId;
	}

	public Money cost() {
		return this.cost;
	}

	public String description() {
		return this.description;
	}

	public Money fee() {
		return this.fee;
	}

	public OrderId orderId() {
		return this.orderId;
	}

	public Date paidOn() {
		return this.paidOn;
	}

	public Money totalAmount() {
		return this.cost().addedTo(this.fee());
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Payment typedObject = (Payment) anObject;
			equalObjects =
					this.accountId().equals(typedObject.accountId()) &&
					this.cost().equals(typedObject.cost()) &&
					this.description().equals(typedObject.description()) &&
					this.fee().equals(typedObject.fee()) &&
					this.orderId().equals(typedObject.orderId()) &&
					this.paidOn().equals(typedObject.paidOn());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (87643 * 107)
			+ this.accountId().hashCode()
			+ this.cost().hashCode()
			+ this.description().hashCode()
			+ this.fee().hashCode()
			+ this.orderId().hashCode()
			+ this.paidOn().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Payment [accountId=" + accountId + ", amount=" + fee
				+ ", description=" + description + ", orderId=" + orderId
				+ ", paidOn=" + paidOn + "]";
	}

	private void setAccountId(AccountId anAccountId) {
		if (anAccountId == null) {
			throw new IllegalArgumentException("Account id must be provided.");
		}
		this.accountId = anAccountId;
	}

	public void setCost(Money aCost) {
		if (aCost == null) {
			throw new IllegalArgumentException("Cost must be provided.");
		}
		this.cost = aCost;
	}

	private void setDescription(String aDescription) {
		if (aDescription == null || aDescription.trim().isEmpty()) {
			throw new IllegalArgumentException("Description must be provided.");
		}
		this.description = aDescription;
	}

	private void setFee(Money aFee) {
		if (aFee == null) {
			throw new IllegalArgumentException("Amount must be provided.");
		}
		this.fee = aFee;
	}

	private void setOrderId(OrderId anOrderId) {
		if (anOrderId == null) {
			throw new IllegalArgumentException("Order id must be provided.");
		}
		this.orderId = anOrderId;
	}

	private void setPaidOn(Date aPaidOnDate) {
		if (aPaidOnDate == null) {
			throw new IllegalArgumentException("Paid on date must be provided.");
		}
		this.paidOn = aPaidOnDate;
	}
}
