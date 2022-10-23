// Usage: node install-iyakuhin-master.js Y.ZIP

const AdmZip = require("adm-zip");
const iconv = require("iconv-lite");
const { parse } = require("csv-parse/sync");
const mysql = require("mysql");

class Row {
  constructor(csvRow) {
    this.csvRow = csvRow;
  }

  getInt(i) {
    return parseInt(this.csvRow[i-1]);
  }

  getString(i) {
    return this.csvRow[i-1];
  }
}

const HenkouKubunNoChange = 0;
const HenkouKubunMasshou = 1;
const HenkouKubunShinki = 3;
const HenkouKubunHenkou = 5;
const HenkouKubunHaishi = 9;

const zipFile = process.argv[2];
const zip = new AdmZip(zipFile);
let zipEntry;
zip.getEntries().forEach(entry => {
  if( entry.entryName === "y.csv" ){
    zipEntry = entry;
  }
});
if( zipEntry === undefined ){
  console.error("Cannot find y.csv in zip");
}
const csvContent = iconv.decode(zipEntry.getData(), "SHIFT_JIS");
const csvRows = parse(csvContent);
let entries = csvRows.map(csvRow => rowToEntry(new Row(csvRow)));
entries = entries.filter(r => 
  r.kubun !== HenkouKubunHaishi && r.kubun !== HenkouKubunMasshou);
entries = entries.filter(r => r.yakka.length <= 10);
const validFrom = dateToSqldate(new Date());
const validUpto = "0000-00-00"
masters = entries.map(entry => entryToMaster(entry, validFrom, validUpto));
const conn = mysql.createConnection({
  host: process.env["MYCLINIC_DB_HOST"] || "127.0.0.1",
  user: process.env["MYCLINIC_DB_USER"],
  password: process.env["MYCLINIC_DB_PASS"],
  database: "myclinic",
  charset: "utf8"
});
conn.connect();
conn.beginTransaction();
masters.forEach(master => enter(conn, master));
conn.commit();
conn.end();

function rowToEntry(row) {
  return {
    kubun: row.getInt(1),
    masterShubetsu: row.getString(2),
    iyakuhincode: row.getInt(3),
    name: row.getString(5),
    yomi: row.getString(7),
    unit: row.getString(10),
    kingakuShubetsu: row.getInt(11),
    yakka: row.getString(12),
    madoku: row.getInt(14),
    kouhatsu: row.getInt(17),
    zaikei: row.getInt(28),
    yakkacode: row.getString(32)
  };
}

function entryToMaster(entry, validFrom, validUpto) {
  return {
    iyakuhincode: entry.iyakuhincode,
    yakkacode: entry.yakkacode,
    name: entry.name,
    yomi: entry.yomi,
    unit: entry.unit,
    yakka: entry.yakka,
    madoku: entry.madoku.toString(),
    kouhatsu: entry.kouhatsu.toString(),
    zaikei: entry.zaikei.toString(),
    valid_from: validFrom,
    valid_upto: validUpto
  };
}

function dateToSqldate(date){
  return date.toISOString().substring(0, 10);
}

function enter(conn, master){
  conn.query("insert into iyakuhin_master_arch set ?", master);
}

