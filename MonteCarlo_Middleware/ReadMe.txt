1.To solve problem1 and problem2, you need to first add activemq to your build path and then start activemq from terminal with this:

(upper directory)/apache-activemq-5.14.1/bin/macosx/activemq start

next you comment/ uncomment the 110/111 line of code in MonteCarloServerPtP.java. Then run the main method in MonteCarloServerPtP.java and MonteCarloClientPtP.java in the middleware package.

Once you run the main method, you should see the program prints the number of iterations, the price estimated and the standard deviation in the console in this way: 

(for the first option:)
Iterations: 38600; Price: 6.20149110094504; Std: 12.595017574087374
Iterations: 38700; Price: 6.204774860767881; Std: 12.5986169835879
Iterations: 38800; Price: 6.205181394112743; Std: 12.602530843581949

(for the second option:)
Iterations: 4400; Price: 2.155881348579666; Std: 5.365827874394071
Iterations: 4500; Price: 2.1551163701449076; Std: 5.362514763848479
Iterations: 4600; Price: 2.1453241577873343; Std: 5.344528539239667

When the results converge, the program prints the iteration times, the discounted option price and elapsed time in the console in this way:

Converge after 67200 times of simulation.
Iterations: 67200; Price: 6.210367982739496; Std: 12.616523012268889
The option's price should be 6.210367982739496
Elapsed time in seconds : 1843.959340158

Converge after 12900 times of simulation.
Iterations: 12900; Price: 2.174597131903541; Std: 5.5006724364794675
The option's price should be 2.174597131903541
Elapsed time in seconds : 85.22493364900001

2.To run the junit test, you need to run the test methods in Test_middleware
in junit folder

for test_SplitText(), you should see no exception and the information printed in console is correspond to the property of the option

for test_BadOptionType(), you should see Bad Type Error in console with an exception indicating that the session has been stopped.

for test_AntiThetic(), you should see no exception. This means the Anti-Thetic decorator generates the random vector we expect

for test_StatsCollector(), you should see no exception. This means the Stats Collector can produce the right values.