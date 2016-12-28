1.To solve problem1, you need to first add javacl.jar to your build path and then run the main method in Simulator.java in the simulation package.

Once you run the main method, you should see the prices of the option being printed out during each simulation. Once the result converge, you should see iteration times, the discounted option price and elapsed time in the console in this way:

Converge after 67291 times of simulation.
The first option's price should be 6.159707767583035
Elapsed time in seconds : 1.3548853520000002

2.To run the firts junit test, you need to run the test method in Test_simulation under simulation package in junit folder

for test_StatsCollector, you should see no exception. This means the Stats Collector
can produce the right values.

To run the second junit test, you need to run the test method in Test_GPU under randomVectorGenerator package in junit folder

for test_GPU, you should see no exception. This means the GPU
can produce the right values.