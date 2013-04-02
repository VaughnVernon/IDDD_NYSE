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

package co.vaughnvernon.nanotrader.domain.model.account;

import java.util.Date;

import junit.framework.TestCase;
import co.vaughnvernon.nanotrader.domain.model.order.OrderId;
import co.vaughnvernon.tradercommon.event.DomainEventPublisher;
import co.vaughnvernon.tradercommon.monetary.Money;

public class PaymentTest extends TestCase {

	public PaymentTest() {
		super();
	}

	public void testPaymentTotalAmount() throws Exception {

		final Money cost = new Money("1000.00");
		final Money fee = new Money("9.99");

		Payment payment =
				new Payment(
						AccountId.unique(),
						OrderId.unique(),
						"This is a test",
						cost,
						fee,
						new Date());

		assertEquals(cost, payment.cost());
		assertEquals(fee, payment.fee());
		assertEquals(cost.addedTo(fee), payment.totalAmount());
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}
}
