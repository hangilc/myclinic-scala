const { BADNAME } = require("dns");
const fs = require("fs");
const { exit } = require("process");
const { parseString} = require("xml2js");

const seikyuuMap = { };
let kokuhoTotalKensuu = 0;
let shahoTotal1 = 0;

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
    let futan;
    switch(map.hokenFutan){
      case "高齢９": 
      case "高齢８": {
        futan = "kourei";
        break;
      }
      case "高齢７": {
        futan = "kourei7";
        break;
      }
      case "本人": {
        futan = "honnin";
        break;
      }
      case "家族": {
        futan = "kazoku";
        break;
      }
      case "三才未満": {
        futan = "child";
        break;
      }
      default: throw new Error(`Cannot handle futan: ${map.hokenFutan}`)
    }
    const tandoku = map.hasKouhi ? "heiyou" : "tandoku";
    return `S:${futan}:${tandoku}`;
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
      console.log("　月：", head["元号"][0], head["年"][0], "年", head["月"][0], "月");
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
  console.log("国保 ===================================");
  console.log("国保分");
  console.log("　　都内分")
  console.log(`　　　　　国保 -- ${kokuhoEntrySummary("K:prop:kennai:kokuho")}`);
  console.log(`　　　　退職者 -- ${kokuhoEntrySummary("K:prop:kennai:taishoku")}`);
  console.log(`　　　　　　計 -- ${kokuhoTotal("K:prop:kennai")}`);
  console.log("　　都外分")
  console.log(`　　　　　国保 -- ${kokuhoEntrySummary("K:prop:kengai:kokuho")}`);
  console.log(`　　　　退職者 -- ${kokuhoEntrySummary("K:prop:kengai:taishoku")}`);
  console.log(`　　　　　　計 -- ${kokuhoTotal("K:prop:kengai")}`);
  console.log("後期高齢者分");
  console.log(`　　都内分     -- ${kokuhoEntrySummary("K:kouki:kennai")}`)
  console.log(`　　都外分     -- ${kokuhoEntrySummary("K:kouki:kengai")}`)

  console.log("");
  console.log("社保 ===================================");
  console.log("医療保険")
  console.log(`　　70以上一般、公費併用 -- ${shahoSummary("S:kourei:heiyou")}`);
  console.log(`　　70以上一般、単独　　 -- ${shahoSummary("S:kourei:tandoku")}`);
  console.log(`　　70以上７割、公費併用 -- ${shahoSummary("S:kourei7:heiyou")}`);
  console.log(`　　70以上７割、単独　　 -- ${shahoSummary("S:kourei7:tandoku")}`);
  console.log(`　　本人、公費併用　　　 -- ${shahoSummary("S:honnin:heiyou")}`);
  console.log(`　　本人、単独　　　　　 -- ${shahoSummary("S:honnin:tandoku")}`);
  console.log(`　　家族、公費併用　　　 -- ${shahoSummary("S:kazoku:heiyou")}`);
  console.log(`　　家族、単独　　　　　 -- ${shahoSummary("S:kazoku:tandoku")}`);
  console.log(`　　六歳、公費併用　　　 -- ${shahoSummary("S:child:heiyou")}`);
  console.log(`　　六歳、単独　　　　　 -- ${shahoSummary("S:child:tandoku")}`);
  console.log(`　　(1) 合計：${shahoTotal1}`);
  console.log("公費負担");
  console.log(`　　(2) 合計：${shahoTotal2()}`)

  if( Object.values(seikyuuMap).map(s => s.length).reduce(add, 0) == kokuhoTotalKensuu + shahoTotal1 ){
    console.log("Total check OK");
  } else {
    console.error("Total check failed");
  }
  
}

function add(a, b){
  return a + b;
}

function kokuhoEntrySummary(key){
  const list = seikyuuMap[key] || []
  kokuhoTotalKensuu += list.length;
  return kokuhoReport(list)
}

function kokuhoReport(list) {
  const rep = {
    kensuu: list.length,
    tensuu: list.map(s => s.tensuu).reduce((a, b) => a + b, 0),
    kouhi: list.filter(s => s.hasKouhi).length
  }
  return `件数：${pad(rep.kensuu, 3)}, 点数：${pad(rep.tensuu, 6)}, 公費併用：${pad(rep.kouhi, 2)}`;
}

function kokuhoTotal(prefix) {
  const list = ["kokuho", "taishoku"].map(k => `${prefix}:${k}`).flatMap(k => seikyuuMap[k] || []);
  return kokuhoReport(list);
}

function shahoSummary(key) {
  const list = seikyuuMap[key] || [];
  shahoTotal1 += list.length;
  return shahoReport(list);
}

function shahoReport(list) {
  if( list.length === 0 ){
    return "";
  }
  const nissuu = list.map(s => s.nissuu).reduce((a, b) => a + b, 0);
  const tensuu = list.map(s => s.tensuu).reduce((a, b) => a + b, 0);
  return `件数：${pad(list.length, 3)}, 実日数：${pad(nissuu, 2)}, 点数：${pad(tensuu, 6)}`;
}

function filterSeikyuu(keyTest) {
  return Object.keys(seikyuuMap).filter(key => keyTest(key))
    .flatMap(key => seikyuuMap[key] || []);
}

function shahoTotal2() {
  const list = filterSeikyuu(key => key.startsWith("S:")).filter(s => s.hasKouhi);
  const tensuu = list.map(s => s.tensuu).reduce((a, b) => a + b, 0);
  return `件数：${pad(list.length, 3)}, 点数：${tensuu}`
}

function mapSeikyuu(seikyuu) {
  const visits = seikyuu["受診"];
  const tensuu = visits.flatMap(v => v["診療"]).map(s => parseInt(s["点数"][0])).reduce((a, b) => a + b, 0);
  return {
    hokenshaBangou: parseInt(seikyuu["保険者番号"][0]),
    tensuu,
    hasKouhi: hasKouhi(seikyuu),
    hokenFutan: seikyuu["保険負担"][0],
    nissuu: seikyuu["受診"].length
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

