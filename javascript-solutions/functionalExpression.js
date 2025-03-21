"use strict"
const coul = (...s) => {
    console.log(Array.prototype.map.call(s, String).join(' '));
}
const cout = (...s) => {
    process.stdout.write(Array.prototype.map.call(s, String).join(' '));
}




const cnst = (num) => () => num

const pi = cnst(Math.PI)

const e = cnst(Math.E)

const variable = (str) => (x, y, z) => ({"x" : x, "y" : y, "z" : z}[str])

const getOperation = (count, func) => (...exps) => (x, y, z) => func(exps.slice(0, count).map((num) => num(x, y, z)))

const add = getOperation(2, (exps) => exps[0] + exps[1])

const subtract = getOperation(2, (exps) => exps[0] - exps[1])

const multiply = getOperation(2, (exps) => exps[0] * exps[1])

const divide = getOperation(2, (exps) => exps[0] / exps[1])

const negate = getOperation(1, (exps) => -exps[0])

const med3 = getOperation(3, (exps) => exps.sort((a, b) => (a - b))[1])

const avg5 = getOperation(5, (exps) => exps.reduce((a, b) => (a + b)) / 5)



const getExpByStr = {"+" : [add, 2], "-" : [subtract, 2], "*" : [multiply, 2], "/" : [divide, 2], "negate" : [negate, 1], "med3" : [med3, 3], "avg5" : [avg5, 5]}
const isNumber = (val) => !isNaN(Number(val))
const isOperation = (str) => str in getExpByStr
const getExpression = (strOp, ...rest) => getExpByStr[strOp][0](...rest)
const getExpInStack = (val) => {
    if (val === "pi" || val === "e") {
        return (val === "pi" ? pi : e)
    }
    if (isNumber(val)) {
        return cnst(Number(val))
    }
    return variable(val)
}
const parse = (str) => {
    const arr = str.trim().split(/\s+/)
    const st = []
    for (const val of arr) {
        if (isOperation(val)) {
            const cnt = getExpByStr[val][1]
            const mas = st.slice(-cnt)
            st.splice(st.length - cnt, cnt)
            st.push(getExpression(val, ...mas))
        }
        else {
            st.push(getExpInStack(val))
        }
    }
    return st[0]
}

const exp = add(
    subtract(
        multiply(variable("x"), variable("x")),
        multiply(cnst(2), variable("x"))
    ),
    cnst(1)
)
for (let i = 0; i <= 10; ++i) {
    coul(exp(i, 0, 0))
}

