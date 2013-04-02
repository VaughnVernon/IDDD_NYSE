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
import java.util.Calendar;
import java.util.Random;

import co.vaughnvernon.tradercommon.monetary.Money;

public class SimulatedEquityActivity {

	public enum Direction {
		Down {
			public boolean isDown() {
				return true;
			}
		},

		Up {
			public boolean isUp() {
				return true;
			}
		};

		public boolean isDown() {
			return false;
		}

		public boolean isUp() {
			return false;
		}

		public Direction opposite() {
			return this == Down ? Up : Down;
		}
	}

	private Random changeRandom;
	private int changeThreshold;
	private Money close;
	private String companyName;
	private Direction direction;
	private int directionStepsCount;
	private int directionStepsTotal;
	private Money high;
	private Money low;
	private Money open;
	private Money price;
	private Random quantityRandom;
	private Random stepsRandom;
	private String ticker;
	private int volumn;

	protected SimulatedEquityActivity(
			String aCompanyName,
			String aTicker,
			Money aPrice,
			int aChangeThreshold,
			Direction aDirection) {

		super();

		this.setChangeRandom(new Random());
		this.setChangeThreshold(aChangeThreshold);
		this.setClose(aPrice);
		this.setCompanyName(aCompanyName);
		this.setDirection(aDirection);
		this.setHigh(aPrice);
		this.setLow(aPrice);
		this.setOpen(new Money());
		this.setPrice(aPrice);
		this.setQuantityRandom(new Random());
		this.setStepsRandom(new Random());
		this.setTicker(aTicker);

		this.setDirectionStepsCount(0);
		this.setDirectionStepsTotal(this.randomTotalSteps());
	}

	public FeedQuote feedQuote() {
		int quantity = this.generateQuantity();

		this.increaseVolumnBy(quantity);

		FeedQuote feedQuote =
				new FeedQuote(
						this.companyName(),
						this.high(),
						this.low(),
						this.open(),
						this.close(),
						this.price(),
						new BigDecimal(quantity),
						this.ticker(),
						new BigDecimal(this.volumn()));

		return feedQuote;
	}

	public void simulateChange() {
		Money changeAmount = this.changeAmount();

		if (this.direction().isUp()) {
			this.setPrice(this.price().addedTo(changeAmount));
			this.checkForNewHigh();
		} else {
			this.setPrice(this.price().subtract(changeAmount));
			this.checkForNewLow();
		}

		if (this.open().isZero()) {
			this.setOpen(this.price());
		}

		if (this.directionStepsCountIncremented() >= this.directionStepsTotal()) {
			this.setDirection(this.direction().opposite());
			this.setDirectionStepsCount(0);
			this.setDirectionStepsTotal(this.randomTotalSteps());
		}
	}

	private Money close() {
		return this.close;
	}

	private void setClose(Money aClose) {
		this.close = aClose;
	}

	private String companyName() {
		return this.companyName;
	}

	private void setCompanyName(String aCompanyName) {
		this.companyName = aCompanyName;
	}

	private Money changeAmount() {
		int changeAmount =
				this.changeRandom()
					.nextInt(this.changeThreshold() + 1);

		if (changeAmount == 0) {
			changeAmount = 1;
		}

		BigDecimal cents = new BigDecimal(changeAmount);

		return new Money(cents.divide(new BigDecimal("100.0")));
	}

	private Random changeRandom() {
		return this.changeRandom;
	}

	private void setChangeRandom(Random aRandom) {
		this.changeRandom = aRandom;
	}

	private int changeThreshold() {
		return this.changeThreshold;
	}

	private void setChangeThreshold(int aChangeThreshold) {
		this.changeThreshold = aChangeThreshold;
	}

	private Direction direction() {
		return this.direction;
	}

	private void setDirection(Direction aDirection) {
		this.direction = aDirection;
	}

	private int directionStepsCount() {
		return this.directionStepsCount;
	}

	private void setDirectionStepsCount(int aCount) {
		this.directionStepsCount = aCount;
	}

	private int directionStepsCountIncremented() {
		this.setDirectionStepsCount(this.directionStepsCount() + 1);

		return this.directionStepsCount();
	}

	private int directionStepsTotal() {
		return this.directionStepsTotal;
	}

	private void setDirectionStepsTotal(int aTotal) {
		this.directionStepsTotal = aTotal;
	}

	private int generateQuantity() {
		Calendar cal = Calendar.getInstance();

		int bound = cal.get(Calendar.SECOND);

		if (bound > 30) {
			bound = bound / 3;
		} else {
			bound = bound / 2;
		}

		if (bound == 0) {
			bound = 1;
		} else if (bound < 0) {
			bound = -bound;
		}

		return this.quantityRandom().nextInt(bound) + 1;
	}

	private Money high() {
		return this.high;
	}

	private void checkForNewHigh() {
		if (this.price().isGreaterThan(this.high())) {
			this.setHigh(this.price());
		}
	}

	private void setHigh(Money aHigh) {
		this.high = aHigh;
	}

	private Money low() {
		return this.low;
	}

	private void checkForNewLow() {
		if (this.price().isLessThan(this.low())) {
			this.setLow(this.price());
		}
	}

	private void setLow(Money aLow) {
		this.low = aLow;
	}

	private Money open() {
		return this.open;
	}

	private void setOpen(Money anOpen) {
		this.open = anOpen;
	}

	private Money price() {
		return this.price;
	}

	private void setPrice(Money aPrice) {
		this.price = aPrice;
	}

	private Random quantityRandom() {
		return this.quantityRandom;
	}

	private void setQuantityRandom(Random aRandom) {
		this.quantityRandom = aRandom;
	}

	private int randomTotalSteps() {
		int totalSteps = this.stepsRandom().nextInt(10);

		if (totalSteps == 0) {
			totalSteps = 1;
		}

		return totalSteps;
	}

	private Random stepsRandom() {
		return this.stepsRandom;
	}

	private void setStepsRandom(Random aRandom) {
		this.stepsRandom = aRandom;
	}

	private String ticker() {
		return this.ticker;
	}

	private void setTicker(String aTicker) {
		this.ticker = aTicker;
	}

	private int volumn() {
		return this.volumn;
	}

	private void increaseVolumnBy(int aNumberOfShares) {
		int otherVolumn = 0;

		if (aNumberOfShares < 500) {
			otherVolumn = aNumberOfShares / 10;
		} else {
			otherVolumn = aNumberOfShares / 8;
		}

		this.setVolumn(this.volumn() + otherVolumn + aNumberOfShares);
	}

	private void setVolumn(int aVolumn) {
		this.volumn = aVolumn;
	}
}
