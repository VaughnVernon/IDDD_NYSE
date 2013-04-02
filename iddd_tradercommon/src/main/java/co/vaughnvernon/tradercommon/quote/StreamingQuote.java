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

public class StreamingQuote {

	private Quote quote;
	private String symbol;

	public StreamingQuote(Quote aQuote) {
		super();

		this.setQuote(aQuote);
		this.setSymbol(aQuote.tickerSymbol().symbol());
	}

	public Quote quote() {
		return this.quote;
	}

	public String symbol() {
		return this.symbol;
	}

	public void updateWith(Money aPrice) {
		this.setQuote(this.quote().withUpdatedPrice(aPrice));
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			StreamingQuote typedObject = (StreamingQuote) anObject;
			equalObjects =
					this.symbol().equals(typedObject.symbol());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
				+ (32733 * 137)
				+ this.symbol().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "StreamingQuote [symbol=" + this.symbol + ", quote="
				+ this.quote + "]";
	}

	private void setQuote(Quote aQuote) {
		if (aQuote == null) {
			throw new IllegalArgumentException("Quote is required.");
		}

		this.quote = aQuote;
	}

	private void setSymbol(String aSymbol) {
		if (aSymbol == null) {
			throw new IllegalArgumentException("The symbol id is required.");
		}

		this.symbol = aSymbol;
	}
}
