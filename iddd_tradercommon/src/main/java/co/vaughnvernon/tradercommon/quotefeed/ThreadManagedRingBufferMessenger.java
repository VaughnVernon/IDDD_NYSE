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

package co.vaughnvernon.tradercommon.quotefeed;

import java.util.ArrayList;
import java.util.List;


public class ThreadManagedRingBufferMessenger
		extends Thread
		implements FeedQuotePublisher {

	private static final int CAPACITY = 1024;

	private int inIndex;
	private Object lock;
	private FeedQuote[] messages;
	private int outIndex;
	private List<FeedQuoteSubscriber> subscribers;

	public ThreadManagedRingBufferMessenger() {
		super();

		this.lock = new Object();
		this.messages = new FeedQuote[CAPACITY];
		this.subscribers = new ArrayList<FeedQuoteSubscriber>();

		this.start();
	}

	@Override
	public void publish(FeedQuote aFeedQuote) {

		boolean mustYield = false;
		boolean published = false;

		while (!published) {

			synchronized (this.lock) {
				mustYield = this.overlapping();

				if (!mustYield) {
					published = true;

					this.messages[this.inIndex] = aFeedQuote;

					this.incrementInIndex();
				}
			}

			if (mustYield) {
				this.interrupt();

				this.yieldFor(1L);
			}
		}

		this.interrupt();
	}

	@Override
	public synchronized void subscribe(FeedQuoteSubscriber aSubscriber) {
		this.subscribers.add(aSubscriber);
	}

	@Override
	public void run() {
		boolean mustYield = true;

		while (true) {
			synchronized (this.lock) {
				if (this.hasMessage()) {
					FeedQuote feedQuote = this.messages[this.outIndex];

					this.messages[this.outIndex] = null;

					for (FeedQuoteSubscriber subscriber : this.subscribers) {
						try {
							subscriber.receive(feedQuote);
						} catch (Exception e) {
							System.out.println(
									"Error: SUBSCRIBER: "
									+ subscriber
									+ " because: "
									+ e.getMessage());
						}
					}

					this.incrementOutIndex();

				} else {
					mustYield = true;
				}
			}

			if (mustYield) {
				this.yieldFor(100L);
			}
		}
	}

	private boolean hasMessage() {
		return this.messages[this.outIndex] != null;
	}

	private void incrementInIndex() {
		if (++this.inIndex >= this.messages.length) {
			this.inIndex = 0;
		}
	}

	private void incrementOutIndex() {
		if (++this.outIndex >= this.messages.length) {
			this.outIndex = 0;
		}
	}

	private boolean overlapping() {
		try {
			return this.messages[this.inIndex] != null;
		} catch (Exception e) {
			return true;
		}
	}

	private void yieldFor(long aMillis) {
		try {
			Thread.sleep(aMillis);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}
