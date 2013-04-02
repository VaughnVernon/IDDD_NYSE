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

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.infrastructure.persistence.MockFeedQuotePublisher;

public class SimulatedQuoteFeederTest extends TestCase {

	private int feedQuoteCount;
	private FeedQuotePublisher feedQuotePublisher;
	private QuoteFeeder quoteFeeder;

	public SimulatedQuoteFeederTest() {
		super();
	}

	public void testGenerateFeedQuotes() throws Exception {
		this.feedQuotePublisher.subscribe(new FeedQuoteSubscriber() {
			@Override
			public void receive(FeedQuote aFeedQuote) {
				++feedQuoteCount;
			}
		});

		this.quoteFeeder.generateFeedQuotes();

		Thread.sleep(2000L);

		assertTrue(this.feedQuoteCount > 100);

		System.out.println("QUOTES: " + this.feedQuoteCount);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.feedQuotePublisher = new MockFeedQuotePublisher();
		this.quoteFeeder = new SimulatedQuoteFeeder(this.feedQuotePublisher);
	}
}
