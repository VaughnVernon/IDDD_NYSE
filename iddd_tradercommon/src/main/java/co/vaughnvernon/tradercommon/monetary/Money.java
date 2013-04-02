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

package co.vaughnvernon.tradercommon.monetary;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class Money {

	private static final int SCALE = 4;

	private BigDecimal amount;
	private int scale;

	public Money() {
		this(BigDecimal.ZERO, SCALE);
	}

	public Money(BigDecimal anAmount) {
		this(anAmount, SCALE);
	}

	public Money(BigDecimal anAmount, int aScale) {
		super();

		this.setAmount(anAmount);
		this.setScale(aScale);
	}

	public Money(int anAmount) {
		this(new BigDecimal(anAmount));
	}

	public Money(String anAmountRepresentation) {
		super();

		this.setAmount(new BigDecimal(anAmountRepresentation));
		this.setScale(SCALE);
	}

	public Money(Money aMoney) {
		this(aMoney.amount(), aMoney.scale());
	}

	public Money addedTo(Money anAmount) {
		BigDecimal total = this.amount().add(anAmount.amount());

		return new Money(total, this.scale());
	}

	public BigDecimal amount() {
		return this.amount;
	}

	public String amountAsString() {
		return this.amount().toPlainString();
	}

	public Money dividedBy(Money anAmount) {
		return this.dividedBy(anAmount.amount());
	}

	public Money dividedBy(BigDecimal anAmount) {
		BigDecimal result =
				this.amount()
					.divide(
							anAmount,
							this.scale(),
							RoundingMode.HALF_UP);

		return new Money(result, this.scale());
	}

	public Money dividedBy(int aQuantity) {
		return this.dividedBy(new BigDecimal(aQuantity));
	}

	public boolean isGreaterThan(Money anAmount) {
		return this.amount().compareTo(anAmount.amount()) > 0;
	}

	public boolean isGreaterThanOrEqualTo(Money anAmount) {
		return this.amount().compareTo(anAmount.amount()) >= 0;
	}

	public boolean isLessThan(Money anAmount) {
		return this.amount().compareTo(anAmount.amount()) < 0;
	}

	public boolean isLessThanOrEqualTo(Money anAmount) {
		return this.amount().compareTo(anAmount.amount()) <= 0;
	}

	public boolean isZero() {
		return this.amount().compareTo(BigDecimal.ZERO) == 0;
	}

	public Money multipliedBy(Money anAmount) {
		return this.multipliedBy(anAmount.amount());
	}

	public Money multipliedBy(BigDecimal anAmount) {
		BigDecimal total = this.amount().multiply(anAmount);

		return new Money(total, this.scale());
	}

	public Money multipliedBy(int aQuantity) {
		return this.multipliedBy(new BigDecimal(aQuantity));
	}

	public Money percentageOf(Money aTotal) {
		BigDecimal percent =
				this.amount()
					.divide(
							aTotal.amount(),
							this.scale(),
							RoundingMode.HALF_UP);

		percent = percent.multiply(
					BigDecimal.valueOf(100),
					new MathContext(4, RoundingMode.HALF_UP));

		return new Money(percent, this.scale());
	}

	public Money subtract(Money anAmount) {
		BigDecimal total = this.amount().subtract(anAmount.amount());

		return new Money(total, this.scale());
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			Money typedObject = (Money) anObject;
			equalObjects =
					this.amount().equals(typedObject.amount()) &&
					this.scale() == typedObject.scale();
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
				+ (99013 * 37)
				+ this.amount().hashCode()
				+ this.scale();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "Money [amount=" + this.amount() + ", scale=" + this.scale() + "]";
	}

	private void setAmount(BigDecimal anAmount) {
		if (anAmount == null) {
			throw new IllegalArgumentException("Amount must be provided.");
		}
		this.amount = anAmount;
	}

	private int scale() {
		return this.scale;
	}

	private void setScale(int aScale) {
		this.scale = aScale;
	}
}
