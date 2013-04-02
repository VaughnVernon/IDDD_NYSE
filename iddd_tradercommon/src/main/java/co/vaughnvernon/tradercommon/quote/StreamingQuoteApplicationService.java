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

import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quotebar.QuoteBar;

public class StreamingQuoteApplicationService {

	private StreamingQuoteRepository streamingQuoteRepository;

	public StreamingQuoteApplicationService(
			StreamingQuoteRepository aStreamingQuoteRepository) {

		super();

		this.streamingQuoteRepository = aStreamingQuoteRepository;
	}

	public Money latestPriceFor(TickerSymbol aTickerSymbol) {
		StreamingQuote streamingQuote =
				this.streamingQuoteRepository()
					.streamingQuoteOfSymbol(aTickerSymbol.symbol());

		return streamingQuote == null ? null : streamingQuote.quote().price();
	}

	public void updateStreamingQuoteWith(QuoteBar aQuoteBar) {
		StreamingQuote streamingQuote =
				this.streamingQuoteRepository()
					.streamingQuoteOfSymbol(aQuoteBar.symbol());

		if (streamingQuote == null) {
			streamingQuote =
					new StreamingQuote(
							new Quote(
									new TickerSymbol(aQuoteBar.symbol()),
									aQuoteBar.price()));
		} else {
			streamingQuote.updateWith(aQuoteBar.price());
		}

		this.streamingQuoteRepository().save(streamingQuote);
	}

	private StreamingQuoteRepository streamingQuoteRepository() {
		return this.streamingQuoteRepository;
	}
}
