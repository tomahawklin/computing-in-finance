1.To solve problem1 and problem2, you need to run the main method in Simulator.java in the simulation package.

Once you run the main method, you should see the iteration times, the discounted option price and elapsed time in the console in this way:

Converge after 68746 times of simulation.
The first option's price should be 6.273957011064902
Converge after 13761 times of simulation.
The second option's price should be 2.258336461253971
Elapsed time in seconds : 3.0834036680000003

2.To run the junit test, you need to run the test method in Test_simulation
in junit folder

for test_AntiThetic(), you should see no exception. This means the Anti-Thetic decorator generates the random vector we expect

for test_StatsCollector, you should see no exception. This means the Stats Collector
can produce the right values.