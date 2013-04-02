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

package co.vaughnvernon.tradercommon.quotebar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.pricevolume.PriceVolume;
import co.vaughnvernon.tradercommon.quotefeed.FeedQuote;

public class QuoteBarAggregator {

	public QuoteBar aggregateFrom(Collection<FeedQuote> aQuoteCollection) {

		Iterator<FeedQuote> iterator = aQuoteCollection.iterator();
		FeedQuote quote = iterator.next();

		String companyName = quote.companyName();
		String symbol = quote.symbol();
		Money close = quote.close();
		Money open = quote.open();

		Money high = new Money(BigDecimal.valueOf(Integer.MIN_VALUE));
		Money low = new Money(BigDecimal.valueOf(Integer.MAX_VALUE));
		Money lastPrice = new Money();
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalVolume = BigDecimal.ZERO;

		List<PriceVolume> priceVolumes = new ArrayList<PriceVolume>();

		do {

			if (high.isLessThan(quote.high())) {
				high = quote.high();
			}

			if (low.isGreaterThan(quote.low())) {
				low = quote.low();
			}

			totalQuantity = totalQuantity.add(quote.quantity());

			priceVolumes.add(new PriceVolume(quote.price(), quote.volume()));

			totalVolume = totalVolume.add(quote.volume());

			if (iterator.hasNext()) {
				quote = iterator.next();
			} else {
				lastPrice = quote.price();

				quote = null;
			}

		} while (quote != null);

		QuoteBar quoteBar =
				new QuoteBar(companyName, symbol, lastPrice, open, close,
						high, low, totalVolume, priceVolumes,
						totalQuantity, aQuoteCollection.size());

		return quoteBar;
	}
}
