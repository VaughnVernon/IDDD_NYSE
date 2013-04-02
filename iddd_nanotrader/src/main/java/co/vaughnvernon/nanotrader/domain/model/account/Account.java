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

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import co.vaughnvernon.nanotrader.domain.model.order.BuyOrder;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class Account {

	private AccountId accountId;
	private Money cashBalance;
	private Date establishedOn;
	private Set<Holding> holdings;
	private Money openingBalance;
	private Set<Payment> payments;
	private ProfileId profileId;

	public Account(ProfileId aProfileId, Money anOpeningCashBalance) {
		super();

		this.setAccountId(AccountId.unique());
		this.setCashBalance(anOpeningCashBalance);
		this.setEstablishedOn(new Date());
		this.setHoldings(new HashSet<Holding>());
		this.setOpeningBalance(anOpeningCashBalance);
		this.setPayments(new HashSet<Payment>());
		this.setProfileId(aProfileId);
	}

	public AccountId accountId() {
		return accountId;
	}

	public Money cashBalance() {
		return this.cashBalance;
	}

	public Money currentBalance() {
		Money balance = new Money(this.cashBalance());

		for (Holding holding : this.modifiableHoldings()) {
			balance = balance.addedTo(holding.totalValue());
		}

		return balance;
	}

	public Date establishedOn() {
		return this.establishedOn;
	}

	public Set<Payment> feePayments() {
		return Collections.unmodifiableSet(this.payments);
	}

	public Set<Holding> holdings() {
		return Collections.unmodifiableSet(this.holdings);
	}

	public Money openingBalance() {
		return this.openingBalance;
	}

	public BuyOrder placeBuyOrder(
			TickerSymbol aTickerSymbol,
			Money aPrice,
			int aNumberOfShares,
			Money anOrderFee) {

		Quote quote = new Quote(aTickerSymbol, aPrice);

		Money totalCost = anOrderFee.addedTo(quote.valueOfPricedShares(aNumberOfShares));

		if (totalCost.isGreaterThan(this.cashBalance())) {
			// TODO: Charge credit card for order

			throw new IllegalStateException("Cash balance is too low for this buy order.");
		}

		return new BuyOrder(
				this.accountId(),
				quote,
				aNumberOfShares,
				anOrderFee);
	}

	public ProfileId profileId() {
		return profileId;
	}

	public void reconcileWith(Payment aPayment) {
		if (!this.accountId().equals(aPayment.accountId())) {
			throw new IllegalStateException("Account and fee payment are unrelated.");
		}
		if (!this.isReconciledWith(aPayment)) {
			this.setCashBalance(this.cashBalance().subtract(aPayment.totalAmount()));

			this.modifiablePayments().add(aPayment);
		}
	}

	public void reconcileWith(Holding aHolding) {
		if (!this.accountId().equals(aHolding.accountId())) {
			throw new IllegalStateException("Account and Holding are unrelated.");
		}
		if (!this.isReconciledWith(aHolding)) {
			this.modifiableHoldings().add(aHolding);
		}
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Account typedObject = (Account) anObject;
			equalObjects =
					this.accountId().equals(typedObject.accountId()) &&
					this.profileId().equals(typedObject.profileId());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (33991 * 83)
			+ this.accountId().hashCode()
			+ this.profileId().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", cashBalance="
				+ cashBalance + ", establishedOn=" + establishedOn
				+ ", openingBalance=" + openingBalance + ", profileId="
				+ profileId + "]";
	}

	private void setAccountId(AccountId anAccountId) {
		if (anAccountId == null) {
			throw new IllegalArgumentException("AccountId must be provided.");
		}
		this.accountId = anAccountId;
	}

	private void setCashBalance(Money aCashBalance) {
		if (aCashBalance == null) {
			throw new IllegalArgumentException("Cash balance must be provided.");
		}
		this.cashBalance = aCashBalance;
	}

	private void setEstablishedOn(Date aDate) {
		if (aDate == null) {
			throw new IllegalArgumentException("Established-on date must be provided.");
		}
		this.establishedOn = aDate;
	}

	private void setHoldings(Set<Holding> aHoldings) {
		if (aHoldings == null) {
			throw new IllegalArgumentException("Holdings must not be null.");
		}
		this.holdings = aHoldings;
	}

	private void setOpeningBalance(Money anOpeningBalance) {
		if (anOpeningBalance == null) {
			throw new IllegalArgumentException("Opening balance must be provided.");
		}
		this.openingBalance = anOpeningBalance;
	}

	private void setPayments(Set<Payment> aPayments) {
		if (aPayments == null) {
			throw new IllegalArgumentException("Payments must be provided.");
		}
		this.payments = aPayments;
	}

	private void setProfileId(ProfileId aProfileId) {
		if (aProfileId == null) {
			throw new IllegalArgumentException("ProfileId must be provided.");
		}
		this.profileId = aProfileId;
	}

	private Set<Holding> modifiableHoldings() {
		return this.holdings;
	}

	private Set<Payment> modifiablePayments() {
		return this.payments;
	}

	private boolean isReconciledWith(Holding aHolding) {
		return this.modifiableHoldings().contains(aHolding);
	}

	private boolean isReconciledWith(Payment aPayment) {
		return this.modifiablePayments().contains(aPayment);
	}
}
