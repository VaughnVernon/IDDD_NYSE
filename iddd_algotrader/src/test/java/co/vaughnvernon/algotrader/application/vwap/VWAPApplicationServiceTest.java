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

package co.vaughnvernon.algotrader.application.vwap;

import java.math.BigDecimal;
import java.util.Arrays;

import junit.framework.TestCase;
import co.vaughnvernon.algotrader.domain.model.vwap.VWAPTradingService;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;
import co.vaughnvernon.tradercommon.quotebar.QuoteBar;

public class VWAPApplicationServiceTest extends TestCase {

	private MockVWAPTradingService mockVWAPTradingService;
	private VWAPApplicationService vwapApplicationService;

	public VWAPApplicationServiceTest() {
		super();
	}

	public void testFillAlgoOrdersUsing() throws Exception {
		QuoteBar quoteBar = this.quoteBarValue();

		vwapApplicationService.fillAlgoOrdersUsing(quoteBar);

		assertTrue(this.mockVWAPTradingService.traded);
	}

	@Override
	protected void setUp() throws Exception {
		this.mockVWAPTradingService =
				new MockVWAPTradingService();

		this.vwapApplicationService =
				new VWAPApplicationService(this.mockVWAPTradingService);
	}

	private QuoteBar quoteBarValue() {
		QuoteBar quoteBar =
				new QuoteBar(
					"Oracle, Inc.",
					"ORCL",
					new Money("32.10"),
					new Money("32.25"),
					new Money("32.15"),
					new Money("33.30"),
					new Money("31.85"),
					new BigDecimal("20"),
					Arrays.asList(new PriceVolume[] {
							new PriceVolume(
									new Money("725.50"),
									new BigDecimal("100")) }),
					new BigDecimal("1000"),
					1);

		return quoteBar;
	}

	private static class MockVWAPTradingService extends VWAPTradingService {
		private boolean traded;

		public MockVWAPTradingService() {
			super(null, null);
		}

		@Override
		public void tradeUnfilledBuyOrdersUsing(QuoteBar aQuoteBar) {
			this.traded = true;
		}
	}
}
