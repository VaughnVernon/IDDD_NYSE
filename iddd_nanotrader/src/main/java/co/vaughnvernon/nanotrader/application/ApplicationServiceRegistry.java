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

package co.vaughnvernon.nanotrader.application;

import co.vaughnvernon.nanotrader.application.account.ProfileApplicationService;
import co.vaughnvernon.nanotrader.application.order.BuyOrderApplicationService;
import co.vaughnvernon.nanotrader.domain.model.order.MarketFillService;
import co.vaughnvernon.nanotrader.infrastructure.persistence.InMemoryBuyOrderRepository;
import co.vaughnvernon.nanotrader.infrastructure.persistence.InMemoryProfileRepository;
import co.vaughnvernon.tradercommon.infrastructure.persistence.InMemoryStreamingQuoteRepository;
import co.vaughnvernon.tradercommon.quote.StreamingQuoteApplicationService;

public class ApplicationServiceRegistry {

	private static BuyOrderApplicationService buyOrderApplicationService;
	private static ProfileApplicationService profileApplicationService;
	private static StreamingQuoteApplicationService streamingQuoteApplicationService;

	public static synchronized BuyOrderApplicationService buyOrderApplicationService() {
		if (buyOrderApplicationService == null) {
			buyOrderApplicationService =
					new BuyOrderApplicationService(
							InMemoryBuyOrderRepository.instance(),
							new MarketFillService(InMemoryBuyOrderRepository.instance()));
		}

		return buyOrderApplicationService;
	}

	public static synchronized ProfileApplicationService profileApplicationService() {
		if (profileApplicationService == null) {
			profileApplicationService =
					new ProfileApplicationService(
							InMemoryProfileRepository.instance());
		}

		return profileApplicationService;
	}

	public static synchronized StreamingQuoteApplicationService streamingQuoteApplicationService() {
		if (streamingQuoteApplicationService == null) {
			streamingQuoteApplicationService =
					new StreamingQuoteApplicationService(
							InMemoryStreamingQuoteRepository.instance());
		}

		return streamingQuoteApplicationService;
	}
}
