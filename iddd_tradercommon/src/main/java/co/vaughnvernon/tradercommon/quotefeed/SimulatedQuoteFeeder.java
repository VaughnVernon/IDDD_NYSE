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

import java.util.ArrayList;
import java.util.List;

import co.vaughnvernon.tradercommon.monetary.Money;

public class SimulatedQuoteFeeder
		extends Thread
		implements QuoteFeeder {

	private static final String[] simulationInitializers = {
		// company : ticker : open price : max increment/decrement in cents
		"Apple, Inc.:APPL:458.47:25",
		"EMC Corporation:EMC:24.68:10",
		"Google, Inc.:GOOG:767.76:25",
		"Microsoft Corporation:MSFT:27.30:15",
		"Oracle Corporation:ORCL:35.08:5",
		"VMware, Inc.:VMW:78.35:20"
	};

	private List<SimulatedEquityActivity> activities;
	private FeedQuotePublisher feedQuotePublisher;

	public SimulatedQuoteFeeder(FeedQuotePublisher aQuoteFeedPublisher) {
		super();

		this.feedQuotePublisher = aQuoteFeedPublisher;

		this.initialize();
	}

	@Override
	public void generateFeedQuotes() {
		this.start();
	}

	@Override
	public void run() {
		while (true) {
			for (SimulatedEquityActivity activity : this.activities) {
				activity.simulateChange();

				FeedQuote feedQuote = activity.feedQuote();

				this.feedQuotePublisher().publish(feedQuote);
			}

			this.sleep();
		}
	}

	private FeedQuotePublisher feedQuotePublisher() {
		return this.feedQuotePublisher;
	}

	private void initialize() {
		this.activities = new ArrayList<SimulatedEquityActivity>();

		SimulatedEquityActivity.Direction direction =
				SimulatedEquityActivity.Direction.Up;

		for (int idx = 0; idx < simulationInitializers.length; ++idx) {
			String[] tickerBasePriceAndChangeThreshold =
					simulationInitializers[idx].split(":");

			SimulatedEquityActivity activity =
					new SimulatedEquityActivity(
							tickerBasePriceAndChangeThreshold[0],
							tickerBasePriceAndChangeThreshold[1],
							new Money(tickerBasePriceAndChangeThreshold[2]),
							Integer.parseInt(tickerBasePriceAndChangeThreshold[3]),
							direction);

			this.activities.add(activity);

			direction = direction.opposite();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(10L);
		} catch (Exception e) {
			// ignore
		}
	}
}
