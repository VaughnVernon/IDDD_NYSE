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

import junit.framework.TestCase;

public class MoneyTest extends TestCase {

	public MoneyTest() {
		super();
	}

	public void testMoneyConstruction() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		assertEquals(money, otherMoney);

		assertNotSame(money, otherMoney);
	}

	public void testMoneyCopy() throws Exception {
		Money money = new Money("123.45");

		Money moneyCopy = new Money(money);

		assertEquals(money, moneyCopy);

		assertNotSame(money, moneyCopy);
	}

	public void testAddedTo() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		assertEquals(money.multipliedBy(2), money.addedTo(otherMoney));

		assertEquals(otherMoney.multipliedBy(2), otherMoney.addedTo(money));
	}

	public void testDividedBy() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		assertEquals(new Money("1.0000"), money.dividedBy(otherMoney));

		Money moreMoney = new Money(new BigDecimal("1000.00"));

		Money lessMoney = new Money(new BigDecimal("10.00"));

		assertEquals(new Money("100.0000"), moreMoney.dividedBy(lessMoney));

		assertEquals(new Money("10.0000"), moreMoney.dividedBy(100));
	}

	public void testIsGreaterThan() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		Money moreMoney = new Money("200.00");

		Money lessMoney = new Money("123.44");

		assertFalse(money.isGreaterThan(otherMoney));

		assertFalse(money.isGreaterThan(moreMoney));

		assertTrue(money.isGreaterThan(lessMoney));
	}

	public void testIsLessThan() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		Money moreMoney = new Money("200.00");

		Money lessMoney = new Money("123.44");

		assertFalse(money.isLessThan(otherMoney));

		assertTrue(money.isLessThan(moreMoney));

		assertFalse(money.isLessThan(lessMoney));
	}

	public void testIsZero() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money noMoney = new Money("0");

		assertFalse(money.isZero());

		assertTrue(noMoney.isZero());
	}

	public void testMultipliedBy() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		assertEquals(money.addedTo(otherMoney), money.multipliedBy(2));

		assertEquals(otherMoney.addedTo(money), otherMoney.multipliedBy(2));

		assertEquals(new Money("15239.9025"), money.multipliedBy(otherMoney));
	}

	public void testPercentageOf() throws Exception {
		Money money = new Money(new BigDecimal("10.00"));

		Money otherMoney = new Money("100.00");

		assertEquals(new Money("10.00"), money.percentageOf(otherMoney));
	}

	public void testSubtractFrom() throws Exception {
		Money money = new Money(new BigDecimal("123.45"));

		Money otherMoney = new Money("123.45");

		assertEquals(new Money("0.00"), money.subtract(otherMoney));

		assertEquals(new Money("123.00"), money.subtract(new Money("0.45")));
	}
}
