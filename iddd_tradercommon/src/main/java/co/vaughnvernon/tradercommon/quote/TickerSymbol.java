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

import java.io.Serializable;

public final class TickerSymbol implements Serializable {

	private static final long serialVersionUID = 1L;

	private String symbol;

	public TickerSymbol(String aSymbol) {
		super();

		this.setSymbol(aSymbol);
	}

	public TickerSymbol(TickerSymbol aTickerSymbol) {
		this(aTickerSymbol.symbol());
	}

	public String symbol() {
		return this.symbol;
	}

	@Override
	public boolean equals(Object anObject) {
		boolean equalObjects = false;
		if (anObject != null && this.getClass() == anObject.getClass()) {
			TickerSymbol typedObject = (TickerSymbol) anObject;
			equalObjects =
					this.symbol().equals(typedObject.symbol());
		}
		return equalObjects;
	}

	@Override
	public int hashCode() {
		int hashCodeValue =
			+ (129067 * 179)
			+ this.symbol().hashCode();

		return hashCodeValue;
	}

	@Override
	public String toString() {
		return "TickerSymbol [symbol=" + this.symbol() + "]";
	}

	private void setSymbol(String aSymbol) {
		if (aSymbol == null || aSymbol.trim().length() == 0) {
			throw new IllegalArgumentException("Symbol must be provide.");
		}
		this.symbol = aSymbol.toUpperCase();
	}
}
