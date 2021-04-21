const express = require(`express`);
const DEFAULT_PORT = 3000;

const expenseService = require('./service/expense.service');
const incomeService = require('./service/income.service');

const app = express();

app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    next();
});

app.get(`/expenses`, async (req, res) => {
    const data = await expenseService.readExpenses();
    res.send(JSON.stringify(data));
});

app.get(`/income`, async (req, res) => {
    const data = await incomeService.readIncome();
    res.send(JSON.stringify(data));
});

app.listen(DEFAULT_PORT,
    () => console.log(`Server was started at ${DEFAULT_PORT}`));
