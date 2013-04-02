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

import co.vaughnvernon.tradercommon.quotefeed.QuoteFeederRunner;
import junit.framework.TestCase;

public class QuoteBarDispatcherTest extends TestCase {

	private int quoteBarCount;

	public void testQuoteBarDispatcher() throws Exception {
		QuoteBarInterest interest = new QuoteBarInterest() {
			@Override
			public void inform(QuoteBar aQuoteBar) {
				System.out.println("" + aQuoteBar);

				++quoteBarCount;
			}
		};

		QuoteBarDispatcherFactory
			.instance()
			.dispatcher()
			.registerQuoteBarInterest(interest);

		QuoteFeederRunner.instance().run();

		Thread.sleep(4100L);

		assertTrue(quoteBarCount > 0);
	}
}
