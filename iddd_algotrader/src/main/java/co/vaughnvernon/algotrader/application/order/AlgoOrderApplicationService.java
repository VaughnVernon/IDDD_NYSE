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

package co.vaughnvernon.algotrader.application.order;

import co.vaughnvernon.algotrader.domain.model.order.AlgoOrder;
import co.vaughnvernon.algotrader.domain.model.order.AlgoOrderRepository;
import co.vaughnvernon.algotrader.domain.model.order.OrderType;
import co.vaughnvernon.tradercommon.monetary.Money;
import co.vaughnvernon.tradercommon.quote.Quote;
import co.vaughnvernon.tradercommon.quote.TickerSymbol;

public class AlgoOrderApplicationService {

	private AlgoOrderRepository algoOrderRepository;

	public AlgoOrderApplicationService(AlgoOrderRepository anAlgoOrderRepository) {
		super();

		this.algoOrderRepository = anAlgoOrderRepository;
	}

	public void createAlgoBuyOrder(
			String anOrderId,
			String aSymbol,
			Money aPrice,
			int aQuantity) {

		AlgoOrder algoOrder =
				new AlgoOrder(
						anOrderId,
						OrderType.Buy,
						new Quote(
								new TickerSymbol(aSymbol),
								aPrice,
								aQuantity));

		this.algoOrderRepository().save(algoOrder);
	}

	private AlgoOrderRepository algoOrderRepository() {
		return this.algoOrderRepository;
	}
}
