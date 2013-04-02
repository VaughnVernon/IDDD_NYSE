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

package co.vaughnvernon.tradercommon.quote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.infrastructure.persistence.InMemoryStreamingQuoteRepository;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;
import co.vaughnvernon.tradercommon.quotebar.QuoteBar;

public class StreamingQuoteApplicationServiceTest extends TestCase {

	private StreamingQuoteApplicationService streamingQuoteApplicationService;

	public StreamingQuoteApplicationServiceTest() {
		super();
	}

	public void testUpdateStreamingQuoteAndQueryLatestPrice() throws Exception {
		QuoteBar googQuoteBar = this.googQuoteBarFixture();
		this.streamingQuoteApplicationService()
			.updateStreamingQuoteWith(googQuoteBar);

		Money googPrice =
				this.streamingQuoteApplicationService()
					.latestPriceFor(new TickerSymbol("GOOG"));

		assertEquals(googQuoteBar.price(), googPrice);

		QuoteBar orclQuoteBar = this.orclQuoteBarFixture();
		this.streamingQuoteApplicationService()
			.updateStreamingQuoteWith(orclQuoteBar);

		Money orclPrice =
				this.streamingQuoteApplicationService()
					.latestPriceFor(new TickerSymbol("ORCL"));

		assertEquals(orclQuoteBar.price(), orclPrice);

		QuoteBar googUpdatedQuoteBar = this.googUpdatedQuoteBarFixture();
		this.streamingQuoteApplicationService()
			.updateStreamingQuoteWith(googUpdatedQuoteBar);

		googPrice =
				this.streamingQuoteApplicationService()
					.latestPriceFor(new TickerSymbol("GOOG"));

		assertEquals(googUpdatedQuoteBar.price(), googPrice);
		assertFalse(googUpdatedQuoteBar.price().equals(googQuoteBar.price()));
	}

	@Override
	protected void setUp() throws Exception {
		this.streamingQuoteApplicationService =
				new StreamingQuoteApplicationService(
						InMemoryStreamingQuoteRepository.instance());

		super.setUp();
	}

	private QuoteBar googQuoteBarFixture() {

		Collection<PriceVolume> priceVolumes =
				this.priceVolumeValues(new Money("720.40"), new BigDecimal("87231"));

		QuoteBar quoteBar =
				new QuoteBar("Google, Inc.", "GOOG", new Money("720.55"), new Money("720.30"), new Money("720.25"),
						new Money("720.60"), new Money("720.25"), new BigDecimal("87231"), priceVolumes,
						new BigDecimal("12003"), priceVolumes.size());

		return quoteBar;
	}

	private QuoteBar googUpdatedQuoteBarFixture() {

		Collection<PriceVolume> priceVolumes =
				this.priceVolumeValues(new Money("721.00"), new BigDecimal("87231"));

		QuoteBar quoteBar =
				new QuoteBar("Google, Inc.", "GOOG", new Money("721.10"), new Money("720.30"), new Money("720.25"),
						new Money("720.60"), new Money("720.25"), new BigDecimal("87231"), priceVolumes,
						new BigDecimal("12003"), 22);

		return quoteBar;
	}

	private QuoteBar orclQuoteBarFixture() {

		Collection<PriceVolume> priceVolumes =
				this.priceVolumeValues(new Money("34.90"), new BigDecimal("73721"));

		QuoteBar quoteBar =
				new QuoteBar("Oracle, Inc.", "ORCL", new Money("35.20"), new Money("35.25"), new Money("35.95"),
						new Money("35.15"), new Money("35.19"), new BigDecimal("73721"), priceVolumes,
						new BigDecimal("15145"), 84);

		return quoteBar;
	}

	private Collection<PriceVolume> priceVolumeValues(
			Money aBasePrice,
			BigDecimal aBaseVolume) {

		List<PriceVolume> priceVolumes = new ArrayList<PriceVolume>();
		Money price = aBasePrice;
		BigDecimal volume = aBaseVolume;

		for (int idx = 0; idx < 100; ++idx) {
			if ((idx % 2) == 0) {
				price = price.addedTo(new Money("0.02"));
			} else if ((idx % 3) == 0) {
				price = price.subtract(new Money("0.03"));
			} else {
				price = price.addedTo(new Money("0.01"));
			}

			volume = volume.add(aBaseVolume);

			priceVolumes.add(new PriceVolume(price, volume));
		}

		return priceVolumes;
	}

	private StreamingQuoteApplicationService streamingQuoteApplicationService() {
		return this.streamingQuoteApplicationService;
	}
}
