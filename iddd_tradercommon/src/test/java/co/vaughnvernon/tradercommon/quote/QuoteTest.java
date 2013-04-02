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

import junit.framework.TestCase;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class QuoteTest extends TestCase {

	public QuoteTest() {
		super();
	}

	public void testQuoteCopy() throws Exception {
		Quote quote = new Quote(new TickerSymbol("GOOG"), new Money("723.41"));

		Quote quoteCopy = new Quote(quote);

		assertEquals(quote, quoteCopy);

		assertNotSame(quote, quoteCopy);
	}

	public void testQuoteTickerSymbol() throws Exception {
		Quote quote = new Quote(new TickerSymbol("GOOG"), new Money("723.41"));

		assertEquals(quote.tickerSymbol(), new TickerSymbol("GOOG"));
	}

	public void testQuotePrice() throws Exception {
		Quote quote = new Quote(new TickerSymbol("GOOG"), new Money("723.41"));

		assertEquals(quote.price(), new Money("723.41"));
	}

	public void testValueOfPricedShares() throws Exception {
		Quote quote = new Quote(new TickerSymbol("GOOG"), new Money("723.41"));

		assertEquals(new Money("2893.64"), quote.valueOfPricedShares(4));

		assertEquals(quote.price().multipliedBy(4), quote.valueOfPricedShares(4));
	}
}
