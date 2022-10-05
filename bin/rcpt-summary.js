const { BADNAME } = require("dns");
const fs = require("fs");
const { exit } = require("process");
const { parseString} = require("xml2js");

const seikyuuMap = { };

function mkKey(map){
  const hokenshaBangou = map.hokenshaBangou;
  if( isKokuho(hokenshaBangou) ){
    const sub1 = isKokuhoProper(hokenshaBangou) ? "prop" : "kouki";
    const loc = isKennai(hokenshaBangou) ? "kennai" : "kengai"
    if( isKokuhoProper(hokenshaBangou ) ){
      const taishoku = isTaishoku(hokenshaBangou) ? "taishoku": "kokuho";
      return `K:${sub1}:${loc}:${taishoku}`;
    } else {
      return `K:${sub1}:${loc}`;
    }
  } else {
    return `S`;
  }
}

function dispatch(map){
  const key = mkKey(map);
  if( !(key in seikyuuMap) ){
    seikyuuMap[key] = [];
  }
  seikyuuMap[key].push(map);
}

const dataFile = process.argv[2];
fs.readFile(dataFile, "UTF-8", (err, text) => {
  if( err ){
    console.error(err);
    process.exit(1);
  } else {
    parseString(text, (err, xml) => {
      const head = xml["レセプト"];
      console.log("月：", head["元号"][0], head["年"][0], "年", head["月"][0], "月");
      console.log("件数：", xml["レセプト"]["請求"].length);
      const shaho = [];
      const seikyuuList = xml["レセプト"]["請求"];
      seikyuuList.forEach(seikyuu => {
        const map = mapSeikyuu(seikyuu);
        dispatch(map);
      });
      report();
    });
  }
});

function pad(n, len) {
  let s = n.toString()
  for(let i=s.length;i<len;i++){
    s = " " + s;
  }
  return s;
}

function report() {
  console.dir(seikyuuMap);
  console.log("国保 ===================================");
  console.log("　　国保分");
  console.log("　　　　都内分")
  console.log(`　　　　　　　国保 -- ${kokuhoEntrySummary("K:prop:kennai:kokuho")}`);
  console.log(`　　　　　　退職者 -- ${kokuhoEntrySummary("K:prop:kennai:taishoku")}`);
  console.log(`　　　　　　　　計 -- ${kokuhoTotal("K:prop:kennai")}`);
  console.log("　　　　都外分")
  console.log(`　　　　　　　国保 -- ${kokuhoEntrySummary("K:prop:kengai:kokuho")}`);
  console.log(`　　　　　　退職者 -- ${kokuhoEntrySummary("K:prop:kengai:taishoku")}`);
  console.log(`　　　　　　　　計 -- ${kokuhoTotal("K:prop:kengai")}`);
  console.log("　　後期高齢者分");
  console.log(`　　　　都内分     -- ${kokuhoEntrySummary("K:kouki:kennai")}`)
  console.log(`　　　　都外分     -- ${kokuhoEntrySummary("K:kouki:kengai")}`)
}

function kokuhoEntrySummary(key){
  return kokuhoReport(seikyuuMap[key] || [])
}

function kokuhoReport(list) {
  const rep = {
    kensuu: list.length,
    tensuu: list.map(s => s.tensuu).reduce((a, b) => a + b, 0),
    kouhi: list.filter(s => s.hasKouhi).length
  }
  return `件数：${pad(rep.kensuu, 4)}, 点数：${pad(rep.tensuu, 6)}, 公費併用：${pad(rep.kouhi, 2)}`;
}

function kokuhoTotal(prefix) {
  const list = ["kokuho", "taishoku"].map(k => `${prefix}:${k}`).flatMap(k => seikyuuMap[k] || []);
  return kokuhoReport(list);
}

function kokuhoSumReport(a, b){
  a = Object.assign({}, a);
  a.kensuu += b.kensuu;
  a.tensuu += b.tensuu;
  a.kouhi += b.kouhi;
}

function mapSeikyuu(seikyuu) {
  const visits = seikyuu["受診"][0]["診療"];
  const tensuu = visits.map(v => parseInt(v["点数"][0])).reduce((a, b) => a + b, 0);
  return {
    hokenshaBangou: parseInt(seikyuu["保険者番号"][0]),
    tensuu,
    hasKouhi: hasKouhi(seikyuu),
  };
}

function isKennai(hokenshaBangou){
  const n = hokenshaBangou;
  const fuken = Math.floor((n % 1000000) / 10000);
  return fuken == 13; // 東京
}

function hasKouhi(map){
  return !!(map["公費1負担者番号"] || map["公費2負担者番号"]);
}

function isKokuho(hokenshaBangou) {
  return isKokuhoProper(hokenshaBangou) || isKoukikourei(hokenshaBangou);
}

function isKokuhoProper(hokenshaBangou) {
  const n = hokenshaBangou;
  if (n >= 10000 && n <= 999999) return true;
  if (n >= 67000000 && n <= 67999999) return true;
  return false;
}

function isKoukikourei(hokenshaBangou) {
  const n = hokenshaBangou;
  return n >= 39000000 && n <= 39999999;
}

function isTaishoku(hokenshaBangou) {
  const n = hokenshaBangou;
  return n > 67000000  
}

function createEntry() {
  return {
    kensuu: 0,
    tensuu: 0,
    kouhi: 0
  };
}

function addToEntry(entry, map) {
  entry.kensuu += 1;
  entry.tensuu += map.tensuu;
  if( map.hasKouhi ){
    entry.kouhi += 1;
  }
}