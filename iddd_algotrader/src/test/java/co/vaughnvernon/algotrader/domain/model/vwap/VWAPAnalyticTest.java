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

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;

public class VWAPAnalyticTest extends TestCase {

	public VWAPAnalyticTest() {
		super();
	}

	public void testVwapAnalytic() {
		Money price = new Money(BigDecimal.valueOf(30.0));
		BigDecimal volume = BigDecimal.valueOf(100.0);
		PriceVolume priceVolume = new PriceVolume(price, volume);
		VWAPAnalytic vwapAnalytic = new VWAPAnalytic("ORCL", priceVolume);
		Money vwap = price.multipliedBy(volume).dividedBy(volume);
		assertEquals(vwap, vwapAnalytic.vwap());
		assertTrue("PriceVolume should be price * volume", priceVolume.calculated().equals(price.multipliedBy(volume)));
	}

	public void testVwapAnalyticMath() {
		Money price = new Money("30.00");
		BigDecimal volume = new BigDecimal("100.00");
		PriceVolume priceVolume = new PriceVolume(price, volume);
		VWAPAnalytic vwapAnalytic = new VWAPAnalytic("ORCL", priceVolume);
		Money secondPrice = new Money("31.00");
		BigDecimal secondVolume = new BigDecimal("1000.00");
		PriceVolume secondPriceVolume = new PriceVolume(secondPrice, secondVolume);
		vwapAnalytic.accumulatePriceVolume(secondPriceVolume);

		Money expectedVwap =
				price.multipliedBy(volume)
					 .addedTo(secondPrice.multipliedBy(secondVolume))
					 .dividedBy(volume.add(secondVolume));

		assertEquals(expectedVwap, vwapAnalytic.vwap());
		assertTrue("PriceVolume should be price * volume", priceVolume.calculated().equals(price.multipliedBy(volume)));
	}

	public void testIsReadyToTrade() throws Exception {
		Money price = new Money("30.00");
		BigDecimal volume = new BigDecimal("100.00");
		PriceVolume priceVolume = new PriceVolume(price, volume);

		VWAPAnalytic vwapAnalytic = new VWAPAnalytic("ORCL", priceVolume);

		for (int idx = 0; idx < VWAPAnalytic.TRADABLE_QUOTE_BARS - 2; ++idx) {
			price = price.addedTo(new Money("0.01"));
			volume = volume.add(new BigDecimal("10.0"));
			vwapAnalytic.accumulatePriceVolume(new PriceVolume(price, volume));
		}

		assertEquals(VWAPAnalytic.TRADABLE_QUOTE_BARS - 1, vwapAnalytic.priceVolumes().size());
		assertFalse(vwapAnalytic.isReadyToTrade());

		price = price.addedTo(new Money("0.01"));
		volume = volume.add(new BigDecimal("10.0"));
		vwapAnalytic.accumulatePriceVolume(new PriceVolume(price, volume));

		assertEquals(VWAPAnalytic.TRADABLE_QUOTE_BARS, vwapAnalytic.priceVolumes().size());
		assertTrue(vwapAnalytic.isReadyToTrade());

		for (int idx = 0; idx < 100; ++idx) {
			price = price.addedTo(new Money("0.01"));
			volume = volume.add(new BigDecimal("10.0"));
			vwapAnalytic.accumulatePriceVolume(new PriceVolume(price, volume));
		}

		assertEquals(VWAPAnalytic.TRADABLE_QUOTE_BARS, vwapAnalytic.priceVolumes().size());
		assertTrue(vwapAnalytic.isReadyToTrade());
	}
}
