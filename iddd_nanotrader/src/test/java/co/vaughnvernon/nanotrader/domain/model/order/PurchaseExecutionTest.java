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

package co.vaughnvernon.nanotrader.domain.model.order;

import java.util.Calendar;

import co.vaughnvernon.tradercommon.event.DomainEventPublisher;

import junit.framework.TestCase;

public class PurchaseExecutionTest extends TestCase {

	public PurchaseExecutionTest() {
		super();
	}

	public void testOpenExecution() throws Exception {

		PurchaseExecution purchaseExecution = new PurchaseExecution(20);

		assertEquals(20, purchaseExecution.quantityOfSharesOrdered());
		assertEquals(20, purchaseExecution.quantityOfSharesOutstanding());
		assertNull(purchaseExecution.filledDate());
		assertNotNull(purchaseExecution.openDate());

		Calendar openDate = Calendar.getInstance();
		openDate.setTime(purchaseExecution.openDate());

		assertEquals(Calendar.getInstance().get(Calendar.MONTH), openDate.get(Calendar.MONTH));
		assertEquals(Calendar.getInstance().get(Calendar.DATE), openDate.get(Calendar.DATE));
		assertEquals(Calendar.getInstance().get(Calendar.YEAR), openDate.get(Calendar.YEAR));
	}

	public void testFillExecution() throws Exception {

		PurchaseExecution purchaseExecution = new PurchaseExecution(20);

		for (int count = 0; count < 4; ++count) {
			assertTrue(purchaseExecution.isOpen());
			assertFalse(purchaseExecution.isFilled());

			purchaseExecution = purchaseExecution.withPurchasedSharesOf(5);
		}

		assertFalse(purchaseExecution.isOpen());
		assertTrue(purchaseExecution.isFilled());

		assertNotNull(purchaseExecution.filledDate());

		Calendar filledDate = Calendar.getInstance();
		filledDate.setTime(purchaseExecution.filledDate());

		assertEquals(Calendar.getInstance().get(Calendar.MONTH), filledDate.get(Calendar.MONTH));
		assertEquals(Calendar.getInstance().get(Calendar.DATE), filledDate.get(Calendar.DATE));
		assertEquals(Calendar.getInstance().get(Calendar.YEAR), filledDate.get(Calendar.YEAR));
	}

	public void testNoNegativeQuantities() throws Exception {

		try {
			new PurchaseExecution(-20);

			fail("Should not allow negative order quantity.");

		} catch (Exception e) {
			// success
		}

		try {
			PurchaseExecution purchaseExecution = new PurchaseExecution(10);

			purchaseExecution.withPurchasedSharesOf(11);

			fail("Should not allow overrun purchase execution.");

		} catch (Exception e) {
			// success
		}
	}

	@Override
	protected void setUp() throws Exception {
		DomainEventPublisher.instance().reset();
	}
}
