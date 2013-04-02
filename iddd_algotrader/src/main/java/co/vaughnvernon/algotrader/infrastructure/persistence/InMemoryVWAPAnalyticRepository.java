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

package co.vaughnvernon.algotrader.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;

import co.vaughnvernon.algotrader.domain.model.vwap.VWAPAnalytic;
import co.vaughnvernon.algotrader.domain.model.vwap.VWAPAnalyticRepository;

public class InMemoryVWAPAnalyticRepository implements VWAPAnalyticRepository {

	private static VWAPAnalyticRepository instance;

	private Map<String,VWAPAnalytic> vwapAnalytics;

	public static void clear() {
		((InMemoryVWAPAnalyticRepository) instance).vwapAnalytics.clear();
	}

	public static synchronized VWAPAnalyticRepository instance() {
		if (instance == null) {
			instance = new InMemoryVWAPAnalyticRepository();
		}

		return instance;
	}

	@Override
	public VWAPAnalytic vwapAnalyticOfSymbol(String aSymbol) {
		return this.vwapAnalytics().get(aSymbol);
	}

	@Override
	public void save(VWAPAnalytic aVWAPAnalytic) {
		this.vwapAnalytics().put(aVWAPAnalytic.symbol(), aVWAPAnalytic);
	}

	private InMemoryVWAPAnalyticRepository() {
		super();

		this.vwapAnalytics = new HashMap<String,VWAPAnalytic>();
	}

	private Map<String,VWAPAnalytic> vwapAnalytics() {
		return this.vwapAnalytics;
	}
}
