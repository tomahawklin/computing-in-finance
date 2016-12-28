1.To solve problem1 and problem2, you need to run the main method in Optimizer.java in the histogram package.

Once you run the main method, you should see the calculated U value and the optimal lookback parameter for each year in the console in this way:

u: 9.686028020707944 at year 2000
Min MSE 0.00220126361196857 is obatained with lookback = 155
u: 5.315467354572783 at year 2001
Min MSE 3.7152113214265303E-4 is obatained with lookback = 65
u: 3.2275230337730783 at year 2002
Min MSE 1.7245642810561864E-4 is obatained with lookback = 155
u: 3.164358299454814 at year 2003
Min MSE 1.3919268683847352E-4 is obatained with lookback = 215
u: 2.9970467479348404 at year 2004
Min MSE 1.0519054908113967E-4 is obatained with lookback = 335
u: 3.002528000668662 at year 2005
Min MSE 2.669243831113824E-4 is obatained with lookback = 215
u: 2.993050303212841 at year 2006
Min MSE 1.250680132102476E-4 is obatained with lookback = 245
u: 2.188553167617015 at year 2007
Min MSE 2.241792823541503E-4 is obatained with lookback = 95
u: 1.5048735317115904 at year 2008
Min MSE 6.799270916740428E-4 is obatained with lookback = 245

You will also find the average optimal lookback parameter at last:

The average optimal lookback is 191.66666666666666

2.To run the first junit test, you need to run the test method in Test_QuotesReader under dataReader package in junit folder

For test_QuoteReader, you should see no exception. This means the up ratios we calculated is right.

To run the second junit test, you need to run the test method in Test_Optimizer under histogram package in junit folder

for test_binomial, you should see no exception. This means the choose function
can produce the right values.