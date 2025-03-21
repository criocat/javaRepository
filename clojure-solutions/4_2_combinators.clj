
(load-file "4_1_base.clj")
(defn +char [chars]
  (_char (set chars)))
;(tabulate (+char "abc") ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +char-not [chars]
  (_char (comp not (set chars))))
;(tabulate (+char-not "abc") ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +map [f parser]
  (comp (partial _map f) parser))
;(tabulate (+map #(Character/toUpperCase %) (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])

(def +parser _parser)



(def +ignore
  (partial +map (constantly 'ignore)))
;(tabulate (+ignore (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])

(defn- iconj [coll value]
  (if (= value 'ignore)
    coll
    (conj coll value)))
(defn +seq [& parsers]
  (reduce (partial _combine iconj) (_empty []) parsers))
;(tabulate (+seq (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~" "aA" ""])

(defn +seqf [f & parsers]
  (+map (partial apply f) (apply +seq parsers)))
;(tabulate (+seqf str (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

(defn +seqn [n & parsers]
  (apply +seqf #(nth %& n) parsers))
;(tabulate (+seqn 1 (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])



(defn +or [parser & parsers]
  (reduce _either parser parsers))
;(tabulate (+or (+char "a") (+char "b") (+char "c")) ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +opt [parser]
  (+or parser (_empty nil)))
;(tabulate (+opt (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])

(defn +star [parser]
  (letfn [(rec [] (+or (+seqf cons parser (delay (rec))) (_empty ())))]
    (rec)))
;(tabulate (+star (+char "ab")) ["a" "a~" "aa" "aa~" "ab" "ababa~" "" "~"])

(defn +plus [parser]
  (+seqf cons parser (+star parser)))
;(tabulate (+plus (+char "ab")) ["a" "a~" "aa" "aa~" "ab" "ababa~" "" "~"])

(defn +str [parser]
  (+map (partial apply str) parser))
;(tabulate (+str (+star (+char "ab"))) ["a" "a~" "aa" "aa~" "ab" "ababa~" "" "~"])
