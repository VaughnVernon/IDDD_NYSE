The IDDD_NYSE repository contains a set of projects
that reflect my take on the SpringTrader project
(formerly NanoTrader). It's my attempt to improve
the model using a DDD approach.

This is a work in progress. The various Bounded Contexts
(iddd_nanotrader, iddd_algotrader, iddd_nanoreports) and
the common Subdomains and reusable tools found in
iddd_tradercommon are not complete. In particular when
a market order and a slice order are executed, the
requests just fall off a cliff. Currently the models
go no further than to track the number of shares
conceptually purchased, and that's it. There are other
holes in the models, too, and it will require more time
with my volunteer domain expert before this effort
provides a more realistic simulation of stock purchases.

In all this example provides a fairly decent look at
Aggregate design and the use of Domain Events in a simple
Event-Driven Architecture. Actually none of the Events
are currently crossing Context boundaries. Still, you
can look at the tests to see how the Events would be
handled if the Bounded Contexts were actually deployed.

I created a simple Pub-Sub messaging mechanism that I
named SlothMQ. (Yeah.) The tests that run the Sloth are
a bit slow since they pause to allow time for the creature
to scoot messages across the wire like a sloth drags its
butt across the ground. Anyway, it works, if even at a
slothful rate, and it can be run just about anywhere.

The run the tests:

1. Install Java SDK 1.7.?
2. Install Gradle 1.5 (doing all the GRADLE_HOME and PATH set up)
3. Grab the code using git into a project root named IDDD_NYSE
4. In the IDDD_NYSE root, run: gradle clean build

That's just about it. You should see the gradle test
output in each of the subprojects:

iddd_algotrader/build/reports/tests/index.html
iddd_nanoreports/build/reports/tests/index.html
iddd_nanotrader/build/reports/tests/index.html
iddd_tradercommon/build/reports/tests/index.html


Actually iddd_nanoreports doesn't do anything yet. It's
just a placeholder to remote the reporting type things
from the iddd_nanotrader Bounded Context. This may change
quite a bit by the time it's done.

If you get a chance to help, I welcome your participation!

Vaughn Vernon
Author: Implementing Domain-Driven Design
