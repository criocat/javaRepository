"use strict"
function coul (...s) {
    console.log(Array.prototype.map.call(s, String).join(' '));
}


const AbstractOperation = {
    toString : function() {
        let res = []
        for (const exp of this.exps) res.push(exp)
        res.push(this.opStr)
        return res.join(' ')
    },
    baseAddToArray: function(arr, isReverse) {
        arr.push("(" + (isReverse ? "" : this.opStr))
        for (const curexp of this.exps) {
            if (!isReverse) {
                arr.push(" ")
            }
            curexp.baseAddToArray(arr, isReverse)
            if (isReverse) {
                arr.push(" ")
            }
         }
        arr.push((isReverse ? this.opStr : "") + ")")
    },
    baseGetString: function(isReverse) {
        let arr = []
        this.baseAddToArray(arr, isReverse)
        return arr.join("")
    },
    prefix : function() {
        return this.baseGetString(false);
    },
    postfix : function() {
        return this.baseGetString(true);
    },
    exps : []
}

function getFactory(opStr, funcRes, diffFunction, countArguments = funcRes.length) {
    const Func = function(...expressions) {
        this.exps =  expressions.slice(0, countArguments)
    }
    Func.prototype = Object.create(AbstractOperation)
    Func.prototype.opStr = opStr
    Func.prototype.countArgs = countArguments
    Func.prototype.constructor = Func;
    Func.prototype.evaluate = function(x, y, z) {
        return funcRes(...this.exps.map((exp) => exp.evaluate(x, y, z)))
    }
    Func.prototype.diff = function(diffStr) {
        // NOTE
        // const
        let diffExps = this.exps.map((exp) => exp.diff(diffStr))
        return diffFunction(diffStr, this.exps, diffExps)
    }
    return Func
}


const Add = getFactory(
    "+",
    // :NOTE: .length
    (a, b) => a + b,
    (diffStr, exps, diffExps) => new Add(diffExps[0], diffExps[1])
)

const Subtract = getFactory(
    "-",
    (a, b) => a - b,
    (diffStr, exps, diffExps) => new Subtract(diffExps[0], diffExps[1])
)

const Multiply = getFactory(
    "*",
    (a, b) => a * b,
    // :NOTE: reformat
    (diffStr, exps, diffExps) => new Add(
        new Multiply(exps[0], diffExps[1]),
        new Multiply(exps[1], diffExps[0])
    )
)

const Divide = getFactory(
    "/",
    (a, b) => a / b,
    (diffStr, exps, diffExps) => new Divide(
        new Subtract(
            new Multiply(exps[1], diffExps[0]),
            new Multiply(exps[0], diffExps[1])
        ),
        new Multiply(exps[1], exps[1])
    )
)

const Negate = getFactory(
    "negate",
    (a) => -a,
    (diffStr, exps, diffExps) => new Negate(diffExps[0])
)

const Hypot = getFactory(
    "hypot",
    (a, b) => a * a + b * b,
    (diffStr, exps, diffExps) => new Add(
        new Multiply(
            Const.TWO,
            new Multiply(exps[0], diffExps[0])),
        new Multiply(
            Const.TWO,
            new Multiply(exps[1], diffExps[1])
        )
    )
)

const HMean = getFactory(
    "hmean",
    (a, b) => 2 / ((1 / a) + (1 / b)),
    // :NOTE: new Const(2)
    (diffStr, exps, diffExps) => new Multiply(
        Const.TWO,
        new Divide(
            new Add(
                new Multiply(
                    diffExps[1],
                    new Multiply(exps[0], exps[0])),
                new Multiply(
                    diffExps[0],
                    new Multiply(exps[1], exps[1]))),
            new Multiply(
                new Add(exps[0], exps[1]),
                new Add(exps[0], exps[1])
            )
        )
    )
)

const ArithMean = getFactory(
    "arithMean",
    (...exps) => exps.reduce((a, b) => a + b, 0) / exps.length,
    (diffStr, exps, diffExps) => new Divide(
        diffExps.reduce(
            (exp1, exp2) => new Add(exp1, exp2),
            Const.ZERO
        ),
        new Const(exps.length)
    ),
    Infinity
)


const GeomMean = getFactory(
    "geomMean",
    (...exps) => Math.pow(exps.reduce((a, b) => a * Math.abs(b), 1), 1 / exps.length),
    (diffStr, exps, diffExps) => new Pow(
            exps.reduce(
                (exp1, exp2) => new Multiply(exp1, exp2),
                new Const(1)
            ),
            new Const(1 / exps.length)
    ).diff(diffStr),
    Infinity
)

const HarmMean = getFactory(
    "harmMean",
    (...exps) => exps.length / (exps.reduce((a, b) => a + (1 / b), 0)),
    (diffStr, exps, diffExps) => new Divide(
        new Const(exps.length),
        exps.reduce(
            (exp1, exp2) => new Add(
                exp1,
                new Divide(new Const(1), exp2)),
             Const.ZERO
        )
    ).diff(diffStr),
    Infinity
)

const Pow = getFactory(
    "pow",
    (a, b) => Math.pow(a, b),
    (diffStr, exps, diffExps) => new Multiply(
        new Multiply(
            new Const(exps[1]),
            new Pow(
                exps[0],
                new Subtract(exps[1], new Const(1))
            )
        ),
        diffExps[0]
    )
)



function Const(number) {
    this.num = number
}

Const.prototype.evaluate = function(x, y, z) { return this.num }
Const.prototype.toString = function() { return this.num + "" }
Const.prototype.diff = (str) => (Const.ZERO)
Const.prototype.prefix = function() { return this.num + "" }
Const.prototype.postfix = function() { return this.num + "" }
Const.prototype.baseAddToArray = function(arr) { arr.push(this.num) }
Const.prototype.constructor = Const

Const.ZERO = new Const(0)
Const.ONE = new Const(1)
Const.TWO = new Const(2)

function Variable(name) {
    this.str = name
}

Variable.prototype.evaluate = function(x, y, z) { return {"x" : x, "y" : y, "z" : z}[this.str] }
Variable.prototype.toString = function() { return this.str }
Variable.prototype.diff = function(diffStr) { return new Const(this.str === diffStr ? 1 : 0) }
Variable.prototype.prefix = function() { return this.str}
Variable.prototype.postfix = function() { return this.str}
Variable.prototype.baseAddToArray = function(arr) { arr.push(this.str) }
Variable.prototype.constructor = Variable


const getExpByStr = {
    "+" : Add,
    "-" :Subtract,
    "*" : Multiply,
    "/" : Divide,
    "negate" : Negate,
    "hypot" : Hypot,
    "hmean" : HMean,
    "arithMean" : ArithMean,
    "geomMean" : GeomMean,
    "harmMean" : HarmMean
}
const isNumber = (val) => isFinite(val) && val !== ""

const variables = {
    "x" : new Variable("x"),
    "y" : new Variable("y"),
    "z" : new Variable("z")
}

function getExpInStack(val) {
    if (isNumber(val)) {
        return new Const(Number(val))
    }
    return variables[val]
}


function parse(str) {
    const st = []
    for (const val of str.trim().split(/\s+/)) {
        if (val in getExpByStr) {
            const cnt = getExpByStr[val].prototype.countArgs
            st.push(new getExpByStr[val](...st.splice(-cnt)))
        } else {
            st.push(getExpInStack(val))
        }
    }
    return st[0]
}

function parsingError(message, str, expArray, pos) {
    this.message = message + ", at position " + getPositionInString(str, expArray, pos)
}

parsingError.prototype = Object.create(Error.prototype)
parsingError.prototype.constructor = parsingError
parsingError.prototype.name = "ParsingError"


function parsePartInPrefix(str, expArray, pos, reverse) {
    if (expArray[pos.val] !== '(') {
            if (expArray[pos.val] in getExpInStack) {
                return getExpByStr[expArray[pos.val++]]
            } else if (["x", "y", "z"].includes(expArray[pos.val]) || isNumber(expArray[pos.val])) {
                return getExpInStack(expArray[pos.val++])
            } else {
                throw new parsingError("invalid token: \"" + expArray[pos.val] + "\"", str, expArray, pos.val)
            }
    }
    else {
        if (pos.val + 1 === expArray.length) {
            throw new parsingError("nothing after the bracket", str, expArray, pos.val)
        } else if(!(expArray[pos.val + 1]  in getExpByStr) && !reverse) {
            throw new parsingError("\"" + expArray[pos.val + 1] + "\" not is a valid operation", str, expArray, pos.val + 1)
        }
        let op
        let opLength = Infinity
        if (reverse === false) {
            op = getExpByStr[expArray[++pos.val]]
            opLength = op.prototype.countArgs
        }
        pos.val++
        let exps = []
        for (let i = 0; expArray[pos.val] !== ")" && !(expArray[pos.val] in getExpByStr) && i < opLength ; ++i) {
            if (pos.val >= expArray.length) {
                throw new parsingError("cannot parse " + i + "-th argument of function " + op.opStr +
                 ": require more arguments", str, expArray, pos.val - 1);
            }
            exps.push(parsePartInPrefix(str, expArray, pos, reverse))
        }
        if (reverse) {
              if(!(expArray[pos.val]  in getExpByStr) && !reverse) {
                   throw new parsingError("\"" + expArray[pos.val + 1] + "\" not is a valid operation", str, expArray, pos.val + 1)
              }
              op = getExpByStr[expArray[pos.val++]]
        }
        if (expArray[pos.val] !== ")") {
            throw new parsingError("expected close bracket", str, expArray, pos.val)
        }
        // NOTE
        // !==
        if (exps.length != op.prototype.countArgs && op.prototype.countArgs != Infinity) {
            throw new parsingError("invalid number of arguments", str, expArray, pos.val)
        }
        pos.val++
        return new op(...exps)
    }
}

function getPositionInString(str, expArray, expPos) {
    let sum = expArray.slice(0, expPos + 1).reduce((a, b) => a + b.length, 0)
    let res = 0
    for (const c of str) {
        if (!(/\s/.test(c))) {
            sum--
        }
        if (sum === 0) break
        res++
    }
    return res
}

function baseParse(str, reverse) {
    const expArray = str.replace((/([()])/g), ' $1 ').trim().split(/\s+/)
    let pos = {
        val : 0
    }
    let res = parsePartInPrefix(str, expArray, pos, reverse)
    if (pos.val !== expArray.length) {
        throw new Error("cannot parse all expression, only part" + ", at position " +
            getPositionInString(str, expArray, pos.val))
    }
    return res
}

function parsePrefix(str) {
    return baseParse(str, false)
}


function parsePostfix(str) {
    return baseParse(str, true)
}

