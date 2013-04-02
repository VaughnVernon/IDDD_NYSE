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

package co.vaughnvernon.tradercommon.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;

import co.vaughnvernon.tradercommon.quote.StreamingQuote;
import co.vaughnvernon.tradercommon.quote.StreamingQuoteRepository;

public class InMemoryStreamingQuoteRepository
		implements StreamingQuoteRepository {

	private static StreamingQuoteRepository instance;

	private Map<String,StreamingQuote> streamingQuotes;

	public static synchronized StreamingQuoteRepository instance() {
		if (instance == null) {
			instance = new InMemoryStreamingQuoteRepository();
		}

		return instance;
	}

	@Override
	public StreamingQuote streamingQuoteOfSymbol(
			String aSymbol) {

		for (StreamingQuote streamingQuote : this.streamingQuotes().values()) {
			if (streamingQuote.symbol().equals(aSymbol)) {
				return streamingQuote;
			}
		}

		return null;
	}

	@Override
	public void remove(StreamingQuote aStreamingQuote) {
		this.streamingQuotes().remove(aStreamingQuote.symbol());
	}

	@Override
	public void save(StreamingQuote aStreamingQuote) {
		this.streamingQuotes().put(aStreamingQuote.symbol(), aStreamingQuote);
	}

	private Map<String,StreamingQuote> streamingQuotes() {
		return this.streamingQuotes;
	}

	private void setStreamingQuotes(Map<String,StreamingQuote> aMap) {
		this.streamingQuotes = aMap;
	}

	private InMemoryStreamingQuoteRepository() {
		super();

		this.setStreamingQuotes(new HashMap<String,StreamingQuote>());
	}
}
