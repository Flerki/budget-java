const fs = require('fs');
const path = require('path');
const {promisify} = require('util');

const config = require('../configuration');
const TEMP_FILE = `${config.PATH_TO_TEMP_DIR}${path.sep}${config.TEMP_INCOME_FILE_NAME}`;

const readIncome = async () => {
    console.log('Start copying of income file');
    await promisify(fs.copyFile)(config.PATH_TO_SOURCE_FILE, TEMP_FILE);
    console.log('End copying of income file');

    const XLSX = require('xlsx');
    const workbook = XLSX.readFile(TEMP_FILE);
    console.log('Temp file was read');

    function getFormattedDate(expense) {
        const date = XLSX.SSF.parse_date_code(expense['дата']);
        const day = ("0" + date.d).slice(-2);
        const month = ("0" + date.m).slice(-2);
        const year = date.y;
        return `${day}.${month}.${year}`;
    }

    const incomeWorksheet = workbook.Sheets['Доходы'];
    const incomeList = XLSX.utils.sheet_to_json(incomeWorksheet);

    const outIncome = [];
    incomeList.forEach(e => {
        const outI = {
            date: getFormattedDate(e),
            sum: e['сумма'],
            category: e['категория'],
            comment: e['комментарий']
        };
        outIncome.push(outI);
    });

    console.log('Removing temp file');
    await promisify(fs.unlink)(TEMP_FILE);
    console.log('Temp file was removed');

    return outIncome;
};
module.exports = {
    readIncome
};
