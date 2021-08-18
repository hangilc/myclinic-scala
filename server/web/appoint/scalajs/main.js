(function(){
'use strict';
var $linkingInfo = Object.freeze({
  "esVersion": 6,
  "assumingES6": true,
  "productionMode": false,
  "linkerVersion": "1.7.0",
  "fileLevelThis": this
});
var $imul = Math.imul;
var $fround = Math.fround;
var $clz32 = Math.clz32;
var $getOwnPropertyDescriptors = (Object.getOwnPropertyDescriptors || (() => {
  var ownKeysFun;
  if ((((typeof Reflect) !== "undefined") && Reflect.ownKeys)) {
    ownKeysFun = Reflect.ownKeys
  } else {
    var getOwnPropertySymbols = (Object.getOwnPropertySymbols || ((o) => []));
    ownKeysFun = ((o) => Object.getOwnPropertyNames(o).concat(getOwnPropertySymbols(o)))
  };
  return ((o) => {
    var ownKeys = ownKeysFun(o);
    var descriptors = {};
    var len = (ownKeys.length | 0);
    var i = 0;
    while ((i !== len)) {
      var key = ownKeys[i];
      Object.defineProperty(descriptors, key, {
        "configurable": true,
        "enumerable": true,
        "writable": true,
        "value": Object.getOwnPropertyDescriptor(o, key)
      });
      i = ((i + 1) | 0)
    };
    return descriptors
  })
})());
var $L0;
function $propertyName(arg0) {
  for (var prop in arg0) {
    return prop
  }
}
function $Char(c) {
  this.c = c
}
$Char.prototype.toString = (function() {
  return String.fromCharCode(this.c)
});
function $throwClassCastException(arg0, arg1) {
  throw new $c_Lorg_scalajs_linker_runtime_UndefinedBehaviorError(new $c_jl_ClassCastException(((arg0 + " is not an instance of ") + arg1)))
}
function $throwArrayCastException(arg0, arg1, arg2) {
  while ((--arg2)) {
    arg1 = ("[" + arg1)
  };
  $throwClassCastException(arg0, arg1)
}
function $throwArrayIndexOutOfBoundsException(arg0) {
  throw new $c_Lorg_scalajs_linker_runtime_UndefinedBehaviorError($ct_jl_ArrayIndexOutOfBoundsException__T__(new $c_jl_ArrayIndexOutOfBoundsException(), ((arg0 === null) ? null : ("" + arg0))))
}
function $noIsInstance(arg0) {
  throw new TypeError("Cannot call isInstance() on a Class representing a JS trait/object")
}
function $newArrayObject(arg0, arg1) {
  return $newArrayObjectInternal(arg0, arg1, 0)
}
function $newArrayObjectInternal(arg0, arg1, arg2) {
  var result = new arg0.constr(arg1[arg2]);
  if ((arg2 < (arg1.length - 1))) {
    var subArrayClassData = arg0.componentData;
    var subLengthIndex = (arg2 + 1);
    var underlying = result.u;
    for (var i = 0; (i < underlying.length); (i++)) {
      underlying[i] = $newArrayObjectInternal(subArrayClassData, arg1, subLengthIndex)
    }
  };
  return result
}
function $objectClone(arg0) {
  return Object.create(Object.getPrototypeOf(arg0), $getOwnPropertyDescriptors(arg0))
}
function $objectOrArrayClone(arg0) {
  return (arg0.$classData.isArrayClass ? arg0.clone__O() : $objectClone(arg0))
}
function $objectGetClass(arg0) {
  switch ((typeof arg0)) {
    case "string": {
      return $d_T.getClassOf()
    }
    case "number": {
      if ($isInt(arg0)) {
        if ((((arg0 << 24) >> 24) === arg0)) {
          return $d_jl_Byte.getClassOf()
        } else if ((((arg0 << 16) >> 16) === arg0)) {
          return $d_jl_Short.getClassOf()
        } else {
          return $d_jl_Integer.getClassOf()
        }
      } else {
        return $d_jl_Float.getClassOf()
      }
    }
    case "boolean": {
      return $d_jl_Boolean.getClassOf()
    }
    case "undefined": {
      return $d_jl_Void.getClassOf()
    }
    default: {
      if ((arg0 === null)) {
        return arg0.getClass__jl_Class()
      } else if ((arg0 instanceof $c_RTLong)) {
        return $d_jl_Long.getClassOf()
      } else if ((arg0 instanceof $Char)) {
        return $d_jl_Character.getClassOf()
      } else if ((!(!(arg0 && arg0.$classData)))) {
        return arg0.$classData.getClassOf()
      } else {
        return null
      }
    }
  }
}
function $objectClassName(arg0) {
  switch ((typeof arg0)) {
    case "string": {
      return "java.lang.String"
    }
    case "number": {
      if ($isInt(arg0)) {
        if ((((arg0 << 24) >> 24) === arg0)) {
          return "java.lang.Byte"
        } else if ((((arg0 << 16) >> 16) === arg0)) {
          return "java.lang.Short"
        } else {
          return "java.lang.Integer"
        }
      } else {
        return "java.lang.Float"
      }
    }
    case "boolean": {
      return "java.lang.Boolean"
    }
    case "undefined": {
      return "java.lang.Void"
    }
    default: {
      if ((arg0 === null)) {
        return arg0.getClass__jl_Class()
      } else if ((arg0 instanceof $c_RTLong)) {
        return "java.lang.Long"
      } else if ((arg0 instanceof $Char)) {
        return "java.lang.Character"
      } else if ((!(!(arg0 && arg0.$classData)))) {
        return arg0.$classData.name
      } else {
        return null.getName__T()
      }
    }
  }
}
function $dp_compareTo__T__I(instance, x0) {
  if (((typeof instance) === "string")) {
    return $f_T__compareTo__T__I(instance, x0)
  } else {
    return instance.compareTo__T__I(x0)
  }
}
function $dp_equals__O__Z(instance, x0) {
  switch ((typeof instance)) {
    case "string": {
      return $f_T__equals__O__Z(instance, x0)
    }
    case "number": {
      return $f_jl_Double__equals__O__Z(instance, x0)
    }
    case "boolean": {
      return $f_jl_Boolean__equals__O__Z(instance, x0)
    }
    case "undefined": {
      return $f_jl_Void__equals__O__Z(instance, x0)
    }
    default: {
      if (((!(!(instance && instance.$classData))) || (instance === null))) {
        return instance.equals__O__Z(x0)
      } else if ((instance instanceof $Char)) {
        return $f_jl_Character__equals__O__Z(instance, x0)
      } else {
        return $c_O.prototype.equals__O__Z.call(instance, x0)
      }
    }
  }
}
function $dp_getChars__I__I__AC__I__V(instance, x0, x1, x2, x3) {
  if (((typeof instance) === "string")) {
    return $f_T__getChars__I__I__AC__I__V(instance, x0, x1, x2, x3)
  } else {
    return instance.getChars__I__I__AC__I__V(x0, x1, x2, x3)
  }
}
function $dp_hashCode__I(instance) {
  switch ((typeof instance)) {
    case "string": {
      return $f_T__hashCode__I(instance)
    }
    case "number": {
      return $f_jl_Double__hashCode__I(instance)
    }
    case "boolean": {
      return $f_jl_Boolean__hashCode__I(instance)
    }
    case "undefined": {
      return $f_jl_Void__hashCode__I(instance)
    }
    default: {
      if (((!(!(instance && instance.$classData))) || (instance === null))) {
        return instance.hashCode__I()
      } else if ((instance instanceof $Char)) {
        return $f_jl_Character__hashCode__I(instance)
      } else {
        return $c_O.prototype.hashCode__I.call(instance)
      }
    }
  }
}
function $dp_toString__T(instance) {
  return ((instance === (void 0)) ? "undefined" : instance.toString())
}
function $intDiv(arg0, arg1) {
  if ((arg1 === 0)) {
    throw new $c_jl_ArithmeticException("/ by zero")
  } else {
    return ((arg0 / arg1) | 0)
  }
}
function $intMod(arg0, arg1) {
  if ((arg1 === 0)) {
    throw new $c_jl_ArithmeticException("/ by zero")
  } else {
    return ((arg0 % arg1) | 0)
  }
}
function $doubleToInt(arg0) {
  return ((arg0 > 2147483647) ? 2147483647 : ((arg0 < (-2147483648)) ? (-2147483648) : (arg0 | 0)))
}
function $resolveSuperRef(arg0, arg1) {
  var getPrototypeOf = Object.getPrototyeOf;
  var getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
  var superProto = arg0.prototype;
  while ((superProto !== null)) {
    var desc = getOwnPropertyDescriptor(superProto, arg1);
    if ((desc !== (void 0))) {
      return desc
    };
    superProto = getPrototypeOf(superProto)
  }
}
function $superGet(arg0, arg1, arg2) {
  var desc = $resolveSuperRef(arg0, arg2);
  if ((desc !== (void 0))) {
    var getter = desc.get;
    return ((getter !== (void 0)) ? getter.call(arg1) : getter.value)
  }
}
function $superSet(arg0, arg1, arg2, arg3) {
  var desc = $resolveSuperRef(arg0, arg2);
  if ((desc !== (void 0))) {
    var setter = desc.set;
    if ((setter !== (void 0))) {
      setter.call(arg1, arg3);
      return (void 0)
    }
  };
  throw new TypeError((("super has no setter '" + arg2) + "'."))
}
function $arraycopyCheckBounds(arg0, arg1, arg2, arg3, arg4) {
  if ((((((arg1 < 0) || (arg3 < 0)) || (arg4 < 0)) || (arg1 > ((arg0 - arg4) | 0))) || (arg3 > ((arg2 - arg4) | 0)))) {
    $throwArrayIndexOutOfBoundsException(null)
  }
}
function $arraycopyGeneric(arg0, arg1, arg2, arg3, arg4) {
  $arraycopyCheckBounds(arg0.length, arg1, arg2.length, arg3, arg4);
  if ((((arg0 !== arg2) || (arg3 < arg1)) || (((arg1 + arg4) | 0) < arg3))) {
    for (var i = 0; (i < arg4); i = ((i + 1) | 0)) {
      arg2[((arg3 + i) | 0)] = arg0[((arg1 + i) | 0)]
    }
  } else {
    for (var i = ((arg4 - 1) | 0); (i >= 0); i = ((i - 1) | 0)) {
      arg2[((arg3 + i) | 0)] = arg0[((arg1 + i) | 0)]
    }
  }
}
var $lastIDHash = 0;
var $idHashCodeMap = new WeakMap();
function $systemIdentityHashCode(obj) {
  switch ((typeof obj)) {
    case "string": {
      return $f_T__hashCode__I(obj)
    }
    case "number": {
      return $f_jl_Double__hashCode__I(obj)
    }
    case "bigint": {
      var biHash = 0;
      if ((obj < BigInt(0))) {
        obj = (~obj)
      };
      while ((obj !== BigInt(0))) {
        biHash = (biHash ^ Number(BigInt.asIntN(32, obj)));
        obj = (obj >> BigInt(32))
      };
      return biHash
    }
    case "boolean": {
      return (obj ? 1231 : 1237)
    }
    case "undefined": {
      return 0
    }
    case "symbol": {
      var description = obj.description;
      return ((description === (void 0)) ? 0 : $f_T__hashCode__I(description))
    }
    default: {
      if ((obj === null)) {
        return 0
      } else {
        var hash = $idHashCodeMap.get(obj);
        if ((hash === (void 0))) {
          hash = (($lastIDHash + 1) | 0);
          $lastIDHash = hash;
          $idHashCodeMap.set(obj, hash)
        };
        return hash
      }
    }
  }
}
function $isByte(arg0) {
  return ((((typeof arg0) === "number") && (((arg0 << 24) >> 24) === arg0)) && ((1 / arg0) !== (1 / (-0))))
}
function $isShort(arg0) {
  return ((((typeof arg0) === "number") && (((arg0 << 16) >> 16) === arg0)) && ((1 / arg0) !== (1 / (-0))))
}
function $isInt(arg0) {
  return ((((typeof arg0) === "number") && ((arg0 | 0) === arg0)) && ((1 / arg0) !== (1 / (-0))))
}
function $bC(arg0) {
  return new $Char(arg0)
}
var $bC0 = $bC(0);
function $uV(arg0) {
  return (((arg0 === (void 0)) || (arg0 === null)) ? (void 0) : $throwClassCastException(arg0, "java.lang.Void"))
}
function $uZ(arg0) {
  return ((((typeof arg0) === "boolean") || (arg0 === null)) ? (!(!arg0)) : $throwClassCastException(arg0, "java.lang.Boolean"))
}
function $uC(arg0) {
  return (((arg0 instanceof $Char) || (arg0 === null)) ? ((arg0 === null) ? 0 : arg0.c) : $throwClassCastException(arg0, "java.lang.Character"))
}
function $uB(arg0) {
  return (($isByte(arg0) || (arg0 === null)) ? (arg0 | 0) : $throwClassCastException(arg0, "java.lang.Byte"))
}
function $uS(arg0) {
  return (($isShort(arg0) || (arg0 === null)) ? (arg0 | 0) : $throwClassCastException(arg0, "java.lang.Short"))
}
function $uI(arg0) {
  return (($isInt(arg0) || (arg0 === null)) ? (arg0 | 0) : $throwClassCastException(arg0, "java.lang.Integer"))
}
function $uJ(arg0) {
  return (((arg0 instanceof $c_RTLong) || (arg0 === null)) ? ((arg0 === null) ? $L0 : arg0) : $throwClassCastException(arg0, "java.lang.Long"))
}
function $uF(arg0) {
  return ((((typeof arg0) === "number") || (arg0 === null)) ? (+arg0) : $throwClassCastException(arg0, "java.lang.Float"))
}
function $uD(arg0) {
  return ((((typeof arg0) === "number") || (arg0 === null)) ? (+arg0) : $throwClassCastException(arg0, "java.lang.Double"))
}
function $uT(arg0) {
  return ((((typeof arg0) === "string") || (arg0 === null)) ? ((arg0 === null) ? "" : arg0) : $throwClassCastException(arg0, "java.lang.String"))
}
function $ct_O__($thiz) {
  return $thiz
}
/** @constructor */
function $c_O() {
  /*<skip>*/
}
$c_O.prototype.constructor = $c_O;
/** @constructor */
function $h_O() {
  /*<skip>*/
}
$h_O.prototype = $c_O.prototype;
$c_O.prototype.hashCode__I = (function() {
  return $systemIdentityHashCode(this)
});
$c_O.prototype.equals__O__Z = (function(that) {
  return (this === that)
});
$c_O.prototype.toString__T = (function() {
  var i = this.hashCode__I();
  return (($objectClassName(this) + "@") + $as_T($uD((i >>> 0)).toString(16)))
});
$c_O.prototype.toString = (function() {
  return this.toString__T()
});
function $ac_O(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Array(arg);
    for (var i = 0; (i < arg); (i++)) {
      this.u[i] = null
    }
  } else {
    this.u = arg
  }
}
$ac_O.prototype = new $h_O();
$ac_O.prototype.constructor = $ac_O;
$ac_O.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_O.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_O.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyGeneric(this.u, srcPos, dest.u, destPos, length)
});
$ac_O.prototype.clone__O = (function() {
  return new $ac_O(this.u.slice())
});
function $ah_O() {
  /*<skip>*/
}
$ah_O.prototype = $ac_O.prototype;
function $ac_Z(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Array(arg);
    for (var i = 0; (i < arg); (i++)) {
      this.u[i] = false
    }
  } else {
    this.u = arg
  }
}
$ac_Z.prototype = new $h_O();
$ac_Z.prototype.constructor = $ac_Z;
$ac_Z.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_Z.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_Z.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyGeneric(this.u, srcPos, dest.u, destPos, length)
});
$ac_Z.prototype.clone__O = (function() {
  return new $ac_Z(this.u.slice())
});
function $ac_C(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Uint16Array(arg)
  } else {
    this.u = arg
  }
}
$ac_C.prototype = new $h_O();
$ac_C.prototype.constructor = $ac_C;
$ac_C.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_C.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_C.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_C.prototype.clone__O = (function() {
  return new $ac_C(this.u.slice())
});
function $ac_B(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Int8Array(arg)
  } else {
    this.u = arg
  }
}
$ac_B.prototype = new $h_O();
$ac_B.prototype.constructor = $ac_B;
$ac_B.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_B.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_B.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_B.prototype.clone__O = (function() {
  return new $ac_B(this.u.slice())
});
function $ac_S(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Int16Array(arg)
  } else {
    this.u = arg
  }
}
$ac_S.prototype = new $h_O();
$ac_S.prototype.constructor = $ac_S;
$ac_S.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_S.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_S.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_S.prototype.clone__O = (function() {
  return new $ac_S(this.u.slice())
});
function $ac_I(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Int32Array(arg)
  } else {
    this.u = arg
  }
}
$ac_I.prototype = new $h_O();
$ac_I.prototype.constructor = $ac_I;
$ac_I.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_I.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_I.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_I.prototype.clone__O = (function() {
  return new $ac_I(this.u.slice())
});
function $ac_J(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Array(arg);
    for (var i = 0; (i < arg); (i++)) {
      this.u[i] = $L0
    }
  } else {
    this.u = arg
  }
}
$ac_J.prototype = new $h_O();
$ac_J.prototype.constructor = $ac_J;
$ac_J.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_J.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_J.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyGeneric(this.u, srcPos, dest.u, destPos, length)
});
$ac_J.prototype.clone__O = (function() {
  return new $ac_J(this.u.slice())
});
function $ac_F(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Float32Array(arg)
  } else {
    this.u = arg
  }
}
$ac_F.prototype = new $h_O();
$ac_F.prototype.constructor = $ac_F;
$ac_F.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_F.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_F.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_F.prototype.clone__O = (function() {
  return new $ac_F(this.u.slice())
});
function $ac_D(arg) {
  if (((typeof arg) === "number")) {
    this.u = new Float64Array(arg)
  } else {
    this.u = arg
  }
}
$ac_D.prototype = new $h_O();
$ac_D.prototype.constructor = $ac_D;
$ac_D.prototype.get = (function(i) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  return this.u[i]
});
$ac_D.prototype.set = (function(i, v) {
  if (((i < 0) || (i >= this.u.length))) {
    $throwArrayIndexOutOfBoundsException(i)
  };
  this.u[i] = v
});
$ac_D.prototype.copyTo = (function(srcPos, dest, destPos, length) {
  $arraycopyCheckBounds(this.u.length, srcPos, dest.u.length, destPos, length);
  dest.u.set(this.u.subarray(srcPos, ((srcPos + length) | 0)), destPos)
});
$ac_D.prototype.clone__O = (function() {
  return new $ac_D(this.u.slice())
});
function $TypeData() {
  this.constr = (void 0);
  this.ancestors = null;
  this.componentData = null;
  this.arrayBase = null;
  this.arrayDepth = 0;
  this.zero = null;
  this.arrayEncodedName = "";
  this._classOf = (void 0);
  this._arrayOf = (void 0);
  this.isAssignableFromFun = (void 0);
  this.wrapArray = (void 0);
  this.name = "";
  this.isPrimitive = false;
  this.isInterface = false;
  this.isArrayClass = false;
  this.isJSClass = false;
  this.isInstance = (void 0)
}
$TypeData.prototype.initPrim = (function(zero, arrayEncodedName, displayName, arrayClass, typedArrayClass) {
  this.ancestors = {};
  this.zero = zero;
  this.arrayEncodedName = arrayEncodedName;
  var self = this;
  this.isAssignableFromFun = ((that) => (that === self));
  this.name = displayName;
  this.isPrimitive = true;
  this.isInstance = ((obj) => false);
  if ((arrayClass !== (void 0))) {
    this._arrayOf = new $TypeData().initSpecializedArray(this, arrayClass, typedArrayClass)
  };
  return this
});
$TypeData.prototype.initClass = (function(internalNameObj, isInterface, fullName, ancestors, isJSType, parentData, isInstance) {
  var internalName = $propertyName(internalNameObj);
  this.ancestors = ancestors;
  this.arrayEncodedName = (("L" + fullName) + ";");
  this.isAssignableFromFun = ((that) => (!(!that.ancestors[internalName])));
  this.isJSType = (!(!isJSType));
  this.name = fullName;
  this.isInterface = isInterface;
  this.isInstance = (isInstance || ((obj) => (!(!((obj && obj.$classData) && obj.$classData.ancestors[internalName])))));
  return this
});
$TypeData.prototype.initSpecializedArray = (function(componentData, arrayClass, typedArrayClass, isAssignableFromFun) {
  arrayClass.prototype.$classData = this;
  var name = ("[" + componentData.arrayEncodedName);
  this.constr = arrayClass;
  this.ancestors = {
    O: 1,
    jl_Cloneable: 1,
    Ljava_io_Serializable: 1
  };
  this.componentData = componentData;
  this.arrayBase = componentData;
  this.arrayDepth = 1;
  this.arrayEncodedName = name;
  this.name = name;
  this.isArrayClass = true;
  var self = this;
  this.isAssignableFromFun = (isAssignableFromFun || ((that) => (self === that)));
  this.wrapArray = (typedArrayClass ? ((array) => new arrayClass(new typedArrayClass(array))) : ((array) => new arrayClass(array)));
  this.isInstance = ((obj) => (obj instanceof arrayClass));
  return this
});
$TypeData.prototype.initArray = (function(componentData) {
  function ArrayClass(arg) {
    if (((typeof arg) === "number")) {
      this.u = new Array(arg);
      for (var i = 0; (i < arg); (i++)) {
        this.u[i] = null
      }
    } else {
      this.u = arg
    }
  }
  ArrayClass.prototype = new $ah_O();
  ArrayClass.prototype.constructor = ArrayClass;
  ArrayClass.prototype.copyTo = (function(srcPos, dest, destPos, length) {
    $arraycopyGeneric(this.u, srcPos, dest.u, destPos, length)
  });
  ArrayClass.prototype.clone__O = (function() {
    return new ArrayClass(this.u.slice())
  });
  var arrayBase = (componentData.arrayBase || componentData);
  var arrayDepth = (componentData.arrayDepth + 1);
  ArrayClass.prototype.$classData = this;
  var name = ("[" + componentData.arrayEncodedName);
  this.constr = ArrayClass;
  this.ancestors = {
    O: 1,
    jl_Cloneable: 1,
    Ljava_io_Serializable: 1
  };
  this.componentData = componentData;
  this.arrayBase = arrayBase;
  this.arrayDepth = arrayDepth;
  this.arrayEncodedName = name;
  this.name = name;
  this.isArrayClass = true;
  var isAssignableFromFun = ((that) => {
    var thatDepth = that.arrayDepth;
    return ((thatDepth === arrayDepth) ? arrayBase.isAssignableFromFun(that.arrayBase) : ((thatDepth > arrayDepth) && (arrayBase === $d_O)))
  });
  this.isAssignableFromFun = isAssignableFromFun;
  this.wrapArray = ((array) => new ArrayClass(array));
  var self = this;
  this.isInstance = ((obj) => {
    var data = (obj && obj.$classData);
    return ((!(!data)) && ((data === self) || isAssignableFromFun(data)))
  });
  return this
});
$TypeData.prototype.getArrayOf = (function() {
  if ((!this._arrayOf)) {
    this._arrayOf = new $TypeData().initArray(this)
  };
  return this._arrayOf
});
$TypeData.prototype.getClassOf = (function() {
  if ((!this._classOf)) {
    this._classOf = new $c_jl_Class(this)
  };
  return this._classOf
});
$TypeData.prototype.isAssignableFrom = (function(that) {
  return ((this === that) || this.isAssignableFromFun(that))
});
$TypeData.prototype.checkCast = (function(obj) {
  if ((((obj !== null) && (!this.isJSType)) && (!this.isInstance(obj)))) {
    $throwClassCastException(obj, this.name)
  }
});
$TypeData.prototype.getSuperclass = (function() {
  return (this.parentData ? this.parentData.getClassOf() : null)
});
$TypeData.prototype.getComponentType = (function() {
  return (this.componentData ? this.componentData.getClassOf() : null)
});
$TypeData.prototype.newArrayOfThisClass = (function(lengths) {
  var arrayClassData = this;
  for (var i = 0; (i < lengths.length); (i++)) {
    arrayClassData = arrayClassData.getArrayOf()
  };
  return $newArrayObject(arrayClassData, lengths)
});
function $isArrayOf_O(obj, depth) {
  var data = (obj && obj.$classData);
  if ((!data)) {
    return false
  } else {
    var arrayDepth = data.arrayDepth;
    return ((arrayDepth === depth) ? (!data.arrayBase.isPrimitive) : (arrayDepth > depth))
  }
}
function $isArrayOf_Z(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_Z))))
}
function $isArrayOf_C(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_C))))
}
function $isArrayOf_B(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_B))))
}
function $isArrayOf_S(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_S))))
}
function $isArrayOf_I(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_I))))
}
function $isArrayOf_J(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_J))))
}
function $isArrayOf_F(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_F))))
}
function $isArrayOf_D(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && (obj.$classData.arrayBase === $d_D))))
}
function $asArrayOf_O(obj, depth) {
  if (($isArrayOf_O(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "Ljava.lang.Object;", depth)
  }
}
function $asArrayOf_Z(obj, depth) {
  if (($isArrayOf_Z(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "Z", depth)
  }
}
function $asArrayOf_C(obj, depth) {
  if (($isArrayOf_C(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "C", depth)
  }
}
function $asArrayOf_B(obj, depth) {
  if (($isArrayOf_B(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "B", depth)
  }
}
function $asArrayOf_S(obj, depth) {
  if (($isArrayOf_S(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "S", depth)
  }
}
function $asArrayOf_I(obj, depth) {
  if (($isArrayOf_I(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "I", depth)
  }
}
function $asArrayOf_J(obj, depth) {
  if (($isArrayOf_J(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "J", depth)
  }
}
function $asArrayOf_F(obj, depth) {
  if (($isArrayOf_F(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "F", depth)
  }
}
function $asArrayOf_D(obj, depth) {
  if (($isArrayOf_D(obj, depth) || (obj === null))) {
    return obj
  } else {
    $throwArrayCastException(obj, "D", depth)
  }
}
var $d_O = new $TypeData();
$d_O.ancestors = {
  O: 1
};
$d_O.arrayEncodedName = "Ljava.lang.Object;";
$d_O.isAssignableFromFun = ((that) => (!that.isPrimitive));
$d_O.name = "java.lang.Object";
$d_O.isInstance = ((obj) => (obj !== null));
$d_O._arrayOf = new $TypeData().initSpecializedArray($d_O, $ac_O, (void 0), ((that) => {
  var thatDepth = that.arrayDepth;
  return ((thatDepth === 1) ? (!that.arrayBase.isPrimitive) : (thatDepth > 1))
}));
$c_O.prototype.$classData = $d_O;
var $d_V = new $TypeData().initPrim((void 0), "V", "void", (void 0), (void 0));
var $d_Z = new $TypeData().initPrim(false, "Z", "boolean", $ac_Z, (void 0));
var $d_C = new $TypeData().initPrim(0, "C", "char", $ac_C, Uint16Array);
var $d_B = new $TypeData().initPrim(0, "B", "byte", $ac_B, Int8Array);
var $d_S = new $TypeData().initPrim(0, "S", "short", $ac_S, Int16Array);
var $d_I = new $TypeData().initPrim(0, "I", "int", $ac_I, Int32Array);
var $d_J = new $TypeData().initPrim(null, "J", "long", $ac_J, (void 0));
var $d_F = new $TypeData().initPrim(0.0, "F", "float", $ac_F, Float32Array);
var $d_D = new $TypeData().initPrim(0.0, "D", "double", $ac_D, Float64Array);
function $s_Ldev_myclinic_web_JsMain__main__AT__V(args) {
  $m_Ldev_myclinic_web_JsMain$().main__AT__V(args)
}
/** @constructor */
function $c_Ldev_myclinic_web_JsMain$() {
  this.Ldev_myclinic_web_JsMain$__f_banner = null;
  $n_Ldev_myclinic_web_JsMain$ = this;
  var e = $m_Lorg_scalajs_dom_package$().document__Lorg_scalajs_dom_raw_HTMLDocument().createElement("div");
  e.classList.add("container-fluid");
  e.innerHTML = "\r\n    <div class=\"row pt-3 pb-2 ml-5 mr-5\">\r\n        <h1 class=\"bg-dark text-white p-3 col-md-12\">\u8a3a\u5bdf\u4e88\u7d04</h1>\r\n    </div>\r\n    ";
  this.Ldev_myclinic_web_JsMain$__f_banner = e
}
$c_Ldev_myclinic_web_JsMain$.prototype = new $h_O();
$c_Ldev_myclinic_web_JsMain$.prototype.constructor = $c_Ldev_myclinic_web_JsMain$;
/** @constructor */
function $h_Ldev_myclinic_web_JsMain$() {
  /*<skip>*/
}
$h_Ldev_myclinic_web_JsMain$.prototype = $c_Ldev_myclinic_web_JsMain$.prototype;
$c_Ldev_myclinic_web_JsMain$.prototype.main__AT__V = (function(args) {
  var body = $m_Lorg_scalajs_dom_package$().document__Lorg_scalajs_dom_raw_HTMLDocument().body;
  body.classList.add("pb-5");
  body.appendChild(this.Ldev_myclinic_web_JsMain$__f_banner);
  var app = new $c_Ldev_myclinic_scala_model_Appoint($m_Ljava_time_LocalDate$().of__I__I__I__Ljava_time_LocalDate(2021, 9, 22), $m_Ljava_time_LocalTime$().of__I__I__I__Ljava_time_LocalTime(9, 40, 0), "\u7530\u4e2d\u592a\u90ce", $m_s_None$(), "");
  var this$2 = $m_s_Console$();
  var this$3 = this$2.out__Ljava_io_PrintStream();
  this$3.java$lang$JSConsoleBasedPrintStream$$printString__T__V((app + "\n"))
});
var $d_Ldev_myclinic_web_JsMain$ = new $TypeData().initClass({
  Ldev_myclinic_web_JsMain$: 0
}, false, "dev.myclinic.web.JsMain$", {
  Ldev_myclinic_web_JsMain$: 1,
  O: 1
});
$c_Ldev_myclinic_web_JsMain$.prototype.$classData = $d_Ldev_myclinic_web_JsMain$;
var $n_Ldev_myclinic_web_JsMain$;
function $m_Ldev_myclinic_web_JsMain$() {
  if ((!$n_Ldev_myclinic_web_JsMain$)) {
    $n_Ldev_myclinic_web_JsMain$ = new $c_Ldev_myclinic_web_JsMain$()
  };
  return $n_Ldev_myclinic_web_JsMain$
}
/** @constructor */
function $c_jl_Class(data0) {
  this.jl_Class__f_data = null;
  this.jl_Class__f_cachedSimpleName = null;
  this.jl_Class__f_data = data0
}
$c_jl_Class.prototype = new $h_O();
$c_jl_Class.prototype.constructor = $c_jl_Class;
/** @constructor */
function $h_jl_Class() {
  /*<skip>*/
}
$h_jl_Class.prototype = $c_jl_Class.prototype;
$c_jl_Class.prototype.toString__T = (function() {
  return ((this.isInterface__Z() ? "interface " : (this.isPrimitive__Z() ? "" : "class ")) + this.getName__T())
});
$c_jl_Class.prototype.isAssignableFrom__jl_Class__Z = (function(that) {
  return $uZ(this.jl_Class__f_data.isAssignableFrom(that.jl_Class__f_data))
});
$c_jl_Class.prototype.isInterface__Z = (function() {
  return $uZ(this.jl_Class__f_data.isInterface)
});
$c_jl_Class.prototype.isArray__Z = (function() {
  return $uZ(this.jl_Class__f_data.isArrayClass)
});
$c_jl_Class.prototype.isPrimitive__Z = (function() {
  return $uZ(this.jl_Class__f_data.isPrimitive)
});
$c_jl_Class.prototype.getName__T = (function() {
  return $as_T(this.jl_Class__f_data.name)
});
$c_jl_Class.prototype.getComponentType__jl_Class = (function() {
  return $as_jl_Class(this.jl_Class__f_data.getComponentType())
});
$c_jl_Class.prototype.newArrayOfThisClass__O__O = (function(dimensions) {
  return this.jl_Class__f_data.newArrayOfThisClass(dimensions)
});
function $as_jl_Class(obj) {
  return (((obj instanceof $c_jl_Class) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Class"))
}
function $isArrayOf_jl_Class(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Class)))
}
function $asArrayOf_jl_Class(obj, depth) {
  return (($isArrayOf_jl_Class(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Class;", depth))
}
var $d_jl_Class = new $TypeData().initClass({
  jl_Class: 0
}, false, "java.lang.Class", {
  jl_Class: 1,
  O: 1
});
$c_jl_Class.prototype.$classData = $d_jl_Class;
/** @constructor */
function $c_jl_FloatingPointBits$() {
  this.jl_FloatingPointBits$__f_java$lang$FloatingPointBits$$_areTypedArraysSupported = false;
  this.jl_FloatingPointBits$__f_arrayBuffer = null;
  this.jl_FloatingPointBits$__f_int32Array = null;
  this.jl_FloatingPointBits$__f_float32Array = null;
  this.jl_FloatingPointBits$__f_float64Array = null;
  this.jl_FloatingPointBits$__f_areTypedArraysBigEndian = false;
  this.jl_FloatingPointBits$__f_highOffset = 0;
  this.jl_FloatingPointBits$__f_lowOffset = 0;
  $n_jl_FloatingPointBits$ = this;
  this.jl_FloatingPointBits$__f_java$lang$FloatingPointBits$$_areTypedArraysSupported = true;
  this.jl_FloatingPointBits$__f_arrayBuffer = new ArrayBuffer(8);
  this.jl_FloatingPointBits$__f_int32Array = new Int32Array(this.jl_FloatingPointBits$__f_arrayBuffer, 0, 2);
  this.jl_FloatingPointBits$__f_float32Array = new Float32Array(this.jl_FloatingPointBits$__f_arrayBuffer, 0, 2);
  this.jl_FloatingPointBits$__f_float64Array = new Float64Array(this.jl_FloatingPointBits$__f_arrayBuffer, 0, 1);
  this.jl_FloatingPointBits$__f_int32Array[0] = 16909060;
  this.jl_FloatingPointBits$__f_areTypedArraysBigEndian = ($uB(new Int8Array(this.jl_FloatingPointBits$__f_arrayBuffer, 0, 8)[0]) === 1);
  this.jl_FloatingPointBits$__f_highOffset = (this.jl_FloatingPointBits$__f_areTypedArraysBigEndian ? 0 : 1);
  this.jl_FloatingPointBits$__f_lowOffset = (this.jl_FloatingPointBits$__f_areTypedArraysBigEndian ? 1 : 0)
}
$c_jl_FloatingPointBits$.prototype = new $h_O();
$c_jl_FloatingPointBits$.prototype.constructor = $c_jl_FloatingPointBits$;
/** @constructor */
function $h_jl_FloatingPointBits$() {
  /*<skip>*/
}
$h_jl_FloatingPointBits$.prototype = $c_jl_FloatingPointBits$.prototype;
$c_jl_FloatingPointBits$.prototype.numberHashCode__D__I = (function(value) {
  var iv = $uI((value | 0));
  if (((iv === value) && ((1.0 / value) !== (-Infinity)))) {
    return iv
  } else {
    this.jl_FloatingPointBits$__f_float64Array[0] = value;
    return ($uI(this.jl_FloatingPointBits$__f_int32Array[0]) ^ $uI(this.jl_FloatingPointBits$__f_int32Array[1]))
  }
});
var $d_jl_FloatingPointBits$ = new $TypeData().initClass({
  jl_FloatingPointBits$: 0
}, false, "java.lang.FloatingPointBits$", {
  jl_FloatingPointBits$: 1,
  O: 1
});
$c_jl_FloatingPointBits$.prototype.$classData = $d_jl_FloatingPointBits$;
var $n_jl_FloatingPointBits$;
function $m_jl_FloatingPointBits$() {
  if ((!$n_jl_FloatingPointBits$)) {
    $n_jl_FloatingPointBits$ = new $c_jl_FloatingPointBits$()
  };
  return $n_jl_FloatingPointBits$
}
/** @constructor */
function $c_jl_Math$() {
  /*<skip>*/
}
$c_jl_Math$.prototype = new $h_O();
$c_jl_Math$.prototype.constructor = $c_jl_Math$;
/** @constructor */
function $h_jl_Math$() {
  /*<skip>*/
}
$h_jl_Math$.prototype = $c_jl_Math$.prototype;
$c_jl_Math$.prototype.addExact__J__J__J = (function(a, b) {
  var alo = a.RTLong__f_lo;
  var ahi = a.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  var lo = ((alo + b.RTLong__f_lo) | 0);
  var hi = ((((-2147483648) ^ lo) < ((-2147483648) ^ alo)) ? ((1 + ((ahi + bhi) | 0)) | 0) : ((ahi + bhi) | 0));
  var resSgnBit = (hi < 0);
  var ahi$1 = a.RTLong__f_hi;
  if ((resSgnBit === (ahi$1 < 0))) {
    var $$x1 = true
  } else {
    var ahi$2 = b.RTLong__f_hi;
    var $$x1 = (resSgnBit === (ahi$2 < 0))
  };
  if ($$x1) {
    return new $c_RTLong(lo, hi)
  } else {
    throw new $c_jl_ArithmeticException("Long overflow")
  }
});
$c_jl_Math$.prototype.floorDiv__J__J__J = (function(a, b) {
  var this$1 = $m_RTLong$();
  var lo = this$1.divideImpl__I__I__I__I__I(a.RTLong__f_lo, a.RTLong__f_hi, b.RTLong__f_lo, b.RTLong__f_hi);
  var hi = this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
  var ahi = a.RTLong__f_hi;
  var ahi$1 = b.RTLong__f_hi;
  if (((ahi < 0) === (ahi$1 < 0))) {
    var $$x1 = true
  } else {
    var blo = b.RTLong__f_lo;
    var a0 = (65535 & lo);
    var a1 = ((lo >>> 16) | 0);
    var b0 = (65535 & blo);
    var b1 = ((blo >>> 16) | 0);
    var a0b0 = $imul(a0, b0);
    var a1b0 = $imul(a1, b0);
    var a0b1 = $imul(a0, b1);
    var lo$1 = ((a0b0 + (((a1b0 + a0b1) | 0) << 16)) | 0);
    var c1part = ((((a0b0 >>> 16) | 0) + a0b1) | 0);
    var hi$1 = (((((((($imul(lo, b.RTLong__f_hi) + $imul(hi, blo)) | 0) + $imul(a1, b1)) | 0) + ((c1part >>> 16) | 0)) | 0) + (((((65535 & c1part) + a1b0) | 0) >>> 16) | 0)) | 0);
    var $$x1 = ((lo$1 === a.RTLong__f_lo) && (hi$1 === a.RTLong__f_hi))
  };
  if ($$x1) {
    return new $c_RTLong(lo, hi)
  } else {
    var lo$2 = (((-1) + lo) | 0);
    var hi$2 = ((lo$2 !== (-1)) ? hi : (((-1) + hi) | 0));
    return new $c_RTLong(lo$2, hi$2)
  }
});
$c_jl_Math$.prototype.floorMod__J__J__J = (function(a, b) {
  var this$1 = $m_RTLong$();
  var lo = this$1.remainderImpl__I__I__I__I__I(a.RTLong__f_lo, a.RTLong__f_hi, b.RTLong__f_lo, b.RTLong__f_hi);
  var hi = this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
  var ahi = a.RTLong__f_hi;
  var ahi$1 = b.RTLong__f_hi;
  if ((((ahi < 0) === (ahi$1 < 0)) || ((lo === 0) && (hi === 0)))) {
    return new $c_RTLong(lo, hi)
  } else {
    var bhi = b.RTLong__f_hi;
    var lo$1 = ((lo + b.RTLong__f_lo) | 0);
    var hi$1 = ((((-2147483648) ^ lo$1) < ((-2147483648) ^ lo)) ? ((1 + ((hi + bhi) | 0)) | 0) : ((hi + bhi) | 0));
    return new $c_RTLong(lo$1, hi$1)
  }
});
var $d_jl_Math$ = new $TypeData().initClass({
  jl_Math$: 0
}, false, "java.lang.Math$", {
  jl_Math$: 1,
  O: 1
});
$c_jl_Math$.prototype.$classData = $d_jl_Math$;
var $n_jl_Math$;
function $m_jl_Math$() {
  if ((!$n_jl_Math$)) {
    $n_jl_Math$ = new $c_jl_Math$()
  };
  return $n_jl_Math$
}
/** @constructor */
function $c_jl_System$Streams$() {
  this.jl_System$Streams$__f_out = null;
  this.jl_System$Streams$__f_err = null;
  this.jl_System$Streams$__f_in = null;
  $n_jl_System$Streams$ = this;
  this.jl_System$Streams$__f_out = new $c_jl_JSConsoleBasedPrintStream(false);
  this.jl_System$Streams$__f_err = new $c_jl_JSConsoleBasedPrintStream(true);
  this.jl_System$Streams$__f_in = null
}
$c_jl_System$Streams$.prototype = new $h_O();
$c_jl_System$Streams$.prototype.constructor = $c_jl_System$Streams$;
/** @constructor */
function $h_jl_System$Streams$() {
  /*<skip>*/
}
$h_jl_System$Streams$.prototype = $c_jl_System$Streams$.prototype;
var $d_jl_System$Streams$ = new $TypeData().initClass({
  jl_System$Streams$: 0
}, false, "java.lang.System$Streams$", {
  jl_System$Streams$: 1,
  O: 1
});
$c_jl_System$Streams$.prototype.$classData = $d_jl_System$Streams$;
var $n_jl_System$Streams$;
function $m_jl_System$Streams$() {
  if ((!$n_jl_System$Streams$)) {
    $n_jl_System$Streams$ = new $c_jl_System$Streams$()
  };
  return $n_jl_System$Streams$
}
function $p_jl_System$SystemProperties$__loadSystemProperties__O($thiz) {
  var result = {};
  result["java.version"] = "1.8";
  result["java.vm.specification.version"] = "1.8";
  result["java.vm.specification.vendor"] = "Oracle Corporation";
  result["java.vm.specification.name"] = "Java Virtual Machine Specification";
  result["java.vm.name"] = "Scala.js";
  var value = $as_T($linkingInfo.linkerVersion);
  result["java.vm.version"] = value;
  result["java.specification.version"] = "1.8";
  result["java.specification.vendor"] = "Oracle Corporation";
  result["java.specification.name"] = "Java Platform API Specification";
  result["file.separator"] = "/";
  result["path.separator"] = ":";
  result["line.separator"] = "\n";
  return result
}
/** @constructor */
function $c_jl_System$SystemProperties$() {
  this.jl_System$SystemProperties$__f_dict = null;
  this.jl_System$SystemProperties$__f_properties = null;
  $n_jl_System$SystemProperties$ = this;
  this.jl_System$SystemProperties$__f_dict = $p_jl_System$SystemProperties$__loadSystemProperties__O(this);
  this.jl_System$SystemProperties$__f_properties = null
}
$c_jl_System$SystemProperties$.prototype = new $h_O();
$c_jl_System$SystemProperties$.prototype.constructor = $c_jl_System$SystemProperties$;
/** @constructor */
function $h_jl_System$SystemProperties$() {
  /*<skip>*/
}
$h_jl_System$SystemProperties$.prototype = $c_jl_System$SystemProperties$.prototype;
$c_jl_System$SystemProperties$.prototype.getProperty__T__T__T = (function(key, default$1) {
  return ((this.jl_System$SystemProperties$__f_dict !== null) ? $as_T($m_jl_Utils$().dictGetOrElse__O__T__O__O(this.jl_System$SystemProperties$__f_dict, key, default$1)) : this.jl_System$SystemProperties$__f_properties.getProperty__T__T__T(key, default$1))
});
var $d_jl_System$SystemProperties$ = new $TypeData().initClass({
  jl_System$SystemProperties$: 0
}, false, "java.lang.System$SystemProperties$", {
  jl_System$SystemProperties$: 1,
  O: 1
});
$c_jl_System$SystemProperties$.prototype.$classData = $d_jl_System$SystemProperties$;
var $n_jl_System$SystemProperties$;
function $m_jl_System$SystemProperties$() {
  if ((!$n_jl_System$SystemProperties$)) {
    $n_jl_System$SystemProperties$ = new $c_jl_System$SystemProperties$()
  };
  return $n_jl_System$SystemProperties$
}
/** @constructor */
function $c_jl_Utils$() {
  /*<skip>*/
}
$c_jl_Utils$.prototype = new $h_O();
$c_jl_Utils$.prototype.constructor = $c_jl_Utils$;
/** @constructor */
function $h_jl_Utils$() {
  /*<skip>*/
}
$h_jl_Utils$.prototype = $c_jl_Utils$.prototype;
$c_jl_Utils$.prototype.dictGetOrElse__O__T__O__O = (function(dict, key, default$1) {
  return ($uZ($m_jl_Utils$Cache$().jl_Utils$Cache$__f_safeHasOwnProperty.call(dict, key)) ? dict[key] : default$1)
});
var $d_jl_Utils$ = new $TypeData().initClass({
  jl_Utils$: 0
}, false, "java.lang.Utils$", {
  jl_Utils$: 1,
  O: 1
});
$c_jl_Utils$.prototype.$classData = $d_jl_Utils$;
var $n_jl_Utils$;
function $m_jl_Utils$() {
  if ((!$n_jl_Utils$)) {
    $n_jl_Utils$ = new $c_jl_Utils$()
  };
  return $n_jl_Utils$
}
/** @constructor */
function $c_jl_Utils$Cache$() {
  this.jl_Utils$Cache$__f_safeHasOwnProperty = null;
  $n_jl_Utils$Cache$ = this;
  this.jl_Utils$Cache$__f_safeHasOwnProperty = Object.prototype.hasOwnProperty
}
$c_jl_Utils$Cache$.prototype = new $h_O();
$c_jl_Utils$Cache$.prototype.constructor = $c_jl_Utils$Cache$;
/** @constructor */
function $h_jl_Utils$Cache$() {
  /*<skip>*/
}
$h_jl_Utils$Cache$.prototype = $c_jl_Utils$Cache$.prototype;
var $d_jl_Utils$Cache$ = new $TypeData().initClass({
  jl_Utils$Cache$: 0
}, false, "java.lang.Utils$Cache$", {
  jl_Utils$Cache$: 1,
  O: 1
});
$c_jl_Utils$Cache$.prototype.$classData = $d_jl_Utils$Cache$;
var $n_jl_Utils$Cache$;
function $m_jl_Utils$Cache$() {
  if ((!$n_jl_Utils$Cache$)) {
    $n_jl_Utils$Cache$ = new $c_jl_Utils$Cache$()
  };
  return $n_jl_Utils$Cache$
}
function $f_jl_Void__equals__O__Z($thiz, that) {
  return ($thiz === that)
}
function $f_jl_Void__hashCode__I($thiz) {
  return 0
}
function $f_jl_Void__toString__T($thiz) {
  return "undefined"
}
var $d_jl_Void = new $TypeData().initClass({
  jl_Void: 0
}, false, "java.lang.Void", {
  jl_Void: 1,
  O: 1
}, (void 0), (void 0), ((x) => (x === (void 0))));
/** @constructor */
function $c_jl_reflect_Array$() {
  /*<skip>*/
}
$c_jl_reflect_Array$.prototype = new $h_O();
$c_jl_reflect_Array$.prototype.constructor = $c_jl_reflect_Array$;
/** @constructor */
function $h_jl_reflect_Array$() {
  /*<skip>*/
}
$h_jl_reflect_Array$.prototype = $c_jl_reflect_Array$.prototype;
$c_jl_reflect_Array$.prototype.newInstance__jl_Class__I__O = (function(componentType, length) {
  return componentType.newArrayOfThisClass__O__O([length])
});
$c_jl_reflect_Array$.prototype.getLength__O__I = (function(array) {
  if ((array instanceof $ac_O)) {
    var x2 = $asArrayOf_O(array, 1);
    return x2.u.length
  } else if ((array instanceof $ac_Z)) {
    var x3 = $asArrayOf_Z(array, 1);
    return x3.u.length
  } else if ((array instanceof $ac_C)) {
    var x4 = $asArrayOf_C(array, 1);
    return x4.u.length
  } else if ((array instanceof $ac_B)) {
    var x5 = $asArrayOf_B(array, 1);
    return x5.u.length
  } else if ((array instanceof $ac_S)) {
    var x6 = $asArrayOf_S(array, 1);
    return x6.u.length
  } else if ((array instanceof $ac_I)) {
    var x7 = $asArrayOf_I(array, 1);
    return x7.u.length
  } else if ((array instanceof $ac_J)) {
    var x8 = $asArrayOf_J(array, 1);
    return x8.u.length
  } else if ((array instanceof $ac_F)) {
    var x9 = $asArrayOf_F(array, 1);
    return x9.u.length
  } else if ((array instanceof $ac_D)) {
    var x10 = $asArrayOf_D(array, 1);
    return x10.u.length
  } else {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "argument type mismatch")
  }
});
var $d_jl_reflect_Array$ = new $TypeData().initClass({
  jl_reflect_Array$: 0
}, false, "java.lang.reflect.Array$", {
  jl_reflect_Array$: 1,
  O: 1
});
$c_jl_reflect_Array$.prototype.$classData = $d_jl_reflect_Array$;
var $n_jl_reflect_Array$;
function $m_jl_reflect_Array$() {
  if ((!$n_jl_reflect_Array$)) {
    $n_jl_reflect_Array$ = new $c_jl_reflect_Array$()
  };
  return $n_jl_reflect_Array$
}
/** @constructor */
function $c_Ljava_time_zone_ZoneRulesProvider() {
  /*<skip>*/
}
$c_Ljava_time_zone_ZoneRulesProvider.prototype = new $h_O();
$c_Ljava_time_zone_ZoneRulesProvider.prototype.constructor = $c_Ljava_time_zone_ZoneRulesProvider;
/** @constructor */
function $h_Ljava_time_zone_ZoneRulesProvider() {
  /*<skip>*/
}
$h_Ljava_time_zone_ZoneRulesProvider.prototype = $c_Ljava_time_zone_ZoneRulesProvider.prototype;
/** @constructor */
function $c_ju_Arrays$() {
  /*<skip>*/
}
$c_ju_Arrays$.prototype = new $h_O();
$c_ju_Arrays$.prototype.constructor = $c_ju_Arrays$;
/** @constructor */
function $h_ju_Arrays$() {
  /*<skip>*/
}
$h_ju_Arrays$.prototype = $c_ju_Arrays$.prototype;
$c_ju_Arrays$.prototype.binarySearch__AI__I__I = (function(a, key) {
  var startIndex = 0;
  var endIndex = a.u.length;
  while (true) {
    if ((startIndex === endIndex)) {
      return (((-1) - startIndex) | 0)
    } else {
      var mid = ((((startIndex + endIndex) | 0) >>> 1) | 0);
      var elem = a.get(mid);
      if ((key < elem)) {
        endIndex = mid
      } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, elem)) {
        return mid
      } else {
        startIndex = ((1 + mid) | 0)
      }
    }
  }
});
$c_ju_Arrays$.prototype.equals__AI__AI__Z = (function(a, b) {
  if ((a === b)) {
    return true
  };
  if (((a === null) || (b === null))) {
    return false
  };
  var len = a.u.length;
  if ((b.u.length !== len)) {
    return false
  };
  var i = 0;
  while ((i !== len)) {
    var x = a.get(i);
    var y = b.get(i);
    if ((!$m_sr_BoxesRunTime$().equals__O__O__Z(x, y))) {
      return false
    };
    i = ((1 + i) | 0)
  };
  return true
});
$c_ju_Arrays$.prototype.copyOf__AO__I__AO = (function(original, newLength) {
  var tagT = $m_s_reflect_ClassTag$().apply__jl_Class__s_reflect_ClassTag($objectGetClass(original).getComponentType__jl_Class());
  if ((newLength < 0)) {
    throw new $c_jl_NegativeArraySizeException()
  };
  var b = original.u.length;
  var copyLength = ((newLength < b) ? newLength : b);
  var ret = tagT.newArray__I__O(newLength);
  original.copyTo(0, ret, 0, copyLength);
  return $asArrayOf_O(ret, 1)
});
$c_ju_Arrays$.prototype.copyOfRange__AO__I__I__AO = (function(original, from, to) {
  var evidence$6 = $m_s_reflect_ClassTag$().apply__jl_Class__s_reflect_ClassTag($objectGetClass(original).getComponentType__jl_Class());
  if ((from > to)) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), ((from + " > ") + to))
  };
  var retLength = ((to - from) | 0);
  var b = ((original.u.length - from) | 0);
  var copyLength = ((retLength < b) ? retLength : b);
  var ret = evidence$6.newArray__I__O(retLength);
  original.copyTo(from, ret, 0, copyLength);
  return $asArrayOf_O(ret, 1)
});
var $d_ju_Arrays$ = new $TypeData().initClass({
  ju_Arrays$: 0
}, false, "java.util.Arrays$", {
  ju_Arrays$: 1,
  O: 1
});
$c_ju_Arrays$.prototype.$classData = $d_ju_Arrays$;
var $n_ju_Arrays$;
function $m_ju_Arrays$() {
  if ((!$n_ju_Arrays$)) {
    $n_ju_Arrays$ = new $c_ju_Arrays$()
  };
  return $n_ju_Arrays$
}
function $p_Lorg_scalajs_dom_package$__window$lzycompute__Lorg_scalajs_dom_raw_Window($thiz) {
  if (((33554432 & $thiz.Lorg_scalajs_dom_package$__f_bitmap$0) === 0)) {
    $thiz.Lorg_scalajs_dom_package$__f_window = window;
    $thiz.Lorg_scalajs_dom_package$__f_bitmap$0 = (33554432 | $thiz.Lorg_scalajs_dom_package$__f_bitmap$0)
  };
  return $thiz.Lorg_scalajs_dom_package$__f_window
}
function $p_Lorg_scalajs_dom_package$__document$lzycompute__Lorg_scalajs_dom_raw_HTMLDocument($thiz) {
  if (((67108864 & $thiz.Lorg_scalajs_dom_package$__f_bitmap$0) === 0)) {
    $thiz.Lorg_scalajs_dom_package$__f_document = $thiz.window__Lorg_scalajs_dom_raw_Window().document;
    $thiz.Lorg_scalajs_dom_package$__f_bitmap$0 = (67108864 | $thiz.Lorg_scalajs_dom_package$__f_bitmap$0)
  };
  return $thiz.Lorg_scalajs_dom_package$__f_document
}
/** @constructor */
function $c_Lorg_scalajs_dom_package$() {
  this.Lorg_scalajs_dom_package$__f_ApplicationCache = null;
  this.Lorg_scalajs_dom_package$__f_Blob = null;
  this.Lorg_scalajs_dom_package$__f_BlobPropertyBag = null;
  this.Lorg_scalajs_dom_package$__f_DOMException = null;
  this.Lorg_scalajs_dom_package$__f_Event = null;
  this.Lorg_scalajs_dom_package$__f_EventException = null;
  this.Lorg_scalajs_dom_package$__f_EventSource = null;
  this.Lorg_scalajs_dom_package$__f_FileReader = null;
  this.Lorg_scalajs_dom_package$__f_FormData = null;
  this.Lorg_scalajs_dom_package$__f_KeyboardEvent = null;
  this.Lorg_scalajs_dom_package$__f_MediaError = null;
  this.Lorg_scalajs_dom_package$__f_MutationObserverInit = null;
  this.Lorg_scalajs_dom_package$__f_Node = null;
  this.Lorg_scalajs_dom_package$__f_NodeFilter = null;
  this.Lorg_scalajs_dom_package$__f_PerformanceNavigation = null;
  this.Lorg_scalajs_dom_package$__f_PositionError = null;
  this.Lorg_scalajs_dom_package$__f_Range = null;
  this.Lorg_scalajs_dom_package$__f_TextEvent = null;
  this.Lorg_scalajs_dom_package$__f_TextTrack = null;
  this.Lorg_scalajs_dom_package$__f_URL = null;
  this.Lorg_scalajs_dom_package$__f_VisibilityState = null;
  this.Lorg_scalajs_dom_package$__f_WebSocket = null;
  this.Lorg_scalajs_dom_package$__f_WheelEvent = null;
  this.Lorg_scalajs_dom_package$__f_XMLHttpRequest = null;
  this.Lorg_scalajs_dom_package$__f_XPathResult = null;
  this.Lorg_scalajs_dom_package$__f_window = null;
  this.Lorg_scalajs_dom_package$__f_document = null;
  this.Lorg_scalajs_dom_package$__f_console = null;
  this.Lorg_scalajs_dom_package$__f_bitmap$0 = 0
}
$c_Lorg_scalajs_dom_package$.prototype = new $h_O();
$c_Lorg_scalajs_dom_package$.prototype.constructor = $c_Lorg_scalajs_dom_package$;
/** @constructor */
function $h_Lorg_scalajs_dom_package$() {
  /*<skip>*/
}
$h_Lorg_scalajs_dom_package$.prototype = $c_Lorg_scalajs_dom_package$.prototype;
$c_Lorg_scalajs_dom_package$.prototype.window__Lorg_scalajs_dom_raw_Window = (function() {
  return (((33554432 & this.Lorg_scalajs_dom_package$__f_bitmap$0) === 0) ? $p_Lorg_scalajs_dom_package$__window$lzycompute__Lorg_scalajs_dom_raw_Window(this) : this.Lorg_scalajs_dom_package$__f_window)
});
$c_Lorg_scalajs_dom_package$.prototype.document__Lorg_scalajs_dom_raw_HTMLDocument = (function() {
  return (((67108864 & this.Lorg_scalajs_dom_package$__f_bitmap$0) === 0) ? $p_Lorg_scalajs_dom_package$__document$lzycompute__Lorg_scalajs_dom_raw_HTMLDocument(this) : this.Lorg_scalajs_dom_package$__f_document)
});
var $d_Lorg_scalajs_dom_package$ = new $TypeData().initClass({
  Lorg_scalajs_dom_package$: 0
}, false, "org.scalajs.dom.package$", {
  Lorg_scalajs_dom_package$: 1,
  O: 1
});
$c_Lorg_scalajs_dom_package$.prototype.$classData = $d_Lorg_scalajs_dom_package$;
var $n_Lorg_scalajs_dom_package$;
function $m_Lorg_scalajs_dom_package$() {
  if ((!$n_Lorg_scalajs_dom_package$)) {
    $n_Lorg_scalajs_dom_package$ = new $c_Lorg_scalajs_dom_package$()
  };
  return $n_Lorg_scalajs_dom_package$
}
/** @constructor */
function $c_s_Array$EmptyArrays$() {
  this.s_Array$EmptyArrays$__f_emptyBooleanArray = null;
  this.s_Array$EmptyArrays$__f_emptyByteArray = null;
  this.s_Array$EmptyArrays$__f_emptyCharArray = null;
  this.s_Array$EmptyArrays$__f_emptyDoubleArray = null;
  this.s_Array$EmptyArrays$__f_emptyFloatArray = null;
  this.s_Array$EmptyArrays$__f_emptyIntArray = null;
  this.s_Array$EmptyArrays$__f_emptyLongArray = null;
  this.s_Array$EmptyArrays$__f_emptyShortArray = null;
  this.s_Array$EmptyArrays$__f_emptyObjectArray = null;
  $n_s_Array$EmptyArrays$ = this;
  this.s_Array$EmptyArrays$__f_emptyBooleanArray = new $ac_Z(0);
  this.s_Array$EmptyArrays$__f_emptyByteArray = new $ac_B(0);
  this.s_Array$EmptyArrays$__f_emptyCharArray = new $ac_C(0);
  this.s_Array$EmptyArrays$__f_emptyDoubleArray = new $ac_D(0);
  this.s_Array$EmptyArrays$__f_emptyFloatArray = new $ac_F(0);
  this.s_Array$EmptyArrays$__f_emptyIntArray = new $ac_I(0);
  this.s_Array$EmptyArrays$__f_emptyLongArray = new $ac_J(0);
  this.s_Array$EmptyArrays$__f_emptyShortArray = new $ac_S(0);
  this.s_Array$EmptyArrays$__f_emptyObjectArray = new $ac_O(0)
}
$c_s_Array$EmptyArrays$.prototype = new $h_O();
$c_s_Array$EmptyArrays$.prototype.constructor = $c_s_Array$EmptyArrays$;
/** @constructor */
function $h_s_Array$EmptyArrays$() {
  /*<skip>*/
}
$h_s_Array$EmptyArrays$.prototype = $c_s_Array$EmptyArrays$.prototype;
var $d_s_Array$EmptyArrays$ = new $TypeData().initClass({
  s_Array$EmptyArrays$: 0
}, false, "scala.Array$EmptyArrays$", {
  s_Array$EmptyArrays$: 1,
  O: 1
});
$c_s_Array$EmptyArrays$.prototype.$classData = $d_s_Array$EmptyArrays$;
var $n_s_Array$EmptyArrays$;
function $m_s_Array$EmptyArrays$() {
  if ((!$n_s_Array$EmptyArrays$)) {
    $n_s_Array$EmptyArrays$ = new $c_s_Array$EmptyArrays$()
  };
  return $n_s_Array$EmptyArrays$
}
/** @constructor */
function $c_s_LowPriorityImplicits2() {
  /*<skip>*/
}
$c_s_LowPriorityImplicits2.prototype = new $h_O();
$c_s_LowPriorityImplicits2.prototype.constructor = $c_s_LowPriorityImplicits2;
/** @constructor */
function $h_s_LowPriorityImplicits2() {
  /*<skip>*/
}
$h_s_LowPriorityImplicits2.prototype = $c_s_LowPriorityImplicits2.prototype;
/** @constructor */
function $c_sc_Hashing$() {
  /*<skip>*/
}
$c_sc_Hashing$.prototype = new $h_O();
$c_sc_Hashing$.prototype.constructor = $c_sc_Hashing$;
/** @constructor */
function $h_sc_Hashing$() {
  /*<skip>*/
}
$h_sc_Hashing$.prototype = $c_sc_Hashing$.prototype;
$c_sc_Hashing$.prototype.improve__I__I = (function(hcode) {
  var h = ((hcode + (~(hcode << 9))) | 0);
  h = (h ^ ((h >>> 14) | 0));
  h = ((h + (h << 4)) | 0);
  return (h ^ ((h >>> 10) | 0))
});
var $d_sc_Hashing$ = new $TypeData().initClass({
  sc_Hashing$: 0
}, false, "scala.collection.Hashing$", {
  sc_Hashing$: 1,
  O: 1
});
$c_sc_Hashing$.prototype.$classData = $d_sc_Hashing$;
var $n_sc_Hashing$;
function $m_sc_Hashing$() {
  if ((!$n_sc_Hashing$)) {
    $n_sc_Hashing$ = new $c_sc_Hashing$()
  };
  return $n_sc_Hashing$
}
function $is_sc_IterableOnce(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_IterableOnce)))
}
function $as_sc_IterableOnce(obj) {
  return (($is_sc_IterableOnce(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.IterableOnce"))
}
function $isArrayOf_sc_IterableOnce(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_IterableOnce)))
}
function $asArrayOf_sc_IterableOnce(obj, depth) {
  return (($isArrayOf_sc_IterableOnce(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.IterableOnce;", depth))
}
function $f_sc_IterableOnceOps__foreach__F1__V($thiz, f) {
  var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
  while (it.hasNext__Z()) {
    f.apply__O__O(it.next__O())
  }
}
function $f_sc_IterableOnceOps__forall__F1__Z($thiz, p) {
  var res = true;
  var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
  while ((res && it.hasNext__Z())) {
    res = $uZ(p.apply__O__O(it.next__O()))
  };
  return res
}
function $f_sc_IterableOnceOps__exists__F1__Z($thiz, p) {
  var res = false;
  var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
  while (((!res) && it.hasNext__Z())) {
    res = $uZ(p.apply__O__O(it.next__O()))
  };
  return res
}
function $f_sc_IterableOnceOps__isEmpty__Z($thiz) {
  return (!$as_sc_IterableOnce($thiz).iterator__sc_Iterator().hasNext__Z())
}
function $f_sc_IterableOnceOps__size__I($thiz) {
  if (($as_sc_IterableOnce($thiz).knownSize__I() >= 0)) {
    return $as_sc_IterableOnce($thiz).knownSize__I()
  } else {
    var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
    var len = 0;
    while (it.hasNext__Z()) {
      len = ((1 + len) | 0);
      it.next__O()
    };
    return len
  }
}
function $f_sc_IterableOnceOps__copyToArray__O__I__I__I($thiz, xs, start, len) {
  var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
  var i = start;
  var y = (($m_sr_ScalaRunTime$().array_length__O__I(xs) - start) | 0);
  var end = ((start + ((len < y) ? len : y)) | 0);
  while (((i < end) && it.hasNext__Z())) {
    $m_sr_ScalaRunTime$().array_update__O__I__O__V(xs, i, it.next__O());
    i = ((1 + i) | 0)
  };
  return ((i - start) | 0)
}
function $f_sc_IterableOnceOps__mkString__T__T__T__T($thiz, start, sep, end) {
  if ($thiz.isEmpty__Z()) {
    return (("" + start) + end)
  } else {
    var this$1 = $thiz.addString__scm_StringBuilder__T__T__T__scm_StringBuilder($ct_scm_StringBuilder__(new $c_scm_StringBuilder()), start, sep, end);
    return this$1.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
  }
}
function $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder($thiz, b, start, sep, end) {
  var jsb = b.scm_StringBuilder__f_underlying;
  if (($uI(start.length) !== 0)) {
    jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content) + start)
  };
  var it = $as_sc_IterableOnce($thiz).iterator__sc_Iterator();
  if (it.hasNext__Z()) {
    var obj = it.next__O();
    jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj);
    while (it.hasNext__Z()) {
      jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
      var obj$1 = it.next__O();
      jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$1)
    }
  };
  if (($uI(end.length) !== 0)) {
    jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + jsb.jl_StringBuilder__f_java$lang$StringBuilder$$content) + end)
  };
  return b
}
function $p_sc_LinearSeqIterator$LazyCell__v$lzycompute__sc_LinearSeqOps($thiz) {
  if ((!$thiz.sc_LinearSeqIterator$LazyCell__f_bitmap$0)) {
    $thiz.sc_LinearSeqIterator$LazyCell__f_v = $as_sc_LinearSeqOps($thiz.sc_LinearSeqIterator$LazyCell__f_st.apply__O());
    $thiz.sc_LinearSeqIterator$LazyCell__f_bitmap$0 = true
  };
  $thiz.sc_LinearSeqIterator$LazyCell__f_st = null;
  return $thiz.sc_LinearSeqIterator$LazyCell__f_v
}
/** @constructor */
function $c_sc_LinearSeqIterator$LazyCell(outer, st) {
  this.sc_LinearSeqIterator$LazyCell__f_v = null;
  this.sc_LinearSeqIterator$LazyCell__f_st = null;
  this.sc_LinearSeqIterator$LazyCell__f_bitmap$0 = false;
  this.sc_LinearSeqIterator$LazyCell__f_st = st
}
$c_sc_LinearSeqIterator$LazyCell.prototype = new $h_O();
$c_sc_LinearSeqIterator$LazyCell.prototype.constructor = $c_sc_LinearSeqIterator$LazyCell;
/** @constructor */
function $h_sc_LinearSeqIterator$LazyCell() {
  /*<skip>*/
}
$h_sc_LinearSeqIterator$LazyCell.prototype = $c_sc_LinearSeqIterator$LazyCell.prototype;
$c_sc_LinearSeqIterator$LazyCell.prototype.v__sc_LinearSeqOps = (function() {
  return ((!this.sc_LinearSeqIterator$LazyCell__f_bitmap$0) ? $p_sc_LinearSeqIterator$LazyCell__v$lzycompute__sc_LinearSeqOps(this) : this.sc_LinearSeqIterator$LazyCell__f_v)
});
var $d_sc_LinearSeqIterator$LazyCell = new $TypeData().initClass({
  sc_LinearSeqIterator$LazyCell: 0
}, false, "scala.collection.LinearSeqIterator$LazyCell", {
  sc_LinearSeqIterator$LazyCell: 1,
  O: 1
});
$c_sc_LinearSeqIterator$LazyCell.prototype.$classData = $d_sc_LinearSeqIterator$LazyCell;
function $p_sci_ChampBaseIterator__initNodes__V($thiz) {
  if (($thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths === null)) {
    $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths = new $ac_I(($m_sci_Node$().sci_Node$__f_MaxDepth << 1));
    $thiz.sci_ChampBaseIterator__f_nodes = new ($d_sci_Node.getArrayOf().constr)($m_sci_Node$().sci_Node$__f_MaxDepth)
  }
}
function $p_sci_ChampBaseIterator__setupPayloadNode__sci_Node__V($thiz, node) {
  $thiz.sci_ChampBaseIterator__f_currentValueNode = node;
  $thiz.sci_ChampBaseIterator__f_currentValueCursor = 0;
  $thiz.sci_ChampBaseIterator__f_currentValueLength = node.payloadArity__I()
}
function $p_sci_ChampBaseIterator__pushNode__sci_Node__V($thiz, node) {
  $p_sci_ChampBaseIterator__initNodes__V($thiz);
  $thiz.sci_ChampBaseIterator__f_currentStackLevel = ((1 + $thiz.sci_ChampBaseIterator__f_currentStackLevel) | 0);
  var cursorIndex = ($thiz.sci_ChampBaseIterator__f_currentStackLevel << 1);
  var lengthIndex = ((1 + ($thiz.sci_ChampBaseIterator__f_currentStackLevel << 1)) | 0);
  $thiz.sci_ChampBaseIterator__f_nodes.set($thiz.sci_ChampBaseIterator__f_currentStackLevel, node);
  $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths.set(cursorIndex, 0);
  $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths.set(lengthIndex, node.nodeArity__I())
}
function $p_sci_ChampBaseIterator__popNode__V($thiz) {
  $thiz.sci_ChampBaseIterator__f_currentStackLevel = (((-1) + $thiz.sci_ChampBaseIterator__f_currentStackLevel) | 0)
}
function $p_sci_ChampBaseIterator__searchNextValueNode__Z($thiz) {
  while (($thiz.sci_ChampBaseIterator__f_currentStackLevel >= 0)) {
    var cursorIndex = ($thiz.sci_ChampBaseIterator__f_currentStackLevel << 1);
    var lengthIndex = ((1 + ($thiz.sci_ChampBaseIterator__f_currentStackLevel << 1)) | 0);
    var nodeCursor = $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths.get(cursorIndex);
    var nodeLength = $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths.get(lengthIndex);
    if ((nodeCursor < nodeLength)) {
      var ev$1 = $thiz.sci_ChampBaseIterator__f_nodeCursorsAndLengths;
      ev$1.set(cursorIndex, ((1 + ev$1.get(cursorIndex)) | 0));
      var nextNode = $thiz.sci_ChampBaseIterator__f_nodes.get($thiz.sci_ChampBaseIterator__f_currentStackLevel).getNode__I__sci_Node(nodeCursor);
      if (nextNode.hasNodes__Z()) {
        $p_sci_ChampBaseIterator__pushNode__sci_Node__V($thiz, nextNode)
      };
      if (nextNode.hasPayload__Z()) {
        $p_sci_ChampBaseIterator__setupPayloadNode__sci_Node__V($thiz, nextNode);
        return true
      }
    } else {
      $p_sci_ChampBaseIterator__popNode__V($thiz)
    }
  };
  return false
}
function $ct_sci_ChampBaseIterator__($thiz) {
  $thiz.sci_ChampBaseIterator__f_currentValueCursor = 0;
  $thiz.sci_ChampBaseIterator__f_currentValueLength = 0;
  $thiz.sci_ChampBaseIterator__f_currentStackLevel = (-1);
  return $thiz
}
function $ct_sci_ChampBaseIterator__sci_Node__($thiz, rootNode) {
  $ct_sci_ChampBaseIterator__($thiz);
  if (rootNode.hasNodes__Z()) {
    $p_sci_ChampBaseIterator__pushNode__sci_Node__V($thiz, rootNode)
  };
  if (rootNode.hasPayload__Z()) {
    $p_sci_ChampBaseIterator__setupPayloadNode__sci_Node__V($thiz, rootNode)
  };
  return $thiz
}
/** @constructor */
function $c_sci_ChampBaseIterator() {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null
}
$c_sci_ChampBaseIterator.prototype = new $h_O();
$c_sci_ChampBaseIterator.prototype.constructor = $c_sci_ChampBaseIterator;
/** @constructor */
function $h_sci_ChampBaseIterator() {
  /*<skip>*/
}
$h_sci_ChampBaseIterator.prototype = $c_sci_ChampBaseIterator.prototype;
$c_sci_ChampBaseIterator.prototype.hasNext__Z = (function() {
  return ((this.sci_ChampBaseIterator__f_currentValueCursor < this.sci_ChampBaseIterator__f_currentValueLength) || $p_sci_ChampBaseIterator__searchNextValueNode__Z(this))
});
function $p_sci_ChampBaseReverseIterator__setupPayloadNode__sci_Node__V($thiz, node) {
  $thiz.sci_ChampBaseReverseIterator__f_currentValueNode = node;
  $thiz.sci_ChampBaseReverseIterator__f_currentValueCursor = (((-1) + node.payloadArity__I()) | 0)
}
function $p_sci_ChampBaseReverseIterator__pushNode__sci_Node__V($thiz, node) {
  $thiz.sci_ChampBaseReverseIterator__f_currentStackLevel = ((1 + $thiz.sci_ChampBaseReverseIterator__f_currentStackLevel) | 0);
  $thiz.sci_ChampBaseReverseIterator__f_nodeStack.set($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel, node);
  $thiz.sci_ChampBaseReverseIterator__f_nodeIndex.set($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel, (((-1) + node.nodeArity__I()) | 0))
}
function $p_sci_ChampBaseReverseIterator__popNode__V($thiz) {
  $thiz.sci_ChampBaseReverseIterator__f_currentStackLevel = (((-1) + $thiz.sci_ChampBaseReverseIterator__f_currentStackLevel) | 0)
}
function $p_sci_ChampBaseReverseIterator__searchNextValueNode__Z($thiz) {
  while (($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel >= 0)) {
    var nodeCursor = $thiz.sci_ChampBaseReverseIterator__f_nodeIndex.get($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel);
    $thiz.sci_ChampBaseReverseIterator__f_nodeIndex.set($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel, (((-1) + nodeCursor) | 0));
    if ((nodeCursor >= 0)) {
      var nextNode = $thiz.sci_ChampBaseReverseIterator__f_nodeStack.get($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel).getNode__I__sci_Node(nodeCursor);
      $p_sci_ChampBaseReverseIterator__pushNode__sci_Node__V($thiz, nextNode)
    } else {
      var currNode = $thiz.sci_ChampBaseReverseIterator__f_nodeStack.get($thiz.sci_ChampBaseReverseIterator__f_currentStackLevel);
      $p_sci_ChampBaseReverseIterator__popNode__V($thiz);
      if (currNode.hasPayload__Z()) {
        $p_sci_ChampBaseReverseIterator__setupPayloadNode__sci_Node__V($thiz, currNode);
        return true
      }
    }
  };
  return false
}
function $ct_sci_ChampBaseReverseIterator__($thiz) {
  $thiz.sci_ChampBaseReverseIterator__f_currentValueCursor = (-1);
  $thiz.sci_ChampBaseReverseIterator__f_currentStackLevel = (-1);
  $thiz.sci_ChampBaseReverseIterator__f_nodeIndex = new $ac_I(((1 + $m_sci_Node$().sci_Node$__f_MaxDepth) | 0));
  $thiz.sci_ChampBaseReverseIterator__f_nodeStack = new ($d_sci_Node.getArrayOf().constr)(((1 + $m_sci_Node$().sci_Node$__f_MaxDepth) | 0));
  return $thiz
}
function $ct_sci_ChampBaseReverseIterator__sci_Node__($thiz, rootNode) {
  $ct_sci_ChampBaseReverseIterator__($thiz);
  $p_sci_ChampBaseReverseIterator__pushNode__sci_Node__V($thiz, rootNode);
  $p_sci_ChampBaseReverseIterator__searchNextValueNode__Z($thiz);
  return $thiz
}
/** @constructor */
function $c_sci_ChampBaseReverseIterator() {
  this.sci_ChampBaseReverseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseReverseIterator__f_currentValueNode = null;
  this.sci_ChampBaseReverseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseReverseIterator__f_nodeIndex = null;
  this.sci_ChampBaseReverseIterator__f_nodeStack = null
}
$c_sci_ChampBaseReverseIterator.prototype = new $h_O();
$c_sci_ChampBaseReverseIterator.prototype.constructor = $c_sci_ChampBaseReverseIterator;
/** @constructor */
function $h_sci_ChampBaseReverseIterator() {
  /*<skip>*/
}
$h_sci_ChampBaseReverseIterator.prototype = $c_sci_ChampBaseReverseIterator.prototype;
$c_sci_ChampBaseReverseIterator.prototype.hasNext__Z = (function() {
  return ((this.sci_ChampBaseReverseIterator__f_currentValueCursor >= 0) || $p_sci_ChampBaseReverseIterator__searchNextValueNode__Z(this))
});
function $p_sci_IndexedSeqDefaults$__liftedTree1$1__I($thiz) {
  try {
    var x = $m_jl_System$SystemProperties$().getProperty__T__T__T("scala.collection.immutable.IndexedSeq.defaultApplyPreferredMaxLength", "64");
    var this$4 = $m_jl_Integer$();
    return this$4.parseInt__T__I__I(x, 10)
  } catch (e) {
    if (false) {
      return 64
    } else {
      throw e
    }
  }
}
/** @constructor */
function $c_sci_IndexedSeqDefaults$() {
  this.sci_IndexedSeqDefaults$__f_defaultApplyPreferredMaxLength = 0;
  $n_sci_IndexedSeqDefaults$ = this;
  this.sci_IndexedSeqDefaults$__f_defaultApplyPreferredMaxLength = $p_sci_IndexedSeqDefaults$__liftedTree1$1__I(this)
}
$c_sci_IndexedSeqDefaults$.prototype = new $h_O();
$c_sci_IndexedSeqDefaults$.prototype.constructor = $c_sci_IndexedSeqDefaults$;
/** @constructor */
function $h_sci_IndexedSeqDefaults$() {
  /*<skip>*/
}
$h_sci_IndexedSeqDefaults$.prototype = $c_sci_IndexedSeqDefaults$.prototype;
var $d_sci_IndexedSeqDefaults$ = new $TypeData().initClass({
  sci_IndexedSeqDefaults$: 0
}, false, "scala.collection.immutable.IndexedSeqDefaults$", {
  sci_IndexedSeqDefaults$: 1,
  O: 1
});
$c_sci_IndexedSeqDefaults$.prototype.$classData = $d_sci_IndexedSeqDefaults$;
var $n_sci_IndexedSeqDefaults$;
function $m_sci_IndexedSeqDefaults$() {
  if ((!$n_sci_IndexedSeqDefaults$)) {
    $n_sci_IndexedSeqDefaults$ = new $c_sci_IndexedSeqDefaults$()
  };
  return $n_sci_IndexedSeqDefaults$
}
/** @constructor */
function $c_sci_MapNode$() {
  this.sci_MapNode$__f_EmptyMapNode = null;
  $n_sci_MapNode$ = this;
  this.sci_MapNode$__f_EmptyMapNode = new $c_sci_BitmapIndexedMapNode(0, 0, ($m_s_reflect_ManifestFactory$AnyManifest$(), new $ac_O(0)), ($m_s_reflect_ManifestFactory$IntManifest$(), new $ac_I(0)), 0, 0)
}
$c_sci_MapNode$.prototype = new $h_O();
$c_sci_MapNode$.prototype.constructor = $c_sci_MapNode$;
/** @constructor */
function $h_sci_MapNode$() {
  /*<skip>*/
}
$h_sci_MapNode$.prototype = $c_sci_MapNode$.prototype;
var $d_sci_MapNode$ = new $TypeData().initClass({
  sci_MapNode$: 0
}, false, "scala.collection.immutable.MapNode$", {
  sci_MapNode$: 1,
  O: 1
});
$c_sci_MapNode$.prototype.$classData = $d_sci_MapNode$;
var $n_sci_MapNode$;
function $m_sci_MapNode$() {
  if ((!$n_sci_MapNode$)) {
    $n_sci_MapNode$ = new $c_sci_MapNode$()
  };
  return $n_sci_MapNode$
}
function $p_sci_Node__arrayIndexOutOfBounds__O__I__jl_ArrayIndexOutOfBoundsException($thiz, as, ix) {
  return $ct_jl_ArrayIndexOutOfBoundsException__T__(new $c_jl_ArrayIndexOutOfBoundsException(), ((ix + " is out of bounds (min 0, max ") + (((-1) + $m_sr_ScalaRunTime$().array_length__O__I(as)) | 0)))
}
/** @constructor */
function $c_sci_Node() {
  /*<skip>*/
}
$c_sci_Node.prototype = new $h_O();
$c_sci_Node.prototype.constructor = $c_sci_Node;
/** @constructor */
function $h_sci_Node() {
  /*<skip>*/
}
$h_sci_Node.prototype = $c_sci_Node.prototype;
$c_sci_Node.prototype.removeElement__AI__I__AI = (function(as, ix) {
  if ((ix < 0)) {
    throw $p_sci_Node__arrayIndexOutOfBounds__O__I__jl_ArrayIndexOutOfBoundsException(this, as, ix)
  };
  if ((ix > (((-1) + as.u.length) | 0))) {
    throw $p_sci_Node__arrayIndexOutOfBounds__O__I__jl_ArrayIndexOutOfBoundsException(this, as, ix)
  };
  var result = new $ac_I((((-1) + as.u.length) | 0));
  as.copyTo(0, result, 0, ix);
  var srcPos = ((1 + ix) | 0);
  var length = (((-1) + ((as.u.length - ix) | 0)) | 0);
  as.copyTo(srcPos, result, ix, length);
  return result
});
$c_sci_Node.prototype.insertElement__AI__I__I__AI = (function(as, ix, elem) {
  if ((ix < 0)) {
    throw $p_sci_Node__arrayIndexOutOfBounds__O__I__jl_ArrayIndexOutOfBoundsException(this, as, ix)
  };
  if ((ix > as.u.length)) {
    throw $p_sci_Node__arrayIndexOutOfBounds__O__I__jl_ArrayIndexOutOfBoundsException(this, as, ix)
  };
  var result = new $ac_I(((1 + as.u.length) | 0));
  as.copyTo(0, result, 0, ix);
  result.set(ix, elem);
  var destPos = ((1 + ix) | 0);
  var length = ((as.u.length - ix) | 0);
  as.copyTo(ix, result, destPos, length);
  return result
});
var $d_sci_Node = new $TypeData().initClass({
  sci_Node: 0
}, false, "scala.collection.immutable.Node", {
  sci_Node: 1,
  O: 1
});
$c_sci_Node.prototype.$classData = $d_sci_Node;
/** @constructor */
function $c_sci_Node$() {
  this.sci_Node$__f_MaxDepth = 0;
  $n_sci_Node$ = this;
  this.sci_Node$__f_MaxDepth = $doubleToInt($uD(Math.ceil(6.4)))
}
$c_sci_Node$.prototype = new $h_O();
$c_sci_Node$.prototype.constructor = $c_sci_Node$;
/** @constructor */
function $h_sci_Node$() {
  /*<skip>*/
}
$h_sci_Node$.prototype = $c_sci_Node$.prototype;
$c_sci_Node$.prototype.maskFrom__I__I__I = (function(hash, shift) {
  return (31 & ((hash >>> shift) | 0))
});
$c_sci_Node$.prototype.bitposFrom__I__I = (function(mask) {
  return (1 << mask)
});
$c_sci_Node$.prototype.indexFrom__I__I__I = (function(bitmap, bitpos) {
  var i = (bitmap & (((-1) + bitpos) | 0));
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_Node$.prototype.indexFrom__I__I__I__I = (function(bitmap, mask, bitpos) {
  return ((bitmap === (-1)) ? mask : this.indexFrom__I__I__I(bitmap, bitpos))
});
var $d_sci_Node$ = new $TypeData().initClass({
  sci_Node$: 0
}, false, "scala.collection.immutable.Node$", {
  sci_Node$: 1,
  O: 1
});
$c_sci_Node$.prototype.$classData = $d_sci_Node$;
var $n_sci_Node$;
function $m_sci_Node$() {
  if ((!$n_sci_Node$)) {
    $n_sci_Node$ = new $c_sci_Node$()
  };
  return $n_sci_Node$
}
/** @constructor */
function $c_sci_SetNode$() {
  this.sci_SetNode$__f_EmptySetNode = null;
  $n_sci_SetNode$ = this;
  this.sci_SetNode$__f_EmptySetNode = new $c_sci_BitmapIndexedSetNode(0, 0, ($m_s_reflect_ManifestFactory$AnyManifest$(), new $ac_O(0)), ($m_s_reflect_ManifestFactory$IntManifest$(), new $ac_I(0)), 0, 0)
}
$c_sci_SetNode$.prototype = new $h_O();
$c_sci_SetNode$.prototype.constructor = $c_sci_SetNode$;
/** @constructor */
function $h_sci_SetNode$() {
  /*<skip>*/
}
$h_sci_SetNode$.prototype = $c_sci_SetNode$.prototype;
var $d_sci_SetNode$ = new $TypeData().initClass({
  sci_SetNode$: 0
}, false, "scala.collection.immutable.SetNode$", {
  sci_SetNode$: 1,
  O: 1
});
$c_sci_SetNode$.prototype.$classData = $d_sci_SetNode$;
var $n_sci_SetNode$;
function $m_sci_SetNode$() {
  if ((!$n_sci_SetNode$)) {
    $n_sci_SetNode$ = new $c_sci_SetNode$()
  };
  return $n_sci_SetNode$
}
function $p_sci_VectorSliceBuilder__addSlice__I__AO__I__I__V($thiz, n, a, lo, hi) {
  while (true) {
    if ((n === 1)) {
      var a$1 = a;
      var start = lo;
      var end = hi;
      $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, 1, (((start === 0) && (end === a$1.u.length)) ? a$1 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$1, start, end)))
    } else {
      var bitsN = $imul(5, (((-1) + n) | 0));
      var widthN = (1 << bitsN);
      var loN = ((lo >>> bitsN) | 0);
      var hiN = ((hi >>> bitsN) | 0);
      var loRest = (lo & (((-1) + widthN) | 0));
      var hiRest = (hi & (((-1) + widthN) | 0));
      if ((loRest === 0)) {
        if ((hiRest === 0)) {
          var $$x1 = n;
          var a$2 = a;
          $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, $$x1, (((loN === 0) && (hiN === a$2.u.length)) ? a$2 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$2, loN, hiN)))
        } else {
          if ((hiN > loN)) {
            var $$x2 = n;
            var a$3 = a;
            $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, $$x2, (((loN === 0) && (hiN === a$3.u.length)) ? a$3 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$3, loN, hiN)))
          };
          var temp$n = (((-1) + n) | 0);
          var temp$a = $asArrayOf_O(a.get(hiN), 1);
          n = temp$n;
          a = temp$a;
          lo = 0;
          hi = hiRest;
          continue
        }
      } else if ((hiN === loN)) {
        var temp$n$2 = (((-1) + n) | 0);
        var temp$a$2 = $asArrayOf_O(a.get(loN), 1);
        n = temp$n$2;
        a = temp$a$2;
        lo = loRest;
        hi = hiRest;
        continue
      } else {
        $p_sci_VectorSliceBuilder__addSlice__I__AO__I__I__V($thiz, (((-1) + n) | 0), $asArrayOf_O(a.get(loN), 1), loRest, widthN);
        if ((hiRest === 0)) {
          if ((hiN > ((1 + loN) | 0))) {
            var $$x3 = n;
            var a$4 = a;
            var start$1 = ((1 + loN) | 0);
            $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, $$x3, (((start$1 === 0) && (hiN === a$4.u.length)) ? a$4 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$4, start$1, hiN)))
          }
        } else {
          if ((hiN > ((1 + loN) | 0))) {
            var $$x4 = n;
            var a$5 = a;
            var start$2 = ((1 + loN) | 0);
            $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, $$x4, (((start$2 === 0) && (hiN === a$5.u.length)) ? a$5 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$5, start$2, hiN)))
          };
          var temp$n$3 = (((-1) + n) | 0);
          var temp$a$3 = $asArrayOf_O(a.get(hiN), 1);
          n = temp$n$3;
          a = temp$a$3;
          lo = 0;
          hi = hiRest;
          continue
        }
      }
    };
    return (void 0)
  }
}
function $p_sci_VectorSliceBuilder__add__I__AO__V($thiz, n, a) {
  if ((n <= $thiz.sci_VectorSliceBuilder__f_maxDim)) {
    var idx = ((11 - n) | 0)
  } else {
    $thiz.sci_VectorSliceBuilder__f_maxDim = n;
    var idx = (((-1) + n) | 0)
  };
  $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set(idx, a)
}
function $p_sci_VectorSliceBuilder__balancePrefix__I__V($thiz, n) {
  if (($thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get((((-1) + n) | 0)) === null)) {
    if ((n === $thiz.sci_VectorSliceBuilder__f_maxDim)) {
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set((((-1) + n) | 0), $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(((11 - n) | 0)));
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set(((11 - n) | 0), null)
    } else {
      $p_sci_VectorSliceBuilder__balancePrefix__I__V($thiz, ((1 + n) | 0));
      var $$x1 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
      var n$1 = ((1 + n) | 0);
      var preN1 = $asArrayOf_O($$x1.get((((-1) + n$1) | 0)), 2);
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set((((-1) + n) | 0), preN1.get(0));
      if ((preN1.u.length === 1)) {
        var $$x2 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
        var n$2 = ((1 + n) | 0);
        $$x2.set((((-1) + n$2) | 0), null);
        if (($thiz.sci_VectorSliceBuilder__f_maxDim === ((1 + n) | 0))) {
          var $$x4 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
          var n$3 = ((1 + n) | 0);
          var $$x3 = ($$x4.get(((11 - n$3) | 0)) === null)
        } else {
          var $$x3 = false
        };
        if ($$x3) {
          $thiz.sci_VectorSliceBuilder__f_maxDim = n
        }
      } else {
        var $$x5 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
        var n$4 = ((1 + n) | 0);
        var to = preN1.u.length;
        $$x5.set((((-1) + n$4) | 0), $m_ju_Arrays$().copyOfRange__AO__I__I__AO(preN1, 1, to))
      }
    }
  }
}
function $p_sci_VectorSliceBuilder__balanceSuffix__I__V($thiz, n) {
  if (($thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(((11 - n) | 0)) === null)) {
    if ((n === $thiz.sci_VectorSliceBuilder__f_maxDim)) {
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set(((11 - n) | 0), $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get((((-1) + n) | 0)));
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set((((-1) + n) | 0), null)
    } else {
      $p_sci_VectorSliceBuilder__balanceSuffix__I__V($thiz, ((1 + n) | 0));
      var $$x1 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
      var n$1 = ((1 + n) | 0);
      var sufN1 = $asArrayOf_O($$x1.get(((11 - n$1) | 0)), 2);
      $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.set(((11 - n) | 0), sufN1.get((((-1) + sufN1.u.length) | 0)));
      if ((sufN1.u.length === 1)) {
        var $$x2 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
        var n$2 = ((1 + n) | 0);
        $$x2.set(((11 - n$2) | 0), null);
        if (($thiz.sci_VectorSliceBuilder__f_maxDim === ((1 + n) | 0))) {
          var $$x4 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
          var n$3 = ((1 + n) | 0);
          var $$x3 = ($$x4.get((((-1) + n$3) | 0)) === null)
        } else {
          var $$x3 = false
        };
        if ($$x3) {
          $thiz.sci_VectorSliceBuilder__f_maxDim = n
        }
      } else {
        var $$x5 = $thiz.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
        var n$4 = ((1 + n) | 0);
        var to = (((-1) + sufN1.u.length) | 0);
        $$x5.set(((11 - n$4) | 0), $m_ju_Arrays$().copyOfRange__AO__I__I__AO(sufN1, 0, to))
      }
    }
  }
}
/** @constructor */
function $c_sci_VectorSliceBuilder(lo, hi) {
  this.sci_VectorSliceBuilder__f_lo = 0;
  this.sci_VectorSliceBuilder__f_hi = 0;
  this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices = null;
  this.sci_VectorSliceBuilder__f_len = 0;
  this.sci_VectorSliceBuilder__f_pos = 0;
  this.sci_VectorSliceBuilder__f_maxDim = 0;
  this.sci_VectorSliceBuilder__f_lo = lo;
  this.sci_VectorSliceBuilder__f_hi = hi;
  this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices = new ($d_O.getArrayOf().getArrayOf().constr)(11);
  this.sci_VectorSliceBuilder__f_len = 0;
  this.sci_VectorSliceBuilder__f_pos = 0;
  this.sci_VectorSliceBuilder__f_maxDim = 0
}
$c_sci_VectorSliceBuilder.prototype = new $h_O();
$c_sci_VectorSliceBuilder.prototype.constructor = $c_sci_VectorSliceBuilder;
/** @constructor */
function $h_sci_VectorSliceBuilder() {
  /*<skip>*/
}
$h_sci_VectorSliceBuilder.prototype = $c_sci_VectorSliceBuilder.prototype;
$c_sci_VectorSliceBuilder.prototype.consider__I__AO__V = (function(n, a) {
  var count = $imul(a.u.length, (1 << $imul(5, (((-1) + n) | 0))));
  var a$1 = ((this.sci_VectorSliceBuilder__f_lo - this.sci_VectorSliceBuilder__f_pos) | 0);
  var lo0 = ((a$1 > 0) ? a$1 : 0);
  var a$2 = ((this.sci_VectorSliceBuilder__f_hi - this.sci_VectorSliceBuilder__f_pos) | 0);
  var hi0 = ((a$2 < count) ? a$2 : count);
  if ((hi0 > lo0)) {
    $p_sci_VectorSliceBuilder__addSlice__I__AO__I__I__V(this, n, a, lo0, hi0);
    this.sci_VectorSliceBuilder__f_len = ((this.sci_VectorSliceBuilder__f_len + ((hi0 - lo0) | 0)) | 0)
  };
  this.sci_VectorSliceBuilder__f_pos = ((this.sci_VectorSliceBuilder__f_pos + count) | 0)
});
$c_sci_VectorSliceBuilder.prototype.result__sci_Vector = (function() {
  if ((this.sci_VectorSliceBuilder__f_len <= 32)) {
    if ((this.sci_VectorSliceBuilder__f_len === 0)) {
      return $m_sci_Vector0$()
    } else {
      var prefix1 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(0);
      var suffix1 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(10);
      if ((prefix1 !== null)) {
        if ((suffix1 !== null)) {
          var newLength = ((prefix1.u.length + suffix1.u.length) | 0);
          var dest = $m_ju_Arrays$().copyOf__AO__I__AO(prefix1, newLength);
          var destPos = prefix1.u.length;
          var length = suffix1.u.length;
          suffix1.copyTo(0, dest, destPos, length);
          var a = dest
        } else {
          var a = prefix1
        }
      } else if ((suffix1 !== null)) {
        var a = suffix1
      } else {
        var prefix2 = $asArrayOf_O(this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1), 2);
        if ((prefix2 !== null)) {
          var a = prefix2.get(0)
        } else {
          var suffix2 = $asArrayOf_O(this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9), 2);
          var a = suffix2.get(0)
        }
      };
      return new $c_sci_Vector1(a)
    }
  } else {
    $p_sci_VectorSliceBuilder__balancePrefix__I__V(this, 1);
    $p_sci_VectorSliceBuilder__balanceSuffix__I__V(this, 1);
    var resultDim = this.sci_VectorSliceBuilder__f_maxDim;
    if ((resultDim < 6)) {
      var $$x1 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
      var n = this.sci_VectorSliceBuilder__f_maxDim;
      var pre = $$x1.get((((-1) + n) | 0));
      var $$x2 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
      var n$1 = this.sci_VectorSliceBuilder__f_maxDim;
      var suf = $$x2.get(((11 - n$1) | 0));
      if (((pre !== null) && (suf !== null))) {
        if ((((pre.u.length + suf.u.length) | 0) <= 30)) {
          var $$x3 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
          var n$2 = this.sci_VectorSliceBuilder__f_maxDim;
          var newLength$1 = ((pre.u.length + suf.u.length) | 0);
          var dest$1 = $m_ju_Arrays$().copyOf__AO__I__AO(pre, newLength$1);
          var destPos$1 = pre.u.length;
          var length$1 = suf.u.length;
          suf.copyTo(0, dest$1, destPos$1, length$1);
          $$x3.set((((-1) + n$2) | 0), dest$1);
          var $$x4 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices;
          var n$3 = this.sci_VectorSliceBuilder__f_maxDim;
          $$x4.set(((11 - n$3) | 0), null)
        } else {
          resultDim = ((1 + resultDim) | 0)
        }
      } else {
        var one = ((pre !== null) ? pre : suf);
        if ((one.u.length > 30)) {
          resultDim = ((1 + resultDim) | 0)
        }
      }
    };
    var prefix1$2 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(0);
    var suffix1$2 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(10);
    var len1 = prefix1$2.u.length;
    var x1 = resultDim;
    switch (x1) {
      case 2: {
        var a$1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var p = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1);
        if ((p !== null)) {
          var $$x5 = p
        } else {
          var s = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9);
          var $$x5 = ((s !== null) ? s : a$1)
        };
        var data2 = $asArrayOf_O($$x5, 2);
        var res = new $c_sci_Vector2(prefix1$2, len1, data2, suffix1$2, this.sci_VectorSliceBuilder__f_len);
        break
      }
      case 3: {
        var a$2 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var p$1 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1);
        var prefix2$2 = $asArrayOf_O(((p$1 !== null) ? p$1 : a$2), 2);
        var a$3 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var p$2 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(2);
        if ((p$2 !== null)) {
          var $$x6 = p$2
        } else {
          var s$1 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(8);
          var $$x6 = ((s$1 !== null) ? s$1 : a$3)
        };
        var data3 = $asArrayOf_O($$x6, 3);
        var a$4 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var s$2 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9);
        var suffix2$2 = $asArrayOf_O(((s$2 !== null) ? s$2 : a$4), 2);
        var len12 = ((len1 + (prefix2$2.u.length << 5)) | 0);
        var res = new $c_sci_Vector3(prefix1$2, len1, prefix2$2, len12, data3, suffix2$2, suffix1$2, this.sci_VectorSliceBuilder__f_len);
        break
      }
      case 4: {
        var a$5 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var p$3 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1);
        var prefix2$3 = $asArrayOf_O(((p$3 !== null) ? p$3 : a$5), 2);
        var a$6 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var p$4 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(2);
        var prefix3 = $asArrayOf_O(((p$4 !== null) ? p$4 : a$6), 3);
        var a$7 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
        var p$5 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(3);
        if ((p$5 !== null)) {
          var $$x7 = p$5
        } else {
          var s$3 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(7);
          var $$x7 = ((s$3 !== null) ? s$3 : a$7)
        };
        var data4 = $asArrayOf_O($$x7, 4);
        var a$8 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var s$4 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(8);
        var suffix3 = $asArrayOf_O(((s$4 !== null) ? s$4 : a$8), 3);
        var a$9 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var s$5 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9);
        var suffix2$3 = $asArrayOf_O(((s$5 !== null) ? s$5 : a$9), 2);
        var len12$2 = ((len1 + (prefix2$3.u.length << 5)) | 0);
        var len123 = ((len12$2 + (prefix3.u.length << 10)) | 0);
        var res = new $c_sci_Vector4(prefix1$2, len1, prefix2$3, len12$2, prefix3, len123, data4, suffix3, suffix2$3, suffix1$2, this.sci_VectorSliceBuilder__f_len);
        break
      }
      case 5: {
        var a$10 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var p$6 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1);
        var prefix2$4 = $asArrayOf_O(((p$6 !== null) ? p$6 : a$10), 2);
        var a$11 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var p$7 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(2);
        var prefix3$2 = $asArrayOf_O(((p$7 !== null) ? p$7 : a$11), 3);
        var a$12 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
        var p$8 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(3);
        var prefix4 = $asArrayOf_O(((p$8 !== null) ? p$8 : a$12), 4);
        var a$13 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty5;
        var p$9 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(4);
        if ((p$9 !== null)) {
          var $$x8 = p$9
        } else {
          var s$6 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(6);
          var $$x8 = ((s$6 !== null) ? s$6 : a$13)
        };
        var data5 = $asArrayOf_O($$x8, 5);
        var a$14 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
        var s$7 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(7);
        var suffix4 = $asArrayOf_O(((s$7 !== null) ? s$7 : a$14), 4);
        var a$15 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var s$8 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(8);
        var suffix3$2 = $asArrayOf_O(((s$8 !== null) ? s$8 : a$15), 3);
        var a$16 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var s$9 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9);
        var suffix2$4 = $asArrayOf_O(((s$9 !== null) ? s$9 : a$16), 2);
        var len12$3 = ((len1 + (prefix2$4.u.length << 5)) | 0);
        var len123$2 = ((len12$3 + (prefix3$2.u.length << 10)) | 0);
        var len1234 = ((len123$2 + (prefix4.u.length << 15)) | 0);
        var res = new $c_sci_Vector5(prefix1$2, len1, prefix2$4, len12$3, prefix3$2, len123$2, prefix4, len1234, data5, suffix4, suffix3$2, suffix2$4, suffix1$2, this.sci_VectorSliceBuilder__f_len);
        break
      }
      case 6: {
        var a$17 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var p$10 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(1);
        var prefix2$5 = $asArrayOf_O(((p$10 !== null) ? p$10 : a$17), 2);
        var a$18 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var p$11 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(2);
        var prefix3$3 = $asArrayOf_O(((p$11 !== null) ? p$11 : a$18), 3);
        var a$19 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
        var p$12 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(3);
        var prefix4$2 = $asArrayOf_O(((p$12 !== null) ? p$12 : a$19), 4);
        var a$20 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty5;
        var p$13 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(4);
        var prefix5 = $asArrayOf_O(((p$13 !== null) ? p$13 : a$20), 5);
        var a$21 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty6;
        var p$14 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(5);
        if ((p$14 !== null)) {
          var $$x9 = p$14
        } else {
          var s$10 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(5);
          var $$x9 = ((s$10 !== null) ? s$10 : a$21)
        };
        var data6 = $asArrayOf_O($$x9, 6);
        var a$22 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty5;
        var s$11 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(6);
        var suffix5 = $asArrayOf_O(((s$11 !== null) ? s$11 : a$22), 5);
        var a$23 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
        var s$12 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(7);
        var suffix4$2 = $asArrayOf_O(((s$12 !== null) ? s$12 : a$23), 4);
        var a$24 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
        var s$13 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(8);
        var suffix3$3 = $asArrayOf_O(((s$13 !== null) ? s$13 : a$24), 3);
        var a$25 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
        var s$14 = this.sci_VectorSliceBuilder__f_scala$collection$immutable$VectorSliceBuilder$$slices.get(9);
        var suffix2$5 = $asArrayOf_O(((s$14 !== null) ? s$14 : a$25), 2);
        var len12$4 = ((len1 + (prefix2$5.u.length << 5)) | 0);
        var len123$3 = ((len12$4 + (prefix3$3.u.length << 10)) | 0);
        var len1234$2 = ((len123$3 + (prefix4$2.u.length << 15)) | 0);
        var len12345 = ((len1234$2 + (prefix5.u.length << 20)) | 0);
        var res = new $c_sci_Vector6(prefix1$2, len1, prefix2$5, len12$4, prefix3$3, len123$3, prefix4$2, len1234$2, prefix5, len12345, data6, suffix5, suffix4$2, suffix3$3, suffix2$5, suffix1$2, this.sci_VectorSliceBuilder__f_len);
        break
      }
      default: {
        var res;
        throw new $c_s_MatchError(x1)
      }
    };
    return res
  }
});
$c_sci_VectorSliceBuilder.prototype.toString__T = (function() {
  return (((((((((("VectorSliceBuilder(lo=" + this.sci_VectorSliceBuilder__f_lo) + ", hi=") + this.sci_VectorSliceBuilder__f_hi) + ", len=") + this.sci_VectorSliceBuilder__f_len) + ", pos=") + this.sci_VectorSliceBuilder__f_pos) + ", maxDim=") + this.sci_VectorSliceBuilder__f_maxDim) + ")")
});
var $d_sci_VectorSliceBuilder = new $TypeData().initClass({
  sci_VectorSliceBuilder: 0
}, false, "scala.collection.immutable.VectorSliceBuilder", {
  sci_VectorSliceBuilder: 1,
  O: 1
});
$c_sci_VectorSliceBuilder.prototype.$classData = $d_sci_VectorSliceBuilder;
/** @constructor */
function $c_sci_VectorStatics$() {
  this.sci_VectorStatics$__f_empty1 = null;
  this.sci_VectorStatics$__f_empty2 = null;
  this.sci_VectorStatics$__f_empty3 = null;
  this.sci_VectorStatics$__f_empty4 = null;
  this.sci_VectorStatics$__f_empty5 = null;
  this.sci_VectorStatics$__f_empty6 = null;
  $n_sci_VectorStatics$ = this;
  this.sci_VectorStatics$__f_empty1 = new $ac_O(0);
  this.sci_VectorStatics$__f_empty2 = new ($d_O.getArrayOf().getArrayOf().constr)(0);
  this.sci_VectorStatics$__f_empty3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(0);
  this.sci_VectorStatics$__f_empty4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(0);
  this.sci_VectorStatics$__f_empty5 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(0);
  this.sci_VectorStatics$__f_empty6 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(0)
}
$c_sci_VectorStatics$.prototype = new $h_O();
$c_sci_VectorStatics$.prototype.constructor = $c_sci_VectorStatics$;
/** @constructor */
function $h_sci_VectorStatics$() {
  /*<skip>*/
}
$h_sci_VectorStatics$.prototype = $c_sci_VectorStatics$.prototype;
$c_sci_VectorStatics$.prototype.copyAppend1__AO__O__AO = (function(a, elem) {
  var alen = a.u.length;
  var ac = new $ac_O(((1 + alen) | 0));
  a.copyTo(0, ac, 0, alen);
  ac.set(alen, elem);
  return ac
});
$c_sci_VectorStatics$.prototype.copyAppend__AO__O__AO = (function(a, elem) {
  var newLength = ((1 + a.u.length) | 0);
  var ac = $m_ju_Arrays$().copyOf__AO__I__AO(a, newLength);
  ac.set((((-1) + ac.u.length) | 0), elem);
  return ac
});
$c_sci_VectorStatics$.prototype.copyPrepend__O__AO__AO = (function(elem, a) {
  var componentType = $objectGetClass(a).getComponentType__jl_Class();
  var length = ((1 + a.u.length) | 0);
  var ac = $asArrayOf_O($m_jl_reflect_Array$().newInstance__jl_Class__I__O(componentType, length), 1);
  var length$1 = a.u.length;
  a.copyTo(0, ac, 1, length$1);
  ac.set(0, elem);
  return ac
});
$c_sci_VectorStatics$.prototype.foreachRec__I__AO__F1__V = (function(level, a, f) {
  var i = 0;
  var len = a.u.length;
  if ((level === 0)) {
    while ((i < len)) {
      f.apply__O__O(a.get(i));
      i = ((1 + i) | 0)
    }
  } else {
    var l = (((-1) + level) | 0);
    while ((i < len)) {
      this.foreachRec__I__AO__F1__V(l, $asArrayOf_O(a.get(i), 1), f);
      i = ((1 + i) | 0)
    }
  }
});
var $d_sci_VectorStatics$ = new $TypeData().initClass({
  sci_VectorStatics$: 0
}, false, "scala.collection.immutable.VectorStatics$", {
  sci_VectorStatics$: 1,
  O: 1
});
$c_sci_VectorStatics$.prototype.$classData = $d_sci_VectorStatics$;
var $n_sci_VectorStatics$;
function $m_sci_VectorStatics$() {
  if ((!$n_sci_VectorStatics$)) {
    $n_sci_VectorStatics$ = new $c_sci_VectorStatics$()
  };
  return $n_sci_VectorStatics$
}
/** @constructor */
function $c_scm_HashMap$Node(_key, _hash, _value, _next) {
  this.scm_HashMap$Node__f__key = null;
  this.scm_HashMap$Node__f__hash = 0;
  this.scm_HashMap$Node__f__value = null;
  this.scm_HashMap$Node__f__next = null;
  this.scm_HashMap$Node__f__key = _key;
  this.scm_HashMap$Node__f__hash = _hash;
  this.scm_HashMap$Node__f__value = _value;
  this.scm_HashMap$Node__f__next = _next
}
$c_scm_HashMap$Node.prototype = new $h_O();
$c_scm_HashMap$Node.prototype.constructor = $c_scm_HashMap$Node;
/** @constructor */
function $h_scm_HashMap$Node() {
  /*<skip>*/
}
$h_scm_HashMap$Node.prototype = $c_scm_HashMap$Node.prototype;
$c_scm_HashMap$Node.prototype.findNode__O__I__scm_HashMap$Node = (function(k, h) {
  var _$this = this;
  while (true) {
    if (((h === _$this.scm_HashMap$Node__f__hash) && $m_sr_BoxesRunTime$().equals__O__O__Z(k, _$this.scm_HashMap$Node__f__key))) {
      return _$this
    } else if (((_$this.scm_HashMap$Node__f__next === null) || (_$this.scm_HashMap$Node__f__hash > h))) {
      return null
    } else {
      _$this = _$this.scm_HashMap$Node__f__next
    }
  }
});
$c_scm_HashMap$Node.prototype.foreachEntry__F2__V = (function(f) {
  var _$this = this;
  while (true) {
    f.apply__O__O__O(_$this.scm_HashMap$Node__f__key, _$this.scm_HashMap$Node__f__value);
    if ((_$this.scm_HashMap$Node__f__next !== null)) {
      _$this = _$this.scm_HashMap$Node__f__next;
      continue
    };
    break
  }
});
$c_scm_HashMap$Node.prototype.toString__T = (function() {
  return ((((((("Node(" + this.scm_HashMap$Node__f__key) + ", ") + this.scm_HashMap$Node__f__value) + ", ") + this.scm_HashMap$Node__f__hash) + ") -> ") + this.scm_HashMap$Node__f__next)
});
function $as_scm_HashMap$Node(obj) {
  return (((obj instanceof $c_scm_HashMap$Node) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.HashMap$Node"))
}
function $isArrayOf_scm_HashMap$Node(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_HashMap$Node)))
}
function $asArrayOf_scm_HashMap$Node(obj, depth) {
  return (($isArrayOf_scm_HashMap$Node(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.HashMap$Node;", depth))
}
var $d_scm_HashMap$Node = new $TypeData().initClass({
  scm_HashMap$Node: 0
}, false, "scala.collection.mutable.HashMap$Node", {
  scm_HashMap$Node: 1,
  O: 1
});
$c_scm_HashMap$Node.prototype.$classData = $d_scm_HashMap$Node;
/** @constructor */
function $c_sc_package$$colon$plus$() {
  /*<skip>*/
}
$c_sc_package$$colon$plus$.prototype = new $h_O();
$c_sc_package$$colon$plus$.prototype.constructor = $c_sc_package$$colon$plus$;
/** @constructor */
function $h_sc_package$$colon$plus$() {
  /*<skip>*/
}
$h_sc_package$$colon$plus$.prototype = $c_sc_package$$colon$plus$.prototype;
var $d_sc_package$$colon$plus$ = new $TypeData().initClass({
  sc_package$$colon$plus$: 0
}, false, "scala.collection.package$$colon$plus$", {
  sc_package$$colon$plus$: 1,
  O: 1
});
$c_sc_package$$colon$plus$.prototype.$classData = $d_sc_package$$colon$plus$;
var $n_sc_package$$colon$plus$;
function $m_sc_package$$colon$plus$() {
  if ((!$n_sc_package$$colon$plus$)) {
    $n_sc_package$$colon$plus$ = new $c_sc_package$$colon$plus$()
  };
  return $n_sc_package$$colon$plus$
}
/** @constructor */
function $c_sc_package$$plus$colon$() {
  /*<skip>*/
}
$c_sc_package$$plus$colon$.prototype = new $h_O();
$c_sc_package$$plus$colon$.prototype.constructor = $c_sc_package$$plus$colon$;
/** @constructor */
function $h_sc_package$$plus$colon$() {
  /*<skip>*/
}
$h_sc_package$$plus$colon$.prototype = $c_sc_package$$plus$colon$.prototype;
var $d_sc_package$$plus$colon$ = new $TypeData().initClass({
  sc_package$$plus$colon$: 0
}, false, "scala.collection.package$$plus$colon$", {
  sc_package$$plus$colon$: 1,
  O: 1
});
$c_sc_package$$plus$colon$.prototype.$classData = $d_sc_package$$plus$colon$;
var $n_sc_package$$plus$colon$;
function $m_sc_package$$plus$colon$() {
  if ((!$n_sc_package$$plus$colon$)) {
    $n_sc_package$$plus$colon$ = new $c_sc_package$$plus$colon$()
  };
  return $n_sc_package$$plus$colon$
}
/** @constructor */
function $c_s_math_Ordered$() {
  /*<skip>*/
}
$c_s_math_Ordered$.prototype = new $h_O();
$c_s_math_Ordered$.prototype.constructor = $c_s_math_Ordered$;
/** @constructor */
function $h_s_math_Ordered$() {
  /*<skip>*/
}
$h_s_math_Ordered$.prototype = $c_s_math_Ordered$.prototype;
var $d_s_math_Ordered$ = new $TypeData().initClass({
  s_math_Ordered$: 0
}, false, "scala.math.Ordered$", {
  s_math_Ordered$: 1,
  O: 1
});
$c_s_math_Ordered$.prototype.$classData = $d_s_math_Ordered$;
var $n_s_math_Ordered$;
function $m_s_math_Ordered$() {
  if ((!$n_s_math_Ordered$)) {
    $n_s_math_Ordered$ = new $c_s_math_Ordered$()
  };
  return $n_s_math_Ordered$
}
/** @constructor */
function $c_s_package$() {
  this.s_package$__f_BigDecimal = null;
  this.s_package$__f_BigInt = null;
  this.s_package$__f_AnyRef = null;
  this.s_package$__f_Traversable = null;
  this.s_package$__f_Iterable = null;
  this.s_package$__f_Seq = null;
  this.s_package$__f_IndexedSeq = null;
  this.s_package$__f_Iterator = null;
  this.s_package$__f_List = null;
  this.s_package$__f_Nil = null;
  this.s_package$__f_$colon$colon = null;
  this.s_package$__f_$plus$colon = null;
  this.s_package$__f_$colon$plus = null;
  this.s_package$__f_Stream = null;
  this.s_package$__f_LazyList = null;
  this.s_package$__f_Vector = null;
  this.s_package$__f_StringBuilder = null;
  this.s_package$__f_Range = null;
  this.s_package$__f_Equiv = null;
  this.s_package$__f_Fractional = null;
  this.s_package$__f_Integral = null;
  this.s_package$__f_Numeric = null;
  this.s_package$__f_Ordered = null;
  this.s_package$__f_Ordering = null;
  this.s_package$__f_Either = null;
  this.s_package$__f_Left = null;
  this.s_package$__f_Right = null;
  this.s_package$__f_bitmap$0 = 0;
  $n_s_package$ = this;
  this.s_package$__f_AnyRef = new $c_s_package$$anon$1();
  this.s_package$__f_Traversable = $m_sc_Iterable$();
  this.s_package$__f_Iterable = $m_sc_Iterable$();
  this.s_package$__f_Seq = $m_sci_Seq$();
  this.s_package$__f_IndexedSeq = $m_sci_IndexedSeq$();
  this.s_package$__f_Iterator = $m_sc_Iterator$();
  this.s_package$__f_List = $m_sci_List$();
  this.s_package$__f_Nil = $m_sci_Nil$();
  this.s_package$__f_$colon$colon = $m_sci_$colon$colon$();
  this.s_package$__f_$plus$colon = $m_sc_package$$plus$colon$();
  this.s_package$__f_$colon$plus = $m_sc_package$$colon$plus$();
  this.s_package$__f_Stream = $m_sci_Stream$();
  this.s_package$__f_LazyList = $m_sci_LazyList$();
  this.s_package$__f_Vector = $m_sci_Vector$();
  this.s_package$__f_StringBuilder = $m_scm_StringBuilder$();
  this.s_package$__f_Range = $m_sci_Range$();
  this.s_package$__f_Equiv = $m_s_math_Equiv$();
  this.s_package$__f_Fractional = $m_s_math_Fractional$();
  this.s_package$__f_Integral = $m_s_math_Integral$();
  this.s_package$__f_Numeric = $m_s_math_Numeric$();
  this.s_package$__f_Ordered = $m_s_math_Ordered$();
  this.s_package$__f_Ordering = $m_s_math_Ordering$();
  this.s_package$__f_Either = $m_s_util_Either$();
  this.s_package$__f_Left = $m_s_util_Left$();
  this.s_package$__f_Right = $m_s_util_Right$()
}
$c_s_package$.prototype = new $h_O();
$c_s_package$.prototype.constructor = $c_s_package$;
/** @constructor */
function $h_s_package$() {
  /*<skip>*/
}
$h_s_package$.prototype = $c_s_package$.prototype;
var $d_s_package$ = new $TypeData().initClass({
  s_package$: 0
}, false, "scala.package$", {
  s_package$: 1,
  O: 1
});
$c_s_package$.prototype.$classData = $d_s_package$;
var $n_s_package$;
function $m_s_package$() {
  if ((!$n_s_package$)) {
    $n_s_package$ = new $c_s_package$()
  };
  return $n_s_package$
}
/** @constructor */
function $c_sr_BoxesRunTime$() {
  /*<skip>*/
}
$c_sr_BoxesRunTime$.prototype = new $h_O();
$c_sr_BoxesRunTime$.prototype.constructor = $c_sr_BoxesRunTime$;
/** @constructor */
function $h_sr_BoxesRunTime$() {
  /*<skip>*/
}
$h_sr_BoxesRunTime$.prototype = $c_sr_BoxesRunTime$.prototype;
$c_sr_BoxesRunTime$.prototype.equals__O__O__Z = (function(x, y) {
  if ((x === y)) {
    return true
  } else if ($is_jl_Number(x)) {
    var x2 = $as_jl_Number(x);
    return this.equalsNumObject__jl_Number__O__Z(x2, y)
  } else if ((x instanceof $Char)) {
    var x3 = $as_jl_Character(x);
    return this.equalsCharObject__jl_Character__O__Z(x3, y)
  } else {
    return ((x === null) ? (y === null) : $dp_equals__O__Z(x, y))
  }
});
$c_sr_BoxesRunTime$.prototype.equalsNumObject__jl_Number__O__Z = (function(xn, y) {
  if ($is_jl_Number(y)) {
    var x2 = $as_jl_Number(y);
    return this.equalsNumNum__jl_Number__jl_Number__Z(xn, x2)
  } else if ((y instanceof $Char)) {
    var x3 = $as_jl_Character(y);
    if (((typeof xn) === "number")) {
      var x2$1 = $uD(xn);
      return (x2$1 === $uC(x3))
    } else if ((xn instanceof $c_RTLong)) {
      var t = $uJ(xn);
      var lo = t.RTLong__f_lo;
      var hi = t.RTLong__f_hi;
      var value = $uC(x3);
      var hi$1 = (value >> 31);
      return ((lo === value) && (hi === hi$1))
    } else {
      return ((xn === null) ? (x3 === null) : $dp_equals__O__Z(xn, x3))
    }
  } else {
    return ((xn === null) ? (y === null) : $dp_equals__O__Z(xn, y))
  }
});
$c_sr_BoxesRunTime$.prototype.equalsNumNum__jl_Number__jl_Number__Z = (function(xn, yn) {
  if (((typeof xn) === "number")) {
    var x2 = $uD(xn);
    if (((typeof yn) === "number")) {
      var x2$2 = $uD(yn);
      return (x2 === x2$2)
    } else if ((yn instanceof $c_RTLong)) {
      var t = $uJ(yn);
      var lo = t.RTLong__f_lo;
      var hi = t.RTLong__f_hi;
      return (x2 === $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D(lo, hi))
    } else if (false) {
      var x4 = $as_s_math_ScalaNumber(yn);
      return x4.equals__O__Z(x2)
    } else {
      return false
    }
  } else if ((xn instanceof $c_RTLong)) {
    var t$1 = $uJ(xn);
    var lo$1 = t$1.RTLong__f_lo;
    var hi$1 = t$1.RTLong__f_hi;
    if ((yn instanceof $c_RTLong)) {
      var t$2 = $uJ(yn);
      var lo$2 = t$2.RTLong__f_lo;
      var hi$2 = t$2.RTLong__f_hi;
      return ((lo$1 === lo$2) && (hi$1 === hi$2))
    } else if (((typeof yn) === "number")) {
      var x3$3 = $uD(yn);
      return ($m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D(lo$1, hi$1) === x3$3)
    } else if (false) {
      var x4$2 = $as_s_math_ScalaNumber(yn);
      return x4$2.equals__O__Z(new $c_RTLong(lo$1, hi$1))
    } else {
      return false
    }
  } else {
    return ((xn === null) ? (yn === null) : $dp_equals__O__Z(xn, yn))
  }
});
$c_sr_BoxesRunTime$.prototype.equalsCharObject__jl_Character__O__Z = (function(xc, y) {
  if ((y instanceof $Char)) {
    var x2 = $as_jl_Character(y);
    return ($uC(xc) === $uC(x2))
  } else if ($is_jl_Number(y)) {
    var x3 = $as_jl_Number(y);
    if (((typeof x3) === "number")) {
      var x2$1 = $uD(x3);
      return (x2$1 === $uC(xc))
    } else if ((x3 instanceof $c_RTLong)) {
      var t = $uJ(x3);
      var lo = t.RTLong__f_lo;
      var hi = t.RTLong__f_hi;
      var value = $uC(xc);
      var hi$1 = (value >> 31);
      return ((lo === value) && (hi === hi$1))
    } else {
      return ((x3 === null) ? (xc === null) : $dp_equals__O__Z(x3, xc))
    }
  } else {
    return ((xc === null) && (y === null))
  }
});
var $d_sr_BoxesRunTime$ = new $TypeData().initClass({
  sr_BoxesRunTime$: 0
}, false, "scala.runtime.BoxesRunTime$", {
  sr_BoxesRunTime$: 1,
  O: 1
});
$c_sr_BoxesRunTime$.prototype.$classData = $d_sr_BoxesRunTime$;
var $n_sr_BoxesRunTime$;
function $m_sr_BoxesRunTime$() {
  if ((!$n_sr_BoxesRunTime$)) {
    $n_sr_BoxesRunTime$ = new $c_sr_BoxesRunTime$()
  };
  return $n_sr_BoxesRunTime$
}
var $d_sr_Null$ = new $TypeData().initClass({
  sr_Null$: 0
}, false, "scala.runtime.Null$", {
  sr_Null$: 1,
  O: 1
});
/** @constructor */
function $c_sr_ScalaRunTime$() {
  /*<skip>*/
}
$c_sr_ScalaRunTime$.prototype = new $h_O();
$c_sr_ScalaRunTime$.prototype.constructor = $c_sr_ScalaRunTime$;
/** @constructor */
function $h_sr_ScalaRunTime$() {
  /*<skip>*/
}
$h_sr_ScalaRunTime$.prototype = $c_sr_ScalaRunTime$.prototype;
$c_sr_ScalaRunTime$.prototype.array_apply__O__I__O = (function(xs, idx) {
  if ((xs instanceof $ac_O)) {
    var x2 = $asArrayOf_O(xs, 1);
    return x2.get(idx)
  } else if ((xs instanceof $ac_I)) {
    var x3 = $asArrayOf_I(xs, 1);
    return x3.get(idx)
  } else if ((xs instanceof $ac_D)) {
    var x4 = $asArrayOf_D(xs, 1);
    return x4.get(idx)
  } else if ((xs instanceof $ac_J)) {
    var x5 = $asArrayOf_J(xs, 1);
    return x5.get(idx)
  } else if ((xs instanceof $ac_F)) {
    var x6 = $asArrayOf_F(xs, 1);
    return x6.get(idx)
  } else if ((xs instanceof $ac_C)) {
    var x7 = $asArrayOf_C(xs, 1);
    return $bC(x7.get(idx))
  } else if ((xs instanceof $ac_B)) {
    var x8 = $asArrayOf_B(xs, 1);
    return x8.get(idx)
  } else if ((xs instanceof $ac_S)) {
    var x9 = $asArrayOf_S(xs, 1);
    return x9.get(idx)
  } else if ((xs instanceof $ac_Z)) {
    var x10 = $asArrayOf_Z(xs, 1);
    return x10.get(idx)
  } else if ((xs === null)) {
    throw new $c_jl_NullPointerException()
  } else {
    throw new $c_s_MatchError(xs)
  }
});
$c_sr_ScalaRunTime$.prototype.array_update__O__I__O__V = (function(xs, idx, value) {
  if ((xs instanceof $ac_O)) {
    var x2 = $asArrayOf_O(xs, 1);
    x2.set(idx, value)
  } else if ((xs instanceof $ac_I)) {
    var x3 = $asArrayOf_I(xs, 1);
    x3.set(idx, $uI(value))
  } else if ((xs instanceof $ac_D)) {
    var x4 = $asArrayOf_D(xs, 1);
    x4.set(idx, $uD(value))
  } else if ((xs instanceof $ac_J)) {
    var x5 = $asArrayOf_J(xs, 1);
    x5.set(idx, $uJ(value))
  } else if ((xs instanceof $ac_F)) {
    var x6 = $asArrayOf_F(xs, 1);
    x6.set(idx, $uF(value))
  } else if ((xs instanceof $ac_C)) {
    var x7 = $asArrayOf_C(xs, 1);
    x7.set(idx, $uC(value))
  } else if ((xs instanceof $ac_B)) {
    var x8 = $asArrayOf_B(xs, 1);
    x8.set(idx, $uB(value))
  } else if ((xs instanceof $ac_S)) {
    var x9 = $asArrayOf_S(xs, 1);
    x9.set(idx, $uS(value))
  } else if ((xs instanceof $ac_Z)) {
    var x10 = $asArrayOf_Z(xs, 1);
    x10.set(idx, $uZ(value))
  } else if ((xs === null)) {
    throw new $c_jl_NullPointerException()
  } else {
    throw new $c_s_MatchError(xs)
  }
});
$c_sr_ScalaRunTime$.prototype.array_length__O__I = (function(xs) {
  return $m_jl_reflect_Array$().getLength__O__I(xs)
});
$c_sr_ScalaRunTime$.prototype._toString__s_Product__T = (function(x) {
  var this$1 = x.productIterator__sc_Iterator();
  var start = (x.productPrefix__T() + "(");
  return $f_sc_IterableOnceOps__mkString__T__T__T__T(this$1, start, ",", ")")
});
var $d_sr_ScalaRunTime$ = new $TypeData().initClass({
  sr_ScalaRunTime$: 0
}, false, "scala.runtime.ScalaRunTime$", {
  sr_ScalaRunTime$: 1,
  O: 1
});
$c_sr_ScalaRunTime$.prototype.$classData = $d_sr_ScalaRunTime$;
var $n_sr_ScalaRunTime$;
function $m_sr_ScalaRunTime$() {
  if ((!$n_sr_ScalaRunTime$)) {
    $n_sr_ScalaRunTime$ = new $c_sr_ScalaRunTime$()
  };
  return $n_sr_ScalaRunTime$
}
/** @constructor */
function $c_sr_Statics$() {
  /*<skip>*/
}
$c_sr_Statics$.prototype = new $h_O();
$c_sr_Statics$.prototype.constructor = $c_sr_Statics$;
/** @constructor */
function $h_sr_Statics$() {
  /*<skip>*/
}
$h_sr_Statics$.prototype = $c_sr_Statics$.prototype;
$c_sr_Statics$.prototype.longHash__J__I = (function(lv) {
  var lo = lv.RTLong__f_lo;
  var hi = lv.RTLong__f_hi;
  return ((hi === (lo >> 31)) ? lo : (lo ^ hi))
});
$c_sr_Statics$.prototype.doubleHash__D__I = (function(dv) {
  var iv = $doubleToInt(dv);
  if ((iv === dv)) {
    return iv
  } else {
    var this$1 = $m_RTLong$();
    var lo = this$1.org$scalajs$linker$runtime$RuntimeLong$$fromDoubleImpl__D__I(dv);
    var hi = this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
    return (($m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D(lo, hi) === dv) ? (lo ^ hi) : $m_jl_FloatingPointBits$().numberHashCode__D__I(dv))
  }
});
$c_sr_Statics$.prototype.anyHash__O__I = (function(x) {
  if ((x === null)) {
    return 0
  } else if (((typeof x) === "number")) {
    var x3 = $uD(x);
    return this.doubleHash__D__I(x3)
  } else if ((x instanceof $c_RTLong)) {
    var t = $uJ(x);
    var lo = t.RTLong__f_lo;
    var hi = t.RTLong__f_hi;
    return this.longHash__J__I(new $c_RTLong(lo, hi))
  } else {
    return $dp_hashCode__I(x)
  }
});
$c_sr_Statics$.prototype.ioobe__I__O = (function(n) {
  throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), ("" + n))
});
var $d_sr_Statics$ = new $TypeData().initClass({
  sr_Statics$: 0
}, false, "scala.runtime.Statics$", {
  sr_Statics$: 1,
  O: 1
});
$c_sr_Statics$.prototype.$classData = $d_sr_Statics$;
var $n_sr_Statics$;
function $m_sr_Statics$() {
  if ((!$n_sr_Statics$)) {
    $n_sr_Statics$ = new $c_sr_Statics$()
  };
  return $n_sr_Statics$
}
/** @constructor */
function $c_sr_Statics$PFMarker$() {
  /*<skip>*/
}
$c_sr_Statics$PFMarker$.prototype = new $h_O();
$c_sr_Statics$PFMarker$.prototype.constructor = $c_sr_Statics$PFMarker$;
/** @constructor */
function $h_sr_Statics$PFMarker$() {
  /*<skip>*/
}
$h_sr_Statics$PFMarker$.prototype = $c_sr_Statics$PFMarker$.prototype;
var $d_sr_Statics$PFMarker$ = new $TypeData().initClass({
  sr_Statics$PFMarker$: 0
}, false, "scala.runtime.Statics$PFMarker$", {
  sr_Statics$PFMarker$: 1,
  O: 1
});
$c_sr_Statics$PFMarker$.prototype.$classData = $d_sr_Statics$PFMarker$;
var $n_sr_Statics$PFMarker$;
function $m_sr_Statics$PFMarker$() {
  if ((!$n_sr_Statics$PFMarker$)) {
    $n_sr_Statics$PFMarker$ = new $c_sr_Statics$PFMarker$()
  };
  return $n_sr_Statics$PFMarker$
}
/** @constructor */
function $c_sjs_js_WrappedDictionary$Cache$() {
  this.sjs_js_WrappedDictionary$Cache$__f_safeHasOwnProperty = null;
  $n_sjs_js_WrappedDictionary$Cache$ = this;
  this.sjs_js_WrappedDictionary$Cache$__f_safeHasOwnProperty = Object.prototype.hasOwnProperty
}
$c_sjs_js_WrappedDictionary$Cache$.prototype = new $h_O();
$c_sjs_js_WrappedDictionary$Cache$.prototype.constructor = $c_sjs_js_WrappedDictionary$Cache$;
/** @constructor */
function $h_sjs_js_WrappedDictionary$Cache$() {
  /*<skip>*/
}
$h_sjs_js_WrappedDictionary$Cache$.prototype = $c_sjs_js_WrappedDictionary$Cache$.prototype;
var $d_sjs_js_WrappedDictionary$Cache$ = new $TypeData().initClass({
  sjs_js_WrappedDictionary$Cache$: 0
}, false, "scala.scalajs.js.WrappedDictionary$Cache$", {
  sjs_js_WrappedDictionary$Cache$: 1,
  O: 1
});
$c_sjs_js_WrappedDictionary$Cache$.prototype.$classData = $d_sjs_js_WrappedDictionary$Cache$;
var $n_sjs_js_WrappedDictionary$Cache$;
function $m_sjs_js_WrappedDictionary$Cache$() {
  if ((!$n_sjs_js_WrappedDictionary$Cache$)) {
    $n_sjs_js_WrappedDictionary$Cache$ = new $c_sjs_js_WrappedDictionary$Cache$()
  };
  return $n_sjs_js_WrappedDictionary$Cache$
}
/** @constructor */
function $c_sjs_js_special_package$() {
  /*<skip>*/
}
$c_sjs_js_special_package$.prototype = new $h_O();
$c_sjs_js_special_package$.prototype.constructor = $c_sjs_js_special_package$;
/** @constructor */
function $h_sjs_js_special_package$() {
  /*<skip>*/
}
$h_sjs_js_special_package$.prototype = $c_sjs_js_special_package$.prototype;
$c_sjs_js_special_package$.prototype.objectLiteral__sci_Seq__sjs_js_Object = (function(properties) {
  var result = {};
  properties.foreach__F1__V(new $c_sjsr_AnonFunction1(((this$1, result$1) => ((pair$2) => {
    var pair = $as_T2(pair$2);
    result$1[pair.T2__f__1] = pair.T2__f__2
  }))(this, result)));
  return result
});
var $d_sjs_js_special_package$ = new $TypeData().initClass({
  sjs_js_special_package$: 0
}, false, "scala.scalajs.js.special.package$", {
  sjs_js_special_package$: 1,
  O: 1
});
$c_sjs_js_special_package$.prototype.$classData = $d_sjs_js_special_package$;
var $n_sjs_js_special_package$;
function $m_sjs_js_special_package$() {
  if ((!$n_sjs_js_special_package$)) {
    $n_sjs_js_special_package$ = new $c_sjs_js_special_package$()
  };
  return $n_sjs_js_special_package$
}
/** @constructor */
function $c_sjs_reflect_InstantiatableClass(runtimeClass, declaredConstructors) {
  this.sjs_reflect_InstantiatableClass__f_runtimeClass = null;
  this.sjs_reflect_InstantiatableClass__f_declaredConstructors = null;
  this.sjs_reflect_InstantiatableClass__f_runtimeClass = runtimeClass;
  this.sjs_reflect_InstantiatableClass__f_declaredConstructors = declaredConstructors
}
$c_sjs_reflect_InstantiatableClass.prototype = new $h_O();
$c_sjs_reflect_InstantiatableClass.prototype.constructor = $c_sjs_reflect_InstantiatableClass;
/** @constructor */
function $h_sjs_reflect_InstantiatableClass() {
  /*<skip>*/
}
$h_sjs_reflect_InstantiatableClass.prototype = $c_sjs_reflect_InstantiatableClass.prototype;
var $d_sjs_reflect_InstantiatableClass = new $TypeData().initClass({
  sjs_reflect_InstantiatableClass: 0
}, false, "scala.scalajs.reflect.InstantiatableClass", {
  sjs_reflect_InstantiatableClass: 1,
  O: 1
});
$c_sjs_reflect_InstantiatableClass.prototype.$classData = $d_sjs_reflect_InstantiatableClass;
/** @constructor */
function $c_sjs_reflect_InvokableConstructor(parameterTypes, newInstanceFun) {
  this.sjs_reflect_InvokableConstructor__f_parameterTypes = null;
  this.sjs_reflect_InvokableConstructor__f_newInstanceFun = null;
  this.sjs_reflect_InvokableConstructor__f_parameterTypes = parameterTypes;
  this.sjs_reflect_InvokableConstructor__f_newInstanceFun = newInstanceFun
}
$c_sjs_reflect_InvokableConstructor.prototype = new $h_O();
$c_sjs_reflect_InvokableConstructor.prototype.constructor = $c_sjs_reflect_InvokableConstructor;
/** @constructor */
function $h_sjs_reflect_InvokableConstructor() {
  /*<skip>*/
}
$h_sjs_reflect_InvokableConstructor.prototype = $c_sjs_reflect_InvokableConstructor.prototype;
var $d_sjs_reflect_InvokableConstructor = new $TypeData().initClass({
  sjs_reflect_InvokableConstructor: 0
}, false, "scala.scalajs.reflect.InvokableConstructor", {
  sjs_reflect_InvokableConstructor: 1,
  O: 1
});
$c_sjs_reflect_InvokableConstructor.prototype.$classData = $d_sjs_reflect_InvokableConstructor;
/** @constructor */
function $c_sjs_reflect_Reflect$() {
  this.sjs_reflect_Reflect$__f_loadableModuleClasses = null;
  this.sjs_reflect_Reflect$__f_instantiatableClasses = null;
  $n_sjs_reflect_Reflect$ = this;
  this.sjs_reflect_Reflect$__f_loadableModuleClasses = {};
  this.sjs_reflect_Reflect$__f_instantiatableClasses = {}
}
$c_sjs_reflect_Reflect$.prototype = new $h_O();
$c_sjs_reflect_Reflect$.prototype.constructor = $c_sjs_reflect_Reflect$;
/** @constructor */
function $h_sjs_reflect_Reflect$() {
  /*<skip>*/
}
$h_sjs_reflect_Reflect$.prototype = $c_sjs_reflect_Reflect$.prototype;
$c_sjs_reflect_Reflect$.prototype.registerInstantiatableClass__T__jl_Class__sjs_js_Array__V = (function(fqcn, runtimeClass, constructors) {
  var len = $uI(constructors.length);
  var res = new Array(len);
  var i = 0;
  while ((i < len)) {
    var $$x1 = i;
    var arg1 = constructors[i];
    var array = arg1[0];
    var this$4 = $ct_sjs_js_WrappedArray__sjs_js_Array__(new $c_sjs_js_WrappedArray(), array);
    $m_sci_List$();
    res[$$x1] = new $c_sjs_reflect_InvokableConstructor($m_sci_Nil$().prependedAll__sc_IterableOnce__sci_List(this$4), arg1[1]);
    i = ((1 + i) | 0)
  };
  var dict = this.sjs_reflect_Reflect$__f_instantiatableClasses;
  var this$8 = $ct_sjs_js_WrappedArray__sjs_js_Array__(new $c_sjs_js_WrappedArray(), res);
  $m_sci_List$();
  var value = new $c_sjs_reflect_InstantiatableClass(runtimeClass, $m_sci_Nil$().prependedAll__sc_IterableOnce__sci_List(this$8));
  dict[fqcn] = value
});
var $d_sjs_reflect_Reflect$ = new $TypeData().initClass({
  sjs_reflect_Reflect$: 0
}, false, "scala.scalajs.reflect.Reflect$", {
  sjs_reflect_Reflect$: 1,
  O: 1
});
$c_sjs_reflect_Reflect$.prototype.$classData = $d_sjs_reflect_Reflect$;
var $n_sjs_reflect_Reflect$;
function $m_sjs_reflect_Reflect$() {
  if ((!$n_sjs_reflect_Reflect$)) {
    $n_sjs_reflect_Reflect$ = new $c_sjs_reflect_Reflect$()
  };
  return $n_sjs_reflect_Reflect$
}
/** @constructor */
function $c_sjsr_package$() {
  /*<skip>*/
}
$c_sjsr_package$.prototype = new $h_O();
$c_sjsr_package$.prototype.constructor = $c_sjsr_package$;
/** @constructor */
function $h_sjsr_package$() {
  /*<skip>*/
}
$h_sjsr_package$.prototype = $c_sjsr_package$.prototype;
$c_sjsr_package$.prototype.wrapJavaScriptException__O__jl_Throwable = (function(e) {
  if ((e instanceof $c_jl_Throwable)) {
    var x2 = $as_jl_Throwable(e);
    return x2
  } else {
    return new $c_sjs_js_JavaScriptException(e)
  }
});
$c_sjsr_package$.prototype.unwrapJavaScriptException__jl_Throwable__O = (function(th) {
  if ((th instanceof $c_sjs_js_JavaScriptException)) {
    var x2 = $as_sjs_js_JavaScriptException(th);
    var e = x2.sjs_js_JavaScriptException__f_exception;
    return e
  } else {
    return th
  }
});
var $d_sjsr_package$ = new $TypeData().initClass({
  sjsr_package$: 0
}, false, "scala.scalajs.runtime.package$", {
  sjsr_package$: 1,
  O: 1
});
$c_sjsr_package$.prototype.$classData = $d_sjsr_package$;
var $n_sjsr_package$;
function $m_sjsr_package$() {
  if ((!$n_sjsr_package$)) {
    $n_sjsr_package$ = new $c_sjsr_package$()
  };
  return $n_sjsr_package$
}
/** @constructor */
function $c_s_util_DynamicVariable(init) {
  this.s_util_DynamicVariable__f_v = null;
  this.s_util_DynamicVariable__f_v = init
}
$c_s_util_DynamicVariable.prototype = new $h_O();
$c_s_util_DynamicVariable.prototype.constructor = $c_s_util_DynamicVariable;
/** @constructor */
function $h_s_util_DynamicVariable() {
  /*<skip>*/
}
$h_s_util_DynamicVariable.prototype = $c_s_util_DynamicVariable.prototype;
$c_s_util_DynamicVariable.prototype.toString__T = (function() {
  return (("DynamicVariable(" + this.s_util_DynamicVariable__f_v) + ")")
});
var $d_s_util_DynamicVariable = new $TypeData().initClass({
  s_util_DynamicVariable: 0
}, false, "scala.util.DynamicVariable", {
  s_util_DynamicVariable: 1,
  O: 1
});
$c_s_util_DynamicVariable.prototype.$classData = $d_s_util_DynamicVariable;
/** @constructor */
function $c_s_util_hashing_MurmurHash3() {
  /*<skip>*/
}
$c_s_util_hashing_MurmurHash3.prototype = new $h_O();
$c_s_util_hashing_MurmurHash3.prototype.constructor = $c_s_util_hashing_MurmurHash3;
/** @constructor */
function $h_s_util_hashing_MurmurHash3() {
  /*<skip>*/
}
$h_s_util_hashing_MurmurHash3.prototype = $c_s_util_hashing_MurmurHash3.prototype;
$c_s_util_hashing_MurmurHash3.prototype.mix__I__I__I = (function(hash, data) {
  var h = this.mixLast__I__I__I(hash, data);
  var i = h;
  h = ((i << 13) | ((i >>> 19) | 0));
  return (((-430675100) + $imul(5, h)) | 0)
});
$c_s_util_hashing_MurmurHash3.prototype.mixLast__I__I__I = (function(hash, data) {
  var k = data;
  k = $imul((-862048943), k);
  var i = k;
  k = ((i << 15) | ((i >>> 17) | 0));
  k = $imul(461845907, k);
  return (hash ^ k)
});
$c_s_util_hashing_MurmurHash3.prototype.finalizeHash__I__I__I = (function(hash, length) {
  return this.scala$util$hashing$MurmurHash3$$avalanche__I__I((hash ^ length))
});
$c_s_util_hashing_MurmurHash3.prototype.scala$util$hashing$MurmurHash3$$avalanche__I__I = (function(hash) {
  var h = hash;
  h = (h ^ ((h >>> 16) | 0));
  h = $imul((-2048144789), h);
  h = (h ^ ((h >>> 13) | 0));
  h = $imul((-1028477387), h);
  h = (h ^ ((h >>> 16) | 0));
  return h
});
$c_s_util_hashing_MurmurHash3.prototype.tuple2Hash__I__I__I__I = (function(x, y, seed) {
  var h = seed;
  h = this.mix__I__I__I(h, $f_T__hashCode__I("Tuple2"));
  h = this.mix__I__I__I(h, x);
  h = this.mix__I__I__I(h, y);
  return this.finalizeHash__I__I__I(h, 2)
});
$c_s_util_hashing_MurmurHash3.prototype.productHash__s_Product__I__Z__I = (function(x, seed, ignorePrefix) {
  var arr = x.productArity__I();
  if ((arr === 0)) {
    return $f_T__hashCode__I(x.productPrefix__T())
  } else {
    var h = seed;
    if ((!ignorePrefix)) {
      h = this.mix__I__I__I(h, $f_T__hashCode__I(x.productPrefix__T()))
    };
    var i = 0;
    while ((i < arr)) {
      var $$x1 = h;
      var x$1 = x.productElement__I__O(i);
      h = this.mix__I__I__I($$x1, $m_sr_Statics$().anyHash__O__I(x$1));
      i = ((1 + i) | 0)
    };
    return this.finalizeHash__I__I__I(h, arr)
  }
});
$c_s_util_hashing_MurmurHash3.prototype.unorderedHash__sc_IterableOnce__I__I = (function(xs, seed) {
  var a = 0;
  var b = 0;
  var n = 0;
  var c = 1;
  var iterator = xs.iterator__sc_Iterator();
  while (iterator.hasNext__Z()) {
    var x = iterator.next__O();
    var h = $m_sr_Statics$().anyHash__O__I(x);
    a = ((a + h) | 0);
    b = (b ^ h);
    c = $imul(c, (1 | h));
    n = ((1 + n) | 0)
  };
  var h$2 = seed;
  h$2 = this.mix__I__I__I(h$2, a);
  h$2 = this.mix__I__I__I(h$2, b);
  h$2 = this.mixLast__I__I__I(h$2, c);
  return this.finalizeHash__I__I__I(h$2, n)
});
$c_s_util_hashing_MurmurHash3.prototype.orderedHash__sc_IterableOnce__I__I = (function(xs, seed) {
  var it = xs.iterator__sc_Iterator();
  var h = seed;
  if ((!it.hasNext__Z())) {
    return this.finalizeHash__I__I__I(h, 0)
  };
  var x0 = it.next__O();
  if ((!it.hasNext__Z())) {
    return this.finalizeHash__I__I__I(this.mix__I__I__I(h, $m_sr_Statics$().anyHash__O__I(x0)), 1)
  };
  var x1 = it.next__O();
  var initial = $m_sr_Statics$().anyHash__O__I(x0);
  h = this.mix__I__I__I(h, initial);
  var h0 = h;
  var prev = $m_sr_Statics$().anyHash__O__I(x1);
  var rangeDiff = ((prev - initial) | 0);
  var i = 2;
  while (it.hasNext__Z()) {
    h = this.mix__I__I__I(h, prev);
    var x = it.next__O();
    var hash = $m_sr_Statics$().anyHash__O__I(x);
    if ((rangeDiff !== ((hash - prev) | 0))) {
      h = this.mix__I__I__I(h, hash);
      i = ((1 + i) | 0);
      while (it.hasNext__Z()) {
        var $$x1 = h;
        var x$1 = it.next__O();
        h = this.mix__I__I__I($$x1, $m_sr_Statics$().anyHash__O__I(x$1));
        i = ((1 + i) | 0)
      };
      return this.finalizeHash__I__I__I(h, i)
    };
    prev = hash;
    i = ((1 + i) | 0)
  };
  return this.scala$util$hashing$MurmurHash3$$avalanche__I__I(this.mix__I__I__I(this.mix__I__I__I(h0, rangeDiff), prev))
});
$c_s_util_hashing_MurmurHash3.prototype.rangeHash__I__I__I__I__I = (function(start, step, last, seed) {
  return this.scala$util$hashing$MurmurHash3$$avalanche__I__I(this.mix__I__I__I(this.mix__I__I__I(this.mix__I__I__I(seed, start), step), last))
});
$c_s_util_hashing_MurmurHash3.prototype.indexedSeqHash__sc_IndexedSeq__I__I = (function(a, seed) {
  var h = seed;
  var l = a.length__I();
  switch (l) {
    case 0: {
      return this.finalizeHash__I__I__I(h, 0);
      break
    }
    case 1: {
      var $$x1 = h;
      var x = a.apply__I__O(0);
      return this.finalizeHash__I__I__I(this.mix__I__I__I($$x1, $m_sr_Statics$().anyHash__O__I(x)), 1);
      break
    }
    default: {
      var x$1 = a.apply__I__O(0);
      var initial = $m_sr_Statics$().anyHash__O__I(x$1);
      h = this.mix__I__I__I(h, initial);
      var h0 = h;
      var x$2 = a.apply__I__O(1);
      var prev = $m_sr_Statics$().anyHash__O__I(x$2);
      var rangeDiff = ((prev - initial) | 0);
      var i = 2;
      while ((i < l)) {
        h = this.mix__I__I__I(h, prev);
        var x$3 = a.apply__I__O(i);
        var hash = $m_sr_Statics$().anyHash__O__I(x$3);
        if ((rangeDiff !== ((hash - prev) | 0))) {
          h = this.mix__I__I__I(h, hash);
          i = ((1 + i) | 0);
          while ((i < l)) {
            var $$x2 = h;
            var x$4 = a.apply__I__O(i);
            h = this.mix__I__I__I($$x2, $m_sr_Statics$().anyHash__O__I(x$4));
            i = ((1 + i) | 0)
          };
          return this.finalizeHash__I__I__I(h, l)
        };
        prev = hash;
        i = ((1 + i) | 0)
      };
      return this.scala$util$hashing$MurmurHash3$$avalanche__I__I(this.mix__I__I__I(this.mix__I__I__I(h0, rangeDiff), prev))
    }
  }
});
$c_s_util_hashing_MurmurHash3.prototype.listHash__sci_List__I__I = (function(xs, seed) {
  var n = 0;
  var h = seed;
  var rangeState = 0;
  var rangeDiff = 0;
  var prev = 0;
  var initial = 0;
  var elems = xs;
  while ((!elems.isEmpty__Z())) {
    var head = elems.head__O();
    var tail = $as_sci_List(elems.tail__O());
    var hash = $m_sr_Statics$().anyHash__O__I(head);
    h = this.mix__I__I__I(h, hash);
    var x1 = rangeState;
    switch (x1) {
      case 0: {
        initial = hash;
        rangeState = 1;
        break
      }
      case 1: {
        rangeDiff = ((hash - prev) | 0);
        rangeState = 2;
        break
      }
      case 2: {
        if ((rangeDiff !== ((hash - prev) | 0))) {
          rangeState = 3
        };
        break
      }
    };
    prev = hash;
    n = ((1 + n) | 0);
    elems = tail
  };
  return ((rangeState === 2) ? this.rangeHash__I__I__I__I__I(initial, rangeDiff, prev, seed) : this.finalizeHash__I__I__I(h, n))
});
function $p_Lzonedb_java_tzdb$__fixedZones$lzycompute__sjs_js_Dynamic($thiz) {
  if (((((2 & $thiz.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24) === 0)) {
    var fields = $m_sci_Nil$();
    $thiz.Lzonedb_java_tzdb$__f_fixedZones = $m_sjs_js_special_package$().objectLiteral__sci_Seq__sjs_js_Object(fields);
    $thiz.Lzonedb_java_tzdb$__f_bitmap$0 = (((2 | $thiz.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.Lzonedb_java_tzdb$__f_fixedZones
}
function $p_Lzonedb_java_tzdb$__stdZones$lzycompute__sjs_js_Dynamic($thiz) {
  if (((((4 & $thiz.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24) === 0)) {
    var array = [new $c_T2("Asia/Tokyo", $m_Lzonedb_java_tzdb$asiky$().Lzonedb_java_tzdb$asiky$__f_Asia_Tokyo)];
    var fields = new $c_sjsr_WrappedVarArgs(array);
    $thiz.Lzonedb_java_tzdb$__f_stdZones = $m_sjs_js_special_package$().objectLiteral__sci_Seq__sjs_js_Object(fields);
    $thiz.Lzonedb_java_tzdb$__f_bitmap$0 = (((4 | $thiz.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.Lzonedb_java_tzdb$__f_stdZones
}
/** @constructor */
function $c_Lzonedb_java_tzdb$() {
  this.Lzonedb_java_tzdb$__f_version = null;
  this.Lzonedb_java_tzdb$__f_fixedZones = null;
  this.Lzonedb_java_tzdb$__f_stdZones = null;
  this.Lzonedb_java_tzdb$__f_zoneLinks = null;
  this.Lzonedb_java_tzdb$__f_bitmap$0 = 0
}
$c_Lzonedb_java_tzdb$.prototype = new $h_O();
$c_Lzonedb_java_tzdb$.prototype.constructor = $c_Lzonedb_java_tzdb$;
/** @constructor */
function $h_Lzonedb_java_tzdb$() {
  /*<skip>*/
}
$h_Lzonedb_java_tzdb$.prototype = $c_Lzonedb_java_tzdb$.prototype;
$c_Lzonedb_java_tzdb$.prototype.fixedZones__sjs_js_Dynamic = (function() {
  return (((((2 & this.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24) === 0) ? $p_Lzonedb_java_tzdb$__fixedZones$lzycompute__sjs_js_Dynamic(this) : this.Lzonedb_java_tzdb$__f_fixedZones)
});
$c_Lzonedb_java_tzdb$.prototype.stdZones__sjs_js_Dynamic = (function() {
  return (((((4 & this.Lzonedb_java_tzdb$__f_bitmap$0) << 24) >> 24) === 0) ? $p_Lzonedb_java_tzdb$__stdZones$lzycompute__sjs_js_Dynamic(this) : this.Lzonedb_java_tzdb$__f_stdZones)
});
var $d_Lzonedb_java_tzdb$ = new $TypeData().initClass({
  Lzonedb_java_tzdb$: 0
}, false, "zonedb.java.tzdb$", {
  Lzonedb_java_tzdb$: 1,
  O: 1
});
$c_Lzonedb_java_tzdb$.prototype.$classData = $d_Lzonedb_java_tzdb$;
var $n_Lzonedb_java_tzdb$;
function $m_Lzonedb_java_tzdb$() {
  if ((!$n_Lzonedb_java_tzdb$)) {
    $n_Lzonedb_java_tzdb$ = new $c_Lzonedb_java_tzdb$()
  };
  return $n_Lzonedb_java_tzdb$
}
/** @constructor */
function $c_Lzonedb_java_tzdb$asiky$() {
  this.Lzonedb_java_tzdb$asiky$__f_Asia_Tokyo = null;
  $n_Lzonedb_java_tzdb$asiky$ = this;
  var array = [new $c_T2("s", 33539), new $c_T2("w", 33539), new $c_T2("t", [[1888001, 1139, 33539, 32400]]), new $c_T2("l", [[1888001, 1139, 33539, 32400], [1948123, 0, 32400, 36000], [1948256, 3600, 36000, 32400], [1949093, 0, 32400, 36000], [1949254, 3600, 36000, 32400], [1950127, 0, 32400, 36000], [1950253, 3600, 36000, 32400], [1951126, 0, 32400, 36000], [1951259, 3600, 36000, 32400]]), new $c_T2("r", [])];
  var fields = new $c_sjsr_WrappedVarArgs(array);
  this.Lzonedb_java_tzdb$asiky$__f_Asia_Tokyo = $m_sjs_js_special_package$().objectLiteral__sci_Seq__sjs_js_Object(fields)
}
$c_Lzonedb_java_tzdb$asiky$.prototype = new $h_O();
$c_Lzonedb_java_tzdb$asiky$.prototype.constructor = $c_Lzonedb_java_tzdb$asiky$;
/** @constructor */
function $h_Lzonedb_java_tzdb$asiky$() {
  /*<skip>*/
}
$h_Lzonedb_java_tzdb$asiky$.prototype = $c_Lzonedb_java_tzdb$asiky$.prototype;
var $d_Lzonedb_java_tzdb$asiky$ = new $TypeData().initClass({
  Lzonedb_java_tzdb$asiky$: 0
}, false, "zonedb.java.tzdb$asiky$", {
  Lzonedb_java_tzdb$asiky$: 1,
  O: 1
});
$c_Lzonedb_java_tzdb$asiky$.prototype.$classData = $d_Lzonedb_java_tzdb$asiky$;
var $n_Lzonedb_java_tzdb$asiky$;
function $m_Lzonedb_java_tzdb$asiky$() {
  if ((!$n_Lzonedb_java_tzdb$asiky$)) {
    $n_Lzonedb_java_tzdb$asiky$ = new $c_Lzonedb_java_tzdb$asiky$()
  };
  return $n_Lzonedb_java_tzdb$asiky$
}
function $p_jl_Character$__nonASCIIZeroDigitCodePoints$lzycompute__AI($thiz) {
  if (((((32 & $thiz.jl_Character$__f_bitmap$0) << 24) >> 24) === 0)) {
    $thiz.jl_Character$__f_nonASCIIZeroDigitCodePoints = new $ac_I(new Int32Array([1632, 1776, 1984, 2406, 2534, 2662, 2790, 2918, 3046, 3174, 3302, 3430, 3664, 3792, 3872, 4160, 4240, 6112, 6160, 6470, 6608, 6784, 6800, 6992, 7088, 7232, 7248, 42528, 43216, 43264, 43472, 43600, 44016, 65296, 66720, 69734, 69872, 69942, 70096, 71360, 120782, 120792, 120802, 120812, 120822]));
    $thiz.jl_Character$__f_bitmap$0 = (((32 | $thiz.jl_Character$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.jl_Character$__f_nonASCIIZeroDigitCodePoints
}
function $p_jl_Character$__nonASCIIZeroDigitCodePoints__AI($thiz) {
  return (((((32 & $thiz.jl_Character$__f_bitmap$0) << 24) >> 24) === 0) ? $p_jl_Character$__nonASCIIZeroDigitCodePoints$lzycompute__AI($thiz) : $thiz.jl_Character$__f_nonASCIIZeroDigitCodePoints)
}
/** @constructor */
function $c_jl_Character$() {
  this.jl_Character$__f_java$lang$Character$$charTypesFirst256 = null;
  this.jl_Character$__f_charTypeIndices = null;
  this.jl_Character$__f_charTypes = null;
  this.jl_Character$__f_isMirroredIndices = null;
  this.jl_Character$__f_combiningClassNoneOrAboveOrOtherIndices = null;
  this.jl_Character$__f_nonASCIIZeroDigitCodePoints = null;
  this.jl_Character$__f_bitmap$0 = 0
}
$c_jl_Character$.prototype = new $h_O();
$c_jl_Character$.prototype.constructor = $c_jl_Character$;
/** @constructor */
function $h_jl_Character$() {
  /*<skip>*/
}
$h_jl_Character$.prototype = $c_jl_Character$.prototype;
$c_jl_Character$.prototype.digitWithValidRadix__I__I__I = (function(codePoint, radix) {
  if ((codePoint < 256)) {
    var value = (((codePoint >= 48) && (codePoint <= 57)) ? (((-48) + codePoint) | 0) : (((codePoint >= 65) && (codePoint <= 90)) ? (((-55) + codePoint) | 0) : (((codePoint >= 97) && (codePoint <= 122)) ? (((-87) + codePoint) | 0) : (-1))))
  } else if (((codePoint >= 65313) && (codePoint <= 65338))) {
    var value = (((-65303) + codePoint) | 0)
  } else if (((codePoint >= 65345) && (codePoint <= 65370))) {
    var value = (((-65335) + codePoint) | 0)
  } else {
    var a = $p_jl_Character$__nonASCIIZeroDigitCodePoints__AI(this);
    var p = $m_ju_Arrays$().binarySearch__AI__I__I(a, codePoint);
    var zeroCodePointIndex = ((p < 0) ? (((-2) - p) | 0) : p);
    if ((zeroCodePointIndex < 0)) {
      var value = (-1)
    } else {
      var v = ((codePoint - $p_jl_Character$__nonASCIIZeroDigitCodePoints__AI(this).get(zeroCodePointIndex)) | 0);
      var value = ((v > 9) ? (-1) : v)
    }
  };
  return ((value < radix) ? value : (-1))
});
var $d_jl_Character$ = new $TypeData().initClass({
  jl_Character$: 0
}, false, "java.lang.Character$", {
  jl_Character$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_Character$.prototype.$classData = $d_jl_Character$;
var $n_jl_Character$;
function $m_jl_Character$() {
  if ((!$n_jl_Character$)) {
    $n_jl_Character$ = new $c_jl_Character$()
  };
  return $n_jl_Character$
}
function $p_jl_Integer$__fail$1__T__E($thiz, s$1) {
  throw new $c_jl_NumberFormatException((("For input string: \"" + s$1) + "\""))
}
/** @constructor */
function $c_jl_Integer$() {
  /*<skip>*/
}
$c_jl_Integer$.prototype = new $h_O();
$c_jl_Integer$.prototype.constructor = $c_jl_Integer$;
/** @constructor */
function $h_jl_Integer$() {
  /*<skip>*/
}
$h_jl_Integer$.prototype = $c_jl_Integer$.prototype;
$c_jl_Integer$.prototype.parseInt__T__I__I = (function(s, radix) {
  var len = ((s === null) ? 0 : $uI(s.length));
  if ((((len === 0) || (radix < 2)) || (radix > 36))) {
    $p_jl_Integer$__fail$1__T__E(this, s)
  };
  var firstChar = (65535 & $uI(s.charCodeAt(0)));
  var negative = (firstChar === 45);
  var maxAbsValue = (negative ? 2.147483648E9 : 2.147483647E9);
  var i = ((negative || (firstChar === 43)) ? 1 : 0);
  if ((i >= $uI(s.length))) {
    $p_jl_Integer$__fail$1__T__E(this, s)
  };
  var result = 0.0;
  while ((i !== len)) {
    var $$x1 = $m_jl_Character$();
    var index = i;
    var digit = $$x1.digitWithValidRadix__I__I__I((65535 & $uI(s.charCodeAt(index))), radix);
    result = ((result * radix) + digit);
    if (((digit === (-1)) || (result > maxAbsValue))) {
      $p_jl_Integer$__fail$1__T__E(this, s)
    };
    i = ((1 + i) | 0)
  };
  if (negative) {
    var n = (-result);
    return $uI((n | 0))
  } else {
    var n$1 = result;
    return $uI((n$1 | 0))
  }
});
$c_jl_Integer$.prototype.bitCount__I__I = (function(i) {
  var t1 = ((i - (1431655765 & (i >> 1))) | 0);
  var t2 = (((858993459 & t1) + (858993459 & (t1 >> 2))) | 0);
  return ($imul(16843009, (252645135 & ((t2 + (t2 >> 4)) | 0))) >> 24)
});
var $d_jl_Integer$ = new $TypeData().initClass({
  jl_Integer$: 0
}, false, "java.lang.Integer$", {
  jl_Integer$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_Integer$.prototype.$classData = $d_jl_Integer$;
var $n_jl_Integer$;
function $m_jl_Integer$() {
  if ((!$n_jl_Integer$)) {
    $n_jl_Integer$ = new $c_jl_Integer$()
  };
  return $n_jl_Integer$
}
/** @constructor */
function $c_jl_Number() {
  /*<skip>*/
}
$c_jl_Number.prototype = new $h_O();
$c_jl_Number.prototype.constructor = $c_jl_Number;
/** @constructor */
function $h_jl_Number() {
  /*<skip>*/
}
$h_jl_Number.prototype = $c_jl_Number.prototype;
function $is_jl_Number(obj) {
  return ((obj instanceof $c_jl_Number) || ((typeof obj) === "number"))
}
function $as_jl_Number(obj) {
  return (($is_jl_Number(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Number"))
}
function $isArrayOf_jl_Number(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Number)))
}
function $asArrayOf_jl_Number(obj, depth) {
  return (($isArrayOf_jl_Number(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Number;", depth))
}
/** @constructor */
function $c_jl_String$() {
  this.jl_String$__f_CASE_INSENSITIVE_ORDER = null;
  this.jl_String$__f_bitmap$0 = false
}
$c_jl_String$.prototype = new $h_O();
$c_jl_String$.prototype.constructor = $c_jl_String$;
/** @constructor */
function $h_jl_String$() {
  /*<skip>*/
}
$h_jl_String$.prototype = $c_jl_String$.prototype;
$c_jl_String$.prototype.new__AC__I__I__T = (function(value, offset, count) {
  var end = ((offset + count) | 0);
  if ((((offset < 0) || (end < offset)) || (end > value.u.length))) {
    throw $ct_jl_StringIndexOutOfBoundsException__(new $c_jl_StringIndexOutOfBoundsException())
  };
  var result = "";
  var i = offset;
  while ((i !== end)) {
    var $$x1 = result;
    var this$1 = value.get(i);
    result = (("" + $$x1) + $as_T(String.fromCharCode(this$1)));
    i = ((1 + i) | 0)
  };
  return result
});
var $d_jl_String$ = new $TypeData().initClass({
  jl_String$: 0
}, false, "java.lang.String$", {
  jl_String$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_String$.prototype.$classData = $d_jl_String$;
var $n_jl_String$;
function $m_jl_String$() {
  if ((!$n_jl_String$)) {
    $n_jl_String$ = new $c_jl_String$()
  };
  return $n_jl_String$
}
function $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, e, enableSuppression, writableStackTrace) {
  $thiz.jl_Throwable__f_s = s;
  $thiz.jl_Throwable__f_e = e;
  $thiz.jl_Throwable__f_enableSuppression = enableSuppression;
  $thiz.jl_Throwable__f_writableStackTrace = writableStackTrace;
  if (writableStackTrace) {
    $thiz.fillInStackTrace__jl_Throwable()
  };
  return $thiz
}
class $c_jl_Throwable extends Error {
  constructor() {
    super();
    this.jl_Throwable__f_s = null;
    this.jl_Throwable__f_e = null;
    this.jl_Throwable__f_enableSuppression = false;
    this.jl_Throwable__f_writableStackTrace = false;
    this.jl_Throwable__f_stackTraceStateInternal = null;
    this.jl_Throwable__f_stackTrace = null;
    this.jl_Throwable__f_suppressed = null
  };
  getMessage__T() {
    return this.jl_Throwable__f_s
  };
  fillInStackTrace__jl_Throwable() {
    var identifyingString = Object.prototype.toString.call(this);
    if ((identifyingString === "[object Error]")) {
      this.jl_Throwable__f_stackTraceStateInternal = this
    } else if ((Error.captureStackTrace === (void 0))) {
      var e = new Error();
      this.jl_Throwable__f_stackTraceStateInternal = e
    } else {
      Error.captureStackTrace(this);
      this.jl_Throwable__f_stackTraceStateInternal = this
    };
    return this
  };
  toString__T() {
    var className = $objectClassName(this);
    var message = this.getMessage__T();
    return ((message === null) ? className : ((className + ": ") + message))
  };
  $js$exported$meth$toString__O() {
    return this.toString__T()
  };
  $js$exported$prop$name__O() {
    return $objectClassName(this)
  };
  $js$exported$prop$message__O() {
    var m = this.getMessage__T();
    return ((m === null) ? "" : m)
  };
  hashCode__I() {
    return $c_O.prototype.hashCode__I.call(this)
  };
  equals__O__Z(that) {
    return $c_O.prototype.equals__O__Z.call(this, that)
  };
  get "message"() {
    return this.$js$exported$prop$message__O()
  };
  get "name"() {
    return this.$js$exported$prop$name__O()
  };
  "toString"() {
    return this.$js$exported$meth$toString__O()
  };
}
function $as_jl_Throwable(obj) {
  return (((obj instanceof $c_jl_Throwable) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Throwable"))
}
function $isArrayOf_jl_Throwable(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Throwable)))
}
function $asArrayOf_jl_Throwable(obj, depth) {
  return (($isArrayOf_jl_Throwable(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Throwable;", depth))
}
function $p_Ljava_time_Duration$__ZERO$lzycompute__Ljava_time_Duration($thiz) {
  if ((!$thiz.Ljava_time_Duration$__f_bitmap$0)) {
    $thiz.Ljava_time_Duration$__f_ZERO = new $c_Ljava_time_Duration($L0, 0);
    $thiz.Ljava_time_Duration$__f_bitmap$0 = true
  };
  return $thiz.Ljava_time_Duration$__f_ZERO
}
/** @constructor */
function $c_Ljava_time_Duration$() {
  this.Ljava_time_Duration$__f_ZERO = null;
  this.Ljava_time_Duration$__f_bitmap$0 = false
}
$c_Ljava_time_Duration$.prototype = new $h_O();
$c_Ljava_time_Duration$.prototype.constructor = $c_Ljava_time_Duration$;
/** @constructor */
function $h_Ljava_time_Duration$() {
  /*<skip>*/
}
$h_Ljava_time_Duration$.prototype = $c_Ljava_time_Duration$.prototype;
$c_Ljava_time_Duration$.prototype.ZERO__Ljava_time_Duration = (function() {
  return ((!this.Ljava_time_Duration$__f_bitmap$0) ? $p_Ljava_time_Duration$__ZERO$lzycompute__Ljava_time_Duration(this) : this.Ljava_time_Duration$__f_ZERO)
});
$c_Ljava_time_Duration$.prototype.ofSeconds__J__J__Ljava_time_Duration = (function(seconds, nanoAdjustment) {
  var t = $m_jl_Math$().floorDiv__J__J__J(nanoAdjustment, new $c_RTLong(1000000000, 0));
  var lo = t.RTLong__f_lo;
  var hi = t.RTLong__f_hi;
  var t$1 = $m_jl_Math$().addExact__J__J__J(seconds, new $c_RTLong(lo, hi));
  var lo$1 = t$1.RTLong__f_lo;
  var hi$1 = t$1.RTLong__f_hi;
  var this$1 = $m_jl_Math$().floorMod__J__J__J(nanoAdjustment, new $c_RTLong(1000000000, 0));
  var nos = this$1.RTLong__f_lo;
  return this.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(lo$1, hi$1), nos)
});
$c_Ljava_time_Duration$.prototype.java$time$Duration$$create__J__I__Ljava_time_Duration = (function(seconds, nanoAdjustment) {
  var hi = (nanoAdjustment >> 31);
  var lo = (seconds.RTLong__f_lo | nanoAdjustment);
  var hi$1 = (seconds.RTLong__f_hi | hi);
  if (((lo === 0) && (hi$1 === 0))) {
    return this.ZERO__Ljava_time_Duration()
  } else {
    return new $c_Ljava_time_Duration(seconds, nanoAdjustment)
  }
});
var $d_Ljava_time_Duration$ = new $TypeData().initClass({
  Ljava_time_Duration$: 0
}, false, "java.time.Duration$", {
  Ljava_time_Duration$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_Duration$.prototype.$classData = $d_Ljava_time_Duration$;
var $n_Ljava_time_Duration$;
function $m_Ljava_time_Duration$() {
  if ((!$n_Ljava_time_Duration$)) {
    $n_Ljava_time_Duration$ = new $c_Ljava_time_Duration$()
  };
  return $n_Ljava_time_Duration$
}
function $p_Ljava_time_LocalDate$__DAYS_PER_CYCLE__I($thiz) {
  if (((((1 & $thiz.Ljava_time_LocalDate$__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalDate.scala: 84")
  };
  return $thiz.Ljava_time_LocalDate$__f_DAYS_PER_CYCLE
}
function $p_Ljava_time_LocalDate$__create__I__Ljava_time_Month__I__Ljava_time_LocalDate($thiz, year, month, dayOfMonth) {
  if ((dayOfMonth > 28)) {
    var $$x2 = $m_Ljava_time_chrono_IsoChronology$().INSTANCE__Ljava_time_chrono_IsoChronology();
    var hi = (year >> 31);
    var $$x1 = (dayOfMonth > month.length__Z__I($$x2.isLeapYear__J__Z(new $c_RTLong(year, hi))))
  } else {
    var $$x1 = false
  };
  if ($$x1) {
    if ((dayOfMonth === 29)) {
      throw new $c_Ljava_time_DateTimeException((("Invalid date 'February 29' as '" + year) + "' is not a leap year"))
    } else {
      throw new $c_Ljava_time_DateTimeException((((("Invalid date '" + month.jl_Enum__f__name) + " ") + dayOfMonth) + "'"))
    }
  } else {
    return new $c_Ljava_time_LocalDate(year, month.getValue__I(), dayOfMonth)
  }
}
/** @constructor */
function $c_Ljava_time_LocalDate$() {
  this.Ljava_time_LocalDate$__f_MIN = null;
  this.Ljava_time_LocalDate$__f_MAX = null;
  this.Ljava_time_LocalDate$__f_DAYS_PER_CYCLE = 0;
  this.Ljava_time_LocalDate$__f_DAYS_0000_TO_1970 = $L0;
  this.Ljava_time_LocalDate$__f_bitmap$init$0 = 0;
  this.Ljava_time_LocalDate$__f_bitmap$0 = 0;
  $n_Ljava_time_LocalDate$ = this;
  this.Ljava_time_LocalDate$__f_DAYS_PER_CYCLE = 146097;
  this.Ljava_time_LocalDate$__f_bitmap$init$0 = (((1 | this.Ljava_time_LocalDate$__f_bitmap$init$0) << 24) >> 24);
  var value = $p_Ljava_time_LocalDate$__DAYS_PER_CYCLE__I(this);
  var hi = (value >> 31);
  var b0 = (65535 & value);
  var b1 = ((value >>> 16) | 0);
  var a0b0 = $imul(5, b0);
  var a0b1 = $imul(5, b1);
  var lo = ((a0b0 + (a0b1 << 16)) | 0);
  var c1part = ((((a0b0 >>> 16) | 0) + a0b1) | 0);
  var hi$1 = (($imul(5, hi) + ((c1part >>> 16) | 0)) | 0);
  var lo$1 = (((-10957) + lo) | 0);
  var hi$2 = ((((-2147483648) ^ lo$1) < 2147472691) ? hi$1 : (((-1) + hi$1) | 0));
  this.Ljava_time_LocalDate$__f_DAYS_0000_TO_1970 = new $c_RTLong(lo$1, hi$2);
  this.Ljava_time_LocalDate$__f_bitmap$init$0 = (((2 | this.Ljava_time_LocalDate$__f_bitmap$init$0) << 24) >> 24)
}
$c_Ljava_time_LocalDate$.prototype = new $h_O();
$c_Ljava_time_LocalDate$.prototype.constructor = $c_Ljava_time_LocalDate$;
/** @constructor */
function $h_Ljava_time_LocalDate$() {
  /*<skip>*/
}
$h_Ljava_time_LocalDate$.prototype = $c_Ljava_time_LocalDate$.prototype;
$c_Ljava_time_LocalDate$.prototype.of__I__I__I__Ljava_time_LocalDate = (function(year, month, dayOfMonth) {
  var this$2 = $m_Ljava_time_temporal_ChronoField$().YEAR__Ljava_time_temporal_ChronoField();
  var hi = (year >> 31);
  this$2.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(year, hi), this$2);
  var this$4 = $m_Ljava_time_temporal_ChronoField$().MONTH_OF_YEAR__Ljava_time_temporal_ChronoField();
  var hi$1 = (month >> 31);
  this$4.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(month, hi$1), this$4);
  var this$6 = $m_Ljava_time_temporal_ChronoField$().DAY_OF_MONTH__Ljava_time_temporal_ChronoField();
  var hi$2 = (dayOfMonth >> 31);
  this$6.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(dayOfMonth, hi$2), this$6);
  return $p_Ljava_time_LocalDate$__create__I__Ljava_time_Month__I__Ljava_time_LocalDate(this, year, $m_Ljava_time_Month$().of__I__Ljava_time_Month(month), dayOfMonth)
});
var $d_Ljava_time_LocalDate$ = new $TypeData().initClass({
  Ljava_time_LocalDate$: 0
}, false, "java.time.LocalDate$", {
  Ljava_time_LocalDate$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_LocalDate$.prototype.$classData = $d_Ljava_time_LocalDate$;
var $n_Ljava_time_LocalDate$;
function $m_Ljava_time_LocalDate$() {
  if ((!$n_Ljava_time_LocalDate$)) {
    $n_Ljava_time_LocalDate$ = new $c_Ljava_time_LocalDate$()
  };
  return $n_Ljava_time_LocalDate$
}
function $p_Ljava_time_LocalTime$__HOURS$lzycompute__ALjava_time_LocalTime($thiz) {
  if (((((1 & $thiz.Ljava_time_LocalTime$__f_bitmap$0) << 24) >> 24) === 0)) {
    var hours = new ($d_Ljava_time_LocalTime.getArrayOf().constr)(24);
    var i = 0;
    while ((i < hours.u.length)) {
      hours.set(i, new $c_Ljava_time_LocalTime(i, 0, 0, 0));
      i = ((1 + i) | 0)
    };
    $thiz.Ljava_time_LocalTime$__f_HOURS = hours;
    $thiz.Ljava_time_LocalTime$__f_bitmap$0 = (((1 | $thiz.Ljava_time_LocalTime$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.Ljava_time_LocalTime$__f_HOURS
}
function $p_Ljava_time_LocalTime$__HOURS__ALjava_time_LocalTime($thiz) {
  return (((((1 & $thiz.Ljava_time_LocalTime$__f_bitmap$0) << 24) >> 24) === 0) ? $p_Ljava_time_LocalTime$__HOURS$lzycompute__ALjava_time_LocalTime($thiz) : $thiz.Ljava_time_LocalTime$__f_HOURS)
}
/** @constructor */
function $c_Ljava_time_LocalTime$() {
  this.Ljava_time_LocalTime$__f_HOURS = null;
  this.Ljava_time_LocalTime$__f_MIN = null;
  this.Ljava_time_LocalTime$__f_MAX = null;
  this.Ljava_time_LocalTime$__f_MIDNIGHT = null;
  this.Ljava_time_LocalTime$__f_NOON = null;
  this.Ljava_time_LocalTime$__f_HOURS_PER_DAY = 0;
  this.Ljava_time_LocalTime$__f_MINUTES_PER_HOUR = 0;
  this.Ljava_time_LocalTime$__f_MINUTES_PER_DAY = 0;
  this.Ljava_time_LocalTime$__f_SECONDS_PER_MINUTE = 0;
  this.Ljava_time_LocalTime$__f_SECONDS_PER_HOUR = 0;
  this.Ljava_time_LocalTime$__f_SECONDS_PER_DAY = 0;
  this.Ljava_time_LocalTime$__f_MILLIS_PER_DAY = $L0;
  this.Ljava_time_LocalTime$__f_MICROS_PER_DAY = $L0;
  this.Ljava_time_LocalTime$__f_NANOS_PER_SECOND = $L0;
  this.Ljava_time_LocalTime$__f_NANOS_PER_MINUTE = $L0;
  this.Ljava_time_LocalTime$__f_NANOS_PER_HOUR = $L0;
  this.Ljava_time_LocalTime$__f_NANOS_PER_DAY = $L0;
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = 0;
  this.Ljava_time_LocalTime$__f_bitmap$0 = 0;
  $n_Ljava_time_LocalTime$ = this;
  this.Ljava_time_LocalTime$__f_HOURS_PER_DAY = 24;
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (1 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_MINUTES_PER_HOUR = 60;
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (2 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_MINUTES_PER_DAY = $imul(this.MINUTES_PER_HOUR__I(), this.HOURS_PER_DAY__I());
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (4 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_SECONDS_PER_MINUTE = 60;
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (8 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_SECONDS_PER_HOUR = $imul(this.SECONDS_PER_MINUTE__I(), this.MINUTES_PER_HOUR__I());
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (16 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_SECONDS_PER_DAY = $imul(this.SECONDS_PER_HOUR__I(), this.HOURS_PER_DAY__I());
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (32 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  var value = this.SECONDS_PER_DAY__I();
  var hi = (value >> 31);
  var b0 = (65535 & value);
  var b1 = ((value >>> 16) | 0);
  var a0b0 = $imul(1000, b0);
  var a0b1 = $imul(1000, b1);
  var lo = ((a0b0 + (a0b1 << 16)) | 0);
  var c1part = ((((a0b0 >>> 16) | 0) + a0b1) | 0);
  var hi$1 = (($imul(1000, hi) + ((c1part >>> 16) | 0)) | 0);
  this.Ljava_time_LocalTime$__f_MILLIS_PER_DAY = new $c_RTLong(lo, hi$1);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (64 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  var value$1 = this.SECONDS_PER_DAY__I();
  var hi$2 = (value$1 >> 31);
  var b0$1 = (65535 & value$1);
  var b1$1 = ((value$1 >>> 16) | 0);
  var a0b0$1 = $imul(16960, b0$1);
  var a1b0 = $imul(15, b0$1);
  var a0b1$1 = $imul(16960, b1$1);
  var lo$1 = ((a0b0$1 + (((a1b0 + a0b1$1) | 0) << 16)) | 0);
  var c1part$1 = ((((a0b0$1 >>> 16) | 0) + a0b1$1) | 0);
  var hi$3 = (((((($imul(1000000, hi$2) + $imul(15, b1$1)) | 0) + ((c1part$1 >>> 16) | 0)) | 0) + (((((65535 & c1part$1) + a1b0) | 0) >>> 16) | 0)) | 0);
  this.Ljava_time_LocalTime$__f_MICROS_PER_DAY = new $c_RTLong(lo$1, hi$3);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (128 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  this.Ljava_time_LocalTime$__f_NANOS_PER_SECOND = new $c_RTLong(1000000000, 0);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (256 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  var this$4 = this.NANOS_PER_SECOND__J();
  var value$2 = this.SECONDS_PER_MINUTE__I();
  var hi$4 = (value$2 >> 31);
  var alo = this$4.RTLong__f_lo;
  var a0 = (65535 & alo);
  var a1 = ((alo >>> 16) | 0);
  var b0$2 = (65535 & value$2);
  var b1$2 = ((value$2 >>> 16) | 0);
  var a0b0$2 = $imul(a0, b0$2);
  var a1b0$1 = $imul(a1, b0$2);
  var a0b1$2 = $imul(a0, b1$2);
  var lo$2 = ((a0b0$2 + (((a1b0$1 + a0b1$2) | 0) << 16)) | 0);
  var c1part$2 = ((((a0b0$2 >>> 16) | 0) + a0b1$2) | 0);
  var hi$5 = (((((((($imul(alo, hi$4) + $imul(this$4.RTLong__f_hi, value$2)) | 0) + $imul(a1, b1$2)) | 0) + ((c1part$2 >>> 16) | 0)) | 0) + (((((65535 & c1part$2) + a1b0$1) | 0) >>> 16) | 0)) | 0);
  this.Ljava_time_LocalTime$__f_NANOS_PER_MINUTE = new $c_RTLong(lo$2, hi$5);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (512 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  var this$6 = this.NANOS_PER_MINUTE__J();
  var value$3 = this.MINUTES_PER_HOUR__I();
  var hi$6 = (value$3 >> 31);
  var alo$1 = this$6.RTLong__f_lo;
  var a0$1 = (65535 & alo$1);
  var a1$1 = ((alo$1 >>> 16) | 0);
  var b0$3 = (65535 & value$3);
  var b1$3 = ((value$3 >>> 16) | 0);
  var a0b0$3 = $imul(a0$1, b0$3);
  var a1b0$2 = $imul(a1$1, b0$3);
  var a0b1$3 = $imul(a0$1, b1$3);
  var lo$3 = ((a0b0$3 + (((a1b0$2 + a0b1$3) | 0) << 16)) | 0);
  var c1part$3 = ((((a0b0$3 >>> 16) | 0) + a0b1$3) | 0);
  var hi$7 = (((((((($imul(alo$1, hi$6) + $imul(this$6.RTLong__f_hi, value$3)) | 0) + $imul(a1$1, b1$3)) | 0) + ((c1part$3 >>> 16) | 0)) | 0) + (((((65535 & c1part$3) + a1b0$2) | 0) >>> 16) | 0)) | 0);
  this.Ljava_time_LocalTime$__f_NANOS_PER_HOUR = new $c_RTLong(lo$3, hi$7);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (1024 | this.Ljava_time_LocalTime$__f_bitmap$init$0);
  var this$8 = this.NANOS_PER_HOUR__J();
  var value$4 = this.HOURS_PER_DAY__I();
  var hi$8 = (value$4 >> 31);
  var alo$2 = this$8.RTLong__f_lo;
  var a0$2 = (65535 & alo$2);
  var a1$2 = ((alo$2 >>> 16) | 0);
  var b0$4 = (65535 & value$4);
  var b1$4 = ((value$4 >>> 16) | 0);
  var a0b0$4 = $imul(a0$2, b0$4);
  var a1b0$3 = $imul(a1$2, b0$4);
  var a0b1$4 = $imul(a0$2, b1$4);
  var lo$4 = ((a0b0$4 + (((a1b0$3 + a0b1$4) | 0) << 16)) | 0);
  var c1part$4 = ((((a0b0$4 >>> 16) | 0) + a0b1$4) | 0);
  var hi$9 = (((((((($imul(alo$2, hi$8) + $imul(this$8.RTLong__f_hi, value$4)) | 0) + $imul(a1$2, b1$4)) | 0) + ((c1part$4 >>> 16) | 0)) | 0) + (((((65535 & c1part$4) + a1b0$3) | 0) >>> 16) | 0)) | 0);
  this.Ljava_time_LocalTime$__f_NANOS_PER_DAY = new $c_RTLong(lo$4, hi$9);
  this.Ljava_time_LocalTime$__f_bitmap$init$0 = (2048 | this.Ljava_time_LocalTime$__f_bitmap$init$0)
}
$c_Ljava_time_LocalTime$.prototype = new $h_O();
$c_Ljava_time_LocalTime$.prototype.constructor = $c_Ljava_time_LocalTime$;
/** @constructor */
function $h_Ljava_time_LocalTime$() {
  /*<skip>*/
}
$h_Ljava_time_LocalTime$.prototype = $c_Ljava_time_LocalTime$.prototype;
$c_Ljava_time_LocalTime$.prototype.HOURS_PER_DAY__I = (function() {
  if (((1 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 90")
  };
  return this.Ljava_time_LocalTime$__f_HOURS_PER_DAY
});
$c_Ljava_time_LocalTime$.prototype.MINUTES_PER_HOUR__I = (function() {
  if (((2 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 93")
  };
  return this.Ljava_time_LocalTime$__f_MINUTES_PER_HOUR
});
$c_Ljava_time_LocalTime$.prototype.SECONDS_PER_MINUTE__I = (function() {
  if (((8 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 99")
  };
  return this.Ljava_time_LocalTime$__f_SECONDS_PER_MINUTE
});
$c_Ljava_time_LocalTime$.prototype.SECONDS_PER_HOUR__I = (function() {
  if (((16 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 102")
  };
  return this.Ljava_time_LocalTime$__f_SECONDS_PER_HOUR
});
$c_Ljava_time_LocalTime$.prototype.SECONDS_PER_DAY__I = (function() {
  if (((32 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 105")
  };
  return this.Ljava_time_LocalTime$__f_SECONDS_PER_DAY
});
$c_Ljava_time_LocalTime$.prototype.NANOS_PER_SECOND__J = (function() {
  if (((256 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 114")
  };
  return this.Ljava_time_LocalTime$__f_NANOS_PER_SECOND
});
$c_Ljava_time_LocalTime$.prototype.NANOS_PER_MINUTE__J = (function() {
  if (((512 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 117")
  };
  return this.Ljava_time_LocalTime$__f_NANOS_PER_MINUTE
});
$c_Ljava_time_LocalTime$.prototype.NANOS_PER_HOUR__J = (function() {
  if (((1024 & this.Ljava_time_LocalTime$__f_bitmap$init$0) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 120")
  };
  return this.Ljava_time_LocalTime$__f_NANOS_PER_HOUR
});
$c_Ljava_time_LocalTime$.prototype.of__I__I__I__Ljava_time_LocalTime = (function(hour, minute, second) {
  var this$2 = $m_Ljava_time_temporal_ChronoField$().HOUR_OF_DAY__Ljava_time_temporal_ChronoField();
  var hi = (hour >> 31);
  this$2.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(hour, hi), this$2);
  if (((minute | second) === 0)) {
    return $p_Ljava_time_LocalTime$__HOURS__ALjava_time_LocalTime(this).get(hour)
  };
  var this$4 = $m_Ljava_time_temporal_ChronoField$().MINUTE_OF_HOUR__Ljava_time_temporal_ChronoField();
  var hi$1 = (minute >> 31);
  this$4.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(minute, hi$1), this$4);
  var this$6 = $m_Ljava_time_temporal_ChronoField$().SECOND_OF_MINUTE__Ljava_time_temporal_ChronoField();
  var hi$2 = (second >> 31);
  this$6.Ljava_time_temporal_ChronoField__f__range.checkValidValue__J__Ljava_time_temporal_TemporalField__J(new $c_RTLong(second, hi$2), this$6);
  return new $c_Ljava_time_LocalTime(hour, minute, second, 0)
});
var $d_Ljava_time_LocalTime$ = new $TypeData().initClass({
  Ljava_time_LocalTime$: 0
}, false, "java.time.LocalTime$", {
  Ljava_time_LocalTime$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_LocalTime$.prototype.$classData = $d_Ljava_time_LocalTime$;
var $n_Ljava_time_LocalTime$;
function $m_Ljava_time_LocalTime$() {
  if ((!$n_Ljava_time_LocalTime$)) {
    $n_Ljava_time_LocalTime$ = new $c_Ljava_time_LocalTime$()
  };
  return $n_Ljava_time_LocalTime$
}
function $p_Ljava_time_Month$__JANUARY$lzycompute__Ljava_time_Month($thiz) {
  if (((1 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_JANUARY = new $c_Ljava_time_Month("JANUARY", 0);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (1 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_JANUARY
}
function $p_Ljava_time_Month$__FEBRUARY$lzycompute__Ljava_time_Month($thiz) {
  if (((2 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_FEBRUARY = new $c_Ljava_time_Month("FEBRUARY", 1);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (2 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_FEBRUARY
}
function $p_Ljava_time_Month$__MARCH$lzycompute__Ljava_time_Month($thiz) {
  if (((4 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_MARCH = new $c_Ljava_time_Month("MARCH", 2);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (4 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_MARCH
}
function $p_Ljava_time_Month$__APRIL$lzycompute__Ljava_time_Month($thiz) {
  if (((8 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_APRIL = new $c_Ljava_time_Month("APRIL", 3);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (8 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_APRIL
}
function $p_Ljava_time_Month$__MAY$lzycompute__Ljava_time_Month($thiz) {
  if (((16 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_MAY = new $c_Ljava_time_Month("MAY", 4);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (16 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_MAY
}
function $p_Ljava_time_Month$__JUNE$lzycompute__Ljava_time_Month($thiz) {
  if (((32 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_JUNE = new $c_Ljava_time_Month("JUNE", 5);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (32 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_JUNE
}
function $p_Ljava_time_Month$__JULY$lzycompute__Ljava_time_Month($thiz) {
  if (((64 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_JULY = new $c_Ljava_time_Month("JULY", 6);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (64 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_JULY
}
function $p_Ljava_time_Month$__AUGUST$lzycompute__Ljava_time_Month($thiz) {
  if (((128 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_AUGUST = new $c_Ljava_time_Month("AUGUST", 7);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (128 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_AUGUST
}
function $p_Ljava_time_Month$__SEPTEMBER$lzycompute__Ljava_time_Month($thiz) {
  if (((256 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_SEPTEMBER = new $c_Ljava_time_Month("SEPTEMBER", 8);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (256 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_SEPTEMBER
}
function $p_Ljava_time_Month$__OCTOBER$lzycompute__Ljava_time_Month($thiz) {
  if (((512 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_OCTOBER = new $c_Ljava_time_Month("OCTOBER", 9);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (512 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_OCTOBER
}
function $p_Ljava_time_Month$__DECEMBER$lzycompute__Ljava_time_Month($thiz) {
  if (((1024 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_DECEMBER = new $c_Ljava_time_Month("DECEMBER", 11);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (1024 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_DECEMBER
}
function $p_Ljava_time_Month$__values$lzycompute__ALjava_time_Month($thiz) {
  if (((2048 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_values = new ($d_Ljava_time_Month.getArrayOf().constr)([$thiz.JANUARY__Ljava_time_Month(), $thiz.FEBRUARY__Ljava_time_Month(), $thiz.MARCH__Ljava_time_Month(), $thiz.APRIL__Ljava_time_Month(), $thiz.MAY__Ljava_time_Month(), $thiz.JUNE__Ljava_time_Month(), $thiz.JULY__Ljava_time_Month(), $thiz.AUGUST__Ljava_time_Month(), $thiz.SEPTEMBER__Ljava_time_Month(), $thiz.OCTOBER__Ljava_time_Month(), $thiz.NOVEMBER__Ljava_time_Month(), $thiz.DECEMBER__Ljava_time_Month()]);
    $thiz.Ljava_time_Month$__f_bitmap$0 = (2048 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_values
}
function $p_Ljava_time_Month$__ENUMS$lzycompute__ALjava_time_Month($thiz) {
  if (((4096 & $thiz.Ljava_time_Month$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_Month$__f_java$time$Month$$ENUMS = $m_Ljava_time_Month$().values__ALjava_time_Month();
    $thiz.Ljava_time_Month$__f_bitmap$0 = (4096 | $thiz.Ljava_time_Month$__f_bitmap$0)
  };
  return $thiz.Ljava_time_Month$__f_java$time$Month$$ENUMS
}
/** @constructor */
function $c_Ljava_time_Month$() {
  this.Ljava_time_Month$__f_JANUARY = null;
  this.Ljava_time_Month$__f_FEBRUARY = null;
  this.Ljava_time_Month$__f_MARCH = null;
  this.Ljava_time_Month$__f_APRIL = null;
  this.Ljava_time_Month$__f_MAY = null;
  this.Ljava_time_Month$__f_JUNE = null;
  this.Ljava_time_Month$__f_JULY = null;
  this.Ljava_time_Month$__f_AUGUST = null;
  this.Ljava_time_Month$__f_SEPTEMBER = null;
  this.Ljava_time_Month$__f_OCTOBER = null;
  this.Ljava_time_Month$__f_DECEMBER = null;
  this.Ljava_time_Month$__f_values = null;
  this.Ljava_time_Month$__f_java$time$Month$$ENUMS = null;
  this.Ljava_time_Month$__f_NOVEMBER = null;
  this.Ljava_time_Month$__f_bitmap$init$0 = false;
  this.Ljava_time_Month$__f_bitmap$0 = 0;
  $n_Ljava_time_Month$ = this;
  this.Ljava_time_Month$__f_NOVEMBER = new $c_Ljava_time_Month("NOVEMBER", 10);
  this.Ljava_time_Month$__f_bitmap$init$0 = true
}
$c_Ljava_time_Month$.prototype = new $h_O();
$c_Ljava_time_Month$.prototype.constructor = $c_Ljava_time_Month$;
/** @constructor */
function $h_Ljava_time_Month$() {
  /*<skip>*/
}
$h_Ljava_time_Month$.prototype = $c_Ljava_time_Month$.prototype;
$c_Ljava_time_Month$.prototype.JANUARY__Ljava_time_Month = (function() {
  return (((1 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__JANUARY$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_JANUARY)
});
$c_Ljava_time_Month$.prototype.FEBRUARY__Ljava_time_Month = (function() {
  return (((2 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__FEBRUARY$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_FEBRUARY)
});
$c_Ljava_time_Month$.prototype.MARCH__Ljava_time_Month = (function() {
  return (((4 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__MARCH$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_MARCH)
});
$c_Ljava_time_Month$.prototype.APRIL__Ljava_time_Month = (function() {
  return (((8 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__APRIL$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_APRIL)
});
$c_Ljava_time_Month$.prototype.MAY__Ljava_time_Month = (function() {
  return (((16 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__MAY$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_MAY)
});
$c_Ljava_time_Month$.prototype.JUNE__Ljava_time_Month = (function() {
  return (((32 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__JUNE$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_JUNE)
});
$c_Ljava_time_Month$.prototype.JULY__Ljava_time_Month = (function() {
  return (((64 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__JULY$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_JULY)
});
$c_Ljava_time_Month$.prototype.AUGUST__Ljava_time_Month = (function() {
  return (((128 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__AUGUST$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_AUGUST)
});
$c_Ljava_time_Month$.prototype.SEPTEMBER__Ljava_time_Month = (function() {
  return (((256 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__SEPTEMBER$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_SEPTEMBER)
});
$c_Ljava_time_Month$.prototype.OCTOBER__Ljava_time_Month = (function() {
  return (((512 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__OCTOBER$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_OCTOBER)
});
$c_Ljava_time_Month$.prototype.NOVEMBER__Ljava_time_Month = (function() {
  if ((!this.Ljava_time_Month$__f_bitmap$init$0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/Month.scala: 140")
  };
  return this.Ljava_time_Month$__f_NOVEMBER
});
$c_Ljava_time_Month$.prototype.DECEMBER__Ljava_time_Month = (function() {
  return (((1024 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__DECEMBER$lzycompute__Ljava_time_Month(this) : this.Ljava_time_Month$__f_DECEMBER)
});
$c_Ljava_time_Month$.prototype.values__ALjava_time_Month = (function() {
  return (((2048 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__values$lzycompute__ALjava_time_Month(this) : this.Ljava_time_Month$__f_values)
});
$c_Ljava_time_Month$.prototype.java$time$Month$$ENUMS__ALjava_time_Month = (function() {
  return (((4096 & this.Ljava_time_Month$__f_bitmap$0) === 0) ? $p_Ljava_time_Month$__ENUMS$lzycompute__ALjava_time_Month(this) : this.Ljava_time_Month$__f_java$time$Month$$ENUMS)
});
$c_Ljava_time_Month$.prototype.of__I__Ljava_time_Month = (function(month) {
  if (((month < 1) || (month > 12))) {
    throw new $c_Ljava_time_DateTimeException(("Invalid value for MonthOfYear: " + month))
  } else {
    return this.java$time$Month$$ENUMS__ALjava_time_Month().get((((-1) + month) | 0))
  }
});
var $d_Ljava_time_Month$ = new $TypeData().initClass({
  Ljava_time_Month$: 0
}, false, "java.time.Month$", {
  Ljava_time_Month$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_Month$.prototype.$classData = $d_Ljava_time_Month$;
var $n_Ljava_time_Month$;
function $m_Ljava_time_Month$() {
  if ((!$n_Ljava_time_Month$)) {
    $n_Ljava_time_Month$ = new $c_Ljava_time_Month$()
  };
  return $n_Ljava_time_Month$
}
function $p_Ljava_time_Year$__MIN_VALUE$lzycompute__I($thiz) {
  if (((((1 & $thiz.Ljava_time_Year$__f_bitmap$0) << 24) >> 24) === 0)) {
    $thiz.Ljava_time_Year$__f_MIN_VALUE = (-999999999);
    $thiz.Ljava_time_Year$__f_bitmap$0 = (((1 | $thiz.Ljava_time_Year$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.Ljava_time_Year$__f_MIN_VALUE
}
function $p_Ljava_time_Year$__MAX_VALUE$lzycompute__I($thiz) {
  if (((((2 & $thiz.Ljava_time_Year$__f_bitmap$0) << 24) >> 24) === 0)) {
    $thiz.Ljava_time_Year$__f_MAX_VALUE = 999999999;
    $thiz.Ljava_time_Year$__f_bitmap$0 = (((2 | $thiz.Ljava_time_Year$__f_bitmap$0) << 24) >> 24)
  };
  return $thiz.Ljava_time_Year$__f_MAX_VALUE
}
/** @constructor */
function $c_Ljava_time_Year$() {
  this.Ljava_time_Year$__f_MIN_VALUE = 0;
  this.Ljava_time_Year$__f_MAX_VALUE = 0;
  this.Ljava_time_Year$__f_PARSER = null;
  this.Ljava_time_Year$__f_bitmap$0 = 0
}
$c_Ljava_time_Year$.prototype = new $h_O();
$c_Ljava_time_Year$.prototype.constructor = $c_Ljava_time_Year$;
/** @constructor */
function $h_Ljava_time_Year$() {
  /*<skip>*/
}
$h_Ljava_time_Year$.prototype = $c_Ljava_time_Year$.prototype;
$c_Ljava_time_Year$.prototype.MIN_VALUE__I = (function() {
  return (((((1 & this.Ljava_time_Year$__f_bitmap$0) << 24) >> 24) === 0) ? $p_Ljava_time_Year$__MIN_VALUE$lzycompute__I(this) : this.Ljava_time_Year$__f_MIN_VALUE)
});
$c_Ljava_time_Year$.prototype.MAX_VALUE__I = (function() {
  return (((((2 & this.Ljava_time_Year$__f_bitmap$0) << 24) >> 24) === 0) ? $p_Ljava_time_Year$__MAX_VALUE$lzycompute__I(this) : this.Ljava_time_Year$__f_MAX_VALUE)
});
var $d_Ljava_time_Year$ = new $TypeData().initClass({
  Ljava_time_Year$: 0
}, false, "java.time.Year$", {
  Ljava_time_Year$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_Year$.prototype.$classData = $d_Ljava_time_Year$;
var $n_Ljava_time_Year$;
function $m_Ljava_time_Year$() {
  if ((!$n_Ljava_time_Year$)) {
    $n_Ljava_time_Year$ = new $c_Ljava_time_Year$()
  };
  return $n_Ljava_time_Year$
}
function $p_Ljava_time_chrono_IsoChronology$__INSTANCE$lzycompute__Ljava_time_chrono_IsoChronology($thiz) {
  if ((!$thiz.Ljava_time_chrono_IsoChronology$__f_bitmap$0)) {
    $thiz.Ljava_time_chrono_IsoChronology$__f_INSTANCE = new $c_Ljava_time_chrono_IsoChronology();
    $thiz.Ljava_time_chrono_IsoChronology$__f_bitmap$0 = true
  };
  return $thiz.Ljava_time_chrono_IsoChronology$__f_INSTANCE
}
/** @constructor */
function $c_Ljava_time_chrono_IsoChronology$() {
  this.Ljava_time_chrono_IsoChronology$__f_INSTANCE = null;
  this.Ljava_time_chrono_IsoChronology$__f_bitmap$0 = false
}
$c_Ljava_time_chrono_IsoChronology$.prototype = new $h_O();
$c_Ljava_time_chrono_IsoChronology$.prototype.constructor = $c_Ljava_time_chrono_IsoChronology$;
/** @constructor */
function $h_Ljava_time_chrono_IsoChronology$() {
  /*<skip>*/
}
$h_Ljava_time_chrono_IsoChronology$.prototype = $c_Ljava_time_chrono_IsoChronology$.prototype;
$c_Ljava_time_chrono_IsoChronology$.prototype.INSTANCE__Ljava_time_chrono_IsoChronology = (function() {
  return ((!this.Ljava_time_chrono_IsoChronology$__f_bitmap$0) ? $p_Ljava_time_chrono_IsoChronology$__INSTANCE$lzycompute__Ljava_time_chrono_IsoChronology(this) : this.Ljava_time_chrono_IsoChronology$__f_INSTANCE)
});
var $d_Ljava_time_chrono_IsoChronology$ = new $TypeData().initClass({
  Ljava_time_chrono_IsoChronology$: 0
}, false, "java.time.chrono.IsoChronology$", {
  Ljava_time_chrono_IsoChronology$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_chrono_IsoChronology$.prototype.$classData = $d_Ljava_time_chrono_IsoChronology$;
var $n_Ljava_time_chrono_IsoChronology$;
function $m_Ljava_time_chrono_IsoChronology$() {
  if ((!$n_Ljava_time_chrono_IsoChronology$)) {
    $n_Ljava_time_chrono_IsoChronology$ = new $c_Ljava_time_chrono_IsoChronology$()
  };
  return $n_Ljava_time_chrono_IsoChronology$
}
function $p_Ljava_time_temporal_ChronoField$__SECOND_OF_MINUTE$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((64 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_temporal_ChronoField$__f_SECOND_OF_MINUTE = new $c_Ljava_time_temporal_ChronoField("SecondOfMinute", 6, $m_Ljava_time_temporal_ChronoUnit$().SECONDS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ChronoUnit$().MINUTES__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ValueRange$().of__J__J__Ljava_time_temporal_ValueRange($L0, new $c_RTLong(59, 0)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (64 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_SECOND_OF_MINUTE
}
function $p_Ljava_time_temporal_ChronoField$__MINUTE_OF_HOUR$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((256 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_temporal_ChronoField$__f_MINUTE_OF_HOUR = new $c_Ljava_time_temporal_ChronoField("MinuteOfHour", 8, $m_Ljava_time_temporal_ChronoUnit$().MINUTES__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ChronoUnit$().HOURS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ValueRange$().of__J__J__Ljava_time_temporal_ValueRange($L0, new $c_RTLong(59, 0)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (256 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_MINUTE_OF_HOUR
}
function $p_Ljava_time_temporal_ChronoField$__HOUR_OF_DAY$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((4096 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_temporal_ChronoField$__f_HOUR_OF_DAY = new $c_Ljava_time_temporal_ChronoField("HourOfDay", 12, $m_Ljava_time_temporal_ChronoUnit$().HOURS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ChronoUnit$().DAYS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ValueRange$().of__J__J__Ljava_time_temporal_ValueRange($L0, new $c_RTLong(23, 0)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (4096 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_HOUR_OF_DAY
}
function $p_Ljava_time_temporal_ChronoField$__DAY_OF_MONTH$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((262144 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    var $$x2 = $m_Ljava_time_temporal_ChronoUnit$().DAYS__Ljava_time_temporal_ChronoUnit();
    var $$x1 = $m_Ljava_time_temporal_ChronoUnit$().MONTHS__Ljava_time_temporal_ChronoUnit();
    var this$1 = $m_Ljava_time_temporal_ValueRange$();
    $thiz.Ljava_time_temporal_ChronoField$__f_DAY_OF_MONTH = new $c_Ljava_time_temporal_ChronoField("DayOfMonth", 18, $$x2, $$x1, this$1.of__J__J__J__J__Ljava_time_temporal_ValueRange(new $c_RTLong(1, 0), new $c_RTLong(1, 0), new $c_RTLong(28, 0), new $c_RTLong(31, 0)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (262144 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_DAY_OF_MONTH
}
function $p_Ljava_time_temporal_ChronoField$__MONTH_OF_YEAR$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((8388608 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_temporal_ChronoField$__f_MONTH_OF_YEAR = new $c_Ljava_time_temporal_ChronoField("MonthOfYear", 23, $m_Ljava_time_temporal_ChronoUnit$().MONTHS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ChronoUnit$().YEARS__Ljava_time_temporal_ChronoUnit(), $m_Ljava_time_temporal_ValueRange$().of__J__J__Ljava_time_temporal_ValueRange(new $c_RTLong(1, 0), new $c_RTLong(12, 0)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (8388608 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_MONTH_OF_YEAR
}
function $p_Ljava_time_temporal_ChronoField$__YEAR$lzycompute__Ljava_time_temporal_ChronoField($thiz) {
  if (((67108864 & $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0)) {
    var $$x4 = $m_Ljava_time_temporal_ChronoUnit$().YEARS__Ljava_time_temporal_ChronoUnit();
    var $$x3 = $m_Ljava_time_temporal_ChronoUnit$().FOREVER__Ljava_time_temporal_ChronoUnit();
    var $$x2 = $m_Ljava_time_temporal_ValueRange$();
    var value = $m_Ljava_time_Year$().MIN_VALUE__I();
    var hi = (value >> 31);
    var $$x1 = new $c_RTLong(value, hi);
    var value$1 = $m_Ljava_time_Year$().MAX_VALUE__I();
    var hi$1 = (value$1 >> 31);
    $thiz.Ljava_time_temporal_ChronoField$__f_YEAR = new $c_Ljava_time_temporal_ChronoField("Year", 26, $$x4, $$x3, $$x2.of__J__J__Ljava_time_temporal_ValueRange($$x1, new $c_RTLong(value$1, hi$1)));
    $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0 = (67108864 | $thiz.Ljava_time_temporal_ChronoField$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoField$__f_YEAR
}
/** @constructor */
function $c_Ljava_time_temporal_ChronoField$() {
  this.Ljava_time_temporal_ChronoField$__f_NANO_OF_SECOND = null;
  this.Ljava_time_temporal_ChronoField$__f_NANO_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_MICRO_OF_SECOND = null;
  this.Ljava_time_temporal_ChronoField$__f_MICRO_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_MILLI_OF_SECOND = null;
  this.Ljava_time_temporal_ChronoField$__f_MILLI_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_SECOND_OF_MINUTE = null;
  this.Ljava_time_temporal_ChronoField$__f_SECOND_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_MINUTE_OF_HOUR = null;
  this.Ljava_time_temporal_ChronoField$__f_MINUTE_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_HOUR_OF_AMPM = null;
  this.Ljava_time_temporal_ChronoField$__f_CLOCK_HOUR_OF_AMPM = null;
  this.Ljava_time_temporal_ChronoField$__f_HOUR_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_CLOCK_HOUR_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_AMPM_OF_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_DAY_OF_WEEK = null;
  this.Ljava_time_temporal_ChronoField$__f_ALIGNED_DAY_OF_WEEK_IN_MONTH = null;
  this.Ljava_time_temporal_ChronoField$__f_ALIGNED_DAY_OF_WEEK_IN_YEAR = null;
  this.Ljava_time_temporal_ChronoField$__f_DAY_OF_MONTH = null;
  this.Ljava_time_temporal_ChronoField$__f_DAY_OF_YEAR = null;
  this.Ljava_time_temporal_ChronoField$__f_EPOCH_DAY = null;
  this.Ljava_time_temporal_ChronoField$__f_ALIGNED_WEEK_OF_MONTH = null;
  this.Ljava_time_temporal_ChronoField$__f_ALIGNED_WEEK_OF_YEAR = null;
  this.Ljava_time_temporal_ChronoField$__f_MONTH_OF_YEAR = null;
  this.Ljava_time_temporal_ChronoField$__f_PROLEPTIC_MONTH = null;
  this.Ljava_time_temporal_ChronoField$__f_YEAR_OF_ERA = null;
  this.Ljava_time_temporal_ChronoField$__f_YEAR = null;
  this.Ljava_time_temporal_ChronoField$__f_ERA = null;
  this.Ljava_time_temporal_ChronoField$__f_INSTANT_SECONDS = null;
  this.Ljava_time_temporal_ChronoField$__f_OFFSET_SECONDS = null;
  this.Ljava_time_temporal_ChronoField$__f_values = null;
  this.Ljava_time_temporal_ChronoField$__f_bitmap$0 = 0
}
$c_Ljava_time_temporal_ChronoField$.prototype = new $h_O();
$c_Ljava_time_temporal_ChronoField$.prototype.constructor = $c_Ljava_time_temporal_ChronoField$;
/** @constructor */
function $h_Ljava_time_temporal_ChronoField$() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ChronoField$.prototype = $c_Ljava_time_temporal_ChronoField$.prototype;
$c_Ljava_time_temporal_ChronoField$.prototype.SECOND_OF_MINUTE__Ljava_time_temporal_ChronoField = (function() {
  return (((64 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__SECOND_OF_MINUTE$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_SECOND_OF_MINUTE)
});
$c_Ljava_time_temporal_ChronoField$.prototype.MINUTE_OF_HOUR__Ljava_time_temporal_ChronoField = (function() {
  return (((256 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__MINUTE_OF_HOUR$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_MINUTE_OF_HOUR)
});
$c_Ljava_time_temporal_ChronoField$.prototype.HOUR_OF_DAY__Ljava_time_temporal_ChronoField = (function() {
  return (((4096 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__HOUR_OF_DAY$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_HOUR_OF_DAY)
});
$c_Ljava_time_temporal_ChronoField$.prototype.DAY_OF_MONTH__Ljava_time_temporal_ChronoField = (function() {
  return (((262144 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__DAY_OF_MONTH$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_DAY_OF_MONTH)
});
$c_Ljava_time_temporal_ChronoField$.prototype.MONTH_OF_YEAR__Ljava_time_temporal_ChronoField = (function() {
  return (((8388608 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__MONTH_OF_YEAR$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_MONTH_OF_YEAR)
});
$c_Ljava_time_temporal_ChronoField$.prototype.YEAR__Ljava_time_temporal_ChronoField = (function() {
  return (((67108864 & this.Ljava_time_temporal_ChronoField$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoField$__YEAR$lzycompute__Ljava_time_temporal_ChronoField(this) : this.Ljava_time_temporal_ChronoField$__f_YEAR)
});
var $d_Ljava_time_temporal_ChronoField$ = new $TypeData().initClass({
  Ljava_time_temporal_ChronoField$: 0
}, false, "java.time.temporal.ChronoField$", {
  Ljava_time_temporal_ChronoField$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_temporal_ChronoField$.prototype.$classData = $d_Ljava_time_temporal_ChronoField$;
var $n_Ljava_time_temporal_ChronoField$;
function $m_Ljava_time_temporal_ChronoField$() {
  if ((!$n_Ljava_time_temporal_ChronoField$)) {
    $n_Ljava_time_temporal_ChronoField$ = new $c_Ljava_time_temporal_ChronoField$()
  };
  return $n_Ljava_time_temporal_ChronoField$
}
function $p_Ljava_time_temporal_ChronoUnit$__SECONDS$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((8 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_SECONDS = new $c_Ljava_time_temporal_ChronoUnit("Seconds", 3, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(1, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (8 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_SECONDS
}
function $p_Ljava_time_temporal_ChronoUnit$__MINUTES$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((16 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_MINUTES = new $c_Ljava_time_temporal_ChronoUnit("Minutes", 4, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(60, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (16 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_MINUTES
}
function $p_Ljava_time_temporal_ChronoUnit$__HOURS$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((32 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_HOURS = new $c_Ljava_time_temporal_ChronoUnit("Hours", 5, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(3600, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (32 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_HOURS
}
function $p_Ljava_time_temporal_ChronoUnit$__DAYS$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((128 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_DAYS = new $c_Ljava_time_temporal_ChronoUnit("Days", 7, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(86400, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (128 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_DAYS
}
function $p_Ljava_time_temporal_ChronoUnit$__MONTHS$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((512 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_MONTHS = new $c_Ljava_time_temporal_ChronoUnit("Months", 9, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(2629746, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (512 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_MONTHS
}
function $p_Ljava_time_temporal_ChronoUnit$__YEARS$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((1024 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    var this$1 = $m_Ljava_time_Duration$();
    $thiz.Ljava_time_temporal_ChronoUnit$__f_YEARS = new $c_Ljava_time_temporal_ChronoUnit("Years", 10, this$1.java$time$Duration$$create__J__I__Ljava_time_Duration(new $c_RTLong(31556952, 0), 0));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (1024 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_YEARS
}
function $p_Ljava_time_temporal_ChronoUnit$__FOREVER$lzycompute__Ljava_time_temporal_ChronoUnit($thiz) {
  if (((32768 & $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0)) {
    $thiz.Ljava_time_temporal_ChronoUnit$__f_FOREVER = new $c_Ljava_time_temporal_ChronoUnit("Forever", 15, $m_Ljava_time_Duration$().ofSeconds__J__J__Ljava_time_Duration(new $c_RTLong((-1), 2147483647), new $c_RTLong(999999999, 0)));
    $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = (32768 | $thiz.Ljava_time_temporal_ChronoUnit$__f_bitmap$0)
  };
  return $thiz.Ljava_time_temporal_ChronoUnit$__f_FOREVER
}
/** @constructor */
function $c_Ljava_time_temporal_ChronoUnit$() {
  this.Ljava_time_temporal_ChronoUnit$__f_NANOS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_MICROS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_MILLIS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_SECONDS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_MINUTES = null;
  this.Ljava_time_temporal_ChronoUnit$__f_HOURS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_HALF_DAYS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_DAYS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_WEEKS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_MONTHS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_YEARS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_DECADES = null;
  this.Ljava_time_temporal_ChronoUnit$__f_CENTURIES = null;
  this.Ljava_time_temporal_ChronoUnit$__f_MILLENNIA = null;
  this.Ljava_time_temporal_ChronoUnit$__f_ERAS = null;
  this.Ljava_time_temporal_ChronoUnit$__f_FOREVER = null;
  this.Ljava_time_temporal_ChronoUnit$__f_values = null;
  this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0 = 0
}
$c_Ljava_time_temporal_ChronoUnit$.prototype = new $h_O();
$c_Ljava_time_temporal_ChronoUnit$.prototype.constructor = $c_Ljava_time_temporal_ChronoUnit$;
/** @constructor */
function $h_Ljava_time_temporal_ChronoUnit$() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ChronoUnit$.prototype = $c_Ljava_time_temporal_ChronoUnit$.prototype;
$c_Ljava_time_temporal_ChronoUnit$.prototype.SECONDS__Ljava_time_temporal_ChronoUnit = (function() {
  return (((8 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__SECONDS$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_SECONDS)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.MINUTES__Ljava_time_temporal_ChronoUnit = (function() {
  return (((16 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__MINUTES$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_MINUTES)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.HOURS__Ljava_time_temporal_ChronoUnit = (function() {
  return (((32 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__HOURS$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_HOURS)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.DAYS__Ljava_time_temporal_ChronoUnit = (function() {
  return (((128 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__DAYS$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_DAYS)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.MONTHS__Ljava_time_temporal_ChronoUnit = (function() {
  return (((512 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__MONTHS$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_MONTHS)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.YEARS__Ljava_time_temporal_ChronoUnit = (function() {
  return (((1024 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__YEARS$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_YEARS)
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.FOREVER__Ljava_time_temporal_ChronoUnit = (function() {
  return (((32768 & this.Ljava_time_temporal_ChronoUnit$__f_bitmap$0) === 0) ? $p_Ljava_time_temporal_ChronoUnit$__FOREVER$lzycompute__Ljava_time_temporal_ChronoUnit(this) : this.Ljava_time_temporal_ChronoUnit$__f_FOREVER)
});
var $d_Ljava_time_temporal_ChronoUnit$ = new $TypeData().initClass({
  Ljava_time_temporal_ChronoUnit$: 0
}, false, "java.time.temporal.ChronoUnit$", {
  Ljava_time_temporal_ChronoUnit$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_temporal_ChronoUnit$.prototype.$classData = $d_Ljava_time_temporal_ChronoUnit$;
var $n_Ljava_time_temporal_ChronoUnit$;
function $m_Ljava_time_temporal_ChronoUnit$() {
  if ((!$n_Ljava_time_temporal_ChronoUnit$)) {
    $n_Ljava_time_temporal_ChronoUnit$ = new $c_Ljava_time_temporal_ChronoUnit$()
  };
  return $n_Ljava_time_temporal_ChronoUnit$
}
/** @constructor */
function $c_Ljava_time_temporal_ValueRange(minSmallest, minLargest, maxSmallest, maxLargest) {
  this.Ljava_time_temporal_ValueRange__f_minSmallest = $L0;
  this.Ljava_time_temporal_ValueRange__f_minLargest = $L0;
  this.Ljava_time_temporal_ValueRange__f_maxSmallest = $L0;
  this.Ljava_time_temporal_ValueRange__f_maxLargest = $L0;
  this.Ljava_time_temporal_ValueRange__f_minSmallest = minSmallest;
  this.Ljava_time_temporal_ValueRange__f_minLargest = minLargest;
  this.Ljava_time_temporal_ValueRange__f_maxSmallest = maxSmallest;
  this.Ljava_time_temporal_ValueRange__f_maxLargest = maxLargest
}
$c_Ljava_time_temporal_ValueRange.prototype = new $h_O();
$c_Ljava_time_temporal_ValueRange.prototype.constructor = $c_Ljava_time_temporal_ValueRange;
/** @constructor */
function $h_Ljava_time_temporal_ValueRange() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ValueRange.prototype = $c_Ljava_time_temporal_ValueRange.prototype;
$c_Ljava_time_temporal_ValueRange.prototype.isValidValue__J__Z = (function(value) {
  var b = this.Ljava_time_temporal_ValueRange__f_minSmallest;
  var ahi = value.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  if (((ahi === bhi) ? (((-2147483648) ^ value.RTLong__f_lo) >= ((-2147483648) ^ b.RTLong__f_lo)) : (ahi > bhi))) {
    var b$1 = this.Ljava_time_temporal_ValueRange__f_maxLargest;
    var ahi$1 = value.RTLong__f_hi;
    var bhi$1 = b$1.RTLong__f_hi;
    return ((ahi$1 === bhi$1) ? (((-2147483648) ^ value.RTLong__f_lo) <= ((-2147483648) ^ b$1.RTLong__f_lo)) : (ahi$1 < bhi$1))
  } else {
    return false
  }
});
$c_Ljava_time_temporal_ValueRange.prototype.checkValidValue__J__Ljava_time_temporal_TemporalField__J = (function(value, field) {
  if ((!this.isValidValue__J__Z(value))) {
    if ((field !== null)) {
      throw new $c_Ljava_time_DateTimeException(((((("Invalid value for " + field) + " (valid values ") + this) + "): ") + value))
    } else {
      throw new $c_Ljava_time_DateTimeException(((("Invalid value (valid values " + this) + "): ") + value))
    }
  } else {
    return value
  }
});
$c_Ljava_time_temporal_ValueRange.prototype.equals__O__Z = (function(obj) {
  if ((obj instanceof $c_Ljava_time_temporal_ValueRange)) {
    var x2 = $as_Ljava_time_temporal_ValueRange(obj);
    if ((this === x2)) {
      return true
    } else {
      var this$1 = this.Ljava_time_temporal_ValueRange__f_minSmallest;
      var b = x2.Ljava_time_temporal_ValueRange__f_minSmallest;
      if (((this$1.RTLong__f_lo === b.RTLong__f_lo) && (this$1.RTLong__f_hi === b.RTLong__f_hi))) {
        var this$2 = this.Ljava_time_temporal_ValueRange__f_minLargest;
        var b$1 = x2.Ljava_time_temporal_ValueRange__f_minLargest;
        var $$x2 = ((this$2.RTLong__f_lo === b$1.RTLong__f_lo) && (this$2.RTLong__f_hi === b$1.RTLong__f_hi))
      } else {
        var $$x2 = false
      };
      if ($$x2) {
        var this$3 = this.Ljava_time_temporal_ValueRange__f_maxSmallest;
        var b$2 = x2.Ljava_time_temporal_ValueRange__f_maxSmallest;
        var $$x1 = ((this$3.RTLong__f_lo === b$2.RTLong__f_lo) && (this$3.RTLong__f_hi === b$2.RTLong__f_hi))
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var this$4 = this.Ljava_time_temporal_ValueRange__f_maxLargest;
        var b$3 = x2.Ljava_time_temporal_ValueRange__f_maxLargest;
        return ((this$4.RTLong__f_lo === b$3.RTLong__f_lo) && (this$4.RTLong__f_hi === b$3.RTLong__f_hi))
      } else {
        return false
      }
    }
  } else {
    return false
  }
});
$c_Ljava_time_temporal_ValueRange.prototype.hashCode__I = (function() {
  var this$1 = this.Ljava_time_temporal_ValueRange__f_minSmallest;
  var b = this.Ljava_time_temporal_ValueRange__f_minLargest;
  var alo = this$1.RTLong__f_lo;
  var ahi = this$1.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  var lo = ((alo + b.RTLong__f_lo) | 0);
  var hi = ((((-2147483648) ^ lo) < ((-2147483648) ^ alo)) ? ((1 + ((ahi + bhi) | 0)) | 0) : ((ahi + bhi) | 0));
  var b$1 = this.Ljava_time_temporal_ValueRange__f_minLargest;
  var bhi$1 = b$1.RTLong__f_hi;
  var lo$1 = ((16 + b$1.RTLong__f_lo) | 0);
  var lo$2 = (((32 & lo$1) === 0) ? (lo << lo$1) : 0);
  var hi$2 = (((32 & lo$1) === 0) ? (((((lo >>> 1) | 0) >>> ((31 - lo$1) | 0)) | 0) | (hi << lo$1)) : (lo << lo$1));
  var b$2 = this.Ljava_time_temporal_ValueRange__f_maxSmallest;
  var bhi$2 = b$2.RTLong__f_hi;
  var lo$3 = ((48 + b$2.RTLong__f_lo) | 0);
  var lo$4 = (((32 & lo$3) === 0) ? (((lo$2 >>> lo$3) | 0) | ((hi$2 << 1) << ((31 - lo$3) | 0))) : (hi$2 >> lo$3));
  var hi$4 = (((32 & lo$3) === 0) ? (hi$2 >> lo$3) : (hi$2 >> 31));
  var b$3 = this.Ljava_time_temporal_ValueRange__f_maxSmallest;
  var bhi$3 = b$3.RTLong__f_hi;
  var lo$5 = ((32 + b$3.RTLong__f_lo) | 0);
  var lo$6 = (((32 & lo$5) === 0) ? (lo$4 << lo$5) : 0);
  var hi$6 = (((32 & lo$5) === 0) ? (((((lo$4 >>> 1) | 0) >>> ((31 - lo$5) | 0)) | 0) | (hi$4 << lo$5)) : (lo$4 << lo$5));
  var b$4 = this.Ljava_time_temporal_ValueRange__f_maxLargest;
  var bhi$4 = b$4.RTLong__f_hi;
  var lo$7 = ((32 + b$4.RTLong__f_lo) | 0);
  var lo$8 = (((32 & lo$7) === 0) ? (((lo$6 >>> lo$7) | 0) | ((hi$6 << 1) << ((31 - lo$7) | 0))) : (hi$6 >> lo$7));
  var hi$8 = (((32 & lo$7) === 0) ? (hi$6 >> lo$7) : (hi$6 >> 31));
  var b$5 = this.Ljava_time_temporal_ValueRange__f_maxLargest;
  var bhi$5 = b$5.RTLong__f_hi;
  var lo$9 = ((48 + b$5.RTLong__f_lo) | 0);
  var lo$10 = (((32 & lo$9) === 0) ? (lo$8 << lo$9) : 0);
  var hi$10 = (((32 & lo$9) === 0) ? (((((lo$8 >>> 1) | 0) >>> ((31 - lo$9) | 0)) | 0) | (hi$8 << lo$9)) : (lo$8 << lo$9));
  var lo$11 = (((lo$10 >>> 16) | 0) | (hi$10 << 16));
  var hi$11 = (hi$10 >> 16);
  var lo$12 = (lo$11 ^ hi$11);
  return lo$12
});
$c_Ljava_time_temporal_ValueRange.prototype.toString__T = (function() {
  var this$1 = this.Ljava_time_temporal_ValueRange__f_minSmallest;
  var b = this.Ljava_time_temporal_ValueRange__f_minLargest;
  if ((!((this$1.RTLong__f_lo === b.RTLong__f_lo) && (this$1.RTLong__f_hi === b.RTLong__f_hi)))) {
    var ss = ("/" + this.Ljava_time_temporal_ValueRange__f_minLargest)
  } else {
    var ss = ""
  };
  var this$2 = this.Ljava_time_temporal_ValueRange__f_maxSmallest;
  var b$1 = this.Ljava_time_temporal_ValueRange__f_maxLargest;
  if ((!((this$2.RTLong__f_lo === b$1.RTLong__f_lo) && (this$2.RTLong__f_hi === b$1.RTLong__f_hi)))) {
    var sl = ("/" + this.Ljava_time_temporal_ValueRange__f_maxLargest)
  } else {
    var sl = ""
  };
  return ((((this.Ljava_time_temporal_ValueRange__f_minSmallest + ss) + " - ") + this.Ljava_time_temporal_ValueRange__f_maxSmallest) + sl)
});
function $as_Ljava_time_temporal_ValueRange(obj) {
  return (((obj instanceof $c_Ljava_time_temporal_ValueRange) || (obj === null)) ? obj : $throwClassCastException(obj, "java.time.temporal.ValueRange"))
}
function $isArrayOf_Ljava_time_temporal_ValueRange(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_time_temporal_ValueRange)))
}
function $asArrayOf_Ljava_time_temporal_ValueRange(obj, depth) {
  return (($isArrayOf_Ljava_time_temporal_ValueRange(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.time.temporal.ValueRange;", depth))
}
var $d_Ljava_time_temporal_ValueRange = new $TypeData().initClass({
  Ljava_time_temporal_ValueRange: 0
}, false, "java.time.temporal.ValueRange", {
  Ljava_time_temporal_ValueRange: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_temporal_ValueRange.prototype.$classData = $d_Ljava_time_temporal_ValueRange;
/** @constructor */
function $c_Ljava_time_temporal_ValueRange$() {
  /*<skip>*/
}
$c_Ljava_time_temporal_ValueRange$.prototype = new $h_O();
$c_Ljava_time_temporal_ValueRange$.prototype.constructor = $c_Ljava_time_temporal_ValueRange$;
/** @constructor */
function $h_Ljava_time_temporal_ValueRange$() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ValueRange$.prototype = $c_Ljava_time_temporal_ValueRange$.prototype;
$c_Ljava_time_temporal_ValueRange$.prototype.of__J__J__Ljava_time_temporal_ValueRange = (function(min, max) {
  var ahi = min.RTLong__f_hi;
  var bhi = max.RTLong__f_hi;
  if (((ahi === bhi) ? (((-2147483648) ^ min.RTLong__f_lo) > ((-2147483648) ^ max.RTLong__f_lo)) : (ahi > bhi))) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "Minimum value must be less than maximum value")
  } else {
    return new $c_Ljava_time_temporal_ValueRange(min, min, max, max)
  }
});
$c_Ljava_time_temporal_ValueRange$.prototype.of__J__J__J__J__Ljava_time_temporal_ValueRange = (function(minSmallest, minLargest, maxSmallest, maxLargest) {
  var ahi = minSmallest.RTLong__f_hi;
  var bhi = minLargest.RTLong__f_hi;
  if (((ahi === bhi) ? (((-2147483648) ^ minSmallest.RTLong__f_lo) > ((-2147483648) ^ minLargest.RTLong__f_lo)) : (ahi > bhi))) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "Smallest minimum value must be less than largest minimum value")
  };
  var ahi$1 = maxSmallest.RTLong__f_hi;
  var bhi$1 = maxLargest.RTLong__f_hi;
  if (((ahi$1 === bhi$1) ? (((-2147483648) ^ maxSmallest.RTLong__f_lo) > ((-2147483648) ^ maxLargest.RTLong__f_lo)) : (ahi$1 > bhi$1))) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "Smallest maximum value must be less than largest maximum value")
  };
  var ahi$2 = minLargest.RTLong__f_hi;
  var bhi$2 = maxLargest.RTLong__f_hi;
  if (((ahi$2 === bhi$2) ? (((-2147483648) ^ minLargest.RTLong__f_lo) > ((-2147483648) ^ maxLargest.RTLong__f_lo)) : (ahi$2 > bhi$2))) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "Minimum value must be less than maximum value")
  };
  return new $c_Ljava_time_temporal_ValueRange(minSmallest, minLargest, maxSmallest, maxLargest)
});
var $d_Ljava_time_temporal_ValueRange$ = new $TypeData().initClass({
  Ljava_time_temporal_ValueRange$: 0
}, false, "java.time.temporal.ValueRange$", {
  Ljava_time_temporal_ValueRange$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_temporal_ValueRange$.prototype.$classData = $d_Ljava_time_temporal_ValueRange$;
var $n_Ljava_time_temporal_ValueRange$;
function $m_Ljava_time_temporal_ValueRange$() {
  if ((!$n_Ljava_time_temporal_ValueRange$)) {
    $n_Ljava_time_temporal_ValueRange$ = new $c_Ljava_time_temporal_ValueRange$()
  };
  return $n_Ljava_time_temporal_ValueRange$
}
function $sct_Ljava_time_zone_TzdbZoneRulesProvider__stinit__() {
  $m_sjs_reflect_Reflect$().registerInstantiatableClass__T__jl_Class__sjs_js_Array__V("java.time.zone.TzdbZoneRulesProvider", $d_Ljava_time_zone_TzdbZoneRulesProvider.getClassOf(), [[[], (() => new $c_Ljava_time_zone_TzdbZoneRulesProvider())]])
}
/** @constructor */
function $c_Ljava_time_zone_TzdbZoneRulesProvider() {
  this.Ljava_time_zone_TzdbZoneRulesProvider__f_stdZonesMap = null;
  this.Ljava_time_zone_TzdbZoneRulesProvider__f_fixedZonesMap = null;
  var dict = $m_Lzonedb_java_tzdb$().stdZones__sjs_js_Dynamic();
  var this$3 = new $c_sjs_js_WrappedDictionary(dict);
  $m_s_$less$colon$less$();
  this.Ljava_time_zone_TzdbZoneRulesProvider__f_stdZonesMap = $m_sci_Map$().from__sc_IterableOnce__sci_Map(this$3);
  var dict$1 = $m_Lzonedb_java_tzdb$().fixedZones__sjs_js_Dynamic();
  var this$6 = new $c_sjs_js_WrappedDictionary(dict$1);
  $m_s_$less$colon$less$();
  this.Ljava_time_zone_TzdbZoneRulesProvider__f_fixedZonesMap = $m_sci_Map$().from__sc_IterableOnce__sci_Map(this$6)
}
$c_Ljava_time_zone_TzdbZoneRulesProvider.prototype = new $h_Ljava_time_zone_ZoneRulesProvider();
$c_Ljava_time_zone_TzdbZoneRulesProvider.prototype.constructor = $c_Ljava_time_zone_TzdbZoneRulesProvider;
/** @constructor */
function $h_Ljava_time_zone_TzdbZoneRulesProvider() {
  /*<skip>*/
}
$h_Ljava_time_zone_TzdbZoneRulesProvider.prototype = $c_Ljava_time_zone_TzdbZoneRulesProvider.prototype;
var $d_Ljava_time_zone_TzdbZoneRulesProvider = new $TypeData().initClass({
  Ljava_time_zone_TzdbZoneRulesProvider: 0
}, false, "java.time.zone.TzdbZoneRulesProvider", {
  Ljava_time_zone_TzdbZoneRulesProvider: 1,
  Ljava_time_zone_ZoneRulesProvider: 1,
  O: 1
});
$c_Ljava_time_zone_TzdbZoneRulesProvider.prototype.$classData = $d_Ljava_time_zone_TzdbZoneRulesProvider;
function $p_RTLong$__toUnsignedString__I__I__T($thiz, lo, hi) {
  if ((((-2097152) & hi) === 0)) {
    var this$1 = ((4.294967296E9 * hi) + $uD((lo >>> 0)));
    return ("" + this$1)
  } else {
    return $as_T($p_RTLong$__unsignedDivModHelper__I__I__I__I__I__O($thiz, lo, hi, 1000000000, 0, 2))
  }
}
function $p_RTLong$__unsigned_$div__I__I__I__I__I($thiz, alo, ahi, blo, bhi) {
  if ((((-2097152) & ahi) === 0)) {
    if ((((-2097152) & bhi) === 0)) {
      var aDouble = ((4.294967296E9 * ahi) + $uD((alo >>> 0)));
      var bDouble = ((4.294967296E9 * bhi) + $uD((blo >>> 0)));
      var rDouble = (aDouble / bDouble);
      var x = (rDouble / 4.294967296E9);
      $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = $uI((x | 0));
      return $uI((rDouble | 0))
    } else {
      $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
      return 0
    }
  } else if (((bhi === 0) && ((blo & (((-1) + blo) | 0)) === 0))) {
    var pow = ((31 - $clz32(blo)) | 0);
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = ((ahi >>> pow) | 0);
    return (((alo >>> pow) | 0) | ((ahi << 1) << ((31 - pow) | 0)))
  } else if (((blo === 0) && ((bhi & (((-1) + bhi) | 0)) === 0))) {
    var pow$2 = ((31 - $clz32(bhi)) | 0);
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
    return ((ahi >>> pow$2) | 0)
  } else {
    return $uI($p_RTLong$__unsignedDivModHelper__I__I__I__I__I__O($thiz, alo, ahi, blo, bhi, 0))
  }
}
function $p_RTLong$__unsigned_$percent__I__I__I__I__I($thiz, alo, ahi, blo, bhi) {
  if ((((-2097152) & ahi) === 0)) {
    if ((((-2097152) & bhi) === 0)) {
      var aDouble = ((4.294967296E9 * ahi) + $uD((alo >>> 0)));
      var bDouble = ((4.294967296E9 * bhi) + $uD((blo >>> 0)));
      var rDouble = (aDouble % bDouble);
      var x = (rDouble / 4.294967296E9);
      $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = $uI((x | 0));
      return $uI((rDouble | 0))
    } else {
      $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = ahi;
      return alo
    }
  } else if (((bhi === 0) && ((blo & (((-1) + blo) | 0)) === 0))) {
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
    return (alo & (((-1) + blo) | 0))
  } else if (((blo === 0) && ((bhi & (((-1) + bhi) | 0)) === 0))) {
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (ahi & (((-1) + bhi) | 0));
    return alo
  } else {
    return $uI($p_RTLong$__unsignedDivModHelper__I__I__I__I__I__O($thiz, alo, ahi, blo, bhi, 1))
  }
}
function $p_RTLong$__unsignedDivModHelper__I__I__I__I__I__O($thiz, alo, ahi, blo, bhi, ask) {
  var shift = ((((bhi !== 0) ? $clz32(bhi) : ((32 + $clz32(blo)) | 0)) - ((ahi !== 0) ? $clz32(ahi) : ((32 + $clz32(alo)) | 0))) | 0);
  var n = shift;
  var lo = (((32 & n) === 0) ? (blo << n) : 0);
  var hi = (((32 & n) === 0) ? (((((blo >>> 1) | 0) >>> ((31 - n) | 0)) | 0) | (bhi << n)) : (blo << n));
  var bShiftLo = lo;
  var bShiftHi = hi;
  var remLo = alo;
  var remHi = ahi;
  var quotLo = 0;
  var quotHi = 0;
  while (((shift >= 0) && (((-2097152) & remHi) !== 0))) {
    var alo$1 = remLo;
    var ahi$1 = remHi;
    var blo$1 = bShiftLo;
    var bhi$1 = bShiftHi;
    if (((ahi$1 === bhi$1) ? (((-2147483648) ^ alo$1) >= ((-2147483648) ^ blo$1)) : (((-2147483648) ^ ahi$1) >= ((-2147483648) ^ bhi$1)))) {
      var lo$1 = remLo;
      var hi$1 = remHi;
      var lo$2 = bShiftLo;
      var hi$2 = bShiftHi;
      var lo$3 = ((lo$1 - lo$2) | 0);
      var hi$3 = ((((-2147483648) ^ lo$3) > ((-2147483648) ^ lo$1)) ? (((-1) + ((hi$1 - hi$2) | 0)) | 0) : ((hi$1 - hi$2) | 0));
      remLo = lo$3;
      remHi = hi$3;
      if ((shift < 32)) {
        quotLo = (quotLo | (1 << shift))
      } else {
        quotHi = (quotHi | (1 << shift))
      }
    };
    shift = (((-1) + shift) | 0);
    var lo$4 = bShiftLo;
    var hi$4 = bShiftHi;
    var lo$5 = (((lo$4 >>> 1) | 0) | (hi$4 << 31));
    var hi$5 = ((hi$4 >>> 1) | 0);
    bShiftLo = lo$5;
    bShiftHi = hi$5
  };
  var alo$2 = remLo;
  var ahi$2 = remHi;
  if (((ahi$2 === bhi) ? (((-2147483648) ^ alo$2) >= ((-2147483648) ^ blo)) : (((-2147483648) ^ ahi$2) >= ((-2147483648) ^ bhi)))) {
    var lo$6 = remLo;
    var hi$6 = remHi;
    var remDouble = ((4.294967296E9 * hi$6) + $uD((lo$6 >>> 0)));
    var bDouble = ((4.294967296E9 * bhi) + $uD((blo >>> 0)));
    if ((ask !== 1)) {
      var x = (remDouble / bDouble);
      var lo$7 = $uI((x | 0));
      var x$1 = (x / 4.294967296E9);
      var hi$7 = $uI((x$1 | 0));
      var lo$8 = quotLo;
      var hi$8 = quotHi;
      var lo$9 = ((lo$8 + lo$7) | 0);
      var hi$9 = ((((-2147483648) ^ lo$9) < ((-2147483648) ^ lo$8)) ? ((1 + ((hi$8 + hi$7) | 0)) | 0) : ((hi$8 + hi$7) | 0));
      quotLo = lo$9;
      quotHi = hi$9
    };
    if ((ask !== 0)) {
      var rem_mod_bDouble = (remDouble % bDouble);
      remLo = $uI((rem_mod_bDouble | 0));
      var x$2 = (rem_mod_bDouble / 4.294967296E9);
      remHi = $uI((x$2 | 0))
    }
  };
  if ((ask === 0)) {
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = quotHi;
    return quotLo
  } else if ((ask === 1)) {
    $thiz.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = remHi;
    return remLo
  } else {
    var lo$10 = quotLo;
    var hi$10 = quotHi;
    var quot = ((4.294967296E9 * hi$10) + $uD((lo$10 >>> 0)));
    var this$3 = remLo;
    var remStr = ("" + this$3);
    var start = $uI(remStr.length);
    return ((("" + quot) + $as_T("000000000".substring(start))) + remStr)
  }
}
/** @constructor */
function $c_RTLong$() {
  this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0
}
$c_RTLong$.prototype = new $h_O();
$c_RTLong$.prototype.constructor = $c_RTLong$;
/** @constructor */
function $h_RTLong$() {
  /*<skip>*/
}
$h_RTLong$.prototype = $c_RTLong$.prototype;
$c_RTLong$.prototype.org$scalajs$linker$runtime$RuntimeLong$$toString__I__I__T = (function(lo, hi) {
  return ((hi === (lo >> 31)) ? ("" + lo) : ((hi < 0) ? ("-" + $p_RTLong$__toUnsignedString__I__I__T(this, ((-lo) | 0), ((lo !== 0) ? (~hi) : ((-hi) | 0)))) : $p_RTLong$__toUnsignedString__I__I__T(this, lo, hi)))
});
$c_RTLong$.prototype.org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D = (function(lo, hi) {
  if ((hi < 0)) {
    var x = ((lo !== 0) ? (~hi) : ((-hi) | 0));
    var $$x1 = $uD((x >>> 0));
    var x$1 = ((-lo) | 0);
    return (-((4.294967296E9 * $$x1) + $uD((x$1 >>> 0))))
  } else {
    return ((4.294967296E9 * hi) + $uD((lo >>> 0)))
  }
});
$c_RTLong$.prototype.org$scalajs$linker$runtime$RuntimeLong$$toFloat__I__I__F = (function(lo, hi) {
  if ((hi < 0)) {
    var lo$1 = ((-lo) | 0);
    var hi$1 = ((lo !== 0) ? (~hi) : ((-hi) | 0));
    var abs__lo = lo$1;
    var abs__hi = hi$1
  } else {
    var abs__lo = lo;
    var abs__hi = hi
  };
  var hi$2 = abs__hi;
  if (((((-2097152) & hi$2) === 0) || ((65535 & abs__lo) === 0))) {
    var compressedAbsLo = abs__lo
  } else {
    var compressedAbsLo = (32768 | ((-65536) & abs__lo))
  };
  var x = abs__hi;
  var absRes = ((4.294967296E9 * $uD((x >>> 0))) + $uD((compressedAbsLo >>> 0)));
  return $fround(((hi < 0) ? (-absRes) : absRes))
});
$c_RTLong$.prototype.fromInt__I__RTLong = (function(value) {
  return new $c_RTLong(value, (value >> 31))
});
$c_RTLong$.prototype.fromDouble__D__RTLong = (function(value) {
  var lo = this.org$scalajs$linker$runtime$RuntimeLong$$fromDoubleImpl__D__I(value);
  return new $c_RTLong(lo, this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn)
});
$c_RTLong$.prototype.org$scalajs$linker$runtime$RuntimeLong$$fromDoubleImpl__D__I = (function(value) {
  if ((value < (-9.223372036854776E18))) {
    this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (-2147483648);
    return 0
  } else if ((value >= 9.223372036854776E18)) {
    this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 2147483647;
    return (-1)
  } else {
    var rawLo = $uI((value | 0));
    var x = (value / 4.294967296E9);
    var rawHi = $uI((x | 0));
    this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (((value < 0.0) && (rawLo !== 0)) ? (((-1) + rawHi) | 0) : rawHi);
    return rawLo
  }
});
$c_RTLong$.prototype.org$scalajs$linker$runtime$RuntimeLong$$compare__I__I__I__I__I = (function(alo, ahi, blo, bhi) {
  return ((ahi === bhi) ? ((alo === blo) ? 0 : ((((-2147483648) ^ alo) < ((-2147483648) ^ blo)) ? (-1) : 1)) : ((ahi < bhi) ? (-1) : 1))
});
$c_RTLong$.prototype.divideImpl__I__I__I__I__I = (function(alo, ahi, blo, bhi) {
  if (((blo | bhi) === 0)) {
    throw new $c_jl_ArithmeticException("/ by zero")
  };
  if ((ahi === (alo >> 31))) {
    if ((bhi === (blo >> 31))) {
      if (((alo === (-2147483648)) && (blo === (-1)))) {
        this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
        return (-2147483648)
      } else {
        var lo = $intDiv(alo, blo);
        this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (lo >> 31);
        return lo
      }
    } else if (((alo === (-2147483648)) && ((blo === (-2147483648)) && (bhi === 0)))) {
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (-1);
      return (-1)
    } else {
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
      return 0
    }
  } else {
    if ((ahi < 0)) {
      var lo$1 = ((-alo) | 0);
      var hi = ((alo !== 0) ? (~ahi) : ((-ahi) | 0));
      var aAbs__lo = lo$1;
      var aAbs__hi = hi
    } else {
      var aAbs__lo = alo;
      var aAbs__hi = ahi
    };
    if ((bhi < 0)) {
      var lo$2 = ((-blo) | 0);
      var hi$1 = ((blo !== 0) ? (~bhi) : ((-bhi) | 0));
      var bAbs__lo = lo$2;
      var bAbs__hi = hi$1
    } else {
      var bAbs__lo = blo;
      var bAbs__hi = bhi
    };
    var absRLo = $p_RTLong$__unsigned_$div__I__I__I__I__I(this, aAbs__lo, aAbs__hi, bAbs__lo, bAbs__hi);
    if (((ahi ^ bhi) >= 0)) {
      return absRLo
    } else {
      var hi$2 = this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = ((absRLo !== 0) ? (~hi$2) : ((-hi$2) | 0));
      return ((-absRLo) | 0)
    }
  }
});
$c_RTLong$.prototype.remainderImpl__I__I__I__I__I = (function(alo, ahi, blo, bhi) {
  if (((blo | bhi) === 0)) {
    throw new $c_jl_ArithmeticException("/ by zero")
  };
  if ((ahi === (alo >> 31))) {
    if ((bhi === (blo >> 31))) {
      if ((blo !== (-1))) {
        var lo = $intMod(alo, blo);
        this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = (lo >> 31);
        return lo
      } else {
        this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
        return 0
      }
    } else if (((alo === (-2147483648)) && ((blo === (-2147483648)) && (bhi === 0)))) {
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = 0;
      return 0
    } else {
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = ahi;
      return alo
    }
  } else {
    if ((ahi < 0)) {
      var lo$1 = ((-alo) | 0);
      var hi = ((alo !== 0) ? (~ahi) : ((-ahi) | 0));
      var aAbs__lo = lo$1;
      var aAbs__hi = hi
    } else {
      var aAbs__lo = alo;
      var aAbs__hi = ahi
    };
    if ((bhi < 0)) {
      var lo$2 = ((-blo) | 0);
      var hi$1 = ((blo !== 0) ? (~bhi) : ((-bhi) | 0));
      var bAbs__lo = lo$2;
      var bAbs__hi = hi$1
    } else {
      var bAbs__lo = blo;
      var bAbs__hi = bhi
    };
    var absRLo = $p_RTLong$__unsigned_$percent__I__I__I__I__I(this, aAbs__lo, aAbs__hi, bAbs__lo, bAbs__hi);
    if ((ahi < 0)) {
      var hi$2 = this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
      this.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn = ((absRLo !== 0) ? (~hi$2) : ((-hi$2) | 0));
      return ((-absRLo) | 0)
    } else {
      return absRLo
    }
  }
});
var $d_RTLong$ = new $TypeData().initClass({
  RTLong$: 0
}, false, "org.scalajs.linker.runtime.RuntimeLong$", {
  RTLong$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_RTLong$.prototype.$classData = $d_RTLong$;
var $n_RTLong$;
function $m_RTLong$() {
  if ((!$n_RTLong$)) {
    $n_RTLong$ = new $c_RTLong$()
  };
  return $n_RTLong$
}
/** @constructor */
function $c_s_$less$colon$less$() {
  this.s_$less$colon$less$__f_singleton = null;
  $n_s_$less$colon$less$ = this;
  this.s_$less$colon$less$__f_singleton = new $c_s_$less$colon$less$$anon$1()
}
$c_s_$less$colon$less$.prototype = new $h_O();
$c_s_$less$colon$less$.prototype.constructor = $c_s_$less$colon$less$;
/** @constructor */
function $h_s_$less$colon$less$() {
  /*<skip>*/
}
$h_s_$less$colon$less$.prototype = $c_s_$less$colon$less$.prototype;
var $d_s_$less$colon$less$ = new $TypeData().initClass({
  s_$less$colon$less$: 0
}, false, "scala.$less$colon$less$", {
  s_$less$colon$less$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_$less$colon$less$.prototype.$classData = $d_s_$less$colon$less$;
var $n_s_$less$colon$less$;
function $m_s_$less$colon$less$() {
  if ((!$n_s_$less$colon$less$)) {
    $n_s_$less$colon$less$ = new $c_s_$less$colon$less$()
  };
  return $n_s_$less$colon$less$
}
function $p_s_Array$__slowcopy__O__I__O__I__I__V($thiz, src, srcPos, dest, destPos, length) {
  var i = srcPos;
  var j = destPos;
  var srcUntil = ((srcPos + length) | 0);
  while ((i < srcUntil)) {
    $m_sr_ScalaRunTime$().array_update__O__I__O__V(dest, j, $m_sr_ScalaRunTime$().array_apply__O__I__O(src, i));
    i = ((1 + i) | 0);
    j = ((1 + j) | 0)
  }
}
/** @constructor */
function $c_s_Array$() {
  /*<skip>*/
}
$c_s_Array$.prototype = new $h_O();
$c_s_Array$.prototype.constructor = $c_s_Array$;
/** @constructor */
function $h_s_Array$() {
  /*<skip>*/
}
$h_s_Array$.prototype = $c_s_Array$.prototype;
$c_s_Array$.prototype.copy__O__I__O__I__I__V = (function(src, srcPos, dest, destPos, length) {
  var srcClass = $objectGetClass(src);
  if ((srcClass.isArray__Z() && $objectGetClass(dest).isAssignableFrom__jl_Class__Z(srcClass))) {
    src.copyTo(srcPos, dest, destPos, length)
  } else {
    $p_s_Array$__slowcopy__O__I__O__I__I__V(this, src, srcPos, dest, destPos, length)
  }
});
var $d_s_Array$ = new $TypeData().initClass({
  s_Array$: 0
}, false, "scala.Array$", {
  s_Array$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_Array$.prototype.$classData = $d_s_Array$;
var $n_s_Array$;
function $m_s_Array$() {
  if ((!$n_s_Array$)) {
    $n_s_Array$ = new $c_s_Array$()
  };
  return $n_s_Array$
}
/** @constructor */
function $c_s_Console$() {
  this.s_Console$__f_outVar = null;
  this.s_Console$__f_errVar = null;
  this.s_Console$__f_inVar = null;
  $n_s_Console$ = this;
  this.s_Console$__f_outVar = new $c_s_util_DynamicVariable($m_jl_System$Streams$().jl_System$Streams$__f_out);
  this.s_Console$__f_errVar = new $c_s_util_DynamicVariable($m_jl_System$Streams$().jl_System$Streams$__f_err);
  this.s_Console$__f_inVar = new $c_s_util_DynamicVariable(null)
}
$c_s_Console$.prototype = new $h_O();
$c_s_Console$.prototype.constructor = $c_s_Console$;
/** @constructor */
function $h_s_Console$() {
  /*<skip>*/
}
$h_s_Console$.prototype = $c_s_Console$.prototype;
$c_s_Console$.prototype.out__Ljava_io_PrintStream = (function() {
  return $as_Ljava_io_PrintStream(this.s_Console$__f_outVar.s_util_DynamicVariable__f_v)
});
var $d_s_Console$ = new $TypeData().initClass({
  s_Console$: 0
}, false, "scala.Console$", {
  s_Console$: 1,
  O: 1,
  s_io_AnsiColor: 1
});
$c_s_Console$.prototype.$classData = $d_s_Console$;
var $n_s_Console$;
function $m_s_Console$() {
  if ((!$n_s_Console$)) {
    $n_s_Console$ = new $c_s_Console$()
  };
  return $n_s_Console$
}
/** @constructor */
function $c_s_LowPriorityImplicits() {
  /*<skip>*/
}
$c_s_LowPriorityImplicits.prototype = new $h_s_LowPriorityImplicits2();
$c_s_LowPriorityImplicits.prototype.constructor = $c_s_LowPriorityImplicits;
/** @constructor */
function $h_s_LowPriorityImplicits() {
  /*<skip>*/
}
$h_s_LowPriorityImplicits.prototype = $c_s_LowPriorityImplicits.prototype;
/** @constructor */
function $c_T2$() {
  /*<skip>*/
}
$c_T2$.prototype = new $h_O();
$c_T2$.prototype.constructor = $c_T2$;
/** @constructor */
function $h_T2$() {
  /*<skip>*/
}
$h_T2$.prototype = $c_T2$.prototype;
$c_T2$.prototype.toString__T = (function() {
  return "Tuple2"
});
var $d_T2$ = new $TypeData().initClass({
  T2$: 0
}, false, "scala.Tuple2$", {
  T2$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_T2$.prototype.$classData = $d_T2$;
var $n_T2$;
function $m_T2$() {
  if ((!$n_T2$)) {
    $n_T2$ = new $c_T2$()
  };
  return $n_T2$
}
/** @constructor */
function $c_sci_$colon$colon$() {
  /*<skip>*/
}
$c_sci_$colon$colon$.prototype = new $h_O();
$c_sci_$colon$colon$.prototype.constructor = $c_sci_$colon$colon$;
/** @constructor */
function $h_sci_$colon$colon$() {
  /*<skip>*/
}
$h_sci_$colon$colon$.prototype = $c_sci_$colon$colon$.prototype;
$c_sci_$colon$colon$.prototype.toString__T = (function() {
  return "::"
});
var $d_sci_$colon$colon$ = new $TypeData().initClass({
  sci_$colon$colon$: 0
}, false, "scala.collection.immutable.$colon$colon$", {
  sci_$colon$colon$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_sci_$colon$colon$.prototype.$classData = $d_sci_$colon$colon$;
var $n_sci_$colon$colon$;
function $m_sci_$colon$colon$() {
  if ((!$n_sci_$colon$colon$)) {
    $n_sci_$colon$colon$ = new $c_sci_$colon$colon$()
  };
  return $n_sci_$colon$colon$
}
/** @constructor */
function $c_sci_HashMapBuilder$$anon$1(outer, x2$1) {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null;
  $ct_sci_ChampBaseIterator__sci_Node__(this, x2$1.sci_HashMap__f_rootNode);
  while (this.hasNext__Z()) {
    var originalHash = this.sci_ChampBaseIterator__f_currentValueNode.getHash__I__I(this.sci_ChampBaseIterator__f_currentValueCursor);
    outer.update__sci_MapNode__O__O__I__I__I__V(outer.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode, $as_sci_MapNode(this.sci_ChampBaseIterator__f_currentValueNode).getKey__I__O(this.sci_ChampBaseIterator__f_currentValueCursor), $as_sci_MapNode(this.sci_ChampBaseIterator__f_currentValueNode).getValue__I__O(this.sci_ChampBaseIterator__f_currentValueCursor), originalHash, $m_sc_Hashing$().improve__I__I(originalHash), 0);
    this.sci_ChampBaseIterator__f_currentValueCursor = ((1 + this.sci_ChampBaseIterator__f_currentValueCursor) | 0)
  }
}
$c_sci_HashMapBuilder$$anon$1.prototype = new $h_sci_ChampBaseIterator();
$c_sci_HashMapBuilder$$anon$1.prototype.constructor = $c_sci_HashMapBuilder$$anon$1;
/** @constructor */
function $h_sci_HashMapBuilder$$anon$1() {
  /*<skip>*/
}
$h_sci_HashMapBuilder$$anon$1.prototype = $c_sci_HashMapBuilder$$anon$1.prototype;
var $d_sci_HashMapBuilder$$anon$1 = new $TypeData().initClass({
  sci_HashMapBuilder$$anon$1: 0
}, false, "scala.collection.immutable.HashMapBuilder$$anon$1", {
  sci_HashMapBuilder$$anon$1: 1,
  sci_ChampBaseIterator: 1,
  O: 1
});
$c_sci_HashMapBuilder$$anon$1.prototype.$classData = $d_sci_HashMapBuilder$$anon$1;
/** @constructor */
function $c_sci_HashSetBuilder$$anon$1(outer, x2$1) {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null;
  $ct_sci_ChampBaseIterator__sci_Node__(this, x2$1.sci_HashSet__f_rootNode);
  while (this.hasNext__Z()) {
    var originalHash = this.sci_ChampBaseIterator__f_currentValueNode.getHash__I__I(this.sci_ChampBaseIterator__f_currentValueCursor);
    outer.update__sci_SetNode__O__I__I__I__V(outer.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode, $as_sci_SetNode(this.sci_ChampBaseIterator__f_currentValueNode).getPayload__I__O(this.sci_ChampBaseIterator__f_currentValueCursor), originalHash, $m_sc_Hashing$().improve__I__I(originalHash), 0);
    this.sci_ChampBaseIterator__f_currentValueCursor = ((1 + this.sci_ChampBaseIterator__f_currentValueCursor) | 0)
  }
}
$c_sci_HashSetBuilder$$anon$1.prototype = new $h_sci_ChampBaseIterator();
$c_sci_HashSetBuilder$$anon$1.prototype.constructor = $c_sci_HashSetBuilder$$anon$1;
/** @constructor */
function $h_sci_HashSetBuilder$$anon$1() {
  /*<skip>*/
}
$h_sci_HashSetBuilder$$anon$1.prototype = $c_sci_HashSetBuilder$$anon$1.prototype;
var $d_sci_HashSetBuilder$$anon$1 = new $TypeData().initClass({
  sci_HashSetBuilder$$anon$1: 0
}, false, "scala.collection.immutable.HashSetBuilder$$anon$1", {
  sci_HashSetBuilder$$anon$1: 1,
  sci_ChampBaseIterator: 1,
  O: 1
});
$c_sci_HashSetBuilder$$anon$1.prototype.$classData = $d_sci_HashSetBuilder$$anon$1;
function $is_sci_LazyList$State(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_LazyList$State)))
}
function $as_sci_LazyList$State(obj) {
  return (($is_sci_LazyList$State(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.LazyList$State"))
}
function $isArrayOf_sci_LazyList$State(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_LazyList$State)))
}
function $asArrayOf_sci_LazyList$State(obj, depth) {
  return (($isArrayOf_sci_LazyList$State(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.LazyList$State;", depth))
}
/** @constructor */
function $c_sci_List$$anon$1() {
  /*<skip>*/
}
$c_sci_List$$anon$1.prototype = new $h_O();
$c_sci_List$$anon$1.prototype.constructor = $c_sci_List$$anon$1;
/** @constructor */
function $h_sci_List$$anon$1() {
  /*<skip>*/
}
$h_sci_List$$anon$1.prototype = $c_sci_List$$anon$1.prototype;
$c_sci_List$$anon$1.prototype.toString__T = (function() {
  return "<function1>"
});
$c_sci_List$$anon$1.prototype.apply__O__O = (function(x) {
  return this
});
var $d_sci_List$$anon$1 = new $TypeData().initClass({
  sci_List$$anon$1: 0
}, false, "scala.collection.immutable.List$$anon$1", {
  sci_List$$anon$1: 1,
  O: 1,
  F1: 1
});
$c_sci_List$$anon$1.prototype.$classData = $d_sci_List$$anon$1;
/** @constructor */
function $c_sci_MapNode() {
  /*<skip>*/
}
$c_sci_MapNode.prototype = new $h_sci_Node();
$c_sci_MapNode.prototype.constructor = $c_sci_MapNode;
/** @constructor */
function $h_sci_MapNode() {
  /*<skip>*/
}
$h_sci_MapNode.prototype = $c_sci_MapNode.prototype;
function $as_sci_MapNode(obj) {
  return (((obj instanceof $c_sci_MapNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.MapNode"))
}
function $isArrayOf_sci_MapNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_MapNode)))
}
function $asArrayOf_sci_MapNode(obj, depth) {
  return (($isArrayOf_sci_MapNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.MapNode;", depth))
}
/** @constructor */
function $c_sci_Range$() {
  /*<skip>*/
}
$c_sci_Range$.prototype = new $h_O();
$c_sci_Range$.prototype.constructor = $c_sci_Range$;
/** @constructor */
function $h_sci_Range$() {
  /*<skip>*/
}
$h_sci_Range$.prototype = $c_sci_Range$.prototype;
var $d_sci_Range$ = new $TypeData().initClass({
  sci_Range$: 0
}, false, "scala.collection.immutable.Range$", {
  sci_Range$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Range$.prototype.$classData = $d_sci_Range$;
var $n_sci_Range$;
function $m_sci_Range$() {
  if ((!$n_sci_Range$)) {
    $n_sci_Range$ = new $c_sci_Range$()
  };
  return $n_sci_Range$
}
/** @constructor */
function $c_sci_SetNode() {
  /*<skip>*/
}
$c_sci_SetNode.prototype = new $h_sci_Node();
$c_sci_SetNode.prototype.constructor = $c_sci_SetNode;
/** @constructor */
function $h_sci_SetNode() {
  /*<skip>*/
}
$h_sci_SetNode.prototype = $c_sci_SetNode.prototype;
function $as_sci_SetNode(obj) {
  return (((obj instanceof $c_sci_SetNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.SetNode"))
}
function $isArrayOf_sci_SetNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_SetNode)))
}
function $asArrayOf_sci_SetNode(obj, depth) {
  return (($isArrayOf_sci_SetNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.SetNode;", depth))
}
function $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable($thiz, xs) {
  if ((xs === $thiz)) {
    $thiz.addAll__sc_IterableOnce__scm_Growable($m_scm_Buffer$().from__sc_IterableOnce__sc_SeqOps(xs))
  } else {
    var it = xs.iterator__sc_Iterator();
    while (it.hasNext__Z()) {
      $thiz.addOne__O__scm_Growable(it.next__O())
    }
  };
  return $thiz
}
/** @constructor */
function $c_scm_StringBuilder$() {
  /*<skip>*/
}
$c_scm_StringBuilder$.prototype = new $h_O();
$c_scm_StringBuilder$.prototype.constructor = $c_scm_StringBuilder$;
/** @constructor */
function $h_scm_StringBuilder$() {
  /*<skip>*/
}
$h_scm_StringBuilder$.prototype = $c_scm_StringBuilder$.prototype;
var $d_scm_StringBuilder$ = new $TypeData().initClass({
  scm_StringBuilder$: 0
}, false, "scala.collection.mutable.StringBuilder$", {
  scm_StringBuilder$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_scm_StringBuilder$.prototype.$classData = $d_scm_StringBuilder$;
var $n_scm_StringBuilder$;
function $m_scm_StringBuilder$() {
  if ((!$n_scm_StringBuilder$)) {
    $n_scm_StringBuilder$ = new $c_scm_StringBuilder$()
  };
  return $n_scm_StringBuilder$
}
/** @constructor */
function $c_s_math_Fractional$() {
  /*<skip>*/
}
$c_s_math_Fractional$.prototype = new $h_O();
$c_s_math_Fractional$.prototype.constructor = $c_s_math_Fractional$;
/** @constructor */
function $h_s_math_Fractional$() {
  /*<skip>*/
}
$h_s_math_Fractional$.prototype = $c_s_math_Fractional$.prototype;
var $d_s_math_Fractional$ = new $TypeData().initClass({
  s_math_Fractional$: 0
}, false, "scala.math.Fractional$", {
  s_math_Fractional$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_math_Fractional$.prototype.$classData = $d_s_math_Fractional$;
var $n_s_math_Fractional$;
function $m_s_math_Fractional$() {
  if ((!$n_s_math_Fractional$)) {
    $n_s_math_Fractional$ = new $c_s_math_Fractional$()
  };
  return $n_s_math_Fractional$
}
/** @constructor */
function $c_s_math_Integral$() {
  /*<skip>*/
}
$c_s_math_Integral$.prototype = new $h_O();
$c_s_math_Integral$.prototype.constructor = $c_s_math_Integral$;
/** @constructor */
function $h_s_math_Integral$() {
  /*<skip>*/
}
$h_s_math_Integral$.prototype = $c_s_math_Integral$.prototype;
var $d_s_math_Integral$ = new $TypeData().initClass({
  s_math_Integral$: 0
}, false, "scala.math.Integral$", {
  s_math_Integral$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_math_Integral$.prototype.$classData = $d_s_math_Integral$;
var $n_s_math_Integral$;
function $m_s_math_Integral$() {
  if ((!$n_s_math_Integral$)) {
    $n_s_math_Integral$ = new $c_s_math_Integral$()
  };
  return $n_s_math_Integral$
}
/** @constructor */
function $c_s_math_Numeric$() {
  /*<skip>*/
}
$c_s_math_Numeric$.prototype = new $h_O();
$c_s_math_Numeric$.prototype.constructor = $c_s_math_Numeric$;
/** @constructor */
function $h_s_math_Numeric$() {
  /*<skip>*/
}
$h_s_math_Numeric$.prototype = $c_s_math_Numeric$.prototype;
var $d_s_math_Numeric$ = new $TypeData().initClass({
  s_math_Numeric$: 0
}, false, "scala.math.Numeric$", {
  s_math_Numeric$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_math_Numeric$.prototype.$classData = $d_s_math_Numeric$;
var $n_s_math_Numeric$;
function $m_s_math_Numeric$() {
  if ((!$n_s_math_Numeric$)) {
    $n_s_math_Numeric$ = new $c_s_math_Numeric$()
  };
  return $n_s_math_Numeric$
}
/** @constructor */
function $c_s_package$$anon$1() {
  /*<skip>*/
}
$c_s_package$$anon$1.prototype = new $h_O();
$c_s_package$$anon$1.prototype.constructor = $c_s_package$$anon$1;
/** @constructor */
function $h_s_package$$anon$1() {
  /*<skip>*/
}
$h_s_package$$anon$1.prototype = $c_s_package$$anon$1.prototype;
$c_s_package$$anon$1.prototype.toString__T = (function() {
  return "object AnyRef"
});
var $d_s_package$$anon$1 = new $TypeData().initClass({
  s_package$$anon$1: 0
}, false, "scala.package$$anon$1", {
  s_package$$anon$1: 1,
  O: 1,
  s_Specializable: 1
});
$c_s_package$$anon$1.prototype.$classData = $d_s_package$$anon$1;
/** @constructor */
function $c_s_reflect_ClassTag$() {
  /*<skip>*/
}
$c_s_reflect_ClassTag$.prototype = new $h_O();
$c_s_reflect_ClassTag$.prototype.constructor = $c_s_reflect_ClassTag$;
/** @constructor */
function $h_s_reflect_ClassTag$() {
  /*<skip>*/
}
$h_s_reflect_ClassTag$.prototype = $c_s_reflect_ClassTag$.prototype;
$c_s_reflect_ClassTag$.prototype.apply__jl_Class__s_reflect_ClassTag = (function(runtimeClass1) {
  return ((runtimeClass1 === $d_B.getClassOf()) ? $m_s_reflect_ManifestFactory$ByteManifest$() : ((runtimeClass1 === $d_S.getClassOf()) ? $m_s_reflect_ManifestFactory$ShortManifest$() : ((runtimeClass1 === $d_C.getClassOf()) ? $m_s_reflect_ManifestFactory$CharManifest$() : ((runtimeClass1 === $d_I.getClassOf()) ? $m_s_reflect_ManifestFactory$IntManifest$() : ((runtimeClass1 === $d_J.getClassOf()) ? $m_s_reflect_ManifestFactory$LongManifest$() : ((runtimeClass1 === $d_F.getClassOf()) ? $m_s_reflect_ManifestFactory$FloatManifest$() : ((runtimeClass1 === $d_D.getClassOf()) ? $m_s_reflect_ManifestFactory$DoubleManifest$() : ((runtimeClass1 === $d_Z.getClassOf()) ? $m_s_reflect_ManifestFactory$BooleanManifest$() : ((runtimeClass1 === $d_V.getClassOf()) ? $m_s_reflect_ManifestFactory$UnitManifest$() : ((runtimeClass1 === $d_O.getClassOf()) ? $m_s_reflect_ManifestFactory$ObjectManifest$() : ((runtimeClass1 === $d_sr_Nothing$.getClassOf()) ? $m_s_reflect_ManifestFactory$NothingManifest$() : ((runtimeClass1 === $d_sr_Null$.getClassOf()) ? $m_s_reflect_ManifestFactory$NullManifest$() : new $c_s_reflect_ClassTag$GenericClassTag(runtimeClass1)))))))))))))
});
var $d_s_reflect_ClassTag$ = new $TypeData().initClass({
  s_reflect_ClassTag$: 0
}, false, "scala.reflect.ClassTag$", {
  s_reflect_ClassTag$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_reflect_ClassTag$.prototype.$classData = $d_s_reflect_ClassTag$;
var $n_s_reflect_ClassTag$;
function $m_s_reflect_ClassTag$() {
  if ((!$n_s_reflect_ClassTag$)) {
    $n_s_reflect_ClassTag$ = new $c_s_reflect_ClassTag$()
  };
  return $n_s_reflect_ClassTag$
}
/** @constructor */
function $c_s_reflect_Manifest$() {
  /*<skip>*/
}
$c_s_reflect_Manifest$.prototype = new $h_O();
$c_s_reflect_Manifest$.prototype.constructor = $c_s_reflect_Manifest$;
/** @constructor */
function $h_s_reflect_Manifest$() {
  /*<skip>*/
}
$h_s_reflect_Manifest$.prototype = $c_s_reflect_Manifest$.prototype;
var $d_s_reflect_Manifest$ = new $TypeData().initClass({
  s_reflect_Manifest$: 0
}, false, "scala.reflect.Manifest$", {
  s_reflect_Manifest$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_reflect_Manifest$.prototype.$classData = $d_s_reflect_Manifest$;
var $n_s_reflect_Manifest$;
function $m_s_reflect_Manifest$() {
  if ((!$n_s_reflect_Manifest$)) {
    $n_s_reflect_Manifest$ = new $c_s_reflect_Manifest$()
  };
  return $n_s_reflect_Manifest$
}
/** @constructor */
function $c_sr_AbstractFunction0() {
  /*<skip>*/
}
$c_sr_AbstractFunction0.prototype = new $h_O();
$c_sr_AbstractFunction0.prototype.constructor = $c_sr_AbstractFunction0;
/** @constructor */
function $h_sr_AbstractFunction0() {
  /*<skip>*/
}
$h_sr_AbstractFunction0.prototype = $c_sr_AbstractFunction0.prototype;
$c_sr_AbstractFunction0.prototype.toString__T = (function() {
  return "<function0>"
});
/** @constructor */
function $c_sr_AbstractFunction1() {
  /*<skip>*/
}
$c_sr_AbstractFunction1.prototype = new $h_O();
$c_sr_AbstractFunction1.prototype.constructor = $c_sr_AbstractFunction1;
/** @constructor */
function $h_sr_AbstractFunction1() {
  /*<skip>*/
}
$h_sr_AbstractFunction1.prototype = $c_sr_AbstractFunction1.prototype;
$c_sr_AbstractFunction1.prototype.toString__T = (function() {
  return "<function1>"
});
/** @constructor */
function $c_sr_AbstractFunction2() {
  /*<skip>*/
}
$c_sr_AbstractFunction2.prototype = new $h_O();
$c_sr_AbstractFunction2.prototype.constructor = $c_sr_AbstractFunction2;
/** @constructor */
function $h_sr_AbstractFunction2() {
  /*<skip>*/
}
$h_sr_AbstractFunction2.prototype = $c_sr_AbstractFunction2.prototype;
$c_sr_AbstractFunction2.prototype.toString__T = (function() {
  return "<function2>"
});
/** @constructor */
function $c_sr_AbstractFunction3() {
  /*<skip>*/
}
$c_sr_AbstractFunction3.prototype = new $h_O();
$c_sr_AbstractFunction3.prototype.constructor = $c_sr_AbstractFunction3;
/** @constructor */
function $h_sr_AbstractFunction3() {
  /*<skip>*/
}
$h_sr_AbstractFunction3.prototype = $c_sr_AbstractFunction3.prototype;
$c_sr_AbstractFunction3.prototype.toString__T = (function() {
  return "<function3>"
});
/** @constructor */
function $c_sr_IntRef(elem) {
  this.sr_IntRef__f_elem = 0;
  this.sr_IntRef__f_elem = elem
}
$c_sr_IntRef.prototype = new $h_O();
$c_sr_IntRef.prototype.constructor = $c_sr_IntRef;
/** @constructor */
function $h_sr_IntRef() {
  /*<skip>*/
}
$h_sr_IntRef.prototype = $c_sr_IntRef.prototype;
$c_sr_IntRef.prototype.toString__T = (function() {
  var i = this.sr_IntRef__f_elem;
  return ("" + i)
});
var $d_sr_IntRef = new $TypeData().initClass({
  sr_IntRef: 0
}, false, "scala.runtime.IntRef", {
  sr_IntRef: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_sr_IntRef.prototype.$classData = $d_sr_IntRef;
/** @constructor */
function $c_sr_ObjectRef(elem) {
  this.sr_ObjectRef__f_elem = null;
  this.sr_ObjectRef__f_elem = elem
}
$c_sr_ObjectRef.prototype = new $h_O();
$c_sr_ObjectRef.prototype.constructor = $c_sr_ObjectRef;
/** @constructor */
function $h_sr_ObjectRef() {
  /*<skip>*/
}
$h_sr_ObjectRef.prototype = $c_sr_ObjectRef.prototype;
$c_sr_ObjectRef.prototype.toString__T = (function() {
  var obj = this.sr_ObjectRef__f_elem;
  return ("" + obj)
});
var $d_sr_ObjectRef = new $TypeData().initClass({
  sr_ObjectRef: 0
}, false, "scala.runtime.ObjectRef", {
  sr_ObjectRef: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_sr_ObjectRef.prototype.$classData = $d_sr_ObjectRef;
/** @constructor */
function $c_s_util_Either$() {
  /*<skip>*/
}
$c_s_util_Either$.prototype = new $h_O();
$c_s_util_Either$.prototype.constructor = $c_s_util_Either$;
/** @constructor */
function $h_s_util_Either$() {
  /*<skip>*/
}
$h_s_util_Either$.prototype = $c_s_util_Either$.prototype;
var $d_s_util_Either$ = new $TypeData().initClass({
  s_util_Either$: 0
}, false, "scala.util.Either$", {
  s_util_Either$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_util_Either$.prototype.$classData = $d_s_util_Either$;
var $n_s_util_Either$;
function $m_s_util_Either$() {
  if ((!$n_s_util_Either$)) {
    $n_s_util_Either$ = new $c_s_util_Either$()
  };
  return $n_s_util_Either$
}
/** @constructor */
function $c_s_util_Left$() {
  /*<skip>*/
}
$c_s_util_Left$.prototype = new $h_O();
$c_s_util_Left$.prototype.constructor = $c_s_util_Left$;
/** @constructor */
function $h_s_util_Left$() {
  /*<skip>*/
}
$h_s_util_Left$.prototype = $c_s_util_Left$.prototype;
$c_s_util_Left$.prototype.toString__T = (function() {
  return "Left"
});
var $d_s_util_Left$ = new $TypeData().initClass({
  s_util_Left$: 0
}, false, "scala.util.Left$", {
  s_util_Left$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_util_Left$.prototype.$classData = $d_s_util_Left$;
var $n_s_util_Left$;
function $m_s_util_Left$() {
  if ((!$n_s_util_Left$)) {
    $n_s_util_Left$ = new $c_s_util_Left$()
  };
  return $n_s_util_Left$
}
/** @constructor */
function $c_s_util_Right$() {
  /*<skip>*/
}
$c_s_util_Right$.prototype = new $h_O();
$c_s_util_Right$.prototype.constructor = $c_s_util_Right$;
/** @constructor */
function $h_s_util_Right$() {
  /*<skip>*/
}
$h_s_util_Right$.prototype = $c_s_util_Right$.prototype;
$c_s_util_Right$.prototype.toString__T = (function() {
  return "Right"
});
var $d_s_util_Right$ = new $TypeData().initClass({
  s_util_Right$: 0
}, false, "scala.util.Right$", {
  s_util_Right$: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_util_Right$.prototype.$classData = $d_s_util_Right$;
var $n_s_util_Right$;
function $m_s_util_Right$() {
  if ((!$n_s_util_Right$)) {
    $n_s_util_Right$ = new $c_s_util_Right$()
  };
  return $n_s_util_Right$
}
/** @constructor */
function $c_s_util_hashing_MurmurHash3$() {
  this.s_util_hashing_MurmurHash3$__f_seqSeed = 0;
  this.s_util_hashing_MurmurHash3$__f_mapSeed = 0;
  this.s_util_hashing_MurmurHash3$__f_setSeed = 0;
  this.s_util_hashing_MurmurHash3$__f_emptyMapHash = 0;
  $n_s_util_hashing_MurmurHash3$ = this;
  this.s_util_hashing_MurmurHash3$__f_seqSeed = $f_T__hashCode__I("Seq");
  this.s_util_hashing_MurmurHash3$__f_mapSeed = $f_T__hashCode__I("Map");
  this.s_util_hashing_MurmurHash3$__f_setSeed = $f_T__hashCode__I("Set");
  this.s_util_hashing_MurmurHash3$__f_emptyMapHash = this.unorderedHash__sc_IterableOnce__I__I($m_s_package$().s_package$__f_Nil, this.s_util_hashing_MurmurHash3$__f_mapSeed)
}
$c_s_util_hashing_MurmurHash3$.prototype = new $h_s_util_hashing_MurmurHash3();
$c_s_util_hashing_MurmurHash3$.prototype.constructor = $c_s_util_hashing_MurmurHash3$;
/** @constructor */
function $h_s_util_hashing_MurmurHash3$() {
  /*<skip>*/
}
$h_s_util_hashing_MurmurHash3$.prototype = $c_s_util_hashing_MurmurHash3$.prototype;
$c_s_util_hashing_MurmurHash3$.prototype.tuple2Hash__O__O__I = (function(x, y) {
  return this.tuple2Hash__I__I__I__I($m_sr_Statics$().anyHash__O__I(x), $m_sr_Statics$().anyHash__O__I(y), (-889275714))
});
$c_s_util_hashing_MurmurHash3$.prototype.seqHash__sc_Seq__I = (function(xs) {
  if ($is_sc_IndexedSeq(xs)) {
    var x2 = $as_sc_IndexedSeq(xs);
    return this.indexedSeqHash__sc_IndexedSeq__I__I(x2, this.s_util_hashing_MurmurHash3$__f_seqSeed)
  } else if ((xs instanceof $c_sci_List)) {
    var x3 = $as_sci_List(xs);
    return this.listHash__sci_List__I__I(x3, this.s_util_hashing_MurmurHash3$__f_seqSeed)
  } else {
    return this.orderedHash__sc_IterableOnce__I__I(xs, this.s_util_hashing_MurmurHash3$__f_seqSeed)
  }
});
$c_s_util_hashing_MurmurHash3$.prototype.mapHash__sc_Map__I = (function(xs) {
  if (xs.isEmpty__Z()) {
    return this.s_util_hashing_MurmurHash3$__f_emptyMapHash
  } else {
    var accum = new $c_s_util_hashing_MurmurHash3$accum$1();
    var h = this.s_util_hashing_MurmurHash3$__f_mapSeed;
    xs.foreachEntry__F2__V(accum);
    h = this.mix__I__I__I(h, accum.s_util_hashing_MurmurHash3$accum$1__f_a);
    h = this.mix__I__I__I(h, accum.s_util_hashing_MurmurHash3$accum$1__f_b);
    h = this.mixLast__I__I__I(h, accum.s_util_hashing_MurmurHash3$accum$1__f_c);
    return this.finalizeHash__I__I__I(h, accum.s_util_hashing_MurmurHash3$accum$1__f_n)
  }
});
var $d_s_util_hashing_MurmurHash3$ = new $TypeData().initClass({
  s_util_hashing_MurmurHash3$: 0
}, false, "scala.util.hashing.MurmurHash3$", {
  s_util_hashing_MurmurHash3$: 1,
  s_util_hashing_MurmurHash3: 1,
  O: 1
});
$c_s_util_hashing_MurmurHash3$.prototype.$classData = $d_s_util_hashing_MurmurHash3$;
var $n_s_util_hashing_MurmurHash3$;
function $m_s_util_hashing_MurmurHash3$() {
  if ((!$n_s_util_hashing_MurmurHash3$)) {
    $n_s_util_hashing_MurmurHash3$ = new $c_s_util_hashing_MurmurHash3$()
  };
  return $n_s_util_hashing_MurmurHash3$
}
/** @constructor */
function $c_s_util_hashing_MurmurHash3$accum$1() {
  this.s_util_hashing_MurmurHash3$accum$1__f_a = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_b = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_n = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_c = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_a = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_b = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_n = 0;
  this.s_util_hashing_MurmurHash3$accum$1__f_c = 1
}
$c_s_util_hashing_MurmurHash3$accum$1.prototype = new $h_O();
$c_s_util_hashing_MurmurHash3$accum$1.prototype.constructor = $c_s_util_hashing_MurmurHash3$accum$1;
/** @constructor */
function $h_s_util_hashing_MurmurHash3$accum$1() {
  /*<skip>*/
}
$h_s_util_hashing_MurmurHash3$accum$1.prototype = $c_s_util_hashing_MurmurHash3$accum$1.prototype;
$c_s_util_hashing_MurmurHash3$accum$1.prototype.toString__T = (function() {
  return "<function2>"
});
$c_s_util_hashing_MurmurHash3$accum$1.prototype.apply__O__O__V = (function(k, v) {
  var h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(k, v);
  this.s_util_hashing_MurmurHash3$accum$1__f_a = ((this.s_util_hashing_MurmurHash3$accum$1__f_a + h) | 0);
  this.s_util_hashing_MurmurHash3$accum$1__f_b = (this.s_util_hashing_MurmurHash3$accum$1__f_b ^ h);
  this.s_util_hashing_MurmurHash3$accum$1__f_c = $imul(this.s_util_hashing_MurmurHash3$accum$1__f_c, (1 | h));
  this.s_util_hashing_MurmurHash3$accum$1__f_n = ((1 + this.s_util_hashing_MurmurHash3$accum$1__f_n) | 0)
});
$c_s_util_hashing_MurmurHash3$accum$1.prototype.apply__O__O__O = (function(v1, v2) {
  this.apply__O__O__V(v1, v2)
});
var $d_s_util_hashing_MurmurHash3$accum$1 = new $TypeData().initClass({
  s_util_hashing_MurmurHash3$accum$1: 0
}, false, "scala.util.hashing.MurmurHash3$accum$1", {
  s_util_hashing_MurmurHash3$accum$1: 1,
  O: 1,
  F2: 1
});
$c_s_util_hashing_MurmurHash3$accum$1.prototype.$classData = $d_s_util_hashing_MurmurHash3$accum$1;
function $f_jl_Boolean__equals__O__Z($thiz, that) {
  return ($thiz === that)
}
function $f_jl_Boolean__hashCode__I($thiz) {
  return ($uZ($thiz) ? 1231 : 1237)
}
function $f_jl_Boolean__toString__T($thiz) {
  var b = $uZ($thiz);
  return ("" + b)
}
var $d_jl_Boolean = new $TypeData().initClass({
  jl_Boolean: 0
}, false, "java.lang.Boolean", {
  jl_Boolean: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => ((typeof x) === "boolean")));
function $f_jl_Character__hashCode__I($thiz) {
  var value = $uC($thiz);
  return value
}
function $f_jl_Character__equals__O__Z($thiz, that) {
  if ((that instanceof $Char)) {
    var $$x1 = $uC($thiz);
    var this$1 = $as_jl_Character(that);
    return ($$x1 === $uC(this$1))
  } else {
    return false
  }
}
function $f_jl_Character__toString__T($thiz) {
  var c = $uC($thiz);
  return $as_T(String.fromCharCode(c))
}
function $as_jl_Character(obj) {
  return (((obj instanceof $Char) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Character"))
}
function $isArrayOf_jl_Character(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Character)))
}
function $asArrayOf_jl_Character(obj, depth) {
  return (($isArrayOf_jl_Character(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Character;", depth))
}
var $d_jl_Character = new $TypeData().initClass({
  jl_Character: 0
}, false, "java.lang.Character", {
  jl_Character: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => (x instanceof $Char)));
function $ct_jl_Enum__T__I__($thiz, _name, _ordinal) {
  $thiz.jl_Enum__f__name = _name;
  $thiz.jl_Enum__f__ordinal = _ordinal;
  return $thiz
}
/** @constructor */
function $c_jl_Enum() {
  this.jl_Enum__f__name = null;
  this.jl_Enum__f__ordinal = 0
}
$c_jl_Enum.prototype = new $h_O();
$c_jl_Enum.prototype.constructor = $c_jl_Enum;
/** @constructor */
function $h_jl_Enum() {
  /*<skip>*/
}
$h_jl_Enum.prototype = $c_jl_Enum.prototype;
$c_jl_Enum.prototype.toString__T = (function() {
  return this.jl_Enum__f__name
});
$c_jl_Enum.prototype.equals__O__Z = (function(that) {
  return (this === that)
});
$c_jl_Enum.prototype.hashCode__I = (function() {
  return $systemIdentityHashCode(this)
});
class $c_jl_Error extends $c_jl_Throwable {
}
function $ct_jl_Exception__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
class $c_jl_Exception extends $c_jl_Throwable {
}
var $d_jl_Exception = new $TypeData().initClass({
  jl_Exception: 0
}, false, "java.lang.Exception", {
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_Exception.prototype.$classData = $d_jl_Exception;
function $f_Ljava_time_chrono_Chronology__equals__O__Z($thiz, obj) {
  if ($is_Ljava_time_chrono_Chronology(obj)) {
    var x2 = $as_Ljava_time_chrono_Chronology(obj);
    return (($thiz === x2) || ($f_T__compareTo__T__I("ISO", "ISO") === 0))
  } else {
    return false
  }
}
function $f_Ljava_time_chrono_Chronology__hashCode__I($thiz) {
  var this$1 = $objectGetClass($thiz);
  return ($systemIdentityHashCode(this$1) ^ $f_T__hashCode__I("ISO"))
}
function $is_Ljava_time_chrono_Chronology(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.Ljava_time_chrono_Chronology)))
}
function $as_Ljava_time_chrono_Chronology(obj) {
  return (($is_Ljava_time_chrono_Chronology(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "java.time.chrono.Chronology"))
}
function $isArrayOf_Ljava_time_chrono_Chronology(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_time_chrono_Chronology)))
}
function $asArrayOf_Ljava_time_chrono_Chronology(obj, depth) {
  return (($isArrayOf_Ljava_time_chrono_Chronology(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.time.chrono.Chronology;", depth))
}
/** @constructor */
function $c_s_$less$colon$less() {
  /*<skip>*/
}
$c_s_$less$colon$less.prototype = new $h_O();
$c_s_$less$colon$less.prototype.constructor = $c_s_$less$colon$less;
/** @constructor */
function $h_s_$less$colon$less() {
  /*<skip>*/
}
$h_s_$less$colon$less.prototype = $c_s_$less$colon$less.prototype;
/** @constructor */
function $c_s_Predef$() {
  this.s_Predef$__f_Map = null;
  this.s_Predef$__f_Set = null;
  this.s_Predef$__f_$minus$greater = null;
  this.s_Predef$__f_Manifest = null;
  this.s_Predef$__f_NoManifest = null;
  $n_s_Predef$ = this;
  $m_s_package$();
  $m_sci_List$();
  this.s_Predef$__f_Map = $m_sci_Map$();
  this.s_Predef$__f_Set = $m_sci_Set$();
  this.s_Predef$__f_$minus$greater = $m_T2$();
  this.s_Predef$__f_Manifest = $m_s_reflect_Manifest$();
  this.s_Predef$__f_NoManifest = $m_s_reflect_NoManifest$()
}
$c_s_Predef$.prototype = new $h_s_LowPriorityImplicits();
$c_s_Predef$.prototype.constructor = $c_s_Predef$;
/** @constructor */
function $h_s_Predef$() {
  /*<skip>*/
}
$h_s_Predef$.prototype = $c_s_Predef$.prototype;
$c_s_Predef$.prototype.require__Z__V = (function(requirement) {
  if ((!requirement)) {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), "requirement failed")
  }
});
var $d_s_Predef$ = new $TypeData().initClass({
  s_Predef$: 0
}, false, "scala.Predef$", {
  s_Predef$: 1,
  s_LowPriorityImplicits: 1,
  s_LowPriorityImplicits2: 1,
  O: 1
});
$c_s_Predef$.prototype.$classData = $d_s_Predef$;
var $n_s_Predef$;
function $m_s_Predef$() {
  if ((!$n_s_Predef$)) {
    $n_s_Predef$ = new $c_s_Predef$()
  };
  return $n_s_Predef$
}
function $f_s_Product2__productElement__I__O($thiz, n) {
  switch (n) {
    case 0: {
      return $thiz.T2__f__1;
      break
    }
    case 1: {
      return $thiz.T2__f__2;
      break
    }
    default: {
      throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (n + " is out of bounds (min 0, max 1)"))
    }
  }
}
function $ct_sc_IterableFactory$Delegate__sc_IterableFactory__($thiz, delegate) {
  $thiz.sc_IterableFactory$Delegate__f_delegate = delegate;
  return $thiz
}
/** @constructor */
function $c_sc_IterableFactory$Delegate() {
  this.sc_IterableFactory$Delegate__f_delegate = null
}
$c_sc_IterableFactory$Delegate.prototype = new $h_O();
$c_sc_IterableFactory$Delegate.prototype.constructor = $c_sc_IterableFactory$Delegate;
/** @constructor */
function $h_sc_IterableFactory$Delegate() {
  /*<skip>*/
}
$h_sc_IterableFactory$Delegate.prototype = $c_sc_IterableFactory$Delegate.prototype;
$c_sc_IterableFactory$Delegate.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.sc_IterableFactory$Delegate__f_delegate.from__sc_IterableOnce__O(it)
});
function $f_sc_IterableOps__sizeCompare__I__I($thiz, otherSize) {
  if ((otherSize < 0)) {
    return 1
  } else {
    var known = $thiz.knownSize__I();
    if ((known >= 0)) {
      return ((known === otherSize) ? 0 : ((known < otherSize) ? (-1) : 1))
    } else {
      var i = 0;
      var it = $thiz.iterator__sc_Iterator();
      while (it.hasNext__Z()) {
        if ((i === otherSize)) {
          return 1
        };
        it.next__O();
        i = ((1 + i) | 0)
      };
      return ((i - otherSize) | 0)
    }
  }
}
function $f_sc_IterableOps__drop__I__O($thiz, n) {
  return $thiz.fromSpecific__sc_IterableOnce__O($ct_sc_View$Drop__sc_IterableOps__I__(new $c_sc_View$Drop(), $thiz, n))
}
function $f_sc_IterableOps__tail__O($thiz) {
  if ($thiz.isEmpty__Z()) {
    throw $ct_jl_UnsupportedOperationException__(new $c_jl_UnsupportedOperationException())
  };
  return $thiz.drop__I__O(1)
}
function $is_sc_IterableOps(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_IterableOps)))
}
function $as_sc_IterableOps(obj) {
  return (($is_sc_IterableOps(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.IterableOps"))
}
function $isArrayOf_sc_IterableOps(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_IterableOps)))
}
function $asArrayOf_sc_IterableOps(obj, depth) {
  return (($isArrayOf_sc_IterableOps(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.IterableOps;", depth))
}
function $f_sc_Iterator__indexWhere__F1__I__I($thiz, p, from) {
  var i = ((from > 0) ? from : 0);
  $thiz.drop__I__sc_Iterator(from);
  while ($thiz.hasNext__Z()) {
    if ($uZ(p.apply__O__O($thiz.next__O()))) {
      return i
    };
    i = ((1 + i) | 0)
  };
  return (-1)
}
function $f_sc_Iterator__drop__I__sc_Iterator($thiz, n) {
  var i = 0;
  while (((i < n) && $thiz.hasNext__Z())) {
    $thiz.next__O();
    i = ((1 + i) | 0)
  };
  return $thiz
}
function $f_sc_Iterator__sameElements__sc_IterableOnce__Z($thiz, that) {
  var those = that.iterator__sc_Iterator();
  while (($thiz.hasNext__Z() && those.hasNext__Z())) {
    if ((!$m_sr_BoxesRunTime$().equals__O__O__Z($thiz.next__O(), those.next__O()))) {
      return false
    }
  };
  return ($thiz.hasNext__Z() === those.hasNext__Z())
}
function $is_sc_Iterator(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_Iterator)))
}
function $as_sc_Iterator(obj) {
  return (($is_sc_Iterator(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.Iterator"))
}
function $isArrayOf_sc_Iterator(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_Iterator)))
}
function $asArrayOf_sc_Iterator(obj, depth) {
  return (($isArrayOf_sc_Iterator(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.Iterator;", depth))
}
/** @constructor */
function $c_sc_Iterator$() {
  this.sc_Iterator$__f_scala$collection$Iterator$$_empty = null;
  $n_sc_Iterator$ = this;
  this.sc_Iterator$__f_scala$collection$Iterator$$_empty = new $c_sc_Iterator$$anon$19()
}
$c_sc_Iterator$.prototype = new $h_O();
$c_sc_Iterator$.prototype.constructor = $c_sc_Iterator$;
/** @constructor */
function $h_sc_Iterator$() {
  /*<skip>*/
}
$h_sc_Iterator$.prototype = $c_sc_Iterator$.prototype;
$c_sc_Iterator$.prototype.from__sc_IterableOnce__O = (function(source) {
  return source.iterator__sc_Iterator()
});
var $d_sc_Iterator$ = new $TypeData().initClass({
  sc_Iterator$: 0
}, false, "scala.collection.Iterator$", {
  sc_Iterator$: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sc_Iterator$.prototype.$classData = $d_sc_Iterator$;
var $n_sc_Iterator$;
function $m_sc_Iterator$() {
  if ((!$n_sc_Iterator$)) {
    $n_sc_Iterator$ = new $c_sc_Iterator$()
  };
  return $n_sc_Iterator$
}
function $ct_sc_MapFactory$Delegate__sc_MapFactory__($thiz, delegate) {
  $thiz.sc_MapFactory$Delegate__f_delegate = delegate;
  return $thiz
}
/** @constructor */
function $c_sc_MapFactory$Delegate() {
  this.sc_MapFactory$Delegate__f_delegate = null
}
$c_sc_MapFactory$Delegate.prototype = new $h_O();
$c_sc_MapFactory$Delegate.prototype.constructor = $c_sc_MapFactory$Delegate;
/** @constructor */
function $h_sc_MapFactory$Delegate() {
  /*<skip>*/
}
$h_sc_MapFactory$Delegate.prototype = $c_sc_MapFactory$Delegate.prototype;
$c_sc_MapFactory$Delegate.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.sc_MapFactory$Delegate__f_delegate.from__sc_IterableOnce__O(it)
});
/** @constructor */
function $c_sc_View$() {
  /*<skip>*/
}
$c_sc_View$.prototype = new $h_O();
$c_sc_View$.prototype.constructor = $c_sc_View$;
/** @constructor */
function $h_sc_View$() {
  /*<skip>*/
}
$h_sc_View$.prototype = $c_sc_View$.prototype;
$c_sc_View$.prototype.from__sc_IterableOnce__sc_View = (function(it) {
  if ($is_sc_View(it)) {
    var x2 = $as_sc_View(it);
    return x2
  } else if ($is_sc_Iterable(it)) {
    var x3 = $as_sc_Iterable(it);
    var it$1 = new $c_sjsr_AnonFunction0(((this$1, x3$1) => (() => x3$1.iterator__sc_Iterator()))(this, x3));
    return new $c_sc_View$$anon$1(it$1)
  } else {
    var this$3 = $m_sci_LazyList$().from__sc_IterableOnce__sci_LazyList(it);
    return $ct_sc_SeqView$Id__sc_SeqOps__(new $c_sc_SeqView$Id(), this$3)
  }
});
$c_sc_View$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sc_View(source)
});
var $d_sc_View$ = new $TypeData().initClass({
  sc_View$: 0
}, false, "scala.collection.View$", {
  sc_View$: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sc_View$.prototype.$classData = $d_sc_View$;
var $n_sc_View$;
function $m_sc_View$() {
  if ((!$n_sc_View$)) {
    $n_sc_View$ = new $c_sc_View$()
  };
  return $n_sc_View$
}
/** @constructor */
function $c_sci_BitmapIndexedMapNode(dataMap, nodeMap, content, originalHashes, size, cachedJavaKeySetHashCode) {
  this.sci_BitmapIndexedMapNode__f_dataMap = 0;
  this.sci_BitmapIndexedMapNode__f_nodeMap = 0;
  this.sci_BitmapIndexedMapNode__f_content = null;
  this.sci_BitmapIndexedMapNode__f_originalHashes = null;
  this.sci_BitmapIndexedMapNode__f_size = 0;
  this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode = 0;
  this.sci_BitmapIndexedMapNode__f_dataMap = dataMap;
  this.sci_BitmapIndexedMapNode__f_nodeMap = nodeMap;
  this.sci_BitmapIndexedMapNode__f_content = content;
  this.sci_BitmapIndexedMapNode__f_originalHashes = originalHashes;
  this.sci_BitmapIndexedMapNode__f_size = size;
  this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode = cachedJavaKeySetHashCode
}
$c_sci_BitmapIndexedMapNode.prototype = new $h_sci_MapNode();
$c_sci_BitmapIndexedMapNode.prototype.constructor = $c_sci_BitmapIndexedMapNode;
/** @constructor */
function $h_sci_BitmapIndexedMapNode() {
  /*<skip>*/
}
$h_sci_BitmapIndexedMapNode.prototype = $c_sci_BitmapIndexedMapNode.prototype;
$c_sci_BitmapIndexedMapNode.prototype.size__I = (function() {
  return this.sci_BitmapIndexedMapNode__f_size
});
$c_sci_BitmapIndexedMapNode.prototype.cachedJavaKeySetHashCode__I = (function() {
  return this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode
});
$c_sci_BitmapIndexedMapNode.prototype.getKey__I__O = (function(index) {
  return this.sci_BitmapIndexedMapNode__f_content.get((index << 1))
});
$c_sci_BitmapIndexedMapNode.prototype.getValue__I__O = (function(index) {
  return this.sci_BitmapIndexedMapNode__f_content.get(((1 + (index << 1)) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.getPayload__I__T2 = (function(index) {
  return new $c_T2(this.sci_BitmapIndexedMapNode__f_content.get((index << 1)), this.sci_BitmapIndexedMapNode__f_content.get(((1 + (index << 1)) | 0)))
});
$c_sci_BitmapIndexedMapNode.prototype.getHash__I__I = (function(index) {
  return this.sci_BitmapIndexedMapNode__f_originalHashes.get(index)
});
$c_sci_BitmapIndexedMapNode.prototype.getNode__I__sci_MapNode = (function(index) {
  return $as_sci_MapNode(this.sci_BitmapIndexedMapNode__f_content.get((((((-1) + this.sci_BitmapIndexedMapNode__f_content.u.length) | 0) - index) | 0)))
});
$c_sci_BitmapIndexedMapNode.prototype.apply__O__I__I__I__O = (function(key, originalHash, keyHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.getKey__I__O(index))) {
      return this.getValue__I__O(index)
    } else {
      throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
    }
  } else if (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
    return this.getNode__I__sci_MapNode($m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos)).apply__O__I__I__I__O(key, originalHash, keyHash, ((5 + shift) | 0))
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sci_BitmapIndexedMapNode.prototype.get__O__I__I__I__s_Option = (function(key, originalHash, keyHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    var key0 = this.getKey__I__O(index);
    return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, key0) ? new $c_s_Some(this.getValue__I__O(index)) : $m_s_None$())
  } else if (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos);
    return this.getNode__I__sci_MapNode(index$2).get__O__I__I__I__s_Option(key, originalHash, keyHash, ((5 + shift) | 0))
  } else {
    return $m_s_None$()
  }
});
$c_sci_BitmapIndexedMapNode.prototype.getOrElse__O__I__I__I__F0__O = (function(key, originalHash, keyHash, shift, f) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    var key0 = this.getKey__I__O(index);
    return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, key0) ? this.getValue__I__O(index) : f.apply__O())
  } else if (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos);
    return this.getNode__I__sci_MapNode(index$2).getOrElse__O__I__I__I__F0__O(key, originalHash, keyHash, ((5 + shift) | 0), f)
  } else {
    return f.apply__O()
  }
});
$c_sci_BitmapIndexedMapNode.prototype.containsKey__O__I__I__I__Z = (function(key, originalHash, keyHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    return ((this.sci_BitmapIndexedMapNode__f_originalHashes.get(index) === originalHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.getKey__I__O(index)))
  } else {
    return (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0) && this.getNode__I__sci_MapNode($m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos)).containsKey__O__I__I__I__Z(key, originalHash, keyHash, ((5 + shift) | 0)))
  }
});
$c_sci_BitmapIndexedMapNode.prototype.updated__O__O__I__I__I__Z__sci_BitmapIndexedMapNode = (function(key, value, originalHash, keyHash, shift, replaceValue) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    var key0 = this.getKey__I__O(index);
    var key0UnimprovedHash = this.getHash__I__I(index);
    if (((key0UnimprovedHash === originalHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(key0, key))) {
      if (replaceValue) {
        var value0 = this.getValue__I__O(index);
        return ((Object.is(key0, key) && Object.is(value0, value)) ? this : this.copyAndSetValue__I__O__O__sci_BitmapIndexedMapNode(bitpos, key, value))
      } else {
        return this
      }
    } else {
      var value0$2 = this.getValue__I__O(index);
      var key0Hash = $m_sc_Hashing$().improve__I__I(key0UnimprovedHash);
      var subNodeNew = this.mergeTwoKeyValPairs__O__O__I__I__O__O__I__I__I__sci_MapNode(key0, value0$2, key0UnimprovedHash, key0Hash, key, value, originalHash, keyHash, ((5 + shift) | 0));
      return this.copyAndMigrateFromInlineToNode__I__I__sci_MapNode__sci_BitmapIndexedMapNode(bitpos, key0Hash, subNodeNew)
    }
  } else if (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos);
    var subNode = this.getNode__I__sci_MapNode(index$2);
    var subNodeNew$2 = subNode.updated__O__O__I__I__I__Z__sci_MapNode(key, value, originalHash, keyHash, ((5 + shift) | 0), replaceValue);
    return ((subNodeNew$2 === subNode) ? this : this.copyAndSetNode__I__sci_MapNode__sci_MapNode__sci_BitmapIndexedMapNode(bitpos, subNode, subNodeNew$2))
  } else {
    return this.copyAndInsertValue__I__O__I__I__O__sci_BitmapIndexedMapNode(bitpos, key, originalHash, keyHash, value)
  }
});
$c_sci_BitmapIndexedMapNode.prototype.removed__O__I__I__I__sci_BitmapIndexedMapNode = (function(key, originalHash, keyHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
    var key0 = this.getKey__I__O(index);
    if ($m_sr_BoxesRunTime$().equals__O__O__Z(key0, key)) {
      var i = this.sci_BitmapIndexedMapNode__f_dataMap;
      if (($m_jl_Integer$().bitCount__I__I(i) === 2)) {
        var i$1 = this.sci_BitmapIndexedMapNode__f_nodeMap;
        var $$x1 = ($m_jl_Integer$().bitCount__I__I(i$1) === 0)
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var newDataMap = ((shift === 0) ? (this.sci_BitmapIndexedMapNode__f_dataMap ^ bitpos) : $m_sci_Node$().bitposFrom__I__I($m_sci_Node$().maskFrom__I__I__I(keyHash, 0)));
        return ((index === 0) ? new $c_sci_BitmapIndexedMapNode(newDataMap, 0, new $ac_O([this.getKey__I__O(1), this.getValue__I__O(1)]), new $ac_I(new Int32Array([this.sci_BitmapIndexedMapNode__f_originalHashes.get(1)])), 1, $m_sc_Hashing$().improve__I__I(this.getHash__I__I(1))) : new $c_sci_BitmapIndexedMapNode(newDataMap, 0, new $ac_O([this.getKey__I__O(0), this.getValue__I__O(0)]), new $ac_I(new Int32Array([this.sci_BitmapIndexedMapNode__f_originalHashes.get(0)])), 1, $m_sc_Hashing$().improve__I__I(this.getHash__I__I(0))))
      } else {
        return this.copyAndRemoveValue__I__I__sci_BitmapIndexedMapNode(bitpos, keyHash)
      }
    } else {
      return this
    }
  } else if (((this.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos);
    var subNode = this.getNode__I__sci_MapNode(index$2);
    var subNodeNew = subNode.removed__O__I__I__I__sci_MapNode(key, originalHash, keyHash, ((5 + shift) | 0));
    if ((subNodeNew === subNode)) {
      return this
    };
    var subNodeNewSize = subNodeNew.size__I();
    return ((subNodeNewSize === 1) ? ((this.sci_BitmapIndexedMapNode__f_size === subNode.size__I()) ? $as_sci_BitmapIndexedMapNode(subNodeNew) : this.copyAndMigrateFromNodeToInline__I__sci_MapNode__sci_MapNode__sci_BitmapIndexedMapNode(bitpos, subNode, subNodeNew)) : ((subNodeNewSize > 1) ? this.copyAndSetNode__I__sci_MapNode__sci_MapNode__sci_BitmapIndexedMapNode(bitpos, subNode, subNodeNew) : this))
  } else {
    return this
  }
});
$c_sci_BitmapIndexedMapNode.prototype.mergeTwoKeyValPairs__O__O__I__I__O__O__I__I__I__sci_MapNode = (function(key0, value0, originalHash0, keyHash0, key1, value1, originalHash1, keyHash1, shift) {
  if ((shift >= 32)) {
    var this$4 = $m_sci_Vector$();
    var array = [new $c_T2(key0, value0), new $c_T2(key1, value1)];
    var elems = new $c_sjsr_WrappedVarArgs(array);
    return new $c_sci_HashCollisionMapNode(originalHash0, keyHash0, this$4.from__sc_IterableOnce__sci_Vector(elems))
  } else {
    var mask0 = $m_sci_Node$().maskFrom__I__I__I(keyHash0, shift);
    var mask1 = $m_sci_Node$().maskFrom__I__I__I(keyHash1, shift);
    var newCachedHash = ((keyHash0 + keyHash1) | 0);
    if ((mask0 !== mask1)) {
      var dataMap = ($m_sci_Node$().bitposFrom__I__I(mask0) | $m_sci_Node$().bitposFrom__I__I(mask1));
      return ((mask0 < mask1) ? new $c_sci_BitmapIndexedMapNode(dataMap, 0, new $ac_O([key0, value0, key1, value1]), new $ac_I(new Int32Array([originalHash0, originalHash1])), 2, newCachedHash) : new $c_sci_BitmapIndexedMapNode(dataMap, 0, new $ac_O([key1, value1, key0, value0]), new $ac_I(new Int32Array([originalHash1, originalHash0])), 2, newCachedHash))
    } else {
      var nodeMap = $m_sci_Node$().bitposFrom__I__I(mask0);
      var node = this.mergeTwoKeyValPairs__O__O__I__I__O__O__I__I__I__sci_MapNode(key0, value0, originalHash0, keyHash0, key1, value1, originalHash1, keyHash1, ((5 + shift) | 0));
      return new $c_sci_BitmapIndexedMapNode(0, nodeMap, new $ac_O([node]), $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyIntArray, node.size__I(), node.cachedJavaKeySetHashCode__I())
    }
  }
});
$c_sci_BitmapIndexedMapNode.prototype.hasNodes__Z = (function() {
  return (this.sci_BitmapIndexedMapNode__f_nodeMap !== 0)
});
$c_sci_BitmapIndexedMapNode.prototype.nodeArity__I = (function() {
  var i = this.sci_BitmapIndexedMapNode__f_nodeMap;
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedMapNode.prototype.hasPayload__Z = (function() {
  return (this.sci_BitmapIndexedMapNode__f_dataMap !== 0)
});
$c_sci_BitmapIndexedMapNode.prototype.payloadArity__I = (function() {
  var i = this.sci_BitmapIndexedMapNode__f_dataMap;
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedMapNode.prototype.dataIndex__I__I = (function(bitpos) {
  var i = (this.sci_BitmapIndexedMapNode__f_dataMap & (((-1) + bitpos) | 0));
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedMapNode.prototype.nodeIndex__I__I = (function(bitpos) {
  var i = (this.sci_BitmapIndexedMapNode__f_nodeMap & (((-1) + bitpos) | 0));
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndSetValue__I__O__O__sci_BitmapIndexedMapNode = (function(bitpos, newKey, newValue) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idx = (dataIx << 1);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O(src.u.length);
  var length = src.u.length;
  src.copyTo(0, dst, 0, length);
  dst.set(((1 + idx) | 0), newValue);
  return new $c_sci_BitmapIndexedMapNode(this.sci_BitmapIndexedMapNode__f_dataMap, this.sci_BitmapIndexedMapNode__f_nodeMap, dst, this.sci_BitmapIndexedMapNode__f_originalHashes, this.sci_BitmapIndexedMapNode__f_size, this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode)
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndSetNode__I__sci_MapNode__sci_MapNode__sci_BitmapIndexedMapNode = (function(bitpos, oldNode, newNode) {
  var idx = (((((-1) + this.sci_BitmapIndexedMapNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O(src.u.length);
  var length = src.u.length;
  src.copyTo(0, dst, 0, length);
  dst.set(idx, newNode);
  return new $c_sci_BitmapIndexedMapNode(this.sci_BitmapIndexedMapNode__f_dataMap, this.sci_BitmapIndexedMapNode__f_nodeMap, dst, this.sci_BitmapIndexedMapNode__f_originalHashes, ((((this.sci_BitmapIndexedMapNode__f_size - oldNode.size__I()) | 0) + newNode.size__I()) | 0), ((((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode__I()) | 0) + newNode.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndInsertValue__I__O__I__I__O__sci_BitmapIndexedMapNode = (function(bitpos, key, originalHash, keyHash, value) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idx = (dataIx << 1);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O(((2 + src.u.length) | 0));
  src.copyTo(0, dst, 0, idx);
  dst.set(idx, key);
  dst.set(((1 + idx) | 0), value);
  var destPos = ((2 + idx) | 0);
  var length = ((src.u.length - idx) | 0);
  src.copyTo(idx, dst, destPos, length);
  var dstHashes = this.insertElement__AI__I__I__AI(this.sci_BitmapIndexedMapNode__f_originalHashes, dataIx, originalHash);
  return new $c_sci_BitmapIndexedMapNode((this.sci_BitmapIndexedMapNode__f_dataMap | bitpos), this.sci_BitmapIndexedMapNode__f_nodeMap, dst, dstHashes, ((1 + this.sci_BitmapIndexedMapNode__f_size) | 0), ((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode + keyHash) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndRemoveValue__I__I__sci_BitmapIndexedMapNode = (function(bitpos, keyHash) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idx = (dataIx << 1);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O((((-2) + src.u.length) | 0));
  src.copyTo(0, dst, 0, idx);
  var srcPos = ((2 + idx) | 0);
  var length = (((-2) + ((src.u.length - idx) | 0)) | 0);
  src.copyTo(srcPos, dst, idx, length);
  var dstHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedMapNode__f_originalHashes, dataIx);
  return new $c_sci_BitmapIndexedMapNode((this.sci_BitmapIndexedMapNode__f_dataMap ^ bitpos), this.sci_BitmapIndexedMapNode__f_nodeMap, dst, dstHashes, (((-1) + this.sci_BitmapIndexedMapNode__f_size) | 0), ((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode - keyHash) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.migrateFromInlineToNodeInPlace__I__I__sci_MapNode__sci_BitmapIndexedMapNode = (function(bitpos, keyHash, node) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idxOld = (dataIx << 1);
  var idxNew = (((((-2) + this.sci_BitmapIndexedMapNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O((((-1) + src.u.length) | 0));
  src.copyTo(0, dst, 0, idxOld);
  var srcPos = ((2 + idxOld) | 0);
  var length = ((idxNew - idxOld) | 0);
  src.copyTo(srcPos, dst, idxOld, length);
  dst.set(idxNew, node);
  var srcPos$1 = ((2 + idxNew) | 0);
  var destPos = ((1 + idxNew) | 0);
  var length$1 = (((-2) + ((src.u.length - idxNew) | 0)) | 0);
  src.copyTo(srcPos$1, dst, destPos, length$1);
  var dstHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedMapNode__f_originalHashes, dataIx);
  this.sci_BitmapIndexedMapNode__f_dataMap = (this.sci_BitmapIndexedMapNode__f_dataMap ^ bitpos);
  this.sci_BitmapIndexedMapNode__f_nodeMap = (this.sci_BitmapIndexedMapNode__f_nodeMap | bitpos);
  this.sci_BitmapIndexedMapNode__f_content = dst;
  this.sci_BitmapIndexedMapNode__f_originalHashes = dstHashes;
  this.sci_BitmapIndexedMapNode__f_size = (((((-1) + this.sci_BitmapIndexedMapNode__f_size) | 0) + node.size__I()) | 0);
  this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode = ((((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode - keyHash) | 0) + node.cachedJavaKeySetHashCode__I()) | 0);
  return this
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndMigrateFromInlineToNode__I__I__sci_MapNode__sci_BitmapIndexedMapNode = (function(bitpos, keyHash, node) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idxOld = (dataIx << 1);
  var idxNew = (((((-2) + this.sci_BitmapIndexedMapNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O((((-1) + src.u.length) | 0));
  src.copyTo(0, dst, 0, idxOld);
  var srcPos = ((2 + idxOld) | 0);
  var length = ((idxNew - idxOld) | 0);
  src.copyTo(srcPos, dst, idxOld, length);
  dst.set(idxNew, node);
  var srcPos$1 = ((2 + idxNew) | 0);
  var destPos = ((1 + idxNew) | 0);
  var length$1 = (((-2) + ((src.u.length - idxNew) | 0)) | 0);
  src.copyTo(srcPos$1, dst, destPos, length$1);
  var dstHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedMapNode__f_originalHashes, dataIx);
  return new $c_sci_BitmapIndexedMapNode((this.sci_BitmapIndexedMapNode__f_dataMap ^ bitpos), (this.sci_BitmapIndexedMapNode__f_nodeMap | bitpos), dst, dstHashes, (((((-1) + this.sci_BitmapIndexedMapNode__f_size) | 0) + node.size__I()) | 0), ((((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode - keyHash) | 0) + node.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.copyAndMigrateFromNodeToInline__I__sci_MapNode__sci_MapNode__sci_BitmapIndexedMapNode = (function(bitpos, oldNode, node) {
  var idxOld = (((((-1) + this.sci_BitmapIndexedMapNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var dataIxNew = this.dataIndex__I__I(bitpos);
  var idxNew = (dataIxNew << 1);
  var key = node.getKey__I__O(0);
  var value = node.getValue__I__O(0);
  var src = this.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O(((1 + src.u.length) | 0));
  src.copyTo(0, dst, 0, idxNew);
  dst.set(idxNew, key);
  dst.set(((1 + idxNew) | 0), value);
  var destPos = ((2 + idxNew) | 0);
  var length = ((idxOld - idxNew) | 0);
  src.copyTo(idxNew, dst, destPos, length);
  var srcPos = ((1 + idxOld) | 0);
  var destPos$1 = ((2 + idxOld) | 0);
  var length$1 = (((-1) + ((src.u.length - idxOld) | 0)) | 0);
  src.copyTo(srcPos, dst, destPos$1, length$1);
  var hash = node.getHash__I__I(0);
  var dstHashes = this.insertElement__AI__I__I__AI(this.sci_BitmapIndexedMapNode__f_originalHashes, dataIxNew, hash);
  return new $c_sci_BitmapIndexedMapNode((this.sci_BitmapIndexedMapNode__f_dataMap | bitpos), (this.sci_BitmapIndexedMapNode__f_nodeMap ^ bitpos), dst, dstHashes, ((1 + ((this.sci_BitmapIndexedMapNode__f_size - oldNode.size__I()) | 0)) | 0), ((((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode__I()) | 0) + node.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedMapNode.prototype.foreachEntry__F2__V = (function(f) {
  var i = this.sci_BitmapIndexedMapNode__f_dataMap;
  var iN = $m_jl_Integer$().bitCount__I__I(i);
  var i$1 = 0;
  while ((i$1 < iN)) {
    f.apply__O__O__O(this.getKey__I__O(i$1), this.getValue__I__O(i$1));
    i$1 = ((1 + i$1) | 0)
  };
  var i$2 = this.sci_BitmapIndexedMapNode__f_nodeMap;
  var jN = $m_jl_Integer$().bitCount__I__I(i$2);
  var j = 0;
  while ((j < jN)) {
    this.getNode__I__sci_MapNode(j).foreachEntry__F2__V(f);
    j = ((1 + j) | 0)
  }
});
$c_sci_BitmapIndexedMapNode.prototype.foreachWithHash__F3__V = (function(f) {
  var i = 0;
  var i$1 = this.sci_BitmapIndexedMapNode__f_dataMap;
  var iN = $m_jl_Integer$().bitCount__I__I(i$1);
  while ((i < iN)) {
    f.apply__O__O__O__O(this.getKey__I__O(i), this.getValue__I__O(i), this.getHash__I__I(i));
    i = ((1 + i) | 0)
  };
  var i$2 = this.sci_BitmapIndexedMapNode__f_nodeMap;
  var jN = $m_jl_Integer$().bitCount__I__I(i$2);
  var j = 0;
  while ((j < jN)) {
    this.getNode__I__sci_MapNode(j).foreachWithHash__F3__V(f);
    j = ((1 + j) | 0)
  }
});
$c_sci_BitmapIndexedMapNode.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_BitmapIndexedMapNode)) {
    var x2 = $as_sci_BitmapIndexedMapNode(that);
    if ((this === x2)) {
      return true
    } else {
      if (((((this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode === x2.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode) && (this.sci_BitmapIndexedMapNode__f_nodeMap === x2.sci_BitmapIndexedMapNode__f_nodeMap)) && (this.sci_BitmapIndexedMapNode__f_dataMap === x2.sci_BitmapIndexedMapNode__f_dataMap)) && (this.sci_BitmapIndexedMapNode__f_size === x2.sci_BitmapIndexedMapNode__f_size))) {
        var a = this.sci_BitmapIndexedMapNode__f_originalHashes;
        var b = x2.sci_BitmapIndexedMapNode__f_originalHashes;
        var $$x1 = $m_ju_Arrays$().equals__AI__AI__Z(a, b)
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var a1 = this.sci_BitmapIndexedMapNode__f_content;
        var a2 = x2.sci_BitmapIndexedMapNode__f_content;
        var length = this.sci_BitmapIndexedMapNode__f_content.u.length;
        if ((a1 === a2)) {
          return true
        } else {
          var isEqual = true;
          var i = 0;
          while ((isEqual && (i < length))) {
            isEqual = $m_sr_BoxesRunTime$().equals__O__O__Z(a1.get(i), a2.get(i));
            i = ((1 + i) | 0)
          };
          return isEqual
        }
      } else {
        return false
      }
    }
  } else {
    return false
  }
});
$c_sci_BitmapIndexedMapNode.prototype.hashCode__I = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "Trie nodes do not support hashing.")
});
$c_sci_BitmapIndexedMapNode.prototype.copy__sci_BitmapIndexedMapNode = (function() {
  var this$1 = this.sci_BitmapIndexedMapNode__f_content;
  var contentClone = this$1.clone__O();
  var contentLength = contentClone.u.length;
  var i = this.sci_BitmapIndexedMapNode__f_dataMap;
  var i$1 = ($m_jl_Integer$().bitCount__I__I(i) << 1);
  while ((i$1 < contentLength)) {
    contentClone.set(i$1, $as_sci_MapNode(contentClone.get(i$1)).copy__sci_MapNode());
    i$1 = ((1 + i$1) | 0)
  };
  var $$x2 = this.sci_BitmapIndexedMapNode__f_dataMap;
  var $$x1 = this.sci_BitmapIndexedMapNode__f_nodeMap;
  var this$2 = this.sci_BitmapIndexedMapNode__f_originalHashes;
  return new $c_sci_BitmapIndexedMapNode($$x2, $$x1, contentClone, this$2.clone__O(), this.sci_BitmapIndexedMapNode__f_size, this.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode)
});
$c_sci_BitmapIndexedMapNode.prototype.copy__sci_MapNode = (function() {
  return this.copy__sci_BitmapIndexedMapNode()
});
$c_sci_BitmapIndexedMapNode.prototype.removed__O__I__I__I__sci_MapNode = (function(key, originalHash, hash, shift) {
  return this.removed__O__I__I__I__sci_BitmapIndexedMapNode(key, originalHash, hash, shift)
});
$c_sci_BitmapIndexedMapNode.prototype.updated__O__O__I__I__I__Z__sci_MapNode = (function(key, value, originalHash, hash, shift, replaceValue) {
  return this.updated__O__O__I__I__I__Z__sci_BitmapIndexedMapNode(key, value, originalHash, hash, shift, replaceValue)
});
$c_sci_BitmapIndexedMapNode.prototype.getNode__I__sci_Node = (function(index) {
  return this.getNode__I__sci_MapNode(index)
});
function $as_sci_BitmapIndexedMapNode(obj) {
  return (((obj instanceof $c_sci_BitmapIndexedMapNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.BitmapIndexedMapNode"))
}
function $isArrayOf_sci_BitmapIndexedMapNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_BitmapIndexedMapNode)))
}
function $asArrayOf_sci_BitmapIndexedMapNode(obj, depth) {
  return (($isArrayOf_sci_BitmapIndexedMapNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.BitmapIndexedMapNode;", depth))
}
var $d_sci_BitmapIndexedMapNode = new $TypeData().initClass({
  sci_BitmapIndexedMapNode: 0
}, false, "scala.collection.immutable.BitmapIndexedMapNode", {
  sci_BitmapIndexedMapNode: 1,
  sci_MapNode: 1,
  sci_Node: 1,
  O: 1
});
$c_sci_BitmapIndexedMapNode.prototype.$classData = $d_sci_BitmapIndexedMapNode;
/** @constructor */
function $c_sci_BitmapIndexedSetNode(dataMap, nodeMap, content, originalHashes, size, cachedJavaKeySetHashCode) {
  this.sci_BitmapIndexedSetNode__f_dataMap = 0;
  this.sci_BitmapIndexedSetNode__f_nodeMap = 0;
  this.sci_BitmapIndexedSetNode__f_content = null;
  this.sci_BitmapIndexedSetNode__f_originalHashes = null;
  this.sci_BitmapIndexedSetNode__f_size = 0;
  this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode = 0;
  this.sci_BitmapIndexedSetNode__f_dataMap = dataMap;
  this.sci_BitmapIndexedSetNode__f_nodeMap = nodeMap;
  this.sci_BitmapIndexedSetNode__f_content = content;
  this.sci_BitmapIndexedSetNode__f_originalHashes = originalHashes;
  this.sci_BitmapIndexedSetNode__f_size = size;
  this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode = cachedJavaKeySetHashCode
}
$c_sci_BitmapIndexedSetNode.prototype = new $h_sci_SetNode();
$c_sci_BitmapIndexedSetNode.prototype.constructor = $c_sci_BitmapIndexedSetNode;
/** @constructor */
function $h_sci_BitmapIndexedSetNode() {
  /*<skip>*/
}
$h_sci_BitmapIndexedSetNode.prototype = $c_sci_BitmapIndexedSetNode.prototype;
$c_sci_BitmapIndexedSetNode.prototype.size__I = (function() {
  return this.sci_BitmapIndexedSetNode__f_size
});
$c_sci_BitmapIndexedSetNode.prototype.cachedJavaKeySetHashCode__I = (function() {
  return this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode
});
$c_sci_BitmapIndexedSetNode.prototype.getPayload__I__O = (function(index) {
  return this.sci_BitmapIndexedSetNode__f_content.get(index)
});
$c_sci_BitmapIndexedSetNode.prototype.getHash__I__I = (function(index) {
  return this.sci_BitmapIndexedSetNode__f_originalHashes.get(index)
});
$c_sci_BitmapIndexedSetNode.prototype.getNode__I__sci_SetNode = (function(index) {
  return $as_sci_SetNode(this.sci_BitmapIndexedSetNode__f_content.get((((((-1) + this.sci_BitmapIndexedSetNode__f_content.u.length) | 0) - index) | 0)))
});
$c_sci_BitmapIndexedSetNode.prototype.contains__O__I__I__I__Z = (function(element, originalHash, elementHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(elementHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedSetNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_dataMap, mask, bitpos);
    return ((this.sci_BitmapIndexedSetNode__f_originalHashes.get(index) === originalHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(element, this.getPayload__I__O(index)))
  };
  if (((this.sci_BitmapIndexedSetNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_nodeMap, mask, bitpos);
    return this.getNode__I__sci_SetNode(index$2).contains__O__I__I__I__Z(element, originalHash, elementHash, ((5 + shift) | 0))
  };
  return false
});
$c_sci_BitmapIndexedSetNode.prototype.updated__O__I__I__I__sci_BitmapIndexedSetNode = (function(element, originalHash, elementHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(elementHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedSetNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_dataMap, mask, bitpos);
    var element0 = this.getPayload__I__O(index);
    if (Object.is(element0, element)) {
      return this
    } else {
      var element0UnimprovedHash = this.getHash__I__I(index);
      var element0Hash = $m_sc_Hashing$().improve__I__I(element0UnimprovedHash);
      if (((originalHash === element0UnimprovedHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(element0, element))) {
        return this
      } else {
        var subNodeNew = this.mergeTwoKeyValPairs__O__I__I__O__I__I__I__sci_SetNode(element0, element0UnimprovedHash, element0Hash, element, originalHash, elementHash, ((5 + shift) | 0));
        return this.copyAndMigrateFromInlineToNode__I__I__sci_SetNode__sci_BitmapIndexedSetNode(bitpos, element0Hash, subNodeNew)
      }
    }
  };
  if (((this.sci_BitmapIndexedSetNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_nodeMap, mask, bitpos);
    var subNode = this.getNode__I__sci_SetNode(index$2);
    var subNodeNew$2 = subNode.updated__O__I__I__I__sci_SetNode(element, originalHash, elementHash, ((5 + shift) | 0));
    if ((subNode === subNodeNew$2)) {
      return this
    } else {
      return this.copyAndSetNode__I__sci_SetNode__sci_SetNode__sci_BitmapIndexedSetNode(bitpos, subNode, subNodeNew$2)
    }
  };
  return this.copyAndInsertValue__I__O__I__I__sci_BitmapIndexedSetNode(bitpos, element, originalHash, elementHash)
});
$c_sci_BitmapIndexedSetNode.prototype.removed__O__I__I__I__sci_BitmapIndexedSetNode = (function(element, originalHash, elementHash, shift) {
  var mask = $m_sci_Node$().maskFrom__I__I__I(elementHash, shift);
  var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
  if (((this.sci_BitmapIndexedSetNode__f_dataMap & bitpos) !== 0)) {
    var index = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_dataMap, mask, bitpos);
    var element0 = this.getPayload__I__O(index);
    if ($m_sr_BoxesRunTime$().equals__O__O__Z(element0, element)) {
      var i = this.sci_BitmapIndexedSetNode__f_dataMap;
      if (($m_jl_Integer$().bitCount__I__I(i) === 2)) {
        var i$1 = this.sci_BitmapIndexedSetNode__f_nodeMap;
        var $$x1 = ($m_jl_Integer$().bitCount__I__I(i$1) === 0)
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var newDataMap = ((shift === 0) ? (this.sci_BitmapIndexedSetNode__f_dataMap ^ bitpos) : $m_sci_Node$().bitposFrom__I__I($m_sci_Node$().maskFrom__I__I__I(elementHash, 0)));
        if ((index === 0)) {
          return new $c_sci_BitmapIndexedSetNode(newDataMap, 0, new $ac_O([this.getPayload__I__O(1)]), new $ac_I(new Int32Array([this.sci_BitmapIndexedSetNode__f_originalHashes.get(1)])), (((-1) + this.sci_BitmapIndexedSetNode__f_size) | 0), $m_sc_Hashing$().improve__I__I(this.sci_BitmapIndexedSetNode__f_originalHashes.get(1)))
        } else {
          return new $c_sci_BitmapIndexedSetNode(newDataMap, 0, new $ac_O([this.getPayload__I__O(0)]), new $ac_I(new Int32Array([this.sci_BitmapIndexedSetNode__f_originalHashes.get(0)])), (((-1) + this.sci_BitmapIndexedSetNode__f_size) | 0), $m_sc_Hashing$().improve__I__I(this.sci_BitmapIndexedSetNode__f_originalHashes.get(0)))
        }
      } else {
        return this.copyAndRemoveValue__I__I__sci_BitmapIndexedSetNode(bitpos, elementHash)
      }
    } else {
      return this
    }
  };
  if (((this.sci_BitmapIndexedSetNode__f_nodeMap & bitpos) !== 0)) {
    var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(this.sci_BitmapIndexedSetNode__f_nodeMap, mask, bitpos);
    var subNode = this.getNode__I__sci_SetNode(index$2);
    var subNodeNew = subNode.removed__O__I__I__I__sci_SetNode(element, originalHash, elementHash, ((5 + shift) | 0));
    if ((subNodeNew === subNode)) {
      return this
    };
    var subNodeNewSize = subNodeNew.size__I();
    if ((subNodeNewSize === 1)) {
      if ((this.sci_BitmapIndexedSetNode__f_size === subNode.size__I())) {
        return $as_sci_BitmapIndexedSetNode(subNodeNew)
      } else {
        return this.copyAndMigrateFromNodeToInline__I__I__sci_SetNode__sci_SetNode__sci_BitmapIndexedSetNode(bitpos, elementHash, subNode, subNodeNew)
      }
    } else if ((subNodeNewSize > 1)) {
      return this.copyAndSetNode__I__sci_SetNode__sci_SetNode__sci_BitmapIndexedSetNode(bitpos, subNode, subNodeNew)
    }
  };
  return this
});
$c_sci_BitmapIndexedSetNode.prototype.mergeTwoKeyValPairs__O__I__I__O__I__I__I__sci_SetNode = (function(key0, originalKeyHash0, keyHash0, key1, originalKeyHash1, keyHash1, shift) {
  if ((shift >= 32)) {
    var this$4 = $m_sci_Vector$();
    var array = [key0, key1];
    var elems = new $c_sjsr_WrappedVarArgs(array);
    return new $c_sci_HashCollisionSetNode(originalKeyHash0, keyHash0, this$4.from__sc_IterableOnce__sci_Vector(elems))
  } else {
    var mask0 = $m_sci_Node$().maskFrom__I__I__I(keyHash0, shift);
    var mask1 = $m_sci_Node$().maskFrom__I__I__I(keyHash1, shift);
    if ((mask0 !== mask1)) {
      var dataMap = ($m_sci_Node$().bitposFrom__I__I(mask0) | $m_sci_Node$().bitposFrom__I__I(mask1));
      var newCachedHashCode = ((keyHash0 + keyHash1) | 0);
      return ((mask0 < mask1) ? new $c_sci_BitmapIndexedSetNode(dataMap, 0, new $ac_O([key0, key1]), new $ac_I(new Int32Array([originalKeyHash0, originalKeyHash1])), 2, newCachedHashCode) : new $c_sci_BitmapIndexedSetNode(dataMap, 0, new $ac_O([key1, key0]), new $ac_I(new Int32Array([originalKeyHash1, originalKeyHash0])), 2, newCachedHashCode))
    } else {
      var nodeMap = $m_sci_Node$().bitposFrom__I__I(mask0);
      var node = this.mergeTwoKeyValPairs__O__I__I__O__I__I__I__sci_SetNode(key0, originalKeyHash0, keyHash0, key1, originalKeyHash1, keyHash1, ((5 + shift) | 0));
      return new $c_sci_BitmapIndexedSetNode(0, nodeMap, new $ac_O([node]), $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyIntArray, node.size__I(), node.cachedJavaKeySetHashCode__I())
    }
  }
});
$c_sci_BitmapIndexedSetNode.prototype.hasPayload__Z = (function() {
  return (this.sci_BitmapIndexedSetNode__f_dataMap !== 0)
});
$c_sci_BitmapIndexedSetNode.prototype.payloadArity__I = (function() {
  var i = this.sci_BitmapIndexedSetNode__f_dataMap;
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedSetNode.prototype.hasNodes__Z = (function() {
  return (this.sci_BitmapIndexedSetNode__f_nodeMap !== 0)
});
$c_sci_BitmapIndexedSetNode.prototype.nodeArity__I = (function() {
  var i = this.sci_BitmapIndexedSetNode__f_nodeMap;
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedSetNode.prototype.dataIndex__I__I = (function(bitpos) {
  var i = (this.sci_BitmapIndexedSetNode__f_dataMap & (((-1) + bitpos) | 0));
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedSetNode.prototype.nodeIndex__I__I = (function(bitpos) {
  var i = (this.sci_BitmapIndexedSetNode__f_nodeMap & (((-1) + bitpos) | 0));
  return $m_jl_Integer$().bitCount__I__I(i)
});
$c_sci_BitmapIndexedSetNode.prototype.copyAndSetNode__I__sci_SetNode__sci_SetNode__sci_BitmapIndexedSetNode = (function(bitpos, oldNode, newNode) {
  var idx = (((((-1) + this.sci_BitmapIndexedSetNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O(src.u.length);
  var length = src.u.length;
  src.copyTo(0, dst, 0, length);
  dst.set(idx, newNode);
  return new $c_sci_BitmapIndexedSetNode(this.sci_BitmapIndexedSetNode__f_dataMap, this.sci_BitmapIndexedSetNode__f_nodeMap, dst, this.sci_BitmapIndexedSetNode__f_originalHashes, ((((this.sci_BitmapIndexedSetNode__f_size - oldNode.size__I()) | 0) + newNode.size__I()) | 0), ((((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode__I()) | 0) + newNode.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedSetNode.prototype.copyAndInsertValue__I__O__I__I__sci_BitmapIndexedSetNode = (function(bitpos, key, originalHash, elementHash) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O(((1 + src.u.length) | 0));
  src.copyTo(0, dst, 0, dataIx);
  dst.set(dataIx, key);
  var destPos = ((1 + dataIx) | 0);
  var length = ((src.u.length - dataIx) | 0);
  src.copyTo(dataIx, dst, destPos, length);
  var dstHashes = this.insertElement__AI__I__I__AI(this.sci_BitmapIndexedSetNode__f_originalHashes, dataIx, originalHash);
  return new $c_sci_BitmapIndexedSetNode((this.sci_BitmapIndexedSetNode__f_dataMap | bitpos), this.sci_BitmapIndexedSetNode__f_nodeMap, dst, dstHashes, ((1 + this.sci_BitmapIndexedSetNode__f_size) | 0), ((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode + elementHash) | 0))
});
$c_sci_BitmapIndexedSetNode.prototype.copyAndRemoveValue__I__I__sci_BitmapIndexedSetNode = (function(bitpos, elementHash) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O((((-1) + src.u.length) | 0));
  src.copyTo(0, dst, 0, dataIx);
  var srcPos = ((1 + dataIx) | 0);
  var length = (((-1) + ((src.u.length - dataIx) | 0)) | 0);
  src.copyTo(srcPos, dst, dataIx, length);
  var dstHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedSetNode__f_originalHashes, dataIx);
  return new $c_sci_BitmapIndexedSetNode((this.sci_BitmapIndexedSetNode__f_dataMap ^ bitpos), this.sci_BitmapIndexedSetNode__f_nodeMap, dst, dstHashes, (((-1) + this.sci_BitmapIndexedSetNode__f_size) | 0), ((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode - elementHash) | 0))
});
$c_sci_BitmapIndexedSetNode.prototype.copyAndMigrateFromInlineToNode__I__I__sci_SetNode__sci_BitmapIndexedSetNode = (function(bitpos, elementHash, node) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idxNew = (((((-1) + this.sci_BitmapIndexedSetNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O(src.u.length);
  src.copyTo(0, dst, 0, dataIx);
  var srcPos = ((1 + dataIx) | 0);
  var length = ((idxNew - dataIx) | 0);
  src.copyTo(srcPos, dst, dataIx, length);
  dst.set(idxNew, node);
  var srcPos$1 = ((1 + idxNew) | 0);
  var destPos = ((1 + idxNew) | 0);
  var length$1 = (((-1) + ((src.u.length - idxNew) | 0)) | 0);
  src.copyTo(srcPos$1, dst, destPos, length$1);
  var dstHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedSetNode__f_originalHashes, dataIx);
  return new $c_sci_BitmapIndexedSetNode((this.sci_BitmapIndexedSetNode__f_dataMap ^ bitpos), (this.sci_BitmapIndexedSetNode__f_nodeMap | bitpos), dst, dstHashes, (((((-1) + this.sci_BitmapIndexedSetNode__f_size) | 0) + node.size__I()) | 0), ((((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode - elementHash) | 0) + node.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedSetNode.prototype.migrateFromInlineToNodeInPlace__I__I__sci_SetNode__sci_BitmapIndexedSetNode = (function(bitpos, keyHash, node) {
  var dataIx = this.dataIndex__I__I(bitpos);
  var idxNew = (((((-1) + this.sci_BitmapIndexedSetNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var srcPos = ((1 + dataIx) | 0);
  var dest = this.sci_BitmapIndexedSetNode__f_content;
  var length = ((idxNew - dataIx) | 0);
  src.copyTo(srcPos, dest, dataIx, length);
  this.sci_BitmapIndexedSetNode__f_content.set(idxNew, node);
  this.sci_BitmapIndexedSetNode__f_dataMap = (this.sci_BitmapIndexedSetNode__f_dataMap ^ bitpos);
  this.sci_BitmapIndexedSetNode__f_nodeMap = (this.sci_BitmapIndexedSetNode__f_nodeMap | bitpos);
  this.sci_BitmapIndexedSetNode__f_originalHashes = this.removeElement__AI__I__AI(this.sci_BitmapIndexedSetNode__f_originalHashes, dataIx);
  this.sci_BitmapIndexedSetNode__f_size = (((((-1) + this.sci_BitmapIndexedSetNode__f_size) | 0) + node.size__I()) | 0);
  this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode = ((((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode - keyHash) | 0) + node.cachedJavaKeySetHashCode__I()) | 0);
  return this
});
$c_sci_BitmapIndexedSetNode.prototype.copyAndMigrateFromNodeToInline__I__I__sci_SetNode__sci_SetNode__sci_BitmapIndexedSetNode = (function(bitpos, elementHash, oldNode, node) {
  var idxOld = (((((-1) + this.sci_BitmapIndexedSetNode__f_content.u.length) | 0) - this.nodeIndex__I__I(bitpos)) | 0);
  var dataIxNew = this.dataIndex__I__I(bitpos);
  var src = this.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O(src.u.length);
  src.copyTo(0, dst, 0, dataIxNew);
  dst.set(dataIxNew, node.getPayload__I__O(0));
  var destPos = ((1 + dataIxNew) | 0);
  var length = ((idxOld - dataIxNew) | 0);
  src.copyTo(dataIxNew, dst, destPos, length);
  var srcPos = ((1 + idxOld) | 0);
  var destPos$1 = ((1 + idxOld) | 0);
  var length$1 = (((-1) + ((src.u.length - idxOld) | 0)) | 0);
  src.copyTo(srcPos, dst, destPos$1, length$1);
  var hash = node.getHash__I__I(0);
  var dstHashes = this.insertElement__AI__I__I__AI(this.sci_BitmapIndexedSetNode__f_originalHashes, dataIxNew, hash);
  return new $c_sci_BitmapIndexedSetNode((this.sci_BitmapIndexedSetNode__f_dataMap | bitpos), (this.sci_BitmapIndexedSetNode__f_nodeMap ^ bitpos), dst, dstHashes, ((1 + ((this.sci_BitmapIndexedSetNode__f_size - oldNode.size__I()) | 0)) | 0), ((((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode__I()) | 0) + node.cachedJavaKeySetHashCode__I()) | 0))
});
$c_sci_BitmapIndexedSetNode.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_BitmapIndexedSetNode)) {
    var x2 = $as_sci_BitmapIndexedSetNode(that);
    if ((this === x2)) {
      return true
    } else {
      if (((((this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode === x2.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode) && (this.sci_BitmapIndexedSetNode__f_nodeMap === x2.sci_BitmapIndexedSetNode__f_nodeMap)) && (this.sci_BitmapIndexedSetNode__f_dataMap === x2.sci_BitmapIndexedSetNode__f_dataMap)) && (this.sci_BitmapIndexedSetNode__f_size === x2.sci_BitmapIndexedSetNode__f_size))) {
        var a = this.sci_BitmapIndexedSetNode__f_originalHashes;
        var b = x2.sci_BitmapIndexedSetNode__f_originalHashes;
        var $$x1 = $m_ju_Arrays$().equals__AI__AI__Z(a, b)
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var a1 = this.sci_BitmapIndexedSetNode__f_content;
        var a2 = x2.sci_BitmapIndexedSetNode__f_content;
        var length = this.sci_BitmapIndexedSetNode__f_content.u.length;
        if ((a1 === a2)) {
          return true
        } else {
          var isEqual = true;
          var i = 0;
          while ((isEqual && (i < length))) {
            isEqual = $m_sr_BoxesRunTime$().equals__O__O__Z(a1.get(i), a2.get(i));
            i = ((1 + i) | 0)
          };
          return isEqual
        }
      } else {
        return false
      }
    }
  } else {
    return false
  }
});
$c_sci_BitmapIndexedSetNode.prototype.hashCode__I = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "Trie nodes do not support hashing.")
});
$c_sci_BitmapIndexedSetNode.prototype.copy__sci_BitmapIndexedSetNode = (function() {
  var this$1 = this.sci_BitmapIndexedSetNode__f_content;
  var contentClone = this$1.clone__O();
  var contentLength = contentClone.u.length;
  var i = this.sci_BitmapIndexedSetNode__f_dataMap;
  var i$1 = $m_jl_Integer$().bitCount__I__I(i);
  while ((i$1 < contentLength)) {
    contentClone.set(i$1, $as_sci_SetNode(contentClone.get(i$1)).copy__sci_SetNode());
    i$1 = ((1 + i$1) | 0)
  };
  var $$x2 = this.sci_BitmapIndexedSetNode__f_dataMap;
  var $$x1 = this.sci_BitmapIndexedSetNode__f_nodeMap;
  var this$2 = this.sci_BitmapIndexedSetNode__f_originalHashes;
  return new $c_sci_BitmapIndexedSetNode($$x2, $$x1, contentClone, this$2.clone__O(), this.sci_BitmapIndexedSetNode__f_size, this.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode)
});
$c_sci_BitmapIndexedSetNode.prototype.copy__sci_SetNode = (function() {
  return this.copy__sci_BitmapIndexedSetNode()
});
$c_sci_BitmapIndexedSetNode.prototype.removed__O__I__I__I__sci_SetNode = (function(element, originalHash, hash, shift) {
  return this.removed__O__I__I__I__sci_BitmapIndexedSetNode(element, originalHash, hash, shift)
});
$c_sci_BitmapIndexedSetNode.prototype.updated__O__I__I__I__sci_SetNode = (function(element, originalHash, hash, shift) {
  return this.updated__O__I__I__I__sci_BitmapIndexedSetNode(element, originalHash, hash, shift)
});
$c_sci_BitmapIndexedSetNode.prototype.getNode__I__sci_Node = (function(index) {
  return this.getNode__I__sci_SetNode(index)
});
function $as_sci_BitmapIndexedSetNode(obj) {
  return (((obj instanceof $c_sci_BitmapIndexedSetNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.BitmapIndexedSetNode"))
}
function $isArrayOf_sci_BitmapIndexedSetNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_BitmapIndexedSetNode)))
}
function $asArrayOf_sci_BitmapIndexedSetNode(obj, depth) {
  return (($isArrayOf_sci_BitmapIndexedSetNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.BitmapIndexedSetNode;", depth))
}
var $d_sci_BitmapIndexedSetNode = new $TypeData().initClass({
  sci_BitmapIndexedSetNode: 0
}, false, "scala.collection.immutable.BitmapIndexedSetNode", {
  sci_BitmapIndexedSetNode: 1,
  sci_SetNode: 1,
  sci_Node: 1,
  O: 1
});
$c_sci_BitmapIndexedSetNode.prototype.$classData = $d_sci_BitmapIndexedSetNode;
/** @constructor */
function $c_sci_HashCollisionMapNode(originalHash, hash, content) {
  this.sci_HashCollisionMapNode__f_originalHash = 0;
  this.sci_HashCollisionMapNode__f_hash = 0;
  this.sci_HashCollisionMapNode__f_content = null;
  this.sci_HashCollisionMapNode__f_originalHash = originalHash;
  this.sci_HashCollisionMapNode__f_hash = hash;
  this.sci_HashCollisionMapNode__f_content = content;
  $m_s_Predef$().require__Z__V((this.sci_HashCollisionMapNode__f_content.length__I() >= 2))
}
$c_sci_HashCollisionMapNode.prototype = new $h_sci_MapNode();
$c_sci_HashCollisionMapNode.prototype.constructor = $c_sci_HashCollisionMapNode;
/** @constructor */
function $h_sci_HashCollisionMapNode() {
  /*<skip>*/
}
$h_sci_HashCollisionMapNode.prototype = $c_sci_HashCollisionMapNode.prototype;
$c_sci_HashCollisionMapNode.prototype.indexOf__O__I = (function(key) {
  var iter = this.sci_HashCollisionMapNode__f_content.iterator__sc_Iterator();
  var i = 0;
  while (iter.hasNext__Z()) {
    if ($m_sr_BoxesRunTime$().equals__O__O__Z($as_T2(iter.next__O()).T2__f__1, key)) {
      return i
    };
    i = ((1 + i) | 0)
  };
  return (-1)
});
$c_sci_HashCollisionMapNode.prototype.size__I = (function() {
  return this.sci_HashCollisionMapNode__f_content.length__I()
});
$c_sci_HashCollisionMapNode.prototype.apply__O__I__I__I__O = (function(key, originalHash, hash, shift) {
  var this$1 = this.get__O__I__I__I__s_Option(key, originalHash, hash, shift);
  if (this$1.isEmpty__Z()) {
    throw $ct_ju_NoSuchElementException__(new $c_ju_NoSuchElementException())
  } else {
    return this$1.get__O()
  }
});
$c_sci_HashCollisionMapNode.prototype.get__O__I__I__I__s_Option = (function(key, originalHash, hash, shift) {
  if ((this.sci_HashCollisionMapNode__f_hash === hash)) {
    var index = this.indexOf__O__I(key);
    return ((index >= 0) ? new $c_s_Some($as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(index)).T2__f__2) : $m_s_None$())
  } else {
    return $m_s_None$()
  }
});
$c_sci_HashCollisionMapNode.prototype.getOrElse__O__I__I__I__F0__O = (function(key, originalHash, hash, shift, f) {
  if ((this.sci_HashCollisionMapNode__f_hash === hash)) {
    var x1 = this.indexOf__O__I(key);
    return ((x1 === (-1)) ? f.apply__O() : $as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(x1)).T2__f__2)
  } else {
    return f.apply__O()
  }
});
$c_sci_HashCollisionMapNode.prototype.containsKey__O__I__I__I__Z = (function(key, originalHash, hash, shift) {
  return ((this.sci_HashCollisionMapNode__f_hash === hash) && (this.indexOf__O__I(key) >= 0))
});
$c_sci_HashCollisionMapNode.prototype.updated__O__O__I__I__I__Z__sci_MapNode = (function(key, value, originalHash, hash, shift, replaceValue) {
  var index = this.indexOf__O__I(key);
  return ((index >= 0) ? (replaceValue ? (Object.is($as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(index)).T2__f__2, value) ? this : new $c_sci_HashCollisionMapNode(originalHash, hash, this.sci_HashCollisionMapNode__f_content.updated__I__O__sci_Vector(index, new $c_T2(key, value)))) : this) : new $c_sci_HashCollisionMapNode(originalHash, hash, this.sci_HashCollisionMapNode__f_content.appended__O__sci_Vector(new $c_T2(key, value))))
});
$c_sci_HashCollisionMapNode.prototype.removed__O__I__I__I__sci_MapNode = (function(key, originalHash, hash, shift) {
  if ((!this.containsKey__O__I__I__I__Z(key, originalHash, hash, shift))) {
    return this
  } else {
    var this$2 = this.sci_HashCollisionMapNode__f_content;
    var pred = new $c_sjsr_AnonFunction1(((this$1, key$1) => ((keyValuePair$2) => {
      var keyValuePair = $as_T2(keyValuePair$2);
      return $m_sr_BoxesRunTime$().equals__O__O__Z(keyValuePair.T2__f__1, key$1)
    }))(this, key));
    var updatedContent = this$2.filterImpl__F1__Z__sci_Vector(pred, true);
    var x1 = updatedContent.length__I();
    if ((x1 === 1)) {
      var x1$2 = $as_T2(updatedContent.apply__I__O(0));
      if ((x1$2 === null)) {
        throw new $c_s_MatchError(x1$2)
      };
      var k = x1$2.T2__f__1;
      var v = x1$2.T2__f__2;
      return new $c_sci_BitmapIndexedMapNode($m_sci_Node$().bitposFrom__I__I($m_sci_Node$().maskFrom__I__I__I(hash, 0)), 0, new $ac_O([k, v]), new $ac_I(new Int32Array([originalHash])), 1, hash)
    } else {
      return new $c_sci_HashCollisionMapNode(originalHash, hash, updatedContent)
    }
  }
});
$c_sci_HashCollisionMapNode.prototype.hasNodes__Z = (function() {
  return false
});
$c_sci_HashCollisionMapNode.prototype.nodeArity__I = (function() {
  return 0
});
$c_sci_HashCollisionMapNode.prototype.getNode__I__sci_MapNode = (function(index) {
  throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), "No sub-nodes present in hash-collision leaf node.")
});
$c_sci_HashCollisionMapNode.prototype.hasPayload__Z = (function() {
  return true
});
$c_sci_HashCollisionMapNode.prototype.payloadArity__I = (function() {
  return this.sci_HashCollisionMapNode__f_content.length__I()
});
$c_sci_HashCollisionMapNode.prototype.getKey__I__O = (function(index) {
  return $as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(index)).T2__f__1
});
$c_sci_HashCollisionMapNode.prototype.getValue__I__O = (function(index) {
  return $as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(index)).T2__f__2
});
$c_sci_HashCollisionMapNode.prototype.getPayload__I__T2 = (function(index) {
  return $as_T2(this.sci_HashCollisionMapNode__f_content.apply__I__O(index))
});
$c_sci_HashCollisionMapNode.prototype.getHash__I__I = (function(index) {
  return this.sci_HashCollisionMapNode__f_originalHash
});
$c_sci_HashCollisionMapNode.prototype.foreachEntry__F2__V = (function(f) {
  this.sci_HashCollisionMapNode__f_content.foreach__F1__V(new $c_sjsr_AnonFunction1(((this$1, f$1) => ((x0$1$2) => {
    var x0$1 = $as_T2(x0$1$2);
    if ((x0$1 !== null)) {
      var k = x0$1.T2__f__1;
      var v = x0$1.T2__f__2;
      return f$1.apply__O__O__O(k, v)
    } else {
      throw new $c_s_MatchError(x0$1)
    }
  }))(this, f)))
});
$c_sci_HashCollisionMapNode.prototype.foreachWithHash__F3__V = (function(f) {
  var iter = this.sci_HashCollisionMapNode__f_content.iterator__sc_Iterator();
  while (iter.hasNext__Z()) {
    var next = $as_T2(iter.next__O());
    f.apply__O__O__O__O(next.T2__f__1, next.T2__f__2, this.sci_HashCollisionMapNode__f_originalHash)
  }
});
$c_sci_HashCollisionMapNode.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_HashCollisionMapNode)) {
    var x2 = $as_sci_HashCollisionMapNode(that);
    if ((this === x2)) {
      return true
    } else if (((this.sci_HashCollisionMapNode__f_hash === x2.sci_HashCollisionMapNode__f_hash) && (this.sci_HashCollisionMapNode__f_content.length__I() === x2.sci_HashCollisionMapNode__f_content.length__I()))) {
      var iter = this.sci_HashCollisionMapNode__f_content.iterator__sc_Iterator();
      while (iter.hasNext__Z()) {
        var x1$2 = $as_T2(iter.next__O());
        if ((x1$2 === null)) {
          throw new $c_s_MatchError(x1$2)
        };
        var key = x1$2.T2__f__1;
        var value = x1$2.T2__f__2;
        var index = x2.indexOf__O__I(key);
        if (((index < 0) || (!$m_sr_BoxesRunTime$().equals__O__O__Z(value, $as_T2(x2.sci_HashCollisionMapNode__f_content.apply__I__O(index)).T2__f__2)))) {
          return false
        }
      };
      return true
    } else {
      return false
    }
  } else {
    return false
  }
});
$c_sci_HashCollisionMapNode.prototype.hashCode__I = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "Trie nodes do not support hashing.")
});
$c_sci_HashCollisionMapNode.prototype.cachedJavaKeySetHashCode__I = (function() {
  return $imul(this.sci_HashCollisionMapNode__f_content.length__I(), this.sci_HashCollisionMapNode__f_hash)
});
$c_sci_HashCollisionMapNode.prototype.copy__sci_MapNode = (function() {
  return new $c_sci_HashCollisionMapNode(this.sci_HashCollisionMapNode__f_originalHash, this.sci_HashCollisionMapNode__f_hash, this.sci_HashCollisionMapNode__f_content)
});
$c_sci_HashCollisionMapNode.prototype.getNode__I__sci_Node = (function(index) {
  return this.getNode__I__sci_MapNode(index)
});
function $as_sci_HashCollisionMapNode(obj) {
  return (((obj instanceof $c_sci_HashCollisionMapNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.HashCollisionMapNode"))
}
function $isArrayOf_sci_HashCollisionMapNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_HashCollisionMapNode)))
}
function $asArrayOf_sci_HashCollisionMapNode(obj, depth) {
  return (($isArrayOf_sci_HashCollisionMapNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.HashCollisionMapNode;", depth))
}
var $d_sci_HashCollisionMapNode = new $TypeData().initClass({
  sci_HashCollisionMapNode: 0
}, false, "scala.collection.immutable.HashCollisionMapNode", {
  sci_HashCollisionMapNode: 1,
  sci_MapNode: 1,
  sci_Node: 1,
  O: 1
});
$c_sci_HashCollisionMapNode.prototype.$classData = $d_sci_HashCollisionMapNode;
/** @constructor */
function $c_sci_HashCollisionSetNode(originalHash, hash, content) {
  this.sci_HashCollisionSetNode__f_originalHash = 0;
  this.sci_HashCollisionSetNode__f_hash = 0;
  this.sci_HashCollisionSetNode__f_content = null;
  this.sci_HashCollisionSetNode__f_originalHash = originalHash;
  this.sci_HashCollisionSetNode__f_hash = hash;
  this.sci_HashCollisionSetNode__f_content = content;
  $m_s_Predef$().require__Z__V((this.sci_HashCollisionSetNode__f_content.length__I() >= 2))
}
$c_sci_HashCollisionSetNode.prototype = new $h_sci_SetNode();
$c_sci_HashCollisionSetNode.prototype.constructor = $c_sci_HashCollisionSetNode;
/** @constructor */
function $h_sci_HashCollisionSetNode() {
  /*<skip>*/
}
$h_sci_HashCollisionSetNode.prototype = $c_sci_HashCollisionSetNode.prototype;
$c_sci_HashCollisionSetNode.prototype.contains__O__I__I__I__Z = (function(element, originalHash, hash, shift) {
  if ((this.sci_HashCollisionSetNode__f_hash === hash)) {
    var this$1 = this.sci_HashCollisionSetNode__f_content;
    return $f_sc_SeqOps__contains__O__Z(this$1, element)
  } else {
    return false
  }
});
$c_sci_HashCollisionSetNode.prototype.updated__O__I__I__I__sci_SetNode = (function(element, originalHash, hash, shift) {
  return (this.contains__O__I__I__I__Z(element, originalHash, hash, shift) ? this : new $c_sci_HashCollisionSetNode(originalHash, hash, this.sci_HashCollisionSetNode__f_content.appended__O__sci_Vector(element)))
});
$c_sci_HashCollisionSetNode.prototype.removed__O__I__I__I__sci_SetNode = (function(element, originalHash, hash, shift) {
  if ((!this.contains__O__I__I__I__Z(element, originalHash, hash, shift))) {
    return this
  } else {
    var this$2 = this.sci_HashCollisionSetNode__f_content;
    var pred = new $c_sjsr_AnonFunction1(((this$1, element$1) => ((element0$2) => $m_sr_BoxesRunTime$().equals__O__O__Z(element0$2, element$1)))(this, element));
    var updatedContent = this$2.filterImpl__F1__Z__sci_Vector(pred, true);
    var x1 = updatedContent.length__I();
    return ((x1 === 1) ? new $c_sci_BitmapIndexedSetNode($m_sci_Node$().bitposFrom__I__I($m_sci_Node$().maskFrom__I__I__I(hash, 0)), 0, new $ac_O([updatedContent.apply__I__O(0)]), new $ac_I(new Int32Array([originalHash])), 1, hash) : new $c_sci_HashCollisionSetNode(originalHash, hash, updatedContent))
  }
});
$c_sci_HashCollisionSetNode.prototype.hasNodes__Z = (function() {
  return false
});
$c_sci_HashCollisionSetNode.prototype.nodeArity__I = (function() {
  return 0
});
$c_sci_HashCollisionSetNode.prototype.getNode__I__sci_SetNode = (function(index) {
  throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), "No sub-nodes present in hash-collision leaf node.")
});
$c_sci_HashCollisionSetNode.prototype.hasPayload__Z = (function() {
  return true
});
$c_sci_HashCollisionSetNode.prototype.payloadArity__I = (function() {
  return this.sci_HashCollisionSetNode__f_content.length__I()
});
$c_sci_HashCollisionSetNode.prototype.getPayload__I__O = (function(index) {
  return this.sci_HashCollisionSetNode__f_content.apply__I__O(index)
});
$c_sci_HashCollisionSetNode.prototype.getHash__I__I = (function(index) {
  return this.sci_HashCollisionSetNode__f_originalHash
});
$c_sci_HashCollisionSetNode.prototype.size__I = (function() {
  return this.sci_HashCollisionSetNode__f_content.length__I()
});
$c_sci_HashCollisionSetNode.prototype.cachedJavaKeySetHashCode__I = (function() {
  return $imul(this.sci_HashCollisionSetNode__f_content.length__I(), this.sci_HashCollisionSetNode__f_hash)
});
$c_sci_HashCollisionSetNode.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_HashCollisionSetNode)) {
    var x2 = $as_sci_HashCollisionSetNode(that);
    if ((this === x2)) {
      return true
    } else {
      if ((this.sci_HashCollisionSetNode__f_hash === x2.sci_HashCollisionSetNode__f_hash)) {
        var this$1 = this.sci_HashCollisionSetNode__f_content;
        var $$x2 = this$1.length__I();
        var this$2 = x2.sci_HashCollisionSetNode__f_content;
        var $$x1 = ($$x2 === this$2.length__I())
      } else {
        var $$x1 = false
      };
      if ($$x1) {
        var this$3 = this.sci_HashCollisionSetNode__f_content;
        var eta$0$1 = x2.sci_HashCollisionSetNode__f_content;
        var res = true;
        var it = this$3.iterator__sc_Iterator();
        while ((res && it.hasNext__Z())) {
          var arg1 = it.next__O();
          res = $f_sc_SeqOps__contains__O__Z(eta$0$1, arg1)
        };
        return res
      } else {
        return false
      }
    }
  } else {
    return false
  }
});
$c_sci_HashCollisionSetNode.prototype.hashCode__I = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "Trie nodes do not support hashing.")
});
$c_sci_HashCollisionSetNode.prototype.copy__sci_SetNode = (function() {
  return new $c_sci_HashCollisionSetNode(this.sci_HashCollisionSetNode__f_originalHash, this.sci_HashCollisionSetNode__f_hash, this.sci_HashCollisionSetNode__f_content)
});
$c_sci_HashCollisionSetNode.prototype.getNode__I__sci_Node = (function(index) {
  return this.getNode__I__sci_SetNode(index)
});
function $as_sci_HashCollisionSetNode(obj) {
  return (((obj instanceof $c_sci_HashCollisionSetNode) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.HashCollisionSetNode"))
}
function $isArrayOf_sci_HashCollisionSetNode(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_HashCollisionSetNode)))
}
function $asArrayOf_sci_HashCollisionSetNode(obj, depth) {
  return (($isArrayOf_sci_HashCollisionSetNode(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.HashCollisionSetNode;", depth))
}
var $d_sci_HashCollisionSetNode = new $TypeData().initClass({
  sci_HashCollisionSetNode: 0
}, false, "scala.collection.immutable.HashCollisionSetNode", {
  sci_HashCollisionSetNode: 1,
  sci_SetNode: 1,
  sci_Node: 1,
  O: 1
});
$c_sci_HashCollisionSetNode.prototype.$classData = $d_sci_HashCollisionSetNode;
/** @constructor */
function $c_sci_HashMap$() {
  this.sci_HashMap$__f_EmptyMap = null;
  $n_sci_HashMap$ = this;
  var this$1 = $m_sci_MapNode$();
  this.sci_HashMap$__f_EmptyMap = new $c_sci_HashMap(this$1.sci_MapNode$__f_EmptyMapNode)
}
$c_sci_HashMap$.prototype = new $h_O();
$c_sci_HashMap$.prototype.constructor = $c_sci_HashMap$;
/** @constructor */
function $h_sci_HashMap$() {
  /*<skip>*/
}
$h_sci_HashMap$.prototype = $c_sci_HashMap$.prototype;
$c_sci_HashMap$.prototype.from__sc_IterableOnce__sci_HashMap = (function(source) {
  if ((source instanceof $c_sci_HashMap)) {
    var x2 = $as_sci_HashMap(source);
    return x2
  } else {
    var this$1 = new $c_sci_HashMapBuilder();
    var this$2 = this$1.addAll__sc_IterableOnce__sci_HashMapBuilder(source);
    return this$2.result__sci_HashMap()
  }
});
$c_sci_HashMap$.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.from__sc_IterableOnce__sci_HashMap(it)
});
var $d_sci_HashMap$ = new $TypeData().initClass({
  sci_HashMap$: 0
}, false, "scala.collection.immutable.HashMap$", {
  sci_HashMap$: 1,
  O: 1,
  sc_MapFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_HashMap$.prototype.$classData = $d_sci_HashMap$;
var $n_sci_HashMap$;
function $m_sci_HashMap$() {
  if ((!$n_sci_HashMap$)) {
    $n_sci_HashMap$ = new $c_sci_HashMap$()
  };
  return $n_sci_HashMap$
}
/** @constructor */
function $c_sci_HashSet$() {
  this.sci_HashSet$__f_EmptySet = null;
  $n_sci_HashSet$ = this;
  var this$1 = $m_sci_SetNode$();
  this.sci_HashSet$__f_EmptySet = new $c_sci_HashSet(this$1.sci_SetNode$__f_EmptySetNode)
}
$c_sci_HashSet$.prototype = new $h_O();
$c_sci_HashSet$.prototype.constructor = $c_sci_HashSet$;
/** @constructor */
function $h_sci_HashSet$() {
  /*<skip>*/
}
$h_sci_HashSet$.prototype = $c_sci_HashSet$.prototype;
$c_sci_HashSet$.prototype.from__sc_IterableOnce__sci_HashSet = (function(source) {
  if ((source instanceof $c_sci_HashSet)) {
    var x2 = $as_sci_HashSet(source);
    return x2
  } else if ((source.knownSize__I() === 0)) {
    return this.sci_HashSet$__f_EmptySet
  } else {
    var this$1 = new $c_sci_HashSetBuilder();
    var this$2 = this$1.addAll__sc_IterableOnce__sci_HashSetBuilder(source);
    return this$2.result__sci_HashSet()
  }
});
$c_sci_HashSet$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_HashSet(source)
});
var $d_sci_HashSet$ = new $TypeData().initClass({
  sci_HashSet$: 0
}, false, "scala.collection.immutable.HashSet$", {
  sci_HashSet$: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_HashSet$.prototype.$classData = $d_sci_HashSet$;
var $n_sci_HashSet$;
function $m_sci_HashSet$() {
  if ((!$n_sci_HashSet$)) {
    $n_sci_HashSet$ = new $c_sci_HashSet$()
  };
  return $n_sci_HashSet$
}
/** @constructor */
function $c_sci_LazyList$State$Cons(head, tail) {
  this.sci_LazyList$State$Cons__f_head = null;
  this.sci_LazyList$State$Cons__f_tail = null;
  this.sci_LazyList$State$Cons__f_head = head;
  this.sci_LazyList$State$Cons__f_tail = tail
}
$c_sci_LazyList$State$Cons.prototype = new $h_O();
$c_sci_LazyList$State$Cons.prototype.constructor = $c_sci_LazyList$State$Cons;
/** @constructor */
function $h_sci_LazyList$State$Cons() {
  /*<skip>*/
}
$h_sci_LazyList$State$Cons.prototype = $c_sci_LazyList$State$Cons.prototype;
$c_sci_LazyList$State$Cons.prototype.head__O = (function() {
  return this.sci_LazyList$State$Cons__f_head
});
$c_sci_LazyList$State$Cons.prototype.tail__sci_LazyList = (function() {
  return this.sci_LazyList$State$Cons__f_tail
});
var $d_sci_LazyList$State$Cons = new $TypeData().initClass({
  sci_LazyList$State$Cons: 0
}, false, "scala.collection.immutable.LazyList$State$Cons", {
  sci_LazyList$State$Cons: 1,
  O: 1,
  sci_LazyList$State: 1,
  Ljava_io_Serializable: 1
});
$c_sci_LazyList$State$Cons.prototype.$classData = $d_sci_LazyList$State$Cons;
/** @constructor */
function $c_sci_LazyList$State$Empty$() {
  /*<skip>*/
}
$c_sci_LazyList$State$Empty$.prototype = new $h_O();
$c_sci_LazyList$State$Empty$.prototype.constructor = $c_sci_LazyList$State$Empty$;
/** @constructor */
function $h_sci_LazyList$State$Empty$() {
  /*<skip>*/
}
$h_sci_LazyList$State$Empty$.prototype = $c_sci_LazyList$State$Empty$.prototype;
$c_sci_LazyList$State$Empty$.prototype.head__E = (function() {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "head of empty lazy list")
});
$c_sci_LazyList$State$Empty$.prototype.tail__sci_LazyList = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "tail of empty lazy list")
});
$c_sci_LazyList$State$Empty$.prototype.head__O = (function() {
  this.head__E()
});
var $d_sci_LazyList$State$Empty$ = new $TypeData().initClass({
  sci_LazyList$State$Empty$: 0
}, false, "scala.collection.immutable.LazyList$State$Empty$", {
  sci_LazyList$State$Empty$: 1,
  O: 1,
  sci_LazyList$State: 1,
  Ljava_io_Serializable: 1
});
$c_sci_LazyList$State$Empty$.prototype.$classData = $d_sci_LazyList$State$Empty$;
var $n_sci_LazyList$State$Empty$;
function $m_sci_LazyList$State$Empty$() {
  if ((!$n_sci_LazyList$State$Empty$)) {
    $n_sci_LazyList$State$Empty$ = new $c_sci_LazyList$State$Empty$()
  };
  return $n_sci_LazyList$State$Empty$
}
/** @constructor */
function $c_sci_Map$() {
  /*<skip>*/
}
$c_sci_Map$.prototype = new $h_O();
$c_sci_Map$.prototype.constructor = $c_sci_Map$;
/** @constructor */
function $h_sci_Map$() {
  /*<skip>*/
}
$h_sci_Map$.prototype = $c_sci_Map$.prototype;
$c_sci_Map$.prototype.from__sc_IterableOnce__sci_Map = (function(it) {
  if ($is_sci_Iterable(it)) {
    var x2 = $as_sci_Iterable(it);
    if (x2.isEmpty__Z()) {
      return $m_sci_Map$EmptyMap$()
    }
  };
  if ($is_sci_Map(it)) {
    var x3 = $as_sci_Map(it);
    return x3
  };
  var this$1 = new $c_sci_MapBuilderImpl();
  var this$2 = this$1.addAll__sc_IterableOnce__sci_MapBuilderImpl(it);
  return this$2.result__sci_Map()
});
$c_sci_Map$.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.from__sc_IterableOnce__sci_Map(it)
});
var $d_sci_Map$ = new $TypeData().initClass({
  sci_Map$: 0
}, false, "scala.collection.immutable.Map$", {
  sci_Map$: 1,
  O: 1,
  sc_MapFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$.prototype.$classData = $d_sci_Map$;
var $n_sci_Map$;
function $m_sci_Map$() {
  if ((!$n_sci_Map$)) {
    $n_sci_Map$ = new $c_sci_Map$()
  };
  return $n_sci_Map$
}
/** @constructor */
function $c_sci_Set$() {
  /*<skip>*/
}
$c_sci_Set$.prototype = new $h_O();
$c_sci_Set$.prototype.constructor = $c_sci_Set$;
/** @constructor */
function $h_sci_Set$() {
  /*<skip>*/
}
$h_sci_Set$.prototype = $c_sci_Set$.prototype;
$c_sci_Set$.prototype.from__sc_IterableOnce__sci_Set = (function(it) {
  if ($is_sci_SortedSet(it)) {
    var this$1 = new $c_sci_SetBuilderImpl();
    var this$2 = this$1.addAll__sc_IterableOnce__sci_SetBuilderImpl(it);
    return this$2.result__sci_Set()
  } else if ((it.knownSize__I() === 0)) {
    return $m_sci_Set$EmptySet$()
  } else if ($is_sci_Set(it)) {
    var x3 = $as_sci_Set(it);
    return x3
  } else {
    var this$3 = new $c_sci_SetBuilderImpl();
    var this$4 = this$3.addAll__sc_IterableOnce__sci_SetBuilderImpl(it);
    return this$4.result__sci_Set()
  }
});
$c_sci_Set$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_Set(source)
});
var $d_sci_Set$ = new $TypeData().initClass({
  sci_Set$: 0
}, false, "scala.collection.immutable.Set$", {
  sci_Set$: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$.prototype.$classData = $d_sci_Set$;
var $n_sci_Set$;
function $m_sci_Set$() {
  if ((!$n_sci_Set$)) {
    $n_sci_Set$ = new $c_sci_Set$()
  };
  return $n_sci_Set$
}
function $is_scm_Builder(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.scm_Builder)))
}
function $as_scm_Builder(obj) {
  return (($is_scm_Builder(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.Builder"))
}
function $isArrayOf_scm_Builder(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_Builder)))
}
function $asArrayOf_scm_Builder(obj, depth) {
  return (($isArrayOf_scm_Builder(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.Builder;", depth))
}
/** @constructor */
function $c_scm_HashMap$() {
  /*<skip>*/
}
$c_scm_HashMap$.prototype = new $h_O();
$c_scm_HashMap$.prototype.constructor = $c_scm_HashMap$;
/** @constructor */
function $h_scm_HashMap$() {
  /*<skip>*/
}
$h_scm_HashMap$.prototype = $c_scm_HashMap$.prototype;
$c_scm_HashMap$.prototype.from__sc_IterableOnce__scm_HashMap = (function(it) {
  var k = it.knownSize__I();
  var cap = ((k > 0) ? $doubleToInt((((1 + k) | 0) / 0.75)) : 16);
  return new $c_scm_HashMap(cap, 0.75).addAll__sc_IterableOnce__scm_HashMap(it)
});
$c_scm_HashMap$.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.from__sc_IterableOnce__scm_HashMap(it)
});
var $d_scm_HashMap$ = new $TypeData().initClass({
  scm_HashMap$: 0
}, false, "scala.collection.mutable.HashMap$", {
  scm_HashMap$: 1,
  O: 1,
  sc_MapFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_HashMap$.prototype.$classData = $d_scm_HashMap$;
var $n_scm_HashMap$;
function $m_scm_HashMap$() {
  if ((!$n_scm_HashMap$)) {
    $n_scm_HashMap$ = new $c_scm_HashMap$()
  };
  return $n_scm_HashMap$
}
/** @constructor */
function $c_s_math_Equiv$() {
  /*<skip>*/
}
$c_s_math_Equiv$.prototype = new $h_O();
$c_s_math_Equiv$.prototype.constructor = $c_s_math_Equiv$;
/** @constructor */
function $h_s_math_Equiv$() {
  /*<skip>*/
}
$h_s_math_Equiv$.prototype = $c_s_math_Equiv$.prototype;
var $d_s_math_Equiv$ = new $TypeData().initClass({
  s_math_Equiv$: 0
}, false, "scala.math.Equiv$", {
  s_math_Equiv$: 1,
  O: 1,
  s_math_LowPriorityEquiv: 1,
  Ljava_io_Serializable: 1
});
$c_s_math_Equiv$.prototype.$classData = $d_s_math_Equiv$;
var $n_s_math_Equiv$;
function $m_s_math_Equiv$() {
  if ((!$n_s_math_Equiv$)) {
    $n_s_math_Equiv$ = new $c_s_math_Equiv$()
  };
  return $n_s_math_Equiv$
}
/** @constructor */
function $c_s_math_Ordering$() {
  /*<skip>*/
}
$c_s_math_Ordering$.prototype = new $h_O();
$c_s_math_Ordering$.prototype.constructor = $c_s_math_Ordering$;
/** @constructor */
function $h_s_math_Ordering$() {
  /*<skip>*/
}
$h_s_math_Ordering$.prototype = $c_s_math_Ordering$.prototype;
var $d_s_math_Ordering$ = new $TypeData().initClass({
  s_math_Ordering$: 0
}, false, "scala.math.Ordering$", {
  s_math_Ordering$: 1,
  O: 1,
  s_math_LowPriorityOrderingImplicits: 1,
  Ljava_io_Serializable: 1
});
$c_s_math_Ordering$.prototype.$classData = $d_s_math_Ordering$;
var $n_s_math_Ordering$;
function $m_s_math_Ordering$() {
  if ((!$n_s_math_Ordering$)) {
    $n_s_math_Ordering$ = new $c_s_math_Ordering$()
  };
  return $n_s_math_Ordering$
}
function $as_s_math_ScalaNumber(obj) {
  return ((false || (obj === null)) ? obj : $throwClassCastException(obj, "scala.math.ScalaNumber"))
}
function $isArrayOf_s_math_ScalaNumber(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.s_math_ScalaNumber)))
}
function $asArrayOf_s_math_ScalaNumber(obj, depth) {
  return (($isArrayOf_s_math_ScalaNumber(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.math.ScalaNumber;", depth))
}
/** @constructor */
function $c_s_reflect_NoManifest$() {
  /*<skip>*/
}
$c_s_reflect_NoManifest$.prototype = new $h_O();
$c_s_reflect_NoManifest$.prototype.constructor = $c_s_reflect_NoManifest$;
/** @constructor */
function $h_s_reflect_NoManifest$() {
  /*<skip>*/
}
$h_s_reflect_NoManifest$.prototype = $c_s_reflect_NoManifest$.prototype;
$c_s_reflect_NoManifest$.prototype.toString__T = (function() {
  return "<?>"
});
var $d_s_reflect_NoManifest$ = new $TypeData().initClass({
  s_reflect_NoManifest$: 0
}, false, "scala.reflect.NoManifest$", {
  s_reflect_NoManifest$: 1,
  O: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1
});
$c_s_reflect_NoManifest$.prototype.$classData = $d_s_reflect_NoManifest$;
var $n_s_reflect_NoManifest$;
function $m_s_reflect_NoManifest$() {
  if ((!$n_s_reflect_NoManifest$)) {
    $n_s_reflect_NoManifest$ = new $c_s_reflect_NoManifest$()
  };
  return $n_s_reflect_NoManifest$
}
var $d_sr_Nothing$ = new $TypeData().initClass({
  sr_Nothing$: 0
}, false, "scala.runtime.Nothing$", {
  sr_Nothing$: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
/** @constructor */
function $c_sjsr_AnonFunction0(f) {
  this.sjsr_AnonFunction0__f_f = null;
  this.sjsr_AnonFunction0__f_f = f
}
$c_sjsr_AnonFunction0.prototype = new $h_sr_AbstractFunction0();
$c_sjsr_AnonFunction0.prototype.constructor = $c_sjsr_AnonFunction0;
/** @constructor */
function $h_sjsr_AnonFunction0() {
  /*<skip>*/
}
$h_sjsr_AnonFunction0.prototype = $c_sjsr_AnonFunction0.prototype;
$c_sjsr_AnonFunction0.prototype.apply__O = (function() {
  return (0, this.sjsr_AnonFunction0__f_f)()
});
var $d_sjsr_AnonFunction0 = new $TypeData().initClass({
  sjsr_AnonFunction0: 0
}, false, "scala.scalajs.runtime.AnonFunction0", {
  sjsr_AnonFunction0: 1,
  sr_AbstractFunction0: 1,
  O: 1,
  F0: 1
});
$c_sjsr_AnonFunction0.prototype.$classData = $d_sjsr_AnonFunction0;
/** @constructor */
function $c_sjsr_AnonFunction1(f) {
  this.sjsr_AnonFunction1__f_f = null;
  this.sjsr_AnonFunction1__f_f = f
}
$c_sjsr_AnonFunction1.prototype = new $h_sr_AbstractFunction1();
$c_sjsr_AnonFunction1.prototype.constructor = $c_sjsr_AnonFunction1;
/** @constructor */
function $h_sjsr_AnonFunction1() {
  /*<skip>*/
}
$h_sjsr_AnonFunction1.prototype = $c_sjsr_AnonFunction1.prototype;
$c_sjsr_AnonFunction1.prototype.apply__O__O = (function(arg1) {
  return (0, this.sjsr_AnonFunction1__f_f)(arg1)
});
var $d_sjsr_AnonFunction1 = new $TypeData().initClass({
  sjsr_AnonFunction1: 0
}, false, "scala.scalajs.runtime.AnonFunction1", {
  sjsr_AnonFunction1: 1,
  sr_AbstractFunction1: 1,
  O: 1,
  F1: 1
});
$c_sjsr_AnonFunction1.prototype.$classData = $d_sjsr_AnonFunction1;
/** @constructor */
function $c_sjsr_AnonFunction2(f) {
  this.sjsr_AnonFunction2__f_f = null;
  this.sjsr_AnonFunction2__f_f = f
}
$c_sjsr_AnonFunction2.prototype = new $h_sr_AbstractFunction2();
$c_sjsr_AnonFunction2.prototype.constructor = $c_sjsr_AnonFunction2;
/** @constructor */
function $h_sjsr_AnonFunction2() {
  /*<skip>*/
}
$h_sjsr_AnonFunction2.prototype = $c_sjsr_AnonFunction2.prototype;
$c_sjsr_AnonFunction2.prototype.apply__O__O__O = (function(arg1, arg2) {
  return (0, this.sjsr_AnonFunction2__f_f)(arg1, arg2)
});
var $d_sjsr_AnonFunction2 = new $TypeData().initClass({
  sjsr_AnonFunction2: 0
}, false, "scala.scalajs.runtime.AnonFunction2", {
  sjsr_AnonFunction2: 1,
  sr_AbstractFunction2: 1,
  O: 1,
  F2: 1
});
$c_sjsr_AnonFunction2.prototype.$classData = $d_sjsr_AnonFunction2;
/** @constructor */
function $c_sjsr_AnonFunction3(f) {
  this.sjsr_AnonFunction3__f_f = null;
  this.sjsr_AnonFunction3__f_f = f
}
$c_sjsr_AnonFunction3.prototype = new $h_sr_AbstractFunction3();
$c_sjsr_AnonFunction3.prototype.constructor = $c_sjsr_AnonFunction3;
/** @constructor */
function $h_sjsr_AnonFunction3() {
  /*<skip>*/
}
$h_sjsr_AnonFunction3.prototype = $c_sjsr_AnonFunction3.prototype;
$c_sjsr_AnonFunction3.prototype.apply__O__O__O__O = (function(arg1, arg2, arg3) {
  return (0, this.sjsr_AnonFunction3__f_f)(arg1, arg2, arg3)
});
var $d_sjsr_AnonFunction3 = new $TypeData().initClass({
  sjsr_AnonFunction3: 0
}, false, "scala.scalajs.runtime.AnonFunction3", {
  sjsr_AnonFunction3: 1,
  sr_AbstractFunction3: 1,
  O: 1,
  F3: 1
});
$c_sjsr_AnonFunction3.prototype.$classData = $d_sjsr_AnonFunction3;
/** @constructor */
function $c_Ldev_myclinic_scala_model_Appoint(date, time, patientName, patientId, memo) {
  this.Ldev_myclinic_scala_model_Appoint__f_date = null;
  this.Ldev_myclinic_scala_model_Appoint__f_time = null;
  this.Ldev_myclinic_scala_model_Appoint__f_patientName = null;
  this.Ldev_myclinic_scala_model_Appoint__f_patientId = null;
  this.Ldev_myclinic_scala_model_Appoint__f_memo = null;
  this.Ldev_myclinic_scala_model_Appoint__f_date = date;
  this.Ldev_myclinic_scala_model_Appoint__f_time = time;
  this.Ldev_myclinic_scala_model_Appoint__f_patientName = patientName;
  this.Ldev_myclinic_scala_model_Appoint__f_patientId = patientId;
  this.Ldev_myclinic_scala_model_Appoint__f_memo = memo
}
$c_Ldev_myclinic_scala_model_Appoint.prototype = new $h_O();
$c_Ldev_myclinic_scala_model_Appoint.prototype.constructor = $c_Ldev_myclinic_scala_model_Appoint;
/** @constructor */
function $h_Ldev_myclinic_scala_model_Appoint() {
  /*<skip>*/
}
$h_Ldev_myclinic_scala_model_Appoint.prototype = $c_Ldev_myclinic_scala_model_Appoint.prototype;
$c_Ldev_myclinic_scala_model_Appoint.prototype.productPrefix__T = (function() {
  return "Appoint"
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.productArity__I = (function() {
  return 5
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.productElement__I__O = (function(x$1) {
  switch (x$1) {
    case 0: {
      return this.Ldev_myclinic_scala_model_Appoint__f_date;
      break
    }
    case 1: {
      return this.Ldev_myclinic_scala_model_Appoint__f_time;
      break
    }
    case 2: {
      return this.Ldev_myclinic_scala_model_Appoint__f_patientName;
      break
    }
    case 3: {
      return this.Ldev_myclinic_scala_model_Appoint__f_patientId;
      break
    }
    case 4: {
      return this.Ldev_myclinic_scala_model_Appoint__f_memo;
      break
    }
    default: {
      return $m_sr_Statics$().ioobe__I__O(x$1)
    }
  }
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.hashCode__I = (function() {
  var this$2 = $m_s_util_hashing_MurmurHash3$();
  return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.toString__T = (function() {
  return $m_sr_ScalaRunTime$()._toString__s_Product__T(this)
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.equals__O__Z = (function(x$1) {
  if ((this === x$1)) {
    return true
  } else if ((x$1 instanceof $c_Ldev_myclinic_scala_model_Appoint)) {
    var Appoint$1 = $as_Ldev_myclinic_scala_model_Appoint(x$1);
    var x = this.Ldev_myclinic_scala_model_Appoint__f_date;
    var x$2 = Appoint$1.Ldev_myclinic_scala_model_Appoint__f_date;
    if (((x === null) ? (x$2 === null) : x.equals__O__Z(x$2))) {
      var x$3 = this.Ldev_myclinic_scala_model_Appoint__f_time;
      var x$4 = Appoint$1.Ldev_myclinic_scala_model_Appoint__f_time;
      var $$x2 = ((x$3 === null) ? (x$4 === null) : x$3.equals__O__Z(x$4))
    } else {
      var $$x2 = false
    };
    if (($$x2 && (this.Ldev_myclinic_scala_model_Appoint__f_patientName === Appoint$1.Ldev_myclinic_scala_model_Appoint__f_patientName))) {
      var x$5 = this.Ldev_myclinic_scala_model_Appoint__f_patientId;
      var x$6 = Appoint$1.Ldev_myclinic_scala_model_Appoint__f_patientId;
      var $$x1 = ((x$5 === null) ? (x$6 === null) : x$5.equals__O__Z(x$6))
    } else {
      var $$x1 = false
    };
    if ($$x1) {
      return (this.Ldev_myclinic_scala_model_Appoint__f_memo === Appoint$1.Ldev_myclinic_scala_model_Appoint__f_memo)
    } else {
      return false
    }
  } else {
    return false
  }
});
function $as_Ldev_myclinic_scala_model_Appoint(obj) {
  return (((obj instanceof $c_Ldev_myclinic_scala_model_Appoint) || (obj === null)) ? obj : $throwClassCastException(obj, "dev.myclinic.scala.model.Appoint"))
}
function $isArrayOf_Ldev_myclinic_scala_model_Appoint(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ldev_myclinic_scala_model_Appoint)))
}
function $asArrayOf_Ldev_myclinic_scala_model_Appoint(obj, depth) {
  return (($isArrayOf_Ldev_myclinic_scala_model_Appoint(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ldev.myclinic.scala.model.Appoint;", depth))
}
var $d_Ldev_myclinic_scala_model_Appoint = new $TypeData().initClass({
  Ldev_myclinic_scala_model_Appoint: 0
}, false, "dev.myclinic.scala.model.Appoint", {
  Ldev_myclinic_scala_model_Appoint: 1,
  O: 1,
  s_Product: 1,
  s_Equals: 1,
  Ljava_io_Serializable: 1
});
$c_Ldev_myclinic_scala_model_Appoint.prototype.$classData = $d_Ldev_myclinic_scala_model_Appoint;
/** @constructor */
function $c_Ljava_io_OutputStream() {
  /*<skip>*/
}
$c_Ljava_io_OutputStream.prototype = new $h_O();
$c_Ljava_io_OutputStream.prototype.constructor = $c_Ljava_io_OutputStream;
/** @constructor */
function $h_Ljava_io_OutputStream() {
  /*<skip>*/
}
$h_Ljava_io_OutputStream.prototype = $c_Ljava_io_OutputStream.prototype;
function $f_jl_Byte__equals__O__Z($thiz, that) {
  return Object.is($thiz, that)
}
function $f_jl_Byte__hashCode__I($thiz) {
  return $uB($thiz)
}
function $f_jl_Byte__toString__T($thiz) {
  var b = $uB($thiz);
  return ("" + b)
}
var $d_jl_Byte = new $TypeData().initClass({
  jl_Byte: 0
}, false, "java.lang.Byte", {
  jl_Byte: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => $isByte(x)));
function $f_jl_Double__equals__O__Z($thiz, that) {
  return Object.is($thiz, that)
}
function $f_jl_Double__hashCode__I($thiz) {
  var value = $uD($thiz);
  return $m_jl_FloatingPointBits$().numberHashCode__D__I(value)
}
function $f_jl_Double__toString__T($thiz) {
  var d = $uD($thiz);
  return ("" + d)
}
function $as_jl_Double(obj) {
  return ((((typeof obj) === "number") || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Double"))
}
function $isArrayOf_jl_Double(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Double)))
}
function $asArrayOf_jl_Double(obj, depth) {
  return (($isArrayOf_jl_Double(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Double;", depth))
}
var $d_jl_Double = new $TypeData().initClass({
  jl_Double: 0
}, false, "java.lang.Double", {
  jl_Double: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => ((typeof x) === "number")));
function $f_jl_Float__equals__O__Z($thiz, that) {
  return Object.is($thiz, that)
}
function $f_jl_Float__hashCode__I($thiz) {
  var value = $uF($thiz);
  return $m_jl_FloatingPointBits$().numberHashCode__D__I(value)
}
function $f_jl_Float__toString__T($thiz) {
  var f = $uF($thiz);
  return ("" + f)
}
var $d_jl_Float = new $TypeData().initClass({
  jl_Float: 0
}, false, "java.lang.Float", {
  jl_Float: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => ((typeof x) === "number")));
function $f_jl_Integer__equals__O__Z($thiz, that) {
  return Object.is($thiz, that)
}
function $f_jl_Integer__hashCode__I($thiz) {
  return $uI($thiz)
}
function $f_jl_Integer__toString__T($thiz) {
  var i = $uI($thiz);
  return ("" + i)
}
var $d_jl_Integer = new $TypeData().initClass({
  jl_Integer: 0
}, false, "java.lang.Integer", {
  jl_Integer: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => $isInt(x)));
function $f_jl_Long__equals__O__Z($thiz, that) {
  if ((that instanceof $c_RTLong)) {
    var x2 = $as_jl_Long(that);
    var this$1 = $uJ($thiz);
    var b = $uJ(x2);
    return ((this$1.RTLong__f_lo === b.RTLong__f_lo) && (this$1.RTLong__f_hi === b.RTLong__f_hi))
  } else {
    return false
  }
}
function $f_jl_Long__hashCode__I($thiz) {
  var t = $uJ($thiz);
  var lo = t.RTLong__f_lo;
  var hi = t.RTLong__f_hi;
  return (lo ^ hi)
}
function $f_jl_Long__toString__T($thiz) {
  var this$1 = $uJ($thiz);
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toString__I__I__T(this$1.RTLong__f_lo, this$1.RTLong__f_hi)
}
function $as_jl_Long(obj) {
  return (((obj instanceof $c_RTLong) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.Long"))
}
function $isArrayOf_jl_Long(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_Long)))
}
function $asArrayOf_jl_Long(obj, depth) {
  return (($isArrayOf_jl_Long(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.Long;", depth))
}
var $d_jl_Long = new $TypeData().initClass({
  jl_Long: 0
}, false, "java.lang.Long", {
  jl_Long: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => (x instanceof $c_RTLong)));
function $ct_jl_RuntimeException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
class $c_jl_RuntimeException extends $c_jl_Exception {
}
var $d_jl_RuntimeException = new $TypeData().initClass({
  jl_RuntimeException: 0
}, false, "java.lang.RuntimeException", {
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_RuntimeException.prototype.$classData = $d_jl_RuntimeException;
function $f_jl_Short__equals__O__Z($thiz, that) {
  return Object.is($thiz, that)
}
function $f_jl_Short__hashCode__I($thiz) {
  return $uS($thiz)
}
function $f_jl_Short__toString__T($thiz) {
  var s = $uS($thiz);
  return ("" + s)
}
var $d_jl_Short = new $TypeData().initClass({
  jl_Short: 0
}, false, "java.lang.Short", {
  jl_Short: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
}, (void 0), (void 0), ((x) => $isShort(x)));
function $f_T__hashCode__I($thiz) {
  var res = 0;
  var mul = 1;
  var i = (((-1) + $uI($thiz.length)) | 0);
  while ((i >= 0)) {
    var $$x1 = res;
    var index = i;
    res = (($$x1 + $imul((65535 & $uI($thiz.charCodeAt(index))), mul)) | 0);
    mul = $imul(31, mul);
    i = (((-1) + i) | 0)
  };
  return res
}
function $f_T__equals__O__Z($thiz, that) {
  return ($thiz === that)
}
function $f_T__compareTo__T__I($thiz, anotherString) {
  var thisLength = $uI($thiz.length);
  var strLength = $uI(anotherString.length);
  var minLength = ((thisLength < strLength) ? thisLength : strLength);
  var i = 0;
  while ((i !== minLength)) {
    var index = i;
    var $$x1 = $uI($thiz.charCodeAt(index));
    var index$1 = i;
    var cmp = (((65535 & $$x1) - (65535 & $uI(anotherString.charCodeAt(index$1)))) | 0);
    if ((cmp !== 0)) {
      return cmp
    };
    i = ((1 + i) | 0)
  };
  return ((thisLength - strLength) | 0)
}
function $f_T__getChars__I__I__AC__I__V($thiz, srcBegin, srcEnd, dst, dstBegin) {
  if (((((srcEnd > $uI($thiz.length)) || (srcBegin < 0)) || (srcEnd < 0)) || (srcBegin > srcEnd))) {
    throw $ct_jl_StringIndexOutOfBoundsException__T__(new $c_jl_StringIndexOutOfBoundsException(), "Index out of Bound")
  };
  var offset = ((dstBegin - srcBegin) | 0);
  var i = srcBegin;
  while ((i < srcEnd)) {
    var $$x1 = i;
    var index = i;
    dst.set((($$x1 + offset) | 0), (65535 & $uI($thiz.charCodeAt(index))));
    i = ((1 + i) | 0)
  }
}
function $f_T__toString__T($thiz) {
  return $thiz
}
function $as_T(obj) {
  return ((((typeof obj) === "string") || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.String"))
}
function $isArrayOf_T(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.T)))
}
function $asArrayOf_T(obj, depth) {
  return (($isArrayOf_T(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.String;", depth))
}
var $d_T = new $TypeData().initClass({
  T: 0
}, false, "java.lang.String", {
  T: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1,
  jl_CharSequence: 1
}, (void 0), (void 0), ((x) => ((typeof x) === "string")));
function $ct_jl_StringBuilder__($thiz) {
  $thiz.jl_StringBuilder__f_java$lang$StringBuilder$$content = "";
  return $thiz
}
function $ct_jl_StringBuilder__T__($thiz, str) {
  $ct_jl_StringBuilder__($thiz);
  if ((str === null)) {
    throw new $c_jl_NullPointerException()
  };
  $thiz.jl_StringBuilder__f_java$lang$StringBuilder$$content = str;
  return $thiz
}
function $ct_jl_StringBuilder__I__($thiz, initialCapacity) {
  $ct_jl_StringBuilder__($thiz);
  if ((initialCapacity < 0)) {
    throw new $c_jl_NegativeArraySizeException()
  };
  return $thiz
}
/** @constructor */
function $c_jl_StringBuilder() {
  this.jl_StringBuilder__f_java$lang$StringBuilder$$content = null
}
$c_jl_StringBuilder.prototype = new $h_O();
$c_jl_StringBuilder.prototype.constructor = $c_jl_StringBuilder;
/** @constructor */
function $h_jl_StringBuilder() {
  /*<skip>*/
}
$h_jl_StringBuilder.prototype = $c_jl_StringBuilder.prototype;
$c_jl_StringBuilder.prototype.append__AC__jl_StringBuilder = (function(str) {
  var this$1 = $m_jl_String$();
  var count = str.u.length;
  var str$1 = this$1.new__AC__I__I__T(str, 0, count);
  this.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str$1);
  return this
});
$c_jl_StringBuilder.prototype.deleteCharAt__I__jl_StringBuilder = (function(index) {
  var oldContent = this.jl_StringBuilder__f_java$lang$StringBuilder$$content;
  if (((index < 0) || (index >= $uI(oldContent.length)))) {
    throw $ct_jl_StringIndexOutOfBoundsException__I__(new $c_jl_StringIndexOutOfBoundsException(), index)
  };
  var $$x1 = $as_T(oldContent.substring(0, index));
  var beginIndex = ((1 + index) | 0);
  this.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + $$x1) + $as_T(oldContent.substring(beginIndex)));
  return this
});
$c_jl_StringBuilder.prototype.toString__T = (function() {
  return this.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
$c_jl_StringBuilder.prototype.length__I = (function() {
  var this$1 = this.jl_StringBuilder__f_java$lang$StringBuilder$$content;
  return $uI(this$1.length)
});
$c_jl_StringBuilder.prototype.setLength__I__V = (function(newLength) {
  if ((newLength < 0)) {
    throw $ct_jl_StringIndexOutOfBoundsException__I__(new $c_jl_StringIndexOutOfBoundsException(), newLength)
  };
  var newContent = this.jl_StringBuilder__f_java$lang$StringBuilder$$content;
  var this$1 = newContent;
  var additional = ((newLength - $uI(this$1.length)) | 0);
  if ((additional < 0)) {
    var this$2 = newContent;
    newContent = $as_T(this$2.substring(0, newLength))
  } else {
    var i = 0;
    while ((i !== additional)) {
      newContent = (newContent + "\u0000");
      i = ((1 + i) | 0)
    }
  };
  this.jl_StringBuilder__f_java$lang$StringBuilder$$content = newContent
});
$c_jl_StringBuilder.prototype.charAt__I__C = (function(index) {
  var this$1 = this.jl_StringBuilder__f_java$lang$StringBuilder$$content;
  return (65535 & $uI(this$1.charCodeAt(index)))
});
$c_jl_StringBuilder.prototype.setCharAt__I__C__V = (function(index, ch) {
  var oldContent = this.jl_StringBuilder__f_java$lang$StringBuilder$$content;
  if (((index < 0) || (index >= $uI(oldContent.length)))) {
    throw $ct_jl_StringIndexOutOfBoundsException__I__(new $c_jl_StringIndexOutOfBoundsException(), index)
  };
  var $$x1 = $as_T(oldContent.substring(0, index));
  var beginIndex = ((1 + index) | 0);
  this.jl_StringBuilder__f_java$lang$StringBuilder$$content = ((("" + $$x1) + $bC(ch)) + $as_T(oldContent.substring(beginIndex)))
});
var $d_jl_StringBuilder = new $TypeData().initClass({
  jl_StringBuilder: 0
}, false, "java.lang.StringBuilder", {
  jl_StringBuilder: 1,
  O: 1,
  jl_CharSequence: 1,
  jl_Appendable: 1,
  Ljava_io_Serializable: 1
});
$c_jl_StringBuilder.prototype.$classData = $d_jl_StringBuilder;
class $c_jl_VirtualMachineError extends $c_jl_Error {
}
/** @constructor */
function $c_RTLong(lo, hi) {
  this.RTLong__f_lo = 0;
  this.RTLong__f_hi = 0;
  this.RTLong__f_lo = lo;
  this.RTLong__f_hi = hi
}
$c_RTLong.prototype = new $h_jl_Number();
$c_RTLong.prototype.constructor = $c_RTLong;
/** @constructor */
function $h_RTLong() {
  /*<skip>*/
}
$h_RTLong.prototype = $c_RTLong.prototype;
$c_RTLong.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_RTLong)) {
    var x2 = $as_RTLong(that);
    return ((this.RTLong__f_lo === x2.RTLong__f_lo) && (this.RTLong__f_hi === x2.RTLong__f_hi))
  } else {
    return false
  }
});
$c_RTLong.prototype.hashCode__I = (function() {
  return (this.RTLong__f_lo ^ this.RTLong__f_hi)
});
$c_RTLong.prototype.toString__T = (function() {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toString__I__I__T(this.RTLong__f_lo, this.RTLong__f_hi)
});
$c_RTLong.prototype.toInt__I = (function() {
  return this.RTLong__f_lo
});
$c_RTLong.prototype.toFloat__F = (function() {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toFloat__I__I__F(this.RTLong__f_lo, this.RTLong__f_hi)
});
$c_RTLong.prototype.toDouble__D = (function() {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D(this.RTLong__f_lo, this.RTLong__f_hi)
});
$c_RTLong.prototype.byteValue__B = (function() {
  return ((this.RTLong__f_lo << 24) >> 24)
});
$c_RTLong.prototype.shortValue__S = (function() {
  return ((this.RTLong__f_lo << 16) >> 16)
});
$c_RTLong.prototype.intValue__I = (function() {
  return this.RTLong__f_lo
});
$c_RTLong.prototype.longValue__J = (function() {
  return $uJ(this)
});
$c_RTLong.prototype.floatValue__F = (function() {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toFloat__I__I__F(this.RTLong__f_lo, this.RTLong__f_hi)
});
$c_RTLong.prototype.doubleValue__D = (function() {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toDouble__I__I__D(this.RTLong__f_lo, this.RTLong__f_hi)
});
$c_RTLong.prototype.compareTo__jl_Long__I = (function(that) {
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$compare__I__I__I__I__I(this.RTLong__f_lo, this.RTLong__f_hi, that.RTLong__f_lo, that.RTLong__f_hi)
});
$c_RTLong.prototype.equals__RTLong__Z = (function(b) {
  return ((this.RTLong__f_lo === b.RTLong__f_lo) && (this.RTLong__f_hi === b.RTLong__f_hi))
});
$c_RTLong.prototype.notEquals__RTLong__Z = (function(b) {
  return (!((this.RTLong__f_lo === b.RTLong__f_lo) && (this.RTLong__f_hi === b.RTLong__f_hi)))
});
$c_RTLong.prototype.$less__RTLong__Z = (function(b) {
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  return ((ahi === bhi) ? (((-2147483648) ^ this.RTLong__f_lo) < ((-2147483648) ^ b.RTLong__f_lo)) : (ahi < bhi))
});
$c_RTLong.prototype.$less$eq__RTLong__Z = (function(b) {
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  return ((ahi === bhi) ? (((-2147483648) ^ this.RTLong__f_lo) <= ((-2147483648) ^ b.RTLong__f_lo)) : (ahi < bhi))
});
$c_RTLong.prototype.$greater__RTLong__Z = (function(b) {
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  return ((ahi === bhi) ? (((-2147483648) ^ this.RTLong__f_lo) > ((-2147483648) ^ b.RTLong__f_lo)) : (ahi > bhi))
});
$c_RTLong.prototype.$greater$eq__RTLong__Z = (function(b) {
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  return ((ahi === bhi) ? (((-2147483648) ^ this.RTLong__f_lo) >= ((-2147483648) ^ b.RTLong__f_lo)) : (ahi > bhi))
});
$c_RTLong.prototype.unary_$tilde__RTLong = (function() {
  return new $c_RTLong((~this.RTLong__f_lo), (~this.RTLong__f_hi))
});
$c_RTLong.prototype.$bar__RTLong__RTLong = (function(b) {
  return new $c_RTLong((this.RTLong__f_lo | b.RTLong__f_lo), (this.RTLong__f_hi | b.RTLong__f_hi))
});
$c_RTLong.prototype.$amp__RTLong__RTLong = (function(b) {
  return new $c_RTLong((this.RTLong__f_lo & b.RTLong__f_lo), (this.RTLong__f_hi & b.RTLong__f_hi))
});
$c_RTLong.prototype.$up__RTLong__RTLong = (function(b) {
  return new $c_RTLong((this.RTLong__f_lo ^ b.RTLong__f_lo), (this.RTLong__f_hi ^ b.RTLong__f_hi))
});
$c_RTLong.prototype.$less$less__I__RTLong = (function(n) {
  var lo = this.RTLong__f_lo;
  return new $c_RTLong((((32 & n) === 0) ? (lo << n) : 0), (((32 & n) === 0) ? (((((lo >>> 1) | 0) >>> ((31 - n) | 0)) | 0) | (this.RTLong__f_hi << n)) : (lo << n)))
});
$c_RTLong.prototype.$greater$greater$greater__I__RTLong = (function(n) {
  var hi = this.RTLong__f_hi;
  return new $c_RTLong((((32 & n) === 0) ? (((this.RTLong__f_lo >>> n) | 0) | ((hi << 1) << ((31 - n) | 0))) : ((hi >>> n) | 0)), (((32 & n) === 0) ? ((hi >>> n) | 0) : 0))
});
$c_RTLong.prototype.$greater$greater__I__RTLong = (function(n) {
  var hi = this.RTLong__f_hi;
  return new $c_RTLong((((32 & n) === 0) ? (((this.RTLong__f_lo >>> n) | 0) | ((hi << 1) << ((31 - n) | 0))) : (hi >> n)), (((32 & n) === 0) ? (hi >> n) : (hi >> 31)))
});
$c_RTLong.prototype.unary_$minus__RTLong = (function() {
  var lo = this.RTLong__f_lo;
  var hi = this.RTLong__f_hi;
  return new $c_RTLong(((-lo) | 0), ((lo !== 0) ? (~hi) : ((-hi) | 0)))
});
$c_RTLong.prototype.$plus__RTLong__RTLong = (function(b) {
  var alo = this.RTLong__f_lo;
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  var lo = ((alo + b.RTLong__f_lo) | 0);
  return new $c_RTLong(lo, ((((-2147483648) ^ lo) < ((-2147483648) ^ alo)) ? ((1 + ((ahi + bhi) | 0)) | 0) : ((ahi + bhi) | 0)))
});
$c_RTLong.prototype.$minus__RTLong__RTLong = (function(b) {
  var alo = this.RTLong__f_lo;
  var ahi = this.RTLong__f_hi;
  var bhi = b.RTLong__f_hi;
  var lo = ((alo - b.RTLong__f_lo) | 0);
  return new $c_RTLong(lo, ((((-2147483648) ^ lo) > ((-2147483648) ^ alo)) ? (((-1) + ((ahi - bhi) | 0)) | 0) : ((ahi - bhi) | 0)))
});
$c_RTLong.prototype.$times__RTLong__RTLong = (function(b) {
  var alo = this.RTLong__f_lo;
  var blo = b.RTLong__f_lo;
  var a0 = (65535 & alo);
  var a1 = ((alo >>> 16) | 0);
  var b0 = (65535 & blo);
  var b1 = ((blo >>> 16) | 0);
  var a0b0 = $imul(a0, b0);
  var a1b0 = $imul(a1, b0);
  var a0b1 = $imul(a0, b1);
  var lo = ((a0b0 + (((a1b0 + a0b1) | 0) << 16)) | 0);
  var c1part = ((((a0b0 >>> 16) | 0) + a0b1) | 0);
  var hi = (((((((($imul(alo, b.RTLong__f_hi) + $imul(this.RTLong__f_hi, blo)) | 0) + $imul(a1, b1)) | 0) + ((c1part >>> 16) | 0)) | 0) + (((((65535 & c1part) + a1b0) | 0) >>> 16) | 0)) | 0);
  return new $c_RTLong(lo, hi)
});
$c_RTLong.prototype.$div__RTLong__RTLong = (function(b) {
  var this$1 = $m_RTLong$();
  var lo = this$1.divideImpl__I__I__I__I__I(this.RTLong__f_lo, this.RTLong__f_hi, b.RTLong__f_lo, b.RTLong__f_hi);
  return new $c_RTLong(lo, this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn)
});
$c_RTLong.prototype.$percent__RTLong__RTLong = (function(b) {
  var this$1 = $m_RTLong$();
  var lo = this$1.remainderImpl__I__I__I__I__I(this.RTLong__f_lo, this.RTLong__f_hi, b.RTLong__f_lo, b.RTLong__f_hi);
  return new $c_RTLong(lo, this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn)
});
$c_RTLong.prototype.compareTo__O__I = (function(x$1) {
  var that = $as_jl_Long(x$1);
  return $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$compare__I__I__I__I__I(this.RTLong__f_lo, this.RTLong__f_hi, that.RTLong__f_lo, that.RTLong__f_hi)
});
function $as_RTLong(obj) {
  return (((obj instanceof $c_RTLong) || (obj === null)) ? obj : $throwClassCastException(obj, "org.scalajs.linker.runtime.RuntimeLong"))
}
function $isArrayOf_RTLong(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.RTLong)))
}
function $asArrayOf_RTLong(obj, depth) {
  return (($isArrayOf_RTLong(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lorg.scalajs.linker.runtime.RuntimeLong;", depth))
}
var $d_RTLong = new $TypeData().initClass({
  RTLong: 0
}, false, "org.scalajs.linker.runtime.RuntimeLong", {
  RTLong: 1,
  jl_Number: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  jl_Comparable: 1
});
$c_RTLong.prototype.$classData = $d_RTLong;
/** @constructor */
function $c_s_$eq$colon$eq() {
  /*<skip>*/
}
$c_s_$eq$colon$eq.prototype = new $h_s_$less$colon$less();
$c_s_$eq$colon$eq.prototype.constructor = $c_s_$eq$colon$eq;
/** @constructor */
function $h_s_$eq$colon$eq() {
  /*<skip>*/
}
$h_s_$eq$colon$eq.prototype = $c_s_$eq$colon$eq.prototype;
/** @constructor */
function $c_sc_AbstractIterator() {
  /*<skip>*/
}
$c_sc_AbstractIterator.prototype = new $h_O();
$c_sc_AbstractIterator.prototype.constructor = $c_sc_AbstractIterator;
/** @constructor */
function $h_sc_AbstractIterator() {
  /*<skip>*/
}
$h_sc_AbstractIterator.prototype = $c_sc_AbstractIterator.prototype;
$c_sc_AbstractIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sc_AbstractIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sc_AbstractIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sc_AbstractIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sc_AbstractIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sc_AbstractIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sc_AbstractIterator.prototype.knownSize__I = (function() {
  return (-1)
});
/** @constructor */
function $c_sc_Iterable$() {
  this.sc_IterableFactory$Delegate__f_delegate = null;
  $ct_sc_IterableFactory$Delegate__sc_IterableFactory__(this, $m_sci_Iterable$())
}
$c_sc_Iterable$.prototype = new $h_sc_IterableFactory$Delegate();
$c_sc_Iterable$.prototype.constructor = $c_sc_Iterable$;
/** @constructor */
function $h_sc_Iterable$() {
  /*<skip>*/
}
$h_sc_Iterable$.prototype = $c_sc_Iterable$.prototype;
var $d_sc_Iterable$ = new $TypeData().initClass({
  sc_Iterable$: 0
}, false, "scala.collection.Iterable$", {
  sc_Iterable$: 1,
  sc_IterableFactory$Delegate: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sc_Iterable$.prototype.$classData = $d_sc_Iterable$;
var $n_sc_Iterable$;
function $m_sc_Iterable$() {
  if ((!$n_sc_Iterable$)) {
    $n_sc_Iterable$ = new $c_sc_Iterable$()
  };
  return $n_sc_Iterable$
}
/** @constructor */
function $c_sc_Map$() {
  this.sc_MapFactory$Delegate__f_delegate = null;
  this.sc_Map$__f_DefaultSentinel = null;
  this.sc_Map$__f_scala$collection$Map$$DefaultSentinelFn = null;
  $ct_sc_MapFactory$Delegate__sc_MapFactory__(this, $m_sci_Map$());
  $n_sc_Map$ = this;
  this.sc_Map$__f_DefaultSentinel = $ct_O__(new $c_O());
  this.sc_Map$__f_scala$collection$Map$$DefaultSentinelFn = new $c_sjsr_AnonFunction0(((this$1) => (() => $m_sc_Map$().sc_Map$__f_DefaultSentinel))(this))
}
$c_sc_Map$.prototype = new $h_sc_MapFactory$Delegate();
$c_sc_Map$.prototype.constructor = $c_sc_Map$;
/** @constructor */
function $h_sc_Map$() {
  /*<skip>*/
}
$h_sc_Map$.prototype = $c_sc_Map$.prototype;
var $d_sc_Map$ = new $TypeData().initClass({
  sc_Map$: 0
}, false, "scala.collection.Map$", {
  sc_Map$: 1,
  sc_MapFactory$Delegate: 1,
  O: 1,
  sc_MapFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sc_Map$.prototype.$classData = $d_sc_Map$;
var $n_sc_Map$;
function $m_sc_Map$() {
  if ((!$n_sc_Map$)) {
    $n_sc_Map$ = new $c_sc_Map$()
  };
  return $n_sc_Map$
}
function $ct_sc_SeqFactory$Delegate__sc_SeqFactory__($thiz, delegate) {
  $thiz.sc_SeqFactory$Delegate__f_delegate = delegate;
  return $thiz
}
/** @constructor */
function $c_sc_SeqFactory$Delegate() {
  this.sc_SeqFactory$Delegate__f_delegate = null
}
$c_sc_SeqFactory$Delegate.prototype = new $h_O();
$c_sc_SeqFactory$Delegate.prototype.constructor = $c_sc_SeqFactory$Delegate;
/** @constructor */
function $h_sc_SeqFactory$Delegate() {
  /*<skip>*/
}
$h_sc_SeqFactory$Delegate.prototype = $c_sc_SeqFactory$Delegate.prototype;
$c_sc_SeqFactory$Delegate.prototype.from__sc_IterableOnce__sc_SeqOps = (function(it) {
  return $as_sc_SeqOps(this.sc_SeqFactory$Delegate__f_delegate.from__sc_IterableOnce__O(it))
});
$c_sc_SeqFactory$Delegate.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sc_SeqOps(source)
});
function $f_sc_SeqOps__indexOf__O__I__I($thiz, elem, from) {
  return $thiz.indexWhere__F1__I__I(new $c_sjsr_AnonFunction1(((this$1, elem$1) => ((x$1$2) => $m_sr_BoxesRunTime$().equals__O__O__Z(elem$1, x$1$2)))($thiz, elem)), from)
}
function $f_sc_SeqOps__contains__O__Z($thiz, elem) {
  return $thiz.exists__F1__Z(new $c_sjsr_AnonFunction1(((this$1, elem$1) => ((x$3$2) => $m_sr_BoxesRunTime$().equals__O__O__Z(x$3$2, elem$1)))($thiz, elem)))
}
function $f_sc_SeqOps__isEmpty__Z($thiz) {
  return ($thiz.lengthCompare__I__I(0) === 0)
}
function $f_sc_SeqOps__sameElements__sc_IterableOnce__Z($thiz, that) {
  var thisKnownSize = $thiz.knownSize__I();
  if ((thisKnownSize !== (-1))) {
    var thatKnownSize = that.knownSize__I();
    var knownSizeDifference = ((thatKnownSize !== (-1)) && (thisKnownSize !== thatKnownSize))
  } else {
    var knownSizeDifference = false
  };
  if ((!knownSizeDifference)) {
    var this$1 = $thiz.iterator__sc_Iterator();
    return $f_sc_Iterator__sameElements__sc_IterableOnce__Z(this$1, that)
  } else {
    return false
  }
}
function $is_sc_SeqOps(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_SeqOps)))
}
function $as_sc_SeqOps(obj) {
  return (($is_sc_SeqOps(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.SeqOps"))
}
function $isArrayOf_sc_SeqOps(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_SeqOps)))
}
function $asArrayOf_sc_SeqOps(obj, depth) {
  return (($isArrayOf_sc_SeqOps(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.SeqOps;", depth))
}
/** @constructor */
function $c_sci_Iterable$() {
  this.sc_IterableFactory$Delegate__f_delegate = null;
  $ct_sc_IterableFactory$Delegate__sc_IterableFactory__(this, $m_sci_List$())
}
$c_sci_Iterable$.prototype = new $h_sc_IterableFactory$Delegate();
$c_sci_Iterable$.prototype.constructor = $c_sci_Iterable$;
/** @constructor */
function $h_sci_Iterable$() {
  /*<skip>*/
}
$h_sci_Iterable$.prototype = $c_sci_Iterable$.prototype;
$c_sci_Iterable$.prototype.from__sc_IterableOnce__sci_Iterable = (function(it) {
  if ($is_sci_Iterable(it)) {
    var x2 = $as_sci_Iterable(it);
    return x2
  } else {
    return $as_sci_Iterable($c_sc_IterableFactory$Delegate.prototype.from__sc_IterableOnce__O.call(this, it))
  }
});
$c_sci_Iterable$.prototype.from__sc_IterableOnce__O = (function(it) {
  return this.from__sc_IterableOnce__sci_Iterable(it)
});
var $d_sci_Iterable$ = new $TypeData().initClass({
  sci_Iterable$: 0
}, false, "scala.collection.immutable.Iterable$", {
  sci_Iterable$: 1,
  sc_IterableFactory$Delegate: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Iterable$.prototype.$classData = $d_sci_Iterable$;
var $n_sci_Iterable$;
function $m_sci_Iterable$() {
  if ((!$n_sci_Iterable$)) {
    $n_sci_Iterable$ = new $c_sci_Iterable$()
  };
  return $n_sci_Iterable$
}
/** @constructor */
function $c_sci_LazyList$() {
  this.sci_LazyList$__f__empty = null;
  this.sci_LazyList$__f_scala$collection$immutable$LazyList$$anyToMarker = null;
  $n_sci_LazyList$ = this;
  var state = new $c_sjsr_AnonFunction0(((this$1) => (() => $m_sci_LazyList$State$Empty$()))(this));
  this.sci_LazyList$__f__empty = new $c_sci_LazyList(state).force__sci_LazyList();
  this.sci_LazyList$__f_scala$collection$immutable$LazyList$$anyToMarker = new $c_sjsr_AnonFunction1(((this$2) => ((x$10$2) => $m_sr_Statics$PFMarker$()))(this))
}
$c_sci_LazyList$.prototype = new $h_O();
$c_sci_LazyList$.prototype.constructor = $c_sci_LazyList$;
/** @constructor */
function $h_sci_LazyList$() {
  /*<skip>*/
}
$h_sci_LazyList$.prototype = $c_sci_LazyList$.prototype;
$c_sci_LazyList$.prototype.scala$collection$immutable$LazyList$$dropImpl__sci_LazyList__I__sci_LazyList = (function(ll, n) {
  var restRef = new $c_sr_ObjectRef(ll);
  var iRef = new $c_sr_IntRef(n);
  var state = new $c_sjsr_AnonFunction0(((this$3, restRef$1, iRef$1) => (() => {
    var rest = $as_sci_LazyList(restRef$1.sr_ObjectRef__f_elem);
    var i = iRef$1.sr_IntRef__f_elem;
    while (((i > 0) && (!rest.isEmpty__Z()))) {
      var this$4 = rest;
      rest = this$4.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
      restRef$1.sr_ObjectRef__f_elem = rest;
      i = (((-1) + i) | 0);
      iRef$1.sr_IntRef__f_elem = i
    };
    return rest.scala$collection$immutable$LazyList$$state__sci_LazyList$State()
  }))(this, restRef, iRef));
  return new $c_sci_LazyList(state)
});
$c_sci_LazyList$.prototype.from__sc_IterableOnce__sci_LazyList = (function(coll) {
  if ((coll instanceof $c_sci_LazyList)) {
    var x2 = $as_sci_LazyList(coll);
    return x2
  } else if ((coll.knownSize__I() === 0)) {
    return this.sci_LazyList$__f__empty
  } else {
    var state = new $c_sjsr_AnonFunction0(((this$1, coll$1) => (() => $m_sci_LazyList$().scala$collection$immutable$LazyList$$stateFromIterator__sc_Iterator__sci_LazyList$State(coll$1.iterator__sc_Iterator())))(this, coll));
    return new $c_sci_LazyList(state)
  }
});
$c_sci_LazyList$.prototype.scala$collection$immutable$LazyList$$stateFromIterator__sc_Iterator__sci_LazyList$State = (function(it) {
  if (it.hasNext__Z()) {
    var hd = it.next__O();
    var state = new $c_sjsr_AnonFunction0(((this$1, it$1) => (() => $m_sci_LazyList$().scala$collection$immutable$LazyList$$stateFromIterator__sc_Iterator__sci_LazyList$State(it$1)))(this, it));
    var tl = new $c_sci_LazyList(state);
    return new $c_sci_LazyList$State$Cons(hd, tl)
  } else {
    return $m_sci_LazyList$State$Empty$()
  }
});
$c_sci_LazyList$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_LazyList(source)
});
var $d_sci_LazyList$ = new $TypeData().initClass({
  sci_LazyList$: 0
}, false, "scala.collection.immutable.LazyList$", {
  sci_LazyList$: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_LazyList$.prototype.$classData = $d_sci_LazyList$;
var $n_sci_LazyList$;
function $m_sci_LazyList$() {
  if ((!$n_sci_LazyList$)) {
    $n_sci_LazyList$ = new $c_sci_LazyList$()
  };
  return $n_sci_LazyList$
}
/** @constructor */
function $c_sci_Stream$() {
  /*<skip>*/
}
$c_sci_Stream$.prototype = new $h_O();
$c_sci_Stream$.prototype.constructor = $c_sci_Stream$;
/** @constructor */
function $h_sci_Stream$() {
  /*<skip>*/
}
$h_sci_Stream$.prototype = $c_sci_Stream$.prototype;
$c_sci_Stream$.prototype.from__sc_IterableOnce__sci_Stream = (function(coll) {
  if ((coll instanceof $c_sci_Stream)) {
    var x2 = $as_sci_Stream(coll);
    return x2
  } else {
    return this.fromIterator__sc_Iterator__sci_Stream(coll.iterator__sc_Iterator())
  }
});
$c_sci_Stream$.prototype.fromIterator__sc_Iterator__sci_Stream = (function(it) {
  return (it.hasNext__Z() ? new $c_sci_Stream$Cons(it.next__O(), new $c_sjsr_AnonFunction0(((this$1, it$1) => (() => $m_sci_Stream$().fromIterator__sc_Iterator__sci_Stream(it$1)))(this, it))) : $m_sci_Stream$Empty$())
});
$c_sci_Stream$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_Stream(source)
});
var $d_sci_Stream$ = new $TypeData().initClass({
  sci_Stream$: 0
}, false, "scala.collection.immutable.Stream$", {
  sci_Stream$: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Stream$.prototype.$classData = $d_sci_Stream$;
var $n_sci_Stream$;
function $m_sci_Stream$() {
  if ((!$n_sci_Stream$)) {
    $n_sci_Stream$ = new $c_sci_Stream$()
  };
  return $n_sci_Stream$
}
/** @constructor */
function $c_sci_WrappedString$() {
  this.sci_WrappedString$__f_empty = null;
  $n_sci_WrappedString$ = this;
  this.sci_WrappedString$__f_empty = new $c_sci_WrappedString("")
}
$c_sci_WrappedString$.prototype = new $h_O();
$c_sci_WrappedString$.prototype.constructor = $c_sci_WrappedString$;
/** @constructor */
function $h_sci_WrappedString$() {
  /*<skip>*/
}
$h_sci_WrappedString$.prototype = $c_sci_WrappedString$.prototype;
$c_sci_WrappedString$.prototype.fromSpecific__sc_IterableOnce__sci_WrappedString = (function(it) {
  var b = this.newBuilder__scm_Builder();
  var s = it.knownSize__I();
  if ((s >= 0)) {
    b.sizeHint__I__V(s)
  };
  b.addAll__sc_IterableOnce__scm_Growable(it);
  return $as_sci_WrappedString(b.result__O())
});
$c_sci_WrappedString$.prototype.newBuilder__scm_Builder = (function() {
  var this$2 = $ct_scm_StringBuilder__(new $c_scm_StringBuilder());
  var f = new $c_sjsr_AnonFunction1(((this$1) => ((x$2) => {
    var x = $as_T(x$2);
    return new $c_sci_WrappedString(x)
  }))(this));
  return new $c_scm_Builder$$anon$1(this$2, f)
});
var $d_sci_WrappedString$ = new $TypeData().initClass({
  sci_WrappedString$: 0
}, false, "scala.collection.immutable.WrappedString$", {
  sci_WrappedString$: 1,
  O: 1,
  sc_SpecificIterableFactory: 1,
  sc_Factory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_WrappedString$.prototype.$classData = $d_sci_WrappedString$;
var $n_sci_WrappedString$;
function $m_sci_WrappedString$() {
  if ((!$n_sci_WrappedString$)) {
    $n_sci_WrappedString$ = new $c_sci_WrappedString$()
  };
  return $n_sci_WrappedString$
}
/** @constructor */
function $c_scm_Builder$$anon$1(outer, f$1) {
  this.scm_Builder$$anon$1__f_$outer = null;
  this.scm_Builder$$anon$1__f_f$1 = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.scm_Builder$$anon$1__f_$outer = outer
  };
  this.scm_Builder$$anon$1__f_f$1 = f$1
}
$c_scm_Builder$$anon$1.prototype = new $h_O();
$c_scm_Builder$$anon$1.prototype.constructor = $c_scm_Builder$$anon$1;
/** @constructor */
function $h_scm_Builder$$anon$1() {
  /*<skip>*/
}
$h_scm_Builder$$anon$1.prototype = $c_scm_Builder$$anon$1.prototype;
$c_scm_Builder$$anon$1.prototype.addOne__O__scm_Builder$$anon$1 = (function(x) {
  var this$1 = this.scm_Builder$$anon$1__f_$outer;
  this$1.addOne__O__scm_Growable(x);
  return this
});
$c_scm_Builder$$anon$1.prototype.addAll__sc_IterableOnce__scm_Builder$$anon$1 = (function(xs) {
  var this$1 = this.scm_Builder$$anon$1__f_$outer;
  this$1.addAll__sc_IterableOnce__scm_Growable(xs);
  return this
});
$c_scm_Builder$$anon$1.prototype.sizeHint__I__V = (function(size) {
  this.scm_Builder$$anon$1__f_$outer.sizeHint__I__V(size)
});
$c_scm_Builder$$anon$1.prototype.result__O = (function() {
  return this.scm_Builder$$anon$1__f_f$1.apply__O__O(this.scm_Builder$$anon$1__f_$outer.result__O())
});
$c_scm_Builder$$anon$1.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__scm_Builder$$anon$1(xs)
});
$c_scm_Builder$$anon$1.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__O__scm_Builder$$anon$1(elem)
});
var $d_scm_Builder$$anon$1 = new $TypeData().initClass({
  scm_Builder$$anon$1: 0
}, false, "scala.collection.mutable.Builder$$anon$1", {
  scm_Builder$$anon$1: 1,
  O: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_scm_Builder$$anon$1.prototype.$classData = $d_scm_Builder$$anon$1;
/** @constructor */
function $c_scm_Iterable$() {
  this.sc_IterableFactory$Delegate__f_delegate = null;
  $ct_sc_IterableFactory$Delegate__sc_IterableFactory__(this, $m_scm_ArrayBuffer$())
}
$c_scm_Iterable$.prototype = new $h_sc_IterableFactory$Delegate();
$c_scm_Iterable$.prototype.constructor = $c_scm_Iterable$;
/** @constructor */
function $h_scm_Iterable$() {
  /*<skip>*/
}
$h_scm_Iterable$.prototype = $c_scm_Iterable$.prototype;
var $d_scm_Iterable$ = new $TypeData().initClass({
  scm_Iterable$: 0
}, false, "scala.collection.mutable.Iterable$", {
  scm_Iterable$: 1,
  sc_IterableFactory$Delegate: 1,
  O: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_Iterable$.prototype.$classData = $d_scm_Iterable$;
var $n_scm_Iterable$;
function $m_scm_Iterable$() {
  if ((!$n_scm_Iterable$)) {
    $n_scm_Iterable$ = new $c_scm_Iterable$()
  };
  return $n_scm_Iterable$
}
/** @constructor */
function $c_scm_Map$() {
  this.sc_MapFactory$Delegate__f_delegate = null;
  $ct_sc_MapFactory$Delegate__sc_MapFactory__(this, $m_scm_HashMap$())
}
$c_scm_Map$.prototype = new $h_sc_MapFactory$Delegate();
$c_scm_Map$.prototype.constructor = $c_scm_Map$;
/** @constructor */
function $h_scm_Map$() {
  /*<skip>*/
}
$h_scm_Map$.prototype = $c_scm_Map$.prototype;
var $d_scm_Map$ = new $TypeData().initClass({
  scm_Map$: 0
}, false, "scala.collection.mutable.Map$", {
  scm_Map$: 1,
  sc_MapFactory$Delegate: 1,
  O: 1,
  sc_MapFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_Map$.prototype.$classData = $d_scm_Map$;
var $n_scm_Map$;
function $m_scm_Map$() {
  if ((!$n_scm_Map$)) {
    $n_scm_Map$ = new $c_scm_Map$()
  };
  return $n_scm_Map$
}
/** @constructor */
function $c_sjs_js_WrappedDictionary$DictionaryIterator(dict) {
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_dict = null;
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_keys = null;
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_index = 0;
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_dict = dict;
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_keys = Object.keys(dict);
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_index = 0
}
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype = new $h_O();
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.constructor = $c_sjs_js_WrappedDictionary$DictionaryIterator;
/** @constructor */
function $h_sjs_js_WrappedDictionary$DictionaryIterator() {
  /*<skip>*/
}
$h_sjs_js_WrappedDictionary$DictionaryIterator.prototype = $c_sjs_js_WrappedDictionary$DictionaryIterator.prototype;
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.hasNext__Z = (function() {
  return (this.sjs_js_WrappedDictionary$DictionaryIterator__f_index < $uI(this.sjs_js_WrappedDictionary$DictionaryIterator__f_keys.length))
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.next__T2 = (function() {
  var key = $as_T(this.sjs_js_WrappedDictionary$DictionaryIterator__f_keys[this.sjs_js_WrappedDictionary$DictionaryIterator__f_index]);
  this.sjs_js_WrappedDictionary$DictionaryIterator__f_index = ((1 + this.sjs_js_WrappedDictionary$DictionaryIterator__f_index) | 0);
  var dict = this.sjs_js_WrappedDictionary$DictionaryIterator__f_dict;
  if ($uZ($m_sjs_js_WrappedDictionary$Cache$().sjs_js_WrappedDictionary$Cache$__f_safeHasOwnProperty.call(dict, key))) {
    var $$x1 = dict[key]
  } else {
    var $$x1;
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  };
  return new $c_T2(key, $$x1)
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.next__O = (function() {
  return this.next__T2()
});
var $d_sjs_js_WrappedDictionary$DictionaryIterator = new $TypeData().initClass({
  sjs_js_WrappedDictionary$DictionaryIterator: 0
}, false, "scala.scalajs.js.WrappedDictionary$DictionaryIterator", {
  sjs_js_WrappedDictionary$DictionaryIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sjs_js_WrappedDictionary$DictionaryIterator.prototype.$classData = $d_sjs_js_WrappedDictionary$DictionaryIterator;
function $ct_Ljava_io_FilterOutputStream__Ljava_io_OutputStream__($thiz, out) {
  $thiz.Ljava_io_FilterOutputStream__f_out = out;
  return $thiz
}
/** @constructor */
function $c_Ljava_io_FilterOutputStream() {
  this.Ljava_io_FilterOutputStream__f_out = null
}
$c_Ljava_io_FilterOutputStream.prototype = new $h_Ljava_io_OutputStream();
$c_Ljava_io_FilterOutputStream.prototype.constructor = $c_Ljava_io_FilterOutputStream;
/** @constructor */
function $h_Ljava_io_FilterOutputStream() {
  /*<skip>*/
}
$h_Ljava_io_FilterOutputStream.prototype = $c_Ljava_io_FilterOutputStream.prototype;
class $c_jl_ArithmeticException extends $c_jl_RuntimeException {
  constructor(s) {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, s, null, true, true)
  };
}
var $d_jl_ArithmeticException = new $TypeData().initClass({
  jl_ArithmeticException: 0
}, false, "java.lang.ArithmeticException", {
  jl_ArithmeticException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_ArithmeticException.prototype.$classData = $d_jl_ArithmeticException;
class $c_jl_ClassCastException extends $c_jl_RuntimeException {
  constructor(s) {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, s, null, true, true)
  };
}
function $as_jl_ClassCastException(obj) {
  return (((obj instanceof $c_jl_ClassCastException) || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.ClassCastException"))
}
function $isArrayOf_jl_ClassCastException(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_ClassCastException)))
}
function $asArrayOf_jl_ClassCastException(obj, depth) {
  return (($isArrayOf_jl_ClassCastException(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.ClassCastException;", depth))
}
var $d_jl_ClassCastException = new $TypeData().initClass({
  jl_ClassCastException: 0
}, false, "java.lang.ClassCastException", {
  jl_ClassCastException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_ClassCastException.prototype.$classData = $d_jl_ClassCastException;
function $ct_jl_IllegalArgumentException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
function $ct_jl_IllegalArgumentException__($thiz) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, null, null, true, true);
  return $thiz
}
class $c_jl_IllegalArgumentException extends $c_jl_RuntimeException {
}
var $d_jl_IllegalArgumentException = new $TypeData().initClass({
  jl_IllegalArgumentException: 0
}, false, "java.lang.IllegalArgumentException", {
  jl_IllegalArgumentException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_IllegalArgumentException.prototype.$classData = $d_jl_IllegalArgumentException;
function $ct_jl_IndexOutOfBoundsException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
class $c_jl_IndexOutOfBoundsException extends $c_jl_RuntimeException {
}
var $d_jl_IndexOutOfBoundsException = new $TypeData().initClass({
  jl_IndexOutOfBoundsException: 0
}, false, "java.lang.IndexOutOfBoundsException", {
  jl_IndexOutOfBoundsException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_IndexOutOfBoundsException.prototype.$classData = $d_jl_IndexOutOfBoundsException;
/** @constructor */
function $c_jl_JSConsoleBasedPrintStream$DummyOutputStream() {
  /*<skip>*/
}
$c_jl_JSConsoleBasedPrintStream$DummyOutputStream.prototype = new $h_Ljava_io_OutputStream();
$c_jl_JSConsoleBasedPrintStream$DummyOutputStream.prototype.constructor = $c_jl_JSConsoleBasedPrintStream$DummyOutputStream;
/** @constructor */
function $h_jl_JSConsoleBasedPrintStream$DummyOutputStream() {
  /*<skip>*/
}
$h_jl_JSConsoleBasedPrintStream$DummyOutputStream.prototype = $c_jl_JSConsoleBasedPrintStream$DummyOutputStream.prototype;
var $d_jl_JSConsoleBasedPrintStream$DummyOutputStream = new $TypeData().initClass({
  jl_JSConsoleBasedPrintStream$DummyOutputStream: 0
}, false, "java.lang.JSConsoleBasedPrintStream$DummyOutputStream", {
  jl_JSConsoleBasedPrintStream$DummyOutputStream: 1,
  Ljava_io_OutputStream: 1,
  O: 1,
  Ljava_io_Closeable: 1,
  jl_AutoCloseable: 1,
  Ljava_io_Flushable: 1
});
$c_jl_JSConsoleBasedPrintStream$DummyOutputStream.prototype.$classData = $d_jl_JSConsoleBasedPrintStream$DummyOutputStream;
class $c_jl_NegativeArraySizeException extends $c_jl_RuntimeException {
  constructor() {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, null, null, true, true)
  };
}
var $d_jl_NegativeArraySizeException = new $TypeData().initClass({
  jl_NegativeArraySizeException: 0
}, false, "java.lang.NegativeArraySizeException", {
  jl_NegativeArraySizeException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_NegativeArraySizeException.prototype.$classData = $d_jl_NegativeArraySizeException;
class $c_jl_NullPointerException extends $c_jl_RuntimeException {
  constructor() {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, null, null, true, true)
  };
}
var $d_jl_NullPointerException = new $TypeData().initClass({
  jl_NullPointerException: 0
}, false, "java.lang.NullPointerException", {
  jl_NullPointerException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_NullPointerException.prototype.$classData = $d_jl_NullPointerException;
function $as_jl_SecurityException(obj) {
  return ((false || (obj === null)) ? obj : $throwClassCastException(obj, "java.lang.SecurityException"))
}
function $isArrayOf_jl_SecurityException(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.jl_SecurityException)))
}
function $asArrayOf_jl_SecurityException(obj, depth) {
  return (($isArrayOf_jl_SecurityException(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.lang.SecurityException;", depth))
}
function $ct_jl_UnsupportedOperationException__($thiz) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, null, null, true, true);
  return $thiz
}
function $ct_jl_UnsupportedOperationException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
class $c_jl_UnsupportedOperationException extends $c_jl_RuntimeException {
}
var $d_jl_UnsupportedOperationException = new $TypeData().initClass({
  jl_UnsupportedOperationException: 0
}, false, "java.lang.UnsupportedOperationException", {
  jl_UnsupportedOperationException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_UnsupportedOperationException.prototype.$classData = $d_jl_UnsupportedOperationException;
class $c_Ljava_time_DateTimeException extends $c_jl_RuntimeException {
  constructor(message) {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, message, null, true, true)
  };
}
var $d_Ljava_time_DateTimeException = new $TypeData().initClass({
  Ljava_time_DateTimeException: 0
}, false, "java.time.DateTimeException", {
  Ljava_time_DateTimeException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_DateTimeException.prototype.$classData = $d_Ljava_time_DateTimeException;
/** @constructor */
function $c_Ljava_time_Duration(seconds, nanos) {
  this.Ljava_time_Duration__f_seconds = $L0;
  this.Ljava_time_Duration__f_nanos = 0;
  this.Ljava_time_Duration__f_seconds = seconds;
  this.Ljava_time_Duration__f_nanos = nanos
}
$c_Ljava_time_Duration.prototype = new $h_O();
$c_Ljava_time_Duration.prototype.constructor = $c_Ljava_time_Duration;
/** @constructor */
function $h_Ljava_time_Duration() {
  /*<skip>*/
}
$h_Ljava_time_Duration.prototype = $c_Ljava_time_Duration.prototype;
$c_Ljava_time_Duration.prototype.equals__O__Z = (function(other) {
  if ((other instanceof $c_Ljava_time_Duration)) {
    var x2 = $as_Ljava_time_Duration(other);
    if ((this === x2)) {
      return true
    } else {
      var this$1 = this.Ljava_time_Duration__f_seconds;
      var b = x2.Ljava_time_Duration__f_seconds;
      if (((this$1.RTLong__f_lo === b.RTLong__f_lo) && (this$1.RTLong__f_hi === b.RTLong__f_hi))) {
        return (this.Ljava_time_Duration__f_nanos === x2.Ljava_time_Duration__f_nanos)
      } else {
        return false
      }
    }
  } else {
    return false
  }
});
$c_Ljava_time_Duration.prototype.hashCode__I = (function() {
  var this$2 = this.Ljava_time_Duration__f_seconds;
  var this$1 = this.Ljava_time_Duration__f_seconds;
  var hi = this$1.RTLong__f_hi;
  var lo = (this$2.RTLong__f_lo ^ hi);
  return ((lo + $imul(51, this.Ljava_time_Duration__f_nanos)) | 0)
});
$c_Ljava_time_Duration.prototype.toString__T = (function() {
  if ((this === $m_Ljava_time_Duration$().ZERO__Ljava_time_Duration())) {
    return "PT0S"
  };
  var this$2 = this.Ljava_time_Duration__f_seconds;
  var value = $m_Ljava_time_LocalTime$().SECONDS_PER_HOUR__I();
  var hi = (value >> 31);
  var this$3 = $m_RTLong$();
  var lo = this$3.divideImpl__I__I__I__I__I(this$2.RTLong__f_lo, this$2.RTLong__f_hi, value, hi);
  var hi$1 = this$3.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
  var this$5 = this.Ljava_time_Duration__f_seconds;
  var value$1 = $m_Ljava_time_LocalTime$().SECONDS_PER_HOUR__I();
  var hi$2 = (value$1 >> 31);
  var this$6 = $m_RTLong$();
  var lo$1 = this$6.remainderImpl__I__I__I__I__I(this$5.RTLong__f_lo, this$5.RTLong__f_hi, value$1, hi$2);
  var hi$3 = this$6.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
  var value$2 = $m_Ljava_time_LocalTime$().SECONDS_PER_MINUTE__I();
  var hi$4 = (value$2 >> 31);
  var this$8 = $m_RTLong$();
  var lo$2 = this$8.divideImpl__I__I__I__I__I(lo$1, hi$3, value$2, hi$4);
  var this$10 = this.Ljava_time_Duration__f_seconds;
  var value$3 = $m_Ljava_time_LocalTime$().SECONDS_PER_MINUTE__I();
  var hi$6 = (value$3 >> 31);
  var this$11 = $m_RTLong$();
  var lo$3 = this$11.remainderImpl__I__I__I__I__I(this$10.RTLong__f_lo, this$10.RTLong__f_hi, value$3, hi$6);
  var buf = $ct_scm_StringBuilder__I__(new $c_scm_StringBuilder(), 24);
  buf.append__T__scm_StringBuilder("PT");
  if ((!((lo === 0) && (hi$1 === 0)))) {
    buf.append__J__scm_StringBuilder(new $c_RTLong(lo, hi$1)).append__C__scm_StringBuilder(72)
  };
  if ((lo$2 !== 0)) {
    buf.append__I__scm_StringBuilder(lo$2).append__C__scm_StringBuilder(77)
  };
  if ((((lo$3 === 0) && (this.Ljava_time_Duration__f_nanos === 0)) && (buf.scm_StringBuilder__f_underlying.length__I() > 2))) {
    return buf.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
  };
  if (((lo$3 < 0) && (this.Ljava_time_Duration__f_nanos > 0))) {
    if ((lo$3 === (-1))) {
      buf.append__T__scm_StringBuilder("-0")
    } else {
      buf.append__I__scm_StringBuilder(((1 + lo$3) | 0))
    }
  } else {
    buf.append__I__scm_StringBuilder(lo$3)
  };
  if ((this.Ljava_time_Duration__f_nanos > 0)) {
    var pos = buf.scm_StringBuilder__f_underlying.length__I();
    if ((lo$3 < 0)) {
      buf.append__I__scm_StringBuilder(((2000000000 - this.Ljava_time_Duration__f_nanos) | 0))
    } else {
      buf.append__I__scm_StringBuilder(((1000000000 + this.Ljava_time_Duration__f_nanos) | 0))
    };
    while (true) {
      var index = (((-1) + buf.scm_StringBuilder__f_underlying.length__I()) | 0);
      if ((buf.scm_StringBuilder__f_underlying.charAt__I__C(index) === 48)) {
        var len = (((-1) + buf.scm_StringBuilder__f_underlying.length__I()) | 0);
        buf.scm_StringBuilder__f_underlying.setLength__I__V(len)
      } else {
        break
      }
    };
    buf.setCharAt__I__C__scm_StringBuilder(pos, 46)
  };
  buf.append__C__scm_StringBuilder(83);
  return buf.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
function $as_Ljava_time_Duration(obj) {
  return (((obj instanceof $c_Ljava_time_Duration) || (obj === null)) ? obj : $throwClassCastException(obj, "java.time.Duration"))
}
function $isArrayOf_Ljava_time_Duration(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_time_Duration)))
}
function $asArrayOf_Ljava_time_Duration(obj, depth) {
  return (($isArrayOf_Ljava_time_Duration(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.time.Duration;", depth))
}
var $d_Ljava_time_Duration = new $TypeData().initClass({
  Ljava_time_Duration: 0
}, false, "java.time.Duration", {
  Ljava_time_Duration: 1,
  O: 1,
  Ljava_time_temporal_TemporalAmount: 1,
  s_math_Ordered: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_Duration.prototype.$classData = $d_Ljava_time_Duration;
/** @constructor */
function $c_Ljava_time_chrono_IsoChronology() {
  /*<skip>*/
}
$c_Ljava_time_chrono_IsoChronology.prototype = new $h_O();
$c_Ljava_time_chrono_IsoChronology.prototype.constructor = $c_Ljava_time_chrono_IsoChronology;
/** @constructor */
function $h_Ljava_time_chrono_IsoChronology() {
  /*<skip>*/
}
$h_Ljava_time_chrono_IsoChronology.prototype = $c_Ljava_time_chrono_IsoChronology.prototype;
$c_Ljava_time_chrono_IsoChronology.prototype.equals__O__Z = (function(obj) {
  return $f_Ljava_time_chrono_Chronology__equals__O__Z(this, obj)
});
$c_Ljava_time_chrono_IsoChronology.prototype.hashCode__I = (function() {
  return $f_Ljava_time_chrono_Chronology__hashCode__I(this)
});
$c_Ljava_time_chrono_IsoChronology.prototype.toString__T = (function() {
  return "ISO"
});
$c_Ljava_time_chrono_IsoChronology.prototype.isLeapYear__J__Z = (function(prolepticYear) {
  var lo = (3 & prolepticYear.RTLong__f_lo);
  if ((lo === 0)) {
    var this$1 = $m_RTLong$();
    var lo$1 = this$1.remainderImpl__I__I__I__I__I(prolepticYear.RTLong__f_lo, prolepticYear.RTLong__f_hi, 100, 0);
    var hi = this$1.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
    if ((!((lo$1 === 0) && (hi === 0)))) {
      return true
    } else {
      var this$2 = $m_RTLong$();
      var lo$2 = this$2.remainderImpl__I__I__I__I__I(prolepticYear.RTLong__f_lo, prolepticYear.RTLong__f_hi, 400, 0);
      var hi$1 = this$2.RTLong$__f_org$scalajs$linker$runtime$RuntimeLong$$hiReturn;
      return ((lo$2 === 0) && (hi$1 === 0))
    }
  } else {
    return false
  }
});
var $d_Ljava_time_chrono_IsoChronology = new $TypeData().initClass({
  Ljava_time_chrono_IsoChronology: 0
}, false, "java.time.chrono.IsoChronology", {
  Ljava_time_chrono_IsoChronology: 1,
  O: 1,
  Ljava_time_chrono_Chronology: 1,
  s_math_Ordered: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_chrono_IsoChronology.prototype.$classData = $d_Ljava_time_chrono_IsoChronology;
/** @constructor */
function $c_Ljava_time_temporal_ChronoField(name, ordinal, baseUnit, rangeUnit, _range) {
  this.jl_Enum__f__name = null;
  this.jl_Enum__f__ordinal = 0;
  this.Ljava_time_temporal_ChronoField__f_name = null;
  this.Ljava_time_temporal_ChronoField__f_ordinal = 0;
  this.Ljava_time_temporal_ChronoField__f_baseUnit = null;
  this.Ljava_time_temporal_ChronoField__f_rangeUnit = null;
  this.Ljava_time_temporal_ChronoField__f__range = null;
  this.Ljava_time_temporal_ChronoField__f_name = name;
  this.Ljava_time_temporal_ChronoField__f_ordinal = ordinal;
  this.Ljava_time_temporal_ChronoField__f_baseUnit = baseUnit;
  this.Ljava_time_temporal_ChronoField__f_rangeUnit = rangeUnit;
  this.Ljava_time_temporal_ChronoField__f__range = _range;
  $ct_jl_Enum__T__I__(this, name, ordinal)
}
$c_Ljava_time_temporal_ChronoField.prototype = new $h_jl_Enum();
$c_Ljava_time_temporal_ChronoField.prototype.constructor = $c_Ljava_time_temporal_ChronoField;
/** @constructor */
function $h_Ljava_time_temporal_ChronoField() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ChronoField.prototype = $c_Ljava_time_temporal_ChronoField.prototype;
$c_Ljava_time_temporal_ChronoField.prototype.toString__T = (function() {
  return this.Ljava_time_temporal_ChronoField__f_name
});
var $d_Ljava_time_temporal_ChronoField = new $TypeData().initClass({
  Ljava_time_temporal_ChronoField: 0
}, false, "java.time.temporal.ChronoField", {
  Ljava_time_temporal_ChronoField: 1,
  jl_Enum: 1,
  O: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1,
  Ljava_time_temporal_TemporalField: 1
});
$c_Ljava_time_temporal_ChronoField.prototype.$classData = $d_Ljava_time_temporal_ChronoField;
/** @constructor */
function $c_Ljava_time_temporal_ChronoUnit(name, ordinal, duration) {
  this.jl_Enum__f__name = null;
  this.jl_Enum__f__ordinal = 0;
  this.Ljava_time_temporal_ChronoUnit__f_name = null;
  this.Ljava_time_temporal_ChronoUnit__f_duration = null;
  this.Ljava_time_temporal_ChronoUnit__f_name = name;
  this.Ljava_time_temporal_ChronoUnit__f_duration = duration;
  $ct_jl_Enum__T__I__(this, name, ordinal)
}
$c_Ljava_time_temporal_ChronoUnit.prototype = new $h_jl_Enum();
$c_Ljava_time_temporal_ChronoUnit.prototype.constructor = $c_Ljava_time_temporal_ChronoUnit;
/** @constructor */
function $h_Ljava_time_temporal_ChronoUnit() {
  /*<skip>*/
}
$h_Ljava_time_temporal_ChronoUnit.prototype = $c_Ljava_time_temporal_ChronoUnit.prototype;
$c_Ljava_time_temporal_ChronoUnit.prototype.toString__T = (function() {
  return this.Ljava_time_temporal_ChronoUnit__f_name
});
var $d_Ljava_time_temporal_ChronoUnit = new $TypeData().initClass({
  Ljava_time_temporal_ChronoUnit: 0
}, false, "java.time.temporal.ChronoUnit", {
  Ljava_time_temporal_ChronoUnit: 1,
  jl_Enum: 1,
  O: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1,
  Ljava_time_temporal_TemporalUnit: 1
});
$c_Ljava_time_temporal_ChronoUnit.prototype.$classData = $d_Ljava_time_temporal_ChronoUnit;
function $ct_ju_NoSuchElementException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
function $ct_ju_NoSuchElementException__($thiz) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, null, null, true, true);
  return $thiz
}
class $c_ju_NoSuchElementException extends $c_jl_RuntimeException {
}
var $d_ju_NoSuchElementException = new $TypeData().initClass({
  ju_NoSuchElementException: 0
}, false, "java.util.NoSuchElementException", {
  ju_NoSuchElementException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_ju_NoSuchElementException.prototype.$classData = $d_ju_NoSuchElementException;
class $c_Lorg_scalajs_linker_runtime_UndefinedBehaviorError extends $c_jl_VirtualMachineError {
  constructor(cause) {
    super();
    var message = ((cause === null) ? null : cause.toString__T());
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, message, cause, true, true)
  };
}
var $d_Lorg_scalajs_linker_runtime_UndefinedBehaviorError = new $TypeData().initClass({
  Lorg_scalajs_linker_runtime_UndefinedBehaviorError: 0
}, false, "org.scalajs.linker.runtime.UndefinedBehaviorError", {
  Lorg_scalajs_linker_runtime_UndefinedBehaviorError: 1,
  jl_VirtualMachineError: 1,
  jl_Error: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_Lorg_scalajs_linker_runtime_UndefinedBehaviorError.prototype.$classData = $d_Lorg_scalajs_linker_runtime_UndefinedBehaviorError;
/** @constructor */
function $c_s_$less$colon$less$$anon$1() {
  /*<skip>*/
}
$c_s_$less$colon$less$$anon$1.prototype = new $h_s_$eq$colon$eq();
$c_s_$less$colon$less$$anon$1.prototype.constructor = $c_s_$less$colon$less$$anon$1;
/** @constructor */
function $h_s_$less$colon$less$$anon$1() {
  /*<skip>*/
}
$h_s_$less$colon$less$$anon$1.prototype = $c_s_$less$colon$less$$anon$1.prototype;
$c_s_$less$colon$less$$anon$1.prototype.apply__O__O = (function(x) {
  return x
});
$c_s_$less$colon$less$$anon$1.prototype.toString__T = (function() {
  return "generalized constraint"
});
var $d_s_$less$colon$less$$anon$1 = new $TypeData().initClass({
  s_$less$colon$less$$anon$1: 0
}, false, "scala.$less$colon$less$$anon$1", {
  s_$less$colon$less$$anon$1: 1,
  s_$eq$colon$eq: 1,
  s_$less$colon$less: 1,
  O: 1,
  F1: 1,
  Ljava_io_Serializable: 1
});
$c_s_$less$colon$less$$anon$1.prototype.$classData = $d_s_$less$colon$less$$anon$1;
function $p_s_MatchError__objString$lzycompute__T($thiz) {
  if ((!$thiz.s_MatchError__f_bitmap$0)) {
    $thiz.s_MatchError__f_objString = (($thiz.s_MatchError__f_obj === null) ? "null" : $p_s_MatchError__liftedTree1$1__T($thiz));
    $thiz.s_MatchError__f_bitmap$0 = true
  };
  return $thiz.s_MatchError__f_objString
}
function $p_s_MatchError__objString__T($thiz) {
  return ((!$thiz.s_MatchError__f_bitmap$0) ? $p_s_MatchError__objString$lzycompute__T($thiz) : $thiz.s_MatchError__f_objString)
}
function $p_s_MatchError__ofClass$1__T($thiz) {
  var this$1 = $thiz.s_MatchError__f_obj;
  return ("of class " + $objectClassName(this$1))
}
function $p_s_MatchError__liftedTree1$1__T($thiz) {
  try {
    return ((($dp_toString__T($thiz.s_MatchError__f_obj) + " (") + $p_s_MatchError__ofClass$1__T($thiz)) + ")")
  } catch (e) {
    var e$2 = $m_sjsr_package$().wrapJavaScriptException__O__jl_Throwable(e);
    if ((e$2 !== null)) {
      return ("an instance " + $p_s_MatchError__ofClass$1__T($thiz))
    } else {
      throw e
    }
  }
}
class $c_s_MatchError extends $c_jl_RuntimeException {
  constructor(obj) {
    super();
    this.s_MatchError__f_objString = null;
    this.s_MatchError__f_obj = null;
    this.s_MatchError__f_bitmap$0 = false;
    this.s_MatchError__f_obj = obj;
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, null, null, true, true)
  };
  getMessage__T() {
    return $p_s_MatchError__objString__T(this)
  };
}
var $d_s_MatchError = new $TypeData().initClass({
  s_MatchError: 0
}, false, "scala.MatchError", {
  s_MatchError: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_s_MatchError.prototype.$classData = $d_s_MatchError;
/** @constructor */
function $c_s_Option() {
  /*<skip>*/
}
$c_s_Option.prototype = new $h_O();
$c_s_Option.prototype.constructor = $c_s_Option;
/** @constructor */
function $h_s_Option() {
  /*<skip>*/
}
$h_s_Option.prototype = $c_s_Option.prototype;
$c_s_Option.prototype.isEmpty__Z = (function() {
  return (this === $m_s_None$())
});
$c_s_Option.prototype.knownSize__I = (function() {
  return (this.isEmpty__Z() ? 0 : 1)
});
$c_s_Option.prototype.iterator__sc_Iterator = (function() {
  if (this.isEmpty__Z()) {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty
  } else {
    $m_sc_Iterator$();
    var a = this.get__O();
    return new $c_sc_Iterator$$anon$20(a)
  }
});
/** @constructor */
function $c_T2(_1, _2) {
  this.T2__f__1 = null;
  this.T2__f__2 = null;
  this.T2__f__1 = _1;
  this.T2__f__2 = _2
}
$c_T2.prototype = new $h_O();
$c_T2.prototype.constructor = $c_T2;
/** @constructor */
function $h_T2() {
  /*<skip>*/
}
$h_T2.prototype = $c_T2.prototype;
$c_T2.prototype.productArity__I = (function() {
  return 2
});
$c_T2.prototype.productElement__I__O = (function(n) {
  return $f_s_Product2__productElement__I__O(this, n)
});
$c_T2.prototype.toString__T = (function() {
  return (((("(" + this.T2__f__1) + ",") + this.T2__f__2) + ")")
});
$c_T2.prototype.productPrefix__T = (function() {
  return "Tuple2"
});
$c_T2.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_T2.prototype.hashCode__I = (function() {
  var this$2 = $m_s_util_hashing_MurmurHash3$();
  return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
});
$c_T2.prototype.equals__O__Z = (function(x$1) {
  if ((this === x$1)) {
    return true
  } else if ((x$1 instanceof $c_T2)) {
    var Tuple2$1 = $as_T2(x$1);
    return ($m_sr_BoxesRunTime$().equals__O__O__Z(this.T2__f__1, Tuple2$1.T2__f__1) && $m_sr_BoxesRunTime$().equals__O__O__Z(this.T2__f__2, Tuple2$1.T2__f__2))
  } else {
    return false
  }
});
function $as_T2(obj) {
  return (((obj instanceof $c_T2) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.Tuple2"))
}
function $isArrayOf_T2(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.T2)))
}
function $asArrayOf_T2(obj, depth) {
  return (($isArrayOf_T2(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.Tuple2;", depth))
}
var $d_T2 = new $TypeData().initClass({
  T2: 0
}, false, "scala.Tuple2", {
  T2: 1,
  O: 1,
  s_Product2: 1,
  s_Product: 1,
  s_Equals: 1,
  Ljava_io_Serializable: 1
});
$c_T2.prototype.$classData = $d_T2;
function $f_sc_IndexedSeqOps__drop__I__O($thiz, n) {
  return $thiz.fromSpecific__sc_IterableOnce__O(new $c_sc_IndexedSeqView$Drop($thiz, n))
}
function $f_sc_Iterable__toString__T($thiz) {
  var start = ($thiz.className__T() + "(");
  return $f_sc_IterableOnceOps__mkString__T__T__T__T($thiz, start, ", ", ")")
}
function $is_sc_Iterable(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_Iterable)))
}
function $as_sc_Iterable(obj) {
  return (($is_sc_Iterable(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.Iterable"))
}
function $isArrayOf_sc_Iterable(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_Iterable)))
}
function $asArrayOf_sc_Iterable(obj, depth) {
  return (($isArrayOf_sc_Iterable(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.Iterable;", depth))
}
/** @constructor */
function $c_sc_Iterator$$anon$19() {
  /*<skip>*/
}
$c_sc_Iterator$$anon$19.prototype = new $h_sc_AbstractIterator();
$c_sc_Iterator$$anon$19.prototype.constructor = $c_sc_Iterator$$anon$19;
/** @constructor */
function $h_sc_Iterator$$anon$19() {
  /*<skip>*/
}
$h_sc_Iterator$$anon$19.prototype = $c_sc_Iterator$$anon$19.prototype;
$c_sc_Iterator$$anon$19.prototype.hasNext__Z = (function() {
  return false
});
$c_sc_Iterator$$anon$19.prototype.next__E = (function() {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "next on empty iterator")
});
$c_sc_Iterator$$anon$19.prototype.knownSize__I = (function() {
  return 0
});
$c_sc_Iterator$$anon$19.prototype.next__O = (function() {
  this.next__E()
});
var $d_sc_Iterator$$anon$19 = new $TypeData().initClass({
  sc_Iterator$$anon$19: 0
}, false, "scala.collection.Iterator$$anon$19", {
  sc_Iterator$$anon$19: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sc_Iterator$$anon$19.prototype.$classData = $d_sc_Iterator$$anon$19;
/** @constructor */
function $c_sc_Iterator$$anon$20(a$1) {
  this.sc_Iterator$$anon$20__f_consumed = false;
  this.sc_Iterator$$anon$20__f_a$1 = null;
  this.sc_Iterator$$anon$20__f_a$1 = a$1;
  this.sc_Iterator$$anon$20__f_consumed = false
}
$c_sc_Iterator$$anon$20.prototype = new $h_sc_AbstractIterator();
$c_sc_Iterator$$anon$20.prototype.constructor = $c_sc_Iterator$$anon$20;
/** @constructor */
function $h_sc_Iterator$$anon$20() {
  /*<skip>*/
}
$h_sc_Iterator$$anon$20.prototype = $c_sc_Iterator$$anon$20.prototype;
$c_sc_Iterator$$anon$20.prototype.hasNext__Z = (function() {
  return (!this.sc_Iterator$$anon$20__f_consumed)
});
$c_sc_Iterator$$anon$20.prototype.next__O = (function() {
  if (this.sc_Iterator$$anon$20__f_consumed) {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  } else {
    this.sc_Iterator$$anon$20__f_consumed = true;
    return this.sc_Iterator$$anon$20__f_a$1
  }
});
var $d_sc_Iterator$$anon$20 = new $TypeData().initClass({
  sc_Iterator$$anon$20: 0
}, false, "scala.collection.Iterator$$anon$20", {
  sc_Iterator$$anon$20: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sc_Iterator$$anon$20.prototype.$classData = $d_sc_Iterator$$anon$20;
/** @constructor */
function $c_sc_Iterator$$anon$9(outer, f$2) {
  this.sc_Iterator$$anon$9__f_$outer = null;
  this.sc_Iterator$$anon$9__f_f$2 = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.sc_Iterator$$anon$9__f_$outer = outer
  };
  this.sc_Iterator$$anon$9__f_f$2 = f$2
}
$c_sc_Iterator$$anon$9.prototype = new $h_sc_AbstractIterator();
$c_sc_Iterator$$anon$9.prototype.constructor = $c_sc_Iterator$$anon$9;
/** @constructor */
function $h_sc_Iterator$$anon$9() {
  /*<skip>*/
}
$h_sc_Iterator$$anon$9.prototype = $c_sc_Iterator$$anon$9.prototype;
$c_sc_Iterator$$anon$9.prototype.knownSize__I = (function() {
  return this.sc_Iterator$$anon$9__f_$outer.knownSize__I()
});
$c_sc_Iterator$$anon$9.prototype.hasNext__Z = (function() {
  return this.sc_Iterator$$anon$9__f_$outer.hasNext__Z()
});
$c_sc_Iterator$$anon$9.prototype.next__O = (function() {
  return this.sc_Iterator$$anon$9__f_f$2.apply__O__O(this.sc_Iterator$$anon$9__f_$outer.next__O())
});
var $d_sc_Iterator$$anon$9 = new $TypeData().initClass({
  sc_Iterator$$anon$9: 0
}, false, "scala.collection.Iterator$$anon$9", {
  sc_Iterator$$anon$9: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sc_Iterator$$anon$9.prototype.$classData = $d_sc_Iterator$$anon$9;
/** @constructor */
function $c_sc_LinearSeqIterator(coll) {
  this.sc_LinearSeqIterator__f_coll = null;
  this.sc_LinearSeqIterator__f_these = null;
  this.sc_LinearSeqIterator__f_coll = coll;
  this.sc_LinearSeqIterator__f_these = new $c_sc_LinearSeqIterator$LazyCell(this, new $c_sjsr_AnonFunction0(((this$1) => (() => this$1.sc_LinearSeqIterator__f_coll))(this)))
}
$c_sc_LinearSeqIterator.prototype = new $h_sc_AbstractIterator();
$c_sc_LinearSeqIterator.prototype.constructor = $c_sc_LinearSeqIterator;
/** @constructor */
function $h_sc_LinearSeqIterator() {
  /*<skip>*/
}
$h_sc_LinearSeqIterator.prototype = $c_sc_LinearSeqIterator.prototype;
$c_sc_LinearSeqIterator.prototype.hasNext__Z = (function() {
  var this$1 = this.sc_LinearSeqIterator__f_these.v__sc_LinearSeqOps();
  return (!this$1.isEmpty__Z())
});
$c_sc_LinearSeqIterator.prototype.next__O = (function() {
  if ((!this.hasNext__Z())) {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  } else {
    var cur = this.sc_LinearSeqIterator__f_these.v__sc_LinearSeqOps();
    var result = cur.head__O();
    this.sc_LinearSeqIterator__f_these = new $c_sc_LinearSeqIterator$LazyCell(this, new $c_sjsr_AnonFunction0(((this$1, cur$1) => (() => $as_sc_LinearSeq(cur$1.tail__O())))(this, cur)));
    return result
  }
});
var $d_sc_LinearSeqIterator = new $TypeData().initClass({
  sc_LinearSeqIterator: 0
}, false, "scala.collection.LinearSeqIterator", {
  sc_LinearSeqIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sc_LinearSeqIterator.prototype.$classData = $d_sc_LinearSeqIterator;
function $f_sc_LinearSeqOps__iterator__sc_Iterator($thiz) {
  return (($thiz.knownSize__I() === 0) ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_sc_LinearSeqIterator($thiz))
}
function $f_sc_LinearSeqOps__length__I($thiz) {
  var these = $as_sc_LinearSeq($thiz);
  var len = 0;
  while (true) {
    var this$1 = these;
    if ((!this$1.isEmpty__Z())) {
      len = ((1 + len) | 0);
      these = $as_sc_LinearSeq(these.tail__O())
    } else {
      break
    }
  };
  return len
}
function $f_sc_LinearSeqOps__lengthCompare__I__I($thiz, len) {
  return ((len < 0) ? 1 : $p_sc_LinearSeqOps__loop$1__I__sc_LinearSeq__I__I($thiz, 0, $as_sc_LinearSeq($thiz), len))
}
function $f_sc_LinearSeqOps__apply__I__O($thiz, n) {
  if ((n < 0)) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), ("" + n))
  };
  var skipped = $as_sc_LinearSeq($thiz.drop__I__O(n));
  if (skipped.isEmpty__Z()) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), ("" + n))
  };
  return skipped.head__O()
}
function $f_sc_LinearSeqOps__exists__F1__Z($thiz, p) {
  var these = $as_sc_LinearSeq($thiz);
  while ((!these.isEmpty__Z())) {
    if ($uZ(p.apply__O__O(these.head__O()))) {
      return true
    };
    these = $as_sc_LinearSeq(these.tail__O())
  };
  return false
}
function $f_sc_LinearSeqOps__sameElements__sc_IterableOnce__Z($thiz, that) {
  if ($is_sc_LinearSeq(that)) {
    var x2 = $as_sc_LinearSeq(that);
    return $p_sc_LinearSeqOps__linearSeqEq$1__sc_LinearSeq__sc_LinearSeq__Z($thiz, $as_sc_LinearSeq($thiz), x2)
  } else {
    return $f_sc_SeqOps__sameElements__sc_IterableOnce__Z($thiz, that)
  }
}
function $f_sc_LinearSeqOps__indexWhere__F1__I__I($thiz, p, from) {
  var i = ((from > 0) ? from : 0);
  var these = $as_sc_LinearSeq($thiz.drop__I__O(from));
  while (true) {
    var this$3 = these;
    if ((!this$3.isEmpty__Z())) {
      if ($uZ(p.apply__O__O(these.head__O()))) {
        return i
      };
      i = ((1 + i) | 0);
      these = $as_sc_LinearSeq(these.tail__O())
    } else {
      break
    }
  };
  return (-1)
}
function $p_sc_LinearSeqOps__loop$1__I__sc_LinearSeq__I__I($thiz, i, xs, len$1) {
  while (true) {
    if ((i === len$1)) {
      return (xs.isEmpty__Z() ? 0 : 1)
    } else if (xs.isEmpty__Z()) {
      return (-1)
    } else {
      var temp$i = ((1 + i) | 0);
      var temp$xs = $as_sc_LinearSeq(xs.tail__O());
      i = temp$i;
      xs = temp$xs
    }
  }
}
function $p_sc_LinearSeqOps__linearSeqEq$1__sc_LinearSeq__sc_LinearSeq__Z($thiz, a, b) {
  while (true) {
    if ((a === b)) {
      return true
    } else {
      var this$1 = a;
      if ((!this$1.isEmpty__Z())) {
        var this$2 = b;
        var $$x1 = (!this$2.isEmpty__Z())
      } else {
        var $$x1 = false
      };
      if (($$x1 && $m_sr_BoxesRunTime$().equals__O__O__Z(a.head__O(), b.head__O()))) {
        var temp$a = $as_sc_LinearSeq(a.tail__O());
        var temp$b = $as_sc_LinearSeq(b.tail__O());
        a = temp$a;
        b = temp$b
      } else {
        return (a.isEmpty__Z() && b.isEmpty__Z())
      }
    }
  }
}
function $is_sc_LinearSeqOps(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_LinearSeqOps)))
}
function $as_sc_LinearSeqOps(obj) {
  return (($is_sc_LinearSeqOps(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.LinearSeqOps"))
}
function $isArrayOf_sc_LinearSeqOps(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_LinearSeqOps)))
}
function $asArrayOf_sc_LinearSeqOps(obj, depth) {
  return (($isArrayOf_sc_LinearSeqOps(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.LinearSeqOps;", depth))
}
/** @constructor */
function $c_sc_StrictOptimizedLinearSeqOps$$anon$1(outer) {
  this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current = null;
  this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current = outer
}
$c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype = new $h_sc_AbstractIterator();
$c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype.constructor = $c_sc_StrictOptimizedLinearSeqOps$$anon$1;
/** @constructor */
function $h_sc_StrictOptimizedLinearSeqOps$$anon$1() {
  /*<skip>*/
}
$h_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype = $c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype;
$c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype.hasNext__Z = (function() {
  return (!this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current.isEmpty__Z())
});
$c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype.next__O = (function() {
  var r = this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current.head__O();
  this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current = $as_sc_Iterable(this.sc_StrictOptimizedLinearSeqOps$$anon$1__f_current.tail__O());
  return r
});
var $d_sc_StrictOptimizedLinearSeqOps$$anon$1 = new $TypeData().initClass({
  sc_StrictOptimizedLinearSeqOps$$anon$1: 0
}, false, "scala.collection.StrictOptimizedLinearSeqOps$$anon$1", {
  sc_StrictOptimizedLinearSeqOps$$anon$1: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sc_StrictOptimizedLinearSeqOps$$anon$1.prototype.$classData = $d_sc_StrictOptimizedLinearSeqOps$$anon$1;
function $p_sci_HashMapBuilder__isAliased__Z($thiz) {
  return ($thiz.sci_HashMapBuilder__f_aliased !== null)
}
function $p_sci_HashMapBuilder__insertElement__AI__I__I__AI($thiz, as, ix, elem) {
  if ((ix < 0)) {
    throw $ct_jl_ArrayIndexOutOfBoundsException__(new $c_jl_ArrayIndexOutOfBoundsException())
  };
  if ((ix > as.u.length)) {
    throw $ct_jl_ArrayIndexOutOfBoundsException__(new $c_jl_ArrayIndexOutOfBoundsException())
  };
  var result = new $ac_I(((1 + as.u.length) | 0));
  as.copyTo(0, result, 0, ix);
  result.set(ix, elem);
  var destPos = ((1 + ix) | 0);
  var length = ((as.u.length - ix) | 0);
  as.copyTo(ix, result, destPos, length);
  return result
}
function $p_sci_HashMapBuilder__insertValue__sci_BitmapIndexedMapNode__I__O__I__I__O__V($thiz, bm, bitpos, key, originalHash, keyHash, value) {
  var dataIx = bm.dataIndex__I__I(bitpos);
  var idx = (dataIx << 1);
  var src = bm.sci_BitmapIndexedMapNode__f_content;
  var dst = new $ac_O(((2 + src.u.length) | 0));
  src.copyTo(0, dst, 0, idx);
  dst.set(idx, key);
  dst.set(((1 + idx) | 0), value);
  var destPos = ((2 + idx) | 0);
  var length = ((src.u.length - idx) | 0);
  src.copyTo(idx, dst, destPos, length);
  var dstHashes = $p_sci_HashMapBuilder__insertElement__AI__I__I__AI($thiz, bm.sci_BitmapIndexedMapNode__f_originalHashes, dataIx, originalHash);
  bm.sci_BitmapIndexedMapNode__f_dataMap = (bm.sci_BitmapIndexedMapNode__f_dataMap | bitpos);
  bm.sci_BitmapIndexedMapNode__f_content = dst;
  bm.sci_BitmapIndexedMapNode__f_originalHashes = dstHashes;
  bm.sci_BitmapIndexedMapNode__f_size = ((1 + bm.sci_BitmapIndexedMapNode__f_size) | 0);
  bm.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode = ((bm.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode + keyHash) | 0)
}
function $p_sci_HashMapBuilder__ensureUnaliased__V($thiz) {
  if ($p_sci_HashMapBuilder__isAliased__Z($thiz)) {
    $p_sci_HashMapBuilder__copyElems__V($thiz)
  };
  $thiz.sci_HashMapBuilder__f_aliased = null
}
function $p_sci_HashMapBuilder__copyElems__V($thiz) {
  $thiz.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode = $thiz.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode.copy__sci_BitmapIndexedMapNode()
}
/** @constructor */
function $c_sci_HashMapBuilder() {
  this.sci_HashMapBuilder__f_aliased = null;
  this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode = null;
  this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode = new $c_sci_BitmapIndexedMapNode(0, 0, $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyObjectArray, $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyIntArray, 0, 0)
}
$c_sci_HashMapBuilder.prototype = new $h_O();
$c_sci_HashMapBuilder.prototype.constructor = $c_sci_HashMapBuilder;
/** @constructor */
function $h_sci_HashMapBuilder() {
  /*<skip>*/
}
$h_sci_HashMapBuilder.prototype = $c_sci_HashMapBuilder.prototype;
$c_sci_HashMapBuilder.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sci_HashMapBuilder.prototype.update__sci_MapNode__O__O__I__I__I__V = (function(mapNode, key, value, originalHash, keyHash, shift) {
  if ((mapNode instanceof $c_sci_BitmapIndexedMapNode)) {
    var x2 = $as_sci_BitmapIndexedMapNode(mapNode);
    var mask = $m_sci_Node$().maskFrom__I__I__I(keyHash, shift);
    var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
    if (((x2.sci_BitmapIndexedMapNode__f_dataMap & bitpos) !== 0)) {
      var index = $m_sci_Node$().indexFrom__I__I__I__I(x2.sci_BitmapIndexedMapNode__f_dataMap, mask, bitpos);
      var key0 = x2.getKey__I__O(index);
      var key0UnimprovedHash = x2.getHash__I__I(index);
      if (((key0UnimprovedHash === originalHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(key0, key))) {
        x2.sci_BitmapIndexedMapNode__f_content.set(((1 + (index << 1)) | 0), value)
      } else {
        var value0 = x2.getValue__I__O(index);
        var key0Hash = $m_sc_Hashing$().improve__I__I(key0UnimprovedHash);
        var subNodeNew = x2.mergeTwoKeyValPairs__O__O__I__I__O__O__I__I__I__sci_MapNode(key0, value0, key0UnimprovedHash, key0Hash, key, value, originalHash, keyHash, ((5 + shift) | 0));
        x2.migrateFromInlineToNodeInPlace__I__I__sci_MapNode__sci_BitmapIndexedMapNode(bitpos, key0Hash, subNodeNew)
      }
    } else if (((x2.sci_BitmapIndexedMapNode__f_nodeMap & bitpos) !== 0)) {
      var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(x2.sci_BitmapIndexedMapNode__f_nodeMap, mask, bitpos);
      var subNode = x2.getNode__I__sci_MapNode(index$2);
      var beforeSize = subNode.size__I();
      var beforeHash = subNode.cachedJavaKeySetHashCode__I();
      this.update__sci_MapNode__O__O__I__I__I__V(subNode, key, value, originalHash, keyHash, ((5 + shift) | 0));
      x2.sci_BitmapIndexedMapNode__f_size = ((x2.sci_BitmapIndexedMapNode__f_size + ((subNode.size__I() - beforeSize) | 0)) | 0);
      x2.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode = ((x2.sci_BitmapIndexedMapNode__f_cachedJavaKeySetHashCode + ((subNode.cachedJavaKeySetHashCode__I() - beforeHash) | 0)) | 0)
    } else {
      $p_sci_HashMapBuilder__insertValue__sci_BitmapIndexedMapNode__I__O__I__I__O__V(this, x2, bitpos, key, originalHash, keyHash, value)
    }
  } else if ((mapNode instanceof $c_sci_HashCollisionMapNode)) {
    var x3 = $as_sci_HashCollisionMapNode(mapNode);
    var index$3 = x3.indexOf__O__I(key);
    if ((index$3 < 0)) {
      x3.sci_HashCollisionMapNode__f_content = x3.sci_HashCollisionMapNode__f_content.appended__O__sci_Vector(new $c_T2(key, value))
    } else {
      x3.sci_HashCollisionMapNode__f_content = x3.sci_HashCollisionMapNode__f_content.updated__I__O__sci_Vector(index$3, new $c_T2(key, value))
    }
  } else {
    throw new $c_s_MatchError(mapNode)
  }
});
$c_sci_HashMapBuilder.prototype.result__sci_HashMap = (function() {
  if ((this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode.sci_BitmapIndexedMapNode__f_size === 0)) {
    var this$1 = $m_sci_HashMap$();
    return this$1.sci_HashMap$__f_EmptyMap
  } else if ((this.sci_HashMapBuilder__f_aliased !== null)) {
    return this.sci_HashMapBuilder__f_aliased
  } else {
    this.sci_HashMapBuilder__f_aliased = new $c_sci_HashMap(this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode);
    return this.sci_HashMapBuilder__f_aliased
  }
});
$c_sci_HashMapBuilder.prototype.addOne__T2__sci_HashMapBuilder = (function(elem) {
  $p_sci_HashMapBuilder__ensureUnaliased__V(this);
  var x = elem.T2__f__1;
  var h = $m_sr_Statics$().anyHash__O__I(x);
  var im = $m_sc_Hashing$().improve__I__I(h);
  this.update__sci_MapNode__O__O__I__I__I__V(this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode, elem.T2__f__1, elem.T2__f__2, h, im, 0);
  return this
});
$c_sci_HashMapBuilder.prototype.addOne__O__O__sci_HashMapBuilder = (function(key, value) {
  $p_sci_HashMapBuilder__ensureUnaliased__V(this);
  var originalHash = $m_sr_Statics$().anyHash__O__I(key);
  this.update__sci_MapNode__O__O__I__I__I__V(this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode, key, value, originalHash, $m_sc_Hashing$().improve__I__I(originalHash), 0);
  return this
});
$c_sci_HashMapBuilder.prototype.addAll__sc_IterableOnce__sci_HashMapBuilder = (function(xs) {
  $p_sci_HashMapBuilder__ensureUnaliased__V(this);
  if ((xs instanceof $c_sci_HashMap)) {
    var x2 = $as_sci_HashMap(xs);
    new $c_sci_HashMapBuilder$$anon$1(this, x2)
  } else if ((xs instanceof $c_scm_HashMap)) {
    var x3 = $as_scm_HashMap(xs);
    var iter = x3.nodeIterator__sc_Iterator();
    while (iter.hasNext__Z()) {
      var next = $as_scm_HashMap$Node(iter.next__O());
      var improvedHash = next.scm_HashMap$Node__f__hash;
      var originalHash = (improvedHash ^ ((improvedHash >>> 16) | 0));
      var hash = $m_sc_Hashing$().improve__I__I(originalHash);
      this.update__sci_MapNode__O__O__I__I__I__V(this.sci_HashMapBuilder__f_scala$collection$immutable$HashMapBuilder$$rootNode, next.scm_HashMap$Node__f__key, next.scm_HashMap$Node__f__value, originalHash, hash, 0)
    }
  } else if ($is_sci_Map(xs)) {
    var x4 = $as_sci_Map(xs);
    x4.foreachEntry__F2__V(new $c_sjsr_AnonFunction2(((this$1) => ((key$2, value$2) => this$1.addOne__O__O__sci_HashMapBuilder(key$2, value$2)))(this)))
  } else {
    var it = xs.iterator__sc_Iterator();
    while (it.hasNext__Z()) {
      this.addOne__T2__sci_HashMapBuilder($as_T2(it.next__O()))
    }
  };
  return this
});
$c_sci_HashMapBuilder.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__sci_HashMapBuilder(xs)
});
$c_sci_HashMapBuilder.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__T2__sci_HashMapBuilder($as_T2(elem))
});
$c_sci_HashMapBuilder.prototype.result__O = (function() {
  return this.result__sci_HashMap()
});
var $d_sci_HashMapBuilder = new $TypeData().initClass({
  sci_HashMapBuilder: 0
}, false, "scala.collection.immutable.HashMapBuilder", {
  sci_HashMapBuilder: 1,
  O: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_sci_HashMapBuilder.prototype.$classData = $d_sci_HashMapBuilder;
function $p_sci_HashSetBuilder__isAliased__Z($thiz) {
  return ($thiz.sci_HashSetBuilder__f_aliased !== null)
}
function $p_sci_HashSetBuilder__insertElement__AI__I__I__AI($thiz, as, ix, elem) {
  if ((ix < 0)) {
    throw $ct_jl_ArrayIndexOutOfBoundsException__(new $c_jl_ArrayIndexOutOfBoundsException())
  };
  if ((ix > as.u.length)) {
    throw $ct_jl_ArrayIndexOutOfBoundsException__(new $c_jl_ArrayIndexOutOfBoundsException())
  };
  var result = new $ac_I(((1 + as.u.length) | 0));
  as.copyTo(0, result, 0, ix);
  result.set(ix, elem);
  var destPos = ((1 + ix) | 0);
  var length = ((as.u.length - ix) | 0);
  as.copyTo(ix, result, destPos, length);
  return result
}
function $p_sci_HashSetBuilder__insertValue__sci_BitmapIndexedSetNode__I__O__I__I__V($thiz, bm, bitpos, key, originalHash, keyHash) {
  var dataIx = bm.dataIndex__I__I(bitpos);
  var src = bm.sci_BitmapIndexedSetNode__f_content;
  var dst = new $ac_O(((1 + src.u.length) | 0));
  src.copyTo(0, dst, 0, dataIx);
  dst.set(dataIx, key);
  var destPos = ((1 + dataIx) | 0);
  var length = ((src.u.length - dataIx) | 0);
  src.copyTo(dataIx, dst, destPos, length);
  var dstHashes = $p_sci_HashSetBuilder__insertElement__AI__I__I__AI($thiz, bm.sci_BitmapIndexedSetNode__f_originalHashes, dataIx, originalHash);
  bm.sci_BitmapIndexedSetNode__f_dataMap = (bm.sci_BitmapIndexedSetNode__f_dataMap | bitpos);
  bm.sci_BitmapIndexedSetNode__f_content = dst;
  bm.sci_BitmapIndexedSetNode__f_originalHashes = dstHashes;
  bm.sci_BitmapIndexedSetNode__f_size = ((1 + bm.sci_BitmapIndexedSetNode__f_size) | 0);
  bm.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode = ((bm.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode + keyHash) | 0)
}
function $p_sci_HashSetBuilder__setValue__sci_BitmapIndexedSetNode__I__O__V($thiz, bm, bitpos, elem) {
  var dataIx = bm.dataIndex__I__I(bitpos);
  bm.sci_BitmapIndexedSetNode__f_content.set(dataIx, elem)
}
function $p_sci_HashSetBuilder__ensureUnaliased__V($thiz) {
  if ($p_sci_HashSetBuilder__isAliased__Z($thiz)) {
    $p_sci_HashSetBuilder__copyElems__V($thiz)
  };
  $thiz.sci_HashSetBuilder__f_aliased = null
}
function $p_sci_HashSetBuilder__copyElems__V($thiz) {
  $thiz.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode = $thiz.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode.copy__sci_BitmapIndexedSetNode()
}
/** @constructor */
function $c_sci_HashSetBuilder() {
  this.sci_HashSetBuilder__f_aliased = null;
  this.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode = null;
  this.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode = new $c_sci_BitmapIndexedSetNode(0, 0, $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyObjectArray, $m_s_Array$EmptyArrays$().s_Array$EmptyArrays$__f_emptyIntArray, 0, 0)
}
$c_sci_HashSetBuilder.prototype = new $h_O();
$c_sci_HashSetBuilder.prototype.constructor = $c_sci_HashSetBuilder;
/** @constructor */
function $h_sci_HashSetBuilder() {
  /*<skip>*/
}
$h_sci_HashSetBuilder.prototype = $c_sci_HashSetBuilder.prototype;
$c_sci_HashSetBuilder.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sci_HashSetBuilder.prototype.update__sci_SetNode__O__I__I__I__V = (function(setNode, element, originalHash, elementHash, shift) {
  if ((setNode instanceof $c_sci_BitmapIndexedSetNode)) {
    var x2 = $as_sci_BitmapIndexedSetNode(setNode);
    var mask = $m_sci_Node$().maskFrom__I__I__I(elementHash, shift);
    var bitpos = $m_sci_Node$().bitposFrom__I__I(mask);
    if (((x2.sci_BitmapIndexedSetNode__f_dataMap & bitpos) !== 0)) {
      var index = $m_sci_Node$().indexFrom__I__I__I__I(x2.sci_BitmapIndexedSetNode__f_dataMap, mask, bitpos);
      var element0 = x2.getPayload__I__O(index);
      var element0UnimprovedHash = x2.getHash__I__I(index);
      if (((element0UnimprovedHash === originalHash) && $m_sr_BoxesRunTime$().equals__O__O__Z(element0, element))) {
        $p_sci_HashSetBuilder__setValue__sci_BitmapIndexedSetNode__I__O__V(this, x2, bitpos, element0)
      } else {
        var element0Hash = $m_sc_Hashing$().improve__I__I(element0UnimprovedHash);
        var subNodeNew = x2.mergeTwoKeyValPairs__O__I__I__O__I__I__I__sci_SetNode(element0, element0UnimprovedHash, element0Hash, element, originalHash, elementHash, ((5 + shift) | 0));
        x2.migrateFromInlineToNodeInPlace__I__I__sci_SetNode__sci_BitmapIndexedSetNode(bitpos, element0Hash, subNodeNew)
      }
    } else if (((x2.sci_BitmapIndexedSetNode__f_nodeMap & bitpos) !== 0)) {
      var index$2 = $m_sci_Node$().indexFrom__I__I__I__I(x2.sci_BitmapIndexedSetNode__f_nodeMap, mask, bitpos);
      var subNode = x2.getNode__I__sci_SetNode(index$2);
      var beforeSize = subNode.size__I();
      var beforeHashCode = subNode.cachedJavaKeySetHashCode__I();
      this.update__sci_SetNode__O__I__I__I__V(subNode, element, originalHash, elementHash, ((5 + shift) | 0));
      x2.sci_BitmapIndexedSetNode__f_size = ((x2.sci_BitmapIndexedSetNode__f_size + ((subNode.size__I() - beforeSize) | 0)) | 0);
      x2.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode = ((x2.sci_BitmapIndexedSetNode__f_cachedJavaKeySetHashCode + ((subNode.cachedJavaKeySetHashCode__I() - beforeHashCode) | 0)) | 0)
    } else {
      $p_sci_HashSetBuilder__insertValue__sci_BitmapIndexedSetNode__I__O__I__I__V(this, x2, bitpos, element, originalHash, elementHash)
    }
  } else if ((setNode instanceof $c_sci_HashCollisionSetNode)) {
    var x3 = $as_sci_HashCollisionSetNode(setNode);
    var this$1 = x3.sci_HashCollisionSetNode__f_content;
    var index$3 = $f_sc_SeqOps__indexOf__O__I__I(this$1, element, 0);
    if ((index$3 < 0)) {
      x3.sci_HashCollisionSetNode__f_content = x3.sci_HashCollisionSetNode__f_content.appended__O__sci_Vector(element)
    } else {
      x3.sci_HashCollisionSetNode__f_content = x3.sci_HashCollisionSetNode__f_content.updated__I__O__sci_Vector(index$3, element)
    }
  } else {
    throw new $c_s_MatchError(setNode)
  }
});
$c_sci_HashSetBuilder.prototype.result__sci_HashSet = (function() {
  if ((this.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode.sci_BitmapIndexedSetNode__f_size === 0)) {
    var this$1 = $m_sci_HashSet$();
    return this$1.sci_HashSet$__f_EmptySet
  } else if ((this.sci_HashSetBuilder__f_aliased !== null)) {
    return this.sci_HashSetBuilder__f_aliased
  } else {
    this.sci_HashSetBuilder__f_aliased = new $c_sci_HashSet(this.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode);
    return this.sci_HashSetBuilder__f_aliased
  }
});
$c_sci_HashSetBuilder.prototype.addOne__O__sci_HashSetBuilder = (function(elem) {
  $p_sci_HashSetBuilder__ensureUnaliased__V(this);
  var h = $m_sr_Statics$().anyHash__O__I(elem);
  var im = $m_sc_Hashing$().improve__I__I(h);
  this.update__sci_SetNode__O__I__I__I__V(this.sci_HashSetBuilder__f_scala$collection$immutable$HashSetBuilder$$rootNode, elem, h, im, 0);
  return this
});
$c_sci_HashSetBuilder.prototype.addAll__sc_IterableOnce__sci_HashSetBuilder = (function(xs) {
  $p_sci_HashSetBuilder__ensureUnaliased__V(this);
  if ((xs instanceof $c_sci_HashSet)) {
    var x2 = $as_sci_HashSet(xs);
    new $c_sci_HashSetBuilder$$anon$1(this, x2)
  } else {
    var it = xs.iterator__sc_Iterator();
    while (it.hasNext__Z()) {
      this.addOne__O__sci_HashSetBuilder(it.next__O())
    }
  };
  return this
});
$c_sci_HashSetBuilder.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__sci_HashSetBuilder(xs)
});
$c_sci_HashSetBuilder.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__O__sci_HashSetBuilder(elem)
});
$c_sci_HashSetBuilder.prototype.result__O = (function() {
  return this.result__sci_HashSet()
});
var $d_sci_HashSetBuilder = new $TypeData().initClass({
  sci_HashSetBuilder: 0
}, false, "scala.collection.immutable.HashSetBuilder", {
  sci_HashSetBuilder: 1,
  O: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_sci_HashSetBuilder.prototype.$classData = $d_sci_HashSetBuilder;
/** @constructor */
function $c_sci_IndexedSeq$() {
  this.sc_SeqFactory$Delegate__f_delegate = null;
  $ct_sc_SeqFactory$Delegate__sc_SeqFactory__(this, $m_sci_Vector$())
}
$c_sci_IndexedSeq$.prototype = new $h_sc_SeqFactory$Delegate();
$c_sci_IndexedSeq$.prototype.constructor = $c_sci_IndexedSeq$;
/** @constructor */
function $h_sci_IndexedSeq$() {
  /*<skip>*/
}
$h_sci_IndexedSeq$.prototype = $c_sci_IndexedSeq$.prototype;
$c_sci_IndexedSeq$.prototype.from__sc_IterableOnce__sci_IndexedSeq = (function(it) {
  if ($is_sci_IndexedSeq(it)) {
    var x2 = $as_sci_IndexedSeq(it);
    return x2
  } else {
    return $as_sci_IndexedSeq($c_sc_SeqFactory$Delegate.prototype.from__sc_IterableOnce__sc_SeqOps.call(this, it))
  }
});
$c_sci_IndexedSeq$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_IndexedSeq(source)
});
$c_sci_IndexedSeq$.prototype.from__sc_IterableOnce__sc_SeqOps = (function(it) {
  return this.from__sc_IterableOnce__sci_IndexedSeq(it)
});
var $d_sci_IndexedSeq$ = new $TypeData().initClass({
  sci_IndexedSeq$: 0
}, false, "scala.collection.immutable.IndexedSeq$", {
  sci_IndexedSeq$: 1,
  sc_SeqFactory$Delegate: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_IndexedSeq$.prototype.$classData = $d_sci_IndexedSeq$;
var $n_sci_IndexedSeq$;
function $m_sci_IndexedSeq$() {
  if ((!$n_sci_IndexedSeq$)) {
    $n_sci_IndexedSeq$ = new $c_sci_IndexedSeq$()
  };
  return $n_sci_IndexedSeq$
}
/** @constructor */
function $c_sci_LazyList$LazyIterator(lazyList) {
  this.sci_LazyList$LazyIterator__f_lazyList = null;
  this.sci_LazyList$LazyIterator__f_lazyList = lazyList
}
$c_sci_LazyList$LazyIterator.prototype = new $h_sc_AbstractIterator();
$c_sci_LazyList$LazyIterator.prototype.constructor = $c_sci_LazyList$LazyIterator;
/** @constructor */
function $h_sci_LazyList$LazyIterator() {
  /*<skip>*/
}
$h_sci_LazyList$LazyIterator.prototype = $c_sci_LazyList$LazyIterator.prototype;
$c_sci_LazyList$LazyIterator.prototype.hasNext__Z = (function() {
  return (!this.sci_LazyList$LazyIterator__f_lazyList.isEmpty__Z())
});
$c_sci_LazyList$LazyIterator.prototype.next__O = (function() {
  if (this.sci_LazyList$LazyIterator__f_lazyList.isEmpty__Z()) {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  } else {
    var this$1 = this.sci_LazyList$LazyIterator__f_lazyList;
    var res = this$1.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
    var this$2 = this.sci_LazyList$LazyIterator__f_lazyList;
    this.sci_LazyList$LazyIterator__f_lazyList = this$2.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
    return res
  }
});
var $d_sci_LazyList$LazyIterator = new $TypeData().initClass({
  sci_LazyList$LazyIterator: 0
}, false, "scala.collection.immutable.LazyList$LazyIterator", {
  sci_LazyList$LazyIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_LazyList$LazyIterator.prototype.$classData = $d_sci_LazyList$LazyIterator;
/** @constructor */
function $c_sci_List$() {
  this.sci_List$__f_scala$collection$immutable$List$$TupleOfNil = null;
  this.sci_List$__f_partialNotApplied = null;
  $n_sci_List$ = this;
  this.sci_List$__f_scala$collection$immutable$List$$TupleOfNil = new $c_T2($m_sci_Nil$(), $m_sci_Nil$());
  this.sci_List$__f_partialNotApplied = new $c_sci_List$$anon$1()
}
$c_sci_List$.prototype = new $h_O();
$c_sci_List$.prototype.constructor = $c_sci_List$;
/** @constructor */
function $h_sci_List$() {
  /*<skip>*/
}
$h_sci_List$.prototype = $c_sci_List$.prototype;
$c_sci_List$.prototype.from__sc_IterableOnce__O = (function(source) {
  return $m_sci_Nil$().prependedAll__sc_IterableOnce__sci_List(source)
});
var $d_sci_List$ = new $TypeData().initClass({
  sci_List$: 0
}, false, "scala.collection.immutable.List$", {
  sci_List$: 1,
  O: 1,
  sc_StrictOptimizedSeqFactory: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_List$.prototype.$classData = $d_sci_List$;
var $n_sci_List$;
function $m_sci_List$() {
  if ((!$n_sci_List$)) {
    $n_sci_List$ = new $c_sci_List$()
  };
  return $n_sci_List$
}
function $ct_sci_Map$Map2$Map2Iterator__sci_Map$Map2__($thiz, outer) {
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    $thiz.sci_Map$Map2$Map2Iterator__f_$outer = outer
  };
  $thiz.sci_Map$Map2$Map2Iterator__f_i = 0;
  return $thiz
}
/** @constructor */
function $c_sci_Map$Map2$Map2Iterator() {
  this.sci_Map$Map2$Map2Iterator__f_i = 0;
  this.sci_Map$Map2$Map2Iterator__f_$outer = null
}
$c_sci_Map$Map2$Map2Iterator.prototype = new $h_sc_AbstractIterator();
$c_sci_Map$Map2$Map2Iterator.prototype.constructor = $c_sci_Map$Map2$Map2Iterator;
/** @constructor */
function $h_sci_Map$Map2$Map2Iterator() {
  /*<skip>*/
}
$h_sci_Map$Map2$Map2Iterator.prototype = $c_sci_Map$Map2$Map2Iterator.prototype;
$c_sci_Map$Map2$Map2Iterator.prototype.hasNext__Z = (function() {
  return (this.sci_Map$Map2$Map2Iterator__f_i < 2)
});
$c_sci_Map$Map2$Map2Iterator.prototype.next__O = (function() {
  var x1 = this.sci_Map$Map2$Map2Iterator__f_i;
  switch (x1) {
    case 0: {
      var k = this.sci_Map$Map2$Map2Iterator__f_$outer.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1;
      var v = this.sci_Map$Map2$Map2Iterator__f_$outer.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1;
      var result = new $c_T2(k, v);
      break
    }
    case 1: {
      var k$1 = this.sci_Map$Map2$Map2Iterator__f_$outer.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2;
      var v$1 = this.sci_Map$Map2$Map2Iterator__f_$outer.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2;
      var result = new $c_T2(k$1, v$1);
      break
    }
    default: {
      var result = $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
    }
  };
  this.sci_Map$Map2$Map2Iterator__f_i = ((1 + this.sci_Map$Map2$Map2Iterator__f_i) | 0);
  return result
});
$c_sci_Map$Map2$Map2Iterator.prototype.drop__I__sc_Iterator = (function(n) {
  this.sci_Map$Map2$Map2Iterator__f_i = ((this.sci_Map$Map2$Map2Iterator__f_i + n) | 0);
  return this
});
function $ct_sci_Map$Map3$Map3Iterator__sci_Map$Map3__($thiz, outer) {
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    $thiz.sci_Map$Map3$Map3Iterator__f_$outer = outer
  };
  $thiz.sci_Map$Map3$Map3Iterator__f_i = 0;
  return $thiz
}
/** @constructor */
function $c_sci_Map$Map3$Map3Iterator() {
  this.sci_Map$Map3$Map3Iterator__f_i = 0;
  this.sci_Map$Map3$Map3Iterator__f_$outer = null
}
$c_sci_Map$Map3$Map3Iterator.prototype = new $h_sc_AbstractIterator();
$c_sci_Map$Map3$Map3Iterator.prototype.constructor = $c_sci_Map$Map3$Map3Iterator;
/** @constructor */
function $h_sci_Map$Map3$Map3Iterator() {
  /*<skip>*/
}
$h_sci_Map$Map3$Map3Iterator.prototype = $c_sci_Map$Map3$Map3Iterator.prototype;
$c_sci_Map$Map3$Map3Iterator.prototype.hasNext__Z = (function() {
  return (this.sci_Map$Map3$Map3Iterator__f_i < 3)
});
$c_sci_Map$Map3$Map3Iterator.prototype.next__O = (function() {
  var x1 = this.sci_Map$Map3$Map3Iterator__f_i;
  switch (x1) {
    case 0: {
      var k = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1;
      var v = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1;
      var result = new $c_T2(k, v);
      break
    }
    case 1: {
      var k$1 = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2;
      var v$1 = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2;
      var result = new $c_T2(k$1, v$1);
      break
    }
    case 2: {
      var k$2 = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3;
      var v$2 = this.sci_Map$Map3$Map3Iterator__f_$outer.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3;
      var result = new $c_T2(k$2, v$2);
      break
    }
    default: {
      var result = $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
    }
  };
  this.sci_Map$Map3$Map3Iterator__f_i = ((1 + this.sci_Map$Map3$Map3Iterator__f_i) | 0);
  return result
});
$c_sci_Map$Map3$Map3Iterator.prototype.drop__I__sc_Iterator = (function(n) {
  this.sci_Map$Map3$Map3Iterator__f_i = ((this.sci_Map$Map3$Map3Iterator__f_i + n) | 0);
  return this
});
function $ct_sci_Map$Map4$Map4Iterator__sci_Map$Map4__($thiz, outer) {
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    $thiz.sci_Map$Map4$Map4Iterator__f_$outer = outer
  };
  $thiz.sci_Map$Map4$Map4Iterator__f_i = 0;
  return $thiz
}
/** @constructor */
function $c_sci_Map$Map4$Map4Iterator() {
  this.sci_Map$Map4$Map4Iterator__f_i = 0;
  this.sci_Map$Map4$Map4Iterator__f_$outer = null
}
$c_sci_Map$Map4$Map4Iterator.prototype = new $h_sc_AbstractIterator();
$c_sci_Map$Map4$Map4Iterator.prototype.constructor = $c_sci_Map$Map4$Map4Iterator;
/** @constructor */
function $h_sci_Map$Map4$Map4Iterator() {
  /*<skip>*/
}
$h_sci_Map$Map4$Map4Iterator.prototype = $c_sci_Map$Map4$Map4Iterator.prototype;
$c_sci_Map$Map4$Map4Iterator.prototype.hasNext__Z = (function() {
  return (this.sci_Map$Map4$Map4Iterator__f_i < 4)
});
$c_sci_Map$Map4$Map4Iterator.prototype.next__O = (function() {
  var x1 = this.sci_Map$Map4$Map4Iterator__f_i;
  switch (x1) {
    case 0: {
      var k = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1;
      var v = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1;
      var result = new $c_T2(k, v);
      break
    }
    case 1: {
      var k$1 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2;
      var v$1 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2;
      var result = new $c_T2(k$1, v$1);
      break
    }
    case 2: {
      var k$2 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3;
      var v$2 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3;
      var result = new $c_T2(k$2, v$2);
      break
    }
    case 3: {
      var k$3 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4;
      var v$3 = this.sci_Map$Map4$Map4Iterator__f_$outer.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4;
      var result = new $c_T2(k$3, v$3);
      break
    }
    default: {
      var result = $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
    }
  };
  this.sci_Map$Map4$Map4Iterator__f_i = ((1 + this.sci_Map$Map4$Map4Iterator__f_i) | 0);
  return result
});
$c_sci_Map$Map4$Map4Iterator.prototype.drop__I__sc_Iterator = (function(n) {
  this.sci_Map$Map4$Map4Iterator__f_i = ((this.sci_Map$Map4$Map4Iterator__f_i + n) | 0);
  return this
});
/** @constructor */
function $c_sci_MapBuilderImpl() {
  this.sci_MapBuilderImpl__f_elems = null;
  this.sci_MapBuilderImpl__f_switchedToHashMapBuilder = false;
  this.sci_MapBuilderImpl__f_hashMapBuilder = null;
  this.sci_MapBuilderImpl__f_elems = $m_sci_Map$EmptyMap$();
  this.sci_MapBuilderImpl__f_switchedToHashMapBuilder = false
}
$c_sci_MapBuilderImpl.prototype = new $h_O();
$c_sci_MapBuilderImpl.prototype.constructor = $c_sci_MapBuilderImpl;
/** @constructor */
function $h_sci_MapBuilderImpl() {
  /*<skip>*/
}
$h_sci_MapBuilderImpl.prototype = $c_sci_MapBuilderImpl.prototype;
$c_sci_MapBuilderImpl.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sci_MapBuilderImpl.prototype.result__sci_Map = (function() {
  return (this.sci_MapBuilderImpl__f_switchedToHashMapBuilder ? this.sci_MapBuilderImpl__f_hashMapBuilder.result__sci_HashMap() : this.sci_MapBuilderImpl__f_elems)
});
$c_sci_MapBuilderImpl.prototype.addOne__O__O__sci_MapBuilderImpl = (function(key, value) {
  if (this.sci_MapBuilderImpl__f_switchedToHashMapBuilder) {
    this.sci_MapBuilderImpl__f_hashMapBuilder.addOne__O__O__sci_HashMapBuilder(key, value)
  } else if ((this.sci_MapBuilderImpl__f_elems.size__I() < 4)) {
    this.sci_MapBuilderImpl__f_elems = $as_sci_Map(this.sci_MapBuilderImpl__f_elems.updated__O__O__sci_MapOps(key, value))
  } else if (this.sci_MapBuilderImpl__f_elems.contains__O__Z(key)) {
    this.sci_MapBuilderImpl__f_elems = $as_sci_Map(this.sci_MapBuilderImpl__f_elems.updated__O__O__sci_MapOps(key, value))
  } else {
    this.sci_MapBuilderImpl__f_switchedToHashMapBuilder = true;
    if ((this.sci_MapBuilderImpl__f_hashMapBuilder === null)) {
      this.sci_MapBuilderImpl__f_hashMapBuilder = new $c_sci_HashMapBuilder()
    };
    $as_sci_Map$Map4(this.sci_MapBuilderImpl__f_elems).buildTo__sci_HashMapBuilder__sci_HashMapBuilder(this.sci_MapBuilderImpl__f_hashMapBuilder);
    this.sci_MapBuilderImpl__f_hashMapBuilder.addOne__O__O__sci_HashMapBuilder(key, value)
  };
  return this
});
$c_sci_MapBuilderImpl.prototype.addAll__sc_IterableOnce__sci_MapBuilderImpl = (function(xs) {
  return (this.sci_MapBuilderImpl__f_switchedToHashMapBuilder ? (this.sci_MapBuilderImpl__f_hashMapBuilder.addAll__sc_IterableOnce__sci_HashMapBuilder(xs), this) : $as_sci_MapBuilderImpl($f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs)))
});
$c_sci_MapBuilderImpl.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__sci_MapBuilderImpl(xs)
});
$c_sci_MapBuilderImpl.prototype.addOne__O__scm_Growable = (function(elem) {
  var elem$1 = $as_T2(elem);
  return this.addOne__O__O__sci_MapBuilderImpl(elem$1.T2__f__1, elem$1.T2__f__2)
});
$c_sci_MapBuilderImpl.prototype.result__O = (function() {
  return this.result__sci_Map()
});
function $as_sci_MapBuilderImpl(obj) {
  return (((obj instanceof $c_sci_MapBuilderImpl) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.MapBuilderImpl"))
}
function $isArrayOf_sci_MapBuilderImpl(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_MapBuilderImpl)))
}
function $asArrayOf_sci_MapBuilderImpl(obj, depth) {
  return (($isArrayOf_sci_MapBuilderImpl(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.MapBuilderImpl;", depth))
}
var $d_sci_MapBuilderImpl = new $TypeData().initClass({
  sci_MapBuilderImpl: 0
}, false, "scala.collection.immutable.MapBuilderImpl", {
  sci_MapBuilderImpl: 1,
  O: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_sci_MapBuilderImpl.prototype.$classData = $d_sci_MapBuilderImpl;
/** @constructor */
function $c_sci_MapKeyValueTupleHashIterator(rootNode) {
  this.sci_ChampBaseReverseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseReverseIterator__f_currentValueNode = null;
  this.sci_ChampBaseReverseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseReverseIterator__f_nodeIndex = null;
  this.sci_ChampBaseReverseIterator__f_nodeStack = null;
  this.sci_MapKeyValueTupleHashIterator__f_hash = 0;
  this.sci_MapKeyValueTupleHashIterator__f_value = null;
  $ct_sci_ChampBaseReverseIterator__sci_Node__(this, rootNode);
  this.sci_MapKeyValueTupleHashIterator__f_hash = 0
}
$c_sci_MapKeyValueTupleHashIterator.prototype = new $h_sci_ChampBaseReverseIterator();
$c_sci_MapKeyValueTupleHashIterator.prototype.constructor = $c_sci_MapKeyValueTupleHashIterator;
/** @constructor */
function $h_sci_MapKeyValueTupleHashIterator() {
  /*<skip>*/
}
$h_sci_MapKeyValueTupleHashIterator.prototype = $c_sci_MapKeyValueTupleHashIterator.prototype;
$c_sci_MapKeyValueTupleHashIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sci_MapKeyValueTupleHashIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sci_MapKeyValueTupleHashIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sci_MapKeyValueTupleHashIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sci_MapKeyValueTupleHashIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sci_MapKeyValueTupleHashIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sci_MapKeyValueTupleHashIterator.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sci_MapKeyValueTupleHashIterator.prototype.hashCode__I = (function() {
  var $$x2 = $m_s_util_hashing_MurmurHash3$();
  var $$x1 = this.sci_MapKeyValueTupleHashIterator__f_hash;
  var x = this.sci_MapKeyValueTupleHashIterator__f_value;
  return $$x2.tuple2Hash__I__I__I__I($$x1, $m_sr_Statics$().anyHash__O__I(x), (-889275714))
});
$c_sci_MapKeyValueTupleHashIterator.prototype.next__sci_MapKeyValueTupleHashIterator = (function() {
  if ((!this.hasNext__Z())) {
    throw $ct_ju_NoSuchElementException__(new $c_ju_NoSuchElementException())
  };
  this.sci_MapKeyValueTupleHashIterator__f_hash = this.sci_ChampBaseReverseIterator__f_currentValueNode.getHash__I__I(this.sci_ChampBaseReverseIterator__f_currentValueCursor);
  this.sci_MapKeyValueTupleHashIterator__f_value = $as_sci_MapNode(this.sci_ChampBaseReverseIterator__f_currentValueNode).getValue__I__O(this.sci_ChampBaseReverseIterator__f_currentValueCursor);
  this.sci_ChampBaseReverseIterator__f_currentValueCursor = (((-1) + this.sci_ChampBaseReverseIterator__f_currentValueCursor) | 0);
  return this
});
$c_sci_MapKeyValueTupleHashIterator.prototype.next__O = (function() {
  return this.next__sci_MapKeyValueTupleHashIterator()
});
var $d_sci_MapKeyValueTupleHashIterator = new $TypeData().initClass({
  sci_MapKeyValueTupleHashIterator: 0
}, false, "scala.collection.immutable.MapKeyValueTupleHashIterator", {
  sci_MapKeyValueTupleHashIterator: 1,
  sci_ChampBaseReverseIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_MapKeyValueTupleHashIterator.prototype.$classData = $d_sci_MapKeyValueTupleHashIterator;
/** @constructor */
function $c_sci_MapKeyValueTupleIterator(rootNode) {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null;
  $ct_sci_ChampBaseIterator__sci_Node__(this, rootNode)
}
$c_sci_MapKeyValueTupleIterator.prototype = new $h_sci_ChampBaseIterator();
$c_sci_MapKeyValueTupleIterator.prototype.constructor = $c_sci_MapKeyValueTupleIterator;
/** @constructor */
function $h_sci_MapKeyValueTupleIterator() {
  /*<skip>*/
}
$h_sci_MapKeyValueTupleIterator.prototype = $c_sci_MapKeyValueTupleIterator.prototype;
$c_sci_MapKeyValueTupleIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sci_MapKeyValueTupleIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sci_MapKeyValueTupleIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sci_MapKeyValueTupleIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sci_MapKeyValueTupleIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sci_MapKeyValueTupleIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sci_MapKeyValueTupleIterator.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sci_MapKeyValueTupleIterator.prototype.next__T2 = (function() {
  if ((!this.hasNext__Z())) {
    throw $ct_ju_NoSuchElementException__(new $c_ju_NoSuchElementException())
  };
  var payload = $as_sci_MapNode(this.sci_ChampBaseIterator__f_currentValueNode).getPayload__I__T2(this.sci_ChampBaseIterator__f_currentValueCursor);
  this.sci_ChampBaseIterator__f_currentValueCursor = ((1 + this.sci_ChampBaseIterator__f_currentValueCursor) | 0);
  return payload
});
$c_sci_MapKeyValueTupleIterator.prototype.next__O = (function() {
  return this.next__T2()
});
var $d_sci_MapKeyValueTupleIterator = new $TypeData().initClass({
  sci_MapKeyValueTupleIterator: 0
}, false, "scala.collection.immutable.MapKeyValueTupleIterator", {
  sci_MapKeyValueTupleIterator: 1,
  sci_ChampBaseIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_MapKeyValueTupleIterator.prototype.$classData = $d_sci_MapKeyValueTupleIterator;
function $p_sci_NewVectorIterator__advanceSlice__V($thiz) {
  if (($thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 <= $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1)) {
    $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  };
  $thiz.sci_NewVectorIterator__f_sliceIdx = ((1 + $thiz.sci_NewVectorIterator__f_sliceIdx) | 0);
  var slice = $thiz.sci_NewVectorIterator__f_v.vectorSlice__I__AO($thiz.sci_NewVectorIterator__f_sliceIdx);
  while ((slice.u.length === 0)) {
    $thiz.sci_NewVectorIterator__f_sliceIdx = ((1 + $thiz.sci_NewVectorIterator__f_sliceIdx) | 0);
    slice = $thiz.sci_NewVectorIterator__f_v.vectorSlice__I__AO($thiz.sci_NewVectorIterator__f_sliceIdx)
  };
  $thiz.sci_NewVectorIterator__f_sliceStart = $thiz.sci_NewVectorIterator__f_sliceEnd;
  var count = $thiz.sci_NewVectorIterator__f_sliceCount;
  var idx = $thiz.sci_NewVectorIterator__f_sliceIdx;
  var c = ((count / 2) | 0);
  var a = ((idx - c) | 0);
  $thiz.sci_NewVectorIterator__f_sliceDim = ((((1 + c) | 0) - ((a < 0) ? ((-a) | 0) : a)) | 0);
  var x1 = $thiz.sci_NewVectorIterator__f_sliceDim;
  switch (x1) {
    case 1: {
      $thiz.sci_NewVectorIterator__f_a1 = slice;
      break
    }
    case 2: {
      $thiz.sci_NewVectorIterator__f_a2 = $asArrayOf_O(slice, 2);
      break
    }
    case 3: {
      $thiz.sci_NewVectorIterator__f_a3 = $asArrayOf_O(slice, 3);
      break
    }
    case 4: {
      $thiz.sci_NewVectorIterator__f_a4 = $asArrayOf_O(slice, 4);
      break
    }
    case 5: {
      $thiz.sci_NewVectorIterator__f_a5 = $asArrayOf_O(slice, 5);
      break
    }
    case 6: {
      $thiz.sci_NewVectorIterator__f_a6 = $asArrayOf_O(slice, 6);
      break
    }
    default: {
      throw new $c_s_MatchError(x1)
    }
  };
  $thiz.sci_NewVectorIterator__f_sliceEnd = (($thiz.sci_NewVectorIterator__f_sliceStart + $imul(slice.u.length, (1 << $imul(5, (((-1) + $thiz.sci_NewVectorIterator__f_sliceDim) | 0))))) | 0);
  if (($thiz.sci_NewVectorIterator__f_sliceEnd > $thiz.sci_NewVectorIterator__f_totalLength)) {
    $thiz.sci_NewVectorIterator__f_sliceEnd = $thiz.sci_NewVectorIterator__f_totalLength
  };
  if (($thiz.sci_NewVectorIterator__f_sliceDim > 1)) {
    $thiz.sci_NewVectorIterator__f_oldPos = (((-1) + (1 << $imul(5, $thiz.sci_NewVectorIterator__f_sliceDim))) | 0)
  }
}
function $p_sci_NewVectorIterator__advance__V($thiz) {
  var pos = (((($thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 - $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1) | 0) + $thiz.sci_NewVectorIterator__f_totalLength) | 0);
  if ((pos === $thiz.sci_NewVectorIterator__f_sliceEnd)) {
    $p_sci_NewVectorIterator__advanceSlice__V($thiz)
  };
  if (($thiz.sci_NewVectorIterator__f_sliceDim > 1)) {
    var io = ((pos - $thiz.sci_NewVectorIterator__f_sliceStart) | 0);
    var xor = ($thiz.sci_NewVectorIterator__f_oldPos ^ io);
    $p_sci_NewVectorIterator__advanceA__I__I__V($thiz, io, xor);
    $thiz.sci_NewVectorIterator__f_oldPos = io
  };
  $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 = (($thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 - $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1) | 0);
  var a = $thiz.sci_NewVectorIterator__f_a1.u.length;
  var b = $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1;
  $thiz.sci_NewVectorIterator__f_a1len = ((a < b) ? a : b);
  $thiz.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = 0
}
function $p_sci_NewVectorIterator__advanceA__I__I__V($thiz, io, xor) {
  if ((xor < 1024)) {
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  } else if ((xor < 32768)) {
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get((31 & ((io >>> 10) | 0)));
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get(0)
  } else if ((xor < 1048576)) {
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get((31 & ((io >>> 15) | 0)));
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get(0);
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get(0)
  } else if ((xor < 33554432)) {
    $thiz.sci_NewVectorIterator__f_a4 = $thiz.sci_NewVectorIterator__f_a5.get((31 & ((io >>> 20) | 0)));
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get(0);
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get(0);
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get(0)
  } else {
    $thiz.sci_NewVectorIterator__f_a5 = $thiz.sci_NewVectorIterator__f_a6.get(((io >>> 25) | 0));
    $thiz.sci_NewVectorIterator__f_a4 = $thiz.sci_NewVectorIterator__f_a5.get(0);
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get(0);
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get(0);
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get(0)
  }
}
function $p_sci_NewVectorIterator__setA__I__I__V($thiz, io, xor) {
  if ((xor < 1024)) {
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  } else if ((xor < 32768)) {
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get((31 & ((io >>> 10) | 0)));
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  } else if ((xor < 1048576)) {
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get((31 & ((io >>> 15) | 0)));
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get((31 & ((io >>> 10) | 0)));
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  } else if ((xor < 33554432)) {
    $thiz.sci_NewVectorIterator__f_a4 = $thiz.sci_NewVectorIterator__f_a5.get((31 & ((io >>> 20) | 0)));
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get((31 & ((io >>> 15) | 0)));
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get((31 & ((io >>> 10) | 0)));
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  } else {
    $thiz.sci_NewVectorIterator__f_a5 = $thiz.sci_NewVectorIterator__f_a6.get(((io >>> 25) | 0));
    $thiz.sci_NewVectorIterator__f_a4 = $thiz.sci_NewVectorIterator__f_a5.get((31 & ((io >>> 20) | 0)));
    $thiz.sci_NewVectorIterator__f_a3 = $thiz.sci_NewVectorIterator__f_a4.get((31 & ((io >>> 15) | 0)));
    $thiz.sci_NewVectorIterator__f_a2 = $thiz.sci_NewVectorIterator__f_a3.get((31 & ((io >>> 10) | 0)));
    $thiz.sci_NewVectorIterator__f_a1 = $thiz.sci_NewVectorIterator__f_a2.get((31 & ((io >>> 5) | 0)))
  }
}
/** @constructor */
function $c_sci_NewVectorIterator(v, totalLength, sliceCount) {
  this.sci_NewVectorIterator__f_v = null;
  this.sci_NewVectorIterator__f_totalLength = 0;
  this.sci_NewVectorIterator__f_sliceCount = 0;
  this.sci_NewVectorIterator__f_a1 = null;
  this.sci_NewVectorIterator__f_a2 = null;
  this.sci_NewVectorIterator__f_a3 = null;
  this.sci_NewVectorIterator__f_a4 = null;
  this.sci_NewVectorIterator__f_a5 = null;
  this.sci_NewVectorIterator__f_a6 = null;
  this.sci_NewVectorIterator__f_a1len = 0;
  this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = 0;
  this.sci_NewVectorIterator__f_oldPos = 0;
  this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 = 0;
  this.sci_NewVectorIterator__f_sliceIdx = 0;
  this.sci_NewVectorIterator__f_sliceDim = 0;
  this.sci_NewVectorIterator__f_sliceStart = 0;
  this.sci_NewVectorIterator__f_sliceEnd = 0;
  this.sci_NewVectorIterator__f_v = v;
  this.sci_NewVectorIterator__f_totalLength = totalLength;
  this.sci_NewVectorIterator__f_sliceCount = sliceCount;
  this.sci_NewVectorIterator__f_a1 = v.sci_Vector__f_prefix1;
  this.sci_NewVectorIterator__f_a1len = this.sci_NewVectorIterator__f_a1.u.length;
  this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = 0;
  this.sci_NewVectorIterator__f_oldPos = 0;
  this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 = this.sci_NewVectorIterator__f_totalLength;
  this.sci_NewVectorIterator__f_sliceIdx = 0;
  this.sci_NewVectorIterator__f_sliceDim = 1;
  this.sci_NewVectorIterator__f_sliceStart = 0;
  this.sci_NewVectorIterator__f_sliceEnd = this.sci_NewVectorIterator__f_a1len
}
$c_sci_NewVectorIterator.prototype = new $h_O();
$c_sci_NewVectorIterator.prototype.constructor = $c_sci_NewVectorIterator;
/** @constructor */
function $h_sci_NewVectorIterator() {
  /*<skip>*/
}
$h_sci_NewVectorIterator.prototype = $c_sci_NewVectorIterator.prototype;
$c_sci_NewVectorIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sci_NewVectorIterator.prototype.isEmpty__Z = (function() {
  return (this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 <= this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1)
});
$c_sci_NewVectorIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sci_NewVectorIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sci_NewVectorIterator.prototype.knownSize__I = (function() {
  return ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 - this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1) | 0)
});
$c_sci_NewVectorIterator.prototype.hasNext__Z = (function() {
  return (this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 > this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1)
});
$c_sci_NewVectorIterator.prototype.next__O = (function() {
  if ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 === this.sci_NewVectorIterator__f_a1len)) {
    $p_sci_NewVectorIterator__advance__V(this)
  };
  var r = this.sci_NewVectorIterator__f_a1.get(this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1);
  this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = ((1 + this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1) | 0);
  return r
});
$c_sci_NewVectorIterator.prototype.drop__I__sc_Iterator = (function(n) {
  if ((n > 0)) {
    var oldpos = ((((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 - this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1) | 0) + this.sci_NewVectorIterator__f_totalLength) | 0);
    var a = ((oldpos + n) | 0);
    var b = this.sci_NewVectorIterator__f_totalLength;
    var newpos = ((a < b) ? a : b);
    if ((newpos === this.sci_NewVectorIterator__f_totalLength)) {
      this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = 0;
      this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 = 0;
      this.sci_NewVectorIterator__f_a1len = 0
    } else {
      while ((newpos >= this.sci_NewVectorIterator__f_sliceEnd)) {
        $p_sci_NewVectorIterator__advanceSlice__V(this)
      };
      var io = ((newpos - this.sci_NewVectorIterator__f_sliceStart) | 0);
      if ((this.sci_NewVectorIterator__f_sliceDim > 1)) {
        var xor = (this.sci_NewVectorIterator__f_oldPos ^ io);
        $p_sci_NewVectorIterator__setA__I__I__V(this, io, xor);
        this.sci_NewVectorIterator__f_oldPos = io
      };
      this.sci_NewVectorIterator__f_a1len = this.sci_NewVectorIterator__f_a1.u.length;
      this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = (31 & io);
      this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 = ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 + ((this.sci_NewVectorIterator__f_totalLength - newpos) | 0)) | 0);
      if ((this.sci_NewVectorIterator__f_a1len > this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1)) {
        this.sci_NewVectorIterator__f_a1len = this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1
      }
    }
  };
  return this
});
$c_sci_NewVectorIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  var xsLen = $m_sr_ScalaRunTime$().array_length__O__I(xs);
  var srcLen = ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$len1 - this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1) | 0);
  var x = ((len < srcLen) ? len : srcLen);
  var y = ((xsLen - start) | 0);
  var x$1 = ((x < y) ? x : y);
  var total = ((x$1 > 0) ? x$1 : 0);
  var copied = 0;
  var isBoxed = (xs instanceof $ac_O);
  while ((copied < total)) {
    if ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 === this.sci_NewVectorIterator__f_a1len)) {
      $p_sci_NewVectorIterator__advance__V(this)
    };
    var a = ((total - copied) | 0);
    var b = ((this.sci_NewVectorIterator__f_a1.u.length - this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1) | 0);
    var count = ((a < b) ? a : b);
    if (isBoxed) {
      var src = this.sci_NewVectorIterator__f_a1;
      var srcPos = this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1;
      var destPos = ((start + copied) | 0);
      src.copyTo(srcPos, xs, destPos, count)
    } else {
      $m_s_Array$().copy__O__I__O__I__I__V(this.sci_NewVectorIterator__f_a1, this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1, xs, ((start + copied) | 0), count)
    };
    this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 = ((this.sci_NewVectorIterator__f_scala$collection$immutable$NewVectorIterator$$i1 + count) | 0);
    copied = ((copied + count) | 0)
  };
  return total
});
var $d_sci_NewVectorIterator = new $TypeData().initClass({
  sci_NewVectorIterator: 0
}, false, "scala.collection.immutable.NewVectorIterator", {
  sci_NewVectorIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1,
  jl_Cloneable: 1
});
$c_sci_NewVectorIterator.prototype.$classData = $d_sci_NewVectorIterator;
/** @constructor */
function $c_sci_Seq$() {
  this.sc_SeqFactory$Delegate__f_delegate = null;
  $ct_sc_SeqFactory$Delegate__sc_SeqFactory__(this, $m_sci_List$())
}
$c_sci_Seq$.prototype = new $h_sc_SeqFactory$Delegate();
$c_sci_Seq$.prototype.constructor = $c_sci_Seq$;
/** @constructor */
function $h_sci_Seq$() {
  /*<skip>*/
}
$h_sci_Seq$.prototype = $c_sci_Seq$.prototype;
$c_sci_Seq$.prototype.from__sc_IterableOnce__sci_Seq = (function(it) {
  if ($is_sci_Seq(it)) {
    var x2 = $as_sci_Seq(it);
    return x2
  } else {
    return $as_sci_Seq($c_sc_SeqFactory$Delegate.prototype.from__sc_IterableOnce__sc_SeqOps.call(this, it))
  }
});
$c_sci_Seq$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_Seq(source)
});
$c_sci_Seq$.prototype.from__sc_IterableOnce__sc_SeqOps = (function(it) {
  return this.from__sc_IterableOnce__sci_Seq(it)
});
var $d_sci_Seq$ = new $TypeData().initClass({
  sci_Seq$: 0
}, false, "scala.collection.immutable.Seq$", {
  sci_Seq$: 1,
  sc_SeqFactory$Delegate: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Seq$.prototype.$classData = $d_sci_Seq$;
var $n_sci_Seq$;
function $m_sci_Seq$() {
  if ((!$n_sci_Seq$)) {
    $n_sci_Seq$ = new $c_sci_Seq$()
  };
  return $n_sci_Seq$
}
/** @constructor */
function $c_sci_SetBuilderImpl() {
  this.sci_SetBuilderImpl__f_elems = null;
  this.sci_SetBuilderImpl__f_switchedToHashSetBuilder = false;
  this.sci_SetBuilderImpl__f_hashSetBuilder = null;
  this.sci_SetBuilderImpl__f_elems = $m_sci_Set$EmptySet$();
  this.sci_SetBuilderImpl__f_switchedToHashSetBuilder = false
}
$c_sci_SetBuilderImpl.prototype = new $h_O();
$c_sci_SetBuilderImpl.prototype.constructor = $c_sci_SetBuilderImpl;
/** @constructor */
function $h_sci_SetBuilderImpl() {
  /*<skip>*/
}
$h_sci_SetBuilderImpl.prototype = $c_sci_SetBuilderImpl.prototype;
$c_sci_SetBuilderImpl.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sci_SetBuilderImpl.prototype.result__sci_Set = (function() {
  return (this.sci_SetBuilderImpl__f_switchedToHashSetBuilder ? this.sci_SetBuilderImpl__f_hashSetBuilder.result__sci_HashSet() : this.sci_SetBuilderImpl__f_elems)
});
$c_sci_SetBuilderImpl.prototype.addOne__O__sci_SetBuilderImpl = (function(elem) {
  if (this.sci_SetBuilderImpl__f_switchedToHashSetBuilder) {
    this.sci_SetBuilderImpl__f_hashSetBuilder.addOne__O__sci_HashSetBuilder(elem)
  } else if ((this.sci_SetBuilderImpl__f_elems.size__I() < 4)) {
    var this$1 = this.sci_SetBuilderImpl__f_elems;
    this.sci_SetBuilderImpl__f_elems = $as_sci_Set(this$1.incl__O__sci_SetOps(elem))
  } else if ((!this.sci_SetBuilderImpl__f_elems.contains__O__Z(elem))) {
    this.sci_SetBuilderImpl__f_switchedToHashSetBuilder = true;
    if ((this.sci_SetBuilderImpl__f_hashSetBuilder === null)) {
      this.sci_SetBuilderImpl__f_hashSetBuilder = new $c_sci_HashSetBuilder()
    };
    $as_sci_Set$Set4(this.sci_SetBuilderImpl__f_elems).buildTo__scm_Builder__scm_Builder(this.sci_SetBuilderImpl__f_hashSetBuilder);
    this.sci_SetBuilderImpl__f_hashSetBuilder.addOne__O__sci_HashSetBuilder(elem)
  };
  return this
});
$c_sci_SetBuilderImpl.prototype.addAll__sc_IterableOnce__sci_SetBuilderImpl = (function(xs) {
  return (this.sci_SetBuilderImpl__f_switchedToHashSetBuilder ? (this.sci_SetBuilderImpl__f_hashSetBuilder.addAll__sc_IterableOnce__sci_HashSetBuilder(xs), this) : $as_sci_SetBuilderImpl($f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs)))
});
$c_sci_SetBuilderImpl.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__sci_SetBuilderImpl(xs)
});
$c_sci_SetBuilderImpl.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__O__sci_SetBuilderImpl(elem)
});
$c_sci_SetBuilderImpl.prototype.result__O = (function() {
  return this.result__sci_Set()
});
function $as_sci_SetBuilderImpl(obj) {
  return (((obj instanceof $c_sci_SetBuilderImpl) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.SetBuilderImpl"))
}
function $isArrayOf_sci_SetBuilderImpl(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_SetBuilderImpl)))
}
function $asArrayOf_sci_SetBuilderImpl(obj, depth) {
  return (($isArrayOf_sci_SetBuilderImpl(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.SetBuilderImpl;", depth))
}
var $d_sci_SetBuilderImpl = new $TypeData().initClass({
  sci_SetBuilderImpl: 0
}, false, "scala.collection.immutable.SetBuilderImpl", {
  sci_SetBuilderImpl: 1,
  O: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_sci_SetBuilderImpl.prototype.$classData = $d_sci_SetBuilderImpl;
/** @constructor */
function $c_sci_SetHashIterator(rootNode) {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null;
  this.sci_SetHashIterator__f_hash = 0;
  $ct_sci_ChampBaseIterator__sci_Node__(this, rootNode);
  this.sci_SetHashIterator__f_hash = 0
}
$c_sci_SetHashIterator.prototype = new $h_sci_ChampBaseIterator();
$c_sci_SetHashIterator.prototype.constructor = $c_sci_SetHashIterator;
/** @constructor */
function $h_sci_SetHashIterator() {
  /*<skip>*/
}
$h_sci_SetHashIterator.prototype = $c_sci_SetHashIterator.prototype;
$c_sci_SetHashIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sci_SetHashIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sci_SetHashIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sci_SetHashIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sci_SetHashIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sci_SetHashIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sci_SetHashIterator.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sci_SetHashIterator.prototype.hashCode__I = (function() {
  return this.sci_SetHashIterator__f_hash
});
$c_sci_SetHashIterator.prototype.next__O = (function() {
  if ((!this.hasNext__Z())) {
    throw $ct_ju_NoSuchElementException__(new $c_ju_NoSuchElementException())
  };
  this.sci_SetHashIterator__f_hash = this.sci_ChampBaseIterator__f_currentValueNode.getHash__I__I(this.sci_ChampBaseIterator__f_currentValueCursor);
  this.sci_ChampBaseIterator__f_currentValueCursor = ((1 + this.sci_ChampBaseIterator__f_currentValueCursor) | 0);
  return this
});
var $d_sci_SetHashIterator = new $TypeData().initClass({
  sci_SetHashIterator: 0
}, false, "scala.collection.immutable.SetHashIterator", {
  sci_SetHashIterator: 1,
  sci_ChampBaseIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_SetHashIterator.prototype.$classData = $d_sci_SetHashIterator;
/** @constructor */
function $c_sci_SetIterator(rootNode) {
  this.sci_ChampBaseIterator__f_currentValueCursor = 0;
  this.sci_ChampBaseIterator__f_currentValueLength = 0;
  this.sci_ChampBaseIterator__f_currentValueNode = null;
  this.sci_ChampBaseIterator__f_currentStackLevel = 0;
  this.sci_ChampBaseIterator__f_nodeCursorsAndLengths = null;
  this.sci_ChampBaseIterator__f_nodes = null;
  $ct_sci_ChampBaseIterator__sci_Node__(this, rootNode)
}
$c_sci_SetIterator.prototype = new $h_sci_ChampBaseIterator();
$c_sci_SetIterator.prototype.constructor = $c_sci_SetIterator;
/** @constructor */
function $h_sci_SetIterator() {
  /*<skip>*/
}
$h_sci_SetIterator.prototype = $c_sci_SetIterator.prototype;
$c_sci_SetIterator.prototype.iterator__sc_Iterator = (function() {
  return this
});
$c_sci_SetIterator.prototype.isEmpty__Z = (function() {
  return (!this.hasNext__Z())
});
$c_sci_SetIterator.prototype.drop__I__sc_Iterator = (function(n) {
  return $f_sc_Iterator__drop__I__sc_Iterator(this, n)
});
$c_sci_SetIterator.prototype.toString__T = (function() {
  return "<iterator>"
});
$c_sci_SetIterator.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sci_SetIterator.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sci_SetIterator.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sci_SetIterator.prototype.next__O = (function() {
  if ((!this.hasNext__Z())) {
    throw $ct_ju_NoSuchElementException__(new $c_ju_NoSuchElementException())
  };
  var payload = $as_sci_SetNode(this.sci_ChampBaseIterator__f_currentValueNode).getPayload__I__O(this.sci_ChampBaseIterator__f_currentValueCursor);
  this.sci_ChampBaseIterator__f_currentValueCursor = ((1 + this.sci_ChampBaseIterator__f_currentValueCursor) | 0);
  return payload
});
var $d_sci_SetIterator = new $TypeData().initClass({
  sci_SetIterator: 0
}, false, "scala.collection.immutable.SetIterator", {
  sci_SetIterator: 1,
  sci_ChampBaseIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_SetIterator.prototype.$classData = $d_sci_SetIterator;
function $p_sci_Vector$__liftedTree1$1__I($thiz) {
  try {
    var x = $m_jl_System$SystemProperties$().getProperty__T__T__T("scala.collection.immutable.Vector.defaultApplyPreferredMaxLength", "250");
    var this$4 = $m_jl_Integer$();
    return this$4.parseInt__T__I__I(x, 10)
  } catch (e) {
    if (false) {
      return 250
    } else {
      throw e
    }
  }
}
/** @constructor */
function $c_sci_Vector$() {
  this.sci_Vector$__f_scala$collection$immutable$Vector$$defaultApplyPreferredMaxLength = 0;
  this.sci_Vector$__f_scala$collection$immutable$Vector$$emptyIterator = null;
  $n_sci_Vector$ = this;
  this.sci_Vector$__f_scala$collection$immutable$Vector$$defaultApplyPreferredMaxLength = $p_sci_Vector$__liftedTree1$1__I(this);
  this.sci_Vector$__f_scala$collection$immutable$Vector$$emptyIterator = new $c_sci_NewVectorIterator($m_sci_Vector0$(), 0, 0)
}
$c_sci_Vector$.prototype = new $h_O();
$c_sci_Vector$.prototype.constructor = $c_sci_Vector$;
/** @constructor */
function $h_sci_Vector$() {
  /*<skip>*/
}
$h_sci_Vector$.prototype = $c_sci_Vector$.prototype;
$c_sci_Vector$.prototype.from__sc_IterableOnce__sci_Vector = (function(it) {
  if ((it instanceof $c_sci_Vector)) {
    var x2 = $as_sci_Vector(it);
    return x2
  } else {
    var knownSize = it.knownSize__I();
    if ((knownSize === 0)) {
      return $m_sci_Vector0$()
    } else if (((knownSize > 0) && (knownSize <= 32))) {
      matchEnd5: {
        var a1$3;
        if (false) {
          var x2$2 = $as_sci_ArraySeq$ofRef(it);
          var x = x2$2.elemTag__s_reflect_ClassTag();
          if (((x !== null) && x.equals__O__Z($d_O.getClassOf()))) {
            var a1$3 = x2$2.unsafeArray__AO();
            break matchEnd5
          }
        };
        if ($is_sci_Iterable(it)) {
          var x3 = $as_sci_Iterable(it);
          var a1 = new $ac_O(knownSize);
          x3.copyToArray__O__I__I__I(a1, 0, 2147483647);
          var a1$3 = a1;
          break matchEnd5
        };
        var a1$2 = new $ac_O(knownSize);
        var this$1 = it.iterator__sc_Iterator();
        this$1.copyToArray__O__I__I__I(a1$2, 0, 2147483647);
        var a1$3 = a1$2
      };
      return new $c_sci_Vector1(a1$3)
    } else {
      var this$2 = new $c_sci_VectorBuilder();
      var this$3 = this$2.addAll__sc_IterableOnce__sci_VectorBuilder(it);
      return this$3.result__sci_Vector()
    }
  }
});
$c_sci_Vector$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sci_Vector(source)
});
var $d_sci_Vector$ = new $TypeData().initClass({
  sci_Vector$: 0
}, false, "scala.collection.immutable.Vector$", {
  sci_Vector$: 1,
  O: 1,
  sc_StrictOptimizedSeqFactory: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector$.prototype.$classData = $d_sci_Vector$;
var $n_sci_Vector$;
function $m_sci_Vector$() {
  if ((!$n_sci_Vector$)) {
    $n_sci_Vector$ = new $c_sci_Vector$()
  };
  return $n_sci_Vector$
}
function $p_sci_VectorBuilder__addArr1__AO__V($thiz, data) {
  var dl = data.u.length;
  if ((dl > 0)) {
    if (($thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 === 32)) {
      $p_sci_VectorBuilder__advance__V($thiz)
    };
    var a = ((32 - $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
    var copy1 = ((a < dl) ? a : dl);
    var copy2 = ((dl - copy1) | 0);
    var dest = $thiz.sci_VectorBuilder__f_a1;
    var destPos = $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1;
    data.copyTo(0, dest, destPos, copy1);
    $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (($thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 + copy1) | 0);
    if ((copy2 > 0)) {
      $p_sci_VectorBuilder__advance__V($thiz);
      var dest$1 = $thiz.sci_VectorBuilder__f_a1;
      data.copyTo(copy1, dest$1, 0, copy2);
      $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (($thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 + copy2) | 0)
    }
  }
}
function $p_sci_VectorBuilder__addVector__sci_Vector__sci_VectorBuilder($thiz, xs) {
  var sliceCount = xs.vectorSliceCount__I();
  var sliceIdx = 0;
  while ((sliceIdx < sliceCount)) {
    var slice = xs.vectorSlice__I__AO(sliceIdx);
    var idx = sliceIdx;
    var c = ((sliceCount / 2) | 0);
    var a = ((idx - c) | 0);
    var x1 = ((((1 + c) | 0) - ((a < 0) ? ((-a) | 0) : a)) | 0);
    if ((x1 === 1)) {
      $p_sci_VectorBuilder__addArr1__AO__V($thiz, slice)
    } else {
      $m_sci_VectorStatics$().foreachRec__I__AO__F1__V((((-2) + x1) | 0), slice, new $c_sjsr_AnonFunction1(((this$3) => ((data$2) => {
        var data = $asArrayOf_O(data$2, 1);
        $p_sci_VectorBuilder__addArr1__AO__V(this$3, data)
      }))($thiz)))
    };
    sliceIdx = ((1 + sliceIdx) | 0)
  };
  return $thiz
}
function $p_sci_VectorBuilder__advance__V($thiz) {
  var idx = ((32 + $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest) | 0);
  var xor = (idx ^ $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest);
  $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = idx;
  $thiz.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = 0;
  $p_sci_VectorBuilder__advance1__I__I__V($thiz, idx, xor)
}
function $p_sci_VectorBuilder__advance1__I__I__V($thiz, idx, xor) {
  if ((xor < 1024)) {
    if (($thiz.sci_VectorBuilder__f_depth === 1)) {
      $thiz.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
      $thiz.sci_VectorBuilder__f_a2.set(0, $thiz.sci_VectorBuilder__f_a1);
      $thiz.sci_VectorBuilder__f_depth = ((1 + $thiz.sci_VectorBuilder__f_depth) | 0)
    };
    $thiz.sci_VectorBuilder__f_a1 = new $ac_O(32);
    $thiz.sci_VectorBuilder__f_a2.set((31 & ((idx >>> 5) | 0)), $thiz.sci_VectorBuilder__f_a1)
  } else if ((xor < 32768)) {
    if (($thiz.sci_VectorBuilder__f_depth === 2)) {
      $thiz.sci_VectorBuilder__f_a3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(32);
      $thiz.sci_VectorBuilder__f_a3.set(0, $thiz.sci_VectorBuilder__f_a2);
      $thiz.sci_VectorBuilder__f_depth = ((1 + $thiz.sci_VectorBuilder__f_depth) | 0)
    };
    $thiz.sci_VectorBuilder__f_a1 = new $ac_O(32);
    $thiz.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a2.set((31 & ((idx >>> 5) | 0)), $thiz.sci_VectorBuilder__f_a1);
    $thiz.sci_VectorBuilder__f_a3.set((31 & ((idx >>> 10) | 0)), $thiz.sci_VectorBuilder__f_a2)
  } else if ((xor < 1048576)) {
    if (($thiz.sci_VectorBuilder__f_depth === 3)) {
      $thiz.sci_VectorBuilder__f_a4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
      $thiz.sci_VectorBuilder__f_a4.set(0, $thiz.sci_VectorBuilder__f_a3);
      $thiz.sci_VectorBuilder__f_depth = ((1 + $thiz.sci_VectorBuilder__f_depth) | 0)
    };
    $thiz.sci_VectorBuilder__f_a1 = new $ac_O(32);
    $thiz.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a2.set((31 & ((idx >>> 5) | 0)), $thiz.sci_VectorBuilder__f_a1);
    $thiz.sci_VectorBuilder__f_a3.set((31 & ((idx >>> 10) | 0)), $thiz.sci_VectorBuilder__f_a2);
    $thiz.sci_VectorBuilder__f_a4.set((31 & ((idx >>> 15) | 0)), $thiz.sci_VectorBuilder__f_a3)
  } else if ((xor < 33554432)) {
    if (($thiz.sci_VectorBuilder__f_depth === 4)) {
      $thiz.sci_VectorBuilder__f_a5 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
      $thiz.sci_VectorBuilder__f_a5.set(0, $thiz.sci_VectorBuilder__f_a4);
      $thiz.sci_VectorBuilder__f_depth = ((1 + $thiz.sci_VectorBuilder__f_depth) | 0)
    };
    $thiz.sci_VectorBuilder__f_a1 = new $ac_O(32);
    $thiz.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a2.set((31 & ((idx >>> 5) | 0)), $thiz.sci_VectorBuilder__f_a1);
    $thiz.sci_VectorBuilder__f_a3.set((31 & ((idx >>> 10) | 0)), $thiz.sci_VectorBuilder__f_a2);
    $thiz.sci_VectorBuilder__f_a4.set((31 & ((idx >>> 15) | 0)), $thiz.sci_VectorBuilder__f_a3);
    $thiz.sci_VectorBuilder__f_a5.set((31 & ((idx >>> 20) | 0)), $thiz.sci_VectorBuilder__f_a4)
  } else if ((xor < 1073741824)) {
    if (($thiz.sci_VectorBuilder__f_depth === 5)) {
      $thiz.sci_VectorBuilder__f_a6 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(64);
      $thiz.sci_VectorBuilder__f_a6.set(0, $thiz.sci_VectorBuilder__f_a5);
      $thiz.sci_VectorBuilder__f_depth = ((1 + $thiz.sci_VectorBuilder__f_depth) | 0)
    };
    $thiz.sci_VectorBuilder__f_a1 = new $ac_O(32);
    $thiz.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a5 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
    $thiz.sci_VectorBuilder__f_a2.set((31 & ((idx >>> 5) | 0)), $thiz.sci_VectorBuilder__f_a1);
    $thiz.sci_VectorBuilder__f_a3.set((31 & ((idx >>> 10) | 0)), $thiz.sci_VectorBuilder__f_a2);
    $thiz.sci_VectorBuilder__f_a4.set((31 & ((idx >>> 15) | 0)), $thiz.sci_VectorBuilder__f_a3);
    $thiz.sci_VectorBuilder__f_a5.set((31 & ((idx >>> 20) | 0)), $thiz.sci_VectorBuilder__f_a4);
    $thiz.sci_VectorBuilder__f_a6.set((31 & ((idx >>> 25) | 0)), $thiz.sci_VectorBuilder__f_a5)
  } else {
    throw $ct_jl_IllegalArgumentException__T__(new $c_jl_IllegalArgumentException(), ((((((((((((((((("advance1(" + idx) + ", ") + xor) + "): a1=") + $thiz.sci_VectorBuilder__f_a1) + ", a2=") + $thiz.sci_VectorBuilder__f_a2) + ", a3=") + $thiz.sci_VectorBuilder__f_a3) + ", a4=") + $thiz.sci_VectorBuilder__f_a4) + ", a5=") + $thiz.sci_VectorBuilder__f_a5) + ", a6=") + $thiz.sci_VectorBuilder__f_a6) + ", depth=") + $thiz.sci_VectorBuilder__f_depth))
  }
}
/** @constructor */
function $c_sci_VectorBuilder() {
  this.sci_VectorBuilder__f_a6 = null;
  this.sci_VectorBuilder__f_a5 = null;
  this.sci_VectorBuilder__f_a4 = null;
  this.sci_VectorBuilder__f_a3 = null;
  this.sci_VectorBuilder__f_a2 = null;
  this.sci_VectorBuilder__f_a1 = null;
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = 0;
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = 0;
  this.sci_VectorBuilder__f_offset = 0;
  this.sci_VectorBuilder__f_depth = 0;
  this.sci_VectorBuilder__f_a1 = new $ac_O(32);
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = 0;
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = 0;
  this.sci_VectorBuilder__f_offset = 0;
  this.sci_VectorBuilder__f_depth = 1
}
$c_sci_VectorBuilder.prototype = new $h_O();
$c_sci_VectorBuilder.prototype.constructor = $c_sci_VectorBuilder;
/** @constructor */
function $h_sci_VectorBuilder() {
  /*<skip>*/
}
$h_sci_VectorBuilder.prototype = $c_sci_VectorBuilder.prototype;
$c_sci_VectorBuilder.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sci_VectorBuilder.prototype.initFrom__AO__V = (function(prefix1) {
  this.sci_VectorBuilder__f_depth = 1;
  var i = prefix1.u.length;
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i);
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
  this.sci_VectorBuilder__f_a1 = ((prefix1.u.length === 32) ? prefix1 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(prefix1, 0, 32));
  if (((this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 === 0) && (this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest > 0))) {
    this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = 32;
    this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = (((-32) + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest) | 0)
  }
});
$c_sci_VectorBuilder.prototype.initFrom__sci_Vector__sci_VectorBuilder = (function(v) {
  var x1 = v.vectorSliceCount__I();
  switch (x1) {
    case 0: {
      break
    }
    case 1: {
      var v1 = $as_sci_Vector1(v);
      this.sci_VectorBuilder__f_depth = 1;
      var i = v1.sci_Vector__f_prefix1.u.length;
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      var a = v1.sci_Vector__f_prefix1;
      this.sci_VectorBuilder__f_a1 = ((a.u.length === 32) ? a : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 0, 32));
      break
    }
    case 3: {
      var v2 = $as_sci_Vector2(v);
      var d2 = v2.sci_Vector2__f_data2;
      var a$1 = v2.sci_BigVector__f_suffix1;
      this.sci_VectorBuilder__f_a1 = ((a$1.u.length === 32) ? a$1 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$1, 0, 32));
      this.sci_VectorBuilder__f_depth = 2;
      this.sci_VectorBuilder__f_offset = ((32 - v2.sci_Vector2__f_len1) | 0);
      var i$1 = ((v2.sci_BigVector__f_length0 + this.sci_VectorBuilder__f_offset) | 0);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i$1);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i$1 - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      this.sci_VectorBuilder__f_a2 = new ($d_O.getArrayOf().getArrayOf().constr)(32);
      this.sci_VectorBuilder__f_a2.set(0, v2.sci_Vector__f_prefix1);
      var dest = this.sci_VectorBuilder__f_a2;
      var length = d2.u.length;
      d2.copyTo(0, dest, 1, length);
      this.sci_VectorBuilder__f_a2.set(((1 + d2.u.length) | 0), this.sci_VectorBuilder__f_a1);
      break
    }
    case 5: {
      var v3 = $as_sci_Vector3(v);
      var d3 = v3.sci_Vector3__f_data3;
      var s2 = v3.sci_Vector3__f_suffix2;
      var a$2 = v3.sci_BigVector__f_suffix1;
      this.sci_VectorBuilder__f_a1 = ((a$2.u.length === 32) ? a$2 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$2, 0, 32));
      this.sci_VectorBuilder__f_depth = 3;
      this.sci_VectorBuilder__f_offset = ((1024 - v3.sci_Vector3__f_len12) | 0);
      var i$2 = ((v3.sci_BigVector__f_length0 + this.sci_VectorBuilder__f_offset) | 0);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i$2);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i$2 - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      this.sci_VectorBuilder__f_a3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(32);
      this.sci_VectorBuilder__f_a3.set(0, $asArrayOf_O($m_sci_VectorStatics$().copyPrepend__O__AO__AO(v3.sci_Vector__f_prefix1, v3.sci_Vector3__f_prefix2), 2));
      var dest$1 = this.sci_VectorBuilder__f_a3;
      var length$1 = d3.u.length;
      d3.copyTo(0, dest$1, 1, length$1);
      this.sci_VectorBuilder__f_a2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s2, 32), 2);
      this.sci_VectorBuilder__f_a3.set(((1 + d3.u.length) | 0), this.sci_VectorBuilder__f_a2);
      this.sci_VectorBuilder__f_a2.set(s2.u.length, this.sci_VectorBuilder__f_a1);
      break
    }
    case 7: {
      var v4 = $as_sci_Vector4(v);
      var d4 = v4.sci_Vector4__f_data4;
      var s3 = v4.sci_Vector4__f_suffix3;
      var s2$2 = v4.sci_Vector4__f_suffix2;
      var a$3 = v4.sci_BigVector__f_suffix1;
      this.sci_VectorBuilder__f_a1 = ((a$3.u.length === 32) ? a$3 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$3, 0, 32));
      this.sci_VectorBuilder__f_depth = 4;
      this.sci_VectorBuilder__f_offset = ((32768 - v4.sci_Vector4__f_len123) | 0);
      var i$3 = ((v4.sci_BigVector__f_length0 + this.sci_VectorBuilder__f_offset) | 0);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i$3);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i$3 - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      this.sci_VectorBuilder__f_a4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
      this.sci_VectorBuilder__f_a4.set(0, $asArrayOf_O($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO(v4.sci_Vector__f_prefix1, v4.sci_Vector4__f_prefix2), v4.sci_Vector4__f_prefix3), 3));
      var dest$2 = this.sci_VectorBuilder__f_a4;
      var length$2 = d4.u.length;
      d4.copyTo(0, dest$2, 1, length$2);
      this.sci_VectorBuilder__f_a3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s3, 32), 3);
      this.sci_VectorBuilder__f_a2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s2$2, 32), 2);
      this.sci_VectorBuilder__f_a4.set(((1 + d4.u.length) | 0), this.sci_VectorBuilder__f_a3);
      this.sci_VectorBuilder__f_a3.set(s3.u.length, this.sci_VectorBuilder__f_a2);
      this.sci_VectorBuilder__f_a2.set(s2$2.u.length, this.sci_VectorBuilder__f_a1);
      break
    }
    case 9: {
      var v5 = $as_sci_Vector5(v);
      var d5 = v5.sci_Vector5__f_data5;
      var s4 = v5.sci_Vector5__f_suffix4;
      var s3$2 = v5.sci_Vector5__f_suffix3;
      var s2$3 = v5.sci_Vector5__f_suffix2;
      var a$4 = v5.sci_BigVector__f_suffix1;
      this.sci_VectorBuilder__f_a1 = ((a$4.u.length === 32) ? a$4 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$4, 0, 32));
      this.sci_VectorBuilder__f_depth = 5;
      this.sci_VectorBuilder__f_offset = ((1048576 - v5.sci_Vector5__f_len1234) | 0);
      var i$4 = ((v5.sci_BigVector__f_length0 + this.sci_VectorBuilder__f_offset) | 0);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i$4);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i$4 - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      this.sci_VectorBuilder__f_a5 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
      this.sci_VectorBuilder__f_a5.set(0, $asArrayOf_O($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO(v5.sci_Vector__f_prefix1, v5.sci_Vector5__f_prefix2), v5.sci_Vector5__f_prefix3), v5.sci_Vector5__f_prefix4), 4));
      var dest$3 = this.sci_VectorBuilder__f_a5;
      var length$3 = d5.u.length;
      d5.copyTo(0, dest$3, 1, length$3);
      this.sci_VectorBuilder__f_a4 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s4, 32), 4);
      this.sci_VectorBuilder__f_a3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s3$2, 32), 3);
      this.sci_VectorBuilder__f_a2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s2$3, 32), 2);
      this.sci_VectorBuilder__f_a5.set(((1 + d5.u.length) | 0), this.sci_VectorBuilder__f_a4);
      this.sci_VectorBuilder__f_a4.set(s4.u.length, this.sci_VectorBuilder__f_a3);
      this.sci_VectorBuilder__f_a3.set(s3$2.u.length, this.sci_VectorBuilder__f_a2);
      this.sci_VectorBuilder__f_a2.set(s2$3.u.length, this.sci_VectorBuilder__f_a1);
      break
    }
    case 11: {
      var v6 = $as_sci_Vector6(v);
      var d6 = v6.sci_Vector6__f_data6;
      var s5 = v6.sci_Vector6__f_suffix5;
      var s4$2 = v6.sci_Vector6__f_suffix4;
      var s3$3 = v6.sci_Vector6__f_suffix3;
      var s2$4 = v6.sci_Vector6__f_suffix2;
      var a$5 = v6.sci_BigVector__f_suffix1;
      this.sci_VectorBuilder__f_a1 = ((a$5.u.length === 32) ? a$5 : $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$5, 0, 32));
      this.sci_VectorBuilder__f_depth = 6;
      this.sci_VectorBuilder__f_offset = ((33554432 - v6.sci_Vector6__f_len12345) | 0);
      var i$5 = ((v6.sci_BigVector__f_length0 + this.sci_VectorBuilder__f_offset) | 0);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = (31 & i$5);
      this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = ((i$5 - this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
      this.sci_VectorBuilder__f_a6 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(32);
      this.sci_VectorBuilder__f_a6.set(0, $asArrayOf_O($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO($m_sci_VectorStatics$().copyPrepend__O__AO__AO(v6.sci_Vector__f_prefix1, v6.sci_Vector6__f_prefix2), v6.sci_Vector6__f_prefix3), v6.sci_Vector6__f_prefix4), v6.sci_Vector6__f_prefix5), 5));
      var dest$4 = this.sci_VectorBuilder__f_a6;
      var length$4 = d6.u.length;
      d6.copyTo(0, dest$4, 1, length$4);
      this.sci_VectorBuilder__f_a5 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s5, 32), 5);
      this.sci_VectorBuilder__f_a4 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s4$2, 32), 4);
      this.sci_VectorBuilder__f_a3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s3$3, 32), 3);
      this.sci_VectorBuilder__f_a2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(s2$4, 32), 2);
      this.sci_VectorBuilder__f_a6.set(((1 + d6.u.length) | 0), this.sci_VectorBuilder__f_a5);
      this.sci_VectorBuilder__f_a5.set(s5.u.length, this.sci_VectorBuilder__f_a4);
      this.sci_VectorBuilder__f_a4.set(s4$2.u.length, this.sci_VectorBuilder__f_a3);
      this.sci_VectorBuilder__f_a3.set(s3$3.u.length, this.sci_VectorBuilder__f_a2);
      this.sci_VectorBuilder__f_a2.set(s2$4.u.length, this.sci_VectorBuilder__f_a1);
      break
    }
    default: {
      throw new $c_s_MatchError(x1)
    }
  };
  if (((this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 === 0) && (this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest > 0))) {
    this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = 32;
    this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest = (((-32) + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest) | 0)
  };
  return this
});
$c_sci_VectorBuilder.prototype.addOne__O__sci_VectorBuilder = (function(elem) {
  if ((this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 === 32)) {
    $p_sci_VectorBuilder__advance__V(this)
  };
  this.sci_VectorBuilder__f_a1.set(this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1, elem);
  this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 = ((1 + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) | 0);
  return this
});
$c_sci_VectorBuilder.prototype.addAll__sc_IterableOnce__sci_VectorBuilder = (function(xs) {
  if ((xs instanceof $c_sci_Vector)) {
    var x2 = $as_sci_Vector(xs);
    return (((this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 === 0) && (this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest === 0)) ? this.initFrom__sci_Vector__sci_VectorBuilder(x2) : $p_sci_VectorBuilder__addVector__sci_Vector__sci_VectorBuilder(this, x2))
  } else {
    return $as_sci_VectorBuilder($f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs))
  }
});
$c_sci_VectorBuilder.prototype.result__sci_Vector = (function() {
  var len = ((this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1 + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest) | 0);
  var realLen = ((len - this.sci_VectorBuilder__f_offset) | 0);
  if ((realLen === 0)) {
    $m_sci_Vector$();
    return $m_sci_Vector0$()
  } else if ((len <= 32)) {
    if ((realLen === 32)) {
      return new $c_sci_Vector1(this.sci_VectorBuilder__f_a1)
    } else {
      var original = this.sci_VectorBuilder__f_a1;
      return new $c_sci_Vector1($m_ju_Arrays$().copyOf__AO__I__AO(original, realLen))
    }
  } else if ((len <= 1024)) {
    var i1 = (31 & (((-1) + len) | 0));
    var i2 = (((((-1) + len) | 0) >>> 5) | 0);
    var original$1 = this.sci_VectorBuilder__f_a2;
    var data = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original$1, 1, i2), 2);
    var prefix1 = this.sci_VectorBuilder__f_a2.get(0);
    var a = this.sci_VectorBuilder__f_a2.get(i2);
    var len$1 = ((1 + i1) | 0);
    var suffix1 = ((a.u.length === len$1) ? a : $m_ju_Arrays$().copyOf__AO__I__AO(a, len$1));
    return new $c_sci_Vector2(prefix1, ((32 - this.sci_VectorBuilder__f_offset) | 0), data, suffix1, realLen)
  } else if ((len <= 32768)) {
    var i1$2 = (31 & (((-1) + len) | 0));
    var i2$2 = (31 & (((((-1) + len) | 0) >>> 5) | 0));
    var i3 = (((((-1) + len) | 0) >>> 10) | 0);
    var original$2 = this.sci_VectorBuilder__f_a3;
    var data$2 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original$2, 1, i3), 3);
    var a$1 = this.sci_VectorBuilder__f_a3.get(0);
    var to = a$1.u.length;
    var prefix2 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$1, 1, to), 2);
    var prefix1$2 = this.sci_VectorBuilder__f_a3.get(0).get(0);
    var original$3 = this.sci_VectorBuilder__f_a3.get(i3);
    var suffix2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$3, i2$2), 2);
    var a$2 = this.sci_VectorBuilder__f_a3.get(i3).get(i2$2);
    var len$2 = ((1 + i1$2) | 0);
    var suffix1$2 = ((a$2.u.length === len$2) ? a$2 : $m_ju_Arrays$().copyOf__AO__I__AO(a$2, len$2));
    var len1 = prefix1$2.u.length;
    var len12 = ((len1 + (prefix2.u.length << 5)) | 0);
    return new $c_sci_Vector3(prefix1$2, len1, prefix2, len12, data$2, suffix2, suffix1$2, realLen)
  } else if ((len <= 1048576)) {
    var i1$3 = (31 & (((-1) + len) | 0));
    var i2$3 = (31 & (((((-1) + len) | 0) >>> 5) | 0));
    var i3$2 = (31 & (((((-1) + len) | 0) >>> 10) | 0));
    var i4 = (((((-1) + len) | 0) >>> 15) | 0);
    var original$4 = this.sci_VectorBuilder__f_a4;
    var data$3 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original$4, 1, i4), 4);
    var a$3 = this.sci_VectorBuilder__f_a4.get(0);
    var to$1 = a$3.u.length;
    var prefix3 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$3, 1, to$1), 3);
    var a$4 = this.sci_VectorBuilder__f_a4.get(0).get(0);
    var to$2 = a$4.u.length;
    var prefix2$2 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$4, 1, to$2), 2);
    var prefix1$3 = this.sci_VectorBuilder__f_a4.get(0).get(0).get(0);
    var original$5 = this.sci_VectorBuilder__f_a4.get(i4);
    var suffix3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$5, i3$2), 3);
    var original$6 = this.sci_VectorBuilder__f_a4.get(i4).get(i3$2);
    var suffix2$2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$6, i2$3), 2);
    var a$5 = this.sci_VectorBuilder__f_a4.get(i4).get(i3$2).get(i2$3);
    var len$3 = ((1 + i1$3) | 0);
    var suffix1$3 = ((a$5.u.length === len$3) ? a$5 : $m_ju_Arrays$().copyOf__AO__I__AO(a$5, len$3));
    var len1$2 = prefix1$3.u.length;
    var len12$2 = ((len1$2 + (prefix2$2.u.length << 5)) | 0);
    var len123 = ((len12$2 + (prefix3.u.length << 10)) | 0);
    return new $c_sci_Vector4(prefix1$3, len1$2, prefix2$2, len12$2, prefix3, len123, data$3, suffix3, suffix2$2, suffix1$3, realLen)
  } else if ((len <= 33554432)) {
    var i1$4 = (31 & (((-1) + len) | 0));
    var i2$4 = (31 & (((((-1) + len) | 0) >>> 5) | 0));
    var i3$3 = (31 & (((((-1) + len) | 0) >>> 10) | 0));
    var i4$2 = (31 & (((((-1) + len) | 0) >>> 15) | 0));
    var i5 = (((((-1) + len) | 0) >>> 20) | 0);
    var original$7 = this.sci_VectorBuilder__f_a5;
    var data$4 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original$7, 1, i5), 5);
    var a$6 = this.sci_VectorBuilder__f_a5.get(0);
    var to$3 = a$6.u.length;
    var prefix4 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$6, 1, to$3), 4);
    var a$7 = this.sci_VectorBuilder__f_a5.get(0).get(0);
    var to$4 = a$7.u.length;
    var prefix3$2 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$7, 1, to$4), 3);
    var a$8 = this.sci_VectorBuilder__f_a5.get(0).get(0).get(0);
    var to$5 = a$8.u.length;
    var prefix2$3 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$8, 1, to$5), 2);
    var prefix1$4 = this.sci_VectorBuilder__f_a5.get(0).get(0).get(0).get(0);
    var original$8 = this.sci_VectorBuilder__f_a5.get(i5);
    var suffix4 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$8, i4$2), 4);
    var original$9 = this.sci_VectorBuilder__f_a5.get(i5).get(i4$2);
    var suffix3$2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$9, i3$3), 3);
    var original$10 = this.sci_VectorBuilder__f_a5.get(i5).get(i4$2).get(i3$3);
    var suffix2$3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$10, i2$4), 2);
    var a$9 = this.sci_VectorBuilder__f_a5.get(i5).get(i4$2).get(i3$3).get(i2$4);
    var len$4 = ((1 + i1$4) | 0);
    var suffix1$4 = ((a$9.u.length === len$4) ? a$9 : $m_ju_Arrays$().copyOf__AO__I__AO(a$9, len$4));
    var len1$3 = prefix1$4.u.length;
    var len12$3 = ((len1$3 + (prefix2$3.u.length << 5)) | 0);
    var len123$2 = ((len12$3 + (prefix3$2.u.length << 10)) | 0);
    var len1234 = ((len123$2 + (prefix4.u.length << 15)) | 0);
    return new $c_sci_Vector5(prefix1$4, len1$3, prefix2$3, len12$3, prefix3$2, len123$2, prefix4, len1234, data$4, suffix4, suffix3$2, suffix2$3, suffix1$4, realLen)
  } else {
    var i1$5 = (31 & (((-1) + len) | 0));
    var i2$5 = (31 & (((((-1) + len) | 0) >>> 5) | 0));
    var i3$4 = (31 & (((((-1) + len) | 0) >>> 10) | 0));
    var i4$3 = (31 & (((((-1) + len) | 0) >>> 15) | 0));
    var i5$2 = (31 & (((((-1) + len) | 0) >>> 20) | 0));
    var i6 = (((((-1) + len) | 0) >>> 25) | 0);
    var original$11 = this.sci_VectorBuilder__f_a6;
    var data$5 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original$11, 1, i6), 6);
    var a$10 = this.sci_VectorBuilder__f_a6.get(0);
    var to$6 = a$10.u.length;
    var prefix5 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$10, 1, to$6), 5);
    var a$11 = this.sci_VectorBuilder__f_a6.get(0).get(0);
    var to$7 = a$11.u.length;
    var prefix4$2 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$11, 1, to$7), 4);
    var a$12 = this.sci_VectorBuilder__f_a6.get(0).get(0).get(0);
    var to$8 = a$12.u.length;
    var prefix3$3 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$12, 1, to$8), 3);
    var a$13 = this.sci_VectorBuilder__f_a6.get(0).get(0).get(0).get(0);
    var to$9 = a$13.u.length;
    var prefix2$4 = $asArrayOf_O($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a$13, 1, to$9), 2);
    var prefix1$5 = this.sci_VectorBuilder__f_a6.get(0).get(0).get(0).get(0).get(0);
    var original$12 = this.sci_VectorBuilder__f_a6.get(i6);
    var suffix5 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$12, i5$2), 5);
    var original$13 = this.sci_VectorBuilder__f_a6.get(i6).get(i5$2);
    var suffix4$2 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$13, i4$3), 4);
    var original$14 = this.sci_VectorBuilder__f_a6.get(i6).get(i5$2).get(i4$3);
    var suffix3$3 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$14, i3$4), 3);
    var original$15 = this.sci_VectorBuilder__f_a6.get(i6).get(i5$2).get(i4$3).get(i3$4);
    var suffix2$4 = $asArrayOf_O($m_ju_Arrays$().copyOf__AO__I__AO(original$15, i2$5), 2);
    var a$14 = this.sci_VectorBuilder__f_a6.get(i6).get(i5$2).get(i4$3).get(i3$4).get(i2$5);
    var len$5 = ((1 + i1$5) | 0);
    var suffix1$5 = ((a$14.u.length === len$5) ? a$14 : $m_ju_Arrays$().copyOf__AO__I__AO(a$14, len$5));
    var len1$4 = prefix1$5.u.length;
    var len12$4 = ((len1$4 + (prefix2$4.u.length << 5)) | 0);
    var len123$3 = ((len12$4 + (prefix3$3.u.length << 10)) | 0);
    var len1234$2 = ((len123$3 + (prefix4$2.u.length << 15)) | 0);
    var len12345 = ((len1234$2 + (prefix5.u.length << 20)) | 0);
    return new $c_sci_Vector6(prefix1$5, len1$4, prefix2$4, len12$4, prefix3$3, len123$3, prefix4$2, len1234$2, prefix5, len12345, data$5, suffix5, suffix4$2, suffix3$3, suffix2$4, suffix1$5, realLen)
  }
});
$c_sci_VectorBuilder.prototype.toString__T = (function() {
  return (((((((("VectorBuilder(len1=" + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$len1) + ", lenRest=") + this.sci_VectorBuilder__f_scala$collection$immutable$VectorBuilder$$lenRest) + ", offset=") + this.sci_VectorBuilder__f_offset) + ", depth=") + this.sci_VectorBuilder__f_depth) + ")")
});
$c_sci_VectorBuilder.prototype.result__O = (function() {
  return this.result__sci_Vector()
});
$c_sci_VectorBuilder.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__sci_VectorBuilder(xs)
});
$c_sci_VectorBuilder.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__O__sci_VectorBuilder(elem)
});
function $as_sci_VectorBuilder(obj) {
  return (((obj instanceof $c_sci_VectorBuilder) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.VectorBuilder"))
}
function $isArrayOf_sci_VectorBuilder(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_VectorBuilder)))
}
function $asArrayOf_sci_VectorBuilder(obj, depth) {
  return (($isArrayOf_sci_VectorBuilder(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.VectorBuilder;", depth))
}
var $d_sci_VectorBuilder = new $TypeData().initClass({
  sci_VectorBuilder: 0
}, false, "scala.collection.immutable.VectorBuilder", {
  sci_VectorBuilder: 1,
  O: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1
});
$c_sci_VectorBuilder.prototype.$classData = $d_sci_VectorBuilder;
function $p_scm_ArrayBuffer$__resizeEnsuring$1__I__I__I__I($thiz, length, end, n) {
  var hi = (length >> 31);
  var newSize__lo = length;
  var newSize__hi = hi;
  var this$2__lo = newSize__lo;
  var this$2__hi = newSize__hi;
  var lo = this$2__lo;
  var lo$1 = (lo << 1);
  var hi$1 = (((lo >>> 31) | 0) | (this$2__hi << 1));
  var t = (((hi$1 === 0) ? (((-2147483648) ^ lo$1) > (-2147483632)) : (hi$1 > 0)) ? new $c_RTLong(lo$1, hi$1) : new $c_RTLong(16, 0));
  var lo$2 = t.RTLong__f_lo;
  var hi$2 = t.RTLong__f_hi;
  var $$x1__lo = lo$2;
  var $$x1__hi = hi$2;
  newSize__lo = $$x1__lo;
  newSize__hi = $$x1__hi;
  while (true) {
    var this$6__lo = newSize__lo;
    var this$6__hi = newSize__hi;
    var hi$3 = (n >> 31);
    var ahi = this$6__hi;
    if (((ahi === hi$3) ? (((-2147483648) ^ this$6__lo) < ((-2147483648) ^ n)) : (ahi < hi$3))) {
      var this$7__lo = newSize__lo;
      var this$7__hi = newSize__hi;
      var lo$3 = this$7__lo;
      var lo$4 = (lo$3 << 1);
      var hi$4 = (((lo$3 >>> 31) | 0) | (this$7__hi << 1));
      var $$x2__lo = lo$4;
      var $$x2__hi = hi$4;
      newSize__lo = $$x2__lo;
      newSize__hi = $$x2__hi
    } else {
      break
    }
  };
  var this$8__lo = newSize__lo;
  var this$8__hi = newSize__hi;
  var ahi$1 = this$8__hi;
  if (((ahi$1 === 0) ? (((-2147483648) ^ this$8__lo) <= (-1)) : (ahi$1 < 0))) {
    var this$9__lo = newSize__lo;
    var this$9__hi = newSize__hi;
    return this$9__lo
  } else if ((end === 2147483647)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O($ct_jl_Exception__T__(new $c_jl_Exception(), "Collections can not have more than 2147483647 elements"))
  } else {
    return 2147483647
  }
}
/** @constructor */
function $c_scm_ArrayBuffer$() {
  /*<skip>*/
}
$c_scm_ArrayBuffer$.prototype = new $h_O();
$c_scm_ArrayBuffer$.prototype.constructor = $c_scm_ArrayBuffer$;
/** @constructor */
function $h_scm_ArrayBuffer$() {
  /*<skip>*/
}
$h_scm_ArrayBuffer$.prototype = $c_scm_ArrayBuffer$.prototype;
$c_scm_ArrayBuffer$.prototype.from__sc_IterableOnce__scm_ArrayBuffer = (function(coll) {
  var k = coll.knownSize__I();
  if ((k >= 0)) {
    var array = new $ac_O(((k > 16) ? k : 16));
    if ($is_sc_Iterable(coll)) {
      var x2 = $as_sc_Iterable(coll);
      x2.copyToArray__O__I__I__I(array, 0, 2147483647)
    } else {
      coll.iterator__sc_Iterator().copyToArray__O__I__I__I(array, 0, 2147483647)
    };
    return $ct_scm_ArrayBuffer__AO__I__(new $c_scm_ArrayBuffer(), array, k)
  } else {
    var this$6 = $ct_scm_ArrayBuffer__(new $c_scm_ArrayBuffer());
    return this$6.addAll__sc_IterableOnce__scm_ArrayBuffer(coll)
  }
});
$c_scm_ArrayBuffer$.prototype.scala$collection$mutable$ArrayBuffer$$ensureSize__AO__I__I__AO = (function(array, end, n) {
  if ((n <= array.u.length)) {
    return array
  } else {
    var a = new $ac_O($p_scm_ArrayBuffer$__resizeEnsuring$1__I__I__I__I(this, array.u.length, end, n));
    $m_s_Array$().copy__O__I__O__I__I__V(array, 0, a, 0, end);
    return a
  }
});
$c_scm_ArrayBuffer$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__scm_ArrayBuffer(source)
});
var $d_scm_ArrayBuffer$ = new $TypeData().initClass({
  scm_ArrayBuffer$: 0
}, false, "scala.collection.mutable.ArrayBuffer$", {
  scm_ArrayBuffer$: 1,
  O: 1,
  sc_StrictOptimizedSeqFactory: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_ArrayBuffer$.prototype.$classData = $d_scm_ArrayBuffer$;
var $n_scm_ArrayBuffer$;
function $m_scm_ArrayBuffer$() {
  if ((!$n_scm_ArrayBuffer$)) {
    $n_scm_ArrayBuffer$ = new $c_scm_ArrayBuffer$()
  };
  return $n_scm_ArrayBuffer$
}
/** @constructor */
function $c_scm_Buffer$() {
  this.sc_SeqFactory$Delegate__f_delegate = null;
  $ct_sc_SeqFactory$Delegate__sc_SeqFactory__(this, $m_sjs_js_WrappedArray$())
}
$c_scm_Buffer$.prototype = new $h_sc_SeqFactory$Delegate();
$c_scm_Buffer$.prototype.constructor = $c_scm_Buffer$;
/** @constructor */
function $h_scm_Buffer$() {
  /*<skip>*/
}
$h_scm_Buffer$.prototype = $c_scm_Buffer$.prototype;
var $d_scm_Buffer$ = new $TypeData().initClass({
  scm_Buffer$: 0
}, false, "scala.collection.mutable.Buffer$", {
  scm_Buffer$: 1,
  sc_SeqFactory$Delegate: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_Buffer$.prototype.$classData = $d_scm_Buffer$;
var $n_scm_Buffer$;
function $m_scm_Buffer$() {
  if ((!$n_scm_Buffer$)) {
    $n_scm_Buffer$ = new $c_scm_Buffer$()
  };
  return $n_scm_Buffer$
}
function $ct_scm_HashMap$HashMapIterator__scm_HashMap__($thiz, outer) {
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    $thiz.scm_HashMap$HashMapIterator__f_$outer = outer
  };
  $thiz.scm_HashMap$HashMapIterator__f_i = 0;
  $thiz.scm_HashMap$HashMapIterator__f_node = null;
  $thiz.scm_HashMap$HashMapIterator__f_len = outer.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length;
  return $thiz
}
/** @constructor */
function $c_scm_HashMap$HashMapIterator() {
  this.scm_HashMap$HashMapIterator__f_i = 0;
  this.scm_HashMap$HashMapIterator__f_node = null;
  this.scm_HashMap$HashMapIterator__f_len = 0;
  this.scm_HashMap$HashMapIterator__f_$outer = null
}
$c_scm_HashMap$HashMapIterator.prototype = new $h_sc_AbstractIterator();
$c_scm_HashMap$HashMapIterator.prototype.constructor = $c_scm_HashMap$HashMapIterator;
/** @constructor */
function $h_scm_HashMap$HashMapIterator() {
  /*<skip>*/
}
$h_scm_HashMap$HashMapIterator.prototype = $c_scm_HashMap$HashMapIterator.prototype;
$c_scm_HashMap$HashMapIterator.prototype.hasNext__Z = (function() {
  if ((this.scm_HashMap$HashMapIterator__f_node !== null)) {
    return true
  } else {
    while ((this.scm_HashMap$HashMapIterator__f_i < this.scm_HashMap$HashMapIterator__f_len)) {
      var n = this.scm_HashMap$HashMapIterator__f_$outer.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get(this.scm_HashMap$HashMapIterator__f_i);
      this.scm_HashMap$HashMapIterator__f_i = ((1 + this.scm_HashMap$HashMapIterator__f_i) | 0);
      if ((n !== null)) {
        this.scm_HashMap$HashMapIterator__f_node = n;
        return true
      }
    };
    return false
  }
});
$c_scm_HashMap$HashMapIterator.prototype.next__O = (function() {
  if ((!this.hasNext__Z())) {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  } else {
    var r = this.extract__scm_HashMap$Node__O(this.scm_HashMap$HashMapIterator__f_node);
    this.scm_HashMap$HashMapIterator__f_node = this.scm_HashMap$HashMapIterator__f_node.scm_HashMap$Node__f__next;
    return r
  }
});
/** @constructor */
function $c_scm_IndexedSeq$() {
  this.sc_SeqFactory$Delegate__f_delegate = null;
  $ct_sc_SeqFactory$Delegate__sc_SeqFactory__(this, $m_scm_ArrayBuffer$())
}
$c_scm_IndexedSeq$.prototype = new $h_sc_SeqFactory$Delegate();
$c_scm_IndexedSeq$.prototype.constructor = $c_scm_IndexedSeq$;
/** @constructor */
function $h_scm_IndexedSeq$() {
  /*<skip>*/
}
$h_scm_IndexedSeq$.prototype = $c_scm_IndexedSeq$.prototype;
var $d_scm_IndexedSeq$ = new $TypeData().initClass({
  scm_IndexedSeq$: 0
}, false, "scala.collection.mutable.IndexedSeq$", {
  scm_IndexedSeq$: 1,
  sc_SeqFactory$Delegate: 1,
  O: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_scm_IndexedSeq$.prototype.$classData = $d_scm_IndexedSeq$;
var $n_scm_IndexedSeq$;
function $m_scm_IndexedSeq$() {
  if ((!$n_scm_IndexedSeq$)) {
    $n_scm_IndexedSeq$ = new $c_scm_IndexedSeq$()
  };
  return $n_scm_IndexedSeq$
}
function $f_s_reflect_ClassTag__equals__O__Z($thiz, x) {
  if ($is_s_reflect_ClassTag(x)) {
    var x$2 = $thiz.runtimeClass__jl_Class();
    var x$3 = $as_s_reflect_ClassTag(x).runtimeClass__jl_Class();
    return (x$2 === x$3)
  } else {
    return false
  }
}
function $p_s_reflect_ClassTag__prettyprint$1__jl_Class__T($thiz, clazz) {
  return (clazz.isArray__Z() ? (("Array[" + $p_s_reflect_ClassTag__prettyprint$1__jl_Class__T($thiz, clazz.getComponentType__jl_Class())) + "]") : clazz.getName__T())
}
function $is_s_reflect_ClassTag(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.s_reflect_ClassTag)))
}
function $as_s_reflect_ClassTag(obj) {
  return (($is_s_reflect_ClassTag(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.reflect.ClassTag"))
}
function $isArrayOf_s_reflect_ClassTag(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.s_reflect_ClassTag)))
}
function $asArrayOf_s_reflect_ClassTag(obj, depth) {
  return (($isArrayOf_s_reflect_ClassTag(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.reflect.ClassTag;", depth))
}
/** @constructor */
function $c_sr_ScalaRunTime$$anon$1(x$2) {
  this.sr_ScalaRunTime$$anon$1__f_c = 0;
  this.sr_ScalaRunTime$$anon$1__f_cmax = 0;
  this.sr_ScalaRunTime$$anon$1__f_x$2 = null;
  this.sr_ScalaRunTime$$anon$1__f_x$2 = x$2;
  this.sr_ScalaRunTime$$anon$1__f_c = 0;
  this.sr_ScalaRunTime$$anon$1__f_cmax = x$2.productArity__I()
}
$c_sr_ScalaRunTime$$anon$1.prototype = new $h_sc_AbstractIterator();
$c_sr_ScalaRunTime$$anon$1.prototype.constructor = $c_sr_ScalaRunTime$$anon$1;
/** @constructor */
function $h_sr_ScalaRunTime$$anon$1() {
  /*<skip>*/
}
$h_sr_ScalaRunTime$$anon$1.prototype = $c_sr_ScalaRunTime$$anon$1.prototype;
$c_sr_ScalaRunTime$$anon$1.prototype.hasNext__Z = (function() {
  return (this.sr_ScalaRunTime$$anon$1__f_c < this.sr_ScalaRunTime$$anon$1__f_cmax)
});
$c_sr_ScalaRunTime$$anon$1.prototype.next__O = (function() {
  var result = this.sr_ScalaRunTime$$anon$1__f_x$2.productElement__I__O(this.sr_ScalaRunTime$$anon$1__f_c);
  this.sr_ScalaRunTime$$anon$1__f_c = ((1 + this.sr_ScalaRunTime$$anon$1__f_c) | 0);
  return result
});
var $d_sr_ScalaRunTime$$anon$1 = new $TypeData().initClass({
  sr_ScalaRunTime$$anon$1: 0
}, false, "scala.runtime.ScalaRunTime$$anon$1", {
  sr_ScalaRunTime$$anon$1: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sr_ScalaRunTime$$anon$1.prototype.$classData = $d_sr_ScalaRunTime$$anon$1;
/** @constructor */
function $c_sjs_js_WrappedArray$() {
  /*<skip>*/
}
$c_sjs_js_WrappedArray$.prototype = new $h_O();
$c_sjs_js_WrappedArray$.prototype.constructor = $c_sjs_js_WrappedArray$;
/** @constructor */
function $h_sjs_js_WrappedArray$() {
  /*<skip>*/
}
$h_sjs_js_WrappedArray$.prototype = $c_sjs_js_WrappedArray$.prototype;
$c_sjs_js_WrappedArray$.prototype.from__sc_IterableOnce__sjs_js_WrappedArray = (function(source) {
  var this$1 = $ct_sjs_js_WrappedArray__(new $c_sjs_js_WrappedArray());
  return $as_sjs_js_WrappedArray($as_scm_Builder($f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this$1, source)).result__O())
});
$c_sjs_js_WrappedArray$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sjs_js_WrappedArray(source)
});
var $d_sjs_js_WrappedArray$ = new $TypeData().initClass({
  sjs_js_WrappedArray$: 0
}, false, "scala.scalajs.js.WrappedArray$", {
  sjs_js_WrappedArray$: 1,
  O: 1,
  sc_StrictOptimizedSeqFactory: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sjs_js_WrappedArray$.prototype.$classData = $d_sjs_js_WrappedArray$;
var $n_sjs_js_WrappedArray$;
function $m_sjs_js_WrappedArray$() {
  if ((!$n_sjs_js_WrappedArray$)) {
    $n_sjs_js_WrappedArray$ = new $c_sjs_js_WrappedArray$()
  };
  return $n_sjs_js_WrappedArray$
}
/** @constructor */
function $c_sjsr_WrappedVarArgs$() {
  /*<skip>*/
}
$c_sjsr_WrappedVarArgs$.prototype = new $h_O();
$c_sjsr_WrappedVarArgs$.prototype.constructor = $c_sjsr_WrappedVarArgs$;
/** @constructor */
function $h_sjsr_WrappedVarArgs$() {
  /*<skip>*/
}
$h_sjsr_WrappedVarArgs$.prototype = $c_sjsr_WrappedVarArgs$.prototype;
$c_sjsr_WrappedVarArgs$.prototype.from__sc_IterableOnce__sjsr_WrappedVarArgs = (function(source) {
  var this$1 = this.newBuilder__scm_Builder();
  return $as_sjsr_WrappedVarArgs($as_scm_Builder(this$1.addAll__sc_IterableOnce__scm_Growable(source)).result__O())
});
$c_sjsr_WrappedVarArgs$.prototype.newBuilder__scm_Builder = (function() {
  var array = [];
  var this$4 = $ct_sjs_js_WrappedArray__sjs_js_Array__(new $c_sjs_js_WrappedArray(), array);
  var f = new $c_sjsr_AnonFunction1(((this$2) => ((x$1$2) => {
    var x$1 = $as_sjs_js_WrappedArray(x$1$2);
    return new $c_sjsr_WrappedVarArgs(x$1.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array)
  }))(this));
  return new $c_scm_Builder$$anon$1(this$4, f)
});
$c_sjsr_WrappedVarArgs$.prototype.from__sc_IterableOnce__O = (function(source) {
  return this.from__sc_IterableOnce__sjsr_WrappedVarArgs(source)
});
var $d_sjsr_WrappedVarArgs$ = new $TypeData().initClass({
  sjsr_WrappedVarArgs$: 0
}, false, "scala.scalajs.runtime.WrappedVarArgs$", {
  sjsr_WrappedVarArgs$: 1,
  O: 1,
  sc_StrictOptimizedSeqFactory: 1,
  sc_SeqFactory: 1,
  sc_IterableFactory: 1,
  Ljava_io_Serializable: 1
});
$c_sjsr_WrappedVarArgs$.prototype.$classData = $d_sjsr_WrappedVarArgs$;
var $n_sjsr_WrappedVarArgs$;
function $m_sjsr_WrappedVarArgs$() {
  if ((!$n_sjsr_WrappedVarArgs$)) {
    $n_sjsr_WrappedVarArgs$ = new $c_sjsr_WrappedVarArgs$()
  };
  return $n_sjsr_WrappedVarArgs$
}
function $ct_jl_ArrayIndexOutOfBoundsException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
function $ct_jl_ArrayIndexOutOfBoundsException__($thiz) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, null, null, true, true);
  return $thiz
}
class $c_jl_ArrayIndexOutOfBoundsException extends $c_jl_IndexOutOfBoundsException {
}
var $d_jl_ArrayIndexOutOfBoundsException = new $TypeData().initClass({
  jl_ArrayIndexOutOfBoundsException: 0
}, false, "java.lang.ArrayIndexOutOfBoundsException", {
  jl_ArrayIndexOutOfBoundsException: 1,
  jl_IndexOutOfBoundsException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_ArrayIndexOutOfBoundsException.prototype.$classData = $d_jl_ArrayIndexOutOfBoundsException;
class $c_jl_NumberFormatException extends $c_jl_IllegalArgumentException {
  constructor(s) {
    super();
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, s, null, true, true)
  };
}
var $d_jl_NumberFormatException = new $TypeData().initClass({
  jl_NumberFormatException: 0
}, false, "java.lang.NumberFormatException", {
  jl_NumberFormatException: 1,
  jl_IllegalArgumentException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_NumberFormatException.prototype.$classData = $d_jl_NumberFormatException;
function $ct_jl_StringIndexOutOfBoundsException__T__($thiz, s) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
function $ct_jl_StringIndexOutOfBoundsException__I__($thiz, index) {
  var s = ("String index out of range: " + index);
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, s, null, true, true);
  return $thiz
}
function $ct_jl_StringIndexOutOfBoundsException__($thiz) {
  $ct_jl_Throwable__T__jl_Throwable__Z__Z__($thiz, null, null, true, true);
  return $thiz
}
class $c_jl_StringIndexOutOfBoundsException extends $c_jl_IndexOutOfBoundsException {
}
var $d_jl_StringIndexOutOfBoundsException = new $TypeData().initClass({
  jl_StringIndexOutOfBoundsException: 0
}, false, "java.lang.StringIndexOutOfBoundsException", {
  jl_StringIndexOutOfBoundsException: 1,
  jl_IndexOutOfBoundsException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1
});
$c_jl_StringIndexOutOfBoundsException.prototype.$classData = $d_jl_StringIndexOutOfBoundsException;
/** @constructor */
function $c_Ljava_time_Month(name, ordinal) {
  this.jl_Enum__f__name = null;
  this.jl_Enum__f__ordinal = 0;
  this.Ljava_time_Month__f_ordinal = 0;
  this.Ljava_time_Month__f_ordinal = ordinal;
  $ct_jl_Enum__T__I__(this, name, ordinal)
}
$c_Ljava_time_Month.prototype = new $h_jl_Enum();
$c_Ljava_time_Month.prototype.constructor = $c_Ljava_time_Month;
/** @constructor */
function $h_Ljava_time_Month() {
  /*<skip>*/
}
$h_Ljava_time_Month.prototype = $c_Ljava_time_Month.prototype;
$c_Ljava_time_Month.prototype.getValue__I = (function() {
  return ((1 + this.Ljava_time_Month__f_ordinal) | 0)
});
$c_Ljava_time_Month.prototype.length__Z__I = (function(leapYear) {
  var x = $m_Ljava_time_Month$().FEBRUARY__Ljava_time_Month();
  if (((x !== null) && (x === this))) {
    return (leapYear ? 29 : 28)
  } else {
    var x$3 = $m_Ljava_time_Month$().APRIL__Ljava_time_Month();
    if (((x$3 !== null) && (x$3 === this))) {
      var $$x1 = true
    } else {
      var x$5 = $m_Ljava_time_Month$().JUNE__Ljava_time_Month();
      if (((x$5 !== null) && (x$5 === this))) {
        var $$x1 = true
      } else {
        var x$7 = $m_Ljava_time_Month$().SEPTEMBER__Ljava_time_Month();
        if (((x$7 !== null) && (x$7 === this))) {
          var $$x1 = true
        } else {
          var x$9 = $m_Ljava_time_Month$().NOVEMBER__Ljava_time_Month();
          var $$x1 = ((x$9 !== null) && (x$9 === this))
        }
      }
    };
    if ($$x1) {
      return 30
    } else {
      return 31
    }
  }
});
var $d_Ljava_time_Month = new $TypeData().initClass({
  Ljava_time_Month: 0
}, false, "java.time.Month", {
  Ljava_time_Month: 1,
  jl_Enum: 1,
  O: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1,
  Ljava_time_temporal_TemporalAccessor: 1,
  Ljava_time_temporal_TemporalAdjuster: 1
});
$c_Ljava_time_Month.prototype.$classData = $d_Ljava_time_Month;
/** @constructor */
function $c_s_None$() {
  /*<skip>*/
}
$c_s_None$.prototype = new $h_s_Option();
$c_s_None$.prototype.constructor = $c_s_None$;
/** @constructor */
function $h_s_None$() {
  /*<skip>*/
}
$h_s_None$.prototype = $c_s_None$.prototype;
$c_s_None$.prototype.get__E = (function() {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "None.get")
});
$c_s_None$.prototype.productPrefix__T = (function() {
  return "None"
});
$c_s_None$.prototype.productArity__I = (function() {
  return 0
});
$c_s_None$.prototype.productElement__I__O = (function(x$1) {
  return $m_sr_Statics$().ioobe__I__O(x$1)
});
$c_s_None$.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_s_None$.prototype.hashCode__I = (function() {
  return 2433880
});
$c_s_None$.prototype.toString__T = (function() {
  return "None"
});
$c_s_None$.prototype.get__O = (function() {
  this.get__E()
});
var $d_s_None$ = new $TypeData().initClass({
  s_None$: 0
}, false, "scala.None$", {
  s_None$: 1,
  s_Option: 1,
  O: 1,
  sc_IterableOnce: 1,
  s_Product: 1,
  s_Equals: 1,
  Ljava_io_Serializable: 1
});
$c_s_None$.prototype.$classData = $d_s_None$;
var $n_s_None$;
function $m_s_None$() {
  if ((!$n_s_None$)) {
    $n_s_None$ = new $c_s_None$()
  };
  return $n_s_None$
}
/** @constructor */
function $c_s_Some(value) {
  this.s_Some__f_value = null;
  this.s_Some__f_value = value
}
$c_s_Some.prototype = new $h_s_Option();
$c_s_Some.prototype.constructor = $c_s_Some;
/** @constructor */
function $h_s_Some() {
  /*<skip>*/
}
$h_s_Some.prototype = $c_s_Some.prototype;
$c_s_Some.prototype.get__O = (function() {
  return this.s_Some__f_value
});
$c_s_Some.prototype.productPrefix__T = (function() {
  return "Some"
});
$c_s_Some.prototype.productArity__I = (function() {
  return 1
});
$c_s_Some.prototype.productElement__I__O = (function(x$1) {
  return ((x$1 === 0) ? this.s_Some__f_value : $m_sr_Statics$().ioobe__I__O(x$1))
});
$c_s_Some.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_s_Some.prototype.hashCode__I = (function() {
  var this$2 = $m_s_util_hashing_MurmurHash3$();
  return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
});
$c_s_Some.prototype.toString__T = (function() {
  return $m_sr_ScalaRunTime$()._toString__s_Product__T(this)
});
$c_s_Some.prototype.equals__O__Z = (function(x$1) {
  if ((this === x$1)) {
    return true
  } else if ((x$1 instanceof $c_s_Some)) {
    var Some$1 = $as_s_Some(x$1);
    return $m_sr_BoxesRunTime$().equals__O__O__Z(this.s_Some__f_value, Some$1.s_Some__f_value)
  } else {
    return false
  }
});
function $as_s_Some(obj) {
  return (((obj instanceof $c_s_Some) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.Some"))
}
function $isArrayOf_s_Some(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.s_Some)))
}
function $asArrayOf_s_Some(obj, depth) {
  return (($isArrayOf_s_Some(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.Some;", depth))
}
var $d_s_Some = new $TypeData().initClass({
  s_Some: 0
}, false, "scala.Some", {
  s_Some: 1,
  s_Option: 1,
  O: 1,
  sc_IterableOnce: 1,
  s_Product: 1,
  s_Equals: 1,
  Ljava_io_Serializable: 1
});
$c_s_Some.prototype.$classData = $d_s_Some;
/** @constructor */
function $c_sc_AbstractIterable() {
  /*<skip>*/
}
$c_sc_AbstractIterable.prototype = new $h_O();
$c_sc_AbstractIterable.prototype.constructor = $c_sc_AbstractIterable;
/** @constructor */
function $h_sc_AbstractIterable() {
  /*<skip>*/
}
$h_sc_AbstractIterable.prototype = $c_sc_AbstractIterable.prototype;
$c_sc_AbstractIterable.prototype.className__T = (function() {
  return this.stringPrefix__T()
});
$c_sc_AbstractIterable.prototype.fromSpecific__sc_IterableOnce__sc_IterableOps = (function(coll) {
  return $as_sc_IterableOps(this.iterableFactory__sc_IterableFactory().from__sc_IterableOnce__O(coll))
});
$c_sc_AbstractIterable.prototype.head__O = (function() {
  return this.iterator__sc_Iterator().next__O()
});
$c_sc_AbstractIterable.prototype.drop__I__O = (function(n) {
  return $f_sc_IterableOps__drop__I__O(this, n)
});
$c_sc_AbstractIterable.prototype.tail__O = (function() {
  return $f_sc_IterableOps__tail__O(this)
});
$c_sc_AbstractIterable.prototype.foreach__F1__V = (function(f) {
  $f_sc_IterableOnceOps__foreach__F1__V(this, f)
});
$c_sc_AbstractIterable.prototype.forall__F1__Z = (function(p) {
  return $f_sc_IterableOnceOps__forall__F1__Z(this, p)
});
$c_sc_AbstractIterable.prototype.exists__F1__Z = (function(p) {
  return $f_sc_IterableOnceOps__exists__F1__Z(this, p)
});
$c_sc_AbstractIterable.prototype.isEmpty__Z = (function() {
  return $f_sc_IterableOnceOps__isEmpty__Z(this)
});
$c_sc_AbstractIterable.prototype.size__I = (function() {
  return $f_sc_IterableOnceOps__size__I(this)
});
$c_sc_AbstractIterable.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sc_AbstractIterable.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sc_AbstractIterable.prototype.knownSize__I = (function() {
  return (-1)
});
$c_sc_AbstractIterable.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  return this.fromSpecific__sc_IterableOnce__sc_IterableOps(coll)
});
/** @constructor */
function $c_sc_IndexedSeqView$IndexedSeqViewIterator(self) {
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_self = null;
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current = 0;
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder = 0;
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_self = self;
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current = 0;
  this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder = self.length__I()
}
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype = new $h_sc_AbstractIterator();
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.constructor = $c_sc_IndexedSeqView$IndexedSeqViewIterator;
/** @constructor */
function $h_sc_IndexedSeqView$IndexedSeqViewIterator() {
  /*<skip>*/
}
$h_sc_IndexedSeqView$IndexedSeqViewIterator.prototype = $c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype;
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.knownSize__I = (function() {
  return this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder
});
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.hasNext__Z = (function() {
  return (this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder > 0)
});
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.next__O = (function() {
  if (this.hasNext__Z()) {
    var r = this.sc_IndexedSeqView$IndexedSeqViewIterator__f_self.apply__I__O(this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current);
    this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current = ((1 + this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current) | 0);
    this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder = (((-1) + this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder) | 0);
    return r
  } else {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  }
});
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.drop__I__sc_Iterator = (function(n) {
  if ((n > 0)) {
    this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current = ((this.sc_IndexedSeqView$IndexedSeqViewIterator__f_current + n) | 0);
    var b = ((this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder - n) | 0);
    this.sc_IndexedSeqView$IndexedSeqViewIterator__f_remainder = ((b < 0) ? 0 : b)
  };
  return this
});
var $d_sc_IndexedSeqView$IndexedSeqViewIterator = new $TypeData().initClass({
  sc_IndexedSeqView$IndexedSeqViewIterator: 0
}, false, "scala.collection.IndexedSeqView$IndexedSeqViewIterator", {
  sc_IndexedSeqView$IndexedSeqViewIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1,
  Ljava_io_Serializable: 1
});
$c_sc_IndexedSeqView$IndexedSeqViewIterator.prototype.$classData = $d_sc_IndexedSeqView$IndexedSeqViewIterator;
function $f_sc_MapOps__getOrElse__O__F0__O($thiz, key, default$1) {
  var x1 = $thiz.get__O__s_Option(key);
  if ((x1 instanceof $c_s_Some)) {
    var x2 = $as_s_Some(x1);
    var v = x2.s_Some__f_value;
    return v
  } else {
    var x = $m_s_None$();
    if ((x === x1)) {
      return default$1.apply__O()
    } else {
      throw new $c_s_MatchError(x1)
    }
  }
}
function $f_sc_MapOps__foreachEntry__F2__V($thiz, f) {
  var it = $thiz.iterator__sc_Iterator();
  while (it.hasNext__Z()) {
    var next = $as_T2(it.next__O());
    f.apply__O__O__O(next.T2__f__1, next.T2__f__2)
  }
}
function $f_sc_MapOps__default__O__O($thiz, key) {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
}
function $f_sc_MapOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder($thiz, sb, start, sep, end) {
  var this$2 = $thiz.iterator__sc_Iterator();
  var f = new $c_sjsr_AnonFunction1(((this$1) => ((x0$1$2) => {
    var x0$1 = $as_T2(x0$1$2);
    if ((x0$1 !== null)) {
      var k = x0$1.T2__f__1;
      var v = x0$1.T2__f__2;
      return ((k + " -> ") + v)
    } else {
      throw new $c_s_MatchError(x0$1)
    }
  }))($thiz));
  var this$3 = new $c_sc_Iterator$$anon$9(this$2, f);
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this$3, sb, start, sep, end)
}
function $is_sci_Iterable(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_Iterable)))
}
function $as_sci_Iterable(obj) {
  return (($is_sci_Iterable(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Iterable"))
}
function $isArrayOf_sci_Iterable(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Iterable)))
}
function $asArrayOf_sci_Iterable(obj, depth) {
  return (($isArrayOf_sci_Iterable(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Iterable;", depth))
}
/** @constructor */
function $c_sci_Map$Map2$$anon$1(outer) {
  this.sci_Map$Map2$Map2Iterator__f_i = 0;
  this.sci_Map$Map2$Map2Iterator__f_$outer = null;
  $ct_sci_Map$Map2$Map2Iterator__sci_Map$Map2__(this, outer)
}
$c_sci_Map$Map2$$anon$1.prototype = new $h_sci_Map$Map2$Map2Iterator();
$c_sci_Map$Map2$$anon$1.prototype.constructor = $c_sci_Map$Map2$$anon$1;
/** @constructor */
function $h_sci_Map$Map2$$anon$1() {
  /*<skip>*/
}
$h_sci_Map$Map2$$anon$1.prototype = $c_sci_Map$Map2$$anon$1.prototype;
var $d_sci_Map$Map2$$anon$1 = new $TypeData().initClass({
  sci_Map$Map2$$anon$1: 0
}, false, "scala.collection.immutable.Map$Map2$$anon$1", {
  sci_Map$Map2$$anon$1: 1,
  sci_Map$Map2$Map2Iterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_Map$Map2$$anon$1.prototype.$classData = $d_sci_Map$Map2$$anon$1;
/** @constructor */
function $c_sci_Map$Map3$$anon$4(outer) {
  this.sci_Map$Map3$Map3Iterator__f_i = 0;
  this.sci_Map$Map3$Map3Iterator__f_$outer = null;
  $ct_sci_Map$Map3$Map3Iterator__sci_Map$Map3__(this, outer)
}
$c_sci_Map$Map3$$anon$4.prototype = new $h_sci_Map$Map3$Map3Iterator();
$c_sci_Map$Map3$$anon$4.prototype.constructor = $c_sci_Map$Map3$$anon$4;
/** @constructor */
function $h_sci_Map$Map3$$anon$4() {
  /*<skip>*/
}
$h_sci_Map$Map3$$anon$4.prototype = $c_sci_Map$Map3$$anon$4.prototype;
var $d_sci_Map$Map3$$anon$4 = new $TypeData().initClass({
  sci_Map$Map3$$anon$4: 0
}, false, "scala.collection.immutable.Map$Map3$$anon$4", {
  sci_Map$Map3$$anon$4: 1,
  sci_Map$Map3$Map3Iterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_Map$Map3$$anon$4.prototype.$classData = $d_sci_Map$Map3$$anon$4;
/** @constructor */
function $c_sci_Map$Map4$$anon$7(outer) {
  this.sci_Map$Map4$Map4Iterator__f_i = 0;
  this.sci_Map$Map4$Map4Iterator__f_$outer = null;
  $ct_sci_Map$Map4$Map4Iterator__sci_Map$Map4__(this, outer)
}
$c_sci_Map$Map4$$anon$7.prototype = new $h_sci_Map$Map4$Map4Iterator();
$c_sci_Map$Map4$$anon$7.prototype.constructor = $c_sci_Map$Map4$$anon$7;
/** @constructor */
function $h_sci_Map$Map4$$anon$7() {
  /*<skip>*/
}
$h_sci_Map$Map4$$anon$7.prototype = $c_sci_Map$Map4$$anon$7.prototype;
var $d_sci_Map$Map4$$anon$7 = new $TypeData().initClass({
  sci_Map$Map4$$anon$7: 0
}, false, "scala.collection.immutable.Map$Map4$$anon$7", {
  sci_Map$Map4$$anon$7: 1,
  sci_Map$Map4$Map4Iterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_sci_Map$Map4$$anon$7.prototype.$classData = $d_sci_Map$Map4$$anon$7;
function $ct_sci_Set$SetNIterator__I__($thiz, n) {
  $thiz.sci_Set$SetNIterator__f_current = 0;
  $thiz.sci_Set$SetNIterator__f_remainder = n;
  return $thiz
}
/** @constructor */
function $c_sci_Set$SetNIterator() {
  this.sci_Set$SetNIterator__f_current = 0;
  this.sci_Set$SetNIterator__f_remainder = 0
}
$c_sci_Set$SetNIterator.prototype = new $h_sc_AbstractIterator();
$c_sci_Set$SetNIterator.prototype.constructor = $c_sci_Set$SetNIterator;
/** @constructor */
function $h_sci_Set$SetNIterator() {
  /*<skip>*/
}
$h_sci_Set$SetNIterator.prototype = $c_sci_Set$SetNIterator.prototype;
$c_sci_Set$SetNIterator.prototype.knownSize__I = (function() {
  return this.sci_Set$SetNIterator__f_remainder
});
$c_sci_Set$SetNIterator.prototype.hasNext__Z = (function() {
  return (this.sci_Set$SetNIterator__f_remainder > 0)
});
$c_sci_Set$SetNIterator.prototype.next__O = (function() {
  if (this.hasNext__Z()) {
    var r = this.apply__I__O(this.sci_Set$SetNIterator__f_current);
    this.sci_Set$SetNIterator__f_current = ((1 + this.sci_Set$SetNIterator__f_current) | 0);
    this.sci_Set$SetNIterator__f_remainder = (((-1) + this.sci_Set$SetNIterator__f_remainder) | 0);
    return r
  } else {
    return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty.next__O()
  }
});
$c_sci_Set$SetNIterator.prototype.drop__I__sc_Iterator = (function(n) {
  if ((n > 0)) {
    this.sci_Set$SetNIterator__f_current = ((this.sci_Set$SetNIterator__f_current + n) | 0);
    var b = ((this.sci_Set$SetNIterator__f_remainder - n) | 0);
    this.sci_Set$SetNIterator__f_remainder = ((b < 0) ? 0 : b)
  };
  return this
});
/** @constructor */
function $c_scm_HashMap$$anon$1(outer) {
  this.scm_HashMap$HashMapIterator__f_i = 0;
  this.scm_HashMap$HashMapIterator__f_node = null;
  this.scm_HashMap$HashMapIterator__f_len = 0;
  this.scm_HashMap$HashMapIterator__f_$outer = null;
  $ct_scm_HashMap$HashMapIterator__scm_HashMap__(this, outer)
}
$c_scm_HashMap$$anon$1.prototype = new $h_scm_HashMap$HashMapIterator();
$c_scm_HashMap$$anon$1.prototype.constructor = $c_scm_HashMap$$anon$1;
/** @constructor */
function $h_scm_HashMap$$anon$1() {
  /*<skip>*/
}
$h_scm_HashMap$$anon$1.prototype = $c_scm_HashMap$$anon$1.prototype;
$c_scm_HashMap$$anon$1.prototype.extract__scm_HashMap$Node__O = (function(nd) {
  return new $c_T2(nd.scm_HashMap$Node__f__key, nd.scm_HashMap$Node__f__value)
});
var $d_scm_HashMap$$anon$1 = new $TypeData().initClass({
  scm_HashMap$$anon$1: 0
}, false, "scala.collection.mutable.HashMap$$anon$1", {
  scm_HashMap$$anon$1: 1,
  scm_HashMap$HashMapIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_scm_HashMap$$anon$1.prototype.$classData = $d_scm_HashMap$$anon$1;
/** @constructor */
function $c_scm_HashMap$$anon$4(outer) {
  this.scm_HashMap$HashMapIterator__f_i = 0;
  this.scm_HashMap$HashMapIterator__f_node = null;
  this.scm_HashMap$HashMapIterator__f_len = 0;
  this.scm_HashMap$HashMapIterator__f_$outer = null;
  $ct_scm_HashMap$HashMapIterator__scm_HashMap__(this, outer)
}
$c_scm_HashMap$$anon$4.prototype = new $h_scm_HashMap$HashMapIterator();
$c_scm_HashMap$$anon$4.prototype.constructor = $c_scm_HashMap$$anon$4;
/** @constructor */
function $h_scm_HashMap$$anon$4() {
  /*<skip>*/
}
$h_scm_HashMap$$anon$4.prototype = $c_scm_HashMap$$anon$4.prototype;
$c_scm_HashMap$$anon$4.prototype.extract__scm_HashMap$Node__O = (function(nd) {
  return nd
});
var $d_scm_HashMap$$anon$4 = new $TypeData().initClass({
  scm_HashMap$$anon$4: 0
}, false, "scala.collection.mutable.HashMap$$anon$4", {
  scm_HashMap$$anon$4: 1,
  scm_HashMap$HashMapIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_scm_HashMap$$anon$4.prototype.$classData = $d_scm_HashMap$$anon$4;
/** @constructor */
function $c_scm_HashMap$$anon$5(outer) {
  this.scm_HashMap$HashMapIterator__f_i = 0;
  this.scm_HashMap$HashMapIterator__f_node = null;
  this.scm_HashMap$HashMapIterator__f_len = 0;
  this.scm_HashMap$HashMapIterator__f_$outer = null;
  this.scm_HashMap$$anon$5__f_hash = 0;
  this.scm_HashMap$$anon$5__f_$outer = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.scm_HashMap$$anon$5__f_$outer = outer
  };
  $ct_scm_HashMap$HashMapIterator__scm_HashMap__(this, outer);
  this.scm_HashMap$$anon$5__f_hash = 0
}
$c_scm_HashMap$$anon$5.prototype = new $h_scm_HashMap$HashMapIterator();
$c_scm_HashMap$$anon$5.prototype.constructor = $c_scm_HashMap$$anon$5;
/** @constructor */
function $h_scm_HashMap$$anon$5() {
  /*<skip>*/
}
$h_scm_HashMap$$anon$5.prototype = $c_scm_HashMap$$anon$5.prototype;
$c_scm_HashMap$$anon$5.prototype.hashCode__I = (function() {
  return this.scm_HashMap$$anon$5__f_hash
});
$c_scm_HashMap$$anon$5.prototype.extract__scm_HashMap$Node__O = (function(nd) {
  var $$x1 = $m_s_util_hashing_MurmurHash3$();
  var improvedHash = nd.scm_HashMap$Node__f__hash;
  var x = nd.scm_HashMap$Node__f__value;
  this.scm_HashMap$$anon$5__f_hash = $$x1.tuple2Hash__O__O__I((improvedHash ^ ((improvedHash >>> 16) | 0)), $m_sr_Statics$().anyHash__O__I(x));
  return this
});
var $d_scm_HashMap$$anon$5 = new $TypeData().initClass({
  scm_HashMap$$anon$5: 0
}, false, "scala.collection.mutable.HashMap$$anon$5", {
  scm_HashMap$$anon$5: 1,
  scm_HashMap$HashMapIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1
});
$c_scm_HashMap$$anon$5.prototype.$classData = $d_scm_HashMap$$anon$5;
/** @constructor */
function $c_s_reflect_ClassTag$GenericClassTag(runtimeClass) {
  this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass = null;
  this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass = runtimeClass
}
$c_s_reflect_ClassTag$GenericClassTag.prototype = new $h_O();
$c_s_reflect_ClassTag$GenericClassTag.prototype.constructor = $c_s_reflect_ClassTag$GenericClassTag;
/** @constructor */
function $h_s_reflect_ClassTag$GenericClassTag() {
  /*<skip>*/
}
$h_s_reflect_ClassTag$GenericClassTag.prototype = $c_s_reflect_ClassTag$GenericClassTag.prototype;
$c_s_reflect_ClassTag$GenericClassTag.prototype.equals__O__Z = (function(x) {
  return $f_s_reflect_ClassTag__equals__O__Z(this, x)
});
$c_s_reflect_ClassTag$GenericClassTag.prototype.hashCode__I = (function() {
  var x = this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass;
  return $m_sr_Statics$().anyHash__O__I(x)
});
$c_s_reflect_ClassTag$GenericClassTag.prototype.toString__T = (function() {
  return $p_s_reflect_ClassTag__prettyprint$1__jl_Class__T(this, this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass)
});
$c_s_reflect_ClassTag$GenericClassTag.prototype.runtimeClass__jl_Class = (function() {
  return this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass
});
$c_s_reflect_ClassTag$GenericClassTag.prototype.newArray__I__O = (function(len) {
  var componentType = this.s_reflect_ClassTag$GenericClassTag__f_runtimeClass;
  return $m_jl_reflect_Array$().newInstance__jl_Class__I__O(componentType, len)
});
var $d_s_reflect_ClassTag$GenericClassTag = new $TypeData().initClass({
  s_reflect_ClassTag$GenericClassTag: 0
}, false, "scala.reflect.ClassTag$GenericClassTag", {
  s_reflect_ClassTag$GenericClassTag: 1,
  O: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ClassTag$GenericClassTag.prototype.$classData = $d_s_reflect_ClassTag$GenericClassTag;
function $ct_Ljava_io_PrintStream__Ljava_io_OutputStream__Z__Ljava_nio_charset_Charset__($thiz, _out, autoFlush, charset) {
  $thiz.Ljava_io_PrintStream__f_autoFlush = autoFlush;
  $thiz.Ljava_io_PrintStream__f_charset = charset;
  $ct_Ljava_io_FilterOutputStream__Ljava_io_OutputStream__($thiz, _out);
  $thiz.Ljava_io_PrintStream__f_closing = false;
  $thiz.Ljava_io_PrintStream__f_java$io$PrintStream$$closed = false;
  $thiz.Ljava_io_PrintStream__f_errorFlag = false;
  return $thiz
}
/** @constructor */
function $c_Ljava_io_PrintStream() {
  this.Ljava_io_FilterOutputStream__f_out = null;
  this.Ljava_io_PrintStream__f_encoder = null;
  this.Ljava_io_PrintStream__f_autoFlush = false;
  this.Ljava_io_PrintStream__f_charset = null;
  this.Ljava_io_PrintStream__f_closing = false;
  this.Ljava_io_PrintStream__f_java$io$PrintStream$$closed = false;
  this.Ljava_io_PrintStream__f_errorFlag = false;
  this.Ljava_io_PrintStream__f_bitmap$0 = false
}
$c_Ljava_io_PrintStream.prototype = new $h_Ljava_io_FilterOutputStream();
$c_Ljava_io_PrintStream.prototype.constructor = $c_Ljava_io_PrintStream;
/** @constructor */
function $h_Ljava_io_PrintStream() {
  /*<skip>*/
}
$h_Ljava_io_PrintStream.prototype = $c_Ljava_io_PrintStream.prototype;
function $as_Ljava_io_PrintStream(obj) {
  return (((obj instanceof $c_Ljava_io_PrintStream) || (obj === null)) ? obj : $throwClassCastException(obj, "java.io.PrintStream"))
}
function $isArrayOf_Ljava_io_PrintStream(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_io_PrintStream)))
}
function $asArrayOf_Ljava_io_PrintStream(obj, depth) {
  return (($isArrayOf_Ljava_io_PrintStream(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.io.PrintStream;", depth))
}
function $p_Ljava_time_LocalTime__hour__B($thiz) {
  if (((((1 & $thiz.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 410")
  };
  return $thiz.Ljava_time_LocalTime__f_hour
}
function $p_Ljava_time_LocalTime__minute__B($thiz) {
  if (((((2 & $thiz.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 413")
  };
  return $thiz.Ljava_time_LocalTime__f_minute
}
function $p_Ljava_time_LocalTime__second__B($thiz) {
  if (((((4 & $thiz.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalTime.scala: 416")
  };
  return $thiz.Ljava_time_LocalTime__f_second
}
/** @constructor */
function $c_Ljava_time_LocalTime(_hour, _minute, _second, nano) {
  this.Ljava_time_LocalTime__f_nano = 0;
  this.Ljava_time_LocalTime__f_hour = 0;
  this.Ljava_time_LocalTime__f_minute = 0;
  this.Ljava_time_LocalTime__f_second = 0;
  this.Ljava_time_LocalTime__f_bitmap$init$0 = 0;
  this.Ljava_time_LocalTime__f_nano = nano;
  this.Ljava_time_LocalTime__f_hour = ((_hour << 24) >> 24);
  this.Ljava_time_LocalTime__f_bitmap$init$0 = (((1 | this.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24);
  this.Ljava_time_LocalTime__f_minute = ((_minute << 24) >> 24);
  this.Ljava_time_LocalTime__f_bitmap$init$0 = (((2 | this.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24);
  this.Ljava_time_LocalTime__f_second = ((_second << 24) >> 24);
  this.Ljava_time_LocalTime__f_bitmap$init$0 = (((4 | this.Ljava_time_LocalTime__f_bitmap$init$0) << 24) >> 24)
}
$c_Ljava_time_LocalTime.prototype = new $h_O();
$c_Ljava_time_LocalTime.prototype.constructor = $c_Ljava_time_LocalTime;
/** @constructor */
function $h_Ljava_time_LocalTime() {
  /*<skip>*/
}
$h_Ljava_time_LocalTime.prototype = $c_Ljava_time_LocalTime.prototype;
$c_Ljava_time_LocalTime.prototype.toNanoOfDay__J = (function() {
  var value = $p_Ljava_time_LocalTime__hour__B(this);
  var hi = (value >> 31);
  var b = $m_Ljava_time_LocalTime$().NANOS_PER_HOUR__J();
  var blo = b.RTLong__f_lo;
  var a0 = (65535 & value);
  var a1 = ((value >>> 16) | 0);
  var b0 = (65535 & blo);
  var b1 = ((blo >>> 16) | 0);
  var a0b0 = $imul(a0, b0);
  var a1b0 = $imul(a1, b0);
  var a0b1 = $imul(a0, b1);
  var lo = ((a0b0 + (((a1b0 + a0b1) | 0) << 16)) | 0);
  var c1part = ((((a0b0 >>> 16) | 0) + a0b1) | 0);
  var hi$1 = (((((((($imul(value, b.RTLong__f_hi) + $imul(hi, blo)) | 0) + $imul(a1, b1)) | 0) + ((c1part >>> 16) | 0)) | 0) + (((((65535 & c1part) + a1b0) | 0) >>> 16) | 0)) | 0);
  var total__lo = lo;
  var total__hi = hi$1;
  var this$3__lo = total__lo;
  var this$3__hi = total__hi;
  var value$1 = $p_Ljava_time_LocalTime__minute__B(this);
  var hi$2 = (value$1 >> 31);
  var b$1 = $m_Ljava_time_LocalTime$().NANOS_PER_MINUTE__J();
  var blo$1 = b$1.RTLong__f_lo;
  var a0$1 = (65535 & value$1);
  var a1$1 = ((value$1 >>> 16) | 0);
  var b0$1 = (65535 & blo$1);
  var b1$1 = ((blo$1 >>> 16) | 0);
  var a0b0$1 = $imul(a0$1, b0$1);
  var a1b0$1 = $imul(a1$1, b0$1);
  var a0b1$1 = $imul(a0$1, b1$1);
  var lo$1 = ((a0b0$1 + (((a1b0$1 + a0b1$1) | 0) << 16)) | 0);
  var c1part$1 = ((((a0b0$1 >>> 16) | 0) + a0b1$1) | 0);
  var hi$3 = (((((((($imul(value$1, b$1.RTLong__f_hi) + $imul(hi$2, blo$1)) | 0) + $imul(a1$1, b1$1)) | 0) + ((c1part$1 >>> 16) | 0)) | 0) + (((((65535 & c1part$1) + a1b0$1) | 0) >>> 16) | 0)) | 0);
  var alo = this$3__lo;
  var ahi = this$3__hi;
  var lo$2 = ((alo + lo$1) | 0);
  var hi$4 = ((((-2147483648) ^ lo$2) < ((-2147483648) ^ alo)) ? ((1 + ((ahi + hi$3) | 0)) | 0) : ((ahi + hi$3) | 0));
  var $$x1__lo = lo$2;
  var $$x1__hi = hi$4;
  total__lo = $$x1__lo;
  total__hi = $$x1__hi;
  var this$6__lo = total__lo;
  var this$6__hi = total__hi;
  var value$2 = $p_Ljava_time_LocalTime__second__B(this);
  var hi$5 = (value$2 >> 31);
  var b$2 = $m_Ljava_time_LocalTime$().NANOS_PER_SECOND__J();
  var blo$2 = b$2.RTLong__f_lo;
  var a0$2 = (65535 & value$2);
  var a1$2 = ((value$2 >>> 16) | 0);
  var b0$2 = (65535 & blo$2);
  var b1$2 = ((blo$2 >>> 16) | 0);
  var a0b0$2 = $imul(a0$2, b0$2);
  var a1b0$2 = $imul(a1$2, b0$2);
  var a0b1$2 = $imul(a0$2, b1$2);
  var lo$3 = ((a0b0$2 + (((a1b0$2 + a0b1$2) | 0) << 16)) | 0);
  var c1part$2 = ((((a0b0$2 >>> 16) | 0) + a0b1$2) | 0);
  var hi$6 = (((((((($imul(value$2, b$2.RTLong__f_hi) + $imul(hi$5, blo$2)) | 0) + $imul(a1$2, b1$2)) | 0) + ((c1part$2 >>> 16) | 0)) | 0) + (((((65535 & c1part$2) + a1b0$2) | 0) >>> 16) | 0)) | 0);
  var alo$1 = this$6__lo;
  var ahi$1 = this$6__hi;
  var lo$4 = ((alo$1 + lo$3) | 0);
  var hi$7 = ((((-2147483648) ^ lo$4) < ((-2147483648) ^ alo$1)) ? ((1 + ((ahi$1 + hi$6) | 0)) | 0) : ((ahi$1 + hi$6) | 0));
  var $$x2__lo = lo$4;
  var $$x2__hi = hi$7;
  total__lo = $$x2__lo;
  total__hi = $$x2__hi;
  var this$9__lo = total__lo;
  var this$9__hi = total__hi;
  var value$3 = this.Ljava_time_LocalTime__f_nano;
  var hi$8 = (value$3 >> 31);
  var alo$2 = this$9__lo;
  var ahi$2 = this$9__hi;
  var lo$5 = ((alo$2 + value$3) | 0);
  var hi$9 = ((((-2147483648) ^ lo$5) < ((-2147483648) ^ alo$2)) ? ((1 + ((ahi$2 + hi$8) | 0)) | 0) : ((ahi$2 + hi$8) | 0));
  var $$x3__lo = lo$5;
  var $$x3__hi = hi$9;
  total__lo = $$x3__lo;
  total__hi = $$x3__hi;
  return new $c_RTLong(total__lo, total__hi)
});
$c_Ljava_time_LocalTime.prototype.equals__O__Z = (function(obj) {
  if ((obj instanceof $c_Ljava_time_LocalTime)) {
    var x2 = $as_Ljava_time_LocalTime(obj);
    return ((this === x2) || (((($p_Ljava_time_LocalTime__hour__B(this) === $p_Ljava_time_LocalTime__hour__B(x2)) && ($p_Ljava_time_LocalTime__minute__B(this) === $p_Ljava_time_LocalTime__minute__B(x2))) && ($p_Ljava_time_LocalTime__second__B(this) === $p_Ljava_time_LocalTime__second__B(x2))) && (this.Ljava_time_LocalTime__f_nano === x2.Ljava_time_LocalTime__f_nano)))
  } else {
    return false
  }
});
$c_Ljava_time_LocalTime.prototype.hashCode__I = (function() {
  var t = this.toNanoOfDay__J();
  var lo = t.RTLong__f_lo;
  var hi = t.RTLong__f_hi;
  var lo$1 = (lo ^ hi);
  return lo$1
});
$c_Ljava_time_LocalTime.prototype.toString__T = (function() {
  var buf = $ct_scm_StringBuilder__I__(new $c_scm_StringBuilder(), 18);
  var hourValue = $p_Ljava_time_LocalTime__hour__B(this);
  var minuteValue = $p_Ljava_time_LocalTime__minute__B(this);
  var secondValue = $p_Ljava_time_LocalTime__second__B(this);
  var nanoValue = this.Ljava_time_LocalTime__f_nano;
  buf.append__T__scm_StringBuilder(((hourValue < 10) ? "0" : "")).append__I__scm_StringBuilder(hourValue).append__T__scm_StringBuilder(((minuteValue < 10) ? ":0" : ":")).append__I__scm_StringBuilder(minuteValue);
  if (((secondValue > 0) || (nanoValue > 0))) {
    buf.append__T__scm_StringBuilder(((secondValue < 10) ? ":0" : ":")).append__I__scm_StringBuilder(secondValue);
    if ((nanoValue > 0)) {
      buf.append__C__scm_StringBuilder(46);
      if ((((nanoValue % 1000000) | 0) === 0)) {
        var i = ((1000 + ((nanoValue / 1000000) | 0)) | 0);
        var this$2 = ("" + i);
        buf.append__T__scm_StringBuilder($as_T(this$2.substring(1)))
      } else if ((((nanoValue % 1000) | 0) === 0)) {
        var i$1 = ((1000000 + ((nanoValue / 1000) | 0)) | 0);
        var this$5 = ("" + i$1);
        buf.append__T__scm_StringBuilder($as_T(this$5.substring(1)))
      } else {
        var i$2 = ((1000000000 + nanoValue) | 0);
        var this$8 = ("" + i$2);
        buf.append__T__scm_StringBuilder($as_T(this$8.substring(1)))
      }
    }
  };
  return buf.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
function $as_Ljava_time_LocalTime(obj) {
  return (((obj instanceof $c_Ljava_time_LocalTime) || (obj === null)) ? obj : $throwClassCastException(obj, "java.time.LocalTime"))
}
function $isArrayOf_Ljava_time_LocalTime(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_time_LocalTime)))
}
function $asArrayOf_Ljava_time_LocalTime(obj, depth) {
  return (($isArrayOf_Ljava_time_LocalTime(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.time.LocalTime;", depth))
}
var $d_Ljava_time_LocalTime = new $TypeData().initClass({
  Ljava_time_LocalTime: 0
}, false, "java.time.LocalTime", {
  Ljava_time_LocalTime: 1,
  O: 1,
  Ljava_time_temporal_TemporalAccessor: 1,
  Ljava_time_temporal_Temporal: 1,
  Ljava_time_temporal_TemporalAdjuster: 1,
  s_math_Ordered: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_LocalTime.prototype.$classData = $d_Ljava_time_LocalTime;
class $c_s_UninitializedFieldError extends $c_jl_RuntimeException {
  constructor(msg) {
    super();
    this.s_UninitializedFieldError__f_msg = null;
    this.s_UninitializedFieldError__f_msg = msg;
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, msg, null, true, true)
  };
  productPrefix__T() {
    return "UninitializedFieldError"
  };
  productArity__I() {
    return 1
  };
  productElement__I__O(x$1) {
    return ((x$1 === 0) ? this.s_UninitializedFieldError__f_msg : $m_sr_Statics$().ioobe__I__O(x$1))
  };
  productIterator__sc_Iterator() {
    return new $c_sr_ScalaRunTime$$anon$1(this)
  };
  hashCode__I() {
    var this$2 = $m_s_util_hashing_MurmurHash3$();
    return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
  };
  equals__O__Z(x$1) {
    if ((this === x$1)) {
      return true
    } else if ((x$1 instanceof $c_s_UninitializedFieldError)) {
      var UninitializedFieldError$1 = $as_s_UninitializedFieldError(x$1);
      return (this.s_UninitializedFieldError__f_msg === UninitializedFieldError$1.s_UninitializedFieldError__f_msg)
    } else {
      return false
    }
  };
}
function $as_s_UninitializedFieldError(obj) {
  return (((obj instanceof $c_s_UninitializedFieldError) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.UninitializedFieldError"))
}
function $isArrayOf_s_UninitializedFieldError(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.s_UninitializedFieldError)))
}
function $asArrayOf_s_UninitializedFieldError(obj, depth) {
  return (($isArrayOf_s_UninitializedFieldError(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.UninitializedFieldError;", depth))
}
var $d_s_UninitializedFieldError = new $TypeData().initClass({
  s_UninitializedFieldError: 0
}, false, "scala.UninitializedFieldError", {
  s_UninitializedFieldError: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  s_Product: 1,
  s_Equals: 1
});
$c_s_UninitializedFieldError.prototype.$classData = $d_s_UninitializedFieldError;
function $f_sc_View__toString__T($thiz) {
  return ($thiz.className__T() + "(<not computed>)")
}
function $is_sc_View(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_View)))
}
function $as_sc_View(obj) {
  return (($is_sc_View(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.View"))
}
function $isArrayOf_sc_View(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_View)))
}
function $asArrayOf_sc_View(obj, depth) {
  return (($isArrayOf_sc_View(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.View;", depth))
}
/** @constructor */
function $c_sci_Set$Set2$$anon$1(outer) {
  this.sci_Set$SetNIterator__f_current = 0;
  this.sci_Set$SetNIterator__f_remainder = 0;
  this.sci_Set$Set2$$anon$1__f_$outer = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.sci_Set$Set2$$anon$1__f_$outer = outer
  };
  $ct_sci_Set$SetNIterator__I__(this, 2)
}
$c_sci_Set$Set2$$anon$1.prototype = new $h_sci_Set$SetNIterator();
$c_sci_Set$Set2$$anon$1.prototype.constructor = $c_sci_Set$Set2$$anon$1;
/** @constructor */
function $h_sci_Set$Set2$$anon$1() {
  /*<skip>*/
}
$h_sci_Set$Set2$$anon$1.prototype = $c_sci_Set$Set2$$anon$1.prototype;
$c_sci_Set$Set2$$anon$1.prototype.apply__I__O = (function(i) {
  return this.sci_Set$Set2$$anon$1__f_$outer.scala$collection$immutable$Set$Set2$$getElem__I__O(i)
});
var $d_sci_Set$Set2$$anon$1 = new $TypeData().initClass({
  sci_Set$Set2$$anon$1: 0
}, false, "scala.collection.immutable.Set$Set2$$anon$1", {
  sci_Set$Set2$$anon$1: 1,
  sci_Set$SetNIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set2$$anon$1.prototype.$classData = $d_sci_Set$Set2$$anon$1;
/** @constructor */
function $c_sci_Set$Set3$$anon$2(outer) {
  this.sci_Set$SetNIterator__f_current = 0;
  this.sci_Set$SetNIterator__f_remainder = 0;
  this.sci_Set$Set3$$anon$2__f_$outer = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.sci_Set$Set3$$anon$2__f_$outer = outer
  };
  $ct_sci_Set$SetNIterator__I__(this, 3)
}
$c_sci_Set$Set3$$anon$2.prototype = new $h_sci_Set$SetNIterator();
$c_sci_Set$Set3$$anon$2.prototype.constructor = $c_sci_Set$Set3$$anon$2;
/** @constructor */
function $h_sci_Set$Set3$$anon$2() {
  /*<skip>*/
}
$h_sci_Set$Set3$$anon$2.prototype = $c_sci_Set$Set3$$anon$2.prototype;
$c_sci_Set$Set3$$anon$2.prototype.apply__I__O = (function(i) {
  return this.sci_Set$Set3$$anon$2__f_$outer.scala$collection$immutable$Set$Set3$$getElem__I__O(i)
});
var $d_sci_Set$Set3$$anon$2 = new $TypeData().initClass({
  sci_Set$Set3$$anon$2: 0
}, false, "scala.collection.immutable.Set$Set3$$anon$2", {
  sci_Set$Set3$$anon$2: 1,
  sci_Set$SetNIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set3$$anon$2.prototype.$classData = $d_sci_Set$Set3$$anon$2;
/** @constructor */
function $c_sci_Set$Set4$$anon$3(outer) {
  this.sci_Set$SetNIterator__f_current = 0;
  this.sci_Set$SetNIterator__f_remainder = 0;
  this.sci_Set$Set4$$anon$3__f_$outer = null;
  if ((outer === null)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O(null)
  } else {
    this.sci_Set$Set4$$anon$3__f_$outer = outer
  };
  $ct_sci_Set$SetNIterator__I__(this, 4)
}
$c_sci_Set$Set4$$anon$3.prototype = new $h_sci_Set$SetNIterator();
$c_sci_Set$Set4$$anon$3.prototype.constructor = $c_sci_Set$Set4$$anon$3;
/** @constructor */
function $h_sci_Set$Set4$$anon$3() {
  /*<skip>*/
}
$h_sci_Set$Set4$$anon$3.prototype = $c_sci_Set$Set4$$anon$3.prototype;
$c_sci_Set$Set4$$anon$3.prototype.apply__I__O = (function(i) {
  return this.sci_Set$Set4$$anon$3__f_$outer.scala$collection$immutable$Set$Set4$$getElem__I__O(i)
});
var $d_sci_Set$Set4$$anon$3 = new $TypeData().initClass({
  sci_Set$Set4$$anon$3: 0
}, false, "scala.collection.immutable.Set$Set4$$anon$3", {
  sci_Set$Set4$$anon$3: 1,
  sci_Set$SetNIterator: 1,
  sc_AbstractIterator: 1,
  O: 1,
  sc_Iterator: 1,
  sc_IterableOnce: 1,
  sc_IterableOnceOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set4$$anon$3.prototype.$classData = $d_sci_Set$Set4$$anon$3;
/** @constructor */
function $c_s_reflect_AnyValManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_AnyValManifest.prototype = new $h_O();
$c_s_reflect_AnyValManifest.prototype.constructor = $c_s_reflect_AnyValManifest;
/** @constructor */
function $h_s_reflect_AnyValManifest() {
  /*<skip>*/
}
$h_s_reflect_AnyValManifest.prototype = $c_s_reflect_AnyValManifest.prototype;
$c_s_reflect_AnyValManifest.prototype.toString__T = (function() {
  return this.s_reflect_AnyValManifest__f_toString
});
$c_s_reflect_AnyValManifest.prototype.equals__O__Z = (function(that) {
  return (this === that)
});
$c_s_reflect_AnyValManifest.prototype.hashCode__I = (function() {
  return this.s_reflect_AnyValManifest__f_hashCode
});
/** @constructor */
function $c_s_reflect_ManifestFactory$ClassTypeManifest() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null
}
$c_s_reflect_ManifestFactory$ClassTypeManifest.prototype = new $h_O();
$c_s_reflect_ManifestFactory$ClassTypeManifest.prototype.constructor = $c_s_reflect_ManifestFactory$ClassTypeManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$ClassTypeManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ClassTypeManifest.prototype = $c_s_reflect_ManifestFactory$ClassTypeManifest.prototype;
class $c_sjs_js_JavaScriptException extends $c_jl_RuntimeException {
  constructor(exception) {
    super();
    this.sjs_js_JavaScriptException__f_exception = null;
    this.sjs_js_JavaScriptException__f_exception = exception;
    $ct_jl_Throwable__T__jl_Throwable__Z__Z__(this, null, null, true, true)
  };
  getMessage__T() {
    return $dp_toString__T(this.sjs_js_JavaScriptException__f_exception)
  };
  fillInStackTrace__jl_Throwable() {
    this.setStackTraceStateInternal__O__(this.sjs_js_JavaScriptException__f_exception);
    return this
  };
  productPrefix__T() {
    return "JavaScriptException"
  };
  productArity__I() {
    return 1
  };
  productElement__I__O(x$1) {
    return ((x$1 === 0) ? this.sjs_js_JavaScriptException__f_exception : $m_sr_Statics$().ioobe__I__O(x$1))
  };
  productIterator__sc_Iterator() {
    return new $c_sr_ScalaRunTime$$anon$1(this)
  };
  hashCode__I() {
    var this$2 = $m_s_util_hashing_MurmurHash3$();
    return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
  };
  equals__O__Z(x$1) {
    if ((this === x$1)) {
      return true
    } else if ((x$1 instanceof $c_sjs_js_JavaScriptException)) {
      var JavaScriptException$1 = $as_sjs_js_JavaScriptException(x$1);
      var x = this.sjs_js_JavaScriptException__f_exception;
      var y = JavaScriptException$1.sjs_js_JavaScriptException__f_exception;
      return $m_sr_BoxesRunTime$().equals__O__O__Z(x, y)
    } else {
      return false
    }
  };
  setStackTraceStateInternal__O__(e) {
    this.jl_Throwable__f_stackTraceStateInternal = e
  };
}
function $as_sjs_js_JavaScriptException(obj) {
  return (((obj instanceof $c_sjs_js_JavaScriptException) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.scalajs.js.JavaScriptException"))
}
function $isArrayOf_sjs_js_JavaScriptException(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sjs_js_JavaScriptException)))
}
function $asArrayOf_sjs_js_JavaScriptException(obj, depth) {
  return (($isArrayOf_sjs_js_JavaScriptException(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.scalajs.js.JavaScriptException;", depth))
}
var $d_sjs_js_JavaScriptException = new $TypeData().initClass({
  sjs_js_JavaScriptException: 0
}, false, "scala.scalajs.js.JavaScriptException", {
  sjs_js_JavaScriptException: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  O: 1,
  Ljava_io_Serializable: 1,
  s_Product: 1,
  s_Equals: 1
});
$c_sjs_js_JavaScriptException.prototype.$classData = $d_sjs_js_JavaScriptException;
function $p_jl_JSConsoleBasedPrintStream__doWriteLine__T__V($thiz, line) {
  if (($as_T((typeof console)) !== "undefined")) {
    if ($thiz.jl_JSConsoleBasedPrintStream__f_isErr) {
      var x = console.error;
      var $$x1 = $uZ((!(!x)))
    } else {
      var $$x1 = false
    };
    if ($$x1) {
      console.error(line)
    } else {
      console.log(line)
    }
  }
}
/** @constructor */
function $c_jl_JSConsoleBasedPrintStream(isErr) {
  this.Ljava_io_FilterOutputStream__f_out = null;
  this.Ljava_io_PrintStream__f_encoder = null;
  this.Ljava_io_PrintStream__f_autoFlush = false;
  this.Ljava_io_PrintStream__f_charset = null;
  this.Ljava_io_PrintStream__f_closing = false;
  this.Ljava_io_PrintStream__f_java$io$PrintStream$$closed = false;
  this.Ljava_io_PrintStream__f_errorFlag = false;
  this.Ljava_io_PrintStream__f_bitmap$0 = false;
  this.jl_JSConsoleBasedPrintStream__f_isErr = false;
  this.jl_JSConsoleBasedPrintStream__f_flushed = false;
  this.jl_JSConsoleBasedPrintStream__f_buffer = null;
  this.jl_JSConsoleBasedPrintStream__f_isErr = isErr;
  var out = new $c_jl_JSConsoleBasedPrintStream$DummyOutputStream();
  $ct_Ljava_io_PrintStream__Ljava_io_OutputStream__Z__Ljava_nio_charset_Charset__(this, out, false, null);
  this.jl_JSConsoleBasedPrintStream__f_flushed = true;
  this.jl_JSConsoleBasedPrintStream__f_buffer = ""
}
$c_jl_JSConsoleBasedPrintStream.prototype = new $h_Ljava_io_PrintStream();
$c_jl_JSConsoleBasedPrintStream.prototype.constructor = $c_jl_JSConsoleBasedPrintStream;
/** @constructor */
function $h_jl_JSConsoleBasedPrintStream() {
  /*<skip>*/
}
$h_jl_JSConsoleBasedPrintStream.prototype = $c_jl_JSConsoleBasedPrintStream.prototype;
$c_jl_JSConsoleBasedPrintStream.prototype.java$lang$JSConsoleBasedPrintStream$$printString__T__V = (function(s) {
  var rest = s;
  while ((rest !== "")) {
    var this$1 = rest;
    var nlPos = $uI(this$1.indexOf("\n"));
    if ((nlPos < 0)) {
      this.jl_JSConsoleBasedPrintStream__f_buffer = (("" + this.jl_JSConsoleBasedPrintStream__f_buffer) + rest);
      this.jl_JSConsoleBasedPrintStream__f_flushed = false;
      rest = ""
    } else {
      var $$x1 = this.jl_JSConsoleBasedPrintStream__f_buffer;
      var this$3 = rest;
      $p_jl_JSConsoleBasedPrintStream__doWriteLine__T__V(this, (("" + $$x1) + $as_T(this$3.substring(0, nlPos))));
      this.jl_JSConsoleBasedPrintStream__f_buffer = "";
      this.jl_JSConsoleBasedPrintStream__f_flushed = true;
      var this$4 = rest;
      var beginIndex = ((1 + nlPos) | 0);
      rest = $as_T(this$4.substring(beginIndex))
    }
  }
});
var $d_jl_JSConsoleBasedPrintStream = new $TypeData().initClass({
  jl_JSConsoleBasedPrintStream: 0
}, false, "java.lang.JSConsoleBasedPrintStream", {
  jl_JSConsoleBasedPrintStream: 1,
  Ljava_io_PrintStream: 1,
  Ljava_io_FilterOutputStream: 1,
  Ljava_io_OutputStream: 1,
  O: 1,
  Ljava_io_Closeable: 1,
  jl_AutoCloseable: 1,
  Ljava_io_Flushable: 1,
  jl_Appendable: 1
});
$c_jl_JSConsoleBasedPrintStream.prototype.$classData = $d_jl_JSConsoleBasedPrintStream;
function $p_Ljava_time_LocalDate__month__S($thiz) {
  if (((((1 & $thiz.Ljava_time_LocalDate__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalDate.scala: 372")
  };
  return $thiz.Ljava_time_LocalDate__f_month
}
function $p_Ljava_time_LocalDate__day__S($thiz) {
  if (((((2 & $thiz.Ljava_time_LocalDate__f_bitmap$init$0) << 24) >> 24) === 0)) {
    throw new $c_s_UninitializedFieldError("Uninitialized field: /home/runner/work/scala-java-time/scala-java-time/core/js/target/scala-2.13/src_managed/main/org/threeten/bp/LocalDate.scala: 375")
  };
  return $thiz.Ljava_time_LocalDate__f_day
}
/** @constructor */
function $c_Ljava_time_LocalDate(year, monthOfYear, dayOfMonth) {
  this.Ljava_time_LocalDate__f_year = 0;
  this.Ljava_time_LocalDate__f_month = 0;
  this.Ljava_time_LocalDate__f_day = 0;
  this.Ljava_time_LocalDate__f_bitmap$init$0 = 0;
  this.Ljava_time_LocalDate__f_year = year;
  this.Ljava_time_LocalDate__f_month = ((monthOfYear << 16) >> 16);
  this.Ljava_time_LocalDate__f_bitmap$init$0 = (((1 | this.Ljava_time_LocalDate__f_bitmap$init$0) << 24) >> 24);
  this.Ljava_time_LocalDate__f_day = ((dayOfMonth << 16) >> 16);
  this.Ljava_time_LocalDate__f_bitmap$init$0 = (((2 | this.Ljava_time_LocalDate__f_bitmap$init$0) << 24) >> 24)
}
$c_Ljava_time_LocalDate.prototype = new $h_O();
$c_Ljava_time_LocalDate.prototype.constructor = $c_Ljava_time_LocalDate;
/** @constructor */
function $h_Ljava_time_LocalDate() {
  /*<skip>*/
}
$h_Ljava_time_LocalDate.prototype = $c_Ljava_time_LocalDate.prototype;
$c_Ljava_time_LocalDate.prototype.compareTo0__Ljava_time_LocalDate__I = (function(otherDate) {
  var cmp = ((this.Ljava_time_LocalDate__f_year - otherDate.Ljava_time_LocalDate__f_year) | 0);
  if ((cmp === 0)) {
    cmp = (($p_Ljava_time_LocalDate__month__S(this) - $p_Ljava_time_LocalDate__month__S(otherDate)) | 0);
    if ((cmp === 0)) {
      cmp = (($p_Ljava_time_LocalDate__day__S(this) - $p_Ljava_time_LocalDate__day__S(otherDate)) | 0)
    }
  };
  return cmp
});
$c_Ljava_time_LocalDate.prototype.equals__O__Z = (function(obj) {
  if ((obj instanceof $c_Ljava_time_LocalDate)) {
    var x2 = $as_Ljava_time_LocalDate(obj);
    return ((this === x2) || (this.compareTo0__Ljava_time_LocalDate__I(x2) === 0))
  } else {
    return false
  }
});
$c_Ljava_time_LocalDate.prototype.hashCode__I = (function() {
  var yearValue = this.Ljava_time_LocalDate__f_year;
  var monthValue = $p_Ljava_time_LocalDate__month__S(this);
  var dayValue = $p_Ljava_time_LocalDate__day__S(this);
  return (((-2048) & yearValue) ^ (((((yearValue << 11) + (monthValue << 6)) | 0) + dayValue) | 0))
});
$c_Ljava_time_LocalDate.prototype.toString__T = (function() {
  var yearValue = this.Ljava_time_LocalDate__f_year;
  var monthValue = $p_Ljava_time_LocalDate__month__S(this);
  var dayValue = $p_Ljava_time_LocalDate__day__S(this);
  var absYear = ((yearValue < 0) ? ((-yearValue) | 0) : yearValue);
  var buf = $ct_scm_StringBuilder__I__(new $c_scm_StringBuilder(), 10);
  if ((absYear < 1000)) {
    if ((yearValue < 0)) {
      buf.append__I__scm_StringBuilder((((-10000) + yearValue) | 0)).deleteCharAt__I__scm_StringBuilder(1)
    } else {
      buf.append__I__scm_StringBuilder(((10000 + yearValue) | 0)).deleteCharAt__I__scm_StringBuilder(0)
    }
  } else {
    if ((yearValue > 9999)) {
      buf.append__C__scm_StringBuilder(43)
    };
    buf.append__I__scm_StringBuilder(yearValue)
  };
  var this$2 = buf.append__T__scm_StringBuilder(((monthValue < 10) ? "-0" : "-")).append__I__scm_StringBuilder(monthValue).append__T__scm_StringBuilder(((dayValue < 10) ? "-0" : "-")).append__I__scm_StringBuilder(dayValue);
  return this$2.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
function $as_Ljava_time_LocalDate(obj) {
  return (((obj instanceof $c_Ljava_time_LocalDate) || (obj === null)) ? obj : $throwClassCastException(obj, "java.time.LocalDate"))
}
function $isArrayOf_Ljava_time_LocalDate(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.Ljava_time_LocalDate)))
}
function $asArrayOf_Ljava_time_LocalDate(obj, depth) {
  return (($isArrayOf_Ljava_time_LocalDate(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Ljava.time.LocalDate;", depth))
}
var $d_Ljava_time_LocalDate = new $TypeData().initClass({
  Ljava_time_LocalDate: 0
}, false, "java.time.LocalDate", {
  Ljava_time_LocalDate: 1,
  O: 1,
  Ljava_time_chrono_ChronoLocalDate: 1,
  Ljava_time_temporal_Temporal: 1,
  Ljava_time_temporal_TemporalAccessor: 1,
  Ljava_time_temporal_TemporalAdjuster: 1,
  s_math_Ordered: 1,
  jl_Comparable: 1,
  Ljava_io_Serializable: 1
});
$c_Ljava_time_LocalDate.prototype.$classData = $d_Ljava_time_LocalDate;
function $p_sc_StrictOptimizedLinearSeqOps__loop$2__I__sc_LinearSeq__sc_LinearSeq($thiz, n, s) {
  while (true) {
    if (((n <= 0) || s.isEmpty__Z())) {
      return s
    } else {
      var temp$n = (((-1) + n) | 0);
      var temp$s = $as_sc_LinearSeq(s.tail__O());
      n = temp$n;
      s = temp$s
    }
  }
}
/** @constructor */
function $c_s_reflect_ManifestFactory$BooleanManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$BooleanManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$BooleanManifest.prototype.constructor = $c_s_reflect_ManifestFactory$BooleanManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$BooleanManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$BooleanManifest.prototype = $c_s_reflect_ManifestFactory$BooleanManifest.prototype;
$c_s_reflect_ManifestFactory$BooleanManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_Z.getClassOf()
});
$c_s_reflect_ManifestFactory$BooleanManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_Z(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$ByteManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$ByteManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$ByteManifest.prototype.constructor = $c_s_reflect_ManifestFactory$ByteManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$ByteManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ByteManifest.prototype = $c_s_reflect_ManifestFactory$ByteManifest.prototype;
$c_s_reflect_ManifestFactory$ByteManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_B.getClassOf()
});
$c_s_reflect_ManifestFactory$ByteManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_B(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$CharManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$CharManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$CharManifest.prototype.constructor = $c_s_reflect_ManifestFactory$CharManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$CharManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$CharManifest.prototype = $c_s_reflect_ManifestFactory$CharManifest.prototype;
$c_s_reflect_ManifestFactory$CharManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_C.getClassOf()
});
$c_s_reflect_ManifestFactory$CharManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_C(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$DoubleManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$DoubleManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$DoubleManifest.prototype.constructor = $c_s_reflect_ManifestFactory$DoubleManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$DoubleManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$DoubleManifest.prototype = $c_s_reflect_ManifestFactory$DoubleManifest.prototype;
$c_s_reflect_ManifestFactory$DoubleManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_D.getClassOf()
});
$c_s_reflect_ManifestFactory$DoubleManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_D(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$FloatManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$FloatManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$FloatManifest.prototype.constructor = $c_s_reflect_ManifestFactory$FloatManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$FloatManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$FloatManifest.prototype = $c_s_reflect_ManifestFactory$FloatManifest.prototype;
$c_s_reflect_ManifestFactory$FloatManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_F.getClassOf()
});
$c_s_reflect_ManifestFactory$FloatManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_F(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$IntManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$IntManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$IntManifest.prototype.constructor = $c_s_reflect_ManifestFactory$IntManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$IntManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$IntManifest.prototype = $c_s_reflect_ManifestFactory$IntManifest.prototype;
$c_s_reflect_ManifestFactory$IntManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_I.getClassOf()
});
$c_s_reflect_ManifestFactory$IntManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_I(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$LongManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$LongManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$LongManifest.prototype.constructor = $c_s_reflect_ManifestFactory$LongManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$LongManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$LongManifest.prototype = $c_s_reflect_ManifestFactory$LongManifest.prototype;
$c_s_reflect_ManifestFactory$LongManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_J.getClassOf()
});
$c_s_reflect_ManifestFactory$LongManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_J(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$PhantomManifest() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$PhantomManifest.prototype = new $h_s_reflect_ManifestFactory$ClassTypeManifest();
$c_s_reflect_ManifestFactory$PhantomManifest.prototype.constructor = $c_s_reflect_ManifestFactory$PhantomManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$PhantomManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$PhantomManifest.prototype = $c_s_reflect_ManifestFactory$PhantomManifest.prototype;
$c_s_reflect_ManifestFactory$PhantomManifest.prototype.toString__T = (function() {
  return this.s_reflect_ManifestFactory$PhantomManifest__f_toString
});
$c_s_reflect_ManifestFactory$PhantomManifest.prototype.equals__O__Z = (function(that) {
  return (this === that)
});
$c_s_reflect_ManifestFactory$PhantomManifest.prototype.hashCode__I = (function() {
  return this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode
});
/** @constructor */
function $c_s_reflect_ManifestFactory$ShortManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$ShortManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$ShortManifest.prototype.constructor = $c_s_reflect_ManifestFactory$ShortManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$ShortManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ShortManifest.prototype = $c_s_reflect_ManifestFactory$ShortManifest.prototype;
$c_s_reflect_ManifestFactory$ShortManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_S.getClassOf()
});
$c_s_reflect_ManifestFactory$ShortManifest.prototype.newArray__I__O = (function(len) {
  return new $ac_S(len)
});
/** @constructor */
function $c_s_reflect_ManifestFactory$UnitManifest() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0
}
$c_s_reflect_ManifestFactory$UnitManifest.prototype = new $h_s_reflect_AnyValManifest();
$c_s_reflect_ManifestFactory$UnitManifest.prototype.constructor = $c_s_reflect_ManifestFactory$UnitManifest;
/** @constructor */
function $h_s_reflect_ManifestFactory$UnitManifest() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$UnitManifest.prototype = $c_s_reflect_ManifestFactory$UnitManifest.prototype;
$c_s_reflect_ManifestFactory$UnitManifest.prototype.runtimeClass__jl_Class = (function() {
  return $d_V.getClassOf()
});
$c_s_reflect_ManifestFactory$UnitManifest.prototype.newArray__I__O = (function(len) {
  return new ($d_jl_Void.getArrayOf().constr)(len)
});
/** @constructor */
function $c_sc_AbstractView() {
  /*<skip>*/
}
$c_sc_AbstractView.prototype = new $h_sc_AbstractIterable();
$c_sc_AbstractView.prototype.constructor = $c_sc_AbstractView;
/** @constructor */
function $h_sc_AbstractView() {
  /*<skip>*/
}
$h_sc_AbstractView.prototype = $c_sc_AbstractView.prototype;
$c_sc_AbstractView.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sc_View$()
});
$c_sc_AbstractView.prototype.toString__T = (function() {
  return $f_sc_View__toString__T(this)
});
$c_sc_AbstractView.prototype.stringPrefix__T = (function() {
  return "View"
});
function $f_sc_Set__equals__O__Z($thiz, that) {
  if (($thiz === that)) {
    return true
  } else if ($is_sc_Set(that)) {
    var x2 = $as_sc_Set(that);
    if (($thiz.size__I() === x2.size__I())) {
      try {
        return $thiz.subsetOf__sc_Set__Z(x2)
      } catch (e) {
        if ((e instanceof $c_jl_ClassCastException)) {
          return false
        } else {
          throw e
        }
      }
    } else {
      return false
    }
  } else {
    return false
  }
}
function $is_sc_Set(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_Set)))
}
function $as_sc_Set(obj) {
  return (($is_sc_Set(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.Set"))
}
function $isArrayOf_sc_Set(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_Set)))
}
function $asArrayOf_sc_Set(obj, depth) {
  return (($isArrayOf_sc_Set(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.Set;", depth))
}
/** @constructor */
function $c_s_reflect_ManifestFactory$AnyManifest$() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = 0;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = "Any";
  var prefix = $m_s_None$();
  var typeArguments = $m_s_package$().s_package$__f_Nil;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = prefix;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = $d_O.getClassOf();
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = typeArguments;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$AnyManifest$.prototype = new $h_s_reflect_ManifestFactory$PhantomManifest();
$c_s_reflect_ManifestFactory$AnyManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$AnyManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$AnyManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$AnyManifest$.prototype = $c_s_reflect_ManifestFactory$AnyManifest$.prototype;
$c_s_reflect_ManifestFactory$AnyManifest$.prototype.runtimeClass__jl_Class = (function() {
  return $d_O.getClassOf()
});
$c_s_reflect_ManifestFactory$AnyManifest$.prototype.newArray__I__O = (function(len) {
  return new $ac_O(len)
});
var $d_s_reflect_ManifestFactory$AnyManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$AnyManifest$: 0
}, false, "scala.reflect.ManifestFactory$AnyManifest$", {
  s_reflect_ManifestFactory$AnyManifest$: 1,
  s_reflect_ManifestFactory$PhantomManifest: 1,
  s_reflect_ManifestFactory$ClassTypeManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$AnyManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$AnyManifest$;
var $n_s_reflect_ManifestFactory$AnyManifest$;
function $m_s_reflect_ManifestFactory$AnyManifest$() {
  if ((!$n_s_reflect_ManifestFactory$AnyManifest$)) {
    $n_s_reflect_ManifestFactory$AnyManifest$ = new $c_s_reflect_ManifestFactory$AnyManifest$()
  };
  return $n_s_reflect_ManifestFactory$AnyManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$BooleanManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Boolean";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$BooleanManifest$.prototype = new $h_s_reflect_ManifestFactory$BooleanManifest();
$c_s_reflect_ManifestFactory$BooleanManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$BooleanManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$BooleanManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$BooleanManifest$.prototype = $c_s_reflect_ManifestFactory$BooleanManifest$.prototype;
var $d_s_reflect_ManifestFactory$BooleanManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$BooleanManifest$: 0
}, false, "scala.reflect.ManifestFactory$BooleanManifest$", {
  s_reflect_ManifestFactory$BooleanManifest$: 1,
  s_reflect_ManifestFactory$BooleanManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$BooleanManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$BooleanManifest$;
var $n_s_reflect_ManifestFactory$BooleanManifest$;
function $m_s_reflect_ManifestFactory$BooleanManifest$() {
  if ((!$n_s_reflect_ManifestFactory$BooleanManifest$)) {
    $n_s_reflect_ManifestFactory$BooleanManifest$ = new $c_s_reflect_ManifestFactory$BooleanManifest$()
  };
  return $n_s_reflect_ManifestFactory$BooleanManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$ByteManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Byte";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$ByteManifest$.prototype = new $h_s_reflect_ManifestFactory$ByteManifest();
$c_s_reflect_ManifestFactory$ByteManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$ByteManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$ByteManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ByteManifest$.prototype = $c_s_reflect_ManifestFactory$ByteManifest$.prototype;
var $d_s_reflect_ManifestFactory$ByteManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$ByteManifest$: 0
}, false, "scala.reflect.ManifestFactory$ByteManifest$", {
  s_reflect_ManifestFactory$ByteManifest$: 1,
  s_reflect_ManifestFactory$ByteManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$ByteManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$ByteManifest$;
var $n_s_reflect_ManifestFactory$ByteManifest$;
function $m_s_reflect_ManifestFactory$ByteManifest$() {
  if ((!$n_s_reflect_ManifestFactory$ByteManifest$)) {
    $n_s_reflect_ManifestFactory$ByteManifest$ = new $c_s_reflect_ManifestFactory$ByteManifest$()
  };
  return $n_s_reflect_ManifestFactory$ByteManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$CharManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Char";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$CharManifest$.prototype = new $h_s_reflect_ManifestFactory$CharManifest();
$c_s_reflect_ManifestFactory$CharManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$CharManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$CharManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$CharManifest$.prototype = $c_s_reflect_ManifestFactory$CharManifest$.prototype;
var $d_s_reflect_ManifestFactory$CharManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$CharManifest$: 0
}, false, "scala.reflect.ManifestFactory$CharManifest$", {
  s_reflect_ManifestFactory$CharManifest$: 1,
  s_reflect_ManifestFactory$CharManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$CharManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$CharManifest$;
var $n_s_reflect_ManifestFactory$CharManifest$;
function $m_s_reflect_ManifestFactory$CharManifest$() {
  if ((!$n_s_reflect_ManifestFactory$CharManifest$)) {
    $n_s_reflect_ManifestFactory$CharManifest$ = new $c_s_reflect_ManifestFactory$CharManifest$()
  };
  return $n_s_reflect_ManifestFactory$CharManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$DoubleManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Double";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$DoubleManifest$.prototype = new $h_s_reflect_ManifestFactory$DoubleManifest();
$c_s_reflect_ManifestFactory$DoubleManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$DoubleManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$DoubleManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$DoubleManifest$.prototype = $c_s_reflect_ManifestFactory$DoubleManifest$.prototype;
var $d_s_reflect_ManifestFactory$DoubleManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$DoubleManifest$: 0
}, false, "scala.reflect.ManifestFactory$DoubleManifest$", {
  s_reflect_ManifestFactory$DoubleManifest$: 1,
  s_reflect_ManifestFactory$DoubleManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$DoubleManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$DoubleManifest$;
var $n_s_reflect_ManifestFactory$DoubleManifest$;
function $m_s_reflect_ManifestFactory$DoubleManifest$() {
  if ((!$n_s_reflect_ManifestFactory$DoubleManifest$)) {
    $n_s_reflect_ManifestFactory$DoubleManifest$ = new $c_s_reflect_ManifestFactory$DoubleManifest$()
  };
  return $n_s_reflect_ManifestFactory$DoubleManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$FloatManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Float";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$FloatManifest$.prototype = new $h_s_reflect_ManifestFactory$FloatManifest();
$c_s_reflect_ManifestFactory$FloatManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$FloatManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$FloatManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$FloatManifest$.prototype = $c_s_reflect_ManifestFactory$FloatManifest$.prototype;
var $d_s_reflect_ManifestFactory$FloatManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$FloatManifest$: 0
}, false, "scala.reflect.ManifestFactory$FloatManifest$", {
  s_reflect_ManifestFactory$FloatManifest$: 1,
  s_reflect_ManifestFactory$FloatManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$FloatManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$FloatManifest$;
var $n_s_reflect_ManifestFactory$FloatManifest$;
function $m_s_reflect_ManifestFactory$FloatManifest$() {
  if ((!$n_s_reflect_ManifestFactory$FloatManifest$)) {
    $n_s_reflect_ManifestFactory$FloatManifest$ = new $c_s_reflect_ManifestFactory$FloatManifest$()
  };
  return $n_s_reflect_ManifestFactory$FloatManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$IntManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Int";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$IntManifest$.prototype = new $h_s_reflect_ManifestFactory$IntManifest();
$c_s_reflect_ManifestFactory$IntManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$IntManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$IntManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$IntManifest$.prototype = $c_s_reflect_ManifestFactory$IntManifest$.prototype;
var $d_s_reflect_ManifestFactory$IntManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$IntManifest$: 0
}, false, "scala.reflect.ManifestFactory$IntManifest$", {
  s_reflect_ManifestFactory$IntManifest$: 1,
  s_reflect_ManifestFactory$IntManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$IntManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$IntManifest$;
var $n_s_reflect_ManifestFactory$IntManifest$;
function $m_s_reflect_ManifestFactory$IntManifest$() {
  if ((!$n_s_reflect_ManifestFactory$IntManifest$)) {
    $n_s_reflect_ManifestFactory$IntManifest$ = new $c_s_reflect_ManifestFactory$IntManifest$()
  };
  return $n_s_reflect_ManifestFactory$IntManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$LongManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Long";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$LongManifest$.prototype = new $h_s_reflect_ManifestFactory$LongManifest();
$c_s_reflect_ManifestFactory$LongManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$LongManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$LongManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$LongManifest$.prototype = $c_s_reflect_ManifestFactory$LongManifest$.prototype;
var $d_s_reflect_ManifestFactory$LongManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$LongManifest$: 0
}, false, "scala.reflect.ManifestFactory$LongManifest$", {
  s_reflect_ManifestFactory$LongManifest$: 1,
  s_reflect_ManifestFactory$LongManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$LongManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$LongManifest$;
var $n_s_reflect_ManifestFactory$LongManifest$;
function $m_s_reflect_ManifestFactory$LongManifest$() {
  if ((!$n_s_reflect_ManifestFactory$LongManifest$)) {
    $n_s_reflect_ManifestFactory$LongManifest$ = new $c_s_reflect_ManifestFactory$LongManifest$()
  };
  return $n_s_reflect_ManifestFactory$LongManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$NothingManifest$() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = 0;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = "Nothing";
  var prefix = $m_s_None$();
  var typeArguments = $m_s_package$().s_package$__f_Nil;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = prefix;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = $d_sr_Nothing$.getClassOf();
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = typeArguments;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$NothingManifest$.prototype = new $h_s_reflect_ManifestFactory$PhantomManifest();
$c_s_reflect_ManifestFactory$NothingManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$NothingManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$NothingManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$NothingManifest$.prototype = $c_s_reflect_ManifestFactory$NothingManifest$.prototype;
$c_s_reflect_ManifestFactory$NothingManifest$.prototype.runtimeClass__jl_Class = (function() {
  return $d_sr_Nothing$.getClassOf()
});
$c_s_reflect_ManifestFactory$NothingManifest$.prototype.newArray__I__O = (function(len) {
  return new $ac_O(len)
});
var $d_s_reflect_ManifestFactory$NothingManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$NothingManifest$: 0
}, false, "scala.reflect.ManifestFactory$NothingManifest$", {
  s_reflect_ManifestFactory$NothingManifest$: 1,
  s_reflect_ManifestFactory$PhantomManifest: 1,
  s_reflect_ManifestFactory$ClassTypeManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$NothingManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$NothingManifest$;
var $n_s_reflect_ManifestFactory$NothingManifest$;
function $m_s_reflect_ManifestFactory$NothingManifest$() {
  if ((!$n_s_reflect_ManifestFactory$NothingManifest$)) {
    $n_s_reflect_ManifestFactory$NothingManifest$ = new $c_s_reflect_ManifestFactory$NothingManifest$()
  };
  return $n_s_reflect_ManifestFactory$NothingManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$NullManifest$() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = 0;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = "Null";
  var prefix = $m_s_None$();
  var typeArguments = $m_s_package$().s_package$__f_Nil;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = prefix;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = $d_sr_Null$.getClassOf();
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = typeArguments;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$NullManifest$.prototype = new $h_s_reflect_ManifestFactory$PhantomManifest();
$c_s_reflect_ManifestFactory$NullManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$NullManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$NullManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$NullManifest$.prototype = $c_s_reflect_ManifestFactory$NullManifest$.prototype;
$c_s_reflect_ManifestFactory$NullManifest$.prototype.runtimeClass__jl_Class = (function() {
  return $d_sr_Null$.getClassOf()
});
$c_s_reflect_ManifestFactory$NullManifest$.prototype.newArray__I__O = (function(len) {
  return new $ac_O(len)
});
var $d_s_reflect_ManifestFactory$NullManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$NullManifest$: 0
}, false, "scala.reflect.ManifestFactory$NullManifest$", {
  s_reflect_ManifestFactory$NullManifest$: 1,
  s_reflect_ManifestFactory$PhantomManifest: 1,
  s_reflect_ManifestFactory$ClassTypeManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$NullManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$NullManifest$;
var $n_s_reflect_ManifestFactory$NullManifest$;
function $m_s_reflect_ManifestFactory$NullManifest$() {
  if ((!$n_s_reflect_ManifestFactory$NullManifest$)) {
    $n_s_reflect_ManifestFactory$NullManifest$ = new $c_s_reflect_ManifestFactory$NullManifest$()
  };
  return $n_s_reflect_ManifestFactory$NullManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$ObjectManifest$() {
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = null;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = null;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = 0;
  this.s_reflect_ManifestFactory$PhantomManifest__f_toString = "Object";
  var prefix = $m_s_None$();
  var typeArguments = $m_s_package$().s_package$__f_Nil;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_prefix = prefix;
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_runtimeClass1 = $d_O.getClassOf();
  this.s_reflect_ManifestFactory$ClassTypeManifest__f_typeArguments = typeArguments;
  this.s_reflect_ManifestFactory$PhantomManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$ObjectManifest$.prototype = new $h_s_reflect_ManifestFactory$PhantomManifest();
$c_s_reflect_ManifestFactory$ObjectManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$ObjectManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$ObjectManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ObjectManifest$.prototype = $c_s_reflect_ManifestFactory$ObjectManifest$.prototype;
$c_s_reflect_ManifestFactory$ObjectManifest$.prototype.runtimeClass__jl_Class = (function() {
  return $d_O.getClassOf()
});
$c_s_reflect_ManifestFactory$ObjectManifest$.prototype.newArray__I__O = (function(len) {
  return new $ac_O(len)
});
var $d_s_reflect_ManifestFactory$ObjectManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$ObjectManifest$: 0
}, false, "scala.reflect.ManifestFactory$ObjectManifest$", {
  s_reflect_ManifestFactory$ObjectManifest$: 1,
  s_reflect_ManifestFactory$PhantomManifest: 1,
  s_reflect_ManifestFactory$ClassTypeManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$ObjectManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$ObjectManifest$;
var $n_s_reflect_ManifestFactory$ObjectManifest$;
function $m_s_reflect_ManifestFactory$ObjectManifest$() {
  if ((!$n_s_reflect_ManifestFactory$ObjectManifest$)) {
    $n_s_reflect_ManifestFactory$ObjectManifest$ = new $c_s_reflect_ManifestFactory$ObjectManifest$()
  };
  return $n_s_reflect_ManifestFactory$ObjectManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$ShortManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Short";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$ShortManifest$.prototype = new $h_s_reflect_ManifestFactory$ShortManifest();
$c_s_reflect_ManifestFactory$ShortManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$ShortManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$ShortManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$ShortManifest$.prototype = $c_s_reflect_ManifestFactory$ShortManifest$.prototype;
var $d_s_reflect_ManifestFactory$ShortManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$ShortManifest$: 0
}, false, "scala.reflect.ManifestFactory$ShortManifest$", {
  s_reflect_ManifestFactory$ShortManifest$: 1,
  s_reflect_ManifestFactory$ShortManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$ShortManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$ShortManifest$;
var $n_s_reflect_ManifestFactory$ShortManifest$;
function $m_s_reflect_ManifestFactory$ShortManifest$() {
  if ((!$n_s_reflect_ManifestFactory$ShortManifest$)) {
    $n_s_reflect_ManifestFactory$ShortManifest$ = new $c_s_reflect_ManifestFactory$ShortManifest$()
  };
  return $n_s_reflect_ManifestFactory$ShortManifest$
}
/** @constructor */
function $c_s_reflect_ManifestFactory$UnitManifest$() {
  this.s_reflect_AnyValManifest__f_toString = null;
  this.s_reflect_AnyValManifest__f_hashCode = 0;
  this.s_reflect_AnyValManifest__f_toString = "Unit";
  this.s_reflect_AnyValManifest__f_hashCode = $systemIdentityHashCode(this)
}
$c_s_reflect_ManifestFactory$UnitManifest$.prototype = new $h_s_reflect_ManifestFactory$UnitManifest();
$c_s_reflect_ManifestFactory$UnitManifest$.prototype.constructor = $c_s_reflect_ManifestFactory$UnitManifest$;
/** @constructor */
function $h_s_reflect_ManifestFactory$UnitManifest$() {
  /*<skip>*/
}
$h_s_reflect_ManifestFactory$UnitManifest$.prototype = $c_s_reflect_ManifestFactory$UnitManifest$.prototype;
var $d_s_reflect_ManifestFactory$UnitManifest$ = new $TypeData().initClass({
  s_reflect_ManifestFactory$UnitManifest$: 0
}, false, "scala.reflect.ManifestFactory$UnitManifest$", {
  s_reflect_ManifestFactory$UnitManifest$: 1,
  s_reflect_ManifestFactory$UnitManifest: 1,
  s_reflect_AnyValManifest: 1,
  O: 1,
  s_reflect_Manifest: 1,
  s_reflect_ClassTag: 1,
  s_reflect_ClassManifestDeprecatedApis: 1,
  s_reflect_OptManifest: 1,
  Ljava_io_Serializable: 1,
  s_Equals: 1
});
$c_s_reflect_ManifestFactory$UnitManifest$.prototype.$classData = $d_s_reflect_ManifestFactory$UnitManifest$;
var $n_s_reflect_ManifestFactory$UnitManifest$;
function $m_s_reflect_ManifestFactory$UnitManifest$() {
  if ((!$n_s_reflect_ManifestFactory$UnitManifest$)) {
    $n_s_reflect_ManifestFactory$UnitManifest$ = new $c_s_reflect_ManifestFactory$UnitManifest$()
  };
  return $n_s_reflect_ManifestFactory$UnitManifest$
}
function $f_sc_Seq__equals__O__Z($thiz, o) {
  if (($thiz === o)) {
    return true
  } else {
    if ($is_sc_Seq(o)) {
      var x2 = $as_sc_Seq(o);
      if (x2.canEqual__O__Z($thiz)) {
        return $thiz.sameElements__sc_IterableOnce__Z(x2)
      }
    };
    return false
  }
}
function $is_sc_Seq(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_Seq)))
}
function $as_sc_Seq(obj) {
  return (($is_sc_Seq(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.Seq"))
}
function $isArrayOf_sc_Seq(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_Seq)))
}
function $asArrayOf_sc_Seq(obj, depth) {
  return (($isArrayOf_sc_Seq(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.Seq;", depth))
}
/** @constructor */
function $c_sc_View$$anon$1(it$1) {
  this.sc_View$$anon$1__f_it$1 = null;
  this.sc_View$$anon$1__f_it$1 = it$1
}
$c_sc_View$$anon$1.prototype = new $h_sc_AbstractView();
$c_sc_View$$anon$1.prototype.constructor = $c_sc_View$$anon$1;
/** @constructor */
function $h_sc_View$$anon$1() {
  /*<skip>*/
}
$h_sc_View$$anon$1.prototype = $c_sc_View$$anon$1.prototype;
$c_sc_View$$anon$1.prototype.iterator__sc_Iterator = (function() {
  return $as_sc_Iterator(this.sc_View$$anon$1__f_it$1.apply__O())
});
var $d_sc_View$$anon$1 = new $TypeData().initClass({
  sc_View$$anon$1: 0
}, false, "scala.collection.View$$anon$1", {
  sc_View$$anon$1: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1
});
$c_sc_View$$anon$1.prototype.$classData = $d_sc_View$$anon$1;
function $ct_sc_View$Drop__sc_IterableOps__I__($thiz, underlying, n) {
  $thiz.sc_View$Drop__f_underlying = underlying;
  $thiz.sc_View$Drop__f_n = n;
  $thiz.sc_View$Drop__f_normN = ((n > 0) ? n : 0);
  return $thiz
}
/** @constructor */
function $c_sc_View$Drop() {
  this.sc_View$Drop__f_underlying = null;
  this.sc_View$Drop__f_n = 0;
  this.sc_View$Drop__f_normN = 0
}
$c_sc_View$Drop.prototype = new $h_sc_AbstractView();
$c_sc_View$Drop.prototype.constructor = $c_sc_View$Drop;
/** @constructor */
function $h_sc_View$Drop() {
  /*<skip>*/
}
$h_sc_View$Drop.prototype = $c_sc_View$Drop.prototype;
$c_sc_View$Drop.prototype.iterator__sc_Iterator = (function() {
  return this.sc_View$Drop__f_underlying.iterator__sc_Iterator().drop__I__sc_Iterator(this.sc_View$Drop__f_n)
});
$c_sc_View$Drop.prototype.knownSize__I = (function() {
  var size = this.sc_View$Drop__f_underlying.knownSize__I();
  if ((size >= 0)) {
    var x = ((size - this.sc_View$Drop__f_normN) | 0);
    return ((x > 0) ? x : 0)
  } else {
    return (-1)
  }
});
$c_sc_View$Drop.prototype.isEmpty__Z = (function() {
  var this$1 = this.iterator__sc_Iterator();
  return (!this$1.hasNext__Z())
});
var $d_sc_View$Drop = new $TypeData().initClass({
  sc_View$Drop: 0
}, false, "scala.collection.View$Drop", {
  sc_View$Drop: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1
});
$c_sc_View$Drop.prototype.$classData = $d_sc_View$Drop;
/** @constructor */
function $c_sc_AbstractSet() {
  /*<skip>*/
}
$c_sc_AbstractSet.prototype = new $h_sc_AbstractIterable();
$c_sc_AbstractSet.prototype.constructor = $c_sc_AbstractSet;
/** @constructor */
function $h_sc_AbstractSet() {
  /*<skip>*/
}
$h_sc_AbstractSet.prototype = $c_sc_AbstractSet.prototype;
$c_sc_AbstractSet.prototype.equals__O__Z = (function(that) {
  return $f_sc_Set__equals__O__Z(this, that)
});
$c_sc_AbstractSet.prototype.hashCode__I = (function() {
  var this$1 = $m_s_util_hashing_MurmurHash3$();
  return this$1.unorderedHash__sc_IterableOnce__I__I(this, this$1.s_util_hashing_MurmurHash3$__f_setSeed)
});
$c_sc_AbstractSet.prototype.stringPrefix__T = (function() {
  return "Set"
});
$c_sc_AbstractSet.prototype.toString__T = (function() {
  return $f_sc_Iterable__toString__T(this)
});
$c_sc_AbstractSet.prototype.subsetOf__sc_Set__Z = (function(that) {
  return this.forall__F1__Z(that)
});
$c_sc_AbstractSet.prototype.apply__O__O = (function(v1) {
  return this.contains__O__Z(v1)
});
function $f_sc_Map__equals__O__Z($thiz, o) {
  if (($thiz === o)) {
    return true
  } else if ($is_sc_Map(o)) {
    var x2 = $as_sc_Map(o);
    if (($thiz.size__I() === x2.size__I())) {
      try {
        return $thiz.forall__F1__Z(new $c_sjsr_AnonFunction1(((this$1, x2$1) => ((kv$2) => {
          var kv = $as_T2(kv$2);
          return $m_sr_BoxesRunTime$().equals__O__O__Z(x2$1.getOrElse__O__F0__O(kv.T2__f__1, $m_sc_Map$().sc_Map$__f_scala$collection$Map$$DefaultSentinelFn), kv.T2__f__2)
        }))($thiz, x2)))
      } catch (e) {
        if ((e instanceof $c_jl_ClassCastException)) {
          return false
        } else {
          throw e
        }
      }
    } else {
      return false
    }
  } else {
    return false
  }
}
function $is_sc_Map(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_Map)))
}
function $as_sc_Map(obj) {
  return (($is_sc_Map(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.Map"))
}
function $isArrayOf_sc_Map(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_Map)))
}
function $asArrayOf_sc_Map(obj, depth) {
  return (($isArrayOf_sc_Map(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.Map;", depth))
}
/** @constructor */
function $c_sc_AbstractSeq() {
  /*<skip>*/
}
$c_sc_AbstractSeq.prototype = new $h_sc_AbstractIterable();
$c_sc_AbstractSeq.prototype.constructor = $c_sc_AbstractSeq;
/** @constructor */
function $h_sc_AbstractSeq() {
  /*<skip>*/
}
$h_sc_AbstractSeq.prototype = $c_sc_AbstractSeq.prototype;
$c_sc_AbstractSeq.prototype.canEqual__O__Z = (function(that) {
  return true
});
$c_sc_AbstractSeq.prototype.equals__O__Z = (function(o) {
  return $f_sc_Seq__equals__O__Z(this, o)
});
$c_sc_AbstractSeq.prototype.hashCode__I = (function() {
  return $m_s_util_hashing_MurmurHash3$().seqHash__sc_Seq__I(this)
});
$c_sc_AbstractSeq.prototype.toString__T = (function() {
  return $f_sc_Iterable__toString__T(this)
});
$c_sc_AbstractSeq.prototype.indexWhere__F1__I__I = (function(p, from) {
  var this$1 = this.iterator__sc_Iterator();
  return $f_sc_Iterator__indexWhere__F1__I__I(this$1, p, from)
});
$c_sc_AbstractSeq.prototype.isEmpty__Z = (function() {
  return $f_sc_SeqOps__isEmpty__Z(this)
});
$c_sc_AbstractSeq.prototype.sameElements__sc_IterableOnce__Z = (function(that) {
  return $f_sc_SeqOps__sameElements__sc_IterableOnce__Z(this, that)
});
/** @constructor */
function $c_sc_AbstractSeqView() {
  /*<skip>*/
}
$c_sc_AbstractSeqView.prototype = new $h_sc_AbstractView();
$c_sc_AbstractSeqView.prototype.constructor = $c_sc_AbstractSeqView;
/** @constructor */
function $h_sc_AbstractSeqView() {
  /*<skip>*/
}
$h_sc_AbstractSeqView.prototype = $c_sc_AbstractSeqView.prototype;
$c_sc_AbstractSeqView.prototype.drop__I__sc_SeqView = (function(n) {
  return $ct_sc_SeqView$Drop__sc_SeqOps__I__(new $c_sc_SeqView$Drop(), this, n)
});
$c_sc_AbstractSeqView.prototype.stringPrefix__T = (function() {
  return "SeqView"
});
$c_sc_AbstractSeqView.prototype.indexWhere__F1__I__I = (function(p, from) {
  var this$1 = this.iterator__sc_Iterator();
  return $f_sc_Iterator__indexWhere__F1__I__I(this$1, p, from)
});
$c_sc_AbstractSeqView.prototype.lengthCompare__I__I = (function(len) {
  return $f_sc_IterableOps__sizeCompare__I__I(this, len)
});
$c_sc_AbstractSeqView.prototype.isEmpty__Z = (function() {
  return $f_sc_SeqOps__isEmpty__Z(this)
});
$c_sc_AbstractSeqView.prototype.drop__I__O = (function(n) {
  return this.drop__I__sc_SeqView(n)
});
function $is_sc_IndexedSeq(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_IndexedSeq)))
}
function $as_sc_IndexedSeq(obj) {
  return (($is_sc_IndexedSeq(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.IndexedSeq"))
}
function $isArrayOf_sc_IndexedSeq(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_IndexedSeq)))
}
function $asArrayOf_sc_IndexedSeq(obj, depth) {
  return (($isArrayOf_sc_IndexedSeq(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.IndexedSeq;", depth))
}
function $is_sc_LinearSeq(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sc_LinearSeq)))
}
function $as_sc_LinearSeq(obj) {
  return (($is_sc_LinearSeq(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.LinearSeq"))
}
function $isArrayOf_sc_LinearSeq(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_LinearSeq)))
}
function $asArrayOf_sc_LinearSeq(obj, depth) {
  return (($isArrayOf_sc_LinearSeq(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.LinearSeq;", depth))
}
function $is_sci_Set(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_Set)))
}
function $as_sci_Set(obj) {
  return (($is_sci_Set(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Set"))
}
function $isArrayOf_sci_Set(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Set)))
}
function $asArrayOf_sci_Set(obj, depth) {
  return (($isArrayOf_sci_Set(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Set;", depth))
}
/** @constructor */
function $c_sc_AbstractMap() {
  /*<skip>*/
}
$c_sc_AbstractMap.prototype = new $h_sc_AbstractIterable();
$c_sc_AbstractMap.prototype.constructor = $c_sc_AbstractMap;
/** @constructor */
function $h_sc_AbstractMap() {
  /*<skip>*/
}
$h_sc_AbstractMap.prototype = $c_sc_AbstractMap.prototype;
$c_sc_AbstractMap.prototype.equals__O__Z = (function(o) {
  return $f_sc_Map__equals__O__Z(this, o)
});
$c_sc_AbstractMap.prototype.hashCode__I = (function() {
  return $m_s_util_hashing_MurmurHash3$().mapHash__sc_Map__I(this)
});
$c_sc_AbstractMap.prototype.stringPrefix__T = (function() {
  return "Map"
});
$c_sc_AbstractMap.prototype.toString__T = (function() {
  return $f_sc_Iterable__toString__T(this)
});
$c_sc_AbstractMap.prototype.fromSpecific__sc_IterableOnce__sc_IterableOps = (function(coll) {
  return $as_sc_IterableOps(this.mapFactory__sc_MapFactory().from__sc_IterableOnce__O(coll))
});
$c_sc_AbstractMap.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return $f_sc_MapOps__getOrElse__O__F0__O(this, key, default$1)
});
$c_sc_AbstractMap.prototype.foreachEntry__F2__V = (function(f) {
  $f_sc_MapOps__foreachEntry__F2__V(this, f)
});
$c_sc_AbstractMap.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(sb, start, sep, end) {
  return $f_sc_MapOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, sb, start, sep, end)
});
$c_sc_AbstractMap.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  return this.fromSpecific__sc_IterableOnce__sc_IterableOps(coll)
});
function $ct_sc_SeqView$Drop__sc_SeqOps__I__($thiz, underlying, n) {
  $thiz.sc_SeqView$Drop__f_underlying = underlying;
  $thiz.sc_SeqView$Drop__f_n = n;
  $ct_sc_View$Drop__sc_IterableOps__I__($thiz, underlying, n);
  return $thiz
}
/** @constructor */
function $c_sc_SeqView$Drop() {
  this.sc_View$Drop__f_underlying = null;
  this.sc_View$Drop__f_n = 0;
  this.sc_View$Drop__f_normN = 0;
  this.sc_SeqView$Drop__f_underlying = null;
  this.sc_SeqView$Drop__f_n = 0
}
$c_sc_SeqView$Drop.prototype = new $h_sc_View$Drop();
$c_sc_SeqView$Drop.prototype.constructor = $c_sc_SeqView$Drop;
/** @constructor */
function $h_sc_SeqView$Drop() {
  /*<skip>*/
}
$h_sc_SeqView$Drop.prototype = $c_sc_SeqView$Drop.prototype;
$c_sc_SeqView$Drop.prototype.stringPrefix__T = (function() {
  return "SeqView"
});
$c_sc_SeqView$Drop.prototype.indexWhere__F1__I__I = (function(p, from) {
  var this$1 = this.iterator__sc_Iterator();
  return $f_sc_Iterator__indexWhere__F1__I__I(this$1, p, from)
});
$c_sc_SeqView$Drop.prototype.lengthCompare__I__I = (function(len) {
  return $f_sc_IterableOps__sizeCompare__I__I(this, len)
});
$c_sc_SeqView$Drop.prototype.isEmpty__Z = (function() {
  return $f_sc_SeqOps__isEmpty__Z(this)
});
$c_sc_SeqView$Drop.prototype.length__I = (function() {
  var this$1 = this.sc_SeqView$Drop__f_underlying;
  var x = ((this$1.length__I() - this.sc_View$Drop__f_normN) | 0);
  return ((x > 0) ? x : 0)
});
$c_sc_SeqView$Drop.prototype.apply__I__O = (function(i) {
  return this.sc_SeqView$Drop__f_underlying.apply__I__O(((i + this.sc_View$Drop__f_normN) | 0))
});
$c_sc_SeqView$Drop.prototype.drop__I__sc_SeqView = (function(n) {
  return $ct_sc_SeqView$Drop__sc_SeqOps__I__(new $c_sc_SeqView$Drop(), this.sc_SeqView$Drop__f_underlying, ((this.sc_SeqView$Drop__f_n + n) | 0))
});
$c_sc_SeqView$Drop.prototype.drop__I__O = (function(n) {
  return this.drop__I__sc_SeqView(n)
});
var $d_sc_SeqView$Drop = new $TypeData().initClass({
  sc_SeqView$Drop: 0
}, false, "scala.collection.SeqView$Drop", {
  sc_SeqView$Drop: 1,
  sc_View$Drop: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1
});
$c_sc_SeqView$Drop.prototype.$classData = $d_sc_SeqView$Drop;
function $ct_sc_SeqView$Id__sc_SeqOps__($thiz, underlying) {
  $thiz.sc_SeqView$Id__f_underlying = underlying;
  return $thiz
}
/** @constructor */
function $c_sc_SeqView$Id() {
  this.sc_SeqView$Id__f_underlying = null
}
$c_sc_SeqView$Id.prototype = new $h_sc_AbstractSeqView();
$c_sc_SeqView$Id.prototype.constructor = $c_sc_SeqView$Id;
/** @constructor */
function $h_sc_SeqView$Id() {
  /*<skip>*/
}
$h_sc_SeqView$Id.prototype = $c_sc_SeqView$Id.prototype;
$c_sc_SeqView$Id.prototype.apply__I__O = (function(idx) {
  return this.sc_SeqView$Id__f_underlying.apply__I__O(idx)
});
$c_sc_SeqView$Id.prototype.length__I = (function() {
  return this.sc_SeqView$Id__f_underlying.length__I()
});
$c_sc_SeqView$Id.prototype.iterator__sc_Iterator = (function() {
  return this.sc_SeqView$Id__f_underlying.iterator__sc_Iterator()
});
$c_sc_SeqView$Id.prototype.knownSize__I = (function() {
  return this.sc_SeqView$Id__f_underlying.knownSize__I()
});
$c_sc_SeqView$Id.prototype.isEmpty__Z = (function() {
  return this.sc_SeqView$Id__f_underlying.isEmpty__Z()
});
var $d_sc_SeqView$Id = new $TypeData().initClass({
  sc_SeqView$Id: 0
}, false, "scala.collection.SeqView$Id", {
  sc_SeqView$Id: 1,
  sc_AbstractSeqView: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1
});
$c_sc_SeqView$Id.prototype.$classData = $d_sc_SeqView$Id;
function $is_sci_Seq(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_Seq)))
}
function $as_sci_Seq(obj) {
  return (($is_sci_Seq(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Seq"))
}
function $isArrayOf_sci_Seq(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Seq)))
}
function $asArrayOf_sci_Seq(obj, depth) {
  return (($isArrayOf_sci_Seq(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Seq;", depth))
}
function $is_sci_Map(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_Map)))
}
function $as_sci_Map(obj) {
  return (($is_sci_Map(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Map"))
}
function $isArrayOf_sci_Map(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Map)))
}
function $asArrayOf_sci_Map(obj, depth) {
  return (($isArrayOf_sci_Map(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Map;", depth))
}
/** @constructor */
function $c_sc_AbstractIndexedSeqView() {
  /*<skip>*/
}
$c_sc_AbstractIndexedSeqView.prototype = new $h_sc_AbstractSeqView();
$c_sc_AbstractIndexedSeqView.prototype.constructor = $c_sc_AbstractIndexedSeqView;
/** @constructor */
function $h_sc_AbstractIndexedSeqView() {
  /*<skip>*/
}
$h_sc_AbstractIndexedSeqView.prototype = $c_sc_AbstractIndexedSeqView.prototype;
$c_sc_AbstractIndexedSeqView.prototype.iterator__sc_Iterator = (function() {
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this)
});
$c_sc_AbstractIndexedSeqView.prototype.stringPrefix__T = (function() {
  return "IndexedSeqView"
});
$c_sc_AbstractIndexedSeqView.prototype.head__O = (function() {
  return this.apply__I__O(0)
});
$c_sc_AbstractIndexedSeqView.prototype.lengthCompare__I__I = (function(len) {
  var x = this.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sc_AbstractIndexedSeqView.prototype.knownSize__I = (function() {
  return this.length__I()
});
$c_sc_AbstractIndexedSeqView.prototype.drop__I__sc_SeqView = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
$c_sc_AbstractIndexedSeqView.prototype.drop__I__O = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
/** @constructor */
function $c_sci_AbstractSet() {
  /*<skip>*/
}
$c_sci_AbstractSet.prototype = new $h_sc_AbstractSet();
$c_sci_AbstractSet.prototype.constructor = $c_sci_AbstractSet;
/** @constructor */
function $h_sci_AbstractSet() {
  /*<skip>*/
}
$h_sci_AbstractSet.prototype = $c_sci_AbstractSet.prototype;
$c_sci_AbstractSet.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_Set$()
});
/** @constructor */
function $c_sc_IndexedSeqView$Drop(underlying, n) {
  this.sc_View$Drop__f_underlying = null;
  this.sc_View$Drop__f_n = 0;
  this.sc_View$Drop__f_normN = 0;
  this.sc_SeqView$Drop__f_underlying = null;
  this.sc_SeqView$Drop__f_n = 0;
  $ct_sc_SeqView$Drop__sc_SeqOps__I__(this, underlying, n)
}
$c_sc_IndexedSeqView$Drop.prototype = new $h_sc_SeqView$Drop();
$c_sc_IndexedSeqView$Drop.prototype.constructor = $c_sc_IndexedSeqView$Drop;
/** @constructor */
function $h_sc_IndexedSeqView$Drop() {
  /*<skip>*/
}
$h_sc_IndexedSeqView$Drop.prototype = $c_sc_IndexedSeqView$Drop.prototype;
$c_sc_IndexedSeqView$Drop.prototype.iterator__sc_Iterator = (function() {
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this)
});
$c_sc_IndexedSeqView$Drop.prototype.stringPrefix__T = (function() {
  return "IndexedSeqView"
});
$c_sc_IndexedSeqView$Drop.prototype.head__O = (function() {
  return this.apply__I__O(0)
});
$c_sc_IndexedSeqView$Drop.prototype.lengthCompare__I__I = (function(len) {
  var x = this.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sc_IndexedSeqView$Drop.prototype.knownSize__I = (function() {
  return this.length__I()
});
$c_sc_IndexedSeqView$Drop.prototype.drop__I__sc_SeqView = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
$c_sc_IndexedSeqView$Drop.prototype.drop__I__O = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
var $d_sc_IndexedSeqView$Drop = new $TypeData().initClass({
  sc_IndexedSeqView$Drop: 0
}, false, "scala.collection.IndexedSeqView$Drop", {
  sc_IndexedSeqView$Drop: 1,
  sc_SeqView$Drop: 1,
  sc_View$Drop: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1,
  sc_IndexedSeqView: 1,
  sc_IndexedSeqOps: 1
});
$c_sc_IndexedSeqView$Drop.prototype.$classData = $d_sc_IndexedSeqView$Drop;
/** @constructor */
function $c_sc_IndexedSeqView$Id(underlying) {
  this.sc_SeqView$Id__f_underlying = null;
  $ct_sc_SeqView$Id__sc_SeqOps__(this, underlying)
}
$c_sc_IndexedSeqView$Id.prototype = new $h_sc_SeqView$Id();
$c_sc_IndexedSeqView$Id.prototype.constructor = $c_sc_IndexedSeqView$Id;
/** @constructor */
function $h_sc_IndexedSeqView$Id() {
  /*<skip>*/
}
$h_sc_IndexedSeqView$Id.prototype = $c_sc_IndexedSeqView$Id.prototype;
$c_sc_IndexedSeqView$Id.prototype.iterator__sc_Iterator = (function() {
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this)
});
$c_sc_IndexedSeqView$Id.prototype.stringPrefix__T = (function() {
  return "IndexedSeqView"
});
$c_sc_IndexedSeqView$Id.prototype.head__O = (function() {
  return this.apply__I__O(0)
});
$c_sc_IndexedSeqView$Id.prototype.lengthCompare__I__I = (function(len) {
  var x = this.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sc_IndexedSeqView$Id.prototype.knownSize__I = (function() {
  return this.length__I()
});
$c_sc_IndexedSeqView$Id.prototype.drop__I__sc_SeqView = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
$c_sc_IndexedSeqView$Id.prototype.drop__I__O = (function(n) {
  return new $c_sc_IndexedSeqView$Drop(this, n)
});
var $d_sc_IndexedSeqView$Id = new $TypeData().initClass({
  sc_IndexedSeqView$Id: 0
}, false, "scala.collection.IndexedSeqView$Id", {
  sc_IndexedSeqView$Id: 1,
  sc_SeqView$Id: 1,
  sc_AbstractSeqView: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1,
  sc_IndexedSeqView: 1,
  sc_IndexedSeqOps: 1
});
$c_sc_IndexedSeqView$Id.prototype.$classData = $d_sc_IndexedSeqView$Id;
/** @constructor */
function $c_sci_AbstractSeq() {
  /*<skip>*/
}
$c_sci_AbstractSeq.prototype = new $h_sc_AbstractSeq();
$c_sci_AbstractSeq.prototype.constructor = $c_sci_AbstractSeq;
/** @constructor */
function $h_sci_AbstractSeq() {
  /*<skip>*/
}
$h_sci_AbstractSeq.prototype = $c_sci_AbstractSeq.prototype;
/** @constructor */
function $c_scm_ArrayBufferView(array, length) {
  this.scm_ArrayBufferView__f_array = null;
  this.scm_ArrayBufferView__f_length = 0;
  this.scm_ArrayBufferView__f_array = array;
  this.scm_ArrayBufferView__f_length = length
}
$c_scm_ArrayBufferView.prototype = new $h_sc_AbstractIndexedSeqView();
$c_scm_ArrayBufferView.prototype.constructor = $c_scm_ArrayBufferView;
/** @constructor */
function $h_scm_ArrayBufferView() {
  /*<skip>*/
}
$h_scm_ArrayBufferView.prototype = $c_scm_ArrayBufferView.prototype;
$c_scm_ArrayBufferView.prototype.length__I = (function() {
  return this.scm_ArrayBufferView__f_length
});
$c_scm_ArrayBufferView.prototype.apply__I__O = (function(n) {
  if ((n < this.scm_ArrayBufferView__f_length)) {
    return this.scm_ArrayBufferView__f_array.get(n)
  } else {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (((n + " is out of bounds (min 0, max ") + (((-1) + this.scm_ArrayBufferView__f_length) | 0)) + ")"))
  }
});
$c_scm_ArrayBufferView.prototype.className__T = (function() {
  return "ArrayBufferView"
});
var $d_scm_ArrayBufferView = new $TypeData().initClass({
  scm_ArrayBufferView: 0
}, false, "scala.collection.mutable.ArrayBufferView", {
  scm_ArrayBufferView: 1,
  sc_AbstractIndexedSeqView: 1,
  sc_AbstractSeqView: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1,
  sc_IndexedSeqView: 1,
  sc_IndexedSeqOps: 1
});
$c_scm_ArrayBufferView.prototype.$classData = $d_scm_ArrayBufferView;
/** @constructor */
function $c_sci_AbstractMap() {
  /*<skip>*/
}
$c_sci_AbstractMap.prototype = new $h_sc_AbstractMap();
$c_sci_AbstractMap.prototype.constructor = $c_sci_AbstractMap;
/** @constructor */
function $h_sci_AbstractMap() {
  /*<skip>*/
}
$h_sci_AbstractMap.prototype = $c_sci_AbstractMap.prototype;
$c_sci_AbstractMap.prototype.mapFactory__sc_MapFactory = (function() {
  return $m_sci_Map$()
});
$c_sci_AbstractMap.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_Iterable$()
});
function $f_sci_IndexedSeq__canEqual__O__Z($thiz, that) {
  if ((!$is_sci_IndexedSeq(that))) {
    return true
  } else {
    var x2 = $as_sci_IndexedSeq(that);
    return ($thiz.length__I() === x2.length__I())
  }
}
function $f_sci_IndexedSeq__sameElements__sc_IterableOnce__Z($thiz, o) {
  if ($is_sci_IndexedSeq(o)) {
    var x2 = $as_sci_IndexedSeq(o);
    if (($thiz === x2)) {
      return true
    } else {
      var length = $thiz.length__I();
      var equal = (length === x2.length__I());
      if (equal) {
        var index = 0;
        var a = $thiz.applyPreferredMaxLength__I();
        var b = x2.applyPreferredMaxLength__I();
        var preferredLength = ((a < b) ? a : b);
        var hi = (length >> 31);
        var hi$1 = (preferredLength >> 31);
        var lo = (preferredLength << 1);
        var hi$2 = (((preferredLength >>> 31) | 0) | (hi$1 << 1));
        if (((hi === hi$2) ? (((-2147483648) ^ length) > ((-2147483648) ^ lo)) : (hi > hi$2))) {
          var maxApplyCompare = preferredLength
        } else {
          var maxApplyCompare = length
        };
        while (((index < maxApplyCompare) && equal)) {
          equal = $m_sr_BoxesRunTime$().equals__O__O__Z($thiz.apply__I__O(index), x2.apply__I__O(index));
          index = ((1 + index) | 0)
        };
        if (((index < length) && equal)) {
          var thisIt = $thiz.iterator__sc_Iterator().drop__I__sc_Iterator(index);
          var thatIt = x2.iterator__sc_Iterator().drop__I__sc_Iterator(index);
          while ((equal && thisIt.hasNext__Z())) {
            equal = $m_sr_BoxesRunTime$().equals__O__O__Z(thisIt.next__O(), thatIt.next__O())
          }
        }
      };
      return equal
    }
  } else {
    return $f_sc_SeqOps__sameElements__sc_IterableOnce__Z($thiz, o)
  }
}
function $is_sci_IndexedSeq(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_IndexedSeq)))
}
function $as_sci_IndexedSeq(obj) {
  return (($is_sci_IndexedSeq(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.IndexedSeq"))
}
function $isArrayOf_sci_IndexedSeq(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_IndexedSeq)))
}
function $asArrayOf_sci_IndexedSeq(obj, depth) {
  return (($isArrayOf_sci_IndexedSeq(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.IndexedSeq;", depth))
}
/** @constructor */
function $c_sci_Set$EmptySet$() {
  /*<skip>*/
}
$c_sci_Set$EmptySet$.prototype = new $h_sci_AbstractSet();
$c_sci_Set$EmptySet$.prototype.constructor = $c_sci_Set$EmptySet$;
/** @constructor */
function $h_sci_Set$EmptySet$() {
  /*<skip>*/
}
$h_sci_Set$EmptySet$.prototype = $c_sci_Set$EmptySet$.prototype;
$c_sci_Set$EmptySet$.prototype.size__I = (function() {
  return 0
});
$c_sci_Set$EmptySet$.prototype.isEmpty__Z = (function() {
  return true
});
$c_sci_Set$EmptySet$.prototype.knownSize__I = (function() {
  return 0
});
$c_sci_Set$EmptySet$.prototype.subsetOf__sc_Set__Z = (function(that) {
  return true
});
$c_sci_Set$EmptySet$.prototype.contains__O__Z = (function(elem) {
  return false
});
$c_sci_Set$EmptySet$.prototype.iterator__sc_Iterator = (function() {
  return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty
});
$c_sci_Set$EmptySet$.prototype.incl__O__sci_SetOps = (function(elem) {
  return new $c_sci_Set$Set1(elem)
});
var $d_sci_Set$EmptySet$ = new $TypeData().initClass({
  sci_Set$EmptySet$: 0
}, false, "scala.collection.immutable.Set$EmptySet$", {
  sci_Set$EmptySet$: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$EmptySet$.prototype.$classData = $d_sci_Set$EmptySet$;
var $n_sci_Set$EmptySet$;
function $m_sci_Set$EmptySet$() {
  if ((!$n_sci_Set$EmptySet$)) {
    $n_sci_Set$EmptySet$ = new $c_sci_Set$EmptySet$()
  };
  return $n_sci_Set$EmptySet$
}
/** @constructor */
function $c_sc_StringView(s) {
  this.sc_StringView__f_s = null;
  this.sc_StringView__f_s = s
}
$c_sc_StringView.prototype = new $h_sc_AbstractIndexedSeqView();
$c_sc_StringView.prototype.constructor = $c_sc_StringView;
/** @constructor */
function $h_sc_StringView() {
  /*<skip>*/
}
$h_sc_StringView.prototype = $c_sc_StringView.prototype;
$c_sc_StringView.prototype.length__I = (function() {
  var this$1 = this.sc_StringView__f_s;
  return $uI(this$1.length)
});
$c_sc_StringView.prototype.toString__T = (function() {
  return (("StringView(" + this.sc_StringView__f_s) + ")")
});
$c_sc_StringView.prototype.productPrefix__T = (function() {
  return "StringView"
});
$c_sc_StringView.prototype.productArity__I = (function() {
  return 1
});
$c_sc_StringView.prototype.productElement__I__O = (function(x$1) {
  return ((x$1 === 0) ? this.sc_StringView__f_s : $m_sr_Statics$().ioobe__I__O(x$1))
});
$c_sc_StringView.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_sc_StringView.prototype.hashCode__I = (function() {
  var this$2 = $m_s_util_hashing_MurmurHash3$();
  return this$2.productHash__s_Product__I__Z__I(this, (-889275714), false)
});
$c_sc_StringView.prototype.equals__O__Z = (function(x$1) {
  if ((this === x$1)) {
    return true
  } else if ((x$1 instanceof $c_sc_StringView)) {
    var StringView$1 = $as_sc_StringView(x$1);
    return (this.sc_StringView__f_s === StringView$1.sc_StringView__f_s)
  } else {
    return false
  }
});
$c_sc_StringView.prototype.apply__I__O = (function(i) {
  var this$1 = this.sc_StringView__f_s;
  return $bC((65535 & $uI(this$1.charCodeAt(i))))
});
function $as_sc_StringView(obj) {
  return (((obj instanceof $c_sc_StringView) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.StringView"))
}
function $isArrayOf_sc_StringView(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sc_StringView)))
}
function $asArrayOf_sc_StringView(obj, depth) {
  return (($isArrayOf_sc_StringView(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.StringView;", depth))
}
var $d_sc_StringView = new $TypeData().initClass({
  sc_StringView: 0
}, false, "scala.collection.StringView", {
  sc_StringView: 1,
  sc_AbstractIndexedSeqView: 1,
  sc_AbstractSeqView: 1,
  sc_AbstractView: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_View: 1,
  Ljava_io_Serializable: 1,
  sc_SeqView: 1,
  sc_SeqOps: 1,
  sc_IndexedSeqView: 1,
  sc_IndexedSeqOps: 1,
  s_Product: 1,
  s_Equals: 1
});
$c_sc_StringView.prototype.$classData = $d_sc_StringView;
/** @constructor */
function $c_sci_Set$Set1(elem1) {
  this.sci_Set$Set1__f_elem1 = null;
  this.sci_Set$Set1__f_elem1 = elem1
}
$c_sci_Set$Set1.prototype = new $h_sci_AbstractSet();
$c_sci_Set$Set1.prototype.constructor = $c_sci_Set$Set1;
/** @constructor */
function $h_sci_Set$Set1() {
  /*<skip>*/
}
$h_sci_Set$Set1.prototype = $c_sci_Set$Set1.prototype;
$c_sci_Set$Set1.prototype.size__I = (function() {
  return 1
});
$c_sci_Set$Set1.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Set$Set1.prototype.knownSize__I = (function() {
  return 1
});
$c_sci_Set$Set1.prototype.contains__O__Z = (function(elem) {
  return $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set1__f_elem1)
});
$c_sci_Set$Set1.prototype.incl__O__sci_Set = (function(elem) {
  return (this.contains__O__Z(elem) ? this : new $c_sci_Set$Set2(this.sci_Set$Set1__f_elem1, elem))
});
$c_sci_Set$Set1.prototype.iterator__sc_Iterator = (function() {
  $m_sc_Iterator$();
  var a = this.sci_Set$Set1__f_elem1;
  return new $c_sc_Iterator$$anon$20(a)
});
$c_sci_Set$Set1.prototype.forall__F1__Z = (function(p) {
  return $uZ(p.apply__O__O(this.sci_Set$Set1__f_elem1))
});
$c_sci_Set$Set1.prototype.head__O = (function() {
  return this.sci_Set$Set1__f_elem1
});
$c_sci_Set$Set1.prototype.tail__O = (function() {
  return $m_sci_Set$EmptySet$()
});
$c_sci_Set$Set1.prototype.incl__O__sci_SetOps = (function(elem) {
  return this.incl__O__sci_Set(elem)
});
var $d_sci_Set$Set1 = new $TypeData().initClass({
  sci_Set$Set1: 0
}, false, "scala.collection.immutable.Set$Set1", {
  sci_Set$Set1: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set1.prototype.$classData = $d_sci_Set$Set1;
/** @constructor */
function $c_sci_Set$Set2(elem1, elem2) {
  this.sci_Set$Set2__f_elem1 = null;
  this.sci_Set$Set2__f_elem2 = null;
  this.sci_Set$Set2__f_elem1 = elem1;
  this.sci_Set$Set2__f_elem2 = elem2
}
$c_sci_Set$Set2.prototype = new $h_sci_AbstractSet();
$c_sci_Set$Set2.prototype.constructor = $c_sci_Set$Set2;
/** @constructor */
function $h_sci_Set$Set2() {
  /*<skip>*/
}
$h_sci_Set$Set2.prototype = $c_sci_Set$Set2.prototype;
$c_sci_Set$Set2.prototype.size__I = (function() {
  return 2
});
$c_sci_Set$Set2.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Set$Set2.prototype.knownSize__I = (function() {
  return 2
});
$c_sci_Set$Set2.prototype.contains__O__Z = (function(elem) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set2__f_elem1) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set2__f_elem2))
});
$c_sci_Set$Set2.prototype.incl__O__sci_Set = (function(elem) {
  return (this.contains__O__Z(elem) ? this : new $c_sci_Set$Set3(this.sci_Set$Set2__f_elem1, this.sci_Set$Set2__f_elem2, elem))
});
$c_sci_Set$Set2.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Set$Set2$$anon$1(this)
});
$c_sci_Set$Set2.prototype.scala$collection$immutable$Set$Set2$$getElem__I__O = (function(i) {
  switch (i) {
    case 0: {
      return this.sci_Set$Set2__f_elem1;
      break
    }
    case 1: {
      return this.sci_Set$Set2__f_elem2;
      break
    }
    default: {
      throw new $c_s_MatchError(i)
    }
  }
});
$c_sci_Set$Set2.prototype.forall__F1__Z = (function(p) {
  return ($uZ(p.apply__O__O(this.sci_Set$Set2__f_elem1)) && $uZ(p.apply__O__O(this.sci_Set$Set2__f_elem2)))
});
$c_sci_Set$Set2.prototype.head__O = (function() {
  return this.sci_Set$Set2__f_elem1
});
$c_sci_Set$Set2.prototype.tail__sci_Set = (function() {
  return new $c_sci_Set$Set1(this.sci_Set$Set2__f_elem2)
});
$c_sci_Set$Set2.prototype.tail__O = (function() {
  return this.tail__sci_Set()
});
$c_sci_Set$Set2.prototype.incl__O__sci_SetOps = (function(elem) {
  return this.incl__O__sci_Set(elem)
});
var $d_sci_Set$Set2 = new $TypeData().initClass({
  sci_Set$Set2: 0
}, false, "scala.collection.immutable.Set$Set2", {
  sci_Set$Set2: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set2.prototype.$classData = $d_sci_Set$Set2;
/** @constructor */
function $c_sci_Set$Set3(elem1, elem2, elem3) {
  this.sci_Set$Set3__f_elem1 = null;
  this.sci_Set$Set3__f_elem2 = null;
  this.sci_Set$Set3__f_elem3 = null;
  this.sci_Set$Set3__f_elem1 = elem1;
  this.sci_Set$Set3__f_elem2 = elem2;
  this.sci_Set$Set3__f_elem3 = elem3
}
$c_sci_Set$Set3.prototype = new $h_sci_AbstractSet();
$c_sci_Set$Set3.prototype.constructor = $c_sci_Set$Set3;
/** @constructor */
function $h_sci_Set$Set3() {
  /*<skip>*/
}
$h_sci_Set$Set3.prototype = $c_sci_Set$Set3.prototype;
$c_sci_Set$Set3.prototype.size__I = (function() {
  return 3
});
$c_sci_Set$Set3.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Set$Set3.prototype.knownSize__I = (function() {
  return 3
});
$c_sci_Set$Set3.prototype.contains__O__Z = (function(elem) {
  return (($m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set3__f_elem1) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set3__f_elem2)) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set3__f_elem3))
});
$c_sci_Set$Set3.prototype.incl__O__sci_Set = (function(elem) {
  return (this.contains__O__Z(elem) ? this : new $c_sci_Set$Set4(this.sci_Set$Set3__f_elem1, this.sci_Set$Set3__f_elem2, this.sci_Set$Set3__f_elem3, elem))
});
$c_sci_Set$Set3.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Set$Set3$$anon$2(this)
});
$c_sci_Set$Set3.prototype.scala$collection$immutable$Set$Set3$$getElem__I__O = (function(i) {
  switch (i) {
    case 0: {
      return this.sci_Set$Set3__f_elem1;
      break
    }
    case 1: {
      return this.sci_Set$Set3__f_elem2;
      break
    }
    case 2: {
      return this.sci_Set$Set3__f_elem3;
      break
    }
    default: {
      throw new $c_s_MatchError(i)
    }
  }
});
$c_sci_Set$Set3.prototype.forall__F1__Z = (function(p) {
  return (($uZ(p.apply__O__O(this.sci_Set$Set3__f_elem1)) && $uZ(p.apply__O__O(this.sci_Set$Set3__f_elem2))) && $uZ(p.apply__O__O(this.sci_Set$Set3__f_elem3)))
});
$c_sci_Set$Set3.prototype.head__O = (function() {
  return this.sci_Set$Set3__f_elem1
});
$c_sci_Set$Set3.prototype.tail__sci_Set = (function() {
  return new $c_sci_Set$Set2(this.sci_Set$Set3__f_elem2, this.sci_Set$Set3__f_elem3)
});
$c_sci_Set$Set3.prototype.tail__O = (function() {
  return this.tail__sci_Set()
});
$c_sci_Set$Set3.prototype.incl__O__sci_SetOps = (function(elem) {
  return this.incl__O__sci_Set(elem)
});
var $d_sci_Set$Set3 = new $TypeData().initClass({
  sci_Set$Set3: 0
}, false, "scala.collection.immutable.Set$Set3", {
  sci_Set$Set3: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set3.prototype.$classData = $d_sci_Set$Set3;
/** @constructor */
function $c_sci_Set$Set4(elem1, elem2, elem3, elem4) {
  this.sci_Set$Set4__f_elem1 = null;
  this.sci_Set$Set4__f_elem2 = null;
  this.sci_Set$Set4__f_elem3 = null;
  this.sci_Set$Set4__f_elem4 = null;
  this.sci_Set$Set4__f_elem1 = elem1;
  this.sci_Set$Set4__f_elem2 = elem2;
  this.sci_Set$Set4__f_elem3 = elem3;
  this.sci_Set$Set4__f_elem4 = elem4
}
$c_sci_Set$Set4.prototype = new $h_sci_AbstractSet();
$c_sci_Set$Set4.prototype.constructor = $c_sci_Set$Set4;
/** @constructor */
function $h_sci_Set$Set4() {
  /*<skip>*/
}
$h_sci_Set$Set4.prototype = $c_sci_Set$Set4.prototype;
$c_sci_Set$Set4.prototype.size__I = (function() {
  return 4
});
$c_sci_Set$Set4.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Set$Set4.prototype.knownSize__I = (function() {
  return 4
});
$c_sci_Set$Set4.prototype.contains__O__Z = (function(elem) {
  return ((($m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set4__f_elem1) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set4__f_elem2)) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set4__f_elem3)) || $m_sr_BoxesRunTime$().equals__O__O__Z(elem, this.sci_Set$Set4__f_elem4))
});
$c_sci_Set$Set4.prototype.incl__O__sci_Set = (function(elem) {
  if (this.contains__O__Z(elem)) {
    return this
  } else {
    var this$1 = $m_sci_HashSet$();
    var this$2 = this$1.sci_HashSet$__f_EmptySet;
    var elem$1 = this.sci_Set$Set4__f_elem1;
    var this$3 = this$2.incl__O__sci_HashSet(elem$1);
    var elem$2 = this.sci_Set$Set4__f_elem2;
    var this$4 = this$3.incl__O__sci_HashSet(elem$2);
    var elem$3 = this.sci_Set$Set4__f_elem3;
    var this$5 = this$4.incl__O__sci_HashSet(elem$3);
    var elem$4 = this.sci_Set$Set4__f_elem4;
    var this$6 = this$5.incl__O__sci_HashSet(elem$4);
    return this$6.incl__O__sci_HashSet(elem)
  }
});
$c_sci_Set$Set4.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Set$Set4$$anon$3(this)
});
$c_sci_Set$Set4.prototype.scala$collection$immutable$Set$Set4$$getElem__I__O = (function(i) {
  switch (i) {
    case 0: {
      return this.sci_Set$Set4__f_elem1;
      break
    }
    case 1: {
      return this.sci_Set$Set4__f_elem2;
      break
    }
    case 2: {
      return this.sci_Set$Set4__f_elem3;
      break
    }
    case 3: {
      return this.sci_Set$Set4__f_elem4;
      break
    }
    default: {
      throw new $c_s_MatchError(i)
    }
  }
});
$c_sci_Set$Set4.prototype.forall__F1__Z = (function(p) {
  return ((($uZ(p.apply__O__O(this.sci_Set$Set4__f_elem1)) && $uZ(p.apply__O__O(this.sci_Set$Set4__f_elem2))) && $uZ(p.apply__O__O(this.sci_Set$Set4__f_elem3))) && $uZ(p.apply__O__O(this.sci_Set$Set4__f_elem4)))
});
$c_sci_Set$Set4.prototype.head__O = (function() {
  return this.sci_Set$Set4__f_elem1
});
$c_sci_Set$Set4.prototype.tail__sci_Set = (function() {
  return new $c_sci_Set$Set3(this.sci_Set$Set4__f_elem2, this.sci_Set$Set4__f_elem3, this.sci_Set$Set4__f_elem4)
});
$c_sci_Set$Set4.prototype.buildTo__scm_Builder__scm_Builder = (function(builder) {
  return $as_scm_Builder(builder.addOne__O__scm_Growable(this.sci_Set$Set4__f_elem1).addOne__O__scm_Growable(this.sci_Set$Set4__f_elem2).addOne__O__scm_Growable(this.sci_Set$Set4__f_elem3).addOne__O__scm_Growable(this.sci_Set$Set4__f_elem4))
});
$c_sci_Set$Set4.prototype.tail__O = (function() {
  return this.tail__sci_Set()
});
$c_sci_Set$Set4.prototype.incl__O__sci_SetOps = (function(elem) {
  return this.incl__O__sci_Set(elem)
});
function $as_sci_Set$Set4(obj) {
  return (((obj instanceof $c_sci_Set$Set4) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Set$Set4"))
}
function $isArrayOf_sci_Set$Set4(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Set$Set4)))
}
function $asArrayOf_sci_Set$Set4(obj, depth) {
  return (($isArrayOf_sci_Set$Set4(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Set$Set4;", depth))
}
var $d_sci_Set$Set4 = new $TypeData().initClass({
  sci_Set$Set4: 0
}, false, "scala.collection.immutable.Set$Set4", {
  sci_Set$Set4: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Set$Set4.prototype.$classData = $d_sci_Set$Set4;
function $is_sci_SortedSet(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.sci_SortedSet)))
}
function $as_sci_SortedSet(obj) {
  return (($is_sci_SortedSet(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.SortedSet"))
}
function $isArrayOf_sci_SortedSet(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_SortedSet)))
}
function $asArrayOf_sci_SortedSet(obj, depth) {
  return (($isArrayOf_sci_SortedSet(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.SortedSet;", depth))
}
/** @constructor */
function $c_scm_AbstractSeq() {
  /*<skip>*/
}
$c_scm_AbstractSeq.prototype = new $h_sc_AbstractSeq();
$c_scm_AbstractSeq.prototype.constructor = $c_scm_AbstractSeq;
/** @constructor */
function $h_scm_AbstractSeq() {
  /*<skip>*/
}
$h_scm_AbstractSeq.prototype = $c_scm_AbstractSeq.prototype;
/** @constructor */
function $c_sci_Map$EmptyMap$() {
  /*<skip>*/
}
$c_sci_Map$EmptyMap$.prototype = new $h_sci_AbstractMap();
$c_sci_Map$EmptyMap$.prototype.constructor = $c_sci_Map$EmptyMap$;
/** @constructor */
function $h_sci_Map$EmptyMap$() {
  /*<skip>*/
}
$h_sci_Map$EmptyMap$.prototype = $c_sci_Map$EmptyMap$.prototype;
$c_sci_Map$EmptyMap$.prototype.size__I = (function() {
  return 0
});
$c_sci_Map$EmptyMap$.prototype.knownSize__I = (function() {
  return 0
});
$c_sci_Map$EmptyMap$.prototype.isEmpty__Z = (function() {
  return true
});
$c_sci_Map$EmptyMap$.prototype.apply__O__E = (function(key) {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
});
$c_sci_Map$EmptyMap$.prototype.contains__O__Z = (function(key) {
  return false
});
$c_sci_Map$EmptyMap$.prototype.get__O__s_Option = (function(key) {
  return $m_s_None$()
});
$c_sci_Map$EmptyMap$.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return default$1.apply__O()
});
$c_sci_Map$EmptyMap$.prototype.iterator__sc_Iterator = (function() {
  return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty
});
$c_sci_Map$EmptyMap$.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return new $c_sci_Map$Map1(key, value)
});
$c_sci_Map$EmptyMap$.prototype.apply__O__O = (function(key) {
  this.apply__O__E(key)
});
var $d_sci_Map$EmptyMap$ = new $TypeData().initClass({
  sci_Map$EmptyMap$: 0
}, false, "scala.collection.immutable.Map$EmptyMap$", {
  sci_Map$EmptyMap$: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$EmptyMap$.prototype.$classData = $d_sci_Map$EmptyMap$;
var $n_sci_Map$EmptyMap$;
function $m_sci_Map$EmptyMap$() {
  if ((!$n_sci_Map$EmptyMap$)) {
    $n_sci_Map$EmptyMap$ = new $c_sci_Map$EmptyMap$()
  };
  return $n_sci_Map$EmptyMap$
}
/** @constructor */
function $c_sci_Map$Map1(key1, value1) {
  this.sci_Map$Map1__f_key1 = null;
  this.sci_Map$Map1__f_value1 = null;
  this.sci_Map$Map1__f_key1 = key1;
  this.sci_Map$Map1__f_value1 = value1
}
$c_sci_Map$Map1.prototype = new $h_sci_AbstractMap();
$c_sci_Map$Map1.prototype.constructor = $c_sci_Map$Map1;
/** @constructor */
function $h_sci_Map$Map1() {
  /*<skip>*/
}
$h_sci_Map$Map1.prototype = $c_sci_Map$Map1.prototype;
$c_sci_Map$Map1.prototype.size__I = (function() {
  return 1
});
$c_sci_Map$Map1.prototype.knownSize__I = (function() {
  return 1
});
$c_sci_Map$Map1.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Map$Map1.prototype.apply__O__O = (function(key) {
  if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map1__f_key1)) {
    return this.sci_Map$Map1__f_value1
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sci_Map$Map1.prototype.contains__O__Z = (function(key) {
  return $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map1__f_key1)
});
$c_sci_Map$Map1.prototype.get__O__s_Option = (function(key) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map1__f_key1) ? new $c_s_Some(this.sci_Map$Map1__f_value1) : $m_s_None$())
});
$c_sci_Map$Map1.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map1__f_key1) ? this.sci_Map$Map1__f_value1 : default$1.apply__O())
});
$c_sci_Map$Map1.prototype.iterator__sc_Iterator = (function() {
  $m_sc_Iterator$();
  var a = new $c_T2(this.sci_Map$Map1__f_key1, this.sci_Map$Map1__f_value1);
  return new $c_sc_Iterator$$anon$20(a)
});
$c_sci_Map$Map1.prototype.updated__O__O__sci_Map = (function(key, value) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map1__f_key1) ? new $c_sci_Map$Map1(this.sci_Map$Map1__f_key1, value) : new $c_sci_Map$Map2(this.sci_Map$Map1__f_key1, this.sci_Map$Map1__f_value1, key, value))
});
$c_sci_Map$Map1.prototype.forall__F1__Z = (function(p) {
  return $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map1__f_key1, this.sci_Map$Map1__f_value1)))
});
$c_sci_Map$Map1.prototype.hashCode__I = (function() {
  var a = 0;
  var b = 0;
  var c = 1;
  var h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map1__f_key1, this.sci_Map$Map1__f_value1);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed;
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, a);
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, b);
  h = $m_s_util_hashing_MurmurHash3$().mixLast__I__I__I(h, c);
  return $m_s_util_hashing_MurmurHash3$().finalizeHash__I__I__I(h, 1)
});
$c_sci_Map$Map1.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return this.updated__O__O__sci_Map(key, value)
});
var $d_sci_Map$Map1 = new $TypeData().initClass({
  sci_Map$Map1: 0
}, false, "scala.collection.immutable.Map$Map1", {
  sci_Map$Map1: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$Map1.prototype.$classData = $d_sci_Map$Map1;
/** @constructor */
function $c_sci_Map$Map2(key1, value1, key2, value2) {
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1 = null;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1 = null;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2 = null;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2 = null;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1 = key1;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1 = value1;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2 = key2;
  this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2 = value2
}
$c_sci_Map$Map2.prototype = new $h_sci_AbstractMap();
$c_sci_Map$Map2.prototype.constructor = $c_sci_Map$Map2;
/** @constructor */
function $h_sci_Map$Map2() {
  /*<skip>*/
}
$h_sci_Map$Map2.prototype = $c_sci_Map$Map2.prototype;
$c_sci_Map$Map2.prototype.size__I = (function() {
  return 2
});
$c_sci_Map$Map2.prototype.knownSize__I = (function() {
  return 2
});
$c_sci_Map$Map2.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Map$Map2.prototype.apply__O__O = (function(key) {
  if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1)) {
    return this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2)) {
    return this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sci_Map$Map2.prototype.contains__O__Z = (function(key) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2))
});
$c_sci_Map$Map2.prototype.get__O__s_Option = (function(key) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1) ? new $c_s_Some(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2) ? new $c_s_Some(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2) : $m_s_None$()))
});
$c_sci_Map$Map2.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1) ? this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2) ? this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2 : default$1.apply__O()))
});
$c_sci_Map$Map2.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Map$Map2$$anon$1(this)
});
$c_sci_Map$Map2.prototype.updated__O__O__sci_Map = (function(key, value) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1) ? new $c_sci_Map$Map2(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1, value, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2) ? new $c_sci_Map$Map2(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2, value) : new $c_sci_Map$Map3(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2, key, value)))
});
$c_sci_Map$Map2.prototype.forall__F1__Z = (function(p) {
  return ($uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2))))
});
$c_sci_Map$Map2.prototype.hashCode__I = (function() {
  var a = 0;
  var b = 0;
  var c = 1;
  var h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key1, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value1);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$key2, this.sci_Map$Map2__f_scala$collection$immutable$Map$Map2$$value2);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed;
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, a);
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, b);
  h = $m_s_util_hashing_MurmurHash3$().mixLast__I__I__I(h, c);
  return $m_s_util_hashing_MurmurHash3$().finalizeHash__I__I__I(h, 2)
});
$c_sci_Map$Map2.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return this.updated__O__O__sci_Map(key, value)
});
var $d_sci_Map$Map2 = new $TypeData().initClass({
  sci_Map$Map2: 0
}, false, "scala.collection.immutable.Map$Map2", {
  sci_Map$Map2: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$Map2.prototype.$classData = $d_sci_Map$Map2;
/** @constructor */
function $c_sci_Map$Map3(key1, value1, key2, value2, key3, value3) {
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3 = null;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1 = key1;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1 = value1;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2 = key2;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2 = value2;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3 = key3;
  this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3 = value3
}
$c_sci_Map$Map3.prototype = new $h_sci_AbstractMap();
$c_sci_Map$Map3.prototype.constructor = $c_sci_Map$Map3;
/** @constructor */
function $h_sci_Map$Map3() {
  /*<skip>*/
}
$h_sci_Map$Map3.prototype = $c_sci_Map$Map3.prototype;
$c_sci_Map$Map3.prototype.size__I = (function() {
  return 3
});
$c_sci_Map$Map3.prototype.knownSize__I = (function() {
  return 3
});
$c_sci_Map$Map3.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Map$Map3.prototype.apply__O__O = (function(key) {
  if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1)) {
    return this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2)) {
    return this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3)) {
    return this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sci_Map$Map3.prototype.contains__O__Z = (function(key) {
  return (($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2)) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3))
});
$c_sci_Map$Map3.prototype.get__O__s_Option = (function(key) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1) ? new $c_s_Some(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2) ? new $c_s_Some(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3) ? new $c_s_Some(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3) : $m_s_None$())))
});
$c_sci_Map$Map3.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1) ? this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2) ? this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3) ? this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3 : default$1.apply__O())))
});
$c_sci_Map$Map3.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Map$Map3$$anon$4(this)
});
$c_sci_Map$Map3.prototype.updated__O__O__sci_Map = (function(key, value) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1) ? new $c_sci_Map$Map3(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, value, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2) ? new $c_sci_Map$Map3(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, value, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3) ? new $c_sci_Map$Map3(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, value) : new $c_sci_Map$Map4(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3, key, value))))
});
$c_sci_Map$Map3.prototype.forall__F1__Z = (function(p) {
  return (($uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2)))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3))))
});
$c_sci_Map$Map3.prototype.hashCode__I = (function() {
  var a = 0;
  var b = 0;
  var c = 1;
  var h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key1, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value1);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key2, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value2);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$key3, this.sci_Map$Map3__f_scala$collection$immutable$Map$Map3$$value3);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed;
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, a);
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, b);
  h = $m_s_util_hashing_MurmurHash3$().mixLast__I__I__I(h, c);
  return $m_s_util_hashing_MurmurHash3$().finalizeHash__I__I__I(h, 3)
});
$c_sci_Map$Map3.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return this.updated__O__O__sci_Map(key, value)
});
var $d_sci_Map$Map3 = new $TypeData().initClass({
  sci_Map$Map3: 0
}, false, "scala.collection.immutable.Map$Map3", {
  sci_Map$Map3: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$Map3.prototype.$classData = $d_sci_Map$Map3;
/** @constructor */
function $c_sci_Map$Map4(key1, value1, key2, value2, key3, value3, key4, value4) {
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4 = null;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1 = key1;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1 = value1;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2 = key2;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2 = value2;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3 = key3;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3 = value3;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4 = key4;
  this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4 = value4
}
$c_sci_Map$Map4.prototype = new $h_sci_AbstractMap();
$c_sci_Map$Map4.prototype.constructor = $c_sci_Map$Map4;
/** @constructor */
function $h_sci_Map$Map4() {
  /*<skip>*/
}
$h_sci_Map$Map4.prototype = $c_sci_Map$Map4.prototype;
$c_sci_Map$Map4.prototype.size__I = (function() {
  return 4
});
$c_sci_Map$Map4.prototype.knownSize__I = (function() {
  return 4
});
$c_sci_Map$Map4.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Map$Map4.prototype.apply__O__O = (function(key) {
  if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1)) {
    return this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2)) {
    return this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3)) {
    return this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4)) {
    return this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sci_Map$Map4.prototype.contains__O__Z = (function(key) {
  return ((($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2)) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3)) || $m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4))
});
$c_sci_Map$Map4.prototype.get__O__s_Option = (function(key) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1) ? new $c_s_Some(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2) ? new $c_s_Some(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3) ? new $c_s_Some(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3) : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4) ? new $c_s_Some(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4) : $m_s_None$()))))
});
$c_sci_Map$Map4.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  return ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1) ? this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2) ? this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3) ? this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3 : ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4) ? this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4 : default$1.apply__O()))))
});
$c_sci_Map$Map4.prototype.iterator__sc_Iterator = (function() {
  return new $c_sci_Map$Map4$$anon$7(this)
});
$c_sci_Map$Map4.prototype.updated__O__O__sci_Map = (function(key, value) {
  if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1)) {
    return new $c_sci_Map$Map4(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, value, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4)
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2)) {
    return new $c_sci_Map$Map4(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, value, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4)
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3)) {
    return new $c_sci_Map$Map4(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, value, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4)
  } else if ($m_sr_BoxesRunTime$().equals__O__O__Z(key, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4)) {
    return new $c_sci_Map$Map4(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, value)
  } else {
    var this$1 = $m_sci_HashMap$();
    return this$1.sci_HashMap$__f_EmptyMap.updated__O__O__sci_HashMap(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1).updated__O__O__sci_HashMap(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2).updated__O__O__sci_HashMap(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3).updated__O__O__sci_HashMap(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4).updated__O__O__sci_HashMap(key, value)
  }
});
$c_sci_Map$Map4.prototype.forall__F1__Z = (function(p) {
  return ((($uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2)))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3)))) && $uZ(p.apply__O__O(new $c_T2(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4))))
});
$c_sci_Map$Map4.prototype.buildTo__sci_HashMapBuilder__sci_HashMapBuilder = (function(builder) {
  return builder.addOne__O__O__sci_HashMapBuilder(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1).addOne__O__O__sci_HashMapBuilder(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2).addOne__O__O__sci_HashMapBuilder(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3).addOne__O__O__sci_HashMapBuilder(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4)
});
$c_sci_Map$Map4.prototype.hashCode__I = (function() {
  var a = 0;
  var b = 0;
  var c = 1;
  var h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key1, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value1);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key2, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value2);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key3, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value3);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().tuple2Hash__O__O__I(this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$key4, this.sci_Map$Map4__f_scala$collection$immutable$Map$Map4$$value4);
  a = ((a + h) | 0);
  b = (b ^ h);
  c = $imul(c, (1 | h));
  h = $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed;
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, a);
  h = $m_s_util_hashing_MurmurHash3$().mix__I__I__I(h, b);
  h = $m_s_util_hashing_MurmurHash3$().mixLast__I__I__I(h, c);
  return $m_s_util_hashing_MurmurHash3$().finalizeHash__I__I__I(h, 4)
});
$c_sci_Map$Map4.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return this.updated__O__O__sci_Map(key, value)
});
function $as_sci_Map$Map4(obj) {
  return (((obj instanceof $c_sci_Map$Map4) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Map$Map4"))
}
function $isArrayOf_sci_Map$Map4(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Map$Map4)))
}
function $asArrayOf_sci_Map$Map4(obj, depth) {
  return (($isArrayOf_sci_Map$Map4(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Map$Map4;", depth))
}
var $d_sci_Map$Map4 = new $TypeData().initClass({
  sci_Map$Map4: 0
}, false, "scala.collection.immutable.Map$Map4", {
  sci_Map$Map4: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Map$Map4.prototype.$classData = $d_sci_Map$Map4;
function $is_scm_Map(obj) {
  return (!(!((obj && obj.$classData) && obj.$classData.ancestors.scm_Map)))
}
function $as_scm_Map(obj) {
  return (($is_scm_Map(obj) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.Map"))
}
function $isArrayOf_scm_Map(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_Map)))
}
function $asArrayOf_scm_Map(obj, depth) {
  return (($isArrayOf_scm_Map(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.Map;", depth))
}
function $p_sci_HashSet__newHashSetOrThis__sci_BitmapIndexedSetNode__sci_HashSet($thiz, newRootNode) {
  return (($thiz.sci_HashSet__f_rootNode === newRootNode) ? $thiz : new $c_sci_HashSet(newRootNode))
}
/** @constructor */
function $c_sci_HashSet(rootNode) {
  this.sci_HashSet__f_rootNode = null;
  this.sci_HashSet__f_rootNode = rootNode
}
$c_sci_HashSet.prototype = new $h_sci_AbstractSet();
$c_sci_HashSet.prototype.constructor = $c_sci_HashSet;
/** @constructor */
function $h_sci_HashSet() {
  /*<skip>*/
}
$h_sci_HashSet.prototype = $c_sci_HashSet.prototype;
$c_sci_HashSet.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_HashSet$()
});
$c_sci_HashSet.prototype.knownSize__I = (function() {
  return this.sci_HashSet__f_rootNode.sci_BitmapIndexedSetNode__f_size
});
$c_sci_HashSet.prototype.size__I = (function() {
  return this.sci_HashSet__f_rootNode.sci_BitmapIndexedSetNode__f_size
});
$c_sci_HashSet.prototype.isEmpty__Z = (function() {
  return (this.sci_HashSet__f_rootNode.sci_BitmapIndexedSetNode__f_size === 0)
});
$c_sci_HashSet.prototype.iterator__sc_Iterator = (function() {
  return (this.isEmpty__Z() ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_sci_SetIterator(this.sci_HashSet__f_rootNode))
});
$c_sci_HashSet.prototype.contains__O__Z = (function(element) {
  var elementUnimprovedHash = $m_sr_Statics$().anyHash__O__I(element);
  var elementHash = $m_sc_Hashing$().improve__I__I(elementUnimprovedHash);
  return this.sci_HashSet__f_rootNode.contains__O__I__I__I__Z(element, elementUnimprovedHash, elementHash, 0)
});
$c_sci_HashSet.prototype.incl__O__sci_HashSet = (function(element) {
  var elementUnimprovedHash = $m_sr_Statics$().anyHash__O__I(element);
  var elementHash = $m_sc_Hashing$().improve__I__I(elementUnimprovedHash);
  var newRootNode = this.sci_HashSet__f_rootNode.updated__O__I__I__I__sci_BitmapIndexedSetNode(element, elementUnimprovedHash, elementHash, 0);
  return $p_sci_HashSet__newHashSetOrThis__sci_BitmapIndexedSetNode__sci_HashSet(this, newRootNode)
});
$c_sci_HashSet.prototype.excl__O__sci_HashSet = (function(element) {
  var elementUnimprovedHash = $m_sr_Statics$().anyHash__O__I(element);
  var elementHash = $m_sc_Hashing$().improve__I__I(elementUnimprovedHash);
  var newRootNode = this.sci_HashSet__f_rootNode.removed__O__I__I__I__sci_BitmapIndexedSetNode(element, elementUnimprovedHash, elementHash, 0);
  return $p_sci_HashSet__newHashSetOrThis__sci_BitmapIndexedSetNode__sci_HashSet(this, newRootNode)
});
$c_sci_HashSet.prototype.head__O = (function() {
  return this.iterator__sc_Iterator().next__O()
});
$c_sci_HashSet.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_HashSet)) {
    var x2 = $as_sci_HashSet(that);
    if ((this === x2)) {
      return true
    } else {
      var x = this.sci_HashSet__f_rootNode;
      var x$2 = x2.sci_HashSet__f_rootNode;
      return ((x === null) ? (x$2 === null) : x.equals__O__Z(x$2))
    }
  } else {
    return $f_sc_Set__equals__O__Z(this, that)
  }
});
$c_sci_HashSet.prototype.className__T = (function() {
  return "HashSet"
});
$c_sci_HashSet.prototype.hashCode__I = (function() {
  var it = new $c_sci_SetHashIterator(this.sci_HashSet__f_rootNode);
  var hash = $m_s_util_hashing_MurmurHash3$().unorderedHash__sc_IterableOnce__I__I(it, $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_setSeed);
  return hash
});
$c_sci_HashSet.prototype.drop__I__O = (function(n) {
  return $as_sci_HashSet($f_sc_IterableOps__drop__I__O(this, n))
});
$c_sci_HashSet.prototype.tail__O = (function() {
  var elem = this.iterator__sc_Iterator().next__O();
  return this.excl__O__sci_HashSet(elem)
});
$c_sci_HashSet.prototype.incl__O__sci_SetOps = (function(elem) {
  return this.incl__O__sci_HashSet(elem)
});
function $as_sci_HashSet(obj) {
  return (((obj instanceof $c_sci_HashSet) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.HashSet"))
}
function $isArrayOf_sci_HashSet(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_HashSet)))
}
function $asArrayOf_sci_HashSet(obj, depth) {
  return (($isArrayOf_sci_HashSet(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.HashSet;", depth))
}
var $d_sci_HashSet = new $TypeData().initClass({
  sci_HashSet: 0
}, false, "scala.collection.immutable.HashSet", {
  sci_HashSet: 1,
  sci_AbstractSet: 1,
  sc_AbstractSet: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Set: 1,
  sc_SetOps: 1,
  F1: 1,
  s_Equals: 1,
  sci_Set: 1,
  sci_Iterable: 1,
  sci_SetOps: 1,
  sci_StrictOptimizedSetOps: 1,
  sc_StrictOptimizedSetOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_HashSet.prototype.$classData = $d_sci_HashSet;
function $p_sci_LazyList__scala$collection$immutable$LazyList$$state$lzycompute__sci_LazyList$State($thiz) {
  if ((!$thiz.sci_LazyList__f_bitmap$0)) {
    if ($thiz.sci_LazyList__f_midEvaluation) {
      throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O($ct_jl_RuntimeException__T__(new $c_jl_RuntimeException(), "self-referential LazyList or a derivation thereof has no more elements"))
    };
    $thiz.sci_LazyList__f_midEvaluation = true;
    try {
      var res = $as_sci_LazyList$State($thiz.sci_LazyList__f_lazyState.apply__O())
    } finally {
      $thiz.sci_LazyList__f_midEvaluation = false
    };
    $thiz.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated = true;
    $thiz.sci_LazyList__f_lazyState = null;
    $thiz.sci_LazyList__f_scala$collection$immutable$LazyList$$state = res;
    $thiz.sci_LazyList__f_bitmap$0 = true
  };
  return $thiz.sci_LazyList__f_scala$collection$immutable$LazyList$$state
}
function $p_sci_LazyList__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder($thiz, b, start, sep, end) {
  b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + start);
  if ((!$thiz.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated)) {
    b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (b.jl_StringBuilder__f_java$lang$StringBuilder$$content + "<not computed>")
  } else if ((!$thiz.isEmpty__Z())) {
    var obj = $thiz.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
    b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj);
    var elem = null;
    elem = $thiz;
    var elem$1 = $thiz.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
    var elem$2 = null;
    elem$2 = elem$1;
    if ((($as_sci_LazyList(elem) !== $as_sci_LazyList(elem$2)) && ((!$as_sci_LazyList(elem$2).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated) || ($as_sci_LazyList(elem).scala$collection$immutable$LazyList$$state__sci_LazyList$State() !== $as_sci_LazyList(elem$2).scala$collection$immutable$LazyList$$state__sci_LazyList$State())))) {
      elem = $as_sci_LazyList(elem$2);
      if (($as_sci_LazyList(elem$2).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && (!$as_sci_LazyList(elem$2).isEmpty__Z()))) {
        var this$3 = $as_sci_LazyList(elem$2);
        elem$2 = this$3.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
        while (((($as_sci_LazyList(elem) !== $as_sci_LazyList(elem$2)) && ($as_sci_LazyList(elem$2).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && (!$as_sci_LazyList(elem$2).isEmpty__Z()))) && ($as_sci_LazyList(elem).scala$collection$immutable$LazyList$$state__sci_LazyList$State() !== $as_sci_LazyList(elem$2).scala$collection$immutable$LazyList$$state__sci_LazyList$State()))) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var this$4 = $as_sci_LazyList(elem);
          var obj$1 = this$4.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$1);
          var this$5 = $as_sci_LazyList(elem);
          elem = this$5.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
          var this$6 = $as_sci_LazyList(elem$2);
          elem$2 = this$6.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
          if (($as_sci_LazyList(elem$2).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && (!$as_sci_LazyList(elem$2).isEmpty__Z()))) {
            var this$7 = $as_sci_LazyList(elem$2);
            elem$2 = this$7.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
          }
        }
      }
    };
    if ((!($as_sci_LazyList(elem$2).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && (!$as_sci_LazyList(elem$2).isEmpty__Z())))) {
      while (($as_sci_LazyList(elem) !== $as_sci_LazyList(elem$2))) {
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
        var this$8 = $as_sci_LazyList(elem);
        var obj$2 = this$8.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$2);
        var this$9 = $as_sci_LazyList(elem);
        elem = this$9.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
      };
      if ((!$as_sci_LazyList(elem).sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated)) {
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (b.jl_StringBuilder__f_java$lang$StringBuilder$$content + "<not computed>")
      }
    } else {
      var runner = $thiz;
      var k = 0;
      while (true) {
        var a = runner;
        var b$1 = $as_sci_LazyList(elem$2);
        if ((!((a === b$1) || (a.scala$collection$immutable$LazyList$$state__sci_LazyList$State() === b$1.scala$collection$immutable$LazyList$$state__sci_LazyList$State())))) {
          var this$10 = runner;
          runner = this$10.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
          var this$11 = $as_sci_LazyList(elem$2);
          elem$2 = this$11.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
          k = ((1 + k) | 0)
        } else {
          break
        }
      };
      var a$1 = $as_sci_LazyList(elem);
      var b$2 = $as_sci_LazyList(elem$2);
      if ((((a$1 === b$2) || (a$1.scala$collection$immutable$LazyList$$state__sci_LazyList$State() === b$2.scala$collection$immutable$LazyList$$state__sci_LazyList$State())) && (k > 0))) {
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
        var this$12 = $as_sci_LazyList(elem);
        var obj$3 = this$12.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$3);
        var this$13 = $as_sci_LazyList(elem);
        elem = this$13.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
      };
      while (true) {
        var a$2 = $as_sci_LazyList(elem);
        var b$3 = $as_sci_LazyList(elem$2);
        if ((!((a$2 === b$3) || (a$2.scala$collection$immutable$LazyList$$state__sci_LazyList$State() === b$3.scala$collection$immutable$LazyList$$state__sci_LazyList$State())))) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var this$14 = $as_sci_LazyList(elem);
          var obj$4 = this$14.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$4);
          var this$15 = $as_sci_LazyList(elem);
          elem = this$15.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
        } else {
          break
        }
      };
      b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
      b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (b.jl_StringBuilder__f_java$lang$StringBuilder$$content + "<cycle>")
    }
  };
  b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + end);
  return b
}
/** @constructor */
function $c_sci_LazyList(lazyState) {
  this.sci_LazyList__f_scala$collection$immutable$LazyList$$state = null;
  this.sci_LazyList__f_lazyState = null;
  this.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated = false;
  this.sci_LazyList__f_midEvaluation = false;
  this.sci_LazyList__f_bitmap$0 = false;
  this.sci_LazyList__f_lazyState = lazyState;
  this.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated = false;
  this.sci_LazyList__f_midEvaluation = false
}
$c_sci_LazyList.prototype = new $h_sci_AbstractSeq();
$c_sci_LazyList.prototype.constructor = $c_sci_LazyList;
/** @constructor */
function $h_sci_LazyList() {
  /*<skip>*/
}
$h_sci_LazyList.prototype = $c_sci_LazyList.prototype;
$c_sci_LazyList.prototype.stringPrefix__T = (function() {
  return "LinearSeq"
});
$c_sci_LazyList.prototype.length__I = (function() {
  return $f_sc_LinearSeqOps__length__I(this)
});
$c_sci_LazyList.prototype.lengthCompare__I__I = (function(len) {
  return $f_sc_LinearSeqOps__lengthCompare__I__I(this, len)
});
$c_sci_LazyList.prototype.apply__I__O = (function(n) {
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_LazyList.prototype.exists__F1__Z = (function(p) {
  return $f_sc_LinearSeqOps__exists__F1__Z(this, p)
});
$c_sci_LazyList.prototype.sameElements__sc_IterableOnce__Z = (function(that) {
  return $f_sc_LinearSeqOps__sameElements__sc_IterableOnce__Z(this, that)
});
$c_sci_LazyList.prototype.indexWhere__F1__I__I = (function(p, from) {
  return $f_sc_LinearSeqOps__indexWhere__F1__I__I(this, p, from)
});
$c_sci_LazyList.prototype.scala$collection$immutable$LazyList$$state__sci_LazyList$State = (function() {
  return ((!this.sci_LazyList__f_bitmap$0) ? $p_sci_LazyList__scala$collection$immutable$LazyList$$state$lzycompute__sci_LazyList$State(this) : this.sci_LazyList__f_scala$collection$immutable$LazyList$$state)
});
$c_sci_LazyList.prototype.isEmpty__Z = (function() {
  return (this.scala$collection$immutable$LazyList$$state__sci_LazyList$State() === $m_sci_LazyList$State$Empty$())
});
$c_sci_LazyList.prototype.knownSize__I = (function() {
  return ((this.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && this.isEmpty__Z()) ? 0 : (-1))
});
$c_sci_LazyList.prototype.head__O = (function() {
  return this.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O()
});
$c_sci_LazyList.prototype.force__sci_LazyList = (function() {
  var these = this;
  var those = this;
  if ((!these.isEmpty__Z())) {
    var this$1 = these;
    these = this$1.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
  };
  while ((those !== these)) {
    if (these.isEmpty__Z()) {
      return this
    };
    var this$2 = these;
    these = this$2.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
    if (these.isEmpty__Z()) {
      return this
    };
    var this$3 = these;
    these = this$3.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList();
    if ((these === those)) {
      return this
    };
    var this$4 = those;
    those = this$4.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
  };
  return this
});
$c_sci_LazyList.prototype.iterator__sc_Iterator = (function() {
  return ((this.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && this.isEmpty__Z()) ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_sci_LazyList$LazyIterator(this))
});
$c_sci_LazyList.prototype.foreach__F1__V = (function(f) {
  var _$this = this;
  while ((!_$this.isEmpty__Z())) {
    var this$1 = _$this;
    f.apply__O__O(this$1.scala$collection$immutable$LazyList$$state__sci_LazyList$State().head__O());
    var this$2 = _$this;
    _$this = this$2.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
  }
});
$c_sci_LazyList.prototype.className__T = (function() {
  return "LazyList"
});
$c_sci_LazyList.prototype.drop__I__sci_LazyList = (function(n) {
  return ((n <= 0) ? this : ((this.sci_LazyList__f_scala$collection$immutable$LazyList$$stateEvaluated && this.isEmpty__Z()) ? $m_sci_LazyList$().sci_LazyList$__f__empty : $m_sci_LazyList$().scala$collection$immutable$LazyList$$dropImpl__sci_LazyList__I__sci_LazyList(this, n)))
});
$c_sci_LazyList.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(sb, start, sep, end) {
  this.force__sci_LazyList();
  $p_sci_LazyList__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder(this, sb.scm_StringBuilder__f_underlying, start, sep, end);
  return sb
});
$c_sci_LazyList.prototype.toString__T = (function() {
  return $p_sci_LazyList__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder(this, $ct_jl_StringBuilder__T__(new $c_jl_StringBuilder(), "LazyList"), "(", ", ", ")").jl_StringBuilder__f_java$lang$StringBuilder$$content
});
$c_sci_LazyList.prototype.apply__O__O = (function(v1) {
  var n = $uI(v1);
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_LazyList.prototype.drop__I__O = (function(n) {
  return this.drop__I__sci_LazyList(n)
});
$c_sci_LazyList.prototype.tail__O = (function() {
  return this.scala$collection$immutable$LazyList$$state__sci_LazyList$State().tail__sci_LazyList()
});
$c_sci_LazyList.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_LazyList$()
});
function $as_sci_LazyList(obj) {
  return (((obj instanceof $c_sci_LazyList) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.LazyList"))
}
function $isArrayOf_sci_LazyList(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_LazyList)))
}
function $asArrayOf_sci_LazyList(obj, depth) {
  return (($isArrayOf_sci_LazyList(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.LazyList;", depth))
}
var $d_sci_LazyList = new $TypeData().initClass({
  sci_LazyList: 0
}, false, "scala.collection.immutable.LazyList", {
  sci_LazyList: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_LinearSeq: 1,
  sc_LinearSeq: 1,
  sc_LinearSeqOps: 1,
  sci_LinearSeqOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_LazyList.prototype.$classData = $d_sci_LazyList;
function $p_sci_Stream__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder($thiz, b, start, sep, end) {
  b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + start);
  if ((!$thiz.isEmpty__Z())) {
    var obj = $thiz.head__O();
    b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj);
    var elem = null;
    elem = $thiz;
    if ($thiz.tailDefined__Z()) {
      var scout = $as_sci_Stream($thiz.tail__O());
      if (($as_sci_Stream(elem) !== scout)) {
        elem = scout;
        if (scout.tailDefined__Z()) {
          scout = $as_sci_Stream(scout.tail__O());
          while ((($as_sci_Stream(elem) !== scout) && scout.tailDefined__Z())) {
            b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
            var obj$1 = $as_sci_Stream(elem).head__O();
            b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$1);
            elem = $as_sci_Stream($as_sci_Stream(elem).tail__O());
            scout = $as_sci_Stream(scout.tail__O());
            if (scout.tailDefined__Z()) {
              scout = $as_sci_Stream(scout.tail__O())
            }
          }
        }
      };
      if ((!scout.tailDefined__Z())) {
        while (($as_sci_Stream(elem) !== scout)) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var obj$2 = $as_sci_Stream(elem).head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$2);
          elem = $as_sci_Stream($as_sci_Stream(elem).tail__O())
        };
        var this$2 = $as_sci_Stream(elem);
        if ((!this$2.isEmpty__Z())) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var obj$3 = $as_sci_Stream(elem).head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$3)
        }
      } else {
        var runner = $thiz;
        var k = 0;
        while ((runner !== scout)) {
          runner = $as_sci_Stream(runner.tail__O());
          scout = $as_sci_Stream(scout.tail__O());
          k = ((1 + k) | 0)
        };
        if ((($as_sci_Stream(elem) === scout) && (k > 0))) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var obj$4 = $as_sci_Stream(elem).head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$4);
          elem = $as_sci_Stream($as_sci_Stream(elem).tail__O())
        };
        while (($as_sci_Stream(elem) !== scout)) {
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
          var obj$5 = $as_sci_Stream(elem).head__O();
          b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + obj$5);
          elem = $as_sci_Stream($as_sci_Stream(elem).tail__O())
        }
      }
    };
    var this$3 = $as_sci_Stream(elem);
    if ((!this$3.isEmpty__Z())) {
      if ((!$as_sci_Stream(elem).tailDefined__Z())) {
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (b.jl_StringBuilder__f_java$lang$StringBuilder$$content + "<not computed>")
      } else {
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + sep);
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (b.jl_StringBuilder__f_java$lang$StringBuilder$$content + "<cycle>")
      }
    }
  };
  b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + end);
  return b
}
/** @constructor */
function $c_sci_Stream() {
  /*<skip>*/
}
$c_sci_Stream.prototype = new $h_sci_AbstractSeq();
$c_sci_Stream.prototype.constructor = $c_sci_Stream;
/** @constructor */
function $h_sci_Stream() {
  /*<skip>*/
}
$h_sci_Stream.prototype = $c_sci_Stream.prototype;
$c_sci_Stream.prototype.stringPrefix__T = (function() {
  return "LinearSeq"
});
$c_sci_Stream.prototype.iterator__sc_Iterator = (function() {
  return $f_sc_LinearSeqOps__iterator__sc_Iterator(this)
});
$c_sci_Stream.prototype.length__I = (function() {
  return $f_sc_LinearSeqOps__length__I(this)
});
$c_sci_Stream.prototype.lengthCompare__I__I = (function(len) {
  return $f_sc_LinearSeqOps__lengthCompare__I__I(this, len)
});
$c_sci_Stream.prototype.apply__I__O = (function(n) {
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_Stream.prototype.exists__F1__Z = (function(p) {
  return $f_sc_LinearSeqOps__exists__F1__Z(this, p)
});
$c_sci_Stream.prototype.sameElements__sc_IterableOnce__Z = (function(that) {
  return $f_sc_LinearSeqOps__sameElements__sc_IterableOnce__Z(this, that)
});
$c_sci_Stream.prototype.indexWhere__F1__I__I = (function(p, from) {
  return $f_sc_LinearSeqOps__indexWhere__F1__I__I(this, p, from)
});
$c_sci_Stream.prototype.className__T = (function() {
  return "Stream"
});
$c_sci_Stream.prototype.foreach__F1__V = (function(f) {
  var _$this = this;
  while ((!_$this.isEmpty__Z())) {
    f.apply__O__O(_$this.head__O());
    _$this = $as_sci_Stream(_$this.tail__O())
  }
});
$c_sci_Stream.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(sb, start, sep, end) {
  this.force__sci_Stream();
  $p_sci_Stream__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder(this, sb.scm_StringBuilder__f_underlying, start, sep, end);
  return sb
});
$c_sci_Stream.prototype.toString__T = (function() {
  return $p_sci_Stream__addStringNoForce__jl_StringBuilder__T__T__T__jl_StringBuilder(this, $ct_jl_StringBuilder__T__(new $c_jl_StringBuilder(), "Stream"), "(", ", ", ")").jl_StringBuilder__f_java$lang$StringBuilder$$content
});
$c_sci_Stream.prototype.apply__O__O = (function(v1) {
  var n = $uI(v1);
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_Stream.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_Stream$()
});
function $as_sci_Stream(obj) {
  return (((obj instanceof $c_sci_Stream) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Stream"))
}
function $isArrayOf_sci_Stream(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Stream)))
}
function $asArrayOf_sci_Stream(obj, depth) {
  return (($isArrayOf_sci_Stream(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Stream;", depth))
}
/** @constructor */
function $c_sci_WrappedString(self) {
  this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self = null;
  this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self = self
}
$c_sci_WrappedString.prototype = new $h_sci_AbstractSeq();
$c_sci_WrappedString.prototype.constructor = $c_sci_WrappedString;
/** @constructor */
function $h_sci_WrappedString() {
  /*<skip>*/
}
$h_sci_WrappedString.prototype = $c_sci_WrappedString.prototype;
$c_sci_WrappedString.prototype.canEqual__O__Z = (function(that) {
  return $f_sci_IndexedSeq__canEqual__O__Z(this, that)
});
$c_sci_WrappedString.prototype.stringPrefix__T = (function() {
  return "IndexedSeq"
});
$c_sci_WrappedString.prototype.iterator__sc_Iterator = (function() {
  var this$1 = new $c_sc_StringView(this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self);
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1)
});
$c_sci_WrappedString.prototype.drop__I__O = (function(n) {
  return $f_sc_IndexedSeqOps__drop__I__O(this, n)
});
$c_sci_WrappedString.prototype.head__O = (function() {
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  return $bC((65535 & $uI(this$1.charCodeAt(0))))
});
$c_sci_WrappedString.prototype.lengthCompare__I__I = (function(len) {
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  var x = $uI(this$1.length);
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sci_WrappedString.prototype.knownSize__I = (function() {
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  return $uI(this$1.length)
});
$c_sci_WrappedString.prototype.length__I = (function() {
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  return $uI(this$1.length)
});
$c_sci_WrappedString.prototype.toString__T = (function() {
  return this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self
});
$c_sci_WrappedString.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  if ((xs instanceof $ac_C)) {
    var x2 = $asArrayOf_C(xs, 1);
    var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
    var srcLen = $uI(this$1.length);
    var destLen = x2.u.length;
    var x = ((len < srcLen) ? len : srcLen);
    var y = ((destLen - start) | 0);
    var x$1 = ((x < y) ? x : y);
    var copied = ((x$1 > 0) ? x$1 : 0);
    $f_T__getChars__I__I__AC__I__V(this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self, 0, copied, x2, start);
    return copied
  } else {
    return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
  }
});
$c_sci_WrappedString.prototype.sameElements__sc_IterableOnce__Z = (function(o) {
  if ((o instanceof $c_sci_WrappedString)) {
    var x2 = $as_sci_WrappedString(o);
    return (this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self === x2.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self)
  } else {
    return $f_sci_IndexedSeq__sameElements__sc_IterableOnce__Z(this, o)
  }
});
$c_sci_WrappedString.prototype.className__T = (function() {
  return "WrappedString"
});
$c_sci_WrappedString.prototype.applyPreferredMaxLength__I = (function() {
  return 2147483647
});
$c_sci_WrappedString.prototype.equals__O__Z = (function(other) {
  if ((other instanceof $c_sci_WrappedString)) {
    var x2 = $as_sci_WrappedString(other);
    return (this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self === x2.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self)
  } else {
    return $f_sc_Seq__equals__O__Z(this, other)
  }
});
$c_sci_WrappedString.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_IndexedSeq$()
});
$c_sci_WrappedString.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  return $m_sci_WrappedString$().fromSpecific__sc_IterableOnce__sci_WrappedString(coll)
});
$c_sci_WrappedString.prototype.fromSpecific__sc_IterableOnce__sc_IterableOps = (function(coll) {
  return $m_sci_WrappedString$().fromSpecific__sc_IterableOnce__sci_WrappedString(coll)
});
$c_sci_WrappedString.prototype.apply__O__O = (function(v1) {
  var i = $uI(v1);
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  return $bC((65535 & $uI(this$1.charCodeAt(i))))
});
$c_sci_WrappedString.prototype.apply__I__O = (function(i) {
  var this$1 = this.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
  return $bC((65535 & $uI(this$1.charCodeAt(i))))
});
function $as_sci_WrappedString(obj) {
  return (((obj instanceof $c_sci_WrappedString) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.WrappedString"))
}
function $isArrayOf_sci_WrappedString(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_WrappedString)))
}
function $asArrayOf_sci_WrappedString(obj, depth) {
  return (($isArrayOf_sci_WrappedString(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.WrappedString;", depth))
}
var $d_sci_WrappedString = new $TypeData().initClass({
  sci_WrappedString: 0
}, false, "scala.collection.immutable.WrappedString", {
  sci_WrappedString: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_WrappedString.prototype.$classData = $d_sci_WrappedString;
/** @constructor */
function $c_sjsr_WrappedVarArgs(array) {
  this.sjsr_WrappedVarArgs__f_scala$scalajs$runtime$WrappedVarArgs$$array = null;
  this.sjsr_WrappedVarArgs__f_scala$scalajs$runtime$WrappedVarArgs$$array = array
}
$c_sjsr_WrappedVarArgs.prototype = new $h_O();
$c_sjsr_WrappedVarArgs.prototype.constructor = $c_sjsr_WrappedVarArgs;
/** @constructor */
function $h_sjsr_WrappedVarArgs() {
  /*<skip>*/
}
$h_sjsr_WrappedVarArgs.prototype = $c_sjsr_WrappedVarArgs.prototype;
$c_sjsr_WrappedVarArgs.prototype.canEqual__O__Z = (function(that) {
  return $f_sci_IndexedSeq__canEqual__O__Z(this, that)
});
$c_sjsr_WrappedVarArgs.prototype.sameElements__sc_IterableOnce__Z = (function(o) {
  return $f_sci_IndexedSeq__sameElements__sc_IterableOnce__Z(this, o)
});
$c_sjsr_WrappedVarArgs.prototype.applyPreferredMaxLength__I = (function() {
  return $m_sci_IndexedSeqDefaults$().sci_IndexedSeqDefaults$__f_defaultApplyPreferredMaxLength
});
$c_sjsr_WrappedVarArgs.prototype.iterator__sc_Iterator = (function() {
  var this$1 = new $c_sc_IndexedSeqView$Id(this);
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1)
});
$c_sjsr_WrappedVarArgs.prototype.drop__I__O = (function(n) {
  return $f_sc_IndexedSeqOps__drop__I__O(this, n)
});
$c_sjsr_WrappedVarArgs.prototype.head__O = (function() {
  return this.apply__I__O(0)
});
$c_sjsr_WrappedVarArgs.prototype.lengthCompare__I__I = (function(len) {
  var x = this.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sjsr_WrappedVarArgs.prototype.knownSize__I = (function() {
  return this.length__I()
});
$c_sjsr_WrappedVarArgs.prototype.equals__O__Z = (function(o) {
  return $f_sc_Seq__equals__O__Z(this, o)
});
$c_sjsr_WrappedVarArgs.prototype.hashCode__I = (function() {
  return $m_s_util_hashing_MurmurHash3$().seqHash__sc_Seq__I(this)
});
$c_sjsr_WrappedVarArgs.prototype.toString__T = (function() {
  return $f_sc_Iterable__toString__T(this)
});
$c_sjsr_WrappedVarArgs.prototype.indexWhere__F1__I__I = (function(p, from) {
  var this$1 = new $c_sc_IndexedSeqView$Id(this);
  var this$2 = new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1);
  return $f_sc_Iterator__indexWhere__F1__I__I(this$2, p, from)
});
$c_sjsr_WrappedVarArgs.prototype.isEmpty__Z = (function() {
  return $f_sc_SeqOps__isEmpty__Z(this)
});
$c_sjsr_WrappedVarArgs.prototype.tail__O = (function() {
  return $f_sc_IterableOps__tail__O(this)
});
$c_sjsr_WrappedVarArgs.prototype.foreach__F1__V = (function(f) {
  $f_sc_IterableOnceOps__foreach__F1__V(this, f)
});
$c_sjsr_WrappedVarArgs.prototype.exists__F1__Z = (function(p) {
  return $f_sc_IterableOnceOps__exists__F1__Z(this, p)
});
$c_sjsr_WrappedVarArgs.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return $f_sc_IterableOnceOps__copyToArray__O__I__I__I(this, xs, start, len)
});
$c_sjsr_WrappedVarArgs.prototype.addString__scm_StringBuilder__T__T__T__scm_StringBuilder = (function(b, start, sep, end) {
  return $f_sc_IterableOnceOps__addString__scm_StringBuilder__T__T__T__scm_StringBuilder(this, b, start, sep, end)
});
$c_sjsr_WrappedVarArgs.prototype.length__I = (function() {
  return $uI(this.sjsr_WrappedVarArgs__f_scala$scalajs$runtime$WrappedVarArgs$$array.length)
});
$c_sjsr_WrappedVarArgs.prototype.apply__I__O = (function(idx) {
  return this.sjsr_WrappedVarArgs__f_scala$scalajs$runtime$WrappedVarArgs$$array[idx]
});
$c_sjsr_WrappedVarArgs.prototype.className__T = (function() {
  return "WrappedVarArgs"
});
$c_sjsr_WrappedVarArgs.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  var this$1 = $m_sjsr_WrappedVarArgs$();
  return this$1.from__sc_IterableOnce__sjsr_WrappedVarArgs(coll)
});
$c_sjsr_WrappedVarArgs.prototype.apply__O__O = (function(v1) {
  return this.apply__I__O($uI(v1))
});
function $as_sjsr_WrappedVarArgs(obj) {
  return (((obj instanceof $c_sjsr_WrappedVarArgs) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.scalajs.runtime.WrappedVarArgs"))
}
function $isArrayOf_sjsr_WrappedVarArgs(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sjsr_WrappedVarArgs)))
}
function $asArrayOf_sjsr_WrappedVarArgs(obj, depth) {
  return (($isArrayOf_sjsr_WrappedVarArgs(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.scalajs.runtime.WrappedVarArgs;", depth))
}
var $d_sjsr_WrappedVarArgs = new $TypeData().initClass({
  sjsr_WrappedVarArgs: 0
}, false, "scala.scalajs.runtime.WrappedVarArgs", {
  sjsr_WrappedVarArgs: 1,
  O: 1,
  sci_IndexedSeq: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_SeqOps: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  Ljava_io_Serializable: 1
});
$c_sjsr_WrappedVarArgs.prototype.$classData = $d_sjsr_WrappedVarArgs;
/** @constructor */
function $c_sci_HashMap(rootNode) {
  this.sci_HashMap__f_rootNode = null;
  this.sci_HashMap__f_rootNode = rootNode
}
$c_sci_HashMap.prototype = new $h_sci_AbstractMap();
$c_sci_HashMap.prototype.constructor = $c_sci_HashMap;
/** @constructor */
function $h_sci_HashMap() {
  /*<skip>*/
}
$h_sci_HashMap.prototype = $c_sci_HashMap.prototype;
$c_sci_HashMap.prototype.mapFactory__sc_MapFactory = (function() {
  return $m_sci_HashMap$()
});
$c_sci_HashMap.prototype.knownSize__I = (function() {
  return this.sci_HashMap__f_rootNode.sci_BitmapIndexedMapNode__f_size
});
$c_sci_HashMap.prototype.size__I = (function() {
  return this.sci_HashMap__f_rootNode.sci_BitmapIndexedMapNode__f_size
});
$c_sci_HashMap.prototype.isEmpty__Z = (function() {
  return (this.sci_HashMap__f_rootNode.sci_BitmapIndexedMapNode__f_size === 0)
});
$c_sci_HashMap.prototype.iterator__sc_Iterator = (function() {
  return (this.isEmpty__Z() ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_sci_MapKeyValueTupleIterator(this.sci_HashMap__f_rootNode))
});
$c_sci_HashMap.prototype.contains__O__Z = (function(key) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var keyHash = $m_sc_Hashing$().improve__I__I(keyUnimprovedHash);
  return this.sci_HashMap__f_rootNode.containsKey__O__I__I__I__Z(key, keyUnimprovedHash, keyHash, 0)
});
$c_sci_HashMap.prototype.apply__O__O = (function(key) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var keyHash = $m_sc_Hashing$().improve__I__I(keyUnimprovedHash);
  return this.sci_HashMap__f_rootNode.apply__O__I__I__I__O(key, keyUnimprovedHash, keyHash, 0)
});
$c_sci_HashMap.prototype.get__O__s_Option = (function(key) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var keyHash = $m_sc_Hashing$().improve__I__I(keyUnimprovedHash);
  return this.sci_HashMap__f_rootNode.get__O__I__I__I__s_Option(key, keyUnimprovedHash, keyHash, 0)
});
$c_sci_HashMap.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var keyHash = $m_sc_Hashing$().improve__I__I(keyUnimprovedHash);
  return this.sci_HashMap__f_rootNode.getOrElse__O__I__I__I__F0__O(key, keyUnimprovedHash, keyHash, 0, default$1)
});
$c_sci_HashMap.prototype.updated__O__O__sci_HashMap = (function(key, value) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var newRootNode = this.sci_HashMap__f_rootNode.updated__O__O__I__I__I__Z__sci_BitmapIndexedMapNode(key, value, keyUnimprovedHash, $m_sc_Hashing$().improve__I__I(keyUnimprovedHash), 0, true);
  return ((newRootNode === this.sci_HashMap__f_rootNode) ? this : new $c_sci_HashMap(newRootNode))
});
$c_sci_HashMap.prototype.removed__O__sci_HashMap = (function(key) {
  var keyUnimprovedHash = $m_sr_Statics$().anyHash__O__I(key);
  var newRootNode = this.sci_HashMap__f_rootNode.removed__O__I__I__I__sci_BitmapIndexedMapNode(key, keyUnimprovedHash, $m_sc_Hashing$().improve__I__I(keyUnimprovedHash), 0);
  return ((newRootNode === this.sci_HashMap__f_rootNode) ? this : new $c_sci_HashMap(newRootNode))
});
$c_sci_HashMap.prototype.tail__sci_HashMap = (function() {
  var key = $as_T2(this.iterator__sc_Iterator().next__O()).T2__f__1;
  return this.removed__O__sci_HashMap(key)
});
$c_sci_HashMap.prototype.foreachEntry__F2__V = (function(f) {
  this.sci_HashMap__f_rootNode.foreachEntry__F2__V(f)
});
$c_sci_HashMap.prototype.equals__O__Z = (function(that) {
  if ((that instanceof $c_sci_HashMap)) {
    var x2 = $as_sci_HashMap(that);
    if ((this === x2)) {
      return true
    } else {
      var x = this.sci_HashMap__f_rootNode;
      var x$2 = x2.sci_HashMap__f_rootNode;
      return ((x === null) ? (x$2 === null) : x.equals__O__Z(x$2))
    }
  } else {
    return $f_sc_Map__equals__O__Z(this, that)
  }
});
$c_sci_HashMap.prototype.hashCode__I = (function() {
  if (this.isEmpty__Z()) {
    return $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_emptyMapHash
  } else {
    var hashIterator = new $c_sci_MapKeyValueTupleHashIterator(this.sci_HashMap__f_rootNode);
    var hash = $m_s_util_hashing_MurmurHash3$().unorderedHash__sc_IterableOnce__I__I(hashIterator, $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed);
    return hash
  }
});
$c_sci_HashMap.prototype.className__T = (function() {
  return "HashMap"
});
$c_sci_HashMap.prototype.drop__I__O = (function(n) {
  return $as_sci_HashMap($f_sc_IterableOps__drop__I__O(this, n))
});
$c_sci_HashMap.prototype.head__O = (function() {
  return $as_T2(this.iterator__sc_Iterator().next__O())
});
$c_sci_HashMap.prototype.tail__O = (function() {
  return this.tail__sci_HashMap()
});
$c_sci_HashMap.prototype.updated__O__O__sci_MapOps = (function(key, value) {
  return this.updated__O__O__sci_HashMap(key, value)
});
function $as_sci_HashMap(obj) {
  return (((obj instanceof $c_sci_HashMap) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.HashMap"))
}
function $isArrayOf_sci_HashMap(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_HashMap)))
}
function $asArrayOf_sci_HashMap(obj, depth) {
  return (($isArrayOf_sci_HashMap(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.HashMap;", depth))
}
var $d_sci_HashMap = new $TypeData().initClass({
  sci_HashMap: 0
}, false, "scala.collection.immutable.HashMap", {
  sci_HashMap: 1,
  sci_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  sci_Map: 1,
  sci_Iterable: 1,
  sci_MapOps: 1,
  sci_StrictOptimizedMapOps: 1,
  sc_StrictOptimizedMapOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_HashMap.prototype.$classData = $d_sci_HashMap;
/** @constructor */
function $c_sci_Stream$Cons(head, tl) {
  this.sci_Stream$Cons__f_head = null;
  this.sci_Stream$Cons__f_tlVal = null;
  this.sci_Stream$Cons__f_tlGen = null;
  this.sci_Stream$Cons__f_head = head;
  this.sci_Stream$Cons__f_tlGen = tl
}
$c_sci_Stream$Cons.prototype = new $h_sci_Stream();
$c_sci_Stream$Cons.prototype.constructor = $c_sci_Stream$Cons;
/** @constructor */
function $h_sci_Stream$Cons() {
  /*<skip>*/
}
$h_sci_Stream$Cons.prototype = $c_sci_Stream$Cons.prototype;
$c_sci_Stream$Cons.prototype.head__O = (function() {
  return this.sci_Stream$Cons__f_head
});
$c_sci_Stream$Cons.prototype.isEmpty__Z = (function() {
  return false
});
$c_sci_Stream$Cons.prototype.tailDefined__Z = (function() {
  return (this.sci_Stream$Cons__f_tlGen === null)
});
$c_sci_Stream$Cons.prototype.tail__sci_Stream = (function() {
  if ((!this.tailDefined__Z())) {
    if ((!this.tailDefined__Z())) {
      this.sci_Stream$Cons__f_tlVal = $as_sci_Stream(this.sci_Stream$Cons__f_tlGen.apply__O());
      this.sci_Stream$Cons__f_tlGen = null
    }
  };
  return this.sci_Stream$Cons__f_tlVal
});
$c_sci_Stream$Cons.prototype.force__sci_Stream$Cons = (function() {
  var these = this;
  var those = this;
  if ((!these.isEmpty__Z())) {
    these = $as_sci_Stream(these.tail__O())
  };
  while ((those !== these)) {
    if (these.isEmpty__Z()) {
      return this
    };
    these = $as_sci_Stream(these.tail__O());
    if (these.isEmpty__Z()) {
      return this
    };
    these = $as_sci_Stream(these.tail__O());
    if ((these === those)) {
      return this
    };
    those = $as_sci_Stream(those.tail__O())
  };
  return this
});
$c_sci_Stream$Cons.prototype.force__sci_Stream = (function() {
  return this.force__sci_Stream$Cons()
});
$c_sci_Stream$Cons.prototype.tail__O = (function() {
  return this.tail__sci_Stream()
});
var $d_sci_Stream$Cons = new $TypeData().initClass({
  sci_Stream$Cons: 0
}, false, "scala.collection.immutable.Stream$Cons", {
  sci_Stream$Cons: 1,
  sci_Stream: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_LinearSeq: 1,
  sc_LinearSeq: 1,
  sc_LinearSeqOps: 1,
  sci_LinearSeqOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Stream$Cons.prototype.$classData = $d_sci_Stream$Cons;
/** @constructor */
function $c_sci_Stream$Empty$() {
  /*<skip>*/
}
$c_sci_Stream$Empty$.prototype = new $h_sci_Stream();
$c_sci_Stream$Empty$.prototype.constructor = $c_sci_Stream$Empty$;
/** @constructor */
function $h_sci_Stream$Empty$() {
  /*<skip>*/
}
$h_sci_Stream$Empty$.prototype = $c_sci_Stream$Empty$.prototype;
$c_sci_Stream$Empty$.prototype.isEmpty__Z = (function() {
  return true
});
$c_sci_Stream$Empty$.prototype.head__E = (function() {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "head of empty stream")
});
$c_sci_Stream$Empty$.prototype.tail__sci_Stream = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "tail of empty stream")
});
$c_sci_Stream$Empty$.prototype.knownSize__I = (function() {
  return 0
});
$c_sci_Stream$Empty$.prototype.tailDefined__Z = (function() {
  return false
});
$c_sci_Stream$Empty$.prototype.force__sci_Stream = (function() {
  return this
});
$c_sci_Stream$Empty$.prototype.tail__O = (function() {
  return this.tail__sci_Stream()
});
$c_sci_Stream$Empty$.prototype.head__O = (function() {
  this.head__E()
});
var $d_sci_Stream$Empty$ = new $TypeData().initClass({
  sci_Stream$Empty$: 0
}, false, "scala.collection.immutable.Stream$Empty$", {
  sci_Stream$Empty$: 1,
  sci_Stream: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_LinearSeq: 1,
  sc_LinearSeq: 1,
  sc_LinearSeqOps: 1,
  sci_LinearSeqOps: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Stream$Empty$.prototype.$classData = $d_sci_Stream$Empty$;
var $n_sci_Stream$Empty$;
function $m_sci_Stream$Empty$() {
  if ((!$n_sci_Stream$Empty$)) {
    $n_sci_Stream$Empty$ = new $c_sci_Stream$Empty$()
  };
  return $n_sci_Stream$Empty$
}
/** @constructor */
function $c_scm_AbstractBuffer() {
  /*<skip>*/
}
$c_scm_AbstractBuffer.prototype = new $h_scm_AbstractSeq();
$c_scm_AbstractBuffer.prototype.constructor = $c_scm_AbstractBuffer;
/** @constructor */
function $h_scm_AbstractBuffer() {
  /*<skip>*/
}
$h_scm_AbstractBuffer.prototype = $c_scm_AbstractBuffer.prototype;
$c_scm_AbstractBuffer.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs)
});
/** @constructor */
function $c_scm_AbstractMap() {
  /*<skip>*/
}
$c_scm_AbstractMap.prototype = new $h_sc_AbstractMap();
$c_scm_AbstractMap.prototype.constructor = $c_scm_AbstractMap;
/** @constructor */
function $h_scm_AbstractMap() {
  /*<skip>*/
}
$h_scm_AbstractMap.prototype = $c_scm_AbstractMap.prototype;
$c_scm_AbstractMap.prototype.mapFactory__sc_MapFactory = (function() {
  return $m_scm_Map$()
});
$c_scm_AbstractMap.prototype.knownSize__I = (function() {
  return (-1)
});
$c_scm_AbstractMap.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_scm_AbstractMap.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs)
});
$c_scm_AbstractMap.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_scm_Iterable$()
});
$c_scm_AbstractMap.prototype.result__O = (function() {
  return this
});
/** @constructor */
function $c_sjs_js_WrappedDictionary(dict) {
  this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict = null;
  this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict = dict
}
$c_sjs_js_WrappedDictionary.prototype = new $h_scm_AbstractMap();
$c_sjs_js_WrappedDictionary.prototype.constructor = $c_sjs_js_WrappedDictionary;
/** @constructor */
function $h_sjs_js_WrappedDictionary() {
  /*<skip>*/
}
$h_sjs_js_WrappedDictionary.prototype = $c_sjs_js_WrappedDictionary.prototype;
$c_sjs_js_WrappedDictionary.prototype.fromSpecific__sc_IterableOnce__sjs_js_WrappedDictionary = (function(coll) {
  var d = new $c_sjs_js_WrappedDictionary({});
  $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(d, coll);
  return d
});
$c_sjs_js_WrappedDictionary.prototype.get__T__s_Option = (function(key) {
  var dict = this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict;
  if ($uZ($m_sjs_js_WrappedDictionary$Cache$().sjs_js_WrappedDictionary$Cache$__f_safeHasOwnProperty.call(dict, key))) {
    return new $c_s_Some(this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict[key])
  } else {
    return $m_s_None$()
  }
});
$c_sjs_js_WrappedDictionary.prototype.apply__T__O = (function(key) {
  var dict = this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict;
  if ($uZ($m_sjs_js_WrappedDictionary$Cache$().sjs_js_WrappedDictionary$Cache$__f_safeHasOwnProperty.call(dict, key))) {
    return this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict[key]
  } else {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), ("key not found: " + key))
  }
});
$c_sjs_js_WrappedDictionary.prototype.addOne__T2__sjs_js_WrappedDictionary = (function(kv) {
  var dict = this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict;
  var key = $as_T(kv.T2__f__1);
  var value = kv.T2__f__2;
  dict[key] = value;
  return this
});
$c_sjs_js_WrappedDictionary.prototype.iterator__sc_Iterator = (function() {
  return new $c_sjs_js_WrappedDictionary$DictionaryIterator(this.sjs_js_WrappedDictionary__f_scala$scalajs$js$WrappedDictionary$$dict)
});
$c_sjs_js_WrappedDictionary.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__T2__sjs_js_WrappedDictionary($as_T2(elem))
});
$c_sjs_js_WrappedDictionary.prototype.apply__O__O = (function(key) {
  return this.apply__T__O($as_T(key))
});
$c_sjs_js_WrappedDictionary.prototype.get__O__s_Option = (function(key) {
  return this.get__T__s_Option($as_T(key))
});
$c_sjs_js_WrappedDictionary.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  return this.fromSpecific__sc_IterableOnce__sjs_js_WrappedDictionary(coll)
});
$c_sjs_js_WrappedDictionary.prototype.fromSpecific__sc_IterableOnce__sc_IterableOps = (function(coll) {
  return this.fromSpecific__sc_IterableOnce__sjs_js_WrappedDictionary(coll)
});
var $d_sjs_js_WrappedDictionary = new $TypeData().initClass({
  sjs_js_WrappedDictionary: 0
}, false, "scala.scalajs.js.WrappedDictionary", {
  sjs_js_WrappedDictionary: 1,
  scm_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  scm_Map: 1,
  scm_Iterable: 1,
  scm_MapOps: 1,
  scm_Cloneable: 1,
  jl_Cloneable: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1,
  scm_Shrinkable: 1
});
$c_sjs_js_WrappedDictionary.prototype.$classData = $d_sjs_js_WrappedDictionary;
function $ct_sci_Vector__AO__($thiz, prefix1) {
  $thiz.sci_Vector__f_prefix1 = prefix1;
  return $thiz
}
/** @constructor */
function $c_sci_Vector() {
  this.sci_Vector__f_prefix1 = null
}
$c_sci_Vector.prototype = new $h_sci_AbstractSeq();
$c_sci_Vector.prototype.constructor = $c_sci_Vector;
/** @constructor */
function $h_sci_Vector() {
  /*<skip>*/
}
$h_sci_Vector.prototype = $c_sci_Vector.prototype;
$c_sci_Vector.prototype.canEqual__O__Z = (function(that) {
  return $f_sci_IndexedSeq__canEqual__O__Z(this, that)
});
$c_sci_Vector.prototype.sameElements__sc_IterableOnce__Z = (function(o) {
  return $f_sci_IndexedSeq__sameElements__sc_IterableOnce__Z(this, o)
});
$c_sci_Vector.prototype.stringPrefix__T = (function() {
  return "IndexedSeq"
});
$c_sci_Vector.prototype.lengthCompare__I__I = (function(len) {
  var x = this.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sci_Vector.prototype.knownSize__I = (function() {
  return this.length__I()
});
$c_sci_Vector.prototype.length__I = (function() {
  return ((this instanceof $c_sci_BigVector) ? $as_sci_BigVector(this).sci_BigVector__f_length0 : this.sci_Vector__f_prefix1.u.length)
});
$c_sci_Vector.prototype.iterator__sc_Iterator = (function() {
  return (($m_sci_Vector0$() === this) ? $m_sci_Vector$().sci_Vector$__f_scala$collection$immutable$Vector$$emptyIterator : new $c_sci_NewVectorIterator(this, this.length__I(), this.vectorSliceCount__I()))
});
$c_sci_Vector.prototype.filterImpl__F1__Z__sci_Vector = (function(pred, isFlipped) {
  var i = 0;
  var len = this.sci_Vector__f_prefix1.u.length;
  while ((i !== len)) {
    if (($uZ(pred.apply__O__O(this.sci_Vector__f_prefix1.get(i))) === isFlipped)) {
      var bitmap = 0;
      var j = ((1 + i) | 0);
      while ((j < len)) {
        if (($uZ(pred.apply__O__O(this.sci_Vector__f_prefix1.get(j))) !== isFlipped)) {
          bitmap = (bitmap | (1 << j))
        };
        j = ((1 + j) | 0)
      };
      var $$x1 = i;
      var i$1 = bitmap;
      var newLen = (($$x1 + $m_jl_Integer$().bitCount__I__I(i$1)) | 0);
      if ((this instanceof $c_sci_BigVector)) {
        var b = new $c_sci_VectorBuilder();
        var k = 0;
        while ((k < i)) {
          b.addOne__O__sci_VectorBuilder(this.sci_Vector__f_prefix1.get(k));
          k = ((1 + k) | 0)
        };
        k = ((1 + i) | 0);
        while ((i !== newLen)) {
          if ((((1 << k) & bitmap) !== 0)) {
            b.addOne__O__sci_VectorBuilder(this.sci_Vector__f_prefix1.get(k));
            i = ((1 + i) | 0)
          };
          k = ((1 + k) | 0)
        };
        $as_sci_BigVector(this).foreachRest__F1__V(new $c_sjsr_AnonFunction1(((this$1, pred$1, isFlipped$1, b$1) => ((v$2) => (($uZ(pred$1.apply__O__O(v$2)) !== isFlipped$1) ? b$1.addOne__O__sci_VectorBuilder(v$2) : (void 0))))(this, pred, isFlipped, b)));
        return b.result__sci_Vector()
      } else {
        if ((newLen === 0)) {
          return $m_sci_Vector0$()
        };
        var newData = new $ac_O(newLen);
        var src = this.sci_Vector__f_prefix1;
        var length = i;
        src.copyTo(0, newData, 0, length);
        var k$2 = ((1 + i) | 0);
        while ((i !== newLen)) {
          if ((((1 << k$2) & bitmap) !== 0)) {
            newData.set(i, this.sci_Vector__f_prefix1.get(k$2));
            i = ((1 + i) | 0)
          };
          k$2 = ((1 + k$2) | 0)
        };
        return new $c_sci_Vector1(newData)
      }
    };
    i = ((1 + i) | 0)
  };
  if ((this instanceof $c_sci_BigVector)) {
    var b$2 = new $c_sci_VectorBuilder();
    b$2.initFrom__AO__V(this.sci_Vector__f_prefix1);
    $as_sci_BigVector(this).foreachRest__F1__V(new $c_sjsr_AnonFunction1(((this$2, pred$2, isFlipped$2, b$2$1) => ((v$3$2) => (($uZ(pred$2.apply__O__O(v$3$2)) !== isFlipped$2) ? b$2$1.addOne__O__sci_VectorBuilder(v$3$2) : (void 0))))(this, pred, isFlipped, b$2)));
    return b$2.result__sci_Vector()
  } else {
    return this
  }
});
$c_sci_Vector.prototype.className__T = (function() {
  return "Vector"
});
$c_sci_Vector.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  return this.iterator__sc_Iterator().copyToArray__O__I__I__I(xs, start, len)
});
$c_sci_Vector.prototype.applyPreferredMaxLength__I = (function() {
  return $m_sci_Vector$().sci_Vector$__f_scala$collection$immutable$Vector$$defaultApplyPreferredMaxLength
});
$c_sci_Vector.prototype.ioob__I__jl_IndexOutOfBoundsException = (function(index) {
  return $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (((index + " is out of bounds (min 0, max ") + (((-1) + this.length__I()) | 0)) + ")"))
});
$c_sci_Vector.prototype.head__O = (function() {
  if ((this.sci_Vector__f_prefix1.u.length === 0)) {
    throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "empty.head")
  } else {
    return this.sci_Vector__f_prefix1.get(0)
  }
});
$c_sci_Vector.prototype.foreach__F1__V = (function(f) {
  var c = this.vectorSliceCount__I();
  var i = 0;
  while ((i < c)) {
    var $$x1 = $m_sci_VectorStatics$();
    var idx = i;
    var c$1 = ((c / 2) | 0);
    var a = ((idx - c$1) | 0);
    $$x1.foreachRec__I__AO__F1__V((((-1) + ((((1 + c$1) | 0) - ((a < 0) ? ((-a) | 0) : a)) | 0)) | 0), this.vectorSlice__I__AO(i), f);
    i = ((1 + i) | 0)
  }
});
$c_sci_Vector.prototype.drop__I__O = (function(n) {
  var until = this.length__I();
  return this.slice__I__I__sci_Vector(n, until)
});
$c_sci_Vector.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_Vector$()
});
function $as_sci_Vector(obj) {
  return (((obj instanceof $c_sci_Vector) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector"))
}
function $isArrayOf_sci_Vector(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector)))
}
function $asArrayOf_sci_Vector(obj, depth) {
  return (($isArrayOf_sci_Vector(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector;", depth))
}
function $as_sci_ArraySeq$ofRef(obj) {
  return ((false || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.ArraySeq$ofRef"))
}
function $isArrayOf_sci_ArraySeq$ofRef(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_ArraySeq$ofRef)))
}
function $asArrayOf_sci_ArraySeq$ofRef(obj, depth) {
  return (($isArrayOf_sci_ArraySeq$ofRef(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.ArraySeq$ofRef;", depth))
}
function $p_sci_List__loop$2__I__sci_List__I__I($thiz, i, xs, len$1) {
  while (true) {
    if ((i === len$1)) {
      return (xs.isEmpty__Z() ? 0 : 1)
    } else if (xs.isEmpty__Z()) {
      return (-1)
    } else {
      var temp$i = ((1 + i) | 0);
      var temp$xs = $as_sci_List(xs.tail__O());
      i = temp$i;
      xs = temp$xs
    }
  }
}
function $p_sci_List__listEq$1__sci_List__sci_List__Z($thiz, a, b) {
  while (true) {
    if ((a === b)) {
      return true
    } else {
      var aEmpty = a.isEmpty__Z();
      var bEmpty = b.isEmpty__Z();
      if (((!(aEmpty || bEmpty)) && $m_sr_BoxesRunTime$().equals__O__O__Z(a.head__O(), b.head__O()))) {
        var temp$a = $as_sci_List(a.tail__O());
        var temp$b = $as_sci_List(b.tail__O());
        a = temp$a;
        b = temp$b
      } else {
        return (aEmpty && bEmpty)
      }
    }
  }
}
/** @constructor */
function $c_sci_List() {
  /*<skip>*/
}
$c_sci_List.prototype = new $h_sci_AbstractSeq();
$c_sci_List.prototype.constructor = $c_sci_List;
/** @constructor */
function $h_sci_List() {
  /*<skip>*/
}
$h_sci_List.prototype = $c_sci_List.prototype;
$c_sci_List.prototype.iterator__sc_Iterator = (function() {
  return new $c_sc_StrictOptimizedLinearSeqOps$$anon$1(this)
});
$c_sci_List.prototype.stringPrefix__T = (function() {
  return "LinearSeq"
});
$c_sci_List.prototype.apply__I__O = (function(n) {
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_List.prototype.sameElements__sc_IterableOnce__Z = (function(that) {
  return $f_sc_LinearSeqOps__sameElements__sc_IterableOnce__Z(this, that)
});
$c_sci_List.prototype.indexWhere__F1__I__I = (function(p, from) {
  return $f_sc_LinearSeqOps__indexWhere__F1__I__I(this, p, from)
});
$c_sci_List.prototype.$colon$colon$colon__sci_List__sci_List = (function(prefix) {
  if (this.isEmpty__Z()) {
    return prefix
  } else if (prefix.isEmpty__Z()) {
    return this
  } else {
    var result = new $c_sci_$colon$colon(prefix.head__O(), this);
    var curr = result;
    var that = $as_sci_List(prefix.tail__O());
    while ((!that.isEmpty__Z())) {
      var temp = new $c_sci_$colon$colon(that.head__O(), this);
      curr.sci_$colon$colon__f_next = temp;
      curr = temp;
      that = $as_sci_List(that.tail__O())
    };
    return result
  }
});
$c_sci_List.prototype.isEmpty__Z = (function() {
  return (this === $m_sci_Nil$())
});
$c_sci_List.prototype.prependedAll__sc_IterableOnce__sci_List = (function(prefix) {
  if ((prefix instanceof $c_sci_List)) {
    var x2 = $as_sci_List(prefix);
    return this.$colon$colon$colon__sci_List__sci_List(x2)
  };
  if ((prefix.knownSize__I() === 0)) {
    return this
  };
  if (false) {
    var x3 = $as_scm_ListBuffer(prefix);
    if (this.isEmpty__Z()) {
      return x3.toList__sci_List()
    }
  };
  var iter = prefix.iterator__sc_Iterator();
  if (iter.hasNext__Z()) {
    var result = new $c_sci_$colon$colon(iter.next__O(), this);
    var curr = result;
    while (iter.hasNext__Z()) {
      var temp = new $c_sci_$colon$colon(iter.next__O(), this);
      curr.sci_$colon$colon__f_next = temp;
      curr = temp
    };
    return result
  } else {
    return this
  }
});
$c_sci_List.prototype.foreach__F1__V = (function(f) {
  var these = this;
  while ((!these.isEmpty__Z())) {
    f.apply__O__O(these.head__O());
    these = $as_sci_List(these.tail__O())
  }
});
$c_sci_List.prototype.length__I = (function() {
  var these = this;
  var len = 0;
  while ((!these.isEmpty__Z())) {
    len = ((1 + len) | 0);
    these = $as_sci_List(these.tail__O())
  };
  return len
});
$c_sci_List.prototype.lengthCompare__I__I = (function(len) {
  return ((len < 0) ? 1 : $p_sci_List__loop$2__I__sci_List__I__I(this, 0, this, len))
});
$c_sci_List.prototype.exists__F1__Z = (function(p) {
  var these = this;
  while ((!these.isEmpty__Z())) {
    if ($uZ(p.apply__O__O(these.head__O()))) {
      return true
    };
    these = $as_sci_List(these.tail__O())
  };
  return false
});
$c_sci_List.prototype.className__T = (function() {
  return "List"
});
$c_sci_List.prototype.equals__O__Z = (function(o) {
  if ((o instanceof $c_sci_List)) {
    var x2 = $as_sci_List(o);
    return $p_sci_List__listEq$1__sci_List__sci_List__Z(this, this, x2)
  } else {
    return $f_sc_Seq__equals__O__Z(this, o)
  }
});
$c_sci_List.prototype.apply__O__O = (function(v1) {
  var n = $uI(v1);
  return $f_sc_LinearSeqOps__apply__I__O(this, n)
});
$c_sci_List.prototype.drop__I__O = (function(n) {
  return $p_sc_StrictOptimizedLinearSeqOps__loop$2__I__sc_LinearSeq__sc_LinearSeq(this, n, this)
});
$c_sci_List.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sci_List$()
});
function $as_sci_List(obj) {
  return (((obj instanceof $c_sci_List) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.List"))
}
function $isArrayOf_sci_List(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_List)))
}
function $asArrayOf_sci_List(obj, depth) {
  return (($isArrayOf_sci_List(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.List;", depth))
}
/** @constructor */
function $c_sci_VectorImpl() {
  this.sci_Vector__f_prefix1 = null
}
$c_sci_VectorImpl.prototype = new $h_sci_Vector();
$c_sci_VectorImpl.prototype.constructor = $c_sci_VectorImpl;
/** @constructor */
function $h_sci_VectorImpl() {
  /*<skip>*/
}
$h_sci_VectorImpl.prototype = $c_sci_VectorImpl.prototype;
$c_sci_VectorImpl.prototype.slice__I__I__sci_Vector = (function(from, until) {
  var lo = ((from > 0) ? from : 0);
  var b = this.length__I();
  var hi = ((until < b) ? until : b);
  var newlen = ((hi - lo) | 0);
  return ((newlen === this.length__I()) ? this : ((hi <= lo) ? $m_sci_Vector0$() : this.slice0__I__I__sci_Vector(lo, hi)))
});
function $as_scm_ArraySeq$ofChar(obj) {
  return ((false || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.ArraySeq$ofChar"))
}
function $isArrayOf_scm_ArraySeq$ofChar(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_ArraySeq$ofChar)))
}
function $asArrayOf_scm_ArraySeq$ofChar(obj, depth) {
  return (($isArrayOf_scm_ArraySeq$ofChar(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.ArraySeq$ofChar;", depth))
}
function $p_scm_HashMap__put0__O__O__I__Z__s_Some($thiz, key, value, hash, getOld) {
  if ((((1 + $thiz.scm_HashMap__f_contentSize) | 0) >= $thiz.scm_HashMap__f_threshold)) {
    $p_scm_HashMap__growTable__I__V($thiz, ($thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length << 1))
  };
  var idx = (hash & (((-1) + $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length) | 0));
  return $p_scm_HashMap__put0__O__O__Z__I__I__s_Some($thiz, key, value, getOld, hash, idx)
}
function $p_scm_HashMap__put0__O__O__Z__s_Some($thiz, key, value, getOld) {
  if ((((1 + $thiz.scm_HashMap__f_contentSize) | 0) >= $thiz.scm_HashMap__f_threshold)) {
    $p_scm_HashMap__growTable__I__V($thiz, ($thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length << 1))
  };
  var originalHash = $m_sr_Statics$().anyHash__O__I(key);
  var hash = (originalHash ^ ((originalHash >>> 16) | 0));
  var idx = (hash & (((-1) + $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length) | 0));
  return $p_scm_HashMap__put0__O__O__Z__I__I__s_Some($thiz, key, value, getOld, hash, idx)
}
function $p_scm_HashMap__put0__O__O__Z__I__I__s_Some($thiz, key, value, getOld, hash, idx) {
  var x1 = $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get(idx);
  if ((x1 === null)) {
    $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.set(idx, new $c_scm_HashMap$Node(key, hash, value, null))
  } else {
    var prev = null;
    var n = x1;
    while (((n !== null) && (n.scm_HashMap$Node__f__hash <= hash))) {
      if (((n.scm_HashMap$Node__f__hash === hash) && $m_sr_BoxesRunTime$().equals__O__O__Z(key, n.scm_HashMap$Node__f__key))) {
        var old = n.scm_HashMap$Node__f__value;
        n.scm_HashMap$Node__f__value = value;
        return (getOld ? new $c_s_Some(old) : null)
      };
      prev = n;
      n = n.scm_HashMap$Node__f__next
    };
    if ((prev === null)) {
      $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.set(idx, new $c_scm_HashMap$Node(key, hash, value, x1))
    } else {
      prev.scm_HashMap$Node__f__next = new $c_scm_HashMap$Node(key, hash, value, prev.scm_HashMap$Node__f__next)
    }
  };
  $thiz.scm_HashMap__f_contentSize = ((1 + $thiz.scm_HashMap__f_contentSize) | 0);
  return null
}
function $p_scm_HashMap__growTable__I__V($thiz, newlen) {
  if ((newlen < 0)) {
    throw $m_sjsr_package$().unwrapJavaScriptException__jl_Throwable__O($ct_jl_RuntimeException__T__(new $c_jl_RuntimeException(), (("new HashMap table size " + newlen) + " exceeds maximum")))
  };
  var oldlen = $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length;
  $thiz.scm_HashMap__f_threshold = $p_scm_HashMap__newThreshold__I__I($thiz, newlen);
  if (($thiz.scm_HashMap__f_contentSize === 0)) {
    $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table = new ($d_scm_HashMap$Node.getArrayOf().constr)(newlen)
  } else {
    var original = $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table;
    $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table = $asArrayOf_scm_HashMap$Node($m_ju_Arrays$().copyOf__AO__I__AO(original, newlen), 1);
    var preLow = new $c_scm_HashMap$Node(null, 0, null, null);
    var preHigh = new $c_scm_HashMap$Node(null, 0, null, null);
    while ((oldlen < newlen)) {
      var i = 0;
      while ((i < oldlen)) {
        var old = $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get(i);
        if ((old !== null)) {
          preLow.scm_HashMap$Node__f__next = null;
          preHigh.scm_HashMap$Node__f__next = null;
          var lastLow = preLow;
          var lastHigh = preHigh;
          var n = old;
          while ((n !== null)) {
            var next = n.scm_HashMap$Node__f__next;
            if (((n.scm_HashMap$Node__f__hash & oldlen) === 0)) {
              lastLow.scm_HashMap$Node__f__next = n;
              lastLow = n
            } else {
              lastHigh.scm_HashMap$Node__f__next = n;
              lastHigh = n
            };
            n = next
          };
          lastLow.scm_HashMap$Node__f__next = null;
          if ((old !== preLow.scm_HashMap$Node__f__next)) {
            $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.set(i, preLow.scm_HashMap$Node__f__next)
          };
          if ((preHigh.scm_HashMap$Node__f__next !== null)) {
            $thiz.scm_HashMap__f_scala$collection$mutable$HashMap$$table.set(((i + oldlen) | 0), preHigh.scm_HashMap$Node__f__next);
            lastHigh.scm_HashMap$Node__f__next = null
          }
        };
        i = ((1 + i) | 0)
      };
      oldlen = (oldlen << 1)
    }
  }
}
function $p_scm_HashMap__tableSizeFor__I__I($thiz, capacity) {
  var x = (((-1) + capacity) | 0);
  var i = ((x > 4) ? x : 4);
  var x$1 = ((((-2147483648) >> $clz32(i)) & i) << 1);
  return ((x$1 < 1073741824) ? x$1 : 1073741824)
}
function $p_scm_HashMap__newThreshold__I__I($thiz, size) {
  return $doubleToInt((size * $thiz.scm_HashMap__f_loadFactor))
}
/** @constructor */
function $c_scm_HashMap(initialCapacity, loadFactor) {
  this.scm_HashMap__f_loadFactor = 0.0;
  this.scm_HashMap__f_scala$collection$mutable$HashMap$$table = null;
  this.scm_HashMap__f_threshold = 0;
  this.scm_HashMap__f_contentSize = 0;
  this.scm_HashMap__f_loadFactor = loadFactor;
  this.scm_HashMap__f_scala$collection$mutable$HashMap$$table = new ($d_scm_HashMap$Node.getArrayOf().constr)($p_scm_HashMap__tableSizeFor__I__I(this, initialCapacity));
  this.scm_HashMap__f_threshold = $p_scm_HashMap__newThreshold__I__I(this, this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length);
  this.scm_HashMap__f_contentSize = 0
}
$c_scm_HashMap.prototype = new $h_scm_AbstractMap();
$c_scm_HashMap.prototype.constructor = $c_scm_HashMap;
/** @constructor */
function $h_scm_HashMap() {
  /*<skip>*/
}
$h_scm_HashMap.prototype = $c_scm_HashMap.prototype;
$c_scm_HashMap.prototype.size__I = (function() {
  return this.scm_HashMap__f_contentSize
});
$c_scm_HashMap.prototype.sizeHint__I__V = (function(size) {
  var target = $p_scm_HashMap__tableSizeFor__I__I(this, $doubleToInt((((1 + size) | 0) / this.scm_HashMap__f_loadFactor)));
  if ((target > this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length)) {
    $p_scm_HashMap__growTable__I__V(this, target)
  }
});
$c_scm_HashMap.prototype.addAll__sc_IterableOnce__scm_HashMap = (function(xs) {
  this.sizeHint__I__V(xs.knownSize__I());
  if ((xs instanceof $c_sci_HashMap)) {
    var x2 = $as_sci_HashMap(xs);
    var f = new $c_sjsr_AnonFunction3(((this$1) => ((k$2, v$2, h$2) => {
      var h = $uI(h$2);
      $p_scm_HashMap__put0__O__O__I__Z__s_Some(this$1, k$2, v$2, (h ^ ((h >>> 16) | 0)), false)
    }))(this));
    x2.sci_HashMap__f_rootNode.foreachWithHash__F3__V(f);
    return this
  } else if ((xs instanceof $c_scm_HashMap)) {
    var x3 = $as_scm_HashMap(xs);
    var iter = x3.nodeIterator__sc_Iterator();
    while (iter.hasNext__Z()) {
      var next = $as_scm_HashMap$Node(iter.next__O());
      $p_scm_HashMap__put0__O__O__I__Z__s_Some(this, next.scm_HashMap$Node__f__key, next.scm_HashMap$Node__f__value, next.scm_HashMap$Node__f__hash, false)
    };
    return this
  } else if ($is_scm_Map(xs)) {
    var x4 = $as_scm_Map(xs);
    x4.foreachEntry__F2__V(new $c_sjsr_AnonFunction2(((this$2) => ((key$2, value$2) => {
      var originalHash = $m_sr_Statics$().anyHash__O__I(key$2);
      return $p_scm_HashMap__put0__O__O__I__Z__s_Some(this$2, key$2, value$2, (originalHash ^ ((originalHash >>> 16) | 0)), false)
    }))(this)));
    return this
  } else {
    return $as_scm_HashMap($f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs))
  }
});
$c_scm_HashMap.prototype.iterator__sc_Iterator = (function() {
  return ((this.scm_HashMap__f_contentSize === 0) ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_scm_HashMap$$anon$1(this))
});
$c_scm_HashMap.prototype.nodeIterator__sc_Iterator = (function() {
  return ((this.scm_HashMap__f_contentSize === 0) ? $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty : new $c_scm_HashMap$$anon$4(this))
});
$c_scm_HashMap.prototype.get__O__s_Option = (function(key) {
  var originalHash = $m_sr_Statics$().anyHash__O__I(key);
  var hash = (originalHash ^ ((originalHash >>> 16) | 0));
  var x1 = this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get((hash & (((-1) + this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length) | 0)));
  var x1$1 = ((x1 === null) ? null : x1.findNode__O__I__scm_HashMap$Node(key, hash));
  return ((x1$1 === null) ? $m_s_None$() : new $c_s_Some(x1$1.scm_HashMap$Node__f__value))
});
$c_scm_HashMap.prototype.apply__O__O = (function(key) {
  var originalHash = $m_sr_Statics$().anyHash__O__I(key);
  var hash = (originalHash ^ ((originalHash >>> 16) | 0));
  var x1 = this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get((hash & (((-1) + this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length) | 0)));
  var x1$1 = ((x1 === null) ? null : x1.findNode__O__I__scm_HashMap$Node(key, hash));
  return ((x1$1 === null) ? $f_sc_MapOps__default__O__O(this, key) : x1$1.scm_HashMap$Node__f__value)
});
$c_scm_HashMap.prototype.getOrElse__O__F0__O = (function(key, default$1) {
  var x = $objectGetClass(this);
  if ((!(x === $d_scm_HashMap.getClassOf()))) {
    return $f_sc_MapOps__getOrElse__O__F0__O(this, key, default$1)
  } else {
    var originalHash = $m_sr_Statics$().anyHash__O__I(key);
    var hash = (originalHash ^ ((originalHash >>> 16) | 0));
    var x1 = this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get((hash & (((-1) + this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length) | 0)));
    var nd = ((x1 === null) ? null : x1.findNode__O__I__scm_HashMap$Node(key, hash));
    return ((nd === null) ? default$1.apply__O() : nd.scm_HashMap$Node__f__value)
  }
});
$c_scm_HashMap.prototype.addOne__T2__scm_HashMap = (function(elem) {
  $p_scm_HashMap__put0__O__O__Z__s_Some(this, elem.T2__f__1, elem.T2__f__2, false);
  return this
});
$c_scm_HashMap.prototype.knownSize__I = (function() {
  return this.scm_HashMap__f_contentSize
});
$c_scm_HashMap.prototype.isEmpty__Z = (function() {
  return (this.scm_HashMap__f_contentSize === 0)
});
$c_scm_HashMap.prototype.foreachEntry__F2__V = (function(f) {
  var len = this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.u.length;
  var i = 0;
  while ((i < len)) {
    var n = this.scm_HashMap__f_scala$collection$mutable$HashMap$$table.get(i);
    if ((n !== null)) {
      n.foreachEntry__F2__V(f)
    };
    i = ((1 + i) | 0)
  }
});
$c_scm_HashMap.prototype.mapFactory__sc_MapFactory = (function() {
  return $m_scm_HashMap$()
});
$c_scm_HashMap.prototype.stringPrefix__T = (function() {
  return "HashMap"
});
$c_scm_HashMap.prototype.hashCode__I = (function() {
  if (this.isEmpty__Z()) {
    return $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_emptyMapHash
  } else {
    var tupleHashIterator = new $c_scm_HashMap$$anon$5(this);
    return $m_s_util_hashing_MurmurHash3$().unorderedHash__sc_IterableOnce__I__I(tupleHashIterator, $m_s_util_hashing_MurmurHash3$().s_util_hashing_MurmurHash3$__f_mapSeed)
  }
});
$c_scm_HashMap.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__T2__scm_HashMap($as_T2(elem))
});
$c_scm_HashMap.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__scm_HashMap(xs)
});
function $as_scm_HashMap(obj) {
  return (((obj instanceof $c_scm_HashMap) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.HashMap"))
}
function $isArrayOf_scm_HashMap(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_HashMap)))
}
function $asArrayOf_scm_HashMap(obj, depth) {
  return (($isArrayOf_scm_HashMap(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.HashMap;", depth))
}
var $d_scm_HashMap = new $TypeData().initClass({
  scm_HashMap: 0
}, false, "scala.collection.mutable.HashMap", {
  scm_HashMap: 1,
  scm_AbstractMap: 1,
  sc_AbstractMap: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Map: 1,
  sc_MapOps: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_MapFactoryDefaults: 1,
  s_Equals: 1,
  scm_Map: 1,
  scm_Iterable: 1,
  scm_MapOps: 1,
  scm_Cloneable: 1,
  jl_Cloneable: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1,
  scm_Shrinkable: 1,
  sc_StrictOptimizedIterableOps: 1,
  sc_StrictOptimizedMapOps: 1,
  Ljava_io_Serializable: 1
});
$c_scm_HashMap.prototype.$classData = $d_scm_HashMap;
function $ct_sci_BigVector__AO__AO__I__($thiz, _prefix1, suffix1, length0) {
  $thiz.sci_BigVector__f_suffix1 = suffix1;
  $thiz.sci_BigVector__f_length0 = length0;
  $ct_sci_Vector__AO__($thiz, _prefix1);
  return $thiz
}
/** @constructor */
function $c_sci_BigVector() {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0
}
$c_sci_BigVector.prototype = new $h_sci_VectorImpl();
$c_sci_BigVector.prototype.constructor = $c_sci_BigVector;
/** @constructor */
function $h_sci_BigVector() {
  /*<skip>*/
}
$h_sci_BigVector.prototype = $c_sci_BigVector.prototype;
$c_sci_BigVector.prototype.foreachRest__F1__V = (function(f) {
  var c = this.vectorSliceCount__I();
  var i = 1;
  while ((i < c)) {
    var $$x1 = $m_sci_VectorStatics$();
    var idx = i;
    var c$1 = ((c / 2) | 0);
    var a = ((idx - c$1) | 0);
    $$x1.foreachRec__I__AO__F1__V((((-1) + ((((1 + c$1) | 0) - ((a < 0) ? ((-a) | 0) : a)) | 0)) | 0), this.vectorSlice__I__AO(i), f);
    i = ((1 + i) | 0)
  }
});
function $as_sci_BigVector(obj) {
  return (((obj instanceof $c_sci_BigVector) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.BigVector"))
}
function $isArrayOf_sci_BigVector(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_BigVector)))
}
function $asArrayOf_sci_BigVector(obj, depth) {
  return (($isArrayOf_sci_BigVector(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.BigVector;", depth))
}
/** @constructor */
function $c_sci_Vector1(_data1) {
  this.sci_Vector__f_prefix1 = null;
  $ct_sci_Vector__AO__(this, _data1)
}
$c_sci_Vector1.prototype = new $h_sci_VectorImpl();
$c_sci_Vector1.prototype.constructor = $c_sci_Vector1;
/** @constructor */
function $h_sci_Vector1() {
  /*<skip>*/
}
$h_sci_Vector1.prototype = $c_sci_Vector1.prototype;
$c_sci_Vector1.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_Vector__f_prefix1.u.length))) {
    return this.sci_Vector__f_prefix1.get(index)
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector1.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_Vector__f_prefix1.u.length))) {
    var a1 = this.sci_Vector__f_prefix1;
    var a1c = a1.clone__O();
    a1c.set(index, elem);
    return new $c_sci_Vector1(a1c)
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector1.prototype.appended__O__sci_Vector = (function(elem) {
  var len1 = this.sci_Vector__f_prefix1.u.length;
  if ((len1 < 32)) {
    return new $c_sci_Vector1($m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_Vector__f_prefix1, elem))
  } else {
    var $$x2 = this.sci_Vector__f_prefix1;
    var $$x1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a = new $ac_O(1);
    a.set(0, elem);
    return new $c_sci_Vector2($$x2, 32, $$x1, a, 33)
  }
});
$c_sci_Vector1.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var original = this.sci_Vector__f_prefix1;
  return new $c_sci_Vector1($m_ju_Arrays$().copyOfRange__AO__I__I__AO(original, lo, hi))
});
$c_sci_Vector1.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector__f_prefix1.u.length === 1)) {
    return $m_sci_Vector0$()
  } else {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    return new $c_sci_Vector1($m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to))
  }
});
$c_sci_Vector1.prototype.vectorSliceCount__I = (function() {
  return 1
});
$c_sci_Vector1.prototype.vectorSlice__I__AO = (function(idx) {
  return this.sci_Vector__f_prefix1
});
$c_sci_Vector1.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector1.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_Vector__f_prefix1.u.length))) {
    return this.sci_Vector__f_prefix1.get(index)
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector1(obj) {
  return (((obj instanceof $c_sci_Vector1) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector1"))
}
function $isArrayOf_sci_Vector1(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector1)))
}
function $asArrayOf_sci_Vector1(obj, depth) {
  return (($isArrayOf_sci_Vector1(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector1;", depth))
}
var $d_sci_Vector1 = new $TypeData().initClass({
  sci_Vector1: 0
}, false, "scala.collection.immutable.Vector1", {
  sci_Vector1: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector1.prototype.$classData = $d_sci_Vector1;
/** @constructor */
function $c_sci_$colon$colon(head, next) {
  this.sci_$colon$colon__f_head = null;
  this.sci_$colon$colon__f_next = null;
  this.sci_$colon$colon__f_head = head;
  this.sci_$colon$colon__f_next = next
}
$c_sci_$colon$colon.prototype = new $h_sci_List();
$c_sci_$colon$colon.prototype.constructor = $c_sci_$colon$colon;
/** @constructor */
function $h_sci_$colon$colon() {
  /*<skip>*/
}
$h_sci_$colon$colon.prototype = $c_sci_$colon$colon.prototype;
$c_sci_$colon$colon.prototype.head__O = (function() {
  return this.sci_$colon$colon__f_head
});
$c_sci_$colon$colon.prototype.productPrefix__T = (function() {
  return "::"
});
$c_sci_$colon$colon.prototype.productArity__I = (function() {
  return 2
});
$c_sci_$colon$colon.prototype.productElement__I__O = (function(x$1) {
  switch (x$1) {
    case 0: {
      return this.sci_$colon$colon__f_head;
      break
    }
    case 1: {
      return this.sci_$colon$colon__f_next;
      break
    }
    default: {
      return $m_sr_Statics$().ioobe__I__O(x$1)
    }
  }
});
$c_sci_$colon$colon.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_sci_$colon$colon.prototype.tail__O = (function() {
  return this.sci_$colon$colon__f_next
});
var $d_sci_$colon$colon = new $TypeData().initClass({
  sci_$colon$colon: 0
}, false, "scala.collection.immutable.$colon$colon", {
  sci_$colon$colon: 1,
  sci_List: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_LinearSeq: 1,
  sc_LinearSeq: 1,
  sc_LinearSeqOps: 1,
  sci_LinearSeqOps: 1,
  sc_StrictOptimizedLinearSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1,
  s_Product: 1
});
$c_sci_$colon$colon.prototype.$classData = $d_sci_$colon$colon;
/** @constructor */
function $c_sci_Nil$() {
  this.sci_Nil$__f_EmptyUnzip = null;
  $n_sci_Nil$ = this;
  this.sci_Nil$__f_EmptyUnzip = new $c_T2($m_sci_Nil$(), $m_sci_Nil$())
}
$c_sci_Nil$.prototype = new $h_sci_List();
$c_sci_Nil$.prototype.constructor = $c_sci_Nil$;
/** @constructor */
function $h_sci_Nil$() {
  /*<skip>*/
}
$h_sci_Nil$.prototype = $c_sci_Nil$.prototype;
$c_sci_Nil$.prototype.head__E = (function() {
  throw $ct_ju_NoSuchElementException__T__(new $c_ju_NoSuchElementException(), "head of empty list")
});
$c_sci_Nil$.prototype.tail__E = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "tail of empty list")
});
$c_sci_Nil$.prototype.knownSize__I = (function() {
  return 0
});
$c_sci_Nil$.prototype.iterator__sc_Iterator = (function() {
  return $m_sc_Iterator$().sc_Iterator$__f_scala$collection$Iterator$$_empty
});
$c_sci_Nil$.prototype.productPrefix__T = (function() {
  return "Nil"
});
$c_sci_Nil$.prototype.productArity__I = (function() {
  return 0
});
$c_sci_Nil$.prototype.productElement__I__O = (function(x$1) {
  return $m_sr_Statics$().ioobe__I__O(x$1)
});
$c_sci_Nil$.prototype.productIterator__sc_Iterator = (function() {
  return new $c_sr_ScalaRunTime$$anon$1(this)
});
$c_sci_Nil$.prototype.tail__O = (function() {
  this.tail__E()
});
$c_sci_Nil$.prototype.head__O = (function() {
  this.head__E()
});
var $d_sci_Nil$ = new $TypeData().initClass({
  sci_Nil$: 0
}, false, "scala.collection.immutable.Nil$", {
  sci_Nil$: 1,
  sci_List: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_LinearSeq: 1,
  sc_LinearSeq: 1,
  sc_LinearSeqOps: 1,
  sci_LinearSeqOps: 1,
  sc_StrictOptimizedLinearSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1,
  s_Product: 1
});
$c_sci_Nil$.prototype.$classData = $d_sci_Nil$;
var $n_sci_Nil$;
function $m_sci_Nil$() {
  if ((!$n_sci_Nil$)) {
    $n_sci_Nil$ = new $c_sci_Nil$()
  };
  return $n_sci_Nil$
}
/** @constructor */
function $c_sci_Vector0$() {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  $ct_sci_BigVector__AO__AO__I__(this, $m_sci_VectorStatics$().sci_VectorStatics$__f_empty1, $m_sci_VectorStatics$().sci_VectorStatics$__f_empty1, 0)
}
$c_sci_Vector0$.prototype = new $h_sci_BigVector();
$c_sci_Vector0$.prototype.constructor = $c_sci_Vector0$;
/** @constructor */
function $h_sci_Vector0$() {
  /*<skip>*/
}
$h_sci_Vector0$.prototype = $c_sci_Vector0$.prototype;
$c_sci_Vector0$.prototype.apply__I__E = (function(index) {
  throw this.ioob__I__jl_IndexOutOfBoundsException(index)
});
$c_sci_Vector0$.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  throw this.ioob__I__jl_IndexOutOfBoundsException(index)
});
$c_sci_Vector0$.prototype.appended__O__sci_Vector = (function(elem) {
  var a = new $ac_O(1);
  a.set(0, elem);
  return new $c_sci_Vector1(a)
});
$c_sci_Vector0$.prototype.tail__sci_Vector = (function() {
  throw $ct_jl_UnsupportedOperationException__T__(new $c_jl_UnsupportedOperationException(), "empty.tail")
});
$c_sci_Vector0$.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  return this
});
$c_sci_Vector0$.prototype.vectorSliceCount__I = (function() {
  return 0
});
$c_sci_Vector0$.prototype.vectorSlice__I__AO = (function(idx) {
  return null
});
$c_sci_Vector0$.prototype.equals__O__Z = (function(o) {
  return ((this === o) || ((!(o instanceof $c_sci_Vector)) && $f_sc_Seq__equals__O__Z(this, o)))
});
$c_sci_Vector0$.prototype.ioob__I__jl_IndexOutOfBoundsException = (function(index) {
  return $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (index + " is out of bounds (empty vector)"))
});
$c_sci_Vector0$.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector0$.prototype.apply__O__O = (function(v1) {
  this.apply__I__E($uI(v1))
});
$c_sci_Vector0$.prototype.apply__I__O = (function(i) {
  this.apply__I__E(i)
});
var $d_sci_Vector0$ = new $TypeData().initClass({
  sci_Vector0$: 0
}, false, "scala.collection.immutable.Vector0$", {
  sci_Vector0$: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector0$.prototype.$classData = $d_sci_Vector0$;
var $n_sci_Vector0$;
function $m_sci_Vector0$() {
  if ((!$n_sci_Vector0$)) {
    $n_sci_Vector0$ = new $c_sci_Vector0$()
  };
  return $n_sci_Vector0$
}
/** @constructor */
function $c_sci_Vector2(_prefix1, len1, data2, _suffix1, _length0) {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  this.sci_Vector2__f_len1 = 0;
  this.sci_Vector2__f_data2 = null;
  this.sci_Vector2__f_len1 = len1;
  this.sci_Vector2__f_data2 = data2;
  $ct_sci_BigVector__AO__AO__I__(this, _prefix1, _suffix1, _length0)
}
$c_sci_Vector2.prototype = new $h_sci_BigVector();
$c_sci_Vector2.prototype.constructor = $c_sci_Vector2;
/** @constructor */
function $h_sci_Vector2() {
  /*<skip>*/
}
$h_sci_Vector2.prototype = $c_sci_Vector2.prototype;
$c_sci_Vector2.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector2__f_len1) | 0);
    if ((io >= 0)) {
      var i2 = ((io >>> 5) | 0);
      var i1 = (31 & io);
      return ((i2 < this.sci_Vector2__f_data2.u.length) ? this.sci_Vector2__f_data2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get((31 & io)))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector2.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    if ((index >= this.sci_Vector2__f_len1)) {
      var io = ((index - this.sci_Vector2__f_len1) | 0);
      var i2 = ((io >>> 5) | 0);
      var i1 = (31 & io);
      if ((i2 < this.sci_Vector2__f_data2.u.length)) {
        var a2 = this.sci_Vector2__f_data2;
        var a2c = a2.clone__O();
        var a1 = a2c.get(i2);
        var a1c = a1.clone__O();
        a1c.set(i1, elem);
        a2c.set(i2, a1c);
        var x$2 = this.sci_Vector__f_prefix1;
        var x$3 = this.sci_Vector2__f_len1;
        var x$4 = this.sci_BigVector__f_suffix1;
        var x$5 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector2(x$2, x$3, a2c, x$4, x$5)
      } else {
        var a1$1 = this.sci_BigVector__f_suffix1;
        var a1c$1 = a1$1.clone__O();
        a1c$1.set(i1, elem);
        var x$7 = this.sci_Vector__f_prefix1;
        var x$8 = this.sci_Vector2__f_len1;
        var x$9 = this.sci_Vector2__f_data2;
        var x$10 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector2(x$7, x$8, x$9, a1c$1, x$10)
      }
    } else {
      var a1$2 = this.sci_Vector__f_prefix1;
      var a1c$2 = a1$2.clone__O();
      a1c$2.set(index, elem);
      var len1 = this.sci_Vector2__f_len1;
      var data2 = this.sci_Vector2__f_data2;
      var suffix1 = this.sci_BigVector__f_suffix1;
      var length0 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector2(a1c$2, len1, data2, suffix1, length0)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector2.prototype.appended__O__sci_Vector = (function(elem) {
  if ((this.sci_BigVector__f_suffix1.u.length < 32)) {
    var x$1 = $m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_BigVector__f_suffix1, elem);
    var x$2 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$3 = this.sci_Vector__f_prefix1;
    var x$4 = this.sci_Vector2__f_len1;
    var x$5 = this.sci_Vector2__f_data2;
    return new $c_sci_Vector2(x$3, x$4, x$5, x$1, x$2)
  } else if ((this.sci_Vector2__f_data2.u.length < 30)) {
    var x$6 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector2__f_data2, this.sci_BigVector__f_suffix1), 2);
    var a = new $ac_O(1);
    a.set(0, elem);
    var x$8 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$9 = this.sci_Vector__f_prefix1;
    var x$10 = this.sci_Vector2__f_len1;
    return new $c_sci_Vector2(x$9, x$10, x$6, a, x$8)
  } else {
    var $$x5 = this.sci_Vector__f_prefix1;
    var $$x4 = this.sci_Vector2__f_len1;
    var $$x3 = this.sci_Vector2__f_data2;
    var $$x2 = this.sci_Vector2__f_len1;
    var $$x1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x = this.sci_BigVector__f_suffix1;
    var a$1 = new ($d_O.getArrayOf().getArrayOf().constr)(1);
    a$1.set(0, x);
    var a$2 = new $ac_O(1);
    a$2.set(0, elem);
    return new $c_sci_Vector3($$x5, $$x4, $$x3, ((960 + $$x2) | 0), $$x1, a$1, a$2, ((1 + this.sci_BigVector__f_length0) | 0))
  }
});
$c_sci_Vector2.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var b = new $c_sci_VectorSliceBuilder(lo, hi);
  b.consider__I__AO__V(1, this.sci_Vector__f_prefix1);
  b.consider__I__AO__V(2, this.sci_Vector2__f_data2);
  b.consider__I__AO__V(1, this.sci_BigVector__f_suffix1);
  return b.result__sci_Vector()
});
$c_sci_Vector2.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector2__f_len1 > 1)) {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    var x$1 = $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to);
    var x$2 = (((-1) + this.sci_Vector2__f_len1) | 0);
    var x$3 = (((-1) + this.sci_BigVector__f_length0) | 0);
    var x$4 = this.sci_Vector2__f_data2;
    var x$5 = this.sci_BigVector__f_suffix1;
    return new $c_sci_Vector2(x$1, x$2, x$4, x$5, x$3)
  } else {
    return this.slice0__I__I__sci_Vector(1, this.sci_BigVector__f_length0)
  }
});
$c_sci_Vector2.prototype.vectorSliceCount__I = (function() {
  return 3
});
$c_sci_Vector2.prototype.vectorSlice__I__AO = (function(idx) {
  switch (idx) {
    case 0: {
      return this.sci_Vector__f_prefix1;
      break
    }
    case 1: {
      return this.sci_Vector2__f_data2;
      break
    }
    case 2: {
      return this.sci_BigVector__f_suffix1;
      break
    }
    default: {
      throw new $c_s_MatchError(idx)
    }
  }
});
$c_sci_Vector2.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector2.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector2__f_len1) | 0);
    if ((io >= 0)) {
      var i2 = ((io >>> 5) | 0);
      var i1 = (31 & io);
      return ((i2 < this.sci_Vector2__f_data2.u.length) ? this.sci_Vector2__f_data2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get((31 & io)))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector2(obj) {
  return (((obj instanceof $c_sci_Vector2) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector2"))
}
function $isArrayOf_sci_Vector2(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector2)))
}
function $asArrayOf_sci_Vector2(obj, depth) {
  return (($isArrayOf_sci_Vector2(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector2;", depth))
}
var $d_sci_Vector2 = new $TypeData().initClass({
  sci_Vector2: 0
}, false, "scala.collection.immutable.Vector2", {
  sci_Vector2: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector2.prototype.$classData = $d_sci_Vector2;
/** @constructor */
function $c_sci_Vector3(_prefix1, len1, prefix2, len12, data3, suffix2, _suffix1, _length0) {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  this.sci_Vector3__f_len1 = 0;
  this.sci_Vector3__f_prefix2 = null;
  this.sci_Vector3__f_len12 = 0;
  this.sci_Vector3__f_data3 = null;
  this.sci_Vector3__f_suffix2 = null;
  this.sci_Vector3__f_len1 = len1;
  this.sci_Vector3__f_prefix2 = prefix2;
  this.sci_Vector3__f_len12 = len12;
  this.sci_Vector3__f_data3 = data3;
  this.sci_Vector3__f_suffix2 = suffix2;
  $ct_sci_BigVector__AO__AO__I__(this, _prefix1, _suffix1, _length0)
}
$c_sci_Vector3.prototype = new $h_sci_BigVector();
$c_sci_Vector3.prototype.constructor = $c_sci_Vector3;
/** @constructor */
function $h_sci_Vector3() {
  /*<skip>*/
}
$h_sci_Vector3.prototype = $c_sci_Vector3.prototype;
$c_sci_Vector3.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector3__f_len12) | 0);
    if ((io >= 0)) {
      var i3 = ((io >>> 10) | 0);
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i3 < this.sci_Vector3__f_data3.u.length) ? this.sci_Vector3__f_data3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector3__f_suffix2.u.length) ? this.sci_Vector3__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1)))
    } else if ((index >= this.sci_Vector3__f_len1)) {
      var io$2 = ((index - this.sci_Vector3__f_len1) | 0);
      return this.sci_Vector3__f_prefix2.get(((io$2 >>> 5) | 0)).get((31 & io$2))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector3.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    if ((index >= this.sci_Vector3__f_len12)) {
      var io = ((index - this.sci_Vector3__f_len12) | 0);
      var i3 = ((io >>> 10) | 0);
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      if ((i3 < this.sci_Vector3__f_data3.u.length)) {
        var a3 = this.sci_Vector3__f_data3;
        var a3c = a3.clone__O();
        var a2 = a3c.get(i3);
        var a2c = a2.clone__O();
        var a1 = a2c.get(i2);
        var a1c = a1.clone__O();
        a1c.set(i1, elem);
        a2c.set(i2, a1c);
        a3c.set(i3, a2c);
        var x$2 = this.sci_Vector__f_prefix1;
        var x$3 = this.sci_Vector3__f_len1;
        var x$4 = this.sci_Vector3__f_prefix2;
        var x$5 = this.sci_Vector3__f_len12;
        var x$6 = this.sci_Vector3__f_suffix2;
        var x$7 = this.sci_BigVector__f_suffix1;
        var x$8 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector3(x$2, x$3, x$4, x$5, a3c, x$6, x$7, x$8)
      } else if ((i2 < this.sci_Vector3__f_suffix2.u.length)) {
        var a2$1 = this.sci_Vector3__f_suffix2;
        var a2c$1 = a2$1.clone__O();
        var a1$1 = a2c$1.get(i2);
        var a1c$1 = a1$1.clone__O();
        a1c$1.set(i1, elem);
        a2c$1.set(i2, a1c$1);
        var x$10 = this.sci_Vector__f_prefix1;
        var x$11 = this.sci_Vector3__f_len1;
        var x$12 = this.sci_Vector3__f_prefix2;
        var x$13 = this.sci_Vector3__f_len12;
        var x$14 = this.sci_Vector3__f_data3;
        var x$15 = this.sci_BigVector__f_suffix1;
        var x$16 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector3(x$10, x$11, x$12, x$13, x$14, a2c$1, x$15, x$16)
      } else {
        var a1$2 = this.sci_BigVector__f_suffix1;
        var a1c$2 = a1$2.clone__O();
        a1c$2.set(i1, elem);
        var x$18 = this.sci_Vector__f_prefix1;
        var x$19 = this.sci_Vector3__f_len1;
        var x$20 = this.sci_Vector3__f_prefix2;
        var x$21 = this.sci_Vector3__f_len12;
        var x$22 = this.sci_Vector3__f_data3;
        var x$23 = this.sci_Vector3__f_suffix2;
        var x$24 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector3(x$18, x$19, x$20, x$21, x$22, x$23, a1c$2, x$24)
      }
    } else if ((index >= this.sci_Vector3__f_len1)) {
      var io$2 = ((index - this.sci_Vector3__f_len1) | 0);
      var a2$2 = this.sci_Vector3__f_prefix2;
      var idx2 = ((io$2 >>> 5) | 0);
      var idx1 = (31 & io$2);
      var a2c$2 = a2$2.clone__O();
      var a1$3 = a2c$2.get(idx2);
      var a1c$3 = a1$3.clone__O();
      a1c$3.set(idx1, elem);
      a2c$2.set(idx2, a1c$3);
      var x$26 = this.sci_Vector__f_prefix1;
      var x$27 = this.sci_Vector3__f_len1;
      var x$28 = this.sci_Vector3__f_len12;
      var x$29 = this.sci_Vector3__f_data3;
      var x$30 = this.sci_Vector3__f_suffix2;
      var x$31 = this.sci_BigVector__f_suffix1;
      var x$32 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector3(x$26, x$27, a2c$2, x$28, x$29, x$30, x$31, x$32)
    } else {
      var a1$4 = this.sci_Vector__f_prefix1;
      var a1c$4 = a1$4.clone__O();
      a1c$4.set(index, elem);
      var len1 = this.sci_Vector3__f_len1;
      var prefix2 = this.sci_Vector3__f_prefix2;
      var len12 = this.sci_Vector3__f_len12;
      var data3 = this.sci_Vector3__f_data3;
      var suffix2 = this.sci_Vector3__f_suffix2;
      var suffix1 = this.sci_BigVector__f_suffix1;
      var length0 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector3(a1c$4, len1, prefix2, len12, data3, suffix2, suffix1, length0)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector3.prototype.appended__O__sci_Vector = (function(elem) {
  if ((this.sci_BigVector__f_suffix1.u.length < 32)) {
    var x$1 = $m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_BigVector__f_suffix1, elem);
    var x$2 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$3 = this.sci_Vector__f_prefix1;
    var x$4 = this.sci_Vector3__f_len1;
    var x$5 = this.sci_Vector3__f_prefix2;
    var x$6 = this.sci_Vector3__f_len12;
    var x$7 = this.sci_Vector3__f_data3;
    var x$8 = this.sci_Vector3__f_suffix2;
    return new $c_sci_Vector3(x$3, x$4, x$5, x$6, x$7, x$8, x$1, x$2)
  } else if ((this.sci_Vector3__f_suffix2.u.length < 31)) {
    var x$9 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector3__f_suffix2, this.sci_BigVector__f_suffix1), 2);
    var a = new $ac_O(1);
    a.set(0, elem);
    var x$11 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$12 = this.sci_Vector__f_prefix1;
    var x$13 = this.sci_Vector3__f_len1;
    var x$14 = this.sci_Vector3__f_prefix2;
    var x$15 = this.sci_Vector3__f_len12;
    var x$16 = this.sci_Vector3__f_data3;
    return new $c_sci_Vector3(x$12, x$13, x$14, x$15, x$16, x$9, a, x$11)
  } else if ((this.sci_Vector3__f_data3.u.length < 30)) {
    var x$17 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector3__f_data3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector3__f_suffix2, this.sci_BigVector__f_suffix1)), 3);
    var x$18 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$1 = new $ac_O(1);
    a$1.set(0, elem);
    var x$20 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$21 = this.sci_Vector__f_prefix1;
    var x$22 = this.sci_Vector3__f_len1;
    var x$23 = this.sci_Vector3__f_prefix2;
    var x$24 = this.sci_Vector3__f_len12;
    return new $c_sci_Vector3(x$21, x$22, x$23, x$24, x$17, x$18, a$1, x$20)
  } else {
    var $$x8 = this.sci_Vector__f_prefix1;
    var $$x7 = this.sci_Vector3__f_len1;
    var $$x6 = this.sci_Vector3__f_prefix2;
    var $$x5 = this.sci_Vector3__f_len12;
    var $$x4 = this.sci_Vector3__f_data3;
    var $$x3 = this.sci_Vector3__f_len12;
    var $$x2 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
    var x = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector3__f_suffix2, this.sci_BigVector__f_suffix1), 2);
    var a$2 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().constr)(1);
    a$2.set(0, x);
    var $$x1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$3 = new $ac_O(1);
    a$3.set(0, elem);
    return new $c_sci_Vector4($$x8, $$x7, $$x6, $$x5, $$x4, ((30720 + $$x3) | 0), $$x2, a$2, $$x1, a$3, ((1 + this.sci_BigVector__f_length0) | 0))
  }
});
$c_sci_Vector3.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var b = new $c_sci_VectorSliceBuilder(lo, hi);
  b.consider__I__AO__V(1, this.sci_Vector__f_prefix1);
  b.consider__I__AO__V(2, this.sci_Vector3__f_prefix2);
  b.consider__I__AO__V(3, this.sci_Vector3__f_data3);
  b.consider__I__AO__V(2, this.sci_Vector3__f_suffix2);
  b.consider__I__AO__V(1, this.sci_BigVector__f_suffix1);
  return b.result__sci_Vector()
});
$c_sci_Vector3.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector3__f_len1 > 1)) {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    var x$1 = $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to);
    var x$2 = (((-1) + this.sci_Vector3__f_len1) | 0);
    var x$3 = (((-1) + this.sci_Vector3__f_len12) | 0);
    var x$4 = (((-1) + this.sci_BigVector__f_length0) | 0);
    var x$5 = this.sci_Vector3__f_prefix2;
    var x$6 = this.sci_Vector3__f_data3;
    var x$7 = this.sci_Vector3__f_suffix2;
    var x$8 = this.sci_BigVector__f_suffix1;
    return new $c_sci_Vector3(x$1, x$2, x$5, x$3, x$6, x$7, x$8, x$4)
  } else {
    return this.slice0__I__I__sci_Vector(1, this.sci_BigVector__f_length0)
  }
});
$c_sci_Vector3.prototype.vectorSliceCount__I = (function() {
  return 5
});
$c_sci_Vector3.prototype.vectorSlice__I__AO = (function(idx) {
  switch (idx) {
    case 0: {
      return this.sci_Vector__f_prefix1;
      break
    }
    case 1: {
      return this.sci_Vector3__f_prefix2;
      break
    }
    case 2: {
      return this.sci_Vector3__f_data3;
      break
    }
    case 3: {
      return this.sci_Vector3__f_suffix2;
      break
    }
    case 4: {
      return this.sci_BigVector__f_suffix1;
      break
    }
    default: {
      throw new $c_s_MatchError(idx)
    }
  }
});
$c_sci_Vector3.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector3.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector3__f_len12) | 0);
    if ((io >= 0)) {
      var i3 = ((io >>> 10) | 0);
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i3 < this.sci_Vector3__f_data3.u.length) ? this.sci_Vector3__f_data3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector3__f_suffix2.u.length) ? this.sci_Vector3__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1)))
    } else if ((index >= this.sci_Vector3__f_len1)) {
      var io$2 = ((index - this.sci_Vector3__f_len1) | 0);
      return this.sci_Vector3__f_prefix2.get(((io$2 >>> 5) | 0)).get((31 & io$2))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector3(obj) {
  return (((obj instanceof $c_sci_Vector3) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector3"))
}
function $isArrayOf_sci_Vector3(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector3)))
}
function $asArrayOf_sci_Vector3(obj, depth) {
  return (($isArrayOf_sci_Vector3(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector3;", depth))
}
var $d_sci_Vector3 = new $TypeData().initClass({
  sci_Vector3: 0
}, false, "scala.collection.immutable.Vector3", {
  sci_Vector3: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector3.prototype.$classData = $d_sci_Vector3;
/** @constructor */
function $c_sci_Vector4(_prefix1, len1, prefix2, len12, prefix3, len123, data4, suffix3, suffix2, _suffix1, _length0) {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  this.sci_Vector4__f_len1 = 0;
  this.sci_Vector4__f_prefix2 = null;
  this.sci_Vector4__f_len12 = 0;
  this.sci_Vector4__f_prefix3 = null;
  this.sci_Vector4__f_len123 = 0;
  this.sci_Vector4__f_data4 = null;
  this.sci_Vector4__f_suffix3 = null;
  this.sci_Vector4__f_suffix2 = null;
  this.sci_Vector4__f_len1 = len1;
  this.sci_Vector4__f_prefix2 = prefix2;
  this.sci_Vector4__f_len12 = len12;
  this.sci_Vector4__f_prefix3 = prefix3;
  this.sci_Vector4__f_len123 = len123;
  this.sci_Vector4__f_data4 = data4;
  this.sci_Vector4__f_suffix3 = suffix3;
  this.sci_Vector4__f_suffix2 = suffix2;
  $ct_sci_BigVector__AO__AO__I__(this, _prefix1, _suffix1, _length0)
}
$c_sci_Vector4.prototype = new $h_sci_BigVector();
$c_sci_Vector4.prototype.constructor = $c_sci_Vector4;
/** @constructor */
function $h_sci_Vector4() {
  /*<skip>*/
}
$h_sci_Vector4.prototype = $c_sci_Vector4.prototype;
$c_sci_Vector4.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector4__f_len123) | 0);
    if ((io >= 0)) {
      var i4 = ((io >>> 15) | 0);
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i4 < this.sci_Vector4__f_data4.u.length) ? this.sci_Vector4__f_data4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector4__f_suffix3.u.length) ? this.sci_Vector4__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector4__f_suffix2.u.length) ? this.sci_Vector4__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1))))
    } else if ((index >= this.sci_Vector4__f_len12)) {
      var io$2 = ((index - this.sci_Vector4__f_len12) | 0);
      return this.sci_Vector4__f_prefix3.get(((io$2 >>> 10) | 0)).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector4__f_len1)) {
      var io$3 = ((index - this.sci_Vector4__f_len1) | 0);
      return this.sci_Vector4__f_prefix2.get(((io$3 >>> 5) | 0)).get((31 & io$3))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector4.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    if ((index >= this.sci_Vector4__f_len123)) {
      var io = ((index - this.sci_Vector4__f_len123) | 0);
      var i4 = ((io >>> 15) | 0);
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      if ((i4 < this.sci_Vector4__f_data4.u.length)) {
        var a4 = this.sci_Vector4__f_data4;
        var a4c = a4.clone__O();
        var a3 = a4c.get(i4);
        var a3c = a3.clone__O();
        var a2 = a3c.get(i3);
        var a2c = a2.clone__O();
        var a1 = a2c.get(i2);
        var a1c = a1.clone__O();
        a1c.set(i1, elem);
        a2c.set(i2, a1c);
        a3c.set(i3, a2c);
        a4c.set(i4, a3c);
        var x$2 = this.sci_Vector__f_prefix1;
        var x$3 = this.sci_Vector4__f_len1;
        var x$4 = this.sci_Vector4__f_prefix2;
        var x$5 = this.sci_Vector4__f_len12;
        var x$6 = this.sci_Vector4__f_prefix3;
        var x$7 = this.sci_Vector4__f_len123;
        var x$8 = this.sci_Vector4__f_suffix3;
        var x$9 = this.sci_Vector4__f_suffix2;
        var x$10 = this.sci_BigVector__f_suffix1;
        var x$11 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector4(x$2, x$3, x$4, x$5, x$6, x$7, a4c, x$8, x$9, x$10, x$11)
      } else if ((i3 < this.sci_Vector4__f_suffix3.u.length)) {
        var a3$1 = this.sci_Vector4__f_suffix3;
        var a3c$1 = a3$1.clone__O();
        var a2$1 = a3c$1.get(i3);
        var a2c$1 = a2$1.clone__O();
        var a1$1 = a2c$1.get(i2);
        var a1c$1 = a1$1.clone__O();
        a1c$1.set(i1, elem);
        a2c$1.set(i2, a1c$1);
        a3c$1.set(i3, a2c$1);
        var x$13 = this.sci_Vector__f_prefix1;
        var x$14 = this.sci_Vector4__f_len1;
        var x$15 = this.sci_Vector4__f_prefix2;
        var x$16 = this.sci_Vector4__f_len12;
        var x$17 = this.sci_Vector4__f_prefix3;
        var x$18 = this.sci_Vector4__f_len123;
        var x$19 = this.sci_Vector4__f_data4;
        var x$20 = this.sci_Vector4__f_suffix2;
        var x$21 = this.sci_BigVector__f_suffix1;
        var x$22 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector4(x$13, x$14, x$15, x$16, x$17, x$18, x$19, a3c$1, x$20, x$21, x$22)
      } else if ((i2 < this.sci_Vector4__f_suffix2.u.length)) {
        var a2$2 = this.sci_Vector4__f_suffix2;
        var a2c$2 = a2$2.clone__O();
        var a1$2 = a2c$2.get(i2);
        var a1c$2 = a1$2.clone__O();
        a1c$2.set(i1, elem);
        a2c$2.set(i2, a1c$2);
        var x$24 = this.sci_Vector__f_prefix1;
        var x$25 = this.sci_Vector4__f_len1;
        var x$26 = this.sci_Vector4__f_prefix2;
        var x$27 = this.sci_Vector4__f_len12;
        var x$28 = this.sci_Vector4__f_prefix3;
        var x$29 = this.sci_Vector4__f_len123;
        var x$30 = this.sci_Vector4__f_data4;
        var x$31 = this.sci_Vector4__f_suffix3;
        var x$32 = this.sci_BigVector__f_suffix1;
        var x$33 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector4(x$24, x$25, x$26, x$27, x$28, x$29, x$30, x$31, a2c$2, x$32, x$33)
      } else {
        var a1$3 = this.sci_BigVector__f_suffix1;
        var a1c$3 = a1$3.clone__O();
        a1c$3.set(i1, elem);
        var x$35 = this.sci_Vector__f_prefix1;
        var x$36 = this.sci_Vector4__f_len1;
        var x$37 = this.sci_Vector4__f_prefix2;
        var x$38 = this.sci_Vector4__f_len12;
        var x$39 = this.sci_Vector4__f_prefix3;
        var x$40 = this.sci_Vector4__f_len123;
        var x$41 = this.sci_Vector4__f_data4;
        var x$42 = this.sci_Vector4__f_suffix3;
        var x$43 = this.sci_Vector4__f_suffix2;
        var x$44 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector4(x$35, x$36, x$37, x$38, x$39, x$40, x$41, x$42, x$43, a1c$3, x$44)
      }
    } else if ((index >= this.sci_Vector4__f_len12)) {
      var io$2 = ((index - this.sci_Vector4__f_len12) | 0);
      var a3$2 = this.sci_Vector4__f_prefix3;
      var idx3 = ((io$2 >>> 10) | 0);
      var idx2 = (31 & ((io$2 >>> 5) | 0));
      var idx1 = (31 & io$2);
      var a3c$2 = a3$2.clone__O();
      var a2$3 = a3c$2.get(idx3);
      var a2c$3 = a2$3.clone__O();
      var a1$4 = a2c$3.get(idx2);
      var a1c$4 = a1$4.clone__O();
      a1c$4.set(idx1, elem);
      a2c$3.set(idx2, a1c$4);
      a3c$2.set(idx3, a2c$3);
      var x$46 = this.sci_Vector__f_prefix1;
      var x$47 = this.sci_Vector4__f_len1;
      var x$48 = this.sci_Vector4__f_prefix2;
      var x$49 = this.sci_Vector4__f_len12;
      var x$50 = this.sci_Vector4__f_len123;
      var x$51 = this.sci_Vector4__f_data4;
      var x$52 = this.sci_Vector4__f_suffix3;
      var x$53 = this.sci_Vector4__f_suffix2;
      var x$54 = this.sci_BigVector__f_suffix1;
      var x$55 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector4(x$46, x$47, x$48, x$49, a3c$2, x$50, x$51, x$52, x$53, x$54, x$55)
    } else if ((index >= this.sci_Vector4__f_len1)) {
      var io$3 = ((index - this.sci_Vector4__f_len1) | 0);
      var a2$4 = this.sci_Vector4__f_prefix2;
      var idx2$1 = ((io$3 >>> 5) | 0);
      var idx1$1 = (31 & io$3);
      var a2c$4 = a2$4.clone__O();
      var a1$5 = a2c$4.get(idx2$1);
      var a1c$5 = a1$5.clone__O();
      a1c$5.set(idx1$1, elem);
      a2c$4.set(idx2$1, a1c$5);
      var x$57 = this.sci_Vector__f_prefix1;
      var x$58 = this.sci_Vector4__f_len1;
      var x$59 = this.sci_Vector4__f_len12;
      var x$60 = this.sci_Vector4__f_prefix3;
      var x$61 = this.sci_Vector4__f_len123;
      var x$62 = this.sci_Vector4__f_data4;
      var x$63 = this.sci_Vector4__f_suffix3;
      var x$64 = this.sci_Vector4__f_suffix2;
      var x$65 = this.sci_BigVector__f_suffix1;
      var x$66 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector4(x$57, x$58, a2c$4, x$59, x$60, x$61, x$62, x$63, x$64, x$65, x$66)
    } else {
      var a1$6 = this.sci_Vector__f_prefix1;
      var a1c$6 = a1$6.clone__O();
      a1c$6.set(index, elem);
      var len1 = this.sci_Vector4__f_len1;
      var prefix2 = this.sci_Vector4__f_prefix2;
      var len12 = this.sci_Vector4__f_len12;
      var prefix3 = this.sci_Vector4__f_prefix3;
      var len123 = this.sci_Vector4__f_len123;
      var data4 = this.sci_Vector4__f_data4;
      var suffix3 = this.sci_Vector4__f_suffix3;
      var suffix2 = this.sci_Vector4__f_suffix2;
      var suffix1 = this.sci_BigVector__f_suffix1;
      var length0 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector4(a1c$6, len1, prefix2, len12, prefix3, len123, data4, suffix3, suffix2, suffix1, length0)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector4.prototype.appended__O__sci_Vector = (function(elem) {
  if ((this.sci_BigVector__f_suffix1.u.length < 32)) {
    var x$1 = $m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_BigVector__f_suffix1, elem);
    var x$2 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$3 = this.sci_Vector__f_prefix1;
    var x$4 = this.sci_Vector4__f_len1;
    var x$5 = this.sci_Vector4__f_prefix2;
    var x$6 = this.sci_Vector4__f_len12;
    var x$7 = this.sci_Vector4__f_prefix3;
    var x$8 = this.sci_Vector4__f_len123;
    var x$9 = this.sci_Vector4__f_data4;
    var x$10 = this.sci_Vector4__f_suffix3;
    var x$11 = this.sci_Vector4__f_suffix2;
    return new $c_sci_Vector4(x$3, x$4, x$5, x$6, x$7, x$8, x$9, x$10, x$11, x$1, x$2)
  } else if ((this.sci_Vector4__f_suffix2.u.length < 31)) {
    var x$12 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix2, this.sci_BigVector__f_suffix1), 2);
    var a = new $ac_O(1);
    a.set(0, elem);
    var x$14 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$15 = this.sci_Vector__f_prefix1;
    var x$16 = this.sci_Vector4__f_len1;
    var x$17 = this.sci_Vector4__f_prefix2;
    var x$18 = this.sci_Vector4__f_len12;
    var x$19 = this.sci_Vector4__f_prefix3;
    var x$20 = this.sci_Vector4__f_len123;
    var x$21 = this.sci_Vector4__f_data4;
    var x$22 = this.sci_Vector4__f_suffix3;
    return new $c_sci_Vector4(x$15, x$16, x$17, x$18, x$19, x$20, x$21, x$22, x$12, a, x$14)
  } else if ((this.sci_Vector4__f_suffix3.u.length < 31)) {
    var x$23 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix2, this.sci_BigVector__f_suffix1)), 3);
    var x$24 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$1 = new $ac_O(1);
    a$1.set(0, elem);
    var x$26 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$27 = this.sci_Vector__f_prefix1;
    var x$28 = this.sci_Vector4__f_len1;
    var x$29 = this.sci_Vector4__f_prefix2;
    var x$30 = this.sci_Vector4__f_len12;
    var x$31 = this.sci_Vector4__f_prefix3;
    var x$32 = this.sci_Vector4__f_len123;
    var x$33 = this.sci_Vector4__f_data4;
    return new $c_sci_Vector4(x$27, x$28, x$29, x$30, x$31, x$32, x$33, x$23, x$24, a$1, x$26)
  } else if ((this.sci_Vector4__f_data4.u.length < 30)) {
    var x$34 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_data4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix2, this.sci_BigVector__f_suffix1))), 4);
    var x$35 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$36 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$2 = new $ac_O(1);
    a$2.set(0, elem);
    var x$38 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$39 = this.sci_Vector__f_prefix1;
    var x$40 = this.sci_Vector4__f_len1;
    var x$41 = this.sci_Vector4__f_prefix2;
    var x$42 = this.sci_Vector4__f_len12;
    var x$43 = this.sci_Vector4__f_prefix3;
    var x$44 = this.sci_Vector4__f_len123;
    return new $c_sci_Vector4(x$39, x$40, x$41, x$42, x$43, x$44, x$34, x$35, x$36, a$2, x$38)
  } else {
    var $$x11 = this.sci_Vector__f_prefix1;
    var $$x10 = this.sci_Vector4__f_len1;
    var $$x9 = this.sci_Vector4__f_prefix2;
    var $$x8 = this.sci_Vector4__f_len12;
    var $$x7 = this.sci_Vector4__f_prefix3;
    var $$x6 = this.sci_Vector4__f_len123;
    var $$x5 = this.sci_Vector4__f_data4;
    var $$x4 = this.sci_Vector4__f_len123;
    var $$x3 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty5;
    var x = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector4__f_suffix2, this.sci_BigVector__f_suffix1)), 3);
    var a$3 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(1);
    a$3.set(0, x);
    var $$x2 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var $$x1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$4 = new $ac_O(1);
    a$4.set(0, elem);
    return new $c_sci_Vector5($$x11, $$x10, $$x9, $$x8, $$x7, $$x6, $$x5, ((983040 + $$x4) | 0), $$x3, a$3, $$x2, $$x1, a$4, ((1 + this.sci_BigVector__f_length0) | 0))
  }
});
$c_sci_Vector4.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var b = new $c_sci_VectorSliceBuilder(lo, hi);
  b.consider__I__AO__V(1, this.sci_Vector__f_prefix1);
  b.consider__I__AO__V(2, this.sci_Vector4__f_prefix2);
  b.consider__I__AO__V(3, this.sci_Vector4__f_prefix3);
  b.consider__I__AO__V(4, this.sci_Vector4__f_data4);
  b.consider__I__AO__V(3, this.sci_Vector4__f_suffix3);
  b.consider__I__AO__V(2, this.sci_Vector4__f_suffix2);
  b.consider__I__AO__V(1, this.sci_BigVector__f_suffix1);
  return b.result__sci_Vector()
});
$c_sci_Vector4.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector4__f_len1 > 1)) {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    var x$1 = $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to);
    var x$2 = (((-1) + this.sci_Vector4__f_len1) | 0);
    var x$3 = (((-1) + this.sci_Vector4__f_len12) | 0);
    var x$4 = (((-1) + this.sci_Vector4__f_len123) | 0);
    var x$5 = (((-1) + this.sci_BigVector__f_length0) | 0);
    var x$6 = this.sci_Vector4__f_prefix2;
    var x$7 = this.sci_Vector4__f_prefix3;
    var x$8 = this.sci_Vector4__f_data4;
    var x$9 = this.sci_Vector4__f_suffix3;
    var x$10 = this.sci_Vector4__f_suffix2;
    var x$11 = this.sci_BigVector__f_suffix1;
    return new $c_sci_Vector4(x$1, x$2, x$6, x$3, x$7, x$4, x$8, x$9, x$10, x$11, x$5)
  } else {
    return this.slice0__I__I__sci_Vector(1, this.sci_BigVector__f_length0)
  }
});
$c_sci_Vector4.prototype.vectorSliceCount__I = (function() {
  return 7
});
$c_sci_Vector4.prototype.vectorSlice__I__AO = (function(idx) {
  switch (idx) {
    case 0: {
      return this.sci_Vector__f_prefix1;
      break
    }
    case 1: {
      return this.sci_Vector4__f_prefix2;
      break
    }
    case 2: {
      return this.sci_Vector4__f_prefix3;
      break
    }
    case 3: {
      return this.sci_Vector4__f_data4;
      break
    }
    case 4: {
      return this.sci_Vector4__f_suffix3;
      break
    }
    case 5: {
      return this.sci_Vector4__f_suffix2;
      break
    }
    case 6: {
      return this.sci_BigVector__f_suffix1;
      break
    }
    default: {
      throw new $c_s_MatchError(idx)
    }
  }
});
$c_sci_Vector4.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector4.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector4__f_len123) | 0);
    if ((io >= 0)) {
      var i4 = ((io >>> 15) | 0);
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i4 < this.sci_Vector4__f_data4.u.length) ? this.sci_Vector4__f_data4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector4__f_suffix3.u.length) ? this.sci_Vector4__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector4__f_suffix2.u.length) ? this.sci_Vector4__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1))))
    } else if ((index >= this.sci_Vector4__f_len12)) {
      var io$2 = ((index - this.sci_Vector4__f_len12) | 0);
      return this.sci_Vector4__f_prefix3.get(((io$2 >>> 10) | 0)).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector4__f_len1)) {
      var io$3 = ((index - this.sci_Vector4__f_len1) | 0);
      return this.sci_Vector4__f_prefix2.get(((io$3 >>> 5) | 0)).get((31 & io$3))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector4(obj) {
  return (((obj instanceof $c_sci_Vector4) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector4"))
}
function $isArrayOf_sci_Vector4(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector4)))
}
function $asArrayOf_sci_Vector4(obj, depth) {
  return (($isArrayOf_sci_Vector4(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector4;", depth))
}
var $d_sci_Vector4 = new $TypeData().initClass({
  sci_Vector4: 0
}, false, "scala.collection.immutable.Vector4", {
  sci_Vector4: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector4.prototype.$classData = $d_sci_Vector4;
/** @constructor */
function $c_sci_Vector5(_prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, data5, suffix4, suffix3, suffix2, _suffix1, _length0) {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  this.sci_Vector5__f_len1 = 0;
  this.sci_Vector5__f_prefix2 = null;
  this.sci_Vector5__f_len12 = 0;
  this.sci_Vector5__f_prefix3 = null;
  this.sci_Vector5__f_len123 = 0;
  this.sci_Vector5__f_prefix4 = null;
  this.sci_Vector5__f_len1234 = 0;
  this.sci_Vector5__f_data5 = null;
  this.sci_Vector5__f_suffix4 = null;
  this.sci_Vector5__f_suffix3 = null;
  this.sci_Vector5__f_suffix2 = null;
  this.sci_Vector5__f_len1 = len1;
  this.sci_Vector5__f_prefix2 = prefix2;
  this.sci_Vector5__f_len12 = len12;
  this.sci_Vector5__f_prefix3 = prefix3;
  this.sci_Vector5__f_len123 = len123;
  this.sci_Vector5__f_prefix4 = prefix4;
  this.sci_Vector5__f_len1234 = len1234;
  this.sci_Vector5__f_data5 = data5;
  this.sci_Vector5__f_suffix4 = suffix4;
  this.sci_Vector5__f_suffix3 = suffix3;
  this.sci_Vector5__f_suffix2 = suffix2;
  $ct_sci_BigVector__AO__AO__I__(this, _prefix1, _suffix1, _length0)
}
$c_sci_Vector5.prototype = new $h_sci_BigVector();
$c_sci_Vector5.prototype.constructor = $c_sci_Vector5;
/** @constructor */
function $h_sci_Vector5() {
  /*<skip>*/
}
$h_sci_Vector5.prototype = $c_sci_Vector5.prototype;
$c_sci_Vector5.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector5__f_len1234) | 0);
    if ((io >= 0)) {
      var i5 = ((io >>> 20) | 0);
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i5 < this.sci_Vector5__f_data5.u.length) ? this.sci_Vector5__f_data5.get(i5).get(i4).get(i3).get(i2).get(i1) : ((i4 < this.sci_Vector5__f_suffix4.u.length) ? this.sci_Vector5__f_suffix4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector5__f_suffix3.u.length) ? this.sci_Vector5__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector5__f_suffix2.u.length) ? this.sci_Vector5__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1)))))
    } else if ((index >= this.sci_Vector5__f_len123)) {
      var io$2 = ((index - this.sci_Vector5__f_len123) | 0);
      return this.sci_Vector5__f_prefix4.get(((io$2 >>> 15) | 0)).get((31 & ((io$2 >>> 10) | 0))).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector5__f_len12)) {
      var io$3 = ((index - this.sci_Vector5__f_len12) | 0);
      return this.sci_Vector5__f_prefix3.get(((io$3 >>> 10) | 0)).get((31 & ((io$3 >>> 5) | 0))).get((31 & io$3))
    } else if ((index >= this.sci_Vector5__f_len1)) {
      var io$4 = ((index - this.sci_Vector5__f_len1) | 0);
      return this.sci_Vector5__f_prefix2.get(((io$4 >>> 5) | 0)).get((31 & io$4))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector5.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    if ((index >= this.sci_Vector5__f_len1234)) {
      var io = ((index - this.sci_Vector5__f_len1234) | 0);
      var i5 = ((io >>> 20) | 0);
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      if ((i5 < this.sci_Vector5__f_data5.u.length)) {
        var a5 = this.sci_Vector5__f_data5;
        var a5c = a5.clone__O();
        var a4 = a5c.get(i5);
        var a4c = a4.clone__O();
        var a3 = a4c.get(i4);
        var a3c = a3.clone__O();
        var a2 = a3c.get(i3);
        var a2c = a2.clone__O();
        var a1 = a2c.get(i2);
        var a1c = a1.clone__O();
        a1c.set(i1, elem);
        a2c.set(i2, a1c);
        a3c.set(i3, a2c);
        a4c.set(i4, a3c);
        a5c.set(i5, a4c);
        var x$2 = this.sci_Vector__f_prefix1;
        var x$3 = this.sci_Vector5__f_len1;
        var x$4 = this.sci_Vector5__f_prefix2;
        var x$5 = this.sci_Vector5__f_len12;
        var x$6 = this.sci_Vector5__f_prefix3;
        var x$7 = this.sci_Vector5__f_len123;
        var x$8 = this.sci_Vector5__f_prefix4;
        var x$9 = this.sci_Vector5__f_len1234;
        var x$10 = this.sci_Vector5__f_suffix4;
        var x$11 = this.sci_Vector5__f_suffix3;
        var x$12 = this.sci_Vector5__f_suffix2;
        var x$13 = this.sci_BigVector__f_suffix1;
        var x$14 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector5(x$2, x$3, x$4, x$5, x$6, x$7, x$8, x$9, a5c, x$10, x$11, x$12, x$13, x$14)
      } else if ((i4 < this.sci_Vector5__f_suffix4.u.length)) {
        var a4$1 = this.sci_Vector5__f_suffix4;
        var a4c$1 = a4$1.clone__O();
        var a3$1 = a4c$1.get(i4);
        var a3c$1 = a3$1.clone__O();
        var a2$1 = a3c$1.get(i3);
        var a2c$1 = a2$1.clone__O();
        var a1$1 = a2c$1.get(i2);
        var a1c$1 = a1$1.clone__O();
        a1c$1.set(i1, elem);
        a2c$1.set(i2, a1c$1);
        a3c$1.set(i3, a2c$1);
        a4c$1.set(i4, a3c$1);
        var x$16 = this.sci_Vector__f_prefix1;
        var x$17 = this.sci_Vector5__f_len1;
        var x$18 = this.sci_Vector5__f_prefix2;
        var x$19 = this.sci_Vector5__f_len12;
        var x$20 = this.sci_Vector5__f_prefix3;
        var x$21 = this.sci_Vector5__f_len123;
        var x$22 = this.sci_Vector5__f_prefix4;
        var x$23 = this.sci_Vector5__f_len1234;
        var x$24 = this.sci_Vector5__f_data5;
        var x$25 = this.sci_Vector5__f_suffix3;
        var x$26 = this.sci_Vector5__f_suffix2;
        var x$27 = this.sci_BigVector__f_suffix1;
        var x$28 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector5(x$16, x$17, x$18, x$19, x$20, x$21, x$22, x$23, x$24, a4c$1, x$25, x$26, x$27, x$28)
      } else if ((i3 < this.sci_Vector5__f_suffix3.u.length)) {
        var a3$2 = this.sci_Vector5__f_suffix3;
        var a3c$2 = a3$2.clone__O();
        var a2$2 = a3c$2.get(i3);
        var a2c$2 = a2$2.clone__O();
        var a1$2 = a2c$2.get(i2);
        var a1c$2 = a1$2.clone__O();
        a1c$2.set(i1, elem);
        a2c$2.set(i2, a1c$2);
        a3c$2.set(i3, a2c$2);
        var x$30 = this.sci_Vector__f_prefix1;
        var x$31 = this.sci_Vector5__f_len1;
        var x$32 = this.sci_Vector5__f_prefix2;
        var x$33 = this.sci_Vector5__f_len12;
        var x$34 = this.sci_Vector5__f_prefix3;
        var x$35 = this.sci_Vector5__f_len123;
        var x$36 = this.sci_Vector5__f_prefix4;
        var x$37 = this.sci_Vector5__f_len1234;
        var x$38 = this.sci_Vector5__f_data5;
        var x$39 = this.sci_Vector5__f_suffix4;
        var x$40 = this.sci_Vector5__f_suffix2;
        var x$41 = this.sci_BigVector__f_suffix1;
        var x$42 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector5(x$30, x$31, x$32, x$33, x$34, x$35, x$36, x$37, x$38, x$39, a3c$2, x$40, x$41, x$42)
      } else if ((i2 < this.sci_Vector5__f_suffix2.u.length)) {
        var a2$3 = this.sci_Vector5__f_suffix2;
        var a2c$3 = a2$3.clone__O();
        var a1$3 = a2c$3.get(i2);
        var a1c$3 = a1$3.clone__O();
        a1c$3.set(i1, elem);
        a2c$3.set(i2, a1c$3);
        var x$44 = this.sci_Vector__f_prefix1;
        var x$45 = this.sci_Vector5__f_len1;
        var x$46 = this.sci_Vector5__f_prefix2;
        var x$47 = this.sci_Vector5__f_len12;
        var x$48 = this.sci_Vector5__f_prefix3;
        var x$49 = this.sci_Vector5__f_len123;
        var x$50 = this.sci_Vector5__f_prefix4;
        var x$51 = this.sci_Vector5__f_len1234;
        var x$52 = this.sci_Vector5__f_data5;
        var x$53 = this.sci_Vector5__f_suffix4;
        var x$54 = this.sci_Vector5__f_suffix3;
        var x$55 = this.sci_BigVector__f_suffix1;
        var x$56 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector5(x$44, x$45, x$46, x$47, x$48, x$49, x$50, x$51, x$52, x$53, x$54, a2c$3, x$55, x$56)
      } else {
        var a1$4 = this.sci_BigVector__f_suffix1;
        var a1c$4 = a1$4.clone__O();
        a1c$4.set(i1, elem);
        var x$58 = this.sci_Vector__f_prefix1;
        var x$59 = this.sci_Vector5__f_len1;
        var x$60 = this.sci_Vector5__f_prefix2;
        var x$61 = this.sci_Vector5__f_len12;
        var x$62 = this.sci_Vector5__f_prefix3;
        var x$63 = this.sci_Vector5__f_len123;
        var x$64 = this.sci_Vector5__f_prefix4;
        var x$65 = this.sci_Vector5__f_len1234;
        var x$66 = this.sci_Vector5__f_data5;
        var x$67 = this.sci_Vector5__f_suffix4;
        var x$68 = this.sci_Vector5__f_suffix3;
        var x$69 = this.sci_Vector5__f_suffix2;
        var x$70 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector5(x$58, x$59, x$60, x$61, x$62, x$63, x$64, x$65, x$66, x$67, x$68, x$69, a1c$4, x$70)
      }
    } else if ((index >= this.sci_Vector5__f_len123)) {
      var io$2 = ((index - this.sci_Vector5__f_len123) | 0);
      var a4$2 = this.sci_Vector5__f_prefix4;
      var idx4 = ((io$2 >>> 15) | 0);
      var idx3 = (31 & ((io$2 >>> 10) | 0));
      var idx2 = (31 & ((io$2 >>> 5) | 0));
      var idx1 = (31 & io$2);
      var a4c$2 = a4$2.clone__O();
      var a3$3 = a4c$2.get(idx4);
      var a3c$3 = a3$3.clone__O();
      var a2$4 = a3c$3.get(idx3);
      var a2c$4 = a2$4.clone__O();
      var a1$5 = a2c$4.get(idx2);
      var a1c$5 = a1$5.clone__O();
      a1c$5.set(idx1, elem);
      a2c$4.set(idx2, a1c$5);
      a3c$3.set(idx3, a2c$4);
      a4c$2.set(idx4, a3c$3);
      var x$72 = this.sci_Vector__f_prefix1;
      var x$73 = this.sci_Vector5__f_len1;
      var x$74 = this.sci_Vector5__f_prefix2;
      var x$75 = this.sci_Vector5__f_len12;
      var x$76 = this.sci_Vector5__f_prefix3;
      var x$77 = this.sci_Vector5__f_len123;
      var x$78 = this.sci_Vector5__f_len1234;
      var x$79 = this.sci_Vector5__f_data5;
      var x$80 = this.sci_Vector5__f_suffix4;
      var x$81 = this.sci_Vector5__f_suffix3;
      var x$82 = this.sci_Vector5__f_suffix2;
      var x$83 = this.sci_BigVector__f_suffix1;
      var x$84 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector5(x$72, x$73, x$74, x$75, x$76, x$77, a4c$2, x$78, x$79, x$80, x$81, x$82, x$83, x$84)
    } else if ((index >= this.sci_Vector5__f_len12)) {
      var io$3 = ((index - this.sci_Vector5__f_len12) | 0);
      var a3$4 = this.sci_Vector5__f_prefix3;
      var idx3$1 = ((io$3 >>> 10) | 0);
      var idx2$1 = (31 & ((io$3 >>> 5) | 0));
      var idx1$1 = (31 & io$3);
      var a3c$4 = a3$4.clone__O();
      var a2$5 = a3c$4.get(idx3$1);
      var a2c$5 = a2$5.clone__O();
      var a1$6 = a2c$5.get(idx2$1);
      var a1c$6 = a1$6.clone__O();
      a1c$6.set(idx1$1, elem);
      a2c$5.set(idx2$1, a1c$6);
      a3c$4.set(idx3$1, a2c$5);
      var x$86 = this.sci_Vector__f_prefix1;
      var x$87 = this.sci_Vector5__f_len1;
      var x$88 = this.sci_Vector5__f_prefix2;
      var x$89 = this.sci_Vector5__f_len12;
      var x$90 = this.sci_Vector5__f_len123;
      var x$91 = this.sci_Vector5__f_prefix4;
      var x$92 = this.sci_Vector5__f_len1234;
      var x$93 = this.sci_Vector5__f_data5;
      var x$94 = this.sci_Vector5__f_suffix4;
      var x$95 = this.sci_Vector5__f_suffix3;
      var x$96 = this.sci_Vector5__f_suffix2;
      var x$97 = this.sci_BigVector__f_suffix1;
      var x$98 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector5(x$86, x$87, x$88, x$89, a3c$4, x$90, x$91, x$92, x$93, x$94, x$95, x$96, x$97, x$98)
    } else if ((index >= this.sci_Vector5__f_len1)) {
      var io$4 = ((index - this.sci_Vector5__f_len1) | 0);
      var a2$6 = this.sci_Vector5__f_prefix2;
      var idx2$2 = ((io$4 >>> 5) | 0);
      var idx1$2 = (31 & io$4);
      var a2c$6 = a2$6.clone__O();
      var a1$7 = a2c$6.get(idx2$2);
      var a1c$7 = a1$7.clone__O();
      a1c$7.set(idx1$2, elem);
      a2c$6.set(idx2$2, a1c$7);
      var x$100 = this.sci_Vector__f_prefix1;
      var x$101 = this.sci_Vector5__f_len1;
      var x$102 = this.sci_Vector5__f_len12;
      var x$103 = this.sci_Vector5__f_prefix3;
      var x$104 = this.sci_Vector5__f_len123;
      var x$105 = this.sci_Vector5__f_prefix4;
      var x$106 = this.sci_Vector5__f_len1234;
      var x$107 = this.sci_Vector5__f_data5;
      var x$108 = this.sci_Vector5__f_suffix4;
      var x$109 = this.sci_Vector5__f_suffix3;
      var x$110 = this.sci_Vector5__f_suffix2;
      var x$111 = this.sci_BigVector__f_suffix1;
      var x$112 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector5(x$100, x$101, a2c$6, x$102, x$103, x$104, x$105, x$106, x$107, x$108, x$109, x$110, x$111, x$112)
    } else {
      var a1$8 = this.sci_Vector__f_prefix1;
      var a1c$8 = a1$8.clone__O();
      a1c$8.set(index, elem);
      var len1 = this.sci_Vector5__f_len1;
      var prefix2 = this.sci_Vector5__f_prefix2;
      var len12 = this.sci_Vector5__f_len12;
      var prefix3 = this.sci_Vector5__f_prefix3;
      var len123 = this.sci_Vector5__f_len123;
      var prefix4 = this.sci_Vector5__f_prefix4;
      var len1234 = this.sci_Vector5__f_len1234;
      var data5 = this.sci_Vector5__f_data5;
      var suffix4 = this.sci_Vector5__f_suffix4;
      var suffix3 = this.sci_Vector5__f_suffix3;
      var suffix2 = this.sci_Vector5__f_suffix2;
      var suffix1 = this.sci_BigVector__f_suffix1;
      var length0 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector5(a1c$8, len1, prefix2, len12, prefix3, len123, prefix4, len1234, data5, suffix4, suffix3, suffix2, suffix1, length0)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector5.prototype.appended__O__sci_Vector = (function(elem) {
  if ((this.sci_BigVector__f_suffix1.u.length < 32)) {
    var x$1 = $m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_BigVector__f_suffix1, elem);
    var x$2 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$3 = this.sci_Vector__f_prefix1;
    var x$4 = this.sci_Vector5__f_len1;
    var x$5 = this.sci_Vector5__f_prefix2;
    var x$6 = this.sci_Vector5__f_len12;
    var x$7 = this.sci_Vector5__f_prefix3;
    var x$8 = this.sci_Vector5__f_len123;
    var x$9 = this.sci_Vector5__f_prefix4;
    var x$10 = this.sci_Vector5__f_len1234;
    var x$11 = this.sci_Vector5__f_data5;
    var x$12 = this.sci_Vector5__f_suffix4;
    var x$13 = this.sci_Vector5__f_suffix3;
    var x$14 = this.sci_Vector5__f_suffix2;
    return new $c_sci_Vector5(x$3, x$4, x$5, x$6, x$7, x$8, x$9, x$10, x$11, x$12, x$13, x$14, x$1, x$2)
  } else if ((this.sci_Vector5__f_suffix2.u.length < 31)) {
    var x$15 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix2, this.sci_BigVector__f_suffix1), 2);
    var a = new $ac_O(1);
    a.set(0, elem);
    var x$17 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$18 = this.sci_Vector__f_prefix1;
    var x$19 = this.sci_Vector5__f_len1;
    var x$20 = this.sci_Vector5__f_prefix2;
    var x$21 = this.sci_Vector5__f_len12;
    var x$22 = this.sci_Vector5__f_prefix3;
    var x$23 = this.sci_Vector5__f_len123;
    var x$24 = this.sci_Vector5__f_prefix4;
    var x$25 = this.sci_Vector5__f_len1234;
    var x$26 = this.sci_Vector5__f_data5;
    var x$27 = this.sci_Vector5__f_suffix4;
    var x$28 = this.sci_Vector5__f_suffix3;
    return new $c_sci_Vector5(x$18, x$19, x$20, x$21, x$22, x$23, x$24, x$25, x$26, x$27, x$28, x$15, a, x$17)
  } else if ((this.sci_Vector5__f_suffix3.u.length < 31)) {
    var x$29 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix2, this.sci_BigVector__f_suffix1)), 3);
    var x$30 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$1 = new $ac_O(1);
    a$1.set(0, elem);
    var x$32 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$33 = this.sci_Vector__f_prefix1;
    var x$34 = this.sci_Vector5__f_len1;
    var x$35 = this.sci_Vector5__f_prefix2;
    var x$36 = this.sci_Vector5__f_len12;
    var x$37 = this.sci_Vector5__f_prefix3;
    var x$38 = this.sci_Vector5__f_len123;
    var x$39 = this.sci_Vector5__f_prefix4;
    var x$40 = this.sci_Vector5__f_len1234;
    var x$41 = this.sci_Vector5__f_data5;
    var x$42 = this.sci_Vector5__f_suffix4;
    return new $c_sci_Vector5(x$33, x$34, x$35, x$36, x$37, x$38, x$39, x$40, x$41, x$42, x$29, x$30, a$1, x$32)
  } else if ((this.sci_Vector5__f_suffix4.u.length < 31)) {
    var x$43 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix2, this.sci_BigVector__f_suffix1))), 4);
    var x$44 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$45 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$2 = new $ac_O(1);
    a$2.set(0, elem);
    var x$47 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$48 = this.sci_Vector__f_prefix1;
    var x$49 = this.sci_Vector5__f_len1;
    var x$50 = this.sci_Vector5__f_prefix2;
    var x$51 = this.sci_Vector5__f_len12;
    var x$52 = this.sci_Vector5__f_prefix3;
    var x$53 = this.sci_Vector5__f_len123;
    var x$54 = this.sci_Vector5__f_prefix4;
    var x$55 = this.sci_Vector5__f_len1234;
    var x$56 = this.sci_Vector5__f_data5;
    return new $c_sci_Vector5(x$48, x$49, x$50, x$51, x$52, x$53, x$54, x$55, x$56, x$43, x$44, x$45, a$2, x$47)
  } else if ((this.sci_Vector5__f_data5.u.length < 30)) {
    var x$57 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_data5, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix2, this.sci_BigVector__f_suffix1)))), 5);
    var x$58 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
    var x$59 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$60 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$3 = new $ac_O(1);
    a$3.set(0, elem);
    var x$62 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$63 = this.sci_Vector__f_prefix1;
    var x$64 = this.sci_Vector5__f_len1;
    var x$65 = this.sci_Vector5__f_prefix2;
    var x$66 = this.sci_Vector5__f_len12;
    var x$67 = this.sci_Vector5__f_prefix3;
    var x$68 = this.sci_Vector5__f_len123;
    var x$69 = this.sci_Vector5__f_prefix4;
    var x$70 = this.sci_Vector5__f_len1234;
    return new $c_sci_Vector5(x$63, x$64, x$65, x$66, x$67, x$68, x$69, x$70, x$57, x$58, x$59, x$60, a$3, x$62)
  } else {
    var $$x14 = this.sci_Vector__f_prefix1;
    var $$x13 = this.sci_Vector5__f_len1;
    var $$x12 = this.sci_Vector5__f_prefix2;
    var $$x11 = this.sci_Vector5__f_len12;
    var $$x10 = this.sci_Vector5__f_prefix3;
    var $$x9 = this.sci_Vector5__f_len123;
    var $$x8 = this.sci_Vector5__f_prefix4;
    var $$x7 = this.sci_Vector5__f_len1234;
    var $$x6 = this.sci_Vector5__f_data5;
    var $$x5 = this.sci_Vector5__f_len1234;
    var $$x4 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty6;
    var x = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector5__f_suffix2, this.sci_BigVector__f_suffix1))), 4);
    var a$4 = new ($d_O.getArrayOf().getArrayOf().getArrayOf().getArrayOf().getArrayOf().constr)(1);
    a$4.set(0, x);
    var $$x3 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
    var $$x2 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var $$x1 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$5 = new $ac_O(1);
    a$5.set(0, elem);
    return new $c_sci_Vector6($$x14, $$x13, $$x12, $$x11, $$x10, $$x9, $$x8, $$x7, $$x6, ((31457280 + $$x5) | 0), $$x4, a$4, $$x3, $$x2, $$x1, a$5, ((1 + this.sci_BigVector__f_length0) | 0))
  }
});
$c_sci_Vector5.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var b = new $c_sci_VectorSliceBuilder(lo, hi);
  b.consider__I__AO__V(1, this.sci_Vector__f_prefix1);
  b.consider__I__AO__V(2, this.sci_Vector5__f_prefix2);
  b.consider__I__AO__V(3, this.sci_Vector5__f_prefix3);
  b.consider__I__AO__V(4, this.sci_Vector5__f_prefix4);
  b.consider__I__AO__V(5, this.sci_Vector5__f_data5);
  b.consider__I__AO__V(4, this.sci_Vector5__f_suffix4);
  b.consider__I__AO__V(3, this.sci_Vector5__f_suffix3);
  b.consider__I__AO__V(2, this.sci_Vector5__f_suffix2);
  b.consider__I__AO__V(1, this.sci_BigVector__f_suffix1);
  return b.result__sci_Vector()
});
$c_sci_Vector5.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector5__f_len1 > 1)) {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    var x$1 = $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to);
    var x$2 = (((-1) + this.sci_Vector5__f_len1) | 0);
    var x$3 = (((-1) + this.sci_Vector5__f_len12) | 0);
    var x$4 = (((-1) + this.sci_Vector5__f_len123) | 0);
    var x$5 = (((-1) + this.sci_Vector5__f_len1234) | 0);
    var x$6 = (((-1) + this.sci_BigVector__f_length0) | 0);
    var x$7 = this.sci_Vector5__f_prefix2;
    var x$8 = this.sci_Vector5__f_prefix3;
    var x$9 = this.sci_Vector5__f_prefix4;
    var x$10 = this.sci_Vector5__f_data5;
    var x$11 = this.sci_Vector5__f_suffix4;
    var x$12 = this.sci_Vector5__f_suffix3;
    var x$13 = this.sci_Vector5__f_suffix2;
    var x$14 = this.sci_BigVector__f_suffix1;
    return new $c_sci_Vector5(x$1, x$2, x$7, x$3, x$8, x$4, x$9, x$5, x$10, x$11, x$12, x$13, x$14, x$6)
  } else {
    return this.slice0__I__I__sci_Vector(1, this.sci_BigVector__f_length0)
  }
});
$c_sci_Vector5.prototype.vectorSliceCount__I = (function() {
  return 9
});
$c_sci_Vector5.prototype.vectorSlice__I__AO = (function(idx) {
  switch (idx) {
    case 0: {
      return this.sci_Vector__f_prefix1;
      break
    }
    case 1: {
      return this.sci_Vector5__f_prefix2;
      break
    }
    case 2: {
      return this.sci_Vector5__f_prefix3;
      break
    }
    case 3: {
      return this.sci_Vector5__f_prefix4;
      break
    }
    case 4: {
      return this.sci_Vector5__f_data5;
      break
    }
    case 5: {
      return this.sci_Vector5__f_suffix4;
      break
    }
    case 6: {
      return this.sci_Vector5__f_suffix3;
      break
    }
    case 7: {
      return this.sci_Vector5__f_suffix2;
      break
    }
    case 8: {
      return this.sci_BigVector__f_suffix1;
      break
    }
    default: {
      throw new $c_s_MatchError(idx)
    }
  }
});
$c_sci_Vector5.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector5.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector5__f_len1234) | 0);
    if ((io >= 0)) {
      var i5 = ((io >>> 20) | 0);
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i5 < this.sci_Vector5__f_data5.u.length) ? this.sci_Vector5__f_data5.get(i5).get(i4).get(i3).get(i2).get(i1) : ((i4 < this.sci_Vector5__f_suffix4.u.length) ? this.sci_Vector5__f_suffix4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector5__f_suffix3.u.length) ? this.sci_Vector5__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector5__f_suffix2.u.length) ? this.sci_Vector5__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1)))))
    } else if ((index >= this.sci_Vector5__f_len123)) {
      var io$2 = ((index - this.sci_Vector5__f_len123) | 0);
      return this.sci_Vector5__f_prefix4.get(((io$2 >>> 15) | 0)).get((31 & ((io$2 >>> 10) | 0))).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector5__f_len12)) {
      var io$3 = ((index - this.sci_Vector5__f_len12) | 0);
      return this.sci_Vector5__f_prefix3.get(((io$3 >>> 10) | 0)).get((31 & ((io$3 >>> 5) | 0))).get((31 & io$3))
    } else if ((index >= this.sci_Vector5__f_len1)) {
      var io$4 = ((index - this.sci_Vector5__f_len1) | 0);
      return this.sci_Vector5__f_prefix2.get(((io$4 >>> 5) | 0)).get((31 & io$4))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector5(obj) {
  return (((obj instanceof $c_sci_Vector5) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector5"))
}
function $isArrayOf_sci_Vector5(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector5)))
}
function $asArrayOf_sci_Vector5(obj, depth) {
  return (($isArrayOf_sci_Vector5(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector5;", depth))
}
var $d_sci_Vector5 = new $TypeData().initClass({
  sci_Vector5: 0
}, false, "scala.collection.immutable.Vector5", {
  sci_Vector5: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector5.prototype.$classData = $d_sci_Vector5;
/** @constructor */
function $c_sci_Vector6(_prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, prefix5, len12345, data6, suffix5, suffix4, suffix3, suffix2, _suffix1, _length0) {
  this.sci_Vector__f_prefix1 = null;
  this.sci_BigVector__f_suffix1 = null;
  this.sci_BigVector__f_length0 = 0;
  this.sci_Vector6__f_len1 = 0;
  this.sci_Vector6__f_prefix2 = null;
  this.sci_Vector6__f_len12 = 0;
  this.sci_Vector6__f_prefix3 = null;
  this.sci_Vector6__f_len123 = 0;
  this.sci_Vector6__f_prefix4 = null;
  this.sci_Vector6__f_len1234 = 0;
  this.sci_Vector6__f_prefix5 = null;
  this.sci_Vector6__f_len12345 = 0;
  this.sci_Vector6__f_data6 = null;
  this.sci_Vector6__f_suffix5 = null;
  this.sci_Vector6__f_suffix4 = null;
  this.sci_Vector6__f_suffix3 = null;
  this.sci_Vector6__f_suffix2 = null;
  this.sci_Vector6__f_len1 = len1;
  this.sci_Vector6__f_prefix2 = prefix2;
  this.sci_Vector6__f_len12 = len12;
  this.sci_Vector6__f_prefix3 = prefix3;
  this.sci_Vector6__f_len123 = len123;
  this.sci_Vector6__f_prefix4 = prefix4;
  this.sci_Vector6__f_len1234 = len1234;
  this.sci_Vector6__f_prefix5 = prefix5;
  this.sci_Vector6__f_len12345 = len12345;
  this.sci_Vector6__f_data6 = data6;
  this.sci_Vector6__f_suffix5 = suffix5;
  this.sci_Vector6__f_suffix4 = suffix4;
  this.sci_Vector6__f_suffix3 = suffix3;
  this.sci_Vector6__f_suffix2 = suffix2;
  $ct_sci_BigVector__AO__AO__I__(this, _prefix1, _suffix1, _length0)
}
$c_sci_Vector6.prototype = new $h_sci_BigVector();
$c_sci_Vector6.prototype.constructor = $c_sci_Vector6;
/** @constructor */
function $h_sci_Vector6() {
  /*<skip>*/
}
$h_sci_Vector6.prototype = $c_sci_Vector6.prototype;
$c_sci_Vector6.prototype.apply__I__O = (function(index) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector6__f_len12345) | 0);
    if ((io >= 0)) {
      var i6 = ((io >>> 25) | 0);
      var i5 = (31 & ((io >>> 20) | 0));
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i6 < this.sci_Vector6__f_data6.u.length) ? this.sci_Vector6__f_data6.get(i6).get(i5).get(i4).get(i3).get(i2).get(i1) : ((i5 < this.sci_Vector6__f_suffix5.u.length) ? this.sci_Vector6__f_suffix5.get(i5).get(i4).get(i3).get(i2).get(i1) : ((i4 < this.sci_Vector6__f_suffix4.u.length) ? this.sci_Vector6__f_suffix4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector6__f_suffix3.u.length) ? this.sci_Vector6__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector6__f_suffix2.u.length) ? this.sci_Vector6__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1))))))
    } else if ((index >= this.sci_Vector6__f_len1234)) {
      var io$2 = ((index - this.sci_Vector6__f_len1234) | 0);
      return this.sci_Vector6__f_prefix5.get(((io$2 >>> 20) | 0)).get((31 & ((io$2 >>> 15) | 0))).get((31 & ((io$2 >>> 10) | 0))).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector6__f_len123)) {
      var io$3 = ((index - this.sci_Vector6__f_len123) | 0);
      return this.sci_Vector6__f_prefix4.get(((io$3 >>> 15) | 0)).get((31 & ((io$3 >>> 10) | 0))).get((31 & ((io$3 >>> 5) | 0))).get((31 & io$3))
    } else if ((index >= this.sci_Vector6__f_len12)) {
      var io$4 = ((index - this.sci_Vector6__f_len12) | 0);
      return this.sci_Vector6__f_prefix3.get(((io$4 >>> 10) | 0)).get((31 & ((io$4 >>> 5) | 0))).get((31 & io$4))
    } else if ((index >= this.sci_Vector6__f_len1)) {
      var io$5 = ((index - this.sci_Vector6__f_len1) | 0);
      return this.sci_Vector6__f_prefix2.get(((io$5 >>> 5) | 0)).get((31 & io$5))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector6.prototype.updated__I__O__sci_Vector = (function(index, elem) {
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    if ((index >= this.sci_Vector6__f_len12345)) {
      var io = ((index - this.sci_Vector6__f_len12345) | 0);
      var i6 = ((io >>> 25) | 0);
      var i5 = (31 & ((io >>> 20) | 0));
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      if ((i6 < this.sci_Vector6__f_data6.u.length)) {
        var a6 = this.sci_Vector6__f_data6;
        var a6c = a6.clone__O();
        var a5 = a6c.get(i6);
        var a5c = a5.clone__O();
        var a4 = a5c.get(i5);
        var a4c = a4.clone__O();
        var a3 = a4c.get(i4);
        var a3c = a3.clone__O();
        var a2 = a3c.get(i3);
        var a2c = a2.clone__O();
        var a1 = a2c.get(i2);
        var a1c = a1.clone__O();
        a1c.set(i1, elem);
        a2c.set(i2, a1c);
        a3c.set(i3, a2c);
        a4c.set(i4, a3c);
        a5c.set(i5, a4c);
        a6c.set(i6, a5c);
        var x$2 = this.sci_Vector__f_prefix1;
        var x$3 = this.sci_Vector6__f_len1;
        var x$4 = this.sci_Vector6__f_prefix2;
        var x$5 = this.sci_Vector6__f_len12;
        var x$6 = this.sci_Vector6__f_prefix3;
        var x$7 = this.sci_Vector6__f_len123;
        var x$8 = this.sci_Vector6__f_prefix4;
        var x$9 = this.sci_Vector6__f_len1234;
        var x$10 = this.sci_Vector6__f_prefix5;
        var x$11 = this.sci_Vector6__f_len12345;
        var x$12 = this.sci_Vector6__f_suffix5;
        var x$13 = this.sci_Vector6__f_suffix4;
        var x$14 = this.sci_Vector6__f_suffix3;
        var x$15 = this.sci_Vector6__f_suffix2;
        var x$16 = this.sci_BigVector__f_suffix1;
        var x$17 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$2, x$3, x$4, x$5, x$6, x$7, x$8, x$9, x$10, x$11, a6c, x$12, x$13, x$14, x$15, x$16, x$17)
      } else if ((i5 < this.sci_Vector6__f_suffix5.u.length)) {
        var a5$1 = this.sci_Vector6__f_suffix5;
        var a5c$1 = a5$1.clone__O();
        var a4$1 = a5c$1.get(i5);
        var a4c$1 = a4$1.clone__O();
        var a3$1 = a4c$1.get(i4);
        var a3c$1 = a3$1.clone__O();
        var a2$1 = a3c$1.get(i3);
        var a2c$1 = a2$1.clone__O();
        var a1$1 = a2c$1.get(i2);
        var a1c$1 = a1$1.clone__O();
        a1c$1.set(i1, elem);
        a2c$1.set(i2, a1c$1);
        a3c$1.set(i3, a2c$1);
        a4c$1.set(i4, a3c$1);
        a5c$1.set(i5, a4c$1);
        var x$19 = this.sci_Vector__f_prefix1;
        var x$20 = this.sci_Vector6__f_len1;
        var x$21 = this.sci_Vector6__f_prefix2;
        var x$22 = this.sci_Vector6__f_len12;
        var x$23 = this.sci_Vector6__f_prefix3;
        var x$24 = this.sci_Vector6__f_len123;
        var x$25 = this.sci_Vector6__f_prefix4;
        var x$26 = this.sci_Vector6__f_len1234;
        var x$27 = this.sci_Vector6__f_prefix5;
        var x$28 = this.sci_Vector6__f_len12345;
        var x$29 = this.sci_Vector6__f_data6;
        var x$30 = this.sci_Vector6__f_suffix4;
        var x$31 = this.sci_Vector6__f_suffix3;
        var x$32 = this.sci_Vector6__f_suffix2;
        var x$33 = this.sci_BigVector__f_suffix1;
        var x$34 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$19, x$20, x$21, x$22, x$23, x$24, x$25, x$26, x$27, x$28, x$29, a5c$1, x$30, x$31, x$32, x$33, x$34)
      } else if ((i4 < this.sci_Vector6__f_suffix4.u.length)) {
        var a4$2 = this.sci_Vector6__f_suffix4;
        var a4c$2 = a4$2.clone__O();
        var a3$2 = a4c$2.get(i4);
        var a3c$2 = a3$2.clone__O();
        var a2$2 = a3c$2.get(i3);
        var a2c$2 = a2$2.clone__O();
        var a1$2 = a2c$2.get(i2);
        var a1c$2 = a1$2.clone__O();
        a1c$2.set(i1, elem);
        a2c$2.set(i2, a1c$2);
        a3c$2.set(i3, a2c$2);
        a4c$2.set(i4, a3c$2);
        var x$36 = this.sci_Vector__f_prefix1;
        var x$37 = this.sci_Vector6__f_len1;
        var x$38 = this.sci_Vector6__f_prefix2;
        var x$39 = this.sci_Vector6__f_len12;
        var x$40 = this.sci_Vector6__f_prefix3;
        var x$41 = this.sci_Vector6__f_len123;
        var x$42 = this.sci_Vector6__f_prefix4;
        var x$43 = this.sci_Vector6__f_len1234;
        var x$44 = this.sci_Vector6__f_prefix5;
        var x$45 = this.sci_Vector6__f_len12345;
        var x$46 = this.sci_Vector6__f_data6;
        var x$47 = this.sci_Vector6__f_suffix5;
        var x$48 = this.sci_Vector6__f_suffix3;
        var x$49 = this.sci_Vector6__f_suffix2;
        var x$50 = this.sci_BigVector__f_suffix1;
        var x$51 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$36, x$37, x$38, x$39, x$40, x$41, x$42, x$43, x$44, x$45, x$46, x$47, a4c$2, x$48, x$49, x$50, x$51)
      } else if ((i3 < this.sci_Vector6__f_suffix3.u.length)) {
        var a3$3 = this.sci_Vector6__f_suffix3;
        var a3c$3 = a3$3.clone__O();
        var a2$3 = a3c$3.get(i3);
        var a2c$3 = a2$3.clone__O();
        var a1$3 = a2c$3.get(i2);
        var a1c$3 = a1$3.clone__O();
        a1c$3.set(i1, elem);
        a2c$3.set(i2, a1c$3);
        a3c$3.set(i3, a2c$3);
        var x$53 = this.sci_Vector__f_prefix1;
        var x$54 = this.sci_Vector6__f_len1;
        var x$55 = this.sci_Vector6__f_prefix2;
        var x$56 = this.sci_Vector6__f_len12;
        var x$57 = this.sci_Vector6__f_prefix3;
        var x$58 = this.sci_Vector6__f_len123;
        var x$59 = this.sci_Vector6__f_prefix4;
        var x$60 = this.sci_Vector6__f_len1234;
        var x$61 = this.sci_Vector6__f_prefix5;
        var x$62 = this.sci_Vector6__f_len12345;
        var x$63 = this.sci_Vector6__f_data6;
        var x$64 = this.sci_Vector6__f_suffix5;
        var x$65 = this.sci_Vector6__f_suffix4;
        var x$66 = this.sci_Vector6__f_suffix2;
        var x$67 = this.sci_BigVector__f_suffix1;
        var x$68 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$53, x$54, x$55, x$56, x$57, x$58, x$59, x$60, x$61, x$62, x$63, x$64, x$65, a3c$3, x$66, x$67, x$68)
      } else if ((i2 < this.sci_Vector6__f_suffix2.u.length)) {
        var a2$4 = this.sci_Vector6__f_suffix2;
        var a2c$4 = a2$4.clone__O();
        var a1$4 = a2c$4.get(i2);
        var a1c$4 = a1$4.clone__O();
        a1c$4.set(i1, elem);
        a2c$4.set(i2, a1c$4);
        var x$70 = this.sci_Vector__f_prefix1;
        var x$71 = this.sci_Vector6__f_len1;
        var x$72 = this.sci_Vector6__f_prefix2;
        var x$73 = this.sci_Vector6__f_len12;
        var x$74 = this.sci_Vector6__f_prefix3;
        var x$75 = this.sci_Vector6__f_len123;
        var x$76 = this.sci_Vector6__f_prefix4;
        var x$77 = this.sci_Vector6__f_len1234;
        var x$78 = this.sci_Vector6__f_prefix5;
        var x$79 = this.sci_Vector6__f_len12345;
        var x$80 = this.sci_Vector6__f_data6;
        var x$81 = this.sci_Vector6__f_suffix5;
        var x$82 = this.sci_Vector6__f_suffix4;
        var x$83 = this.sci_Vector6__f_suffix3;
        var x$84 = this.sci_BigVector__f_suffix1;
        var x$85 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$70, x$71, x$72, x$73, x$74, x$75, x$76, x$77, x$78, x$79, x$80, x$81, x$82, x$83, a2c$4, x$84, x$85)
      } else {
        var a1$5 = this.sci_BigVector__f_suffix1;
        var a1c$5 = a1$5.clone__O();
        a1c$5.set(i1, elem);
        var x$87 = this.sci_Vector__f_prefix1;
        var x$88 = this.sci_Vector6__f_len1;
        var x$89 = this.sci_Vector6__f_prefix2;
        var x$90 = this.sci_Vector6__f_len12;
        var x$91 = this.sci_Vector6__f_prefix3;
        var x$92 = this.sci_Vector6__f_len123;
        var x$93 = this.sci_Vector6__f_prefix4;
        var x$94 = this.sci_Vector6__f_len1234;
        var x$95 = this.sci_Vector6__f_prefix5;
        var x$96 = this.sci_Vector6__f_len12345;
        var x$97 = this.sci_Vector6__f_data6;
        var x$98 = this.sci_Vector6__f_suffix5;
        var x$99 = this.sci_Vector6__f_suffix4;
        var x$100 = this.sci_Vector6__f_suffix3;
        var x$101 = this.sci_Vector6__f_suffix2;
        var x$102 = this.sci_BigVector__f_length0;
        return new $c_sci_Vector6(x$87, x$88, x$89, x$90, x$91, x$92, x$93, x$94, x$95, x$96, x$97, x$98, x$99, x$100, x$101, a1c$5, x$102)
      }
    } else if ((index >= this.sci_Vector6__f_len1234)) {
      var io$2 = ((index - this.sci_Vector6__f_len1234) | 0);
      var a5$2 = this.sci_Vector6__f_prefix5;
      var idx5 = ((io$2 >>> 20) | 0);
      var idx4 = (31 & ((io$2 >>> 15) | 0));
      var idx3 = (31 & ((io$2 >>> 10) | 0));
      var idx2 = (31 & ((io$2 >>> 5) | 0));
      var idx1 = (31 & io$2);
      var a5c$2 = a5$2.clone__O();
      var a4$3 = a5c$2.get(idx5);
      var a4c$3 = a4$3.clone__O();
      var a3$4 = a4c$3.get(idx4);
      var a3c$4 = a3$4.clone__O();
      var a2$5 = a3c$4.get(idx3);
      var a2c$5 = a2$5.clone__O();
      var a1$6 = a2c$5.get(idx2);
      var a1c$6 = a1$6.clone__O();
      a1c$6.set(idx1, elem);
      a2c$5.set(idx2, a1c$6);
      a3c$4.set(idx3, a2c$5);
      a4c$3.set(idx4, a3c$4);
      a5c$2.set(idx5, a4c$3);
      var x$104 = this.sci_Vector__f_prefix1;
      var x$105 = this.sci_Vector6__f_len1;
      var x$106 = this.sci_Vector6__f_prefix2;
      var x$107 = this.sci_Vector6__f_len12;
      var x$108 = this.sci_Vector6__f_prefix3;
      var x$109 = this.sci_Vector6__f_len123;
      var x$110 = this.sci_Vector6__f_prefix4;
      var x$111 = this.sci_Vector6__f_len1234;
      var x$112 = this.sci_Vector6__f_len12345;
      var x$113 = this.sci_Vector6__f_data6;
      var x$114 = this.sci_Vector6__f_suffix5;
      var x$115 = this.sci_Vector6__f_suffix4;
      var x$116 = this.sci_Vector6__f_suffix3;
      var x$117 = this.sci_Vector6__f_suffix2;
      var x$118 = this.sci_BigVector__f_suffix1;
      var x$119 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector6(x$104, x$105, x$106, x$107, x$108, x$109, x$110, x$111, a5c$2, x$112, x$113, x$114, x$115, x$116, x$117, x$118, x$119)
    } else if ((index >= this.sci_Vector6__f_len123)) {
      var io$3 = ((index - this.sci_Vector6__f_len123) | 0);
      var a4$4 = this.sci_Vector6__f_prefix4;
      var idx4$1 = ((io$3 >>> 15) | 0);
      var idx3$1 = (31 & ((io$3 >>> 10) | 0));
      var idx2$1 = (31 & ((io$3 >>> 5) | 0));
      var idx1$1 = (31 & io$3);
      var a4c$4 = a4$4.clone__O();
      var a3$5 = a4c$4.get(idx4$1);
      var a3c$5 = a3$5.clone__O();
      var a2$6 = a3c$5.get(idx3$1);
      var a2c$6 = a2$6.clone__O();
      var a1$7 = a2c$6.get(idx2$1);
      var a1c$7 = a1$7.clone__O();
      a1c$7.set(idx1$1, elem);
      a2c$6.set(idx2$1, a1c$7);
      a3c$5.set(idx3$1, a2c$6);
      a4c$4.set(idx4$1, a3c$5);
      var x$121 = this.sci_Vector__f_prefix1;
      var x$122 = this.sci_Vector6__f_len1;
      var x$123 = this.sci_Vector6__f_prefix2;
      var x$124 = this.sci_Vector6__f_len12;
      var x$125 = this.sci_Vector6__f_prefix3;
      var x$126 = this.sci_Vector6__f_len123;
      var x$127 = this.sci_Vector6__f_len1234;
      var x$128 = this.sci_Vector6__f_prefix5;
      var x$129 = this.sci_Vector6__f_len12345;
      var x$130 = this.sci_Vector6__f_data6;
      var x$131 = this.sci_Vector6__f_suffix5;
      var x$132 = this.sci_Vector6__f_suffix4;
      var x$133 = this.sci_Vector6__f_suffix3;
      var x$134 = this.sci_Vector6__f_suffix2;
      var x$135 = this.sci_BigVector__f_suffix1;
      var x$136 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector6(x$121, x$122, x$123, x$124, x$125, x$126, a4c$4, x$127, x$128, x$129, x$130, x$131, x$132, x$133, x$134, x$135, x$136)
    } else if ((index >= this.sci_Vector6__f_len12)) {
      var io$4 = ((index - this.sci_Vector6__f_len12) | 0);
      var a3$6 = this.sci_Vector6__f_prefix3;
      var idx3$2 = ((io$4 >>> 10) | 0);
      var idx2$2 = (31 & ((io$4 >>> 5) | 0));
      var idx1$2 = (31 & io$4);
      var a3c$6 = a3$6.clone__O();
      var a2$7 = a3c$6.get(idx3$2);
      var a2c$7 = a2$7.clone__O();
      var a1$8 = a2c$7.get(idx2$2);
      var a1c$8 = a1$8.clone__O();
      a1c$8.set(idx1$2, elem);
      a2c$7.set(idx2$2, a1c$8);
      a3c$6.set(idx3$2, a2c$7);
      var x$138 = this.sci_Vector__f_prefix1;
      var x$139 = this.sci_Vector6__f_len1;
      var x$140 = this.sci_Vector6__f_prefix2;
      var x$141 = this.sci_Vector6__f_len12;
      var x$142 = this.sci_Vector6__f_len123;
      var x$143 = this.sci_Vector6__f_prefix4;
      var x$144 = this.sci_Vector6__f_len1234;
      var x$145 = this.sci_Vector6__f_prefix5;
      var x$146 = this.sci_Vector6__f_len12345;
      var x$147 = this.sci_Vector6__f_data6;
      var x$148 = this.sci_Vector6__f_suffix5;
      var x$149 = this.sci_Vector6__f_suffix4;
      var x$150 = this.sci_Vector6__f_suffix3;
      var x$151 = this.sci_Vector6__f_suffix2;
      var x$152 = this.sci_BigVector__f_suffix1;
      var x$153 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector6(x$138, x$139, x$140, x$141, a3c$6, x$142, x$143, x$144, x$145, x$146, x$147, x$148, x$149, x$150, x$151, x$152, x$153)
    } else if ((index >= this.sci_Vector6__f_len1)) {
      var io$5 = ((index - this.sci_Vector6__f_len1) | 0);
      var a2$8 = this.sci_Vector6__f_prefix2;
      var idx2$3 = ((io$5 >>> 5) | 0);
      var idx1$3 = (31 & io$5);
      var a2c$8 = a2$8.clone__O();
      var a1$9 = a2c$8.get(idx2$3);
      var a1c$9 = a1$9.clone__O();
      a1c$9.set(idx1$3, elem);
      a2c$8.set(idx2$3, a1c$9);
      var x$155 = this.sci_Vector__f_prefix1;
      var x$156 = this.sci_Vector6__f_len1;
      var x$157 = this.sci_Vector6__f_len12;
      var x$158 = this.sci_Vector6__f_prefix3;
      var x$159 = this.sci_Vector6__f_len123;
      var x$160 = this.sci_Vector6__f_prefix4;
      var x$161 = this.sci_Vector6__f_len1234;
      var x$162 = this.sci_Vector6__f_prefix5;
      var x$163 = this.sci_Vector6__f_len12345;
      var x$164 = this.sci_Vector6__f_data6;
      var x$165 = this.sci_Vector6__f_suffix5;
      var x$166 = this.sci_Vector6__f_suffix4;
      var x$167 = this.sci_Vector6__f_suffix3;
      var x$168 = this.sci_Vector6__f_suffix2;
      var x$169 = this.sci_BigVector__f_suffix1;
      var x$170 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector6(x$155, x$156, a2c$8, x$157, x$158, x$159, x$160, x$161, x$162, x$163, x$164, x$165, x$166, x$167, x$168, x$169, x$170)
    } else {
      var a1$10 = this.sci_Vector__f_prefix1;
      var a1c$10 = a1$10.clone__O();
      a1c$10.set(index, elem);
      var len1 = this.sci_Vector6__f_len1;
      var prefix2 = this.sci_Vector6__f_prefix2;
      var len12 = this.sci_Vector6__f_len12;
      var prefix3 = this.sci_Vector6__f_prefix3;
      var len123 = this.sci_Vector6__f_len123;
      var prefix4 = this.sci_Vector6__f_prefix4;
      var len1234 = this.sci_Vector6__f_len1234;
      var prefix5 = this.sci_Vector6__f_prefix5;
      var len12345 = this.sci_Vector6__f_len12345;
      var data6 = this.sci_Vector6__f_data6;
      var suffix5 = this.sci_Vector6__f_suffix5;
      var suffix4 = this.sci_Vector6__f_suffix4;
      var suffix3 = this.sci_Vector6__f_suffix3;
      var suffix2 = this.sci_Vector6__f_suffix2;
      var suffix1 = this.sci_BigVector__f_suffix1;
      var length0 = this.sci_BigVector__f_length0;
      return new $c_sci_Vector6(a1c$10, len1, prefix2, len12, prefix3, len123, prefix4, len1234, prefix5, len12345, data6, suffix5, suffix4, suffix3, suffix2, suffix1, length0)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
$c_sci_Vector6.prototype.appended__O__sci_Vector = (function(elem) {
  if ((this.sci_BigVector__f_suffix1.u.length < 32)) {
    var x$1 = $m_sci_VectorStatics$().copyAppend1__AO__O__AO(this.sci_BigVector__f_suffix1, elem);
    var x$2 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$3 = this.sci_Vector__f_prefix1;
    var x$4 = this.sci_Vector6__f_len1;
    var x$5 = this.sci_Vector6__f_prefix2;
    var x$6 = this.sci_Vector6__f_len12;
    var x$7 = this.sci_Vector6__f_prefix3;
    var x$8 = this.sci_Vector6__f_len123;
    var x$9 = this.sci_Vector6__f_prefix4;
    var x$10 = this.sci_Vector6__f_len1234;
    var x$11 = this.sci_Vector6__f_prefix5;
    var x$12 = this.sci_Vector6__f_len12345;
    var x$13 = this.sci_Vector6__f_data6;
    var x$14 = this.sci_Vector6__f_suffix5;
    var x$15 = this.sci_Vector6__f_suffix4;
    var x$16 = this.sci_Vector6__f_suffix3;
    var x$17 = this.sci_Vector6__f_suffix2;
    return new $c_sci_Vector6(x$3, x$4, x$5, x$6, x$7, x$8, x$9, x$10, x$11, x$12, x$13, x$14, x$15, x$16, x$17, x$1, x$2)
  } else if ((this.sci_Vector6__f_suffix2.u.length < 31)) {
    var x$18 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix2, this.sci_BigVector__f_suffix1), 2);
    var a = new $ac_O(1);
    a.set(0, elem);
    var x$20 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$21 = this.sci_Vector__f_prefix1;
    var x$22 = this.sci_Vector6__f_len1;
    var x$23 = this.sci_Vector6__f_prefix2;
    var x$24 = this.sci_Vector6__f_len12;
    var x$25 = this.sci_Vector6__f_prefix3;
    var x$26 = this.sci_Vector6__f_len123;
    var x$27 = this.sci_Vector6__f_prefix4;
    var x$28 = this.sci_Vector6__f_len1234;
    var x$29 = this.sci_Vector6__f_prefix5;
    var x$30 = this.sci_Vector6__f_len12345;
    var x$31 = this.sci_Vector6__f_data6;
    var x$32 = this.sci_Vector6__f_suffix5;
    var x$33 = this.sci_Vector6__f_suffix4;
    var x$34 = this.sci_Vector6__f_suffix3;
    return new $c_sci_Vector6(x$21, x$22, x$23, x$24, x$25, x$26, x$27, x$28, x$29, x$30, x$31, x$32, x$33, x$34, x$18, a, x$20)
  } else if ((this.sci_Vector6__f_suffix3.u.length < 31)) {
    var x$35 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix2, this.sci_BigVector__f_suffix1)), 3);
    var x$36 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$1 = new $ac_O(1);
    a$1.set(0, elem);
    var x$38 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$39 = this.sci_Vector__f_prefix1;
    var x$40 = this.sci_Vector6__f_len1;
    var x$41 = this.sci_Vector6__f_prefix2;
    var x$42 = this.sci_Vector6__f_len12;
    var x$43 = this.sci_Vector6__f_prefix3;
    var x$44 = this.sci_Vector6__f_len123;
    var x$45 = this.sci_Vector6__f_prefix4;
    var x$46 = this.sci_Vector6__f_len1234;
    var x$47 = this.sci_Vector6__f_prefix5;
    var x$48 = this.sci_Vector6__f_len12345;
    var x$49 = this.sci_Vector6__f_data6;
    var x$50 = this.sci_Vector6__f_suffix5;
    var x$51 = this.sci_Vector6__f_suffix4;
    return new $c_sci_Vector6(x$39, x$40, x$41, x$42, x$43, x$44, x$45, x$46, x$47, x$48, x$49, x$50, x$51, x$35, x$36, a$1, x$38)
  } else if ((this.sci_Vector6__f_suffix4.u.length < 31)) {
    var x$52 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix2, this.sci_BigVector__f_suffix1))), 4);
    var x$53 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$54 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$2 = new $ac_O(1);
    a$2.set(0, elem);
    var x$56 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$57 = this.sci_Vector__f_prefix1;
    var x$58 = this.sci_Vector6__f_len1;
    var x$59 = this.sci_Vector6__f_prefix2;
    var x$60 = this.sci_Vector6__f_len12;
    var x$61 = this.sci_Vector6__f_prefix3;
    var x$62 = this.sci_Vector6__f_len123;
    var x$63 = this.sci_Vector6__f_prefix4;
    var x$64 = this.sci_Vector6__f_len1234;
    var x$65 = this.sci_Vector6__f_prefix5;
    var x$66 = this.sci_Vector6__f_len12345;
    var x$67 = this.sci_Vector6__f_data6;
    var x$68 = this.sci_Vector6__f_suffix5;
    return new $c_sci_Vector6(x$57, x$58, x$59, x$60, x$61, x$62, x$63, x$64, x$65, x$66, x$67, x$68, x$52, x$53, x$54, a$2, x$56)
  } else if ((this.sci_Vector6__f_suffix5.u.length < 31)) {
    var x$69 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix5, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix2, this.sci_BigVector__f_suffix1)))), 5);
    var x$70 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
    var x$71 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$72 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$3 = new $ac_O(1);
    a$3.set(0, elem);
    var x$74 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$75 = this.sci_Vector__f_prefix1;
    var x$76 = this.sci_Vector6__f_len1;
    var x$77 = this.sci_Vector6__f_prefix2;
    var x$78 = this.sci_Vector6__f_len12;
    var x$79 = this.sci_Vector6__f_prefix3;
    var x$80 = this.sci_Vector6__f_len123;
    var x$81 = this.sci_Vector6__f_prefix4;
    var x$82 = this.sci_Vector6__f_len1234;
    var x$83 = this.sci_Vector6__f_prefix5;
    var x$84 = this.sci_Vector6__f_len12345;
    var x$85 = this.sci_Vector6__f_data6;
    return new $c_sci_Vector6(x$75, x$76, x$77, x$78, x$79, x$80, x$81, x$82, x$83, x$84, x$85, x$69, x$70, x$71, x$72, a$3, x$74)
  } else if ((this.sci_Vector6__f_data6.u.length < 62)) {
    var x$86 = $asArrayOf_O($m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_data6, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix5, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix4, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix3, $m_sci_VectorStatics$().copyAppend__AO__O__AO(this.sci_Vector6__f_suffix2, this.sci_BigVector__f_suffix1))))), 6);
    var x$87 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty5;
    var x$88 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty4;
    var x$89 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty3;
    var x$90 = $m_sci_VectorStatics$().sci_VectorStatics$__f_empty2;
    var a$4 = new $ac_O(1);
    a$4.set(0, elem);
    var x$92 = ((1 + this.sci_BigVector__f_length0) | 0);
    var x$93 = this.sci_Vector__f_prefix1;
    var x$94 = this.sci_Vector6__f_len1;
    var x$95 = this.sci_Vector6__f_prefix2;
    var x$96 = this.sci_Vector6__f_len12;
    var x$97 = this.sci_Vector6__f_prefix3;
    var x$98 = this.sci_Vector6__f_len123;
    var x$99 = this.sci_Vector6__f_prefix4;
    var x$100 = this.sci_Vector6__f_len1234;
    var x$101 = this.sci_Vector6__f_prefix5;
    var x$102 = this.sci_Vector6__f_len12345;
    return new $c_sci_Vector6(x$93, x$94, x$95, x$96, x$97, x$98, x$99, x$100, x$101, x$102, x$86, x$87, x$88, x$89, x$90, a$4, x$92)
  } else {
    throw $ct_jl_IllegalArgumentException__(new $c_jl_IllegalArgumentException())
  }
});
$c_sci_Vector6.prototype.slice0__I__I__sci_Vector = (function(lo, hi) {
  var b = new $c_sci_VectorSliceBuilder(lo, hi);
  b.consider__I__AO__V(1, this.sci_Vector__f_prefix1);
  b.consider__I__AO__V(2, this.sci_Vector6__f_prefix2);
  b.consider__I__AO__V(3, this.sci_Vector6__f_prefix3);
  b.consider__I__AO__V(4, this.sci_Vector6__f_prefix4);
  b.consider__I__AO__V(5, this.sci_Vector6__f_prefix5);
  b.consider__I__AO__V(6, this.sci_Vector6__f_data6);
  b.consider__I__AO__V(5, this.sci_Vector6__f_suffix5);
  b.consider__I__AO__V(4, this.sci_Vector6__f_suffix4);
  b.consider__I__AO__V(3, this.sci_Vector6__f_suffix3);
  b.consider__I__AO__V(2, this.sci_Vector6__f_suffix2);
  b.consider__I__AO__V(1, this.sci_BigVector__f_suffix1);
  return b.result__sci_Vector()
});
$c_sci_Vector6.prototype.tail__sci_Vector = (function() {
  if ((this.sci_Vector6__f_len1 > 1)) {
    var a = this.sci_Vector__f_prefix1;
    var to = a.u.length;
    var x$1 = $m_ju_Arrays$().copyOfRange__AO__I__I__AO(a, 1, to);
    var x$2 = (((-1) + this.sci_Vector6__f_len1) | 0);
    var x$3 = (((-1) + this.sci_Vector6__f_len12) | 0);
    var x$4 = (((-1) + this.sci_Vector6__f_len123) | 0);
    var x$5 = (((-1) + this.sci_Vector6__f_len1234) | 0);
    var x$6 = (((-1) + this.sci_Vector6__f_len12345) | 0);
    var x$7 = (((-1) + this.sci_BigVector__f_length0) | 0);
    var x$8 = this.sci_Vector6__f_prefix2;
    var x$9 = this.sci_Vector6__f_prefix3;
    var x$10 = this.sci_Vector6__f_prefix4;
    var x$11 = this.sci_Vector6__f_prefix5;
    var x$12 = this.sci_Vector6__f_data6;
    var x$13 = this.sci_Vector6__f_suffix5;
    var x$14 = this.sci_Vector6__f_suffix4;
    var x$15 = this.sci_Vector6__f_suffix3;
    var x$16 = this.sci_Vector6__f_suffix2;
    var x$17 = this.sci_BigVector__f_suffix1;
    return new $c_sci_Vector6(x$1, x$2, x$8, x$3, x$9, x$4, x$10, x$5, x$11, x$6, x$12, x$13, x$14, x$15, x$16, x$17, x$7)
  } else {
    return this.slice0__I__I__sci_Vector(1, this.sci_BigVector__f_length0)
  }
});
$c_sci_Vector6.prototype.vectorSliceCount__I = (function() {
  return 11
});
$c_sci_Vector6.prototype.vectorSlice__I__AO = (function(idx) {
  switch (idx) {
    case 0: {
      return this.sci_Vector__f_prefix1;
      break
    }
    case 1: {
      return this.sci_Vector6__f_prefix2;
      break
    }
    case 2: {
      return this.sci_Vector6__f_prefix3;
      break
    }
    case 3: {
      return this.sci_Vector6__f_prefix4;
      break
    }
    case 4: {
      return this.sci_Vector6__f_prefix5;
      break
    }
    case 5: {
      return this.sci_Vector6__f_data6;
      break
    }
    case 6: {
      return this.sci_Vector6__f_suffix5;
      break
    }
    case 7: {
      return this.sci_Vector6__f_suffix4;
      break
    }
    case 8: {
      return this.sci_Vector6__f_suffix3;
      break
    }
    case 9: {
      return this.sci_Vector6__f_suffix2;
      break
    }
    case 10: {
      return this.sci_BigVector__f_suffix1;
      break
    }
    default: {
      throw new $c_s_MatchError(idx)
    }
  }
});
$c_sci_Vector6.prototype.tail__O = (function() {
  return this.tail__sci_Vector()
});
$c_sci_Vector6.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  if (((index >= 0) && (index < this.sci_BigVector__f_length0))) {
    var io = ((index - this.sci_Vector6__f_len12345) | 0);
    if ((io >= 0)) {
      var i6 = ((io >>> 25) | 0);
      var i5 = (31 & ((io >>> 20) | 0));
      var i4 = (31 & ((io >>> 15) | 0));
      var i3 = (31 & ((io >>> 10) | 0));
      var i2 = (31 & ((io >>> 5) | 0));
      var i1 = (31 & io);
      return ((i6 < this.sci_Vector6__f_data6.u.length) ? this.sci_Vector6__f_data6.get(i6).get(i5).get(i4).get(i3).get(i2).get(i1) : ((i5 < this.sci_Vector6__f_suffix5.u.length) ? this.sci_Vector6__f_suffix5.get(i5).get(i4).get(i3).get(i2).get(i1) : ((i4 < this.sci_Vector6__f_suffix4.u.length) ? this.sci_Vector6__f_suffix4.get(i4).get(i3).get(i2).get(i1) : ((i3 < this.sci_Vector6__f_suffix3.u.length) ? this.sci_Vector6__f_suffix3.get(i3).get(i2).get(i1) : ((i2 < this.sci_Vector6__f_suffix2.u.length) ? this.sci_Vector6__f_suffix2.get(i2).get(i1) : this.sci_BigVector__f_suffix1.get(i1))))))
    } else if ((index >= this.sci_Vector6__f_len1234)) {
      var io$2 = ((index - this.sci_Vector6__f_len1234) | 0);
      return this.sci_Vector6__f_prefix5.get(((io$2 >>> 20) | 0)).get((31 & ((io$2 >>> 15) | 0))).get((31 & ((io$2 >>> 10) | 0))).get((31 & ((io$2 >>> 5) | 0))).get((31 & io$2))
    } else if ((index >= this.sci_Vector6__f_len123)) {
      var io$3 = ((index - this.sci_Vector6__f_len123) | 0);
      return this.sci_Vector6__f_prefix4.get(((io$3 >>> 15) | 0)).get((31 & ((io$3 >>> 10) | 0))).get((31 & ((io$3 >>> 5) | 0))).get((31 & io$3))
    } else if ((index >= this.sci_Vector6__f_len12)) {
      var io$4 = ((index - this.sci_Vector6__f_len12) | 0);
      return this.sci_Vector6__f_prefix3.get(((io$4 >>> 10) | 0)).get((31 & ((io$4 >>> 5) | 0))).get((31 & io$4))
    } else if ((index >= this.sci_Vector6__f_len1)) {
      var io$5 = ((index - this.sci_Vector6__f_len1) | 0);
      return this.sci_Vector6__f_prefix2.get(((io$5 >>> 5) | 0)).get((31 & io$5))
    } else {
      return this.sci_Vector__f_prefix1.get(index)
    }
  } else {
    throw this.ioob__I__jl_IndexOutOfBoundsException(index)
  }
});
function $as_sci_Vector6(obj) {
  return (((obj instanceof $c_sci_Vector6) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.immutable.Vector6"))
}
function $isArrayOf_sci_Vector6(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sci_Vector6)))
}
function $asArrayOf_sci_Vector6(obj, depth) {
  return (($isArrayOf_sci_Vector6(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.immutable.Vector6;", depth))
}
var $d_sci_Vector6 = new $TypeData().initClass({
  sci_Vector6: 0
}, false, "scala.collection.immutable.Vector6", {
  sci_Vector6: 1,
  sci_BigVector: 1,
  sci_VectorImpl: 1,
  sci_Vector: 1,
  sci_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  sci_Seq: 1,
  sci_Iterable: 1,
  sci_SeqOps: 1,
  sci_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  sci_IndexedSeqOps: 1,
  sci_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_sci_Vector6.prototype.$classData = $d_sci_Vector6;
function $ct_scm_StringBuilder__jl_StringBuilder__($thiz, underlying) {
  $thiz.scm_StringBuilder__f_underlying = underlying;
  return $thiz
}
function $ct_scm_StringBuilder__($thiz) {
  $ct_scm_StringBuilder__jl_StringBuilder__($thiz, $ct_jl_StringBuilder__(new $c_jl_StringBuilder()));
  return $thiz
}
function $ct_scm_StringBuilder__I__($thiz, capacity) {
  $ct_scm_StringBuilder__jl_StringBuilder__($thiz, $ct_jl_StringBuilder__I__(new $c_jl_StringBuilder(), capacity));
  return $thiz
}
/** @constructor */
function $c_scm_StringBuilder() {
  this.scm_StringBuilder__f_underlying = null
}
$c_scm_StringBuilder.prototype = new $h_scm_AbstractSeq();
$c_scm_StringBuilder.prototype.constructor = $c_scm_StringBuilder;
/** @constructor */
function $h_scm_StringBuilder() {
  /*<skip>*/
}
$h_scm_StringBuilder.prototype = $c_scm_StringBuilder.prototype;
$c_scm_StringBuilder.prototype.stringPrefix__T = (function() {
  return "IndexedSeq"
});
$c_scm_StringBuilder.prototype.iterator__sc_Iterator = (function() {
  var this$1 = new $c_sc_IndexedSeqView$Id(this);
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1)
});
$c_scm_StringBuilder.prototype.drop__I__O = (function(n) {
  return $f_sc_IndexedSeqOps__drop__I__O(this, n)
});
$c_scm_StringBuilder.prototype.head__O = (function() {
  return $bC(this.scm_StringBuilder__f_underlying.charAt__I__C(0))
});
$c_scm_StringBuilder.prototype.lengthCompare__I__I = (function(len) {
  var x = this.scm_StringBuilder__f_underlying.length__I();
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_scm_StringBuilder.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_scm_StringBuilder.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, xs)
});
$c_scm_StringBuilder.prototype.length__I = (function() {
  return this.scm_StringBuilder__f_underlying.length__I()
});
$c_scm_StringBuilder.prototype.knownSize__I = (function() {
  return this.scm_StringBuilder__f_underlying.length__I()
});
$c_scm_StringBuilder.prototype.addOne__C__scm_StringBuilder = (function(x) {
  var this$1 = this.scm_StringBuilder__f_underlying;
  var str = $as_T(String.fromCharCode(x));
  this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str);
  return this
});
$c_scm_StringBuilder.prototype.toString__T = (function() {
  return this.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
$c_scm_StringBuilder.prototype.append__T__scm_StringBuilder = (function(s) {
  var this$1 = this.scm_StringBuilder__f_underlying;
  this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content) + s);
  return this
});
$c_scm_StringBuilder.prototype.appendAll__sc_IterableOnce__scm_StringBuilder = (function(xs) {
  if ((xs instanceof $c_sci_WrappedString)) {
    var x2 = $as_sci_WrappedString(xs);
    var this$3 = this.scm_StringBuilder__f_underlying;
    $m_sci_WrappedString$();
    var str = x2.sci_WrappedString__f_scala$collection$immutable$WrappedString$$self;
    this$3.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$3.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str)
  } else if (false) {
    var x3 = $as_scm_ArraySeq$ofChar(xs);
    this.scm_StringBuilder__f_underlying.append__AC__jl_StringBuilder(x3.array__AC())
  } else if ((xs instanceof $c_scm_StringBuilder)) {
    var x4 = $as_scm_StringBuilder(xs);
    var this$4 = this.scm_StringBuilder__f_underlying;
    var s = x4.scm_StringBuilder__f_underlying;
    this$4.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$4.jl_StringBuilder__f_java$lang$StringBuilder$$content) + s)
  } else {
    var ks = xs.knownSize__I();
    if ((ks !== 0)) {
      var b = this.scm_StringBuilder__f_underlying;
      if ((ks > 0)) {
        b.length__I()
      };
      var it = xs.iterator__sc_Iterator();
      while (it.hasNext__Z()) {
        var c = $uC(it.next__O());
        var str$1 = $as_T(String.fromCharCode(c));
        b.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + b.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str$1)
      }
    }
  };
  return this
});
$c_scm_StringBuilder.prototype.append__I__scm_StringBuilder = (function(x) {
  var this$1 = this.scm_StringBuilder__f_underlying;
  var str = ("" + x);
  this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content = (this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content + str);
  return this
});
$c_scm_StringBuilder.prototype.append__J__scm_StringBuilder = (function(x) {
  var this$1 = this.scm_StringBuilder__f_underlying;
  var str = $m_RTLong$().org$scalajs$linker$runtime$RuntimeLong$$toString__I__I__T(x.RTLong__f_lo, x.RTLong__f_hi);
  this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str);
  return this
});
$c_scm_StringBuilder.prototype.append__C__scm_StringBuilder = (function(x) {
  var this$1 = this.scm_StringBuilder__f_underlying;
  var str = $as_T(String.fromCharCode(x));
  this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content = (("" + this$1.jl_StringBuilder__f_java$lang$StringBuilder$$content) + str);
  return this
});
$c_scm_StringBuilder.prototype.deleteCharAt__I__scm_StringBuilder = (function(index) {
  this.scm_StringBuilder__f_underlying.deleteCharAt__I__jl_StringBuilder(index);
  return this
});
$c_scm_StringBuilder.prototype.setCharAt__I__C__scm_StringBuilder = (function(index, ch) {
  this.scm_StringBuilder__f_underlying.setCharAt__I__C__V(index, ch);
  return this
});
$c_scm_StringBuilder.prototype.isEmpty__Z = (function() {
  return (this.scm_StringBuilder__f_underlying.length__I() === 0)
});
$c_scm_StringBuilder.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_scm_IndexedSeq$()
});
$c_scm_StringBuilder.prototype.result__O = (function() {
  return this.scm_StringBuilder__f_underlying.jl_StringBuilder__f_java$lang$StringBuilder$$content
});
$c_scm_StringBuilder.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__C__scm_StringBuilder($uC(elem))
});
$c_scm_StringBuilder.prototype.fromSpecific__sc_IterableOnce__O = (function(coll) {
  return $ct_scm_StringBuilder__(new $c_scm_StringBuilder()).appendAll__sc_IterableOnce__scm_StringBuilder(coll)
});
$c_scm_StringBuilder.prototype.fromSpecific__sc_IterableOnce__sc_IterableOps = (function(coll) {
  return $ct_scm_StringBuilder__(new $c_scm_StringBuilder()).appendAll__sc_IterableOnce__scm_StringBuilder(coll)
});
$c_scm_StringBuilder.prototype.apply__O__O = (function(v1) {
  var i = $uI(v1);
  return $bC(this.scm_StringBuilder__f_underlying.charAt__I__C(i))
});
$c_scm_StringBuilder.prototype.apply__I__O = (function(i) {
  return $bC(this.scm_StringBuilder__f_underlying.charAt__I__C(i))
});
function $as_scm_StringBuilder(obj) {
  return (((obj instanceof $c_scm_StringBuilder) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.StringBuilder"))
}
function $isArrayOf_scm_StringBuilder(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_StringBuilder)))
}
function $asArrayOf_scm_StringBuilder(obj, depth) {
  return (($isArrayOf_scm_StringBuilder(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.StringBuilder;", depth))
}
var $d_scm_StringBuilder = new $TypeData().initClass({
  scm_StringBuilder: 0
}, false, "scala.collection.mutable.StringBuilder", {
  scm_StringBuilder: 1,
  scm_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  scm_Seq: 1,
  scm_Iterable: 1,
  scm_SeqOps: 1,
  scm_Cloneable: 1,
  jl_Cloneable: 1,
  scm_ReusableBuilder: 1,
  scm_Builder: 1,
  scm_Growable: 1,
  scm_Clearable: 1,
  scm_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  scm_IndexedSeqOps: 1,
  jl_CharSequence: 1,
  Ljava_io_Serializable: 1
});
$c_scm_StringBuilder.prototype.$classData = $d_scm_StringBuilder;
function $as_scm_ListBuffer(obj) {
  return ((false || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.ListBuffer"))
}
function $isArrayOf_scm_ListBuffer(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_ListBuffer)))
}
function $asArrayOf_scm_ListBuffer(obj, depth) {
  return (($isArrayOf_scm_ListBuffer(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.ListBuffer;", depth))
}
function $ct_scm_ArrayBuffer__AO__I__($thiz, initialElements, initialSize) {
  $thiz.scm_ArrayBuffer__f_array = initialElements;
  $thiz.scm_ArrayBuffer__f_size0 = initialSize;
  return $thiz
}
function $ct_scm_ArrayBuffer__($thiz) {
  $ct_scm_ArrayBuffer__AO__I__($thiz, new $ac_O(16), 0);
  return $thiz
}
/** @constructor */
function $c_scm_ArrayBuffer() {
  this.scm_ArrayBuffer__f_array = null;
  this.scm_ArrayBuffer__f_size0 = 0
}
$c_scm_ArrayBuffer.prototype = new $h_scm_AbstractBuffer();
$c_scm_ArrayBuffer.prototype.constructor = $c_scm_ArrayBuffer;
/** @constructor */
function $h_scm_ArrayBuffer() {
  /*<skip>*/
}
$h_scm_ArrayBuffer.prototype = $c_scm_ArrayBuffer.prototype;
$c_scm_ArrayBuffer.prototype.iterator__sc_Iterator = (function() {
  var this$1 = new $c_scm_ArrayBufferView(this.scm_ArrayBuffer__f_array, this.scm_ArrayBuffer__f_size0);
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1)
});
$c_scm_ArrayBuffer.prototype.drop__I__O = (function(n) {
  return $f_sc_IndexedSeqOps__drop__I__O(this, n)
});
$c_scm_ArrayBuffer.prototype.head__O = (function() {
  return this.apply__I__O(0)
});
$c_scm_ArrayBuffer.prototype.lengthCompare__I__I = (function(len) {
  var x = this.scm_ArrayBuffer__f_size0;
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_scm_ArrayBuffer.prototype.knownSize__I = (function() {
  return this.scm_ArrayBuffer__f_size0
});
$c_scm_ArrayBuffer.prototype.ensureSize__I__V = (function(n) {
  this.scm_ArrayBuffer__f_array = $m_scm_ArrayBuffer$().scala$collection$mutable$ArrayBuffer$$ensureSize__AO__I__I__AO(this.scm_ArrayBuffer__f_array, this.scm_ArrayBuffer__f_size0, n)
});
$c_scm_ArrayBuffer.prototype.apply__I__O = (function(n) {
  var hi = ((1 + n) | 0);
  if ((n < 0)) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (((n + " is out of bounds (min 0, max ") + (((-1) + this.scm_ArrayBuffer__f_size0) | 0)) + ")"))
  };
  if ((hi > this.scm_ArrayBuffer__f_size0)) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), ((((((-1) + hi) | 0) + " is out of bounds (min 0, max ") + (((-1) + this.scm_ArrayBuffer__f_size0) | 0)) + ")"))
  };
  return this.scm_ArrayBuffer__f_array.get(n)
});
$c_scm_ArrayBuffer.prototype.update__I__O__V = (function(index, elem) {
  var hi = ((1 + index) | 0);
  if ((index < 0)) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), (((index + " is out of bounds (min 0, max ") + (((-1) + this.scm_ArrayBuffer__f_size0) | 0)) + ")"))
  };
  if ((hi > this.scm_ArrayBuffer__f_size0)) {
    throw $ct_jl_IndexOutOfBoundsException__T__(new $c_jl_IndexOutOfBoundsException(), ((((((-1) + hi) | 0) + " is out of bounds (min 0, max ") + (((-1) + this.scm_ArrayBuffer__f_size0) | 0)) + ")"))
  };
  this.scm_ArrayBuffer__f_array.set(index, elem)
});
$c_scm_ArrayBuffer.prototype.length__I = (function() {
  return this.scm_ArrayBuffer__f_size0
});
$c_scm_ArrayBuffer.prototype.addOne__O__scm_ArrayBuffer = (function(elem) {
  var i = this.scm_ArrayBuffer__f_size0;
  this.ensureSize__I__V(((1 + this.scm_ArrayBuffer__f_size0) | 0));
  this.scm_ArrayBuffer__f_size0 = ((1 + this.scm_ArrayBuffer__f_size0) | 0);
  this.update__I__O__V(i, elem);
  return this
});
$c_scm_ArrayBuffer.prototype.addAll__sc_IterableOnce__scm_ArrayBuffer = (function(elems) {
  if ((elems instanceof $c_scm_ArrayBuffer)) {
    var x2 = $as_scm_ArrayBuffer(elems);
    this.ensureSize__I__V(((this.scm_ArrayBuffer__f_size0 + x2.scm_ArrayBuffer__f_size0) | 0));
    $m_s_Array$().copy__O__I__O__I__I__V(x2.scm_ArrayBuffer__f_array, 0, this.scm_ArrayBuffer__f_array, this.scm_ArrayBuffer__f_size0, x2.scm_ArrayBuffer__f_size0);
    this.scm_ArrayBuffer__f_size0 = ((this.scm_ArrayBuffer__f_size0 + x2.scm_ArrayBuffer__f_size0) | 0)
  } else {
    $f_scm_Growable__addAll__sc_IterableOnce__scm_Growable(this, elems)
  };
  return this
});
$c_scm_ArrayBuffer.prototype.stringPrefix__T = (function() {
  return "ArrayBuffer"
});
$c_scm_ArrayBuffer.prototype.copyToArray__O__I__I__I = (function(xs, start, len) {
  var srcLen = this.scm_ArrayBuffer__f_size0;
  var destLen = $m_sr_ScalaRunTime$().array_length__O__I(xs);
  var x = ((len < srcLen) ? len : srcLen);
  var y = ((destLen - start) | 0);
  var x$1 = ((x < y) ? x : y);
  var copied = ((x$1 > 0) ? x$1 : 0);
  if ((copied > 0)) {
    $m_s_Array$().copy__O__I__O__I__I__V(this.scm_ArrayBuffer__f_array, 0, xs, start, copied)
  };
  return copied
});
$c_scm_ArrayBuffer.prototype.addAll__sc_IterableOnce__scm_Growable = (function(xs) {
  return this.addAll__sc_IterableOnce__scm_ArrayBuffer(xs)
});
$c_scm_ArrayBuffer.prototype.addOne__O__scm_Growable = (function(elem) {
  return this.addOne__O__scm_ArrayBuffer(elem)
});
$c_scm_ArrayBuffer.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_scm_ArrayBuffer$()
});
$c_scm_ArrayBuffer.prototype.apply__O__O = (function(v1) {
  return this.apply__I__O($uI(v1))
});
function $as_scm_ArrayBuffer(obj) {
  return (((obj instanceof $c_scm_ArrayBuffer) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.collection.mutable.ArrayBuffer"))
}
function $isArrayOf_scm_ArrayBuffer(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.scm_ArrayBuffer)))
}
function $asArrayOf_scm_ArrayBuffer(obj, depth) {
  return (($isArrayOf_scm_ArrayBuffer(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.collection.mutable.ArrayBuffer;", depth))
}
var $d_scm_ArrayBuffer = new $TypeData().initClass({
  scm_ArrayBuffer: 0
}, false, "scala.collection.mutable.ArrayBuffer", {
  scm_ArrayBuffer: 1,
  scm_AbstractBuffer: 1,
  scm_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  scm_Seq: 1,
  scm_Iterable: 1,
  scm_SeqOps: 1,
  scm_Cloneable: 1,
  jl_Cloneable: 1,
  scm_Buffer: 1,
  scm_Growable: 1,
  scm_Clearable: 1,
  scm_Shrinkable: 1,
  scm_IndexedBuffer: 1,
  scm_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  scm_IndexedSeqOps: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scg_DefaultSerializable: 1,
  Ljava_io_Serializable: 1
});
$c_scm_ArrayBuffer.prototype.$classData = $d_scm_ArrayBuffer;
function $ct_sjs_js_WrappedArray__sjs_js_Array__($thiz, array) {
  $thiz.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array = array;
  return $thiz
}
function $ct_sjs_js_WrappedArray__($thiz) {
  $ct_sjs_js_WrappedArray__sjs_js_Array__($thiz, []);
  return $thiz
}
/** @constructor */
function $c_sjs_js_WrappedArray() {
  this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array = null
}
$c_sjs_js_WrappedArray.prototype = new $h_scm_AbstractBuffer();
$c_sjs_js_WrappedArray.prototype.constructor = $c_sjs_js_WrappedArray;
/** @constructor */
function $h_sjs_js_WrappedArray() {
  /*<skip>*/
}
$h_sjs_js_WrappedArray.prototype = $c_sjs_js_WrappedArray.prototype;
$c_sjs_js_WrappedArray.prototype.sizeHint__I__V = (function(size) {
  /*<skip>*/
});
$c_sjs_js_WrappedArray.prototype.stringPrefix__T = (function() {
  return "IndexedSeq"
});
$c_sjs_js_WrappedArray.prototype.iterator__sc_Iterator = (function() {
  var this$1 = new $c_sc_IndexedSeqView$Id(this);
  return new $c_sc_IndexedSeqView$IndexedSeqViewIterator(this$1)
});
$c_sjs_js_WrappedArray.prototype.drop__I__O = (function(n) {
  return $f_sc_IndexedSeqOps__drop__I__O(this, n)
});
$c_sjs_js_WrappedArray.prototype.head__O = (function() {
  return this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array[0]
});
$c_sjs_js_WrappedArray.prototype.lengthCompare__I__I = (function(len) {
  var x = $uI(this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array.length);
  return ((x === len) ? 0 : ((x < len) ? (-1) : 1))
});
$c_sjs_js_WrappedArray.prototype.apply__I__O = (function(index) {
  return this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array[index]
});
$c_sjs_js_WrappedArray.prototype.length__I = (function() {
  return $uI(this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array.length)
});
$c_sjs_js_WrappedArray.prototype.knownSize__I = (function() {
  return $uI(this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array.length)
});
$c_sjs_js_WrappedArray.prototype.className__T = (function() {
  return "WrappedArray"
});
$c_sjs_js_WrappedArray.prototype.result__O = (function() {
  return this
});
$c_sjs_js_WrappedArray.prototype.addOne__O__scm_Growable = (function(elem) {
  this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array.push(elem);
  return this
});
$c_sjs_js_WrappedArray.prototype.apply__O__O = (function(v1) {
  var index = $uI(v1);
  return this.sjs_js_WrappedArray__f_scala$scalajs$js$WrappedArray$$array[index]
});
$c_sjs_js_WrappedArray.prototype.iterableFactory__sc_IterableFactory = (function() {
  return $m_sjs_js_WrappedArray$()
});
function $as_sjs_js_WrappedArray(obj) {
  return (((obj instanceof $c_sjs_js_WrappedArray) || (obj === null)) ? obj : $throwClassCastException(obj, "scala.scalajs.js.WrappedArray"))
}
function $isArrayOf_sjs_js_WrappedArray(obj, depth) {
  return (!(!(((obj && obj.$classData) && (obj.$classData.arrayDepth === depth)) && obj.$classData.arrayBase.ancestors.sjs_js_WrappedArray)))
}
function $asArrayOf_sjs_js_WrappedArray(obj, depth) {
  return (($isArrayOf_sjs_js_WrappedArray(obj, depth) || (obj === null)) ? obj : $throwArrayCastException(obj, "Lscala.scalajs.js.WrappedArray;", depth))
}
var $d_sjs_js_WrappedArray = new $TypeData().initClass({
  sjs_js_WrappedArray: 0
}, false, "scala.scalajs.js.WrappedArray", {
  sjs_js_WrappedArray: 1,
  scm_AbstractBuffer: 1,
  scm_AbstractSeq: 1,
  sc_AbstractSeq: 1,
  sc_AbstractIterable: 1,
  O: 1,
  sc_Iterable: 1,
  sc_IterableOnce: 1,
  sc_IterableOps: 1,
  sc_IterableOnceOps: 1,
  sc_IterableFactoryDefaults: 1,
  sc_Seq: 1,
  s_PartialFunction: 1,
  F1: 1,
  sc_SeqOps: 1,
  s_Equals: 1,
  scm_Seq: 1,
  scm_Iterable: 1,
  scm_SeqOps: 1,
  scm_Cloneable: 1,
  jl_Cloneable: 1,
  scm_Buffer: 1,
  scm_Growable: 1,
  scm_Clearable: 1,
  scm_Shrinkable: 1,
  sc_StrictOptimizedSeqOps: 1,
  sc_StrictOptimizedIterableOps: 1,
  scm_IndexedSeq: 1,
  sc_IndexedSeq: 1,
  sc_IndexedSeqOps: 1,
  scm_IndexedSeqOps: 1,
  scm_IndexedBuffer: 1,
  scm_Builder: 1,
  Ljava_io_Serializable: 1
});
$c_sjs_js_WrappedArray.prototype.$classData = $d_sjs_js_WrappedArray;
$L0 = new $c_RTLong(0, 0);
$d_J.zero = $L0;
$sct_Ljava_time_zone_TzdbZoneRulesProvider__stinit__();
$s_Ldev_myclinic_web_JsMain__main__AT__V(new ($d_T.getArrayOf().constr)([]));
}).call(this);
//# sourceMappingURL=main.js.map
