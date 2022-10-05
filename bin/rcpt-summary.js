const fs = require("fs");
const { exit } = require("process");
const { parseString} = require("xml2js");

const kokuhoSummary = {
  kokuhoProper: {
    kennai: createEntry(),
    kengai: createEntry()
  },
  koukikourei: {
    kennai: createEntry(),
    kengai: createEntry()
  }
}

function dispatchKokuho(map){
  if( isKoukikourei(map.hokenshaBangou) ){
    if( isKennai(map.hokenshaBangou) ){
      addToEntry(kokuhoSummary.koukikourei.kennai, map);
    } else {
      addToEntry(kokuhoSummary.koukikourei.kengai, map);
    }
  } else {
    if( isKennai(map.hokenshaBangou) ){
      addToEntry(kokuhoSummary.kokuhoProper.kennai, map);
    } else {
      addToEntry(kokuhoSummary.kokuhoProper.kengai, map);
    }
  }
}

function dispatch(map){
  if( isKokuho(map.hokenshaBangou) || isKoukikourei(map.hokenshaBangou)){
    dispatchKokuho(map);
  } else {

  }
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

function report() {
  console.dir(kokuhoSummary);
  console.log("国保 ===================================");
  console.log("　　国保分");
  console.log("　　　　都内文")
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
  return map["公費1負担者番号"] || map["公費2負担者番号"];
}

function isKokuho(hokenshaBangou) {
  const n = hokenshaBangou;
  if (n >= 10000 && n <= 999999) return true;
  if (n >= 67000000 && n <= 67999999) return true;
  return false;
}

function isKoukikourei(hokenshaBangou) {
  const n = hokenshaBangou;
  return n >= 39000000 && n <= 39999999;
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