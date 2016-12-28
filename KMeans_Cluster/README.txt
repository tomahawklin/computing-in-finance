1.Run KMeans.java as a java application. Then you will find the cluster results in result_KMeans.txt (using traditional KMeans Algorithm) and result_KMeans2.txt (using twist KMeans Algorithm). You can also observe the status of points movement in the console. 

In the .txt file, you are expected to see what points does each cluster contain after each iteration, followed with a fitness value indicating how good is the model's fitness. (The smaller, the better)

In the console, you are expected to see for each iteration, how many points move to another cluster. When no point moves to another cluster, the iteration will stop.

2.Run Test_*.java as JUnit Test.

For Test_Point.java, you should observe no errors and failures.

For Test_Cluster.java, you should observe 1 error for testBadLabel function, as the input parameter causes an exception.

For Test_KMeans.java, you should observe no errors and failures and get a file named 'result_KMeans.txt', where you can observe two iteration on a small sample and compare the status before and after the iteration.


For Test_KMeans2.java, you should observe no errors and failures and get a file named 'result_KMeans2.txt', where you can observe two iteration on a small sample and compare the status before and after the iteration.