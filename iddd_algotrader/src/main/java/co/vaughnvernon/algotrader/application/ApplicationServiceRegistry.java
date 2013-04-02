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

package co.vaughnvernon.algotrader.application;

import co.vaughnvernon.algotrader.application.order.AlgoOrderApplicationService;
import co.vaughnvernon.algotrader.application.vwap.VWAPApplicationService;
import co.vaughnvernon.algotrader.domain.model.vwap.VWAPTradingService;
import co.vaughnvernon.algotrader.infrastructure.persistence.InMemoryAlgoOrderRepository;
import co.vaughnvernon.algotrader.infrastructure.persistence.InMemoryVWAPAnalyticRepository;
import co.vaughnvernon.tradercommon.infrastructure.persistence.InMemoryStreamingQuoteRepository;
import co.vaughnvernon.tradercommon.quote.StreamingQuoteApplicationService;

public class ApplicationServiceRegistry {

	private static AlgoOrderApplicationService algoOrderApplicationService;
	private static StreamingQuoteApplicationService streamingQuoteApplicationService;
	private static VWAPApplicationService vwapApplicationService;

	public static synchronized AlgoOrderApplicationService algoOrderApplicationService() {
		if (algoOrderApplicationService == null) {
			algoOrderApplicationService =
					new AlgoOrderApplicationService(
							InMemoryAlgoOrderRepository.instance());
		}

		return algoOrderApplicationService;
	}

	public static synchronized StreamingQuoteApplicationService streamingQuoteApplicationService() {
		if (streamingQuoteApplicationService == null) {
			streamingQuoteApplicationService =
					new StreamingQuoteApplicationService(
							InMemoryStreamingQuoteRepository.instance());
		}

		return streamingQuoteApplicationService;
	}

	public static synchronized VWAPApplicationService vwapApplicationService() {
		if (vwapApplicationService == null) {
			vwapApplicationService =
					new VWAPApplicationService(
							new VWAPTradingService(
									InMemoryVWAPAnalyticRepository.instance(),
									InMemoryAlgoOrderRepository.instance()));
		}

		return vwapApplicationService;
	}
}
