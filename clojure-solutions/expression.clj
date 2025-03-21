(defn constant [val] (constantly val))

(defn variable [str] (fn [m] (m str)))

(defn abstractOperation [f defaultValue] (fn [& exps] (if (== (count exps) 1)
                                                        (fn [m] (reduce f defaultValue (mapv #(% m) exps)))
                                                        (fn [m] (reduce f (mapv #(% m) exps))))))

(defn abstractUnaryOperation [f] (fn [exp] (fn [m] (f (exp m)))))

(def add (abstractOperation + 0))

(def subtract (abstractOperation - 0))

(def multiply (abstractOperation * 1))

(defn power [& exps] (fn [m] (java.lang.Math/pow ((nth exps 0) m) ((nth exps 1) m))))

(def divide (abstractOperation #(/ (double %1) (double %2)) 1))

(def negate (abstractUnaryOperation -))

(defn arithMean [& exps] (divide (apply add exps) (constant (count exps))))

(defn absExp [exp] (fn [m] (abs (exp m))))

(defn geomMean [& exps] (power (absExp (reduce multiply (constant 1) exps)) (divide (constant 1) (constant (count exps)))))

(defn harmMean [& exps] (divide (constant (count exps)) (reduce add (constant 0) (mapv #(divide (constant 1) %) exps))))

(def functions {"+" add "-" subtract "*" multiply "/" divide "negate" negate "arithMean" arithMean "geomMean" geomMean "harmMean" harmMean})


(defn abstractParseFunction [funcList, thisConst, thisVar] (fn [curstr] ((fn partCalculation [o] (cond
                                                                                                   (number? o) (thisConst o)
                                                                                                   (= (type o) clojure.lang.PersistentList) (apply (funcList (str (first o))) (mapv partCalculation (rest o)))
                                                                                                   :else (thisVar (str o))
                                                                                                   )) (read-string curstr))))

(def parseFunction (abstractParseFunction functions, constant, variable))



(defn proto-get
  ([obj val] (proto-get obj val nil))
  ([obj val default] (cond
                       (contains? obj val) (obj val)
                       (contains? obj :prototype) (proto-get (obj :prototype) val default)
                       :else default)))

(defn proto-call [this key & args] (apply (proto-get this key) this args))

(defn field [key] (fn [this] (proto-get this key)))

(defn method [key] (fn [this & args] (apply proto-call this key args)))

(def __exps (field :exps))
(def __opStr (field :opStr))
(def __var (field :var))
(def __val (field :val))
(def __op (field :op))

(def evaluate (method :evaluate))
(def _toString (method :toString))
(def _toStringPostfix (method :toStringPostfix))
(def _toStringInfix (method :toStringInfix))
(defn toString [this] (str (_toString this)))
(defn toStringPostfix [this] (str (_toStringPostfix this)))
(defn toStringInfix [this] (_toStringInfix this))
(def diff (method :diff))
(def advancedDiff (method :advancedDiff))


(declare Add Subtract Multiply Divide Negate Constant Variable Power ArithMean GeomMean HarmMean)


(def ExpressionPrototype {
                          :exps            ()
                          :opStr           nil
                          :op              nil
                          :toString        (fn [this] (let [lst (apply list (symbol (__opStr this)) (map _toString (__exps this)))] (if (== (count lst) 1)
                                                                                                                                      (list (symbol (__opStr this)) (symbol ""))
                                                                                                                                      lst)))
                          :toStringPostfix (fn [this] (let [lst (apply list (conj (mapv _toStringPostfix (__exps this)) (symbol (__opStr this))))] (if (== (count lst) 1)
                                                                                                                                                     (list (symbol (__opStr this)) (symbol ""))
                                                                                                                                                     lst)))
                          :toStringInfix   (fn [this] (let [curstr (let [strs (mapv _toStringInfix (__exps this))] (cond (== (count strs) 2)
                                                                                                                     (str "(" (strs 0) " " (__opStr this) " " (strs 1) ")")
                                                                                                                         (or (some #(= % (__opStr this)) ["sinp" "cosp"]))
                                                                                                                         (str "(" (strs 0) " " (__opStr this) ")")
                                                                                                                         :else
                                                                                                                         (str (__opStr this) " " (strs 0))))] curstr))
                          :evaluate        (fn [this m] (apply (__op this) (map #(evaluate % m) (__exps this))))
                          :diff            (fn [this diffStr] ((proto-get this :advancedDiff) (__exps this) (map #(diff % diffStr) (__exps this)) this diffStr))
                          :advancedDiff    nil
                          })


(def ConstPrototype {
                     :val             nil
                     :toString        (fn [this] (__val this))
                     :toStringPostfix (fn [this] (__val this))
                     :toStringInfix   (fn [this] (str (__val this)))
                     :evaluate        (fn [this m] (__val this))
                     :diff            (fn [this diffStr] (Constant 0))
                     })

(defn Constant [val] (assoc {:prototype ConstPrototype} :val val))


(def VariablePrototype {
                        :var             nil
                        :toString        (fn [this] (symbol (__var this)))
                        :toStringPostfix (fn [this] (symbol (__var this)))
                        :toStringInfix   (fn [this] (__var this))
                        :evaluate        (fn [this m] (m (clojure.string/lower-case (str (get (__var this) 0)))))
                        :diff            (fn [this diffStr] (if (= (clojure.string/lower-case (str (get (__var this) 0))) diffStr)
                                                                            (Constant 1)
                                                                            (Constant 0)))
                        })

(defn Variable [var] (assoc {:prototype VariablePrototype} :var var))


(defn _Expression [op opStr diffFunc] (fn [& exps] (assoc {:prototype (assoc ExpressionPrototype
                                                                        :opStr opStr
                                                                        :op op
                                                                        :advancedDiff diffFunc)}
                                                     :exps exps)))

(def Add (_Expression
           +
           "+"
           (fn [exps diffexps this diffStr] (apply Add diffexps))
           ))
(def Subtract (_Expression
                -
                "-"
                (fn [exps diffexps this diffStr] (apply Subtract diffexps))
                ))

(def po (fn [val] (println val) val))

(def Multiply (_Expression
                *
                "*"
                (fn [exps diffexps this diffStr] (Multiply
                                                   (apply Multiply exps)
                                                   (apply Add (mapv (fn [val1 val2] (Divide val1 val2)) diffexps exps))))
                ))
(def Divide (_Expression
              (fn [& args] (if (== (count args) 1)
                             (/ 1.0 (double (first args)))
                             (reduce #(/ (double %1) (double %2)) args)))
              "/"
              (fn [exps diffexps this diffStr] (Multiply
                                                 (apply Divide exps)
                                                 (apply Subtract (mapv (fn [val1 val2] (Divide val1 val2)) diffexps exps))))
              ))
(def Negate (_Expression
              -
              "negate"
              (fn [exps diffexps this diffStr] (apply Negate diffexps))
              ))
(def Power (_Expression
             #(java.lang.Math/pow (abs %1) %2)
             "power"
             nil
             ))

(def ArithMean (_Expression
                 (fn [& exps] (/ (apply + exps) (count exps)))
                 "arithMean"
                 (fn [exps diffexps this diffStr] (Divide
                                                    (apply Add diffexps)
                                                    (Constant (count exps))))
                 ))
(def GeomMean (_Expression
                (fn [& exps] (java.lang.Math/pow (abs (apply * exps)) (/ 1.0 (count exps))))
                "geomMean"
                (fn [exps diffexps this diffStr] (Multiply
                                                   (Constant (/ (count exps)))
                                                   (Power (apply Multiply exps)
                                                          (Constant (/ (count exps))))
                                                   (apply Add (mapv (fn [val1 val2] (Divide val1 val2)) diffexps exps))))
                ))
(def HarmMean (_Expression
                (fn [& exps] (/ (count exps) (double (apply + (mapv #(/ 1.0 (double %)) exps)))))
                "harmMean"
                (fn [exps diffexps this diffStr] (Divide
                                                   (Multiply
                                                     (Constant (count exps))
                                                     (apply Add (mapv (fn [val1 val2] (Divide val1 (Multiply val2 val2))) diffexps exps)))
                                                   (Multiply (apply Add (map Divide exps)) (apply Add (map Divide exps)))))
                ))





(def SinC (_Expression
            #(* (Math/sin %2) (Math/cosh %1))
            "sinc"
            nil
            ))


(def CosC (_Expression
            #(* (Math/cos %2) (Math/cosh %1))
            "cosc"
            nil
            ))

(def Cos (_Expression
           #(Math/cos %)
           "cos"
           nil
           ))

(def Sin (_Expression
           #(Math/sin %)
           "sin"
           nil
           ))

(def SinP (_Expression
            (fn [val] 0.0)
           "sinp"
           nil
           ))

(def CosP (_Expression
            #(Math/cosh %)
            "cosp"
            nil
            ))


(def FuncByName {"+"         Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "const" Constant "var" Variable "power" Power
                 "arithMean" ArithMean "geomMean" GeomMean "harmMean" HarmMean "sinc" SinC "cosc" CosC "sin" Sin "cos" Cos "cosp" CosP "sinp" SinP})

(def parseObject (abstractParseFunction FuncByName, Constant, Variable))




(load-file "4_1_base.clj")
(load-file "4_2_combinators.clj")

(def operationVec ["+" "-" "*" "/" "negate"])

(def *space (+char " \t\n\r"))
(defn *word [curstr] (apply +seqf (fn [& exps] (symbol (apply str exps))) (map #(+char (str %)) curstr)))
(def *ws (+ignore (+star *space)))
(def *digit (+char "0123456789"))
(def *number (+map #(Constant (read-string %)) (+seqf str (+opt (+char "-")) (+str (+plus *digit)) (+opt (+seqf str (+char ".") (+str (+plus *digit)))))))
(def *operation (apply +or (mapv *word operationVec)))
(def *unaryOperation (apply +or (mapv *word ["negate" "sin" "cos"])))
(def *postfixUnaryOperation (apply +or (mapv *word ["sinp" "cosp"])))
(def *variable (+map #(Variable (apply str %)) (+plus (+char-not "*/+-sc()0123456789 \t\n\r\u0000n"))))

(def parseObjectPostfix (letfn [(parser [] (+or
                                             (+seqf (fn [& exps] (nth exps 0)) *ws *number *ws)
                                             (+seqf (fn [& exps] (nth exps 0)) *ws *variable *ws)
                                             (+seqf (fn [& exps] (apply (FuncByName (str (second exps))) (nth exps 0)))
                                                    *ws
                                                    (+ignore (+char "("))
                                                    *ws
                                                    (+star (+seqn 0
                                                                  (delay (parser))
                                                                  *ws))
                                                    *ws
                                                    *operation
                                                    *ws
                                                    (+ignore (+char ")"))
                                                    *ws)))]
                          (_parser (parser))))

(declare *listmultiply *multiply *listsummary *summary *unary)

(def *bracket (+seqn 1
                     *ws
                     (+char "(")
                     *ws
                     (delay *summary)
                     *ws
                     (+char ")")
                     *ws))

(def *expression (+or
                   *number
                   *bracket
                   (delay *unary)
                   *variable
                   ))

(def *unary (+seqf (fn [op exp] ((FuncByName (str op)) exp))
                   *ws
                   *unaryOperation
                   *ws
                   *expression
                   *ws))

(def *listPostfixUnary (+seqf (fn [& exps] (if (= (second exps) nil)
                                             (list (first exps))
                                             (conj (second exps) (first exps))))
                              *ws
                              *postfixUnaryOperation
                              *ws
                              (delay (+opt *listPostfixUnary))))

(def *newexp (+seqf (fn [& exps] (if (= (second exps) nil)
                     (first exps)
                     (reduce #((FuncByName (str %2)) %1) (first exps) (second exps))))
              *ws
              *expression
              *ws
              (+opt *listPostfixUnary)
              *ws))

(defn get*parser [listparser func] (+map (fn [curexps] (let [exps (apply conj (list) curexps)]
                                                         (reduce #(apply (FuncByName (str (first %2))) func(%1 (second %2))) (first exps) (partition 2 (pop exps)))))
                                         listparser))

(def *listsin (+seqf (fn [m rest] (if (= (second rest) nil)
                                    (list m)
                                    (conj (second rest) (first rest) m)))
                     *ws
                     *newexp
                     *ws
                     (+opt
                       (+seq
                         (+or
                           (*word "sinc")
                           (*word "cosc"))
                         *ws
                         (delay *listsin)
                         *ws))))

(def *sin (+map (fn [curexps] (let [exps (apply conj (list) curexps)]
                                (reduce #((FuncByName (str (first %2))) (second %2) %1) (first exps) (partition 2 (pop exps)))))
                *listsin))

(def *listmultiply (+seqf (fn [m rest] (if (= (second rest) nil)
                                         (list m)
                                         (conj (second rest) (first rest) m)))
                          *ws
                          *sin
                          *ws
                          (+opt
                            (+seq
                              (+char "*/")
                              *ws
                              (delay *listmultiply)
                              *ws))))

(def *multiply (+map (fn [exps] (reduce #((FuncByName (str (first %2))) %1 (second %2)) (first exps) (partition 2 (pop exps))))
                     *listmultiply))



(def *listsummary (+seqf (fn [m rest] (if (= (second rest) nil)
                                        (list m)
                                        (conj (second rest) (first rest) m)))
                         *ws
                         *multiply
                         *ws
                         (+opt
                           (+seq
                             (+char "+-")
                             *ws
                             (delay *listsummary)
                             *ws))))

(def *summary (+map (fn [exps] (reduce #((FuncByName (str (first %2))) %1 (second %2)) (first exps) (partition 2 (pop exps))))
                    *listsummary))

(def parseObjectInfix (+parser *summary))

