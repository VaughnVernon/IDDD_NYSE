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

import co.vaughnvernon.tradercommon.quote.TickerSymbol;
import junit.framework.TestCase;

public class TickerSymbolTest extends TestCase {

	public TickerSymbolTest() {
		super();
	}

	public void testTickerSymbolCopy() throws Exception {

		TickerSymbol tickerSymbol = new TickerSymbol("GOOG");

		TickerSymbol tickerSymbolCopy = new TickerSymbol(tickerSymbol);

		assertEquals(tickerSymbol, tickerSymbolCopy);

		assertNotSame(tickerSymbol, tickerSymbolCopy);
	}

	public void testTickerSymbolSymbol() throws Exception {

		TickerSymbol tickerSymbol = new TickerSymbol("GOOG");

		assertEquals("GOOG", tickerSymbol.symbol());

		TickerSymbol otherTickerSymbol = new TickerSymbol("MSFT");

		assertFalse(tickerSymbol.equals(otherTickerSymbol));
	}
}
