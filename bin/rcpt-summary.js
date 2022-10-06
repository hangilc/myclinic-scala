const { BADNAME } = require("dns");
const fs = require("fs");
const { exit } = require("process");
const { parseString} = require("xml2js");

const seikyuuMap = { };
let kokuhoTotalKensuu = 0;

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

function pad(n, len, c = " ") {
  let s = n.toString()
  for(let i=s.length;i<len;i++){
    s = c + s;
  }
  return s;
}

class ShahoContext {
  kensuu = 0;
}

class ShahoSubContext {
  map = {};

  add(seikyuu) {
    const hoken = pad(Math.floor(seikyuu.hokenshaBangou / 1000000), 2, "0");
    if( !(hoken in this.map) ){
      this.map[hoken] = {
        kensuu: 0,
        nissuu: 0,
        tensuu: 0
      }
    }
    const bind = this.map[hoken];
    bind.kensuu += 1;
    bind.nissuu += seikyuu.nissuu;
    bind.tensuu += seikyuu.tensuu;
  }

  get kensuu() {
    return Object.values(this.map).map(e => e.kensuu).reduce(add, 0);
  }

  get nissuu() {
    return Object.values(this.map).map(e => e.nissuu).reduce(add, 0);
  }

  get tensuu() {
    return Object.values(this.map).map(e => e.tensuu).reduce(add, 0);
  }
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

  let shahoCtx = new ShahoContext();
  console.log("");
  console.log("社保 ===================================");
  console.log("医療保険")
  console.log("　　70以上一般、公費併用 -- " + shahoHeiyouSummary("S:kourei:heiyou", shahoCtx));
  console.log("　　70以上一般、単独　　");
  let shahoSubCtx = new ShahoSubContext();
  printShahoSubs("S:kourei:tandoku", shahoSubCtx, shahoCtx);
  console.log("　　　　　　　　　　　　 -- " +
    formatShahoReport(shahoSubCtx.kensuu, shahoSubCtx.nissuu, shahoSubCtx.tensuu));
  console.log(`　　70以上７割、公費併用 -- ${shahoHeiyouSummary("S:kourei7:heiyou", shahoCtx)}`);
  console.log("　　70以上７割、単独　　");
  shahoSubCtx = new ShahoSubContext();
  printShahoSubs("S:kourei7:tandoku", shahoSubCtx, shahoCtx);
  console.log("　　　　　　　　　　　　 -- " +
    formatShahoReport(shahoSubCtx.kensuu, shahoSubCtx.nissuu, shahoSubCtx.tensuu));
  console.log(`　　本人、公費併用　　　 -- ${shahoHeiyouSummary("S:honnin:heiyou", shahoCtx)}`);
  console.log("　　本人、単独　　　　　");
  shahoSubCtx = new ShahoSubContext();
  printShahoSubs("S:honnin:tandoku", shahoSubCtx, shahoCtx);
  console.log("　　　　　　　　　　　　 -- " +
    formatShahoReport(shahoSubCtx.kensuu, shahoSubCtx.nissuu, shahoSubCtx.tensuu));
  console.log(`　　家族、公費併用　　　 -- ${shahoHeiyouSummary("S:kazoku:heiyou", shahoCtx)}`);
  console.log(`　　家族、単独　　　　　`);
  shahoSubCtx = new ShahoSubContext();
  printShahoSubs("S:kazoku:tandoku", shahoSubCtx, shahoCtx);
  console.log("　　　　　　　　　　　　 -- " +
    formatShahoReport(shahoSubCtx.kensuu, shahoSubCtx.nissuu, shahoSubCtx.tensuu));
  console.log(`　　六歳、公費併用　　　 -- ${shahoHeiyouSummary("S:child:heiyou", shahoCtx)}`);
  console.log(`　　六歳、単独　　　　　`);
  shahoSubCtx = new ShahoSubContext();
  printShahoSubs("S:child:tandoku", shahoSubCtx, shahoCtx);
  console.log("　　　　　　　　　　　　 -- " +
    formatShahoReport(shahoSubCtx.kensuu, shahoSubCtx.nissuu, shahoSubCtx.tensuu));
  console.log(`　　(1) 合計：${shahoCtx.kensuu}`);
  console.log("公費負担");
  printShahoKouhiFutan();
  console.log(`　　(2) 合計：${shahoTotal2()}`)

  if( Object.values(seikyuuMap).map(s => s.length).reduce(add, 0) == kokuhoTotalKensuu + shahoCtx.kensuu ){
    console.log("Total check OK");
  } else {
    console.error("Total check failed");
  }
  
}

function add(a, b){
  return a + b;
}

function seikyuuWithPrefix(prefix){
  return Object.keys(seikyuuMap)
    .filter(k => k.startsWith(prefix))
    .flatMap(k => seikyuuMap[k]);
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

function shahoHeiyouSummary(key, shahoCtx) {
  const list = seikyuuMap[key] || [];
  shahoCtx.kensuu += list.length;
  return shahoReport(list);
}

function shahoReport(list) {
  if( list.length === 0 ){
    return "";
  }
  const nissuu = list.map(s => s.nissuu).reduce((a, b) => a + b, 0);
  const tensuu = list.map(s => s.tensuu).reduce((a, b) => a + b, 0);
  return formatShahoReport(list.length, nissuu, tensuu);
}

function formatShahoReport(kensuu, nissuu, tensuu) {
  return `件数：${pad(kensuu, 3)}, 実日数：${pad(nissuu, 2)}, 点数：${pad(tensuu, 6)}`;
}

function printShahoSubs(prefix, subCtx, ctx){
  const list = seikyuuWithPrefix(prefix);
  list.forEach(seikyuu => subCtx.add(seikyuu));
  Object.keys(subCtx.map).sort().forEach(hoken => {
    const bind = subCtx.map[hoken];
    const repKensuu = `件数：${pad(bind.kensuu, 2)}`;
    const repNissuu = `日数：${pad(bind.nissuu, 2)}`;
    const repTensuu = `点数：${pad(bind.tensuu, 5)}`;
    console.log(`      保険（${hoken}）  ${repKensuu}, ${repNissuu}, ${repTensuu}`)
  });
  ctx.kensuu += subCtx.kensuu;
}

function filterSeikyuu(keyTest) {
  return Object.keys(seikyuuMap).filter(key => keyTest(key))
    .flatMap(key => seikyuuMap[key] || []);
}

function printShahoKouhiFutan() {
  const list = filterSeikyuu(key => key.startsWith("S:")).filter(s => s.hasKouhi);
  const map = {};
  list.forEach(seikyuu => {
    const kouhi = seikyuu.kouhi;
    const key = Math.floor(kouhi / 1000000).toString();
    if( !(key in map) ){
      map[key] = {
        kensuu: 0,
        nissuu: 0,
        tensuu: 0
      }
    }
    const bind = map[key];
    bind.kensuu += 1;
    bind.nissuu += seikyuu.nissuu;
    bind.tensuu += seikyuu.tensuu
  })
  Object.keys(map).sort().forEach(key => {
    const bind = map[key];
    const repKensuu = `件数：${pad(bind.kensuu, 2)}`;
    const repNissuu = `日数：${pad(bind.nissuu, 2)}`;
    const repTensuu = `点数：${pad(bind.tensuu, 5)}`;
    console.log(`      公費（${key}）  ${repKensuu}, ${repNissuu}, ${repTensuu}`)
  });
}

function shahoTotal2() {
  const list = filterSeikyuu(key => key.startsWith("S:")).filter(s => s.hasKouhi);
  const tensuu = list.map(s => s.tensuu).reduce((a, b) => a + b, 0);
  return `件数：${pad(list.length, 3)}, 点数：${tensuu}`
}

function mapSeikyuu(seikyuu) {
  const visits = seikyuu["受診"];
  const tensuu = visits.flatMap(v => v["診療"]).map(s => parseInt(s["点数"][0])).reduce((a, b) => a + b, 0);
  const kouhi = getKouhi(seikyuu);
  return {
    hokenshaBangou: parseInt(seikyuu["保険者番号"][0]),
    tensuu,
    kouhi,
    hasKouhi: !!kouhi,
    hokenFutan: seikyuu["保険負担"][0],
    nissuu: seikyuu["受診"].length
  };
}

function isKennai(hokenshaBangou){
  const n = hokenshaBangou;
  const fuken = Math.floor((n % 1000000) / 10000);
  return fuken == 13; // 東京
}

function getKouhi(map){
  return map["公費1負担者番号"] || map["公費2負担者番号"];
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

