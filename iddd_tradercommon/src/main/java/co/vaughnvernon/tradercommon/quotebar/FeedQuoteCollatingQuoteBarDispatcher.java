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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vaughnvernon.tradercommon.quotefeed.FeedQuote;
import co.vaughnvernon.tradercommon.quotefeed.FeedQuoteSubscriber;

public class FeedQuoteCollatingQuoteBarDispatcher
		implements FeedQuoteSubscriber, QuoteBarDispatcher {

	private Map<String,CollatedFeedQuotes> collatedFeedQuotes;
	private int maximumQuoteCount;
	private QuoteBarAggregator quoteBarAggregator;
	private List<QuoteBarInterest> quoteBarInterests;
	private long timeoutThreshold;

	public FeedQuoteCollatingQuoteBarDispatcher(
			QuoteBarAggregator aQuoteBarAggregator,
			long aTimeoutThreshold,
			int aMaximumQuoteCount) {

		super();

		this.collatedFeedQuotes = new HashMap<String,CollatedFeedQuotes>();
		this.maximumQuoteCount = aMaximumQuoteCount;
		this.quoteBarAggregator = aQuoteBarAggregator;
		this.quoteBarInterests = new ArrayList<QuoteBarInterest>();
		this.timeoutThreshold = aTimeoutThreshold;
	}

	@Override
	public synchronized void receive(FeedQuote aFeedQuote) {
		String symbol = aFeedQuote.symbol();

		CollatedFeedQuotes collated = this.collatedFeedQuotes.get(symbol);

		if (collated == null) {
			collated = new CollatedFeedQuotes();

			this.collatedFeedQuotes.put(symbol, collated);
		}

		collated.addFeedQuote(aFeedQuote);

		this.aggregateOnThresholdsReached();
	}

	@Override
	public void dispatch(QuoteBar aQuoteBar) {
		synchronized (this.quoteBarInterests) {
			for (QuoteBarInterest interest : this.quoteBarInterests) {
				interest.inform(aQuoteBar);
			}
		}
	}

	@Override
	public void registerQuoteBarInterest(QuoteBarInterest aQuoteBarInterest) {
		synchronized (this.quoteBarInterests) {
			this.quoteBarInterests.add(aQuoteBarInterest);
		}
	}

	private void aggregateOnThresholdsReached() {
		Map<String,CollatedFeedQuotes> collatesToAggregate =
				new HashMap<String,CollatedFeedQuotes>();

		for (String symbol : this.collatedFeedQuotes.keySet()) {
			CollatedFeedQuotes collated = this.collatedFeedQuotes.get(symbol);

			if (collated.isThresholdReached()) {
				collatesToAggregate.put(symbol, collated);
			}
		}

		this.collatedFeedQuotes.keySet().removeAll(collatesToAggregate.keySet());

		this.dispatchAggregatedQuoteBars(collatesToAggregate.values());
	}

	private void dispatchAggregatedQuoteBars(
			final Collection<CollatedFeedQuotes> aCollatesToAggregate) {

		Thread dispatcher = new Thread() {
			@Override
			public void run() {
				for (CollatedFeedQuotes collate : aCollatesToAggregate) {
					QuoteBar quoteBar = quoteBarAggregator.aggregateFrom(collate.feedQuotes);

					dispatch(quoteBar);
				}
			}
		};

		dispatcher.start();
	}

	private class CollatedFeedQuotes {
		private List<FeedQuote> feedQuotes;
		private long startTime;

		CollatedFeedQuotes() {
			super();

			this.feedQuotes = new ArrayList<FeedQuote>();
			this.startTime = new Date().getTime();
		}

		public void addFeedQuote(FeedQuote aFeedQuote) {
			this.feedQuotes.add(aFeedQuote);
		}

		public boolean isThresholdReached() {
			if (this.feedQuotes.size() >= maximumQuoteCount) {
				return true;
			}

			return ((new Date().getTime() - this.startTime) >= timeoutThreshold);
		}
	}
}
