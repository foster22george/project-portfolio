import yfinance as yf
import backtrader as bt 

# make sure you have the virtual environment set up when you want to run the code
# to create it in terminal: python3 -m venv .venv
# activating it in terminal: source .venv/bin/activate
# make sure you also have installed the proper packages: pip install yfinance backtrader matplotlib pandas


class MovingAverageStrategy(bt.Strategy) :
    def __init__(self) :
        self.ma_shorts = {}
        self.ma_longs = {}
        for d in self.datas : 
            self.ma_shorts[d] = bt.indicators.SimpleMovingAverage(d.close, period = 5)
            self.ma_longs[d] = bt.indicators.SimpleMovingAverage(d.close, period = 50)

    def next(self) :
        gap = 0.001 
        #larger gap means less whipsaws and less trading, also increases drawdown and reduces sharpe in my experience
        for d in self.datas:
            pos = self.getposition(d).size
            ma_short = self.ma_shorts[d][0]
            ma_long = self.ma_longs[d][0]

            if not pos:
                if ma_short > ma_long and abs(ma_short - ma_long) > ma_long * gap:
                    cash = self.broker.getcash()
                    price = d.close[0]
                    percentage = 0.5
                    shrAmt = int((cash * percentage) / price)
                    if shrAmt > 0:
                        self.buy(data = d, size = shrAmt)
            else:
                if ma_short < ma_long and abs(ma_short - ma_long) > ma_long * gap:
                    self.close(data=d)

if __name__ == "__main__" : 
    tickers = ["AAPL", "NFLX"]#, "GOOGL", "DIS", "TSLA"]

    cerebro = bt.Cerebro() # the brain of running the backtest on Backtrader

    cerebro.addanalyzer(bt.analyzers.SharpeRatio, _name = "sharpe")
    cerebro.addanalyzer(bt.analyzers.DrawDown, _name = "drawdown")
    #cerebro.addanalyzer(bt.analyzers.TimeReturn, _name = "timereturn")

    cerebro.addstrategy(MovingAverageStrategy)

    for t in tickers : 
        data = yf.Ticker(t).history(start = "2015-01-01", end = "2020-01-01", auto_adjust = True)
        bt_data = bt.feeds.PandasData(dataname = data, name = t)
        cerebro.adddata(bt_data)



    cerebro.broker.set_cash(10000)
    print("Portfolio Value at Start: " + str(round(cerebro.broker.getvalue(),2)))

    results = cerebro.run()
    strategy = results[0]
    print("Portfolio Value at End: " + str(round(cerebro.broker.getvalue(),2)))
    print("Sharpe Ratio: " + str(round(strategy.analyzers.sharpe.get_analysis().get('sharperatio', 'N/A'),2))) # avg portfolio return - risk-free rate (bond) / stand. dev. of portfolio returns
    print("Max Drawdown: " + str(round(strategy.analyzers.drawdown.get_analysis()['max']['drawdown'], 2)) + "%") #highest peak value to valley / peak (as a percentage)

    cerebro.plot()

